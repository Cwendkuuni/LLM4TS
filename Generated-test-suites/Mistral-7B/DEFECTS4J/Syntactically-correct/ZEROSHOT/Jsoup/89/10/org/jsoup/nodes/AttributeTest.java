import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class AttributeTest {

 @BeforeAll
 static void setUp() {
 // Initialize any static variables or perform any setup before tests run
 }

 @AfterAll
 static void tearDown() {
 // Perform any cleanup after tests run
 }

 @Test
 public void testAttributeCreation() {
 // Test constructor with key and value
 Attribute attribute = new Attribute("key", "value");
 assertEquals("key", attribute.getKey());
 assertEquals("value", attribute.getValue());

 // Test constructor with key, value, and parent
 Attribute attributeWithParent = new Attribute("key", "value", null);
 Attribute parent = new Attributes();
 attributeWithParent = new Attribute("key", "value", parent);
 assertEquals("key", attributeWithParent.getKey());
 assertEquals("value", attributeWithParent.getValue());
 assertEquals(attributeWithParent, parent.get("key"));
 }

 @Test
 public void testSettingKey() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setKey("newKey");
 assertEquals("newKey", attribute.getKey());
 }

 @Test
 public void testSettingValue() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setValue("newValue");
 assertEquals("newValue", attribute.getValue());
 }

 @Test
 public void testHtmlMethod() throws IOException {
 Attribute attribute = new Attribute("key", "value");
 StringBuilder sb = StringUtil.borrowBuilder();
 attribute.html(sb, new Document("").outputSettings());
 String expectedHtml = "key=\"value\"";
 assertEquals(expectedHtml, StringUtil.releaseBuilder(sb));
 }

 @Test
 public void testToStringMethod() {
 Attribute attribute = new Attribute("key", "value");
 assertEquals(attribute.html(), attribute.toString());
 }

 @Test
 public void testCreateFromEncoded() {
 String encodedKey = "data-my-attribute";
 String encodedValue = "data-value";
 Attribute attribute = Attribute.createFromEncoded("my-attribute", encodedValue);
 assertEquals("my-attribute", attribute.getKey());
 assertEquals("data-value", attribute.getValue());
 }

 @Test
 public void testIsDataAttribute() {
 Attribute attribute = new Attribute("key", "value");
 assertTrue(attribute.isDataAttribute());
 Attribute dataAttribute = new Attribute("data-key", null);
 assertTrue(dataAttribute.isDataAttribute());
 Attribute nonDataAttribute = new Attribute("key", "value");
 assertFalse(nonDataAttribute.isDataAttribute());
 }

 @Test
 public void testShouldCollapseAttribute() throws IOException {
 Document.OutputSettings settings = new Document.OutputSettings();
 Attribute attribute = new Attribute("key", "value");
 assertTrue(attribute.shouldCollapseAttribute(settings));
 settings.syntax(Document.OutputSettings.Syntax.html);
 attribute.setValue("value");
 assertFalse(attribute.shouldCollapseAttribute(settings));
 settings.syntax(Document.OutputSettings.Syntax.xml);
 assertTrue(attribute.shouldCollapseAttribute(settings));
 }

 @Test
 public void testIsBooleanAttribute() {
 Attribute booleanAttribute = new Attribute("checked", null);
 assertTrue(booleanAttribute.isBooleanAttribute());
 Attribute nonBooleanAttribute = new Attribute("key", "value");
 assertFalse(nonBooleanAttribute.isBooleanAttribute());
 }

 @Test
 public void testEqualsMethod() {
 Attribute attribute1 = new Attribute("key1", "value1");
 Attribute attribute2 = new Attribute("key1", "value1");
 Attribute attribute3 = new Attribute("key2", "value2");
 assertTrue(attribute1.equals(attribute1));
 assertTrue(attribute1.equals(attribute2));
 assertFalse(attribute1.equals(attribute3));
 }

 @Test
 public void testHashCodeMethod() {
 Attribute attribute1 = new Attribute("key1", "value1");
 Attribute attribute2 = new Attribute("key1", "value1");
 Attribute attribute3 = new Attribute("key2", "value2");
 int hashCode1 = attribute1.hashCode();
 int hashCode2 = attribute2.hashCode();
 int hashCode3 = attribute3.hashCode();
 assertEquals(hashCode1, hashCode2);
 assertNotEquals(hashCode1, hashCode3);
 }

 @Test
 public void testCloneMethod() throws CloneNotSupportedException {
 Attribute attribute = new Attribute("key", "value");
 Attribute clonedAttribute = attribute.clone();
 assertEquals(attribute, clonedAttribute);
 assertNotSame(attribute, clonedAttribute);
 }
}