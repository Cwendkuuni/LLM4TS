import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectIdWriter;
import com.fasterxml.jackson.databind.SerializerProvider;

import static org.junit.Assert.*;

/**
 * Test class for WritableObjectId.
 */
public class WritableObjectIdTest {

 private ObjectIdGenerator<Long> idGenerator;
 private JsonGenerator jsonGeneratorMock;
 private SerializerProvider serializerProviderMock;
 private ObjectIdWriter objectIdWriterMock;

 @Before
 public void setup() {
 idGenerator = mock(ObjectIdGenerator.class);
 jsonGeneratorMock = mock(JsonGenerator.class);
 serializerProviderMock = mock(SerializerProvider.class);
 objectIdWriterMock = new ObjectIdWriter(null, null, null, null, jsonGeneratorMock, serializerProviderMock);
 }

 @Test
 public void testWriteAsId_WhenIdIsNotNullAndIdWrittenOrAlwaysAsIdIsTrue_ThenObjectIdIsWritten()
 throws IOException {
 Object id = new Long(1L);
 WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
 writableObjectId.id = id;
 when(jsonGeneratorMock.canWriteObjectId()).thenReturn(true);

 assertTrue(writableObjectId.writeAsId(jsonGeneratorMock, serializerProviderMock, objectIdWriterMock));

 verify(jsonGeneratorMock, times(1)).writeObjectRef(eq("1"));
 }

 @Test
 public void testWriteAsId_WhenIdIsNullAndIdWrittenIsFalse_ThenNoObjectIdIsWritten()
 throws IOException {
 WritableObjectId writableObjectId = new WritableObjectId(idGenerator);

 assertFalse(writableObjectId.writeAsId(jsonGeneratorMock, serializerProviderMock, objectIdWriterMock));

 verify(jsonGeneratorMock, never()).writeObjectRef(anyString());
 }

 @Test
 public void testWriteAsId_WhenIdIsNullAndIdWrittenIsTrue_ThenNoObjectIdIsWritten()
 throws IOException {
 WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
 writableObjectId.idWritten = true;

 assertFalse(writableObjectId.writeAsId(jsonGeneratorMock, serializerProviderMock, objectIdWriterMock));

 verify(jsonGeneratorMock, never()).writeObjectRef(anyString());
 }

 @Test
 public void testWriteAsId_WhenIdIsNotNullButJsonGeneratorCannotWriteObjectId_ThenObjectIsSerialized()
 throws IOException {
 Object id = new Long(1L);
 WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
 writableObjectId.id = id;
 when(jsonGeneratorMock.canWriteObjectId()).thenReturn(false);

 WritableObject idSerializer = mock(WritableObject.class);
 when(serializerProviderMock.findValueSerializer(id.getClass(), null)).thenReturn(idSerializer);

 assertTrue(writableObjectId.writeAsId(jsonGeneratorMock, serializerProviderMock, objectIdWriterMock));

 verify(idSerializer, times(1)).serialize(id, jsonGeneratorMock, serializerProviderMock);
 }

 @Test
 public void testGenerateId_WhenForPojoIsNotNull_ThenNewObjectIdIsGenerated() {
 Object id = new Long(1L);
 WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
 Object forPojo = new Object();

 writableObjectId.generateId(forPojo);

 assertEquals(id, writableObjectId.id);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGenerateId_WhenForPojoIsNull_ThenThrowsIllegalArgumentException() {
 WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
 Object forPojo = null;

 writableObjectId.generateId(forPojo);
 }
}