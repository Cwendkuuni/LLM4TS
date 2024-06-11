import org.junit.Test;
import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;

public class NumberSerializerTest {

 @Test
 public void testSerializeBigDecimal() throws Exception {
 NumberSerializer serializer = NumberSerializer.instance;

 BigDecimal value = new BigDecimal("123.45");
 StringWriter stringWriter = new StringWriter();
 JsonGenerator generator = new JsonFactory().createGenerator(stringWriter);
 serializer.serialize(value, generator, new ObjectMapper.DefaultSerializerProvider());
 generator.close();
 String json = stringWriter.toString();
 JsonParser parser = new ObjectMapper().getFactory().createParser(json);
 Object result = parser.readValueAs(BigDecimal.class);
 assertEquals(value, result);
 }

 @Test
 public void testSerializeBigInteger() throws Exception {
 NumberSerializer serializer = NumberSerializer.instance;

 BigInteger value = new BigInteger("12345678901234567890");
 StringWriter stringWriter = new StringWriter();
 JsonGenerator generator = new JsonFactory().createGenerator(stringWriter);
 serializer.serialize(value, generator, new ObjectMapper.DefaultSerializerProvider());
 generator.close();
 String json = stringWriter.toString();
 JsonParser parser = new ObjectMapper().getFactory().createParser(json);
 Object result = parser.readValueAs(BigInteger.class);
 assertEquals(value, result);
 }

 @Test
 public void testSerializeOtherNumberTypes() throws Exception {
 NumberSerializer serializer = NumberSerializer.instance;

 int valueInt = 123;
 double valueDouble = 123.45;
 StringWriter stringWriter = new StringWriter();
 JsonGenerator generator = new JsonFactory().createGenerator(stringWriter);
 serializer.serialize(valueInt, generator, new ObjectMapper.DefaultSerializerProvider());
 generator.close();
 String jsonInt = stringWriter.toString();
 JsonParser parserInt = new ObjectMapper().getFactory().createParser(jsonInt);
 Object resultInt = parserInt.readValueAs(Integer.class);
 assertEquals(valueInt, resultInt);
 serializer.serialize(valueDouble, generator, new ObjectMapper.DefaultSerializerProvider());
 generator.close();
 String jsonDouble = stringWriter.toString();
 JsonParser parserDouble = new ObjectMapper().getFactory().createParser(jsonDouble);
 Object resultDouble = parserDouble.readValueAs(Double.class);
 assertEquals(valueDouble, resultDouble);
 }

 @Test
 public void testDeserializeBigDecimal() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 String json = "123.45";
 Object result = mapper.readValue(json, BigDecimal.class);
 assertEquals(new BigDecimal("123.45"), result);
 }

 @Test
 public void testDeserializeBigInteger() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 String json = "12345678901234567890";
 Object result = mapper.readValue(json, BigInteger.class);
 assertEquals(new BigInteger("12345678901234567890"), result);
 }

 @Test(expected = NumberFormatException.class)
 public void testDeserializeInvalidNumber() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 String json = "abc";
 mapper.readValue(json, Number.class);
 }
}