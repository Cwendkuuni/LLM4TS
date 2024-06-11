import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import static org.junit.Assert.*;

public class AttributeTest {

 private Attribute attr;
 private Attributes attributes;

 @Before
 public void setUp() {
 attributes = new Attributes();
 attr = new Attribute("key", "value", attributes);
 }

 /**
 * Test for Attribute constructor with unencoded key and value
 */
 @Test
 public void testAttributeConstructorWithUnencodedKeyAndValue() {
 String key = "key";
 String value = "value";
 Attribute a = new Attribute(key, value);
 assertEquals(key, a.getKey());
 assertEquals(value, a.getValue());
 }

 /**
 * Test for Attribute constructor with unencoded key, value and parent
 */
 @Test
 public void testAttributeConstructorWithUnencodedKeyValueAndParent() {
 String key = "key";
 String value = "value";
 Attributes parent = new Attributes();
 Attribute a = new Attribute(key, value, parent);
 assertEquals(key, a.getKey());
 assertEquals(value, a.getValue());
 assertEquals(parent, a.getParent());
 }

 /**
 * Test for getKey method
 */
 @Test
 public void testGetKey() {
 String key = "key";
 Attribute a = new Attribute(key, "value");
 assertEquals(key, a.getKey());
 }

 /**
 * Test for setKey method
 */
 @Test
 public void testSetKey() {
 String oldKey = "oldKey";
 String newKey = "newKey";
 Attribute a = new Attribute(oldKey, "value");
 a.setKey(newKey);
 assertEquals(newKey, a.getKey());
 assertNotEquals(oldKey, a.getKey());
 }

 /**
 * Test for getValue method
 */
 @Test
 public void testGetValue() {
 String value = "value";
 Attribute a = new Attribute("key", value);
 assertEquals(value, a.getValue());
 }

 /**
 * Test for setValue method
 */
 @Test
 public void testSetValue() {
 String oldValue = "oldValue";
 String newValue = "newValue";
 Attribute a = new Attribute("key", oldValue);
 a.setValue(newValue);
 assertEquals(newValue, a.getValue());
 assertNotEquals(oldValue, a.getValue());
 }

 /**
 * Test for html method
 */
 @Test
 public void testHtml() throws IOException {
 Document doc = new Document();
 StringBuilder sb = StringUtil.borrowBuilder();
 Attribute a = new Attribute("key", "value");
 a.html(sb, doc.outputSettings());
 String expected = "key=\"value\"";
 assertEquals(expected, sb.toString());
 }

 /**
 * Test for toString method
 */
 @Test
 public void testToString() {
 Attribute a = new Attribute("key", "value");
 String expected = "key=\"value\"";
 assertEquals(expected, a.toString());
 }

 /**
 * Test for createFromEncoded method
 */
 @Test
 public void testCreateFromEncoded() {
 String unencodedKey = "key";
 String encodedValue = "key=\"value\"";
 Attribute a = Attribute.createFromEncoded(unencodedKey, encodedValue);
 assertEquals(unencodedKey, a.getKey());
 assertEquals("value", a.getValue());
 }

 /**
 * Test for isDataAttribute method
 */
 @Test
 public void testIsDataAttribute() {
 String key = "data-key";
 Attribute a = new Attribute(key, null);
 assertTrue(a.isDataAttribute());
 }

 /**
 * Test for shouldCollapseAttribute method
 */
 @Test
 public void testShouldCollapseAttribute() throws IOException {
 Document doc = new Document();
 assertTrue(attr.shouldCollapseAttribute(doc.outputSettings()));
 }

 /**
 * Test for isBooleanAttribute deprecated method
 */
 @Test(expected = DeprecationException.class)
 public void testIsBooleanAttributeDeprecated() {
 Attribute a = new Attribute("checked", null);
 a.isBooleanAttribute();
 }

 /**
 * Test for isBooleanAttribute method
 */
 @Test
 public void testIsBooleanAttribute() {
 String key = "checked";
 Attribute a = new Attribute(key, null);
 assertTrue(a.isBooleanAttribute(key));
 }

 /**
 * Test for equals method
 */
 @Test
 public void testEquals() {
 String key1 = "key1";
 String key2 = "key2";
 String value = "value";
 Attribute a1 = new Attribute(key1, value);
 Attribute a2 = new Attribute(key2, value);
 Attribute a3 = new Attribute(key1, value);Attribute a4 = new Attribute(key1, null);
 assertEquals(a1, a3); // same key and value
 assertNotEquals(a1, a2); // different keys
 assertNotEquals(a1, a4); // null value
 }

 /**
 * Test for hashCode method
 */
 @Test
 public void testHashCode() {
 String key1 = "key1";
 String key2 = "key2";
 String value = "value";
 Attribute a1 = new Attribute(key1, value);
 Attribute a2 = new Attribute(key2, value);
 int hashCode1 = a1.hashCode();
 int hashCode2 = a2.hashCode();
 assertNotEquals(hashCode1, hashCode2);
 }

 /**
 * Test for clone method
 */
 @Test
 public void testClone() {
 String key = "key";
 String value = "value";
 Attribute original = new Attribute(key, value);
 Attribute cloned = original.clone();
 assertEquals(original, cloned);
 }
}