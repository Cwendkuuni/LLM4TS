package com.fasterxml.jackson.databind.ser.test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonAnyGetter;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.MapSerializer;
import com.fasterxml.jackson.databind.ser.std.MapSerializer.StandardSerializer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class AnyGetterWriterTest {

 private ObjectMapper objectMapper;
 private BeanProperty property;
 private AnnotatedMember accessor;
 private MapSerializer mapSerializer;
 private AnyGetterWriter anyGetterWriter;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNakeCaseStrategy.INSTANCE);
 property = Mockito.mock(BeanProperty.class);
 accessor = Mockito.mock(AnnotatedMember.class);
 mapSerializer = new MapSerializer(StandardSerializer.STANDARD_MAP_SERIALIZER, null, null, null, null, null);
 anyGetterWriter = new AnyGetterWriter(property, accessor, mapSerializer);
 }

 @Test
 public void testGetAndSerialize() throws JsonGenerationException, IOException {
 JsonGenerator gen = Mockito.mock(JsonGenerator.class);
 SerializerProvider provider = Mockito.mock(SerializerProvider.class);
 Object bean = new HashMap<String, String>() {{
 put("key1", "value1");
 put("key2", "value2");
 }};

 when(accessor.getValue(bean)).thenReturn(bean);
 anyGetterWriter.getAndSerialize(bean, gen, provider);

 verify(gen, times(1)).writeStartObject();
 verify(gen, times(1)).writeFieldName("key1");
 verify(gen, times(1)).writeString("value1");
 verify(gen, times(1)).writeFieldName("key2");
 verify(gen, times(1)).writeString("value2");
 verify(gen, times(1)).writeEndObject();
 }

 @Test(expected = JsonMappingException.class)
 public void testGetAndSerializeWithInvalidValue() throws JsonGenerationException, IOException {
 JsonGenerator gen = Mockito.mock(JsonGenerator.class);
 SerializerProvider provider = Mockito.mock(SerializerProvider.class);
 Object bean = new Object();

 when(accessor.getValue(bean)).thenReturn(bean);
 anyGetterWriter.getAndSerialize(bean, gen, provider);

 verify(gen, never()).writeStartObject();
 verify(gen, never()).writeFieldName("key1");
 verify(gen, never()).writeString("value1");
 verify(gen, never()).writeEndObject();

 throw new JsonMappingException("Unexpected Exception");
 }

 @Test
 public void testGetAndFilter() throws JsonGenerationException, IOException {
 JsonGenerator gen = Mockito.mock(JsonGenerator.class);
 SerializerProvider provider = Mockito.mock(SerializerProvider.class);
 PropertyFilter filter = Mockito.mock(PropertyFilter.class);
 Object bean = new HashMap<String, String>() {{
 put("key1", "value1");
 put("key2", "value2");
 put("key3", "value3");
 }};

 when(accessor.getValue(bean)).thenReturn(bean);
 when(filter.includeProperty("key2")).thenReturn(true);
 when(filter.includeProperty("key3")).thenReturn(false);

 anyGetterWriter.getAndFilter(bean, gen, provider, filter);

 verify(gen, times(1)).writeStartObject();
 verify(gen, times(1)).writeFieldName("key2");
 verify(gen, times(1)).writeString("value2");
 verify(gen, never()).writeFieldName("key1");
 verify(gen, never()).writeFieldName("key3");
 verify(gen, times(1)).writeEndObject();
 }

 @Test
 public void testResolve() throws JsonMappingException {
 SerializerProvider provider = Mockito.mock(SerializerProvider.class);
 TypeSerializer typeSerializer = Mockito.mock(TypeSerializer.class);

 when(provider.findValueSerializer(Map.class, null)).thenReturn(mapSerializer);
 when(mapSerializer.getTypeSerializer()).thenReturn(typeSerializer);

 anyGetterWriter.resolve(provider);

 verify(provider, times(1)).handleContextualization(mapSerializer, property);
 }
}