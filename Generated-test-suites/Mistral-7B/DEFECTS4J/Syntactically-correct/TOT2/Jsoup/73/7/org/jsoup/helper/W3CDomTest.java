import org.junit.Test;
import static org.junit.Assert.assertEquals;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDocument = Jsoup.parse("<html><body><h1>Test</h1></body></html>");
        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);
        assertEquals(jsoupDocument.html(), w3cDocument.getDocumentElement().getTagName(), "Root elements should match");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromJsoup_EmptyDocument() {
        new W3CDom().fromJsoup(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromJsoup_InvalidXml() {
        org.jsoup.nodes.Document jsoupDocument = Jsoup.parse("<html><body><h1><script>alert('XSS')</script></h1></body></html>");
        new W3CDom().fromJsoup(jsoupDocument);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromJsoup_NoRootElement() {
        org.jsoup.nodes.Document jsoupDocument = Jsoup.parse("<html><body>");
        new W3CDom().fromJsoup(jsoupDocument);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromJsoup_InvalidTagName() {
        org.jsoup.nodes.Document jsoupDocument = Jsoup.parse("<html><body><invalid:tag>Test</invalid:tag></body></html>");
        new W3CDom().fromJsoup(jsoupDocument);
    }

    @Test(expected = ParserConfigurationException.class)
    public void testFromJsoup_ParserConfigurationException() {
        W3CDom w3CDom = new W3CDom();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        w3CDom.factory = factory;
        try {
            factory.setFeature("http://xml.org/sax/features/validation/schema", false);
            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.setEntityResolver(new EntityResolver() {
                public InputSource resolveEntity(String publicId, String systemId) throws IOException, SAXException {
                    throw new IOException();
                }
            });
            org.jsoup.nodes.Document jsoupDocument = Jsoup.parse("<html><body><h1>Test</h1></body></html>");
            Document w3cDocument = w3CDom.fromJsoup(jsoupDocument);
        } catch (ParserConfigurationException e) {
            // Test passes
        }
    }

    @Test(expected = TransformerException.class)
    public void testFromJsoup_TransformerException() throws TransformerException {
        Document jsoupDocument = Jsoup.parse("<html><body><h1>Test</h1></body></html>");
        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);
        DOMSource domSource = new DOMSource(w3cDocument);
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.transform(domSource, result);
        transformer.transform(domSource, result); // Should throw TransformerException
    }
}