import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class AttributeTest {

 private Attribute attribute;
 private Attributes attributes;

 @Before
 public void setUp() {
 attributes = new Attributes();
 attribute = new Attribute("key", "value");
 attribute.setParent(attributes);
 }

 @Test
 public void testConstructorWithKeyAndValue() {
 assertEquals("key", attribute.getKey());
 assertEquals("value", attribute.getValue());
 }

 @Test
 public void testConstructorWithKeyValueAndParent() {
 Attribute attributeWithParent = new Attribute("key", "value", attributes);
 assertEquals("key", attributeWithParent.getKey());
 assertEquals("value", attributeWithParent.getValue());
 assertSame(attributes, attributeWithParent.getParent());
 }

 @Test
 public void testGetKey() {
 assertEquals("key", attribute.getKey());
 }

 @Test
 public void testSetKey() {
 String newKey = "newKey";
 attribute.setKey(newKey);
 assertEquals(newKey, attribute.getKey());
 }

 @Test
 public void testGetValue() {
 assertEquals("value", attribute.getValue());
 }

 @Test
 public void testSetValue() {
 String newValue = "newValue";
 String oldValue = attribute.getValue();
 attribute.setValue(newValue);
 assertEquals(newValue, attribute.getValue());
 assertEquals(oldValue, attributes.get("key"));
 }

 @Test
 public void testHtml() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 attribute.html(sb, new Document("").outputSettings());
 assertEquals("key=\"value\"", StringUtil.releaseBuilder(sb).trim());
 }

 @Test
 public void testToString() {
 assertEquals("key=\"value\"", attribute.toString().trim());
 }

 @Test
 public void testCreateFromEncoded() {
 Attribute attributeFromEncoded = Attribute.createFromEncoded("key", "key=\"value\"");
 assertEquals("key", attributeFromEncoded.getKey());
 assertEquals("value", attributeFromEncoded.getValue());
 }

 @Test
 public void testIsDataAttribute() {
 assertFalse(attribute.isDataAttribute());
 String dataKey = "data-test";
 Attribute dataAttribute = new Attribute(dataKey, null, attributes);
 assertTrue(dataAttribute.isDataAttribute());
 }

 @Test
 public void testShouldCollapseAttribute() {
 Document.OutputSettings outputSettings = new Document.OutputSettings();
 assertTrue(attribute.shouldCollapseAttribute(outputSettings));
 }

 @Test
 public void testEquals() {
 Attribute anotherAttribute = new Attribute("key", "value");
 assertTrue(attribute.equals(anotherAttribute));
 }

 @Test
 public void testClone() {
 Attribute clonedAttribute = attribute.clone();
 assertEquals(attribute, clonedAttribute);
 }

 @Test(expected = RuntimeException.class)
 public void testCloneFailure() {
 try {
 Attribute clonedAttribute = (Attribute) attribute.clone();
 } catch (CloneNotSupportedException e) {
 throw new RuntimeException("Clone not supported exception should not be thrown", e);
 }
 }

 @Test
 public void testIsBooleanAttribute() {
 assertFalse(attribute.isBooleanAttribute());
 String booleanKey = "checked";
 Attribute booleanAttribute = new Attribute(booleanKey, null, attributes);
 assertTrue(booleanAttribute.isBooleanAttribute());
 }
}