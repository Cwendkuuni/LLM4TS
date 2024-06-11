package org.jsoup.nodes.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;

import java.util.HashMap;
import java.util.Map;

public class AttributeTest {

    @Test
    public void testAttributeCreation() {
        String key = "testKey";
        String value = "testValue";
        Attribute attribute = new Attribute(key, value);

        Assert.assertEquals(key, attribute.getKey());
        Assert.assertEquals(value, attribute.getValue());

        Attribute encodedAttribute = Attribute.createFromEncoded("testKey", "testValue%20%61%6D%65%20testValue");
        Assert.assertEquals(key, encodedAttribute.getKey());
        Assert.assertEquals(value, encodedAttribute.getValue());
    }

    @Test
    public void testAttributeSetting() {
        Attribute attribute = new Attribute("testKey", "testValue");
        String newKey = "newTestKey";
        String newValue = "newTestValue";

        attribute.setKey(newKey);
        Assert.assertEquals(newKey, attribute.getKey());

        attribute.setValue(newValue);
        Assert.assertEquals(newValue, attribute.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAttributeNullKey() {
        new Attribute(null, "testValue");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAttributeEmptyKey() {
        new Attribute("", "testValue");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAttributeNullValue() {
        new Attribute("testKey", null);
    }

    @Test
    public void testAttributeEquals() {
        Attribute attribute1 = new Attribute("testKey1", "testValue1");
        Attribute attribute2 = new Attribute("testKey1", "testValue1");
        Attribute attribute3 = new Attribute("testKey2", "testValue2");

        Assert.assertTrue(attribute1.equals(attribute1));
        Assert.assertTrue(attribute1.equals(attribute2));
        Assert.assertFalse(attribute1.equals(attribute3));
    }

    @Test
    public void testAttributeHtml() throws IOException {
        String key = "testKey";
        String value = "testValue";

        Map<String, String> attributes = new HashMap<>();
        attributes.put(key, value);

        Document document = new Document();
        Attributes docAttributes = document.attributes();
        docAttributes.put(new Attribute(key, value));

        StringBuilder testOutput = StringUtil.borrowBuilder();
        document.outputSettings().syntax(Document.OutputSettings.Syntax.html);
        document.outputSettings().prettyPrint(false);
        document.outputSettings().charset("UTF-8");
        document.outputSettings().indentAmount(0);

        document.select("body").html(testOutput);

        Assert.assertEquals("<body" + System.lineSeparator() +
                "           " + key + "=\"" + value + "\">" + System.lineSeparator() +
                "</body>", testOutput.toString());
    }

    @Test
    public void testShouldCollapseAttribute() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        boolean shouldCollapse = Attribute.shouldCollapseAttribute("testKey1", null, outputSettings);
        Assert.assertFalse(shouldCollapse);

        shouldCollapse = Attribute.shouldCollapseAttribute("testKey2", "testValue2", outputSettings);
        Assert.assertFalse(shouldCollapse);

        shouldCollapse = Attribute.shouldCollapseAttribute("testKey3", "testValue3", outputSettings);
        Assert.assertTrue(shouldCollapse);

        shouldCollapse = Attribute.shouldCollapseAttribute("testKey4", "", outputSettings);
        Assert.assertTrue(shouldCollapse);

        shouldCollapse = Attribute.shouldCollapseAttribute("testKey5", "testKey5", outputSettings);
        Assert.assertTrue(shouldCollapse);
    }

    @Test
    public void testIsBooleanAttribute() {
        String booleanAttribute = "checked";
        String nonBooleanAttribute = "testAttribute";

        Assert.assertTrue(Attribute.isBooleanAttribute(booleanAttribute));
        Assert.assertFalse(Attribute.isBooleanAttribute(nonBooleanAttribute));
    }
}