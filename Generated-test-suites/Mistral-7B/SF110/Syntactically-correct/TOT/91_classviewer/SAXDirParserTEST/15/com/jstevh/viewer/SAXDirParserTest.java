import org.junit.Test;
import static org.junit.Assert.*;
import com.jstevh.viewer.SAXDirParser;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

public class SAXDirParserTest {

 @Test
 public void testParsingValidXML() throws Exception {
 SAXParserFactory factory = SAXParserFactory.newInstance();
 SAXParser saxParser = factory.newSAXParser();
 XMLReader xmlReader = saxParser.getXMLReader();
 SAXDirParser handler = new SAXDirParser();
 InputStream inputStream = new File("src/test/resources/validXML.xml").getInputStream();
 xmlReader.parse(new InputStreamReader(inputStream));

 // Test getDirectory() method
 HashMap<String, String> expectedDirectory = new HashMap<>();
 expectedDirectory.put("java.lang", "path/to/java.lang");
 expectedDirectory.put("javax.swing.border", "path/to/javax.swing.border");
 assertEquals(expectedDirectory, SAXDirParser.getDirectory());

 // Test getLocalPackages() method
 List<String> expectedLocalPackages = List.of("package1", "package2");
 assertEquals(expectedLocalPackages, SAXDirParser.getLocalPackages());

 // Test getLocalBrowser() method
 String expectedLocalBrowser = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3094.110 Safari/56.0";
 assertEquals(expectedLocalBrowser, SAXDirParser.getLocalBrowser());

 // Test acceptsLineNumber() method
 assertTrue(SAXDirParser.acceptsLineNumber());

 // Test lineNumberParameter() method
 String expectedLineNumberParameter = "56";
 assertEquals(expectedLineNumberParameter, SAXDirParser.lineNumberParameter());
 }

 @Test(expected = AssertionError.class)
 public void testParsingInvalidXML() throws Exception {
 SAXParserFactory factory = SAXParserFactory.newInstance();
 SAXParser saxParser = factory.newSAXParser();
 XMLReader xmlReader = saxParser.getXMLReader();
 SAXDirParser handler = new SAXDirParser();
 InputStream inputStream = new File("src/test/resources/invalidXML.xml").getInputStream();
 xmlReader.parse(new InputStreamReader(inputStream));
 fail("Expected an AssertionError due to invalid XML");
 }
}