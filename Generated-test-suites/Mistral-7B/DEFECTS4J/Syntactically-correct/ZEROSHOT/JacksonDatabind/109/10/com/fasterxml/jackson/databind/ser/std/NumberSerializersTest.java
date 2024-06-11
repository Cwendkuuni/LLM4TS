import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
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
 String json = "\"123\"";

 JsonSerializer<Integer> integerSerializer = objectMapper.findValueSerializer(Integer.class);
 JsonGenerator generator = objectMapper.getPolymorphicSerializerProvider().getGenerator();

 integerSerializer.serialize(number, generator, objectMapper.getSerializerProvider());
 assertEquals(json, generator.getOutputContent());
 }

 @Test
 public void testIntegerSerializerWithType() throws JsonProcessingException {
 Integer number = 123;
 String json = "{\"@type\":\"java.lang.Integer\",\"number\":123}";

 JsonSerializer<Integer> integerSerializer = objectMapper.findValueSerializer(Integer.class);
 TypeSerializer typeSerializer = objectMapper.getSerializerProvider().getTypeSerializer();
 JsonGenerator generator = objectMapper.getPolymorphicSerializerProvider().getGenerator();

 integerSerializer.serializeWithType(number, generator, objectMapper.getSerializerProvider(), typeSerializer);
 assertEquals(json, generator.getOutputContent());
 }

 @Test
 public void testShortSerializer() throws JsonProcessingException {
 Short number = 123;
 String json = "\"123\"";

 JsonSerializer<Short> shortSerializer = objectMapper.findValueSerializer(Short.class);
 JsonGenerator generator = objectMapper.getPolymorphicSerializerProvider().getGenerator();

 shortSerializer.serialize(number, generator, objectMapper.getSerializerProvider());
 assertEquals(json, generator.getOutputContent());
 }

 @Test
 public void testShortSerializerWithType() throws JsonProcessingException {
 Short number = 123;
 String json = "{\"@type\":\"java.lang.Short\",\"number\":123}";

 JsonSerializer<Short> shortSerializer = objectMapper.findValueSerializer(Short.class);
 TypeSerializer typeSerializer = objectMapper.getSerializerProvider().getTypeSerializer();
 JsonGenerator generator = objectMapper.getPolymorphicSerializerProvider().getGenerator();

 shortSerializer.serializeWithType(number, generator, objectMapper.getSerializerProvider(), typeSerializer);
 assertEquals(json, generator.getOutputContent());
 }

 @Test
 public void testLongSerializer() throws JsonProcessingException {
 Long number = 123L;
 String json = "\"123\"";

 JsonSerializer<Long> longSerializer = objectMapper.findValueSerializer(Long.class);
 JsonGenerator generator = objectMapper.getPolymorphicSerializerProvider().getGenerator();

 longSerializer.serialize(number, generator, objectMapper.getSerializerProvider());
 assertEquals(json, generator.getOutputContent());
 }

 @Test
 public void testLongSerializerWithType() throws JsonProcessingException {
 Long number = 123L;
 String json = "{\"@type\":\"java.lang.Long\",\"number\":123}";

 JsonSerializer<Long> longSerializer = objectMapper.findValueSerializer(Long.class);
 TypeSerializer typeSerializer = objectMapper.getSerializerProvider().getTypeSerializer();
 JsonGenerator generator = objectMapper.getPolymorphicSerializerProvider().getGenerator();

 longSerializer.serializeWithType(number, generator, objectMapper.getSerializerProvider(), typeSerializer);
 assertEquals(json, generator.getOutputContent());
 }

 @Test
 public void testFloatSerializer() throws JsonProcessingException {
 Float number = 123.45f;
 String json = "123.45";

 JsonSerializer<Float> floatSerializer = objectMapper.findValueSerializer(Float.class);
 JsonGenerator generator = objectMapper.getPolymorphicSerializerProvider().getGenerator();

 floatSerializer.serialize(number, generator, objectMapper.getSerializerProvider());
 assertEquals(json, generator.getOutputContent());
 }

 @Test
 public void testFloatSerializerWithType() throws JsonProcessingException {
 Float number = 123.45f;
 String json = "{\"@type\":\"java.lang.Float\",\"number\":123.45}";

 JsonSerializer<Float> floatSerializer = objectMapper.findValueSerializer(Float.class);
 TypeSerializer typeSerializer = objectMapper.getSerializerProvider().getTypeSerializer();
 JsonGenerator generator = objectMapper.getPolymorphicSerializerProvider().getGenerator();

 floatSerializer.serializeWithType(number, generator, objectMapper.getSerializerProvider(), typeSerializer);
 assertEquals(json, generator.getOutputContent());
 }

 @Test
 public void testDoubleSerializer() throws JsonProcessingException {
 Double number = 123.45;
 String json = "123.45";

 JsonSerializer<Double> doubleSerializer = objectMapper.findValueSerializer(Double.class);
 JsonGenerator generator = objectMapper.getPolymorphicSerializerProvider().getGenerator();

 doubleSerializer.serialize(number, generator, objectMapper.getSerializerProvider());
 assertEquals(json, generator.getOutputContent());
 }

 @Test
 public void testDoubleSerializerWithType() throws JsonProcessingException {
 Double number = 123.45;
 String json = "{\"@type\":\"java.lang.Double\",\"number\":123.45}";

 JsonSerializer<Double> doubleSerializer = objectMapper.findValueSerializer(Double.class);
 TypeSerializer typeSerializer = objectMapper.getSerializerProvider().getTypeSerializer();
 JsonGenerator generator = objectMapper.getPolymorphicSerializerProvider().getGenerator();

 doubleSerializer.serializeWithType(number, generator, objectMapper.getSerializerProvider(), typeSerializer);
 assertEquals(json, generator.getOutputContent());
 }

 @Test
 public void testIntLikeSerializer() throws JsonProcessingException {
 Byte number = 123;
 String json = "\"123\"";

 JsonSerializer<Byte> intLikeSerializer = objectMapper.findValueSerializer(Byte.class);
 JsonGenerator generator = objectMapper.getPolymorphicSerializerProvider().getGenerator();

 intLikeSerializer.serialize(number, generator, objectMapper.getSerializerProvider());
 assertEquals(json, generator.getOutputContent());
 }

 @Test
 public void testIntLikeSerializerWithType() throws JsonProcessingException {
 Byte number = 123;
 String json = "{\"@type\":\"java.lang.Byte\",\"number\":123}";

 JsonSerializer<Byte> intLikeSerializer = objectMapper.findValueSerializer(Byte.class);
 TypeSerializer typeSerializer = objectMapper.getSerializerProvider().getTypeSerializer();
 JsonGenerator generator = objectMapper.getPolymorphicSerializerProvider().getGenerator();

 intLikeSerializer.serializeWithType(number, generator, objectMapper.getSerializerProvider(), typeSerializer);
 assertEquals(json, generator.getOutputContent());
 }
}