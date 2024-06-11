import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NumberSerializersTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 }

 /**
 * Test for IntegerSerializer
 */
 @Test
 public void testIntegerSerializer() throws Exception {
 Integer number = 123;
 String json = "{\"number\":123}";

 // Test serialization
 ObjectWriter writer = objectMapper.writerWithRootValue(new Object());
 String serialized = writer.writeValueAsString(number);
 assertEquals(json, serialized);

 // Test deserialization
 JsonParser parser = objectMapper.getFactory().createParser(json);
 JsonToken token = parser.nextToken();
 assertEquals(JsonToken.START_OBJECT, token);
 Object deserialized = parser.readValueAs(Integer.class);
 assertEquals(number, deserialized);
 }

 /**
 * Test for IntegerSerializer with negative number
 */
 @Test
 public void testIntegerSerializerNegative() throws Exception {
 Integer number = -123;
 String json = "{\"number\":-123}";

 // Test serialization
 ObjectWriter writer = objectMapper.writerWithRootValue(new Object());
 String serialized = writer.writeValueAsString(number);
 assertEquals(json, serialized);

 // Test deserialization
 JsonParser parser = objectMapper.getFactory().createParser(json);
 JsonToken token = parser.nextToken();
 assertEquals(JsonToken.START_OBJECT, token);
 Object deserialized = parser.readValueAs(Integer.class);
 assertEquals(number, deserialized);
 }

 /**
 * Test for IntegerSerializer with max value
 */
 @Test
 public void testIntegerSerializerMaxValue() throws Exception {
 Integer number = Integer.MAX_VALUE;
 String json = "{\"number\":" + Integer.MAX_VALUE + "}";

 // Test serialization
 ObjectWriter writer = objectMapper.writerWithRootValue(new Object());
 String serialized = writer.writeValueAsString(number);
 assertEquals(json, serialized);

 // Test deserialization
 JsonParser parser = objectMapper.getFactory().createParser(json);
 JsonToken token = parser.nextToken();
 assertEquals(JsonToken.START_OBJECT, token);
 Object deserialized = parser.readValueAs(Integer.class);
 assertEquals(number, deserialized);
 }

 /**
 * Test for IntegerSerializer with min value
 */
 @Test
 public void testIntegerSerializerMinValue() throws Exception {
 Integer number = Integer.MIN_VALUE;
 String json = "{\"number\":" + Integer.MIN_VALUE + "}";

 // Test serialization
 ObjectWriter writer = objectMapper.writerWithRootValue(new Object());
 String serialized = writer.writeValueAsString(number);
 assertEquals(json, serialized);

 // Test deserialization
 JsonParser parser = objectMapper.getFactory().createParser(json);
 JsonToken token = parser.nextToken();
 assertEquals(JsonToken.START_OBJECT, token);
 Object deserialized = parser.readValueAs(Integer.class);
 assertEquals(number, deserialized);
 }

 /**
 * Test for LongSerializer
 */
 @Test
 public void testLongSerializer() throws Exception {
 Long number = 123L;
 String json = "{\"number\":123}";

 // Test serialization
 ObjectWriter writer = objectMapper.writerWithRootValue(new Object());
 String serialized = writer.writeValueAsString(number);
 assertEquals(json, serialized);

 // Test deserialization
 JsonParser parser = objectMapper.getFactory().createParser(json);
 JsonToken token = parser.nextToken();
 assertEquals(JsonToken.START_OBJECT, token);
 Object deserialized = parser.readValueAs(Long.class);
 assertEquals(number, deserialized);
 }

 /**
 * Test for LongSerializer with negative number
 */
 @Test
 public void testLongSerializerNegative() throws Exception {
 Long number = -123L;
 String json = "{\"number\":-123}";

 // Test serialization
 ObjectWriter writer = objectMapper.writerWithRootValue(new Object());
 String serialized = writer.writeValueAsString(number);
 assertEquals(json, serialized);

 // Test deserialization
 JsonParser parser = objectMapper.getFactory().createParser(json);
 JsonToken token = parser.nextToken();
 assertEquals(JsonToken.START_OBJECT, token);
 Object deserialized = parser.readValueAs(Long.class);
 assertEquals(number, deserialized);
 }

 /**
 * Test for LongSerializer with max value
 */
 @Test
 public void testLongSerializerMaxValue() throws Exception {
 Long number = Long.MAX_VALUE;
 String json = "{\"number\":" + Long.MAX_VALUE + "}";

 // Test serialization
 ObjectWriter writer = objectMapper.writerWithRootValue(new Object());
 String serialized = writer.writeValueAsString(number);
 assertEquals(json, serialized);

 // Test deserialization
 JsonParser parser = objectMapper.getFactory().createParser(json);
 JsonToken token = parser.nextToken();
 assertEquals(JsonToken.START_OBJECT, token);
 Object deserialized = parser.readValueAs(Long.class);
 assertEquals(number, deserialized);
 }

 /**
 * Test for LongSerializer with min value
 */
 @Test
 public void testLongSerializerMinValue() throws Exception {
 Long number = Long.MIN_VALUE;
 String json = "{\"number\":" + Long.MIN_VALUE + "}";

 // Test serialization
 ObjectWriter writer = objectMapper.writerWithRootValue(new Object());
 String serialized = writer.writeValueAsString(number);
 assertEquals(json, serialized);

 // Test deserialization
 JsonParser parser = objectMapper.getFactory().createParser(json);
 JsonToken token = parser.nextToken();
 assertEquals(JsonToken.START_OBJECT, token);
 Object deserialized = parser.readValueAs(Long.class);
 assertEquals(number, deserialized);
 }

 /**
 * Test for FloatSerializer
 */
 @Test
 public void testFloatSerializer() throws Exception {
 Float number = 123.45f;
 String json = "{\"number\":123.45}";

 // Test serialization
 ObjectWriter writer = objectMapper.writerWithRootValue(new Object());
 String serialized = writer.writeValueAsString(number);
 assertEquals(json, serialized);

 // Test deserialization
 JsonParser parser = objectMapper.getFactory().createParser(json);
 JsonToken token = parser.nextToken();
 assertEquals(JsonToken.START_OBJECT, token);
 Object deserialized = parser.readValueAs(Float.class);
 assertEquals(number, deserialized);
 }

 /**
 * Test for FloatSerializer with negative number
 */
 @Test
 public void testFloatSerializerNegative() throws Exception {
 Float number = -123.45f;
 String json = "{\"number\":-123.45}";

 // Test serialization
 ObjectWriter writer = objectMapper.writerWithRootValue(new Object());
 String serialized = writer.writeValueAsString(number);
 assertEquals(json, serialized);

 // Test deserialization
 JsonParser parser = objectMapper.getFactory().createParser(json);
 JsonToken token = parser.nextToken();
 assertEquals(JsonToken.START_OBJECT, token);
 Object deserialized = parser.readValueAs(Float.class);
 assertEquals(number, deserialized);
 }

 /**
 * Test for FloatSerializer with max value
 */
 @Test
 public void testFloatSerializerMaxValue() throws Exception {
 Float number = Float.MAX_VALUE;
 String json = "{\"number\":" + Float.MAX_VALUE + "}";

 // Test serialization
 ObjectWriter writer = objectMapper.writerWithRootValue(new Object());
 String serialized = writer.writeValueAsString(number);
 assertEquals(json, serialized);

 // Test deserialization
 JsonParser parser = objectMapper.getFactory().createParser(json);
 JsonToken token = parser.nextToken();
 assertEquals(JsonToken.START_OBJECT, token);
 Object deserialized = parser.readValueAs(Float.class);
 assertEquals(number, deserialized);
 }

 /**
 * Test for FloatSerializer with min value
 */
 @Test
 public void testFloatSerializerMinValue() throws Exception {
 Float number = Float.MIN_VALUE;
 String json = "{\"number\":" + Float.MIN_VALUE + "}";

 // Test serialization
 ObjectWriter writer = objectMapper.writerWithRootValue(new Object());
 String serialized = writer.writeValueAsString(number);
 assertEquals(json, serialized);

 // Test deserialization
 JsonParser parser = objectMapper.getFactory().createParser(json);
 JsonToken token = parser.nextToken();
 assertEquals(JsonToken.START_OBJECT, token);
 Object deserialized = parser.readValueAs(Float.class);
 assertEquals(number, deserialized);
 }
}