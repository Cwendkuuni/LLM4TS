package nu.staldal.lagoon;

import static org.junit.Assert.*;
import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class LagoonCLITest {

    @Test
    public void testInitializeWithPropertyFile() throws Exception {
        String[] args = new String[]{"property_file.xml"};
        LagoonCLI.main(args);
        assertTrue(LagoonCLI.properties != null);
        assertEquals("targetURL", LagoonCLI.getProperty("targetURL"));
        assertEquals("sitemapFile", LagoonCLI.getProperty("sitemapFile"));
        assertEquals("sourceDir", LagoonCLI.getProperty("sourceDir"));
        assertNull(LagoonCLI.properties.getProperty("password"));
    }

    @Test(expected = LagoonException.class)
    public void testInitializeWithInvalidPropertyFile() throws Exception {
        String[] args = new String[]{"invalid_property_file.xml"};
        LagoonCLI.main(args);
    }

    @Test
    public void testInitializeWithSitemapFile() throws Exception {
        String[] args = new String[]{"sitemap.xml"};
        LagoonCLI.main(args);
        assertNull(LagoonCLI.properties);
        assertNotNull(LagoonCLI.properties = new Properties());
        assertEquals("", LagoonCLI.getProperty("targetURL"));
        assertEquals("sitemap.xml", LagoonCLI.getProperty("sitemapFile"));
        assertEquals("", LagoonCLI.getProperty("sourceDir"));
        assertNull(LagoonCLI.properties.getProperty("password"));
    }

    @Test(expected = NumberFormatException.class)
    public void testInitializeWithInvalidInterval() throws Exception {
        String[] args = new String[]{"property_file.xml", "invalid_interval"};
        LagoonCLI.main(args);
    }

    @Test
    public void testInitializeWithValidInterval() throws Exception {
        String[] args = new String[]{"property_file.xml", "3000"};
        LagoonCLI.main(args);
        long interval = 3000L;
        assertTrue(LagoonCLI.interval > 0L);
        assertEquals(interval, LagoonCLI.interval);
    }

    @Test
    public void testParseXML() throws Exception {
        String filePath = "test_sitemap.xml";
        File file = new File(filePath);
        Element sitemapTree = LagoonCLI.parseXML(TreeBuilder.fileToInputSource(file));
        assertNotNull(sitemapTree);
    }

    @Test(expected = SAXException.class)
    public void testParseXMLWithException() throws Exception {
        String filePath = "test_sitemap_exception.xml";
        File file = new File(filePath);
        LagoonCLI.parseXML(TreeBuilder.fileToInputSource(file));
    }
}