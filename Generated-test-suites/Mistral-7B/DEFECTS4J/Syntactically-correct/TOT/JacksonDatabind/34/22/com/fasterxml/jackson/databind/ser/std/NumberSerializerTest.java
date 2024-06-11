import org.junit.Test;
import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

public class NumberSerializerTest {

 @Test
 public void testSerializeBigDecimal() throws IOException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 ObjectMapper objectMapper = new ObjectMapper();
 ObjectWriter writer = objectMapper.writer()
 .with(SerializationFeature.WRITE_NUMBERS_AS_STRINGS, false)
 .with(numberSerializer);
 String json = writer.writeValueAsString(new BigDecimal("123.45"));
 JsonParser parser = new JsonFactory().createParser(json);
 Number deserialized = objectMapper.readValue(parser, Number.class);
 assertEquals(new BigDecimal("123.45"), deserialized);
 }

 @Test
 public void testSerializeBigInteger() throws IOException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 ObjectMapper objectMapper = new ObjectMapper();
 ObjectWriter writer = objectMapper.writer()
 .with(SerializationFeature.WRITE_NUMBERS_AS_STRINGS, false)
 .with(numberSerializer);
 String json = writer.writeValueAsString(new BigInteger("123456789012345678901234567890"));
 JsonParser parser = new JsonFactory().createParser(json);
 Number deserialized = objectMapper.readValue(parser, Number.class);
 assertEquals(new BigInteger("123456789012345678901234567890"), deserialized);
 }

 @Test
 public void testSerializeInteger() throws IOException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 ObjectMapper objectMapper = new ObjectMapper();
 String json = objectMapper.writeValueAsString(new Integer(123));
 JsonParser parser = new JsonFactory().createParser(json);
 Number deserialized = objectMapper.readValue(parser, Number.class);
 assertEquals(new Integer(123), deserialized);
 }

 @Test
 public void testSerializeLong() throws IOException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 ObjectMapper objectMapper = new ObjectMapper();
 String json = objectMapper.writeValueAsString(new Long(12345678901234567890L));
 JsonParser parser = new JsonFactory().createParser(json);
 Number deserialized = objectMapper.readValue(parser, Number.class);
 assertEquals(new Long(12345678901234567890L), deserialized);
 }

 @Test
 public void testSerializeDouble() throws IOException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 ObjectMapper objectMapper = new ObjectMapper();
 String json = objectMapper.writeValueAsString(new Double(123.45));
 JsonParser parser = new JsonFactory().createParser(json);
 Number deserialized = objectMapper.readValue(parser, Number.class);
 assertEquals(new Double(123.45), deserialized);
 }

 @Test
 public void testSerializeFloat() throws IOException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 ObjectMapper objectMapper = new ObjectMapper();
 String json = objectMapper.writeValueAsString(new Float(123.45f));
 JsonParser parser = new JsonFactory().createParser(json);
 Number deserialized = objectMapper.readValue(parser, Number.class);
 assertEquals(new Float(123.45f), deserialized);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSerializeUnsupportedType() throws IOException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 ObjectMapper objectMapper = new ObjectMapper();
 ObjectWriter writer = objectMapper.writer()
 .with(SerializationFeature.WRITE_NUMBERS_AS_STRINGS, false)
 .with(numberSerializer);
 String json = writer.writeValueAsString(new String("Hello World"));
 JsonParser parser = new JsonFactory().createParser(json);
 // This test case should throw an IllegalArgumentException
 Number deserialized = objectMapper.readValue(parser, Number.class);
 }
}