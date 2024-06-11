import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Token;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Assert;
import org.junit.Test;

public class XmlTreeBuilderTest {

    @Test
    public void testDefaultSettings() {
        ParseSettings settings = XmlTreeBuilder.defaultSettings();
        assertEquals(ParseSettings.PreserveCase.class, settings.getClass());
    }

    @Test
    public void testParseReader() throws IOException {
        String xmlData = "<root><element>Test</element></root>";
        String baseUri = "";
        Reader reader = new StringReader(xmlData);
        Document doc = new XmlTreeBuilder().parse(reader, baseUri);
        assertEquals(1, doc.numChildren());
    }

    @Test
    public void testParseString() throws IOException {
        String xmlData = "<root><element>Test</element></root>";
        String baseUri = "";
        Document doc = new XmlTreeBuilder().parse(xmlData, baseUri);
        assertEquals(1, doc.numChildren());
    }

    @Test
    public void testProcessToken() {
        // Initialize XmlTreeBuilder and create tokens
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token token = new Token.StartTag("root", null, null, 1, 1);

        // Test process method
        assertTrue(builder.process(token));
        assertEquals(1, builder.stack.size());
    }

    @Test
    public void testInsertNode() {
        // Create a test Node
        Element element = new Element("test_element");

        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.currentElement().appendChild(element);

        assertEquals(1, builder.stack.get(builder.stack.size() - 1).numChildren());
    }

    // Add more test cases for edge cases and exception handling here

}