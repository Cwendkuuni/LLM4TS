package nu.staldal.lagoon;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import nu.staldal.xtree.Element;

public class LagoonCLITest {

    private static final String TEST_XML_FILE = "test.xml";
    private static final String TEST_PROPERTY_FILE = "test.properties";
    private static final String TEST_SIMAP_FILE = "test.sitemap";
    private static final String TEST_TARGET_URL = "http://test.com";

    private Properties properties;
    private File propertyFile;
    private File sitemapFile;
    private Element sitemapTree;

    @Before
    public void setUp() {
        properties = new Properties();
        propertyFile = new File(TEST_PROPERTY_FILE);
        sitemapFile = new File(TEST_SIMAP_FILE);
    }

    @After
    public void tearDown() throws IOException {
        if (propertyFile.exists()) {
            propertyFile.delete();
        }
        if (sitemapFile.exists()) {
            sitemapFile.delete();
        }
    }

    @Test
    public void testMainWithPropertyFileAndNoArguments() {
        String[] args = new String[] {"test.properties"};
        LagoonCLI.main(args);
        assertTrue(LagoonCLI.DEBUG == false);
        assertNull(LagoonCLI.properties);
    }

    @Test
    public void testMainWithPropertyFileAndArguments() {
        String[] args = new String[] {"test.properties", "1"};
        LagoonCLI.main(args);
        assertTrue(LagoonCLI.DEBUG == false);
        assertFalse(LagoonCLI.build);
        assertFalse(LagoonCLI.force);
        assertEquals(1000, LagoonCLI.interval);
    }

    @Test
    public void testMainWithSitemapFileAndNoArguments() {
        String[] args = new String[] {"test.sitemap"};
        LagoonCLI.main(args);
        assertTrue(LagoonCLI.DEBUG == false);
        assertNull(LagoonCLI.properties);
        try {
            sitemapTree = TreeBuilder.parseXML(TreeBuilder.fileToInputSource(sitemapFile), false);
        } catch (Exception e) {
            fail("Exception occurred during parsing sitemap file");
        }
    }

    @Test(expected = LagoonException.class)
    public void testMainWithInvalidArgument() {
        String[] args = new String[] {"test.properties", "invalid"};
        LagoonCLI.main(args);
    }

    @Test
    public void testMainWithInvalidSitemapFile() {
        String[] args = new String[] {"test.sitemap2"};
        LagoonCLI.main(args);
        assertTrue(LagoonCLI.DEBUG == false);
        assertNull(LagoonCLI.properties);
        assertThrows(FileNotFoundException.class, () -> TreeBuilder.parseXML(TreeBuilder.fileToInputSource(sitemapFile), false));
    }

    @Test(expected = NumberFormatException.class)
    public void testMainWithInvalidInterval() {
        String[] args = new String[] {"test.properties", "invalid_interval"};
        LagoonCLI.main(args);
    }

    @Test
    public void testMainWithValidArgumentForBuild() {
        String[] args = new String[] {"test.properties", "build"};
        LagoonCLI.main(args);
        assertTrue(LagoonCLI.DEBUG == false);
        assertTrue(LagoonCLI.build);
        assertFalse(LagoonCLI.force);
        assertNull(LagoonCLI.interval);
    }

    @Test
    public void testMainWithValidArgumentForForce() {
        String[] args = new String[] {"test.properties", "force"};
        LagoonCLI.main(args);
        assertTrue(LagoonCLI.DEBUG == false);
        assertFalse(LagoonCLI.build);
        assertFalse(LagoonCLI.force);
        assertNull(LagoonCLI.interval);
    }

    @Test
    public void testMainWithValidArgumentForInterval() {
        String[] args = new String[] {"test.properties", "10"};
        LagoonCLI.main(args);
        assertTrue(LagoonCLI.DEBUG == false);
        assertFalse(LagoonCLI.build);
        assertFalse(LagoonCLI.force);
        assertEquals(10000, LagoonCLI.interval);
    }

    @Test(expected = AuthenticationException.class)
    public void testMainWithInvalidPassword() {
        properties.setProperty("password", "wrong_password");
        String[] args = new String[] {"test.properties"};
        LagoonCLI.main(args);
    }

    @Test(expected = FileNotFoundException.class)
    public void testMainWithMissingPropertyFile() {
        String[] args = new String[] {"missing.properties"};
        LagoonCLI.main(args);
    }

    @Test(expected = IOException.class)
    public void testMainWithMissingSitemapFile() {
        String[] args = new String[] {"test.properties"};
        LagoonCLI.main(args);
    }
}