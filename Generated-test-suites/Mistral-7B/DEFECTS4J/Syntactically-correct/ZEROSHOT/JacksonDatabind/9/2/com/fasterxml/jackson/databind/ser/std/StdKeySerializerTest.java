import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class StdKeySerializerTest {

 @InjectMocks
 private StdKeySerializer stdKeySerializer;
 private final ObjectMapper objectMapper = new ObjectMapper();

 @Test
 public void testSerializeDate() throws IOException {
 Date date = new Date();
 JsonGenerator jsonGenerator = Mockito.mock(JsonGenerator.class);
 SerializerProvider serializerProvider = Mockito.mock(SerializerProvider.class);

 stdKeySerializer.serialize(date, jsonGenerator, serializerProvider);

 verify(serializerProvider).defaultSerializeDateKey(date, jsonGenerator);
 }

 @Test
 public void testSerializeString() throws IOException {
 String testString = "testString";
 JsonGenerator jsonGenerator = Mockito.mock(JsonGenerator.class);
 SerializerProvider serializerProvider = Mockito.mock(SerializerProvider.class);

 stdKeySerializer.serialize(testString, jsonGenerator, serializerProvider);

 verify(jsonGenerator).writeFieldName(testString);
 }

 @Test
 public void testGetSchema() throws JsonMappingException {
 SerializerProvider serializerProvider = Mockito.mock(SerializerProvider.class);

 JsonNode expectedSchema = createSchemaNode("string");
 when(serializerProvider.getConfig()).thenReturn(createObjectNode());

 JsonNode actualSchema = stdKeySerializer.getSchema(serializerProvider, Object.class);

 assertEquals(expectedSchema, actualSchema);
 }

 @Test
 public void testAcceptJsonFormatVisitor() throws JsonMappingException {
 JavaType javaType = Mockito.mock(JavaType.class);
 JsonFormatVisitorWrapper jsonFormatVisitorWrapper = Mockito.mock(JsonFormatVisitorWrapper.class);

 stdKeySerializer.acceptJsonFormatVisitor(jsonFormatVisitorWrapper, javaType);

 verify(jsonFormatVisitorWrapper).expectStringFormat(javaType);
 }

 private JsonNode createSchemaNode(String type) {
 ObjectNode objectNode = objectMapper.createObjectNode();
 objectNode.put("type", type);
 return objectNode;
 }

 private ObjectNode createObjectNode() {
 return (ObjectNode) objectMapper.createObjectNode();
 }
}