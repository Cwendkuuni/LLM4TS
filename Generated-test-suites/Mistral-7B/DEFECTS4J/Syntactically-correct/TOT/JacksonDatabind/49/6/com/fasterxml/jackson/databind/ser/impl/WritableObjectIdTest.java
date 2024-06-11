import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectIdGenerator;
import com.fasterxml.jackson.databind.ObjectIdWriter;
import com.fasterxml.jackson.databind.SerializerProvider;

public class WritableObjectIdTest {

 private ObjectIdGenerator<Object> idGenerator;
 private JsonGenerator mockJsonGenerator;
 private SerializerProvider mockSerializerProvider;
 private ObjectIdWriter mockObjectIdWriter;
 private WritableObjectId writableObjectId;

 @Before
 public void setUp() {
 idGenerator = mock(ObjectIdGenerator.class);
 mockJsonGenerator = mock(JsonGenerator.class);
 mockSerializerProvider = mock(SerializerProvider.class);
 mockObjectIdWriter = new ObjectIdWriter(mockSerializerProvider);
 mockObjectIdWriter.alwaysAsId = false;
 writableObjectId = new WritableObjectId(idGenerator);
 }

 @Test
 public void testWriteAsId_NullId_FalseReturned() throws JsonProcessingException {
 when(mockJsonGenerator.canWriteObjectId()).thenReturn(false);
 when(mockSerializerProvider.findValueSerializer(any())).thenReturn(mock(JsonSerializer.class));

 assertFalse(writableObjectId.writeAsId(mockJsonGenerator, mockSerializerProvider, mockObjectIdWriter));
 }

 @Test
 public void testWriteAsId_IdWritten_TrueReturned() throws JsonProcessingException {
 when(mockJsonGenerator.canWriteObjectId()).thenReturn(true);
 when(mockSerializerProvider.findValueSerializer(any())).thenReturn(mock(JsonSerializer.class));

 Object testId = new Object();
 when(idGenerator.generateId(any())).thenReturn(testId);

 assertTrue(writableObjectId.writeAsId(mockJsonGenerator, mockSerializerProvider, mockObjectIdWriter));

 verify(mockJsonGenerator).writeObjectId(eq(testId.toString()));
 }

 @Test
 public void testWriteAsId_IdWrittenAndAlwaysAsId_TrueReturned() throws JsonProcessingException {
 mockObjectIdWriter.alwaysAsId = true;

 Object testId = new Object();
 when(idGenerator.generateId(any())).thenReturn(testId);

 assertTrue(writableObjectId.writeAsId(mockJsonGenerator, mockSerializerProvider, mockObjectIdWriter));

 verify(mockJsonGenerator).writeObjectRef(eq(testId.toString()));
 }

 @Test
 public void testGenerateId_NullForPojo_NullReturned() {
 Object testPojo = null;
 Object testId = writableObjectId.generateId(testPojo);
 assertNull(testId);
 }

 @Test
 public void testGenerateId_NotNullForPojo_GeneratedIdReturned() {
 Object testPojo = new Object();
 Object testId = writableObjectId.generateId(testPojo);
 assertNotNull(testId);
 }

 @Test
 public void testWriteAsField_NullPropertyName_WriteAsFieldMethodCalled() throws JsonProcessingException {
 when(mockJsonGenerator.canWriteObjectId()).thenReturn(false);
 when(mockSerializerProvider.getSerializableString(anyString())).thenReturn(mock(SerializableString.class));

 writableObjectId.writeAsField(mockJsonGenerator, mockSerializerProvider, mockObjectIdWriter);

 verify(mockJsonGenerator).writeFieldName(null);
 }

 @Test
 public void testWriteAsField_NonEmptyPropertyName_WriteNameAndValueMethodCalled() throws JsonProcessingException {
 SerializableString testPropertyName = mock(SerializableString.class);
 when(mockSerializerProvider.getSerializableString(anyString())).thenReturn(testPropertyName);

 when(mockJsonGenerator.canWriteObjectId()).thenReturn(false);

 writableObjectId.writeAsField(mockJsonGenerator, mockSerializerProvider, mockObjectIdWriter);

 verify(mockJsonGenerator).writeFieldName(testPropertyName);
 verify(mockObjectIdWriter.serializer, times(1)).serialize(any(), eq(mockJsonGenerator), eq(mockSerializerProvider));
 }
}