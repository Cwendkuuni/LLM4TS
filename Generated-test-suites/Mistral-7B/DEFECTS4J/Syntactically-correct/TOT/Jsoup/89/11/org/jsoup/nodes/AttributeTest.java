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
    }

    @Test
    public void testAttributeKeySetter() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("newKey");
        Assert.assertEquals("newKey", attribute.getKey());
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

    @Test
    public void testAttributeValueSetter() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setValue("newValue");
        Assert.assertEquals("newValue", attribute.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAttributeValueSetterNull() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setValue(null);
    }

    @Test
    public void testAttributeHtml() throws IOException {
        Attribute attribute = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        attribute.html(sb, new Document("").outputSettings());
        Assert.assertEquals("key=\"value\"", StringUtil.releaseBuilder(sb));
    }

    @Test
    public void testAttributeIsBooleanAttribute() {
        Attribute booleanAttribute = new Attribute("checked", null);
        assertTrue(booleanAttribute.isBooleanAttribute());

        Attribute nonBooleanAttribute = new Attribute("key", "value");
        assertFalse(nonBooleanAttribute.isBooleanAttribute());
    }

    @Test
    public void testAttributeShouldCollapseAttribute() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();

        Attribute booleanAttribute = new Attribute("checked", "");
        assertTrue(booleanAttribute.shouldCollapseAttribute(outputSettings));

        Attribute nonBooleanAttribute = new Attribute("key", "value");
        assertFalse(nonBooleanAttribute.shouldCollapseAttribute(outputSettings));
    }
}