import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.Converter;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.JsonSerializer.Typing;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class StdDelegatingSerializerTest {

 private ObjectMapper objectMapper;
 private JsonSerializer<Object> delegateSerializerMock;
 private StdDelegatingSerializer delegatingSerializer;
 private Converter<Object, Integer> converterMock;

 @Before
 public void setup() throws Exception {
 objectMapper = new ObjectMapper();
 delegateSerializerMock = mock(JsonSerializer.class);
 converterMock = mock(Converter.class);
 when(converterMock.getOutputType(any(TypeFactory.class))).thenReturn(JsonTypeFactory.integerType());
 delegatingSerializer = new StdDelegatingSerializer(converterMock, JsonTypeFactory.javaTypeOf(Integer.class), delegateSerializerMock);
 }

 @Test
 public void testSerializationWithConvertedValue() throws JsonGenerationException, JsonMappingException {
 ObjectNode input = objectMapper.createObjectNode();
 input.put("value", 123);
 ObjectNode expectedOutput = objectMapper.createObjectNode();
 expectedOutput.put("i", 123);

 Object value = input.get("value");

 Object result = delegatingSerializer.serialize(value, objectMapper.getFactory().createGenerator(), objectMapper.getSerializerProvider());
 ObjectNode actualOutput = objectMapper.readTree(objectMapper.writerFor(JsonNode.class).writeValueAsString(result));

 verify(delegateSerializerMock, times(1)).serialize(any(), any(), any());
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testSerializationWithNullValue() throws JsonGenerationException, JsonMappingException {
 ObjectNode input = objectMapper.createObjectNode();
 Object value = null;

 Object result = delegatingSerializer.serialize(value, objectMapper.getFactory().createGenerator(), objectMapper.getSerializerProvider());
 ObjectNode actualOutput = objectMapper.readTree(objectMapper.writerFor(JsonNode.class).writeValueAsString(result));

 verify(delegateSerializerMock, never()).serialize(any(), any(), any());
 assertEquals(objectMapper.getFactory().getNullValueSerializer().serialize(value, objectMapper.getFactory().createGenerator(), objectMapper.getSerializerProvider()), result);
 }

 @Test
 public void testSerializeWithType() throws JsonGenerationException, JsonMappingException {
 Type typeHint = JsonTypeFactory.javaTypeOf("java.lang.Integer");
 ObjectNode input = objectMapper.createObjectNode();
 input.put("value", 123);
 ObjectNode expectedOutput = objectMapper.createObjectNode();
 expectedOutput.put("i", 123);
 expectedOutput.put("@type", "NumberInt");

 Object value = input.get("value");

 Object result = delegatingSerializer.serializeWithType(value, objectMapper.getFactory().createGenerator(), objectMapper.getSerializerProvider(), objectMapper.getTypeSerializer());
 ObjectNode actualOutput = objectMapper.readTree(objectMapper.writerFor(JsonNode.class).writeValueAsString(result));

 verify(delegateSerializerMock, times(1)).serializeWithType(any(), any(), any(), any());
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testIsEmpty() throws JsonGenerationException, JsonMappingException {
 ObjectNode input = objectMapper.createObjectNode();
 Object value = null;

 boolean empty = delegatingSerializer.isEmpty(null, value);

 verify(delegateSerializerMock, never()).isEmpty(any(), any());
 assertEquals(true, empty);
 }

 @Test
 public void testGetSchema() throws JsonMappingException {
 Schema schema = mock(Schema.class);
 ObjectNode expectedOutput = objectMapper.createObjectNode();
 expectedOutput.put("__type", "array");
 expectedOutput.put("items", expectedOutput.objectNode().putObject("type", "number").put("items", expectedOutput.objectNode().putObject("type", "integer")));

 when(delegateSerializerMock.getSchema(any(), any())).thenReturn(schema);

 JsonNode result = delegatingSerializer.getSchema(objectMapper.getSerializerProvider(), JsonTypeFactory.javaTypeOf("java.lang.Object"));

 assertEquals(expectedOutput, result);
 }

 @Test
 public void testAcceptJsonFormatVisitor() throws JsonMappingException {
 ObjectNode input = objectMapper.createObjectNode();
 ObjectNode output = objectMapper.createObjectNode();
 JsonFormatVisitor visitor = mock(JsonFormatVisitor.class);
 when(visitor.startArrayElement()).thenReturn(output.objectNode());
 when(visitor.endArrayElement()).thenReturn(output);

 delegatingSerializer.acceptJsonFormatVisitor(visitor, JsonTypeFactory.javaTypeOf("java.lang.Object"));

 verify(visitor, times(1)).startArrayElement();
 verify(visitor, times(1)).endArrayElement();
 }
}