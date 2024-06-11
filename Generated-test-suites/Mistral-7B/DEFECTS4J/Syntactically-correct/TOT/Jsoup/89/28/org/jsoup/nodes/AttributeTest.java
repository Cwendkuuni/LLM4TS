import org.junit.Test;
import org.jsoup.nodes.Attribute;
import static org.junit.Assert.*;

public class AttributeTest {

 @Test
 public void testNewAttribute() {
 Attribute attr = new Attribute("class", "test");
 assertEquals("class", attr.getKey());
 assertEquals("test", attr.getValue());
 assertNull(attr.getParent());
 }

 @Test
 public void testNewAttributeWithParent() {
 Attributes parent = new Attributes(); // Assuming Attributes is a class that can be instantiated
 Attribute attr = new Attribute("class", "test", parent);
 assertEquals("class", attr.getKey());
 assertEquals("test", attr.getValue());
 assertEquals(parent, attr.getParent());
 }

 @Test
 public void testSettingKey() {
 Attribute attr = new Attribute("class", "test");
 attr.setKey("id");
 assertEquals("id", attr.getKey());
 }

 @Test(expected = NullPointerException.class)
 public void testSettingNullKey() {
 Attribute attr = new Attribute("class", "test");
 attr.setKey(null);
 }

 @Test
 public void testSettingValue() {
 Attribute attr = new Attribute("class", "test");
 attr.setValue("id");
 assertEquals("id", attr.getValue());
 }

 @Test(expected = NullPointerException.class)
 public void testSettingNullValue() {
 Attribute attr = new Attribute("class", "test");
 attr.setValue(null);
 }

 @Test
 public void testGettingHtml() throws Exception {
 Attribute attr = new Attribute("class", "test");
 StringBuilder sb = StringUtil.borrowBuilder();
 attr.html(sb, new Document("").outputSettings());
 assertEquals("class=\"test\"", sb.toString());
 }

 @Test
 public void testShouldCollapseAttribute() {
 Attribute attr = new Attribute("checked", "");
 assertTrue(attr.shouldCollapseAttribute(null));
 Attribute attr1 = new Attribute("class", "test");
 assertFalse(attr1.shouldCollapseAttribute(null));
 }

 @Test
 public void testIsBooleanAttribute() {
 Attribute attr = new Attribute("checked", "");
 assertTrue(attr.isBooleanAttribute());
 Attribute attr1 = new Attribute("class", "test");
 assertFalse(attr1.isBooleanAttribute());
 }

 @Test
 public void testEquals() {
 Attribute attr1 = new Attribute("class", "test");
 Attribute attr2 = new Attribute("class", "test");
 Attribute attr3 = new Attribute("id", "test");
 assertTrue(attr1.equals(attr2));
 assertFalse(attr1.equals(attr3));
 }

 @Test
 public void testHashCode() {
 Attribute attr1 = new Attribute("class", "test");
 Attribute attr2 = new Attribute("class", "test");
 Attribute attr3 = new Attribute("id", "test");
 assertEquals(attr1.hashCode(), attr2.hashCode());
 assertNotEquals(attr1.hashCode(), attr3.hashCode());
 }

 @Test
 public void testCloning() {
 Attribute attr = new Attribute("class", "test");
 Attribute clonedAttr = attr.clone();
 assertEquals(attr, clonedAttr);
 }
}