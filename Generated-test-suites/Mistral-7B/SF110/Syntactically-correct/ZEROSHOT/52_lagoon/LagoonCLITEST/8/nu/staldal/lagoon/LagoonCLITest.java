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

public class LagoonCLITest {

 private static final String TEST_PROPERTY_FILE = "testPropertyFile.xml";
 private static final String TEST_SIMAP_FILE = "testSitemapFile.xml";
 private static final String TEST_TARGET_URL = "http://test.com";
 private static final String TEST_SOURCE_DIR = "testSourceDir";
 private static final String TEST_PASSWORD = "testPassword";

 @org.junit.Test
 public void testValidArguments() throws Exception {
 LagoonCLI.main(new String[]{"testPropertyFile.xml"});
 }

 @org.junit.Test(expected = LagoonException.class)
 public void testMissingArgument() throws Exception {
 LagoonCLI.main(new String[0]);
 }

 @org.junit.Test(expected = LagoonException.class)
 public void testInvalidArgumentBuild() throws Exception {
 LagoonCLI.main(new String[]{"testPropertyFile.xml", "invalid"});
 }

 @org.junit.Test(expected = LagoonException.class)
 public void testInvalidArgumentForce() throws Exception {
 LagoonCLI.main(new String[]{"testPropertyFile.xml", "force"});
 }

 @org.junit.Test(expected = NumberFormatException.class)
 public void testInvalidArgumentInterval() throws Exception {
 LagoonCLI.main(new String[]{"testPropertyFile.xml", "-1"});
 }

 @org.junit.Test(expected = LagoonException.class)
 public void testMissingPropertyFile() throws Exception {
 LagoonCLI.main(new String[]{"testPropertyFile.xml", "build"});
 File testPropertyFile = new File(TEST_PROPERTY_FILE);
 testPropertyFile.delete(); // Delete the file to make it missing
 LagoonCLI.main(new String[]{"testPropertyFile.xml", "build"});
 }

 @org.junit.Test
 public void testValidPropertyFile() throws Exception {
 Properties properties = new Properties();
 File testPropertyFile = new File(TEST_PROPERTY_FILE);
 properties.load(new FileInputStream(testPropertyFile));
 LagoonCLI.properties = properties;
 LagoonCLI.main(new String[]{"testPropertyFile.xml", "build"});
 }

 @org.junit.Test
 public void testValidSitemapFile() throws Exception {
 File testSitemapFile = new File(TEST_SIMAP_FILE);
 LagoonCLI.main(new String[]{"testPropertyFile.xml", TEST_SIMAP_FILE, "build"});
 Element sitemapTree = TreeBuilder.parseXML(TreeBuilder.fileToInputSource(testSitemapFile), false);
 LagoonProcessor processor = new LagoonProcessor(TEST_TARGET_URL, sitemapTree, new File(TEST_SOURCE_DIR), TEST_PASSWORD, new PrintWriter(System.out, true), new PrintWriter(System.err, true));
 assertTrue(processor.build(false));
 testSitemapFile.delete(); // Delete the file to make it missing
 }

 @org.junit.Test(expected = FileNotFoundException.class)
 public void testMissingSitemapFile() throws Exception {
 LagoonCLI.main(new String[]{"testPropertyFile.xml", TEST_SIMAP_FILE, "build"});
 File testSitemapFile = new File(TEST_SIMAP_FILE);
 testSitemapFile.createNewFile(); // Create the file to make it exist
 LagoonCLI.main(new String[]{"testPropertyFile.xml", TEST_SIMAP_FILE, "build"});
 testSitemapFile.delete(); // Delete the file to make it missing
 }

 @org.junit.Test(expected = IOException.class)
 public void testMissingPassword() throws Exception {
 LagoonCLI.main(new String[]{"testPropertyFile.xml", TEST_SIMAP_FILE});
 }

 @org.junit.Test(expected = AuthenticationException.class)
 public void testIncorrectPassword() throws Exception {
 Properties properties = new Properties();
 properties.setProperty("password", "incorrectPassword");
 LagoonCLI.properties = properties;
 LagoonCLI.main(new String[]{"testPropertyFile.xml", TEST_SIMAP_FILE});
 }

 @org.junit.Test
 public void testBuildWithoutSitemap() throws Exception {
 LagoonCLI.main(new String[]{"testPropertyFile.xml", "build"});
 }

 @org.junit.Test
 public void testBuildWithSitemap() throws Exception {
 LagoonCLI.main(new String[]{"testPropertyFile.xml", TEST_SIMAP_FILE, "build"});
 }

 @org.junit.Test
 public void testBuildWithInterval() throws Exception {
 LagoonCLI.main(new String[]{"testPropertyFile.xml", "1000", "build"});
 }

 @org.junit.Test
 public void testBuildWithIntervalNegative() throws Exception {
 LagoonCLI.main(new String[]{"testPropertyFile.xml", "-1000", "build"});
 }

 @org.junit.Test
 public void testForce() throws Exception {
 LagoonCLI.main(new String[]{"testPropertyFile.xml", TEST_SIMAP_File, "force"});
 }

 @org.junit.Test
 public void testInterval() throws Exception {
 LagoonCLI.main(new String[]{"testPropertyFile.xml", "5000", "build"});
 Thread.sleep(5500);
 LagoonCLI.main(new String[]{"testPropertyFile.xml", "build"});
 }

 @org.junit.Test
 public void testValidArgumentsWithDebug() throws Exception {
 LagoonCLI.DEBUG = true;
 LagoonCLI.main(new String[]{"testPropertyFile.xml", "build"});
 }

 @org.junit.Test(expected = SAXException.class)
 public void testInvalidSitemapFile() throws Exception {
 File testSitemapFile = new File(TEST_SIMAP_FILE);
 testSitemapFile.createNewFile(); // Create the file to make it exist
 LagoonCLI.main(new String[]{"testPropertyFile.xml", TEST_SIMAP_FILE, "build"});
 testSitemapFile.delete(); // Delete the file to make it missing
 }
}