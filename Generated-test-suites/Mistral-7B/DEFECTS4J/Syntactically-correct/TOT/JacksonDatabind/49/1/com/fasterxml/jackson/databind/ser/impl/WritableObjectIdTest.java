import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.ObjectIdGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.ObjectIdWriter;

import junit.framework.TestCase;

public class WritableObjectIdTest extends TestCase {

 private ObjectIdGenerator<Object> objectIdGenerator;
 private WritableObjectId writableObjectId;
 private ObjectIdWriter objectIdWriter;
 private JsonGenerator jsonGeneratorMock;
 private SerializerProvider serializerProviderMock;

 @Before
 public void setUp() {
 objectIdGenerator = mock(ObjectIdGenerator.class);
 writableObjectId = new WritableObjectId(objectIdGenerator);
 objectIdWriter = new ObjectIdWriter(new StdSerializer<Object>(Object.class), new ObjectMapper());
 jsonGeneratorMock = mock(JsonGenerator.class);
 serializerProviderMock = mock(SerializerProvider.class);
 }

 @Test
 public void testWriteAsId() throws IOException {
 Object id = new Object();
 when(objectIdGenerator.generateId(writableObjectId)).thenReturn(id);
 when(jsonGeneratorMock.canWriteObjectId()).thenReturn(true);

 writableObjectId.writeAsId(jsonGeneratorMock, serializerProviderMock, objectIdWriter);

 // Check if ObjectId was written in JsonGenerator
 verify(jsonGeneratorMock).writeObjectRef(eq(String.valueOf(id)));
 }

 @Test
 public void testWriteAsId_WithoutId() throws IOException {
 when(jsonGeneratorMock.canWriteObjectId()).thenReturn(false);
 when(objectIdGenerator.generateId(writableObjectId)).thenReturn(null);

 writableObjectId.writeAsId(jsonGeneratorMock, serializerProviderMock, objectIdWriter);

 // Check if no ObjectId was written in JsonGenerator
 verify(jsonGeneratorMock, never()).writeObjectId(anyString());
 }

 @Test
 public void testWriteAsId_WithAlwaysAsId() throws IOException {
 Object id = new Object();
 when(objectIdGenerator.generateId(writableObjectId)).thenReturn(id);
 when(jsonGeneratorMock.canWriteObjectId()).thenReturn(false);
 when(objectIdWriter.alwaysAsId).thenReturn(true);

 writableObjectId.writeAsId(jsonGeneratorMock, serializerProviderMock, objectIdWriter);

 // Check if ObjectId was serialized using serializer
 verify(objectIdWriter.serializer).serialize(eq(id), eq(jsonGeneratorMock), any(SerializerProvider.class));
 }

 @Test
 public void testGenerateId() {
 Object id = writableObjectId.generateId(new Object());
 assertEquals(id, objectIdGenerator.generateId(new Object()));
 }

 @Test(expected = IllegalStateException.class)
 public void testGenerateId_WithIdWritten() {
 writableObjectId.idWritten = true;
 writableObjectId.generateId(new Object());
 }
}