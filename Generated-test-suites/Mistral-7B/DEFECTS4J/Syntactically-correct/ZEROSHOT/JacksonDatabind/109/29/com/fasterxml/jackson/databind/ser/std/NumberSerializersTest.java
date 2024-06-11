import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NumberSerializersTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testIntegerSerializer() throws JsonProcessingException {
 Integer number = 123;
 JsonNode expected = nodeFactory.numberNode(123);
 JsonNode actual = objectMapper.writeValueAsNode(number);
 assertEquals(expected, actual);
 }

 @Test
 public void testIntegerSerializerWithJsonFormat() throws JsonProcessingException {
 Integer number = 123;
 @JsonFormat(shape = JsonFormat.Shape.STRING)
 Integer formattedNumber = number;
 JsonNode expected = nodeFactory.textNode("123");
 JsonNode actual = objectMapper.writeValueAsNode(formattedNumber);
 assertEquals(expected, actual);
 }

 @Test
 public void testIntegerSerializerContextual() throws JsonProcessingException {
 int contextualNumber = 456;
 JsonNode expected = nodeFactory.numberNode(456);
 ObjectMapper contextualMapper = new ObjectMapper();
 contextualMapper.registerModule(new ParamNameModule());
 contextualMapper.addHandler(new BeanPropertyMapperHandler(contextualMapper()));
 JsonNode actual = contextualMapper.writeValueAsNode(contextualNumber);
 assertEquals(expected, actual);
 }

 @Test
 public void testIntegerSerializerWithTypeSerializer() throws JsonProcessingException {
 Integer number = 789;
 JsonGenerator generator = objectMapper.getPolymorphicSerializer(Number.class)
 .getSerializerProvider().getSerializer(Number.class)
 .createContextual(null, null);
 generator.setSerializerProvider(objectMapper.getSerializerProvider());
 generator.setRootValueSerializer(contextualSerializer());
 JsonNode expected = nodeFactory.numberNode(789);
 ObjectWriter writer = objectMapper.writerWithView(MyCustomView.class);
 JsonNode actual = writer.writeValueAsNode(number);
 assertEquals(expected, actual);
 }

 @Test
 public void testLongSerializer() throws JsonProcessingException {
 Long number = 1234567890L;
 JsonNode expected = nodeFactory.numberNode(1234567890L);
 JsonNode actual = objectMapper.writeValueAsNode(number);
 assertEquals(expected, actual);
 }

 @Test
 public void testFloatSerializer() throws JsonProcessingException {
 Float number = 123.45f;
 JsonNode expected = nodeFactory.numberNode(123.45f);
 JsonNode actual = objectMapper.writeValueAsNode(number);
 assertEquals(expected, actual);
 }

 @Test
 public void testDoubleSerializer() throws JsonProcessingException {
 Double number = 123.456789d;
 JsonNode expected = nodeFactory.numberNode(123.456789d);
 JsonNode actual = objectMapper.writeValueAsNode(number);
 assertEquals(expected, actual);
 }

 private JsonNodeFactory nodeFactory = new DefaultJsonNodeFactory();
 private JsonSerializer<Object> contextualSerializer() {
 return objectMapper.getSerializerProvider().getSerializer(Number.class);
 }
}