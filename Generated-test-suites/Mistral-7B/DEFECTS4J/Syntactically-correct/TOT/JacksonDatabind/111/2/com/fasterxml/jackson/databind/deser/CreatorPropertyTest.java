import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CreatorPropertyTest {

 private ObjectMapper objectMapper;
 private CreatorProperty creatorProperty;

 @Before
 public void setup() throws JsonProcessingException {
 objectMapper = new ObjectMapper();
 creatorProperty = new CreatorProperty(
 PropertyName.construct("testProperty"),
 JavaType.constructRawType(String.class),
 PropertyName.construct("wrapperName"),
 null,
 null,
 AnnotatedParameter.std(null, String.class),
 0,
 null,
 PropertyMetadata.STANDARD_EMPTY);
 }

 @Test
 public void testDefaultConstruction() throws IOException {
 Object testObject = new Object();
 creatorProperty.set(testObject, "defaultValue");
 assertEquals("defaultValue", creatorProperty.get(testObject));
 }

 @Test
 public void testSerializationAndDeserialization() throws IOException {
 Object testObject = new Object();
 creatorProperty.set(testObject, "serializedValue");
 String json = objectMapper.writeValueAsString(testObject);
 Object deserializedObject = objectMapper.readValue(json, Object.class);
 assertEquals("serializedValue", creatorProperty.get(deserializedObject));
 }

 @Test(expected = IOException.class)
 public void testMissingSetter() throws IOException {
 Object testObject = new Object();
 creatorProperty._fallbackSetter = null;
 creatorProperty.set(testObject, "valueToSet");
 }

 @Test
 public void testInjectableValue() throws IOException {
 Object testObject = new Object();
 BeanDescription beanDescription = objectMapper.getTypeFactory().constructBeanDescription(testObject.getClass());
 CreatorPropertyInjector injector = new CreatorPropertyInjector(objectMapper.getSerializationConfig(), beanDescription);
 Object injectedValue = new Object();
 beanDescription.findInjectableValueId(_creatorProperty.getName(), testObject.getClass())
 .ifPresent(id -> injector.inject(testObject, id, injectedValue));
 assertEquals(injectedValue, creatorProperty.get(testObject));
 }

 @Test
 public void testIgnoreProperty() throws IOException {
 creatorProperty.markAsIgnorable();
 Object testObject = new Object();
 creatorProperty.set(testObject, "ignoredValue");
 assertNull(creatorProperty.get(testObject));
 }

 @Test
 public void testSerializationAndDeserializationWithTypeHint() throws IOException {
 Object testObject = new Object();
 creatorProperty.set(testObject, new MyCustomClass());
 String json = objectMapper.writeValueAsString(testObject);
 Object deserializedObject = objectMapper.readValue(json, new TypeReference<Object>() {});
 MyCustomClass deserializedCustomClass = (MyCustomClass) deserializedObject;
 assertEquals(new MyCustomClass(), creatorProperty.get(deserializedObject));
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidInjectableValueId() throws IOException {
 Object testObject = new Object();
 creatorProperty.set(testObject, "invalidValueId");
 DeserializationContext context = new TestDeserializationContext();
 context.findInjectableValue(_creatorProperty.getName(), testObject.getClass());
 }
}