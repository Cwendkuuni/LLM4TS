import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;
import static org.junit.Assert.*;
import com.jstevh.viewer.SAXDirParser;
import com.jstevh.viewer.SAXParserFactory;
import com.jstevh.viewer.XMLReader;
import java.io.File;
import java.io.IOException;

public class SAXDirParserTest {

 private SAXDirParser parser;
 private HashMap<String, String> expectedDirectory;
 private StringList expectedLocalPackages;
 private String expectedLocalBrowser;
 private String expectedEditor;
 private boolean expectedLineNumAccepted;
 private String expectedLineNumParam;

 @Before
 public void setup() {
 expectedDirectory = new HashMap<>();
 expectedDirectory.put("java.lang", "path/to/java.lang");
 expectedDirectory.put("javax.swing.border", "path/to/javax.swing.border");
 expectedLocalPackages = new StringList();
 expectedLocalPackages.add("com.local.package1");
 expectedLocalPackages.add("com.local.package2");
 expectedLocalBrowser = "Firefox";
 expectedEditor = "Notepad++";
 expectedLineNumAccepted = true;
 expectedLineNumParam = "123";
 }

 @Test
 public void testGetDirectory() throws IOException, SAXException {
 File xmlFile = new File("packagedirectory.xml");
 SAXParserFactory factory = SAXParserFactory.newInstance();
 XMLReader reader = factory.newSAXParser().getXMLReader();
 reader.setContentHandler(parser = new SAXDirParser());
 reader.parse(xmlFile);
 HashMap<String, String> actualDirectory = SAXDirParser.getDirectory();
 assertEquals(expectedDirectory, actualDirectory);
 }

 @Test(expected = IllegalStateException.class)
 public void testGetDirectoryBeforeParse() {
 HashMap<String, String> actualDirectory = SAXDirParser.getDirectory();
 fail("Should throw IllegalStateException before parse");
 }

 @Test
 public void testGetLocalPackages() {
 File xmlFile = new File("packagedirectory.xml");
 SAXParserFactory factory = SAXParserFactory.newInstance();
 XMLReader reader = factory.newSAXParser().getXMLReader();
 reader.setContentHandler(parser = new SAXDirParser());
 reader.parse(xmlFile);
 StringList actualLocalPackages = SAXDirParser.getLocalPackages();
 assertEquals(expectedLocalPackages, actualLocalPackages);
 }

 @Test(expected = NullPointerException.class)
 public void testGetLocalPackagesWhenEmpty() {
 StringList actualLocalPackages = SAXDirParser.getLocalPackages();
 fail("Should throw NullPointerException when empty");
 }

 @Test
 public void testGetLocalBrowser() {
 File xmlFile = new File("packagedirectory.xml");
 SAXParserFactory factory = SAXParserFactory.newInstance();
 XMLReader reader = factory.newSAXParser().getXMLReader();
 reader.setContentHandler(parser = new SAXDirParser());
 reader.parse(xmlFile);
 String actualLocalBrowser = SAXDirParser.getLocalBrowser();
 assertEquals(expectedLocalBrowser, actualLocalBrowser);
 }

 @Test
 public void testGetEditor() {
 File xmlFile = new File("packagedirectory.xml");
 SAXParserFactory factory = SAXParserFactory.newInstance();
 XMLReader reader = factory.newSAXParser().getXMLReader();
 reader.setContentHandler(parser = new SAXDirParser());
 reader.parse(xmlFile);
 String actualEditor = SAXDirParser.getEditor();
 assertEquals(expectedEditor, actualEditor);
 }

 @Test
 public void testAcceptsLineNumber() {
 File xmlFile = new File("packagedirectory.xml");
 SAXParserFactory factory = SAXParserFactory.newInstance();
 XMLReader reader = factory.newSAXParser().getXMLReader();
 reader.setContentHandler(parser = new SAXDirParser());
 reader.parse(xmlFile);
 boolean actualLineNumAccepted = SAXDirParser.acceptsLineNumber();
 assertEquals(expectedLineNumAccepted, actualLineNumAccepted);
 }

 @Test
 public void testLineNumberParameter() {
 File xmlFile = new File("packagedirectory.xml");
 SAXParserFactory factory = SAXParserFactory.newInstance();
 XMLReader reader = factory.newSAXParser().getXMLReader();
 reader.setContentHandler(parser = new SAXDirParser());
 reader.parse(xmlFile);
 String actualLineNumParam = SAXDirParser.lineNumberParameter();
 assertEquals(expectedLineNumParam, actualLineNumParam);
 }
}