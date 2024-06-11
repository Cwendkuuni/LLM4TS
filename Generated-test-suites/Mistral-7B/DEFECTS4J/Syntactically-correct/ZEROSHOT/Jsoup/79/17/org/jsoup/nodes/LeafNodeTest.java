import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.LeafNode;
import org.jsoup.nodes.Node;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThat;
import org.hamcrest.CoreMatchers;

public class LeafNodeTest {

 @Test
 public void testLeafNodeWithValue() {
 LeafNode leafNode = new LeafNode("div");
 leafNode.coreValue("Testvalue");

 assertEquals("Testvalue", leafNode.coreValue());
 assertEquals("Testvalue", leafNode.attr(leafNode.nodeName()));
 }

 @Test
 public void testLeafNodeWithEmptyValue() {
 LeafNode leafNode = new LeafNode("div");

 assertNull(leafNode.coreValue());
 assertNull(leafNode.attr(leafNode.nodeName()));
 }

 @Test
 public void testLeafNodeWithAttributes() {
 LeafNode leafNode = new LeafNode("div");
 Attributes attributes = new Attributes();
 attributes.put("class", "testClass");
 leafNode.value = attributes;

 assertEquals(1, leafNode.attributes().size());
 assertTrue(leafNode.attributes().containsKey("class"));
 assertEquals("testClass", leafNode.attributes().get("class"));
 }

 @Test
 public void testHasAttributes() {
 LeafNode leafNodeWithValue = new LeafNode("div");
 leafNodeWithValue.coreValue("Testvalue");

 assertFalse(leafNodeWithValue.hasAttributes());

 LeafNode leafNodeWithAttributes = new LeafNode("div");
 Attributes attributes = new Attributes();
 attributes.put("class", "testClass");
 leafNodeWithAttributes.value = attributes;

 assertTrue(leafNodeWithAttributes.hasAttributes());
 }

 @Test
 public void testAttributes() {
 LeafNode leafNodeWithAttributes = new LeafNode("div");
 Attributes attributes = new Attributes();
 attributes.put("class", "testClass");
 leafNodeWithAttributes.value = attributes;

 assertEquals(attributes, leafNodeWithAttributes.attributes());
 }

 @Test (expected = UnsupportedOperationException.class)
 public void testChildNodeSize() {
 LeafNode leafNode = new LeafNode("div");
 leafNode.coreValue("Testvalue");

 leafNode.childNodeSize();
 }

 @Test (expected = UnsupportedOperationException.class)
 public void testEnsureChildNodes() {
 LeafNode leafNode = new LeafNode("div");
 leafNode.coreValue("Testvalue");

 leafNode.ensureChildNodes();
 }
}