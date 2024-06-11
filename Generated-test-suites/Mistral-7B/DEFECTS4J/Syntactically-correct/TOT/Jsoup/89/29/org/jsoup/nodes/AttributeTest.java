import org.junit.Test;
import org.jsoup.nodes.Attribute;
import static org.junit.Assert.*;

public class AttributeTest {

 @Test
 public void testAttributeCreation() {
 String key = "class";
 String value = "myClass";
 Attribute attribute = new Attribute(key, value);
 assertEquals(key, attribute.getKey());
 assertEquals(value, attribute.getValue());
 }

 @Test
 public void testSettingKey() {
 String key = "class";
 String value = "myClass";
 Attribute attribute = new Attribute(key, value);
 String newKey = "id";
 attribute.setKey(newKey);
 assertEquals(newKey, attribute.getKey());
 }

 @Test
 public void testSettingValue() {
 String key = "class";
 String value = "myClass";
 Attribute attribute = new Attribute(key, value);
 String newValue = "newValue";
 attribute.setValue(newValue);
 assertEquals(newValue, attribute.getValue());
 }

 @Test
 public void testSettingParent() {
 String key = "class";
 String value = "myClass";
 Attributes parent = new Attributes(); // assuming Attributes is a valid class
 Attribute attribute = new Attribute(key, value, parent);
 attribute.setKey("id");
 assertEquals("id", parent.getKey(0));
 assertEquals("myClass", parent.getValue(0));
 }

 @Test(expected = NullPointerException.class)
 public void testNullKey() {
 Attribute attribute = new Attribute(null, "value");
 }

 @Test(expected = NullPointerException.class)
 public void testNullValue() {
 Attribute attribute = new Attribute("key", null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyKey() {
 Attribute attribute = new Attribute("", "value");
 }

 @Test
 public void testHtml() throws IOException {
 String key = "class";
 String value = "myClass";
 Attribute attribute = new Attribute(key, value);
 StringBuilder sb = StringUtil.borrowBuilder();
 attribute.html(sb, new Document("").outputSettings());
 String expected = key + "=\"myClass\"";
 assertEquals(expected, sb.toString());
 }

 @Test
 public void testIsDataAttribute() {
 String key = "data-test";
 boolean expected = true;
 Attribute attribute = new Attribute(key, null);
 assertTrue(attribute.isDataAttribute());
 }

 @Test
 public void testIsNotDataAttribute() {
 String key = "class";
 boolean expected = false;
 Attribute attribute = new Attribute(key, null);
 assertFalse(attribute.isDataAttribute());
 }

 @Test
 public void testShouldCollapseAttribute() throws IOException {
 Document.OutputSettings out = new Document.OutputSettings();
 String key1 = "checked";
 String value1 = "checked";
 Attribute attribute1 = new Attribute(key1, value1);
 assertTrue(attribute1.shouldCollapseAttribute(out));
 String expected1 = key1;
 StringBuilder sb1 = StringUtil.borrowBuilder();
 attribute1.html(sb1, out);
 assertEquals(expected1, sb1.toString());

 String key2 = "class";
 String value2 = "";
 Attribute attribute2 = new Attribute(key2, value2);
 assertTrue(attribute2.shouldCollapseAttribute(out));
 String expected2 = key2;
 StringBuilder sb2 = StringUtil.borrowBuilder();
 attribute2.html(sb2, out);
 assertEquals(expected2, sb2.toString());

 String key3 = "data-test";
 String value3 = "test";
 Attribute attribute3 = new Attribute(key3, value3);
 assertFalse(attribute3.shouldCollapseAttribute(out));
 String expected3 = key3 + "=\"test\"";
 StringBuilder sb3 = StringUtil.borrowBuilder();
 attribute3.html(sb3, out);
 assertEquals(expected3, sb3.toString());
 }

 @Test
 public void testIsBooleanAttribute() {
 String key1 = "checked";
 assertTrue(Attribute.isBooleanAttribute(key1));

 String key2 = "class";
 assertFalse(Attribute.isBooleanAttribute(key2));
 }
}