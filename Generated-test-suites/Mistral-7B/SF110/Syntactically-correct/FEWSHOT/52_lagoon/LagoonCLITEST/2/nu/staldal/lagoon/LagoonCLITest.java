package nu.staldal.lagoon.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.xml.sax.SAXException;

import nu.staldal.lagoon.*;
import nu.staldal.lagoon.core.*;
import java.io.*;
import nu.staldal.xtree.*;

public class LagoonCLITest {

 private static final String SYNTAX_MSG = "Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldon.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]";
 static Properties properties;

 @Test
 public void testEmptyArgs() {
 LagoonCLI.main(new String[0]);
 assertEquals(SYNTAX_MSG, System.out.getErrorStream().toString());
 }

 @Test
 public void testInvalidArgument() {
 LagoonCLI.main(new String[]{"test.xml", "invalid"});
 assertEquals(SYNTAX_MSG, System.out.getErrorStream().toString());
 }

 @Test
 public void testInvalidInterval() {
 LagoonCLI.main(new String[]{"property.xml", "-1"});
 assertEquals(SYNTAX_MSG, System.out.getErrorStream().toString());
 }

 @Test
 public void testValidArguments() {
 LagoonCLI.main(new String[]{"property.xml", "1000"});
 assertFalse(LagoonCLI.DEBUG);
 }

 @Test
 public void testBuildMode() {
 LagoonCLI.main(new String[]{"property.xml", "build"});
 assertFalse(LagoonCLI.DEBUG);
 }

 @Test
 public void testForceMode() {
 LagoonCLI.main(new String[]{"property.xml", "force"});
 assertFalse(LagoonCLI.DEBUG);
 }

 @Test
 public void testInvalidPropertyFile() {
 LagoonCLI.main(new String[]{"nonexistent.xml", "build"});
 assertEquals(SYNTAX_MSG, System.out.getErrorStream().toString());
 }

 @Test
 public void testValidPropertyFile() throws IOException {
 properties = new Properties();
 FileInputStream fis = new FileInputStream("property.txt");
 properties.load(fis);
 fis.close();
 LagoonCLI.main(new String[]{"property.xml", "build"});
 assertFalse(LagoonCLI.DEBUG);
 }

 @Test(expected = NumberFormatException.class)
 public void testInvalidIntervalValue() {
 LagoonCLI.main(new String[]{"property.xml", "abc"});
 }

 @Test
 public void testGetProperty() {
 properties = new Properties();
 properties.setProperty("targetURL", "http://test.com");
 String value = LagoonCLI.getProperty("targetURL");
 assertEquals("http://test.com", value);
 }

 @Test(expected = LagoonException.class)
 public void testMissingProperty() {
 properties = new Properties();
 LagoonCLI.getProperty("missingProperty");
 }

 @Test
 public void testShowTimeSmall() {
 long ms = 23;
 LagoonCLI.showTime(ms);
 assertEquals("in " + ms + " ms", System.out.getBuffer().toString());
 }

 @Test
 public void testShowTimeBig() {
 long ms = 23000;
 LagoonCLI.showTime(ms);
 assertEquals("in " + 23 + " s", System.out.getBuffer().toString());
 }

 @Test(expected = FileNotFoundException.class)
 public void testFileNotFoundException() {
 LagoonCLI.main(new String[]{"property.xml", "build"});
 File sitemapFile = new File("sitemap.xml");
 sitemapFile.delete();
 LagoonCLI.main(new String[]{"property.xml", "build"});
 }

 @Test(expected = IOException.class)
 public void testIOException() throws IOException, SAXException {
 properties = new Properties();
 properties.setProperty("sitemapFile", "sitemap.xml");
 File file = new File("sitemap.xml");
 InputStream inputStream = new ByteArrayInputStream("<?xml version=\"1.0\" encoding=\"UTF-8\"?>".getBytes());
 InputSource inputSource = new InputSource(inputStream);
 TreeBuilder treeBuilder = new TreeBuilder();
 treeBuilder.parseXML(inputSource, false);
 Element element = treeBuilder.getDocumentElement();
 file.delete();
 LagoonCLI.main(new String[]{"property.xml", "build"});
 InputStream inputStream2 = new FileInputStream(file);
 InputSource inputSource2 = new InputSource(inputStream2);
 TreeBuilder treeBuilder2 = new TreeBuilder();
 assertThrows(IOException.class, () -> treeBuilder2.parseXML(inputSource2, false));
 }

 @Test(expected = LagoonException.class)
 public void testAuthenticationMissingException() {
 LagoonCLI.main(new String[]{"property.xml", "build"});
 properties.setProperty("password", "");
 LagoonCLI.main(new String[]{"property.xml", "build"});
 }

 @Test(expected = AuthenticationException.class)
 public void testAuthenticationException() {
 LagoonCLI.main(new String[]{"property.xml", "build"});
 properties.setProperty("password", "wrongpassword");
 LagoonCLI.main(new String[]{"property.xml", "build"});
 }

 @Test(expected = FileNotFoundException.class)
 public void testFileNotFoundException2() {
 LagoonCLI.main(new String[]{"property.xml", "build"});
 properties.setProperty("sitemapFile", "sitemap.xml");
 File file = new File("sitemap.xml");
 file.delete();
 LagoonCLI.main(new String[]{"property.xml", "build"});
 }

 @Test(expected = IOException.class)
 public void testIOException2() throws IOException, SAXException {
 properties = new Properties();
 properties.setProperty("sitemapFile", "sitemap.xml");
 File file = new File("sitemap.xml");
 InputStream inputStream = new ByteArrayInputStream("<?xml version=\"1.0\" encoding=\"UTF-8\"?>".getBytes());
 InputSource inputSource = new InputSource(inputStream);
 TreeBuilder treeBuilder = new TreeBuilder();
 treeBuilder.parseXML(inputSource, false);
 Element element = treeBuilder.getDocumentElement();
 file.delete();
 LagoonCLI.main(new String[]{"property.xml", "build"});
 InputStream inputStream2 = new FileInputStream(file);
 InputSource inputSource2 = new InputSource(inputStream2);
 TreeBuilder treeBuilder2 = new TreeBuilder();
 assertThrows(IOException.class, () -> treeBuilder2.parseXML(inputSource2, false));
 }

 @Test(expected = LagoonException.class)
 public void testLagoonException() {
 LagoonCLI.main(new String[]{"property.xml", "build"});
 properties.setProperty("password", "");
 LagoonCLI.main(new String[]{"property.xml", "build"});
 }

 @Test
 public void testDestroy() {
 LagoonCLI.main(new String[]{"property.xml", "build"});
 LagoonProcessor processor = new LagoonProcessor("http://test.com", null, null, null, null, null);
 processor.destroy();
 }

 @Test
 public void testDestroy2() {
 LagoonCLI.main(new String[]{"property.xml", "build"});
 LagoonProcessor processor = new LagoonProcessor("http://test.com", null, null, null, null, null);
 processor.destroy();
 LagoonCLI.main(new String[]{"property.xml", "build"});
 LagoonProcessor processor2 = new LagoonProcessor("http://test.com", null, null, null, null, null);
 processor2.destroy();
 }

 @Test
 public void testBuildModeWithErrors() {
 LagoonCLI.main(new String[]{"property.xml", "build"});
 LagoonProcessor processor = new LagoonProcessor("http://test.com", null, null, null, null, null);
 assertFalse(processor.build(false));
 }

 @Test
 public void testForceBuildModeWithErrors() {
 LagoonCLI.main(new String[]{"property.xml", "force"});
 LagoonProcessor processor = new LagoonProcessor("http://test.com", null, null, null, null, null);
 assertFalse(processor.build(true));
 }

 @Test
 public void testBuildWithInterval() {
 LagoonCLI.main(new String[]{"property.xml", "1000"});
 LagoonProcessor processor = new LagoonProcessor("http://test.com", null, null, null, null, null);
 long timeBefore = System.currentTimeMillis();
 assertFalse(processor.build(false));
 long timeElapsed = System.currentTimeMillis() - timeBefore;
 LagoonCLI.showTime(timeElapsed);
 }

 @Test
 public void testBuildWithInterval2() {
 LagoonCLI.main(new String[]{"property.xml", "1000"});
 LagoonProcessor processor = new LagoonProcessor("http://test.com", null, null, null, null, null);
 long timeBefore = System.currentTimeMillis();
 assertFalse(processor.build(false));
 long timeElapsed = System.currentTimeMillis() - timeBefore;
 LagoonCLI.showTime(timeElapsed);
 try {
 Thread.sleep(1050);
 }
 catch (InterruptedException e) {}
 long timeElapsed2 = System.currentTimeMillis() - timeBefore;
 LagoonCLI.showTime(timeElapsed2);
 }
}