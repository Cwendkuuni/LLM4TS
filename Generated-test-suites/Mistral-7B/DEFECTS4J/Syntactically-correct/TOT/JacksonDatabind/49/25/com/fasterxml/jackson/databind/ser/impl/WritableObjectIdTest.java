import org.junit.Test;
import static org.mockito.Mockito.*;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.ObjectIdGenerator;
import com.fasterxml.jackson.databind.util.ObjectIdWriter;

import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class WritableObjectIdTest {

 private ObjectIdGenerator<Long> idGenerator = new ObjectIdGenerator<Long>() {
 @Override
 public Long generateId(Object o) {
 return System.currentTimeMillis();
 }
 };

 @Test
 public void testWriteAsId() throws IOException {
 WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
 JsonGenerator gen = mock(JsonGenerator.class);
 SerializerProvider provider = mock(SerializerProvider.class);
 ObjectIdWriter w = new ObjectIdWriter() {
 @Override
 public boolean alwaysAsId = false;
 @Override
 public Serializer<?> serializer = mock(StdSerializer.class);
 @Override
 public SerializableString propertyName = new SerializableString("testProperty");
 };

 when(gen.canWriteObjectId()).thenReturn(true);
 when(provider.findValueSerializer(Long.class)).thenReturn(w.serializer);

 writableObjectId.writeAsId(gen, provider, w);

 verify(gen, times(1)).writeObjectRef(eq("testProperty:" + writableObjectId.id));
 assertTrue(writableObjectId.idWritten);
 }

 @Test
 public void testWriteAsIdWithNoIdOrAlwaysAsId() throws IOException {
 WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
 JsonGenerator gen = mock(JsonGenerator.class);
 SerializerProvider provider = mock(SerializerProvider.class);
 ObjectIdWriter w = new ObjectIdWriter() {
 @Override
 public boolean alwaysAsId = true;
 @Override
 public Serializer<?> serializer = mock(StdSerializer.class);
 @Override
 public SerializableString propertyName = null;
 };

 when(gen.canWriteObjectId()).thenReturn(false);
 when(provider.findValueSerializer(Long.class)).thenReturn(w.serializer);

 writableObjectId.writeAsId(gen, provider, w);

 verify(w.serializer, times(1)).serialize(eq(writableObjectId.id), any(JsonGenerator.class), any(SerializerProvider.class));
 assertTrue(writableObjectId.idWritten);
 }

 @Test
 public void testGenerateId() {
 WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
 Object obj = new Object();
 writableObjectId.generateId(obj);
 assertNotNull(writableObjectId.id);
 }

 @Test
 public void testGenerateIdWithNullInput() {
 WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
 writableObjectId.generateId(null);
 assertNull(writableObjectId.id);
 }

 @Test
 public void testWriteAsField() throws IOException {
 WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
 JsonGenerator gen = mock(JsonGenerator.class);
 SerializerProvider provider = mock(SerializerProvider.class);
 ObjectIdWriter w = new ObjectIdWriter() {
 @Override
 public boolean alwaysAsId = false;
 @Override
 public Serializer<?> serializer = mock(StdSerializer.class);
 @Override
 public SerializableString propertyName = new SerializableString("testProperty");
 };

 when(gen.canWriteObjectId()).thenReturn(false);
 when(provider.findValueSerializer(Long.class)).thenReturn(w.serializer);

 writableObjectId.writeAsField(gen, provider, w);

 verify(gen, times(1)).writeFieldName(eq("testProperty"));
 verify(gen, times(1)).writeValue(any(Long.class));
 verify(w.serializer, times(1)).serialize(eq(writableObjectId.id), any(JsonGenerator.class), any(SerializerProvider.class));
 assertTrue(writableObjectId.idWritten);
 }

 @Test
 public void testWriteAsFieldWithNullPropertyName() throws IOException {
 WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
 JsonGenerator gen = mock(JsonGenerator.class);
 SerializerProvider provider = mock(SerializerProvider.class);
 ObjectIdWriter w = new ObjectIdWriter() {
 @Override
 public boolean alwaysAsId = false;
 @Override
 public Serializer<?> serializer = mock(StdSerializer.class);
 @Override
 public SerializableString propertyName = null;
 };

 when(gen.canWriteObjectId()).thenReturn(false);
 when(provider.findValueSerializer(Long.class)).thenReturn(w.serializer);

 writableObjectId.writeAsField(gen, provider, w);

 verify(gen, times(1)).writeStartObject();
 verify(gen, times(1)).writeFieldName(eq("$id"));
 verify(w.serializer, times(1)).serialize(eq(writableObjectId.id), any(JsonGenerator.class), any(SerializerProvider.class));
 verify(gen, times(1)).writeEndObject();
 assertTrue(writableObjectId.idWritten);
 }

 @Test(expected = JsonMappingException.class)
 public void testWriteAsFieldWithNullObjectId() throws IOException {
 WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
 JsonGenerator gen = mock(JsonGenerator.class);
 SerializerProvider provider = mock(SerializerProvider.class);
 ObjectIdWriter w = new ObjectIdWriter() {
 @Override
 public boolean alwaysAsId = false;
 @Override
 public Serializer<?> serializer = mock(StdSerializer.class);
 @Override
 public SerializableString propertyName = new SerializableString("testProperty");
 };

 when(gen.canWriteObjectId()).thenReturn(false);
 when(provider.findValueSerializer(Long.class)).thenReturn(w.serializer);

 writableObjectId.id = null;
 writableObjectId.writeAsField(gen, provider, w);
 }

 @Test
 public void testSerializeToJson() throws IOException {
 WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
 ObjectMapper mapper = new ObjectMapper();

 ObjectPojo obj = new ObjectPojo();
 obj.id = writableObjectId;

 String json = mapper.writeValueAsString(obj);
 Object idFromJson = mapper.readValue(json, new TypeReference<Map<String, Object>>(){}.getType()).get("$id");
 assertEquals(writableObjectId.id, idFromJson);
 }

}

class ObjectPojo {
 ObjectIdGenerator<?> generator;
 WritableObjectId id;
}