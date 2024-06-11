package org.jsoup.nodes.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Document.OutputSettings;
import org.jsoup.nodes.Element;
import org.junit.Before;
import org.junit.Test;

/**
 * Testing the Attribute class
 */
public class AttributeTest {

    private Attribute att;
    private Attributes attrs;
    private Element elem;
    private Document doc;

    @Before
    public void setUp() {
        doc = new Document();
        elem = doc.createElement("html");
        attrs = elem.attributes();
        att = new Attribute("test", "testValue");
    }

    @Test
    public void testAttributeCreation() {
        Attribute attribute = new Attribute("test", "testValue");
        assertTrue(attribute instanceof Attribute);
        assertEquals("test", attribute.getKey());
        assertEquals("testValue", attribute.getValue());
        assertNull(attribute.getParent());
    }

    @Test
    public void testAttributeCreationWithParent() {
        Attribute attribute = new Attribute("test", "testValue", attrs);
        assertTrue(attribute instanceof Attribute);
        assertEquals("test", attribute.getKey());
        assertEquals("testValue", attribute.getValue());
        assertEquals(attrs, attribute.getParent());
    }

    @Test
    public void testSettingAndGettingKey() {
        att.setKey("newKey");
        assertEquals("newKey", att.getKey());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSettingNullKey() {
        att.setKey(null);
    }

    @Test
    public void testSettingAndGettingValue() {
        att.setValue("newValue");
        assertEquals("newValue", att.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSettingNullValue() {
        att.setValue(null);
    }

    @Test
    public void testSettingNewValueWithSameKey() {
        att.setValue("newValue");
        att.setValue("newerValue");
        assertEquals("newerValue", att.getValue());
    }

    @Test
    public void testSettingNewValueWithDifferentKey() {
        Attribute newAtt = new Attribute("newKey", "newValue");
        attrs.put(newAtt);
        assertEquals("newValue", attrs.get("newKey").getValue());
    }

    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("test", "testValue");
        Attribute attribute2 = new Attribute("test", "testValue");
        Attribute attribute3 = new Attribute("test2", "testValue");
        Attribute attribute4 = new Attribute("test", null);
        Attribute attribute5 = new Attribute("test", "testValue");
        Attribute attribute6 = attribute1;

        assertTrue(attribute1.equals(attribute1));
        assertTrue(attribute1.equals(attribute5));
        assertTrue(attribute1.equals(attribute6));
        assertFalse(attribute1.equals(attribute2));
        assertFalse(attribute1.equals(attribute3));
        assertFalse(attribute1.equals(attribute4));
    }

    @Test
    public void testHashCode() {
        Attribute attribute1 = new Attribute("test", "testValue");
        Attribute attribute2 = new Attribute("test", "testValue");
        Attribute attribute3 = new Attribute("test2", "testValue");
        Attribute attribute4 = new Attribute("test", null);
        Attribute attribute5 = new Attribute("test", "testValue");

        int hashCode1 = attribute1.hashCode();
        int hashCode2 = attribute2.hashCode();
        int hashCode3 = attribute3.hashCode();
        int hashCode4 = attribute4.hashCode();
        int hashCode5 = attribute5.hashCode();

        assertTrue(hashCode1 == hashCode5);
        assertFalse(hashCode1 == hashCode2);
        assertFalse(hashCode1 == hashCode3);
        assertFalse(hashCode1 == hashCode4);
    }

    @Test
    public void testClone() {
        Attribute attribute = new Attribute("test", "testValue");
        Attribute clonedAttribute = attribute.clone();

        assertTrue(attribute.equals(clonedAttribute));
        assertFalse(attribute == clonedAttribute);
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded("test", "testValue%3D%3D");

        assertEquals("test", attribute.getKey());
        assertEquals("testValue", attribute.getValue());
    }

    @Test
    public void testIsDataAttribute() {
        assertTrue(att.isDataAttribute());
        assertFalse(new Attribute("test", null).isDataAttribute());
    }

    @Test
    public void testIsBooleanAttribute() {
        assertTrue(att.isBooleanAttribute());
        assertFalse(new Attribute("test2", null).isBooleanAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        OutputSettings out = new OutputSettings();

        assertTrue(att.shouldCollapseAttribute(out));
        assertFalse(new Attribute("test2", null).shouldCollapseAttribute(out));
    }

    @Test
    public void testHtml() throws IOException {
        StringBuilder sb = StringUtil.borrowBuilder();
        att.html(sb, out);
        String html = StringUtil.releaseBuilder(sb);

        assertEquals(" test=\"testValue\"", html);
    }

    @Test
    public void testToString() {
        Attribute attribute = new Attribute("test", "testValue");

        assertEquals(" test=\"testValue\"", attribute.toString());
    }
}