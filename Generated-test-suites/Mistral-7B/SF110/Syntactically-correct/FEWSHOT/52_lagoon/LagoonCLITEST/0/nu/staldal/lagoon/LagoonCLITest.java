package nu.staldal.lagoon;

import static org.junit.Assert.*;

import org.junit.Test;
import org.xml.sax.SAXException;

import nu.staldal.lagoon.core.*;
import java.io.*;
import nu.staldal.xtree.*;

public class LagoonCLITest {

    @Test
    public void testInitializeWithPropertyFile() throws Exception {
        String[] args = new String[]{"property_file.xml"};
        LagoonCLI.main(args);
        assertTrue(LagoonCLI.properties != null);
    }

    @Test(expected = LagoonException.class)
    public void testInitializeWithInvalidPropertyFile() throws Exception {
        String[] args = new String[]{"invalid_property_file"};
        LagoonCLI.main(args);
    }

    @Test
    public void testInitializeWithSitemapFile() throws Exception {
        String[] args = new String[]{"sitemap.xml"};
        LagoonCLI.main(args);
        assertNotNull(LagoonCLI.properties);
        assertNull(LagoonCLI.properties.getProperty("targetURL"));
        assertNull(LagoonCLI.properties.getProperty("sitemapFile"));
        assertNull(LagoonCLI.properties.getProperty("sourceDir"));
        assertNull(LagoonCLI.properties.getProperty("password"));
    }

    @Test(expected = LagoonException.class)
    public void testInitializeWithInvalidSitemapFile() throws Exception {
        String[] args = new String[]{"invalid_sitemap.xml"};
        LagoonCLI.main(args);
    }

    @Test
    public void testValidArguments() throws Exception {
        String[] args = new String[]{"property_file.xml", "1500"};
        LagoonCLI.main(args);
        assertEquals(1500L, LagoonCLI.interval);
    }

    @Test(expected = NumberFormatException.class)
    public void testInvalidArgument() throws Exception {
        String[] args = new String[]{"property_file.xml", "invalid_interval"};
        LagoonCLI.main(args);
    }

    @Test(expected = LagoonException.class)
    public void testInvalidArgumentWithNegativeInterval() throws Exception {
        String[] args = new String[]{"property_file.xml", "-1"};
        LagoonCLI.main(args);
    }

    @Test
    public void testBuildWebsite() throws Exception {
        String[] args = new String[]{"property_file.xml", "build"};
        LagoonCLI.main(args);
        assertTrue(LagoonCLI.processor != null);
        processor.build(false);
    }

    @Test
    public void testForceBuildWebsite() throws Exception {
        String[] args = new String[]{"property_file.xml", "force"};
        LagoonCLI.main(args);
        assertTrue(LagoonCLI.processor != null);
        processor.build(true);
    }

    @Test
    public void testPeriodicBuilding() throws Exception {
        String[] args = new String[]{"property_file.xml", "5000"};
        LagoonCLI.main(args);
        assertTrue(LagoonCLI.processor != null);
        long timeElapsed = 0;
        while (LagoonCLI.processor.build(false)) {
            timeElapsed += 500;
            Thread.sleep(500);
        }
        assertTrue(timeElapsed > 5000);
    }

    @Test(expected = LagoonException.class)
    public void testMissingPassword() throws Exception {
        String[] args = new String[]{"property_file.xml"};
        LagoonCLI.main(args);
    }

    @Test(expected = AuthenticationException.class)
    public void testIncorrectPassword() throws Exception {
        String[] args = new String[]{"property_file.xml", "wrong_password"};
        LagoonCLI.main(args);
    }

    @Test(expected = FileNotFoundException.class)
    public void testMissingPropertyFile() throws Exception {
        String[] args = new String[]{"missing_property_file.xml"};
        LagoonCLI.main(args);
    }

    @Test(expected = FileNotFoundException.class)
    public void testMissingSitemapFile() throws Exception {
        String[] args = new String[]{"property_file.xml", "sitemap_file.xml"};
        LagoonCLI.main(args);
    }

    @Test(expected = IOException.class)
    public void testIOExceptionDuringParsingSitemap() throws Exception {
        String[] args = new String[]{"property_file.xml", "sitemap_file_with_io_error.xml"};
        LagoonCLI.main(args);
    }

    @Test(expected = SAXException.class)
    public void testSAXExceptionDuringParsingSitemap() throws Exception {
        String[] args = new String[]{"property_file.xml", "sitemap_file_with_sax_error.xml"};
        LagoonCLI.main(args);
    }

    @Test
    public void testDebugMode() throws Exception {
        String[] args = new String[]{"property_file.xml"};
        LagoonCLI.DEBUG = true;
        LagoonCLI.main(args);
    }
}