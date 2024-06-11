import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ObjectIdGenerator;
import com.fasterxml.jackson.databind.ObjectIdWriter;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test class for WritableObjectId class.
 */
public class WritableObjectIdTest {

 private ObjectIdGenerator<Object> objectIdGenerator;
 private JsonGenerator jsonGeneratorMock;
 private SerializerProvider serializerProviderMock;
 private ObjectIdWriter objectIdWriterMock;
 private WritableObjectId writableObjectId;

 @Before
 public void setup() {
 objectIdGenerator = mock(ObjectIdGenerator.class);
 jsonGeneratorMock = mock(JsonGenerator.class);
 serializerProviderMock = mock(SerializerProvider.class);
 objectIdWriterMock = mock(ObjectIdWriter.class);
 when(objectIdGenerator.generateId(any())).thenReturn("generatedId");
 writableObjectId = new WritableObjectId(objectIdGenerator);
 }

 @Test
 public void testWriteAsId() throws IOException {
 // Given: Id is not null and idWritten is false
 idWritten = false;
 Object id = "existingId";
 writableObjectId.id = id;

 // When: writeAsId is called and JsonGenerator can write Object Ids
 when(jsonGeneratorMock.canWriteObjectId()).thenReturn(true);

 // Then: Object Id is written as a reference
 verify(jsonGeneratorMock).writeObjectRef(eq(id.toString()));
 assertTrue(writableObjectId.writeAsId(jsonGeneratorMock, serializerProviderMock, objectIdWriterMock));

 // Given: Id is not null and idWritten is true or alwaysAsId is true
 idWritten = true;
 when(objectIdWriterMock.alwaysAsId).thenReturn(true);

 // When: writeAsId is called and JsonGenerator can write Object Ids
 when(jsonGeneratorMock.canWriteObjectId()).thenReturn(true);

 // Then: Object Id is written as a reference
 verify(jsonGeneratorMock).writeObjectRef(eq(id.toString()));
 assertTrue(writableObjectId.writeAsId(jsonGeneratorMock, serializerProviderMock, objectIdWriterMock));

 // Given: Id is null
 id = null;
 writableObjectId.id = id;

 // When: writeAsId is called and JsonGenerator can write Object Ids
 when(jsonGeneratorMock.canWriteObjectId()).thenReturn(true);

 // Then: Nothing is written, as Id is null
 verify(jsonGeneratorMock, never()).writeObjectRef(anyString());
 assertFalse(writableObjectId.writeAsId(jsonGeneratorMock, serializerProviderMock, objectIdWriterMock));
 }

 @Test
 public void testGenerateId() {
 // Given: ForPojo is not null
 Object forPojo = new Object();

 // When: generateId is called
 Object generatedId = writableObjectId.generateId(forPojo);

 // Then: Id is generated and assigned to the writableObjectId instance
 assertEquals("generatedId", generatedId);
 assertNotNull(writableObjectId.id);
 }

 @Test(expected = NullPointerException.class)
 public void testGenerateIdWithNullForPojo() {
 // Given: ForPojo is null
 Object forPojo = null;

 // When: generateId is called
 writableObjectId.generateId(forPojo);
 }

 @Test
 public void testWriteAsField() throws IOException {
 // Given: JsonGenerator can write field names
 when(jsonGeneratorMock.canWriteFieldName(anyString())).thenReturn(true);

 // When: writeAsField is called
 writableObjectId.writeAsField(jsonGeneratorMock, serializerProviderMock, objectIdWriterMock);

 // Then: Id is written as a field
 verify(jsonGeneratorMock).writeFieldName(anyString());
 verify(jsonGeneratorMock).writeValue(eq(writableObjectId.id));
 }

 @Test(expected = IllegalStateException.class)
 public void testWriteAsFieldWhenIdWrittenIsTrue() throws IOException {
 // Given: Id has been written
 idWritten = true;

 // When: writeAsField is called
 writableObjectId.writeAsField(jsonGeneratorMock, serializerProviderMock, objectIdWriterMock);

 // Then: Throws an IllegalStateException
 verify(jsonGeneratorMock, never()).writeFieldName(anyString());
 verify(jsonGeneratorMock, never()).writeValue(any(Object.class));
 }
}