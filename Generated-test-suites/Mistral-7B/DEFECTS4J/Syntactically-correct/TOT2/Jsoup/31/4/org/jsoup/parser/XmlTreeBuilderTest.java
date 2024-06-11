import org.junit.Test;
import org.jsoup.nodes.*;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse("xmlInput", null, null);
        assertNotNull(xmlTreeBuilder.doc);
    }

    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Element expectedElement = new Element("tagName", null, null);
        Token startTagToken = new Token.StartTag("tagName", null, null);
        xmlTreeBuilder.process(startTagToken);
        Element actualElement = xmlTreeBuilder.currentElement();
        assertEquals(expectedElement.nodeName(), actualElement.nodeName());
    }

    // ... other test cases for other methods

    @Test
    public void testProcessEndTag() {
        // ... setup for this test case
        Token endTagToken = new Token.EndTag("tagName");
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.process(endTagToken);
        Element currentElement = xmlTreeBuilder.currentElement();
        assertNull(currentElement);
    }

    // ... other test cases for other methods

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Element rootElement = new Element("root", null, null);
        Element innerElement = new Element("inner", null, null);
        xmlTreeBuilder.stack.add(rootElement);
        xmlTreeBuilder.stack.add(innerElement);
        xmlTreeBuilder.popStackToClose(new Token.EndTag("inner"));
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals(rootElement, xmlTreeBuilder.stack.peek());
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Node node = new TextNode("textContent", null);
        xmlTreeBuilder.insertNode(node);
        Element currentElement = xmlTreeBuilder.currentElement();
        assertEquals(node, currentElement.childNodes().get(0));
    }

    // ... other test cases for other methods
}