import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Document;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        // Regular cases
        DocumentType documentType1 = new DocumentType("html", "publicId", "systemId", "baseUri");
        assertEquals("#doctype", documentType1.nodeName());

        DocumentType documentType2 = new DocumentType("html", "", "", "");
        assertEquals("#doctype", documentType2.nodeName());

        DocumentType documentType3 = new DocumentType("html", null, null, null);
        assertEquals("#doctype", documentType3.nodeName());

        DocumentType documentType4 = new DocumentType("html", "publicId", null, "");
        assertEquals("#doctype", documentType4.nodeName());

        DocumentType documentType5 = new DocumentType("html", null, "systemId", "");
        assertEquals("#doctype", documentType5.nodeName());

        DocumentType documentType6 = new DocumentType(null, "publicId", "systemId", "");
        assertEquals("#doctype", documentType6.nodeName());

        DocumentType documentType7 = new DocumentType("", "publicId", "systemId", "");
        assertEquals("#doctype", documentType7.nodeName());

        // Edge cases
        DocumentType documentType8 = new DocumentType("   ", "publicId", "systemId", "baseUri");
        assertEquals("#doctype", documentType8.nodeName());

        DocumentType documentType9 = new DocumentType("html", "publicId with special chars", "systemId with special chars", "baseUri with special chars");
        assertEquals("#doctype", documentType9.nodeName());
    }

    @Test
    public void testNodeName() {
        // Regular cases
        DocumentType documentType1 = new DocumentType("html", "publicId", "systemId", "baseUri");
        assertEquals("#doctype", documentType1.nodeName());

        // Edge cases
        DocumentType documentType2 = new DocumentType("", "publicId", "systemId", "");
        assertEquals("#doctype", documentType2.nodeName());

        DocumentType documentType3 = new DocumentType("html", null, null, null);
        assertEquals("#doctype", documentType3.nodeName());

        DocumentType documentType4 = new DocumentType("html", "publicId", null, null);
        assertEquals("#doctype", documentType4.nodeName());

        DocumentType documentType5 = new DocumentType("html", null, "systemId", null);
        assertEquals("#doctype", documentType5.nodeName());

        DocumentType documentType6 = new DocumentType(null, "publicId", "systemId", null);
        assertEquals("#doctype", documentType6.nodeName());

        DocumentType documentType7 = new DocumentType("html", "publicId", "systemId", "");
        assertEquals("#doctype", documentType7.nodeName());

        DocumentType documentType8 = new DocumentType("html", "publicId", "systemId", " baseUri ");
        assertEquals("#doctype", documentType8.nodeName());

        DocumentType documentType9 = new DocumentType("html", "publicId with special chars", "systemId with special chars", "baseUri with special chars");
        assertEquals("#doctype", documentType9.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", "baseUri");

        // Regular cases
        StringBuilder accum1 = new StringBuilder();
        documentType.outerHtmlHead(accum1, 0, null);
        String expected1 = "<!DOCTYPE html PUBLIC \"publicId\" systemId>\n";
        assertEquals(expected1, accum1.toString());

        StringBuilder accum2 = new StringBuilder();
        Document document = new Document();
        document.appendDocument(documentType);
        document.outputSettings().syntax(Document.OutputSettings.Syntax.html);
        document.appendBody();
        document.body().appendElement("div");

        documentType.outerHtmlHead(accum2, 0, document.outputSettings());
        String expected2 = "<!DOCTYPE html PUBLIC \"publicId\" systemId>\n";
        assertEquals(expected2, accum2.toString());

        // Edge cases
        StringBuilder accum3 = new StringBuilder();
        documentType.outerHtmlHead(accum3, 0, null);
        String expected3 = "<!DOCTYPE html>\n";
        assertEquals(expected3, accum3.toString());

        StringBuilder accum4 = new StringBuilder();
        Document document4 = new Document();
        document.appendDocument(documentType);
        Document.OutputSettings out = new Document.OutputSettings();
        document.outputSettings().syntax(Document.OutputSettings.Syntax.html);
        document.appendBody();
        document.body().appendElement("div");

        documentType.outerHtmlHead(accum4, 0, out);
        String expected4 = "<!DOCTYPE html>\n";
        assertEquals(expected4, accum4.toString());

        StringBuilder accum5 = new StringBuilder();
        Document document5 = new Document();
        document.appendDocument(documentType);
        document.outputSettings().syntax(Document.OutputSettings.Syntax.html);
        document.appendBody();
        document.body().appendElement("div");

        documentType.outerHtmlHead(accum5, 1, document.outputSettings());
        String expected5 = "<!DOCTYPE html>\n";
        assertEquals(expected5, accum5.toString());

        StringBuilder accum6 = new StringBuilder();
        Document document6 = new Document();
        document.appendDocument(documentType);
        document.outputSettings().syntax(Document.OutputSettings.Syntax.html);
        document.appendBody();
        document.body().appendElement("div");

        documentType.outerHtmlHead(accum6, -1, document.outputSettings());
        String expected6 = "<!DOCTYPE html>\n";
        assertEquals(expected6, accum6.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", "baseUri");

        // Regular cases
        StringBuilder accum1 = new StringBuilder();
        documentType.outerHtmlTail(accum1, 0, null);
        String expected1 = ">";
        assertEquals(expected1, accum1.toString());

        StringBuilder accum2 = new StringBuilder();
        Document document = new Document();
        document.appendDocument(documentType);
        document.outputSettings().syntax(Document.OutputSettings.Syntax.html);
        document.appendBody();
        document.body().appendElement("div");

        documentType.outerHtmlTail(accum2, 0, document.outputSettings());
        String expected2 = ">";
        assertEquals(expected2, accum2.toString());

        // Edge cases
        StringBuilder accum3 = new StringBuilder();
        documentType.outerHtmlTail(accum3, 0, null);
        String expected3 = "";
        assertEquals(expected3, accum3.toString());

        StringBuilder accum4 = new StringBuilder();
        Document document4 = new Document();
        document.appendDocument(documentType);
        Document.OutputSettings out = new Document.OutputSettings();
        document.outputSettings().syntax(Document.OutputSettings.Syntax.html);
        document.appendBody();
        document.body().appendElement("div");

        documentType.outerHtmlTail(accum4, 0, out);
        String expected4 = "";
        assertEquals(expected4, accum4.toString());

        StringBuilder accum5 = new StringBuilder();
        Document document5 = new Document();
        document.appendDocument(documentType);
        document.outputSettings().syntax(Document.OutputSettings.Syntax.html);
        document.appendBody();
        document.body().appendElement("div");

        documentType.outerHtmlTail(accum5, 1, document.outputSettings());
        String expected5 = "";
        assertEquals(expected5, accum5.toString());

        StringBuilder accum6 = new StringBuilder();
        Document document6 = new Document();
        document.appendDocument(documentType);
        document.outputSettings().syntax(Document.OutputSettings.Syntax.html);
        document.appendBody();
        document.body().appendElement("div");

        documentType.outerHtmlTail(accum6, -1, document.outputSettings());
        String expected6 = "";
        assertEquals(expected6, accum6.toString());
    }
}