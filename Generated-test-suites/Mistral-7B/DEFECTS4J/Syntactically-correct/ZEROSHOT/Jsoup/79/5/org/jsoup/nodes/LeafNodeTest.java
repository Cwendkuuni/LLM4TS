import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.LeafNode;
import static org.jsoup.nodes.Node.*;

public class LeafNodeTest {

 @Test
 public void testHasAttributes() {
 LeafNode leafNode = new Element("div");
 Assert.assertFalse(leafNode.hasAttributes());
 leafNode.coreValue("test");;
 Assert.assertTrue(leafNode.hasAttributes());
 }

 @Test
 public void testAttributes() {
 LeafNode leafNode = new Element("div");
 Attributes attributes = leafNode.attributes();
 Assert.assertNull(attributes);
 leafNode.coreValue("test");
 Assert.assertNotNull(leafNode.attributes());
 }

 @Test
 public void testEnsureAttributes() {
 LeafNode leafNode = new Element("div");
 leafNode.coreValue("test");
 Attributes attributes = leafNode.attributes();
 attributes.put("test", "value");
 Assert.assertEquals("value", leafNode.attr("test"));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testChildNodeSize() {
 LeafNode leafNode = new Element("div");
 leafNode.childNodeSize();
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testEnsureChildNodes() {
 LeafNode leafNode = new Element("div");
 leafNode.ensureChildNodes();
 }

 @Test
 public void testCoreValue() {
 LeafNode leafNode = new Element("div");
 leafNode.coreValue("test");
 Assert.assertEquals("test", leafNode.coreValue());
 }

 @Test
 public void testCoreValueSetter() {
 LeafNode leafNode = new Element("div");
 leafNode.coreValue("test");
 leafNode.coreValue("newTest");
 Assert.assertEquals("newTest", leafNode.coreValue());
 }

 @Test
 public void testAttr() {
 LeafNode leafNode = new Element("div");
 Assert.assertEquals("", leafNode.attr("test"));
 leafNode.coreValue("test");
 Assert.assertEquals("test", leafNode.attr("test"));
 }

 @Test
 public void testAttrSetter() {
 LeafNode leafNode = new Element("div");
 leafNode.attr("test", "value");
 Assert.assertEquals("value", leafNode.attr("test"));
 }

 @Test
 public void testHasAttr() {
 LeafNode leafNode = new Element("div");
 Assert.assertFalse(leafNode.hasAttr("test"));
 leafNode.coreValue("test");
 Assert.assertTrue(leafNode.hasAttr("test"));
 }

 @Test
 public void testRemoveAttr() {
 LeafNode leafNode = new Element("div");
 leafNode.coreValue("test");
 leafNode.removeAttr("test");
 Assert.assertEquals("", leafNode.attr("test"));
 }

 @Test
 public void testAbsUrl() {
 LeafNode leafNode = new Element("img", "src", "test.jpg");
 Assert.assertEquals("test.jpg", leafNode.absUrl("src"));
 }

 @Test
 public void testBaseUri() {
 LeafNode parent = new Element("html");
 LeafNode leafNode = new Element("img", "src", "test.jpg");
 parent.appendChild(leafNode);
 Assert.assertEquals("", leafNode.baseUri());
 parent.attr("base", "/test");
 Assert.assertEquals("/test/test.jpg", leafNode.absUrl("src"));
 }
}