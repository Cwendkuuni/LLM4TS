import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import static org.junit.Assert.assertEquals;

public class NumberSerializersTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 SimpleModule module = new SimpleModule();
 NumberSerializers.addAll(module.getDefaultSerializerProvider().getDeserializerProvider().findValueDeserializers());
 objectMapper = new ObjectMapper(module);
 }

 @Test
 public void testIntegerSerializer() throws IOException {
 Object intValue = 123;
 String json = objectMapper.writeValueAsString(intValue);
 JsonParser parser = objectMapper.getFactory().createParser(json);
 assertEquals(intValue, parser.readValueAs(Integer.class));
 }

 @Test
 public void testIntegerSerializerNegative() throws IOException {
 Object intValue = Integer.MIN_VALUE;
 String json = objectMapper.writeValueAsString(intValue);
 JsonParser parser = objectMapper.getFactory().createParser(json);
 assertEquals(intValue, parser.readValueAs(Integer.class));
 }

 @Test
 public void testIntegerSerializerNaN() throws IOException {
 Object intValue = Double.NaN;
 String json = objectMapper.writeValueAsString(intValue);
 JsonParser parser = objectMapper.getFactory().createParser(json);
 assertEquals(intValue, parser.readValueAs(Integer.class));
 }

 @Test(expected = IOException.class)
 public void testIntegerSerializerNull() throws IOException {
 Object intValue = null;
 String json = objectMapper.writeValueAsString(intValue);
 JsonParser parser = objectMapper.getFactory().createParser(json);
 parser.nextToken(); // consume [null] token
 parser.nextToken(); // consume start object token
 parser.nextToken(); // consume "int" field name
 parser.nextToken(); // consume null value
 parser.nextToken(); // consume end object token
 parser.nextToken(); // consume EOF token
 parser.nextToken(); // consume unexpected EOF token
 }

 @Test
 public void testIntegerSerializerBigInteger() throws IOException {
 Object bigIntegerValue = new Long("9223372036854775807L");
 String json = objectMapper.writeValueAsString(bigIntegerValue);
 JsonParser parser = objectMapper.getFactory().createParser(json);
 assertEquals(bigIntegerValue, parser.readValueAs(Long.class));
 }

 // Add test cases for other NumberSerializers (ShortSerializer, LongSerializer, FloatSerializer, DoubleSerializer) here

}