package nu.staldal.lagoon.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import nu.staldal.lagoon.*;
import nu.staldal.lagoon.core.*;
import nu.staldal.xtree.*;
import org.xml.sax.*;
import org.junit.Test;

public class LagoonCLITest {

 private static final String TEST_PROPERTY_FILE = "test.properties";
 private static final String TEST_SIMAP_FILE = "test.sitemap.xml";

 @Test
 public void testInvalidArgument() {
 LagoonCLI.main(new String[0]);
 String expected = "Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]";
 String actual = System.out.getErrors().toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testPropertyFileInvalid() {
 LagoonCLI.main(new String[]{TEST_PROPERTY_FILE + ".txt"});
 String expected = "Error while initializing Lagoon:\nFile not found: " + TEST_PROPERTY_FILE + ".txt";
 String actual = System.err.getErrors().toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testPropertyFileValid() throws IOException {
 Properties properties = new Properties();
 FileInputStream fis = new FileInputStream(TEST_PROPERTY_FILE);
 properties.load(fis);
 fis.close();
 String targetURL = properties.getProperty("targetURL");
 String sitemapFileStr = properties.getProperty("sitemapFile");
 File sitemapFile = new File(sitemapFileStr);
 LagoonCLI.main(new String[]{TEST_PROPERTY_FILE});
 assertEquals(targetURL, LagoonCLI.getProperty("targetURL"));
 assertEquals(sitemapFile.getAbsolutePath(), LagoonCLI.getProperty("sitemapFile"));
 }

 @Test(expected = NumberFormatException.class)
 public void testInvalidInterval() {
 LagoonCLI.main(new String[]{TEST_PROPERTY_FILE, "abc"});
 }

 @Test
 public void testValidInterval() {
 LagoonCLI.main(new String[]{TEST_PROPERTY_FILE, "100"});
 }

 @Test
 public void testBuild() throws IOException, SAXException {
 File sitemapFile = new File(TEST_SIMAP_FILE);
 LagoonCLI.main(new String[]{TEST_PROPERTY_FILE, "build"});
 assertTrue(new File(LagoonCLI.getProperty("sourceDir") + "/build").exists());
 }

 @Test
 public void testForceBuild() throws IOException, SAXException {
 File sitemapFile = new File(TEST_SIMAP_FILE);
 LagoonCLI.main(new String[]{TEST_PROPERTY_FILE, "force"});
 assertTrue(new File(LagoonCLI.getProperty("sourceDir") + "/build").exists());
 }

 @Test
 public void testInterval() throws IOException, InterruptedException, SAXException {
 File sitemapFile = new File(TEST_SIMAP_FILE);
 Properties properties = new Properties();
 FileInputStream fis = new FileInputStream(TEST_PROPERTY_FILE);
 properties.load(fis);
 fis.close();
 String targetURL = properties.getProperty("targetURL");
 String sitemapFileStr = properties.getProperty("sitemapFile");
 File file = new File(sitemapFileStr);
 LagoonCLI.main(new String[]{TEST_PROPERTY_FILE, "1000"});
 Thread.sleep(2000);
 LagoonCLI.main(new String[]{TEST_PROPERTY_FILE});
 LagoonCLI.main(new String[]{TEST_PROPERTY_FILE});
 LagoonCLI.main(new String[]{TEST_PROPERTY_FILE});
 long timeElapsed = System.currentTimeMillis() - Long.parseLong(LagoonCLI.getProperty("lastBuildTime"));
 assertTrue(timeElapsed > 900);
 }
}