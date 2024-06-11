import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.helpers.DefaultHandler;
import static org.junit.Assert.*;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.StringList;

public class SAXDirParserTest {

 private SAXDirParser parser;
 private String xmlData;

 @Before
 public void setUp() {
 parseData = new SAXDirParser();
 xmlData = "<Directory>\n" +
 " <Web>\n" +
 " <Local>\n" +
 " <Names>\n" +
 " <pkg java.lang/>\n" +
 " <pkg javax.swing.border>\n" +
 " </Names>\n" +
 " </Local>\n" +
 " <BrowserLoc>Firefox</BrowserLoc>\n" +
 " <Editor>Eclipse</Editor>\n" +
 " <acceptsLineNumber>Yes</acceptsLineNumber>\n" +
 " <parameter>10</parameter>\n" +
 " </Web>\n" +
 " </Directory>";
 }

 @After
 public void tearDown() {
 parseData = null;
 xmlData = null;
 }

 @Test
 public void testGetWebData() {
 SAXDirParser parser = new SAXDirParser();
 assertEquals("", parser.getWebData());
 }

 @Test
 public void testStartElement_BrowserLoc() {
 parser = new SAXDirParser();
 StringReader reader = new StringReader(xmlData);
 SAXParserFactory factory = SAXParserFactory.newInstance();
 XMLReader xmlReader = factory.newSAXParser(reader).getXMLReader();
 xmlReader.setContentHandler(parser);
 xmlReader.parse(new InputSource(new StringReader(xmlData)));
 assertEquals("Firefox", parser.getWebData());
 }

 @Test
 public void testStartElement_Editor() {
 parser = new SAXDirParser();
 StringReader reader = new StringReader(xmlData);
 SAXParserFactory factory = SAXParserFactory.newInstance();
 XMLReader xmlReader = factory.newSAXParser(reader).getXMLReader();
 xmlReader.setContentHandler(parser);
 xmlReader.parse(new InputSource(new StringReader(xmlData)));
 assertEquals("Eclipse", parser.getEditor());
 }

 @Test
 public void testStartElement_acceptsLineNumber() {
 parser = new SAXDirParser();
 StringReader reader = new StringReader(xmlData);
 SAXParserFactory factory = SAXParserFactory.newInstance();
 XMLReader xmlReader = factory.newSAXParser(reader).getXMLReader();
 xmlReader.setContentHandler(parser);
 xmlReader.parse(new InputSource(new StringReader(xmlData)));
 assertTrue(parser.acceptsLineNumber());
 }

 @Test
 public void testStartElement_parameter() {
 parser = new SAXDirParser();
 StringReader reader = new StringReader(xmlData);
 SAXParserFactory factory = SAXParserFactory.newInstance();
 XMLReader xmlReader = factory.newSAXParser(reader).getXMLReader();
 xmlReader.setContentHandler(parser);
 xmlReader.parse(new InputSource(new StringReader(xmlData)));
 assertEquals("10", parser.lineNumberParameter());
 }

 @Test(expected = SAXException.class)
 public void testStartElement_UnknownElement() {
 parser = new SAXDirParser();
 StringReader reader = new StringReader("<UnknownElement/>");
 SAXParserFactory factory = SAXParserFactory.newInstance();
 XMLReader xmlReader = factory.newSAXParser(reader).getXMLReader();
 xmlReader.setContentHandler(parser);
 xmlReader.parse(new InputSource(new StringReader("<UnknownElement/>")));
 fail("SAXException should be thrown");
 }

 @Test
 public void testGetDirectory() {
 Map<String, String> expectedDirectory = new HashMap<>();
 expectedDirectory.put("java.lang", "test1");
 expectedDirectory.put("javax.swing.border", "test2");
 SAXDirParser parser = new SAXDirParser();
 StringReader reader = new StringReader(xmlData);
 SAXParserFactory factory = SAXParserFactory.newInstance();
 XMLReader xmlReader = factory.newSAXParser(reader).getXMLReader();
 xmlReader.setContentHandler(parser);
 xmlReader.parse(new InputSource(new StringReader(xmlData)));
 Map<String, String> actualDirectory = parser.getDirectory();
 assertEquals(expectedDirectory, actualDirectory);
 }

 @Test
 public void testGetLocalPackages() {
 StringList expectedLocalPackages = new StringList();
 expectedLocalPackages.add("java.lang");
 expectedLocalPackages.add("javax.swing.border");
 SAXDirParser parser = new SAXDirParser();
 StringReader reader = new StringReader(xmlData);
 SAXParserFactory factory = SAXParserFactory.newInstance();
 XMLReader xmlReader = factory.newSAXParser(reader).getXMLReader();
 xmlReader.setContentHandler(parser);
 xmlReader.parse(new InputSource(new StringReader(xmlData)));
 StringList actualLocalPackages = parser.getLocalPackages();
 assertEquals(expectedLocalPackages, actualLocalPackages);
 }

 @Test
 public void testGetLocalBrowser() {
 SAXDirParser parser = new SAXDirParser();
 StringReader reader = new StringReader(xmlData);
 SAXParserFactory factory = SAXParserFactory.newInstance();
 XMLReader xmlReader = factory.newSAXParser(reader).getXMLReader();
 xmlReader.setContentHandler(parser);
 xmlReader.parse(new InputSource(new StringReader(xmlData)));
 assertEquals("Firefox", parser.getLocalBrowser());
 }

 @Test
 public void testGetEditor() {
 SAXDirParser parser = new SAXDirParser();
 StringReader reader = new StringReader(xmlData);
 SAXParserFactory factory = SAXParserFactory.newInstance();
 XMLReader xmlReader = factory.newSAXParser(reader).getXMLReader();
 xmlReader.setContentHandler(parser);
 xmlReader.parse(new InputSource(new StringReader(xmlData)));
 assertEquals("Eclipse", parser.getEditor());
 }

 @Test
 public void testAcceptsLineNumber() {
 SAXDirParser parser = new SAXDirParser();
 StringReader reader = new StringReader(xmlData);
 SAXParserFactory factory = SAXParserFactory.newInstance();
 XMLReader xmlReader = factory.newSAXParser(reader).getXMLReader();
 xmlReader.setContentHandler(parser);
 xmlReader.parse(new InputSource(new StringReader(xmlData)));
 assertTrue(parser.acceptsLineNumber());
 }

 @Test
 public void testLineNumberParameter() {
 SAXDirParser parser = new SAXDirParser();
 StringReader reader = new StringReader(xmlData);
 SAXParserFactory factory = SAXParserFactory.newInstance();
 XMLReader xmlReader = factory.newSAXParser(reader).getXMLReader();
 xmlReader.setContentHandler(parser);
 xmlReader.parse(new InputSource(new StringReader(xmlData)));
 assertEquals("10", parser.lineNumberParameter());
 }

 @Test(expected = NullPointerException.class)
 public void testGetDirectory_EmptyDirectory() {
 SAXDirParser parser = new SAXDirParser();
 assertEquals(parser.getDirectory(), null);
 }

 @Test(expected = NullPointerException.class)
 public void testGetLocalPackages_EmptyLocalPackages() {
 SAXDirParser parser = new SAXDirParser();
 assertEquals(parser.getLocalPackages(), null);
 }
}