import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeTest {

 @Test
 public void testAttributeCreation() {
 Attribute attribute = new Attribute("testKey", "testValue");
 assertEquals("testKey", attribute.getKey());
 assertEquals("testValue", attribute.getValue());
 }

 @Test
 public void testAttributeCreationWithParent() {
 Attributes attributes = new Attributes();
 Attribute attribute = new Attribute("testKey", "testValue", attributes);
 assertEquals("testKey", attribute.getKey());
 assertEquals("testValue", attribute.getValue());
 assertEquals(attributes, attribute.getParent());
 }

 @Test
 public void testSettingKey() {
 Attribute attribute = new Attribute("testKey", "testValue");
 attribute.setKey("newTestKey");
 assertEquals("newTestKey", attribute.getKey());
 }

 @Test(expected = NullPointerException.class)
 public void testSettingNullKey() {
 Attribute attribute = new Attribute("testKey", "testValue");
 attribute.setKey(null);
 }

 @Test
 public void testSettingValue() {
 Attribute attribute = new Attribute("testKey", "testValue");
 attribute.setValue("newTestValue");
 assertEquals("newTestValue", attribute.getValue());
 }

 @Test(expected = NullPointerException.class)
 public void testSettingNullValue() {
 Attribute attribute = new Attribute("testKey", "testValue");
 attribute.setValue(null);
 }

 @Test
 public void testHTML() throws IOException {
 Attribute attribute = new Attribute("testKey", "testValue");
 StringBuilder sb = StringUtil.borrowBuilder();
 attribute.html(sb, new Document("").outputSettings());
 assertEquals("testKey=\"testValue\"", StringUtil.releaseBuilder(sb).toString());
 }

 @Test
 public void testIsBooleanAttribute() {
 assertTrue(Attribute.isBooleanAttribute("checked"));
 assertFalse(Attribute.isBooleanAttribute("testKey"));
 }

 @Test
 public void testShouldCollapseAttribute() {
 Document.OutputSettings outputSettings = new Document.OutputSettings();
 Attribute attribute = new Attribute("checked", null);
 assertTrue(attribute.shouldCollapseAttribute(outputSettings));
 Attribute attribute1 = new Attribute("testKey", "testValue");
 assertFalse(attribute1.shouldCollapseAttribute(outputSettings));
 }

 @Test
 public void testEquals() {
 Attribute attribute1 = new Attribute("testKey1", "testValue1");
 Attribute attribute2 = new Attribute("testKey1", "testValue1");
 Attribute attribute3 = new Attribute("testKey2", "testValue2");
 assertTrue(attribute1.equals(attribute1));
 assertTrue(attribute1.equals(attribute2));
 assertFalse(attribute1.equals(attribute3));
 }

 @Test
 public void testHashCode() {
 Attribute attribute1 = new Attribute("testKey1", "testValue1");
 Attribute attribute2 = new Attribute("testKey1", "testValue1");
 Attribute attribute3 = new Attribute("testKey2", "testValue2");
 int hashCode1 = attribute1.hashCode();
 int hashCode2 = attribute2.hashCode();
 int hashCode3 = attribute3.hashCode();
 assertEquals(hashCode1, hashCode2);
 assertNotEquals(hashCode1, hashCode3);
 }

 @Test
 public void testClone() throws CloneNotSupportedException {
 Attribute attribute = new Attribute("testKey", "testValue");
 Attribute clonedAttribute = attribute.clone();
 assertEquals(attribute, clonedAttribute);
}

 @Test(expected = CloneNotSupportedException.class)
 public void testCloneException() throws CloneNotSupportedException {
 Attribute attribute = new Attribute("testKey", "testValue");
 Attribute clonedAttribute = (Attribute) attribute.clone();
 }
}