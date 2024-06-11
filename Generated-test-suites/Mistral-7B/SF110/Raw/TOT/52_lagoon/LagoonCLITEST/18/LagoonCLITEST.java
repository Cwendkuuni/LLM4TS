package nu.staldal.lagoon;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockitoAnnotations;

import nu.staldal.lagoon.core.AuthenticationMissingException;
import nu.staldal.lagoon.core.AuthenticationException;
import nu.staldal.lagoon.core.LagoonException;
import nu.staldal.lagoon.core.LagoonProcessor;
import nu.staldal.lagoon.core.PropertyReader;
import nu.staldal.lagoon.sax.TreeBuilder;
import nu.staldal.xtree.Element;

public class LagoonCLITest {

    private static final String SYNTAX_ERR_MSG = "Syntax: nu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]";
    private static final String PROPERTY_FILE_INVALID_PATH = "invalidPropertyFile.txt";
    private static final String INVALID_INTERVAL = "invalid_interval";
    private static final String PASSWORD = "password";
    private static final String SITEMAP_XML = "sitemap.xml";
    private static final String PROPERTY_FILE = "property.txt";

    private LagoonCLI cli;
    private LagoonProcessor processorMock;
    private PropertyReader propertyReaderMock;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        processorMock = mock(LagoonProcessor.class);
        propertyReaderMock = mock(PropertyReader.class);
        cli = new LagoonCLI();
        cli.properties = new Properties();
        cli.properties.setProperty("reader", propertyReaderMock.getClass().getName());
    }

    @Test
    public void testInvalidSyntax() {
        when(propertyReaderMock.load(any(FileInputStream.class))).thenThrow(new IOException("Invalid file"));
        cli.main(new String[] { PROPERTY_FILE });
        assertEquals(SYNTAX_ERR_MSG, System.out.toString());
        verify(systemOut, times(1)).println(SYNTAX_ERR_MSG);
    }

    @Test
    public void testPropertyFileNotFound() {
        when(propertyReaderMock.load(any(FileInputStream.class))).thenThrow(new FileNotFoundException(PROPERTY_FILE_INVALID_PATH));
        cli.main(new String[] { PROPERTY_FILE });
        assertEquals(String.format("Error while initializing Lagoon:\nFile not found: %s", PROPERTY_FILE_INVALID_PATH), System.err.toString());
        verify(systemErr, times(1)).println(String.format("Error while initializing Lagoon:\nFile not found: %s", PROPERTY_FILE_INVALID_PATH));
    }

    @Test
    public void testInvalidInterval() {
        cli.main(new String[] { PROPERTY_FILE, INVALID_INTERVAL });
        assertEquals(SYNTAX_ERR_MSG, System.out.toString());
        verify(systemOut, times(1)).println(SYNTAX_ERR_MSG);
    }

    @Test
    public void testPasswordMissing() throws AuthenticationMissingException {
        when(propertyReaderMock.getProperty("password")).thenReturn(null);
        when(propertyReaderMock.load(any(FileInputStream.class))).thenReturn(new Properties());

        try {
            cli.main(new String[] { PROPERTY_FILE, "build" });
            fail();
        } catch (Exception e) {
            assertEquals("Error while initializing Lagoon:\nPassword is required but not specified", e.getMessage());
        }

        verify(systemErr, times(1)).println("Error while initializing Lagoon:\nPassword is required but not specified");
    }

    @Test
    public void testInvalidPassword() throws AuthenticationException {
        when(propertyReaderMock.getProperty("password")).thenReturn("bad_password");
        when(propertyReaderMock.load(any(FileInputStream.class))).thenReturn(new Properties());

        try {
            cli.main(new String[] { PROPERTY_FILE, "build" });
            fail();
        } catch (Exception e) {
            assertEquals("Error while initializing Lagoon:\nIncorrect password", e.getMessage());
        }

        verify(systemErr, times(1)).println("Error while initializing Lagoon:\nIncorrect password");
    }

    @Test
    public void testSitemapFileNotFound() throws IOException {
        when(propertyReaderMock.getProperty("sitemapFile")).thenReturn(SITEMAP_XML);
        when(propertyReaderMock.load(any(FileInputStream.class))).thenThrow(new FileNotFoundException(SITEMAP_XML));

        try {
            cli.main(new String[] { PROPERTY_FILE, "build" });
            fail();
        } catch (Exception e) {
            assertEquals(String.format("Error while initializing Lagoon:\nFile not found: %s", SITEMAP_XML), e.getMessage());
        }

        verify(systemErr, times(1)).println(String.format("Error while initializing Lagoon:\nFile not found: %s", SITEMAP_XML));
    }

    @Test
    public void testBuildSuccess() {
        when(propertyReaderMock.getProperty("targetURL")).thenReturn("http://example.com");
        when(propertyReaderMock.getProperty("sourceDir")).thenReturn("/path/to/source");
        when(propertyReaderMock.getProperty("password")).thenReturn(PASSWORD);
        when(propertyReaderMock.load(any(FileInputStream.class))).thenReturn(new Properties());

        ArgumentCaptor<File> sitemapFileCaptor = ArgumentCaptor.forClass(File.class);
        when(propertyReaderMock.getFile(eq("sitemapFile"))).thenReturn(new File(SITEMAP_XML));

        when(propertyReaderMock.getFile(eq("property_file"))).thenReturn(new File(PROPERTY_FILE));

        File sitemapTreeFile = new File("sitemapTree.xml");
        when(TreeBuilder.parseXML(any(InputSource.class), anyBoolean())).thenReturn(mock(Element.class));

        cli.main(new String[] { PROPERTY_FILE, "build" });

        verify(processorMock, times(1)).build(false);
        verify(systemOut, times(1)).println("Building website...");
    }

    @Test
    public void testForceBuildSuccess() {
        when(propertyReaderMock.getProperty("targetURL")).thenReturn("http://example.com");
        when(propertyReaderMock.getProperty("sourceDir")).thenReturn("/path/to/source");
        when(propertyReaderMock.getProperty("password")).thenReturn(PASSWORD);
        when(propertyReaderMock.load(any(FileInputStream.class))).thenReturn(new Properties());

        ArgumentCaptor<File> sitemapFileCaptor = ArgumentCaptor.forClass(File.class);
        when(propertyReaderMock.getFile(eq("sitemapFile"))).thenReturn(new File(SITEMAP_XML));

        when(propertyReaderMock.getFile(eq("property_file"))).thenReturn(new File(PROPERTY_FILE));

        File sitemapTreeFile = new File("sitemapTree.xml");
        when(TreeBuilder.parseXML(any(InputSource.class), anyBoolean())).thenReturn(mock(Element.class));

        cli.main(new String[] { PROPERTY_FILE, "force" });

        verify(processorMock, times(1)).build(true);
        verify(systemOut, times(1)).println("Force building website...");
    }

    @Test
    public void testInterval() {
        when(propertyReaderMock.getProperty("targetURL")).thenReturn("http://example.com");
        when(propertyReaderMock.getProperty("sourceDir")).thenReturn("/path/to/source");
        when(propertyReaderMock.getProperty("password")).thenReturn(PASSWORD);
        when(propertyReaderMock.load(any(FileInputStream.class))).thenReturn(new Properties());

        ArgumentCaptor<File> sitemapFileCaptor = ArgumentCaptor.forClass(File.class);
        when(propertyReaderMock.getFile(eq("sitemapFile"))).thenReturn(new File(SITEMAP_XML));

        when(propertyReaderMock.getFile(eq("property_file"))).thenReturn(new File(PROPERTY_FILE));

        File sitemapTreeFile = new File("sitemapTree.xml");
        when(TreeBuilder.parseXML(any(InputSource.class), anyBoolean())).thenReturn(mock(Element.class));

        cli.main(new String[] { PROPERTY_FILE, "5000" });

        ArgumentCaptor<Long> intervalCaptor = ArgumentCaptor.forClass(Long.class);
        verify(processorMock, times(1)).build(intervalCaptor.capture(interval));
        assertTrue(interval.getValue() >= 5000L);
    }
}