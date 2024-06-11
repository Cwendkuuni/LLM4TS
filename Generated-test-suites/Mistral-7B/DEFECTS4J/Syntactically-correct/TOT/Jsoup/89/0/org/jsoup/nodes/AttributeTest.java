import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
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
 Attribute newAttribute = new Attribute("key", "value");
 assertEquals("key", newAttribute.getKey());
 assertEquals("value", newAttribute.getValue());
 }

 @Test
 public void testSetKey() {
 attribute.setKey("newKey");
 assertEquals("newKey", attribute.getKey());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetKeyNull() {
 attribute.setKey(null);
 fail("IllegalArgumentException expected");
 }

 @Test
 public void testSetValue() {
 attribute.setValue("newValue");
 assertEquals("newValue", attribute.getValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetValueNull() {
 attribute.setValue(null);
 fail("IllegalArgumentException expected");
 }

 @Test
 public void testHtml() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 attribute.html(sb, new Document("").outputSettings());
 assertEquals("key=\"value\"", StringUtil.releaseBuilder(sb).toString());
 }

 @Test(expected = SerializationException.class)
 public void testHtmlException() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 attribute.html(sb, null);
 fail("SerializationException expected");
 }

 @Test
 public void testCreateFromEncoded() {
 Attribute newAttribute = Attribute.createFromEncoded("key", "key=\"value\"");
 assertEquals("key", newAttribute.getKey());
 assertEquals("value", newAttribute.getValue());
 }

 @Test
 public void testIsDataAttribute() {
 assertTrue(attribute.isDataAttribute());
 Attribute dataAttribute = new Attribute("data-key", null);
 assertTrue(dataAttribute.isDataAttribute());
 Attribute notDataAttribute = new Attribute("key", null);
 assertFalse(notDataAttribute.isDataAttribute());
 }

 @Test
 public void testShouldCollapseAttribute() {
 assertTrue(attribute.shouldCollapseAttribute(new Document.OutputSettings().syntax(Document.OutputSettings.Syntax.html)));
 attributes.put("key", attribute);
 assertFalse(attribute.shouldCollapseAttribute(new Document.OutputSettings().syntax(Document.OutputSettings.Syntax.html)));
 }

 @Test
 public void testIsBooleanAttribute() {
 assertFalse(attribute.isBooleanAttribute());
 Attribute booleanAttribute = new Attribute("checked", null);
 assertTrue(booleanAttribute.isBooleanAttribute());
 }

 @Test
 public void testEquals() {
 Attribute newAttribute = new Attribute("key", "value");
 Attribute identicalAttribute = new Attribute("key", "value");
 assertEquals(newAttribute, identicalAttribute);
 Attribute differentAttribute = new Attribute("key2", "value");
 assertNotEquals(newAttribute, differentAttribute);
 }

 @Test
 public void testClone() {
 Attribute clonedAttribute = attribute.clone();
 assertEquals(attribute.getKey(), clonedAttribute.getKey());
 assertEquals(attribute.getValue(), clonedAttribute.getValue());
 }
}