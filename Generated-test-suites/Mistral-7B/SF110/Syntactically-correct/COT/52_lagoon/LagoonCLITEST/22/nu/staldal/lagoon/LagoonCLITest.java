package nu.staldal.lagoon.test;

import static org.junit.Assert.*;
import nu.staldal.lagoon.*;
import org.xml.sax.SAXException;
import nu.staldal.xtree.*;

import org.junit.Test;
import java.io.*;
import java.util.*;

public class LagoonCLITest {

 private static final String TEST_PROPERTY_FILE = "testPropertyFile.xml";
 private static final String TEST_SIMAP_FILE = "testSitemapFile.xml";

 @Test
 public void testMainWithPropertyFile() {
  String[] args = new String[]{"testPropertyFile.xml"};
  LagoonCLI.main(args);
  assertTrue(LagoonCLI.DEBUG == false);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMainWithInvalidArgument() {
  String[] args = new String[]{"test", "invalid"};
  LagoonCLI.main(args);
 }

 @Test(expected = NumberFormatException.class)
 public void testMainWithInvalidInterval() {
  String[] args = new String[]{"testPropertyFile.xml", "a"};
  LagoonCLI.main(args);
 }

 @Test
 public void testMainWithPropertyFileAndBuild() {
  String[] args = new String[]{"testPropertyFile.xml", "build"};
  LagoonCLI.main(args);
 }

 @Test
 public void testMainWithPropertyFileAndForce() {
  String[] args = new String[]{"testPropertyFile.xml", "force"};
  LagoonCLI.main(args);
 }

 @Test
 public void testMainWithPropertyFileAndInterval() {
  String[] args = new String[]{"testPropertyFile.xml", "5000"};
  LagoonCLI.main(args);
 }

 @Test(expected = FileNotFoundException.class)
 public void testMainWithNonExistentPropertyFile() {
  String[] args = new String[]{"nonExistentPropertyFile.xml"};
  LagoonCLI.main(args);
 }

 @Test(expected = LagoonException.class)
 public void testMainWithMissingProperty() {
  String xml = "<lagoon>\n<property name=\"targetURL\" value=\"http://test.com\"/>\n</lagoon>";
  InputSource inputSource = new InputSource(new StringReader(xml));
  Properties properties = new Properties();
  properties.load(inputSource);
  String[] args = new String[]{"testPropertyFile.xml"};
  LagoonCLI.main(args);
 }

 @Test(expected = SAXException.class)
 public void testMainWithInvalidSitemapFile() throws SAXException, IOException {
  String[] args = new String[]{"testSitemapFile.xml"};
  LagoonCLI.main(args);
 }

 @Test
 public void testGetProperty() throws LagoonException {
  String xml = "<lagoon>\n<property name=\"targetURL\" value=\"http://test.com\"/>\n<property name=\"property2\" value=\"test2\"/>\n</lagoon>";
  InputSource inputSource = new InputSource(new StringReader(xml));
  Properties properties = new Properties();
  properties.load(inputSource);
  String result = LagoonCLI.getProperty("targetURL");
  assertEquals("http://test.com", result);
  String result2 = LagoonCLI.getProperty("property2");
  assertEquals("test2", result2);
 }

 @Test(expected = LagoonException.class)
 public void testGetPropertyWithMissingProperty() throws LagoonException {
  String xml = "<lagoon>\n<property name=\"targetURL\" value=\"http://test.com\"/>\n</lagoon>";
  InputSource inputSource = new InputSource(new StringReader(xml));
  Properties properties = new Properties();
  properties.load(inputSource);
  LagoonCLI.getProperty("missingProperty");
 }

 @Test
 public void testShowTime() {
  long ms = 100;
  LagoonCLI.showTime(ms);
  assertTrue(ms >= 0);
 }

 @Test
 public void testShowTimeWithShortTime() {
  long ms = 5;
  LagoonCLI.showTime(ms);
  assertTrue("in " + ms + " ms".startsWith("in in "));
 }

 @Test
 public void testShowTimeWithLongTime() {
  long ms = 5000;
  LagoonCLI.showTime(ms);
  assertTrue("in " + ms / 1000L + " s".endsWith(" in 5 s"));
 }
}