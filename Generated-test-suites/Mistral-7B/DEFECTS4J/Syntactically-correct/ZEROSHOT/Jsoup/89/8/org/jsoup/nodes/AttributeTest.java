import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.junit.Before;
import org.junit.Test;
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
 public void testCreateAttribute() {
 Attribute attribute = new Attribute("key", "value");
 assertNotNull(attribute);
 assertEquals("key", attribute.getKey());
 assertEquals("value", attribute.getValue());
 }

 @Test
 public void testCreateAttributeWithParent() {
 Attribute attribute = new Attribute("key", "value", attributes);
 assertNotNull(attribute);
 assertEquals("key", attribute.getKey());
 assertEquals("value", attribute.getValue());
 assertTrue(attributes.containsKey("key"));
 }

 @Test
 public void testSetKey() {
 attribute.setKey("newKey");
 assertEquals("newKey", attribute.getKey());
 assertFalse(attributes.containsKey("key"));
 assertTrue(attributes.containsKey("newKey"));
 }

 @Test
 public void testSetValue() {
 attribute.setValue("newValue");
 assertEquals("newValue", attribute.getValue());
 assertTrue(attributes.containsValue("newValue"));
 }

 @Test
 public void testHtml() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 attribute.html(sb, new Document("").outputSettings());
 assertEquals("key=\"value\"", StringUtil.releaseBuilder(sb));
 }

 @Test
 public void testShouldCollapseAttribute() {
 Document.OutputSettings out = new Document.OutputSettings();
 assertTrue(attribute.shouldCollapseAttribute(out));
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
 Attribute dataAttribute = new Attribute("data-key", null, attributes);
 assertTrue(dataAttribute.isDataAttribute());
 }

 @Test
 public void testEquals() {
 Attribute otherAttribute = new Attribute("key", "value", attributes);
 assertTrue(attribute.equals(otherAttribute));
 }

 @Test
 public void testCreateFromEncoded() {
 Attribute createdAttribute = Attribute.createFromEncoded("key", "key=\"value\"");
 assertEquals("key", createdAttribute.getKey());
 assertEquals("value", createdAttribute.getValue());
 }

 @Test
 public void testShouldCollapseAttributeWithBooleanAttribute() {
 Document.OutputSettings out = new Document.OutputSettings();
 assertTrue(new Attribute("checked", null, attributes).shouldCollapseAttribute(out));
 }

 @Test(expected = SerializationException.class)
 public void testHtmlWithIoException() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 attribute.html(sb, new Document("").outputSettings());
 fail("IOException expected");
 }

 @Test
 public void testClone() throws CloneNotSupportedException {
 Attribute clonedAttribute = attribute.clone();
 assertEquals(attribute, clonedAttribute);
 }
}