import org.junit.Test;
import static org.junit.Assert.*;
import com.jstevh.viewer.*;
import org.xml.sax.SAXException;
import java.io.FileNotFoundException;

public class SAXDirParserTest {

 private SAXDirParser parser;

 @Test
 public void testGetWebData() {
 SAXDirParser parser = new SAXDirParser();
 assertEquals("", parser.getWebData());
 }

 @Test
 public void testStartElement_BrowserLoc() throws SAXException {
 parser = new SAXDirParser();
 String xml = "<Web>\n" +
            "<BrowserLoc>Mozilla/5.0</BrowserLoc>\n" +
            "</Web>";
 SAXParserFactory factory = SAXParserFactory.newInstance();
 XMLReader reader = factory.newSAXParser().getXMLReader();
 reader.setContentHandler(parser);
 InputSource source = new InputSource(new StringReader(xml));
 reader.parse(source);
 assertEquals("Mozilla/5.0", SAXDirParser.localBrowser);
 }

 @Test
 public void testStartElement_Editor() throws SAXException {
 parser = new SAXDirParser();
 String xml = "<Web>\n" +
            "<Editor>Notepad</Editor>\n" +
            "</Web>";
 SAXParserFactory factory = SAXParserFactory.newInstance();
 XMLReader reader = factory.newSAXParser().getXMLReader();
 reader.setContentHandler(parser);
 InputSource source = new InputSource(new StringReader(xml));
 reader.parse(source);
 assertEquals("Notepad", SAXDirParser.file_editor);
 }

 @Test
 public void testStartElement_acceptsLineNumber() throws SAXException {
 parser = new SAXDirParser();
 String xml = "<Web>\n" +
            "<acceptsLineNumber>Yes</acceptsLineNumber>\n" +
            "</Web>";
 SAXParserFactory factory = SAXParserFactory.newInstance();
 XMLReader reader = factory.newSAXParser().getXMLReader();
 reader.setContentHandler(parser);
 InputSource source = new InputSource(new StringReader(xml));
 reader.parse(source);
 assertTrue(SAXDirParser.lineNumAccepted);
 }

 @Test
 public void testStartElement_parameter() throws SAXException {
 parser = new SAXDirParser();
 String xml = "<Web>\n" +
            "<parameter>12345</parameter>\n" +
            "</Web>";
 SAXParserFactory factory = SAXParserFactory.newInstance();
 XMLReader reader = factory.newSAXParser().getXMLReader();
 reader.setContentHandler(parser);
 InputSource source = new InputSource(new StringReader(xml));
 reader.parse(source);
 assertEquals("12345", SAXDirParser.lineNumParam);
 }

 @Test
 public void testStartElement_Group() throws SAXException {
 parser = new SAXDirParser();
 String xml = "<Web>\n" +
            "<Group>\n" +
            "</Group>\n" +
            "</Web>";
 SAXParserFactory factory = SAXParserFactory.newInstance();
 XMLReader reader = factory.newSAXParser().getXMLReader();
 reader.setContentHandler(parser);
 InputSource source = new InputSource(new StringReader(xml));
 reader.parse(source);
 assertFalse(parser.check);
 }

 @Test
 public void testStartElement_Web() throws SAXException, FileNotFoundException {
 parser = new SAXDirParser();
 SAXParserFactory factory = SAXParserFactory.newInstance();
 XMLReader reader = factory.newSAXParser().getXMLReader();
 reader.setContentHandler(parser);
 InputSource source = new InputSource(new FileInputStream("packagedirectory.xml"));
 reader.parse(source);
 assertTrue(parser.check);
 }

 @Test
 public void testStartElement_Local() throws SAXException {
 parser = new SAXDirParser();
 String xml = "<Web>\n" +
            "<Local>True</Local>\n" +
            "</Web>";
 SAXParserFactory factory = SAXParserFactory.newInstance();
 XMLReader reader = factory.newSAXParser().getXMLReader();
 reader.setContentHandler(parser);
 InputSource source = new InputSource(new StringReader(xml));
 reader.parse(source);
 assertTrue(parser.local);
 }

 @Test(expected = SAXException.class)
 public void testStartElement_InvalidElement() throws SAXException {
 parser = new SAXDirParser();
 String xml = "<Web>\n" +
            "<InvlidElement>Invalid</InvlidElement>\n" +
            "</Web>";
 SAXParserFactory factory = SAXParserFactory.newInstance();
 XMLReader reader = factory.newSAXParser().getXMLReader();
 reader.setContentHandler(parser);
 InputSource source = new InputSource(new StringReader(xml));
 reader.parse(source);
 fail("SAXException expected but not thrown");
 }

 @Test
 public void testGetDirectory() {
 HashMap<String, String> directory = SAXDirParser.getDirectory();
 assertTrue(directory.containsKey("java.lang"));
 assertTrue(directory.containsKey("javax.swing.border"));
 assertEquals("", directory.get(""));
 }

 @Test
 public void testGetLocalPackages() {
 StringList localPackages = SAXDirParser.getLocalPackages();
 assertNotNull(localPackages);
 }

 @Test
 public void testGetLocalBrowser() {
 assertEquals("", SAXDirParser.getLocalBrowser());
 }

 @Test
 public void testGetEditor() {
 assertEquals("", SAXDirParser.getEditor());
 }

 @Test
 public void testAcceptsLineNumber() {
 assertTrue(SAXDirParser.acceptsLineNumber());
 }

 @Test
 public void testLineNumberParameter() {
 assertEquals("", SAXDirParser.lineNumberParameter());
 }
}