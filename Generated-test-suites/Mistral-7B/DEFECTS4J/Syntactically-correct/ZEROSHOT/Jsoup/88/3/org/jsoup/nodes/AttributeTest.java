import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class AttributeTest {

 @BeforeEach
 void setUp() {
 Attributes parent = mock(Attributes.class);
 String key = "testKey";
 String value = "testValue";
 Attribute attribute = new Attribute(key, value, parent);
 }

 @Test
 void testConstructorWithKeyAndValue() {
 Attribute attribute = new Attribute("testKey", "testValue");
 assertEquals("testKey", attribute.getKey());
 assertEquals("testValue", attribute.getValue());
 }

 @Test
 void testConstructorWithKeyValueAndParent() {
 Attribute attribute = new Attribute("testKey", "testValue", mock(Attributes.class));
 assertEquals("testKey", attribute.getKey());
 assertEquals("testValue", attribute.getValue());
verify(mock(Attributes.class)).add(eq(attribute));
 }

 @Test
 void testSetKey() {
 Attribute attribute = new Attribute("testKey", "testValue");
 attribute.setKey("newKey");
 assertEquals("newKey", attribute.getKey());
 }

 @Test
 void testSetValue() {
 Attribute attribute = new Attribute("testKey", "testValue");
 String newValue = "newValue";
 String oldValue = attribute.getValue();
 attribute.setValue(newValue);
 assertEquals(newValue, attribute.getValue());
 assertEquals(oldValue, attribute.parent.get("testKey"));
 }

 @Test
 void testHtml() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 Attribute attribute = new Attribute("testKey", "testValue");
 attribute.html(sb, new Document("").outputSettings());
 assertEquals("testKey=\"testValue\"", StringUtil.releaseBuilder(sb));
 }

 @Test
 void testToString() {
 Attribute attribute = new Attribute("testKey", "testValue");
 assertEquals("testKey=\"testValue\"", attribute.toString());
 }

 @Test
 void testCreateFromEncoded() {
 String encodedValue = "testValue%20%26%20test";
 Attribute attribute = Attribute.createFromEncoded("testKey", encodedValue);
 assertEquals("testKey", attribute.getKey());
 assertEquals("test Value & test", attribute.getValue());
 }

 @Test
 void testIsDataAttribute() {
 Attribute dataAttribute = new Attribute("data-test", null);
 Attribute notDataAttribute = new Attribute("test", null);
 assertTrue(dataAttribute.isDataAttribute());
 assertFalse(notDataAttribute.isDataAttribute());
 }

 @Test
 void testShouldCollapseAttribute() {
 Document.OutputSettings outputSettings = Document.OutputSettings.html;
 assertTrue(attribute.shouldCollapseAttribute(outputSettings));
 assertFalse(new Attribute("test", "test").shouldCollapseAttribute(outputSettings));
 }

 @Test
 void testIsBooleanAttribute() {
 Attribute booleanAttribute = new Attribute("checked", null);
 Attribute notBooleanAttribute = new Attribute("test", null);
 assertTrue(booleanAttribute.isBooleanAttribute());
 assertFalse(notBooleanAttribute.isBooleanAttribute());
 }

}