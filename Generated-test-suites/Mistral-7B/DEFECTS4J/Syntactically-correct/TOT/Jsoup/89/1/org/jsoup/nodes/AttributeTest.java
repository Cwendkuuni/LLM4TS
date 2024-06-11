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
        Assert.assertEquals("value", attributeWithParent.parent.get("key"));
    }

    @Test(expected = NullPointerException.class)
    public void testAttributeNullKey() {
        new Attribute(null, "value");
    }

    @Test(expected = NullPointerException.class)
    public void testAttributeNullValue() {
        new Attribute("key", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAttributeEmptyKey() {
        new Attribute("", "value");
    }

    @Test
    public void testAttributeHtml() throws IOException {
        Attribute attribute = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        attribute.html(sb, new Document("").outputSettings());
        Assert.assertEquals("key=\"value\"", StringUtil.releaseBuilder(sb).toString());
    }

    @Test
    public void testIsDataAttribute() {
        Attribute attribute = new Attribute("data-key", "value");
        Assert.assertTrue(attribute.isDataAttribute());
        Attribute attributeNotData = new Attribute("key", "value");
        Assert.assertFalse(attributeNotData.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.syntax(Document.OutputSettings.Syntax.html);

        Attribute attributeBool = new Attribute("checked", "");
        Assert.assertTrue(attributeBool.shouldCollapseAttribute(outputSettings));

        Attribute attributeNotBool = new Attribute("key", "value");
        Assert.assertFalse(attributeNotBool.shouldCollapseAttribute(outputSettings));
    }

    @Test
    public void testIsBooleanAttribute() {
        Attribute attribute = new Attribute("checked", "");
        Assert.assertTrue(attribute.isBooleanAttribute());
        Attribute attributeNotBool = new Attribute("key", "value");
        Assert.assertFalse(attributeNotBool.isBooleanAttribute());
    }

    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = new Attribute("key1", "value1");
        Attribute attribute3 = new Attribute("key2", "value2");

        Assert.assertTrue(attribute1.equals(attribute2));
        Assert.assertFalse(attribute1.equals(attribute3));
    }

    @Test
    public void testClone() {
        Attribute attribute = new Attribute("key", "value");
        Attribute clonedAttribute = attribute.clone();
        Assert.assertEquals(attribute, clonedAttribute);
    }
}