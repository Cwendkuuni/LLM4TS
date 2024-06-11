import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializersTest {

 @Test
 public void testIntegerSerializer() throws JsonProcessingException {
 Integer value = 123;
 ObjectMapper mapper = new ObjectMapper();
 String json = mapper.writeValueAsString(value);
 JsonParser parser = mapper.getFactory().createParser(json);
 assertEquals(value.intValue(), parser.readValueAs(Integer.class));
 }

 @Test
 public void testIntegerSerializerWithNegativeValue() throws JsonProcessingException {
 Integer value = -123;
 ObjectMapper mapper = new ObjectMapper();
 String json = mapper.writeValueAsString(value);
 JsonParser parser = mapper.getFactory().createParser(json);
 assertEquals(value.intValue(), parser.readValueAs(Integer.class));
 }

 @Test
 public void testIntegerSerializerWithZero() throws JsonProcessingException {
 Integer value = 0;
 ObjectMapper mapper = new ObjectMapper();
 String json = mapper.writeValueAsString(value);
 JsonParser parser = mapper.getFactory().createParser(json);
 assertEquals(value.intValue(), parser.readValueAs(Integer.class));
 }

 @Test(expected = IOException.class)
 public void testIntegerSerializerWithNonIntegerValue() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 String json = mapper.writeValueAsString("not an integer");
 JsonParser parser = mapper.getFactory().createParser(json);
 parser.nextToken(); // consume START_OBJECT token
 parser.nextToken(); // consume NUMBER token (with non-integer value)
 parser.readValueAs(Integer.class); // throws IOException
 }

 @Test
 public void testIntegerSerializerWithMinValue() throws JsonProcessingException {
 Integer minValue = Integer.MIN_VALUE;
 ObjectMapper mapper = new ObjectMapper();
 String json = mapper.writeValueAsString(minValue);
 JsonParser parser = mapper.getFactory().createParser(json);
 assertEquals(minValue.intValue(), parser.readValueAs(Integer.class));
 }

 @Test
 public void testIntegerSerializerWithMaxValue() throws JsonProcessingException {
 Integer maxValue = Integer.MAX_VALUE;
 ObjectMapper mapper = new ObjectMapper();
 String json = mapper.writeValueAsString(maxValue);
 JsonParser parser = mapper.getFactory().createParser(json);
 assertEquals(maxValue.intValue(), parser.readValueAs(Integer.class));
 }

 // ... similar tests for other serializers
}