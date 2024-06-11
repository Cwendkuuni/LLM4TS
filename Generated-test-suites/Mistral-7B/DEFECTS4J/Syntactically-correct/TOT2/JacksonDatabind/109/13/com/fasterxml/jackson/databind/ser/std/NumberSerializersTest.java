import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Test;

import static com.fasterxml.jackson.databind.node.TextNode.value;
import static org.junit.Assert.assertEquals;

public class NumberSerializersTest {

 private ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testIntegerSerializer_normal() throws JsonProcessingException {
 Integer integer = 123;
 String json = mapper.writeValueAsString(integer);
 assertEquals(value("123"), mapper.readTree(json).get(""));
 }

 @Test
 public void testIntegerSerializer_minValue() throws JsonProcessingException {
 Integer minInt = Integer.MIN_VALUE;
 String json = mapper.writeValueAsString(minInt);
 assertEquals(value("-2147483648"), mapper.readTree(json).get(""));
 }

 @Test
 public void testIntegerSerializer_maxValue() throws JsonProcessingException {
 Integer maxInt = Integer.MAX_VALUE;
 String json = mapper.writeValueAsString(maxInt);
 assertEquals(value("2147483647"), mapper.readTree(json).get(""));
 }

 @Test
 public void testIntegerSerializer_negative() throws JsonProcessingException {
 Integer negativeInteger = -123;
 String json = mapper.writeValueAsString(negativeInteger);
 assertEquals(value("-123"), mapper.readTree(json).get(""));
 }

 @Test
 public void testIntegerSerializer_null() throws JsonProcessingException {
 Integer nullValue = null;
 String json = mapper.writeValueAsString(nullValue);
 assertEquals(null, mapper.readTree(json));
 }

 @Test
 public void testIntegerSerializer_nan() throws JsonProcessingException {
 Float nan = Float.NaN;
 Integer intNaN = Integer.valueOf(nan.intValue());
 String json = mapper.writeValueAsString(intNaN);
 assertEquals(null, mapper.readTree(json));
 }

 @Test
 public void testIntegerSerializer_infinity() throws JsonProcessingException {
 Float infinity = Float.POSITIVE_INFINITY;
 Integer intInfinity = Integer.valueOf(infinity.intValue());
 String json = mapper.writeValueAsString(intInfinity);
 assertEquals(null, mapper.readTree(json));
 }

 @Test
 public void testShortSerializer_normal() throws JsonProcessingException {
 Short shortValue = 123;
 String json = mapper.writeValueAsString(shortValue);
 assertEquals(value("123"), mapper.readTree(json).get(""));
 }

 @Test
 public void testShortSerializer_minValue() throws JsonProcessingException {
 Short minShort = Short.MIN_VALUE;
 String json = mapper.writeValueAsString(minShort);
 assertEquals(value("-32768"), mapper.readTree(json).get(""));
 }

 @Test
 public void testShortSerializer_maxValue() throws JsonProcessingException {
 Short maxShort = Short.MAX_VALUE;
 String json = mapper.writeValueAsString(maxShort);
 assertEquals(value("32767"), mapper.readTree(json).get(""));
 }

 @Test
 public void testShortSerializer_negative() throws JsonProcessingException {
 Short negativeShort = -123;
 String json = mapper.writeValueAsString(negativeShort);
 assertEquals(value("-123"), mapper.readTree(json).get(""));
 }

 @Test
 public void testShortSerializer_null() throws JsonProcessingException {
 Short nullValue = null;
 String json = mapper.writeValueAsString(nullValue);
 assertEquals(null, mapper.readTree(json));
 }

 @Test
 public void testShortSerializer_nan() throws JsonProcessingException {
 Float nan = Float.NaN;
 Short shortNaN = Short.valueOf((short) nan.intValue());
 String json = mapper.writeValueAsString(shortNaN);
 assertEquals(null, mapper.readTree(json));
 }

 @Test
 public void testShortSerializer_infinity() throws JsonProcessingException {
 Float infinity = Float.POSITIVE_INFINITY;
 Short shortInfinity = Short.valueOf((short) infinity.intValue());
 String json = mapper.writeValueAsString(shortInfinity);
 assertEquals(null, mapper.readTree(json));
 }

 @Test
 public void testLongSerializer_normal() throws JsonProcessingException {
 Long longValue = 123456789123L;
 String json = mapper.writeValueAsString(longValue);
 assertEquals(value("123456789123"), mapper.readTree(json).get(""));
 }

 @Test
 public void testLongSerializer_minValue() throws JsonProcessingException {
 Long minLong = Long.MIN_VALUE;
 String json = mapper.writeValueAsString(minLong);
 assertEquals(value("-9223372036854775808"), mapper.readTree(json).get(""));
 }

 @Test
 public void testLongSerializer_maxValue() throws JsonProcessingException {
 Long maxLong = Long.MAX_VALUE;
 String json = mapper.writeValueAsString(maxLong);
 assertEquals(value("9223372036854775807"), mapper.readTree(json).get(""));
 }

 @Test
 public void testLongSerializer_negative() throws JsonProcessingException {
 Long negativeLong = -123456789123L;
 String json = mapper.writeValueAsString(negativeLong);
 assertEquals(value("-123456789123"), mapper.readTree(json).get(""));
 }

 @Test
 public void testLongSerializer_null() throws JsonProcessingException {
 Long nullValue = null;
 String json = mapper.writeValueAsString(nullValue);
 assertEquals(null, mapper.readTree(json));
 }

 @Test
 public void testLongSerializer_nan() throws JsonProcessingException {
 Float nan = Float.NaN;
 Long longNaN = Long.valueOf(nan.longValue());
 String json = mapper.writeValueAsString(longNaN);
 assertEquals(null, mapper.readTree(json));
 }

 @Test
 public void testLongSerializer_infinity() throws JsonProcessingException {
 Float infinity = Float.POSITIVE_INFINITY;
 Long longInfinity = Long.valueOf(infinity.longValue());
 String json = mapper.writeValueAsString(longInfinity);
 assertEquals(null, mapper.readTree(json));
 }

 @Test
 public void testFloatSerializer_normal() throws JsonProcessingException {
 Float floatValue = 123.45f;
 String json = mapper.writeValueAsString(floatValue);
 assertEquals(value("123.45"), mapper.readTree(json).get(""));
 }

 @Test
 public void testFloatSerializer_minValue() throws JsonProcessingException {
 Float minFloat = Float.MIN_VALUE;
 String json = mapper.writeValueAsString(minFloat);
 assertEquals(value("-3.402823E38"), mapper.readTree(json).get(""));
 }

 @Test
 public void testFloatSerializer_maxValue() throws JsonProcessingException {
 Float maxFloat = Float.MAX_VALUE;
 String json = mapper.writeValueAsString(maxFloat);
 assertEquals(value("3.402823E38"), mapper.readTree(json).get(""));
 }

 @Test
 public void testFloatSerializer_negative() throws JsonProcessingException {
 Float negativeFloat = -123.45f;
 String json = mapper.writeValueAsString(negativeFloat);
 assertEquals(value("-123.45"), mapper.readTree(json).get(""));
 }

 @Test
 public void testFloatSerializer_null() throws JsonProcessingException {
 Float nullValue = null;
 String json = mapper.writeValueAsString(nullValue);
 assertEquals(null, mapper.readTree(json));
 }

 @Test
 public void testFloatSerializer_nan() throws JsonProcessingException {
 Float nan = Float.NaN;
 String json = mapper.writeValueAsString(nan);
 assertEquals(null, mapper.readTree(json));
 }

 @Test
 public void testFloatSerializer_infinity() throws JsonProcessingException {
 Float infinity = Float.POSITIVE_INFINITY;
 String json = mapper.writeValueAsString(infinity);
 assertEquals(null, mapper.readTree(json));
 }

 @Test
 public void testDoubleSerializer_normal() throws JsonProcessingException {
 Double doubleValue = 123.45;
 String json = mapper.writeValueAsString(doubleValue);
 assertEquals(value("123.45"), mapper.readTree(json).get(""));
 }

 @Test
 public void testDoubleSerializer_minValue() throws JsonProcessingException {
 Double minDouble = Double.MIN_VALUE;
 String json = mapper.writeValueAsString(minDouble);
 assertEquals(value("-4.940666E-324"), mapper.readTree(json).get(""));
 }

 @Test
 public void testDoubleSerializer_maxValue() throws JsonProcessingException {
 Double maxDouble = Double.MAX_VALUE;
 String json = mapper.writeValueAsString(maxDouble);
 assertEquals(value("4.940666E-324"), mapper.readTree(json).get(""));
 }

 @Test
 public void testDoubleSerializer_negative() throws JsonProcessingException {
 Double negativeDouble = -123.45;
 String json = mapper.writeValueAsString(negativeDouble);
 assertEquals(value("-123.45"), mapper.readTree(json).get(""));
 }

 @Test
 public void testDoubleSerializer_null() throws JsonProcessingException {
 Double nullValue = null;
 String json = mapper.writeValueAsString(nullValue);
 assertEquals(null, mapper.readTree(json));
 }

 @Test
 public void testDoubleSerializer_nan() throws JsonProcessingException {
 Double nan = Double.NaN;
 String json = mapper.writeValueAsString(nan);
 assertEquals(null, mapper.readTree(json));
 }

 @Test
 public void testDoubleSerializer_infinity() throws JsonProcessingException {
 Double infinity = Double.POSITIVE_INFINITY;
 String json = mapper.writeValueAsString(infinity);
 assertEquals(null, mapper.readTree(json));
 }

 // Add more test cases for handling cases with JsonFormat annotation, custom type serialization, etc.
}