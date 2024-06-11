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
    public void testSettingKey() {
        Attribute attribute = new Attribute("key", "value");
        Assert.assertEquals("key", attribute.getKey());

        attribute.setKey("newKey");
        Assert.assertEquals("newKey", attribute.getKey());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSettingNullKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey(null);
    }

    @Test
    public void testSettingValue() {
        Attribute attribute = new Attribute("key", "value");
        Assert.assertEquals("value", attribute.getValue());

        attribute.setValue("newValue");
        Assert.assertEquals("newValue", attribute.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSettingNullValue() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setValue(null);
    }

    @Test
    public void testHtmlRepresentation() throws IOException {
        Attribute attribute = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        attribute.html(sb);

        String html = StringUtil.releaseBuilder(sb);
        Assert.assertEquals("<key value=\"value\"></key>", html);
    }

    @Test
    public void testBooleanAttributeCollapse() throws IOException {
        Document.OutputSettings settings = new Document.OutputSettings();
        settings.syntax(Document.OutputSettings.Syntax.html);

        Attribute booleanAttribute = new Attribute("checked", null);
        StringBuilder sb = StringUtil.borrowBuilder();
        boolean collapsible = booleanAttribute.shouldCollapseAttribute(settings);
        booleanAttribute.html(sb, settings);

        String html = StringUtil.releaseBuilder(sb);
        Assert.assertEquals("<checked></checked>", html);
        Assert.assertTrue(collapsible);

        Attribute nonBooleanAttribute = new Attribute("key", "value");
        boolean collapsibleNonBoolean = nonBooleanAttribute.shouldCollapseAttribute(settings);
        nonBooleanAttribute.html(sb, settings);

        html = StringUtil.releaseBuilder(sb);
        Assert.assertEquals("<key value=\"value\"></key>", html);
        Assert.assertFalse(collapsibleNonBoolean);
    }
}