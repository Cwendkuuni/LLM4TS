import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.jstevh.viewer.SAXDirParser;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.junit.runner.RunWith;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

@RunWith(org.junit.platform.runner.JUnitPlatform.class)
public class SAXDirParserTest {

 private SAXDirParser parser;
 private DocumentBuilderFactory dbFactory;
 private DocumentBuilder dBuilder;

 @Before
 public void setUp() {
 parser = new SAXDirParser();
 dbFactory = DocumentBuilderFactory.newInstance();
 try {
 dBuilder = dbFactory.newDocumentBuilder();
 } catch (ParserConfigurationException e) {
 e.printStackTrace();
 }
 }

 @After
 public void tearDown() {
 parser = null;
 dbFactory = null;
 dBuilder = null;
 }

 @Test
 public void testGetDirectory() throws IOException, SAXException, ParserConfigurationException {
 String xml = "<Web>\n" +
 "<Local>\n" +
 "<Names>\n" +
 "<pkg java.lang></pkg>\n" +
 "<pkg javax.swing.border></pkg>\n" +
 "</Names>\n" +
 "</Local>\n" +
 "<Group>\n" +
 "<BrowserLoc>Firefox</BrowserLoc>\n" +
 "</Group>\n" +
 "</Web>";
 StringReader sr = new StringReader(xml);
 InputSource is = new InputSource(sr);
 dBuilder.parse(is);
 parser.handleStartElement("", "Web", null, null);
 parser.handleEndElement("", "Web", null);
 Map<String, String> expected = new HashMap<>();
 expected.put("java.lang", "/path/to/java.lang");
 expected.put("javax.swing.border", "/path/to/javax.swing.border");
 Map<String, String> actual = parser.getDirectory();
 assertEquals(expected, actual);
 }

 @Test(expected = AssertionError.class)
 public void testGetDirectory_NoLocalPackages() throws IOException, SAXException, ParserConfigurationException {
 String xml = "<Web>\n" +
 "<Names>\n" +
 "<pkg java.lang></pkg>\n" +
 "<pkg javax.swing.border></pkg>\n" +
 "</Names>\n" +
 "</Web>";
 StringReader sr = new StringReader(xml);
 InputSource is = new InputSource(sr);
 dBuilder.parse(is);
 parser.handleStartElement("", "Web", null, null);
 parser.handleEndElement("", "Web", null);
 Map<String, String> actual = parser.getDirectory();
 assertNull(actual);
 }

 @Test(expected = AssertionError.class)
 public void testGetDirectory_NullLocalPackages() throws IOException, SAXException, ParserConfigurationException {
 String xml = "<Web>\n" +
 "<Names>\n" +
 "<pkg java.lang></pkg>\n" +
 "<pkg javax.swing.border></pkg>\n" +
 "</Names>\n" +
 "</Web>";
 StringReader sr = new StringReader(xml);
 InputSource is = new InputSource(sr);
 SAXDirParser parserWithNullLocalPackages = new SAXDirParser();
 parserWithNullLocalPackages.handleStartElement("", "Web", null, null);
 parserWithNullLocalPackages.handleEndElement("", "Web", null);
 SAXDirParser.localPackages = null;
 Map<String, String> actual = parserWithNullLocalPackages.getDirectory();
 assertNull(actual);
 }

 @Test
 public void testGetLocalPackages() throws IOException, SAXException, ParserConfigurationException {
 String xml = "<Web>\n" +
 "<Local>\n" +
 "<Names>\n" +
 "<pkg java.lang></pkg>\n" +
 "<pkg javax.swing.border></pkg>\n" +
 "</Names>\n" +
 "</Local>\n" +
 "</Web>";
 StringReader sr = new StringReader(xml);
 InputSource is = new InputSource(sr);
 dBuilder.parse(is);
 parser.handleStartElement("", "Web", null, null);
 parser.handleEndElement("", "Web", null);
 StringList expected = new StringList();
 expected.add("java.lang");
 expected.add("javax.swing.border");
 StringList actual = parser.getLocalPackages();
 assertEquals(expected, actual);
 }

 @Test(expected = AssertionError.class)
 public void testGetLocalPackages_EmptyList() throws IOException, SAXException, ParserConfigurationException {
 String xml = "<Web>\n" +
 "<Local>\n" +
 "</Local>\n" +
 "</Web>";
 StringReader sr = new StringReader(xml);
 InputSource is = new InputSource(sr);
 dBuilder.parse(is);
 parser.handleStartElement("", "Web", null, null);
 parser.handleEndElement("", "Web", null);
 StringList actual = parser.getLocalPackages();
 assertNull(actual);
 }

 @Test
 public void testGetLocalBrowser() throws IOException, SAXException, ParserConfigurationException {
 String xml = "<Web>\n" +
 "<Local>\n" +
 "<BrowserLoc>Firefox</BrowserLoc>\n" +
 "</Local>\n" +
 "</Web>";
 StringReader sr = new StringReader(xml);
 InputSource is = new InputSource(sr);
 dBuilder.parse(is);
 parser.handleStartElement("", "Web", null, null);
 parser.handleEndElement("", "Web", null);
 String expected = "Firefox";
 String actual = parser.getLocalBrowser();
 assertEquals(expected, actual);
 }

 @Test(expected = AssertionError.class)
 public void testGetLocalBrowser_NullValue() throws IOException, SAXException, ParserConfigurationException {
 String xml = "<Web>\n" +
 "<Local>\n" +
 "<BrowserLoc></BrowserLoc>\n" +
 "</Local>\n" +
 "</Web>";
 StringReader sr = new StringReader(xml);
 InputSource is = new InputSource(sr);
 dBuilder.parse(is);
 parser.handleStartElement("", "Web", null, null);
 parser.handleEndElement("", "Web", null);
 String actual = parser.getLocalBrowser();
 assertNull(actual);
 }

 @Test
 public void testAcceptsLineNumber() throws IOException, SAXException, ParserConfigurationException {
 String xml = "<Web>\n" +
 "<acceptsLineNumber>Yes</acceptsLineNumber>\n" +
 "</Web>";
 StringReader sr = new StringReader(xml);
 InputSource is = new InputSource(sr);
 dBuilder.parse(is);
 parser.handleStartElement("", "Web", null, null);
 parser.handleEndElement("", "Web", null);
 boolean expected = true;
 boolean actual = parser.acceptsLineNumber();
 assertEquals(expected, actual);
 }

 @Test(expected = AssertionError.class)
 public void testAcceptsLineNumber_FalseValue() throws IOException, SAXException, ParserConfigurationException {
 String xml = "<Web>\n" +
 "<acceptsLineNumber>No</acceptsLineNumber>\n" +
 "</Web>";
 StringReader sr = new StringReader(xml);
 InputSource is = new InputSource(sr);
 dBuilder.parse(is);
 parser.handleStartElement("", "Web", null, null);
 parser.handleEndElement("", "Web", null);
 boolean actual = parser.acceptsLineNumber();
 assertFalse(actual);
 }

 @Test(expected = AssertionError.class)
 public void testAcceptsLineNumber_NullValue() throws IOException, SAXException, ParserConfigurationException {
 String xml = "<Web>\n" +
 "<acceptsLineNumber></acceptsLineNumber>\n" +
 "</Web>";
 StringReader sr = new StringReader(xml);
 InputSource is = new InputSource(sr);
 dBuilder.parse(is);
 parser.handleStartElement("", "Web", null, null);
 parser.handleEndElement("", "Web", null);
 boolean actual = parser.acceptsLineNumber();
 assertFalse(actual);
 }

 @Test(expected = AssertionError.class)
 public void testLineNumberParameter_EmptyString() throws IOException, SAXException, ParserConfigurationException {
 String xml = "<Web>\n" +
 "<acceptsLineNumber>Yes</acceptsLineNumber>\n" +
 "<parameter></parameter>\n" +
 "</Web>";
 StringReader sr = new StringReader(xml);
 InputSource is = new InputSource(sr);
 dBuilder.parse(is);
 parser.handleStartElement("", "Web", null, null);
 parser.handleEndElement("", "Web", null);
 String actual = parser.lineNumberParameter();
 assertNull(actual);
 }

 @Test
 public void testLineNumberParameter() throws IOException, SAXException, ParserConfigurationException {
 String xml = "<Web>\n" +
 "<acceptsLineNumber>Yes</acceptsLineNumber>\n" +
 "<parameter>32</parameter>\n" +
 "</Web>";
 StringReader sr = new StringReader(xml);
 InputSource is = new InputSource(sr);
 dBuilder.parse(is);
 parser.handleStartElement("", "Web", null, null);
 parser.handleEndElement("", "Web", null);
 String expected = "32";
 String actual = parser.lineNumberParameter();
 assertEquals(expected, actual);
 }
}