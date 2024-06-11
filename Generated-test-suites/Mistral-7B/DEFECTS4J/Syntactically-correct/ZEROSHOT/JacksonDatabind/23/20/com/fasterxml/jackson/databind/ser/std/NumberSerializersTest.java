import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NumberSerializersTest {

 private ObjectMapper mapper;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 }

 @Test
 public void testIntegerSerializer() throws Exception {
 Integer input = 123;
 String output = mapper.writeValueAsString(input);
 JsonParser parser = mapper.getFactory().createParser(output);
 Object readValue = mapper.readValue(parser, Integer.class);
 assertEquals(input, readValue);
 }

 @Test
 public void testIntegerSerializerWithAnnotation() throws Exception {
 @JsonFormat(shape = JsonFormat.Shape.STRING)
 Integer input = 123;
 String output = mapper.writeValueAsString(input);
 JsonParser parser = mapper.getFactory().createParser(output);
 Object readValue = mapper.readValue(parser, Integer.class);
 assertEquals(input, readValue);
 }

 @Test
 public void testIntegerSerializerWithType() throws Exception {
 ObjectMapper mapperWithType = new ObjectMapper();
 TypeSerializer typeSerializer = mapperWithType.getTypeFactory().constructTypeSerializer(Integer.class);
 JsonGenerator gen = mapperWithType.getFactory().createGenerator(new ByteArrayOutputStream(), mapperWithType.getSerializerProvider().findValueSerializer(Object.class));
 mapperWithType.serializeValue(123, gen, mapperWithType.getSerializerProvider(), typeSerializer);
 ByteArrayOutputStream byteArrayOutputStream = (ByteArrayOutputStream) gen.getOutputStandaloneStream();
 String output = new String(byteArrayOutputStream.toByteArray());
 Object inputFromJson = mapperWithType.readValue(output, new TypeReference<Integer>() {});
 assertEquals(123, inputFromJson);
 }

 @Test
 public void testShortSerializer() throws Exception {
 Short input = 123;
 String output = mapper.writeValueAsString(input);
 JsonParser parser = mapper.getFactory().createParser(output);
 Object readValue = mapper.readValue(parser, Short.class);
 assertEquals(input, readValue);
 }

 @Test
 public void testShortSerializerWithAnnotation() throws Exception {
 @JsonFormat(shape = JsonFormat.Shape.STRING)
 Short input = 123;
 String output = mapper.writeValueAsString(input);
 JsonParser parser = mapper.getFactory().createParser(output);
 Object readValue = mapper.readValue(parser, Short.class);
 assertEquals(input, readValue);
 }

 @Test
 public void testLongSerializer() throws Exception {
 Long input = 123L;
 String output = mapper.writeValueAsString(input);
 JsonParser parser = mapper.getFactory().createParser(output);
 Object readValue = mapper.readValue(parser, Long.class);
 assertEquals(input, readValue);
 }

 @Test
 public void testLongSerializerWithAnnotation() throws Exception {
 @JsonFormat(shape = JsonFormat.Shape.STRING)
 Long input = 123L;
 String output = mapper.writeValueAsString(input);
 JsonParser parser = mapper.getFactory().createParser(output);
 Object readValue = mapper.readValue(parser, Long.class);
 assertEquals(input, readValue);
 }

 @Test
 public void testFloatSerializer() throws Exception {
 Float input = 1.2f;
 String output = mapper.writeValueAsString(input);
 JsonParser parser = mapper.getFactory().createParser(output);
 Object readValue = mapper.readValue(parser, Float.class);
 assertEquals(input, readValue);
 }

 @Test
 public void testFloatSerializerWithAnnotation() throws Exception {
 @JsonFormat(shape = JsonFormat.Shape.STRING)
 Float input = 1.2f;
 String output = mapper.writeValueAsString(input);
 JsonParser parser = mapper.getFactory().createParser(output);
 Object readValue = mapper.readValue(parser, Float.class);
 assertEquals(input, readValue);
 }

 @Test
 public void testDoubleSerializer() throws Exception {
 Double input = 1.2;
 String output = mapper.writeValueAsString(input);
 JsonParser parser = mapper.getFactory().createParser(output);
 Object readValue = mapper.readValue(parser, Double.class);
 assertEquals(input, readValue);
 }

 @Test
 public void testDoubleSerializerWithAnnotation() throws Exception {
 @JsonFormat(shape = JsonFormat.Shape.STRING)
 Double input = 1.2;
 String output = mapper.writeValueAsString(input);
 JsonParser parser = mapper.getFactory().createParser(output);
 Object readValue = mapper.readValue(parser, Double.class);
 assertEquals(input, readValue);
 }
}