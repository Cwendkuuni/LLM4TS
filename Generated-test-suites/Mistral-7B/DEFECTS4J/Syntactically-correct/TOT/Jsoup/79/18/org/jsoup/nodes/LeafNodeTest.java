import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.LeafNode;
import org.jsoup.nodes.TextNode;
import static org.junit.Assert.*;

public class LeafNodeTest {

 @Test
 public void testLeafNodeWithStringValue() {
 LeafNode leafNode = new Element("p").text("Hello World");
 assertEquals("Hello World", leafNode.coreValue());
 assertEquals("p", leafNode.nodeName());
 }

 @Test
 public void testLeafNodeWithAttributes() {
 LeafNode leafNode = new Element("p").attr("id", "testID").attr("class", "testClass");
 assertEquals("testID testClass", leafNode.coreValue());
 assertEquals("p", leafNode.nodeName());
 assertEquals("id", leafNode.attr("id"));
 assertEquals("testClass", leafNode.attr("class"));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testLeafNodeNoChildNodes() {
 LeafNode leafNode = new Element("p").text("Hello World");
 assertNull(leafNode.childNodeSize());
 }

 @Test
 public void testLeafNodeHasParent() {
 Element parentElement = new Element("div");
 Element leafNode = new Element("p").text("Hello World").parent(parentElement);
 assertEquals("", leafNode.baseUri()); // since it has a parent, baseUri should be empty
 }

 @Test
 public void testLeafNodeSetCoreValue() {
 LeafNode leafNode = new Element("p");
 leafNode.coreValue("New Value");
 assertEquals("New Value", leafNode.coreValue());
 }

 @Test
 public void testLeafNodeRemoveAttribute() {
 LeafNode leafNode = new Element("p").attr("id", "testID").attr("class", "testClass");
 leafNode.removeAttr("class");
 assertEquals("id", leafNode.coreValue());
 }

 @Test
 public void testLeafNodeInvalidAttributeName() {
 LeafNode leafNode = new Element("p").attr("id", "testID");
 try {
 leafNode.attr("invalidAttributeName", "testValue");
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testLeafNodeNullAttributeValue() {
 LeafNode leafNode = new Element("p").attr("id", null);
 assertEquals("", leafNode.coreValue());
 }
}