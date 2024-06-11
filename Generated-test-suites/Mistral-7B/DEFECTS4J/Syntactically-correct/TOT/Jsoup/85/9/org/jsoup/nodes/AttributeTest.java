import org.junit.Test;
import org.jsoup.nodes.Attribute;
import static org.junit.Assert.*;

public class AttributeTest {

 @Test
 public void testAttributeCreation() {
 Attribute attribute = new Attribute("key", "value");
 assertEquals("key", attribute.getKey());
 assertEquals("value", attribute.getValue());
 }

 @Test
 public void testAttributeCreationWithParent() {
 Attributes parent = new Attributes(); // Assuming Attributes is a class that implements Map interface
 Attribute attribute = new Attribute("key", "value", parent);
 assertEquals("key", attribute.getKey());
 assertEquals("value", attribute.getValue());
 // Check if the attribute is added to the parent Attributes
 assertEquals(1, parent.size());
 assertEquals("key", parent.keySet().iterator().next());
 assertEquals("value", parent.get("key"));
 }

 @Test
 public void testKeySetter() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setKey("newKey");
 assertEquals("newKey", attribute.getKey());
 }

 @Test(expected = NullPointerException.class)
 public void testNullKeySetter() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setKey(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyKeySetter() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setKey("");
 }

 @Test
 public void testValueSetter() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setValue("newValue");
 assertEquals("newValue", attribute.getValue());
 }

 @Test(expected = NullPointerException.class)
 public void testNullValueSetter() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setValue(null);
 }

 @Test
 public void testHtmlMethod() throws IOException {
 Document document = new Document("");
 Attributes attributes = new Attributes();
 attributes.put(new Attribute("key", "value"), document);
 attribute.html(document.bodyAppender(), document.outputSettings());
 String actualHtml = document.body().html();
 String expectedHtml = "key=\"value\"";
 assertEquals(expectedHtml, actualHtml);
 }

 @Test
 public void testShouldCollapseAttribute() {
 Document.OutputSettings outputSettings = new Document.OutputSettings();
 assertTrue(attribute.shouldCollapseAttribute(outputSettings));
 }

 @Test
 public void testShouldCollapseAttributeWithEmptyValue() {
 Document.OutputSettings outputSettings = new Document.OutputSettings();
 assertTrue(attribute.shouldCollapseAttribute(outputSettings));
 attribute.setValue("");
 assertTrue(attribute.shouldCollapseAttribute(outputSettings));
 }

 @Test
 public void testShouldCollapseAttributeWithNonEmptyValue() {
 Document.OutputSettings outputSettings = new Document.OutputSettings();
 assertFalse(attribute.shouldCollapseAttribute(outputSettings));
 }

 @Test
 public void testShouldCollapseAttributeWithBooleanAttribute() {
 Document.OutputSettings outputSettings = new Document.OutputSettings();
 String booleanAttributeKey = "checked";
 Attribute booleanAttribute = new Attribute(booleanAttributeKey, null);
 assertTrue(booleanAttribute.shouldCollapseAttribute(outputSettings));
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
 int hashCode2 = attribute2.hashCode();
 int hashCode3 = attribute3.hashCode();
 assertEquals(hashCode1, hashCode2); // As they are equal, hash codes should be the same
 assertNotEquals(hashCode1, hashCode3);
 }

 @Test
 public void testClone() {
 Attribute originalAttribute = new Attribute("key", "value");
 Attribute clonedAttribute = originalAttribute.clone();
 assertEquals(originalAttribute, clonedAttribute);
 }
}