import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import com.fasterxml.jackson.databind.util.BeanUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class AnyGetterWriterTest {

 private ObjectMapper objectMapper;
 private AnnotatedMember annotatedMember;
 private BeanProperty beanProperty;
 private MapSerializer mapSerializer;
 private AnyGetterWriter anyGetterWriter;

 @Before
 public void setUp() throws Exception {
 objectMapper = new ObjectMapper();
 annotatedMember = mock(AnnotatedMember.class);
 beanProperty = mock(BeanProperty.class);
 mapSerializer = mock(MapSerializer.class);
 anyGetterWriter = new AnyGetterWriter(beanProperty, annotatedMember, mapSerializer);
 }

 @Test
 public void testGetAndSerializeNullValue() throws Exception {
 Object bean = mock(Object.class);
 when(annotatedMember.getValue(bean)).thenReturn(null);

 JsonGenerator jsonGenerator = mock(JsonGenerator.class);
 SerializerProvider serializerProvider = mock(SerializerProvider.class);
 JsonMappingException exception = null;
 try {
 anyGetterWriter.getAndSerialize(bean, jsonGenerator, serializerProvider);
 } catch (JsonMappingException e) {
 exception = e;
 }
 assertNull(exception);
 }

 @Test
 public void testGetAndSerializeMapValue() throws Exception {
 Object bean = mock(Object.class);
 Map<String, Object> map = new HashMap<>();
 map.put("key1", "value1");
 map.put("key2", "value2");
 when(annotatedMember.getValue(bean)).thenReturn(map);

 JsonGenerator jsonGenerator = mock(JsonGenerator.class);
 SerializerProvider serializerProvider = mock(SerializerProvider.class);
 ObjectMapper objectMapper = mock(ObjectMapper.class);
 when(serializerProvider.getSerializerProvider().findValueSerializer(Map.class)).thenReturn(objectMapper);
 MapSerializer mapSerializer = mock(MapSerializer.class);
 when(objectMapper.getSerializerProvider().findValueSerializer(Map.class)).thenReturn(mapSerializer);

 anyGetterWriter.getAndSerialize(bean, jsonGenerator, serializerProvider);

 Map<String, Object> serializedMap = new HashMap<>();
 serializedMap.put("key1", "value1");
 serializedMap.put("key2", "value2");

 assertEquals(serializedMap, map);
 }

 @Test(expected = JsonMappingException.class)
 public void testGetAndSerializeNonMapValue() throws Exception {
 Object bean = mock(Object.class);
 Integer value = 1;
 when(annotatedMember.getValue(bean)).thenReturn(value);

 JsonGenerator jsonGenerator = mock(JsonGenerator.class);
 SerializerProvider serializerProvider = mock(SerializerProvider.class);

 anyGetterWriter.getAndSerialize(bean, jsonGenerator, serializerProvider);
 }

 @Test
 public void testGetAndFilterNullValue() throws Exception {
 Object bean = mock(Object.class);
 when(annotatedMember.getValue(bean)).thenReturn(null);

 PropertyFilter filter = mock(PropertyFilter.class);
 JsonGenerator jsonGenerator = mock(JsonGenerator.class);
 SerializerProvider serializerProvider = mock(SerializerProvider.class);

 anyGetterWriter.getAndFilter(bean, jsonGenerator, serializerProvider, filter);
 }

 @Test
 public void testGetAndFilterMapValue() throws Exception {
 Object bean = mock(Object.class);
 Map<String, Object> map = new HashMap<>();
 map.put("key1", "value1");
 map.put("key2", "value2");
 when(annotatedMember.getValue(bean)).thenReturn(map);

 PropertyFilter filter = mock(PropertyFilter.class);
 when(filter.include("key1")).thenReturn(true);
 when(filter.include("key2")).thenReturn(false);
 JsonGenerator jsonGenerator = mock(JsonGenerator.class);
 SerializerProvider serializerProvider = mock(SerializerProvider.class);

 anyGetterWriter.getAndFilter(bean, jsonGenerator, serializerProvider, filter);

 Map<String, Object> filteredMap = new HashMap<>();
 filteredMap.put("key1", "value1");
 assertEquals(filteredMap, map);
 }

 @Test
 public void testResolveNullSerializerProvider() throws Exception {
 SerializerProvider serializerProvider = null;
 anyGetterWriter.resolve(serializerProvider);
 assertNull(anyGetterWriter._mapSerializer);
 }

 @Test
 public void testResolveValidSerializerProvider() throws Exception {
 ObjectMapper objectMapper = mock(ObjectMapper.class);
 MapSerializer mapSerializer = mock(MapSerializer.class);
 when(objectMapper.getSerializerProvider().findValueSerializer(Map.class)).thenReturn(mapSerializer);
 SerializerProvider serializerProvider = mock(SerializerProvider.class);
 when(serializerProvider.handlePrimaryContextualization(mapSerializer, beanProperty)).thenReturn(objectMapper);

 anyGetterWriter.resolve(serializerProvider);
 assertEquals(mapSerializer, anyGetterWriter._mapSerializer);
 }
}