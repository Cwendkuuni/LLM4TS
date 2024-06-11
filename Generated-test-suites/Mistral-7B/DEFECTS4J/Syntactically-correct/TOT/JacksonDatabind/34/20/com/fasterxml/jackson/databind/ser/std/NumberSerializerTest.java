import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Test;

import static org.junit.Assert.*;

public class NumberSerializerTest {

 @Test
 public void testSerializeBigDecimal() throws JsonGenerationException, JsonMappingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

 BigDecimal number = new BigDecimal("123.45");
 String json = mapper.writerWithTypeValue(number.getClass())
 .writeValueAsString(number);

 JsonParser parser = mapper.getFactory().createParser(json);
 Number deserializedNumber = mapper.readValue(parser, Number.class);

 assertEquals(number, deserializedNumber);
 }

 @Test
 public void testSerializeBigInteger() throws JsonGenerationException, JsonMappingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

 BigInteger number = BigInteger.valueOf(12345);
 String json = mapper.writerWithTypeValue(number.getClass())
 .writeValueAsString(number);

 JsonParser parser = mapper.getFactory().createParser(json);
 Number deserializedNumber = mapper.readValue(parser, Number.class);

 assertEquals(number, deserializedNumber);
 }

 @Test
 public void testSerializeInteger() throws JsonGenerationException, JsonMappingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

 Integer number = 123;
 String json = mapper.writerWithTypeValue(number.getClass())
 .writeValueAsString(number);

 JsonParser parser = mapper.getFactory().createParser(json);
 Number deserializedNumber = mapper.readValue(parser, Number.class);

 assertEquals(number, deserializedNumber);
 }

 @Test
 public void testSerializeLong() throws JsonGenerationException, JsonMappingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

 Long number = 123456L;
 String json = mapper.writerWithTypeValue(number.getClass())
 .writeValueAsString(number);

 JsonParser parser = mapper.getFactory().createParser(json);
 Number deserializedNumber = mapper.readValue(parser, Number.class);

 assertEquals(number, deserializedNumber);
 }

 @Test
 public void testSerializeDouble() throws JsonGenerationException, JsonMappingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

 Double number = 123.45;
 String json = mapper.writerWithTypeValue(number.getClass())
 .writeValueAsString(number);

 JsonParser parser = mapper.getFactory().createParser(json);
 Number deserializedNumber = mapper.readValue(parser, Number.class);

 assertEquals(number, deserializedNumber, 0.001); // allow slight difference due to floating point precision
 }

 @Test
 public void testSerializeFloat() throws JsonGenerationException, JsonMappingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

 Float number = 123.45f;
 String json = mapper.writerWithTypeValue(number.getClass())
 .writeValueAsString(number);

 JsonParser parser = mapper.getFactory().createParser(json);
 Number deserializedNumber = mapper.readValue(parser, Number.class);

 assertEquals(number, deserializedNumber, 0.001f); // allow slight difference due to floating point precision
 }

 @Test
 public void testSerializeByte() throws JsonGenerationException, JsonMappingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

 Byte number = (byte) 127;
 String json = mapper.writerWithTypeValue(number.getClass())
 .writeValueAsString(number);

 JsonParser parser = mapper.getFactory().createParser(json);
 Number deserializedNumber = mapper.readValue(parser, Number.class);

 assertEquals(number, deserializedNumber);
 }

 @Test
 public void testSerializeShort() throws JsonGenerationException, JsonMappingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

 Short number = (short) 32767;
 String json = mapper.writerWithTypeValue(number.getClass())
 .writeValueAsString(number);

 JsonParser parser = mapper.getFactory().createParser(json);
 Number deserializedNumber = mapper.readValue(parser, Number.class);

 assertEquals(number, deserializedNumber);
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeString() throws JsonGenerationException, JsonMappingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

 String number = "not a number";
 String json = mapper.writerWithTypeValue(number.getClass())
 .writeValueAsString(number);

 JsonParser parser = mapper.getFactory().createParser(json);
 Number deserializedNumber = mapper.readValue(parser, Number.class);

 fail("Expected JsonMappingException");
 }
}