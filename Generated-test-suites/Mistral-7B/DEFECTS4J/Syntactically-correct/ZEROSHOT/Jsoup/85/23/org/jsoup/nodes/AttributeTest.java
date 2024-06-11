package test;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import static org.junit.Assert.*;

public class AttributeTest {

 private Attribute attribute;
 private Attributes attributes;

 @Before
 public void setup() {
 attribute = new Attribute("key", "value");
 attributes = new Attributes();
 attributes.put(attribute);
 }

 @Test
 public void testConstructorWithNullKey() {
 assertNull(new Attribute(null, "value"));
 }

 @Test
 public void testConstructorWithEmptyKey() {
 assertNull(new Attribute("", "value"));
 }

 @Test
 public void testConstructorWithEmptyValue() {
 Attribute a = new Attribute("key", "");
 assertEquals("key", a.getKey());
 assertNull(a.getValue());
 }

 @Test
 public void testSetKey() {
 attribute.setKey("newKey");
 assertEquals("newKey", attribute.getKey());
 }

 @Test(expected = NullPointerException.class)
 public void testSetKeyWithNullKey() {
 attribute.setKey(null);
 }

 @Test
 public void testSetValue() {
 attribute.setValue("newValue");
 assertEquals("newValue", attribute.getValue());
 }

 @Test(expected = NullPointerException.class)
 public void testSetValueWithNullValue() {
 attribute.setValue(null);
 }

 @Test
 public void testHtml() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 attribute.html(sb, new Document("").outputSettings());
 String html = StringUtil.releaseBuilder(sb);
 assertEquals("key=\"value\"", html);
 }

 @Test
 public void testIsDataAttribute() {
 assertFalse(attribute.isDataAttribute());
 Attribute dataAttribute = new Attribute("data-test", null);
 assertTrue(dataAttribute.isDataAttribute());
 }

 @Test
 public void testShouldCollapseAttribute() throws IOException {
 Document.OutputSettings out = new Document.OutputSettings();
 assertTrue(attribute.shouldCollapseAttribute(out));
 assertTrue(attribute.shouldCollapseAttribute("key", "", out));
 assertFalse(attribute.shouldCollapseAttribute("key1", "value1", out));
 assertFalse(attribute.shouldCollapseAttribute("key2", null, out));
 }

 @Test
 public void testCreateFromEncoded() {
 Attribute a = Attribute.createFromEncoded("key", "key=\"value\"");
 assertEquals("key", a.getKey());
 assertEquals("value", a.getValue());
 }

 @Test
 public void testIsBooleanAttribute() {
 assertFalse(attribute.isBooleanAttribute());
 Attribute booleanAttribute = new Attribute("checked", null);
 assertTrue(booleanAttribute.isBooleanAttribute());
 }

 @Test
 public void testEquals() {
 Attribute a = new Attribute("key1", "value1");
 Attribute b = new Attribute("key1", "value1");
 Attribute c = new Attribute("key2", "value2");
 assertTrue(a.equals(a));
 assertTrue(a.equals(b));
 assertFalse(a.equals(c));
 }

 @Test
 public void testHashCode() {
 Attribute a = new Attribute("key1", "value1");
 Attribute b = new Attribute("key1", "value1");
 Attribute c = new Attribute("key2", "value2");
 int hashA = a.hashCode();
 int hashB = b.hashCode();
 int hashC = c.hashCode();
 assertEquals(hashA, hashB);
 assertNotSame(hashA, hashC);
 }

 @Test
 public void testClone() throws CloneNotSupportedException {
 Attribute cloned = attribute.clone();
 assertEquals(attribute.getKey(), cloned.getKey());
 assertEquals(attribute.getValue(), cloned.getValue());
 }

 @Test
 public void testPutAndGetFromAttributes() {
 attributes.put(attribute);
 Attribute retrieved = attributes.get("key");
 assertEquals(attribute.getKey(), retrieved.getKey());
 assertEquals(attribute.getValue(), retrieved.getValue());
 }
}