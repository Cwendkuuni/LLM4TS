import static org.junit.Assert.*;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.ParserFactory;
import static org.mockito.Mockito.*;
import org.mockito.ArgumentMatchers;
import org.junit.Before;
import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import static java.util.Arrays.asList;

public class SAXDirParserTest {
    private SAXDirParser parser;
    private Map<String, String> directory;
    private ArrayList<String> localPackages;
    private String localBrowser;
    private String fileEditor;
    private boolean lineNumAccepted;
    private String lineNumParam;

    @Before
    public void setup() {
        parser = new SAXDirParser();
        directory = new HashMap<>();
        localPackages = new ArrayList<>();
        when(ParserFactory.newInstance().newSAXParser()).thenReturn(mock(SAXParser.class));
    }

    @Test
    public void testGetWebData() {
        assertEquals("", parser.getWebData());
    }

    @Test
    public void testStartElement_BrowserLoc() {
        String xml = "<Web>\n" +
                "  <BrowserLoc value=\"Google Chrome\"/>\n" +
                "</Web>";

        try {
            ParserFactory parserFactory = ParserFactory.newInstance();
            SAXParser saxParser = parserFactory.newSAXParser();
            XMLReader xmlReader = saxParser.getXMLReader();
            xmlReader.setContentHandler(parser);
            File file = new File("input.xml");
            xmlReader.parse(file);

            assertEquals("Google Chrome", parser.getWebData());
        } catch (Exception e) {
            fail("Exception should not be thrown.");
        }
    }

    @Test
    public void testStartElement_Editor() {
        String xml = "<Web>\n" +
                "  <Editor value=\"Notepad\"/>\n" +
                "</Web>";

        try {
            ParserFactory parserFactory = ParserFactory.newInstance();
            SAXParser saxParser = parserFactory.newSAXParser();
            XMLReader xmlReader = saxParser.getXMLReader();
            xmlReader.setContentHandler(parser);
            File file = new File("input.xml");
            xmlReader.parse(file);

            assertEquals("Notepad", parser.getEditor());
        } catch (Exception e) {
            fail("Exception should not be thrown.");
        }
    }

    @Test
    public void testStartElement_acceptsLineNumber() {
        String xml = "<Web>\n" +
                "  <acceptsLineNumber value=\"Yes\"/>\n" +
                "</Web>";

        try {
            ParserFactory parserFactory = ParserFactory.newInstance();
            SAXParser saxParser = parserFactory.newSAXParser();
            XMLReader xmlReader = saxParser.getXMLReader();
            xmlReader.setContentHandler(parser);
            File file = new File("input.xml");
            xmlReader.parse(file);

            assertTrue(parser.acceptsLineNumber());
        } catch (Exception e) {
            fail("Exception should not be thrown.");
        }
    }

    @Test
    public void testStartElement_lineNumberParameter() {
        String xml = "<Web>\n" +
                "  <acceptsLineNumber value=\"Yes\"/>\n" +
                "  <parameter name=\"lineNumber\" value=\"5\"/>\n" +
                "</Web>";

        try {
            ParserFactory parserFactory = ParserFactory.newInstance();
            SAXParser saxParser = parserFactory.newSAXParser();
            XMLReader xmlReader = saxParser.getXMLReader();
            xmlReader.setContentHandler(parser);
            File file = new File("input.xml");
            xmlReader.parse(file);

            assertEquals("5", parser.lineNumberParameter());
        } catch (Exception e) {
            fail("Exception should not be thrown.");
        }
    }

    @Test
    public void testStartElement_Local() {
        String xml = "<Web>\n" +
                "  <Local value=\"Yes\"/>\n" +
                "</Web>";

        try {
            ParserFactory parserFactory = ParserFactory.newInstance();
            SAXParser saxParser = parserFactory.newSAXParser();
            XMLReader xmlReader = saxParser.getXMLReader();
            xmlReader.setContentHandler(parser);
            File file = new File("input.xml");
            xmlReader.parse(file);

            assertTrue(parser.isLocal());
        } catch (Exception e) {
            fail("Exception should not be thrown.");
        }
    }

    @Test
    public void testStartElement_Names() {
        String xml = "<Web>\n" +
                "  <Names>\n" +
                "    <pkg name=\"java.lang\"/>\n" +
                "    <pkg name=\"javax.swing.border\"/>\n" +
                "  </Names>\n" +
                "</Web>";

        try {
            ParserFactory parserFactory = ParserFactory.newInstance();
            SAXParser saxParser = parserFactory.newSAXParser();
            XMLReader xmlReader = saxParser.getXMLReader();
            xmlReader.setContentHandler(parser);
            File file = new File("input.xml");
            xmlReader.parse(file);

            assertEquals(asList("java.lang", "javax.swing.border"), parser.getDirectory().keySet());
        } catch (Exception e) {
            fail("Exception should not be thrown.");
        }
    }

    @Test
    public void testStartElement_pkg() {
        String xml = "<Web>\n" +
                "  <Names>\n" +
                "    <pkg name=\"java.lang\"/>\n" +
                "    <pkg name=\"javax.swing.border\"/>\n" +
                "  </Names>\n" +
                "</Web>";

        try {
            ParserFactory parserFactory = ParserFactory.newInstance();
            SAXParser saxParser = parserFactory.newSAXParser();
            XMLReader xmlReader = saxParser.getXMLReader();
            xmlReader.setContentHandler(parser);
            File file = new File("input.xml");
            xmlReader.parse(file);

            assertEquals("java.lang", parser.getDirectory().get("java.lang"));
            assertEquals("javax.swing.border", parser.getDirectory().get("javax.swing.border"));
        } catch (Exception e) {
            fail("Exception should not be thrown.");
        }
    }

    @Test(expected = AssertionError.class)
    public void testStartElement_invalidElement() {
        String xml = "<Web>\n" +
                "  <InvalidElement>\n" +
                "</Web>";

        try {
            ParserFactory parserFactory = ParserFactory.newInstance();
            SAXParser saxParser = parserFactory.newSAXParser();
            XMLReader xmlReader = saxParser.getXMLReader();
            xmlReader.setContentHandler(parser);
            File file = new File("input.xml");
            xmlReader.parse(file);

            fail("Exception should be thrown.");
        } catch (SAXException e) {
            verify(parser, times(1)).fireWarning(ArgumentMatchers.anyString(), ArgumentMatchers.anyString());
        } catch (IOException e) {
            fail("IOException should not be thrown.");
        }
    }
}