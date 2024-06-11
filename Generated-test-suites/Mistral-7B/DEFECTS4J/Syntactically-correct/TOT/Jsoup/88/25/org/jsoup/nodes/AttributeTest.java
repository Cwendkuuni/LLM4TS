import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;

public class AttributeTest {

    @Test
    public void testAttributeCreation() {
        Attribute attr = new Attribute("key", "value");
        Assert.assertEquals("key", attr.getKey());
        Assert.assertEquals("value", attr.getValue());

        Attribute attrWithParent = new Attribute("key", "value", new Attributes());
        Assert.assertEquals("key", attrWithParent.getKey());
        Assert.assertEquals("value", attrWithParent.getValue());
        Assert.assertNull(attrWithParent.getParent());
    }

    @Test
    public void testAttributeKeySetter() {
        Attribute attr = new Attribute("key", "value");
        attr.setKey("newKey");
        Assert.assertEquals("newKey", attr.getKey());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAttributeKeySetterWithNullKey() {
        Attribute attr = new Attribute("key", "value");
        attr.setKey(null);
    }

    @Test
    public void testAttributeValueSetter() {
        Attribute attr = new Attribute("key", "value");
        attr.setValue("newValue");
        Assert.assertEquals("newValue", attr.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAttributeValueSetterWithNullValue() {
        Attribute attr = new Attribute("key", "value");
        attr.setValue(null);
    }

    @Test
    public void testAttributeHtml() throws IOException {
        Attribute attr = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        attr.html(sb, new Document("").outputSettings());
        String expectedHtml = "key=\"value\"";
        String actualHtml = StringUtil.releaseBuilder(sb);
        Assert.assertEquals(expectedHtml, actualHtml);
    }

    @Test
    public void testAttributeIsDataAttribute() {
        Attribute attr = new Attribute("key", "value");
        Assert.assertFalse(attr.isDataAttribute());

        Attribute dataAttr = new Attribute("data-key", "value");
        Assert.assertTrue(dataAttr.isDataAttribute());
    }

    @Test
    public void testAttributeShouldCollapseAttribute() {
        Attribute attr = new Attribute("checked", null);
        Assert.assertTrue(attr.shouldCollapseAttribute(new Document.OutputSettings()));

        Attribute attrBoolean = new Attribute("checked", "checked");
        Assert.assertTrue(attrBoolean.shouldCollapseAttribute(new Document.OutputSettings()));

        Attribute attrNotBoolean = new Attribute("key", "value");
        Assert.assertFalse(attrNotBoolean.shouldCollapseAttribute(new Document.OutputSettings()));
    }

    @Test
    public void testAttributeClone() {
        Attribute original = new Attribute("key", "value");
        Attribute cloned = original.clone();
        Assert.assertEquals(original.getKey(), cloned.getKey());
        Assert.assertEquals(original.getValue(), cloned.getValue());
    }
}