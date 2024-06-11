import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>");
        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);

        assertNotNull(w3cDocument);
        assertEquals("<html xmlns:html=\"http://www.w3.org/1999/xhtml\">\n<head>\n<title>Test</title>\n</head>\n<body>\n<h1>Hello World</h1>\n</body>\n</html>", W3CDomTest.asString(w3cDocument));
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>");
        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);
        Document emptyW3cDocument = w3cDocument;

        new W3CDom().convert(jsoupDocument, emptyW3cDocument);

        assertEquals("html", emptyW3cDocument.getDocumentElement().getTagName());
        assertEquals(1, emptyW3cDocument.getDocumentElement().getChildNodes().getLength());
        assertEquals("head", emptyW3cDocument.getDocumentElement().getChildNodes().item(0).getTagName());
        assertEquals(1, emptyW3cDocument.getDocumentElement().getChildNodes().item(0).getChildNodes().getLength());
        assertEquals("title", emptyW3cDocument.getDocumentElement().getChildNodes().item(0).getChildNodes().item(0).getTagName());
        assertEquals("Test", emptyW3cDocument.getDocumentElement().getChildNodes().item(0).getChildNodes().item(0).getTextContent());
    }

    @Test
    public void testAsString() {
        Document w3cDocument = new W3CDom().fromJsoup(org.jsoup.Jsoup.parse("<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>"));
        String w3cString = new W3CDom().asString(w3cDocument);

        assertEquals("<html xmlns:html=\"http://www.w3.org/1999/xhtml\">\n<head>\n<title>Test</title>\n</head>\n<body>\n<h1>Hello World</h1>\n</body>\n</html>", w3cString);
    }

    @Test(expected = IllegalStateException.class)
    public void testFromJsoupNullDocument() {
        new W3CDom().fromJsoup(null);
    }

    @Test(expected = IllegalStateException.class)
    public void testConvertNullDocuments() {
        new W3CDom().convert(null, null);
    }

    private static String asString(Document document) {
        return new String(document.getDocumentElement().getDocumentURI().getBytes(0, document.getDocumentElement().getDocumentURI().length()));
    }
}