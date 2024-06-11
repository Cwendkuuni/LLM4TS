package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;

import static org.jsoup.nodes.Attributes.dataPrefix;

/**
 * Test class for the Attributes class.
 */
public class AttributesTest {

    /**
     * Tests the basic functionality of adding and getting attributes using the case-sensitive method.
     */
    @Test
    public void testBasicFunctionality() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Assert.assertEquals("value1", attributes.get("key1"));
        Assert.assertEquals("value2", attributes.get("key2"));
        Assert.assertEquals(2, attributes.size());
    }

    /**
     * Tests the basic functionality of adding and getting attributes using the case-insensitive method.
     */
    @Test
    public void testBasicFunctionalityIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("key1", "value1");
        attributes.putIgnoreCase("key2", "value2");

        Assert.assertEquals("value1", attributes.getIgnoreCase("key1"));
        Assert.assertEquals("value2", attributes.getIgnoreCase("key2"));
        Assert.assertEquals(2, attributes.size());
    }

    /**
     * Tests the removal of an attribute using the case-sensitive method.
     */
    @Test
    public void testRemoveAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Assert.assertEquals(2, attributes.size());
        attributes.remove("key1");
        Assert.assertEquals("value2", attributes.get("key2"));
        Assert.assertEquals(1, attributes.size());
    }

    /**
     * Tests the removal of an attribute using the case-insensitive method.
     */
    @Test
    public void testRemoveAttributeIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("key1", "value1");
        attributes.putIgnoreCase("key2", "value2");

        Assert.assertEquals(2, attributes.size());
        attributes.removeIgnoreCase("key1");
        Assert.assertEquals("value2", attributes.getIgnoreCase("key2"));
        Assert.assertEquals(1, attributes.size());
    }

    /**
     * Tests the addition and removal of duplicate attributes using the case-sensitive method.
     */
    @Test
    public void testDuplicateAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key1", "value2");

        Assert.assertEquals("value2", attributes.get("key1"));
        Assert.assertEquals(1, attributes.size());
    }

    /**
     * Tests the addition and removal of duplicate attributes using the case-insensitive method.
     */
    @Test
    public void testDuplicateAttributesIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("key1", "value1");
        attributes.putIgnoreCase("key1", "value2");

        Assert.assertEquals("value2", attributes.getIgnoreCase("key1"));
        Assert.assertEquals(1, attributes.size());
    }

    /**
     * Tests the addition of a boolean attribute using the case-insensitive method.
     */
    @Test
    public void testBooleanAttribute() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("class", true);

        Assert.assertNull(attributes.get("class"));
        Assert.assertTrue(attributes.hasKeyIgnoreCase("class"));
    }

    /**
     * Tests the addition of multiple boolean attributes using the case-insensitive method.
     */
    @Test
    public void testMultipleBooleanAttributes() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("class", true);
        attributes.putIgnoreCase("disabled", true);

        Assert.assertNull(attributes.get("class"));
        Assert.assertNull(attributes.get("disabled"));
        Assert.assertTrue(attributes.hasKeyIgnoreCase("class"));
        Assert.assertTrue(attributes.hasKeyIgnoreCase("disabled"));
    }

    /**
     * Tests the addition and removal of an attribute with a special character in its key using the case-sensitive method.
     */
    @Test
    public void testAttributeWithSpecialCharacter() {
        Attributes attributes = new Attributes();
        attributes.put("key-with-special-character", "value");

        Assert.assertEquals("value", attributes.get("key-with-special-character"));
        attributes.remove("key-with-special-character");
        Assert.assertFalse(attributes.hasKey("key-with-special-character"));
    }

    /**
     * Tests the addition and removal of an attribute with a special character in its key using the case-insensitive method.
     */
    @Test
    public void testAttributeWithSpecialCharacterIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("key-with-special-character", "value");

        Assert.assertEquals("value", attributes.getIgnoreCase("key-with-special-character"));
        attributes.removeIgnoreCase("key-with-special-character");
        Assert.assertFalse(attributes.hasKeyIgnoreCase("key-with-special-character"));
    }
}