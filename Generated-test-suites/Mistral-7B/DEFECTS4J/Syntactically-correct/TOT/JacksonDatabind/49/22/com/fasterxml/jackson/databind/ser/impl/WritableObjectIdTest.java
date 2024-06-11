import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectIdWriter;
import com.fasterxml.jackson.databind.SerializerProvider;

import static org.junit.Assert.*;

public class WritableObjectIdTest {

 private ObjectIdGenerator<Object> idGenerator;
 private SerializerProvider provider;
 private JsonGenerator gen;
 private ObjectIdWriter idWriter;
 private WritableObjectId writableObjectId;

 @Before
 public void setup() {
 idGenerator = mock(ObjectIdGenerator.class);
 provider = mock(SerializerProvider.class);
 gen = mock(JsonGenerator.class);
 idWriter = new ObjectIdWriter("name", provider);
 writableObjectId = new WritableObjectId(idGenerator);
 }

 @Test
 public void testWriteAsIdTrue() throws IOException {
 when(idGenerator.generateId(any())).thenReturn("id1");
 when(gen.canWriteObjectId()).thenReturn(true);

 writableObjectId.writeAsId(gen, provider, idWriter);

 verify(gen, times(1)).writeObjectRef(eq("id1"));
 }

 @Test
 public void testWriteAsIdFalse() throws IOException {
 when(idGenerator.generateId(any())).thenReturn(null);
 when(gen.canWriteObjectId()).thenReturn(false);

 writableObjectId.writeAsId(gen, provider, idWriter);

 verify(idWriter, times(1)).serializer.serialize(eq(null), eq(gen), eq(provider));
 }

 @Test
 public void testGenerateId() throws JsonMappingException {
 Object pojo = new Object();
 when(idGenerator.generateId(eq(pojo))).thenReturn("id2");

 writableObjectId.generateId(pojo);

 assertEquals("id2", writableObjectId.id);
 }

 @Test(expected = IllegalStateException.class)
 public void testWriteAsFieldTwice() throws IOException {
 writableObjectId.writeAsField(gen, provider, idWriter);
 writableObjectId.writeAsField(gen, provider, idWriter);
 }

 @Test
 public void testWriteAsFieldWithPropertyName() throws IOException {
 SerializableString name = new SerializableString("propertyName");
 when(idGenerator.generateId(any())).thenReturn("id3");
 when(gen.canWriteObjectId()).thenReturn(false);

 writableObjectId.writeAsField(gen, provider, idWriter);

 verify(gen, times(1)).writeFieldName(eq("propertyName"));
 verify(idWriter, times(1)).serializer.serialize(eq("id3"), eq(gen), eq(provider));
 }

 @Test(expected = NullPointerException.class)
 public void testWriteAsFieldWithNullPropertyName() throws IOException {
 idWriter.propertyName = null;
 writableObjectId.writeAsField(gen, provider, idWriter);
 }
}