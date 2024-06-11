import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.nodes.*;

public class LeafNodeTest {
  @Test
  public void testHasAttributes() {
    LeafNode node = new LeafNode();
    assertFalse(node.hasAttributes());

    LeafNode nodeWithAttributes = new LeafNode();
    nodeWithAttributes.value = new Attributes();
    assertTrue(nodeWithAttributes.hasAttributes());
  }

  @Test(expected = NullPointerException.class)
  public void testAttrWithNullKey() {
    LeafNode node = new LeafNode();
    node.attr(null, "");
  }

  @Test
  public void testCoreValue() {
    LeafNode node = new LeafNode();
    assertEquals("", node.coreValue());

    LeafNode nodeWithValue = new LeafNode();
    nodeWithValue.coreValue("TestValue");
    assertEquals("TestValue", nodeWithValue.coreValue());
  }

  @Test
  public void testAttr() {
    LeafNode node = new LeafNode();
    node.attr("testKey", "TestValue");
    assertEquals("TestValue", node.attr("testKey"));
  }

  @Test
  public void testRemoveAttr() {
    LeafNode nodeWithAttribute = new LeafNode();
    nodeWithAttribute.attr("testKey", "TestValue");
    LeafNode nodeWithoutAttribute = nodeWithAttribute.removeAttr("testKey");
    assertEquals(0, nodeWithoutAttribute.attributes().size());
    assertNotSame(nodeWithAttribute, nodeWithoutAttribute);
  }

  @Test
  public void testAbsUrlWithNullBaseUri() {
    LeafNode node = new LeafNode();
    try {
      node.absUrl(null);
      fail("Expected NullPointerException");
    } catch (NullPointerException e) {
      // Expected
    }
  }

  @Test
  public void testBaseUriWithNoParent() {
    LeafNode node = new LeafNode();
    assertEquals("", node.baseUri());
  }

  @Test
  public void testHasAttr() {
    LeafNode node = new LeafNode();
    assertFalse(node.hasAttr("testKey"));

    LeafNode nodeWithAttribute = new LeafNode();
    nodeWithAttribute.attr("testKey", "TestValue");
    assertTrue(nodeWithAttribute.hasAttr("testKey"));
  }

  @Test
  public void testRemoveAttrWithNoAttribute() {
    LeafNode node = new LeafNode();
    assertEquals(node, node.removeAttr("testKey"));
  }

  @Test
  public void testChildNodeSize() {
    LeafNode node = new LeafNode();
    assertEquals(0, node.childNodeSize());
  }
}