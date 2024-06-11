package nu.staldal.lagoon;

import static org.junit.Assert.*;
import org.xml.sax.SAXException;
import static org.mockito.Mockito.*;

import java.io.*;
import java.util.Properties;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.xml.sax.helpers.DefaultHandler;

import nu.staldal.lagoon.LagoonCLI;
import nu.staldal.lagoon.LagoonException;
import nu.staldal.lagoon.LagoonProcessor;
import nu.staldal.xtree.TreeBuilder;

public class LagoonCLITest {

    private LagoonProcessor mockProcessor;
    private DefaultHandler mockXMLHandler;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockProcessor = mock(LagoonProcessor.class);
        mockXMLHandler = mock(DefaultHandler.class);
    }

    @Test
    public void testInitializationWithPropertyFile() throws Exception {
        final String propertyFile = "testPropertyFile.xml";
        final Properties properties = new Properties();
        properties.setProperty("targetURL", "http://example.com");
        properties.setProperty("sitemapFile", "testSitemap.xml");
        properties.setProperty("password", "testPassword");

        final File propertyFileFile = new File(propertyFile);
        when(mockXMLHandler.getInputSource()).thenReturn(new FileInputSource(propertyFileFile));

        LagoonCLI.properties = properties;

        LagoonCLI.main(new String[]{"propertyFile.xml"});

        verify(mockProcessor, times(1)).__Constructor__(eq("http://example.com"), eq(mockXMLHandler), eq(new File("testSitemap.xml")), eq("testPassword"), eq(new PrintWriter(System.out, true)), eq(new PrintWriter(System.err, true)));
        verifyNoMoreInteractions(mockProcessor);
    }

    @Test(expected = LagoonException.class)
    public void testInvalidInitializationWithPropertyFile() throws Exception {
        final String propertyFile = "testPropertyFile.xml";
        final Properties properties = new Properties();
        properties.setProperty("targetURL", "http://example.com");
        properties.setProperty("password", "testPassword");

        final File propertyFileFile = new File(propertyFile);
        when(mockXMLHandler.getInputSource()).thenReturn(new FileInputSource(propertyFileFile));

        LagoonCLI.properties = properties;

        LagoonCLI.main(new String[]{"propertyFile.xml", "invalid"});

        verify(mockProcessor, never()).__Constructor__(any(), any(), any(), any(), any(), any());
        throw new AssertionError();
    }

    @Test(expected = LagoonException.class)
    public void testInvalidInitializationWithSitemapFile() throws Exception {
        final String propertyFile = "testPropertyFile.xml";
        final String sitemapFile = "testSitemapFile.txt";

        final Properties properties = new Properties();
        properties.setProperty("targetURL", "http://example.com");
        properties.setProperty("sitemapFile", sitemapFile);
        properties.setProperty("password", "testPassword");

        when(mockXMLHandler.getInputSource()).thenThrow(new SAXException("Test SAX Exception"));

        LagoonCLI.properties = properties;

        LagoonCLI.main(new String[]{"propertyFile.xml", sitemapFile});

        verify(mockProcessor, never()).__Constructor__(any(), any(), any(), any(), any(), any());
        throw new AssertionError();
    }

    @Test(expected = LagoonException.class)
    public void testAuthenticationMissingException() throws Exception {
        final String propertyFile = "testPropertyFile.xml";
        final String sitemapFile = "testSitemap.xml";

        final Properties properties = new Properties();
        properties.setProperty("targetURL", "http://example.com");
        properties.setProperty("sitemapFile", sitemapFile);

        when(mockXMLHandler.getInputSource()).thenReturn(new FileInputSource(new File(sitemapFile)));

        LagoonCLI.properties = properties;

        doThrow(new AuthenticationMissingException("Test Authentication Missing Exception")).when(mockProcessor).__Constructor__(any(), any(), any(), any(), any(), any());

        LagoonCLI.main(new String[]{"propertyFile.xml", sitemapFile});

        verify(mockProcessor, times(1)).__Constructor__(eq("http://example.com"), eq(mockXMLHandler), eq(new File(sitemapFile)), eq(null), eq(new PrintWriter(System.out, true)), eq(new PrintWriter(System.err, true)));
        verify(mockProcessor).throwException(eq(new AuthenticationMissingException("Test Authentication Missing Exception")));
    }

    @Test(expected = LagoonException.class)
    public void testAuthenticationException() throws Exception {
        final String propertyFile = "testPropertyFile.xml";
        final String sitemapFile = "testSitemap.xml";

        final Properties properties = new Properties();
        properties.setProperty("targetURL", "http://example.com");
        properties.setProperty("sitemapFile", sitemapFile);
        properties.setProperty("password", "testPasswordIncorrect");

        when(mockXMLHandler.getInputSource()).thenReturn(new FileInputSource(new File(sitemapFile)));

        LagoonCLI.properties = properties;

        doThrow(new AuthenticationException("Test Authentication Exception")).when(mockProcessor).__Constructor__(any(), any(), any(), any(), any(), any());

        LagoonCLI.main(new String[]{"propertyFile.xml", sitemapFile});

        verify(mockProcessor, times(1)).__Constructor__(eq("http://example.com"), eq(mockXMLHandler), eq(new File(sitemapFile)), eq(null), eq(new PrintWriter(System.out, true)), eq(new PrintWriter(System.err, true)));
        verify(mockProcessor).throwException(eq(new AuthenticationException("Test Authentication Exception")));
    }

    @Test
    public void testBuild() throws Exception {
        final String propertyFile = "testPropertyFile.xml";
        final String sitemapFile = "testSitemap.xml";

        final Properties properties = new Properties();
        properties.setProperty("targetURL", "http://example.com");
        properties.setProperty("sitemapFile", sitemapFile);
        properties.setProperty("password", "testPassword");

        final File propertyFileFile = new File(propertyFile);
        when(mockXMLHandler.getInputSource()).thenReturn(new FileInputSource(propertyFileFile));

        LagoonCLI.properties = properties;

        when(mockProcessor.build(anyBoolean())).thenReturn(true);

        LagoonCLI.main(new String[]{"propertyFile.xml"});

        verify(mockProcessor, times(1)).build(eq(false));
        verifyNoMoreInteractions(mockProcessor);
    }

    @Test
    public void testForceBuild() throws Exception {
        final String propertyFile = "testPropertyFile.xml";
        final String sitemapFile = "testSitemap.xml";

        final Properties properties = new Properties();
        properties.setProperty("targetURL", "http://example.com");
        properties.setProperty("sitemapFile", sitemapFile);
        properties.setProperty("password", "testPassword");

        final File propertyFileFile = new File(propertyFile);
        when(mockXMLHandler.getInputSource()).thenReturn(new FileInputSource(propertyFileFile));

        LagoonCLI.properties = properties;

        when(mockProcessor.build(anyBoolean())).thenReturn(true);

        LagoonCLI.main(new String[]{"propertyFile.xml", "force"});

        verify(mockProcessor, times(1)).build(eq(true));
        verifyNoMoreInteractions(mockProcessor);
    }

    @Test
    public void testInterval() throws Exception {
        final String propertyFile = "testPropertyFile.xml";
        final String sitemapFile = "testSitemap.xml";
        final long interval = 12345;

        final Properties properties = new Properties();
        properties.setProperty("targetURL", "http://example.com");
        properties.setProperty("sitemapFile", sitemapFile);
        properties.setProperty("password", "testPassword");

        final File propertyFileFile = new File(propertyFile);
        when(mockXMLHandler.getInputSource()).thenReturn(new FileInputSource(propertyFileFile));

        LagoonCLI.properties = properties;

        when(mockProcessor.build(anyBoolean())).thenReturn(true);

        LagoonCLI.main(new String[]{"propertyFile.xml", "12345"});

        verify(mockProcessor, times(1)).build(eq(false));
        verify(mockProcessor).sleep(eq(interval));
        verifyNoMoreInteractions(mockProcessor);
    }
}