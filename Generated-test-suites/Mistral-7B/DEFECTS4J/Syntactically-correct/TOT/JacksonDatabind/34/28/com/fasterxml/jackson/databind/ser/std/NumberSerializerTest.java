import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NumberSerializerTest {

 private ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testSerializeBigDecimal() throws IOException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 BigDecimal bigDecimal = new BigDecimal("123.456");
 JsonGenerator jsonGenerator = mapper.getPolymorphicSerializer(Number.class).getWriter(null, null, null, null, false, false);
 numberSerializer.serialize(bigDecimal, jsonGenerator, mapper.getSerializerProvider());
 jsonGenerator.flush();
 String json = jsonGenerator.getValueAsString();
 assertEquals("123.456", json);
 }

 @Test
 public void testSerializeBigInteger() throws IOException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 BigInteger bigInteger = BigInteger.valueOf(1234567890123L);
 JsonGenerator jsonGenerator = mapper.getPolymorphicSerializer(Number.class).getWriter(null, null, null, null, false, false);
 numberSerializer.serialize(bigInteger, jsonGenerator, mapper.getSerializerProvider());
 jsonGenerator.flush();
 String json = jsonGenerator.getValueAsString();
 assertEquals("1234567890123", json);
 }

 @Test
 public void testSerializeInt() throws IOException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 int intValue = 123;
 JsonGenerator jsonGenerator = mapper.getPolymorphicSerializer(Number.class).getWriter(null, null, null, null, false, false);
 numberSerializer.serialize(intValue, jsonGenerator, mapper.getSerializerProvider());
 jsonGenerator.flush();
 String json = jsonGenerator.getValueAsString();
 assertEquals("123", json);
 }

 @Test
 public void testSerializeLong() throws IOException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 long longValue = 1234567890123L;
 JsonGenerator jsonGenerator = mapper.getPolymorphicSerializer(Number.class).getWriter(null, null, null, null, false, false);
 numberSerializer.serialize(longValue, jsonGenerator, mapper.getSerializerProvider());
 jsonGenerator.flush();
 String json = jsonGenerator.getValueAsString();
 assertEquals("1234567890123", json);
 }

 @Test
 public void testSerializeDouble() throws IOException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 double doubleValue = 123.456;
 JsonGenerator jsonGenerator = mapper.getPolymorphicSerializer(Number.class).getWriter(null, null, null, null, false, false);
 numberSerializer.serialize(doubleValue, jsonGenerator, mapper.getSerializerProvider());
 jsonGenerator.flush();
 String json = jsonGenerator.getValueAsString();
 assertEquals("123.456", json);
 }

 @Test
 public void testSerializeFloat() throws IOException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 float floatValue = 123.456f;
 JsonGenerator jsonGenerator = mapper.getPolymorphicSerializer(Number.class).getWriter(null, null, null, null, false, false);
 numberSerializer.serialize(floatValue, jsonGenerator, mapper.getSerializerProvider());
 jsonGenerator.flush();
 String json = jsonGenerator.getValueAsString();
 assertEquals("123.456", json);
 }

 @Test
 public void testSerializeByte() throws IOException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 byte byteValue = 127;
 JsonGenerator jsonGenerator = mapper.getPolymorphicSerializer(Number.class).getWriter(null, null, null, null, false, false);
 numberSerializer.serialize(byteValue, jsonGenerator, mapper.getSerializerProvider());
 jsonGenerator.flush();
 String json = jsonGenerator.getValueAsString();
 assertEquals("127", json);
 }

 @Test
 public void testSerializeShort() throws IOException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 short shortValue = 123;
 JsonGenerator jsonGenerator = mapper.getPolymorphicSerializer(Number.class).getWriter(null, null, null, null, false, false);
 numberSerializer.serialize(shortValue, jsonGenerator, mapper.getSerializerProvider());
 jsonGenerator.flush();
 String json = jsonGenerator.getValueAsString();
 assertEquals("123", json);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSerializeString() throws IOException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 String stringValue = "abc";
 JsonGenerator jsonGenerator = mapper.getPolymorphicSerializer(Number.class).getWriter(null, null, null, null, false, false);
 numberSerializer.serialize(stringValue, jsonGenerator, mapper.getSerializerProvider());
 }
}