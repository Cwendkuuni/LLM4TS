import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeTest {

 @Test
 public void testAttributeCreation() {
 Attribute attribute = new Attribute("key", "value");
 assertNotNull(attribute);
 assertEquals("key", attribute.getKey());
 assertEquals("value", attribute.getValue());
 }

 @Test
 public void testAttributeCreationWithParent() {
 Attributes parent = new Attributes();
 Attribute attribute = new Attribute("key", "value", parent);
 assertNotNull(attribute);
 assertEquals("key", attribute.getKey());
 assertEquals("value", attribute.getValue());
 assertEquals(attribute, parent.get("key"));
 }

 @Test(expected = NullPointerException.class)
 public void testAttributeCreationWithNullKey() {
 Attribute attribute = new Attribute(null, "value");
 }

 @Test(expected = NullPointerException.class)
 public void testAttributeCreationWithNullValue() {
 Attribute attribute = new Attribute("key", null);
 }

 @Test
 public void testSetKey() {
 Attribute attribute = new Attribute("key1", "value");
 attribute.setKey("key2");
 assertEquals("key2", attribute.getKey());
 }

 @Test(expected = NullPointerException.class)
 public void testSetKeyWithNullKey() {
 Attribute attribute = new Attribute("key1", "value");
 attribute.setKey(null);
 }

 @Test
 public void testEquals() {
 Attribute attribute1 = new Attribute("key1", "value1");
 Attribute attribute2 = new Attribute("key1", "value1");
 Attribute attribute3 = new Attribute("key2", "value2");
 assertTrue(attribute1.equals(attribute1));
 assertTrue(attribute1.equals(attribute2));
 assertFalse(attribute1.equals(attribute3));
 }

 @Test
 public void testHashCode() {
 Attribute attribute1 = new Attribute("key1", "value1");
 Attribute attribute2 = new Attribute("key1", "value1");
 Attribute attribute3 = new Attribute("key2", "value2");
 assertEquals(attribute1.hashCode(), attribute1.hashCode());
 assertEquals(attribute1.hashCode(), attribute2.hashCode());
 assertNotEquals(attribute1.hashCode(), attribute3.hashCode());
 }

 @Test
 public void testClone() throws CloneNotSupportedException {
 Attribute attribute = new Attribute("key", "value");
 Attribute clonedAttribute = attribute.clone();
 assertEquals(attribute, clonedAttribute);
 }

 @Test
 public void testBooleanAttributes() {
 Attribute booleanAttribute = new Attribute("checked", "");
 assertTrue(booleanAttribute.isBooleanAttribute());
 Attribute nonBooleanAttribute = new Attribute("key", "value");
 assertFalse(nonBooleanAttribute.isBooleanAttribute());
 }

 @Test
 public void testShouldCollapseAttribute() {
 Document.OutputSettings outputSettings = new Document.OutputSettings();
 Attribute booleanAttribute = new Attribute("checked", "");
 assertTrue(booleanAttribute.shouldCollapseAttribute(outputSettings));
 Attribute nonBooleanAttribute = new Attribute("key", "value");
 assertFalse(nonBooleanAttribute.shouldCollapseAttribute(outputSettings));
 }
}