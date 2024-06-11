import org.junit.Assert;
import org.junit.Test;
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
    }

    @Test
    public void testSettingKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("newKey");
        Assert.assertEquals("newKey", attribute.getKey());
    }

    @Test(expected = AssertionError.class)
    public void testSettingNullKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey(null);
    }

    @Test
    public void testSettingEmptyKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("");
        Assert.assertEquals("", attribute.getKey());
    }

    @Test
    public void testSettingValue() {
        Attribute attribute = new Attribute("key", "value");
        String newValue = "newValue";
        String oldValue = attribute.setValue(newValue);
        Assert.assertEquals("newValue", attribute.getValue());
        Assert.assertEquals("value", oldValue);
    }

    @Test(expected = AssertionError.class)
    public void testSettingNullValue() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setValue(null);
    }

    @Test
    public void testHtmlMethod() throws IOException {
        Attribute attribute = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        attribute.html(sb, new Document("").outputSettings());
        String expectedHtml = "key=\"value\"";
        Assert.assertEquals(expectedHtml, StringUtil.releaseBuilder(sb));
    }

    @Test(expected = SerializationException.class)
    public void testIoExceptionThrownInHtmlMethod() throws IOException {
        Attribute attribute = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        attribute.html(sb, new Document("").outputSettings()); // IOException is expected to be thrown here
    }

    @Test
    public void testToStringMethod() {
        Attribute attribute = new Attribute("key", "value");
        String expectedToString = "key=\"value\"";
        Assert.assertEquals(expectedToString, attribute.toString());
    }

    @Test
    public void testCreateFromEncoded() {
        String encodedKey = "key='value'";
        Attribute attribute = Attribute.createFromEncoded("key", encodedKey);
        Assert.assertEquals("key", attribute.getKey());
        Assert.assertEquals("value", attribute.getValue());
    }

    @Test
    public void testIsDataAttributeMethod() {
        Attribute attribute = new Attribute("key", "value");
        Assert.assertFalse(attribute.isDataAttribute());

        Attribute dataAttribute = new Attribute("data-key", "data-value");
        Assert.assertTrue(dataAttribute.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttributeMethod() throws IOException {
        Document.OutputSettings settings = new Document.OutputSettings();
        Attribute attribute = new Attribute("key", "value");
        Assert.assertFalse(attribute.shouldCollapseAttribute(settings));

        settings.syntax(Document.OutputSettings.Syntax.html);
        Attribute booleanAttribute = new Attribute("booleanAttribute", "");
        Assert.assertTrue(booleanAttribute.shouldCollapseAttribute(settings));
    }

    @Test
    public void testIsBooleanAttribute() {
        Attribute attribute = new Attribute("key", "value");
        Assert.assertFalse(attribute.isBooleanAttribute());

        Attribute booleanAttribute = new Attribute("checked", null);
        Assert.assertTrue(booleanAttribute.isBooleanAttribute());
    }
}