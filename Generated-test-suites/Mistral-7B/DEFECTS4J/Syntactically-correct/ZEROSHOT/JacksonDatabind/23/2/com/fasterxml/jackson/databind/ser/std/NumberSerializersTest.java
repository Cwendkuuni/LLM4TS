import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.*;
import java.io.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class NumberSerializersTest {

 private ObjectMapper objectMapper;

 @BeforeAll
 public void setUp() throws Exception {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testIntegerSerializer() throws IOException {
 Integer input = 123;
 String output = "\"123\"";

 JsonSerializer<Object> serializer = objectMapper.findValueSerializer(Integer.class);
 JsonGenerator generator = objectMapper.getPolymorphicSerializerProvider().getGeneratorForCurrentType(null);
 TypeSerializer typeSerializer = objectMapper.getTypeSerializer();

 serializer.serialize(input, generator, objectMapper.getSerializerProvider(), typeSerializer);
 String actualOutput = generator.getValueAsString();

 assertEquals(output, actualOutput, "Expected output \"123\" but got \"%s\" instead".formatted(actualOutput));
 }

 @Test
 public void testIntegerSerializerWithType() throws IOException {
 Integer input = 123;
 String output = "123";

 JsonSerializer<Object> serializer = objectMapper.findValueSerializer(Integer.class);
 JsonGenerator generator = objectMapper.getPolymorphicSerializerProvider().getGeneratorForCurrentType(null);
 TypeSerializer typeSerializer = objectMapper.getTypeSerializer();

 serializer.serializeWithType(input, generator, objectMapper.getSerializerProvider(), typeSerializer);
 String actualOutput = generator.getValueAsString();

 assertEquals(output, actualOutput, "Expected output \"123\" but got \"%s\" instead".formatted(actualOutput));
 }

 @Test
 public void testLongSerializer() throws IOException {
 Long input = 1234567890123L;
 String output = "\"1234567890123\"";

 JsonSerializer<Object> serializer = objectMapper.findValueSerializer(Long.class);
 JsonGenerator generator = objectMapper.getPolymorphicSerializerProvider().getGeneratorForCurrentType(null);
 TypeSerializer typeSerializer = objectMapper.getTypeSerializer();

 serializer.serialize(input, generator, objectMapper.getSerializerProvider(), typeSerializer);
 String actualOutput = generator.getValueAsString();

 assertEquals(output, actualOutput, "Expected output \"1234567890123\" but got \"%s\" instead".formatted(actualOutput));
 }

 @Test
 public void testLongSerializerWithType() throws IOException {
 Long input = 1234567890123L;
 String output = "1234567890123";

 JsonSerializer<Object> serializer = objectMapper.findValueSerializer(Long.class);
 JsonGenerator generator = objectMapper.getPolymorphicSerializerProvider().getGeneratorForCurrentType(null);
 TypeSerializer typeSerializer = objectMapper.getTypeSerializer();

 serializer.serializeWithType(input, generator, objectMapper.getSerializerProvider(), typeSerializer);
 String actualOutput = generator.getValueAsString();

 assertEquals(output, actualOutput, "Expected output \"1234567890123\" but got \"%s\" instead".formatted(actualOutput));
 }

 @Test
 public void testShortSerializer() throws IOException {
 Short input = 127;
 String output = "\"127\"";

 JsonSerializer<Short> serializer = objectMapper.findValueSerializer(Short.class);
 JsonGenerator generator = objectMapper.getPolymorphicSerializerProvider().getGeneratorForCurrentType(null);
 TypeSerializer typeSerializer = objectMapper.getTypeSerializer();

 serializer.serialize(input, generator, objectMapper.getSerializerProvider(), typeSerializer);
 String actualOutput = generator.getValueAsString();

 assertEquals(output, actualOutput, "Expected output \"127\" but got \"%s\" instead".formatted(actualOutput));
 }

 @Test
 public void testShortSerializerWithType() throws IOException {
 Short input = 127;
 String output = "127";

 JsonSerializer<Short> serializer = objectMapper.findValueSerializer(Short.class);
 JsonGenerator generator = objectMapper.getPolymorphicSerializerProvider().getGeneratorForCurrentType(null);
 TypeSerializer typeSerializer = objectMapper.getTypeSerializer();

 serializer.serializeWithType(input, generator, objectMapper.getSerializerProvider(), typeSerializer);
 String actualOutput = generator.getValueAsString();

 assertEquals(output, actualOutput, "Expected output \"127\" but got \"%s\" instead".formatted(actualOutput));
 }

 @Test
 public void testFloatSerializer() throws IOException {
 Float input = 3.14F;
 String output = "3.14";

 JsonSerializer<Object> serializer = objectMapper.findValueSerializer(Float.class);
 JsonGenerator generator = objectMapper.getPolymorphicSerializerProvider().getGeneratorForCurrentType(null);
 TypeSerializer typeSerializer = objectMapper.getTypeSerializer();

 serializer.serialize(input, generator, objectMapper.getSerializerProvider(), typeSerializer);
 String actualOutput = generator.getValueAsString();

 assertEquals(output, actualOutput, "Expected output \"3.14\" but got \"%s\" instead".formatted(actualOutput));
 }

 @Test
 public void testFloatSerializerWithType() throws IOException {
 Float input = 3.14F;
 String output = "3.14";

 JsonSerializer<Object> serializer = objectMapper.findValueSerializer(Float.class);
 JsonGenerator generator = objectMapper.getPolymorphicSerializerProvider().getGeneratorForCurrentType(null);
 TypeSerializer typeSerializer = objectMapper.getTypeSerializer();

 serializer.serializeWithType(input, generator, objectMapper.getSerializerProvider(), typeSerializer);
 String actualOutput = generator.getValueAsString();

 assertEquals(output, actualOutput, "Expected output \"3.14\" but got \"%s\" instead".formatted(actualOutput));
 }

 @Test
 public void testDoubleSerializer() throws IOException {
 Double input = 3.14159265358979323846;
 String output = "3.14159265358979323846";

 JsonSerializer<Object> serializer = objectMapper.findValueSerializer(Double.class);
 JsonGenerator generator = objectMapper.getPolymorphicSerializerProvider().getGeneratorForCurrentType(null);
 TypeSerializer typeSerializer = objectMapper.getTypeSerializer();

 serializer.serialize(input, generator, objectMapper.getSerializerProvider(), typeSerializer);
 String actualOutput = generator.getValueAsString();

 assertEquals(output, actualOutput, "Expected output \"3.14159265358979323846\" but got \"%s\" instead".formatted(actualOutput));
 }

 @Test
 public void testDoubleSerializerWithType() throws IOException {
 Double input = 3.14159265358979323846;
 String output = "3.14159265358979323846";

 JsonSerializer<Object> serializer = objectMapper.findValueSerializer(Double.class);
 JsonGenerator generator = objectMapper.getPolymorphicSerializerProvider().getGeneratorForCurrentType(null);
 TypeSerializer typeSerializer = objectMapper.getTypeSerializer();

 serializer.serializeWithType(input, generator, objectMapper.getSerializerProvider(), typeSerializer);
 String actualOutput = generator.getValueAsString();

 assertEquals(output, actualOutput, "Expected output \"3.14159265358979323846\" but got \"%s\" instead".formatted(actualOutput));
 }
}