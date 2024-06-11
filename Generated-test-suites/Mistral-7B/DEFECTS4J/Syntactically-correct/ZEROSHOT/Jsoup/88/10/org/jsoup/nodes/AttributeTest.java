import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import java.util.HashMap;
import static org.junit.Assert.*;

public class AttributeTest {

 private Attribute attribute;
 private Attributes attributes;

 @Before
 public void setUp() {
 attributes = new Attributes();
 attribute = new Attribute("testKey", "testValue", attributes);
 }

 @Test
 public void testAttributeCreationWithNullKey() {
 assertNull(new Attribute(null, "testValue"));
 }

 @Test
 public void testAttributeCreationWithEmptyKey() {
 assertNull(new Attribute("", "testValue"));
 }

 @Test
 public void testAttributeCreationWithKeyAndValue() {
 assertEquals("testKey", attribute.getKey());
 assertEquals("testValue", attribute.getValue());
 }

 @Test
 public void testAttributeCreationWithKeyAndValueAndParent() {
 Attribute attributeWithParent = new Attribute("testKey", "testValue", attributes);
 assertEquals("testKey", attributeWithParent.getKey());
 assertEquals("testValue", attributeWithParent.getValue());
 assertSame(attributes, attributeWithParent.parent);
 }

 @Test
 public void testSettingKey() {
 attribute.setKey("newKey");
 assertEquals("newKey", attribute.getKey());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSettingKeyWithNullKey() {
 attribute.setKey(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSettingKeyWithEmptyKey() {
 attribute.setKey("");
 }

 @Test
 public void testSettingValue() {
 attribute.setValue("newValue");
 assertEquals("newValue", attribute.getValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSettingValueWithNullValue() {
 attribute.setValue(null);
 }

 @Test
 public void testHtml() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 attribute.html(sb, new Document("").outputSettings());
 String htmlRepresentation = StringUtil.releaseBuilder(sb);
 assertEquals("testKey=\"testValue\"", htmlRepresentation);
 }

 @Test
 public void testCreateFromEncoded() {
 Attribute createdAttribute = Attribute.createFromEncoded("testKey", "%65%6e%63%6f%64%65%64%20%74%72%79%20%74%68%69%73%20%61%6e%64%6f%77%73%20%73%72%63%3d%22%22");
 assertEquals("testKey", createdAttribute.getKey());
 assertEquals("test value", createdAttribute.getValue());
 }

 @Test
 public void testIsDataAttribute() {
 assertTrue(attribute.isDataAttribute());
 }

 @Test
 public void testShouldCollapseAttribute() throws IOException {
 Document.OutputSettings outputSettings = new Document.OutputSettings();
 assertTrue(attribute.shouldCollapseAttribute(outputSettings));
 }

 @Test
 public void testIsBooleanAttribute() {
 assertFalse(attribute.isBooleanAttribute());
 }

 @Test
 public void testIsBooleanAttributeWithBooleanAttributeName() {
 Attribute booleanAttribute = new Attribute("checked", null, attributes);
 assertTrue(booleanAttribute.isBooleanAttribute());
 }

 @Test
 public void testEquals() {
 Attribute equalAttribute = new Attribute("testKey", "testValue", attributes);
 assertTrue(attribute.equals(equalAttribute));
 }

 @Test
 public void testNotEqualsWithDifferentKey() {
 Attribute differentKeyAttribute = new Attribute("differentKey", "testValue", attributes);
 assertFalse(attribute.equals(differentKeyAttribute));
 }

 @Test
 public void testNotEqualsWithDifferentValue() {
 Attribute differentValueAttribute = new Attribute("testKey", "differentValue", attributes);
 assertFalse(attribute.equals(differentValueAttribute));
 }

 @Test
 public void testClone() {
 Attribute clonedAttribute = attribute.clone();
 assertEquals(attribute, clonedAttribute);
 }

 @Test
 public void testHashCode() {
 int firstHashCode = attribute.hashCode();
 attribute.setValue("newValue");
 int secondHashCode = attribute.hashCode();
 assertNotEquals(firstHashCode, secondHashCode);
 }
}