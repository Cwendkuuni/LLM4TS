package nu.staldal.lagoon.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.xml.sax.SAXException;

import nu.staldal.lagoon.LagoonCLI;
import nu.staldal.lagoon.LagoonException;
import nu.staldal.lagoon.core.AuthenticationException;
import nu.staldal.lagoon.core.AuthenticationMissingException;
import nu.staldal.lagoon.core.LagoonProcessor;
import nu.staldal.lagoon.core.PrintWriter;
import nu.staldal.lagoon.tree.TreeBuilder;
import nu.staldal.xtree.InputSource;

public class LagoonCLITest {

    @org.junit.Test
    public void testMainWithPropertyFileAndNoOptions() throws Exception {
        String[] args = new String[]{"property_file.xml"};
        LagoonCLI.main(args);
    }

    @org.junit.Test(expected = LagoonException.class)
    public void testMainWithInvalidSyntax() throws Exception {
        String[] args = new String[]{"property_file.xml", "invalid_option"};
        LagoonCLI.main(args);
    }

    @org.junit.Test(expected = NumberFormatException.class)
    public void testMainWithInvalidInterval() throws Exception {
        String[] args = new String[]{"property_file.xml", "-1"};
        LagoonCLI.main(args);
    }

    @org.junit.Test(expected = LagoonException.class)
    public void testMainWithMissingPropertyFile() throws Exception {
        String[] args = new String[]{"missing_file.xml"};
        LagoonCLI.main(args);
    }

    @org.junit.Test
    public void testMainWithValidPropertyFileAndNoOptions() throws Exception {
        String[] args = new String[]{"property_file.xml"};
        LagoonCLI.main(args);
    }

    @org.junit.Test
    public void testMainWithValidPropertyFileAndBuildOption() throws Exception {
        String[] args = new String[]{"property_file.xml", "build"};
        LagoonCLI.main(args);
    }

    @org.junit.Test
    public void testMainWithValidPropertyFileAndForceOption() throws Exception {
        String[] args = new String[]{"property_file.xml", "force"};
        LagoonCLI.main(args);
    }

    @org.junit.Test
    public void testMainWithValidPropertyFileAndIntervalOption() throws Exception {
        String[] args = new String[]{"property_file.xml", "5000"};
        LagoonCLI.main(args);
    }

    @org.junit.Test(expected = LagoonException.class)
    public void testMainWithInvalidPropertyFile() throws Exception {
        String[] args = new String[]{"invalid_property_file.xml"};
        LagoonCLI.main(args);
    }

    @org.junit.Test(expected = LagoonException.class)
    public void testMainWithMissingPasswordInPropertyFile() throws Exception {
        String[] args = new String[]{"property_file_without_password.xml"};
        LagoonCLI.main(args);
    }

    @org.junit.Test(expected = AuthenticationException.class)
    public void testMainWithInvalidPasswordInPropertyFile() throws Exception {
        String[] args = new String[]{"property_file_with_invalid_password.xml"};
        LagoonCLI.main(args);
    }

    @org.junit.Test(expected = FileNotFoundException.class)
    public void testMainWithMissingSitemapFile() throws Exception {
        String[] args = new String[]{"property_file.xml"};
        LagoonCLI.main(args);
    }

    @org.junit.Test(expected = IOException.class)
    public void testMainWithInvalidSitemapFile() throws Exception {
        String[] args = new String[]{"property_file.xml", "invalid_sitemap_file.xml"};
        LagoonCLI.main(args);
    }

    @org.junit.Test
    public void testGetProperty() throws Exception {
        Properties properties = new Properties();
        properties.setProperty("targetURL", "http://www.example.com");
        String result = LagoonCLI.getProperty("targetURL", properties);
        assertEquals("http://www.example.com", result);
    }

    @org.junit.Test(expected = LagoonException.class)
    public void testGetPropertyWithMissingProperty() throws Exception {
        Properties properties = new Properties();
        String result = LagoonCLI.getProperty("missing_property", properties);
        fail("Property not specified should have thrown an exception");
    }

    @org.junit.Test
    public void testShowTime() throws Exception {
        long time = 5000L;
        LagoonCLI.showTime(time);
        String expectedOutput = "in 5 s";
        String actualOutput = System.out.getBuffer().toString();
        assertEquals(expectedOutput, actualOutput);
    }

    @org.junit.Test
    public void testShowTimeWithShortTime() throws Exception {
        long time = 120L;
        LagoonCLI.showTime(time);
        String expectedOutput = "in 120 ms";
        String actualOutput = System.out.getBuffer().toString();
        assertEquals(expectedOutput, actualOutput);
    }

    @org.junit.Test
    public void testParseSitemapFile() throws Exception {
        Properties properties = new Properties();
        properties.setProperty("sitemapFile", "sitemap.xml");
        File sitemapFile = new File("sitemap.xml");
        InputSource inputSource = TreeBuilder.fileToInputSource(sitemapFile);
        Element sitemapTree = LagoonCLI.TreeBuilder.parseXML(inputSource, false);
        assertNotNull(sitemapTree);
    }

    @org.junit.Test(expected = SAXException.class)
    public void testParseSitemapFileWithException() throws Exception {
        Properties properties = new Properties();
        properties.setProperty("sitemapFile", "exception_sitemap.xml");
        File sitemapFile = new File("exception_sitemap.xml");
        InputSource inputSource = TreeBuilder.fileToInputSource(sitemapFile);
        LagoonCLI.TreeBuilder.parseXML(inputSource, false);
        fail("Exception should have been thrown");
    }

    @org.junit.Test
    public void testDestroyProcessor() throws Exception {
        LagoonCLI.LagoonProcessor processor = new LagoonCLI.LagoonProcessor("http://www.example.com", null, null, null, null, null);
        processor.destroy();
    }
}