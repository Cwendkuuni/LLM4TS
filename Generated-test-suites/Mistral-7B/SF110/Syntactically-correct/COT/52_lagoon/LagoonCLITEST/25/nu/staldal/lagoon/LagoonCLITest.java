package nu.staldal.lagoon;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.junit.BeforeClass;
import org.junit.Test;

import nu.staldal.lagoon.LagoonCLI;
import nu.staldal.lagoon.LagoonException;
import nu.staldal.xtree.TreeBuilder;

public class LagoonCLITest {

    private static Properties properties;
    
    @BeforeClass
    public static void setup() {
        LagoonCLI.properties = new Properties();
    }

    

    @Test
    public void testMain_NoArgs() {
        String[] args = new String[0];
        LagoonCLI.main(args);
        assertEquals("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]", System.out.getHeapMemoryUsage().getMemoryUsed());
    }

    @Test
    public void testMain_InvalidArgument() {
        String[] args = {"test.txt"};
        LagoonCLI.main(args);
        assertEquals("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]", System.out.getHeapMemoryUsage().getMemoryUsed());
    }

    @Test
    public void testMain_InvalidInterval() {
        String[] args = {"properties.xml", "-1"};
        LagoonCLI.main(args);
        assertEquals("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]", System.out.getHeapMemoryUsage().getMemoryUsed());
    }

    @Test
    public void testMain_InvalidBuildOrForce() {
        String[] args = {"properties.xml", "invalid"};
        LagoonCLI.main(args);
        assertEquals("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]", System.out.getHeapMemoryUsage().getMemoryUsed());
    }

    @Test
    public void testMain_ValidArguments() {
        String[] args = {"properties.xml", "5000"};
        LagoonCLI.main(args);
        assertFalse(System.out.getHeapMemoryUsage().getMemoryUsed() > 10000);
    }

    @Test
    public void testMain_PropertyFileMissing() throws IOException, LagoonException {
        String[] args = {"missing.xml", "valid_interval"};
        FileInputStream fis = new FileInputStream("missing.xml");
        try {
            LagoonCLI.main(args);
        } finally {
            fis.close();
        }
        assertEquals("Error while initializing Lagoon:\nFile not found: missing.xml", System.out.getHeapMemoryUsage().getMemoryUsed());
    }

    @Test
    public void testMain_InvalidPropertyFile() throws IOException, LagoonException {
        Properties properties = new Properties();
        properties.setProperty("targetURL", "invalid_url");
        FileInputStream fis = new FileInputStream("properties.xml");
        try {
            LagoonCLI.properties.load(fis);
            LagoonCLI.main(new String[]{"properties.xml", "valid_interval"});
        } finally {
            fis.close();
        }
        assertEquals("Error while initializing Lagoon:\nProperty targetURL not specified", System.out.getHeapMemoryUsage().getMemoryUsed());
    }

    @Test
    public void testMain_InvalidSitemapFile() throws SAXException, IOException, LagoonException {
        String[] args = {"properties.xml", "property_file.xml"};
        Element sitemapTree = TreeBuilder.parseXML(TreeBuilder.fileToInputSource(new File("property_file.xml")), false);
        LagoonCLI lagoonCLI = new LagoonCLI();
        try {
            lagoonCLI.init(args);
            fail("Expected LagoonException to be thrown");
        } catch (LagoonException e) {
            assertTrue(e.getMessage().contains("Error while parsing XML file"));
        }
    }

    @Test
    public void testMain_AuthenticationMissingException() throws IOException, LagoonException {
        String[] args = {"properties.xml", "property_file.xml"};
        Properties properties = new Properties();
        properties.setProperty("password", "");
        LagoonCLI.properties = properties;
        LagoonCLI lagoonCLI = new LagoonCLI();
        try {
            lagoonCLI.init(args);
            fail("Expected AuthenticationMissingException to be thrown");
        } catch (AuthenticationMissingException e) {
            assertTrue(e.getMessage().contains("Password is required but not specified"));
        }
    }

    @Test
    public void testMain_AuthenticationException() throws IOException, LagoonException {
        String[] args = {"properties.xml", "property_file.xml"};
        Properties properties = new Properties();
        properties.setProperty("password", "wrong_password");
        LagoonCLI.properties = properties;
        LagoonCLI lagoonCLI = new LagoonCLI();
        try {
            lagoonCLI.init(args);
            fail("Expected AuthenticationException to be thrown");
        } catch (AuthenticationException e) {
            assertTrue(e.getMessage().contains("Incorrect password"));
        }
    }

    @Test
    public void testInit_PropertyFileIsXML() throws IOException, LagoonException {
        String[] args = {"properties.xml", "valid_interval"};
        LagoonCLI lagoonCLI = new LagoonCLI();
        lagoonCLI.init(args);
        assertTrue(lagoonCLI.getSitemapFile() instanceof File);
    }

    @Test
    public void testInit_PropertyFileIsNotXML() throws IOException, LagoonException {
        String[] args = {"properties.txt", "valid_interval"};
        LagoonCLI lagoonCLI = new LagoonCLI();
        try {
            lagoonCLI.init(args);
            fail("Expected LagoonException to be thrown");
        } catch (LagoonException e) {
            assertTrue(e.getMessage().contains("Property file is not a valid XML file"));
        }
    }

    @Test
    public void testInit_InvalidInterval() throws IOException, LagoonException {
        String[] args = {"properties.xml", "-1"};
        LagoonCLI lagoonCLI = new LagoonCLI();
        try {
            lagoonCLI.init(args);
            fail("Expected LagoonException to be thrown");
        } catch (LagoonException e) {
            assertTrue(e.getMessage().contains("Invalid interval"));
        }
    }

    @Test
    public void testInit_ValidArguments() throws IOException, LagoonException {
        String[] args = {"properties.xml", "valid_interval"};
        LagoonCLI lagoonCLI = new LagoonCLI();
        lagoonCLI.init(args);
        assertNotNull(lagoonCLI.getProcessor());
    }

    @Test
    public void testDestroy() throws IOException {
        String[] args = {"properties.xml", "valid_interval"};
        LagoonCLI lagoonCLI = new LagoonCLI();
        lagoonCLI.init(args);
        lagoonCLI.destroy();
        assertNull(lagoonCLI.getProcessor());
    }

    
}