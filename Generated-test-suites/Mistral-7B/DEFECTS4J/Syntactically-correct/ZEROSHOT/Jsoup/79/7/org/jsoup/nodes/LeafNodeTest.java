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
 leafNode = new LeafNode("testNode") {
 @Override
 public String nodeName() {
 return "testNode";
 }
 };
 }

 @Test
 public void testHasAttributes() {
 assertFalse(leafNode.hasAttributes());
 leafNode.coreValue("testAttribute: value");
 assertTrue(leafNode.hasAttributes());
 }

 @Test
 public void testAttributes() {
 Attributes attributes = leafNode.attributes();
 assertNotNull(attributes);
 assertTrue(attributes.isEmpty());
 }

 @Test
 public void testEnsureAttributes() {
 leafNode.coreValue("testAttribute: value1");
 Attributes attributes1 = leafNode.attributes();
 assertEquals("value1", attributes1.get("testNode"));

 leafNode.coreValue("testAttribute: value2");
 Attributes attributes2 = leafNode.attributes();
 assertEquals("value2", attributes2.get("testNode"));
 assertEquals(2, attributes2.size());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testChildNodeSize() {
 leafNode.childNodeSize();
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testEnsureChildNodes() {
 leafNode.ensureChildNodes();
 }

 @Test
 public void testAttr() {
 assertNull(leafNode.attr("nonexistentAttribute"));
 assertEquals("testNode", leafNode.attr("nodeName"));
 }

 @Test
 public void testAttrWithValue() {
 leafNode.coreValue("testValue");
 assertEquals("testValue", leafNode.attr("nodeName"));
 }

 @Test
 public void testAttrWithNewValue() {
 leafNode.coreValue("testValue1");
 leafNode.attr("nodeName", "testValue2");
 assertEquals("testValue2", leafNode.attr("nodeName"));
 }

 @Test
 public void testHasAttr() {
 assertFalse(leafNode.hasAttr("nonexistentAttribute"));
 assertTrue(leafNode.hasAttr("nodeName"));
 }

 @Test
 public void testRemoveAttr() {
 leafNode.coreValue("testValue");
 assertEquals("testValue", leafNode.attr("nodeName"));
 leafNode.removeAttr("nodeName");
 assertNull(leafNode.attr("nodeName"));
 }

 @Test
 public void testAbsUrl() {
 assertNull(leafNode.absUrl("nonexistentAttribute"));
 assertNull(leafNode.absUrl("nodeName"));
 }

 @Test
 public void testBaseUri() {
 Node parentNode = new Elements.SingleElement("parentNode").add(new TextNode("parentValue"));
 parentNode.coreValue("baseUri: http://test.com");
 leafNode.parent(parentNode);
 assertEquals("http://test.com", leafNode.baseUri());
 }
}