package nu.staldal.lagoon;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class LagoonCLITest {

 private static final String VALID_PROPERTY_FILE = "testData/validPropertyFile.xml";
 private static final String VALID_SIMAP_FILE = "testData/validSitemap.xml";
 private static final String INVALID_PROPERTY_FILE = "testData/invalidPropertyFile.xml";
 private static final String INVALID_SIMAP_FILE = "testData/invalidSitemap.xml";
 private static final String INVALID_ARGUMENT = "invalidArgument";
 private LagoonCLI lagoonCLI;

 @Before
 public void setUp() {
 lagoonCLI = new LagoonCLI();
 }

 @Test
 public void testMainWithValidPropertyFileAndNoArguments() {
 String[] args = new String[]{"testData/validPropertyFile.xml"};
 lagoonCLI.main(args);
 assertTrue(lagoonCLI.isInitialized());
 }

 @Test
 public void testMainWithValidPropertyFileAndBuildArgument() {
 String[] args = new String[]{"testData/validPropertyFile.xml", "build"};
 lagoonCLI.main(args);
 assertTrue(lagoonCLI.isInitialized());
 }

 @Test
 public void testMainWithValidPropertyFileAndIntervalArgument() {
 String[] args = new String[]{"testData/validPropertyFile.xml", "5000"};
 lagoonCLI.main(args);
 assertTrue(lagoonCLI.isInitialized());
 try {
 TimeUnit.MILLISECONDS.sleep(10001);
 }
 catch (InterruptedException e) {
 fail("Test should have exited after 5 seconds");
 }
 }

 @Test(expected = LagoonException.class)
 public void testMainWithInvalidPropertyFile() {
 String[] args = new String[]{"testData/invalidPropertyFile.xml"};
 lagoonCLI.main(args);
 fail("Expected LagoonException to be thrown");
 }

 @Test(expected = NumberFormatException.class)
 public void testMainWithInvalidIntervalArgument() {
 String[] args = new String[]{"testData/validPropertyFile.xml", "abc"};
 lagoonCLI.main(args);
 fail("Expected NumberFormatException to be thrown");
 }

 @Test
 public void testMainWithInvalidSitemapFile() {
 String[] args = new String[]{"testData/invalidSitemap.xml", "build"};
 File expectedExceptionFile = new File("testData/expectedException.txt");
 try {
 lagoonCLI.main(args);
 fail("Expected LagoonException to be thrown");
 }
 catch (LagoonException e) {
 File actualExceptionFile = new File("testData/actualException.txt");
 assertTrue(actualExceptionFile.exists());
 assertEquals(expectedExceptionFile.getAbsolutePath(), actualExceptionFile.getAbsolutePath());
 }
 }

 @Test(expected = AuthenticationMissingException.class)
 public void testMainWithMissingPassword() {
 String[] args = new String[]{"testData/missingPasswordPropertyFile.xml"};
 lagoonCLI.main(args);
 fail("Expected AuthenticationMissingException to be thrown");
 }

 @Test(expected = AuthenticationException.class)
 public void testMainWithIncorrectPassword() {
 String[] args = new String[]{"testData/incorrectPasswordPropertyFile.xml"};
 lagoonCLI.main(args);
 fail("Expected AuthenticationException to be thrown");
 }

 @Test(expected = FileNotFoundException.class)
 public void testMainWithMissingPropertyFile() {
 String[] args = new String[]{"nonexistentfile.xml"};
 lagoonCLI.main(args);
 fail("Expected FileNotFoundException to be thrown");
 }

 @Test(expected = IOException.class)
 public void testMainWithIOException() {
 String[] args = new String[]{"testData/validPropertyFile.xml", "ioException"};
 lagoonCLI.main(args);
 fail("Expected IOException to be thrown");
 }

 @Test
 public void testBuildWithValidSitemapFile() {
 Properties properties = new Properties();
 properties.setProperty("targetURL", "http://example.com");
 properties.setProperty("sitemapFile", "testData/validSitemap.xml");
 properties.setProperty("sourceDir", "target/classes");
 properties.setProperty("password", "testPassword");
 File sitemapFile = new File("testData/validSitemap.xml");
 LagoonCLI lagoonCLI = new LagoonCLI();
 lagoonCLI.main(new String[]{"testData/validPropertyFile.xml"});
 assertNotNull(lagoonCLI.getProcessor());
 lagoonCLI.getProcessor().build(false);
 }

 @Test
 public void testBuildWithInvalidSitemapFile() {
 Properties properties = new Properties();
 properties.setProperty("targetURL", "http://example.com");
 properties.setProperty("sitemapFile", "testData/invalidSitemap.xml");
 properties.setProperty("sourceDir", "target/classes");
 properties.setProperty("password", "testPassword");
 File sitemapFile = new File("testData/invalidSitemap.xml");
 LagoonCLI lagoonCLI = new LagoonCLI();
 lagoonCLI.main(new String[]{"testData/validPropertyFile.xml"});
 assertNotNull(lagoonCLI.getProcessor());
 assertFalse(lagoonCLI.getProcessor().build(false));
 }

 @Test
 public void testGetProperty() {
 Properties properties = new Properties();
 properties.setProperty("targetURL", "http://example.com");
 properties.setProperty("sitemapFile", "testData/validSitemap.xml");
 properties.setProperty("sourceDir", "target/classes");
 properties.setProperty("password", "testPassword");
 LagoonCLI lagoonCLI = new LagoonCLI();
 lagoonCLI.main(new String[]{"testData/validPropertyFile.xml"});
 String targetURL = lagoonCLI.getProperty("targetURL");
 String sitemapFile = lagoonCLI.getProperty("sitemapFile");
 String sourceDir = lagoonCLI.getProperty("sourceDir");
 String password = lagoonCLI.getProperty("password");
 assertEquals("http://example.com", targetURL);
 assertEquals("testData/validSitemap.xml", sitemapFile);
 assertEquals("target/classes", sourceDir);
 assertNull(lagoonCLI.getProperty("nonexistentProperty"));
 }

 @Test
 public void testShowTime() {
 long shortTime = 200L;
 long mediumTime = 2500L;
 long longTime = 10000L;
 lagoonCLI.showTime(shortTime);
 assertTrue(lagoonCLI.isShortTime(shortTime));
 lagoonCLI.showTime(mediumTime);
 assertFalse(lagoonCLI.isShortTime(mediumTime));
 lagoonCLI.showTime(longTime);
 assertFalse(lagoonCLI.isShortTime(longTime));
 }
}