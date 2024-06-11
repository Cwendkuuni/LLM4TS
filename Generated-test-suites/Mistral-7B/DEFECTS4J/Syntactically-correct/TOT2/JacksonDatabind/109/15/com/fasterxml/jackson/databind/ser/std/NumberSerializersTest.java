import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers;
import org.junit.Assert;
import org.junit.Test;

public class NumberSerializersTest {

 private ObjectMapper objectMapper = new JsonMapper();

 @Test
 public void testIntegerSerializer_IntegerValue() throws JsonProcessingException {
 Integer value = 123;
 NumberSerializers.IntegerSerializer intSerializer = new NumberSerializers.IntegerSerializer(Integer.class);
 String json = intSerializer.serialize(value, null, new SerializerProvider());

 Assert.assertEquals("123", json);
 }

 @Test
 public void testIntegerSerializer_IntValue() throws JsonProcessingException {
 int value = 123;
 NumberSerializers.IntegerSerializer intSerializer = new NumberSerializers.IntegerSerializer(Integer.class);
 String json = intSerializer.serialize(value, null, new SerializerProvider());

 Assert.assertEquals("123", json);
 }

 @Test
 public void testIntegerSerializer_NegativeValue() throws JsonProcessingException {
 Integer value = -123;
 NumberSerializers.IntegerSerializer intSerializer = new NumberSerializers.IntegerSerializer(Integer.class);
 String json = intSerializer.serialize(value, null, new SerializerProvider());

 Assert.assertEquals("-123", json);
 }

 @Test
 public void testIntegerSerializer_ZeroValue() throws JsonProcessingException {
 Integer value = 0;
 NumberSerializers.IntegerSerializer intSerializer = new NumberSerializers.IntegerSerializer(Integer.class);
 String json = intSerializer.serialize(value, null, new SerializerProvider());

 Assert.assertEquals("0", json);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIntegerSerializer_NullValue() {
 NumberSerializers.IntegerSerializer intSerializer = new NumberSerializers.IntegerSerializer(Integer.class);
 int value = null;
 String json = intSerializer.serialize(value, null, new SerializerProvider());
 }

 @Test
 public void testIntLikeSerializer_NumberValue() throws JsonProcessingException {
 Number value = 123;
 NumberSerializers.IntLikeSerializer intLikeSerializer = NumberSerializers.IntLikeSerializer.instance;
 String json = intLikeSerializer.serialize(value, null, new SerializerProvider());

 Assert.assertEquals("123", json);
 }

 @Test
 public void testIntLikeSerializer_ByteValue() throws JsonProcessingException {
 Byte value = 127;
 NumberSerializers.IntLikeSerializer intLikeSerializer = NumberSerializers.IntLikeSerializer.instance;
 String json = intLikeSerializer.serialize(value, null, new SerializerProvider());

 Assert.assertEquals("127", json);
 }

 @Test
 public void testIntLikeSerializer_ShortValue() throws JsonProcessingException {
 Short value = 32767;
 NumberSerializers.IntLikeSerializer intLikeSerializer = NumberSerializers.IntLikeSerializer.instance;
 String json = intLikeSerializer.serialize(value, null, new SerializerProvider());

 Assert.assertEquals("32767", json);
 }

 @Test
 public void testLongSerializer_LongValue() throws JsonProcessingException {
 Long value = 123456789012345L;
 NumberSerializers.LongSerializer longSerializer = new NumberSerializers.LongSerializer(Long.class);
 String json = longSerializer.serialize(value, null, new SerializerProvider());

 Assert.assertEquals("123456789012345", json);
 }

 @Test
 public void testLongSerializer_StringValue() throws JsonProcessingException {
 String value = "123456789012345";
 NumberSerializers.LongSerializer longSerializer = new NumberSerializers.LongSerializer(Long.class);
 Number deserializedValue = objectMapper.readValue(value, Number.class);
 Long longValue = (Long) deserializedValue;

 Assert.assertEquals(123456789012345L, longValue.longValue());
 }

 @Test
 public void testFloatSerializer_FloatValue() throws JsonProcessingException {
 Float value = 123.45F;
 NumberSerializers.FloatSerializer floatSerializer = NumberSerializers.FloatSerializer.instance;
 String json = floatSerializer.serialize(value, null, new SerializerProvider());

 Assert.assertEquals("123.45", json);
 }

 @Test
 public void testFloatSerializer_DoubleValue() throws JsonProcessingException {
 Double value = 123.45;
 NumberSerializers.FloatSerializer floatSerializer = NumberSerializers.FloatSerializer.instance;
 String json = floatSerializer.serialize(value, null, new SerializerProvider());

 Assert.assertEquals("123.45", json);
 }

 @Test
 public void testDoubleSerializer_DoubleValue() throws JsonProcessingException {
 Double value = 123.45;
 NumberSerializers.DoubleSerializer doubleSerializer = new NumberSerializers.DoubleSerializer(Double.class);
 String json = doubleSerializer.serialize(value, null, new SerializerProvider());

 Assert.assertEquals("123.45", json);
 }

 @Test
 public void testDoubleSerializer_StringValue() throws JsonProcessingException {
 String value = "123.45";
 NumberSerializers.DoubleSerializer doubleSerializer = new NumberSerializers.DoubleSerializer(Double.class);
 Number deserializedValue = objectMapper.readValue(value, Number.class);
 Double doubleValue = (Double) deserializedValue;

 Assert.assertEquals(123.45, doubleValue.doubleValue(), 0.001);
 }
}