import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;

public class AttributeTest {

    @Test
    public void testAttributeCreation() {
        String key = "class";
        String value = "test";
        Attribute attribute = new Attribute(key, value);

        Assert.assertEquals(key, attribute.getKey());
        Assert.assertEquals(value, attribute.getValue());
        Assert.assertNull(attribute.getParent());
    }

    @Test
    public void testAttributeCreationWithParent() {
        String key = "class";
        String value = "test";
        Attributes parent = new Attributes();
        Attribute attribute = new Attribute(key, value, parent);

        Assert.assertEquals(key, attribute.getKey());
        Assert.assertEquals(value, attribute.getValue());
        Assert.assertSame(parent, attribute.getParent());
        Assert.assertFalse(parent.containsValue(value));
        Assert.assertFalse(parent.containsKey(key));
        parent.put(key, value);
        Assert.assertTrue(parent.containsValue(value));
        Assert.assertTrue(parent.containsKey(key));
    }

    @Test
    public void testSettingKey() {
        String key = "class";
        String value = "test";
        Attribute attribute = new Attribute(key, value);

        String newKey = "id";
        attribute.setKey(newKey);

        Assert.assertEquals(newKey, attribute.getKey());
        Assert.assertEquals(value, attribute.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testSettingKeyNull() {
        String key = "class";
        String value = "test";
        Attribute attribute = new Attribute(key, value);

        attribute.setKey(null);
    }

    @Test
    public void testSettingValue() {
        String key = "class";
        String initialValue = "test";
        String newValue = "newTest";
        Attribute attribute = new Attribute(key, initialValue);

        String oldValue = attribute.setValue(newValue);

        Assert.assertEquals(newValue, attribute.getValue());
        Assert.assertNotSame(initialValue, oldValue);
    }

    @Test(expected = NullPointerException.class)
    public void testSettingValueNull() {
        String key = "class";
        String value = "test";
        Attribute attribute = new Attribute(key, value);

        attribute.setValue(null);
    }

    @Test
    public void testHtml() throws IOException {
        String key = "class";
        String value = "test";
        Attribute attribute = new Attribute(key, value);

        StringBuilder expected = StringUtil.borrowBuilder();
        attribute.html(expected, new Document("").outputSettings());

        String actual = attribute.html();

        Assert.assertEquals(expected.toString(), actual);
    }

    @Test
    public void testShouldCollapseAttribute() {
        String key = "checked";
        String value = "checked";
        Document.OutputSettings out = new Document.OutputSettings();

        Assert.assertTrue(Attribute.shouldCollapseAttribute(key, value, out));
    }

    @Test
    public void testShouldCollapseAttributeFalse() {
        String key = "class";
        String value = "test";
        Document.OutputSettings out = new Document.OutputSettings();

        Assert.assertFalse(Attribute.shouldCollapseAttribute(key, value, out));
    }

    @Test
    public void testIsBooleanAttribute() {
        String key = "checked";
        Assert.assertTrue(Attribute.isBooleanAttribute(key));

        key = "class";
        Assert.assertFalse(Attribute.isBooleanAttribute(key));
    }
}