import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.ObjectIdGenerator;
import com.fasterxml.jackson.databind.util.ObjectIdWriter;

public class WritableObjectIdTest {

 private ObjectIdGenerator<Object> idGenerator;
 private Object id;
 private SerializerProvider provider;
 private JsonGenerator gen;
 private ObjectIdWriter idWriter;
 private WritableObjectId writableObjectId;

 @Before
 public void setup() {
 idGenerator = mock(ObjectIdGenerator.class);
 id = new Object();
 provider = mock(SerializerProvider.class);
 gen = mock(JsonGenerator.class);
 idWriter = new ObjectIdWriter() {
 @Override
 public void writePropertyName(JsonGenerator gen) throws IOException {
 gen.writeString("id");
 }

 @Override
 public SerializerProvider serializerProvider() {
 return provider;
 }

 @Override
 public SerializableString propertyName;
 };
 writableObjectId = new WritableObjectId(idGenerator);
 }

 @Test
 public void testWriteAsId() throws IOException {
 when(idGenerator.generateId(id)).thenReturn("testId");
 when(gen.canWriteObjectId()).thenReturn(true);

 writableObjectId.writeAsId(gen, provider, idWriter);

 // Verify that Object Id was written
 verify(gen, times(1)).writeObjectRef("testId");
 }

 @Test
 public void testWriteAsIdWhenCannotWriteObjectId() throws IOException {
 when(idGenerator.generateId(id)).thenReturn("testId");
 when(gen.canWriteObjectId()).thenReturn(false);

 WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
 writableObjectId.writeAsId(gen, provider, idWriter);

 // Verify that Object Id was serialized using serializer
 verify(provider, times(1)).findValueSerializer(id.getClass());
 }

 @Test(expected = IOException.class)
 public void testWriteAsIdWhenIdIsNull() throws IOException {
 writableObjectId.writeAsId(gen, provider, idWriter);

 // Verify that an exception was thrown
 verify(gen, times(1)).writeObjectId(anyString());
 }

 @Test
 public void testGenerateId() {
 id = new Object();
 id = writableObjectId.generateId(id);

 assertEquals("testId", id);
 }

 @Test(expected = JsonMappingException.class)
 public void testGenerateIdWhenIdGeneratorThrowsException() {
 when(idGenerator.generateId(id)).thenThrow(new JsonMappingException("Test Error"));

 writableObjectId.generateId(id);

 // Verify that an exception was thrown
 verify(idGenerator, times(1)).generateId(id);
 }

 @Test
 public void testWriteAsField() throws IOException {
 when(idGenerator.generateId(id)).thenReturn("testId");
 when(gen.canWriteObjectId()).thenReturn(true);

 writableObjectId.writeAsField(gen, provider, idWriter);

 // Verify that Object Id was written
 verify(gen, times(1)).writeObjectId("testId");
 }

 @Test
 public void testWriteAsFieldWhenCannotWriteObjectId() throws IOException {
 when(idGenerator.generateId(id)).thenReturn("testId");
 when(gen.canWriteObjectId()).thenReturn(false);

 WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
 writableObjectId.writeAsField(gen, provider, idWriter);

 // Verify that Object Id was serialized using serializer
 verify(provider, times(1)).findValueSerializer(id.getClass());
 }

 @Test
 public void testWriteAsFieldWhenIdIsNull() throws IOException {
 writableObjectId.writeAsField(gen, provider, idWriter);

 // Verify that no action was taken
 verify(gen, never()).writeObjectId(anyString());
 }

 @Test
 public void testWriteAsFieldWhenPropertyNameIsNull() throws IOException {
 idWriter.propertyName = null;
 writableObjectId.writeAsField(gen, provider, idWriter);

 // Verify that field name was written and Object Id was serialized using serializer
 verify(gen, times(1)).writeFieldName(anyString());
 verify(provider, times(1)).findValueSerializer(id.getClass());
 }

 @Test
 public void testWriteAsFieldWhenPropertyNameIsEmptyString() throws IOException {
 idWriter.propertyName = "";
 writableObjectId.writeAsField(gen, provider, idWriter);

 // Verify that field name was written and Object Id was serialized using serializer
 verify(gen, times(1)).writeFieldName("id");
 verify(provider, times(1)).findValueSerializer(id.getClass());
 }
}