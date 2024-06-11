import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.junit.Assert;
import org.junit.Test;

public class AttributeTest {

 @Test
 public void testAttributeCreation() {
 Attribute attribute = new Attribute("key", "value");
 Assert.assertEquals("key", attribute.getKey());
 Assert.assertEquals("value", attribute.getValue());
 }

 @Test
 public void testAttributeCreationWithParent() {
 Attributes parentAttributes = new Attributes();
 Attribute attribute = new Attribute("key", "value", parentAttributes);
 Assert.assertEquals("key", attribute.getKey());
 Assert.assertEquals("value", attribute.getValue());
 Assert.assertEquals(attribute, parentAttributes.get("key"));
 }

 @Test
 public void testSettingKey() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setKey("newKey");
 Assert.assertEquals("newKey", attribute.getKey());
 }

 @Test
 public void testSettingValue() {
 Attribute attribute = new Attribute("key", "value");
 String newValue = "newValue";
 attribute.setValue(newValue);
 Assert.assertEquals(newValue, attribute.getValue());
 }

 @Test
 public void testHtmlMethod() throws IOException {
 Attribute attribute = new Attribute("key", "value");
 StringBuilder sb = StringUtil.borrowBuilder();
 attribute.html(sb, new Document("").outputSettings());
 String html = StringUtil.releaseBuilder(sb);
 Assert.assertEquals("key=\"value\"", html);
 }

 @Test
 public void testToStringMethod() {
 Attribute attribute = new Attribute("key", "value");
 Assert.assertEquals("key=\"value\"", attribute.toString());
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
 public void testShouldCollapseAttribute() {
 Document.OutputSettings outputSettings = new Document.OutputSettings();
 Attribute attribute = new Attribute("key", "value");
 Assert.assertFalse(attribute.shouldCollapseAttribute(outputSettings));
 Attribute booleanAttribute = new Attribute("checked", "");
 Assert.assertTrue(booleanAttribute.shouldCollapseAttribute(outputSettings));
 }

 @Test
 public void testIsBooleanAttribute() {
 Attribute attribute = new Attribute("key", "value");
 Assert.assertFalse(attribute.isBooleanAttribute());
 Attribute booleanAttribute = new Attribute("checked", "");
 Assert.assertTrue(booleanAttribute.isBooleanAttribute());
 }

 @Test
 public void testIsDataAttributeMethod() {
 Attribute attribute = new Attribute("key", "value");
 Assert.assertFalse(attribute.isDataAttribute());
 Attribute dataAttribute = new Attribute("data-key", "value");
 Assert.assertTrue(dataAttribute.isDataAttribute());
 }

 @Test
 public void testEqualsMethod() {
 Attribute attribute1 = new Attribute("key1", "value1");
 Attribute attribute2 = new Attribute("key2", "value2");
 Attribute attribute3 = new Attribute("key1", "value1");
 Attribute attribute4 = new Attribute("key1", "value1");
 Attribute attribute5 = attribute3;
 Assert.assertFalse(attribute1.equals(attribute2));
 Assert.assertTrue(attribute3.equals(attribute4));
 Assert.assertTrue(attribute3.equals(attribute5));
 }

 @Test
 public void testHashCodeMethod() {
 Attribute attribute1 = new Attribute("key1", "value1");
 Attribute attribute2 = new Attribute("key2", "value2");
 Attribute attribute3 = new Attribute("key1", "value1");
 Attribute attribute4 = new Attribute("key1", "value1");
 Attribute attribute5 = attribute3;
 Assert.assertEquals(attribute1.hashCode(), attribute4.hashCode());
 Assert.assertEquals(attribute3.hashCode(), attribute5.hashCode());
 }

 @Test
 public void testCloneMethod() {
 Attribute attribute = new Attribute("key", "value");
 Attribute clonedAttribute = attribute.clone();
 Assert.assertEquals(attribute, clonedAttribute);
 Assert.assertNotSame(attribute, clonedAttribute);
 }
}