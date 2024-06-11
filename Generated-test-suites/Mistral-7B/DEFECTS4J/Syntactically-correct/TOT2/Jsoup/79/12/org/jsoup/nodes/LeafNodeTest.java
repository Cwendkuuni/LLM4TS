import org.jsoup.nodes.Attributes;
import org.junit.Test;

import static org.junit.Assert.*;

public class LeafNodeTest {

    @Test
    public void testHasAttributes() {
        LeafNode leafNode = new LeafNode() {
            {
                value = new Attributes();
            }
        };
        assertTrue(leafNode.hasAttributes());

        LeafNode leafNodeWithoutAttributes = new LeafNode();
        assertFalse(leafNodeWithoutAttributes.hasAttributes());
    }

    @Test
    public void testCoreValue() {
        LeafNode leafNode = new LeafNode();
        leafNode.coreValue("Test Value");
        assertEquals("Test Value", leafNode.coreValue());

        LeafNode leafNodeEmpty = new LeafNode();
        assertEquals("", leafNodeEmpty.coreValue());

        LeafNode leafNodeWithEmptyStringValue = new LeafNode();
        leafNodeWithEmptyStringValue.coreValue(" ");
        assertEquals("", leafNodeWithEmptyStringValue.coreValue());

        LeafNode leafNodeWithNullValue = new LeafNode();
        leafNodeWithNullValue.coreValue(null);
        assertNull(leafNodeWithNullValue.coreValue());
    }

    @Test
    public void testAttr() {
        LeafNode leafNode = new LeafNode();
        leafNode.attr("testKey", "Test Value");
        assertEquals("Test Value", leafNode.attr("testKey"));

        LeafNode leafNodeWithEmptyStringValue = new LeafNode();
        leafNodeWithEmptyStringValue.attr("testKey", "");
        assertEquals("", leafNodeWithEmptyStringValue.attr("testKey"));

        LeafNode leafNodeWithNullValue = new LeafNode();
        leafNodeWithNullValue.attr("testKey", null);
        assertNull(leafNodeWithNullValue.attr("testKey"));

        LeafNode leafNodeWithUnknownKey = new LeafNode();
        leafNodeWithUnknownKey.attr("testKey", "Test Value");
        assertFalse(leafNodeWithUnknownKey.hasAttr("testKeyOther"));
    }

    @Test
    public void testHasAttr() {
        LeafNode leafNode = new LeafNode();
        leafNode.attr("testKey", "Test Value");
        assertTrue(leafNode.hasAttr("testKey"));

        LeafNode leafNodeWithoutAttribute = new LeafNode();
        assertFalse(leafNodeWithoutAttribute.hasAttr("testKey"));

        LeafNode leafNodeWithEmptyStringValue = new LeafNode();
        leafNodeWithEmptyStringValue.attr("testKey", "");
        assertTrue(leafNodeWithEmptyStringValue.hasAttr("testKey"));

        LeafNode leafNodeWithNullValue = new LeafNode();
        leafNodeWithNullValue.attr("testKey", null);
        assertFalse(leafNodeWithNullValue.hasAttr("testKey"));
    }

    // Add other method tests here
}