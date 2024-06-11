import org.junit.jupiter.api.Test;
import org.jsoup.nodes.DocumentType;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;

class DocumentTypeTest {

    @Test
    void testDocumentTypeConstructor() {
        DocumentType docType = new DocumentType("html", "publicID", "systemID", "baseUri");
        assertEquals("#doctype", docType.nodeName());
        assertNotNull(docType.attr(DocumentType.NAME));
        assertEquals("publicID", docType.attr(DocumentType.PUBLIC_ID));
        assertEquals("systemID", docType.attr(DocumentType.SYSTEM_ID));
        assertEquals("baseUri", docType.baseUri());
    }

    @Test
    void testOuterHtmlHead() throws IOException {
        DocumentType docType = new DocumentType("html", "publicID", "systemID", "baseUri");
        final Appendable accum = new StringBuffer();
        docType.outerHtmlHead(accum, 0, Document.OutputSettings.syntax(Syntax.html));
        assertEquals("<!doctype html PUBLIC \"publicID\" \"systemID\">", accum.toString());
    }

    @Test
    void testOuterHtmlHeadWithNullArguments() {
        assertThrows(NullPointerException.class, () -> new DocumentType(null, "publicID", "systemID", "baseUri"));
        assertThrows(NullPointerException.class, () -> new DocumentType("html", null, "systemID", "baseUri"));
        assertThrows(NullPointerException.class, () -> new DocumentType("html", "publicID", null, "baseUri"));
        assertThrows(NullPointerException.class, () -> new DocumentType("html", "publicID", "systemID", null));
    }

    @Test
    void testOuterHtmlHeadWithEmptyArguments() {
        assertThrows(IllegalArgumentException.class, () -> new DocumentType("", "", "", "baseUri"));
        assertThrows(IllegalArgumentException.class, () -> new DocumentType("html", "", "", ""));
        assertThrows(IllegalArgumentException.class, () -> new DocumentType("html", "publicID", "", "baseUri"));
        assertThrows(IllegalArgumentException.class, () -> new DocumentType("html", "publicID", "systemID", ""));
    }

    @Test
    void testOuterHtmlHeadWithDifferentOutputSettings() throws IOException {
        DocumentType docType = new DocumentType("html", "publicID", "systemID", "baseUri");
        final Appendable accum = new StringBuffer();
        docType.outerHtmlHead(accum, 0, Document.OutputSettings.syntax(Syntax.xml));
        assertEquals("<!DOCTYPE html PUBLIC \"publicID\" \"systemID\">", accum.toString());
    }
}