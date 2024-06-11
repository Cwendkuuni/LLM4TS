package org.jsoup.helper;

import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextDocument;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.junit.Assert;
import org.junit.Test;

import javax.xml.parsers.ParserConfigurationException;

public class W3CDomTest {

 @Test
 public void testFromJsoup() {
 W3CDom w3CDom = new W3CDom();

 // Test null input
 Assert.assertNull(w3CDom.fromJsoup(null));

 // Test empty jsoup document
 Document jsoupDocument = new TextDocument("");
 Document w3cDocument = w3CDom.fromJsoup(jsoupDocument);
 Assert.assertNotNull(w3cDocument);
 Assert.assertEquals(0, w3cDocument.getDocumentElement().getChildNodes().getLength());

 // Test jsoup document with root element
 Document jsoupDocumentWithRoot = new TextDocument("<?xml version=\"1.0\"?>\n<root></root>");
 Document w3cDocumentWithRoot = w3CDom.fromJsoup(jsoupDocumentWithRoot);
 Assert.assertNotNull(w3cDocumentWithRoot);
 Element w3cRoot = w3cDocumentWithRoot.getDocumentElement();
 Assert.assertEquals("root", w3cRoot.getTagName());
 Assert.assertEquals(0, w3cRoot.getChildNodes().getLength());

 // Test jsoup document with root and child element
 Document jsoupDocumentWithChild = new TextDocument("<?xml version=\"1.0\"?>\n<root><child></child></root>");
 Document w3cDocumentWithChild = w3CDom.fromJsoup(jsoupDocumentWithChild);
 Assert.assertNotNull(w3cDocumentWithChild);
 Element w3cRootWithChild = w3cDocumentWithChild.getDocumentElement();
 Assert.assertEquals("root", w3cRootWithChild.getTagName());
 Element w3cChild = (Element) w3cRootWithChild.getChildNodes().item(0);
 Assert.assertEquals("child", w3cChild.getTagName());
 Assert.assertEquals(0, w3cChild.getChildNodes().getLength());
 }

 @Test
 public void testConvert() throws ParserConfigurationException {
 W3CDom w3CDom = new W3CDom();

 // Test null inputs
 Assert.assertNull(w3CDom.convert(null, null));

 // Test empty jsoup document and w3c document
 Document jsoupDocument = new TextDocument("");
 Document w3cDocument = w3CDom.fromJsoup(jsoupDocument);
 Document convertedDocument = w3CDom.convert(jsoupDocument, w3cDocument);
 Assert.assertNull(convertedDocument);

 // Test jsoup document with root element and w3c document
 Document jsoupDocumentWithRoot = new TextDocument("<?xml version=\"1.0\"?>\n<root></root>");
 Document w3cDocumentWithRoot = w3CDom.fromJsoup(jsoupDocumentWithRoot);
 Document convertedDocumentWithRoot = w3CDom.convert(jsoupDocumentWithRoot, w3cDocumentWithRoot);
 Assert.assertNotNull(convertedDocumentWithRoot);
 Element jsoupRoot = jsoupDocumentWithRoot.rootElement();
 Element w3cRoot = w3cDocumentWithRoot.getDocumentElement();
 Assert.assertEquals(jsoupRoot.tagName(), w3cRoot.getTagName());

 // Test jsoup document with root and child element and w3c document
 Document jsoupDocumentWithRootAndChild = new TextDocument("<?xml version=\"1.0\"?>\n<root><child></child></root>");
 Document w3cDocumentWithRootAndChild = w3CDom.fromJsoup(jsoupDocumentWithRootAndChild);
 Document convertedDocumentWithRootAndChild = w3CDom.convert(jsoupDocumentWithRootAndChild, w3cDocumentWithRootAndChild);
 Assert.assertNotNull(convertedDocumentWithRootAndChild);
 Element jsoupRootWithChild = jsoupDocumentWithRootAndChild.rootElement();
 Element w3cRootWithChild = w3cDocumentWithRootAndChild.getDocumentElement();
 Element jsoupChild = jsoupRootWithChild.child(0);
 Element w3cChild = (Element) w3cRootWithChild.getChildNodes().item(0);
 Assert.assertEquals(jsoupRootWithChild.tagName(), w3cRootWithChild.getTagName());
 Assert.assertEquals(jsoupChild.tagName(), w3cChild.getTagName());
 }

 @Test
 public void testAsString() {
 W3CDom w3CDom = new W3CDom();

 // Test empty w3c document
 Document w3cDocument = new Document();
 String w3cString = w3CDom.asString(w3cDocument);
 Assert.assertEquals("", w3cString);

 // Test w3c document with root and child element
 Document w3cDocumentWithRootAndChild = w3CDom.fromJsoup(new TextDocument("<?xml version=\"1.0\"?>\n<root><child></child></root>"));
 String w3cStringWithRootAndChild = w3CDom.asString(w3cDocumentWithRootAndChild);
 Assert.assertFalse(w3cStringWithRootAndChild.isEmpty());

 // Test w3c document with comment
 Document w3cDocumentWithComment = w3CDom.fromJsoup(new TextDocument("<?xml version=\"1.0\"?>\n<!-- Test comment -->\n<root></root>"));
 String w3cStringWithComment = w3CDom.asString(w3cDocumentWithComment);
 Assert.assertFalse(w3cStringWithComment.isEmpty());
 }
}