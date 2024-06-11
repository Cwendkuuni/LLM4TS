import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
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
 Integer input = 123;
 String outputJson = "123";

 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
 NumberSerializers.IntegerSerializer serializer = new NumberSerializers.IntegerSerializer();
 serializer.serialize(input, jsonGenerator, new SerializerProvider() {});
 jsonGenerator.flush();

 String actualJson = jsonGenerator.getOutputString();
 assertEquals(outputJson, actualJson);
 }

 @Test
 public void testLongSerializer() throws JsonProcessingException {
 Long input = 123L;
 String outputJson = "123";

 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
 NumberSerializers.LongSerializer serializer = new NumberSerializers.LongSerializer();
 serializer.serialize(input, jsonGenerator, new SerializerProvider() {});
 jsonGenerator.flush();

 String actualJson = jsonGenerator.getOutputString();
 assertEquals(outputJson, actualJson);
 }

 @Test
 public void testShortSerializer() throws JsonProcessingException {
 Short input = (short) 123;
 String outputJson = "123";

 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
 NumberSerializers.ShortSerializer serializer = new NumberSerializers.ShortSerializer();
 serializer.serialize(input, jsonGenerator, new SerializerProvider() {});
 jsonGenerator.flush();

 String actualJson = jsonGenerator.getOutputString();
 assertEquals(outputJson, actualJson);
 }

 @Test
 public void testFloatSerializer() throws JsonProcessingException {
 Float input = 123.45f;
 String outputJson = "123.45";

 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
 NumberSerializers.FloatSerializer serializer = new NumberSerializers.FloatSerializer();
 serializer.serialize(input, jsonGenerator, new SerializerProvider() {});
 jsonGenerator.flush();

 String actualJson = jsonGenerator.getOutputString();
 assertEquals(outputJson, actualJson);
 }

 @Test
 public void testDoubleSerializer() throws JsonProcessingException {
 Double input = 123.45;
 String outputJson = "123.45";

 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
 NumberSerializers.DoubleSerializer serializer = new NumberSerializers.DoubleSerializer();
 serializer.serialize(input, jsonGenerator, new SerializerProvider() {});
 jsonGenerator.flush();

 String actualJson = jsonGenerator.getOutputString();
 assertEquals(outputJson, actualJson);
 }
}