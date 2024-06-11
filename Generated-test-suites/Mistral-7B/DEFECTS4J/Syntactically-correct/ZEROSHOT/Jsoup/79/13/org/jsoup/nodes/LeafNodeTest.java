import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.LeafNode;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class LeafNodeTest {

 private LeafNode leafNode;

 @Before
 public void setUp() {
 leafNode = new LeafNode("test") {
 @Override
 public String attr(String key) {
 return super.attr(key);
 }

 @Override
 public void attr(String key, String value) {
 super.attr(key, value);
 }

 @Override
 public boolean hasAttr(String key) {
 return super.hasAttr(key);
 }

 @Override
 public Node removeAttr(String key) {
 return super.removeAttr(key);
 }

 @Override
 public String absUrl(String key) {
 return super.absUrl(key);
 }

 @Override
 public String baseUri() {
 return super.baseUri();
 }

 @Override
 protected void doSetBaseUri(String baseUri) {
 // noop
 }

 @Override
 public int childNodeSize() {
 throw new UnsupportedOperationException("Leaf Nodes do not have child nodes.");
 }

 @Override
 protected List<Node> ensureChildNodes() {
 throw new UnsupportedOperationException("Leaf Nodes do not have child nodes.");
 }
 };
 }

 @Test
 public void testHasAttributes() {
 assertFalse(leafNode.hasAttributes());
 leafNode.coreValue("testValue");
 assertTrue(leafNode.hasAttributes());
 leafNode.ensureAttributes();
 leafNode.removeAttribute(nodeName());
 assertFalse(leafNode.hasAttributes());
 }

 @Test
 public void testAttributes() {
 Attributes attributes = new Attributes();
 attributes.put("testKey", "testValue");
 leafNode.value = attributes;
 assertSame(attributes, leafNode.attributes());
 }

 @Test
 public void testCoreValue() {
 leafNode.coreValue("testValue");
 assertEquals("testValue", leafNode.coreValue());
 }

 @Test
 public void testSetCoreValue() {
 leafNode.coreValue("testValue");
 assertEquals("testValue", leafNode.attr(nodeName()));
 leafNode.coreValue("newTestValue");
 assertEquals("newTestValue", leafNode.attr(nodeName()));
 }

 @Test
 public void testAttr() {
 leafNode.attr("testKey", "testValue");
 assertEquals("testValue", leafNode.attr("testKey"));
 assertNull(leafNode.attr("testOtherKey"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetAttrNullKey() {
 leafNode.attr(null, "testValue");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetAttrNullValue() {
 leafNode.attr("testKey", null);
 }

 @Test
 public void testRemoveAttr() {
 leafNode.attr("testKey", "testValue");
 leafNode.removeAttr("testKey");
 assertNull(leafNode.attr("testKey"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRemoveAttrNullKey() {
 leafNode.removeAttr(null);
 }

 @Test
 public void testAbsUrl() {
 // Assume baseUri and absUrl are set appropriately for testing
 assertEquals("http://test.com/testValue", leafNode.absUrl("testValue"));
 }

 @Test
 public void testBaseUri() {
 leafNode.coreValue("testValue");
 assertEquals("", leafNode.baseUri());
 leafNode.ensureParent(new Element("testParent"));
 assertEquals("testParent", leafNode.baseUri());
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