import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.helper.Validate;
import static org.junit.Assert.*;

public class AttributeTest {

 private Attributes attributes;

 @Before
 public void setUp() {
 attributes = new Attributes();
 }

 @Test
 public void testAttributeCreation() {
 // Test creating an attribute with a valid key and value
 Attribute attribute = new Attribute("key", "value");
 assertEquals("key", attribute.getKey());
 assertEquals("value", attribute.getValue());
 assertEquals(attribute, attributes.put("key", attribute));

 // Test creating an attribute with a valid key, value, and parent
 Attribute attributeParent = new Attribute("parentKey", "parentValue", attributes);
 attribute = new Attribute("key", "value", attributeParent);
 assertEquals("key", attribute.getKey());
 assertEquals("value", attribute.getValue());
 assertEquals(attribute, attributes.put("key", attribute));
 assertEquals(attributeParent, attribute.getParent());
 }

 @Test
 public void testAttributeKeySetting() {
 // Test setting the key of an existing attribute
 Attribute attribute = new Attribute("key", "value");
 assertEquals("key", attribute.getKey());
 attribute.setKey("newKey");
 assertEquals("newKey", attribute.getKey());
 assertEquals(attribute, attributes.put("newKey", attribute));
 // Test setting the key of a non-existent attribute
 attribute = new Attribute("key", "value");
 Attribute newAttribute = new Attribute("newKey", "newValue");
 assertEquals(null, attributes.get("newKey"));
 attribute.setKey("newKey");
 assertEquals(newAttribute, attributes.get("newKey"));
 }

 @Test
 public void testAttributeValueSetting() {
 // Test setting the value of an existing attribute
 Attribute attribute = new Attribute("key", "value");
 assertEquals("value", attribute.getValue());
 attribute.setValue("newValue");
 assertEquals("newValue", attribute.getValue());
 assertEquals(attribute, attributes.put("key", attribute));
 // Test setting the value of a non-existent attribute
 attribute = new Attribute("key", "value");
 Attribute newAttribute = new Attribute("key", "newValue");
 assertEquals(null, attributes.get("key"));
 attribute.setValue("newValue");
 assertEquals(newAttribute, attributes.get("key"));
 }

 @Test
 public void testAttributeHtml() throws IOException {
 // Test HTML representation of an attribute
 Attribute attribute = new Attribute("key", "value");
 StringBuilder sb = StringUtil.borrowBuilder();
 attribute.html(sb, new Document("").outputSettings());
 assertEquals("key=\"value\"", sb.toString());

 // Test HTML representation of a data attribute
 attribute = new Attribute("data-key", "value");
 sb = StringUtil.borrowBuilder();
 attribute.html(sb, new Document("").outputSettings());
 assertEquals("data-key=\"value\"", sb.toString());
 }

 @Test(expected = SerializationException.class)
 public void testAttributeSerializationException() throws IOException {
 Attribute attribute = new Attribute("key", "value");
 StringBuilder sb = StringUtil.borrowBuilder();
 attribute.html(sb, new Document("").outputSettings());
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
 assertEquals("key", clonedAttribute.getKey());
 assertEquals("value", clonedAttribute.getValue());
 assertNotSame(attribute, clonedAttribute);
 }
}