package nu.staldal.lagoon.test;

import static org.junit.Assert.*;
import nu.staldal.lagoon.*;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.junit.Test;

public class LagoonCLITest {

    @Test
    public void testMainWithInvalidArgs() {
        String[] args = new String[0];
        LagoonCLI.main(args);
        assertTrue(System.out.toString().contains("Syntax:"));
    }

    @Test(expected = LagoonException.class)
    public void testMainWithMissingPropertyFile() {
        String[] args = new String[]{"invalid_file_path"};
        LagoonCLI.main(args);
    }

    @Test(expected = LagoonException.class)
    public void testMainWithInvalidPropertyFile() {
        String[] args = new String[]{"invalid_file_path.txt"};
        LagoonCLI.main(args);
    }

    @Test
    public void testMainWithValidPropertyFile() {
        String[] args = new String[]{"valid_property_file.xml"};
        LagoonCLI.main(args);
        assertTrue(System.out.toString().contains("Lagoon initialized successfully"));
    }

    @Test(expected = NumberFormatException.class)
    public void testMainWithInvalidInterval() {
        String[] args = new String[]{"property_file.xml", "-1"};
        LagoonCLI.main(args);
    }

    @Test
    public void testMainWithBuildArgument() {
        String[] args = new String[]{"property_file.xml", "build"};
        LagoonCLI.main(args);
        assertTrue(System.out.toString().contains("Building website..."));
    }

    @Test
    public void testMainWithForceArgument() {
        String[] args = new String[]{"property_file.xml", "force"};
        LagoonCLI.main(args);
        assertTrue(System.out.toString().contains("Force building website..."));
    }

    @Test
    public void testMainWithIntervalArgument() {
        String[] args = new String[]{"property_file.xml", "1500"};
        LagoonCLI.main(args);
        assertTrue(System.out.toString().contains("Building website..."));
        assertTrue(System.out.toString().contains("in 1.5 s"));
    }

    @Test(expected = LagoonException.class)
    public void testMainWithMissingPasswordProperty() {
        String[] args = new String[]{"property_file.xml"};
        LagoonCLI.main(args);
    }

    @Test(expected = AuthenticationException.class)
    public void testMainWithInvalidPasswordProperty() {
        Properties properties = new Properties();
        properties.setProperty("password", "invalid_password");
        FileInputStream fis = new FileInputStream(new File("property_file.xml"));
        properties.load(fis);
        fis.close();
        String[] args = new String[]{"property_file.xml"};
        LagoonCLI.main(args);
    }

    @Test
    public void testMainWithValidPasswordProperty() {
        Properties properties = new Properties();
        properties.setProperty("password", "valid_password");
        FileInputStream fis = new FileInputStream(new File("property_file.xml"));
        properties.load(fis);
        fis.close();
        String[] args = new String[]{"property_file.xml"};
        LagoonCLI.main(args);
    }

    @Test(expected = FileNotFoundException.class)
    public void testMainWithMissingSitemapFile() {
        String[] args = new String[]{"property_file.xml", "build"};
        LagoonCLI.main(args);
    }

    @Test(expected = IOException.class)
    public void testMainWithInvalidSitemapFile() {
        String[] args = new String[]{"property_file.xml", "build"};
        LagoonCLI.main(args);
        File sitemapFile = new File("sitemap_file.xml");
        sitemapFile.setWriteable(true);
        FileInputStream fis = new FileInputStream(sitemapFile);
        SAXParseException saxpe = null;
        try {
            sitemapFile.setReadable(true);
            TreeBuilder.parseXML(TreeBuilder.fileToInputSource(sitemapFile), false);
        } catch (SAXException e) {
            saxpe = (SAXParseException) e;
        } finally {
            fis.close();
            sitemapFile.setWriteable(false);
        }
        String[] args2 = new String[]{"property_file.xml", "build"};
        LagoonCLI.main(args2);
        assertEquals(saxpe.getLineNumber(), LagoonCLI.getLastExceptionLineNumber());
        assertEquals(saxpe.getMessage(), LagoonCLI.getLastExceptionMessage());
    }

    @Test
    public void testMainWithValidSitemapFile() {
        String[] args = new String[]{"property_file.xml", "build"};
        LagoonCLI.main(args);
        assertTrue(System.out.toString().contains("Building website..."));
    }

    @Test
    public void testGetProperty() {
        Properties properties = new Properties();
        properties.setProperty("targetURL", "http://www.example.com");
        String targetURL = LagoonCLI.getProperty("targetURL");
        assertEquals("http://www.example.com", targetURL);
    }

    @Test(expected = LagoonException.class)
    public void testGetPropertyWithMissingProperty() {
        Properties properties = new Properties();
        String targetURL = LagoonCLI.getProperty("missing_property");
    }

    @Test
    public void testShowTime() {
        long ms = 1000;
        LagoonCLI.showTime(ms);
        assertTrue(System.out.toString().contains("in " + ms + " ms"));
        ms = 1500;
        LagoonCLI.showTime(ms);
        assertTrue(System.out.toString().contains("in 1.5 s"));
    }

}