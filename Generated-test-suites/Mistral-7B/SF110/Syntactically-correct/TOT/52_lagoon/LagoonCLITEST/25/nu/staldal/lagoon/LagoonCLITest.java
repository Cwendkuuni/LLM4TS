package nu.staldal.lagoon;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import nu.staldal.lagoon.core.AuthenticationException;
import nu.staldal.lagoon.core.AuthenticationMissingException;
import nu.staldal.lagoon.core.LagoonException;
import nu.staldal.lagoon.core.LagoonProcessor;
import nu.staldal.lagoon.core.PrintWriter;
import nu.staldal.xtree.TreeBuilder;

public class LagoonCLITest {

    private static final String TEST_SYNTAX_XML = "test.xml";
    private static final String TEST_SYNTAX_PROPERTIES = "test.properties";
    private static final String TEST_SITEMAP_XML = "test.sitemap.xml";
    private static final String TEST_TARGET_URL = "http://test.com";
    private static final String TEST_SOURCE_DIR = "/test/source";
    private static final String TEST_PASSWORD = "testPassword";
    private static final long TEST_INTERVAL = 1000L;

    private Properties testProperties;
    private File testPropertyFile;
    private File testSitemapFile;
    private LagoonProcessor testProcessor;
    private PrintWriter testOut;
    private PrintWriter testErr;

    @Before
    public void setup() {
        testProperties = new Properties();
        testPropertyFile = new File(TEST_SYNTAX_PROPERTIES);
        testSitemapFile = new File(TEST_SYNTAX_XML);
        testProcessor = mock(LagoonProcessor.class);
        testOut = mock(PrintWriter.class);
        testErr = mock(PrintWriter.class);
    }

    @Test
    public void testInvalidSyntax() {
        when(testProcessor.build(anyBoolean())).thenThrow(new LagoonException("Test Error"));

        try {
            LagoonCLI.main(new String[]{"", "build"});
            fail("Expected LagoonCLI to throw exception with invalid syntax argument");
        } catch (LagoonException e) {
            assertEquals("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]", e.getMessage());
        }
    }

    @Test
    public void testInvalidPropertyFile() {
        when(testProcessor.build(anyBoolean())).thenReturn(true);

        try {
            testProperties.load(new FileInputStream(testPropertyFile));
            fail("Expected FileNotFoundException when testing invalid property file");
        } catch (FileNotFoundException e) {
            // Expected
        }

        LagoonCLI.main(new String[]{TEST_SYNTAX_PROPERTIES, "build"});
    }

    @Test
    public void testValidPropertyFile() {
        when(testProcessor.build(anyBoolean())).thenReturn(true);
        testProperties.setProperty("targetURL", TEST_TARGET_URL);
        testProperties.setProperty("sitemapFile", TEST_SITEMAP_XML);
        testProperties.setProperty("sourceDir", TEST_SOURCE_DIR);
        testProperties.setProperty("password", TEST_PASSWORD);

        when(TreeBuilder.parseXML(any(InputSource.class), anyBoolean())).thenReturn(mock(Element.class));

        LagoonCLI.main(new String[]{TEST_SYNTAX_PROPERTIES, "build"});

        verify(testProcessor, times(1)).build(false);
    }

    @Test(expected = AuthenticationMissingException.class)
    public void testMissingPassword() {
        testProperties.setProperty("targetURL", TEST_TARGET_URL);
        testProperties.setProperty("sitemapFile", TEST_SITEMAP_XML);
        testProperties.setProperty("sourceDir", TEST_SOURCE_DIR);

        when(TreeBuilder.parseXML(any(InputSource.class), anyBoolean())).thenReturn(mock(Element.class));

        LagoonCLI.main(new String[]{TEST_SYNTAX_XML, "build"});
    }

    @Test(expected = AuthenticationException.class)
    public void testInvalidPassword() {
        testProperties.setProperty("targetURL", TEST_TARGET_URL);
        testProperties.setProperty("sitemapFile", TEST_SITEMAP_XML);
        testProperties.setProperty("sourceDir", TEST_SOURCE_DIR);
        testProperties.setProperty("password", "wrongPassword");

        when(TreeBuilder.parseXML(any(InputSource.class), anyBoolean())).thenReturn(mock(Element.class));

        LagoonCLI.main(new String[]{TEST_SYNTAX_XML, "build"});
    }

    @Test
    public void testBuild() throws IOException {
        when(testProcessor.build(anyBoolean())).thenReturn(true);
        testProperties.setProperty("targetURL", TEST_TARGET_URL);
        testProperties.setProperty("sitemapFile", TEST_SITEMAP_XML);
        testProperties.setProperty("sourceDir", TEST_SOURCE_DIR);
        testProperties.setProperty("password", TEST_PASSWORD);

        when(TreeBuilder.parseXML(any(InputSource.class), anyBoolean())).thenReturn(mock(Element.class));

        LagoonCLI.main(new String[]{TEST_SYNTAX_PROPERTIES, "build"});

        verify(testProcessor, times(1)).build(false);
    }

    @Test
    public void testBuildWithForce() throws IOException {
        when(testProcessor.build(anyBoolean())).thenReturn(true);
        testProperties.setProperty("targetURL", TEST_TARGET_URL);
        testProperties.setProperty("sitemapFile", TEST_SITEMAP_XML);
        testProperties.setProperty("sourceDir", TEST_SOURCE_DIR);
        testProperties.setProperty("password", TEST_PASSWORD);

        when(TreeBuilder.parseXML(any(InputSource.class), anyBoolean())).thenReturn(mock(Element.class));

        LagoonCLI.main(new String[]{TEST_SYNTAX_PROPERTIES, "force"});

        verify(testProcessor, times(1)).build(true);
    }

    @Test
    public void testBuildWithInterval() throws InterruptedException {
        when(testProcessor.build(anyBoolean())).thenReturn(true);
        testProperties.setProperty("targetURL", TEST_TARGET_URL);
        testProperties.setProperty("sitemapFile", TEST_SITEMAP_XML);
        testProperties.setProperty("sourceDir", TEST_SOURCE_DIR);
        testProperties.setProperty("password", TEST_PASSWORD);

        when(TreeBuilder.parseXML(any(InputSource.class), anyBoolean())).thenReturn(mock(Element.class));

        LagoonCLI.main(new String[]{TEST_SYNTAX_PROPERTIES, "1000"});

        Thread.sleep(TEST_INTERVAL);

        verify(testProcessor, times(2)).build(false);
    }

    @Test
    public void testBuildWithInvalidInterval() {
        testProperties.setProperty("targetURL", TEST_TARGET_URL);
        testProperties.setProperty("sitemapFile", TEST_SITEMAP_XML);
        testProperties.setProperty("sourceDir", TEST_SOURCE_DIR);
        testProperties.setProperty("password", TEST_PASSWORD);

        when(TreeBuilder.parseXML(any(InputSource.class), anyBoolean())).thenReturn(mock(Element.class));

        LagoonCLI.main(new String[]{TEST_SYNTAX_PROPERTIES, "-1000"});
    }
}