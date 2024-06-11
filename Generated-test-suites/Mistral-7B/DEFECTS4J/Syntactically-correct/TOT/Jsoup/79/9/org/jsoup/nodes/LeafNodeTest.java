import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.LeafNode;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.helper.Validate;

public class LeafNodeTest {

 @Test
 public void testLeafNodeHasAttributes() {
 LeafNode leafNode = new Element("div"); // create a new LeafNode instance
 leafNode.coreValue("test"); // set the core value

 assertTrue(leafNode.hasAttributes()); // test if the node has attributes
 assertEquals("test", leafNode.attributes().get("name")); // test if the attribute value is correct
 }

 @Test
 public void testLeafNodeNoAttributes() {
 LeafNode leafNode = new TextNode("Hello World"); // create a new LeafNode instance

 assertFalse(leafNode.hasAttributes()); // test if the node has no attributes
 }

 @Test
 public void testLeafNodeSetAndGetCoreValue() {
 LeafNode leafNode = new Element("div"); // create a new LeafNode instance

 leafNode.coreValue("test"); // set the core value
 assertEquals("test", leafNode.coreValue()); // test if the core value is correctly set and gotten

 leafNode.coreValue("newTest"); // set the core value again
 assertEquals("newTest", leafNode.coreValue()); // test if the new core value is correctly set and gotten
 }

 @Test
 public void testLeafNodeSetAndGetAttribute() {
 LeafNode leafNode = new Element("div"); // create a new LeafNode instance

 leafNode.attr("name", "test"); // set the attribute
 assertEquals("test", leafNode.attr("name")); // test if the attribute value is correctly set and gotten

 leafNode.attr("name", "newTest"); // set the attribute value again
 assertEquals("newTest", leafNode.attr("name")); // test if the new attribute value is correctly set and gotten
}

 @Test
 public void testLeafNodeSetAttributeWithSameNameAsNodeName() {
 LeafNode leafNode = new Element("div"); // create a new LeafNode instance

 leafNode.coreValue("test"); // set the core value
 leafNode.attr("div", "newTest"); // set the attribute with the same name as the node name

 assertEquals("newTest", leafNode.coreValue()); // test if the core value is changed
 assertEquals("newTest", leafNode.attr("div")); // test if the attribute value is correctly set and gotten
}

 @Test(expected = IllegalArgumentException.class)
 public void testLeafNodeSetAttributeWithNullName() {
 LeafNode leafNode = new Element("div"); // create a new LeafNode instance

 leafNode.attr(null, "value"); // attempt to set the attribute with a null name
}

 @Test(expected = IllegalArgumentException.class)
 public void testLeafNodeSetAttributeWithEmptyStringName() {
 LeafNode leafNode = new Element("div"); // create a new LeafNode instance

 leafNode.attr("", "value"); // attempt to set the attribute with an empty string name
}

 @Test(expected = IllegalStateException.class)
 public void testLeafNodeSetAttributeAfterEnsuringAttributes() {
 LeafNode leafNode = new Element("div"); // create a new LeafNode instance
 leafNode.setAttributeNames("name"); // ensure attributes
 leafNode.attr("name", "test"); // attempt to set the attribute after ensuring attributes
}

 @Test
 public void testLeafNodeHasAttr() {
 LeafNode leafNode = new Element("div"); // create a new LeafNode instance
 leafNode.attr("name", "test"); // set the attribute

 assertTrue(leafNode.hasAttr("name")); // test if the node has the attribute
}

 @Test
 public void testLeafNodeRemoveAttr() {
 LeafNode leafNode = new Element("div"); // create a new LeafNode instance
 leafNode.attr("name", "test"); // set the attribute
 leafNode.removeAttr("name"); // remove the attribute

 assertFalse(leafNode.hasAttr("name")); // test if the attribute was removed
}
}