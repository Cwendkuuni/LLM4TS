package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AttributesTest {

    @Test
    public void testGetAttribute() {
        Attributes attributes = new Attributes()
                .put("id", "testId")
                .put("class", "testClass")
                .put("data-customData", "testData");

        Assert.assertEquals("testId", attributes.get("id"));
        Assert.assertEquals("testClass", attributes.get("class"));
        Assert.assertEquals("testData", attributes.get("data-customData"));
        Assert.assertEquals(Attributes.EMPTY_STRING, attributes.get("nonExistentAttribute"));

        Attributes lowerCaseAttributes = new Attributes()
                .put("ID", "testId")
                .put("CLASS", "testClass");

        Assert.assertEquals("testId", lowerCaseAttributes.get("id"));
        Assert.assertEquals("testClass", lowerCaseAttributes.get("class"));
        Assert.assertEquals(Attributes.EMPTY_STRING, lowerCaseAttributes.get("id2"));
    }

    @Test
    public void testGetAttributeIgnoreCase() {
        Attributes attributes = new Attributes()
                .put("id", "testId")
                .put("class", "testClass")
                .put("data-customData", "testData");

        Assert.assertEquals("testId", attributes.get("id"));
        Assert.assertEquals("testClass", attributes.get("class"));
        Assert.assertEquals("testData", attributes.getIgnoreCase("data-customData"));
        Assert.assertEquals(Attributes.EMPTY_STRING, attributes.getIgnoreCase("NonExistentAttribute"));

        Attributes lowerCaseAttributes = new Attributes()
                .put("ID", "testId")
                .put("CLASS", "testClass");

        Assert.assertEquals("testId", lowerCaseAttributes.get("id"));
        Assert.assertEquals("testClass", lowerCaseAttributes.get("class"));
        Assert.assertEquals(Attributes.EMPTY_STRING, lowerCaseAttributes.getIgnoreCase("Id"));
    }

    @Test
    public void testPutAttribute() {
        Attributes attributes = new Attributes();

        Attributes newAttributes = attributes.put("id", "testId")
                .put("class", "testClass")
                .put("data-customData", "testData");

        Assert.assertEquals(3, newAttributes.size());
        Assert.assertEquals("testId", newAttributes.get("id"));
        Assert.assertEquals("testClass", newAttributes.get("class"));
        Assert.assertEquals("testData", newAttributes.get("data-customData"));
        Assert.assertEquals(Attributes.EMPTY_STRING, newAttributes.get("nonExistentAttribute"));

        Attributes lowerCaseNewAttributes = newAttributes.put("ID", "testId2")
                .put("CLASS", "testClass2");

        Assert.assertEquals(5, lowerCaseNewAttributes.size());
        Assert.assertEquals("testId", lowerCaseNewAttributes.get("id"));
        Assert.assertEquals("testClass2", lowerCaseNewAttributes.get("class"));
        Assert.assertEquals("testData", lowerCaseNewAttributes.get("data-customData"));
        Assert.assertEquals(Attributes.EMPTY_STRING, lowerCaseNewAttributes.get("id2"));
    }

    @Test
    public void testPutAttributeWithSameKey() {
        Attributes attributes = new Attributes()
                .put("id", "testId")
                .put("id", "newTestId");

        Assert.assertEquals(1, attributes.size());
        Assert.assertEquals("newTestId", attributes.get("id"));
    }

    @Test
    public void testPutBooleanAttribute() {
        Attributes attributes = new Attributes();

        Attributes newAttributes = attributes.put("data-isChecked", true)
                .put("data-isChecked", false);

        Assert.assertEquals(1, newAttributes.size());
        Assert.assertNull(newAttributes.get("data-isChecked"));
    }

    @Test
    public void testPutAttributeWithSameKeyIgnoreCase() {
        Attributes attributes = new Attributes()
                .put("Data-isChecked", true)
                .put("Data-isChecked", false);

        Assert.assertEquals(1, attributes.size());
        Assert.assertNull(attributes.get("Data-isChecked"));
    }

    @Test
    public void testRemoveAttribute() {
        Attributes attributes = new Attributes()
                .put("id", "testId")
                .put("class", "testClass")
                .put("data-customData", "testData");

        Attributes newAttributes = attributes.remove("id");

        Assert.assertEquals(2, newAttributes.size());
        Assert.assertEquals("testClass", newAttributes.get("class"));
        Assert.assertEquals(Attributes.EMPTY_STRING, newAttributes.get("id"));
        Assert.assertEquals("testData", newAttributes.get("data-customData"));
    }

    @Test
    public void testRemoveAttributeIgnoreCase() {
        Attributes attributes = new Attributes()
                .put("ID", "testId")
                .put("CLASS", "testClass")
                .put("data-customData", "testData");

        Attributes newAttributes = attributes.removeIgnoreCase("id");

        Assert.assertEquals(2, newAttributes.size());
        Assert.assertEquals("testClass", newAttributes.get("CLASS"));
        Assert.assertEquals(Attributes.EMPTY_STRING, newAttributes.get("ID"));
        Assert.assertEquals("testData", newAttributes.get("data-customData"));
    }

    @Test
    public void testRemoveNonExistentAttribute() {
        Attributes attributes = new Attributes()
                .put("id", "testId")
                .put("class", "testClass")
                .put("data-customData", "testData");

        Attributes newAttributes = attributes.remove("nonExistentAttribute");

        Assert.assertEquals(3, newAttributes.size());
        Assert.assertEquals("testId", newAttributes.get("id"));
        Assert.assertEquals("testClass", newAttributes.get("class"));
        Assert.assertEquals("testData", newAttributes.get("data-customData"));
    }

    @Test
    public void testHasAttribute() {
        Attributes attributes = new Attributes()
                .put("id", "testId")
                .put("class", "testClass")
                .put("data-customData", "testData");

        Assert.assertTrue(attributes.hasKey("id"));
        Assert.assertTrue(attributes.hasKey("class"));
        Assert.assertTrue(attributes.hasKey("data-customData"));
        Assert.assertFalse(attributes.hasKey("nonExistentAttribute"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("ID"));
    }

    @Test
    public void testHasAttributeIgnoreCase() {
        Attributes attributes = new Attributes()
                .put("ID", "testId")
                .put("CLASS", "testClass")
                .put("data-customData", "testData");

        Assert.assertTrue(attributes.hasKeyIgnoreCase("id"));
        Assert.assertTrue(attributes.hasKeyIgnoreCase("CLASS"));
        Assert.assertTrue(attributes.hasKeyIgnoreCase("data-customData"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("ID2"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("nonExistentAttribute"));
    }

    @Test
    public void testRemoveAllAttributes() {
        Attributes attributes = new Attributes()
                .put("id", "testId")
                .put("class", "testClass")
                .put("data-customData", "testData");

        Attributes emptyAttributes = attributes.clear();

        Assert.assertEquals(0, emptyAttributes.size());
        Assert.assertNull(emptyAttributes.get("id"));
        Assert.assertNull(emptyAttributes.get("class"));
        Assert.assertNull(emptyAttributes.get("data-customData"));
    }

    @Test
    public void testAddAllAttributes() {
        Attributes baseAttributes = new Attributes()
                .put("id", "testId")
                .put("class", "testClass");

        Attributes newAttributes = new Attributes()
                .put("data-customData", "testData")
                .put("data-customData2", "testData2");

        Attributes resultAttributes = baseAttributes.addAll(newAttributes);

        Assert.assertEquals(4, resultAttributes.size());
        Assert.assertEquals("testId", resultAttributes.get("id"));
        Assert.assertEquals("testClass", resultAttributes.get("class"));
        Assert.assertEquals("testData", resultAttributes.get("data-customData"));
        Assert.assertEquals("testData2", resultAttributes.get("data-customData2"));
    }

    @Test
    public void testClone() {
        Attributes attributes = new Attributes()
                .put("id", "testId")
                .put("class", "testClass")
                .put("data-customData", "testData");

        Attributes clonedAttributes = attributes.clone();

        Assert.assertEquals(3, clonedAttributes.size());
        Assert.assertEquals("testId", clonedAttributes.get("id"));
        Assert.assertEquals("testClass", clonedAttributes.get("class"));
        Assert.assertEquals("testData", clonedAttributes.get("data-customData"));

        Assert.assertNotSame(attributes, clonedAttributes);
    }

    @Test
    public void testNormalize() {
        Attributes attributes = new Attributes()
                .put("ID", "TestId")
                .put("CLASSES", "TestClass, testClass2")
                .put("DATA-CUSTOM-DATA", "TestData")
                .put("data-custom-data2", "TestData2");

        attributes.normalize();

        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("id", "testid");
        expectedMap.put("class", "testclass, testclass2");
        expectedMap.put("data-custom-data", "testdata");
        expectedMap.put("data-custom-data2", "testdat2");

        Assert.assertEquals(expectedMap, attributes.asMap());
    }

    @Test
    public void testGetDataset() {
        Attributes attributes = new Attributes()
                .put("data-id", "testId")
                .put("data-name", "testName")
                .put("data-isActive", "true");

        Map<String, String> dataset = attributes.dataset();

        Assert.assertEquals(3, dataset.size());
        Assert.assertEquals("testId", dataset.get("id"));
        Assert.assertEquals("testName", dataset.get("name"));
        Assert.assertEquals("true", dataset.get("isActive"));
    }

    @Test
    public void testToString() {
        Attributes attributes = new Attributes()
                .put("id", "testId")
                .put("class", "testClass")
                .put("data-customData", "testData");

        Assert.assertEquals("id=\"testId\" class=\"testClass\" data-customData=\"testData\"", attributes.toString());
    }
}