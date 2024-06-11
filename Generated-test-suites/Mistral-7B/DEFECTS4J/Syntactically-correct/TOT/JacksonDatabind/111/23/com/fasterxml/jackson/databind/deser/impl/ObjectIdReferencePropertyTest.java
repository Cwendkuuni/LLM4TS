import com.fasterxml.jackson.core.BaseJsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.ObjectIdProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;

@RunWith(MockitoJUnitRunner.class)
public class ObjectIdReferencePropertyTest {

 private ObjectMapper objectMapper;
 @Mock
 private JsonDeserializer<Object> mockDeserializer;
 @Mock
 private NullValueProvider mockNullValueProvider;
 @Mock
 private JsonParser mockJsonParser;
 @Mock
 private DeserializationContext mockContext;
 @Mock
 private ObjectIdProvider mockObjectIdProvider;

 private ObjectIdReferenceProperty property;
 private ObjectIdReferenceProperty forwardProperty;
 private ObjectIdInfo objectIdInfo;

 @Before
 public void setup() throws Exception {
 objectMapper = new ObjectMapper();
 objectMapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
 objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

 objectIdInfo = Mockito.mock(ObjectIdInfo.class);
 forwardProperty = Mockito.mock(ObjectIdReferenceProperty.class);
 property = new ObjectIdReferenceProperty(forwardProperty, mockDeserializer, mockNullValueProvider);
 }

 @Test
 public void testWithName() throws Exception {
 PropertyName newName = PropertyName.construct("newName");
 ObjectIdReferenceProperty newProperty = property.withName(newName);
 assertSame(property, newProperty.getBackReference());
 assertEquals(newName, newProperty.getName());
 }

 @Test
 public void testWithValueDeserializer() throws Exception {
 ObjectIdReferenceProperty newProperty = property.withValueDeserializer(mockDeserializer);
 assertSame(property, newProperty.getBackReference());
 assertSame(mockDeserializer, newProperty.getValueDeserializer());
 }

 @Test
 public void testWithNullProvider() throws Exception {
 ObjectIdReferenceProperty newProperty = property.withNullProvider(mockNullValueProvider);
 assertSame(property, newProperty.getBackReference());
 assertSame(mockNullValueProvider, newProperty.getNullProvider());
 }

 @Test
 public void testDeserializeAndSet() throws Exception {
 ObjectNode jsonNode = objectMapper.createObjectNode();
 JsonNode value = objectMapper.createObjectNode();
 value.put("name", "John Doe");
 jsonNode.put("name", value);

 Object pojo = new Object();
 String expectedName = "John Doe";
 property.setValueDeserializer(new StdDeserializer<>());
 property.setObjectIdInfo(objectIdInfo);
 when(mockContext.findValueDeserializer(eq(Object.class), ArgumentMatchers.eq(ObjectIdInfo.class)))
 .thenReturn(property.getValueDeserializer());
 when(mockObjectIdProvider.findId(ArgumentMatchers.any(JsonParser.class), ArgumentMatchers.any(DeserializationContext.class)))
 .thenReturn("id");

 property.deserializeAndSet(mockJsonParser, mockContext, pojo);
 assertEquals(expectedName, pojo.getClass().getField("name").get(pojo));
 }

 @Test
 public void testDeserializeSetAndReturn() throws Exception {
 Object pojo = new Object();
 String expectedName = "John Doe";
 ObjectIdReferenceProperty newProperty = new ObjectIdReferenceProperty(property, mockDeserializer, mockNullValueProvider) {
 @Override
 public Object deserializeSetAndReturn(JsonParser p, DeserializationContext ctxt, Object instance) throws IOException {
 setAndReturn(instance, expectedName);
 return expectedName;
 }
 };
 property.setValueDeserializer(new PropertyName("name").stdDeserializer(objectMapper));
 when(mockContext.findValueDeserializer(eq(Object.class), ArgumentMatchers.eq(PropertyName.class)))
 .thenReturn(newProperty.getValueDeserializer());
 when(mockJsonParser.getValueAsString())
 .thenReturn("John Doe");

 assertEquals(expectedName, property.deserializeSetAndReturn(mockJsonParser, mockContext, pojo));
 assertEquals(expectedName, pojo.getClass().getField("name").get(pojo));
 }

 @Test
 public void testFixAccess() throws Exception {
 property.fixAccess(mockContext);
 verify(forwardProperty, Mockito.times(1)).fixAccess(mockContext);
 }

 @Test
 public void testGetAnnotation() throws Exception {
 Annotation annotation = Mockito.mock(Annotation.class);
 when(forwardProperty.getAnnotation(Annotation.class))
 .thenReturn(annotation);
 assertEquals(annotation, property.getAnnotation(Annotation.class));
 }

 @Test
 public void testGetMember() throws Exception {
 AnnotatedMember member = Mockito.mock(AnnotatedMember.class);
 when(forwardProperty.getMember())
 .thenReturn(member);
 assertEquals(member, property.getMember());
 }

 @Test
 public void testFixAccessWithNullForwardProperty() throws Exception {
 property.fixAccess(mockContext);
 verify(mockContext, Mockito.never()).findValueDeserializer(ArgumentMatchers.any(Class.class), ArgumentMatchers.eq(ObjectIdInfo.class));
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeAndSetWithNullForwardProperty() throws Exception {
 property.deserializeAndSet(mockJsonParser, mockContext, null);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeAndSetWithNullValue() throws Exception {
 property.setValueDeserializer(new StdDeserializer<>());
 property.deserializeAndSet(mockJsonParser, mockContext, null);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeAndSetWithEmptyValue() throws Exception {
 property.setValueDeserializer(new StdDeserializer<>());
 property.deserializeAndSet(mockJsonParser, mockContext, new Object());
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeSetAndReturnWithNullValue() throws Exception {
 property.setValueDeserializer(new PropertyName("name").stdDeserializer(objectMapper));
 property.deserializeSetAndReturn(mockJsonParser, mockContext, null);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeSetAndReturnWithEmptyValue() throws Exception {
 property.setValueDeserializer(new PropertyName("name").stdDeserializer(objectMapper));
 property.deserializeSetAndReturn(mockJsonParser, mockContext, new Object());
 }

 @Test(expected = UnresolvedForwardReference.class)
 public void testDeserializeAndSetWithUnresolvedForwardReference() throws Exception {
 property.setValueDeserializer(new StdDeserializer<>());
 property.deserializeAndSet(mockJsonParser, mockContext, new Object());
 }

 @Test(expected = JsonMappingException.class)
 public void testSetWithNullValue() throws Exception {
 Object pojo = new Object();
 property.set(pojo, null);
 assertNull(pojo.getClass().getField("name").get(pojo));
 }

 @Test(expected = JsonMappingException.class)
 public void testSetWithEmptyValue() throws Exception {
 Object pojo = new Object();
 property.set(pojo, "");
 assertNull(pojo.getClass().getField("name").get(pojo));
 }

 @Test
 public void testSetWithValue() throws Exception {
 Object pojo = new Object();
 String value = "John Doe";
 property.set(pojo, value);
 assertEquals(value, pojo.getClass().getField("name").get(pojo));
 }

 @Test
 public void testSetAndReturnWithValue() throws Exception {
 Object pojo = new Object();
 String expectedValue = "John Doe";
 Object newValue = new Object();
 when(forwardProperty.setAndReturn(ArgumentMatchers.any(Object.class), ArgumentMatchers.any(Object.class)))
 .thenReturn(newValue);
 property.setAndReturn(pojo, expectedValue);
 assertEquals(newValue, pojo.getClass().getField("name").get(pojo));
 }

 @Test
 public void testSetAndReturnWithNullValue() throws Exception {
 Object pojo = new Object();
 property.setAndReturn(pojo, null);
 assertNull(pojo.getClass().getField("name").get(pojo));
 }

 @Test
 public void testSetAndReturnWithEmptyValue() throws Exception {
 Object pojo = new Object();
 property.setAndReturn(pojo, "");
 assertNull(pojo.getClass().getField("name").get(pojo));
 }

 @Test
 public void testSetAndReturnWithValueAndValueDeserializer() throws Exception {
 Object pojo = new Object();
 String expectedValue = "John Doe";
 ObjectIdReferenceProperty newProperty = new ObjectIdReferenceProperty(property, new StdDeserializer<>()) {
 @Override
 public Object deserializeSetAndReturn(JsonParser p, DeserializationContext ctxt, Object instance) throws IOException {
 setAndReturn(instance, expectedValue);
 return expectedValue;
 }
 };
 property.setValueDeserializer(newProperty);
 property.setAndReturn(pojo, "John Doe");
 assertEquals(expectedValue, pojo.getClass().getField("name").get(pojo));
 }

 @Test
 public void testSetAndReturnWithValueAndNullValueProvider() throws Exception {
 Object pojo = new Object();
 String expectedValue = "John Doe";
 ObjectIdReferenceProperty newProperty = new ObjectIdReferenceProperty(property, null) {
 @Override
 public Object deserializeSetAndReturn(JsonParser p, DeserializationContext ctxt, Object instance) throws IOException {
 setAndReturn(instance, expectedValue);
 return expectedValue;
 }
 };
 property.setValueDeserializer(null);
 property.setAndReturn(pojo, "John Doe");
 assertEquals(expectedValue, pojo.getClass().getField("name").get(pojo));
 }

 @Test
 public void testSetAndReturnWithValueAndValueDeserializerAndNullValueProvider() throws Exception {
 Object pojo = new Object();
 String expectedValue = "John Doe";
 ObjectIdReferenceProperty newProperty = new ObjectIdReferenceProperty(property, new StdDeserializer<>(), null) {
 @Override
 public Object deserializeSetAndReturn(JsonParser p, DeserializationContext ctxt, Object instance) throws IOException {
 setAndReturn(instance, expectedValue);
 return expectedValue;
 }
 };
 property.setValueDeserializer(newProperty);
 property.setValueDeserializer(null);
 property.setAndReturn(pojo, "John Doe");
 assertEquals(expectedValue, pojo.getClass().getField("name").get(pojo));
 }
}