import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;
import static org.junit.Assert.*;
import com.jstevh.viewer.SAXDirParser;
import com.jstevh.viewer.SAXParserFactory;
import com.jstevh.viewer.XMLReader;
import com.jstevh.viewer.StringList;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class SAXDirParserTest {

 private SAXDirParser parser;
 private Map<String, String> directory;
 private StringList localPackages;

 @Before
 public void setUp() {
 parser = new SAXDirParser();
 directory = new HashMap<>();
 localPackages = new StringList();
 }

 @Test
 public void testGetDirectory() throws Exception {
 InputStream is = new FileInputStream(new File("path/to/packagedirectory.xml"));
 SAXParserFactory factory = SAXParserFactory.newInstance();
 XMLReader xmlReader = factory.newSAXParser().getXMLReader();
 xmlReader.setContentHandler(parser);
 xmlReader.parse(is);
 is.close();

 directory = parser.getDirectory();

 assertEquals("java.lang", directory.get("java.lang"));
 assertEquals("javax.swing.border", directory.get("javax.swing.border"));
 assertEquals(null, directory.get("nonexistentpackage"));
 }

 @Test
 public void testGetLocalPackages() {
 parser.local = true;
 parser.location = "local directory";
 parser.level = 4;
 parser.characters('p', 0, "java.util.".length());
 parser.characters('.', 0, 1);
 parser.characters('.', 1, 1);
 parser.characters('.', 2, 1);
 parser.characters('.', 3, 1);
 parser.characters('.', 4, 1);
 parser.characters('.', 5, 1);
 parser.characters('.', 6, 1);
 parser.characters('.', 7, 1);
 parser.characters('.', 8, 1);
 parser.characters('M', 9, 1);
 parser.characters('y', 10, 1);
 parser.characters('Util', 11, 3);
 parser.check = false;
 parser.level = -1;

 localPackages = SAXDirParser.getLocalPackages();

 assertNotNull(localPackages);
 assertTrue(localPackages.contains("java.util"));
 }

 @Test(expected = NullPointerException.class)
 public void testGetLocalPackagesNullInput() {
 parser = new SAXDirParser();
 localPackages = SAXDirParser.getLocalPackages();
 }

 @Test
 public void testGetLocalBrowser() throws Exception {
 InputStream is = new FileInputStream(new File("path/to/packagedirectory.xml"));
 SAXParserFactory factory = SAXParserFactory.newInstance();
 XMLReader xmlReader = factory.newSAXParser().getXMLReader();
 xmlReader.setContentHandler(parser);
 xmlReader.parse(is);
 is.close();

 assertEquals("Internet Explorer", parser.localBrowser);
 }

 @Test
 public void testGetEditor() throws Exception {
 InputStream is = new FileInputStream(new File("path/to/packagedirectory.xml"));
 SAXParserFactory factory = SAXParserFactory.newInstance();
 XMLReader xmlReader = factory.newSAXParser().getXMLReader();
 xmlReader.setContentHandler(parser);
 xmlReader.parse(is);
 is.close();

 assertEquals("Notepad", parser.file_editor);
 }

 @Test
 public void testAcceptsLineNumber() throws Exception {
 InputStream is = new FileInputStream(new File("path/to/packagedirectory.xml"));
 SAXParserFactory factory = SAXParserFactory.newInstance();
 XMLReader xmlReader = factory.newSAXParser().getXMLReader();
 xmlReader.setContentHandler(parser);
 xmlReader.parse(is);
 is.close();

 assertTrue(parser.acceptsLineNumber());
 }

 @Test
 public void testLineNumberParameter() throws Exception {
 InputStream is = new FileInputStream(new File("path/to/packagedirectory.xml"));
 SAXParserFactory factory = SAXParserFactory.newInstance();
 XMLReader xmlReader = factory.newSAXParser().getXMLReader();
 xmlReader.setContentHandler(parser);
 xmlReader.parse(is);
 is.close();

 assertEquals("5", parser.lineNumberParameter());
 }

 @Test(expected = IllegalStateException.class)
 public void testAcceptsLineNumberFalse() throws Exception {
 InputStream is = new FileInputStream(new File("path/to/packagedirectory.xml"));
 SAXParserFactory factory = SAXParserFactory.newInstance();
 XMLReader xmlReader = factory.newSAXParser().getXMLReader();
 xmlReader.setContentHandler(parser);
 xmlReader.parse(is);
 is.close();

 parser.check = false;
 parser.level = 11;
 parser.characters('N'.toString().getBytes()[0], 0, 1);
 parser.check = true;
 parser.level = -1;

 parser.acceptsLineNumber();
 }
}