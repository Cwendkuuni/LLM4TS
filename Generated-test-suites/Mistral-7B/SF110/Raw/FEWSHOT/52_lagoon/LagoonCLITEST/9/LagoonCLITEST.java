package nu.staldal.lagoon;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import org.xml.sax.SAXException;
import org.junit.Test;
import org.xml.sax.helpers.FileInputSource;

import nu.staldal.lagoon.core.LagoonException;
import nu.staldal.xtree.TreeBuilder;

public class LagoonCLITest {

    @Test
    public void testGetProperty() {
        Properties props = new Properties();
        props.setProperty("targetURL", "http://example.com");
        LagoonCLI lagoonCLI = new LagoonCLI();
        String result = lagoonCLI.getProperty("targetURL");
        assertTrue(result.equals("http://example.com"));
    }

    @Test(expected = LagoonException.class)
    public void testGetPropertyWhenPropertyNotSpecified() {
        Properties props = new Properties();
        LagoonCLI lagoonCLI = new LagoonCLI();
        String result = lagoonCLI.getProperty("nonExistentProperty");
    }

    @Test
    public void testShowTimeLessThan10000ms() {
        LagoonCLI lagoonCLI = new LagoonCLI();
        long time = 5000;
        lagoonCLI.showTime(time);
        String output = System.out.getBuffer().toString();
        assertTrue(output.contains("in " + time + " ms"));
    }

    @Test
    public void testShowTimeMoreThan10000ms() {
        LagoonCLI lagoonCLI = new LagoonCLI();
        long time = 15000;
        lagoonCLI.showTime(time);
        String output = System.out.getBuffer().toString();
        assertTrue(output.contains("in " + time / 1000L + " s"));
    }

    @Test(expected = LagoonException.class)
    public void testInitializeWithMissingPropertyFile() {
        LagoonCLI lagoonCLI = new LagoonCLI();
        String[] args = new String[]{"nonexistentfile.xml"};
        lagoonCLI.main(args);
    }

    @Test(expected = LagoonException.class)
    public void testInitializeWithInvalidPropertyFile() {
        Properties props = new Properties();
        props.setProperty("targetURL", "http://example.com");
        LagoonCLI lagoonCLI = new LagoonCLI();
        String[] args = new String[]{"nonexistentfile.xml"};
        lagoonCLI.main(args);
    }

    @Test
    public void testInitializeWithValidPropertyFile() {
        Properties props = new Properties();
        props.setProperty("targetURL", "http://example.com");
        props.setProperty("sitemapFile", "test.xml");
        props.setProperty("sourceDir", "test_source");
        LagoonCLI lagoonCLI = new LagoonCLI();
        String[] args = new String[]{"property_file.xml"};
        lagoonCLI.main(args);
    }

    @Test(expected = LagoonException.class)
    public void testInitializeWithMissingPassword() {
        Properties props = new Properties();
        props.setProperty("targetURL", "http://example.com");
        LagoonCLI lagoonCLI = new LagoonCLI();
        String[] args = new String[]{"property_file_with_password.xml"};
        lagoonCLI.main(args);
    }

    @Test(expected = LagoonException.class)
    public void testInitializeWithInvalidPassword() {
        Properties props = new Properties();
        props.setProperty("targetURL", "http://example.com");
        props.setProperty("password", "incorrect_password");
        LagoonCLI lagoonCLI = new LagoonCLI();
        String[] args = new String[]{"property_file_with_password.xml"};
        lagoonCLI.main(args);
    }

    @Test
    public void testInitializeWithValidPassword() {
        Properties props = new Properties();
        props.setProperty("targetURL", "http://example.com");
        props.setProperty("password", "correct_password");
        LagoonCLI lagoonCLI = new LagoonCLI();
        String[] args = new String[]{"property_file_with_password.xml"};
        lagoonCLI.main(args);
    }

    @Test(expected = SAXException.class)
    public void testParsingSitemapFileWithException() throws SAXException {
        String xml = "<?xml version='1.0' encoding='UTF-8'?>\n<root>\n<error>\n<code>1</code>\n<message>Test error</message>\n</error>\n</root>";
        InputSource inputSource = new FileInputSource(new ByteArrayInputStream(xml.getBytes()));
        try {
            TreeBuilder.parseXML(inputSource, false);
        } catch (IOException e) {
            throw new SAXException(e);
        }
    }

    @Test
    public void testParseSitemapFileSuccessfully() {
        String xml = "<?xml version='1.0' encoding='UTF-8'?>\n<sitemapindex xmlns='http://www.sitemaps.org/schemas/sitemap/0.9'>\n<sitemap>\n<loc>http://example.com/page1.html</loc>\n</sitemap>\n<sitemap>\n<loc>http://example.com/page2.html</loc>\n</sitemap>\n</sitemapindex>";
        InputSource inputSource = new FileInputSource(new ByteArrayInputStream(xml.getBytes()));
        Element root = null;
        try {
            root = TreeBuilder.parseXML(inputSource, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Element sitemap1 = root.getChildElements().get(0);
        List<Element> locs = sitemap1.getChildElements();
        assertEquals(1, locs.size());
        assertEquals("http://example.com/page1.html", locs.get(0).getTextTrim());

        Element sitemap2 = root.getChildElements().get(1);
        locs = sitemap2.getChildElements();
        assertEquals(1, locs.size());
        assertEquals("http://example.com/page2.html", locs.get(0).getTextTrim());
    }

    @Test
    public void testBuildWebsite() {
        LagoonCLI lagoonCLI = new LagoonCLI();
        String[] args = new String[]{"property_file.xml"};
        lagoonCLI.main(args);
        assertTrue(System.out.getBuffer().toString().matches(".*Building website.*"));
    }

    @Test
    public void testForceBuildWebsite() {
        LagoonCLI lagoonCLI = new LagoonCLI();
        String[] args = new String[]{"property_file.xml", "force"};
        lagoonCLI.main(args);
        assertTrue(System.out.getBuffer().toString().matches(".*Force building website.*"));
    }

    @Test
    public void testBuildWebsiteWithErrors() {
        LagoonCLI lagoonCLI = new LagoonCLI();
        String[] args = new String[]{"property_file.xml", "build"};
        lagoonCLI.main(args);
        assertTrue(System.out.getBuffer().toString().matches(".*with errors.*"));
    }

    @Test
    public void testBuildWebsiteWithInterval() {
        LagoonCLI lagoonCLI = new LagoonCLI();
        String[] args = new String[]{"property_file.xml", "5000"};
        lagoonCLI.main(args);
        assertTrue(System.out.getBuffer().toString().matches(".*Building website.*"));
    }

    @Test
    public void testBuildWebsiteWithIntervalAndErrors() {
        LagoonCLI lagoonCLI = new LagoonCLI();
        String[] args = new String[]{"property_file.xml", "5000", "build"};
        lagoonCLI.main(args);
        assertTrue(System.out.getBuffer().toString().matches(".*Building website.*"));
        assertTrue(System.out.getBuffer().toString().matches(".*with errors.*"));
    }

    @Test
    public void testInterruptBuildWebsite() {
        LagoonCLI lagoonCLI = new LagoonCLI();
        String[] args = new String[]{"property_file.xml"};
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        in.readLine();
        lagoonCLI.main(args);
        in.readLine();
        in.close();
        assertEquals("q", in.readLine());
        assertTrue(System.out.getBuffer().toString().matches(".*Ready.*"));
        assertTrue(System.out.getBuffer().toString().matches(".*Building website.*"));
        assertTrue(System.out.getBuffer().toString().matches(".*with errors.*"));
    }
}