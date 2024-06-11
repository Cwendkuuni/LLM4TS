package org.jsoup.nodes;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.jsoup.nodes.Jsoup.parse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class NodeTest {

 @Mock
 private Document document;

 @InjectMocks
 private Node node;

 @Before
 public void setUp() {
 node = new Element("div") {
 @Override
 public String nodeName() {
 return "div";
 }
 }.init();
 node.setBaseUri("http://example.com");
 node.setAttributes(new Attributes().put("id", "myDiv"));
 }

 @Test
 public void testAttr() {
 node.attr("class", "test");
 assertThat(node.attr("class"), is("test"));
 }

 @Test
 public void testAttrMissing() {
 assertThat(node.attr("nonExistentAttr"), is(""));
 }

 @Test
 public void testAbsUrl() {
 node.attr("href", "relative.html");
 when(document.baseUri()).thenReturn(new URL("http://test.com"));
 assertThat(node.absUrl("href"), is("http://test.com/relative.html"));
 }

 @Test
 public void testAbsUrlAbsolute() {
 node.attr("abs:href", "http://test.com");
 assertThat(node.absUrl("abs:href"), is("http://test.com"));
 }

 @Test
 public void testAbsUrlMalformed() {
 node.attr("abs:href", "http://test.com/foo/bar"); // malformed
 assertThat(node.absUrl("abs:href"), is(""));
 }

 @Test
 public void testHasAttr() {
 assertThat(node.hasAttr("class"), is(true));
 assertThat(node.hasAttr("nonExistentAttr"), is(false));
 }

 @Test
 public void testRemoveAttr() {
 node.attr("class", "test");
 node.removeAttr("class");
 assertThat(node.attr("class"), is(""));
 }

 @Test
 public void testSetBaseUri() {
 node.setBaseUri("http://test.com");
 node.attr("href", "relative.html");
 assertThat(node.absUrl("href"), is("http://test.com/relative.html"));
 }

 @Test
 public void testSetBaseUriMalformed() {
 node.setBaseUri("http://test.com/foo/bar"); // malformed
 node.attr("href", "relative.html");
 assertThat(node.absUrl("href"), is(""));
 }

 @Test
 public void testChildNodesEmpty() {
 node.addChildren(new TextNode("Child 1"));
 node.addChildren(new TextNode("Child 2"));

 List<Node> childNodes = node.childNodes();
 assertThat(childNodes.size(), is(2));
 }

 @Test
 public void testChildNodesWithText() {
 node.addChildren(new TextNode("Child 1"));
 node.addChildren(new TextNode("Child 2"));
 node.addChildren(new TextNode("Child 3"));
 node.addChildren(new Element("p").init());
 node.addChildren(new Element("ul").init());

 List<Node> childNodes = node.childNodes();
 assertThat(childNodes.size(), is(5));
 }

 @Test
 public void testChildNodesWithAttributes() {
 node.addChildren(new Element("a").init().attr("href", "http://test.com/foo"));
 node.addChildren(new Element("img").init().attr("src", "image.png").attr("alt", "Image"));

 List<Node> childNodes = node.childNodes();
 Element link = (Element) childNodes.get(0);
 Element image = (Element) childNodes.get(1);

 assertThat(link.attr("href"), is("http://test.com/foo"));
 assertThat(image.attr("src"), is("image.png"));
 assertThat(image.attr("alt"), is("Image"));
 }

 @Test
 public void testChildNodesWithAttributesAndText() {
 node.addChildren(new TextNode("Child 1"));
 node.addChildren(new Element("p").init().attr("class", "test").text("Child 2"));
 node.addChildren(new Element("ul").init().attr("id", "testList"));

 List<Node> childNodes = node.childNodes();
 TextNode textNode = (TextNode) childNodes.get(0);
 Element pElement = (Element) childNodes.get(1);
 Element uListElement = (Element) childNodes.get(2);

 assertThat(textNode.text(), is("Child 1"));
 assertThat(pElement.nodeName(), is("p"));
 assertThat(pElement.attr("class"), is("test"));
 assertThat(uListElement.nodeName(), is("ul"));
 assertThat(uListElement.attr("id"), is("testList"));
 }

 @Test
 public void testSetParentNode() {
 Element parent = new Element("body").init();
 node.setParentNode(parent);
 parent.addChildren(node);

 Element grandparent = new Element("html").init();
 parent.setParentNode(grandparent);

 List<Node> children = grandparent.childNodes();
 Element bodyElement = (Element) children.get(0);
 List<Node> nodeChildren = bodyElement.childNodes();
 Element testElement = (Element) nodeChildren.get(0);

 assertThat(testElement.getParent(), is(bodyElement));
 assertThat(bodyElement.getParent(), is(grandparent));
 }

 @Test
 public void testRemoveChild() {
 Element parent = new Element("body").init();
 node.setParentNode(parent);
 parent.addChildren(node);

 List<Node> children = parent.childNodes();
 Node childToRemove = children.get(0);
 parent.removeChild(childToRemove);

 List<Node> remainingChildren = parent.childNodes();
 assertThat(remainingChildren.size(), is(0));
 }

 @Test
 public void testReplaceChild() throws MalformedURLException {
 Element parent = new Element("body").init();
 node.setParentNode(parent);
 parent.addChildren(node);

 Element newChild = new Element("p").init().attr("id", "newChild");
 parent.replaceChild(node, newChild);

 List<Node> children = parent.childNodes();
 Node replacedChild = children.get(0);

 assertThat(replacedChild.getClass(), is(Element.class));
 assertThat(replacedChild.nodeName(), is("p"));
 assertThat(replacedChild.attributes().get("id"), is("newChild"));
 }

 @Test
 public void testAddChildren() {
 Element parent = new Element("body").init();
 node.setParentNode(parent);

 Element child1 = new Element("p").init().attr("class", "test1");
 Element child2 = new Element("p").init().attr("class", "test2");
 node.addChildren(child1, child2);

 List<Node> children = parent.childNodes();
 List<Node> nodeChildren = node.childNodes();

 assertThat(children.size(), is(3));
 assertThat(nodeChildren.size(), is(2));
 }

 @Test
 public void testAddChildrenWithText() {
 Element parent = new Element("body").init();
 node.setParentNode(parent);

 TextNode textChild = new TextNode("Child 1");
 node.addChildren(textChild);

 List<Node> children = parent.childNodes();
 List<Node> nodeChildren = node.childNodes();

 assertThat(children.size(), is(2));
 assertThat(nodeChildren.size(), is(1));
 }

 @Test
 public void testAddChildrenWithAttributes() {
 Element parent = new Element("body").init();
 node.setParentNode(parent);

 Element child1 = new Element("a").init().attr("href", "http://test.com");
 Element child2 = new Element("img").init().attr("src", "image.png").attr("alt", "Image");
 node.addChildren(child1, child2);

 List<Node> children = parent.childNodes();
 List<Node> nodeChildren = node.childNodes();

 assertThat(children.size(), is(3));
 assertThat(nodeChildren.size(), is(2));
 }

 @Test
 public void testAddChildrenWithAttributesAndText() {
 Element parent = new Element("body").init();
 node.setParentNode(parent);

 TextNode textChild = new TextNode("Child 1");
 Element pElement = new Element("p").init().attr("class", "test");
 node.addChildren(textChild, pElement);

 List<Node> children = parent.childNodes();
 List<Node> nodeChildren = node.childNodes();

 assertThat(children.size(), is(3));
 assertThat(nodeChildren.size(), is(2));
 }
}