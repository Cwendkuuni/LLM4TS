import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.helper.Validate;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

public class AttributeTest {

 private Attribute attribute;
 private Document document;

 @Before
 public void setup() {
 document = new Document();
 attribute = new Attribute("key", "value");
 }

 @Test
 public void testCreateAttribute() {
 assertEquals("key", attribute.getKey());
 assertEquals("value", attribute.getValue());
 assertNull(attribute.getParent());
 }

 @Test
 public void testCreateAttributeWithParent() {
 Attributes parents = new Attributes();
 parents.put(new Attribute("parentKey", "parentValue"));
 attribute = new Attribute("key", "value", parents);
 assertEquals("parentKey", parents.getParent().getKey());
 assertEquals("parentValue", parents.getParent().getValue());
 assertEquals(parents, attribute.getParent());
 }

 @Test
 public void testSetKey() {
 String newKey = "newKey";
 attribute.setKey(newKey);
 assertEquals(newKey, attribute.getKey());
 }

 @Test
 public void testSetKeyWithNull() {
 assertThrows(NullPointerException.class, () -> attribute.setKey(null));
 }

 @Test
 public void testSetKeyWithEmptyString() {
 assertThrows(IllegalArgumentException.class, () -> attribute.setKey(""));
 }

 @Test
 public void testSetValue() {
 String newValue = "newValue";
 attribute.setValue(newValue);
 assertEquals(newValue, attribute.getValue());
 }

 @Test
 public void testSetValueWithNull() {
 assertThrows(NullPointerException.class, () -> attribute.setValue(null));
 }

 @Test
 public void testHtml() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 attribute.html(sb, document.outputSettings());
 String html = StringUtil.releaseBuilder(sb);
 assertEquals("key=\"value\"", html);
 }

 @Test
 public void testShouldCollapseAttribute() throws IOException {
 Document.OutputSettings out = new Document.OutputSettings();
 assertTrue(attribute.shouldCollapseAttribute(out));
 }

 @Test
 public void testShouldCollapseAttributeWithBooleanAttribute() throws IOException {
 Document.OutputSettings out = new Document.OutputSettings();
 assertTrue(attribute.shouldCollapseAttribute("checked", null, out));
 assertFalse(attribute.shouldCollapseAttribute("notBoolean", null, out));
 }

 @Test
 public void testShouldCollapseAttributeWithEmptyValue() throws IOException {
 Document.OutputSettings out = new Document.OutputSettings();
 assertTrue(attribute.shouldCollapseAttribute(null, "", out));
 }

 @Test
 public void testShouldCollapseAttributeWithSameValueAsKeyAndBooleanAttribute() throws IOException {
 Document.OutputSettings out = new Document.OutputSettings();
 assertTrue(attribute.shouldCollapseAttribute("checked", "checked", out));
 }

 @Test
 public void testShouldCollapseAttributeWithDifferentValueAsKeyAndBooleanAttribute() throws IOException {
 Document.OutputSettings out = new Document.OutputSettings();
 assertFalse(attribute.shouldCollapseAttribute("checked", "value", out));
 }

 @Test
 public void testIsDataAttribute() {
 assertTrue(attribute.isDataAttribute());
 assertFalse(attribute.isDataAttribute("notDataAttribute"));
 }

 @Test
 public void testIsBooleanAttribute() {
 assertFalse(attribute.isBooleanAttribute());
 assertFalse(attribute.isBooleanAttribute("notBooleanAttribute"));
 assertTrue(attribute.isBooleanAttribute("checked"));
 }

 @Test
 public void testEquals() {
 Attribute equalAttribute = new Attribute("key", "value");
 assertEquals(attribute, equalAttribute);
 }

 @Test
 public void testEqualsWithDifferentKey() {
 Attribute differentAttribute = new Attribute("differentKey", "value");
 assertNotSame(attribute, differentAttribute);
 }

 @Test
 public void testEqualsWithDifferentValue() {
 Attribute differentAttribute = new Attribute("key", "differentValue");
 assertNotSame(attribute, differentAttribute);
 }

 @Test
 public void testClone() throws CloneNotSupportedException {
 Attribute clonedAttribute = attribute.clone();
 assertEquals(attribute, clonedAttribute);
 }
}