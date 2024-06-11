import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import static org.junit.Assert.*;

public class WritableObjectIdTest {

 private ObjectIdGenerator<Long> idGenerator;
 private WritableObjectId writableObjectId;
 private ObjectMapper mapper;
 private SerializerProvider provider;
 private JsonGenerator jsonGenerator;
 private Object forPojo;
 private ObjectIdWriter objectIdWriter;

 @Before
 public void setUp() throws Exception {
 idGenerator = mock(ObjectIdGenerator.class);
 writableObjectId = new WritableObjectId(idGenerator);
 mapper = new ObjectMapper();
 provider = mapper.getSerializerProvider();
 jsonGenerator = mock(JsonGenerator.class);
 forPojo = new Object();
 objectIdWriter = new ObjectIdWriter();
 objectIdWriter.serializer = new StdSerializer<Object>(Object.class) {};
 objectIdWriter.propertyName = new SerializableString("propertyName");
 }

 @Test
 public void testWriteAsId() throws IOException {
 when(jsonGenerator.canWriteObjectId()).thenReturn(true);
 when(jsonGenerator.writeObjectRef(anyString())).thenReturn(null);

 idGenerator.generateId(forPojo) = 1L;
 assertTrue(writableObjectId.writeAsId(jsonGenerator, provider, objectIdWriter));
 verify(jsonGenerator, times(1)).writeObjectRef(eq("1"));
 }

 @Test
 public void testWriteAsIdWhenCannotWriteObjectId() throws IOException {
 when(jsonGenerator.canWriteObjectId()).thenReturn(false);
 when(objectIdWriter.serializer.serialize(any(), eq(jsonGenerator), eq(provider)))
 .thenReturn(null);

 idGenerator.generateId(forPojo) = 1L;
 assertFalse(writableObjectId.writeAsId(jsonGenerator, provider, objectIdWriter));
 verify(jsonGenerator, never()).writeObjectId(anyString());
 verify(objectIdWriter.serializer, times(1)).serialize(eq(1), eq(jsonGenerator), eq(provider));
 }

 @Test
 public void testGenerateId() throws Exception {
 Long id = 1L;
 when(idGenerator.generateId(forPojo)).thenReturn(id);
 assertEquals(id, writableObjectId.generateId(forPojo));
 }

 @Test(expected = JsonMappingException.class)
 public void testGenerateIdWhenIdGenerationFails() throws Exception {
 when(idGenerator.generateId(forPojo)).thenThrow(new JsonMappingException("Test error"));
 assertNotNull(writableObjectId.generateId(forPojo)); // should not throw
 fail("Expected JsonMappingException");
 }

 @Test
 public void testWriteAsField() throws IOException {
 when(jsonGenerator.canWriteObjectId()).thenReturn(false);
 when(jsonGenerator.writeFieldName(anyString())).thenReturn(null);
 when(objectIdWriter.serializer.serialize(any(), eq(jsonGenerator), eq(provider)))
 .thenReturn("testValue".getBytes());

 idGenerator.generateId(forPojo) = 1L;
 writableObjectId.writeAsField(jsonGenerator, provider, objectIdWriter);
 verify(jsonGenerator, times(1)).writeFieldName("propertyName");
 verify(jsonGenerator, times(1)).writeBytes("testValue".getBytes());
 }
}