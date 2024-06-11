import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;

public class AttributeTest {

 @Test
 public void testAttributeCreation() {
 String key = "class";
 String value = "active";
 Attribute attribute = new Attribute(key, value);

 assertEquals(key, attribute.getKey());
 assertEquals(value, attribute.getValue());
 }

 @Test
 public void testAttributeSetKey() {
 String key = "id";
 String value = "element-1";
 Attribute attribute = new Attribute(key, value);

 attribute.setKey("class");
 assertEquals("class", attribute.getKey());
 }

 @Test
 public void testAttributeSetValue() {
 String key = "src";
 String value = "image.jpg";
 Attribute attribute = new Attribute(key, value);

 attribute.setValue("another-image.jpg");
 assertEquals("another-image.jpg", attribute.getValue());
 }

 @Test(expected = NullPointerException.class)
 public void testNullKey() {
 Attribute attribute = new Attribute(null, "value");
 }

 @Test(expected = NullPointerException.class)
 public void testNullValue() {
 Attribute attribute = new Attribute("key", null);
 }

 @Test
 public void testHtmlMethod() throws IOException {
 String key = "href";
 String value = "http://example.com";
 Attribute attribute = new Attribute(key, value);

 StringBuilder sb = StringUtil.borrowBuilder();
 attribute.html(sb, new Document("").outputSettings());
 String html = StringUtil.releaseBuilder(sb);

 assertEquals("<a href=\"http://example.com\">", html);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidBooleanAttribute() {
 Attribute attribute = new Attribute("invalid", "value");
 attribute.html(StringUtil.borrowBuilder(), new Document("").outputSettings());
 }

 @Test
 public void testBooleanAttributeCollapsing() {
 Document.OutputSettings outputSettings = new Document.OutputSettings();
 outputSettings.syntax(Document.OutputSettings.Syntax.html);

 String key = "checked";
 String value = null;
 Attribute attribute = new Attribute(key, value);

 assertTrue(attribute.shouldCollapseAttribute(outputSettings));
 }

 @Test
 public void testStringBooleanAttributeCollapsing() {
 Document.OutputSettings outputSettings = new Document.OutputSettings();
 outputSettings.syntax(Document.OutputSettings.Syntax.html);

 String key = "checked";
 String value = "true";
 Attribute attribute = new Attribute(key, value);

 assertFalse(attribute.shouldCollapseAttribute(outputSettings));
 }
}