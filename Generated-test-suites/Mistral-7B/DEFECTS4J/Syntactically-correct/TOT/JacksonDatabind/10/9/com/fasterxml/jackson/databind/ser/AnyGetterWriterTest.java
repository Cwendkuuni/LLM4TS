import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanProperty;
import com.fasterxml.jackson.databind.ser.MapSerializer;
import com.fasterxml.jackson.databind.ser.std.MapSerializerBase;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.test.BaseTest;

public class AnyGetterWriterTest extends BaseTest {

 private AnyGetterWriter anyGetterWriter;
 private ObjectMap node;
 private BeanProperty property;
 private AnnotatedMember accessor;
 private Object bean;
 private MapSerializer mapSerializer;
 private SerializerProvider provider;
 private JsonGenerator gen;
 private Object anyMap;

 @Before
 public void setUp() throws Exception {
 property = mock(BeanProperty.class);
 accessor = mock(AnnotatedMember.class);
 when(accessor.getName()).thenReturn("anyGetterName");
 when(accessor.getValue(anyMap)).thenReturn(anyMap);
 bean = new Object();
 mapSerializer = new MapSerializerBase(ObjectMap.class, ObjectMapper.getDefaultInstance().getTypeFactory(), null, null, null, null, null);
 provider = mock(SerializerProvider.class);
 gen = mock(JsonGenerator.class);
 anyMap = new HashMap<>();
 anyGetterWriter = new AnyGetterWriter(property, accessor, mapSerializer);
 }

 @Test
 public void testGetAndSerialize() throws Exception {
 anyMap.put("key1", "value1");
 when(provider.findValueSerializer(ObjectMap.class, ObjectMapper.getDefaultInstance(), anyMap)).thenReturn(mapSerializer);
 anyGetterWriter.getAndSerialize(bean, gen, provider);
 ObjectWriter writer = ObjectMapper.getDefaultInstance().writerFor(ObjectMap.class);
 ObjectWriter result = writer.writeValueAsString(anyMap);
 String expected = "{\"key1\":\"value1\"}";
 assertEquals(expected, result.toString());
 }

 @Test
 public void testGetAndFilter() throws Exception {
 anyMap.put("key1", "value1");
 when(provider.findValueSerializer(ObjectMap.class, ObjectMapper.getDefaultInstance(), anyMap)).thenReturn(mapSerializer);
 when(mapSerializer.serializeFilteredFields(anyMap, gen, provider, eq(null), any())).thenReturn(true);
 anyGetterWriter.getAndFilter(bean, gen, provider, mock(PropertyFilter.FilterProvider.class));
 ObjectWriter writer = ObjectMapper.getDefaultInstance().writerFor(ObjectMap.class);
 ObjectWriter result = writer.writeValueAsString(anyMap);
 String expected = "{\"key1\":\"value1\"}";
 assertEquals(expected, result.toString());
 }

 @Test(expected = JsonMappingException.class)
 public void testGetAndSerializeWithInvalidValue() throws Exception {
 anyMap.put("key1", new Object());
 when(provider.findValueSerializer(ObjectMap.class, ObjectMapper.getDefaultInstance(), anyMap)).thenReturn(mapSerializer);
 anyGetterWriter.getAndSerialize(bean, gen, provider);
 }

 @Test
 public void testResolve() throws Exception {
 anyGetterWriter.resolve(provider);
 verify(provider).handlePrimaryContextualization(mapSerializer, property);
 }
}