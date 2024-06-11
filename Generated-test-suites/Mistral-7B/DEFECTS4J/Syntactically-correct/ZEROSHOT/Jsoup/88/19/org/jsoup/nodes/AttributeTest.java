import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeTest {

 @Test
 public void testAttributeCreation() {
 Attribute attribute = new Attribute("key", "value");
 Attribute attributeWithParent = new Attribute("key", "value", null);
 Attribute attributeWithParentAndKeyChange = new Attribute("newKey", "value", null);
 Attribute attributeWithParentAndValueChange = new Attribute("key", "newValue", null);

 assertEquals("key", attribute.getKey());
 assertEquals("value", attribute.getValue());
 assertNull(attribute.getParent());

 assertEquals("key", attributeWithParent.getKey());
 assertEquals("value", attributeWithParent.getValue());
 assertNull(attributeWithParent.getParent());

 assertEquals("newKey", attributeWithParentAndKeyChange.getKey());
 assertEquals("value", attributeWithParentAndKeyChange.getValue());
 assertNull(attributeWithParentAndKeyChange.getParent());

 assertEquals("key", attributeWithParentAndValueChange.getKey());
 assertEquals("newValue", attributeWithParentAndValueChange.getValue());
 assertNull(attributeWithParentAndValueChange.getParent());
 }

 @Test
 public void testAttributeKeySetter() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setKey("newKey");

 assertEquals("newKey", attribute.getKey());
 }

 @Test(expected = NullPointerException.class)
 public void testAttributeKeySetterNull() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setKey(null);
 }

 @Test
 public void testAttributeValueSetter() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setValue("newValue");

 assertEquals("value", attribute.getValue());
 assertEquals("newValue", attribute.setValue("newValue2"));
 assertEquals("newValue2", attribute.getValue());
 }

 @Test(expected = NullPointerException.class)
 public void testAttributeValueSetterNull() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setValue(null);
 }

 @Test
 public void testAttributeHTML() throws IOException {
 Attribute attribute = new Attribute("key", "value");
 StringBuilder sb = StringUtil.borrowBuilder();
 attribute.html(sb, new Document("").outputSettings());
 String html = StringUtil.releaseBuilder(sb);

 assertEquals("key=\"value\"", html);
 }

 @Test
 public void testAttributeEquals() {
 Attribute attribute1 = new Attribute("key1", "value1");
 Attribute attribute2 = new Attribute("key1", "value1");
 Attribute attribute3 = new Attribute("key2", "value2");

 assertTrue(attribute1.equals(attribute1));
 assertTrue(attribute1.equals(attribute2));
 assertFalse(attribute1.equals(attribute3));
 }

 @Test
 public void testAttributeClone() {
 Attribute attribute = new Attribute("key", "value");
 Attribute clonedAttribute = attribute.clone();

 assertEquals(attribute, clonedAttribute);
 assertNotSame(attribute, clonedAttribute);
 assertEquals("key", clonedAttribute.getKey());
 assertEquals("value", clonedAttribute.getValue());
 assertNull(clonedAttribute.getParent());
 }
}