import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializerTest {

 @Test
 public void testSerializeBigDecimal() throws JsonProcessingException {
 NumberSerializer numberSerializer = new NumberSerializer(Number.class);
 ObjectMapper objectMapper = new ObjectMapper();
 JsonGenerator jsonGenerator = objectMapper.getPolymorphicSerializer(Number.class).getSerializerProvider().getGeneratorFactory().createGenerator(null);

 BigDecimal bigDecimal = new BigDecimal("123.456");
 numberSerializer.serialize(bigDecimal, jsonGenerator, objectMapper.getSerializerProvider());
 jsonGenerator.flush();
 String actual = jsonGenerator.getOutputBuffer().toString();
 String expected = "123.456";
 assertEquals(expected, actual);
 }

 @Test
 public void testSerializeBigInteger() throws JsonProcessingException {
 NumberSerializer numberSerializer = new NumberSerializer(Number.class);
 ObjectMapper objectMapper = new ObjectMapper();
 JsonGenerator jsonGenerator = objectMapper.getPolymorphicSerializer(Number.class).getSerializerProvider().getGeneratorFactory().createGenerator(null);

 BigInteger bigInteger = new BigInteger("12345678901234567890");
 numberSerializer.serialize(bigInteger, jsonGenerator, objectMapper.getSerializerProvider());
 jsonGenerator.flush();
 String actual = jsonGenerator.getOutputBuffer().toString();
 String expected = "12345678901234567890";
 assertEquals(expected, actual);
 }

 @Test
 public void testSerializeLong() throws JsonProcessingException {
 NumberSerializer numberSerializer = new NumberSerializer(Number.class);
 ObjectMapper objectMapper = new ObjectMapper();
 JsonGenerator jsonGenerator = objectMapper.getPolymorphicSerializer(Number.class).getSerializerProvider().getGeneratorFactory().createGenerator(null);

 Long longValue = 123L;
 numberSerializer.serialize(longValue, jsonGenerator, objectMapper.getSerializerProvider());
 jsonGenerator.flush();
 String actual = jsonGenerator.getOutputBuffer().toString();
 String expected = "123";
 assertEquals(expected, actual);
 }

 @Test
 public void testSerializeDouble() throws JsonProcessingException {
 NumberSerializer numberSerializer = new NumberSerializer(Number.class);
 ObjectMapper objectMapper = new ObjectMapper();
 JsonGenerator jsonGenerator = objectMapper.getPolymorphicSerializer(Number.class).getSerializerProvider().getGeneratorFactory().createGenerator(null);

 Double doubleValue = 123.456;
 numberSerializer.serialize(doubleValue, jsonGenerator, objectMapper.getSerializerProvider());
 jsonGenerator.flush();
 String actual = jsonGenerator.getOutputBuffer().toString();
 String expected = "123.456";
 assertEquals(expected, actual);
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeNull() throws JsonProcessingException {
 NumberSerializer numberSerializer = new NumberSerializer(Number.class);
 ObjectMapper objectMapper = new ObjectMapper();
 JsonGenerator jsonGenerator = objectMapper.getPolymorphicSerializer(Number.class).getSerializerProvider().getGeneratorFactory().createGenerator(null);

 Number number = null;
 numberSerializer.serialize(number, jsonGenerator, objectMapper.getSerializerProvider());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSerializeString() throws JsonProcessingException {
 NumberSerializer numberSerializer = new NumberSerializer(Number.class);
 ObjectMapper objectMapper = new ObjectMapper();
 JsonGenerator jsonGenerator = objectMapper.getPolymorphicSerializer(Number.class).getSerializerProvider().getGeneratorFactory().createGenerator(null);

 String stringValue = "invalid";
 numberSerializer.serialize(stringValue, jsonGenerator, objectMapper.getSerializerProvider());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSerializeCharacter() throws JsonProcessingException {
 NumberSerializer numberSerializer = new NumberSerializer(Number.class);
 ObjectMapper objectMapper = new ObjectMapper();
 JsonGenerator jsonGenerator = objectMapper.getPolymorphicSerializer(Number.class).getSerializerProvider().getGeneratorFactory().createGenerator(null);

 char characterValue = 'A';
 numberSerializer.serialize(characterValue, jsonGenerator, objectMapper.getSerializerProvider());
 }
}