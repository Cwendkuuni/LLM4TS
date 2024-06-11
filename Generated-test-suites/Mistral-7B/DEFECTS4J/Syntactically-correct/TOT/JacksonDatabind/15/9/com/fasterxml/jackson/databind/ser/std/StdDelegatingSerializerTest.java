import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

import static com.fasterxml.jackson.databind.SerializationFeature.FAIL_ON_EMPTY_BEANS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class StdDelegatingSerializerTest {

 private ObjectMapper objectMapper;
 private StdDelegatingSerializer stringSerializer;
 private StdDelegatingSerializer intSerializer;

 @Before
 public void setup() {
 ObjectMapper.getDefaultInstance().configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 objectMapper = new ObjectMapper();
 stringSerializer = new StdDelegatingSerializer(String.class, new StringToNumberConverter());
 intSerializer = new StdDelegatingSerializer(int[].class, new NumberToStringConverter());
 }

 @Test
 public void testSerializeString() throws JsonGenerationException, IOException {
 Object input = "Test String";
 String expected = "\"Test String\"";

 String output = stringSerializer.serializeWithType(input, null, objectMapper.getSerializerProvider(), null);

 assertNotNull(output);
 assertEquals(expected, output.getTextValue());
 }

 @Test
 public void testSerializeIntArray() throws JsonGenerationException, IOException {
 Object input = new int[]{1, 2, 3};
 int[] expected = {1, 2, 3};

 int[] output = intSerializer.deserialize(inputSerializer.writeValueAsBytes(input), int[].class);

 assertNotNull(output);
 assertEquals(Arrays.asList(expected), Arrays.asList(output));
 }

 @Test
 public void testNullInput() throws JsonGenerationException, IOException {
 Object input = null;
 String expected = "null";

 String output = stringSerializer.serializeWithType(input, null, objectMapper.getSerializerProvider(), null);

 assertNotNull(output);
 assertEquals(expected, output.getTextValue());
 }

 @Test(expected = JsonMappingException.class)
 public void testMismatchedTypes() throws JsonGenerationException, IOException {
 Object input = new int[]{1, 2, 3};
 String output = stringSerializer.serializeWithType(input, null, objectMapper.getSerializerProvider(), null);

 assertNotNull(output);
 }

 @Test
 public void testGetSchema() throws JsonMappingException {
 ObjectMapper mapper = new ObjectMapper();
 JsonNode expected = mapper.readTree("{\"title\":\"Number\",\"type\":\"array\",\"items\":{\"title\":\"Number\",\"type\":\"integer\"}}");

 JsonNode output = stringSerializer.getSchema(objectMapper.getSerializerProvider(), Object.class);

 assertNotNull(output);
 assertEquals(expected.toString(), output.toString());
 }

 @Test
 public void testAcceptJsonFormatVisitor() throws JsonMappingException {
 ObjectMapper mapper = new ObjectMapper();
 JsonFormatVisitorWrapper visitor = mapper.schemaFactory().getJsonSchemaFactory().getJsonFormatVisitor(SchemaFormat.FULL, null);

 stringSerializer.acceptJsonFormatVisitor(visitor, Object.class);

 JsonNode output = visitor.getSchema();

 assertNotNull(output);
 }

 class NumberToStringConverter implements Converter<Number, String> {
 @Override
 public String convert(Number src) {
 return String.valueOf(src.intValue());
 }
 }

 class StringToNumberConverter implements Converter<String, Number> {
 @Override
 public Number convert(String src) {
 return Integer.parseInt(src);
 }
 }
}