import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.MapSerializer;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import com.fasterxml.jackson.databind.util.BeanUtil;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AnyGetterWriterTest {

 private Object testObject;
 private AnyGetterWriter anyGetterWriter;
 private MapSerializer mapSerializer;
 private JsonGenerator mockJsonGenerator;
 private SerializerProvider mockSerializerProvider;
 private Object mapValue;

 @Before
 public void setup() {
 testObject = new TestObject();
 mapValue = new HashMap<>();
 mapValue.put("key1", "value1");
 mapValue.put("key2", "value2");
 anyGetterWriter = new AnyGetterWriter(
 BeanProperty.stdProperty("anyGetterProperty", TestObject.class, "getAnyMap"),
 mock(AnnotatedMember.class), mapSerializer
 );
 mapSerializer = mock(MapSerializer.class);
 mockJsonGenerator = mock(JsonGenerator.class);
 mockSerializerProvider = mock(SerializerProvider.class);
 when(mockSerializerProvider.handlePrimaryContextualization(any(MapSerializer.class), any(BeanProperty.class))).thenReturn(mapSerializer);
 }

 @Test
 public void testGetAndSerialize() throws Exception {
 when(mapSerializer.serializeFields((Map<?,?>) anyCaptor.capture(Map.class), eq(mockJsonGenerator), eq(mockSerializerProvider))).thenReturn(null);
 anyGetterWriter.getAndSerialize(testObject, mockJsonGenerator, mockSerializerProvider);
 Map<String, String> actualMap = anyCaptor.getValue();
 assertEquals(mapValue, actualMap);
 verify(mockJsonGenerator).writeStartObject();
 verify(mockJsonGenerator).writeFieldNames("key1", "key2");
 verify(mockJsonGenerator).writeString("key1", "value1");
 verify(mockJsonGenerator).writeString("key2", "value2");
 verify(mockJsonGenerator).writeEndObject();
 }

 @Test(expected = JsonMappingException.class)
 public void testGetAndSerializeWithNullValue() throws Exception {
 when(mapSerializer.serializeFields((Map<?,?>) anyCaptor.capture(Map.class), eq(mockJsonGenerator), eq(mockSerializerProvider))).thenReturn(null);
 anyGetterWriter.getAndSerialize(testObject, mockJsonGenerator, mockSerializerProvider);
 verify(mockJsonGenerator, never()).writeStartObject();
 verify(mockJsonGenerator, never()).writeFieldNames("key1", "key2");
 verify(mockJsonGenerator, never()).writeString("key1", "value1");
 verify(mockJsonGenerator, never()).writeString("key2", "value2");
 anyGetterWriter.getAndSerialize(testObject, mockJsonGenerator, mockSerializerProvider);
 verify(mockJsonGenerator, never()).writeStartObject();
 verify(mockJsonGenerator, never()).writeFieldNames("key1", "key2");
 verify(mockJsonGenerator, never()).writeString("key1", "value1");
 verify(mockJsonGenerator, never()).writeString("key2", "value2");
 // Verify that exception is thrown only once
 verify(mockJsonGenerator, never()).writeStartObject();
 throw new JsonMappingException("Expected exception");
 }

 @Test(expected = JsonMappingException.class)
 public void testGetAndSerializeWithNonMapValue() throws Exception {
 when(mapSerializer.serializeFields((Map<?,?>) anyCaptor.capture(Map.class), eq(mockJsonGenerator), eq(mockSerializerProvider))).thenReturn(null);
 Object nonMapValue = new Object();
 doReturn(nonMapValue).when(testObject).getAnyMap();
 anyGetterWriter.getAndSerialize(testObject, mockJsonGenerator, mockSerializerProvider);
 Map<String, String> expectedMap = new HashMap<>();
 expectedMap.put("message", "Value returned by 'any-getter' not java.util.Map but " + nonMapValue.getClass().getName());
 JsonMappingException exception = new JsonMappingException(expectedMap.get("message"));
 throw exception;
 }

 @Test
 public void testGetAndFilter() throws Exception {
 PropertyFilter filter = mock(PropertyFilter.class);
 when(mockSerializerProvider.handlePrimaryContextualization(any(MapSerializer.class), any(BeanProperty.class))).thenReturn(mapSerializer);
 when(mapSerializer.serializeFilteredFields((Map<?,?>) anyCaptor.capture(Map.class), eq(mockJsonGenerator), eq(mockSerializerProvider), eq(filter), any(Object[].class))).thenReturn(null);
 anyGetterWriter.getAndFilter(testObject, mockJsonGenerator, mockSerializerProvider, filter);
 Map<String, String> actualMap = anyCaptor.getValue();
 assertEquals(mapValue, actualMap);
 verify(mockJsonGenerator).writeStartObject();
 verify(mockJsonGenerator).writeFieldNames("key1", "key2");
 // Filter test
 verify(mockJsonGenerator, never()).writeString("key1", "value1");
 verify(mockJsonGenerator, never()).writeString("key2", "value2");
 }

 @Test
 public void testResolve() throws Exception {
 Object primaryContextualization = new Object();
 SerializerProvider serializerProvider = mock(SerializerProvider.class);
 when(serializerProvider.handlePrimaryContextualization(any(MapSerializer.class), any(BeanProperty.class))).thenReturn(primaryContextualization);
 anyGetterWriter.resolve(serializerProvider);
 assertEquals(mapSerializer, anyGetterWriter._mapSerializer);
 verify(serializerProvider).handlePrimaryContextualization(eq(mapSerializer), any(BeanProperty.class));
 }

 class TestObject {
 public Map<String, String> getAnyMap() {
 return mapValue;
 }
 }
}