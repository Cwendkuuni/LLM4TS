import org.xml.sax.*;
import org.xml.sax.helpers.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;
import java.util.*;

public class SAXDirParserTest {

 private SAXDirParser parser;
 private StringReader reader;

 @Before
 public void setup() {
 parser = new SAXDirParser();
 String xml = "<Web>\n" +
 " <Local name=\"java.lang\">\n" +
 " <Group>\n" +
 " <BrowserLoc>Firefox</BrowserLoc>\n" +
 " </Group>\n" +
 " <Names>\n" +
 " <pkg>java.lang</pkg>\n" +
 " <pkg>java.util</pkg>\n" +
 " </Names>\n" +
 " <acceptsLineNumber>Yes</acceptsLineNumber>\n" +
 " <parameter>5</parameter>\n" +
 " </Local>\n" +
 " <Local name=\"javax.swing.border\">\n" +
 " <Group>\n" +
 " <BrowserLoc>Chrome</BrowserLoc>\n" +
 " </Group>\n" +
 " <Names>\n" +
 " <pkg>javax.swing</pkg>\n" +
 " <pkg>javax.swing.border</pkg>\n" +
 " </Names>\n" +
 " <acceptsLineNumber>No</acceptsLineNumber>\n" +
 " </Local>\n" +
 " </Web>";
 try {
 reader = new StringReader(xml);
 } catch (IOException e) {
 e.printStackTrace();
 }
}

 @Test
 public void testGetDirectory() throws Exception {
 parser.parse(new InputSource(new StringReader(reader.toString())));
 Map<String, String> expectedDir = new HashMap<>();
 expectedDir.put("java.lang", "java.lang");
 expectedDir.put("javax.swing.border", "javax.swing.border");
 assertEquals(expectedDir, SAXDirParser.getDirectory());
}

 @Test(expected = IllegalStateException.class)
 public void testGetDirectory_empty() throws Exception {
 parser.parse(new InputSource(new StringReader("")));
 assertNull(SAXDirParser.getDirectory());
}

 @Test
 public void testGetLocalPackages() throws Exception {
 parser.parse(new InputSource(new StringReader(reader.toString())));
 StringList expectedPackages = new StringList();
 expectedPackages.add("java.lang");
 expectedPackages.add("javax.swing.border");
 assertEquals(expectedPackages, SAXDirParser.getLocalPackages());
}

 @Test(expected = NullPointerException.class)
 public void testGetLocalPackages_empty() throws Exception {
 parser.parse(new InputSource(new StringReader("")));
 assertNull(SAXDirParser.getLocalPackages());
}

 @Test
 public void testGetLocalBrowser() throws Exception {
 parser.parse(new InputSource(new StringReader(reader.toString())));
 assertEquals("Firefox", SAXDirParser.getLocalBrowser());
}

 @Test
 public void testEditor() throws Exception {
 parser.parse(new InputSource(new StringReader(reader.toString())));
 assertEquals("", SAXDirParser.getEditor());
}

 @Test
 public void testAcceptsLineNumber() throws Exception {
 parser.parse(new InputSource(new StringReader(reader.toString())));
 assertTrue(SAXDirParser.acceptsLineNumber());
}

 @Test
 public void testAcceptsLineNumber_false() throws Exception {
 SAXDirParser parser2 = new SAXDirParser();
 parser2.parse(new InputSource(new StringReader("<Web>\n" +
 " <Local name=\"java.lang\">\n" +
 " <Group>\n" +
 " <BrowserLoc>Firefox</BrowserLoc>\n" +
 " </Group>\n" +
 " <Names>\n" +
 " <pkg>java.lang</pkg>\n" +
 " <pkg>java.util</pkg>\n" +
 " </Names>\n" +
 " <acceptsLineNumber>No</acceptsLineNumber>\n" +
 " </Local>\n" +
 " </Web>")));
 assertFalse(SAXDirParser.acceptsLineNumber());
}

 @Test
 public void testLineNumberParameter() throws Exception {
 parser.parse(new InputSource(new StringReader(reader.toString())));
 assertEquals("5", SAXDirParser.lineNumberParameter());
}

 @Test(expected = NullPointerException.class)
 public void testLineNumberParameter_empty() throws Exception {
 parser.parse(new InputSource(new StringReader("")));
 assertNull(SAXDirParser.lineNumberParameter());
}
}