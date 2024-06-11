import org.junit.Test;
import static org.junit.Assert.*;
import com.jstevh.viewer.SAXDirParser;
import org.xml.sax.helpers.DefaultHandler;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

public class SAXDirParserTest {

 private SAXDirParser saxDirParser;
 private Map<String, String> expectedDirectory;
 private List<String> expectedLocalPackages;
 private String expectedLocalBrowser;
 private String expectedEditor;
 private boolean expectedAcceptsLineNumber;
 private String expectedLineNumberParameter;

 @Before
 public void setUp() {
 saxDirParser = new SAXDirParser();
 expectedDirectory = new HashMap<>();
 expectedDirectory.put("java.lang", "location1");
 expectedDirectory.put("javax.swing.border", "location2");
 expectedLocalPackages = new ArrayList<>();
 expectedLocalPackages.add("package1");
 expectedLocalPackages.add("package2");
 expectedLocalBrowser = "localBrowser";
 expectedEditor = "editor";
 expectedAcceptsLineNumber = true;
 expectedLineNumberParameter = "param";
 }

 @Test
 public void testGetWebData() {
 assertEquals("", saxDirParser.getWebData());
 }

 @Test
 public void testStartElement_BrowserLoc() {
 saxDirParser.startElement("", "BrowserLoc", "BrowserLoc", null);
 assertTrue(saxDirParser.isCheck());
 assertEquals(0, saxDirParser.getLevel());
 }

 @Test(expected = SAXException.class)
 public void testStartElement_BrowserLoc_Exception() {
 saxDirParser.startElement("", "BrowserLoc", "BrowserLoc", null);
 saxDirParser.startElement("", "BrowserLoc", "BrowserLoc", null);
 }

 @Test
 public void testStartElement_Editor() {
 saxDirParser.startElement("", "Editor", "Editor", null);
 assertTrue(saxDirParser.isCheck());
 assertEquals(10, saxDirParser.getLevel());
 }

 @Test(expected = SAXException.class)
 public void testStartElement_Editor_Exception() {
 saxDirParser.startElement("", "Editor", "Editor", null);
 saxDirParser.startElement("", "Editor", "Editor", null);
 }

 @Test
 public void testStartElement_acceptsLineNumber() {
 saxDirParser.startElement("", "acceptsLineNumber", "acceptsLineNumber", null);
 assertTrue(saxDirParser.isCheck());
 assertEquals(11, saxDirParser.getLevel());
 }

 @Test
 public void testStartElement_acceptsLineNumber_Yes() {
 saxDirParser.startElement("", "acceptsLineNumber", "acceptsLineNumber", null);
 saxDirParser.characters(new char[]{'Y'}, 0, 1);
 assertTrue(saxDirParser.isLineNumAccepted());
 }

 @Test
 public void testStartElement_acceptsLineNumber_No() {
 saxDirParser.startElement("", "acceptsLineNumber", "acceptsLineNumber", null);
 saxDirParser.characters(new char[]{'N'}, 0, 1);
 assertFalse(saxDirParser.isLineNumAccepted());
 }

 @Test
 public void testStartElement_parameter() {
 saxDirParser.startElement("", "parameter", "parameter", null);
 assertTrue(saxDirParser.isCheck());
 assertEquals(12, saxDirParser.getLevel());
 }

 @Test
 public void testStartElement_parameter_Param() {
 saxDirParser.startElement("", "parameter", "parameter", null);
 saxDirParser.characters(new char[]{'p', 'a', 'r', 'a', 'm'}, 0, 5);
 assertEquals("param", saxDirParser.getLineNumberParameter());
 }

 @Test
 public void testStartElement_Group() {
 saxDirParser.startElement("", "Group", "Group", null);
 assertFalse(saxDirParser.isCheck());
 }

 @Test
 public void testStartElement_Web() {
 saxDirParser.startElement("", "Web", "Web", null);
 assertTrue(saxDirParser.isCheck());
 assertEquals(30, saxDirParser.getLevel());
 }

 @Test
 public void testStartElement_Local() {
 saxDirParser.startElement("", "Local", "Local", null);
 assertTrue(saxDirParser.isCheck());
 assertEquals(31, saxDirParser.getLevel());
 }

 @Test
 public void testStartElement_Names() {
 saxDirParser.startElement("", "Names", "Names", null);
 assertFalse(saxDirParser.isCheck());
 }

 @Test
 public void testStartElement_pkg() {
 saxDirParser.startElement("", "pkg", "pkg", null);
 assertEquals(4, saxDirParser.getLevel());
 }

 @Test
 public void testCharacters_java_lang() {
 saxDirParser.startElement("", "", "", null);
 saxDirParser.characters(new char[]{'j', 'a', 'v', 'a', '.', 'l', 'a', 'n', 'g', '.', 'java', '.'}, 0, 16);
 assertEquals("java.lang", saxDirParser.getLocation());
 }

 @Test
 public void testCharacters_javax_swing_border() {
 saxDirParser.startElement("", "", "", null);
 saxDirParser.characters(new char[]{'j', 'e', 'r', 's', 'e', '.', 's', 'w', 'i', 'n', 'g', '.', 'b', 'o', 'r', 'd', 'e', 'r'}, 0, 18);
 assertEquals("javax.swing.border", saxDirParser.getLocation());
 }

 @Test
 public void testCharacters_localBrowser() {
 saxDirParser.startElement("", "", "", null);
 saxDirParser.characters(new char[]{'l', 'o', 'c', 'a', 'l', 'B', 'r', 'o', 'w', 's', 'e', 'r'}, 0, 12);
 assertEquals(expectedLocalBrowser, saxDirParser.getLocalBrowser());
 }

 @Test
 public void testCharacters_editor() {
 saxDirParser.startElement("", "", "", null);
 saxDirParser.characters(new char[]{'e', 'd', 'i', 't', 'o', 'r'}, 0, 6);
 assertEquals(expectedEditor, saxDirParser.getEditor());
 }

 @Test
 public void testCharacters_directory_java_lang() {
 saxDirParser.startElement("", "pkg", "", null);
 saxDirParser.characters(new char[]{'j', 'a', 'v', 'a', '.', 'l', 'a', 'n', 'g', '.'}, 0, 11);
 assertEquals("java.lang", saxDirParser.getLocation());
 assertEquals("location1", saxDirParser.getDirectory().get("java.lang"));
 }

 @Test
 public void testCharacters_directory_javax_swing_border() {
 saxDirParser.startElement("", "pkg", "", null);
 saxDirParser.characters(new char[]{'j', 'e', 'r', 's', 'e', '.', 's', 'w', 'i', 'n', 'g', '.', 'b', 'o', 'r', 'd', 'e', 'r'}, 0, 18);
 assertEquals("javax.swing.border", saxDirParser.getLocation());
 assertEquals("location2", saxDirParser.getDirectory().get("javax.swing.border"));
 }

 @Test
 public void testGetDirectory() {
 assertEquals(expectedDirectory, SAXDirParser.getDirectory());
 }

 @Test
 public void testGetLocalPackages() {
 assertEquals(expectedLocalPackages, SAXDirParser.getLocalPackages());
 }

 @Test
 public void testAcceptsLineNumber() {
 assertEquals(expectedAcceptsLineNumber, SAXDirParser.acceptsLineNumber());
 }

 @Test
 public void testLineNumberParameter() {
 assertEquals(expectedLineNumberParameter, SAXDirParser.lineNumberParameter());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetDirectory_Mutable() {
 SAXDirParser.getDirectory().put("test", "test");
 }

 @Test(expected = NullPointerException.class)
 public void testGetDirectory_Null() {
 SAXDirParser.directory = null;
 }

 @Test(expected = NoSuchElementException.class)
 public void testGetDirectory_NoSuchElement() {
 assertEquals(null, SAXDirParser.getDirectory().get("javax.swing.jtable"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetDirectory_IllegalArgument() {
 SAXDirParser.getDirectory().put("", "");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetLocalPackages_Mutable() {
 SAXDirParser.getLocalPackages().add("test");
 }

 @Test(expected = NullPointerException.class)
 public void testGetLocalPackages_Null() {
 SAXDirParser.localPackages = null;
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetLocalPackages_IndexOutOfBounds() {
 assertEquals(null, SAXDirParser.getLocalPackages().get(0));
 }

 @Test
 public void testEndElement_Group() {
 saxDirParser.startElement("", "Group", "Group", null);
 saxDirParser.endElement("", "Group", "Group");
 assertFalse(saxDirParser.isCheck());
 }
}