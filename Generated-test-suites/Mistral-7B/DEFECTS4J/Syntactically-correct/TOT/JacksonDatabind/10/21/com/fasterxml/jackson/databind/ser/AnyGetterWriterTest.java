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
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AnyGetterWriterTest {

 private ObjectMapper mapper;
 private AnnotatedMember accessorMock;
 private BeanProperty propertyMock;
 private AnyGetterWriter writer;
 private MapSerializer mapSerializerMock;

 @Before
 public void setup() throws Exception {
 mapper = new ObjectMapper();
 accessorMock = mock(AnnotatedMember.class);
 propertyMock = mock(BeanProperty.class);
 mapSerializerMock = mock(MapSerializer.class);
 writer = new AnyGetterWriter(propertyMock, accessorMock, mapSerializerMock);
 }

 @Test
 public void testGetAndSerializeNullMap() throws Exception {
 Object bean = new Object();
 JsonGenerator gen = mock(JsonGenerator.class);
 SerializerProvider provider = mock(SerializerProvider.class);
 writer.getAndSerialize(bean, gen, provider);
 verify(gen).flush();
 }

 @Test
 public void testGetAndSerializeEmptyMap() throws Exception {
 Object bean = new Object();
 Map<String, Object> map = new HashMap<>();
 ObjectNode expected = mapper.createObjectNode();
 expected.put("testKey", null);
 writer.getAndSerialize(bean, mock(JsonGenerator.class), mock(SerializerProvider.class));
 JsonGenerator gen = mock(JsonGenerator.class);
 writer.getAndSerialize(bean, gen, provider);
 ObjectNode result = mapper.readValue(gen.getOutputBuffer(), ObjectNode.class);
 assertEquals(expected, result);
 }

 @Test
 public void testGetAndSerializeMapWithValues() throws Exception {
 Object bean = new Object();
 Map<String, Object> map = new HashMap<>();
 map.put("testKey1", "value1");
 map.put("testKey2", "value2");
 ObjectNode expected = mapper.createObjectNode();
 expected.put("testKey1", "value1");
 expected.put("testKey2", "value2");
 writer.getAndSerialize(bean, mock(JsonGenerator.class), mock(SerializerProvider.class));
 ObjectNode result = mapper.readValue(gen.getOutputBuffer(), ObjectNode.class);
 assertEquals(expected, result);
 }

 @Test(expected = JsonMappingException.class)
 public void testGetAndSerializeNonMapValue() throws Exception {
 Object bean = new Object();
 Object value = new Object();
 ObjectNode expected = mapper.createObjectNode();
 writer.getAndSerialize(bean, mock(JsonGenerator.class), mock(SerializerProvider.class));
 ObjectNode result = mapper.readValue(gen.getOutputBuffer(), ObjectNode.class);
 assertEquals(expected, result);
 value = new int[1];
 writer.getAndSerialize(bean, mock(JsonGenerator.class), mock(SerializerProvider.class));
 fail();
 }

 @Test(expected = JsonMappingException.class)
 public void testGetAndFilterNullMap() throws Exception {
 Object bean = new Object();
 JsonGenerator gen = mock(JsonGenerator.class);
 SerializerProvider provider = mock(SerializerProvider.class);
 PropertyFilter filter = mock(PropertyFilter.class);
 writer.getAndFilter(bean, gen, provider, filter);
 verify(gen, never()).writeStartObject();
 }

 @Test
 public void testGetAndFilterEmptyMap() throws Exception {
 Object bean = new Object();
 Map<String, Object> map = new HashMap<>();
 ObjectNode expected = mapper.createObjectNode();
 expected.put("testKey1", null);
 expected.put("testKey2", null);
 writer.getAndFilter(bean, mock(JsonGenerator.class), mock(SerializerProvider.class), mock(PropertyFilter.class));
 ObjectNode result = mapper.readValue(gen.getOutputBuffer(), ObjectNode.class);
 assertEquals(expected, result);
 }

 @Test
 public void testGetAndFilterMapWithValues() throws Exception {
 Object bean = new Object();Map<String, Object> map = new HashMap<>(); map.put("testKey1", "value1"); map.put("testKey2", "value2"); ObjectNode expected = mapper.createObjectNode(); expected.put("testKey1", "value1"); expected.put("testKey2", "value2"); PropertyFilter filter = mock(PropertyFilter.class); when(filter.include("testKey1")).thenReturn(true); when(filter.include("testKey2")).thenReturn(false); writer.getAndFilter(bean, mock(JsonGenerator.class), mock(SerializerProvider.class), filter); ObjectNode result = mapper.readValue(gen.getOutputBuffer(), ObjectNode.class); assertEquals(expected, result); }

 @Test(expected = JsonMappingException.class) public void testGetAndFilterNonMapValue() throws Exception { Object bean = new Object(); PropertyFilter filter = mock(PropertyFilter.class); writer.getAndFilter(bean, mock(JsonGenerator.class), mock(SerializerProvider.class), filter); verify(gen, never()).writeStartObject(); }

 @Test public void testResolve() throws Exception { SerializerProvider provider = mock(SerializerProvider.class); MapSerializer mapSerializer = new MapSerializer(mapper.getFactory(), ObjectMap.class, null, null, null, null, null, null); writer.resolve(provider); assertEquals(mapSerializer, mapSerializerMock); }
}