import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.nodes.*;

public class LeafNodeTest {

    @Test
    public void testHasAttributes() {
        // Test case from basic test file
    }

    @Test
    public void testAttributes() {
        // Test case from basic test file
    }

    @Test
    public void testCoreValue() {
        // Test case from basic test file
    }

    @Test
    public void testCoreValue_EmptyString() {
        LeafNode leafNode = new LeafNode() {
            {
                value = "";
            }
        };
        assertEquals("", leafNode.coreValue());
    }

    @Test
    public void testAttr_EmptyString() {
        LeafNode leafNode = new LeafNode() {
            {
                value = new Attributes();
            }
        };
        leafNode.attr("test", "");
        assertEquals("", leafNode.attr("test"));
    }

    @Test(expected = NullPointerException.class)
    public void testAttr_NullKey() {
        LeafNode leafNode = new LeafNode() {
            {
                value = new Attributes();
            }
        };
        leafNode.attr(null, "Test Value");
    }

    @Test
    public void testAttr_NullValue() {
        LeafNode leafNode = new LeafNode() {
            {
                value = new Attributes();
            }
        };
        leafNode.attr("test", null);
        assertNull(leafNode.attr("test"));
    }

    @Test
    public void testGetAttributeNames() {
        LeafNode leafNode = new LeafNode() {
            {
                value = new Attributes();
                value.put("test1", "Test Value 1");
                value.put("test2", "Test Value 2");
            }
        };
        Attributes attributes = leafNode.attributes();
        List<String> attributeNames = attributes.attributeNames();
        assertEquals(2, attributeNames.size());
        assertTrue(attributeNames.contains("test1"));
        assertTrue(attributeNames.contains("test2"));
    }

    @Test
    public void testGetAttributeValues() {
        LeafNode leafNode = new LeafNode() {
            {
                value = new Attributes();
                value.put("test1", "Test Value 1");
                value.put("test2", "Test Value 2");
            }
        };
        Attributes attributes = leafNode.attributes();
        List<String> attributeValues = attributes.attributeValues();
        assertEquals(2, attributeValues.size());
        assertEquals("Test Value 1", attributeValues.get(0));
        assertEquals("Test Value 2", attributeValues.get(1));
    }

    @Test
    public void testGetAttribute() {
        LeafNode leafNode = new LeafNode() {
            {
                value = new Attributes();
                value.put("test", "Test Value");
            }
        };
        assertEquals("Test Value", leafNode.getAttribute("test"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEnsureChildNodes() {
        // Test case from basic test file
    }

    @Test
    public void testAbsUrl() {
        // Test case from basic test file
    }

    @Test
    public void testBaseUri() {
        // Test case from basic test file
    }

    @Test
    public void testChildNodeSize() {
        // Test case from basic test file
    }
}