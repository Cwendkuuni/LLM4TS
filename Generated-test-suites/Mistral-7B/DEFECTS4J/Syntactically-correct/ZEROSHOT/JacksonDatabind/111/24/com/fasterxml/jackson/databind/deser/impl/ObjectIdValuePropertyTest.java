package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.annotation.Annotation;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.node.ObjectIdReader.IdType;
import com.fasterxml.jackson.databind.ser.std.StringSerializer;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.fasterxml.jackson.databind.util.NameTransformer;

public class ObjectIdValuePropertyTest {

private ObjectIdReader mockObjectIdReader;
private BeanPropertyDefinition mockBeanPropertyDefinition;
private SettableBeanProperty objectIdValueProperty;

@Before
public void setUp() throws Exception {
mockObjectIdReader = Mockito.mock(ObjectIdReader.class);
mockBeanPropertyDefinition = Mockito.mock(BeanPropertyDefinition.class);
ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(mockObjectIdReader,
mockBeanPropertyDefinition);
this.objectIdValueProperty = objectIdValueProperty;
}

@Test
public void testConstructorWithObjectIdReaderAndPropertyMetadata() {
// Test constructor with ObjectIdReader and PropertyMetadata
ObjectIdValueProperty constructedObjectIdValueProperty = new ObjectIdValueProperty(mockObjectIdReader,
new PropertyMetadata());
assertNotNull(constructedObjectIdValueProperty);
assertEquals(mockObjectIdReader.propertyName, constructedObjectIdValueProperty.getName());
assertEquals(mockObjectIdReader.getIdType(), constructedObjectIdValueProperty.getType());
assertEquals(mockObjectIdReader.getDeserializer(), constructedObjectIdValueProperty.getDeserializer());
assertEquals(mockObjectIdReader, constructedObjectIdValueProperty._objectIdReader);
}

@Test
public void testConstructorWithCopyAndName() {
// Test copy constructor
ObjectIdValueProperty srcObjectIdValueProperty = this.objectIdValueProperty;
ObjectIdValueProperty copyObjectIdValueProperty = new ObjectIdValueProperty(srcObjectIdValueProperty,
ArgumentMatchers.any(JsonDeserializer.class), ArgumentMatchers.any(NullValueProvider.class));
assertEquals(srcObjectIdValueProperty, copyObjectIdValueProperty);
assertEquals(srcObjectIdValueProperty._objectIdReader, copyObjectIdValueProperty._objectIdReader);

// Test constructor with name
ObjectIdValueProperty nameChangedObjectIdValueProperty = new ObjectIdValueProperty(srcObjectIdValueProperty,
new PropertyName("newName"));
assertNotEquals(srcObjectIdValueProperty, nameChangedObjectIdValueProperty);
assertEquals("newName", nameChangedObjectIdValueProperty.getName());
assertEquals(srcObjectIdValueProperty._objectIdReader, nameChangedObjectIdValueProperty._objectIdReader);
}

@Test
public void testGetAnnotation() {
// Test getAnnotation
assertNull(objectIdValueProperty.getAnnotation(String.class));
}

@Test
public void testGetMember() {
// Test getMember
assertNull(objectIdValueProperty.getMember());
}

@Test
public void testDeserializeAndSet() throws IOException {
// Test deserializeAndSet
JsonParser mockJsonParser = Mockito.mock(JsonParser.class);
DeserializationContext mockContext = Mockito.mock(DeserializationContext.class);
Object instance = new Object();
ObjectIdValueProperty deserializedObjectIdValueProperty = new ObjectIdValueProperty(mockObjectIdReader,
mockBeanPropertyDefinition);
deserializedObjectIdValueProperty.deserializeAndSet(mockJsonParser, mockContext, instance);
assertEquals(instance, deserializedObjectIdValueProperty.getDeserializer().deserialize(mockJsonParser,
mockContext));
}

@Test
public void testDeserializeSetAndReturn() throws IOException {
// Test deserializeSetAndReturn
JsonParser mockJsonParser = Mockito.mock(JsonParser.class);
DeserializationContext mockContext = Mockito.mock(DeserializationContext.class);
Object id = "id";
JsonDeserializer mockJsonDeserializer = Mockito.mock(JsonDeserializer.class);
Mockito.when(mockJsonDeserializer.deserialize(ArgumentMatchers.any(JsonParser.class),
ArgumentMatchers.any(DeserializationContext.class))).thenReturn(id);
Mockito.when(mockObjectIdReader.idProperty).thenReturn(Mockito.mock(SettableBeanProperty.class));
ObjectIdValueProperty deserializedObjectIdValueProperty = new ObjectIdValueProperty(mockObjectIdReader,
mockBeanPropertyDefinition);
Object result = deserializedObjectIdValueProperty.deserializeSetAndReturn(mockJsonParser, mockContext,
instance);
assertEquals(result, id);
}

@Test
public void testSetAndReturn() throws IOException {
// Test setAndReturn
Object instance = new Object();
Object value = "value";
SettableBeanProperty idProperty = Mockito.mock(SettableBeanProperty.class);
Mockito.when(mockObjectIdReader.idProperty).thenReturn(idProperty);
ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(mockObjectIdReader,
mockBeanPropertyDefinition);
Object result = objectIdValueProperty.setAndReturn(instance, value);
assertEquals(result, value);
assertEquals(value, idProperty.get(instance));
}

@Test
public void testWithName() {
// Test withName
PropertyName newName = new PropertyName("newName");
ObjectIdValueProperty newNameObjectIdValueProperty = objectIdValueProperty.withName(newName);
assertEquals(newName, newNameObjectIdValueProperty.getName());
assertEquals(objectIdValueProperty, newNameObjectIdValueProperty);
}

@Test
public void testWithValueDeserializer() {
// Test withValueDeserializer
JsonDeserializer mockJsonDeserializer = Mockito.mock(JsonDeserializer.class);
ObjectIdValueProperty jsonDeserializerObjectIdValueProperty = new ObjectIdValueProperty(objectIdValueProperty,
mockJsonDeserializer, ArgumentMatchers.any(NullValueProvider.class));
assertEquals(mockJsonDeserializer, jsonDeserializerObjectIdValueProperty.getDeserializer());
assertEquals(objectIdValueProperty, jsonDeserializerObjectIdValueProperty);
}

@Test
public void testWithNullProvider() {
// Test withNullProvider
NullValueProvider nullValueProvider = Mockito.mock(NullValueProvider.class);
ObjectIdValueProperty nullValueProviderObjectIdValueProperty = new ObjectIdValueProperty(objectIdValueProperty,
ArgumentMatchers.any(JsonDeserializer.class), nullValueProvider);
assertEquals(nullValueProvider, nullValueProviderObjectIdValueProperty._nullProvider);
assertEquals(objectIdValueProperty, nullValueProviderObjectIdValueProperty);
}

@Test
public void testSerialization() throws IOException {
// Test serialization
ObjectMapper objectMapper = new ObjectMapper();
ObjectWriter objectWriter = objectMapper.writer();
ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(mockObjectIdReader,
mockBeanPropertyDefinition);
ObjectReader mockObjectReader = Mockito.mock(ObjectReader.class);
JsonFactory jsonFactory = objectMapper.getFactory();
Mockito.when(mockObjectIdReader.getIdType()).thenReturn(IdType.INT_ID);
Mockito.when(mockObjectReader.getDeserializer()).thenReturn(Mockito.mock(JsonDeserializer.class));
Mockito.when(mockObjectIdReader.getPropertyName()).thenReturn("propertyName");
Mockito.when(mockObjectIdReader.getIdProperty()).thenReturn(Mockito.mock(SettableBeanProperty.class));
ObjectNode objectNode = objectWriter.writeValueAsTree(objectIdValueProperty);
assertEquals("{\"className\":\"com.fasterxml.jackson.databind.deser.impl.ObjectIdValueProperty\",\"idType\":\"INT_ID\",\"name\":\"propertyName\",\"propertyName\":\"propertyName\"}",
objectNode.toString());
}
}