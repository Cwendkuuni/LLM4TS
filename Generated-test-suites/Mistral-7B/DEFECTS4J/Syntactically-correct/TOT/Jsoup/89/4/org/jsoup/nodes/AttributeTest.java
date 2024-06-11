import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.helper.Validate;

public class AttributeTest {

 @Test
 public void testAttributeCreation() {
 String key = "class";
 String value = "test";
 Attribute attribute = new Attribute(key, value); // unencoded key and value
 Assert.assertEquals(key, attribute.getKey());
 Assert.assertEquals(value, attribute.getValue());

 key = "class";
 value = "test";
 Attribute attributeWithParent = new Attribute(key, value, null); // unencoded key, value, and null parent
 Assert.assertEquals(key, attributeWithParent.getKey());
 Assert.assertEquals(value, attributeWithParent.getValue());
 Assert.assertNull(attributeWithParent.getParent());

 key = "class";
 value = "test";
 Attributes parentAttributes = new Attributes();
 Attribute attributeWithParent2 = new Attribute(key, value, parentAttributes); // unencoded key, value, and Attributes parent
 Assert.assertEquals(key, attributeWithParent2.getKey());
 Assert.assertEquals(value, attributeWithParent2.getValue());
 Assert.assertEquals(parentAttributes, attributeWithParent2.getParent());
 }

 @Test
 public void testAttributeKeySetting() {
 Attribute attribute = new Attribute("key", "value");
 Assert.assertEquals("key", attribute.getKey());

 attribute.setKey("newKey");
 Assert.assertEquals("newKey", attribute.getKey());
 }

 @Test
 public void testAttributeValueSetting() {
 Attribute attribute = new Attribute("key", "value");
 Assert.assertEquals("value", attribute.getValue());

 attribute.setValue("newValue");
 Assert.assertEquals("newValue", attribute.getValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAttributeKeyNull() {
 Attribute attribute = new Attribute(null, "value");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAttributeValueNull() {
 Attribute attribute = new Attribute("key", null);
 }

 @Test
 public void testToStringMethod() {
 Attribute attribute = new Attribute("key", "value");
 Assert.assertEquals("key=\"" + "value" + "\"", attribute.toString());
 }

 @Test
 public void testCreateFromEncoded() {
 String unencodedKey = "class";
 String encodedValue = "test=\"test\"";
 Attribute attribute = Attribute.createFromEncoded(unencodedKey, encodedValue);
 Assert.assertEquals(unencodedKey, attribute.getKey());
 Assert.assertEquals("test", attribute.getValue());
 }

 @Test
 public void testIsBooleanAttribute() {
 Attribute booleanAttribute = new Attribute("checked", null);
 Assert.assertTrue(booleanAttribute.isBooleanAttribute());

 Attribute nonBooleanAttribute = new Attribute("class", "test");
 Assert.assertFalse(nonBooleanAttribute.isBooleanAttribute());
 }

 @Test
 public void testShouldCollapseAttribute() {
 Document.OutputSettings outputSettings = Document.OutputSettings.html();
 Attribute booleanAttribute = new Attribute("checked", "");
 Assert.assertTrue(booleanAttribute.shouldCollapseAttribute(outputSettings));

 Attribute nonBooleanAttribute = new Attribute("class", "test");
 Assert.assertFalse(nonBooleanAttribute.shouldCollapseAttribute(outputSettings));

 Attribute booleanAttributeWithValue = new Attribute("checked", "checked");
 Assert.assertTrue(booleanAttributeWithValue.shouldCollapseAttribute(outputSettings));

 Attribute nonBooleanAttributeWithValue = new Attribute("class", "testValue");
 Assert.assertFalse(nonBooleanAttributeWithValue.shouldCollapseAttribute(outputSettings));
 }

 @Test
 public void testEqualsMethod() {
 Attribute attribute1 = new Attribute("key1", "value1");
 Attribute attribute2 = new Attribute("key1", "value1");
 Attribute attribute3 = new Attribute("key2", "value2");
 Assert.assertTrue(attribute1.equals(attribute2));
 Assert.assertFalse(attribute1.equals(attribute3));
 }

 @Test
 public void testHashCodeMethod() {
 Attribute attribute1 = new Attribute("key1", "value1");
 Attribute attribute2 = new Attribute("key1", "value1");
 Attribute attribute3 = new Attribute("key2", "value2");
 Assert.assertEquals(attribute1.hashCode(), attribute2.hashCode());
 Assert.assertNotEquals(attribute1.hashCode(), attribute3.hashCode());
 }

 @Test
 public void testCloneMethod() {
 Attribute attribute = new Attribute("key", "value");
 Attribute clonedAttribute = attribute.clone();
 Assert.assertEquals(attribute, clonedAttribute);
 }
}