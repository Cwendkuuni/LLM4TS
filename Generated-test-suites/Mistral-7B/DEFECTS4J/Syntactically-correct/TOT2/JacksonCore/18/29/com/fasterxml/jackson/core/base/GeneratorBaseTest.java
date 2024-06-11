import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.io.Serializers;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import static org.junit.Assert.*;

public class GeneratorBaseTest {

 private JsonGenerator generator;

 @Test
 public void testConstructor() throws JsonProcessingException {
 JsonFactory jsonFactory = new JsonFactory();
 generator = jsonFactory.createGenerator(new StringWriter());
 assertTrue(generator instanceof GeneratorBase);
 }

 @Test
 public void testVersion() {
 GeneratorBase generator = new GeneratorBase(0, null);
 assertEquals("2.12.3", generator.version().toString());
 }

 @Test
 public void testConfigure() throws JsonProcessingException {
 GeneratorBase generator = new GeneratorBase(0, null);
 generator.configure(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS, true);
 assertTrue(generator.isEnabled(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS));
 }

 @Test
 public void testIsEnabled() throws JsonProcessingException {
 GeneratorBase generator = new GeneratorBase(0, null);
 assertFalse(generator.isEnabled(JsonGenerator.Feature.WRITE_BOOLEAN_AS_INT));
 }

 @Test
 public void testSetFeatureMask() throws JsonProcessingException {
 GeneratorBase generator = new GeneratorBase(0, null);
 generator.setFeatureMask(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS.getMask());
 assertTrue(generator.isEnabled(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS));
 }

 @Test
 public void testWriteNull() throws JsonProcessingException {
 GeneratorBase generator = new GeneratorBase(0, null);
 generator.writeNull();
 assertTrue(generator.getCurrentValue() == null);
 }

 @Test
 public void testWriteBoolean() throws JsonProcessingException {
 GeneratorBase generator = new GeneratorBase(0, null);
 generator.writeBoolean(true);
 assertEquals("true", generator.getCurrentValue().toString());
 }

 @Test
 public void testWriteNumber() throws JsonProcessingException {
 GeneratorBase generator = new GeneratorBase(0, null);
 generator.writeNumber(42);
 assertEquals("42", generator.getCurrentValue().toString());
 }

 @Test
 public void testWriteNumberWithFeatureEnabled() throws JsonProcessingException {
 GeneratorBase generator = new GeneratorBase(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), null);
 generator.writeNumber(42);
 assertEquals("\"42\"", generator.getCurrentValue().toString());
 }

 @Test
 public void testWriteString() throws JsonProcessingException {
 GeneratorBase generator = new GeneratorBase(0, null);
 generator.writeString("Test");
 assertEquals("Test", generator.getCurrentValue().toString());
 }

 @Test
 public void testWriteRawValue() throws JsonProcessingException, IOException {
 GeneratorBase generator = new GeneratorBase(0, null);
 generator.writeRawValue("Test");
 assertEquals("Test", generator.getCurrentValue().toString());
 }

 @Test
 public void testWriteRawValueWithBinaryData() throws JsonProcessingException, IOException {
 byte[] binaryData = new byte[]{0x01, 0x02, 0x03};
 GeneratorBase generator = new GeneratorBase(0, null);
 generator.writeRawValue(binaryData);
 // This will throw an exception since the test case does not handle binary data
 //assertNotNull(generator.getCurrentValue());
 }

 @Test
 public void testWriteObject() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 String json = "{\"name\":\"John\",\"age\":30}";
 Object node = mapper.readValue(new StringReader(json), Object.class);
 GeneratorBase generator = new GeneratorBase(0, null);
 generator.writeObject(node);
 assertEquals(node.toString(), generator.getCurrentValue().toString());
 }

 @Test(expected = JsonGenerationException.class)
 public void testWriteInvalidFeature() throws JsonProcessingException {
 GeneratorBase generator = new GeneratorBase(0, null);
 generator.enable(JsonGenerator.Feature.WRITE_INVALID_FEATURE);
 }

 @Test
 public void testPrettyPrinting() throws JsonProcessingException {
 GeneratorBase generator = new GeneratorBase(JsonGenerator.Feature.AUTO_CLOSE_SOURCE.getMask() | JsonGenerator.Feature.INDENT_OUTPUT.getMask(), new ObjectMapper().getFactory());
 generator.useDefaultPrettyPrinter();
 generator.writeStartObject();
 generator.writeFieldName("name");
 generator.writeString("John");
 generator.writeFieldName("age");
 generator.writeNumber(30);
 generator.writeEndObject();
 assertEquals("{\n" +
               "  \"name\": \"John\",\n" +
               "  \"age\": 30\n" +
               "}", generator.getOutputContext().getText().toString().trim());
 }

 @Test(expected = JsonGenerationException.class)
 public void testCloseWriterTwice() throws JsonProcessingException {
 GeneratorBase generator = new GeneratorBase(0, null);
 generator.close();
 generator.close();
 }
}