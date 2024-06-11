package nu.staldal.lagoon;

import static org.junit.Assert.*;
import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class LagoonCLITest {

    @Test
    public void testGetProperty() {
        Properties properties = new Properties();
        properties.setProperty("targetURL", "http://localhost:8080");
        LagoonCLI lagoonCLI = new LagoonCLI();
        String actual = lagoonCLI.getProperty("targetURL");
        String expected = "http://localhost:8080";
        assertEquals(expected, actual);
    }

    @Test(expected = LagoonException.class)
    public void testGetPropertyWithNullPropertyFile() {
        LagoonCLI lagoonCLI = new LagoonCLI();
        lagoonCLI.properties = null;
        lagoonCLI.getProperty("targetURL");
    }

    @Test(expected = LagoonException.class)
    public void testGetPropertyWithMissingProperty() {
        Properties properties = new Properties();
        LagoonCLI lagoonCLI = new LagoonCLI();
        lagoonCLI.properties = properties;
        lagoonCLI.getProperty("missingProperty");
    }

    @Test
    public void testInitializationWithPropertyFile() throws IOException, SAXException, LagoonException {
        String propertyFile = "test_property_file.xml";
        File file = new File(propertyFile);
        Properties properties = new Properties();
        properties.setProperty("targetURL", "http://localhost:8080");
        FileInputStream fis = new FileInputStream(file);
        properties.load(fis);
        fis.close();

        LagoonCLI lagoonCLI = new LagoonCLI();
        lagoonCLI.properties = properties;
        String actualTargetURL = lagoonCLI.getProperty("targetURL");
        String expectedTargetURL = "http://localhost:8080";
        assertEquals(expectedTargetURL, actualTargetURL);

        File sitemapFile = new File("test_sitemap_file.xml");
        Element sitemapTree = TreeBuilder.parseXML(TreeBuilder.fileToInputSource(sitemapFile), false);
        LagoonProcessor processor = new LagoonProcessor("http://test.com", sitemapTree, new File("./src"), null, new PrintWriter(System.out, true), new PrintWriter(System.err, true));
    }

    @Test(expected = LagoonException.class)
    public void testInitializationWithInvalidPropertyFile() throws IOException, SAXException, LagoonException {
        String propertyFile = "test_property_file.txt";
        File file = new File(propertyFile);
        Properties properties = new Properties();
        LagoonCLI lagoonCLI = new LagoonCLI();
        lagoonCLI.properties = properties;
        lagoonCLI.getProperty("targetURL");
    }

    @Test
    public void testInitializationWithInvalidSitemapFile() throws IOException, SAXException, LagoonException {
        String propertyFile = "test_property_file.xml";
        File propertyFileObj = new File(propertyFile);
        Properties properties = new Properties();
        properties.setProperty("targetURL", "http://localhost:8080");
        FileInputStream fis = new FileInputStream(propertyFileObj);
        properties.load(fis);
        fis.close();

        String sitemapFile = "test_sitemap_file.txt";
        File sitemapFileObj = new File(sitemapFile);
        LagoonCLI lagoonCLI = new LagoonCLI();
        lagoonCLI.properties = properties;
        try {
            lagoonCLI.initialize(sitemapFileObj);
        } catch (Exception e) {
            // expected
        }
    }

    @Test(expected = LagoonException.class)
    public void testInitializationWithMissingPassword() throws IOException, SAXException, LagoonException {
        String propertyFile = "test_property_file.xml";
        File propertyFileObj = new File(propertyFile);
        Properties properties = new Properties();
        properties.setProperty("targetURL", "http://localhost:8080");
        FileInputStream fis = new FileInputStream(propertyFileObj);
        properties.load(fis);
        fis.close();

        String sitemapFile = "test_sitemap_file.xml";
        File sitemapFileObj = new File(sitemapFile);
        LagoonCLI lagoonCLI = new LagoonCLI();
        lagoonCLI.properties = properties;
        try {
            lagoonCLI.initialize(sitemapFileObj, null);
        } catch (Exception e) {
            // expected
        }
    }

    @Test
    public void testBuild() throws IOException, SAXException, LagoonException, InterruptedException {
        String propertyFile = "test_property_file.xml";
        File propertyFileObj = new File(propertyFile);
        Properties properties = new Properties();
        properties.setProperty("targetURL", "http://localhost:8080");
        FileInputStream fis = new FileInputStream(propertyFileObj);
        properties.load(fis);
        fis.close();

        String sitemapFile = "test_sitemap_file.xml";
        File sitemapFileObj = new File(sitemapFile);
        LagoonCLI lagoonCLI = new LagoonCLI();
        lagoonCLI.properties = properties;
        lagoonCLI.initialize(sitemapFileObj, null);

        boolean built = lagoonCLI.build(false);
        assertTrue(built);
    }

    @Test
    public void testForceBuild() throws IOException, SAXException, LagoonException, InterruptedException {
        String propertyFile = "test_property_file.xml";
        File propertyFileObj = new File(propertyFile);
        Properties properties = new Properties();
        properties.setProperty("targetURL", "http://localhost:8080");
        FileInputStream fis = new FileInputStream(propertyFileObj);
        properties.load(fis);
        fis.close();

        String sitemapFile = "test_sitemap_file.xml";
        File sitemapFileObj = new File(sitemapFile);
        LagoonCLI lagoonCLI = new LagoonCLI();
        lagoonCLI.properties = properties;
        lagoonCLI.initialize(sitemapFileObj, null);

        boolean forcedBuilt = lagoonCLI.build(true);
        assertTrue(forcedBuilt);
    }

    @Test
    public void testIdleMode() throws IOException, SAXException, InterruptedException, LagoonException {
        String propertyFile = "test_property_file.xml";
        File propertyFileObj = new File(propertyFile);
        Properties properties = new Properties();
        properties.setProperty("targetURL", "http://localhost:8080");
        FileInputStream fis = new FileInputStream(propertyFileObj);
        properties.load(fis);
        fis.close();

        String sitemapFile = "test_sitemap_file.xml";
        File sitemapFileObj = new File(sitemapFile);
        LagoonCLI lagoonCLI = new LagoonCLI();
        lagoonCLI.properties = properties;
        lagoonCLI.initialize(sitemapFileObj, null);

        long interval = 1000L;
        lagoonCLI.build(false);
        long timeElapsed;
        long timeBefore = System.currentTimeMillis();
        while (true) {
            timeElapsed = System.currentTimeMillis() - timeBefore;
            if (timeElapsed < interval) {
                Thread.sleep(interval - timeElapsed);
            }
            if (!lagoonCLI.build(false)) {
                break;
            }
        }
    }
}