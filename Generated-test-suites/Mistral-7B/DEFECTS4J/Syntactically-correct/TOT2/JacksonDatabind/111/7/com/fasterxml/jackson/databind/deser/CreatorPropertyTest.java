import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class CreatorPropertyTest {

 // Test method for CreatorProperty constructor
 @Test
 public void testCreatorPropertyConstructor() throws JsonMappingException, IOException {
 CreatorProperty creatorProperty = new CreatorProperty(
 PropertyName.construct("testName"),
 JavaType.construct(),
 PropertyName.construct("wrapperName"),
 null,
 null,
 null,
 0,
 null,
 PropertyMetadata.empty());
 assertNotNull(creatorProperty);
 }

 // Test method for CreatorProperty copy constructor
 @Test
 public void testCreatorPropertyCopyConstructor() throws JsonMappingException, IOException {
 CreatorProperty originalCreatorProperty = new CreatorProperty(
 PropertyName.construct("testName"),
 JavaType.construct(),
 PropertyName.construct("wrapperName"),
 null,
 null,
 null,
 0,
 null,
 PropertyMetadata.empty());
 CreatorProperty copiedCreatorProperty = new CreatorProperty(originalCreatorProperty, null);
 assertEquals(originalCreatorProperty, copiedCreatorProperty);
 }

 // Test method for CreatorProperty withValueDeserializer constructor
 @Test
 public void testCreatorPropertyWithValueDeserializerConstructor() throws JsonMappingException, IOException {
 JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
 CreatorProperty creatorProperty = new CreatorProperty(
 PropertyName.construct("testName"),
 JavaType.construct(),
 PropertyName.construct("wrapperName"),
 null,
 null,
 null,
 0,
 null,
 PropertyMetadata.empty(),
 deserializer,
 null);
 assertNotNull(creatorProperty);
 verify(deserializer).deserialize(any(JsonParser.class), any(DeserializationContext.class), any(Object.class));
 }

 // Test method for CreatorProperty withNullProvider constructor
 @Test
 public void testCreatorPropertyWithNullProviderConstructor() throws JsonMappingException, IOException {
 NullValueProvider nullValueProvider = mock(NullValueProvider.class);
 CreatorProperty creatorProperty = new CreatorProperty(
 PropertyName.construct("testName"),
 JavaType.construct(),
 PropertyName.construct("wrapperName"),
 null,
 null,
 null,
 0,
 null,
 PropertyMetadata.empty(),
 null,
 nullValueProvider);
 assertNotNull(creatorProperty);
 }

 // Test method for CreatorProperty setFallbackSetter
 @Test
 public void testCreatorPropertySetFallbackSetter() throws JsonMappingException, IOException {
 CreatorProperty creatorProperty = new CreatorProperty(
 PropertyName.construct("testName"),
 JavaType.construct(),
 PropertyName.construct("wrapperName"),
 null,
 null,
 null,
 0,
 null,
 PropertyMetadata.empty());
 SettableBeanProperty fallbackSetter = mock(SettableBeanProperty.class);
 creatorProperty.setFallbackSetter(fallbackSetter);
 assertEquals(fallbackSetter, creatorProperty._fallbackSetter);
 }

 // Test method for CreatorProperty markAsIgnorable
 @Test
 public void testCreatorPropertyMarkAsIgnorable() {
 CreatorProperty creatorProperty = new CreatorProperty(
 PropertyName.construct("testName"),
 JavaType.construct(),
 PropertyName.construct("wrapperName"),
 null,
 null,
 null,
 0,
 null,
 PropertyMetadata.empty());
 creatorProperty.markAsIgnorable();
 assertTrue(creatorProperty.isIgnorable());
 }

 // Test method for CreatorProperty findInjectableValue
 @Test(expected = JsonMappingException.class)
 public void testCreatorPropertyFindInjectableValueNullInjectableValueId() throws JsonMappingException, IOException {
 DeserializationContext context = mock(DeserializationContext.class);
 CreatorProperty creatorProperty = new CreatorProperty(
 PropertyName.construct("testName"),
 JavaType.construct(),
 PropertyName.construct("wrapperName"),
 null,
 null,
 null,
 0,
 null,
 PropertyMetadata.empty(),
 null,
 null);
 creatorProperty.findInjectableValue(context, null);
 fail("Expected JsonMappingException");
 }

 @Test
 public void testCreatorPropertyFindInjectableValueNotNullInjectableValueId() throws JsonMappingException, IOException {
 DeserializationContext context = mock(DeserializationContext.class);
 Object injectableValueId = new Object();
 CreatorProperty creatorProperty = new CreatorProperty(
 PropertyName.construct("testName"),
 JavaType.construct(),
 PropertyName.construct("wrapperName"),
 null,
 null,
 null,
 0,
 null,
 PropertyMetadata.empty(),
 null,
 null);
 when(context.findInjectableValue(injectableValueId, any(CreatorProperty.class), any(Object.class)))
 .thenReturn(new Object());
 assertEquals(new Object(), creatorProperty.findInjectableValue(context, null));
 }

 // Test method for CreatorProperty inject
 @Test(expected = IOException.class)
 public void testCreatorPropertyInjectNullBeanInstance() throws JsonMappingException, IOException {
 DeserializationContext context = mock(DeserializationContext.class);
 CreatorProperty creatorProperty = new CreatorProperty(
 PropertyName.construct("testName"),
 JavaType.construct(),
 PropertyName.construct("wrapperName"),
 null,
 null,
 null,
 0,
 null,
 PropertyMetadata.empty(),
 null,
 null);
 creatorProperty.inject(context, null);
 fail("Expected IOException");
 }

 @Test
 public void testCreatorPropertyInjectNotNullBeanInstance() throws JsonMappingException, IOException {
 DeserializationContext context = mock(DeserializationContext.class);
 Object beanInstance = new Object();
 Object injectableValue = new Object();
 CreatorProperty creatorProperty = new CreatorProperty(
 PropertyName.construct("testName"),
 JavaType.construct(),
 PropertyName.construct("wrapperName"),
 null,
 null,
 null,
 0,
 null,
 PropertyMetadata.empty(),
 null,
 null);
 when(context.findInjectableValue(_eq(creatorProperty._injectableValueId), any(CreatorProperty.class), any(Object.class)))
 .thenReturn(injectableValue);
 creatorProperty.inject(context, beanInstance);
 assertEquals(injectableValue, creatorProperty.get(beanInstance));
 }

 // Test method for CreatorProperty get
 @Test
 public void testCreatorPropertyGet() throws JsonMappingException, IOException {
 CreatorProperty creatorProperty = new CreatorProperty(
 PropertyName.construct("testName"),
 JavaType.construct(),
 PropertyName.construct("wrapperName"),
 null,
 null,
 null,
 0,
 null,
 PropertyMetadata.empty(),
 null,
 null);
 Object value = new Object();
 creatorProperty.set(null, value);
 assertEquals(value, creatorProperty.get(null));
 }

 // Test method for CreatorProperty set
 @Test(expected = IOException.class)
 public void testCreatorPropertySetNullBeanInstance() throws JsonMappingException, IOException {
 CreatorProperty creatorProperty = new CreatorProperty(
 PropertyName.construct("testName"),
 JavaType.construct(),
 PropertyName.construct("wrapperName"),
 null,
 null,
 null,
 0,
 null,
 PropertyMetadata.empty(),
 null,
 null);
 creatorProperty.set(null, new Object());
 fail("Expected IOException");
 }

 @Test
 public void testCreatorPropertySetNotNullBeanInstance() throws JsonMappingException, IOException {
 CreatorProperty creatorProperty = new CreatorProperty(
 PropertyName.construct("testName"),
 JavaType.construct(),
 PropertyName.construct("wrapperName"),
 null,
 null,
 null,
 0,
 null,
 PropertyMetadata.empty(),
 null,
 null);
 Object beanInstance = new Object();
 Object value = new Object();
 creatorProperty.set(beanInstance, value);
 assertEquals(value, creatorProperty.get(beanInstance));
 }

 // Test method for CreatorProperty setAndReturn
 @Test(expected = IOException.class)
 public void testCreatorPropertySetAndReturnNullBeanInstance() throws JsonMappingException, IOException {
 CreatorProperty creatorProperty = new CreatorProperty(
 PropertyName.construct("testName"),
 JavaType.construct(),
 PropertyName.construct("wrapperName"),
 null,
 null,
 null,
 0,
 null,
 PropertyMetadata.empty(),
 null,
 null);
 Object value = new Object();
 creatorProperty.setAndReturn(null, value);
 fail("Expected IOException");
 }

 @Test
 public void testCreatorPropertySetAndReturnNotNullBeanInstance() throws JsonMappingException, IOException {
 CreatorProperty creatorProperty = new CreatorProperty(
 PropertyName.construct("testName"),
 JavaType.construct(),
 PropertyName.construct("wrapperName"),
 null,
 null,
 null,
 0,
 null,
 PropertyMetadata.empty(),
 null,
 null);
 Object beanInstance = new Object();
 Object value = new Object();
 assertEquals(value, creatorProperty.setAndReturn(beanInstance, value));
 }

 // Test method for CreatorProperty deserializeAndSet
 @Test
 public void testCreatorPropertyDeserializeAndSet() throws JsonMappingException, IOException {
 CreatorProperty creatorProperty = new CreatorProperty(
 PropertyName.construct("testName"),
 JavaType.construct(),
 PropertyName.construct("wrapperName"),
 null,
 null,
 null,
 0,
 null,
 PropertyMetadata.empty(),
 null,
 null);
 JsonParser p = mock(JsonParser.class);
 ObjectInstanceManager objMgr = mock(ObjectInstanceManager.class);
 ObjectMocker mocker = new ObjectMapper().getPolymorphicValueFactory().getObjectMocker();
 when(p.getValueAsToken()).thenReturn(mocker.constructValueToken(new Object()));
 JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
 when(deserializer.deserialize(any(JsonParser.class), any(DeserializationContext.class), any(Object.class)))
 .thenReturn(new Object());
 Object beanInstance = mock(Object.class);
 when(objMgr.findDefaultInstance(JavaType.construct(), any(DeserializationConfig.class)))
 .thenReturn(beanInstance);
 creatorProperty.deserializeAndSet(p, null, beanInstance);
 assertEquals(new Object(), creatorProperty.get(beanInstance));
 }

 // Test method for CreatorProperty deserializeSetAndReturn
 @Test
 public void testCreatorPropertyDeserializeSetAndReturn() throws JsonMappingException, IOException {
 CreatorProperty creatorProperty = new CreatorProperty(
 PropertyName.construct("testName"),
 JavaType.construct(),
 PropertyName.construct("wrapperName"),
 null,
 null,
 null,
 0,
 null,
 PropertyMetadata.empty(),
 null,
 null);
 JsonParser p = mock(JsonParser.class);
 ObjectInstanceManager objMgr = mock(ObjectInstanceManager.class);
 ObjectMocker mocker = new ObjectMapper().getPolymorphicValueFactory().getObjectMocker();
 when(p.getValueAsToken()).thenReturn(mocker.constructValueToken(new Object()));
 JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
 when(deserializer.deserialize(any(JsonParser.class), any(DeserializationContext.class), any(Object.class)))
 .thenReturn(new Object());
 Object beanInstance = mock(Object.class);
 Object returnValue = new Object();
 when(objMgr.findDefaultInstance(JavaType.construct(), any(DeserializationConfig.class)))
 .thenReturn(beanInstance);
 when(creatorProperty._fallbackSetter.setAndReturn(beanInstance, anyObject()))
 .thenReturn(returnValue);
 assertEquals(returnValue, creatorProperty.deserializeSetAndReturn(p, null, beanInstance));
 }

 // Test method for CreatorProperty toString
 @Test
 public void testCreatorPropertyToString() {
 CreatorProperty creatorProperty = new CreatorProperty(
 PropertyName.construct("testName"),
 JavaType.construct(),
 PropertyName.construct("wrapperName"),
 null,
 null,
 null,
 0,
 null,
 PropertyMetadata.empty(),
 null,
 null);
 assertEquals("[creator property, name 'testName'; inject id null]", creatorProperty.toString());
 }
}