import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SAXDirParserTest {

 private SAXDirParser parser;

 @Test
 public void testGetDirectory() throws Exception {
  parser = new SAXDirParser();
  File file = new File("packageDirectory.xml");
  SAXParserFactory factory = SAXParserFactory.newInstance();
  SAXParser saxParser = factory.newSAXParser();
  XMLReader xmlReader = saxParser.getXMLReader();
  xmlReader.setContentHandler(parser);
  xmlReader.parse(file);

  Map<String, String> expectedDirectory = new HashMap<>();
  expectedDirectory.put("java.lang", "path/to/java.lang");
  expectedDirectory.put("javax.swing.border", "path/to/javax.swing.border");

  Map<String, String> actualDirectory = parser.getDirectory();

  Assert.assertEquals(expectedDirectory, actualDirectory);
 }

 @Test(expected = SAXException.class)
 public void testIncorrectXMLFile() throws Exception {
  parser = new SAXDirParser();
  File file = new File("invalidFile.xml");
  SAXParserFactory factory = SAXParserFactory.newInstance();
  SAXParser saxParser = factory.newSAXParser();
  XMLReader xmlReader = saxParser.getXMLReader();
  xmlReader.setContentHandler(parser);
  xmlReader.parse(file);
 }

 @Test
 public void testEmptyDirectory() throws Exception {
  parser = new SAXDirParser();
  parser.setDirectory(new HashMap<>());
  Assert.assertEquals(parser.getDirectory().size(), 0);
 }

 @Test
 public void testGetLocalPackages() {
  parser = new SAXDirParser();
  parser.setDirectory(new HashMap<String, String>() {{
    put("java.lang", "path/to/java.lang");
    put("javax.swing.border", "path/to/javax.swing.border");
    put("com.custom.package", "path/to/custompackage");
  }});
  parser.setLocalPackages(new StringList());
  parser.local = true;

  StringList expectedLocalPackages = new StringList();
  expectedLocalPackages.add("com.custom.package");

  StringList actualLocalPackages = parser.getLocalPackages();

  Assert.assertEquals(expectedLocalPackages, actualLocalPackages);
 }

 @Test
 public void testGetLocalPackages_empty() {
  parser = new SAXDirParser();
  parser.setDirectory(new HashMap<String, String>() {{
    put("java.lang", "path/to/java.lang");
    put("javax.swing.border", "path/to/javax.swing.border");
  }});
  parser.setLocalPackages(new StringList());

  Assert.assertNull(parser.getLocalPackages());
 }

 @Test
 public void testGetLocalBrowser() {
  parser = new SAXDirParser();
  parser.check = true;
  parser.level = 0;
  parser.webData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
          "<Group>" +
          " <BrowserLoc>Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:75.0) Gecko/20100101 Firefox/75.0</BrowserLoc>" +
          "</Group>";

  String expectedLocalBrowser = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:75.0) Gecko/20100101 Firefox/75.0";

  String actualLocalBrowser = parser.getLocalBrowser();

  Assert.assertEquals(expectedLocalBrowser, actualLocalBrowser);
 }

 @Test
 public void testGetEditor() {
  parser = new SAXDirParser();
  parser.check = true;
  parser.level = 10;
  parser.webData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
          "<Group>" +
          " <Editor>Notepad++</Editor>" +
          "</Group>";

  String expectedEditor = "Notepad++";

  String actualEditor = parser.getEditor();

  Assert.assertEquals(expectedEditor, actualEditor);
 }

 @Test
 public void testAcceptsLineNumber() {
  parser = new SAXDirParser();
  parser.check = true;
  parser.level = 11;
  parser.webData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
          "<Group>" +
          " <acceptsLineNumber>Yes</acceptsLineNumber>" +
          "</Group>";

  boolean expectedAcceptsLineNumber = true;

  boolean actualAcceptsLineNumber = parser.acceptsLineNumber();

  Assert.assertEquals(expectedAcceptsLineNumber, actualAcceptsLineNumber);
 }

 @Test
 public void testAcceptsLineNumber_false() {
  parser = new SAXDirParser();
  parser.check = true;
  parser.level = 11;
  parser.webData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
          "<Group>" +
          " <acceptsLineNumber>No</acceptsLineNumber>" +
          "</Group>";

  boolean expectedAcceptsLineNumber = false;

  boolean actualAcceptsLineNumber = parser.acceptsLineNumber();

  Assert.assertEquals(expectedAcceptsLineNumber, actualAcceptsLineNumber);
 }

 @Test
 public void testLineNumberParameter() {
  parser = new SAXDirParser();
  parser.check = true;
  parser.level = 12;
  parser.webData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
          "<Group>" +
          " <parameter>lineNumber</parameter>" +
          "</Group>";

  String expectedLineNumberParameter = "lineNumber";

  String actualLineNumberParameter = parser.lineNumberParameter();

  Assert.assertEquals(expectedLineNumberParameter, actualLineNumberParameter);
 }

 @Test
 public void testLineNumberParameter_empty() {
  parser = new SAXDirParser();
  parser.check = true;
  parser.level = 12;
  parser.webData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
          "<Group>" +
          "</Group>";

  String expectedLineNumberParameter = "";

  String actualLineNumberParameter = parser.lineNumberParameter();

  Assert.assertEquals(expectedLineNumberParameter, actualLineNumberParameter);
 }
}