package nu.staldal.lagoon.test;

import static org.junit.Assert.*;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import nu.staldal.lagoon.*;
import nu.staldal.lagoon.core.LagoonProcessor;
import nu.staldal.lagoon.core.LagoonException;
import nu.staldal.xtree.TreeBuilder;
import nu.staldal.xtree.input.InputSource;

import org.junit.Test;

public class LagoonCLITest
{
    @Test
    public void testInitialization() throws Exception
    {
        String[] args = new String[]{"propertyFile.xml"};
        LagoonCLI.main(args);
        assertTrue(LagoonCLI.properties != null);
    }

    @Test(expected = LagoonException.class)
    public void testMissingPropertyFile() throws Exception
    {
        String[] args = new String[0];
        LagoonCLI.main(args);
        fail("LagoonException expected but not thrown");
    }

    @Test
    public void testInvalidInterval() throws Exception
    {
        String[] args = new String[]{"propertyFile.xml", "-1"};
        LagoonCLI.main(args);
        fail("LagoonException expected but not thrown");
    }

    @Test
    public void testValidInterval() throws Exception
    {
        String[] args = new String[]{"propertyFile.xml", "5000"};
        LagoonCLI.main(args);
        // In this test we don't check the actual execution of the program,
        // we just check that it doesn't throw an exception and initializes correctly.
    }

    @Test
    public void testValidBuild() throws Exception
    {
        String[] args = new String[]{"propertyFile.xml", "build"};
        LagoonCLI.main(args);
        // In this test we don't check the actual execution of the program,
        // we just check that it doesn't throw an exception and initializes correctly.
    }

    @Test
    public void testValidForce() throws Exception
    {
        String[] args = new String[]{"propertyFile.xml", "force"};
        LagoonCLI.main(args);
        // In this test we don't check the actual execution of the program,
        // we just check that it doesn't throw an exception and initializes correctly.
    }

    @Test(expected = NumberFormatException.class)
    public void testInvalidArgument() throws Exception
    {
        String[] args = new String[]{"propertyFile.xml", "invalidArg"};
        LagoonCLI.main(args);
        fail("NumberFormatException expected but not thrown");
    }

    @Test
    public void testParseXML() throws Exception
    {
        File sitemapFile = new File("sitemap.xml");
        InputSource inputSource = TreeBuilder.fileToInputSource(sitemapFile);
        Element sitemapTree = TreeBuilder.parseXML(inputSource, false);
        assertNotNull(sitemapTree);
    }

    @Test(expected = SAXException.class)
    public void testParseXMLWithException() throws Exception
    {
        File sitemapFile = new File("sitemapWithException.xml");
        InputSource inputSource = TreeBuilder.fileToInputSource(sitemapFile);
        Element sitemapTree = TreeBuilder.parseXML(inputSource, false);
        fail("SAXException expected but not thrown");
    }

    @Test
    public void testGetProperty() throws Exception
    {
        String propertyFile = "propertyFile.xml";
        String propertyName = "targetURL";
        Properties properties = new Properties();
        properties.setProperty(propertyName, "http://localhost");
        LagoonCLI.properties = properties;
        String result = LagoonCLI.getProperty(propertyName);
        assertEquals("http://localhost", result);
    }

    @Test(expected = LagoonException.class)
    public void testMissingProperty() throws Exception
    {
        String propertyName = "missingProperty";
        LagoonCLI.getProperty(propertyName);
        fail("LagoonException expected but not thrown");
    }
}