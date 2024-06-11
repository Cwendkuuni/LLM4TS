package org.jsoup.helper;

import org.junit.Test;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

public class W3CDomTest {

 @Test
 public void testFromJsoup() throws Exception {
 W3CDom w3cdom = new W3CDom();
 org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>");
 Document w3cDocument = w3cdom.fromJsoup(jsoupDocument);
 assertNotNull(w3cDocument);
 }

 @Test
 public void testConvert() throws Exception {
 W3CDom w3cdom = new W3CDom();
 org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>");
 Document w3cDocument = w3cdom.fromJsoup(jsoupDocument);
 w3cdom.convert(jsoupDocument, w3cDocument);

 assertEquals("<html xmlns:xmlns=\"http://www.w3.org/1999/xhtml\">" +
               "<head xmlns=\"http://www.w3.org/1999/xhtml\">" +
               " <title>Test</title>" +
               "</head>" +
               "<body xmlns=\"http://www.w3.org/1999/xhtml\">" +
               " <h1>Hello World</h1>" +
               "</body>" +
               "</html>", w3cdom.asString(w3cDocument));
 }

 @Test
 public void testEmptyDocument() throws Exception {
 W3CDom w3cdom = new W3CDom();
 org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("");
 Document w3cDocument = w3cdom.fromJsoup(jsoupDocument);
 assertEquals("", w3cdom.asString(w3cDocument));
 }

 @Test
 public void testTextNode() throws Exception {
 W3CDom w3cdom = new W3CDom();
 org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<p>Hello World</p>");
 Document w3cDocument = w3cdom.fromJsoup(jsoupDocument);
 w3cdom.convert(jsoupDocument, w3cDocument);

 assertEquals("<p>Hello World</p>", w3cdom.asString(w3cDocument));
 }

 @Test
 public void testCommentNode() throws Exception {
 W3CDom w3cdom = new W3CDom();
 org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><!-- Test --></html>");
 Document w3cDocument = w3cdom.fromJsoup(jsoupDocument);
 w3cdom.convert(jsoupDocument, w3cDocument);

 assertEquals("<html><!--Test--></html>", w3cdom.asString(w3cDocument));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullDocument() {
 W3CDom w3cdom = new W3CDom();
 Document w3cDocument = null;
 w3cdom.fromJsoup(w3cDocument);
 }
}