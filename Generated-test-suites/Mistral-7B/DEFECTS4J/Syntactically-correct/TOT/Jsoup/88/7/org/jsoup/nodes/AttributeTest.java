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

 @Test
 public void testAttributeCreation() {
 // Test creating an Attribute with a raw key and value
 Attribute attribute1 = new Attribute("key1", "value1");
 assertEquals("key1", attribute1.getKey());
 assertEquals("value1", attribute1.getValue());

 // Test creating an Attribute with a raw key, value, and parent Attributes
 Attribute attribute2 = new Attribute("key2", "value2", attributes);
 assertEquals("key2", attribute2.getKey());
 assertEquals("value2", attribute2.getValue());
 assertEquals(attributes, attribute2.parent);
 }

 @Test
 public void testSettingKey() {
 Attribute attribute = new Attribute("key1", "value1");
 attribute.setKey("key2");
 assertEquals("key2", attribute.getKey());
 }

 @Test
 public void testSettingValue() {
 Attribute attribute = new Attribute("key1", "value1");
 String oldValue = attribute.setValue("value2");
 assertEquals("value2", attribute.getValue());
 assertEquals("value1", oldValue);
 }

 @Test(expected = NullPointerException.class)
 public void testNullKey() {
 Attribute attribute = new Attribute(null, "value");
 }

 @Test(expected = NullPointerException.class)
 public void testNullValue() {
 Attribute attribute = new Attribute("key", null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyKey() {
 Attribute attribute = new Attribute("", "value");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyValue() {
 Attribute attribute = new Attribute("key", "");
 }

 @Test
 public void testHtmlMethod() throws IOException, SerializationException {
 Attribute attribute = new Attribute("key", "value");
 StringBuilder sb = StringUtil.borrowBuilder();
 attribute.html(sb, new Document("").outputSettings());
 assertEquals("key=\"value\"", sb.toString());
 }

 @Test
 public void testHtmlWithEncodedValue() throws IOException, SerializationException {
 Attribute attribute = Attribute.createFromEncoded("key", "key=\"value\"");
 StringBuilder sb = StringUtil.borrowBuilder();
 attribute.html(sb, new Document("").outputSettings());
 assertEquals("key=\"value\"", sb.toString());
 }

 @Test
 public void testShouldCollapseAttribute() {
 assertTrue(Attribute.shouldCollapseAttribute("key", null, new Document.OutputSettings()));
 assertTrue(Attribute.shouldCollapseAttribute("key1", "key1", new Document.OutputSettings()));
 assertTrue(Attribute.shouldCollapseAttribute("key1", "", new Document.OutputSettings()));
 assertFalse(Attribute.shouldCollapseAttribute("key1", "value1", new Document.OutputSettings()));
 }

 @Test
 public void testIsBooleanAttribute() {
 assertTrue(Attribute.isBooleanAttribute("checked"));
 assertFalse(Attribute.isBooleanAttribute("key1"));
 }
}