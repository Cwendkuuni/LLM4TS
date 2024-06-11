import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AttributeTest {

 private Attribute attribute;

 @BeforeEach
 void setUp() {
 attribute = new Attribute("key", "value");
 }

 @Test
 void testAttributeCreationWithKeyAndValue() {
 Attribute newAttribute = new Attribute("key", "value");
 assertEquals(attribute.getKey(), newAttribute.getKey());
 assertEquals(attribute.getValue(), newAttribute.getValue());
 }

 @Test
 void testAttributeCreationWithKeyValueAndParent() {
 Attribute newAttribute = new Attribute("key", "value", new Attributes());
 assertEquals(attribute.getKey(), newAttribute.getKey());
 assertEquals(attribute.getValue(), newAttribute.getValue());
 assertNull(attribute.getParent());
 assertNull(newAttribute.getParent());
 }

 @Test
 void testGetKey() {
 assertEquals("key", attribute.getKey());
 }

 @Test
 void testSetKey() {
 attribute.setKey("newKey");
 assertEquals("newKey", attribute.getKey());
 }

 @Test
 void testGetValue() {
 assertEquals("value", attribute.getValue());
 }

 @Test
 void testSetValue() {
 attribute.setValue("newValue");
 assertEquals("newValue", attribute.getValue());
 }

 @Test
 void testHtmlMethod() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 attribute.html(sb, new Document("").outputSettings());
 String htmlOutput = StringUtil.releaseBuilder(sb);
 assertEquals("<key value=\"newValue\"/>", htmlOutput);
 }

 @Test
 void testToStringMethod() {
 assertEquals("<key value=\"value\"/>", attribute.toString());
 }

 @Test
 void testCreateFromEncoded() {
 Attribute newAttribute = Attribute.createFromEncoded("key", "value=\"newValue\"");
 assertEquals(attribute.getKey(), newAttribute.getKey());
 assertEquals(attribute.getValue(), newAttribute.getValue());
 }

 @Test
 void testIsDataAttribute() {
 assertFalse(attribute.isDataAttribute());
 Attribute dataAttribute = new Attribute("data-key", null);
 assertTrue(dataAttribute.isDataAttribute());
 }

 @Test
 void testShouldCollapseAttribute() {
 assertFalse(attribute.shouldCollapseAttribute());
 Attribute booleanAttribute = new Attribute("checked", null);
 assertTrue(booleanAttribute.shouldCollapseAttribute());
 }

 @Test
 void testIsBooleanAttribute() {
 assertFalse(attribute.isBooleanAttribute());
 Attribute booleanAttribute = new Attribute("checked", null);
 assertTrue(booleanAttribute.isBooleanAttribute());
 }

 @Test
 void testEquals() {
 Attribute newAttribute = new Attribute("key", "value");
 assertTrue(attribute.equals(newAttribute));
 }

 @Test
 void testHashCode() {
 Attribute newAttribute = new Attribute("key", "value");
 int attributeHashCode = attribute.hashCode();
 int newAttributeHashCode = newAttribute.hashCode();
 assertEquals(attributeHashCode, newAttributeHashCode);
 }

 @Test
 void testClone() {
 Attribute clonedAttribute = attribute.clone();
 assertEquals(attribute.getKey(), clonedAttribute.getKey());
 assertEquals(attribute.getValue(), clonedAttribute.getValue());
 }
}