package com.example.test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StringCollectionDeserializerTest {

 @Test
 public void testStringCollectionDeserializer() throws Exception {
 StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
 JavaType.constructCollectionType(List.class, String.class),
 null,
 null,
 null,
 null,
 false
 );

 // Test default behavior
 ObjectMapper mapper = new ObjectMapper();
 JsonParser parser = mapper.getFactory().createParser("[\"abc\",\"def\"]");
 List<String> list = deserializer.deserialize(parser, mapper.getDeserializationContext());
 assertEquals(2, list.size());
 assertEquals("abc", list.get(0));
 assertEquals("def", list.get(1));

 // Test custom value deserializer
 JsonDeserializer<String> valueDeserializer = new StdDeserializer<>(String.class);
 deserializer = new StringCollectionDeserializer(
 JavaType.constructCollectionType(List.class, String.class),
 null,
 null,
 valueDeserializer,
 null,
 false
 );
 parser = mapper.getFactory().createParser("[\"[123]\",\"def\"]");
 list = deserializer.deserialize(parser, mapper.getDeserializationContext());
 assertEquals(2, list.size());
 assertEquals("[123]", list.get(0));
 assertEquals("def", list.get(1));

 // Test delegate deserializer
 JsonDeserializer<Object> delegateDeserializer = new StandardDeserializer<>(Object.class);
 deserializer = new StringCollectionDeserializer(
 JavaType.constructCollectionType(List.class, String.class),
 null,
 delegateDeserializer,
 null,
 null,
 false
 );
 JsonNode node = mapper.readTree("{\"foo\":[\"abc\",\"def\"]}");
 deserializer = deserializer.withResolved(delegateDeserializer, null, null, false);
 list = deserializer.deserialize(parser, mapper.getDeserializationContext());
 assertEquals(2, list.size());
 assertEquals("abc", list.get(0));
 assertEquals("def", list.get(1));
 }

 @Test
 public void testIsCachable() throws Exception {
 StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
 JavaType.constructCollectionType(List.class, String.class),
 null,
 null,
 null,
 null,
 false
 );
 assertFalse(deserializer.isCachable());
 }

 @Test
 public void testCreateContextual() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
 JavaType.constructCollectionType(List.class, String.class),
 null,
 null,
 null,
 null,
 false
 );
 BeanProperty property = new BeanProperty("strings", JavaType.constructCollectionType(List.class, String.class), null);
 JsonDeserializer<?> deserializerFromCreateContextual = deserializer.createContextual(mapper.getDeserializationContext(), property);
 assertEquals(deserializer.getClass(), deserializerFromCreateContextual.getClass());
 }

 @Test
 public void testGetContentDeserializer() throws Exception {
 StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
 JavaType.constructCollectionType(List.class, String.class),
 null,
 null,
 null,
 null,
 false
 );
 JsonDeserializer<Object> contentDeserializer = deserializer.getContentDeserializer();
 assertEquals(deserializer.getClass().getSuperclass(), contentDeserializer.getClass().getSuperclass());
 assertEquals(StringDeserializer.class, contentDeserializer.getClass().getSuperclass().getSuperclass());
 }

 @Test
 public void testGetValueInstantiator() throws Exception {
 StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
 JavaType.constructCollectionType(List.class, String.class),
 null,
 null,
 null,
 null,
 false
 );
 ValueInstantiator valueInstantiator = deserializer.getValueInstantiator();
 assertEquals(List.class, valueInstantiator.getValueType(null));
 }
}