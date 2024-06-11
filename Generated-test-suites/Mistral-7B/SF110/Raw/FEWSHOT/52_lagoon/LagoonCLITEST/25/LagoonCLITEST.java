package nu.staldal.lagoon;

import static org.junit.Assert.*;
import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.*;
import java.util.Properties;

public class LagoonCLITest {

    @Test
    public void testInitializeWithPropertyFile() throws Exception {
        String[] args = new String[]{"propertyFile.xml"};
        LagoonCLI.main(args);
        assertTrue(LagoonCLI.properties != null);
    }

    @Test(expected = LagoonException.class)
    public void testInitializeWithoutPropertyFile() {
        String[] args = new String[0];
        LagoonCLI.main(args);
    }

    @Test(expected = NumberFormatException.class)
    public void testInitializeInvalidInterval() throws Exception {
        String[] args = new String[]{"propertyFile.xml", "invalidInterval"};
        LagoonCLI.main(args);
    }

    @Test(expected = LagoonException.class)
    public void testInitializeNegativeInterval() throws Exception {
        String[] args = new String[]{"propertyFile.xml", "-1"};
        LagoonCLI.main(args);
    }

    @Test
    public void testInitializeWithSitemapFile() throws Exception {
        String[] args = new String[]{"sitemapFile.xml"};
        LagoonCLI.main(args);
        assertNotNull(LagoonCLI.properties);
    }

    @Test(expected = FileNotFoundException.class)
    public void testInitializeWithNonExistentPropertyFile() throws Exception {
        String[] args = new String[]{"nonexistentFile.xml"};
        LagoonCLI.main(args);
    }

    @Test(expected = FileNotFoundException.class)
    public void testInitializeWithNonExistentSitemapFile() throws Exception {
        String[] args = new String[]{"sitemapFile.xml"};
        LagoonCLI.main(args);
    }

    @Test(expected = AuthenticationMissingException.class)
    public void testInitializeWithMissingPassword() throws Exception {
        String[] args = new String[]{"propertyFile.xml"};
        LagoonCLI.main(args);
    }

    @Test(expected = AuthenticationException.class)
    public void testInitializeWithIncorrectPassword() throws Exception {
        Properties properties = new Properties();
        properties.setProperty("password", "incorrectPassword");
        String[] args = new String[]{"propertyFile.xml"};
        LagoonCLI.main(args);
    }

    @Test
    public void testParseXML() throws Exception {
        String fileName = "sitemapFile.xml";
        File file = new File(fileName);
        InputSource inputSource = new FileInputSource(file);
        Element sitemapTree = LagoonCLI.TreeBuilder.parseXML(inputSource, false);
        assertNotNull(sitemapTree);
    }

    @Test(expected = SAXException.class)
    public void testParseXMLWithException() throws Exception {
        String fileName = "sitemapFileWithException.xml";
        File file = new File(fileName);
        InputSource inputSource = new FileInputSource(file);
        LagoonCLI.TreeBuilder.parseXML(inputSource, false);
    }
}