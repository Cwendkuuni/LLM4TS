package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;

import java.util.EmptyStackException;

public class LeafNodeTest {

    @Test
    public void testHasAttributes() {
        LeafNode leafNode = new LeafNode() {{
            value = new Attributes();
            value.put("testKey1", "testValue1");
        }};

        assertTrue(leafNode.hasAttributes());

        leafNode = new LeafNode();

        assertFalse(leafNode.hasAttributes());
    }

    @Test
    public void testCoreValue() {
        LeafNode leafNode = new LeafNode() {{
            value = "testValue";
        }};

        assertEquals("testValue", leafNode.coreValue());

        leafNode = new LeafNode();

        assertEquals(EmptyString.INSTANCE, leafNode.coreValue());
    }

    @Test
    public void testCoreValue_SetValue() {
        LeafNode leafNode = new LeafNode();

        leafNode.coreValue("testValue");

        assertEquals("testValue", leafNode.coreValue());
    }

    @Test
    public void testAttr() {
        LeafNode leafNode = new LeafNode() {{
            value = new Attributes() {{
                put("testKey1", "testValue1");
            }};
        }};

        assertEquals("testValue1", leafNode.attr("testKey1"));

        leafNode = new LeafNode();

        assertEquals(EmptyString.INSTANCE, leafNode.attr("testKey1"));
    }

    @Test
    public void testAttr_SetValue() {
        LeafNode leafNode = new LeafNode();

        leafNode.attr("testKey1", "testValue1");

        assertEquals("testValue1", leafNode.attr("testKey1"));
    }

    @Test
    public void testAttr_NullName() {
        LeafNode leafNode = new LeafNode();

        Assert.assertThrows(NullPointerException.class, () -> leafNode.attr(null));
    }

    @Test
    public void testAttr_EmptyValue() {
        LeafNode leafNode = new LeafNode();

        leafNode.attr("testKey1", "");

        assertEquals("", leafNode.attr("testKey1"));
    }

    @Test
    public void testAttr_DuplicateKey() {
        LeafNode leafNode = new LeafNode() {{
            value = new Attributes() {{
                put("testKey1", "testValue1");
                put("testKey1", "testValue2");
            }};
        }};

        assertEquals("testValue2", leafNode.attr("testKey1"));
    }

    @Test
    public void testAttr_SetValue_NullValue() {
        LeafNode leafNode = new LeafNode();

        leafNode.attr("testKey1", null);

        assertNull(leafNode.attr("testKey1"));
    }

    @Test
    public void testAttr_SetValue_DuplicateValue() {
        LeafNode leafNode = new LeafNode() {{
            value = new Attributes() {{
                put("testKey1", "testValue1");
                put("testKey1", "testValue2");
            }};
        }};

        assertEquals("testValue2", leafNode.attr("testKey1"));

        leafNode.attr("testKey1", "testValue3");

        assertEquals("testValue3", leafNode.attr("testKey1"));

        assertNotEquals("testValue1", leafNode.attr("testKey1"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetBaseUri() {
        LeafNode leafNode = new LeafNode();

        leafNode.setBaseUri("testBaseUri");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testChildNodes() {
        LeafNode leafNode = new LeafNode();

        leafNode.childNodes();
    }
}