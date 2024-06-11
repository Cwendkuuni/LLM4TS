import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attribute;
import static org.jsoup.nodes.Attributes.*;

public class AttributeTest {

 @Test
 public void testAttributeCreation() {
 Attribute attr = new Attribute("key", "value");
 assertEquals("key", attr.getKey());
 assertEquals("value", attr.getValue());
 }

 @Test
 public void testAttributeCreationWithParent() {
 Attributes parent = new Attributes();
 Attribute attr = new Attribute("key", "value", parent);
 assertEquals("key", attr.getKey());
 assertEquals("value", attr.getValue());
 Assert.assertNotNull(parent);
 Assert.assertTrue(parent.containsKey("key"));
 Assert.assertEquals("value", parent.get("key"));
 }

 @Test
 public void testSettingKey() {
 Attribute attr = new Attribute("key", "value");
 attr.setKey("newKey");
 assertEquals("newKey", attr.getKey());
 }

 @Test(expected = NullPointerException.class)
 public void testNullKey() {
 Attribute attr = new Attribute(null, "value");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyKey() {
 Attribute attr = new Attribute("", "value");
 }

 @Test
 public void testSettingValue() {
 Attribute attr = new Attribute("key", "value");
 String newValue = "newValue";
 assertEquals("value", attr.getValue());
 attr.setValue(newValue);
 assertEquals("newValue", attr.getValue());
 }

 @Test(expected = NullPointerException.class)
 public void testNullValue() {
 Attribute attr = new Attribute("key", null);
 }

 @Test
 public void testHtmlEncoding() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 Attribute attr = new Attribute("key", "value<>&\"'");
 attr.html(sb, new Document("").outputSettings());
 String encodedValue = StringUtil.releaseBuilder(sb);
 assertEquals("key=\"value&lt;&amp;&gt;&quot;&apos;\"", encodedValue);
 }

 @Test
 public void testIsDataAttribute() {
 Attribute dataAttr = new Attribute("data-key", "value");
 assertTrue(dataAttr.isDataAttribute());
 Attribute nonDataAttr = new Attribute("key", "value");
 assertFalse(nonDataAttr.isDataAttribute());
 }

 @Test
 public void testShouldCollapseAttribute() throws IOException {
 Document.OutputSettings out = new Document.OutputSettings();
 Attribute attr1 = new Attribute("key", "value");
 assertFalse(attr1.shouldCollapseAttribute(out));
 Attribute attr2 = new Attribute("key", "");
 assertTrue(attr2.shouldCollapseAttribute(out));
 Attribute attr3 = new Attribute("checked", "");
 assertTrue(attr3.shouldCollapseAttribute(out));
 Attribute attr4 = new Attribute("key", "value");
 Attribute parent = new Attributes();
 parent.put(attr4);
 assertEquals("key", parent.get(0).getKey());
 assertTrue(attr4.shouldCollapseAttribute(out));
 }

 @Test
 public void testIsBooleanAttribute() {
 Attribute attr1 = new Attribute("checked", null);
 assertTrue(attr1.isBooleanAttribute());
 Attribute attr2 = new Attribute("key", "value");
 assertFalse(attr2.isBooleanAttribute());
 }

 @Test
 public void testEquals() {
 Attribute attr1 = new Attribute("key1", "value1");
 Attribute attr2 = new Attribute("key1", "value1");
 Attribute attr3 = new Attribute("key2", "value2");
 assertTrue(attr1.equals(attr1));
 assertTrue(attr1.equals(attr2));
 assertFalse(attr1.equals(attr3));
 }

 @Test
 public void testClone() throws CloneNotSupportedException {
 Attribute attr = new Attribute("key", "value");
 Attribute clonedAttr = attr.clone();
 assertEquals(attr.getKey(), clonedAttr.getKey());
 assertEquals(attr.getValue(), clonedAttr.getValue());
 }
}