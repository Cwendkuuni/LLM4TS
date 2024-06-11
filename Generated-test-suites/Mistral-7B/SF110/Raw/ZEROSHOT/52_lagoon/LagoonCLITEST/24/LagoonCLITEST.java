package nu.staldal.lagoon.test;

import static org.junit.Assert.*;
import org.xml.sax.SAXException;
import static nu.staldal.lagoon.LagoonCLI.*;
import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class LagoonCLITest {

 @Test
 public void testValidArguments() throws Exception {
 Properties props = new Properties();
 File propertyFile = new File("testPropertyFile.xml");
 String[] args = new String[]{"propertyFile.xml"};
 LagoonCLI.main(args);
 assertTrue(System.out.contains("Lagoon initialized successfully"));
 }

 @Test(expected = Exception.class)
 public void testInvalidSyntax() throws Exception {
 String[] args = new String[]{"testPropertyFile.xml", "invalidArg"};
 LagoonCLI.main(args);
 fail("Exception expected but not thrown");
 }

 @Test(expected = NumberFormatException.class)
 public void testInvalidInterval() throws Exception {
 String[] args = new String[]{"propertyFile.xml", "-1"};
 LagoonCLI.main(args);
 fail("NumberFormatException expected but not thrown");
 }

 @Test(expected = LagoonException.class)
 public void testMissingPropertyFile() throws Exception {
 String[] args = new String[]{"missingPropertyFile.xml"};
 LagoonCLI.main(args);
 fail("LagoonException expected but not thrown");
 }

 @Test(expected = IOException.class)
 public void testFileNotFound() throws Exception {
 String[] args = new String[]{"nonexistentFile.xml"};
 LagoonCLI.main(args);
 fail("IOException expected but not thrown");
 }

 @Test(expected = AuthenticationMissingException.class)
 public void testMissingPassword() throws Exception {
 File sitemapFile = new File("testSitemapFile.xml");
 String[] args = new String[]{"sitemapFile.xml"};
 LagoonCLI.main(args);
 fail("AuthenticationMissingException expected but not thrown");
 }

 @Test(expected = AuthenticationException.class)
 public void testIncorrectPassword() throws Exception {
 Properties props = new Properties();
 props.setProperty("password", "wrongPassword");
 File sitemapFile = new File("testSitemapFile.xml");
 String[] args = new String[]{"sitemapFile.xml"};
 System.setProperty("user.dir", System.getProperty("user.dir") + File.separator + "test");
 LagoonCLI.main(args);
 fail("AuthenticationException expected but not thrown");
 }

 @Test
 public void testBuild() throws Exception {
 Properties props = new Properties();
 props.setProperty("targetURL", "http://localhost:8080/");
 props.setProperty("sitemapFile", "testSitemapFile.xml");
 props.setProperty("sourceDir", "testSourceDir");
 File sitemapFile = new File("testSitemapFile.xml");
 String[] args = new String[]{"propertyFile.xml", "build"};
 System.setProperty("user.dir", System.getProperty("user.dir") + File.separator + "test");
 LagoonCLI.main(args);
 assertTrue(System.out.contains("Building website..."));
 }

 @Test
 public void testForceBuild() throws Exception {
 Properties props = new Properties();
 props.setProperty("targetURL", "http://localhost:8080/");
 props.setProperty("sitemapFile", "testSitemapFile.xml");
 props.setProperty("sourceDir", "testSourceDir");
 File sitemapFile = new File("testSitemapFile.xml");
 String[] args = new String[]{"propertyFile.xml", "force"};
 System.setProperty("user.dir", System.getProperty("user.dir") + File.separator + "test");
 LagoonCLI.main(args);
 assertTrue(System.out.contains("Force building website..."));
 }

 @Test
 public void testInterval() throws Exception {
 Properties props = new Properties();
 props.setProperty("targetURL", "http://localhost:8080/");
 props.setProperty("sitemapFile", "testSitemapFile.xml");
 props.setProperty("sourceDir", "testSourceDir");
 props.setProperty("interval", "5000");
 File sitemapFile = new File("testSitemapFile.xml");
 String[] args = new String[]{"propertyFile.xml"};
 System.setProperty("user.dir", System.getProperty("user.dir") + File.separator + "test");
 LagoonCLI.main(args);
 assertTrue(System.out.contains("Building website..."));
 Thread.sleep(6000);
 assertTrue(System.out.contains("Building website..."));
 }
}