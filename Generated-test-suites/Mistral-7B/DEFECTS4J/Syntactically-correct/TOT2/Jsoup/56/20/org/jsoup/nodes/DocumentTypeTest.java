
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Document.OutputSettings;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

public class DocumentTypeTest {
    @Test
    public void testEmptyConstructor() {
        // This should throw an IllegalArgumentException
        new DocumentType();
    }

    @Test
    public void testHtmlDoctype() {
        DocumentType htmlDoctype = new DocumentType("html", "HTML", "HTML5", "http://www.w3.org/TR/html5/");
        assertEquals("#doctype", htmlDoctype.nodeName());
    }

    @Test
    public void testNonHtmlDoctype() {
        DocumentType nonHtmlDoctype = new DocumentType("xml", "XML", "XML5", "http://www.w3.org/TR/xml/");
        assertEquals("#doctype", nonHtmlDoctype.nodeName());
    }

    @Test
    public void testDoctypeWithPublicId() {
        DocumentType doctype = new DocumentType("html", "HTML", "HTML5", "http://www.w3.org/TR/html5/");
        assertTrue(doctype.has("PUBLIC_ID"));
    }

    @Test
    public void testDoctypeWithoutPublicId() {
        DocumentType doctype = new DocumentType("html", "", "", "http://www.w3.org/TR/html5/");
        assertFalse(doctype.has("PUBLIC_ID"));
    }

    @Test
    public void testDoctypeWithSystemId() {
        DocumentType doctype = new DocumentType("html", "", "HTML5", "http://www.w3.org/TR/html5/");
        assertTrue(doctype.has("SYSTEM_ID"));
    }

    @Test
    public void testDoctypeWithoutSystemId() {
        DocumentType doctype = new DocumentType("html", "HTML", "", "http://www.w3.org/TR/html5/");
        assertFalse(doctype.has("SYSTEM_ID"));
    }

    @Test
    public void testOuterHtmlHeadWithHtmlOutputSettings() throws IOException {
        DocumentType doctype = new DocumentType("html", "HTML", "HTML5", "http://www.w3.org/TR/html5/");
        Document document = mock(Document.class);
        Appendable appendable = mock(Appendable.class);
        OutputSettings out = new OutputSettings();
        out.syntax(OutputSettings.Syntax.html);

        when(document.newAppendingElement(Document.Type.HEAD)).thenReturn(appendable);

        doctype.outerHtmlHead(appendable, 0, out);
        String expected = "<!doctype html PUBLIC \"HTML\" \"HTML5\" >";
        String actual = appendable.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testOuterHtmlHeadWithXmlOutputSettings() throws IOException {
        DocumentType doctype = new DocumentType("html", "HTML", "HTML5", "http://www.w3.org/TR/html5/");
        Document document = mock(Document.class);
        Appendable appendable = mock(Appendable.class);
        OutputSettings out = new OutputSettings();
        out.syntax(OutputSettings.Syntax.xml);

        when(document.newAppendingElement(Document.Type.HEAD)).thenReturn(appendable);

        doctype.outerHtmlHead(appendable, 0, out);
        String expected = "<!DOCTYPE html PUBLIC \"HTML\" \"HTML5\" >";
        String actual = appendable.toString();
        assertEquals(expected, actual);
    }

    @Test(expected = RuntimeException.class)
    public void testOuterHtmlHeadWithFullAppendable() throws IOException {
        DocumentType doctype = new DocumentType("html", "HTML", "HTML5", "http://www.w3.org/TR/html5/");
        Document document = mock(Document.class);
        Appendable appendable = mock(Appendable.class);
        OutputSettings out = new OutputSettings();

        when(document.newAppendingElement(Document.Type.HEAD)).thenReturn(appendable);
        when(appendable.append(anyString())).thenThrow(new RuntimeException("Appendable is full"));

        doctype.outerHtmlHead(appendable, 0, out);
    }
}
