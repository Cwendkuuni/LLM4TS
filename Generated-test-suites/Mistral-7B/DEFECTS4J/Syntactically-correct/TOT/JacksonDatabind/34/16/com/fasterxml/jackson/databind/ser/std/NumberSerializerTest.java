import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class NumberSerializerTest {

 private ObjectMapper mapper;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 }

 @Test
 public void testSerializeBigDecimal() throws Exception {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
 Number value = new BigDecimal("123.45");
 numberSerializer.serialize(value, jsonGenerator, mapper.getSerializerProvider());
 jsonGenerator.flush();
 String expected = "123.45";
 String actual = jsonGenerator.getOutputString();
 assertEquals("Expected '123.45' as JSON output, got: " + actual, expected, actual);
 }

 @Test
 public void testSerializeBigInteger() throws Exception {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
 Number value = new BigInteger("123456789012345678901234567890");
 numberSerializer.serialize(value, jsonGenerator, mapper.getSerializerProvider());
 jsonGenerator.flush();
 String expected = "123456789012345678901234567890";
 String actual = jsonGenerator.getOutputString();
 assertEquals("Expected '123456789012345678901234567890' as JSON output, got: " + actual, expected, actual);
 }

 @Test
 public void testSerializeInteger() throws Exception {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
 Number value = 123;
 numberSerializer.serialize(value, jsonGenerator, mapper.getSerializerProvider());
 jsonGenerator.flush();
 String expected = "123";
 String actual = jsonGenerator.getOutputString();
 assertEquals("Expected '123' as JSON output, got: " + actual, expected, actual);
 }

 @Test
 public void testSerializeLong() throws Exception {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
 Number value = 1234567890123456789L;
 numberSerializer.serialize(value, jsonGenerator, mapper.getSerializerProvider());
 jsonGenerator.flush();
 String expected = "1234567890123456789";
 String actual = jsonGenerator.getOutputString();
 assertEquals("Expected '1234567890123456789' as JSON output, got: " + actual, expected, actual);
 }

 @Test
 public void testSerializeDouble() throws Exception {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
 Number value = 123.45;
 numberSerializer.serialize(value, jsonGenerator, mapper.getSerializerProvider());
 jsonGenerator.flush();
 String expected = "123.45";
 String actual = jsonGenerator.getOutputString();
 assertEquals("Expected '123.45' as JSON output, got: " + actual, expected, actual);
 }

 @Test
 public void testSerializeFloat() throws Exception {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
 Number value = 123.45f;
 numberSerializer.serialize(value, jsonGenerator, mapper.getSerializerProvider());
 jsonGenerator.flush();
 String expected = "123.45";
 String actual = jsonGenerator.getOutputString();
 // Float serialization uses "float" instead of "number" in JSON:
 assertEquals("Expected '123.45' as JSON output, got: " + actual, "123.45", actual);
 }

 @Test
 public void testSerializeByte() throws Exception {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
 Number value = (byte) 127;
 numberSerializer.serialize(value, jsonGenerator, mapper.getSerializerProvider());
 jsonGenerator.flush();
 String expected = "127";
 String actual = jsonGenerator.getOutputString();
 assertEquals("Expected '127' as JSON output, got: " + actual, expected, actual);
 }

 @Test
 public void testSerializeShort() throws Exception {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
 Number value = (short) 12345;
 numberSerializer.serialize(value, jsonGenerator, mapper.getSerializerProvider());
 jsonGenerator.flush();
 String expected = "12345";
 String actual = jsonGenerator.getOutputString();
 assertEquals("Expected '12345' as JSON output, got: " + actual, expected, actual);
 }

 @Test
 public void testSerializeUnsupportedNumberType() throws Exception {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
 Number value = new ComplexNumber(1, 2); // Assume this is an unsupported Number subclass
 numberSerializer.serialize(value, jsonGenerator, mapper.getSerializerProvider());
 jsonGenerator.flush();
 String expected = "NaN";
 String actual = jsonGenerator.getOutputString();
 // In case of unsupported Number types, the serializer should write "NaN" to the output.
 assertEquals("Expected 'NaN' as JSON output, got: " + actual, expected, actual);
 }
}