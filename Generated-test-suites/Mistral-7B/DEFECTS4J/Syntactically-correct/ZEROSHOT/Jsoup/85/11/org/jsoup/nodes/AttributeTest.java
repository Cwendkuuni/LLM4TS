package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RunWith(Parameterized.class)
public class AttributeTest {

    private final String key;
    private final String value;
    private final boolean expectedBoolean;

    public AttributeTest(String key, String value, boolean expectedBoolean) {
        this.key = key;
        this.value = value;
        this.expectedBoolean = expectedBoolean;
    }

    @Parameterized.Parameters(name = "Test for Attribute with key: {0} and value: {1}")
    public static List<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"key1", "value1", false},
                {"booleanAttribute", null, true},
                {"booleanAttribute", "", true},
                {"checked", "checked", true},
                {"disabled", null, true},
                {"disabled", "", true},
                {"disabled", "disabled", true},
                {"data-test", "data-test", false},
                {"data-test", "data-test-value", false},
                {"data-test", "true", false},
                {"data-test", "true", true},
                {"data-test", "false", false},
                {"data-test", "false", true},
                {"data-test", "value", false}
        });
    }

    @Test
    public void testCreation() {
        Attribute attribute = new Attribute(key, value);
        assertEquals(key, attribute.getKey());
        assertEquals(value, attribute.getValue());
        assertEquals(expectedBoolean, attribute.isBooleanAttribute());
    }

    @Test
    public void testCreationWithParent() {
        Attributes parent = new Attributes();
        Attribute attribute = new Attribute(key, value, parent);
        assertEquals(key, attribute.getKey());
        assertEquals(value, attribute.getValue());
        assertEquals(expectedBoolean, attribute.isBooleanAttribute());
        assertEquals(attribute, parent.get(key));
    }

    @Test(expected = NullPointerException.class)
    public void testNullKey() {
        new Attribute(null, value);
    }

    @Test(expected = NullPointerException.class)
    public void testNullValue() {
        new Attribute(key, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyKey() {
        new Attribute("", value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyValue() {
        new Attribute(key, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyKeyAndValue() {
        new Attribute("", "");
    }

    @Test
    public void testBooleanAttributes() {
        assertTrue(Arrays.binarySearch(Attribute.booleanAttributes, "checked") >= 0);
        assertTrue(Arrays.binarySearch(Attribute.booleanAttributes, "disabled") >= 0);
        assertTrue(Arrays.binarySearch(Attribute.booleanAttributes, "booleanAttribute") >= 0);
        assertFalse(Arrays.binarySearch(Attribute.booleanAttributes, "notBooleanAttribute") >= 0);
    }

    @Test
    public void testShouldCollapseAttribute() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        assertTrue(Attribute.shouldCollapseAttribute(outputSettings));
        assertTrue(Attribute.shouldCollapseAttribute(key, value, outputSettings));
        assertTrue(Attribute.shouldCollapseAttribute(key, expectedBoolean ? "" : value, outputSettings));
        assertFalse(Attribute.shouldCollapseAttribute(key, value, null));
        assertFalse(Attribute.shouldCollapseAttribute(key, value, new Document.OutputSettings(Document.OutputSettings.Syntax.xml)));
    }

    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute(key, value);
        Attribute attribute2 = new Attribute(key, value);
        Attribute attribute3 = new Attribute("differentKey", value);
        Attribute attribute4 = new Attribute(key, "differentValue");

        assertEquals(attribute1, attribute1);
        assertEquals(attribute1, attribute2);
        assertNotEquals(attribute1, attribute3);
        assertNotEquals(attribute1, attribute4);
    }

    @Test
    public void testHashCode() {
        Attribute attribute1 = new Attribute(key, value);
        Attribute attribute2 = new Attribute(key, value);
        Attribute attribute3 = new Attribute("differentKey", value);
        Attribute attribute4 = new Attribute(key, "differentValue");

        int hashCode1 = attribute1.hashCode();
        int hashCode2 = attribute2.hashCode();
        int hashCode3 = attribute3.hashCode();
        int hashCode4 = attribute4.hashCode();

        assertEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
        assertNotEquals(hashCode1, hashCode4);
    }

    @Test
    public void testClone() {
        Attribute attribute = new Attribute(key, value);
        Attribute clonedAttribute = attribute.clone();

        assertEquals(attribute.getClass(), clonedAttribute.getClass());
        assertEquals(attribute.getKey(), clonedAttribute.getKey());
        assertEquals(attribute.getValue(), clonedAttribute.getValue());
        assertEquals(attribute.isBooleanAttribute(), clonedAttribute.isBooleanAttribute());
    }
}