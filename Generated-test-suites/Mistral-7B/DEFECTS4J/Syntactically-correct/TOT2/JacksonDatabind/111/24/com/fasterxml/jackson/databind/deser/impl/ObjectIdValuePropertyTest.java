import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.fasterxml.jackson.core.test.BaseTest.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdValueProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.NameTransformer;

public class ObjectIdValuePropertyTest {

 private ObjectIdReader objectIdReader;
 private ObjectIdValueProperty objectIdValueProperty;

 @Before
 public void setup() throws JsonMappingException {
 ObjectIdReader.Std objectIdReaderStd = new ObjectIdReader.Std();
 objectIdReader = new ObjectIdReader(objectIdReaderStd, new StdSerializer<Object>(Object.class), null, null, null, null);
 ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.empty);
 this.objectIdValueProperty = objectIdValueProperty;
 }

 @Test
 public void testEmptyConstructor() {
 assertNotNull(objectIdValueProperty);
 }

 @Test
 public void testWithNameConstructor() throws JsonProcessingException {
 String propertyName = "testPropertyName";
 ObjectIdValueProperty objectIdValuePropertyWithName = new ObjectIdValueProperty(objectIdValueProperty, new PropertyName(propertyName));
 assertEquals(propertyName, objectIdValuePropertyWithName.getName());
 }

 @Test
 public void testWithValueDeserializerConstructor() throws JsonProcessingException {
 JsonDeserializer<Object> valueDeserializer = createSimpleDeserializer(Object.class);
 ObjectIdValueProperty objectIdValuePropertyWithDeserializer = new ObjectIdValueProperty(objectIdValueProperty, valueDeserializer, null);
 assertEquals(valueDeserializer, objectIdValuePropertyWithDeserializer.getValueDeserializer());
 }

 @Test
 public void testWithNullProviderConstructor() throws JsonProcessingException {
 NullValueProvider nullValueProvider = new NullValueProvider() {
 @Override
 public Object getNullValue(DeserializationContext context, BeanPropertyDefinition propertyDefinition) {
 return null;
 }
 };
 ObjectIdValueProperty objectIdValuePropertyWithNullProvider = new ObjectIdValueProperty(objectIdValueProperty, null, nullValueProvider);
 assertEquals(nullValueProvider, objectIdValuePropertyWithNullProvider.getNullProvider());
 }

 @Test
 public void testGetAnnotation() {
 assertNull(objectIdValueProperty.getAnnotation(Object.class));
 }

 @Test
 public void testGetMember() {
 assertNull(objectIdValueProperty.getMember());
 }

 @Test
 public void testDeserializeAndSet() throws JsonProcessingException {
 Object inputObject = new Object();
 Object id = new Object();
 JsonParser mockParser = createParserWithValue("{\"id\":\"" + id + "\"}");
 Object idValue = objectIdValueProperty.deserializeAndSet(mockParser, new TestContext(), inputObject);
 assertEquals(id, objectIdValue);
 }

 @Test
 public void testDeserializeSetAndReturnNull() throws JsonProcessingException {
 JsonParser mockParser = createParserWithValue("null");
 Object idValue = objectIdValueProperty.deserializeSetAndReturn(mockParser, new TestContext(), new Object());
 assertNull(idValue);
 }

 @Test
 public void testSetAndReturnNull() throws JsonProcessingException {
 Object inputObject = new Object();
 Object idValue = objectIdValueProperty.setAndReturn(inputObject, null);
 assertNull(idValue);
 }

 @Test
 public void testSetAndReturn() throws JsonProcessingException {
 Object inputObject = new Object();
 Object id = new Object();
 objectIdValueProperty.setAndReturn(inputObject, id);
 // check that the set value is correctly set in the object
 AnnotatedMember annotatedMember = new AnnotatedMember(new BeanPropertyDefinition(new PropertyName("testPropertyName"), Object.class, new NameTransformer() {
 @Override
 public String transform(String name, Class<?> clazz) {
 return name;
 }
 }), null, null, null, null);
 assertEquals(id, getValue(annotatedMember, inputObject));
 }
}