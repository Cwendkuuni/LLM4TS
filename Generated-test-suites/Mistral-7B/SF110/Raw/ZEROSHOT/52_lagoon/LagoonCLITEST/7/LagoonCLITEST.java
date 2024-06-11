package nu.staldal.lagoon.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import nu.staldal.lagoon.LagoonCLI;
import nu.staldal.lagoon.LagoonException;
import nu.staldal.lagoon.core.LagoonProcessor;
import nu.staldal.xtree.TreeBuilder;

public class LagoonCLITest {

 private Properties properties;
 private File sitemapFile;
 private LagoonCLI lagoonCLI;

 @Before
 public void setUp() {
 properties = new Properties();
 sitemapFile = new File("src/test/resources/test.sitemap.xml");
 }

 @Test
 public void testInvalidArguments() {
 LagoonCLI.main(new String[0]);
 assertEquals("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]", System.out.getErrorStream().toString());
 }

 @Test
 public void testPropertyFileArgument() throws LagoonException, IOException {
 properties.setProperty("targetURL", "http://localhost:8080");
 properties.setProperty("sitemapFile", sitemapFile.getAbsolutePath());
 properties.setProperty("sourceDir", "target");

 LagoonCLI.main(new String[]{"src/test/resources/test.properties"});
 assertEquals("Lagoon initialized successfully", System.out.getErrorStream().toString());
 }

 @Test(expected = LagoonException.class)
 public void testMissingPasswordProperty() throws LagoonException, IOException {
 properties.setProperty("targetURL", "http://localhost:8080");
 properties.setProperty("sitemapFile", sitemapFile.getAbsolutePath());

 LagoonCLI.main(new String[]{"src/test/resources/test.missingPassword.properties"});
 fail("Should throw LagoonException: Password is required but not specified");
 }

 @Test(expected = LagoonException.class)
 public void testIncorrectPassword() throws LagoonException, IOException {
 properties.setProperty("targetURL", "http://localhost:8080");
 properties.setProperty("sitemapFile", sitemapFile.getAbsolutePath());
 properties.setProperty("password", "incorrect");

 LagoonCLI.main(new String[]{"src/test/resources/test.properties"});
 fail("Should throw LagoonException: Incorrect password");
 }

 @Test(expected = FileNotFoundException.class)
 public void testMissingSitemapFile() throws LagoonException, IOException {
 properties.setProperty("targetURL", "http://localhost:8080");
 properties.setProperty("sitemapFile", "missingfile.xml");

 LagoonCLI.main(new String[]{"src/test/resources/test.properties"});
 fail("Should throw FileNotFoundException: File not found: missingfile.xml");
 }

 @Test(expected = IOException.class)
 public void testInputFileReadError() throws LagoonException, IOException {
 properties.setProperty("targetURL", "http://localhost:8080");
 properties.setProperty("sitemapFile", "inputFilereaderror.xml");

 IOException exception = null;
 try {
 LagoonCLI.main(new String[]{"src/test/resources/test.properties"});
 }
 catch (IOException e) {
 exception = e;
 }
 assertTrue("Should throw IOException: I/O error: ", exception != null);
 }

 @Test
 public void testBuildMode() throws LagoonException, IOException {
 properties.setProperty("targetURL", "http://localhost:8080");
 properties.setProperty("sitemapFile", sitemapFile.getAbsolutePath());
 properties.setProperty("password", "correct");

 LagoonCLI.main(new String[]{"src/test/resources/test.properties", "build"});
 assertEquals("Building website...", System.out.getErrorStream().toString().substring(0, 17));
 }

 @Test
 public void testForceBuildMode() throws LagoonException, IOException {
 properties.setProperty("targetURL", "http://localhost:8080");
 properties.setProperty("sitemapFile", sitemapFile.getAbsolutePath());
 properties.setProperty("password", "correct");

 LagoonCLI.main(new String[]{"src/test/resources/test.properties", "force"});
 assertEquals("Force building website...", System.out.getErrorStream().toString().substring(0, 19));
 }

 @Test
 public void testIntervalMode() throws LagoonException, IOException, InterruptedException {
 properties.setProperty("targetURL", "http://localhost:8080");
 properties.setProperty("sitemapFile", sitemapFile.getAbsolutePath());
 properties.setProperty("password", "correct");
 properties.setProperty("interval", "5000");

 lagoonCLI = new LagoonCLI();
 lagoonCLI.main(new String[]{"src/test/resources/test.properties"});
 assertEquals("Lagoon initialized successfully", System.out.getErrorStream().toString());

 Thread.sleep(1000);
 assertTrue("Should build website at least once", new File("target").exists());

 Thread.sleep(5001);
 Thread.sleep(1);

 LagoonProcessor processor = lagoonCLI.getProcessor();
 processor.build(false);
 Thread.sleep(1);

 assertTrue("Should build website at least twice", new File("target").exists() && new File("target").listFiles().length >= 2);
 }
}