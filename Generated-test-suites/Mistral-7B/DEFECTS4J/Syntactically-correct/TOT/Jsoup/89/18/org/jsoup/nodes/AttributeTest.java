import org.junit.Test;
import org.jsoup.nodes.Attribute;
import static org.junit.Assert.*;

public class AttributeTest {

 @Test
 public void testAttributeCreation() {
 String key = "class";
 String value = "newClass";
 Attribute attribute = new Attribute(key, value, null);
 assertEquals("class", attribute.getKey());
 assertEquals("newClass", attribute.getValue());
 }

 @Test
 public void testAttributeCreationWithParent() {
 String key = "id";
 String value = "testID";
 Attributes parent = new Attributes(); // Assuming Attributes is a class that can be instantiated
 Attribute attribute = new Attribute(key, value, parent);
 assertEquals("id", attribute.getKey());
 assertEquals("testID", attribute.getValue());
 // Verify that the attribute is added to the parent Attributes
 assertEquals(1, parent.size());
 assertEquals(attribute, parent.values().toArray()[0]);
 }

 @Test
 public void testSettingKey() {
 Attribute attribute = new Attribute("key", "value", null);
 attribute.setKey("newKey");
 assertEquals("newKey", attribute.getKey());
 }

 @Test
 public void testSettingEmptyKey() {
 Attribute attribute = new Attribute("key", "value", null);
 try {
 attribute.setKey("");
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testSettingNullKey() {
 Attribute attribute = new Attribute("key", "value", null);
 try {
 attribute.setKey(null);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testSettingValue() {
 Attribute attribute = new Attribute("key", "value", null);
 String newValue = "newValue";
 attribute.setValue(newValue);
 assertEquals("newValue", attribute.getValue());
 }

 @Test
 public void testSettingNullValue() {
 Attribute attribute = new Attribute("key", "value", null);
 try {
 attribute.setValue(null);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testHtml() throws IOException {
 Attribute attribute = new Attribute("key", "value", null);
 StringBuilder sb = StringUtil.borrowBuilder();
 attribute.html(sb, new Document("").outputSettings());
 String expectedHtml = "key=\"value\"";
 assertEquals(expectedHtml, StringUtil.releaseBuilder(sb));
 }

 @Test
 public void testIsDataAttribute() {
 Attribute attribute = new Attribute("key", "value", null);
 assertFalse(attribute.isDataAttribute());
 attribute.setKey("data-test");
 assertTrue(attribute.isDataAttribute());
 }

 @Test
 public void testShouldCollapseAttribute() throws IOException {
 Document.OutputSettings out = new Document.OutputSettings();
 Attribute attribute1 = new Attribute("class", null, null);
 Attribute attribute2 = new Attribute("checked", null, null);
 assertTrue(attribute1.shouldCollapseAttribute(out));
 assertFalse(attribute2.shouldCollapseAttribute(out));
 }

 @Test
 public void testIsBooleanAttribute() {
 Attribute attribute = new Attribute("checked", null, null);
 assertTrue(attribute.isBooleanAttribute());
 attribute.setValue("true");
 assertTrue(attribute.isBooleanAttribute());
 attribute.setValue("false");
 assertTrue(attribute.isBooleanAttribute());
 attribute.setValue("notBoolean");
 assertFalse(attribute.isBooleanAttribute());
 }

 @Test
 public void testEquals() {
 Attribute attribute1 = new Attribute("key1", "value1", null);
 Attribute attribute2 = new Attribute("key2", "value2", null);
 Attribute attribute3 = new Attribute("key1", "value1", null);
 Attribute attribute4 = attribute1;
 assertFalse(attribute1.equals(attribute2));
 assertTrue(attribute1.equals(attribute3));
 assertTrue(attribute1.equals(attribute4));
 }

 @Test
 public void testHashCode() {
 Attribute attribute1 = new Attribute("key1", "value1", null);
 Attribute attribute2 = new Attribute("key2", "value2", null);
 Attribute attribute3 = new Attribute("key1", "value1", null);
 Attribute attribute4 = attribute1;
 assertEquals(attribute1.hashCode(), attribute3.hashCode());
 assertEquals(attribute1.hashCode(), attribute4.hashCode());
 assertNotEquals(attribute1.hashCode(), attribute2.hashCode());
 }

 @Test
 public void testClone() {
 Attribute original = new Attribute("key", "value", null);
 Attribute cloned = original.clone();
 assertEquals(original.getKey(), cloned.getKey());
 assertEquals(original.getValue(), cloned.getValue());
 }
}