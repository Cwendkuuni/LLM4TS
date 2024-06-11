import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import static org.junit.Assert.*;

public class LeafNodeTest {

 private LeafNode leafNode;

 @Before
 public void setUp() {
 leafNode = new Element("testNode");
 }

 @Test
 public void testHasAttributes() {
 assertFalse(leafNode.hasAttributes());
 leafNode.coreValue("testAttribute");
 assertTrue(leafNode.hasAttributes());
 }

 @Test
 public void testAttributes() {
 leafNode.coreValue("testAttributeValue");
 Attributes attributes = leafNode.attributes();
 assertEquals("testAttributeValue", attributes.get("testAttribute"));
 }

 @Test
 public void testCoreValue() {
 leafNode.coreValue("testCoreValue");
 assertEquals("testCoreValue", leafNode.coreValue());
 }

 @Test
 public void testCoreValueNull() {
 leafNode.coreValue(null);
 assertNull(leafNode.coreValue());
 }

 @Test
 public void testCoreValueEmptyString() {
 leafNode.coreValue("");
 assertEquals("", leafNode.coreValue());
 }

 @Test
 public void testCoreValueSetWithAttributes() {
 Attributes attributes = new Attributes();
 attributes.put("testAttribute", "testAttributeValue");
 leafNode.value = attributes;
 leafNode.coreValue("testCoreValue");
 assertEquals("testCoreValue", leafNode.coreValue());
 assertEquals("testAttributeValue", leafNode.attr("testAttribute"));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testChildNodes() {
 leafNode.ensureChildNodes();
 }
}