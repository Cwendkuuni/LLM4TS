import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;

public class AttributeTest {

    @Test
    public void testAttributeCreation() {
        Attribute attribute = new Attribute("key", "value");
        Assert.assertEquals("key", attribute.getKey());
        Assert.assertEquals("value", attribute.getValue());

        Attribute attributeWithParent = new Attribute("key", "value", new Attributes());
        Assert.assertEquals("key", attributeWithParent.getKey());
        Assert.assertEquals("value", attributeWithParent.getValue());
        Assert.assertNull(attributeWithParent.parent);
    }

    @Test
    public void testAttributeKeySetter() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("newKey");
        Assert.assertEquals("newKey", attribute.getKey());

        Attribute attributeWithParent = new Attribute("key", "value", new Attributes());
        attributeWithParent.setKey("newKey");
        Assert.assertEquals("newKey", attributeWithParent.getKey());
        Assert.assertEquals("value", attributeWithParent.getValue());
        Assert.assertNotNull(attributeWithParent.parent);
    }

    @Test
    public void testAttributeValueSetter() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setValue("newValue");
        Assert.assertEquals("newValue", attribute.getValue());

        Attribute attributeWithParent = new Attribute("key", "value", new Attributes());
        attributeWithParent.setValue("newValue");
        Assert.assertEquals("newValue", attributeWithParent.getValue());
        Assert.assertNotNull(attributeWithParent.parent);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAttributeKeySetterNull() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAttributeKeySetterEmpty() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAttributeValueSetterNull() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setValue(null);
    }

    @Test
    public void testHtmlMethod() throws IOException {
        Attribute attribute = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        attribute.html(sb);
        String expectedHtml = "key=\"value\"";
        Assert.assertEquals(expectedHtml, StringUtil.releaseBuilder(sb));
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded("key", "key=\"value\"");
        Assert.assertEquals("key", attribute.getKey());
        Assert.assertEquals("value", attribute.getValue());
    }

    @Test
    public void testIsDataAttribute() {
        Attribute attribute = new Attribute("key", "value");
        Assert.assertFalse(attribute.isDataAttribute());

        Attribute dataAttribute = new Attribute("data-key", "value");
        Assert.assertTrue(dataAttribute.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() throws IOException {
        Document.OutputSettings out = new Document.OutputSettings();
        Attribute attribute = new Attribute("key", "value");
        Assert.assertFalse(attribute.shouldCollapseAttribute(out));

        Attribute booleanAttribute = new Attribute("checked", null);
        Assert.assertTrue(booleanAttribute.shouldCollapseAttribute(out));

        Attribute emptyBooleanAttribute = new Attribute("checked", "");
        Assert.assertTrue(booleanAttribute.shouldCollapseAttribute(out));

        Attribute booleanAttributeWithValue = new Attribute("checked", "checked");
        Assert.assertTrue(booleanAttributeWithValue.shouldCollapseAttribute(out));
    }
}