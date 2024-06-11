import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeTest {

 @Test
 public void testAttributeCreation() {
 Attribute attribute = new Attribute("key", "value");
 assertEquals("key", attribute.getKey());
 assertEquals("value", attribute.getValue());
 }

 @Test(expected = NullPointerException.class)
 public void testNullKeyCreation() {
 Attribute attribute = new Attribute(null, "value");
 fail("Should not be able to create attribute with null key");
 }

 @Test(expected = NullPointerException.class)
 public void testNullValueCreation() {
 Attribute attribute = new Attribute("key", null);
 fail("Should not be able to create attribute with null value");
 }

 @Test
 public void testSettingKey() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setKey("newKey");
 assertEquals("newKey", attribute.getKey());
 }

 @Test(expected = NullPointerException.class)
 public void testNullKeySetting() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setKey(null);
 fail("Should not be able to set attribute key to null");
 }

 @Test
 public void testSettingValue() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setValue("newValue");
 assertEquals("newValue", attribute.getValue());
 }

 @Test(expected = NullPointerException.class)
 public void testNullValueSetting() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setValue(null);
 fail("Should not be able to set attribute value to null");
 }

 @Test
 public void testHtmlMethod() {
 StringBuilder sb = StringUtil.borrowBuilder();
 Attribute attribute = new Attribute("key", "value");
 try {
 attribute.html(sb, new Document("").outputSettings());
 } catch (IOException e) {
 e.printStackTrace();
 fail("IOException should not be thrown when calling html method");
 }
 String expectedOutput = "key=\"value\"";
 assertEquals(expectedOutput, StringUtil.releaseBuilder(sb));
 }

 @Test
 public void testBooleanAttributes() {
 Attribute booleanAttribute = new Attribute("checked", null);
 assertTrue(booleanAttribute.isBooleanAttribute());
 Attribute nonBooleanAttribute = new Attribute("key", "value");
 assertFalse(nonBooleanAttribute.isBooleanAttribute());
 }

 @Test
 public void testIsDataAttribute() {
 Attribute dataAttribute = new Attribute("data-key", null);
 assertTrue(dataAttribute.isDataAttribute());
 Attribute nonDataAttribute = new Attribute("key", null);
 assertFalse(nonDataAttribute.isDataAttribute());
 }

 @Test
 public void testShouldCollapseAttribute() {
 Document.OutputSettings outputSettings = new Document.OutputSettings();
 Attribute booleanAttribute = new Attribute("checked", "");
 assertTrue(booleanAttribute.shouldCollapseAttribute(outputSettings));
 Attribute nonBooleanAttribute = new Attribute("key", "value");
 assertFalse(nonBooleanAttribute.shouldCollapseAttribute(outputSettings));
 }

 @Test
 public void testClone() {
 Attribute originalAttribute = new Attribute("key", "value");
 Attribute clonedAttribute = originalAttribute.clone();
 assertEquals(originalAttribute.getKey(), clonedAttribute.getKey());
 assertEquals(originalAttribute.getValue(), clonedAttribute.getValue());
 }
}