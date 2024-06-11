package nu.staldal.lagoon.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import nu.staldal.lagoon.LagoonCLI;
import nu.staldal.lagoon.LagoonException;
import nu.staldal.lagoon.core.LagoonProcessor;
import nu.staldal.xtree.TreeBuilder;
import org.xml.sax.SAXException;

public class LagoonCLITest {

 private static final String TEST_PROPERTY_FILE = "testPropertyFile.xml";
 private static final String TEST_SIMAP_FILE = "testSitemapFile.xml";
 private static final String TEST_TARGET_URL = "http://test.com";

 @org.junit.BeforeClass
 public static void setUpBeforeClass() throws Exception {
 LagoonCLI.DEBUG = true;
 }

 @org.junit.AfterClass
 public static void tearDownAfterClass() throws Exception {
 LagoonCLI.DEBUG = false;
 }

 @org.junit.Test
 public void testInvalidSyntax() {
 LagoonCLI.main(new String[0]);
 assertTrue(System.out.toString().contains(LagoonCLI.syntaxMsg));
 }

 @org.junit.Test
 public void testInvalidArgument1() {
 LagoonCLI.main(new String[]{"test.xml", "abc"});
 assertTrue(System.out.toString().contains(LagoonCLI.syntaxMsg));
 }

 @org.junit.Test
 public void testInvalidArgument2() {
 LagoonCLI.main(new String[]{"test.xml", "build", "10"});
 assertTrue(System.out.toString().contains(LagoonCLI.syntaxMsg));
 }

 @org.junit.Test
 public void testValidArgumentsPropertyFile() throws Exception {
 Properties properties = new Properties();
 properties.setProperty("targetURL", TEST_TARGET_URL);
 properties.setProperty("sitemapFile", TEST_SIMAP_FILE);
 properties.setProperty("sourceDir", "testSourceDir");
 File propertyFile = new File(TEST_PROPERTY_FILE);
 FileInputStream fis = new FileInputStream(propertyFile);
 properties.load(fis);
 fis.close();

 LagoonCLI.main(new String[]{TEST_PROPERTY_FILE});
 assertTrue(System.out.toString().contains("Lagoon initialized successfully"));
 }

 @org.junit.Test
 public void testValidArgumentsSitemapFile() throws Exception {
 File sitemapFile = new File(TEST_SIMAP_FILE);
 Element sitemapTree = TreeBuilder.parseXML(TreeBuilder.fileToInputSource(sitemapFile), false);
 LagoonCLI.main(new String[]{TEST_SIMAP_FILE.toString(), "build"});
 assertTrue(System.out.toString().contains("Building website..."));
 }

 @org.junit.Test(expected = LagoonException.class)
 public void testMissingPassword() throws Exception {
 Properties properties = new Properties();
 properties.setProperty("targetURL", TEST_TARGET_URL);
 properties.setProperty("sitemapFile", TEST_SIMAP_FILE);
 File propertyFile = new File(TEST_PROPERTY_FILE);
 FileInputStream fis = new FileInputStream(propertyFile);
 properties.load(fis);
 fis.close();

 LagoonCLI.main(new String[]{TEST_SIMAP_FILE.toString()});
 fail("Expected LagoonException but did not occur");
 }

 @org.junit.Test(expected = AuthenticationException.class)
 public void testIncorrectPassword() throws Exception {
 Properties properties = new Properties();
 properties.setProperty("targetURL", TEST_TARGET_URL);
 properties.setProperty("sitemapFile", TEST_SIMAP_FILE);
 properties.setProperty("password", "incorrectPass");
 File propertyFile = new File(TEST_PROPERTY_FILE);
 FileInputStream fis = new FileInputStream(propertyFile);
 properties.load(fis);
 fis.close();

 LagoonCLI.main(new String[]{TEST_PROPERTY_FILE.toString()});
 fail("Expected AuthenticationException but did not occur");
 }

 @org.junit.Test(expected = FileNotFoundException.class)
 public void testFileNotFound() throws Exception {
 LagoonCLI.main(new String[]{TEST_SIMAP_FILE.toString(), "build"});
 FileNotFoundException fileNotFoundException = assertThrows(FileNotFoundException.class, () -> new File(TEST_SIMAP_FILE.toString()).exists());
 assertEquals("Sitemap file not found: " + TEST_SIMAP_FILE, fileNotFoundException.getMessage());
 }

 @org.junit.Test
 public void testBuild() throws Exception {
 File sitemapFile = new File(TEST_SIMAP_FILE);
 Element sitemapTree = TreeBuilder.parseXML(TreeBuilder.fileToInputSource(sitemapFile), false);
 LagoonProcessor processor = new LagoonProcessor(TEST_TARGET_URL, sitemapTree, new File("testSourceDir"), null, new PrintWriter(System.out, true), new PrintWriter(System.err, true));
 assertTrue(processor.build(false));
 }

 @org.junit.Test
 public void testForceBuild() throws Exception {
 File sitemapFile = new File(TEST_SIMAP_FILE);
 Element sitemapTree = TreeBuilder.parseXML(TreeBuilder.fileToInputSource(sitemapFile), false);
 LagoonProcessor processor = new LagoonProcessor(TEST_TARGET_URL, sitemapTree, new File("testSourceDir"), null, new PrintWriter(System.out, true), new PrintWriter(System.err, true));
 assertTrue(processor.build(true));
 }

 @org.junit.Test
 public void testInterval() throws Exception, InterruptedException {
 File sitemapFile = new File(TEST_SIMAP_FILE);
 Element sitemapTree = TreeBuilder.parseXML(TreeBuilder.fileToInputSource(sitemapFile), false);
 LagoonProcessor processor = new LagoonProcessor(TEST_TARGET_URL, sitemapTree, new File("testSourceDir"), null, new PrintWriter(System.out, true), new PrintWriter(System.err, true));
 long timeBefore = System.currentTimeMillis();
 long timeElapsed;
 processor.build(false);
 timeElapsed = System.currentTimeMillis() - timeBefore;
 showTime(timeElapsed);
 Thread.sleep(500);
 timeBefore = System.currentTimeMillis();
 processor.build(false);
 timeElapsed = System.currentTimeMillis() - timeBefore;
 showTime(timeElapsed);
 assertTrue(timeElapsed < 500);
 }

 @org.junit.Test
 public void testManualInput() throws IOException, InterruptedException {
 File sitemapFile = new File(TEST_SIMAP_FILE);
 Element sitemapTree = TreeBuilder.parseXML(TreeBuilder.fileToInputSource(sitemapFile), false);
 LagoonProcessor processor = new LagoonProcessor(TEST_TARGET_URL, sitemapTree, new File("testSourceDir"), null, new BufferedReader(new InputStreamReader(System.in)), new PrintWriter(System.out, true), new PrintWriter(System.err, true));
 BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
 String input;
 while ((input = in.readLine()) != null) {
 if (input.equalsIgnoreCase("b")) {
 processor.build(false);
 }
 else if (input.equalsIgnoreCase("f")) {
 processor.build(true);
 }
 else if (input.equalsIgnoreCase("q")) {
 break;
 }
 }
 }

 private static void showTime(long ms) {
 if (ms < 10000L) {
 System.out.println("in " + ms + " ms");
 }
 else {
 System.out.println("in " + ms / 1000L + " s");
 }
 }
}