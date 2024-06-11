package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import static com.fasterxml.jackson.databind.JsonMapping.BuilderPattern.STANDARD_PROPERTY_NAMING;

public class AsPropertyTypeDeserializerTest {

 private AsPropertyTypeDeserializer deserializer;
 private TypeIdResolver typeIdResolver;
 private ObjectMapper objectMapper;
 private JsonParser mockJsonParser;
 private DeserializationContext mockContext;

 @Before
 public void setup() {
 typeIdResolver = mock(TypeIdResolver.class);
 objectMapper = new ObjectMapper();
 objectMapper.setSerializerFactory(objectMapper.getDefaultSerializerFactory());
 objectMapper.setTypeIdResolver(typeIdResolver);
 mockJsonParser = mock(JsonParser.class);
 mockContext = mock(DeserializationContext.class);

 deserializer = new AsPropertyTypeDeserializer(
 new SimpleType(Object.class), typeIdResolver, "_typePropertyName", true, null, As.PROPERTY);
 }

 @Test
 public void testDeserializeTypedFromObjectWithNativeTypeId() throws Exception {
 when(mockJsonParser.canReadTypeId()).thenReturn(true);
 when(mockJsonParser.getTypeId()).thenReturn("com.test.MyClass");

 Object result = deserializer.deserializeTypedFromObject(mockJsonParser, mockContext);

 verify(mockJsonParser).nextToken();
 verify(mockJsonParser).nextToken();
 verify(mockJsonParser).nextToken(); // to skip past String value

 TypeIdResolver typeIdResolverSpy = spy(typeIdResolver);
 doReturn(typeIdResolverSpy).when(deserializer)._findDeserializer(mockContext, "com.test.MyClass");
 Object deserializedObject = new Object();
 doReturn(deserializedObject).when(typeIdResolverSpy).deserializeValue(mockContext, typeFromString("com.test.MyClass"), null);

 assertEquals(deserializedObject, result);
 }

 @Test(expected = IOException.class)
 public void testDeserializeTypedFromObjectWithoutTypeId() throws Exception {
 when(mockJsonParser.canReadTypeId()).thenReturn(false);
 when(mockJsonParser.getText()).thenReturn("MyClass");

 deserializer.deserializeTypedFromObject(mockJsonParser, mockContext);

 verify(mockContext).reportWrongTokenException(mockJsonParser, JsonToken.FIELD_NAME,
 "missing property '_typePropertyName' that is to contain type id  (for class Object)");
 }

 @Test
 public void testDeserializeTypedFromObjectFailure() throws Exception {
 when(mockJsonParser.getText()).thenReturn("invalidTypeId");

 try {
 deserializer.deserializeTypedFromObject(mockJsonParser, mockContext);
 } catch (IOException e) {
 //expected
 }

 verify(mockContext).reportWrongTokenException(mockJsonParser, JsonToken.FIELD_NAME,
 "missing property '_typePropertyName' that is to contain type id  (for class Object)");
 }

 @Test
 public void testDeserializeTypedFromObjectDefaultImpl() throws Exception {
 when(mockJsonParser.canReadTypeId()).thenReturn(false);
 when(mockJsonParser.getText()).thenReturn("MyClass");
 when(mockContext.findValueDeserializer(eq(Object.class), anyString())).thenReturn(mock(JsonDeserializer.class));

 Object result = deserializer.deserializeTypedFromObject(mockJsonParser, mockContext);

 verify(mockJsonParser).nextToken();
 verify(mockJsonParser).nextToken(); // to skip past String value
 verify(mockJsonParser).nextToken(); // to skip past opening bracket of JSON object
 verify(mockJsonParser).nextToken(); // to skip past field name of JSON object
 verify(mockJsonParser).nextToken(); // to skip past closing bracket of JSON object
 verify(mockJsonParser).nextToken(); // to skip past opening bracket of JSON array
 verify(mockJsonParser).nextToken(); // to skip past value of JSON array
 verify(mockJsonParser).nextToken(); // to skip past closing bracket of JSON array

 JsonDeserializer<Object> deserializedObject = mock(JsonDeserializer.class);
 doReturn(deserializedObject).when(mockContext).findValueDeserializer(eq(Object.class), anyString());
 doReturn(deserializedObject).when(deserializedObject).deserialize(mockJsonParser, mockContext);

 assertEquals(deserializedObject, result);
 }

 @Test
 public void testDeserializeTypedFromObjectPropertyNotFound() throws Exception {
 when(mockJsonParser.getText()).thenReturn("_typePropertyName");
 when(mockJsonParser.nextToken()).thenReturn(JsonToken.START_OBJECT);
 when(mockJsonParser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
 when(mockJsonParser.getCurrentName()).thenReturn("nonExistentPropertyName");
 when(mockJsonParser.nextToken()).thenReturn(JsonToken.END_OBJECT);

 deserializer.deserializeTypedFromObject(mockJsonParser, mockContext);

 verify(mockContext).reportWrongTokenException(mockJsonParser, JsonToken.FIELD_NAME,
 "missing property '_typePropertyName' that is to contain type id  (for class Object)");
 }

 @Test
 public void testDeserializeTypedFromObjectPropertyFound() throws Exception {
 when(mockJsonParser.getText()).thenReturn("_typePropertyName");
 when(mockJsonParser.nextToken()).thenReturn(JsonToken.START_OBJECT);
 when(mockJsonParser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
 when(mockJsonParser.getCurrentName()).thenReturn("typePropertyName");
 when(mockJsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(mockJsonParser.getText()).thenReturn("MyClass");
 when(mockJsonParser.nextToken()).thenReturn(JsonToken.END_OBJECT);

 Object result = deserializer.deserializeTypedFromObject(mockJsonParser, mockContext);

 TypeIdResolver typeIdResolverSpy = spy(typeIdResolver);
 doReturn(typeIdResolverSpy).when(deserializer)._findDeserializer(mockContext, "MyClass");
 Object deserializedObject = new Object();
 doReturn(deserializedObject).when(typeIdResolverSpy).deserializeValue(mockContext, typeFromString("MyClass"), null);

 assertEquals(deserializedObject, result);
 }

 @Test
 public void testDeserializeTypedFromAny() throws Exception {
 when(mockJsonParser.getText()).thenReturn("MyClass");

 Object result = deserializer.deserializeTypedFromAny(mockJsonParser, mockContext);

 TypeIdResolver typeIdResolverSpy = spy(typeIdResolver);
 doReturn(typeIdResolverSpy).when(deserializer)._findDeserializer(mockContext, "MyClass");
 Object deserializedObject = new Object();
 doReturn(deserializedObject).when(typeIdResolverSpy).deserializeValue(mockContext, typeFromString("MyClass"), null);

 assertEquals(deserializedObject, result);
 }

 @Test(expected = IOException.class)
 public void testDeserializeTypedFromAnyWithoutTypeId() throws Exception {
 when(mockJsonParser.getText()).thenReturn("MyClass");

 deserializer.deserializeTypedFromAny(mockJsonParser, mockContext);

 verify(mockContext).reportWrongTokenException(mockJsonParser, JsonToken.FIELD_NAME,
 "missing property '_typePropertyName' that is to contain type id  (for class Object)");
 }

 @Test
 public void testDeserializeTypedFromAnyFailure() throws Exception {
 when(mockJsonParser.getText()).thenReturn("invalidTypeId");

 try {
 deserializer.deserializeTypedFromAny(mockJsonParser, mockContext);
 } catch (IOException e) {
 //expected
 }

 verify(mockContext).reportWrongTokenException(mockJsonParser, JsonToken.FIELD_NAME,
 "missing property '_typePropertyName' that is to contain type id  (for class Object)");
 }

 @Test
 public void testDeserializeTypedFromArray() throws Exception {
 when(mockJsonParser.getText()).thenReturn("[");
 when(mockJsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);
 when(mockJsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(mockJsonParser.getText()).thenReturn("MyClass");
 when(mockJsonParser.nextToken()).thenReturn(JsonToken.END_ARRAY);

 Object result = deserializer.deserializeTypedFromArray(mockJsonParser, mockContext);

 TypeIdResolver typeIdResolverSpy = spy(typeIdResolver);
 doReturn(typeIdResolverSpy).when(deserializer)._findDeserializer(mockContext, "MyClass");
 Object deserializedObject = new Object();
 doReturn(deserializedObject).when(typeIdResolverSpy).deserializeValue(mockContext, typeFromString("MyClass"), null);

 assertEquals(deserializedObject, result);
 }

 @Test(expected = IOException.class)
 public void testDeserializeTypedFromArrayWithoutTypeId() throws Exception {
 when(mockJsonParser.getText()).thenReturn("[");
 when(mockJsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);
 when(mockJsonParser.nextToken()).thenReturn(JsonToken.VALUE_NULL);
 when(mockJsonParser.nextToken()).thenReturn(JsonToken.END_ARRAY);

 deserializer.deserializeTypedFromArray(mockJsonParser, mockContext);

 verify(mockContext).reportWrongTokenException(mockJsonParser, JsonToken.FIELD_NAME,
 "missing property '_typePropertyName' that is to contain type id  (for class Object)");
 }

 @Test
 public void testDeserializeTypedFromArrayFailure() throws Exception {
 when(mockJsonParser.getText()).thenReturn("[");
 when(mockJsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);
 when(mockJsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(mockJsonParser.getText()).thenReturn("invalidTypeId");
 when(mockJsonParser.nextToken()).thenReturn(JsonToken.END_ARRAY);

 try {
 deserializer.deserializeTypedFromArray(mockJsonParser, mockContext);
 } catch (IOException e) {
 //expected
 }

 verify(mockContext).reportWrongTokenException(mockJsonParser, JsonToken.FIELD_NAME,
 "missing property '_typePropertyName' that is to contain type id  (for class Object)");
 }

 @Test
 public void testFindDeserializer() throws Exception {
 when(mockContext.findValueDeserializer(eq(Object.class), anyString())).thenReturn(mock(JsonDeserializer.class));

 Object result = deserializer.findDeserializer(mockContext, "MyClass");

 assertEquals(mock(JsonDeserializer.class), result);
 }

 @Test
 public void testFindDefaultImplDeserializer() throws Exception {
 when(mockContext.findValueDeserializer(eq(Object.class), anyString())).thenReturn(null);
 when(mockContext.findSerializer(eq(Object.class))).thenReturn(new SimpleSerializer(Object.class, STANDARD_PROPERTY_NAMING));

 JsonDeserializer<Object> result = deserializer.findDefaultImplDeserializer(mockContext);

 assertEquals(mock(SimpleSerializer.class).getDeserializationContext().getDeserializer(), result);
 }

 @Test
 public void testIsPropertyName() throws Exception {
 assertTrue(deserializer.isPropertyName("_typePropertyName"));
 assertFalse(deserializer.isPropertyName("nonExistentPropertyName"));
 }
}