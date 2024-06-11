package nu.staldal.lagoon;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;
import org.xml.sax.SAXException;

public class LagoonCLITest {

 @Test
 public void testValidArguments() {
 LagoonCLI.main(new String[]{"property_file.xml", "build"});
 assertFalse(LagoonCLI.build);
 assertFalse(LagoonCLI.force);
 assertFalse(LagoonCLI.interval > 0);
 }

 @Test
 public void testInvalidIntervalArgument() {
 LagoonCLI.main(new String[]{"property_file.xml", "-1"});
 assertFalse(LagoonCLI.build);
 assertFalse(LagoonCLI.force);
 assertFalse(LagoonCLI.interval > 0);
 }

 @Test
 public void testMissingArgument() {
 LagoonCLI.main(new String[0]);
 assertFalse(LagoonCLI.build);
 assertFalse(LagoonCLI.force);
 assertFalse(LagoonCLI.interval > 0);
 }

 @Test
 public void testPropertyFileArgument() {
 LagoonCLI.main(new String[]{"property_file.xml", "build"});
 assertFalse(LagoonCLI.build);
 assertFalse(LagoonCLI.force);
 assertFalse(LagoonCLI.interval > 0);
 }

 @Test(expected = NumberFormatException.class)
 public void testInvalidIntervalArgumentType() {
 LagoonCLI.main(new String[]{"property_file.xml", "abc"});
 }

 @Test
 public void testBuildArgument() {
 LagoonCLI.main(new String[]{"property_file.xml", "build"});
 assertTrue(LagoonCLI.build);
 assertFalse(LagoonCLI.force);
 assertFalse(LagoonCLI.interval > 0);
 }

 @Test
 public void testForceArgument() {
 LagoonCLI.main(new String[]{"property_file.xml", "force"});
 assertTrue(LagoonCLI.force);
 assertFalse(LagoonCLI.build);
 assertFalse(LagoonCLI.interval > 0);
 }

 @Test
 public void testIntervalArgument() {
 LagoonCLI.main(new String[]{"property_file.xml", "1000"});
 assertFalse(LagoonCLI.build);
 assertFalse(LagoonCLI.force);
 assertTrue(LagoonCLI.interval > 0);
 }

 @Test
 public void testInvalidPropertyFile() {
 File invalidFile = new File("invalid_file.txt");
 LagoonCLI.main(new String[]{invalidFile.getPath()});
 assertFalse(LagoonCLI.build);
 assertFalse(LagoonCLI.force);
 assertFalse(LagoonCLI.interval > 0);
 }

 @Test(expected = FileNotFoundException.class)
 public void testMissingPropertyFile() {
 LagoonCLI.main(new String[]{"missing_file.xml"});
 assertFalse(LagoonCLI.build);
 assertFalse(LagoonCLI.force);
 assertFalse(LagoonCLI.interval > 0);
 }

 @Test
 public void testPasswordMissingException() {
 LagoonCLI.properties = new Properties();
 LagoonCLI.properties.setProperty("targetURL", "http://www.example.com");
 LagoonCLI.properties.setProperty("sitemapFile", "sitemap.xml");
 File sitemapFile = new File("sitemap.xml");
 Element sitemapTree = null;
 try {
 sitemapTree = TreeBuilder.parseXML(TreeBuilder.fileToInputSource(sitemapFile), false);
 }
 catch (SAXException e) {
 throw new RuntimeException(e);
 }
 LagoonProcessor processor = null;
 assertThrows(AuthenticationMissingException.class, () -> {
 processor = new LagoonProcessor("http://www.example.com", sitemapTree, new File("."), null, new PrintWriter(System.out, true), new PrintWriter(System.err, true));
 });
 }

 @Test
 public void testPasswordIncorrectException() {
 LagoonCLI.properties = new Properties();
 LagoonCLI.properties.setProperty("targetURL", "http://www.example.com");
 LagoonCLI.properties.setProperty("sitemapFile", "sitemap.xml");
 LagoonCLI.properties.setProperty("password", "wrong_password");
 File sitemapFile = new File("sitemap.xml");
 Element sitemapTree = null;
 try {
 sitemapTree = TreeBuilder.parseXML(TreeBuilder.fileToInputSource(sitemapFile), false);
 }
 catch (SAXException e) {
 throw new RuntimeException(e);
 }
 LagoonProcessor processor = null;
 assertThrows(AuthenticationException.class, () -> {
 processor = new LagoonProcessor("http://www.example.com", sitemapTree, new File("."), "wrong_password".toCharArray(), new PrintWriter(System.out, true), new PrintWriter(System.err, true));
 });
 }

 @Test
 public void testFileNotFoundException() {
 LagoonCLI.properties = new Properties();
 LagoonCLI.properties.setProperty("targetURL", "http://www.example.com");
 LagoonCLI.properties.setProperty("sitemapFile", "nonexistent_file.xml");
 assertThrows(FileNotFoundException.class, () -> {
 LagoonCLI.main(new String[]{"property_file.xml", "build"});
 });
 }

 @Test
 public void testIOException() {
 LagoonCLI.properties = new Properties();
 LagoonCLI.properties.setProperty("targetURL", "http://www.example.com");
 LagoonCLI.properties.setProperty("sitemapFile", "sitemap.xml");
 File sitemapFile = new File("sitemap.xml");
 Element sitemapTree = null;
 try {
 sitemapTree = TreeBuilder.parseXML(TreeBuilder.fileToInputSource(sitemapFile), false);
 }
 catch (SAXException e) {
 throw new RuntimeException(e);
 }
 assertThrows(IOException.class, () -> {
 LagoonCLI.main(new String[]{"property_file.xml", "build", "io_error"});
 });
 }

 @Test
 public void testLagoonException() {
 LagoonCLI.properties = new Properties();
 LagoonCLI.properties.setProperty("targetURL", "http://www.example.com");
 LagoonCLI.properties.setProperty("sitemapFile", "sitemap.xml");
 File sitemapFile = new File("sitemap.xml");
 Element sitemapTree = null;
 try {
 sitemapTree = TreeBuilder.parseXML(TreeBuilder.fileToInputSource(sitemapFile), false);
 }
 catch (SAXException e) {
 throw new RuntimeException(e);
 }
 assertThrows(LagoonException.class, () -> {
 LagoonCLI.main(new String[]{"property_file.xml", "build", "lagoon_error"});
 });
 }
}