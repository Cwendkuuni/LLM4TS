import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializersTest {

 private ObjectMapper mapper;

 @BeforeClass
 public static void setup() {
 mapper = new ObjectMapper();
 }

 @Test
 public void testSerializer_Short() throws IOException {
 Short number = Short.MIN_VALUE;
 String json = mapper.writerWithTypeValidator(false).writeValueAsString(number);
 assertNotNull(json);
 assertEquals("-1", json);
 }

 @Test
 public void testSerializer_Integer() throws IOException {
 Integer number = Integer.MAX_VALUE;
 String json = mapper.writerWithTypeValidator(false).writeValueAsString(number);
 assertNotNull(json);
 assertEquals("9223372036854775807", json);
 }

 @Test
 public void testSerializer_IntegerString() throws IOException {
 String input = "\"123\"";
 Short output = mapper.readValue(input, Short.class);
 assertEquals((short) 123, output.shortValue());
 }

 @Test
 public void testSerializer_Long() throws IOException {
 Long number = Long.MIN_VALUE;
 String json = mapper.writerWithTypeValidator(false).writeValueAsString(number);
 assertNotNull(json);
 assertEquals("-9223372036854775808", json);
 }

 @Test
 public void testSerializer_Float() throws IOException {
 Float number = Float.MAX_VALUE;
 String json = mapper.writerWithTypeValidator(false).writeValueAsString(number);
 assertNotNull(json);
 assertEquals("3.4028235E38", json);
 }

 @Test
 public void testSerializer_Double() throws IOException {
 Double number = Double.MAX_VALUE;
 String json = mapper.writerWithTypeValidator(false).writeValueAsString(number);
 assertNotNull(json);
 assertEquals("1.79769313486231E308", json);
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializer_InvalidType() throws IOException {
 String invalidInput = "\"invalid_type\"";
 mapper.readValue(invalidInput, Short.class);
 fail("Expected JsonMappingException but nothing was thrown");
 }
}