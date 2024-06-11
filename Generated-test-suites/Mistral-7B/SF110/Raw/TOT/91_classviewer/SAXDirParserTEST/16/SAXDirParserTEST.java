import org.junit.Test;
import org.xml.sax.SAXException;
import static org.junit.Assert.*;
import com.jstevh.viewer.SAXDirParser;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.FileNotFoundException;
import java.io.IOException;

public class SAXDirParserTest {

 @Test
 public void testGetDirectory() throws Exception {
 SAXParserFactory factory = SAXParserFactory.newInstance();
 SAXParser saxParser = factory.newSAXParser();
 SAXDirParser handler = new SAXDirParser();
 saxParser.setContentHandler(handler);
 saxParser.parse("src/test/resources/packagedirectory.xml");

 HashMap<String, String> expectedDirectory = new HashMap<>();
 expectedDirectory.put("java.lang", "/java/java.lang/");
 expectedDirectory.put("javax.swing.border", "/javax/javax.swing/javax.swing.border/");

 HashMap<String, String> actualDirectory = handler.getDirectory();
 assertEquals(expectedDirectory, actualDirectory);
 }

 @Test(expected = NullPointerException.class)
 public void testGetDirectory_whenXmlFileNotFound() throws Exception {
 SAXParserFactory factory = SAXParserFactory.newInstance();
 SAXParser saxParser = factory.newSAXParser();
 SAXDirParser handler = new SAXDirParser();
 saxParser.setContentHandler(handler);
 saxParser.parse("non_existent_file.xml");

 HashMap<String, String> actualDirectory = handler.getDirectory();
 assertNull(actualDirectory);
 }

 @Test
 public void testGetLocalPackages() throws Exception {
 SAXParserFactory factory = SAXParserFactory.newInstance();
 SAXParser saxParser = factory.newSAXParser();
 SAXDirParser handler = new SAXDirParser();
 saxParser.setContentHandler(handler);
 saxParser.parse("src/test/resources/packagedirectory.xml");

 StringList expectedLocalPackages = new StringList();
 expectedLocalPackages.add("java");
 expectedLocalPackages.add("javax.swing");

 StringList actualLocalPackages = handler.getLocalPackages();
 assertEquals(expectedLocalPackages, actualLocalPackages);
 }

 @Test(expected = NullPointerException.class)
 public void testGetLocalPackages_whenNoLocalPackagesFound() throws Exception {
 SAXParserFactory factory = SAXParserFactory.newInstance();
 SAXParser saxParser = factory.newSAXParser();
 SAXDirParser handler = new SAXDirParser();
 saxParser.setContentHandler(handler);
 saxParser.parse("src/test/resources/packagedirectory_empty.xml");

 StringList actualLocalPackages = handler.getLocalPackages();
 assertNull(actualLocalPackages);
 }

 @Test
 public void testGetLocalBrowser() throws Exception {
 SAXParserFactory factory = SAXParserFactory.newInstance();
 SAXParser saxParser = factory.newSAXParser();
 SAXDirParser handler = new SAXDirParser();
 saxParser.setContentHandler(handler);
 saxParser.parse("src/test/resources/packagedirectory.xml");

 String expectedLocalBrowser = "Firefox";
 String actualLocalBrowser = handler.getLocalBrowser();
 assertEquals(expectedLocalBrowser, actualLocalBrowser);
 }

 @Test(expected = IllegalStateException.class)
 public void testGetLocalBrowser_whenNoBrowserSpecified() throws Exception {
 SAXParserFactory factory = SAXParserFactory.newInstance();
 SAXParser saxParser = factory.newSAXParser();
 SAXDirParser handler = new SAXDirParser();
 saxParser.setContentHandler(handler);
 saxParser.parse("src/test/resources/packagedirectory_no_browser.xml");

 String actualLocalBrowser = handler.getLocalBrowser();
 fail("Expected IllegalStateException but got " + actualLocalBrowser);
 }

 @Test
 public void testAcceptsLineNumber() throws Exception {
 SAXParserFactory factory = SAXParserFactory.newInstance();
 SAXParser saxParser = factory.newSAXParser();
 SAXDirParser handler = new SAXDirParser();
 saxParser.setContentHandler(handler);
 saxParser.parse("src/test/resources/packagedirectory.xml");

 boolean expectedAcceptsLineNumber = true;
 boolean actualAcceptsLineNumber = handler.acceptsLineNumber();
 assertEquals(expectedAcceptsLineNumber, actualAcceptsLineNumber);
 }

 @Test
 public void testAcceptsLineNumber_whenNoLineNumberParamSpecified() throws Exception {
 SAXParserFactory factory = SAXParserFactory.newInstance();SAXParser saxParser = factory.newSAXParser();
 SAXDirParser handler = new SAXDirParser();
 saxParser.setContentHandler(handler);
 saxParser.parse("src/test/resources/packagedirectory_no_line_number.xml");

 boolean actualAcceptsLineNumber = handler.acceptsLineNumber();
 assertFalse(actualAcceptsLineNumber);
 }

 @Test
 public void testLineNumberParameter() throws Exception {
 SAXParserFactory factory = SAXParserFactory.newInstance();
 SAXParser saxParser = factory.newSAXParser();
 SAXDirParser handler = new SAXDirParser();
 saxParser.setContentHandler(handler);
 saxParser.parse("src/test/resources/packagedirectory.xml");

 String expectedLineNumberParam = "123";
 String actualLineNumberParam = handler.lineNumberParameter();
 assertEquals(expectedLineNumberParam, actualLineNumberParam);
 }

 @Test(expected = IllegalStateException.class)
 public void testLineNumberParameter_whenNoLineNumberParamSpecified() throws Exception {
 SAXParserFactory factory = SAXParserFactory.newInstance();
 SAXParser saxParser = factory.newSAXParser();
 SAXDirParser handler = new SAXDirParser();
 saxParser.setContentHandler(handler);
 saxParser.parse("src/test/resources/packagedirectory_no_line_number_param.xml");

 String actualLineNumberParam = handler.lineNumberParameter();
 fail("Expected IllegalStateException but got " + actualLineNumberParam);
 }
}