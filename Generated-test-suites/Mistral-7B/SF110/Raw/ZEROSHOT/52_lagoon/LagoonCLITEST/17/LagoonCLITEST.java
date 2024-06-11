package nu.staldal.lagoon.test;

import org.junit.Test;
import static org.junit.Assert.*;
import nu.staldal.lagoon.*;

public class LagoonCLITest {

 @Test
 public void testInvalidSyntax() {
 String[] args = new String[0];
 LagoonCLI.main(args);
 String expected = "Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]";
 String actual = System.out.getFileName();
 assertEquals(expected, actual);
 }

 @Test
 public void testMissingPropertyFile() {
 String[] args = new String[1];
 args[0] = "missingFile.txt";
 LagoonCLI.main(args);
 String expected = "Error while initializing Lagoon:\nFile not found: missingFile.txt";
 String actual = System.err.getFileName();
 assertEquals(expected, actual);
 }

 @Test
 public void testInvalidArgument() {
 String[] args = new String[2];
 args[0] = "propertyFile.xml";
 args[1] = "invalidArg";
 LagoonCLI.main(args);
 String expected = "Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]";
 String actual = System.out.getFileName();
 assertEquals(expected, actual);
 }

 @Test
 public void testInvalidInterval() {
 String[] args = new String[3];
 args[0] = "propertyFile.xml";
 args[1] = "invalidInterval";
 LagoonCLI.main(args);
 String expected = "Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]";
 String actual = System.out.getFileName();
 assertEquals(expected, actual);
 }

 @Test
 public void testNegativeInterval() {
 String[] args = new String[3];
 args[0] = "propertyFile.xml";
 args[1] = "-1000";
 LagoonCLI.main(args);
 String expected = "Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]";
 String actual = System.out.getFileName();
 assertEquals(expected, actual);
 }

 @Test
 public void testPropertyFileParsing() throws Exception {
 String[] args = new String[2];
 args[0] = "testPropertyFile.xml";
 LagoonCLI.main(args);
 String targetURL = LagoonCLI.getProperty("targetURL");
 String sitemapFile = LagoonCLI.getProperty("sitemapFile");
 String sourceDir = LagoonCLI.getProperty("sourceDir");
 String password = LagoonCLI.getProperty("password");
 String expectedTargetURL = "http://www.example.com";
 String expectedSitemapFile = "src/test/resources/testSitemap.xml";
 String expectedSourceDir = "src/test/output";
 String expectedPassword = "testPassword";
 assertEquals(expectedTargetURL, targetURL);
 assertEquals(expectedSitemapFile, sitemapFile);
 assertEquals(expectedSourceDir, sourceDir);
 assertEquals(expectedPassword, password);
 }

 @Test
 public void testSitemapFileParsing() throws Exception {
 String[] args = new String[3];
 args[0] = "propertyFile.xml";
 args[1] = "1000";
 args[2] = "sitemapFile.xml";
 LagoonCLI.main(args);
 Element sitemapTree = LagoonCLI.getSitemapTree();
 int expectedNumberOfUrls = 5;
 int actualNumberOfUrls = sitemapTree.getChildNodes().getLength();
 assertEquals(expectedNumberOfUrls, actualNumberOfUrls);
 }

 @Test(expected = AuthenticationMissingException.class)
 public void testMissingPassword() throws Exception {
 String[] args = new String[3];
 args[0] = "propertyFile.xml";
 args[1] = "1000";
 args[2] = "sitemapFile.xml";
 LagoonCLI.main(args);
 }

 @Test(expected = AuthenticationException.class)
 public void testIncorrectPassword() throws Exception {
 String[] args = new String[4];
 args[0] = "propertyFile.xml";
 args[1] = "1000";
 args[2] = "sitemapFile.xml";
 args[3] = "incorrectPassword";
 LagoonCLI.main(args);
 }

 @Test
 public void testBuildingWebsite() throws Exception {
 String[] args = new String[3];
 args[0] = "propertyFile.xml";
 args[1] = "1000";
 args[2] = "sitemapFile.xml";
 LagoonCLI.main(args);
 boolean built = LagoonCLI.isWebsiteBuilt();
 assertTrue(built);
 }

 @Test
 public void testForceBuildingWebsite() throws Exception {
 String[] args = new String[4];
 args[0] = "propertyFile.xml";
 args[1] = "1000";
 args[2] = "sitemapFile.xml";
 args[3] = "force";
 LagoonCLI.main(args);
 boolean built = LagoonCLI.isWebsiteBuilt();
 assertTrue(built);
 }

 @Test
 public void testPeriodicBuildingWebsite() throws Exception {
 String[] args = new String[4];
 args[0] = "propertyFile.xml";
 args[1] = "1000";
 args[2] = "sitemapFile.xml";
 args[3] = "build";
 LagoonCLI.main(args);
 Thread.sleep(5000);
 boolean built = LagoonCLI.isWebsiteBuilt();
 assertTrue(built);
 }
}