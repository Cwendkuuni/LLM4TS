package nu.staldal.lagoon;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import org.xml.sax.SAXException;

import org.junit.Before;
import org.junit.Test;

public class LagoonCLITest {

    private static final String SYNTAX_MSG = "Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]";
    private Properties properties;
    private LagoonCLI lagoonCLI;

    @Before
    public void setup() {
        properties = new Properties();
        lagoonCLI = new LagoonCLI();
    }

    @Test
    public void testSyntaxWithNoArguments() {
        lagoonCLI.main(new String[0]);
        assertEquals(SYNTAX_MSG, System.out.getErrorStream().toString());
    }

    @Test
    public void testSyntaxWithInvalidArgument() {
        lagoonCLI.main(new String[]{"invalid_file"});
        assertEquals(SYNTAX_MSG, System.out.getErrorStream().toString());
    }

    @Test
    public void testSyntaxWithInvalidBuildOrForce() {
        lagoonCLI.main(new String[]{"property_file", "invalid_build_or_force"});
        assertEquals(SYNTAX_MSG, System.out.getErrorStream().toString());
    }

    @Test
    public void testSyntaxWithInvalidInterval() {
        lagoonCLI.main(new String[]{"property_file", "-1"});
        assertEquals(SYNTAX_MSG, System.out.getErrorStream().toString());
    }

    @Test
    public void testInitializationWithValidPropertyFile() throws IOException, SAXException, InterruptedException {
        File propertyFile = new File("test_property_file.xml");
        properties.setProperty("targetURL", "http://test.com");
        properties.setProperty("sitemapFile", "test_sitemap.xml");
        properties.setProperty("sourceDir", "test_source_dir");
        properties.setProperty("password", "test_password");

        FileInputStream fis = new FileInputStream(propertyFile);
        properties.load(fis);
        fis.close();

        lagoonCLI.main(new String[]{"property_file"});
        assertEquals("Lagoon initialized successfully", System.out.getBufferStrategy().toString());
    }

    @Test
    public void testInitializationWithInvalidPropertyFile() {
        lagoonCLI.main(new String[]{"test_invalid_property_file.xml"});
        assertEquals(SYNTAX_MSG, System.out.getErrorStream().toString());
    }

    @Test
    public void testInitializationWithMissingPassword() {
        File propertyFile = new File("test_property_file.xml");
        properties.setProperty("targetURL", "http://test.com");
        properties.setProperty("sitemapFile", "test_sitemap.xml");
        properties.setProperty("sourceDir", "test_source_dir");

        FileInputStream fis = new FileInputStream(propertyFile);
        properties.load(fis);
        fis.close();

        lagoonCLI.main(new String[]{"property_file"});
        assertThrows(LagoonException.class, () -> {
            lagoonCLI.main(new String[]{"property_file", "build"});
        });
        assertEquals("Error while initializing Lagoon:\nPassword is required but not specified", System.err.getBufferStrategy().toString());
    }

    @Test
    public void testInitializationWithIncorrectPassword() {
        File propertyFile = new File("test_property_file.xml");
        properties.setProperty("targetURL", "http://test.com");
        properties.setProperty("sitemapFile", "test_sitemap.xml");
        properties.setProperty("sourceDir", "test_source_dir");
        properties.setProperty("password", "incorrect_password");

        FileInputStream fis = new FileInputStream(propertyFile);
        properties.load(fis);
        fis.close();

        assertThrows(AuthenticationException.class, () -> {
            lagoonCLI.main(new String[]{"property_file", "build"});
        });
        assertEquals("Error while initializing Lagoon:\nIncorrect password", System.err.getBufferStrategy().toString());
    }

    @Test
    public void testInitializationWithMissingFile() {
        lagoonCLI.main(new String[]{"invalid_property_file.xml"});
        assertEquals(SYNTAX_MSG, System.out.getErrorStream().toString());
    }

    @Test
    public void testInitializationWithMissingSitemapFile() {
        File propertyFile = new File("test_property_file.xml");
        properties.setProperty("targetURL", "http://test.com");
        properties.setProperty("sitemapFile", "");
        properties.setProperty("sourceDir", "test_source_dir");
        properties.setProperty("password", "test_password");

        FileInputStream fis = new FileInputStream(propertyFile);
        properties.load(fis);
        fis.close();

        lagoonCLI.main(new String[]{"property_file"});
        assertThrows(LagoonException.class, () -> {
            lagoonCLI.main(new String[]{"property_file", "build"});
        });
        assertEquals("Error while initializing Lagoon:\nFile not found: sitemapFile", System.err.getBufferStrategy().toString());
    }

    @Test
    public void testBuildingWebsite() throws IOException, InterruptedException, SAXException {
        File propertyFile = new File("test_property_file.xml");
        properties.setProperty("targetURL", "http://test.com");
        properties.setProperty("sitemapFile", "test_sitemap.xml");
        properties.setProperty("sourceDir", "test_source_dir");
        properties.setProperty("password", "test_password");

        FileInputStream fis = new FileInputStream(propertyFile);
        properties.load(fis);
        fis.close();

        lagoonCLI.main(new String[]{"property_file", "build"});
        String output = System.out.getBufferStrategy().toString();
        assertTrue(output.contains("Building website..."));
        assertTrue(output.contains("Lagoon initialized successfully"));
        assertFalse(output.contains("with errors..."));
    }

    @Test
    public void testForceBuildingWebsite() throws IOException, InterruptedException, SAXException {
        File propertyFile = new File("test_property_file.xml");
        properties.setProperty("targetURL", "http://test.com");
        properties.setProperty("sitemapFile", "test_sitemap.xml");
        properties.setProperty("sourceDir", "test_source_dir");
        properties.setProperty("password", "test_password");

        FileInputStream fis = new FileInputStream(propertyFile);
        properties.load(fis);
        fis.close();

        lagoonCLI.main(new String[]{"property_file", "force"});
        String output = System.out.getBufferStrategy().toString();
        assertTrue(output.contains("Force building website..."));
        assertTrue(output.contains("Lagoon initialized successfully"));
        assertFalse(output.contains("with errors..."));
    }

    @Test
    public void testBuildingWebsiteWithErrors() throws IOException, InterruptedException, SAXException {
        File propertyFile = new File("test_property_file.xml");
        properties.setProperty("targetURL", "http://test.com");
        properties.setProperty("sitemapFile", "test_sitemap_with_errors.xml");
        properties.setProperty("sourceDir", "test_source_dir");
        properties.setProperty("password", "test_password");

        FileInputStream fis = new FileInputStream(propertyFile);
        properties.load(fis);
        fis.close();

        lagoonCLI.main(new String[]{"property_file", "build"});
        String output = System.out.getBufferStrategy().toString();
        assertTrue(output.contains("Building website..."));
        assertTrue(output.contains("with errors..."));
    }

    @Test
    public void testBuildingWebsiteOnInterval() throws IOException, InterruptedException, SAXException {
        File propertyFile = new File("test_property_file.xml");
        properties.setProperty("targetURL", "http://test.com");
        properties.setProperty("sitemapFile", "test_sitemap.xml");
        properties.setProperty("sourceDir", "test_source_dir");
        properties.setProperty("password", "test_password");
        properties.setProperty("interval", "100");

        FileInputStream fis = new FileInputStream(propertyFile);
        properties.load(fis);
        fis.close();

        lagoonCLI.main(new String[]{"property_file"});
        Thread.sleep(110);

        lagoonCLI.main(new String[]{"property_file", "build"});
        String output = System.out.getBufferStrategy().toString();
        assertTrue(output.contains("Building website..."));
        assertTrue(output.contains("Lagoon initialized successfully"));
        assertFalse(output.contains("with errors..."));

        Thread.sleep(90);
        lagoonCLI.main(new String[]{"property_file", "build"});
        output = System.out.getBufferStrategy().toString();
        assertTrue(output.contains("Building website..."));
        assertTrue(output.contains("Lagoon initialized successfully"));
        assertFalse(output.contains("with errors..."));

        Thread.sleep(10);
        lagoonCLI.main(new String[]{"property_file", "build"});
        output = System.out.getBufferStrategy().toString();
        assertTrue(output.contains("Building website..."));
        assertTrue(output.contains("Lagoon initialized successfully"));
        assertFalse(output.contains("with errors..."));

        Thread.sleep(90);
        lagoonCLI.main(new String[]{"property_file", "build"});
        output = System.out.getBufferStrategy().toString();
        assertTrue(output.contains("Building website..."));
        assertTrue(output.contains("Lagoon initialized successfully"));
        assertFalse(output.contains("with errors..."));
    }
}