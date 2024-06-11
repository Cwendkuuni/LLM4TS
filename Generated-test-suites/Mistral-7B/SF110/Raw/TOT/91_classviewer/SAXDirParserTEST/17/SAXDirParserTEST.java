import org.junit.Test;
import org.xml.sax.SAXException;
import static org.junit.Assert.*;
import com.jstevh.viewer.SAXDirParser;
import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;

public class SAXDirParserTest {

 @Test
 public void testGetDirectory() throws ParserConfigurationException, SAXException, IOException {
 SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
 XMLInputFactory factory = parser.getXMLInputFactory();
 File xmlFile = new File("packagedirectory.xml");
 InputSource inputSource = new InputSource(factory.createXMLDocumentSource(xmlFile));
 SAXDirParser saxDirParser = new SAXDirParser();
 parser.setContentHandler(saxDirParser);
 parser.parse(inputSource);

 Map<String, String> expectedDirectory = new HashMap<>();
 expectedDirectory.put("java.lang", "location1");
 expectedDirectory.put("javax.swing.border", "location2");

 Map<String, String> actualDirectory = SAXDirParser.getDirectory();
 assertEquals(expectedDirectory, actualDirectory);
 }

 @Test
 public void testGetLocalPackages() {
 SAXDirParser saxDirParser = new SAXDirParser();
 SAXDirParser.directory.put("java.lang", "location1");
 SAXDirParser.directory.put("javax.swing.border", "location2");
 SAXDirParser.localPackages.add("package1");
 SAXDirParser.localPackages.add("package2");
 StringList expectedLocalPackages = new StringList();
 expectedLocalPackages.add("package1");
 expectedLocalPackages.add("package2");

 StringList actualLocalPackages = SAXDirParser.getLocalPackages();
 assertEquals(expectedLocalPackages, actualLocalPackages);
 }

 @Test
 public void testGetLocalBrowser() {
 SAXDirParser saxDirParser = new SAXDirParser();
 saxDirParser.localBrowser = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3094.110 Safari/56.0";
 String expectedLocalBrowser = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3094.110 Safari/56.0";

 String actualLocalBrowser = SAXDirParser.getLocalBrowser();
 assertEquals(expectedLocalBrowser, actualLocalBrowser);
 }

 @Test
 public void testAcceptsLineNumber() {
 SAXDirParser saxDirParser = new SAXDirParser();
 saxDirParser.lineNumAccepted = true;

 boolean expectedAcceptsLineNumber = true;
 boolean actualAcceptsLineNumber = SAXDirParser.acceptsLineNumber();
 assertEquals(expectedAcceptsLineNumber, actualAcceptsLineNumber);
 }

 @Test
 public void testLineNumberParameter() {
 SAXDirParser saxDirParser = new SAXDirParser();
 saxDirParser.lineNumParam = "123";

 String expectedLineNumberParameter = "123";
 String actualLineNumberParameter = SAXDirParser.lineNumberParameter();
 assertEquals(expectedLineNumberParameter, actualLineNumberParameter);
 }

 @Test(expected = IllegalStateException.class)
 public void testGetDirectoryWhenXMLFileIsEmpty() throws ParserConfigurationException, SAXException, IOException {
 SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
 XMLInputFactory factory = parser.getXMLInputFactory();
 File xmlFile = new File("empty.xml");
 InputSource inputSource = new InputSource(factory.createXMLDocumentSource(xmlFile));
 SAXDirParser saxDirParser = new SAXDirParser();
 parser.setContentHandler(saxDirParser);
 parser.parse(inputSource);

 Map<String, String> actualDirectory = SAXDirParser.getDirectory();
 assertTrue(actualDirectory.isEmpty());
 }
}