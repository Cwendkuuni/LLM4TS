package nu.staldal.lagoon.test;

import static org.junit.Assert.*;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import nu.staldal.lagoon.LagoonCLI;
import nu.staldal.lagoon.LagoonException;
import nu.staldal.lagoon.core.LagoonProcessor;
import nu.staldal.xtree.TreeBuilder;
import nu.staldal.xtree.XMLInputSource;

public class LagoonCLITest {

    @org.junit.BeforeClass
    public static void setup() {
        LagoonCLI.DEBUG = false;
    }

    @org.junit.Test
    public void testInvalidArgumentCount() {
        String[] args = new String[0];
        try {
            LagoonCLI.main(args);
            fail("Expected error message with invalid argument count");
        } catch (LagoonException e) {
            assertTrue(e.getMessage().contains(LagoonCLI.syntaxMsg));
        }
    }

    @org.junit.Test
    public void testInvalidSecondArgumentPropertyFile() {
        String[] args = new String[]{"propertyFile.xml"};
        try {
            LagoonCLI.main(args);
            fail("Expected error message with invalid second argument");
        } catch (LagoonException e) {
            assertTrue(e.getMessage().contains(LagoonCLI.syntaxMsg));
        }
    }

    @org.junit.Test
    public void testInvalidThirdArgumentInvalidInterval() {
        String[] args = new String[]{"propertyFile.xml", "invalidArgument"};
        try {
            LagoonCLI.main(args);
            fail("Expected error message with invalid third argument");
        } catch (LagoonException e) {
            assertTrue(e.getMessage().contains(LagoonCLI.syntaxMsg));
        }
    }

    @org.junit.Test
    public void testValidArgumentsPropertyFileAndInterval() {
        String[] args = new String[]{"propertyFile.xml", "5000"};
        try {
            LagoonCLI.main(args);
            fail("Expected error message with invalid third argument");
        } catch (Exception e) {
            // No error expected
        }

        Properties properties = new Properties();
        properties.setProperty("targetURL", "http://example.com");
        properties.setProperty("sitemapFile", "sitemap.xml");
        properties.setProperty("sourceDir", "/tmp");
        properties.setProperty("password", "password");

        File propertyFile = new File("propertyFile.xml");
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(propertyFile);
            properties.load(fis);
            fis.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        args = new String[]{"propertyFile.xml"};
        try {
            LagoonCLI.main(args);
            fail("Expected initializing Lagoon message");
        } catch (Exception e) {
            // No error expected
        }

        args = new String[]{"propertyFile.xml", "build"};
        try {
            LagoonCLI.main(args);
            fail("Expected building website message");
        } catch (Exception e) {
            // No error expected
        }

        args = new String[]{"propertyFile.xml", "force"};
        try {
            LagoonCLI.main(args);
            fail("Expected force building website message");
        } catch (Exception e) {
            // No error expected
        }

        args = new String[]{"propertyFile.xml", "5000"};
        try {
            LagoonCLI.main(args);
            fail("Expected initializing Lagoon message");
        } catch (Exception e) {
            // No error expected
        }
    }

    @org.junit.Test
    public void testInvalidSitemapFile() {
        String[] args = new String[]{"propertyFile.xml", "5000", "invalidSitemap.xml"};
        try {
            LagoonCLI.main(args);
            fail("Expected error message with invalid sitemap file");
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("I/O error"));
        }
    }

    @org.junit.Test
    public void testValidSitemapFile() throws IOException, SAXException {
        String[] args = new String[]{"propertyFile.xml", "5000", "sitemap.xml"};
        Properties properties = new Properties();
        properties.setProperty("targetURL", "http://example.com");
        properties.setProperty("sitemapFile", "sitemap.xml");
        properties.setProperty("sourceDir", "/tmp");
        properties.setProperty("password", "password");

        File propertyFile = new File("propertyFile.xml");
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(propertyFile);
            properties.load(fis);
            fis.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        File sitemapFile = new File("sitemap.xml");
        Element sitemapTree = TreeBuilder.parseXML(new XMLInputSource(sitemapFile), false);

        try {
            LagoonCLI.main(args);
            fail("Expected initializing Lagoon message");
        } catch (Exception e) {
            // No error expected
        }

        LagoonProcessor processor = new LagoonProcessor(properties.getProperty("targetURL"), sitemapTree, new File("/tmp"), "password", new PrintWriter(System.out, true), new PrintWriter(System.err, true));

        args = new String[]{"build"};
        try {
            LagoonCLI.main(args);
            fail("Expected building website message");
        } catch (Exception e) {
            // No error expected
        }

        args = new String[]{"force"};
        try {
            LagoonCLI.main(args);
            fail("Expected force building website message");
        } catch (Exception e) {
            // No error expected
        }

        args = new String[]{"5000"};
        try {
            LagoonCLI.main(args);
            fail("Expected interval message");
        } catch (Exception e) {
            // No error expected
        }

        args = new String[]{"build"};
        try {
            LagoonCLI.main(args);
            fail("Expected building website message");
        } catch (Exception e) {
            // No error expected
        }

        args = new String[]{"force"};
        try {
            LagoonCLI.main(args);
            fail("Expected force building website message");
        } catch (Exception e) {
            // No error expected
        }

        args = new String[]{"build"};
        try {
            LagoonCLI.main(args);
            fail("Expected building website message");
        } catch (Exception e) {
            // No error expected
        }

        args = new String[]{"force"};
        try {
            LagoonCLI.main(args);
            fail("Expected force building website message");
        } catch (Exception e) {
            // No error expected
        }

        args = new String[]{"5000"};
        try {
            LagoonCLI.main(args);
            fail("Expected interval message");
        } catch (Exception e) {
            // No error expected
        }

        args = new String[]{"q"};
        try {
            LagoonCLI.main(args);
            fail("Expected exiting Lagoon message");
        } catch (Exception e) {
            // No error expected
        }

        processor.destroy();
    }
}