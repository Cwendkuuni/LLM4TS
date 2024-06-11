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
 public void testCreateAttribute() {
 Attribute newAttribute = new Attribute("newKey", "newValue");
 assertEquals("newKey", newAttribute.getKey());
 assertEquals("newValue", newAttribute.getValue());
 }

 @Test
 public void testSetKey() {
 attribute.setKey("newKey");
 assertEquals("newKey", attribute.getKey());
 }

 @Test(expected = NullPointerException.class)
 public void testSetNullKey() {
 attribute.setKey(null);
 }

 @Test
 public void testSetEmptyKey() {
 attribute.setKey("");
 assertEquals("", attribute.getKey());
 }

 @Test
 public void testSetValue() {
 attribute.setValue("newValue");
 assertEquals("newValue", attribute.getValue());
 }

 @Test(expected = NullPointerException.class)
 public void testSetNullValue() {
 attribute.setValue(null);
 }

 @Test
 public void testHtml() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 attribute.html(sb, new Document("").outputSettings());
 String expected = "key=\"value\"";
 assertEquals(expected, StringUtil.releaseBuilder(sb));
 }

 @Test
 public void testHtmlWithEmptyValue() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 attribute.setKey("key");
 attribute.setValue("");
 attribute.html(sb, new Document("").outputSettings());
 String expected = "key";
 assertEquals(expected, StringUtil.releaseBuilder(sb));
 }

 @Test(expected = SerializationException.class)
 public void testHtmlWithIOException() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 throw new IOException();
 }

 @Test
 public void testCreateFromEncoded() {
 Attribute newAttribute = Attribute.createFromEncoded("key", "key=\"value\"");
 assertEquals("key", newAttribute.getKey());
 assertEquals("value", newAttribute.getValue());
 }

 @Test(expected = NullPointerException.class)
 public void testCreateFromEncodedWithNullUnencodedKey() {
 Attribute.createFromEncoded(null, "key=\"value\"");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateFromEncodedWithNullEncodedValue() {
 Attribute.createFromEncoded("key", null);
 }

 @Test
 public void testIsDataAttribute() {
 assertTrue(attribute.isDataAttribute());
 Attribute dataAttribute = new Attribute("data-test", null);
 assertTrue(dataAttribute.isDataAttribute());
 Attribute nonDataAttribute = new Attribute("key", null);
 assertFalse(nonDataAttribute.isDataAttribute());
 }

 @Test
 public void testShouldCollapseAttribute() {
 assertTrue(attribute.shouldCollapseAttribute(new Document.OutputSettings()));
 Attribute booleanAttribute = new Attribute("checked", null);
 assertTrue(booleanAttribute.shouldCollapseAttribute(new Document.OutputSettings()));
 Attribute nonBooleanAttribute = new Attribute("key", "value");
 assertFalse(nonBooleanAttribute.shouldCollapseAttribute(new Document.OutputSettings()));
 }

 @Test
 public void testIsBooleanAttribute() {
 assertFalse(attribute.isBooleanAttribute());
 Attribute booleanAttribute = new Attribute("checked", null);
 assertTrue(booleanAttribute.isBooleanAttribute());
 Attribute nonBooleanAttribute = new Attribute("key", null);
 assertFalse(nonBooleanAttribute.isBooleanAttribute());
 }

 @Test
 public void testEquals() {
 Attribute newAttribute = new Attribute("key", "value");
 assertEquals(attribute, newAttribute);
 }

 @Test
 public void testClone() {
 Attribute clonedAttribute = attribute.clone();
 assertEquals(attribute.getKey(), clonedAttribute.getKey());
 assertEquals(attribute.getValue(), clonedAttribute.getValue());
 }
}