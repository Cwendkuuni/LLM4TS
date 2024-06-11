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
        Assert.assertNull(attributeWithParent.getParent());
    }

    @Test
    public void testAttributeKeySetter() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("newKey");
        Assert.assertEquals("newKey", attribute.getKey());

        Attribute attributeWithParent = new Attribute("key", "value", new Attributes());
        attributeWithParent.setKey("newKey");
        Assert.assertEquals("newKey", attributeWithParent.getKey());
        Assert.assertNotNull(attributeWithParent.getParent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAttributeKeySetterNull() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey(null);
    }

    @Test
    public void testAttributeValueSetter() {
        Attribute attribute = new Attribute("key", "value");
        Attribute attributeWithParent = new Attribute("key", "value", new Attributes());

        String newValue = "newValue";
        attribute.setValue(newValue);
        Assert.assertEquals(newValue, attribute.getValue());
        Assert.assertNull(attribute.getParent());

        attributeWithParent.setValue(newValue);
        Assert.assertEquals(newValue, attributeWithParent.getValue());
        Assert.assertNotNull(attributeWithParent.getParent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAttributeValueSetterNull() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setValue(null);
    }

    @Test
    public void testHtmlMethod() throws Exception {
        Attribute attribute = new Attribute("key", "value");

        StringBuilder sb = StringUtil.borrowBuilder();
        attribute.html(sb, new Document("").outputSettings());
        Assert.assertEquals("key=\"value\"", StringUtil.releaseBuilder(sb));

        sb = StringUtil.borrowBuilder();
        attribute.html(sb);
        Assert.assertEquals("key=\"value\"", StringUtil.releaseBuilder(sb));
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
    public void testShouldCollapseAttribute() throws Exception {
        Document.OutputSettings out = new Document.OutputSettings();
        Attribute attribute = new Attribute("key", "");
        Assert.assertTrue(attribute.shouldCollapseAttribute(out));

        Attribute attributeWithValue = new Attribute("key", "value");
        Assert.assertFalse(attributeWithValue.shouldCollapseAttribute(out));

        out.syntax(Document.OutputSettings.Syntax.xml);
        Attribute xmlAttribute = new Attribute("key", "");
        Assert.assertTrue(xmlAttribute.shouldCollapseAttribute(out));

        out.syntax(Document.OutputSettings.Syntax.none);
        Attribute noSyntaxAttribute = new Attribute("key", "");
        Assert.assertFalse(noSyntaxAttribute.shouldCollapseAttribute(out));
    }
}