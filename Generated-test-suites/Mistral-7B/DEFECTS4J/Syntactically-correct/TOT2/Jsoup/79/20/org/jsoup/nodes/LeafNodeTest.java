import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class LeafNodeTest {

    @Test
    public void testHasAttributes() {
        LeafNode leafNode = new LeafNode();
        assertFalse(leafNode.hasAttributes());

        LeafNode leafNodeWithAttributes = new LeafNode();
        Attributes attributes = new Attributes();
        leafNodeWithAttributes.value = attributes;
        assertTrue(leafNodeWithAttributes.hasAttributes());
    }

    @Test
    public void testCoreValue() {
        LeafNode leafNode = new LeafNode();
        assertNull(leafNode.coreValue());

        LeafNode leafNodeWithValue = new LeafNode();
        leafNodeWithValue.value = "Test Value";
        assertEquals("Test Value", leafNodeWithValue.coreValue());
    }

    @Test
    public void testAttr() {
        LeafNode leafNode = new LeafNode();
        assertEquals(EmptyString, leafNode.attr("test"));

        LeafNode leafNodeWithValue = new LeafNode();
        Attributes attributes = new Attributes();
        leafNodeWithValue.value = attributes;
        attributes.put("test", "Test Value");
        assertEquals("Test Value", leafNodeWithValue.attr("test"));
    }

    @Test
    public void testHasAttr() {
        LeafNode leafNode = new LeafNode();
        assertFalse(leafNode.hasAttr("test"));

        LeafNode leafNodeWithAttribute = new LeafNode();
        Attributes attributes = new Attributes();
        leafNodeWithAttribute.value = attributes;
        attributes.put("test", "Test Value");
        assertTrue(leafNodeWithAttribute.hasAttr("test"));
    }

    @Test
    public void testEmptyAttributes() {
        LeafNode leafNode = new LeafNode();
        Attributes attributes = new Attributes();
        leafNode.value = attributes;
        assertFalse(leafNode.hasAttributes());
        assertEquals(EmptyString, leafNode.attr("test"));
        assertFalse(leafNode.hasAttr("test"));
    }

    @Test
    public void testSingleAttribute() {
        LeafNode leafNode = new LeafNode();
        Attributes attributes = new Attributes();
        leafNode.value = attributes;
        attributes.put("test", "Test Value");
        assertTrue(leafNode.hasAttributes());
        assertEquals("Test Value", leafNode.attr("test"));
        assertTrue(leafNode.hasAttr("test"));
    }

    @Test
    public void testMultipleAttributes() {
        LeafNode leafNode = new LeafNode();
        Attributes attributes = new Attributes();
        leafNode.value = attributes;
        attributes.put("test1", "Test Value 1");
        attributes.put("test2", "Test Value 2");
        assertTrue(leafNode.hasAttributes());
        assertEquals("Test Value 1", leafNode.attr("test1"));
        assertEquals("Test Value 2", leafNode.attr("test2"));
        assertTrue(leafNode.hasAttr("test1"));
        assertTrue(leafNode.hasAttr("test2"));
    }

    @Test
    public void testNullValue() {
        LeafNode leafNode = new LeafNode();
        Attributes attributes = new Attributes();
        leafNode.value = attributes;
        attributes.put("test", null);
        assertTrue(leafNode.hasAttributes());
        assertNull(leafNode.attr("test"));
        assertTrue(leafNode.hasAttr("test"));
    }

    @Test
    public void testEmptyStringValue() {
        LeafNode leafNode = new LeafNode();
        Attributes attributes = new Attributes();
        leafNode.value = attributes;
        attributes.put("test", EmptyString);
        assertTrue(leafNode.hasAttributes());
        assertEquals(EmptyString, leafNode.attr("test"));
        assertTrue(leafNode.hasAttr("test"));
    }
}