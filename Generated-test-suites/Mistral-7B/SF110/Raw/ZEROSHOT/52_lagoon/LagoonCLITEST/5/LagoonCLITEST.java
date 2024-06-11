package nu.staldal.lagoon.test;

import static org.junit.Assert.*;
import nu.staldal.lagoon.*;
import org.xml.sax.SAXException;
import org.junit.Before;
import org.junit.Test;
import java.io.File;
import java.io.IOException;

public class LagoonCLITest {

 private LagoonCLI lagoonCLI;
 private Properties properties;
 private File propertyFile;
 private File sitemapFile;

 @Before
 public void setUp() {
 properties = new Properties();
 propertyFile = new File("testPropertyFile.xml");
 sitemapFile = new File("testSitemapFile.xml");
 try {
 properties.load(new FileInputStream("testPropertyFile.xml"));
 } catch (FileNotFoundException e) {
 try {
 properties.load(new FileInputStream("src/test/resources/testPropertyFile.xml"));
 } catch (FileNotFoundException ex) {
 fail("Test property file not found");
 }
 }
 lagoonCLI = new LagoonCLI();
 }

 @Test
 public void testInvalidSyntax() {
 lagoonCLI.main(new String[0]);
 String expected = "Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]";
 String actual = System.out.getErrorStream().asString();
 assertEquals(expected, actual);
 }

 @Test
 public void testValidSyntaxWithPropertyFile() {
 String[] args = new String[]{"testPropertyFile.xml"};
 lagoonCLI.main(args);
 String expected = "Initializing Lagoon...";
 String actual = System.out.getBuffer().toString();
 assertTrue(actual.contains(expected));
 }

 @Test(expected = LagoonException.class)
 public void testInvalidPropertyFile() {
 propertyFile = new File("nonexistentfile.xml");
 String[] args = new String[]{propertyFile.getAbsolutePath()};
 lagoonCLI.main(args);
 }

 @Test(expected = LagoonException.class)
 public void testMissingPasswordProperty() {
 properties.setProperty("targetURL", "http://localhost");
 properties.setProperty("sitemapFile", sitemapFile.getAbsolutePath());
 String[] args = new String[]{propertyFile.getAbsolutePath()};
 lagoonCLI.main(args);
 }

 @Test
 public void testValidSyntaxWithSitemapFile() {
 String[] args = new String[]{sitemapFile.getAbsolutePath()};
 lagoonCLI.main(args);
 String expected = "Initializing Lagoon...";
 String actual = System.out.getBuffer().toString();
 assertTrue(actual.contains(expected));
 }

 @Test(expected = NumberFormatException.class)
 public void testInvalidInterval() {
 properties.setProperty("targetURL", "http://localhost");
 properties.setProperty("sitemapFile", sitemapFile.getAbsolutePath());
 String[] args = new String[]{propertyFile.getAbsolutePath(), "abc"};
 lagoonCLI.main(args);
 }

 @Test
 public void testBuildWithSuccess() {
 properties.setProperty("targetURL", "http://localhost");
 properties.setProperty("sitemapFile", sitemapFile.getAbsolutePath());
 String[] args = new String[]{propertyFile.getAbsolutePath(), "build"};
 lagoonCLI.main(args);
 String expected = "Building website...";
 String actual = System.out.getBuffer().toString();
 assertTrue(actual.contains(expected));
 }

 @Test(expected = LagoonException.class)
 public void testForceBuildWithFailure() {
 properties.setProperty("targetURL", "http://localhost");
 properties.setProperty("sitemapFile", sitemapFile.getAbsolutePath());
 String[] args = new String[]{propertyFile.getAbsolutePath(), "force"};
 try {
 lagoonCLI.main(args);
 fail("Force build should fail");
 } catch (LagoonException e) {
 // expected
 }
 }

 @Test
 public void testIntervalWithSuccess() {
 properties.setProperty("targetURL", "http://localhost");
 properties.setProperty("sitemapFile", sitemapFile.getAbsolutePath());
 properties.setProperty("interval", "1000");
 String[] args = new String[]{propertyFile.getAbsolutePath()};
 lagoonCLI.main(args);
 try {
 Thread.sleep(2000);
 } catch (InterruptedException e) {
 // expected
 }
 String expected = "Building website...";
 String actual = System.out.getBuffer().toString();
 assertTrue(actual.contains(expected));
 try {
 Thread.sleep(5000);
 } catch (InterruptedException e) {
 // expected
 }
 String expectedTimeElapsed = "in 5 s";
 String actualTimeElapsed = System.out.getBuffer().toString().split(expected)[1];
 assertEquals(expectedTimeElapsed, actualTimeElapsed);
 }

 @Test
 public void testBuildWithErrors() {
 properties.setProperty("targetURL", "http://localhost");
 properties.setProperty("sitemapFile", sitemapFile.getAbsolutePath());
 String[] args = new String[]{propertyFile.getAbsolutePath(), "build"};
 String expectedErrorMessage = "Error while building website.";
 try {
 lagoonCLI.main(args);
 fail("Build with errors should fail");
 } catch (IOException e) {
 String actualErrorMessage = e.getMessage();
 assertEquals(expectedErrorMessage, actualErrorMessage);
 }
 }

 @Test
 public void testInvalidPassword() {
 properties.setProperty("targetURL", "http://localhost");
 properties.setProperty("sitemapFile", sitemapFile.getAbsolutePath());
 properties.setProperty("password", "wrongpassword");
 String[] args = new String[]{propertyFile.getAbsolutePath()};
 try {
 lagoonCLI.main(args);
 fail("Invalid password should fail");
 } catch (LagoonException e) {
 // expected
 }
 }

 @Test
 public void testValidPassword() {
 properties.setProperty("targetURL", "http://localhost");
 properties.setProperty("sitemapFile", sitemapFile.getAbsolutePath());
 properties.setProperty("password", "correctpassword");
 String[] args = new String[]{propertyFile.getAbsolutePath()};
 try {
 lagoonCLI.main(args);
 } catch (IOException e) {
 e.printStackTrace();
 fail("Build with valid password should succeed");
 }
 }

 @Test(expected = SAXException.class)
 public void testInvalidSitemapFile() {
 properties.setProperty("targetURL", "http://localhost");
 properties.setProperty("sitemapFile", new File("testSitemapFileInvalid.xml").getAbsolutePath());
 String[] args = new String[]{propertyFile.getAbsolutePath()};
 lagoonCLI.main(args);
 }

}