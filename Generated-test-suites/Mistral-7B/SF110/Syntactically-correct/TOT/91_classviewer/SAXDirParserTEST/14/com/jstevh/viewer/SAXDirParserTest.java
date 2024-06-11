import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;
import static org.junit.Assert.*;
import com.jstevh.viewer.SAXDirParser;
import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.XMLIOException;

public class SAXDirParserTest {

 private SAXDirParser parser;
 private File inputFile;

 @Before
 public void init() throws ParserConfigurationException, SAXException, IOException, XMLIOException {
 inputFile = new File("path/to/packagedirectory.xml");
 parser = new SAXDirParser();
 SAXParserFactory factory = SAXParserFactory.newInstance();
 SAXParser saxParser = factory.newSAXParser();
 saxParser.setContentHandler(parser);
 saxParser.parse(inputFile);
 }

 @Test
 public void testGetDirectory() {
 HashMap<String, String> expectedDirectory = new HashMap<>();
 expectedDirectory.put("java.lang", "path/to/java.lang");
 expectedDirectory.put("javax.swing.border", "path/to/javax.swing.border");

 HashMap<String, String> actualDirectory = parser.getDirectory();
 assertEquals(expectedDirectory, actualDirectory);
 }

 @Test
 public void testGetLocalPackages() {
 StringList expectedLocalPackages = new StringList();
 expectedLocalPackages.add("package1");
 expectedLocalPackages.add("package2");

 StringList actualLocalPackages = parser.getLocalPackages();
 assertEquals(expectedLocalPackages, actualLocalPackages);
 }

 @Test
 public void testGetLocalBrowser() {
 String expectedLocalBrowser = "Firefox";
 String actualLocalBrowser = parser.getLocalBrowser();
 assertEquals(expectedLocalBrowser, actualLocalBrowser);
 }

 @Test
 public void testEditor() {
 String expectedEditor = "Notepad++";
 String actualEditor = parser.getEditor();
 assertEquals(expectedEditor, actualEditor);
 }

 @Test
 public void testAcceptsLineNumber() {
 boolean expectedAcceptsLineNumber = true;
 boolean actualAcceptsLineNumber = parser.acceptsLineNumber();
 assertEquals(expectedAcceptsLineNumber, actualAcceptsLineNumber);
 }

 @Test
 public void testLineNumberParameter() {
 String expectedLineNumberParameter = "5";
 String actualLineNumberParameter = parser.lineNumberParameter();
 assertEquals(expectedLineNumberParameter, actualLineNumberParameter);
 }

 @Test(expected = IllegalStateException.class)
 public void testGetDirectoryWhenNotParsed() {
 SAXDirParser uninitializedParser = new SAXDirParser();
 HashMap<String, String> actualDirectory = uninitializedParser.getDirectory();
 fail("Unexpected call to getDirectory() method without parsing the XML file first.");
 }
}