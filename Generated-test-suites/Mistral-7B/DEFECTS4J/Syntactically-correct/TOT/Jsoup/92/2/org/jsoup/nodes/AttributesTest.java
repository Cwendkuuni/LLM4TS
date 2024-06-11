package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;

public class AttributesTest {

    @Test
    public void testGetAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("class", "example");
        String result = attributes.get("class");
        Assert.assertEquals("example", result);
    }

    @Test
    public void testGetIgnoreCaseAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("Class", "example");
        String result = attributes.getIgnoreCase("class");
        Assert.assertEquals("example", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetNonExistentAttribute() {
        Attributes attributes = new Attributes();
        String result = attributes.get("nonExistent");
    }

    @Test
    public void testPutAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("class", "example");
        attributes.put("class", "newExample");
        String result = attributes.get("class");
        Assert.assertEquals("newExample", result);
    }

    @Test
    public void testPutIgnoreCaseAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("Class", "example");
        attributes.putIgnoreCase("class", "newExample");
        String result = attributes.getIgnoreCase("class");
        Assert.assertEquals("newExample", result);
    }

    @Test
    public void testPutBooleanAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("data-checked", true);
        boolean result = attributes.getBoolean("data-checked");
        Assert.assertTrue(result);
    }

    @Test
    public void testPutBooleanAttributeRemove() {
        Attributes attributes = new Attributes();
        attributes.put("data-checked", true);
        attributes.put("data-checked", false);
        boolean result = attributes.getBoolean("data-checked");
        Assert.assertFalse(result);
    }

    @Test
    public void testPutAttributeWithValue() {
        Attributes attributes = new Attributes();
        attributes.put("class", "example");
        attributes.put("class", "newExample value");
        String result = attributes.get("class");
        Assert.assertEquals("newExample value", result);
    }

    @Test
    public void testPutIgnoreCaseAttributeWithValue() {
        Attributes attributes = new Attributes();
        attributes.put("Class", "example value");
        attributes.putIgnoreCase("class", "newExample value");
        String result = attributes.getIgnoreCase("class");
        Assert.assertEquals("newExample value", result);
    }

    @Test
    public void testPutDuplicateAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("class", "example");
        attributes.put("class", "newExample");
        int size = attributes.size();
        Assert.assertEquals(size, 2);
    }

    @Test
    public void testPutDuplicateAttributeIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Class", "example");
        attributes.putIgnoreCase("class", "newExample");
        int size = attributes.size();
        Assert.assertEquals(size, 2);
    }

    @Test
    public void testRemoveAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("class", "example");
        attributes.remove("class");
        String result = attributes.get("class");
        Assert.assertEquals(EmptyString, result);
    }

    @Test
    public void testRemoveIgnoreCaseAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("Class", "example");
        attributes.removeIgnoreCase("class");
        String result = attributes.getIgnoreCase("class");
        Assert.assertEquals(EmptyString, result);
    }

    @Test
    public void testHasKeyAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("class", "example");
        boolean result = attributes.hasKey("class");
        Assert.assertTrue(result);
    }

    @Test
    public void testHasKeyIgnoreCaseAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("Class", "example");
        boolean result = attributes.hasKeyIgnoreCase("class");
        Assert.assertTrue(result);
    }

    @Test
    public void testHasKeyNonExistentAttribute() {
        Attributes attributes = new Attributes();
        boolean result = attributes.hasKey("nonExistent");
        Assert.assertFalse(result);
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        attributes.put("class", "example");
        int result = attributes.size();
        Assert.assertEquals(1, result);
    }

    @Test
    public void testAddAllAttributes() {
        Attributes attributes1 = new Attributes();
        attributes1.put("class1", "example1");

        Attributes attributes2 = new Attributes();
        attributes2.put("class2", "example2");

        Attributes result = new Attributes();
        result.addAll(attributes1);
        result.addAll(attributes2);

        int size = result.size();
        Assert.assertEquals(2, size);
    }

    @Test
    public void testGetHtml() {
        Attributes attributes = new Attributes();
        attributes.put("class", "example");
        String result = attributes.html();
        Assert.assertEquals(" class=\"example\"", result);
    }

    @Test
    public void testGetHtmlWithEmptyAttributes() {
        Attributes attributes = new Attributes();
        String result = attributes.html();
        Assert.assertEquals("", result);
    }
}