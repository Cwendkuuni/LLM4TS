import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class AttributeTest {

 @Test
 public void testAttributeCreation() {
 Attribute attribute = new Attribute("key", "value");
 assertEquals("key", attribute.getKey());
 assertEquals("value", attribute.getValue());
 assertNull(attribute.getParent());

 Attribute attributeWithParent = new Attribute("key", "value", new Attributes());
 assertEquals("key", attributeWithParent.getKey());
 assertEquals("value", attributeWithParent.getValue());
 assertSame(attributeWithParent.getParent(), attributeWithParent.getParent());
 }

 @Test
 public void testSettingKey() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setKey("newKey");
 assertEquals("newKey", attribute.getKey());

 Attribute attributeWithParent = new Attribute("key", "value", new Attributes());
 attributeWithParent.setKey("newKey");
 assertEquals("newKey", attributeWithParent.getKey());
 }

 @Test
 public void testSettingValue() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setValue("newValue");
 assertEquals("newValue", attribute.getValue());

 Attribute attributeWithParent = new Attribute("key", "value", new Attributes());
 attributeWithParent.setValue("newValue");
 assertEquals("newValue", attributeWithParent.getValue());
 }

 @Test
 public void testHtml() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 Attribute attribute = new Attribute("key", "value");
 attribute.html(sb, new Document("").outputSettings());
 assertEquals("key=\"value\"", StringUtil.releaseBuilder(sb));

 sb = StringUtil.borrowBuilder();
 Attribute booleanAttribute = new Attribute("checked", "");
 booleanAttribute.html(sb, new Document("").outputSettings());
 assertTrue(StringUtil.releaseBuilder(sb).isEmpty());
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
 Attribute attribute4 = new Attribute("key1", "value1");

 assertEquals(attribute1.hashCode(), attribute1.hashCode());
 assertEquals(attribute1.hashCode(), attribute2.hashCode());
 assertEquals(attribute1.hashCode(), attribute4.hashCode());
 assertNotEquals(attribute1.hashCode(), attribute3.hashCode());
 }

 @Test
 public void testClone() throws CloneNotSupportedException {
 Attribute attribute = new Attribute("key", "value");
 Attribute clonedAttribute = attribute.clone();
 assertEquals(attribute, clonedAttribute);
 assertEquals(attribute.getClass(), clonedAttribute.getClass());
 assertNotSame(attribute, clonedAttribute);
 }

 @Test
 public void testIsBooleanAttribute() {
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