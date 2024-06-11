package nu.staldal.lagoon;

import static org.junit.Assert.*;
import org.junit.Test;
import org.xml.sax.SAXException;
import nu.staldal.lagoon.core.LagoonException;

public class LagoonCLITest {
    @Test
    public void testGetProperty() throws LagoonException {
        Properties properties = new Properties();
        properties.setProperty("targetURL", "http://example.com");
        LagoonCLI lagoonCLI = new LagoonCLI();
        String result = lagoonCLI.getProperty("targetURL");
        assertEquals("http://example.com", result);
    }

    @Test(expected = LagoonException.class)
    public void testGetPropertyWhenPropertyIsNull() throws LagoonException {
        Properties properties = new Properties();
        LagoonCLI lagoonCLI = new LagoonCLI();
        lagoonCLI.getProperty("nonExistentProperty");
    }

    @Test
    public void testShowTime() {
        long ms = 5000;
        LagoonCLI.showTime(ms);
        String expected = "in 5 s";
        StringBuilder actual = new StringBuilder();
        System.out.flush();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String input = reader.readLine();
        while (input == null || input.length() < 1) {
            input = reader.readLine();
        }
        actual.append(input);
        assertEquals(expected, actual.toString());
    }

    @Test
    public void testShowTimeShort() {
        long ms = 100;
        LagoonCLI.showTime(ms);
        String expected = "in 100 ms";
        StringBuilder actual = new StringBuilder();
        System.out.flush();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String input = reader.readLine();
        while (input == null || input.length() < 1) {
            input = reader.readLine();
        }
        actual.append(input);
        assertEquals(expected, actual.toString());
    }

    @Test(expected = LagoonException.class)
    public void testInitializeWithMissingPassword() throws LagoonException, IOException, SAXException {
        String[] args = {"property_file.xml"};
        LagoonCLI.main(args);
    }

    @Test(expected = LagoonException.class)
    public void testInitializeWithIncorrectPassword() throws LagoonException, IOException, SAXException {
        Properties properties = new Properties();
        properties.setProperty("password", "wrongPassword");
        String[] args = {"property_file.xml"};
        LagoonCLI lagoonCLI = new LagoonCLI();
        lagoonCLI.properties = properties;
        LagoonCLI.main(args);
    }

    @Test(expected = FileNotFoundException.class)
    public void testInitializeWithMissingPropertyFile() throws LagoonException, IOException, SAXException {
        String[] args = {"nonExistentFile.xml"};
        LagoonCLI.main(args);
    }

    @Test(expected = IOException.class)
    public void testInitializeWithMissingSitemapFile() throws LagoonException, IOException, SAXException {
        Properties properties = new Properties();
        properties.setProperty("sitemapFile", "nonExistentFile.xml");
        String[] args = {"property_file.xml"};
        LagoonCLI lagoonCLI = new LagoonCLI();
        lagoonCLI.properties = properties;
        LagoonCLI.main(args);
    }

    @Test(expected = LagoonException.class)
    public void testInitializeWithInvalidInterval() throws LagoonException, IOException, SAXException {
        Properties properties = new Properties();
        properties.setProperty("interval", "-5");
        String[] args = {"property_file.xml", "5"};
        LagoonCLI lagoonCLI = new LagoonCLI();
        lagoonCLI.properties = properties;
        LagoonCLI.main(args);
    }

    @Test
    public void testInitialize() throws LagoonException, IOException, SAXException {
        Properties properties = new Properties();
        properties.setProperty("targetURL", "http://example.com");
        properties.setProperty("sitemapFile", "sitemap.xml");
        properties.setProperty("sourceDir", "source");
        properties.setProperty("password", "password");
        String[] args = {"property_file.xml"};
        LagoonCLI.main(args);
    }

    @Test
    public void testBuild() throws LagoonException, IOException, SAXException, InterruptedException {
        Properties properties = new Properties();
        properties.setProperty("targetURL", "http://example.com");
        properties.setProperty("sitemapFile", "sitemap.xml");
        properties.setProperty("sourceDir", "source");
        properties.setProperty("password", "password");
        String[] args = {"property_file.xml"};
        LagoonCLI.main(args);
        LagoonProcessor processor = new LagoonProcessor(properties.getProperty("targetURL"), null, null, null, null, null);
        assertTrue(processor.build(false));
    }

    @Test
    public void testForceBuild() throws LagoonException, IOException, SAXException, InterruptedException {
        Properties properties = new Properties();
        properties.setProperty("targetURL", "http://example.com");
        properties.setProperty("sitemapFile", "sitemap.xml");
        properties.setProperty("sourceDir", "source");
        properties.setProperty("password", "password");
        String[] args = {"property_file.xml", "force"};
        LagoonCLI.main(args);
        LagoonProcessor processor = new LagoonProcessor(properties.getProperty("targetURL"), null, null, null, null, null);
        assertTrue(processor.build(true));
    }

    @Test
    public void testLongBuild() throws LagoonException, IOException, SAXException, InterruptedException {
        Properties properties = new Properties();
        properties.setProperty("targetURL", "http://example.com");
        properties.setProperty("sitemapFile", "sitemap.xml");
        properties.setProperty("sourceDir", "source");
        properties.setProperty("password", "password");
        String[] args = {"property_file.xml", "build"};
        LagoonCLI.main(args);
        LagoonProcessor processor = new LagoonProcessor(properties.getProperty("targetURL"), null, null, null, null, null);
        long timeBefore = System.currentTimeMillis();
        processor.build(false);
        long timeElapsed = System.currentTimeMillis() - timeBefore;
        assertTrue(timeElapsed > 5000);
    }
}