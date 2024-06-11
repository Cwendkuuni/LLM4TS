import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.LeafNode;
import org.jsoup.nodes.Node;
import static org.junit.Assert.*;
import static org.jsoup.helper.Validate.*;

public class LeafNodeTest {

 @Test
 public void testHasAttributes() {
 LeafNode leafNode = new MyLeafNode("root");
 assertFalse(leafNode.hasAttributes());
 leafNode.coreValue("key1");
 leafNode.coreValue("value1");
 assertTrue(leafNode.hasAttributes());
 }

 @Test
 public void testAttributes() {
 LeafNode leafNode = new MyLeafNode("root");
 assertNull(leafNode.attributes());
 leafNode.coreValue("key1 value1");
 Attributes attributes = leafNode.attributes();
 assertEquals("key1 value1", attributes.get("key1"));
 }

 @Test
 public void testEnsureAttributes() {
 LeafNode leafNode = new MyLeafNode("root");
 leafNode.coreValue("key1 value1");
 LeafNode leafNode2 = new MyLeafNode("root2");
 Attributes attributes = new Attributes();
 attributes.put("key2", "value2");
 leafNode2.value = attributes;

 LeafNode leafNodeWithAttributes = new LeafNode() {{
 value = leafNode.attributes();
 coreValue("newKey newValue");
 }};
 assertEquals(attributes, leafNodeWithAttributes.attributes());
 }

 @Test
 public void testCoreValue() {
 LeafNode leafNode = new MyLeafNode("root");
 leafNode.coreValue("initialValue");
 assertEquals("initialValue", leafNode.coreValue());

 LeafNode leafNode2 = new MyLeafNode("root2");
 leafNode2.coreValue("updatedValue");
 leafNode.coreValue("overwrittenValue");
 assertEquals("overwrittenValue", leafNode.coreValue());
 }

 @Test
 public void testAttr() {
 LeafNode leafNode = new MyLeafNode("root");
 leafNode.coreValue("rootKey rootValue");

 LeafNode leafNodeWithAttribute = new MyLeafNode("newNode") {{
 coreValue("newNodeKey newNodeValue");
 attr("rootKey", "updatedRootValue");
 }};
 assertEquals("updatedRootValue", leafNode.attr("rootKey"));
 assertEquals("newNodeKey newNodeValue", leafNodeWithAttribute.coreValue());
 }

 @Test(expected = NullPointerException.class)
 public void testAttrWithNullKey() {
 LeafNode leafNode = new MyLeafNode("root");
 leafNode.coreValue("rootKey rootValue");
 leafNode.attr(null, "value");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveAttr() {
 LeafNode leafNode = new MyLeafNode("root") {{
 coreValue("rootKey rootValue");
 }};
 LeafNode removedAttrNode = leafNode.removeAttr("rootKey");
 assertNotNull(removedAttrNode);
 assertEquals("rootValue", removedAttrNode.coreValue());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testChildNodes() {
 LeafNode leafNode = new MyLeafNode("root");
 List<Node> childNodes = leafNode.ensureChildNodes();
 assertNotNull(childNodes);
 assertEquals(0, childNodes.size());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetBaseUri() {
 LeafNode leafNode = new MyLeafNode("root");
 leafNode.setBaseUri("baseUri");
 }
}

class MyLeafNode extends LeafNode {
 public MyLeafNode(String nodeName) {
 super(nodeName);
 }
}