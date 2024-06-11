package org.jsoup.nodes.test;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.PutAttribute;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class AttributeTest {

    @Test
    public void testAttributeCreation() {
        Attribute attr = new Attribute("key", "value");
        Assert.assertEquals("key", attr.getKey());
        Assert.assertEquals("value", attr.getValue());

        Attribute attrWithParent = new Attribute("key", "value", new Attributes());
        Assert.assertEquals("key", attrWithParent.getKey());
        Assert.assertEquals("value", attrWithParent.getValue());
        Assert.assertNull(attrWithParent.parent);
    }

    @Test
    public void testAttributeSetters() {
        Attribute attr = new Attribute("key", "value");

        attr.setKey("newKey");
        Assert.assertEquals("newKey", attr.getKey());

        attr.setValue("newValue");
        Assert.assertEquals("newValue", attr.getValue());

        attr.setKey("newKey2");
        Assert.assertEquals("newKey2", attr.getKey());
        Assert.assertEquals("newValue", attr.getValue());
    }

    @Test
    public void testAttributeHtml() throws IOException {
        Attribute attr = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        Document doc = new Document("");

        attr.html(sb, doc.outputSettings());
        String expectedHtml = "key=\"value\"";
        Assert.assertEquals(expectedHtml, sb.toString());

        String expectedToString = "Attribute{key='key', value='value'}";
        Assert.assertEquals(expectedToString, attr.toString());
    }

    @Test(expected = SerializationException.class)
    public void testAttributeHtmlWithIOException() throws IOException {
        Attribute attr = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        Document doc = new Document("") {
            @Override
            public void outputSettings(Document.OutputSettings out) throws IOException {
                throw new IOException("test exception");
            }
        };

        attr.html(sb, doc.outputSettings());
    }

    @Test
    public void testAttributeCreationFromEncoded() {
        Attribute attr = Attribute.createFromEncoded("key", "key=\"value\"");
        Assert.assertEquals("key", attr.getKey());
        Assert.assertEquals("value", attr.getValue());
    }

    @Test
    public void testBooleanAttribute() {
        Attribute attr = new Attribute("checked", null);
        Assert.assertTrue(Attribute.isBooleanAttribute(attr.getKey()));

        attr = new Attribute("notBoolean", null);
        Assert.assertFalse(Attribute.isBooleanAttribute(attr.getKey()));
    }

    @Test
    public void testShouldCollapseAttribute() {
        Document.OutputSettings out = new Document.OutputSettings();
        out.syntax(Document.OutputSettings.Syntax.html);

        Attribute attr = new Attribute("checked", "checked");
        Assert.assertTrue(attr.shouldCollapseAttribute(out));
        Assert.assertEquals("checked", attr.html(new StringBuilder(), out).toString());

        attr = new Attribute("checked", "otherValue");
        Assert.assertFalse(attr.shouldCollapseAttribute(out));
        Assert.assertNotNull(attr.html(new StringBuilder(), out).toString());

        attr = new Attribute("notBoolean", null);
        Assert.assertFalse(attr.shouldCollapseAttribute(out));
        Assert.assertNotNull(attr.html(new StringBuilder(), out).toString());

        attr = new Attribute("notBoolean", "otherValue");
        Assert.assertFalse(attr.shouldCollapseAttribute(out));
        Assert.assertNotNull(attr.html(new StringBuilder(), out).toString());
    }

    @Test
    public void testPutAttribute() {
        Element elem = new Element("div");
        PutAttribute putAttr = elem.putAttribute("key", "value");
        Attribute attr = (Attribute) putAttr;

        Assert.assertEquals("key", attr.getKey());
        Assert.assertEquals("value", attr.getValue());
        Assert.assertEquals("div", attr.parent.getClass().getName());
    }
}