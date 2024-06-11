import org.junit.Test;
import org.xml.sax.SAXException;
import static org.junit.Assert.*;
import com.jstevh.viewer.*;

public class SAXDirParserTest {

 private SAXDirParser parser;

 @Test
 public void testGetWebData() {
 SAXDirParser parser = new SAXDirParser();
 assertEquals("", parser.getWebData());
 }

 @Test
 public void testStartElement() throws SAXException {
 parser = new SAXDirParser();
 String xmlString = "<Web>\n" +
         "<BrowserLoc>Mozilla/5.0</BrowserLoc>\n" +
         "<Editor>Notepad</Editor>\n" +
         "<acceptsLineNumber>Yes</acceptsLineNumber>\n" +
         "<parameter>123</parameter>\n" +
         "<Group>\n" +
         "</Web>\n";
 SAXParserFactory factory = SAXParserFactory.newInstance();
 SAXParser saxParser = factory.newSAXParser();
 XMLReader reader = saxParser.getXMLReader();
 reader.setContentHandler(parser);
 StringReader stringReader = new StringReader(xmlString);
 InputSource inputSource = new InputSource(stringReader);
 reader.parse(inputSource);
 assertEquals("Mozilla/5.0", parser.getWebData());
 }

 @Test
 public void testStartElement_WrongElement() throws SAXException {
 parser = new SAXDirParser();
 String xmlString = "<Web>\n" +
         "<BrowserLoc>Mozilla/5.0</BrowserLoc>\n" +
         "<Editor>Notepad</Editor>\n" +
         "<acceptsLineNumber>Yes</acceptsLineNumber>\n" +
         "<parameter>123</parameter>\n" +
         "<WrongElement></WrongElement>\n" +
         "</Web>\n";
 SAXParserFactory factory = SAXParserFactory.newInstance();
 SAXParser saxParser = factory.newSAXParser();
 XMLReader reader = saxParser.getXMLReader();
 reader.setContentHandler(parser);
 StringReader stringReader = new StringReader(xmlString);
 InputSource inputSource = new InputSource(stringReader);
 try {
 reader.parse(inputSource);
 fail("Expected SAXException when parsing unexpected element: WrongElement");
 } catch (SAXException e) {
 // Expected
 }
 }

 @Test
 public void testGetDirectory() {
 HashMap<String, String> expectedDirectory = new HashMap<>();
 expectedDirectory.put("java.lang", "path/to/java.lang.xml");
 expectedDirectory.put("javax.swing.border", "path/to/javax.swing.border.xml");
 SAXDirParser parser = new SAXDirParser();
 SAXParserFactory factory = SAXParserFactory.newInstance();
 SAXParser saxParser = factory.newSAXParser();
 XMLReader reader = saxParser.getXMLReader();
 reader.setContentHandler(parser);
 String xmlString = "<Web>\n" +
         "<BrowserLoc>Mozilla/5.0</BrowserLoc>\n" +
         "<Editor>Notepad</Editor>\n" +
         "<acceptsLineNumber>Yes</acceptsLineNumber>\n" +
         "<parameter>123</parameter>\n" +
         "<Names>\n" +
         "<pkg java.lang></pkg>\n" +
         "<pkg javax.swing.border></pkg>\n" +
         "</Names>\n" +
         "</Web>\n";
 StringReader stringReader = new StringReader(xmlString);
 InputSource inputSource = new InputSource(stringReader);
 reader.parse(inputSource);
 HashMap<String, String> actualDirectory = SAXDirParser.getDirectory();
 assertEquals(expectedDirectory, actualDirectory);
 }

 @Test
 public void testGetDirectory_Empty() {
 HashMap<String, String> expectedDirectory = new HashMap<>();
 HashMap<String, String> actualDirectory = SAXDirParser.getDirectory();
 assertEquals(expectedDirectory, actualDirectory);
 }

 @Test
 public void testGetLocalPackages() {
 StringList expectedLocalPackages = new StringList();
 expectedLocalPackages.add("java.lang");
 expectedLocalPackages.add("javax.swing.border");
 SAXDirParser parser = new SAXDirParser();
 SAXParserFactory factory = SAXParserFactory.newInstance();
 SAXParser saxParser = factory.newSAXParser();
 XMLReader reader = saxParser.getXMLReader();
 reader.setContentHandler(parser);
 String xmlString = "<Web>\n" +
         "<BrowserLoc>Mozilla/5.0</BrowserLoc>\n" +
         "<Editor>Notepad</Editor>\n" +
         "<acceptsLineNumber>Yes</acceptsLineNumber>\n" +
         "<parameter>123</parameter>\n" +
         "<Names>\n" +
         "<pkg java.lang local=\"true\"></pkg>\n" +
         "<pkg javax.swing.border local=\"true\"></pkg>\n" +
         "</Names>\n" +
         "</Web>\n";
 StringReader stringReader = new StringReader(xmlString);
 InputSource inputSource = new InputSource(stringReader);
 reader.parse(inputSource);
 StringList actualLocalPackages = SAXDirParser.getLocalPackages();
 assertEquals(expectedLocalPackages, actualLocalPackages);
 }

 @Test
 public void testGetLocalPackages_Empty() {
 StringList expectedLocalPackages = new StringList();
 StringList actualLocalPackages = SAXDirParser.getLocalPackages();
 assertEquals(expectedLocalPackages, actualLocalPackages);
 }

 @Test
 public void testGetLocalBrowser() {
 String expectedLocalBrowser = "Mozilla/5.0";
 SAXDirParser parser = new SAXDirParser();
 SAXParserFactory factory = SAXParserFactory.newInstance();
 SAXParser saxParser = factory.newSAXParser();
 XMLReader reader = saxParser.getXMLReader();
 reader.setContentHandler(parser);
 String xmlString = "<Web>\n" +
         "<BrowserLoc>Mozilla/5.0</BrowserLoc>\n" +
         "<Editor>Notepad</Editor>\n" +
         "<acceptsLineNumber>Yes</acceptsLineNumber>\n" +
         "<parameter>123</parameter>\n" +
         "</Web>\n";
 StringReader stringReader = new StringReader(xmlString);
 InputSource inputSource = new InputSource(stringReader);
 reader.parse(inputSource);
 assertEquals(expectedLocalBrowser, SAXDirParser.getLocalBrowser());
 }

 @Test
 public void testGetEditor() {
 String expectedEditor = "Notepad";
 SAXDirParser parser = new SAXDirParser();
 SAXParserFactory factory = SAXParserFactory.newInstance();
 SAXParser saxParser = factory.newSAXParser();
 XMLReader reader = saxParser.getXMLReader();
 reader.setContentHandler(parser);
 String xmlString = "<Web>\n" +
         "<BrowserLoc>Mozilla/5.0</BrowserLoc>\n" +
         "<Editor>Notepad</Editor>\n" +
         "<acceptsLineNumber>Yes</acceptsLineNumber>\n" +
         "<parameter>123</parameter>\n" +
         "</Web>\n";
 StringReader stringReader = new StringReader(xmlString);
 InputSource inputSource = new InputSource(stringReader);
 reader.parse(inputSource);
 assertEquals(expectedEditor, SAXDirParser.getEditor());
 }

 @Test
 public void testAcceptsLineNumber() {
 boolean expectedAcceptsLineNumber = true;
 SAXDirParser parser = new SAXDirParser();
 SAXParserFactory factory = SAXParserFactory.newInstance();
 SAXParser saxParser = factory.newSAXParser();
 XMLReader reader = saxParser.getXMLReader();
 reader.setContentHandler(parser);
 String xmlString = "<Web>\n" +
         "<BrowserLoc>Mozilla/5.0</BrowserLoc>\n" +
         "<Editor>Notepad</Editor>\n" +
         "<acceptsLineNumber>Yes</acceptsLineNumber>\n" +
         "<parameter>123</parameter>\n" +
         "</Web>\n";
 StringReader stringReader = new StringReader(xmlString);
 InputSource inputSource = new InputSource(stringReader);
 reader.parse(inputSource);
 assertTrue(SAXDirParser.acceptsLineNumber());
 }

 @Test
 public void testAcceptsLineNumber_False() {
 boolean expectedAcceptsLineNumber = false;
 SAXDirParser parser = new SAXDirParser();
 SAXParserFactory factory = SAXParserFactory.newInstance();
 SAXParser saxParser = factory.newSAXParser();
 XMLReader reader = saxParser.getXMLReader();
 reader.setContentHandler(parser);
 String xmlString = "<Web>\n" +
         "<BrowserLoc>Mozilla/5.0</BrowserLoc>\n" +
         "<Editor>Notepad</Editor>\n" +
         "<acceptsLineNumber>No</acceptsLineNumber>\n" +
         "<parameter>123</parameter>\n" +
         "</Web>\n";
 StringReader stringReader = new StringReader(xmlString);
 InputSource inputSource = new InputSource(stringReader);
 reader.parse(inputSource);
 assertFalse(SAXDirParser.acceptsLineNumber());
 }

 @Test
 public void testLineNumberParameter() {
 String expectedLineNumberParameter = "123";
 SAXDirParser parser = new SAXDirParser();
 SAXParserFactory factory = SAXParserFactory.newInstance();
 SAXParser saxParser = factory.newSAXParser();
 XMLReader reader = saxParser.getXMLReader();
 reader.setContentHandler(parser);
 String xmlString = "<Web>\n" +
         "<BrowserLoc>Mozilla/5.0</BrowserLoc>\n" +
         "<Editor>Notepad</Editor>\n" +
         "<acceptsLineNumber>Yes</acceptsLineNumber>\n" +
         "<parameter>123</parameter>\n" +
         "</Web>\n";
 StringReader stringReader = new StringReader(xmlString);
 InputSource inputSource = new InputSource(stringReader);
 reader.parse(inputSource);
 assertEquals(expectedLineNumberParameter, SAXDirParser.lineNumberParameter());
 }

 @Test
 public void testLineNumberParameter_Empty() {
 String expectedLineNumberParameter = "";
 String actualLineNumberParameter = SAXDirParser.lineNumberParameter();
 assertEquals(expectedLineNumberParameter, actualLineNumberParameter);
 }
}