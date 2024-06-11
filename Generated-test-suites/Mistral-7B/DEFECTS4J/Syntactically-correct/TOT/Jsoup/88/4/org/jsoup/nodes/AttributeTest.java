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
 public void testSettingAttributeKey() {
 attribute.setKey("newKey");
 assertEquals("newKey", attribute.getKey());
 }

 @Test
 public void testSettingAttributeValue() {
 attribute.setValue("newValue");
 assertEquals("newValue", attribute.getValue());
 }

 @Test
 public void testSettingAttributeKeyWithNull() {
 try {
 attribute.setKey(null);
 fail("Exception expected");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testSettingAttributeKeyWithEmptyString() {
 try {
 attribute.setKey("");
 fail("Exception expected");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testSettingAttributeValueWithNull() {
 try {
 attribute.setValue(null);
 fail("Exception expected");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testSettingParentWhenKeyChanged() {
 String newKey = "newKey";
 attribute.setKey(newKey);
 int index = attributes.indexOfKey(attribute.getKey());
 assertEquals(newKey, attributes.keys[index]);
 }

 @Test
 public void testSettingParentWhenValueChanged() {
 String newValue = "newValue";
 attribute.setValue(newValue);
 assertEquals(newValue, attributes.vals[attributes.indexOfKey(attribute.getKey())]);
 }

 @Test
 public void testHtml() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 attribute.html(sb, null);
 String expected = "key=\"value\"";
 assertEquals(expected, StringUtil.releaseBuilder(sb));
 }

 @Test
 public void testHtmlWithEmptyValue() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 String emptyValue = "";
 attribute.setValue(emptyValue);
 attribute.html(sb, null);
 String expected = "key=\"\"";
 assertEquals(expected, StringUtil.releaseBuilder(sb));
 }

 @Test
 public void testHtmlWithSameKeyAndValue() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 attribute.setValue(attribute.getKey());
 attribute.html(sb, null);
 String expected = "key";
 assertEquals(expected, StringUtil.releaseBuilder(sb));
 }

 @Test
 public void testHtmlWithBooleanAttribute() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 attribute.setKey("checked");
 attribute.setValue(null);
 attribute.html(sb, null);
 String expected = "checked";
 assertEquals(expected, StringUtil.releaseBuilder(sb));
 }

 @Test
 public void testHtmlWithBooleanAttributeAndValue() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 attribute.setKey("checked");
 attribute.setValue("checked");
 attribute.html(sb, null);
 String expected = "checked=\"checked\"";
 assertEquals(expected, StringUtil.releaseBuilder(sb));
 }
}