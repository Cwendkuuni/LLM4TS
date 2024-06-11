package nu.staldal.lagoon;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.junit.Test;

public class LagoonCLITest {

    @Test
    public void testInitializeWithPropertyFile() {
        String[] args = new String[]{"property_file.xml"};
        LagoonCLI.main(args);
        assertNotNull(LagoonCLI.properties);
    }

    @Test(expected = LagoonException.class)
    public void testInitializeWithInvalidPropertyFile() {
        String[] args = new String[]{"invalid_file.txt"};
        LagoonCLI.main(args);
    }

    @Test
    public void testInitializeWithSitemapFile() {
        String[] args = new String[]{"sitemap.xml"};
        LagoonCLI.main(args);
        assertNotNull(LagoonCLI.properties, "Properties should be initialized");
        assertEquals("targetURL", LagoonCLI.properties.getProperty("targetURL"));
        assertEquals("sitemapFile", LagoonCLI.properties.getProperty("sitemapFile"));
        assertEquals("sourceDir", LagoonCLI.properties.getProperty("sourceDir"));
        assertNull(LagoonCLI.properties.getProperty("password"));
    }

    @Test(expected = LagoonException.class)
    public void testInitializeWithMissingPasswordProperty() {
        String[] args = new String[]{"property_file.xml"};
        LagoonCLI.main(args);
        fail("Password property should be required but not initialized");
    }

    @Test(expected = AuthenticationException.class)
    public void testInitializeWithIncorrectPassword() {
        String[] args = new String[]{"property_file.xml", "password123"};
        LagoonCLI.main(args);
        fail("Authentication error: Incorrect password");
    }

    @Test(expected = FileNotFoundException.class)
    public void testInitializeWithMissingPropertyFile() {
        String[] args = new String[]{"missing_file.txt"};
        LagoonCLI.main(args);
        fail("FileNotFoundException: File not found: missing_file.txt");
    }

    @Test(expected = IOException.class)
    public void testInitializeWithIOError() {
        String[] args = new String[]{"property_file.xml", "password"};
        LagoonCLI.main(args);
        fail("IOException: I/O error: ");
    }

    @Test
    public void testBuildWithoutArguments() {
        String[] args = new String[]{"property_file.xml"};
        LagoonCLI.main(args);
        assertFalse(LagoonCLI.build);
        assertFalse(LagoonCLI.force);
        assertFalse(LagoonCLI.interval > 0);
    }

    @Test
    public void testBuildWithInterval() {
        String[] args = new String[]{"property_file.xml", "1000"};
        LagoonCLI.main(args);
        assertTrue(LagoonCLI.interval > 0);
    }

    @Test(expected = NumberFormatException.class)
    public void testBuildWithInvalidInterval() {
        String[] args = new String[]{"property_file.xml", "abc"};
        LagoonCLI.main(args);
        fail("NumberFormatException: For input string: \"abc\"");
    }

    @Test
    public void testBuildWithForce() {
        String[] args = new String[]{"property_file.xml", "build", "force"};
        LagoonCLI.main(args);
        assertTrue(LagoonCLI.build);
        assertTrue(LagoonCLI.force);
        assertFalse(LagoonCLI.interval > 0);
    }

    @Test
    public void testBuildWithNoForce() {
        String[] args = new String[]{"property_file.xml", "build"};
        LagoonCLI.main(args);
        assertTrue(LagoonCLI.build);
        assertFalse(LagoonCLI.force);
        assertFalse(LagoonCLI.interval > 0);
    }

    @Test
    public void testHelpMessage() {
        String[] args = new String[]{"help"};
        LagoonCLI.main(args);
        assertEquals("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.stagdal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]", System.out.getBuffer().toString());
    }

    @Test
    public void testParseXML() throws SAXException {
        String filePath = "src/test/resources/sitemap.xml";
        XMLReader parser = XMLReaderFactory.newDefaultXMLReader();
        DefaultHandler handler = new DefaultHandler();
        parser.setContentHandler(handler);
        File inputFile = new File(filePath);
        InputSource inputSource = new InputSource(new FileInputStream(inputFile));
        parser.parse(inputSource);
    }
}