import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;

import static org.junit.Assert.*;

public class AttributeTest {

 private Attributes attributes;

 @Before
 public void setUp() {
 attributes = new Attributes();
 }

 @Test
 public void testCreateAttribute() {
 String key = "class";
 String value = "test";
 Attribute attribute = new Attribute(key, value, attributes);

 assertEquals(key, attribute.getKey());
 assertEquals(value, attribute.getValue());
 assertEquals(attribute, attributes.put(key, value));
 }

 @Test
 public void testCreateAttributeWithParent() {
 String key = "class";
 String value = "test";
 Attribute attribute = new Attribute(key, value, attributes);

 assertEquals(key, attribute.getKey());
 assertEquals(value, attribute.getValue());
 assertEquals(attribute, attributes.get(key));
 }

 @Test
 public void testSetKey() {
 String key = "class";
 String value = "test";
 Attribute attribute = new Attribute(key, value, attributes);

 attribute.setKey("id");
 assertEquals("id", attribute.getKey());
 }

 @Test
 public void testSetValue() {
 String key = "class";
 String oldValue = "test";
 String newValue = "newTest";
 Attribute attribute = new Attribute(key, oldValue, attributes);

 assertEquals(oldValue, attribute.getValue());
 assertEquals(attribute, attributes.get(key));
 attribute.setValue(newValue);
 assertEquals(newValue, attribute.getValue());
 assertEquals(attribute, attributes.get(key));
 }

 @Test
 public void testHtml() throws IOException {
 String key = "class";
 String value = "test";
 Attribute attribute = new Attribute(key, value, attributes);

 StringBuilder expected = StringUtil.borrowBuilder();
 StringUtil.html(key, value, expected, new Document.OutputSettings());
 String actual = attribute.html();
 assertEquals(expected.toString(), actual);
 }

 @Test
 public void testCreateFromEncoded() {
 String unencodedKey = "data-test";
 String encodedValue = "data-test=\"test\"";
 Attribute attribute = Attribute.createFromEncoded(unencodedKey, encodedValue);

 assertEquals(unencodedKey, attribute.getKey());
 assertEquals("test", attribute.getValue());
 }

 @Test
 public void testIsBooleanAttribute() {
 String key = "checked";
 assertTrue(Attribute.isBooleanAttribute(key));
 }

 @Test
 public void testIsDataAttribute() {
 String key = "data-test";
 assertTrue(attribute.isDataAttribute());
 assertTrue(attribute.isDataAttribute(key));
 }

 @Test
 public void testShouldCollapseAttribute() {
 Document.OutputSettings outputSettings = new Document.OutputSettings();
 outputSettings.syntax(Document.OutputSettings.Syntax.html);

 Attribute attribute = new Attribute("checked", null, attributes);
 assertTrue(attribute.shouldCollapseAttribute(outputSettings));
 attribute.setValue("checked");
 assertFalse(attribute.shouldCollapseAttribute(outputSettings));
 }

 @Test
 public void testEquals() {
 Attribute attribute1 = new Attribute("class", "test", attributes);
 Attribute attribute2 = new Attribute("class", "test", attributes);
 Attribute attribute3 = new Attribute("id", "test", attributes);

 assertEquals(attribute1, attribute1);
 assertEquals(attribute1, attribute2);
 assertNotEquals(attribute1, attribute3);
 }

 @Test
 public void testHashCode() {
 Attribute attribute1 = new Attribute("class", "test", attributes);
 Attribute attribute2 = new Attribute("class", "test", attributes);
 Attribute attribute3 = new Attribute("id", "test", attributes);

 int hashCode1 = attribute1.hashCode();
 int hashCode2 = attribute1.hashCode();
 int hashCode3 = attribute3.hashCode();

 assertEquals(hashCode1, hashCode2);
 assertNotEquals(hashCode1, hashCode3);
 }

 @Test
 public void testClone() {
 Attribute attribute = new Attribute("class", "test", attributes);
 Attribute clonedAttribute = attribute.clone();

 assertEquals(attribute, clonedAttribute);
 assertNotSame(attribute, clonedAttribute);
 }
}