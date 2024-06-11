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
    public void testAttributeSetKey() {
        Attribute attr = new Attribute("key", "value");
        attr.setKey("newKey");
        Assert.assertEquals("newKey", attr.getKey());

        Attribute attrWithParent = new Attribute("key", "value", new Attributes());
        attrWithParent.setKey("newKey");
        Assert.assertEquals("newKey", attrWithParent.getKey());
        Assert.assertNotNull(attrWithParent.getParent());
    }

    @Test(expected = NullPointerException.class)
    public void testSetKeyWithNull() {
        Attribute attr = new Attribute("key", "value");
        attr.setKey(null);
    }

    @Test
    public void testAttributeSetValue() {
        Attribute attr = new Attribute("key", "value");
        String oldValue = attr.setValue("newValue");
        Assert.assertEquals("newValue", attr.getValue());
        Assert.assertEquals("value", oldValue);

        Attribute attrWithParent = new Attribute("key", "value", new Attributes());
        String oldValueWithParent = attrWithParent.setValue("newValue");
        Assert.assertEquals("newValue", attrWithParent.getValue());
        Assert.assertEquals("value", oldValueWithParent);
    }

    @Test(expected = NullPointerException.class)
    public void testSetValueWithNull() {
        Attribute attr = new Attribute("key", "value");
        attr.setValue(null);
    }

    @Test
    public void testAttributeHtml() throws IOException {
        Attribute attr = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        attr.html(sb, new Document("").outputSettings());
        String result = StringUtil.releaseBuilder(sb);
        Assert.assertEquals("key=\"value\"", result);
    }

    @Test
    public void testIsDataAttribute() {
        Attribute attr = new Attribute("key", "value");
        Assert.assertFalse(attr.isDataAttribute());

        Attribute dataAttr = new Attribute("data-key", "value");
        Assert.assertTrue(dataAttr.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() throws IOException {
        Attribute attr = new Attribute("key", "value");
        Assert.assertFalse(attr.shouldCollapseAttribute(new Document.OutputSettings()));

        Attribute booleanAttr = new Attribute("checked", "");
        Assert.assertTrue(booleanAttr.shouldCollapseAttribute(new Document.OutputSettings()));

        Attribute booleanAttrWithValue = new Attribute("checked", "checked");
        Assert.assertTrue(booleanAttrWithValue.shouldCollapseAttribute(new Document.OutputSettings()));
    }

    @Test
    public void testIsBooleanAttribute() {
        Attribute attr = new Attribute("key", "value");
        Assert.assertFalse(attr.isBooleanAttribute());

        Attribute booleanAttr = new Attribute("checked", null);
        Assert.assertTrue(booleanAttr.isBooleanAttribute());
    }

    @Test
    public void testEquals() {
        Attribute attr1 = new Attribute("key1", "value1");
        Attribute attr2 = new Attribute("key1", "value1");
        Attribute attr3 = new Attribute("key2", "value2");

        Assert.assertTrue(attr1.equals(attr1));
        Assert.assertTrue(attr1.equals(attr2));
        Assert.assertFalse(attr1.equals(attr3));
    }

    @Test
    public void testClone() {
        Attribute attr = new Attribute("key", "value");
        Attribute clonedAttr = attr.clone();
        Assert.assertEquals(attr.getKey(), clonedAttr.getKey());
        Assert.assertEquals(attr.getValue(), clonedAttr.getValue());
        Assert.assertNotEquals(attr, clonedAttr);
    }
}