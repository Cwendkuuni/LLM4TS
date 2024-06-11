package nu.staldal.lagoon.test;

import static org.junit.Assert.*;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import nu.staldal.lagoon.LagoonCLI;
import nu.staldal.lagoon.LagoonException;
import nu.staldal.lagoon.core.AuthenticationException;
import nu.staldal.lagoon.core.AuthenticationMissingException;
import nu.staldal.xtree.TreeBuilder;

public class LagoonCLITest {

 private static final String TEST_PROPERTY_FILE = "testPropertyFile.xml";
 private static final String TEST_SIMAP_FILE = "testSitemapFile.xml";
 private static final String TEST_TARGET_URL = "http://test.com";
 private static final String TEST_SOURCE_DIR = "src/test/resources";
 private static final String TEST_PASSWORD = "testPassword";

 @org.junit.BeforeClass
 public static void setupClass() {
 LagoonCLI.DEBUG = true;
 }

 @org.junit.AfterClass
 public static void tearDownClass() {
 LagoonCLI.DEBUG = false;
 }

 @org.junit.Test
 public void testInvalidArgs() {
 String[] args = new String[0];
 LagoonCLI.main(args);
 assertEquals("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]", System.out.getHWBuf().toString());
 }

 @org.junit.Test
 public void testInvalidInterval() {
 String[] args = new String[]{"testPropertyFile.xml", "-1"};
 LagoonCLI.main(args);
 assertEquals("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]", System.out.getHWBuf().toString());
 }

 @org.junit.Test
 public void testValidArgsPropertyFile() throws IOException, SAXException, LagoonException {
 String[] args = new String[]{"testPropertyFile.xml"};
 Properties expectedProperties = new Properties();
 expectedProperties.setProperty("targetURL", TEST_TARGET_URL);
 expectedProperties.setProperty("sitemapFile", TEST_SIMAP_FILE);
 expectedProperties.setProperty("sourceDir", TEST_SOURCE_DIR);
 expectedProperties.setProperty("password", TEST_PASSWORD);
 LagoonCLI.main(args);
 assertSame("Unexpected initialization message", "Initializing Lagoon...", System.out.getHWBuf().toString().substring(0, 20));
 Properties properties = LagoonCLI.properties;
 assertArrayEquals("Unexpected properties", new String[]{"targetURL", TEST_TARGET_URL, "sitemapFile", TEST_SIMAP_FILE, "sourceDir", TEST_SOURCE_DIR, "password", TEST_PASSWORD}, properties.stringPropertyNames());
 }

 @org.junit.Test
 public void testValidArgsSitemapFile() throws IOException, SAXException, LagoonException {
 File sitemapFile = new File(TEST_SIMAP_FILE);
 Element sitemapTree = TreeBuilder.parseXML(TreeBuilder.fileToInputSource(sitemapFile), false);
 String[] args = new String[]{"testSitemapFile.xml"};
 LagoonCLI.main(args);
 assertSame("Unexpected initialization message", "Initializing Lagoon...", System.out.getHWBuf().toString().substring(0, 20));
 }

 @org.junit.Test
 public void testInvalidPropertyFile() {
 String[] args = new String[]{"invalidPropertyFile.xml"};
 LagoonCLI.main(args);
 assertEquals("Error while initializing Lagoon:\nFile not found: invalidPropertyFile.xml", System.out.getHWBuf().toString());
 }

 @org.junit.Test
 public void testMissingPassword() throws IOException, SAXException, LagoonException, AuthenticationMissingException {
 String[] args = new String[]{"testPropertyFile.xml"};
 LagoonCLI.main(args);
 assertEquals("Error while initializing Lagoon:\nPassword is required but not specified", System.out.getHWBuf().toString());
 }

 @org.junit.Test
 public void testInvalidPassword() throws IOException, SAXException, LagoonException, AuthenticationException {
 String[] args = new String[]{"testPropertyFile.xml", TEST_PASSWORD + "invalid"};
 LagoonCLI.main(args);
 assertEquals("Error while initializing Lagoon:\nIncorrect password", System.out.getHWBuf().toString());
 }

 @org.junit.Test
 public void testBuild() throws IOException, SAXException, LagoonException {
 String[] args = new String[]{"testPropertyFile.xml", "build"};
 LagoonCLI.main(args);
 assertSame("Unexpected initialization message", "Initializing Lagoon...", System.out.getHWBuf().toString().substring(0, 20));
 assertFalse("Unexpected build result", LagoonCLI.DEBUG);
 }

 @org.junit.Test
 public void testForceBuild() throws IOException, SAXException, LagoonException {
 String[] args = new String[]{"testPropertyFile.xml", "force"};
 LagoonCLI.main(args);
 assertSame("Unexpected initialization message", "Initializing Lagoon...", System.out.getHWBuf().toString().substring(0, 20));
 assertFalse("Unexpected build result", LagoonCLI.DEBUG);
 }

 @org.junit.Test
 public void testInterval() throws IOException, SAXException, LagoonException, InterruptedException {
 long interval = 100L;
 String[] args = new String[]{"testPropertyFile.xml", Long.toString(interval)};
 LagoonCLI.main(args);
 assertSame("Unexpected initialization message", "Initializing Lagoon...", System.out.getHWBuf().toString().substring(0, 20));
 assertFalse("Unexpected build result", LagoonCLI.DEBUG);
 Thread.sleep(interval + 50);
 }
}