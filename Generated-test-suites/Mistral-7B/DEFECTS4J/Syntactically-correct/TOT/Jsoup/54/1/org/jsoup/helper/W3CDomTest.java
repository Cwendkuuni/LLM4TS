import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.jsoup.Jsoup;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        // Test with null input
        Assertions.assertThrows(IllegalArgumentException.class, () -> W3CDom.fromJsoup(null));

        // Test with empty jsoup document
        org.jsoup.nodes.Document jsoupDocument = Jsoup.parse("");
        Document w3cDocument = W3CDom.fromJsoup(jsoupDocument);
        Assertions.assertEquals(0, w3cDocument.getDocumentElement().getChildNodes().getLength());

        // Test with a simple jsoup document
        org.jsoup.nodes.Element jsoupRoot = Jsoup.parse("<html><body><p>Hello World</p></body></html>").child(0);
        Document w3cDocument = W3CDom.fromJsoup(jsoupDocument);
        Element w3cRoot = w3cDocument.getDocumentElement();
        Assertions.assertEquals("html", w3cRoot.getTagName());
        Assertions.assertEquals("body", w3cRoot.getElementsByTagName("body").item(0).getTagName());
        Assertions.assertEquals("p", w3cRoot.getElementsByTagName("p").item(0).getTagName());
        Assertions.assertEquals("Hello World", w3cRoot.getElementsByTagName("p").item(0).getTextContent());

        // Test with jsoup document having attributes
        org.jsoup.nodes.Element jsoupAttributeElement = Jsoup.parse("<html><body myattr='value'><p>Hello World</p></body></html>").child(0);
        Document w3cDocumentWithAttributes = W3CDom.fromJsoup(jsoupDocument);
        Element w3cRootWithAttributes = w3cDocumentWithAttributes.getDocumentElement();
        Assertions.assertEquals("html", w3cRootWithAttributes.getTagName());
        Assertions.assertEquals("body", w3cRootWithAttributes.getElementsByTagName("body").item(0).getTagName());
        Element body = w3cRootWithAttributes.getElementsByTagName("body").item(0);
        Assertions.assertEquals("myattr", body.getAttributeName("myattr"));
        Assertions.assertEquals("value", body.getAttribute("myattr"));

        // Test with jsoup document having comments
        org.jsoup.nodes.Comment jsoupComment = Jsoup.parse("<html><!--This is a comment--><body><p>Hello World</p></body></html>").child(1);
        Document w3cDocumentWithComment = W3CDom.fromJsoup(jsoupDocument);
        Element w3cRootWithComment = w3cDocumentWithComment.getDocumentElement();
        Assertions.assertEquals("html", w3cRootWithComment.getTagName());
        Assertions.assertEquals("comment", w3cRootWithComment.getElementsByTagName("comment").item(0).getNodeType());
        Assertions.assertEquals("This is a comment", w3cRootWithComment.getElementsByTagName("comment").item(0).getNodeValue());

        // Test with jsoup document having text nodes
        org.jsoup.nodes.Text jsoupTextNode = Jsoup.parse("<html><body><p>Hello<text>World</text>World</p></body></html>").child(1).child(1);
        Document w3cDocumentWithTextNode = W3CDom.fromJsoup(jsoupDocument);
        Element w3cRootWithTextNode = w3cDocumentWithTextNode.getDocumentElement();
        Assertions.assertEquals("html", w3cRootWithTextNode.getTagName());
        Assertions.assertEquals("p", w3cRootWithTextNode.getElementsByTagName("p").item(0).getTagName());
        Text textNode = (Text) w3cRootWithTextNode.getElementsByTagName("p").item(0).getChildNodes().item(1);
        Assertions.assertEquals("World", textNode.getData());

        // Test with jsoup document having data nodes
        org.jsoup.nodes.DataNode jsoupDataNode = Jsoup.parse("<html><body><p><script type='text/javascript'>alert('Hello World');</script></p></body></html>").child(1).child(0);
        Document w3cDocumentWithDataNode = W3CDom.fromJsoup(jsoupDocument);
        Element w3cRootWithDataNode = w3cDocumentWithDataNode.getDocumentElement();
        Assertions.assertEquals("html", w3cRootWithDataNode.getTagName());
        Assertions.assertEquals("p", w3cRootWithDataNode.getElementsByTagName("p").item(0).getTagName());
        Text dataNodeText = (Text) w3cRootWithDataNode.getElementsByTagName("p").item(0).getChildNodes().item(0);
        Assertions.assertEquals("script type='text/javascript'>alert('Hello World');</script>", dataNodeText.getData());
    }

    @Test
    public void testConvert() {
        // Test with null input
        Assertions.assertThrows(NullPointerException.class, () -> W3CDom.convert(null, null));

        // Test with empty jsoup document
        org.jsoup.nodes.Document jsoupDocument = Jsoup.parse("");
        Document w3cDocument = W3CDom.newInstance().fromJsoup(jsoupDocument);
        W3CDom.convert(jsoupDocument, w3cDocument);
        Assertions.assertEquals(0, w3cDocument.getDocumentElement().getChildNodes().getLength());

        // Test with a simple jsoup document
        org.jsoup.nodes.Element jsoupRoot = Jsoup.parse("<html><body><p>Hello World</p></body></html>").child(0);
        Document w3cDocument = W3CDom.newInstance().fromJsoup(jsoupDocument);
        W3CDom.convert(jsoupDocument, w3cDocument);
        Element w3cRoot = (Element) w3cDocument.getDocumentElement().getChildNodes().item(0);
        Assertions.assertEquals("html", w3cRoot.getTagName());
        Assertions.assertEquals("body", w3cRoot.getElementsByTagName("body").item(0).getTagName());
        Assertions.assertEquals("p", w3cRoot.getElementsByTagName("p").item(0).getTagName());
        Assertions.assertEquals("Hello World", w3cRoot.getElementsByTagName("p").item(0).getTextContent());

        // Test with jsoup document having attributes
        org.jsoup.nodes.Element jsoupAttributeElement = Jsoup.parse("<html><body myattr='value'><p>Hello World</p></body></html>").child(0);
        Document w3cDocumentWithAttributes = W3CDom.newInstance().fromJsoup(jsoupDocument);
        W3CDom.convert(jsoupDocument, w3cDocumentWithAttributes);
        Element w3cRootWithAttributes = (Element) w3cDocumentWithAttributes.getDocumentElement();
        Assertions.assertEquals("html", w3cRootWithAttributes.getTagName());
        Assertions.assertEquals("body", w3cRootWithAttributes.getElementsByTagName("body").item(0).getTagName());
        Element body = (Element) w3cRootWithAttributes.getElementsByTagName("body").item(0);
        Assertions.assertEquals("myattr", body.getAttributeNode("myattr").getNodeName());
        Assertions.assertEquals("value", body.getAttributeNode("myattr").getNodeValue());

        // Test with jsoup document having comments
        org.jsoup.nodes.Comment jsoupComment = Jsoup.parse("<html><!--This is a comment--><body><p>Hello World</p></body></html>").child(1);
        Document w3cDocumentWithComment = W3CDom.newInstance().fromJsoup(jsoupDocument);
        W3CDom.convert(jsoupDocument, w3cDocumentWithComment);
        Element w3cRootWithComment = (Element) w3cDocumentWithComment.getDocumentElement();
        Assertions.assertEquals("html", w3cRootWithComment.getTagName());
        Assertions.assertEquals("comment", w3cRootWithComment.getElementsByTagName("comment").item(0).getNodeType());
        Assertions.assertEquals("This is a comment", w3cRootWithComment.getElementsByTagName("comment").item(0).getNodeValue());

        // Test with jsoup document having text nodes
        org.jsoup.nodes.Text jsoupTextNode = Jsoup.parse("<html><body><p>Hello<text>World</text>World</p></body></html>").child(1).child(1);
        Document w3cDocumentWithTextNode = W3CDom.newInstance().fromJsoup(jsoupDocument);
        W3CDom.convert(jsoupDocument, w3cDocumentWithTextNode);
        Element w3cRootWithTextNode = (Element) w3cDocumentWithTextNode.getDocumentElement();
        Assertions.assertEquals("html", w3cRootWithTextNode.getTagName());
        Assertions.assertEquals("p", w3cRootWithTextNode.getElementsByTagName("p").item(0).getTagName());
        Text textNode = (Text) w3cRootWithTextNode.getElementsByTagName("p").item(0).getChildNodes().item(1);
        Assertions.assertEquals("World", textNode.getData());

        // Test with jsoup document having data nodes
        org.jsoup.nodes.DataNode jsoupDataNode = Jsoup.parse("<html><body><p><script type='text/javascript'>alert('Hello World');</script></p></body></html>").child(1).child(0);
        Document w3cDocumentWithDataNode = W3CDom.newInstance().fromJsoup(jsoupDocument);
        W3CDom.convert(jsoupDocument, w3cDocumentWithDataNode);
        Element w3cRootWithDataNode = (Element) w3cDocumentWithDataNode.getDocumentElement();
        Assertions.assertEquals("html", w3cRootWithDataNode.getTagName());
        Assertions.assertEquals("p", w3cRootWithDataNode.getElementsByTagName("p").item(0).getTagName());
        Text dataNodeText = (Text) w3cRootWithDataNode.getElementsByTagName("p").item(0).getChildNodes().item(0);
        Assertions.assertEquals("script type='text/javascript'>alert('Hello World');</script>", dataNodeText.getData());
    }

    @Test
    public void testAsString() {
        // Test with null input
        Assertions.assertThrows(NullPointerException.class, () -> W3CDom.asString(null));

        // Test with empty w3c document
        Document w3cDocument = W3CDom.newInstance().fromJsoup(Jsoup.parse(""));
        Assertions.assertEquals("", W3CDom.asString(w3cDocument));

        // Test with a simple w3c document
        Document w3cDocumentSimple = W3CDom.newInstance().fromJsoup(Jsoup.parse("<html><body><p>Hello World</p></body></html>"));
        String expectedXml = "<html><body><p>Hello World</p></body></html>";
        Assertions.assertEquals(expectedXml, W3CDom.asString(w3cDocumentSimple));

        // Test with a w3c document having attributes
        Document w3cDocumentWithAttributes = W3CDom.newInstance().fromJsoup(Jsoup.parse("<html myattr='value'><body><p>Hello World</p></body></html>"));
        String expectedXmlWithAttributes = "<html myattr='value'><body><p>Hello World</p></body></html>";
        Assertions.assertEquals(expectedXmlWithAttributes, W3CDom.asString(w3cDocumentWithAttributes));

        // Test with a w3c document having comments
        Document w3cDocumentWithComment = W3CDom.newInstance().fromJsoup(Jsoup.parse("<html><!--This is a comment--><body><p>Hello World</p></body></html>"));
        String expectedXmlWithComment = "<html><!--This is a comment--><body><p>Hello World</p></body></html>";
        Assertions.assertEquals(expectedXmlWithComment, W3CDom.asString(w3cDocumentWithComment));

        // Test with a w3c document having text nodes
        Document w3cDocumentWithTextNode = W3CDom.newInstance().fromJsoup(Jsoup.parse("<html><body><p>Hello<text>World</text>World</p></body></html>"));
        String expectedXmlWithTextNode = "<html><body><p>HelloWorldWorld</p></body></html>";
        Assertions.assertEquals(expectedXmlWithTextNode, W3CDom.asString(w3cDocumentWithTextNode));

        // Test with a w3c document having data nodes
        Document w3cDocumentWithDataNode = W3CDom.newInstance().fromJsoup(Jsoup.parse("<html><body><p><script type='text/javascript'>alert('Hello World');</script></p></body></html>"));
        String expectedXmlWithDataNode = "<html><body><p><script type='text/javascript'>alert('Hello World');</script></p></body></html>";
        Assertions.assertEquals(expectedXmlWithDataNode, W3CDom.asString(w3cDocumentWithDataNode));
    }
}