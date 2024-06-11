package org.jsoup.nodes.test;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.PutAttribute;

import java.util.Arrays;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.jsoup.nodes.Attributes.not;

public class AttributeTest {

    private Element elem;

    @Before
    public void setUp() {
        elem = new Element("html");
    }

    @Test
    public void testNewAttribute() {
        Attribute attr = new Attribute("key", "value");
        assertThat(attr.getKey(), is("key"));
        assertThat(attr.getValue(), is("value"));

        Attribute booleanAttr = new Attribute("checked", null);
        boolean isBooleanAttribute = attr.isBooleanAttribute();
        assertThat(isBooleanAttribute, is(false));
    }

    @Test
    public void testNewAttributeWithParent() {
        Attributes attrs = new Attributes();
        attrs.put("key", "value");
        Attribute attr = new Attribute("key", "newValue", attrs);
        assertThat(attr.getKey(), is("key"));
        attrs.put("key", "newValue2");
        assertThat(attr.getValue(), is("newValue2"));
    }

    @Test
    public void testSetKey() {
        Attribute attr = new Attribute("key", "value");
        attr.setKey("newKey");
        assertThat(attr.getKey(), is("newKey"));
    }

    @Test
    public void testSetValue() {
        Attribute attr = new Attribute("key", "value");
        String oldValue = attr.setValue("newValue");
        assertThat(attr.getValue(), is("newValue"));
        assertThat(oldValue, is("value"));
    }

    @Test
    public void testHtml() throws Exception {
        Attribute attr = new Attribute("key", "value");
        String html = attr.html();
        String expectedHtml = "key=\"value\"";
        assertThat(html, is(expectedHtml));
    }

    @Test
    public void testCollapsibleBooleanAttributes() throws Exception {
        Attributes attrs = new Attributes();
        attrs.put(new Attribute("checked", ""));
        attrs.put(new Attribute("disabled", ""));

        Element elem = new Element("input");
        elem.attributes().putAll(attrs);

        String html = elem.html();
        String expectedHtml = "<input checked disabled>";
        assertThat(html, is(expectedHtml));
    }

    @Test
    public void testToString() {
        Attribute attr = new Attribute("key", "value");
        String toString = attr.toString();
        String expectedToString = "Attribute{key='key', value='value'}";
        assertThat(toString, is(expectedToString));
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute attr = Attribute.createFromEncoded("key", "key=\"value\"");
        assertThat(attr.getKey(), is("key"));
        assertThat(attr.getValue(), is("value"));
    }

    @Test
    public void testIsBooleanAttribute() {
        Attribute attr = new Attribute("checked", null);
        boolean isBooleanAttribute = attr.isBooleanAttribute();
        assertThat(isBooleanAttribute, is(true));
    }

    @Test
    public void testShouldCollapseAttribute() {
        Document doc = new Document();
        Attribute attr = new Attribute("key", "value");

        String html = attr.html(doc.outputSettings());
        boolean shouldCollapse = attr.shouldCollapseAttribute(doc.outputSettings());

        String expectedHtml = "key=\"value\"";
        assertThat(html, is(expectedHtml));
        assertThat(shouldCollapse, is(false));
    }

    @Test
    public void testShouldCollapseAttributeForBooleanAttribute() {
        Document doc = new Document();
        Attribute attr = new Attribute("checked", null);

        String html = attr.html(doc.outputSettings());
        boolean shouldCollapsible = attr.shouldCollapseAttribute(doc.outputSettings());

        String expectedHtml = "checked";
        assertThat(html, is(expectedHtml));
        assertThat(shouldCollapsible, is(true));
    }

    @Test
    public void testShouldCollapseAttributeForEmptyBooleanAttribute() {
        Document doc = new Document();
        Attribute attr = new Attribute("checked", "");

        String html = attr.html(doc.outputSettings());
        boolean shouldCollapsible = attr.shouldCollapseAttribute(doc.outputSettings());

        String expectedHtml = "";
        assertThat(html, is(expectedHtml));
        assertThat(shouldCollapsible, is(true));
    }

    @Test
    public void testPutAttributeWithExistingKey() {
        elem.putAttr("key", "value1");
        Attribute attr = new Attribute("key", "value2");
        elem.put(attr);

        String html = elem.html();
        String expectedHtml = "<html key=\"value2\"></html>";
        assertThat(html, is(expectedHtml));
    }

    @Test
    public void testPutAttributeWithNullValue() {
        elem.putAttr("key", "value1");
        Attribute attr = new Attribute("key", null);
        elem.put(attr);

        String html = elem.html();
        String expectedHtml = "<html key=\"\"></html>";
        assertThat(html, is(expectedHtml));
    }

    @Test
    public void testPutAttributeWithEmptyValue() {
        elem.putAttr("key", "value1");
        Attribute attr = new Attribute("key", "");
        elem.put(attr);

        String html = elem.html();
        String expectedHtml = "<html key=\"\"></html>";
        assertThat(html, is(expectedHtml));
    }

    @Test
    public void testPutAttributeWithEmptyKey() {
        elem.putAttr("key", "value1");
        Attribute attr = new Attribute("", "value2");
        elem.put(attr);

        String html = elem.html();
        assertThat(html, is(nullValue()));
    }

    @Test
    public void testPutAttributeWithInvalidKey() {
        elem.putAttr("key", "value1");
        Attribute attr = new Attribute("&&^%$", "value2");

        PutAttribute putAttribute = null;
        try {
            putAttribute = (PutAttribute) elem.put(attr);
        } catch (IllegalArgumentException e) {
            // expected
        }

        assertThat(putAttribute, is(nullValue()));
    }
}