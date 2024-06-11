import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import static org.junit.Assert.*;

public class AttributeTest {

 private Attributes attributes;

 @Before
 public void setUp() {
 attributes = new Attributes();
 }

 /**
 * Test constructor with unencoded key and value.
 */
 @Test
 public void testAttributeCreationUnencodedKeyValue() {
 Attribute attribute = new Attribute("key", "value");
 assertEquals("key", attribute.getKey());
 assertEquals("value", attribute.getValue());
 }

 /**
 * Test constructor with unencoded key, value, and parent Attributes.
 */
 @Test
 public void testAttributeCreationUnencodedKeyValueParent() {
 Attribute attribute = new Attribute("key", "value", attributes);
 assertEquals("key", attribute.getKey());
 assertEquals("value", attribute.getValue());
 assertEquals(attributes, attribute.parent);
 }

 /**
 * Test equals method with same object.
 */
 @Test
 public void testEqualsSameObject() {
 Attribute attribute1 = new Attribute("key", "value");
 Attribute attribute2 = attribute1;
 assertTrue(attribute1.equals(attribute2));
 }

 /**
 * Test equals method with different objects.
 */
 @Test
 public void testEqualsDifferentObjects() {
 Attribute attribute1 = new Attribute("key1", "value1");
 Attribute attribute2 = new Attribute("key2", "value2");
 assertFalse(attribute1.equals(attribute2));
 }

 /**
 * Test equals method with same key and value.
 */
 @Test
 public void testEqualsSameKeyValue() {
 Attribute attribute1 = new Attribute("key", "value");
 Attribute attribute2 = new Attribute("key", "value");
 assertTrue(attribute1.equals(attribute2));
 }

 /**
 * Test equals method with different keys and same value.
 */
 @Test
 public void testEqualsDifferentKeysSameValue() {
 Attribute attribute1 = new Attribute("key1", "value");
 Attribute attribute2 = new Attribute("key2", "value");
 assertFalse(attribute1.equals(attribute2));
 }

 /**
 * Test equals method with same key and different values.
 */
 @Test
 public void testEqualsSameKeyDifferentValues() {
 Attribute attribute1 = new Attribute("key", "value1");
 Attribute attribute2 = new Attribute("key", "value2");
 assertFalse(attribute1.equals(attribute2));
 }

 /**
 * Test equals method with different keys and different values.
 */
 @Test
 public void testEqualsDifferentKeysDifferentValues() {
 Attribute attribute1 = new Attribute("key1", "value1");
 Attribute attribute2 = new Attribute("key2", "value2");
 assertFalse(attribute1.equals(attribute2));
 }

 /**
 * Test hashCode method with same object.
 */
 @Test
 public void testHashCodeSameObject() {
 Attribute attribute1 = new Attribute("key", "value");
 Attribute attribute2 = attribute1;
 assertEquals(attribute1.hashCode(), attribute2.hashCode());
 }

 /**
 * Test hashCode method with different objects.
 */
 @Test
 public void testHashCodeDifferentObjects() {
 Attribute attribute1 = new Attribute("key1", "value1");
 Attribute attribute2 = new Attribute("key2", "value2");
 assertNotEquals(attribute1.hashCode(), attribute2.hashCode());
 }

 /**
 * Test hashCode method with same key and same value.
 */
 @Test
 public void testHashCodeSameKeySameValue() {
 Attribute attribute1 = new Attribute("key", "value");
 Attribute attribute2 = new Attribute("key", "value");
 assertEquals(attribute1.hashCode(), attribute2.hashCode());
 }

 /**
 * Test hashCode method with same key and different values.
 */
 @Test
 public void testHashCodeSameKeyDifferentValues() {
 Attribute attribute1 = new Attribute("key", "value1");
 Attribute attribute2 = new Attribute("key", "value2");
 assertNotEquals(attribute1.hashCode(), attribute2.hashCode());
 }

 /**
 * Test hashCode method with different keys and same value.
 */
 @Test
 public void testHashCodeDifferentKeysSameValue() {
 Attribute attribute1 = new Attribute("key1", "value");
 Attribute attribute2 = new Attribute("key2", "value");
 assertNotEquals(attribute1.hashCode(), attribute2.hashCode());
 }

 /**
 * Test hashCode method with different keys and different values.
 */
 @Test
 public void testHashCodeDifferentKeysDifferentValues() {
 Attribute attribute1 = new Attribute("key1", "value1");
 Attribute attribute2 = new Attribute("key2", "value2");
 assertNotEquals(attribute1.hashCode(), attribute2.hashCode());
 }

 /**
 * Test clone method.
 */
 @Test
 public void testClone() {
 Attribute attribute = new Attribute("key", "value");
 Attribute clonedAttribute = attribute.clone();
 assertEquals("key", clonedAttribute.getKey());
 assertEquals("value", clonedAttribute.getValue());
 assertNotSame(attribute, clonedAttribute);
 }

 /**
 * Test getKey method.
 */
 @Test
 public void testGetKey() {
 Attribute attribute = new Attribute("key", "value");
 assertEquals("key", attribute.getKey());
 }

 /**
 * Test setKey method.
 */
 @Test
 public void testSetKey() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setKey("newKey");
 assertEquals("newKey", attribute.getKey());
 }

 /**
 * Test getValue method.
 */
 @Test
 public void testGetValue() {
 Attribute attribute = new Attribute("key", "value");
 assertEquals("value", attribute.getValue());
 }

 /**
 * Test setValue method.
 */
 @Test
 public void testSetValue() {
 Attribute attribute = new Attribute("key", "value");
 assertEquals("value", attribute.getValue());
 attribute.setValue("newValue");
 assertEquals("newValue", attribute.getValue());
 }

 /**
 * Test html method with valid key and value.
 */
 @Test
 public void testHtmlValidKeyValue() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 Attribute attribute = new Attribute("key", "value");
 attribute.html(sb, null);
 assertEquals("key=\"value\"", StringUtil.releaseBuilder(sb).toString());
 }

 /**
 * Test html method with empty key and value.
 */
 @Test
 public void testHtmlEmptyKeyValue() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 Attribute attribute = new Attribute(null, null);
 attribute.html(sb, null);
 assertEquals("", StringUtil.releaseBuilder(sb).toString());
 }

 /**
 * Test html method with invalid key and value.
 */
 @Test(expected = IllegalArgumentException.class)
 public void testHtmlInvalidKeyValue() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 Attribute attribute = new Attribute(null, "value");
 attribute.html(sb, null);
 }

 /**
 * Test createFromEncoded method with valid unencoded key and encoded value.
 */
 @Test
 public void testCreateFromEncodedValidUnencodedEncoded() {
 Attribute attribute = Attribute.createFromEncoded("key", "key=\"value\"");
 assertEquals("key", attribute.getKey());
 assertEquals("value", attribute.getValue());
 }

 /**
 * Test createFromEncoded method with invalid unencoded key.
 */
 @Test(expected = IllegalArgumentException.class)
 public void testCreateFromEncodedInvalidUnencoded() {
 Attribute.createFromEncoded(null, "");
 }

 /**
 * Test isDataAttribute method with valid data attribute key.
 */
 @Test
 public void testIsDataAttributeValidDataAttribute() {
 assertTrue(new Attribute("data-foo", "value").isDataAttribute());
 }

 /**
 * Test isDataAttribute method with non-data attribute key.
 */
 @Test
 public void testIsDataAttributeNonDataAttribute() {
 assertFalse(new Attribute("key", "value").isDataAttribute());
 }

 /**
 * Test shouldCollapseAttribute method with boolean attribute and empty value.
 */
 @Test
 public void testShouldCollapseAttributeBooleanAttributeEmptyValue() {
 assertTrue(new Attribute("checked", "").shouldCollapseAttribute(null));
 }

 /**
 * Test shouldCollapseAttribute method with boolean attribute and same value as key.
 */
 @Test
 public void testShouldCollapseAttributeBooleanAttributeSameValueAsKey() {
 assertTrue(new Attribute("checked", "checked").shouldCollapseAttribute(null));
 }

 /**
 * Test shouldCollapseAttribute method with non-boolean attribute.
 */
 @Test
 public void testShouldCollapseAttributeNonBooleanAttribute() {
 assertFalse(new Attribute("key", "value").shouldCollapseAttribute(null));
 }
}