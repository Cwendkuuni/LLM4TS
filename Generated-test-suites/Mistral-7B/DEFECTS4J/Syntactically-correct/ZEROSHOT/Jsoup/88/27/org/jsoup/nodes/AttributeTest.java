import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Map;

public class AttributeTest {

 @Test
 public void testAttributeCreation() {
 Attribute attribute = new Attribute("key", "value");
 Assertions.assertEquals("key", attribute.getKey());
 Assertions.assertEquals("value", attribute.getValue());
 }

 @Test
 public void testSetKey() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setKey("newKey");
 Assertions.assertEquals("newKey", attribute.getKey());
 }

 @Test
 public void testSetValue() {
 Attribute attribute = new Attribute("key", "value");
 String newValue = "newValue";
 Assertions.assertEquals("value", attribute.getValue());
 String oldValue = attribute.setValue(newValue);
 Assertions.assertEquals("value", oldValue);
 Assertions.assertEquals("newValue", attribute.getValue());
 }

 @Test
 public void testHtml() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 Attribute attribute = new Attribute("key", "value");
 attribute.html(sb, new Document("").outputSettings());
 String expectedHtml = "key=\"value\"";
 Assertions.assertEquals(expectedHtml, StringUtil.releaseBuilder(sb));
 }

 @Test
 public void testCreateFromEncoded() {
 Attribute attribute = Attribute.createFromEncoded("key", "key=\"value\"");
 Assertions.assertEquals("key", attribute.getKey());
 Assertions.assertEquals("value", attribute.getValue());
 }

 @Test
 public void testIsDataAttribute() {
 Attribute dataAttribute = new Attribute("data-key", null);
 Attribute nonDataAttribute = new Attribute("key", null);
 Assertions.assertTrue(dataAttribute.isDataAttribute());
 Assertions.assertFalse(nonDataAttribute.isDataAttribute());
 }

 @Test
 public void testShouldCollapseAttribute() throws IOException {
 Document.OutputSettings outputSettings = new Document.OutputSettings();
 Attribute booleanAttribute = new Attribute("checked", null);
 Attribute nonBooleanAttribute = new Attribute("key", "value");
 Assertions.assertTrue(booleanAttribute.shouldCollapseAttribute(outputSettings));
 Assertions.assertFalse(nonBooleanAttribute.shouldCollapseAttribute(outputSettings));
 }

 @Test
 public void testIsBooleanAttribute() {
 Attribute booleanAttribute = new Attribute("checked", null);
 Attribute nonBooleanAttribute = new Attribute("key", null);
 Assertions.assertTrue(booleanAttribute.isBooleanAttribute());
 Assertions.assertFalse(nonBooleanAttribute.isBooleanAttribute());
 }

 @Test
 public void testIsBooleanAttributeWithValue() {
 Attribute booleanAttribute = new Attribute("checked", "checked");
 Attribute nonBooleanAttribute = new Attribute("key", "value");
 Assertions.assertTrue(booleanAttribute.isBooleanAttribute());
 Assertions.assertFalse(nonBooleanAttribute.isBooleanAttribute());
 }

 @Test
 public void testEquals() {
 Attribute attribute1 = new Attribute("key1", "value1");
 Attribute attribute2 = new Attribute("key2", "value2");
 Attribute attribute3 = new Attribute("key1", "value1");
 Attribute attribute4 = attribute1;
 Assertions.assertFalse(attribute1.equals(attribute2));
 Assertions.assertTrue(attribute1.equals(attribute3));
 Assertions.assertTrue(attribute1.equals(attribute4));
 }

 @Test
 public void testHashCode() {
 Attribute attribute1 = new Attribute("key1", "value1");
 Attribute attribute2 = new Attribute("key2", "value2");
 Attribute attribute3 = new Attribute("key1", "value1");
 Attribute attribute4 = attribute1;
 Assertions.assertNotEquals(attribute1.hashCode(), attribute2.hashCode());
 Assertions.assertEquals(attribute1.hashCode(), attribute3.hashCode());
 Assertions.assertEquals(attribute1.hashCode(), attribute4.hashCode());
 }

 @Test
 public void testClone() {
 Attribute attribute = new Attribute("key", "value");
 Attribute clonedAttribute = attribute.clone();
 Assertions.assertEquals(attribute.getKey(), clonedAttribute.getKey());
 Assertions.assertEquals(attribute.getValue(), clonedAttribute.getValue());
 }
}