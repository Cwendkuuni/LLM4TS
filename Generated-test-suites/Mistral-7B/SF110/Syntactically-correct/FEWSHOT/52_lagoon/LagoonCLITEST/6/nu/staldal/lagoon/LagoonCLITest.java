package nu.staldal.lagoon;

import static org.junit.Assert.*;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class LagoonCLITest {

    @Test
    public void testInitWithPropertyFile() throws Exception {
        String[] args = new String[]{"property_file.xml"};
        LagoonCLI.main(args);
        assertTrue(LagoonCLI.properties != null);
    }

    @Test(expected = LagoonException.class)
    public void testInitWithInvalidPropertyFile() throws Exception {
        String[] args = new String[]{"invalid_property_file.xml"};
        LagoonCLI.main(args);
    }

    @Test
    public void testInitWithSitemapFile() throws Exception {
        String[] args = new String[]{"sitemap.xml"};
        LagoonCLI.main(args);
        assertNotNull(LagoonCLI.properties);
    }

    @Test(expected = LagoonException.class)
    public void testInitWithInvalidSitemapFile() throws Exception {
        String[] args = new String[]{"invalid_sitemap.xml"};
        LagoonCLI.main(args);
    }

    @Test
    public void testParseXML() throws Exception {
        String xml = "<sitemap>";
        InputStream input = new ByteArrayInputStream(xml.getBytes());
        DefaultHandler handler = new DefaultHandler();
        LagoonCLI.LagoonProcessor processor = new LagoonCLI.LagoonProcessor(null, null, null, null, null, null);
        LagoonCLI.parseXML(input, processor);
        assertNotNull(processor.getSiteMapTree());
    }

    @Test(expected = SAXException.class)
    public void testParseXMLWithException() throws Exception {
        String xml = "<sitemap><error>";
        InputStream input = new ByteArrayInputStream(xml.getBytes());
        LagoonCLI.LagoonProcessor processor = new LagoonCLI.LagoonProcessor(null, null, null, null, null, null);
        LagoonCLI.parseXML(input, processor);
    }

    @Test
    public void testGetProperty() throws Exception {
        String propertyFile = "property_file.xml";
        String[] args = new String[]{propertyFile};
        LagoonCLI.main(args);
        String targetURL = LagoonCLI.getProperty("targetURL");
        assertNotNull(targetURL);
    }

    @Test(expected = LagoonException.class)
    public void testGetPropertyWithUnspecifiedProperty() throws Exception {
        String propertyFile = "property_file.xml";
        String[] args = new String[]{propertyFile};
        LagoonCLI.main(args);
        String unspecifiedProperty = LagoonCLI.getProperty("unspecified_property");
        fail("Property 'unspecified_property' was not expected to be specified.");
    }

    @Test
    public void testShowTime() {
        long ms = 12345;
        LagoonCLI.showTime(ms);
        if (ms < 10000) {
            assertTrue(LagoonCLI.stdoutContains("in " + ms + " ms"));
        } else {
            assertTrue(LagoonCLI.stdoutContains("in " + ms / 1000 + " s"));
        }
    }
}