import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.ObjectIdGenerator.IdIntGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonschema.JsonSchema;
import com.fasterxml.jackson.databind.jsonschema.JsonSchemaGenerator;
import com.fasterxml.jackson.databind.jsonschema.SchemaFactory;
import com.fasterxml.jackson.databind.jsonschema.Validator;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class WritableObjectIdTest {

 private ObjectIdGenerator<Object> idGenerator;
 private IdIntGenerator idIntGenerator;
 private JsonGenerator jsonGenerator;
 private SerializerProvider serializerProvider;
 private ObjectMapper objectMapper;
 private JsonSchemaGenerator jsonSchemaGenerator;
 private SchemaFactory schemaFactory;
 private Validator validator;
 private WritableObjectId writableObjectId;
 private ObjectIdWriter objectIdWriter;

 @Before
 public void setUp() {
 idGenerator = mock(ObjectIdGenerator.class);
 idIntGenerator = mock(IdIntGenerator.class);
 jsonGenerator = mock(JsonGenerator.class);
 serializerProvider = mock(SerializerProvider.class);
 objectMapper = new ObjectMapper();
 jsonSchemaGenerator = new JsonSchemaGenerator();
 schemaFactory = jsonSchemaGenerator.getFactory();
 }

 @Test
 public void testWriteAsId_IdWrittenAndIdNotNull_WritesIdToJsonGenerator() throws Exception {
 id = 1L;
 idWritten = true;
 when(idGenerator.generateId(any())).thenReturn(id);
 when(jsonGenerator.canWriteObjectId()).thenReturn(true);
 when(jsonGenerator.writeObjectId(anyString())).thenReturn(jsonGenerator);

 writableObjectId = new WritableObjectId(idGenerator);
 ObjectIdWriter objectIdWriter = new ObjectIdWriter(new StdSerializer<>());

 writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter);

 verify(jsonGenerator).writeObjectRef(eq(String.valueOf(id)));
 }

 @Test
 public void testWriteAsId_IdWrittenFalseAndIdNull_DoesNotWriteIdToJsonGenerator() throws Exception {
 id = null;
 idWritten = false;
 when(jsonGenerator.canWriteObjectId()).thenReturn(true);

 writableObjectId = new WritableObjectId(idGenerator);
 ObjectIdWriter objectIdWriter = new ObjectIdWriter(new StdSerializer<>());

 writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter);

 verify(jsonGenerator, never()).writeObjectRef(anyString());
 }

 @Test
 public void testWriteAsId_IdWrittenFalseAndIdNotNull_WritesIdToJsonGeneratorViaSerializer() throws Exception {
 id = new Object();
 idWritten = false;
 when(idGenerator.generateId(any())).thenReturn(id);
 when(jsonGenerator.canWriteObjectId()).thenReturn(false);
 when(jsonGenerator.writeFieldName(anyString())).thenReturn(jsonGenerator);
 when(jsonMapper.findValueSerializer(id.getClass(), serializerProvider)).thenReturn(new StdSerializer<>());

 writableObjectId = new WritableObjectId(idGenerator);
 ObjectIdWriter objectIdWriter = new ObjectIdWriter(new StdSerializer<>());

 writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter);

 verify(jsonGenerator).writeFieldName(anyString());
 verify(jsonMapper).findValueSerializer(eq(id.getClass()), eq(serializerProvider));
 }

 @Test
 public void testGenerateId_ForNonPointerType_ReturnsIdGeneratedByObjectIdGenerator() throws Exception {
 id = 1L;
 when(idGenerator.generateId(any())).thenReturn(id);

 writableObjectId = new WritableObjectId(idGenerator);

 assertEquals(id, writableObjectId.generateId(new Object()));
 }

 @Test
 public void testGenerateId_ForPointerType_ReturnsNull() throws Exception {
 writableObjectId = new WritableObjectId(idGenerator);

 assertNull(writableObjectId.generateId(null));
 }

 @Test
 public void testWriteAsField_IdWrittenAndIdNotNull_WritesIdToJsonGeneratorAsField() throws Exception {
 id = 1L;
 idWritten = true;
 when(idGenerator.generateId(any())).thenReturn(id);
 when(jsonGenerator.canWriteObjectId()).thenReturn(false);
 when(jsonGenerator.writeFieldName(anyString())).thenReturn(jsonGenerator);
 when(jsonMapper.findValueSerializer(id.getClass(), serializerProvider)).thenReturn(new StdSerializer<>());
 SerializableString name = new SerializableString("name");

 writableObjectId = new WritableObjectId(idGenerator);
 ObjectIdWriter objectIdWriter = new ObjectIdWriter(new StdSerializer<>());

 writableObjectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);

 verify(jsonGenerator).writeFieldName(eq("name"));
 }

 @Test
 public void testWriteAsField_IdWrittenFalseAndIdNull_DoesNotWriteIdToJsonGeneratorAsField() throws Exception {
 id = null;
 idWritten = false;
 when(jsonGenerator.canWriteObjectId()).thenReturn(false);

 writableObjectId = new WritableObjectId(idGenerator);
 ObjectIdWriter objectIdWriter = new ObjectIdWriter(new StdSerializer<>());

 writableObjectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);

 verify(jsonGenerator, never()).writeFieldName(anyString());
 }

 @Test
 public void testWriteAsField_IdWrittenFalseAndIdNotNull_WritesIdToJsonGeneratorAsFieldViaSerializer() throws Exception {
 id = new Object();
 idWritten = false;
 when(jsonGenerator.canWriteObjectId()).thenReturn(false);
 when(jsonGenerator.writeFieldName(anyString())).thenReturn(jsonGenerator);
 when(jsonMapper.findValueSerializer(id.getClass(), serializerProvider)).thenReturn(new StdSerializer<>());

 writableObjectId = new WritableObjectId(idGenerator);
 ObjectIdWriter objectIdWriter = new ObjectIdWriter(new StdSerializer<>());

 writableObjectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);

 verify(jsonGenerator).writeFieldName(anyString());
 verify(jsonMapper).findValueSerializer(eq(id.getClass()), eq(serializerProvider));
 }

 @Test
 public void testJsonSchemaGeneration() throws Exception {
 id = 1L;
 String schemaString = "{\"$schema\":\"http://json-schema.org/draft-07/schema#\",\"title\":\"WritableObjectId\",\"type\":\"object\",\"properties\":{\"id\":{\"type\":\"integer\"}}}";

 when(idGenerator.generateId(any())).thenReturn(id);

 writableObjectId = new WritableObjectId(idGenerator);

 JsonSchema schema = jsonSchemaGenerator.generateSchema(writableObjectId);
 JsonSchemaValidator validator = new JsonSchemaValidator(schemaFactory, schema);

 assertTrue(validator.validate(new ObjectMapper().readValue(new ByteArrayInputStream(schemaString.getBytes()), WritableObjectId.class)));
 }

 @Test
 public void testJsonSchemaGeneration_WithNullIdGenerator_ThrowsIllegalStateException() throws Exception {
 idGenerator = null;
 writableObjectId = new WritableObjectId(idGenerator);

 assertThrows(IllegalStateException.class, () -> jsonSchemaGenerator.generateSchema(writableObjectId));
 }
}

class ObjectIdWriter {
 private final StdSerializer<?> serializer;
 private SerializableString propertyName;

 public ObjectIdWriter(StdSerializer<?> serializer) {
 this.serializer = serializer;
 }
}