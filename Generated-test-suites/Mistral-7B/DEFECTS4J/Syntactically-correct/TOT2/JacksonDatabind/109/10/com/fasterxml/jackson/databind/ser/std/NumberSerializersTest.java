import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializersTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 }

 // Test for addAll method
 @Test
 public void testAddAll() {
 Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
 NumberSerializers.addAll(allDeserializers);

 assertEquals(6, allDeserializers.size());
 }

 // Test for IntegerSerializer
 @Test
 public void testIntegerSerializer() throws JsonProcessingException {
 Integer number = 123;
 String json = objectMapper.writeValueAsString(number);
 JsonParser parser = objectMapper.getFactory().createParser(json);

 assertEquals(number.intValue(), parser.readValueAs(Integer.class));
 }

 // Test for IntegerSerializer with negative number
 @Test
 public void testIntegerSerializerNegativeNumber() throws JsonProcessingException {
 Integer number = -123;
 String json = objectMapper.writeValueAsString(number);
 JsonParser parser = objectMapper.getFactory().createParser(json);

 assertEquals(number.intValue(), parser.readValueAs(Integer.class));
 }

 // Test for IntegerSerializer with minimum and maximum integer value
 @Test
 public void testIntegerSerializerMinMaxValue() throws JsonProcessingException {
 Integer minValue = Integer.MIN_VALUE;
 Integer maxValue = Integer.MAX_VALUE;
 String minJson = objectMapper.writeValueAsString(minValue);
 String maxJson = objectMapper.writeValueAsString(maxValue);
 JsonParser minParser = objectMapper.getFactory().createParser(minJson);
 JsonParser maxParser = objectMapper.getFactory().createParser(maxJson);

 assertEquals(minValue.intValue(), minParser.readValueAs(Integer.class));
 assertEquals(maxValue.intValue(), maxParser.readValueAs(Integer.class));
 }

 // Test for LongSerializer
 @Test
 public void testLongSerializer() throws JsonProcessingException {
 Long number = 123L;
 String json = objectMapper.writeValueAsString(number);
 JsonParser parser = objectMapper.getFactory().createParser(json);

 assertEquals(number.longValue(), parser.readValueAs(Long.class));
 }

 // Test for LongSerializer with negative number
 @Test
 public void testLongSerializerNegativeNumber() throws JsonProcessingException {
 Long number = -123L;
 String json = objectMapper.writeValueAsString(number);
 JsonParser parser = objectMapper.getFactory().createParser(json);

 assertEquals(number.longValue(), parser.readValueAs(Long.class));
 }

 // Test for LongSerializer with minimum and maximum long value
 @Test
 public void testLongSerializerMinMaxValue() throws JsonProcessingException {
 Long minValue = Long.MIN_VALUE;
 Long maxValue = Long.MAX_VALUE;
 String minJson = objectMapper.writeValueAsString(minValue);
 String maxJson = objectMapper.writeValueAsString(maxValue);
 JsonParser minParser = objectMapper.getFactory().createParser(minJson);
 JsonParser maxParser = objectMapper.getFactory().createParser(maxJson);

 assertEquals(minValue.longValue(), minParser.readValueAs(Long.class));
 assertEquals(maxValue.longValue(), maxParser.readValueAs(Long.class));
 }

 // Test for ShortSerializer
 @Test
 public void testShortSerializer() throws JsonProcessingException {
 Short number = (short) 123;
 String json = objectMapper.writeValueAsString(number);
 JsonParser parser = objectMapper.getFactory().createParser(json);

 assertEquals(number.shortValue(), parser.readValueAs(Short.class));
 }

 // Test for ShortSerializer with negative number
 @Test
 public void testShortSerializerNegativeNumber() throws JsonProcessingException {
 Short number = (short) -123;
 String json = objectMapper.writeValueAsString(number);
 JsonParser parser = objectMapper.getFactory().createParser(json);

 assertEquals(number.shortValue(), parser.readValueAs(Short.class));
 }

 // Test for ShortSerializer with minimum and maximum short value
 @Test
 public void testShortSerializerMinMaxValue() throws JsonProcessingException {
 Short minValue = Short.MIN_VALUE;
 Short maxValue = Short.MAX_VALUE;
 String minJson = objectMapper.writeValueAsString(minValue);
 String maxJson = objectMapper.writeValueAsString(maxValue);
 JsonParser minParser = objectMapper.getFactory().createParser(minJson);
 JsonParser maxParser = objectMapper.getFactory().createParser(maxJson);

 assertEquals(minValue.shortValue(), minParser.readValueAs(Short.class));
 assertEquals(maxValue.shortValue(), maxParser.readValueAs(Short.class));
 }

 // Test for FloatSerializer
 @Test
 public void testFloatSerializer() throws JsonProcessingException {
 Float number = 123.45f;
 String json = objectMapper.writeValueAsString(number);
 JsonParser parser = objectMapper.getFactory().createParser(json);

 assertEquals(number.floatValue(), parser.readValueAs(Float.class));
 }

 // Test for FloatSerializer with minimum and maximum float value
 @Test
 public void testFloatSerializerMinMaxValue() throws JsonProcessingException {
 Float minValue = Float.MIN_VALUE;
 Float maxValue = Float.MAX_VALUE;
 String minJson = objectMapper.writeValueAsString(minValue);
 String maxJson = objectMapper.writeValueAsString(maxValue);
 JsonParser minParser = objectMapper.getFactory().createParser(minJson);
 JsonParser maxParser = objectMapper.getFactory().createParser(maxJson);

 assertEquals(minValue.floatValue(), minParser.readValueAs(Float.class));
 assertEquals(maxValue.floatValue(), maxParser.readValueAs(Float.class));
 }

 // Test for DoubleSerializer
 @Test
 public void testDoubleSerializer() throws JsonProcessingException {
 Double number = 123.45;
 String json = objectMapper.writeValueAsString(number);
 JsonParser parser = objectMapper.getFactory().createParser(json);

 assertEquals(number.doubleValue(), parser.readValueAs(Double.class));
 }

 // Test for DoubleSerializer with minimum and maximum double value
 @Test
 public void testDoubleSerializerMinMaxValue() throws JsonProcessingException {
 Double minValue = Double.MIN_VALUE;
 Double maxValue = Double.MAX_VALUE;
 String minJson = objectMapper.writeValueAsString(minValue);
 String maxJson = objectMapper.writeValueAsString(maxValue);
 JsonParser minParser = objectMapper.getFactory().createParser(minJson);
 JsonParser maxParser = objectMapper.getFactory().createParser(maxJson);

 assertEquals(minValue.doubleValue(), minParser.readValueAs(Double.class));
 assertEquals(maxValue.doubleValue(), maxParser.readValueAs(Double.class));
 }
}