import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class DocumentTypeTest {
    @Test
    public void testDocumentTypeCreation() {
        // Test creating a new document type with valid arguments
        DocumentType docType = new DocumentType("html", "publicId1", "systemId1", "baseUri1");
        assertEquals("#doctype", docType.nodeName());
        assertEquals("publicId1", docType.attr("publicId"));
        assertEquals("systemId1", docType.attr("systemId"));
        assertEquals("baseUri1", docType.baseUri());

        // Test creating a new document type with empty arguments
        DocumentType docTypeEmpty = new DocumentType("", "", "", "");
        assertEquals("#doctype", docTypeEmpty.nodeName());
        assertEquals("", docTypeEmpty.attr("publicId"));
        assertEquals("", docTypeEmpty.attr("systemId"));
        assertEquals("", docTypeEmpty.baseUri());
    }

    @Test
    public void testDocumentTypeOuterHtml() {
        Document doc = new Document();
        DocumentType docType = new DocumentType("html", "publicId1", "systemId1", "baseUri1");
        Element root = doc.createElement("html");
        root.appendChild(docType);

        StringBuilder expected = new StringBuilder();
        docType.outerHtmlHead(expected, 0, null);
        expected.append("<!DOCTYPE html PUBLIC \"publicId1\" systemId1>\n");

        docType.outerHtmlTail(expected, 0, null);

        String actual = root.outerHtml();

        assertEquals(expected.toString(), actual);
    }
}