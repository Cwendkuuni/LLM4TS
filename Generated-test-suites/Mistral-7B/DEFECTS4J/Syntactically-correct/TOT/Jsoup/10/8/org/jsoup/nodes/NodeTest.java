package org.jsoup.nodes;

import org.junit.Assert; import org.junit.Test; import static org.junit.Assert.*;

import java.net.MalformedURLException; import java.net.URL;
import java.util.ArrayList; import java.util.Arrays; import java.util.List;

public class NodeTest {

 @Test
 public void testNodeCreation() {
 Node node = new Elements().html(); // create an empty HTML document

 // create a simple node with baseUri and attributes
 String baseUri = "https://example.com";
 Attributes attributes = new Attributes();
 attributes.put("id", "myId");
 Node simpleNode = new Element("div", baseUri, attributes);

 // test node properties
 assertEquals("div", simpleNode.nodeName());
 assertEquals(baseUri, simpleNode.baseUri());
 assertEquals(attributes, simpleNode.attributes());

 // test setting and getting attributes
 simpleNode.attr("class", "myClass");
 assertEquals("myClass", simpleNode.attr("class"));

 // test setting and getting attributes that start with 'abs:'
 simpleNode.attr("abs:href", "https://example.com/test");
 assertEquals("https://example.com/test", simpleNode.absUrl("href"));

 // test attribute absence
 assertFalse(simpleNode.hasAttr("nonexistent"));

 // test attribute removal
 simpleNode.removeAttr("class");
 assertFalse(simpleNode.hasAttr("class"));
 }

 @Test
 public void testNodeWithChildren() {
 // create a simple node with baseUri, attributes, and children
 String baseUri = "https://example.com";
 Attributes attributes = new Attributes();
 attributes.put("id", "myId");
 Node parentNode = new Element("div", baseUri, attributes);

 Node child1 = new Element("p", baseUri, new Attributes());
 child1.setText("Child 1");
 parentNode.appendChild(child1);

 Node child2 = new Element("p", baseUri, new Attributes().put("class", "child2"));
 child2.setText("Child 2 with class");
 parentNode.appendChild(child2);

 // test node properties
 assertEquals("div", parentNode.nodeName());
 assertEquals(baseUri, parentNode.baseUri());
 assertEquals(Arrays.asList(child1, child2), parentNode.childNodes());

 // test child node properties
 assertEquals("p", child1.nodeName());
 assertEquals("", child1.attributes().toString());
 assertEquals("Child 1", child1.outerHtml());

 assertEquals("p", child2.nodeName());
 assertEquals("child2", child2.attr("class"));
 assertEquals("Child 2 with class", child2.outerHtml());
 }

 @Test
 public void testNodeWithAttributesAndBaseUri() {
 // create a simple node with baseUri, attributes, and a relative URL attribute
 String baseUri = "https://example.com";
 Attributes attributes = new Attributes().put("id", "myId").put("abs:href", "https://example.com/test");
 Node node = new Element("a", baseUri, attributes);

 // test absolute URL from relative URL attribute
 assertEquals("https://example.com/test", node.absUrl("href"));

 // test setting baseUri
 String newBaseUri = "https://www.example.com";
 node.setBaseUri(newBaseUri);
 assertEquals(newBaseUri, node.baseUri());

 // test absolute URL from relative URL attribute with new baseUri
 assertEquals("https://www.example.com/test", node.absUrl("href"));
 }

 @Test
 public void testMalformedURL() {
 // test creating node with malformed URL baseUri
 String baseUri = "http://example.com";
 Attributes attributes = new Attributes();
 try {
 Node node = new Element("a", baseUri + "://foo.bar.com", attributes);
 fail("Expected MalformedURLException");
 } catch (MalformedURLException e) {
 // expected
 }
 }
}