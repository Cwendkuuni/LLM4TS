import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanProperty;
import com.fasterxml.jackson.databind.ser.MapSerializer;
import com.fasterxml.jackson.databind.ser.std.MapSerializer.StdSerializer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AnyGetterWriterTest {

 private ObjectMapper objectMapper;
 private AnyGetterWriter anyGetterWriter;
 private BeanProperty property;
 private AnnotatedMember accessor;
 private MapSerializer mapSerializer;
 private SerializerProvider provider;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 property = mock(BeanProperty.class);
 accessor = mock(AnnotatedMember.class);
 when(accessor.getValue(anyObject())).thenReturn(new HashMap<>());
 mapSerializer = new StdSerializer<Map<String, Object>>(HashMap.class) {};
 anyGetterWriter = new AnyGetterWriter(property, accessor, mapSerializer);
 provider = mock(SerializerProvider.class);
 }

 @Test
 public void testGetAndSerialize() throws IOException {
 JsonGenerator gen = mock(JsonGenerator.class);
 when(provider.getSerializerProvider().findValueSerializer(Map.class)).thenReturn(mapSerializer);
 anyGetterWriter.getAndSerialize(new Object(), gen, provider);
 verify(gen).writeStartObject();
 verify(gen).writeFieldName("anyGetterName");
 verify(gen).writeStartArray();
 // Write fields of the Map
 verify(mapSerializer).serializeFields((Map) anyObject(), eq(gen), eq(provider));
 verify(gen).writeEndArray();
 verify(gen).writeEndObject();
 }

 @Test
 public void testGetAndFilter() throws IOException, JsonMappingException {
 JsonGenerator gen = mock(JsonGenerator.class);
 PropertyFilter filter = mock(PropertyFilter.class);
 when(provider.getSerializerProvider().findValueSerializer(Map.class)).thenReturn(mapSerializer);
 anyGetterWriter.getAndFilter(new Object(), gen, provider, filter);
 verify(mapSerializer).serializeFilteredFields((Map) anyObject(), eq(gen), eq(provider), eq(filter), null);
 }

 @Test(expected = JsonMappingException.class)
 public void testValueIsNotMap() throws IOException {
 Object bean = new Object();
 anyGetterWriter.getAndSerialize(bean, mock(JsonGenerator.class), provider);
 fail("Expected JsonMappingException but was not thrown");
 }

 @Test
 public void testResolve() {
 anyGetterWriter.resolve(provider);
 verify(provider).handlePrimaryContextualization(mapSerializer, property);
 }
}