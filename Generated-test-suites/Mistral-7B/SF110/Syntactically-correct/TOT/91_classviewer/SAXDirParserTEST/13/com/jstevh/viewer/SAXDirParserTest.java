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
import javax.xml.parsers.xml.XMLInputFactory;
import javax.xml.transform.stream.StreamSource;

public class SAXDirParserTest {

 private SAXDirParser parser;

 @Before
 public void setUp() {
 parser = new SAXDirParser();
 }

 @Test
 public void testGetDirectory() throws Exception {
 // Test with valid XML file
 File inputFile = new File("testData/validXML.xml");
 SAXParserFactory factory = SAXParserFactory.newInstance();
 SAXParser saxParser = factory.newSAXParser();
 XMLInputFactory xif = saxParser.getXMLInputFactory();
 xif.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, false);
 InputSource inputSource = new InputSource(new FileInputStream(inputFile));
 saxParser.parse(inputSource, parser);
 Map<String, String> expectedDirectory = new HashMap<>();
 expectedDirectory.put("group1", "location1");
 expectedDirectory.put("group2", "location2");
 expectedDirectory.put("java.lang", "location3");
 assertEquals(expectedDirectory, SAXDirParser.getDirectory());

 // Test with empty XML file
 inputFile = new File("testData/emptyXML.xml");
 SAXParserFactory factoryEmpty = SAXParserFactory.newInstance();
 SAXParser saxParserEmpty = factoryEmpty.newSAXParser();
 XMLInputFactory xifEmpty = saxParserEmpty.getXMLInputFactory();
 InputSource inputSourceEmpty = new InputSource(new FileInputStream(inputFile));
 saxParserEmpty.parse(inputSourceEmpty, parser);
 assertEquals(0, SAXDirParser.getDirectory().size());

 // Test with invalid XML file
 File inputFileInvalid = new File("testData/invalidXML.xml");
 SAXParserFactory factoryInvalid = SAXParserFactory.newInstance();
 SAXParser saxParserInvalid = factoryInvalid.newSAXParser();
 XMLInputFactory xifInvalid = saxParserInvalid.getXMLInputFactory();
 xifInvalid.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, false);
 InputSource inputSourceInvalid = new InputSource(new FileInputStream(inputFileInvalid));
 try {
 saxParserInvalid.parse(inputSourceInvalid, parser);
 fail("Expected SAXException");
 } catch (SAXException e) {
 // Expected
 }
 assertEquals(0, SAXDirParser.getDirectory().size());
 }

 @Test
 public void testGetLocalPackages() {
 // Test with valid XML file
 SAXParserFactory factory = SAXParserFactory.newInstance();
 SAXParser saxParser = factory.newSAXParser();
 XMLInputFactory xif = saxParser.getXMLInputFactory();
 xif.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, false);
 InputSource inputSource = new InputSource(new FileInputStream(new File("testData/validXML.xml")));
 saxParser.parse(inputSource, parser);
 StringList expectedLocalPackages = new StringList();
 expectedLocalPackages.add("pkg1");
 expectedLocalPackages.add("pkg2");
 assertEquals(expectedLocalPackages, SAXDirParser.getLocalPackages());

 // Test with empty XML file
 SAXParserFactory factoryEmpty = SAXParserFactory.newInstance();
 SAXParser saxParserEmpty = factoryEmpty.newSAXParser();
 XMLInputFactory xifEmpty = saxParserEmpty.getXMLInputFactory();
 InputSource inputSourceEmpty = new InputSource(new FileInputStream(new File("testData/emptyXML.xml")));
 saxParserEmpty.parse(inputSourceEmpty, parser);
 assertNull(SAXDirParser.getLocalPackages());

 // Test with invalid XML file
 SAXParserFactory factoryInvalid = SAXParserFactory.newInstance();
 SAXParser saxParserInvalid = factoryInvalid.newSAXParser();
 XMLInputFactory xifInvalid = saxParserInvalid.getXMLInputFactory();
 xifInvalid.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, false);
 InputSource inputSourceInvalid = new InputSource(new FileInputStream(new File("testData/invalidXML.xml")));
 try {
 saxParserInvalid.parse(inputSourceInvalid, parser);
 fail("Expected SAXException");
 } catch (SAXException e) {
 // Expected
 }
 assertNull(SAXDirParser.getLocalPackages());
 }

 @Test
 public void testGetLocalBrowser() {
 // Test with valid XML file
 SAXParserFactory factory = SAXParserFactory.newInstance();
 SAXParser saxParser = factory.newSAXParser();
 XMLInputFactory xif = saxParser.getXMLInputFactory();
 xif.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, false);
 InputSource inputSource = new InputSource(new FileInputStream(new File("testData/validXML.xml")));
 saxParser.parse(inputSource, parser);
 String expectedLocalBrowser = "Mozilla/5.0";
 assertEquals(expectedLocalBrowser, SAXDirParser.getLocalBrowser());

 // Test with empty XML file
 SAXParserFactory factoryEmpty = SAXParserFactory.newInstance();
 SAXParser saxParserEmpty = factoryEmpty.newSAXParser();
 XMLInputFactory xifEmpty = saxParserEmpty.getXMLInputFactory();
 InputSource inputSourceEmpty = new InputSource(new FileInputStream(new File("testData/emptyXML.xml")));
 saxParserEmpty.parse(inputSourceEmpty, parser);
 assertNull(SAXDirParser.getLocalBrowser());

 // Test with invalid XML file
 SAXParserFactory factoryInvalid = SAXParserFactory.newInstance();
 SAXParser saxParserInvalid = factoryInvalid.newSAXParser();
 XMLInputFactory xifInvalid = saxParserInvalid.getXMLInputFactory();
 xifInvalid.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, false);
 InputSource inputSourceInvalid = new InputSource(new FileInputStream(new File("testData/invalidXML.xml")));
 try {
 saxParserInvalid.parse(inputSourceInvalid, parser);
 fail("Expected SAXException");
 } catch (SAXException e) {
 // Expected
 }
 assertNull(SAXDirParser.getLocalBrowser());
 }

 @Test
 public void testAcceptsLineNumber() {
 // Test with valid XML file with line number accepted
 SAXParserFactory factory = SAXParserFactory.newInstance();
 SAXParser saxParser = factory.newSAXParser();
 XMLInputFactory xif = saxParser.getXMLInputFactory();
 xif.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, false);
 InputSource inputSource = new InputSource(new FileInputStream(new File("testData/validXMLWithLineNumberAccepted.xml")));
 saxParser.parse(inputSource, parser);
 assertTrue(SAXDirParser.acceptsLineNumber());

 // Test with valid XML file with line number not accepted
 SAXParserFactory factory2 = SAXParserFactory.newInstance();
 SAXParser saxParser2 = factory2.newSAXParser();
 XMLInputFactory xif2 = saxParser2.getXMLInputFactory();
 xif2.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, false);
 InputSource inputSource2 = new InputSource(new FileInputStream(new File("testData/validXMLWithLineNumberNotAccepted.xml")));
 saxParser2.parse(inputSource2, parser);
 assertFalse(SAXDirParser.acceptsLineNumber());

 // Test with invalid XML file
 SAXParserFactory factoryInvalid = SAXParserFactory.newInstance();
 SAXParser saxParserInvalid = factoryInvalid.newSAXParser();
 XMLInputFactory xifInvalid = saxParserInvalid.getXMLInputFactory();
 xifInvalid.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, false);
 InputSource inputSourceInvalid = new InputSource(new FileInputStream(new File("testData/invalidXML.xml")));
 try {
 saxParserInvalid.parse(inputSourceInvalid, parser);
 fail("Expected SAXException");
 } catch (SAXException e) {
 // Expected
 }
 assertFalse(SAXDirParser.acceptsLineNumber());
 }

 @Test
 public void testLineNumberParameter() {
 // Test with valid XML file with line number parameter
 SAXParserFactory factory = SAXParserFactory.newInstance();
 SAXParser saxParser = factory.newSAXParser();
 XMLInputFactory xif = saxParser.getXMLInputFactory();
 xif.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, false);
 InputSource inputSource = new InputSource(new FileInputStream(new File("testData/validXMLWithLineNumberParameter.xml")));
 saxParser.parse(inputSource, parser);
 assertEquals("123", SAXDirParser.lineNumberParameter());

 // Test with valid XML file with no line number parameter
 SAXParserFactory factory2 = SAXParserFactory.newInstance();
 SAXParser saxParser2 = factory2.newSAXParser();
 XMLInputFactory xif2 = saxParser2.getXMLInputFactory();
 xif2.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, false);
 InputSource inputSource2 = new InputSource(new FileInputStream(new File("testData/validXMLWithNoLineNumberParameter.xml")));
 saxParser2.parse(inputSource2, parser);
 assertNull(SAXDirParser.lineNumberParameter());

 // Test with invalid XML file
 SAXParserFactory factoryInvalid = SAXParserFactory.newInstance();
 SAXParser saxParserInvalid = factoryInvalid.newSAXParser();
 XMLInputFactory xifInvalid = saxParserInvalid.getXMLInputFactory();
 xifInvalid.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, false);
 InputSource inputSourceInvalid = new InputSource(new FileInputStream(new File("testData/invalidXML.xml")));
 try {
 saxParserInvalid.parse(inputSourceInvalid, parser);
 fail("Expected SAXException");
 } catch (SAXException e) {
 // Expected
 }
 assertNull(SAXDirParser.lineNumberParameter());
 }
}