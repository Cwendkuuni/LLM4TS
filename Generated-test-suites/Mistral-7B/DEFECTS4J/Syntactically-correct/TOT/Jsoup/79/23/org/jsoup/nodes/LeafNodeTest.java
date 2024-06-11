import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.LeafNode;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.helper.Validate;
import static org.junit.Assert.*;

public class LeafNodeTest {

 @Test
 public void testHasAttributes() {
 LeafNode leafNode = new TextNode("Test Text");
 assertFalse(leafNode.hasAttributes());

 LeafNode leafNodeWithAttributes = new Attributes().add("testAttribute", "testValue").setParent(new Elements.Document());
 assertTrue(leafNodeWithAttributes.hasAttributes());
 }

 @Test
 public void testAttributes() {
 LeafNode leafNodeWithAttributes = new Attributes().add("testAttribute", "testValue").setParent(new Elements.Document());
 assertEquals("testValue", leafNodeWithAttributes.attributes().get("testAttribute"));
 }

 @Test
 public void testEnsureAttributes() {
 LeafNode leafNode = new TextNode("Test Text");
 assertNull(leafNode.attributes());

 leafNode.ensureAttributes();
 assertTrue(leafNode.hasAttributes());
 assertEquals("Test Text", leafNode.attributes().get("name"));
 }

 @Test
 public void testCoreValue() {
 LeafNode leafNode = new TextNode("Test Text");
 assertEquals("Test Text", leafNode.coreValue());

 LeafNode leafNodeWithAttributes = new Attributes().add("testAttribute", "testValue").setParent(new Elements.Document());
 assertEquals("", leafNodeWithAttributes.coreValue());
 }

 @Test
 public void testCoreValueSetter() {
 LeafNode leafNode = new TextNode("Test Text");
 leafNode.coreValue("Test Value");
 assertEquals("Test Value", leafNode.coreValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAttrNullKey() {
 LeafNode leafNode = new TextNode("Test Text");
 leafNode.attr(null, "Test Value");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAttrEmptyKey() {
 LeafNode leafNode = new TextNode("Test Text");
 leafNode.attr("", "Test Value");
 }

 @Test
 public void testAttr() {
 LeafNode leafNode = new TextNode("Test Text");
 leafNode.attr("testAttribute", "testValue");
 assertEquals("testValue", leafNode.attr("testAttribute"));
 }

 @Test
 public void testAttrSetterWithExistingAttribute() {
 LeafNode leafNode = new Attributes().add("testAttribute", "testValue").setParent(new Elements.Document());
 leafNode.attr("testAttribute", "newTestValue");
 assertEquals("newTestValue", leafNode.attr("testAttribute"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAttrExistingKeyWithNullValue() {
 LeafNode leafNode = new Attributes().add("testAttribute", "testValue").setParent(new Elements.Document());
 leafNode.attr("testAttribute", null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAttrExistingKeyWithEmptyValue() {
 LeafNode leafNode = new Attributes().add("testAttribute", "testValue").setParent(new Elements.Document());
 leafNode.attr("testAttribute", "");
 }

 @Test
 public void testHasAttr() {
 LeafNode leafNode = new TextNode("Test Text");
 assertFalse(leafNode.hasAttr("testAttribute"));

 LeafNode leafNodeWithAttribute = new Attributes().add("testAttribute", "testValue").setParent(new Elements.Document());
 assertTrue(leafNodeWithAttribute.hasAttr("testAttribute"));
 }

 @Test
 public void testRemoveAttr() {
 LeafNode leafNodeWithAttribute = new Attributes().add("testAttribute", "testValue").setParent(new Elements.Document());
 leafNodeWithAttribute.removeAttr("testAttribute");
 assertFalse(leafNodeWithAttribute.hasAttr("testAttribute"));
 }

 @Test
 public void testAbsUrl() {
 LeafNode leafNode = new TextNode("Test Text");
 assertNull(leafNode.absUrl("testAttribute"));
 }

 @Test
 public void testBaseUri() {
 LeafNode leafNode = new TextNode("Test Text").setParent(new Elements.Document());
 assertEquals("", leafNode.baseUri());

 Elements.Document document = new Elements.Document();
 Element element = document.head();
 element.attr("base", "http://test.com");
 leafNode.setParent(element);
 assertEquals("http://test.com", leafNode.baseUri());
 }

 @Test
 public void testChildNodeSize() {
 LeafNode leafNode = new TextNode("Test Text");
 assertEquals(0, leafNode.childNodeSize());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testEnsureChildNodes() {
 LeafNode leafNode = new TextNode("Test Text");
 leafNode.ensureChildNodes();
 }
}