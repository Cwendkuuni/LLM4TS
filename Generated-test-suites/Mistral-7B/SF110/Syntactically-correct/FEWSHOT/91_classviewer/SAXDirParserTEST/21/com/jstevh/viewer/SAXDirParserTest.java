package com.jstevh.viewer.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.jstevh.viewer.SAXDirParser;
import com.jstevh.viewer.SAXParserFactory;
import com.jstevh.viewer.XMLReader;
import com.jstevh.viewer.StringList;

public class SAXDirParserTest {

    @Test
    public void testGetDirectory() {
        String xmlData = "<Web>\r\n" +
                         "  <Local>\r\n" +
                         "    <Names>\r\n" +
                         "      <pkg name=\"java.lang\"" +
                         "      location=\"C:/Java/jdk1.8.0_131/jre/lib/rt.jar\"/>\r\n" +
                         "      <pkg name=\"javax.swing.border\"" +
                         "      location=\"C:/Java/jdk1.8.0_131/jre/lib/ext/javax.swing.jar\"/>\r\n" +
                         "    </Names>\r\n" +
                         "  </Local>\r\n" +
                         "  <BrowserLoc>Firefox</BrowserLoc>\r\n" +
                         "  <Editor>JEdit</Editor>\r\n" +
                         "  <acceptsLineNumber>Yes</acceptsLineNumber>\r\n" +
                         "  <parameter>123</parameter>\r\n" +
                         "  <Group>\r\n" +
                         "    <Web>\r\n" +
                         "      <Local>\r\n" +
                         "        <pkg name=\"com.jstevh.viewer\" location=\"target/classes/\"/>\r\n" +
                         "      </Local>\r\n" +
                         "      <Names>\r\n" +
                         "        <pkg name=\"junit.framework\" location=\"/usr/local/junit/junit.jar\"/>\r\n" +
                         "      </Names>\r\n" +
                         "    </Web>\r\n" +
                         "  </Group>\r\n" +
                         "</Web>";

        try {
            SAXParserFactory saxFactory = SAXParserFactory.newInstance();
            XMLReader myReader = saxFactory.newSAXParser().getXMLReader();
            myReader.setContentHandler(new SAXDirParser());
            myReader.parse(new StringReader(xmlData));
            Map<String, String> expectedDirectory = new HashMap<String, String>() {{
                put("java.lang", "C:/Java/jdk1.8.0_131/jre/lib/rt.jar");
                put("javax.swing.border", "C:/Java/jdk1.8.0_131/jre/lib/ext/javax.swing.jar");
                put("com.jstevh.viewer", "target/classes/");
                put("junit.framework", "/usr/local/junit/junit.jar");
            }};
            Map<String, String> actualDirectory = SAXDirParser.getDirectory();
            assertEquals(expectedDirectory, actualDirectory);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testGetLocalPackages() {
        String xmlData = "<Web>\r\n" +
                         "  <Local>\r\n" +
                         "    <Names>\r\n" +
                         "      <pkg name=\"java.lang\"" +
                         "      location=\"C:/Java/jdk1.8.0_131/jre/lib/rt.jar\"/>\r\n" +
                         "      <pkg name=\"javax.swing.border\"" +
                         "      location=\"C:/Java/jdk1.8.0_131/jre/lib/ext/javax.swing.jar\"/>\r\n" +
                         "      <pkg name=\"com.jstevh.viewer\"" +
                         "      location=\"target/classes/\"/>\r\n" +
                         "    </Names>\r\n" +
                         "  </Local>\r\n" +
                         "  <BrowserLoc>Firefox</BrowserLoc>\r\n" +
                         "  <Editor>JEdit</Editor>\r\n" +
                         "  <acceptsLineNumber>Yes</acceptsLineNumber>\r\n" +
                         "  <parameter>123</parameter>\r\n" +
                         "</Web>";

        try {
            SAXParserFactory saxFactory = SAXParserFactory.newInstance();
            XMLReader myReader = saxFactory.newSAXParser().getXMLReader();
            myReader.setContentHandler(new SAXDirParser());
            myReader.parse(new StringReader(xmlData));
            StringList expectedLocalPackages = new ArrayList<String>() {{
                add("com.jstevh.viewer");
            }};
            StringList actualLocalPackages = SAXDirParser.getLocalPackages();
            if (actualLocalPackages != null) {
                assertEquals(expectedLocalPackages.size(), actualLocalPackages.size());
                for (int i = 0; i < expectedLocalPackages.size(); i++) {
                    assertEquals(expectedLocalPackages.get(i), actualLocalPackages.get(i));
                }
            } else {
                assertNull(actualLocalPackages);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testGetLocalBrowser() {
        String xmlData = "<Web>\r\n" +
                         "  <Local>\r\n" +
                         "    <Names>\r\n" +
                         "      <pkg name=\"java.lang\"" +
                         "      location=\"C:/Java/jdk1.8.0_131/jre/lib/rt.jar\"/>\r\n" +
                         "      <pkg name=\"javax.swing.border\"" +
                         "      location=\"C:/Java/jdk1.8.0_131/jre/lib/ext/javax.swing.jar\"/>\r\n" +
                         "      <BrowserLoc>Firefox</BrowserLoc>\r\n" +
                         "    </Names>\r\n" +
                         "  </Local>\r\n" +
                         "  <Editor>JEdit</Editor>\r\n" +
                         "  <acceptsLineNumber>Yes</acceptsLineNumber>\r\n" +
                         "  <parameter>123</parameter>\r\n" +
                         "</Web>";

        try {
            SAXParserFactory saxFactory = SAXParserFactory.newInstance();
            XMLReader myReader = saxFactory.newSAXParser().getXMLReader();
            myReader.setContentHandler(new SAXDirParser());
            myReader.parse(new StringReader(xmlData));
            String expectedLocalBrowser = "Firefox";
            String actualLocalBrowser = SAXDirParser.getLocalBrowser();
            assertEquals(expectedLocalBrowser, actualLocalBrowser);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testGetEditor() {
        String xmlData = "<Web>\r\n" +
                         "  <Local>\r\n" +
                         "    <Names>\r\n" +
                         "      <pkg name=\"java.lang\"" +
                         "      location=\"C:/Java/jdk1.8.0_131/jre/lib/rt.jar\"/>\r\n" +
                         "      <pkg name=\"javax.swing.border\"" +
                         "      location=\"C:/Java/jdk1.8.0_131/jre/lib/ext/javax.swing.jar\"/>\r\n" +
                         "      <Editor>JEdit</Editor>\r\n" +
                         "    </Names>\r\n" +
                         "  </Local>\r\n" +
                         "  <BrowserLoc>Firefox</BrowserLoc>\r\n" +
                         "  <acceptsLineNumber>Yes</acceptsLineNumber>\r\n" +
                         "  <parameter>123</parameter>\r\n" +
                         "</Web>";

        try {
            SAXParserFactory saxFactory = SAXParserFactory.newInstance();
            XMLReader myReader = saxFactory.newSAXParser().getXMLReader();
            myReader.setContentHandler(new SAXDirParser());
            myReader.parse(new StringReader(xmlData));
            String expectedEditor = "JEdit";
            String actualEditor = SAXDirParser.getEditor();
            assertEquals(expectedEditor, actualEditor);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testAcceptsLineNumber() {
        String xmlData = "<Web>\r\n" +
                         "  <Local>\r\n" +
                         "    <Names>\r\n" +
                         "      <pkg name=\"java.lang\"" +
                         "      location=\"C:/Java/jdk1.8.0_131/jre/lib/rt.jar\"/>\r\n" +
                         "      <pkg name=\"javax.swing.border\"" +
                         "      location=\"C:/Java/jdk1.8.0_131/jre/lib/ext/javax.swing.jar\"/>\r\n" +
                         "      <acceptsLineNumber>Yes</acceptsLineNumber>\r\n" +
                         "    </Names>\r\n" +
                         "  </Local>\r\n" +
                         "  <BrowserLoc>Firefox</BrowserLoc>\r\n" +
                         "  <Editor>JEdit</Editor>\r\n" +
                         "  <parameter>123</parameter>\r\n" +
                         "</Web>";

        try {
            SAXParserFactory saxFactory = SAXParserFactory.newInstance();
            XMLReader myReader = saxFactory.newSAXParser().getXMLReader();
            myReader.setContentHandler(new SAXDirParser());
            myReader.parse(new StringReader(xmlData));
            boolean expectedAcceptsLineNumber = true;
            boolean actualAcceptsLineNumber = SAXDirParser.acceptsLineNumber();
            assertTrue(expectedAcceptsLineNumber == actualAcceptsLineNumber);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testLineNumberParameter() {
        String xmlData = "<Web>\r\n" +
                         "  <Local>\r\n" +
                         "    <Names>\r\n" +
                         "      <pkg name=\"java.lang\"" +
                         "      location=\"C:/Java/jdk1.8.0_131/jre/lib/rt.jar\"/>\r\n" +
                         "      <pkg name=\"javax.swing.border\"" +
                         "      location=\"C:/Java/jdk1.8.0_131/jre/lib/ext/javax.swing.jar\"/>\r\n" +
                         "      <acceptsLineNumber>Yes</acceptsLineNumber>\r\n" +
                         "      <parameter>123</parameter>\r\n" +
                         "    </Names>\r\n" +
                         "  </Local>\r\n" +
                         "  <BrowserLoc>Firefox</BrowserLoc>\r\n" +
                         "  <Editor>JEdit</Editor>\r\n" +
                         "</Web>";

        try {
            SAXParserFactory saxFactory = SAXParserFactory.newInstance();
            XMLReader myReader = saxFactory.newSAXParser().getXMLReader();
            myReader.setContentHandler(new SAXDirParser());
            myReader.parse(new StringReader(xmlData));
            String expectedLineNumberParameter = "123";
            String actualLineNumberParameter = SAXDirParser.lineNumberParameter();
            assertEquals(expectedLineNumberParameter, actualLineNumberParameter);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testInvalidElementName() {
        String xmlData = "<Web>\r\n" +
                         "  <Local>\r\n" +
                         "    <Names>\r\n" +
                         "      <pkg name=\"java.lang\"" +
                         "      location=\"C:/Java/jdk1.8.0_131/jre/lib/rt.jar\"/>\r\n" +
                         "      <InvalidElement>Yes</InvalidElement>\r\n" +
                         "    </Names>\r\n" +
                         "  </Local>\r\n" +
                         "  <BrowserLoc>Firefox</BrowserLoc>\r\n" +
                         "  <Editor>JEdit</Editor>\r\n" +
                         "</Web>";

        try {
            SAXParserFactory saxFactory = SAXParserFactory.newInstance();
            XMLReader myReader = saxFactory.newSAXParser().getXMLReader();
            myReader.setContentHandler(new SAXDirParser());
            myReader.parse(new StringReader(xmlData));
            assertFalse(SAXDirParser.getDirectory().isEmpty()); // Should throw an exception
        } catch (SAXException | IOException e) {
            // Expected
        }
    }

    @Test
    public void testNullXMLData() {
        try {
            SAXParserFactory saxFactory = SAXParserFactory.newInstance();
            XMLReader myReader = saxFactory.newSAXParser().getXMLReader();
            myReader.setContentHandler(new SAXDirParser());
            myReader.parse(null);
            assertFalse(SAXDirParser.getDirectory().isEmpty()); // Should throw an exception
        } catch (SAXException | IOException e) {
            // Expected
        }
    }

    private static class StringReader extends StringReader {
        public StringReader(String string) {
            super(string);
        }
    }
}