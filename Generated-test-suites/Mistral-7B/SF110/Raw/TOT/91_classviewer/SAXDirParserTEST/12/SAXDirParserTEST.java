import org.junit.Test;
import org.xml.sax.SAXException;
import static org.junit.Assert.*;
import com.jstevh.viewer.SAXDirParser;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.XMLParserConfigurationException;

public class SAXDirParserTest {

 @Test
 public void testGetDirectory() throws ParserConfigurationException, SAXException, XMLParserConfigurationException {
 SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
 SAXDirParser parser = new SAXDirParser();
 String xmlFile = "packagedirectory.xml";
 saxParser.parse(new File(xmlFile), parser);
 HashMap<String, String> expectedDirectory = new HashMap<>();
 expectedDirectory.put("java.lang", "path/to/java.lang");
 expectedDirectory.put("javax.swing.border", "path/to/javax.swing.border");
 assertEquals(expectedDirectory, SAXDirParser.getDirectory());
 }

 @Test(expected = NullPointerException.class)
 public void testGetDirectoryWhenXMLFileIsNull() {
 SAXDirParser.getDirectory();
 }

 @Test
 public void testGetLocalPackages() {
 SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
 SAXDirParser parser = new SAXDirParser();
 String xmlFile = "packagedirectory.xml";
 saxParser.parse(new File(xmlFile), parser);
 StringList expectedLocalPackages = new StringList();
 expectedLocalPackages.add("package1");
 expectedLocalPackages.add("package2");
 assertEquals(expectedLocalPackages, SAXDirParser.getLocalPackages());
 }

 @Test(expected = NullPointerException.class)
 public void testGetLocalPackagesWhenDirectoryIsEmpty() {
 SAXDirParser.getLocalPackages();
 }

 @Test
 public void testGetLocalBrowser() {
 SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
 SAXDirParser parser = new SAXDirParser();
 String xmlFile = "packagedirectory.xml";
 saxParser.parse(new File(xmlFile), parser);
 String expectedLocalBrowser = "Firefox";
 assertEquals(expectedLocalBrowser, SAXDirParser.getLocalBrowser());
 }

 @Test(expected = NullPointerException.class)
 public void testGetLocalBrowserWhenBrowserIsNotDefined() {
 SAXDirParser.getLocalBrowser();
 }

 @Test
 public void testAcceptsLineNumber() {
 SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
 SAXDirParser parser = new SAXDirParser();
 String xmlFile = "packagedirectory.xml";
 saxParser.parse(new File(xmlFile), parser);
 assertTrue(SAXDirParser.acceptsLineNumber());
 }

 @Test
 public void testAcceptsLineNumberWhenFalse() {
 SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
 SAXDirParser parser = new SAXDirParser();
 String xmlFile = "packagedirectory_acceptsLineNumber_false.xml"; // A file that defines "acceptsLineNumber" as "No"
 try {
 saxParser.parse(new File(xmlFile), parser);
 } catch (Exception e) {
 // Expected
 }
 assertFalse(SAXDirParser.acceptsLineNumber());
 }

 @Test(expected = NullPointerException.class)
 public void testLineNumberParameterWhenDirectoryIsEmpty() {
 SAXDirParser.lineNumberParameter();
 }

 @Test
 public void testLineNumberParameter() {
 SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
 SAXDirParser parser = new SAXDirParser();
 String xmlFile = "packagedirectory.xml";
 saxParser.parse(new File(xmlFile), parser);
 String expectedLineNumberParam = "56";
 assertEquals(expectedLineNumberParam, SAXDirParser.lineNumberParameter());
 }
}