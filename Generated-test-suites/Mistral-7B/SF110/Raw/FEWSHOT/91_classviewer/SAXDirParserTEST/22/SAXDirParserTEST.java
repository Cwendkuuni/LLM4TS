package com.jstevh.viewer;

import static org.junit.Assert.*;
import org.xml.sax.*;
import org.junit.Test;
import org.xml.sax.helpers.DefaultHandler;

public class SAXDirParserTest {

    @Test
    public void testGetDirectory() {
        SAXDirParser parser = new SAXDirParser();
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            XMLReader reader = factory.newSAXParser().getXMLReader();
            reader.setContentHandler(parser);
            reader.parse("path/to/packagedirectory.xml");
            HashMap<String, String> expectedDirectory = new HashMap<>();
            expectedDirectory.put("java.lang", "test1");
            expectedDirectory.put("javax.swing.border", "test2");
            assertEquals(expectedDirectory, SAXDirParser.getDirectory());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testGetLocalPackages() {
        SAXDirParser parser = new SAXDirParser();
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            XMLReader reader = factory.newSAXParser().getXMLReader();
            reader.setContentHandler(parser);
            reader.parse("path/to/packagedirectory.xml");
            StringList expectedLocalPackages = new StringList();
            expectedLocalPackages.add("com.jstevh.viewer");
            expectedLocalPackages.add("org.xml.sax.helpers");
            assertEquals(expectedLocalPackages, SAXDirParser.getLocalPackages());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testGetLocalBrowser() {
        SAXDirParser parser = new SAXDirParser();
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            XMLReader reader = factory.newSAXParser().getXMLReader();
            reader.setContentHandler(parser);
            reader.parse("path/to/packagedirectory.xml");
            String expectedLocalBrowser = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3099.110 Safari/56.0";
            assertEquals(expectedLocalBrowser, SAXDirParser.getLocalBrowser());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testGetEditor() {
        SAXDirParser parser = new SAXDirParser();
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            XMLReader reader = factory.newSAXParser().getXMLReader();
            reader.setContentHandler(parser);
            reader.parse("path/to/packagedirectory.xml");
            String expectedEditor = "Notepad";
            assertEquals(expectedEditor, SAXDirParser.getEditor());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testAcceptsLineNumber() {
        SAXDirParser parser = new SAXDirParser();
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            XMLReader reader = factory.newSAXParser().getXMLReader();
            reader.setContentHandler(parser);
            reader.parse("path/to/packagedirectory.xml");
            assertTrue(SAXDirParser.acceptsLineNumber());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testLineNumberParameter() {
        SAXDirParser parser = new SAXDirParser();
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            XMLReader reader = factory.newSAXParser().getXMLReader();
            reader.setContentHandler(parser);
            reader.parse("path/to/packagedirectory.xml");
            String expectedLineNumberParameter = "1234";
            assertEquals(expectedLineNumberParameter, SAXDirParser.lineNumberParameter());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testHandler_BrowserLoc() {
        SAXDirParser parser = new SAXDirParser() {
            private String browser;

            @Override
            public void characters(char[] ch, int start, int length) throws SAXException {
                super.characters(ch, start, length);
                if (check && level == 0) {
                    browser = new String(ch, start, length);
                }
            }

            @Test
            public void testBrowser() {
                SAXParserFactory factory = SAXParserFactory.newInstance();
                XMLReader reader = factory.newSAXParser().getXMLReader();
                reader.setContentHandler(this);
                reader.parse("path/to/packagedirectory.xml");
                String expectedBrowser = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3099.110 Safari/56.0";
                assertEquals(expectedBrowser, browser);
            }
        };
    }

    @Test
    public void testHandler_Editor() {
        SAXDirParser parser = new SAXDirParser() {
            private String editor;

            @Override
            public void characters(char[] ch, int start, int length) throws SAXException {
                super.characters(ch, start, length);
                if (check && level == 10) {
                    editor = new String(ch, start, length);
                }
            }

            @Test
            public void testEditor() {
                SAXParserFactory factory = SAXParserFactory.newInstance();
                XMLReader reader = factory.newSAXParser().getXMLReader();
                reader.setContentHandler(this);
                reader.parse("path/to/packagedirectory.xml");
                String expectedEditor = "Notepad";
                assertEquals(expectedEditor, editor);
            }
        };
    }

    @Test
    public void testHandler_AcceptLineNumber() {
        SAXDirParser parser = new SAXDirParser() {
            private boolean acceptLineNumber;

            @Override
            public void characters(char[] ch, int start, int length) throws SAXException {
                super.characters(ch, start, length);
                if (check && level == 11) {
                    String lineNumberParam = new String(ch, start, length);
                    acceptLineNumber = lineNumberParam.equalsIgnoreCase("Yes");
                }
            }

            @Test
            public void testAcceptsLineNumber() {
                SAXParserFactory factory = SAXParserFactory.newInstance();
                XMLReader reader = factory.newSAXParser().getXMLReader();
                reader.setContentHandler(this);
                reader.parse("path/to/packagedirectory.xml");
                assertTrue(acceptLineNumber);
            }
        };
    }

    @Test
    public void testHandler_LineNumberParameter() {
        SAXDirParser parser = new SAXDirParser() {
            private String lineNumberParameter;

            @Override
            public void characters(char[] ch, int start, int length) throws SAXException {
                super.characters(ch, start, length);
                if (check && level == 12) {
                    lineNumberParameter = new String(ch, start, length);
                }
            }

            @Test
            public void testLineNumberParameter() {
                SAXParserFactory factory = SAXParserFactory.newInstance();
                XMLReader reader = factory.newSAXParser().getXMLReader();
                reader.setContentHandler(this);
                reader.parse("path/to/packagedirectory.xml");
                String expectedLineNumberParameter = "1234";
                assertEquals(expectedLineNumberParameter, lineNumberParameter);
            }
        };
    }

    @Test
    public void testHandler_WebData() {
        SAXDirParser parser = new SAXDirParser() {
            private String webData;

            @Override
            public void characters(char[] ch, int start, int length) throws SAXException {
                super.characters(ch, start, length);
                if (check && level >= 30) {
                    webData += new String(ch, start, length);
                }
            }

            @Test
            public void testWebData() {
                SAXParserFactory factory = SAXParserFactory.newInstance();
                XMLReader reader = factory.newSAXParser().getXMLReader();
                reader.setContentHandler(this);
                reader.parse("path/to/packagedirectory.xml");
                String expectedWebData = "test1/java.lang/test2/javax.swing.border";
                assertEquals(expectedWebData, parser.getWebData());
            }
        };
    }
}