import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.LeafNode;
import org.jsoup.nodes.Node;
import static org.junit.Assert.*;

public class LeafNodeTest {

 private LeafNode leafNode;

 @Before
 public void setup() {
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
 leafNode.coreValue("testAttribute: testValue");
 assertTrue(leafNode.hasAttributes());
 }

 @Test
 public void testAttributes() {
 assertNull(leafNode.attributes());
 leafNode.coreValue("testAttribute: testValue");
 Attributes attributes = leafNode.attributes();
 assertEquals("testValue", attributes.get("testAttribute"));
 }

 @Test(expected = NullPointerException.class)
 public void testAttributesNullKey() {
 leafNode.attributes().get(null);
 }

 @Test
 public void testCoreValue() {
 leafNode.coreValue("testValue");
 assertEquals("testValue", leafNode.coreValue());
 }

 @Test
 public void testCoreValueSetter() {
 leafNode.coreValue("testValue");
 leafNode.coreValue("newTestValue");
 assertEquals("newTestValue", leafNode.coreValue());
 }

 @Test
 public void testAttr() {
 assertEquals(EmptyString, leafNode.attr("testAttribute"));
 leafNode.coreValue("testAttribute: testValue");
 assertEquals("testValue", leafNode.attr("testAttribute"));
 }

 @Test
 public void testAttrSetter() {
 leafNode.coreValue("testAttribute: testValue");
 leafNode.attr("testAttribute", "newTestValue");
 assertEquals("newTestValue", leafNode.attr("testAttribute"));
 }

 @Test
 public void testHasAttr() {
 assertFalse(leafNode.hasAttr("testAttribute"));
 leafNode.coreValue("testAttribute: testValue");
 assertTrue(leafNode.hasAttr("testAttribute"));
 }

 @Test
 public void testRemoveAttr() {
 leafNode.coreValue("testAttribute: testValue");
 leafNode.removeAttr("testAttribute");
 assertFalse(leafNode.hasAttr("testAttribute"));
 }

 @Test
 public void testAbsUrl() {
 assertNull(leafNode.absUrl("testAttribute"));
 }

 @Test
 public void testBaseUri() {
 assertEmpty(leafNode.baseUri());
 Node parentNode = new Element("parent");
 parentNode.appendChild(leafNode);
 assertEquals("", parentNode.baseUri());
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