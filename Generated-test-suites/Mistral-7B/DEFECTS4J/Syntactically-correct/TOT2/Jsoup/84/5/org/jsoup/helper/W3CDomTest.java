import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.jsoup.helper.W3CDom;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        // Given: a jsoup Document
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse("<html><body><p>Hello World</p></body></html>");

        // When: converting the jsoup Document to a W3C Document
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);

        // Then: the W3C Document should not be null
        Assert.assertNotNull(w3cDoc);

        // And: the root element of the W3C Document should have the correct tag name
        Element root = w3cDoc.getDocumentElement();
        Assert.assertEquals("html", root.getTagName());
    }

    @Test
    public void testConvert() {
        // Given: a jsoup Document
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse("<html><body><p>Hello World</p></body></html>");

        // And: a W3C Document
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);

        // When: converting the jsoup Document to the provided W3C Document
        new W3CDom().convert(jsoupDoc, w3cDoc);

        // Then: the W3C Document should have the same content as the original jsoup Document
        Assert.assertEquals(jsoupDoc.html(), w3cDom.getDocumentElement().getTagName(), "html");
        Assert.assertEquals(jsoupDoc.body().html(), w3cDoc.getElementsByTagName("body").item(0).getTagName(), "body");
        Assert.assertEquals(jsoupDoc.select("p").text(), w3cDoc.getElementsByTagName("p").item(0).getTextContent(), "Hello World");
    }

    @Test
    public void testAsString() {
        // Given: a W3C Document
        Document w3cDoc = new W3CDom().fromJsoup(org.jsoup.Jsoup.parse("<html><body><p>Hello World</p></body></html>"));

        // When: serializing the W3C Document to a String
        String w3cString = new W3CDom().asString(w3cDoc);

        // Then: the String should match the original jsoup Document
        Assert.assertEquals(org.jsoup.Jsoup.parse(w3cString).html(), org.jsoup.Jsoup.parse("<html><body><p>Hello World</p></body></html>").html());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromJsoupNullDocument() {
        // Given: a null jsoup Document
        new W3CDom().fromJsoup(null);

        // Then: an IllegalArgumentException should be thrown
    }
}