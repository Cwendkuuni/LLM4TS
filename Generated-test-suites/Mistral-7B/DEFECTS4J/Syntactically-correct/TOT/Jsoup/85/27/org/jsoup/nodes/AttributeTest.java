import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;

import static org.junit.Assert.*;

public class AttributeTest {

 private Attribute attribute;
 private Attributes attributes;

 @Before
 public void setUp() {
 attribute = new Attribute("key", "value");
 attributes = new Attributes();
 attributes.put(attribute);
 }

 @Test
 public void testAttributeCreation() {
 Attribute expectedAttribute = new Attribute("key", "value");
 assertEquals(expectedAttribute, attribute);
 }

 @Test
 public void testSettingKey() {
 String newKey = "newKey";
 attribute.setKey(newKey);
 assertEquals(newKey, attribute.getKey());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSettingEmptyKey() {
 attribute.setKey("");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSettingNullKey() {
 attribute.setKey(null);
 }

 @Test
 public void testSettingValue() {
 String newValue = "newValue";
 attribute.setValue(newValue);
 assertEquals(newValue, attribute.getValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSettingNullValue() {
 attribute.setValue(null);
 }

 @Test
 public void testSettingParent() {
 attributes.put(attribute);
 assertEquals(attribute, attributes.get("key"));
 }

 @Test
 public void testGettingParent() {
 attribute.setParent(attributes);
 assertEquals(attributes, attribute.getParent());
 }

 @Test
 public void testHtml() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 attribute.html(sb, new Document("").outputSettings());
 String expectedHtml = "key=\"value\"";
 assertEquals(expectedHtml, StringUtil.releaseBuilder(sb));
 }

 @Test
 public void testCreatingFromEncoded() {
 String encodedKey = "key=\"value\"";
 Attribute expectedAttribute = new Attribute("key", "value");
 Attribute createdAttribute = Attribute.createFromEncoded("key", encodedKey);
 assertEquals(expectedAttribute, createdAttribute);
 }

 @Test
 public void testIsDataAttribute() {
 assertTrue(attribute.isDataAttribute());
 }

 @Test
 public void testShouldCollapseAttribute() {
 Document.OutputSettings out = new Document.OutputSettings();
 assertTrue(attribute.shouldCollapseAttribute(out));
 }

 @Test
 public void testIsBooleanAttribute() {
 assertFalse(attribute.isBooleanAttribute());
 attribute.setKey("checked");
 assertTrue(attribute.isBooleanAttribute());
 }

 @Test
 public void testEquals() {
 Attribute anotherAttribute = new Attribute("key", "value");
 assertTrue(attribute.equals(anotherAttribute));
 }

 @Test
 public void testHashCode() {
 Attribute anotherAttribute = new Attribute("key", "value");
 int expectedHashCode = attribute.hashCode();
 int anotherHashCode = anotherAttribute.hashCode();
 assertEquals(expectedHashCode, anotherHashCode);
 }

 @Test
 public void testClone() throws CloneNotSupportedException {
 Attribute clonedAttribute = attribute.clone();
 assertEquals(attribute, clonedAttribute);
 }
}