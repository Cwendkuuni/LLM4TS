package org.jsoup.nodes.test;

import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.LeafNode;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.helper.Validate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class LeafNodeTest {

 @Test
 public void testLeafNodeHasNoChildren() {
 LeafNode leafNode = new TextNode("Test LeafNode");
 assertEquals(0, leafNode.childNodeSize());
 }

 @Test
 public void testSimpleLeafNodeValue() {
 LeafNode leafNode = new TextNode("Test LeafNode");
 assertEquals("Test LeafNode", leafNode.coreValue());
 }

 @Test
 public void testLeafNodeWithEmptyAttributes() {
 LeafNode leafNode = new TextNode("Test LeafNode");
 assertFalse(leafNode.hasAttributes());
 assertNull(leafNode.attributes());
 }

 @Test
 public void testLeafNodeWithSingleAttribute() {
 LeafNode leafNode = new TextNode("Test LeafNode").attr("data-test", "value");
 assertTrue(leafNode.hasAttributes());
 assertEquals("value", leafNode.attr("data-test"));
 }

 @Test
 public void testLeafNodeWithMultipleAttributes() {
 LeafNode leafNode = new TextNode("Test LeafNode")
 .attr("data-test1", "value1")
 .attr("data-test2", "value2");
 assertTrue(leafNode.hasAttributes());
 assertEquals("value1", leafNode.attr("data-test1"));
 assertEquals("value2", leafNode.attr("data-test2"));
 }

 @Test
 public void testLeafNodeAttributeValueSetter() {
 LeafNode leafNode = new TextNode("Test LeafNode");
 leafNode.coreValue("New Value");
 assertEquals("New Value", leafNode.coreValue());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testLeafNodeChildNodes() {
 LeafNode leafNode = new TextNode("Test LeafNode");
 leafNode.ensureChildNodes();
 }

 @Test
 public void testLeafNodeBaseUri() {
 LeafNode leafNode = new TextNode("Test LeafNode").parent(new Elements.Element("Test Parent"));
 assertEquals("", leafNode.baseUri());
 LeafNode parent = new Elements.Element("Test Parent");
 leafNode = new TextNode("Test LeafNode").parent(parent);
 assertEquals(parent.baseUri(), leafNode.baseUri());
 }

 @Test
 public void testLeafNodeAttributes() {
 LeafNode leafNode = new TextNode("Test LeafNode")
 .attr("data-test1", "value1")
 .attr("data-test2", "value2");
 Attributes attributes = leafNode.attributes();
 assertEquals("value1", attributes.get("data-test1"));
 assertEquals("value2", attributes.get("data-test2"));
 }

 @Test
 public void testLeafNodeHasAttr() {
 LeafNode leafNode = new TextNode("Test LeafNode").attr("data-test", "value");
 assertTrue(leafNode.hasAttr("data-test"));
 }

 @Test
 public void testLeafNodeRemoveAttr() {
 LeafNode leafNode = new TextNode("Test LeafNode").attr("data-test", "value");
 assertTrue(leafNode.hasAttr("data-test"));
 leafNode.removeAttr("data-test");
 assertFalse(leafNode.hasAttr("data-test"));
 }

 @Test
 public void testLeafNodeAbsUrl() {
 LeafNode leafNode = new TextNode("Test LeafNode")
 .parent(new Elements.Element("Test Parent"));
 Node parent = new Elements.Element("Test Parent")
 .attr("href", "http://test.com");
 leafNode = new TextNode("Test LeafNode").parent(parent);
 assertEquals("http://test.com/Test%20LeafNode", leafNode.absUrl("href"));
 }

}