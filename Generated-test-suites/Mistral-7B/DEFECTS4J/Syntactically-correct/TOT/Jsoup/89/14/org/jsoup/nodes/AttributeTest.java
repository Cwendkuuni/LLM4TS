import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;

public class AttributeTest {

 private Attribute attribute;

 @Before
 public void setUp() {
 attribute = new Attribute("key", "value");
 }

 @Test
 public void testGetKey() {
 String key = attribute.getKey();
 assertEquals("key", key);
 }

 @Test
 public void testSetKey() {
 attribute.setKey("newKey");
 String newKey = attribute.getKey();
 assertEquals("newKey", newKey);
 }

 @Test
 public void testGetValue() {
 String value = attribute.getValue();
 assertEquals("value", value);
 }

 @Test
 public void testSetValue() {
 attribute.setValue("newValue");
 String newValue = attribute.getValue();
 assertEquals("newValue", newValue);
 }

 @Test
 public void testHtml() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 attribute.html(sb, new Document("").outputSettings());
 String html = StringUtil.releaseBuilder(sb);
 assertEquals("key=\"value\"", html);
 }

 @Test
 public void testCreateFromEncoded() {
 String encodedKey = "key=\"value\"";
 Attribute createdAttribute = Attribute.createFromEncoded("key", encodedKey);
 assertEquals("key", createdAttribute.getKey());
 assertEquals("value", createdAttribute.getValue());
 }

 @Test
 public void testIsDataAttribute() {
 assertFalse(attribute.isDataAttribute());
 String dataKey = "data-test";
 Attribute dataAttribute = new Attribute(dataKey, null, null);
 assertTrue(dataAttribute.isDataAttribute());
 }

 @Test
 public void testShouldCollapseAttribute() {
 Document.OutputSettings outputSettings = new Document.OutputSettings();
 outputSettings.syntax(Document.OutputSettings.Syntax.html);
 boolean shouldCollapse = attribute.shouldCollapseAttribute(outputSettings);
 assertFalse(shouldCollapse);
 }

 @Test(expected = SerializationException.class)
 public void testHtmlWithIOException() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 attribute.html(sb, new Document("").outputSettings());
 fail("IOException should be thrown");
 }

 @Test
 public void testEquals() {
 Attribute anotherAttribute = new Attribute("key", "value");
 assertTrue(attribute.equals(anotherAttribute));
 }

 @Test
 public void testNotEquals() {
 Attribute anotherAttribute = new Attribute("key2", "value2");
 assertFalse(attribute.equals(anotherAttribute));
 }

 @Test
 public void testClone() throws CloneNotSupportedException {
 Attribute clonedAttribute = attribute.clone();
 assertEquals(attribute, clonedAttribute);
 }
}