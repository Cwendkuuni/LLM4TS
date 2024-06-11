package nu.staldal.lagoon.test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import nu.staldal.lagoon.*;
import nu.staldal.lagoon.core.*;
import nu.staldal.xtree.*;

public class LagoonCLITest {

 private LagoonCLI lagoonCLI;
 private Properties properties;
 private File sitemapFile;

 @Before
 public void setup() {
 properties = new Properties();
 sitemapFile = new File("src/test/resources/test.sitemap.xml");
 }

 @Test
 public void testInvalidArgs() {
 lagoonCLI = new LagoonCLI(new String[0]);
 assertFalse(lagoonCLI.isValid());
 }

 @Test
 public void testInvalidInterval() {
 lagoonCLI = new LagoonCLI(new String[]{"propertyFile.xml", "-1"});
 assertFalse(lagoonCLI.isValid());
 }

 @Test
 public void testValidArgs() {
 lagoonCLI = new LagoonCLI(new String[]{"propertyFile.xml", "100"});
 assertTrue(lagoonCLI.isValid());
 }

 @Test
 public void testBuild() throws IOException, SAXException, InterruptedException {
 properties.setProperty("targetURL", "http://localhost:8080");
 properties.setProperty("sitemapFile", sitemapFile.getAbsolutePath());
 properties.setProperty("sourceDir", "src/test/resources/output");

 lagoonCLI = new LagoonCLI(new String[]{"propertyFile.xml", "build"});
 assertNotNull(lagoonCLI.getProcessor());
 }

 @Test(expected = LagoonException.class)
 public void testMissingPassword() throws IOException, SAXException, InterruptedException {
 properties.setProperty("targetURL", "http://localhost:8080");
 properties.setProperty("sitemapFile", sitemapFile.getAbsolutePath());

 lagoonCLI = new LagoonCLI(new String[]{"propertyFile.xml"});
 fail("Expected LagoonException, but got: " + lagoonCLI.getMessage());
 }

 @Test(expected = AuthenticationException.class)
 public void testIncorrectPassword() throws IOException, SAXException, InterruptedException {
 properties.setProperty("targetURL", "http://localhost:8080");
 properties.setProperty("sitemapFile", sitemapFile.getAbsolutePath());
 properties.setProperty("password", "wrongPassword");

 lagoonCLI = new LagoonCLI(new String[]{"propertyFile.xml"});
 fail("Expected AuthenticationException, but got: " + lagoonCLI.getMessage());
 }

 @Test(expected = FileNotFoundException.class)
 public void testMissingPropertyFile() throws IOException, SAXException, InterruptedException {
 lagoonCLI = new LagoonCLI(new String[]{"nonExistentFile.xml", "build"});
 fail("Expected FileNotFoundException, but got: " + lagoonCLI.getMessage());
 }

 @Test(expected = IOException.class)
 public void testMissingSitemapFile() throws IOException, SAXException, InterruptedException {
 properties.setProperty("targetURL", "http://localhost:8080");
 properties.setProperty("propertyFile", "propertyFile.xml");

 lagoonCLI = new LagoonCLI(new String[]{"propertyFile.xml"});
 fail("Expected IOException, but got: " + lagoonCLI.getMessage());
 }

 @Test
 public void testInterval() throws IOException, SAXException, InterruptedException {
 properties.setProperty("targetURL", "http://localhost:8080");
 properties.setProperty("sitemapFile", sitemapFile.getAbsolutePath());
 properties.setProperty("interval", "2000");

 lagoonCLI = new LagoonCLI(new String[]{"propertyFile.xml", "interval"});
 assertEquals(2000, lagoonCLI.getInterval());
 }

 @Test
 public void testForce() throws IOException, SAXException, InterruptedException {
 properties.setProperty("targetURL", "http://localhost:8080");
 properties.setProperty("sitemapFile", sitemapFile.getAbsolutePath());

 lagoonCLI = new LagoonCLI(new String[]{"propertyFile.xml", "force"});
 assertTrue(lagoonCLI.isForce());
 }

 @Test
 public void testBuildWithProgress() throws IOException, SAXException, InterruptedException {
 properties.setProperty("targetURL", "http://localhost:8080");
 properties.setProperty("sitemapFile", sitemapFile.getAbsolutePath());
 properties.setProperty("sourceDir", "src/test/resources/output");
 properties.setProperty("outputDir", "src/test/resources/output");

 PrintWriter out = mock(PrintWriter.class);
 PrintWriter err = mock(PrintWriter.class);
 LagoonProcessor processor = mock(LagoonProcessor.class);
 when(processor.build(false)).thenReturn(true);
 when(processor.getExitValue()).thenReturn(0);
 when(processor.getErrorStream()).thenReturn(err);
 when(processor.getOutputStream()).thenReturn(out);

 lagoonCLI = new LagoonCLI(new String[]{"propertyFile.xml", "build"});
 assertEquals(processor, lagoonCLI.getProcessor());
 verify(processor).build(false);
 }

 @Test
 public void testForceWithProgress() throws IOException, SAXException, InterruptedException {
 properties.setProperty("targetURL", "http://localhost:8080");
 properties.setProperty("sitemapFile", sitemapFile.getAbsolutePath());
 properties.setProperty("sourceDir", "src/test/resources/output");
 properties.setProperty("outputDir", "src/test/resources/output");

 PrintWriter out = mock(PrintWriter.class);
 PrintWriter err = mock(PrintWriter.class);
 LagoonProcessor processor = mock(LagoonProcessor.class);
 when(processor.build(true)).thenReturn(true);
 when(processor.getExitValue()).thenReturn(0);
 when(processor.getErrorStream()).thenReturn(err);
 when(processor.getOutputStream()).thenReturn(out);

 lagoonCLI = new LagoonCLI(new String[]{"propertyFile.xml", "force"});
 assertEquals(processor, lagoonCLI.getProcessor());
 verify(processor).build(true);
 }

 @Test
 public void testIntervalWithProgress() throws IOException, SAXException, InterruptedException {
 properties.setProperty("targetURL", "http://localhost:8080");
 properties.setProperty("sitemapFile", sitemapFile.getAbsolutePath());
 properties.setProperty("sourceDir", "src/test/resources/output");
 properties.setProperty("outputDir", "src/test/resources/output");
 properties.setProperty("interval", "5000");

 PrintWriter out = mock(PrintWriter.class);
 PrintWriter err = mock(PrintWriter.class);
 LagoonProcessor processor = mock(LagoonProcessor.class);
 when(processor.build(false)).thenReturn(true);
 when(processor.getExitValue()).thenReturn(0);
 when(processor.getErrorStream()).thenReturn(err);
 when(processor.getOutputStream()).thenReturn(out);

 lagoonCLI = new LagoonCLI(new String[]{"propertyFile.xml", "interval"});
 assertEquals(processor, lagoonCLI.getProcessor());
 verify(processor).build(false);

 Thread thread = new Thread(() -> {
 try {
 Thread.sleep(10000);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
 });
 thread.start();

 lagoonCLI.run();
 thread.join();
 }
}