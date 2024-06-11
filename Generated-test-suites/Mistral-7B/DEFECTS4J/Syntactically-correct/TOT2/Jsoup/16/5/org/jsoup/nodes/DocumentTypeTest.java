import org.junit.Test;
import org.jsoup.nodes.*;
import org.junit.Assert.*;
import static org.junit.Assert.*;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeConstructor() {
        DocumentType docType = new DocumentType("html", "publicId", "systemId", "baseUri");
        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr("name"));
        assertEquals("publicId", docType.attr("publicId"));
        assertEquals("systemId", docType.attr("systemId"));
        assertEquals("baseUri", docType.baseUri());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDocumentTypeConstructorNullName() {
        new DocumentType(null, "publicId", "systemId", "baseUri");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDocumentTypeConstructorEmptyName() {
        new DocumentType("", "publicId", "systemId", "baseUri");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDocumentTypeConstructorNullBaseUri() {
        new DocumentType("html", "publicId", "systemId", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDocumentTypeConstructorInvalidBaseUri() {
        new DocumentType("html", "publicId", "systemId", "baseUri:/invalid");
    }

    @Test
    public void testDocumentTypeNodeName() {
        Document doc = new Document();
        Element element = new Element("html");
        doc.appendChild(element);
        doc.appendChild(new DocumentType("html", null, null, null));
        assertEquals("#doctype", doc.getElementsByTag("!DOCTYPE").get(0).nodeName());
    }

    @Test
    public void testDocumentTypeOuterHtmlHead() {
        Document doc = new Document();
        DocumentType docType = new DocumentType("html", "publicId", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        doc.appendDocument(accum);
        doc.body().appendChild(docType);
        doc.appendDocument(accum);
        String expected = "<!DOCTYPE htmlPUBLIC \"publicId\" systemId=\"systemId\">";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlTail() {
        Document doc = new Document();
        DocumentType docType = new DocumentType("html", "publicId", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        doc.appendDocument(accum);
        doc.body().appendChild(docType);
        doc.appendDocument(accum);
        doc.body().appendChild(new TextNode(""));
        doc.appendDocument(accum);
        String expected = "<!DOCTYPE htmlPUBLIC \"publicId\" systemId=\"systemId\">";
        assertEquals(expected, accum.toString());
    }
}