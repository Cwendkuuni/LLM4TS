import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import static org.junit.Assert.*;

public class AttributeTest {

 private Attribute attribute;
 private Attributes attributes;

 @Before
 public void setUp() {
 attributes = new Attributes();
 attribute = new Attribute("key", "value", attributes);
 }

 @Test
 public void testAttributeCreation() {
 Attribute newAttribute = new Attribute("newKey", "newValue", attributes);
 assertEquals("newKey", newAttribute.getKey());
 assertEquals("newValue", newAttribute.getValue());
 }

 @Test
 public void testSettingKey() {
 attribute.setKey("newKey");
 assertEquals("newKey", attribute.getKey());
 }

 @Test(expected = NullPointerException.class)
 public void testSettingNullKey() {
 attribute.setKey(null);
 }

 @Test
 public void testSettingEmptyKey() {
 attribute.setKey("");
 assertEquals("", attribute.getKey());
 }

 @Test
 public void testSettingValue() {
 attribute.setValue("newValue");
 assertEquals("newValue", attribute.getValue());
 }

 @Test(expected = NullPointerException.class)
 public void testSettingNullValue() {
 attribute.setValue(null);
 }

 @Test
 public void testGetHtml() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 attribute.html(sb, new Document("").outputSettings());
 assertEquals("key=\"value\"", sb.toString());
 }

 @Test
 public void testGetHtmlWithEmptyValue() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 attribute.setKey("key");
 attribute.setValue("");
 attribute.html(sb, new Document("").outputSettings());
 assertEquals("key", sb.toString());
 }

 @Test
 public void testGetHtmlWithBooleanAttribute() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 attribute.setKey("checked");
 attribute.setValue(null);
 attribute.html(sb, new Document("").outputSettings());
 assertEquals("checked", sb.toString());
 }

 @Test
 public void testIsDataAttribute() {
 assertTrue(attribute.isDataAttribute());
 Attribute dataAttribute = new Attribute("data-key", null, attributes);
 assertFalse(dataAttribute.isDataAttribute());
 }

 @Test
 public void testShouldCollapseAttribute() {
 assertTrue(attribute.shouldCollapseAttribute());
 Attribute booleanAttribute = new Attribute("checked", null, attributes);
 assertFalse(booleanAttribute.shouldCollapseAttribute());
 }

 @Test
 public void testToString() {
 assertEquals("key=value", attribute.toString());
 }

 @Test
 public void testClone() {
 Attribute clonedAttribute = attribute.clone();
 assertEquals(attribute, clonedAttribute);
 }
}