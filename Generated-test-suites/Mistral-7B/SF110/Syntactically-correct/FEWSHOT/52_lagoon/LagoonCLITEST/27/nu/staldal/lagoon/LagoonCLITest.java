package nu.staldal.lagoon;

import static org.junit.Assert.*;
import org.junit.Test;

public class LagoonCLITest {
    @Test
    public void testGetProperty() {
        Properties properties = new Properties();
        properties.setProperty("targetURL", "http://example.com");
        LagoonCLI lagoonCLI = new LagoonCLI();
        String result = lagoonCLI.getProperty("targetURL");
        assertEquals("http://example.com", result);
    }

    @Test(expected = LagoonException.class)
    public void testGetPropertyWithNullProperties() {
        LagoonCLI lagoonCLI = new LagoonCLI();
        String result = lagoonCLI.getProperty("targetURL");
        assertNull(result);
        // This test case should throw LagoonException because the property is not set
    }

    @Test
    public void testGetPropertyWithNullName() {
        Properties properties = new Properties();
        properties.setProperty("targetURL", "http://example.com");
        LagoonCLI lagoonCLI = new LagoonCLI();
        String result = lagoonCLI.getProperty(null);
        assertNull(result);
    }

    @Test
    public void testShowTime() {
        LagoonCLI lagoonCLI = new LagoonCLI();
        long ms = 123;
        lagoonCLI.showTime(ms);
        if (ms < 10000L) {
            assertTrue(lagoonCLI.stdoutContains("in " + ms + " ms"));
        } else {
            assertTrue(lagoonCLI.stdoutContains("in " + ms / 1000L + " s"));
        }
    }

    @Test
    public void testInitWithPropertyFile() {
        String propertyFile = "test.properties";
        String targetURL = "http://example.com";
        String sitemapFile = "sitemap.xml";
        String sourceDir = "src";
        String password = "secret";

        try (FileWriter writer = new FileWriter(propertyFile)) {
            writer.write("targetURL=" + targetURL);
            writer.write("\nsitemapFile=" + sitemapFile);
            writer.write("\nsourceDir=" + sourceDir);
            writer.write("\npassword=" + password);
            writer.flush();
        }

        String[] args = new String[]{"test.properties"};
        LagoonCLI lagoonCLI = new LagoonCLI();
        lagoonCLI.main(args);

        assertEquals(targetURL, lagoonCLI.getProperty("targetURL"));
        assertEquals(sitemapFile, lagoonCLI.getProperty("sitemapFile"));
        assertEquals(sourceDir, lagoonCLI.getProperty("sourceDir"));
        assertNull(lagoonCLI.getProperty("unknownProperty"));

        // Clean up
        File file = new File(propertyFile);
        file.delete();
    }

    @Test
    public void testInitWithXMLFile() {
        String sitemapFile = "sitemap.xml";
        String targetURL = "http://example.com";
        String password = "secret";

        Element sitemapTree = createMockSitemapTree();

        String[] args = new String[]{sitemapFile};
        LagoonCLI lagoonCLI = new LagoonCLI();
        lagoonCLI.main(args);

        assertEquals(targetURL, lagoonCLI.getProperty("targetURL"));
        assertNull(lagoonCLI.getProperty("sitemapFile")); // it should be loaded from the XML file
        assertEquals(null, lagoonCLI.getProperty("password"));

        // Clean up
        sitemapTree.getDocument().unload();
    }

    private Element createMockSitemapTree() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Element root = builder.newDocument().createElement("sitemapindex");
            Element loc = builder.newDocument().createElement("sitemap");
            loc.setAttribute("xmlns", "http://www.sitemaps.org/schemas/sitemap/0.9");
            loc.setAttribute("loc", "/path/to/sitemap.xml");
            root.appendChild(loc);
            return root;
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
    }
}