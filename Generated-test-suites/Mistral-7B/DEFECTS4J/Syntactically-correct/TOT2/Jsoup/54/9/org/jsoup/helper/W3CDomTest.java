import org.junit.Test;
import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import static org.junit.Assert.*;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        // Given
        org.jsoup.nodes.Document jsoupDocument = new org.jsoup.nodes.Document();

        // When
        Document w3cDocument = W3CDom.fromJsoup(jsoupDocument);

        // Then
        assertNotNull(w3cDocument);
    }

    @Test
    public void testConvertWithEmptyInput() {
        // Given
        org.jsoup.nodes.Document jsoupDocument = new org.jsoup.nodes.Document();
        Document w3cDocument = new Document();

        // When
        W3CDom.convert(jsoupDocument, w3cDocument);

        // Then
        assertNotNull(w3cDocument.getDocumentElement());
        assertEquals(0, w3cDocument.getDocumentElement().getChildNodes().getLength());
    }

    @Test
    public void testConvertWithTextNode() {
        // Given
        org.jsoup.nodes.Document jsoupDocument = new org.jsoup.nodes.Document();
        Document w3cDocument = new Document();
        Element rootEl = jsoupDocument.createElement("root");
        Text textNode = jsoupDocument.createTextNode("Hello World");
        rootEl.appendChild(textNode);
        jsoupDocument.appendChild(rootEl);

        // When
        W3CDom.convert(jsoupDocument, w3cDocument);

        // Then
        Element w3cRootEl = w3cDocument.getDocumentElement();
        assertNotNull(w3cRootEl);
        assertEquals("root", w3cRootEl.getTagName());
        Text w3cTextNode = (Text) w3cRootEl.getFirstChild();
        assertNotNull(w3cTextNode);
        assertEquals("Hello World", w3cTextNode.getNodeValue());
    }

    @Test
    public void testConvertWithCommentNode() {
        // Given
        org.jsoup.nodes.Document jsoupDocument = new org.jsoup.nodes.Document();
        Document w3cDocument = new Document();
        Element rootEl = jsoupDocument.createElement("root");
        Comment commentNode = jsoupDocument.createComment("This is a comment");
        rootEl.appendChild(commentNode);
        jsoupDocument.appendChild(rootEl);

        // When
        W3CDom.convert(jsoupDocument, w3cDocument);

        // Then
        Element w3cRootEl = w3cDocument.getDocumentElement();
        assertNotNull(w3cRootEl);
        assertEquals("root", w3cRootEl.getTagName());
        Comment w3cCommentNode = (Comment) w3cRootEl.getFirstChild();
        assertNotNull(w3cCommentNode);
        assertEquals("This is a comment", w3cCommentNode.getNodeValue());
    }

    @Test
    public void testConvertWithDataNode() {
        // Given
        org.jsoup.nodes.Document jsoupDocument = new org.jsoup.nodes.Document();
        Document w3cDocument = new Document();
        Element rootEl = jsoupDocument.createElement("root");
        DataNode dataNode = jsoupDocument.createDataNode("Hello World");
        rootEl.appendChild(dataNode);
        jsoupDocument.appendChild(rootEl);

        // When
        W3CDom.convert(jsoupDocument, w3cDocument);

        // Then
        Element w3cRootEl = w3cDocument.getDocumentElement();
        assertNotNull(w3cRootEl);
        assertEquals("root", w3cRootEl.getTagName());
        Text w3cTextNode = (Text) w3cRootEl.getFirstChild();
        assertNotNull(w3cTextNode);
        assertEquals("Hello World", w3cTextNode.getNodeValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConvertWithNullJsoupDocument() {
        // Given
        Document w3cDocument = new Document();

        // When
        W3CDom.convert(null, w3cDocument);

        // Then
        fail("IllegalArgumentException expected");
    }
}