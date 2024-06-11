import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeTest {

 @Test
 public void testAttributeCreation() {
 Attribute attribute = new Attribute("key", "value");
 Attribute attributeWithParent = new Attribute("key", "value", new Attributes());
 assertEquals("key", attribute.getKey());
 assertEquals("value", attribute.getValue());
 assertNull(attribute.getParent());
 assertEquals("key", attributeWithParent.getKey());
 assertEquals("value", attributeWithParent.getValue());
 assertNotNull(attributeWithParent.getParent());
 }

 @Test(expected = NullPointerException.class)
 public void testNullKey() {
 Attribute attribute = new Attribute(null, "value");
 }

 @Test(expected = NullPointerException.class)
 public void testNullValue() {
 Attribute attribute = new Attribute("key", null);
 }

 @Test
 public void testSetKey() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setKey("newKey");
 assertEquals("newKey", attribute.getKey());
 }

 @Test
 public void testValueEqualToKey() {
 Attribute attribute = new Attribute("key", "key");
 assertTrue(attribute.shouldCollapseAttribute(Attributes.OutputSettings.html()));
 }

 @Test
 public void testValueNotEqualToKey() {
 Attribute attribute = new Attribute("key", "value");
 assertFalse(attribute.shouldCollapseAttribute(Attributes.OutputSettings.html()));
 }

 @Test
 public void testEmptyValue() {
 Attribute attribute = new Attribute("key", "");
 assertFalse(attribute.shouldCollapseAttribute(Attributes.OutputSettings.html()));
 }

 @Test
 public void testBooleanAttributeEmptyValue() {
 Attribute attribute = new Attribute("checked", "");
 assertTrue(attribute.shouldCollapseAttribute(Attributes.OutputSettings.html()));
 }

 @Test
 public void testBooleanAttributeSameValue() {
 Attribute attribute = new Attribute("checked", "checked");
 assertTrue(attribute.shouldCollapseAttribute(Attributes.OutputSettings.html()));
 }

 @Test
 public void testBooleanAttributeDifferentValue() {
 Attribute attribute = new Attribute("checked", "notchecked");
 assertFalse(attribute.shouldCollapseAttribute(Attributes.OutputSettings.html()));
 }

 @Test
 public void testIsBooleanAttribute() {
 Attribute attribute = new Attribute("checked", null);
 assertTrue(attribute.isBooleanAttribute());
 }

 @Test
 public void testIsNotBooleanAttribute() {
 Attribute attribute = new Attribute("key", "value");
 assertFalse(attribute.isBooleanAttribute());
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
 int hashCode1 = attribute1.hashCode();
 int hashCode2 = attribute1.hashCode();
 int hashCode3 = attribute3.hashCode();
 assertEquals(hashCode1, hashCode2);
 assertNotEquals(hashCode1, hashCode3);
 }

 @Test
 public void testClone() throws CloneNotSupportedException {
 Attribute attribute = new Attribute("key", "value");
 Attribute clonedAttribute = attribute.clone();
 assertEquals(attribute, clonedAttribute);
 }
}