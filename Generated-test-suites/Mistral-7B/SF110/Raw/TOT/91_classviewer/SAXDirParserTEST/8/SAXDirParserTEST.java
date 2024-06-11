import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;
import static org.junit.Assert.*;
import com.jstevh.viewer.SAXDirParser;
import com.jstevh.viewer.SAXParserFactory;
import com.jstevh.viewer.StringList;
import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;

public class SAXDirParserTest {

 private SAXDirParser parser;
 private String xmlFilePath = "packagedirectory.xml";

 @Before
 public void setUp() throws ParserConfigurationException, SAXException, IOException {
 parser = new SAXDirParser();
 SAXParserFactory.newInstance().newSAXParser().setContentHandler(parser);
 SAXParserFactory.newInstance().newSAXParser().parse(new File(xmlFilePath));
 }

 @Test
 public void testGetDirectory() {
 HashMap<String, String> expectedDirectory = new HashMap<>();
 expectedDirectory.put("java.lang", "java.lang");
 expectedDirectory.put("javax.swing.border", "javax.swing.border");
 HashMap<String, String> actualDirectory = SAXDirParser.getDirectory();
 assertEquals(expectedDirectory, actualDirectory);
 }

 @Test(expected = NullPointerException.class)
 public void testGetDirectoryWhenDirectoryIsEmpty() {
 HashMap<String, String> actualDirectory = SAXDirParser.getDirectory();
 assertNull(actualDirectory);
 }

 @Test
 public void testGetLocalPackages() {
 StringList expectedLocalPackages = new StringList();
 expectedLocalPackages.add("java.lang");
 StringList actualLocalPackages = SAXDirParser.getLocalPackages();
 assertEquals(expectedLocalPackages, actualLocalPackages);
 }

 @Test(expected = NullPointerException.class)
 public void testGetLocalPackagesWhenNoLocalPackages() {
 StringList actualLocalPackages = SAXDirParser.getLocalPackages();
 assertNull(actualLocalPackages);
 }

 @Test
 public void testGetLocalBrowser() {
 String expectedLocalBrowser = "localBrowserValue";
 String actualLocalBrowser = SAXDirParser.getLocalBrowser();
 assertEquals(expectedLocalBrowser, actualLocalBrowser);
 }

 @Test(expected = NullPointerException.class)
 public void testGetLocalBrowserWhenNoLocalBrowser() {
 String actualLocalBrowser = SAXDirParser.getLocalBrowser();
 assertNull(actualLocalBrowser);
 }

 @Test
 public void testAcceptsLineNumber() {
 boolean expectedAcceptsLineNumber = true;
 boolean actualAcceptsLineNumber = SAXDirParser.acceptsLineNumber();
 assertEquals(expectedAcceptsLineNumber, actualAcceptsLineNumber);
 }

 @Test
 public void testAcceptsLineNumberWhenFalse() {
 boolean expectedAcceptsLineNumber = false;
 SAXDirParser.lineNumAccepted = false;
 boolean actualAcceptsLineNumber = SAXDirParser.acceptsLineNumber();
 assertEquals(expectedAcceptsLineNumber, actualAcceptsLineNumber);
 }

 @Test
 public void testLineNumberParameter() {
 String expectedLineNumberParameter = "lineNumberParamValue";
 String actualLineNumberParameter = SAXDirParser.lineNumberParameter();
 assertEquals(expectedLineNumberParameter, actualLineNumberParameter);
 }

 @Test
 public void testLineNumberParameterWhenEmpty() {
 String actualLineNumberParameter = SAXDirParser.lineNumberParameter();
 assertEquals("", actualLineNumberParameter);
 }
}