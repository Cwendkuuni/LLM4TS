import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attribute;
import static org.junit.Assert.*;
import java.util.Arrays;

public class AttributeTest {

 private Attribute attribute;

 @Before
 public void setUp() {
 attribute = new Attribute("key", "value");
 }

 @Test
 public void testCreateAttribute() {
 Attribute attr = new Attribute("key", "value");
 assertEquals("key", attr.getKey());
 assertEquals("value", attr.getValue());
 }

 @Test
 public void testSetKey() {
 attribute.setKey("newKey");
 assertEquals("newKey", attribute.getKey());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetKeyNull() {
 attribute.setKey(null);
 fail("Should throw IllegalArgumentException for null key");
 }

 @Test(expected = IllegalStateException.class)
 public void testSetKeySameAsParent() {
 Attributes parent = new Attributes();
 parent.put(attribute.getKey(), attribute);
 attribute.setKey("sameKey");
 fail("Should throw IllegalStateException when key already exists in parent");
 }

 @Test
 public void testSetValue() {
 attribute.setValue("newValue");
 assertEquals("newValue", attribute.getValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetValueNull() {
 attribute.setValue(null);
 fail("Should throw IllegalArgumentException for null value");
 }

 @Test
 public void testIsBooleanAttribute() {
 assertFalse(attribute.isBooleanAttribute());
 }

 @Test
 public void testIsDataAttribute() {
 assertFalse(attribute.isDataAttribute());
 }

 @Test
 public void testIsDataAttributeWithDataPrefix() {
 Attribute dataAttribute = Attribute.createFromEncoded("data-test", "data");
 assertTrue(dataAttribute.isDataAttribute());
 }

 @Test
 public void testShouldCollapseAttribute() {
 Document.OutputSettings out = Document.OutputSettings.syntaxHtml();
 assertTrue(attribute.shouldCollapseAttribute(out));
 }

 @Test
 public void testShouldCollapseAttributeFalse() {
 Document.OutputSettings out = new Document.OutputSettings();
 assertFalse(attribute.shouldCollapseAttribute(out));
 }

 @Test
 public void testEquals() {
 Attribute other = new Attribute("key", "value");
 Attribute same = new Attribute("key", "value");
 Attribute differentKey = new Attribute("differentKey", "value");
 Attribute differentValue = new Attribute("key", "differentValue");
 Attribute nullAttribute = null;

 assertEquals(other, same);
 assertNotEquals(other, differentKey);
 assertNotEquals(other, differentValue);
 assertNotEquals(other, nullAttribute);
 }

 @Test
 public void testHashCode() {
 Attribute other = new Attribute("key", "value");
 Attribute same = new Attribute("key", "value");
 Attribute differentKey = new Attribute("differentKey", "value");
 Attribute differentValue = new Attribute("key", "differentValue");
 Attribute nullAttribute = null;

 assertEquals(other.hashCode(), same.hashCode());
 assertNotEquals(other.hashCode(), differentKey.hashCode());
 assertNotEquals(other.hashCode(), differentValue.hashCode());
 assertNotEquals(other.hashCode(), nullAttribute.hashCode());
 }

 @Test
 public void testClone() {
 Attribute cloned = attribute.clone();
 assertEquals(attribute.getKey(), cloned.getKey());
 assertEquals(attribute.getValue(), cloned.getValue());
 }
}