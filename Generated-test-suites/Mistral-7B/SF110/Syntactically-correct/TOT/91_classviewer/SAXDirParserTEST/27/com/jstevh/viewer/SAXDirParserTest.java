import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.jstevh.viewer.SAXDirParser;
import java.io.ByteArrayInputStream;
import java.io.StringReader;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;

public class SAXDirParserTest {

 private SAXDirParser parser;

 @Before
 public void setUp() {
 parser = new SAXDirParser();
 }

 @Test
 public void testGetWebData() throws ParserConfigurationException, SAXException, IOException {
 SAXParserFactory saxFactory = SAXParserFactory.newInstance();
 SAXParser myParser = saxFactory.newSAXParser();
 myParser.setContentHandler(parser);
 String inputXML = "<Web>\n" +
 "<Local>\n" +
 "<acceptsLineNumber>Yes</acceptsLineNumber>\n" +
 "<parameter>123</parameter>\n" +
 "</Local>\n" +
 "</Web>";
 ByteArrayInputStream is = new ByteArrayInputStream(inputXML.getBytes());
 myParser.parse(is);
 assertEquals("", parser.getWebData());
 }

 @Test
 public void testGetDirectoryReturnsExpectedValues() throws ParserConfigurationException, SAXException, IOException {
 SAXParserFactory saxFactory = SAXParserFactory.newInstance();
 SAXParser myParser = saxFactory.newSAXParser();
 StringReader reader = new StringReader("<Web>\n" +
 "<Local>\n" +
 "<Names>\n" +
 "<pkg java.lang></pkg>\n" +
 "<pkg javax.swing.border></pkg>\n" +
 "</Names>\n" +
 "<acceptsLineNumber>Yes</acceptsLineNumber>\n" +
 "<parameter>123</parameter>\n" +
 "</Local>\n" +
 "</Web>");
 myParser.setContentHandler(parser);
 myParser.parse(new InputSource(reader));
 Map<String, String> expectedDirectory = new HashMap<>();
 expectedDirectory.put("java.lang", "expectedLocation1");
 expectedDirectory.put("javax.swing.border", "expectedLocation2");
 Map<String, String> actualDirectory = parser.getDirectory();
 assertEquals(expectedDirectory, actualDirectory);
 }

 @Test
 public void testGetLocalPackagesReturnsExpectedValues() throws ParserConfigurationException, SAXException, IOException {
 SAXParserFactory saxFactory = SAXParserFactory.newInstance();
 SAXParser myParser = saxFactory.newSAXParser();
 StringReader reader = new StringReader("<Web>\n" +
 "<Local>\n" +
 "<Names>\n" +
 "<pkg java.lang></pkg>\n" +
 "<pkg javax.swing.border></pkg>\n" +
 "</Names>\n" +
 "<LocalPackages>\n" +
 "<pkg>java.util</pkg>\n" +
 "</LocalPackages>\n" +
 "</Local>\n" +
 "</Web>");
 myParser.setContentHandler(parser);
 myParser.parse(new InputSource(reader));
 StringList expectedLocalPackages = new StringList();
 expectedLocalPackages.add("java.util");
 StringList actualLocalPackages = parser.getLocalPackages();
 assertEquals(expectedLocalPackages, actualLocalPackages);
 }

 @Test
 public void testGetLocalBrowserReturnsExpectedValue() throws ParserConfigurationException, SAXException, IOException {
 SAXParserFactory saxFactory = SAXParserFactory.newInstance();
 SAXParser myParser = saxFactory.newSAXParser();
 StringReader reader = new StringReader("<Web>\n" +
 "<Local>\n" +
 "<BrowserLoc>Internet Explorer</BrowserLoc>\n" +
 "</Local>\n" +
 "</Web>");
 myParser.setContentHandler(parser);
 myParser.parse(new InputSource(reader));
 assertEquals("Internet Explorer", parser.getLocalBrowser());
 }

 @Test
 public void testGetEditorReturnsExpectedValue() throws ParserConfigurationException, SAXException, IOException {
 SAXParserFactory saxFactory = SAXParserFactory.newInstance();
 SAXParser myParser = saxFactory.newSAXParser();
 StringReader reader = new StringReader("<Web>\n" +
 "<Editor>Notepad</Editor>\n" +
 "</Web>");
 myParser.setContentHandler(parser);
 myParser.parse(new InputSource(reader));
 assertEquals("Notepad", parser.getEditor());
 }

 @Test
 public void testAcceptsLineNumberReturnsExpectedValue() throws ParserConfigurationException, SAXException, IOException {
 SAXParserFactory saxFactory = SAXParserFactory.newInstance();
 SAXParser myParser = saxFactory.newSAXParser();
 StringReader reader = new StringReader("<Web>\n" +
 "<acceptsLineNumber>Yes</acceptsLineNumber>\n" +
 "</Web>");
 myParser.setContentHandler(parser);
 myParser.parse(new InputSource(reader));
 assertTrue(parser.acceptsLineNumber());
 }

 @Test
 public void testLineNumberParameterReturnsExpectedValue() throws ParserConfigurationException, SAXException, IOException {
 SAXParserFactory saxFactory = SAXParserFactory.newInstance();
 SAXParser myParser = saxFactory.newSAXParser();
 StringReader reader = new StringReader("<Web>\n" +
 "<acceptsLineNumber>Yes</acceptsLineNumber>\n" +
 "<parameter>456</parameter>\n" +
 "</Web>");
 myParser.setContentHandler(parser);
 myParser.parse(new InputSource(reader));
 assertEquals("456", parser.lineNumberParameter());
 }
}