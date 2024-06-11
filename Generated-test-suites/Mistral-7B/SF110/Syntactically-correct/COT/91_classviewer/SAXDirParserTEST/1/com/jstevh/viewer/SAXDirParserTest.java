import org.junit.Test;
import static org.junit.Assert.*;
import com.jstevh.viewer.*;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import java.io.StringReader;
import javax.xml.parsers.*;

public class SAXDirParserTest {

 private SAXDirParser parser;

 @Before
 public void setUp() {
 parser = new SAXDirParser();
 }

 @Test
 public void testGetWebData() {
 SAXDirParser instance = new SAXDirParser();
 String result = instance.getWebData();
 assertEquals("", result);
 }

 @Test
 public void testStartElement_BrowserLoc() {
 try {
 parser.startElement("", "BrowserLoc", "BrowserLoc", new AttributesImpl());
 assertTrue(parser.isCheck());
 assertEquals(0, parser.getLevel());
 } catch (SAXException e) {
 fail("Exception should not be thrown in this test case");
 }
 }

 @Test
 public void testStartElement_Editor() {
 try {
 parser.startElement("", "Editor", "Editor", new AttributesImpl());
 assertTrue(parser.isCheck());
 assertEquals(10, parser.getLevel());
 } catch (SAXException e) {
 fail("Exception should not be thrown in this test case");
 }
 }

 @Test
 public void testStartElement_acceptsLineNumber() {
 try {
 parser.startElement("", "acceptsLineNumber", "acceptsLineNumber", new AttributesImpl());
 assertTrue(parser.isCheck());
 assertEquals(11, parser.getLevel());
 } catch (SAXException e) {
 fail("Exception should not be thrown in this test case");
 }
 }

 @Test
 public void testStartElement_parameter() {
 try {
 parser.startElement("", "parameter", "parameter", new AttributesImpl());
 assertTrue(parser.isCheck());
 assertEquals(12, parser.getLevel());
 } catch (SAXException e) {
 fail("Exception should not be thrown in this test case");
 }
 }

 @Test
 public void testStartElement_Group() {
 try {
 parser.startElement("", "Group", "Group", new AttributesImpl());
 assertFalse(parser.isCheck());
 } catch (SAXException e) {
 fail("Exception should not be thrown in this test case");
 }
 }

 @Test
 public void testStartElement_Web() {
 try {
 parser.startElement("", "Web", "Web", new AttributesImpl());
 assertTrue(parser.isCheck());
 assertEquals(30, parser.getLevel());
 } catch (SAXException e) {
 fail("Exception should not be thrown in this test case");
 }
 }

 @Test
 public void testStartElement_Local() {
 try {
 parser.startElement("", "Local", "Local", new AttributesImpl());
 assertTrue(parser.isCheck());
 assertEquals(31, parser.getLevel());
 } catch (SAXException e) {
 fail("Exception should not be thrown in this test case");
 }
 }

 @Test
 public void testStartElement_Names() {
 try {
 parser.startElement("", "Names", "Names", new AttributesImpl());
 assertTrue(parser.isCheck());
 } catch (SAXException e) {
 fail("Exception should not be thrown in this test case");
 }
 }

 @Test
 public void testStartElement_pkg() {
 try {
 parser.startElement("", "pkg", "pkg", new AttributesImpl());
 assertTrue(parser.isCheck());
 assertEquals(4, parser.getLevel());
 } catch (SAXException e) {
 fail("Exception should not be thrown in this test case");
 }
 }

 @Test(expected = SAXException.class)
 public void testStartElement_InvalidElement() {
 try {
 parser.startElement("", "InvalidElement", "InvalidElement", new AttributesImpl());
 fail("Exception should be thrown in this test case");
 } catch (SAXException e) {
 // expected
 }
 }

 @Test
 public void testCharacters_BrowserLoc() {
 String input = "<BrowserLoc>Internet Explorer</BrowserLoc>";
 try {
 parser.characters(new StringReader(input).asCharArray(), 0, input.length());
 assertEquals("Internet Explorer", SAXDirParser.localBrowser);
 } catch (SAXException | IOException e) {
 fail("Exception should not be thrown in this test case");
 }
 }

 @Test
 public void testCharacters_Editor() {
 String input = "<Editor>Notepad++</Editor>";
 try {
 parser.characters(new StringReader(input).asCharArray(), 0, input.length());
 assertEquals("Notepad++", SAXDirParser.file_editor);
 } catch (SAXException | IOException e) {
 fail("Exception should not be thrown in this test case");
 }
}

 @Test
 public void testCharacters_acceptsLineNumber() {
 try {
 parser.characters(new StringReader("Yes").asCharArray(), 0, 3);
 assertTrue(SAXDirParser.lineNumAccepted);
 } catch (SAXException | IOException e) {
 fail("Exception should not be thrown in this test case");
 }

 try {
 parser.characters(new StringReader("No").asCharArray(), 0, 2);
 assertFalse(SAXDirParser.lineNumAccepted);
 } catch (SAXException | IOException e) {
 fail("Exception should not be thrown in this test case");
 }
 }

 @Test
 public void testCharacters_parameter() {
 try {
 parser.characters(new StringReader("10"), 0, 2);
 assertEquals("10", SAXDirParser.lineNumParam);
 } catch (SAXException | IOException e) {
 fail("Exception should not be thrown in this test case");
 }
 }

 @Test
 public void testCharacters_invalidInput() {
 try {
 parser.characters(new StringReader("InvalidInput").asCharArray(), 0, 9);
 fail("Exception should be thrown in this test case");
 } catch (SAXException e) {
 // expected
 }
 }

 @Test
 public void testGetDirectory() {
 HashMap<String, String> expected = new HashMap<>();
 expected.put("java.lang", "Java Language");
 expected.put("javax.swing.border", "Swing Border Packages");
 SAXDirParser.getDirectory();
 HashMap<String, String> actual = (HashMap) SAXDirParser.directory.clone();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetLocalPackages() {
 StringList expected = new StringList();
 expected.add("java.lang");
 expected.add("javax.swing.border");
 SAXDirParser.getLocalPackages();
 StringList actual = SAXDirParser.localPackages.clone();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetLocalBrowser() {
 SAXDirParser.localBrowser = "Firefox";
 assertEquals("Firefox", SAXDirParser.getLocalBrowser());
 }

 @Test
 public void testGetEditor() {
 SAXDirParser.file_editor = "Visual Studio Code";
 assertEquals("Visual Studio Code", SAXDirParser.getEditor());
 }

 @Test
 public void testAcceptsLineNumber() {
 SAXDirParser.lineNumAccepted = true;
 assertTrue(SAXDirParser.acceptsLineNumber());
 }

 @Test
 public void testLineNumberParameter() {
 SAXDirParser.lineNumParam = "5";
 assertEquals("5", SAXDirParser.lineNumberParameter());
 }

 @Test
 public void testMain() throws Exception {
 SAXParserFactory saxFactory = SAXParserFactory.newInstance();
 SAXParser myParser = saxFactory.newSAXParser();
 XMLReader myReader = myParser.getXMLReader();
 myReader.setContentHandler(parser);
 myReader.parse(new InputSource(new StringReader("<Web>\n" +
 "<Local acceptsLineNumber=\"Yes\" lineNumberParameter=\"10\">\n" +
 "<pkg>java.lang</pkg>\n" +
 "<pkg>javax.swing.border</pkg>\n" +
 "</Local>\n" +
 "</Web>")));
 assertEquals("Java Language", SAXDirParser.directory.get("java.lang"));
 assertEquals("Swing Border Packages", SAXDirParser.directory.get("javax.swing.border"));
 assertEquals("Firefox", SAXDirParser.localBrowser);
 assertEquals("Notepad++", SAXDirParser.file_editor);
 assertTrue(SAXDirParser.lineNumAccepted);
 assertEquals("10", SAXDirParser.lineNumParam);
 }

 @After
 public void tearDown() {
 parser = null;
 }
}