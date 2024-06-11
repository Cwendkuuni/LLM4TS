import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.LeafNode;
import org.jsoup.nodes.Node;
import static org.junit.Assert.*;

public class LeafNodeTest {

 private LeafNode leafNode;

 @Before
 public void setUp() {
 leafNode = new LeafNode("test");
 }

 @Test
 public void testHasAttributes() {
 assertFalse(leafNode.hasAttributes());
 }

 @Test
 public void testAttributes() {
 leafNode.coreValue("test value");
 LeafNode nodeWithAttributes = (LeafNode) leafNode.attributes();
 assertEquals("test value", nodeWithAttributes.coreValue());
 assertEquals("test", nodeWithAttributes.nodeName());
 assertTrue(nodeWithAttributes.hasAttributes());
 Assertions.assertNotNull(nodeWithAttributes.attributes());
 }

 @Test
 public void testCoreValue() {
 assertEquals("test", leafNode.nodeName());
 assertEquals(EmptyString, leafNode.coreValue());
 leafNode.coreValue("test value");
 assertEquals("test value", leafNode.coreValue());
 }

 @Test
 public void testCoreValueSetter() {
 leafNode.coreValue("test value");
 assertEquals("test value", leafNode.coreValue());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAttributeWhenNoAttributes() {
 leafNode.attr("name", "value");
 }

 @Test
 public void testAttr() {
 leafNode.coreValue("test value");
 assertEquals("test value", leafNode.attr("name"));
 }

 @Test
 public void testAttrSetter() {
 leafNode.coreValue("test value");
 leafNode.attr("name", "new value");
 assertEquals("new value", leafNode.attr("name"));
 }

 @Test
 public void testHasAttr() {
 assertFalse(leafNode.hasAttr("name"));
 leafNode.coreValue("test value");
 leafNode.attr("name", "value");
 assertTrue(leafNode.hasAttr("name"));
 }

 @Test
 public void testRemoveAttr() {
 leafNode.coreValue("test value");
 leafNode.attr("name", "value");
 Node removedAttrNode = leafNode.removeAttr("name");
 assertFalse(leafNode.hasAttr("name"));
 assertNull(removedAttrNode);
 }

 @Test
 public void testAbsUrl() {
 assertEquals(EmptyString, leafNode.absUrl(""));
 }

 @Test
 public void testBaseUri() {
 leafNode.parent(new Elements.SingleElement<Node>("parent"));
 leafNode.parent().baseUri("testBaseUri");
 assertEquals("testBaseUri", leafNode.baseUri());
 }

 @Test
 public void testChildNodeSize() {
 assertEquals(0, leafNode.childNodeSize());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testEnsureChildNodes() {
 leafNode.ensureChildNodes();
 }
}