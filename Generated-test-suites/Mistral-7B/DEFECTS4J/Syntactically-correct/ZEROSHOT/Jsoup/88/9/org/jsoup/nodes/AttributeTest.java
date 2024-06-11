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

 @Test(expected = NullPointerException.class)
 public void testAttributeCreationWithNullKey() {
 Attribute attribute = new Attribute(null, "value");
 }

 @Test(expected = NullPointerException.class)
 public void testAttributeCreationWithNullValue() {
 Attribute attribute = new Attribute("key", null);
 }

 @Test
 public void testAttributeCreationWithParent() {
 Attribute attribute = new Attribute("key", "value", new Attributes(new Attribute[0]));
 assertEquals("key", attribute.getKey());
 assertEquals("value", attribute.getValue());
 }

 @Test
 public void testSetKey() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setKey("newKey");
 assertEquals("newKey", attribute.getKey());
 }

 @Test(expected = NullPointerException.class)
 public void testSetKeyWithNullKey() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setKey(null);
 }

 @Test
 public void testSetKeyWithEmptyString() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setKey("");
 assertEquals("", attribute.getKey());
 }

 @Test
 public void testSetValue() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setValue("newValue");
 assertEquals("newValue", attribute.getValue());
 }

 @Test(expected = NullPointerException.class)
 public void testSetValueWithNullValue() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setValue(null);
 }

 @Test
 public void testHtml() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 Attribute attribute = new Attribute("key", "value");
 attribute.html(sb, new Document("").outputSettings());
 assertEquals("key=\"value\"", StringUtil.releaseBuilder(sb).toString());
 }

 @Test
 public void testHtmlCollapsed() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 Attribute booleanAttribute = new Attribute("checked", null);
 booleanAttribute.html(sb, new Document("").outputSettings());
 assertEquals("checked", StringUtil.releaseBuilder(sb).toString());
 }

 @Test
 public void testIsDataAttribute() {
 Attribute dataAttribute = new Attribute("data-test", "test");
 assertTrue(dataAttribute.isDataAttribute());
 Attribute nonDataAttribute = new Attribute("key", "value");
 assertFalse(nonDataAttribute.isDataAttribute());
 }

 @Test
 public void testShouldCollapseAttribute() throws IOException {
 Document.OutputSettings outputSettings = new Document.OutputSettings();
 Attribute booleanAttribute = new Attribute("checked", null);
 assertTrue(booleanAttribute.shouldCollapseAttribute(outputSettings));
 outputSettings.syntax(Document.OutputSettings.Syntax.xml);
 Attribute booleanAttributeXml = new Attribute("checked", null);
 assertFalse(booleanAttributeXml.shouldCollapseAttribute(outputSettings));
 }

 @Test
 public void testIsBooleanAttribute() {
 Attribute booleanAttribute = new Attribute("checked", null);
 assertTrue(booleanAttribute.isBooleanAttribute());
 Attribute nonBooleanAttribute = new Attribute("key", "value");
 assertFalse(nonBooleanAttribute.isBooleanAttribute());
 }

 @Test
 public void testIsBooleanAttributeWithEncodedKey() {
 Attribute booleanAttributeEncoded = Attribute.createFromEncoded("checked", "");
 assertTrue(booleanAttributeEncoded.isBooleanAttribute());
 Attribute nonBooleanAttributeEncoded = Attribute.createFromEncoded("key", "value");
 assertFalse(nonBooleanAttributeEncoded.isBooleanAttribute());
 }

 @Test
 public void testEquals() {
 Attribute attribute1 = new Attribute("key1", "value1");
 Attribute attribute2 = new Attribute("key2", "value2");
 Attribute attribute3 = new Attribute("key1", "value1");
 Attribute cloneAttribute = attribute1.clone();
 assertTrue(attribute1.equals(attribute3));
 assertTrue(attribute1.equals(cloneAttribute));
 assertFalse(attribute1.equals(attribute2));
 }

 @Test
 public void testHashCode() {
 Attribute attribute1 = new Attribute("key1", "value1");
 Attribute attribute2 = new Attribute("key2", "value2");Attribute cloneAttribute = attribute1.clone();
 int hashCode1 = attribute1.hashCode();
 int hashCode2 = attribute2.hashCode();
 int hashCode3 = cloneAttribute.hashCode();
 assertNotEquals(hashCode1, hashCode2);
 assertNotEquals(hashCode1, hashCode3);
 assertNotEquals(hashCode2, hashCode3);
 }
}