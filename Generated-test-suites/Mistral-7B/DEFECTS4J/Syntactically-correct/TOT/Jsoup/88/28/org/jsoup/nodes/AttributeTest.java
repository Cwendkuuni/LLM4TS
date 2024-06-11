import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;

public class AttributeTest {

    @Test
    public void testAttributeCreation() {
        Attribute attr = new Attribute("key", "value");
        Attribute expectedAttr = new Attribute("key", "value", null);
        assertEquals(expectedAttr, attr);
    }

    @Test
    public void testSettingKey() {
        Attribute attr = new Attribute("key", "value");
        attr.setKey("newKey");
        Attribute expectedAttr = new Attribute("newKey", "value", null);
        assertEquals(expectedAttr, attr);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSettingEmptyKey() {
        Attribute attr = new Attribute("key", "value");
        attr.setKey("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSettingNullKey() {
        Attribute attr = new Attribute("key", "value");
        attr.setKey(null);
    }

    @Test
    public void testSettingValue() {
        Attribute attr = new Attribute("key", "value");
        String newValue = "newValue";
        String oldValue = attr.getValue();
        attr.setValue(newValue);
        Attribute expectedAttr = new Attribute("key", newValue, null);
        assertEquals(expectedAttr, attr);
        assertEquals(oldValue, attr.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSettingNullValue() {
        Attribute attr = new Attribute("key", "value");
        attr.setValue(null);
    }

    @Test
    public void testHTMLRepresentation() throws IOException {
        Attribute attr = new Attribute("key", "value");
        StringBuilder expected = StringUtil.borrowBuilder();
        attr.html(expected, new Document("").outputSettings());
        String actual = attr.html();
        assertEquals(expected.toString(), actual);
    }

    @Test
    public void testToString() {
        Attribute attr = new Attribute("key", "value");
        String expected = attr.html();
        String actual = attr.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute attr = Attribute.createFromEncoded("key", "key=\"value\"");
        Attribute expectedAttr = new Attribute("key", "value", null);
        assertEquals(expectedAttr, attr);
    }

    @Test
    public void testIsDataAttribute() {
        Attribute attr = new Attribute("key", "value");
        assertFalse(attr.isDataAttribute());

        Attribute dataAttr = new Attribute(Attributes.dataPrefix + "key", "value");
        assertTrue(dataAttr.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        Attribute attr = new Attribute("key", "value");

        Document.OutputSettings out = new Document.OutputSettings();
        assertTrue(attr.shouldCollapseAttribute(out));
        out.syntax(Document.OutputSettings.Syntax.xml);
        assertFalse(attr.shouldCollapseAttribute(out));
    }

    @Test
    public void testIsBooleanAttribute() {
        Attribute attr = new Attribute("key", "value");
        assertFalse(attr.isBooleanAttribute());

        Attribute booleanAttr = new Attribute("checked", null);
        assertTrue(booleanAttr.isBooleanAttribute());
    }
}