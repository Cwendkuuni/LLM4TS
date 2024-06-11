import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader.IdType;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.introspect.NestedObjectIntrospector;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.BeanUtil;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ObjectIdValuePropertyTest {

 private ObjectIdReader mockObjectIdReader;
 private BeanPropertyDefinition mockBeanPropertyDefinition;
 private JsonDeserializer<Object> mockValueDeserializer;
 private ObjectIdValueProperty objectIdValueProperty;
 private JsonParser mockJsonParser;
 private DeserializationContext mockDeserializationContext;
 private Object instance;

 @Before
 public void setup() throws InstantiationException, IllegalAccessException {
 mockObjectIdReader = mock(ObjectIdReader.class);
 mockBeanPropertyDefinition = mock(BeanPropertyDefinition.class);
 mockValueDeserializer = mock(JsonDeserializer.class);
 mockJsonParser = mock(JsonParser.class);
 mockDeserializationContext = mock(DeserializationContext.class);

 when(mockObjectIdReader.propertyName).thenReturn("id");
 when(mockObjectIdReader.getIdType()).thenReturn(IdType.STRING);
 when(mockObjectIdReader.getDeserializer()).thenReturn(mockValueDeserializer);
 when(mockObjectIdReader.generator).thenReturn(new ObjectIdGenerator());
 when(mockObjectIdReader.resolver).thenReturn(new ObjectIdResolver());
 when(mockObjectIdReader.idProperty).thenReturn(new StdSerializer<Object>(Object.class) {});

 instance = new HashMap<>();
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 String id = "123";
 when(mockValueDeserializer.deserialize(mockJsonParser, mockDeserializationContext))
         .thenReturn(id);

 ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(mockObjectIdReader, null);
 objectIdValueProperty.deserializeAndSet(mockJsonParser, mockDeserializationContext, instance);

 Object idValue = _getFieldValue(instance, "id");
 assertEquals(id, idValue);
 }

 @Test
 public void testDeserializeSetAndReturn() throws IOException {
 String id = "123";
 when(mockValueDeserializer.deserialize(mockJsonParser, mockDeserializationContext))
         .thenReturn(id);

 Object result = objectIdValueProperty.deserializeSetAndReturn(mockJsonParser, mockDeserializationContext, instance);
 assertEquals(instance, result);
 }

 @Test
 public void testSetAndReturn() throws IOException {
 String id = "123";
 ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(mockObjectIdReader, null);
 objectIdValueProperty.setAndReturn(instance, id);

 Object idValue = _getFieldValue(instance, "id");
 assertEquals(id, idValue);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetWithoutIdProperty() throws IOException {
 ObjectIdReader mockObjectIdReaderWithoutIdProperty = mock(ObjectIdReader.class);
 ObjectIdValueProperty objectIdValuePropertyWithoutIdProperty = new ObjectIdValueProperty(mockObjectIdReaderWithoutIdProperty, null);

 objectIdValuePropertyWithoutIdProperty.setAndReturn(instance, "id");
 }

 private Object _getFieldValue(Object instance, String fieldName) {
 try {
 Field field = instance.getClass().getDeclaredField(fieldName);
 field.setAccessible(true);
 return field.get(instance);
 } catch (NoSuchFieldException e) {
 throw new RuntimeException(e);
 } catch (IllegalAccessException e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testNullId() throws IOException {
 when(mockValueDeserializer.deserialize(mockJsonParser, mockDeserializationContext)).thenReturn(null);

 ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(mockObjectIdReader, null);
 objectIdValueProperty.deserializeAndSet(mockJsonParser, mockDeserializationContext, instance);

 assertNull(_getFieldValue(instance, "id"));
 }

 @Test
 public void testNotFoundIdProperty() throws IOException {
 ObjectIdReader mockObjectIdReaderNotFoundProperty = mock(ObjectIdReader.class);
 ObjectIdValueProperty objectIdValuePropertyNotFoundProperty = new ObjectIdValueProperty(mockObjectIdReaderNotFoundProperty, null);

 NestedObjectIntrospector introspector = new NestedObjectIntrospector(new AnnotationIntrospector(), new ObjectMapper());
 BeanPropertyDefinition idPropertyDefinition = introspector.findBeanPropertyDefinition("nonExistentProperty", instance.getClass());
 ObjectIdReader idReader = new ObjectIdReader() {
 @Override
 public PropertyName propertyName() {
 return idPropertyDefinition.getName();
 }

 @Override
 public IdType getIdType() {
 return IdType.STRING;
 }

 @Override
 public JsonDeserializer<?> getDeserializer() {
 return mockValueDeserializer;
 }

 @Override
 public ObjectIdGenerator generator() {
 return null;
 }

 @Override
 public ObjectIdResolver resolver() {
 return null;
 }

 @Override
 public SettableBeanProperty idProperty() {
 return null;
 }
 };
 ObjectIdValueProperty objectIdValuePropertyNotFoundProperty = new ObjectIdValueProperty(idReader, null);

 objectIdValuePropertyNotFoundProperty.deserializeAndSet(mockJsonParser, mockDeserializationContext, instance);

 assertNull(_getFieldValue(instance, "nonExistentProperty"));
 }
}