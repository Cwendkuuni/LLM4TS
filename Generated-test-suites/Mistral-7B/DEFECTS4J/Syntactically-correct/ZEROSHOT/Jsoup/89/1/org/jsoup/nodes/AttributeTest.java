import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;

public class AttributeTest {

 @Test
 public void testAttributeCreation() {
 // Test creating a new Attribute with key and value
 Attribute attribute = new Attribute("key", "value");
 Assert.assertEquals("key", attribute.getKey());
 Assert.assertEquals("value", attribute.getValue());

 // Test creating a new Attribute with key, value, and parent
 Attribute attributeWithParent = new Attribute("key", "value", new Attributes());
 Assert.assertEquals("key", attributeWithParent.getKey());
 Assert.assertEquals("value", attributeWithParent.getValue());

 // Test creating an Attribute from encoded key and value
 Attribute encodedAttribute = Attribute.createFromEncoded("key", "value%20%3D%22%22%22");
 Assert.assertEquals("key", encodedAttribute.getKey());
 Assert.assertEquals("value\"\"", encodedAttribute.getValue());
 }

 @Test
 public void testSettingKey() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setKey("newKey");
 Assert.assertEquals("newKey", attribute.getKey());
 }

 @Test(expected = NullPointerException.class)
 public void testSettingNullKey() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setKey(null);
 }

 @Test
 public void testSettingValue() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setValue("newValue");
 Assert.assertEquals("newValue", attribute.getValue());
 }

 @Test(expected = NullPointerException.class)
 public void testSettingNullValue() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setValue(null);
 }

 @Test
 public void testCollapsibleAttribute() throws IOException {
 Document.OutputSettings outputSettings = new Document.OutputSettings();
 Attribute attribute = new Attribute("booleanAttribute", "true");
 Assert.assertTrue(attribute.shouldCollapseAttribute(outputSettings));
 Attribute attribute2 = new Attribute("booleanAttribute", "");
 Assert.assertTrue(attribute2.shouldCollapseAttribute(outputSettings));
 Attribute attribute3 = new Attribute("booleanAttribute", "false");
 Assert.assertFalse(attribute3.shouldCollapseAttribute(outputSettings));
 Attribute attribute4 = new Attribute("key", "value");
 Assert.assertFalse(attribute4.shouldCollapseAttribute(outputSettings));
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
 public void testHashCode() {
 Attribute attribute1 = new Attribute("key1", "value1");
 Attribute attribute2 = new Attribute("key1", "value1");
 Attribute attribute3 = new Attribute("key2", "value2");
 Assert.assertEquals(attribute1.hashCode(), attribute2.hashCode());
 Assert.assertNotEquals(attribute1.hashCode(), attribute3.hashCode());
 }

 @Test
 public void testClone() {
 Attribute attribute = new Attribute("key", "value");
 Attribute clonedAttribute = attribute.clone();
 Assert.assertEquals(attribute.getKey(), clonedAttribute.getKey());
 Assert.assertEquals(attribute.getValue(), clonedAttribute.getValue());
 }
}