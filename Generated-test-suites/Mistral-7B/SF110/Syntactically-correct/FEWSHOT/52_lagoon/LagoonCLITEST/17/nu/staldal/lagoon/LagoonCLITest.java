package nu.staldal.lagoon;

import static org.junit.Assert.*;
import org.junit.Test;
import org.xml.sax.SAXException;

import nu.staldal.lagoon.core.LagoonException;
import nu.staldal.lagoon.core.LagoonProcessor;
import nu.staldal.xtree.TreeBuilder;

public class LagoonCLITest {

    @Test
    public void testGetProperty() throws LagoonException {
        Properties properties = new Properties();
        properties.setProperty("targetURL", "http://test.com");
        String result = LagoonCLI.getProperty("targetURL");
        assertEquals("http://test.com", result);
    }

    @Test(expected = LagoonException.class)
    public void testGetPropertyMissingProperty() throws LagoonException {
        Properties properties = new Properties();
        String result = LagoonCLI.getProperty("missingProperty");
        fail("Property missingProperty should not be found");
    }

    @Test
    public void testInitializeLagoonWithPropertyFile() throws Exception {
        String propertyFile = "test_property_file.xml";
        File file = new File(propertyFile);
        FileInputStream fis = new FileInputStream(file);
        Properties properties = new Properties();
        properties.load(fis);
        fis.close();

        String targetURL = LagoonCLI.getProperty("targetURL");
        File sitemapFile = new File(getProperty("sitemapFile"));
        File sourceDir = new File(getProperty("sourceDir"));

        assertEquals("http://test.com", targetURL);
        assertEquals("test_sitemap_file.xml", sitemapFile.getName());
        assertEquals("test_source_dir", sourceDir.getName());
    }

    @Test
    public void testInitializeLagoonWithXMLFile() throws Exception {
        String sitemapFile = "test_sitemap.xml";
        File file = new File(sitemapFile);
        Element sitemapTree = TreeBuilder.parseXML(TreeBuilder.fileToInputSource(file), false);

        String targetURL = LagoonCLI.getProperty("targetURL");
        File sitemapFileTest = new File(getProperty("sitemapFile"));
        File sourceDir = new File(getProperty("sourceDir"));

        assertEquals("http://test.com", targetURL);
        assertEquals(sitemapFile, sitemapFileTest);
        assertNotNull(sourceDir);
    }

    @Test(expected = AuthenticationException.class)
    public void testInitializeLagoonWithInvalidPassword() throws Exception {
        String propertyFile = "test_property_file.xml";
        File file = new File(propertyFile);
        Properties properties = new Properties();
        properties.load(new FileInputStream(file));
        String password = "wrong_password";

        LagoonCLI.initializeLagoon("", properties, password);
    }

    @Test(expected = AuthenticationMissingException.class)
    public void testInitializeLagoonWithoutPassword() throws Exception {
        String propertyFile = "test_property_file.xml";
        File file = new File(propertyFile);
        Properties properties = new Properties();
        properties.load(new FileInputStream(file));

        LagoonCLI.initializeLagoon("", properties, null);
    }

    @Test(expected = FileNotFoundException.class)
    public void testInitializeLagoonWithMissingPropertyFile() throws Exception {
        String propertyFile = "missing_property_file.xml";
        LagoonCLI.initializeLagoon(propertyFile, null, null);
    }

    @Test(expected = FileNotFoundException.class)
    public void testInitializeLagoonWithMissingSitemapFile() throws Exception {
        String propertyFile = "test_property_file.xml";
        Properties properties = new Properties();
        properties.load(new FileInputStream(new File(propertyFile)));

        LagoonCLI.initializeLagoon(propertyFile, properties, null);
    }

    @Test(expected = IOException.class)
    public void testInitializeLagoonWithInvalidXMLFile() throws Exception {
        String sitemapFile = "test_invalid_xml.xml";
        File file = new File(sitemapFile);
        InputSource inputSource = new InputSource(new FileInputStream(file));

        LagoonCLI.initializeLagoon("", null, null);
    }

    @Test
    public void testBuildWebsite() throws Exception {
        String propertyFile = "test_property_file.xml";
        Properties properties = new Properties();
        properties.load(new FileInputStream(new File(propertyFile)));

        LagoonCLI.initializeLagoon(propertyFile, properties, null);
        LagoonProcessor processor = LagoonCLI.getProcessor();

        processor.build(false);
    }

    @Test
    public void testForceBuildWebsite() throws Exception {
        String propertyFile = "test_property_file.xml";
        Properties properties = new Properties();
        properties.load(new FileInputStream(new File(propertyFile)));

        LagoonCLI.initializeLagoon(propertyFile, properties, null);
        LagoonProcessor processor = LagoonCLI.getProcessor();

        processor.build(true);
    }

    @Test
    public void testIntervalBuildWebsite() throws Exception {
        String propertyFile = "test_property_file.xml";
        Properties properties = new Properties();
        properties.load(new FileInputStream(new File(propertyFile)));

        LagoonCLI.initializeLagoon(propertyFile, properties, null);
        LagoonProcessor processor = LagoonCLI.getProcessor();

        processor.build(false);
        Thread.sleep(500);
        processor.build(false);
        Thread.sleep(500);
        processor.build(false);
    }

    private String getProperty(String name) throws LagoonException {
        return LagoonCLI.getProperty(name);
    }
}