package nu.staldal.lagoon.test;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import nu.staldal.lagoon.LagoonCLI;
import nu.staldal.lagoon.LagoonException;
import nu.staldal.lagoon.core.AuthenticationException;
import nu.staldal.lagoon.core.AuthenticationMissingException;
import nu.staldal.lagoon.core.LagoonProcessor;
import nu.staldal.lagoon.core.PropertyManager;
import nu.staldal.xtree.TreeBuilder;
import nu.staldal.xtree.XMLInputSource;

public class LagoonCLITest {

 private static final String TEST_PROPERTY_FILE = "src/test/resources/test.properties";
 private static final String TEST_SIMAP_FILE = "src/test/resources/test.sitemap.xml";
 private static final String TEST_TARGET_URL = "http://test.com";
 private Properties testProperties;
 private LagoonCLI testLagoonCLI;

 @Before
 public void setUp() throws Exception {
 testProperties = new Properties();
 testProperties.load(new FileInputStream(TEST_PROPERTY_FILE));
 testLagoonCLI = new LagoonCLI();
 }

 @Test
 public void testInvalidArgumentCount() {
 testLagoonCLI.main(new String[0]);
 assertEquals("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]", System.out.getStackTrace()[0].getMessage());
 }

 @Test
 public void testInvalidArgumentFormat() {
 testLagoonCLI.main(new String[]{"test.properties", "invalid"});
 assertEquals("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]", System.out.getStackTrace()[0].getMessage());
 }

 @Test
 public void testInvalidInterval() {
 testLagoonCLI.main(new String[]{"test.properties", "-500"});
 assertEquals("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]", System.out.getStackTrace()[0].getMessage());
 }

 @Test
 public void testValidArgumentFormat() {
 String[] args = new String[]{"test.properties", "1000"};
 testLagoonCLI.main(args);
 assertEquals("Initializing Lagoon...", System.out.getHistory(0).getSuffix().trim());
 }

 @Test(expected = AuthenticationMissingException.class)
 public void testMissingPassword() throws Exception {
 testLagoonCLI.main(new String[]{"test.xml"});
 fail("Expected AuthenticationMissingException");
 }

 @Test(expected = AuthenticationException.class)
 public void testInvalidPassword() throws Exception {
 testProperties.setProperty("password", "wrongpassword");
 testLagoonCLI.main(new String[]{"test.properties"});
 fail("Expected AuthenticationException");
 }

 @Test(expected = FileNotFoundException.class)
 public void testNonExistentPropertyFile() throws Exception {
 testLagoonCLI.main(new String[]{"non_existent_file.properties"});
 fail("Expected FileNotFoundException");
 }

 @Test(expected = FileNotFoundException.class)
 public void testNonExistentSitemapFile() throws Exception {
 testLagoonCLI.main(new String[]{"test.properties", "non_existent_file.xml"});
 fail("Expected FileNotFoundException");
 }

 @Test(expected = IOException.class)
 public void testIOException() throws Exception {
 TreeBuilder treeBuilder = new TreeBuilder();
 XMLInputSource xmlInputSource = new XMLInputSource(new FileInputStream(TEST_SIMAP_FILE));
 treeBuilder.parseXML(xmlInputSource, false);
 File sitemapFile = new File("src/test/resources/invalid_sitemap.xml");
 testLagoonCLI.main(new String[]{"test.properties", "build", sitemapFile.getAbsolutePath()});
 fail("Expected IOException");
 }

 @Test
 public void testValidArgumentFormatWithBuild() throws Exception {
 testLagoonCLI.main(new String[]{"test.properties", "build"});
 String targetURL = testProperties.getProperty("targetURL");
 assertEquals(targetURL, TEST_TARGET_URL);
 }

 @Test
 public void testValidArgumentFormatWithForce() throws Exception {
 testLagoonCLI.main(new String[]{"test.properties", "force"});
 String targetURL = testProperties.getProperty("targetURL");
 assertEquals(targetURL, TEST_TARGET_URL);
 }

 @Test
 public void testValidArgumentFormatWithInterval() throws Exception {
 testLagoonCLI.main(new String[]{"test.properties", "5000"});
 String targetURL = testProperties.getProperty("targetURL");
 assertEquals(targetURL, TEST_TARGET_URL);
 }

 @Test
 public void testBuild() throws Exception {
 testProperties.setProperty("interval", "0");
 testLagoonCLI.main(new String[]{"test.properties", "build"});
 LagoonProcessor processor = new LagoonProcessor(TEST_TARGET_URL, null, null, null, null, null);
 assertTrue(processor.isBuilt());
 }

 @Test
 public void testBuildWithErrors() throws Exception {
 testProperties.setProperty("interval", "0");
 File errorFile = new File("src/test/resources/error.txt");
 System.setErr(new PrintStream(errorFile));
 testLagoonCLI.main(new String[]{"test.properties", "build"});
 assertTrue(errorFile.exists());
 System.setErr(System.out);
 }

 @Test
 public void testForceBuild() throws Exception {
 testProperties.setProperty("interval", "0");
 testLagoonCLI.main(new String[]{"test.properties", "force"});
 LagoonProcessor processor = new LagoonProcessor(TEST_TARGET_URL, null, null, null, null, null);
 assertTrue(processor.isBuilt());
 }

 @Test
 public void testForceBuildWithErrors() throws Exception {
 testProperties.setProperty("interval", "0");
 File errorFile = new File("src/test/resources/error.txt");
 System.setErr(new PrintStream(errorFile));
 testLagoonCLI.main(new String[]{"test.properties", "force"});
 assertTrue(errorFile.exists());
 System.setErr(System.out);
 }

 @Test
 public void testInterval() throws Exception {
 testProperties.setProperty("interval", "1000");
 testLagoonCLI.main(new String[]{"test.properties"});
 Thread.sleep(2000);
 testLagoonCLI.main(new String[]{"test.properties"});
 Thread.sleep(3000);
 testLagoonCLI.main(new String[]{"test.properties"});
 }

 @Test
 public void testInvalidCharacterInput() {
 testLagoonCLI.main(new String[]{"test.properties", "qwe@123"});
 assertEquals("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]", System.out.getStackTrace()[0].getMessage());
 }

 @Test
 public void testQuit() throws Exception {
 testLagoonCLI.main(new String[]{"test.properties"});
 BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
 in.readLine();
 in.readLine();
 in.readLine();
 in.readLine();
 in.readLine();
 in.readLine();
 in.readLine();
 in.readLine();
 in.readLine();
 in.close();
 String targetURL = testProperties.getProperty("targetURL");
 assertEquals(targetURL, TEST_TARGET_URL);
 }

}