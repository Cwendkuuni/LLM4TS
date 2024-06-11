import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*; // For Mockito library

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ObjectIdGenerator;
import com.fasterxml.jackson.databind.ObjectIdWriter;
import com.fasterxml.jackson.databind.node.ObjectNode;

import static org.junit.Assert.*;

public class WritableObjectIdTest {

private ObjectIdGenerator<Long> longIdGenerator;
private ObjectMapper objectMapper;
private JsonGenerator jsonGenerator;
private SerializerProvider serializerProvider;
private ObjectIdWriter idWriter;

@Before
public void setup() {
longIdGenerator = mock(ObjectIdGenerator.class);
objectMapper = new ObjectMapper();
jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
serializerProvider = objectMapper.getSerializerProvider();
idWriter = new ObjectIdWriter(jsonGenerator, serializerProvider, null);
}

@Test
public void testWriteAsId_ExistingId_Written() throws JsonProcessingException {
 WritableObjectId writableObjectId = new WritableObjectId(longIdGenerator);
 Long id = 1L;
 writableObjectId.id = id;

 // Call writeAsId method
 writableObjectId.writeAsId(jsonGenerator, serializerProvider, idWriter);

 // Verify that id has been written
 assertEquals(String.valueOf(id), jsonGenerator.getOutputBuffer().toString());
}

@Test
public void testWriteAsId_NullId_NotWritten() throws JsonProcessingException {
 WritableObjectId writableObjectId = new WritableObjectId(longIdGenerator);
 writableObjectId.id = null;

 // Call writeAsId method
 assertFalse(writableObjectId.writeAsId(jsonGenerator, serializerProvider, idWriter));

 // Verify that id has not been written
 assertEquals("", jsonGenerator.getOutputBuffer().toString());
}

@Test
public void testWriteAsId_NoIdGenerated_NotWritten() throws JsonProcessingException {
 WritableObjectId writableObjectId = new WritableObjectId(longIdGenerator);
 Object pojo = new Object();

 // Call writeAsId method with no id
 writableObjectId.generateId(pojo);
 writableObjectId.writeAsId(jsonGenerator, serializerProvider, idWriter);

 // Verify that id has not been written
 assertEquals("", jsonGenerator.getOutputBuffer().toString());

 // Verify that id has been generated
 verify(longIdGenerator).generateId(pojo);
}

@Test
public void testWriteAsId_AlwaysAsId_Written() throws JsonProcessingException {
 WritableObjectId writableObjectId = new WritableObjectId(longIdGenerator);
 Object pojo = new Object();
 Long id = 1L;

 // Set alwaysAsId flag to true
 idWriter.alwaysAsId = true;

 // Generate id and write it as id
 writableObjectId.generateId(pojo);
 writableObjectId.writeAsId(jsonGenerator, serializerProvider, idWriter);

 // Verify that id has been written
 assertEquals(String.valueOf(id), jsonGenerator.getOutputBuffer().toString());
}

@Test
public void testGenerateId_NullPojo_NullIdGenerated() {
 WritableObjectId writableObjectId = new WritableObjectId(longIdGenerator);
 Object pojo = null;

 // Call generateId method
 Object id = writableObjectId.generateId(pojo);

 // Verify that id is null
 assertNull(id);
}

@Test
public void testGenerateId_NonnullPojo_ValidIdGenerated() {
 WritableObjectId writableObjectId = new WritableObjectId(longIdGenerator);
 Object pojo = new Object();

 // Call generateId method
 Object id = writableObjectId.generateId(pojo);

 // Verify that id is not null
 assertNotNull(id);
}

@Test
public void testWriteAsField_ValidId_Written() throws JsonProcessingException {
 WritableObjectId writableObjectId = new WritableObjectId(longIdGenerator);
 Long id = 1L;
 writableObjectId.id = id;
ObjectNode root = objectMapper.createObjectNode();
root.put("id", "testPropertyName");
idWriter.propertyName = "id";
idWriter.serializer = objectMapper.findValueSerializer(Object.class);

// Call writeAsField method
writableObjectId.writeAsField(jsonGenerator, serializerProvider, idWriter);
jsonGenerator.flush();

// Verify that id has been written as a field
ObjectNode result = objectMapper.readTree(jsonGenerator.getOutputBuffer());
assertEquals(id.toString(), result.get("id").asText());
}

@Test
public void testWriteAsField_NullId_NotWritten() throws JsonProcessingException {
 WritableObjectId writableObjectId = new WritableObjectId(longIdGenerator);
 ObjectNode root = objectMapper.createObjectNode();
 idWriter.propertyName = "id";
 idWriter.serializer = objectMapper.findValueSerializer(Object.class);

 // Call writeAsField method with no id
 writableObjectId.writeAsField(jsonGenerator, serializerProvider, idWriter);
 jsonGenerator.flush();

 // Verify that id has not been written
 ObjectNode result = objectMapper.readTree(jsonGenerator.getOutputBuffer());
 assertTrue(result.isNull("id"));
}
}