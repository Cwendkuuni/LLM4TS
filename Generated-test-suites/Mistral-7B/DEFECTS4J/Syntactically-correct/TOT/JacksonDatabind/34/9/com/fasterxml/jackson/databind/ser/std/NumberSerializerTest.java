import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NumberSerializerTest {

 private ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testSerializeBigDecimal() throws IOException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator jsonGenerator = mapper.getPolymorphicSerializer(Number.class).acceptJsonGenerator(mapper.getSerializationConfig(), null);

 BigDecimal bigDecimal = new BigDecimal("123.456");
 numberSerializer.serialize(bigDecimal, jsonGenerator, mapper.getSerializationConfig().getDefaultSerializerProvider());

 String actual = jsonGenerator.getOutputFactory().writeValueAsString(jsonGenerator.getBuffer());
 String expected = "123.456";

 assertEquals(expected, actual);
 }

 @Test
 public void testSerializeBigInteger() throws IOException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator jsonGenerator = mapper.getPolymorphicSerializer(Number.class).acceptJsonGenerator(mapper.getSerializationConfig(), null);

 BigInteger bigInteger = new BigInteger("12345678901234567890");
 numberSerializer.serialize(bigInteger, jsonGenerator, mapper.getSerializationConfig().getDefaultSerializerProvider());

 String actual = jsonGenerator.getOutputFactory().writeValueAsString(jsonGenerator.getBuffer());
 String expected = "12345678901234567890";

 assertEquals(expected, actual);
 }

 @Test
 public void testSerializeInteger() throws IOException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator jsonGenerator = mapper.getPolymorphicSerializer(Number.class).acceptJsonGenerator(mapper.getSerializationConfig(), null);

 Integer integer = 123;
 numberSerializer.serialize(integer, jsonGenerator, mapper.getSerializationConfig().getDefaultSerializerProvider());

 String actual = jsonGenerator.getOutputFactory().writeValueAsString(jsonGenerator.getBuffer());
 String expected = "123";

 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSerializeNull() throws IOException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator jsonGenerator = mapper.getPolymorphicSerializer(Number.class).acceptJsonGenerator(mapper.getSerializationConfig(), null);

 numberSerializer.serialize(null, jsonGenerator, mapper.getSerializationConfig().getDefaultSerializerProvider());
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeUnsupportedType() throws IOException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator jsonGenerator = mapper.getPolymorphicSerializer(Number.class).acceptJsonGenerator(mapper.getSerializationConfig(), null);

 String unsupportedType = "unsupported";
 numberSerializer.serialize(unsupportedType, jsonGenerator, mapper.getSerializationConfig().getDefaultSerializerProvider());
 }

 @Test
 public void testDeserializeBigDecimal() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

 String json = "123.456";
 Number deserializedNumber = mapper.readValue(json, Number.class);

 BigDecimal expected = new BigDecimal("123.456");
 BigDecimal actual = (BigDecimal) deserializedNumber;

 assertEquals(expected, actual);
 }

 @Test
 public void testDeserializeBigInteger() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

 String json = "12345678901234567890";
 Number deserializedNumber = mapper.readValue(json, Number.class);

 BigInteger expected = new BigInteger("12345678901234567890");
 BigInteger actual = (BigInteger) deserializedNumber;

 assertEquals(expected, actual);
 }

 @Test
 public void testDeserializeInteger() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

 String json = "123";
 Number deserializedNumber = mapper.readValue(json, Number.class);

 Integer expected = 123;
 Integer actual = (Integer) deserializedNumber;

 assertEquals(expected, actual);
 }

 @Test(expected = IOException.class)
 public void testDeserializeNull() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

 String json = null;
 Number deserializedNumber = mapper.readValue(json, Number.class);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeUnsupportedFormat() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

 String json = "\"unsupported\"";
 Number deserializedNumber = mapper.readValue(json, Number.class);
 }
}