package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectBuffer;
import com.fasterxml.jackson.databind.TypeDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer.EmptyTypeDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializers.StringSerializer;

public class StringArrayDeserializerTest {

 private StringArrayDeserializer stringArrayDeserializer;
 private JsonParser jsonParserMock;
 private DeserializationContext deserializationContextMock;
 private ObjectBuffer objectBufferMock;
 private JsonDeserializer<String> jsonDeserializerMock;
 private TypeDeserializer typeDeserializerMock;

 @Before
 public void setUp() {
 stringArrayDeserializer = new StringArrayDeserializer();
 jsonParserMock = mock(JsonParser.class);
 deserializationContextMock = mock(DeserializationContext.class);
 objectBufferMock = mock(ObjectBuffer.class);
 jsonDeserializerMock = mock(JsonDeserializer.class);
 typeDeserializerMock = mock(TypeDeserializer.class);
 }

 @Test
 public void testDeserializeWithDefaultElementDeserializer() throws IOException, JsonMappingException {
 when(jsonParserMock.nextToken()).thenReturn(JsonToken.START_ARRAY);
 when(jsonParserMock.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(jsonParserMock.getText()).thenReturn("element1");
 when(jsonParserMock.nextToken()).thenReturn(JsonToken.VALUE_NULL);
 when(jsonParserMock.nextToken()).thenReturn(JsonToken.END_ARRAY);
 when(deserializationContextMock.leaseObjectBuffer()).thenReturn(objectBufferMock);

 String[] result = stringArrayDeserializer.deserialize(jsonParserMock, deserializationContextMock);
 assertArrayEquals(new String[]{"element1", null}, result);
 }

 @Test
 public void testDeserializeWithCustomElementDeserializer() throws IOException, JsonMappingException {
 String[] expectedResult = new String[]{"customElement1", "customElement2"};
 when(jsonParserMock.nextToken()).thenReturn(JsonToken.START_ARRAY);
 when(jsonParserMock.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(jsonParserMock.getText()).thenReturn("customElement1");
 when(jsonParserMock.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(jsonParserMock.getText()).thenReturn("customElement2");
 when(jsonParserMock.nextToken()).thenReturn(JsonToken.END_ARRAY);
 when(deserializationContextMock.leaseObjectBuffer()).thenReturn(objectBufferMock);
 when(deserializationContextMock.findValueDeserializer(String.class)).thenReturn(jsonDeserializerMock);
 when(jsonDeserializerMock.deserialize(jsonParserMock, deserializationContextMock)).thenReturn("customElement1");
 when(jsonDeserializerMock.deserialize(jsonParserMock, deserializationContextMock)).thenReturn("customElement2");

 StringArrayDeserializer contextualDeserializer = new StringArrayDeserializer(jsonDeserializerMock);
 String[] result = contextualDeserializer.deserialize(jsonParserMock, deserializationContextMock);
 assertArrayEquals(expectedResult, result);
 }

 @Test
 public void testDeserializeWithNullElementDeserializer() throws IOException, JsonMappingException {
 String[] expectedResult = new String[]{"nullElement1", null};
 when(jsonParserMock.nextToken()).thenReturn(JsonToken.START_ARRAY);
 when(jsonParserMock.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(jsonParserMock.getText()).thenReturn("nullElement1");
 when(jsonParserMock.nextToken()).thenReturn(JsonToken.VALUE_NULL);
 when(jsonParserMock.nextToken()).thenReturn(JsonToken.END_ARRAY);
 when(deserializationContextMock.leaseObjectBuffer()).thenReturn(objectBufferMock);
 when(deserializationContextMock.isEnabled(DeserializationFeature.FAIL_ON_EMPTY_BEANS)).thenReturn(false);
 when(deserializationContextMock.findValueDeserializer(String.class)).thenReturn(null);
 when(deserializationContextMock.handleSecondaryContextualization(null, mock(BeanProperty.class))).thenReturn(jsonDeserializerMock);
 when(jsonDeserializerMock.deserialize(jsonParserMock, deserializationContextMock)).thenReturn("nullElement1");

 StringArrayDeserializer contextualDeserializer = new StringArrayDeserializer();
 String[] result = contextualDeserializer.deserialize(jsonParserMock, deserializationContextMock);
 assertArrayEquals(expectedResult, result);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeWithInvalidToken() throws IOException, JsonMappingException {
 when(jsonParserMock.nextToken()).thenReturn(JsonToken.VALUE_NUMBER);
 StringArrayDeserializer stringArrayDeserializer = new StringArrayDeserializer();
 stringArrayDeserializer.deserialize(jsonParserMock, deserializationContextMock);
 }

 @Test
 public void testDeserializeWithTypeDeserializer() throws IOException, JsonMappingException {
 String[] expectedResult = new String[]{"typeElement1", "typeElement2"};
 when(jsonParserMock.nextToken()).thenReturn(JsonToken.START_ARRAY);
 when(jsonParserMock.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(jsonParserMock.getText()).thenReturn("typeElement1");
 when(jsonParserMock.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(jsonParserMock.getText()).thenReturn("typeElement2");
 when(jsonParserMock.nextToken()).thenReturn(JsonToken.END_ARRAY);
 when(deserializationContextMock.leaseObjectBuffer()).thenReturn(objectBufferMock);
 when(deserializationContextMock.findContextualValueDeserializer(String[].class, mock(BeanProperty.class))).thenReturn(stringArrayDeserializer);
 when(typeDeserializerMock.deserializeTypedFromArray(jsonParserMock, deserializationContextMock)).thenReturn(expectedResult);

 String[] result = stringArrayDeserializer.deserializeWithType(jsonParserMock, deserializationContextMock, typeDeserializerMock);
 assertArrayEquals(expectedResult, result);
 }

 @Test
 public void testHandleNonArray() throws IOException, JsonMappingException {
 when(jsonParserMock.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(jsonParserMock.getText()).thenReturn("singleElement");
 StringArrayDeserializer stringArrayDeserializer = new StringArrayDeserializer();
 String[] result = stringArrayDeserializer.handleNonArray(jsonParserMock, deserializationContextMock);
 assertArrayEquals(new String[]{"singleElement"}, result);
 }
}