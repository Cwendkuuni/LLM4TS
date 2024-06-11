import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.LeafNode;
import org.jsoup.nodes.Node;
import org.junit.Test;
import static org.junit.Assert.*;

public class LeafNodeTest {

 @Test
 public void testHasAttributes() {
 LeafNode node = new MyLeafNode(); // Assuming MyLeafNode is a subclass of LeafNode
 node.coreValue("testValue");

 // Test case for hasAttributes() with no attributes
 assertTrue(node.hasAttributes() == false);

 // Test case for hasAttributes() with attributes
 Attributes attributes = new Attributes();
 attributes.put("testKey", "testValue");
 node.value = attributes;
 assertTrue(node.hasAttributes() == true);
 }

 @Test
 public void testAttributes() {
 LeafNode node = new MyLeafNode();
 node.coreValue("testValue");

 // Test case for attributes() with no attributes
 assertNull(node.attributes());

 // Test case for attributes() with attributes
 Attributes attributes = new Attributes();
 attributes.put("testKey1", "testValue1");
 attributes.put("testKey2", "testValue2");
 node.value = attributes;
 assertNotNull(node.attributes());
 assertEquals("testValue1", node.attributes().get("testKey1"));
 assertEquals("testValue2", node.attributes().get("testKey2"));
 }

 @Test
 public void testEnsureAttributes() {
 LeafNode node = new MyLeafNode();
 node.coreValue("testValue");

 // Test case for ensureAttributes() with no attributes
 node.ensureAttributes();
 assertNotNull(node.value);
 assertInstanceOf(Attributes.class, node.value);
 assertEquals("testValue", node.value.get(node.nodeName()));

 // Test case for ensureAttributes() with attributes
 Attributes attributes = new Attributes();
 attributes.put("testKey1", "testValue1");
 attributes.put("testKey2", "testValue2");
 node.value = attributes;
 node.ensureAttributes();
 assertSame(node.value, node.attributes());
 }

 @Test
 public void testCoreValue() {
 LeafNode node = new MyLeafNode();

 // Test case for coreValue() with no value
 assertNull(node.coreValue());

 // Test case for coreValue() with a value
 node.coreValue("testValue");
 assertEquals("testValue", node.coreValue());
 }

 @Test(expected = NullPointerException.class)
 public void testCoreValueNullPointer() {
 LeafNode node = new MyLeafNode();
 node.coreValue(null);
 }

 @Test
 public void testAttr() {
 LeafNode node = new MyLeafNode();
 node.coreValue("testValue");

 // Test case for attr() with no attributes
 assertEquals("testValue", node.attr(node.nodeName()));

 // Test case for attr() with attributes
 Attributes attributes = new Attributes();
 attributes.put("testKey", "testValue");
 node.value = attributes;
 assertEquals("testValue", node.attr("testKey"));
 assertNull(node.attr("nonExistentKey"));
 }

 @Test(expected = NullPointerException.class)
 public void testAttrNullPointer() {
 LeafNode node = new MyLeafNode();
 node.attr(null, "testValue");
 }

 @Test
 public void testAttrSet() {
 LeafNode node = new MyLeafNode();
 node.coreValue("testValue");

 // Test case for attr() with no attributes
 node.attr(node.nodeName(), "testValue");
 assertEquals("testValue", node.attr(node.nodeName()));

 // Test case for attr() with attributes
 Attributes attributes = new Attributes();
 attributes.put("testKey1", "testValue1");
 attributes.put("testKey2", "testValue2");
 node.value = attributes;
 node.attr("testKey3", "testValue3");
 assertEquals("testValue3", node.attr("testKey3"));
 }

 @Test(expected = NullPointerException.class)
 public void testAttrSetNullPointer() {
 LeafNode node = new MyLeafNode();
 node.attr(null, "testValue");
 }

 @Test
 public void testHasAttr() {
 LeafNode node = new MyLeafNode();
 node.coreValue("testValue");

 // Test case for hasAttr() with no attributes
 assertFalse(node.hasAttr("nonExistentKey"));

 // Test case for hasAttr() with attributes
 Attributes attributes = new Attributes();
 attributes.put("testKey1", "testValue1");
 node.value = attributes;
 assertTrue(node.hasAttr("testKey1"));
 }

 @Test
 public void testRemoveAttr() {
 LeafNode node = new MyLeafNode();
 node.coreValue("testValue");
 Attributes attributes = new Attributes();
 attributes.put("testKey1", "testValue1");
 node.value = attributes;

 // Test case for removeAttr() with no attributes
 node.removeAttr("nonExistentKey");
 assertNull(node.value);

 // Test case for removeAttr() with attributes
 node.removeAttr("testKey1");
 assertNotNull(node.value);
 assertFalse(node.hasAttr("testKey1"));
 }

 @Test
 public void testAbsUrl() {
 LeafNode node = new MyLeafNode();
 node.coreValue("testValue");

 // Test case for absUrl() with no baseUri
 assertEquals("", node.absUrl("testKey"));

 // Test case for absUrl() with a baseUri
 String baseUri = "http://example.com";
 node.parent().baseUri(baseUri);
 assertEquals("http://example.com/testValue", node.absUrl("testKey"));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testChildNodeSize() {
 LeafNode node = new MyLeafNode();
 node.childNodeSize();
 }
}