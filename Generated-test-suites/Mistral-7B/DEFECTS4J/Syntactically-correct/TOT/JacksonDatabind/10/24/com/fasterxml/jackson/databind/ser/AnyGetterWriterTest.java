import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AnyGetterWriterTest {

 private AnyGetterWriter writer;
 private ObjectNode node;
 private JsonGenerator generator;
 private SerializerProvider provider;
 private Object bean;

 @Before
 public void setUp() {
 Map<String, Object> map = new HashMap<>();
 map.put("key1", "value1");
 map.put("key2", "value2");
 bean = new Object() {};
 when(bean.getClass().getDeclaredField("anyMap")).thenReturn(mock(AnnotatedMember.class));
 when(mock(AnnotatedMember.class).getValue(bean)).thenReturn(map);
 provider = mock(SerializerProvider.class);
 when(provider.findValueSerializer(Map.class)).thenReturn(mock(MapSerializer.class));
 node = mock(ObjectNode.class);
 generator = mock(JsonGenerator.class);
 writer = new AnyGetterWriter(new BeanProperty(new BeanProperty.StdGetter(null), "anyMap"), mock(AnnotatedMember.class), mock(MapSerializer.class));
 }

 @Test
 public void testGetAndSerialize() throws Exception {
 ObjectNode result = new Object();
 when(generator.getOutputPrinter()).thenReturn(new ObjectMapper().getFactory().createValueFactory().getObjectWriter().getOutputPrinter());
 when(node.writeValueAsString(result)).thenAnswer(i -> result.toString());
 writer.getAndSerialize(bean, generator, provider);
 MapSerializer mapSerializer = (MapSerializer) provider.getSerializerProvider().getSerializerProvider().findValueSerializer(Map.class);
 ObjectMapper objectMapper = new ObjectMapper();
 Object readValue = objectMapper.readValue(node.toString(), Object.class);
 Map<String, Object> actualMap = (Map<String, Object>) readValue;
 Map<String, Object> expectedMap = (Map<String, Object>) map;
 assert mapEquals(expectedMap, actualMap);
 }

 @Test
 public void testGetAndFilter() throws Exception {
 PropertyFilter filter = mock(PropertyFilter.class);
 when(generator.getOutputPrinter()).thenReturn(new ObjectMapper().getFactory().createValueFactory().getObjectWriter().getOutputPrinter());
 writer.getAndFilter(bean, generator, provider, filter);
 verify(filter, times(1)).includeFilteredProperty("key1");
 verify(filter, times(1)).includeFilteredProperty("key2");
 }

 @Test(expected = JsonMappingException.class)
 public void testWrongType() throws Exception {
 Object wrongType = new Object() {};
 when(bean.getClass().getDeclaredField("anyField")).thenReturn(mock(AnnotatedMember.class));
 when(mock(AnnotatedMember.class).getValue(bean)).thenReturn(wrongType);
 writer.getAndSerialize(bean, generator, provider);
 }

 @Test(expected = JsonMappingException.class)
 public void testNullValue() throws Exception {
 writer.getAndSerialize(bean, generator, provider);
 when(bean.getClass().getDeclaredField("anyMap")).thenReturn(null);
 writer.getAndSerialize(bean, generator, provider);
 }

 private void mapEquals(Map<String, Object> map1, Map<String, Object> map2) {
 for (Map.Entry<String, Object> entry : map1.entrySet()) {
 String key = entry.getKey();
 Object value1 = entry.getValue();
 Object value2 = map2.get(key);
 assertEquals(value1, value2);
 }
 }

 @Test
 public void testResolve() throws JsonMappingException {
 ObjectMapper objectMapper = new ObjectMapper();
 writer.resolve(provider);
 ObjectMapper resolvedObjectMapper = (ObjectMapper) provider.getSerializerProvider().getProviderInstance(ObjectMapper.class);
 assertEquals(objectMapper, resolvedObjectMapper);
 }
}