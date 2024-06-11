import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Test;

import static org.junit.Assert.*;

public class NumberSerializersTest {

 private ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testIntegerSerializer() throws Exception {
 Integer number = 123;
 String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(number);
 JsonParser parser = mapper.getFactory().createParser(json);
 int deserializedNumber = parser.readValueAs(int.class);
 assertEquals(number, deserializedNumber);
 }

 @Test
 public void testIntegerSerializerWithNegativeNumber() throws Exception {
 Integer number = -123;
 String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(number);
 JsonParser parser = mapper.getFactory().createParser(json);
 int deserializedNumber = parser.readValueAs(int.class);
 assertEquals(number, deserializedNumber);
 }

 @Test
 public void testIntegerSerializerWithMaxValue() throws Exception {
 Integer number = Integer.MAX_VALUE;
 String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(number);
 JsonParser parser = mapper.getFactory().createParser(json);
 int deserializedNumber = parser.readValueAs(int.class);
 assertEquals(number, deserializedNumber);
 }

 @Test(expected = IOException.class)
 public void testIntegerSerializerWithString() throws Exception {
 String json = "\"abc\"";
 JsonParser parser = mapper.getFactory().createParser(json);
 parser.readValueAs(int.class);
 }

 @Test
 public void testIntegerSerializerWithNaN() throws Exception {
 double nan = Double.NaN;
 String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(nan);
 JsonParser parser = mapper.getFactory().createParser(json);
 assertFalse(parser.readValueAs(int.class) instanceof Number);
 }

 @Test
 public void testLongSerializer() throws Exception {
 Long number = 123L;
 String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(number);
 JsonParser parser = mapper.getFactory().createParser(json);
 long deserializedNumber = parser.readValueAs(long.class);
 assertEquals(number, deserializedNumber);
 }

 @Test
 public void testLongSerializerWithNegativeNumber() throws Exception {
 Long number = -123L;
 String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(number);
 JsonParser parser = mapper.getFactory().createParser(json);
 long deserializedNumber = parser.readValueAs(long.class);
 assertEquals(number, deserializedNumber);
 }

 @Test
 public void testLongSerializerWithMaxValue() throws Exception {
 Long number = Long.MAX_VALUE;
 String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(number);
 JsonParser parser = mapper.getFactory().createParser(json);
 long deserializedNumber = parser.readValueAs(long.class);
 assertEquals(number, deserializedNumber);
 }

 @Test(expected = IOException.class)
 public void testLongSerializerWithString() throws Exception {
 String json = "\"abc\"";
 JsonParser parser = mapper.getFactory().createParser(json);
 parser.readValueAs(long.class);
 }

 @Test
 public void testLongSerializerWithNaN() throws Exception {
 double nan = Double.NaN;
 String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(nan);
 JsonParser parser = mapper.getFactory().createParser(json);
 assertFalse(parser.readValueAs(long.class) instanceof Number);
 }

 @Test
 public void testShortSerializer() throws Exception {
 Short number = (short) 123;
 String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(number);
 JsonParser parser = mapper.getFactory().createParser(json);
 short deserializedNumber = parser.readValueAs(short.class);
 assertEquals(number, deserializedNumber);
 }

 @Test
 public void testShortSerializerWithNegativeNumber() throws Exception {
 Short number = (short) -123;
 String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(number);
 JsonParser parser = mapper.getFactory().createParser(json);
 short deserializedNumber = parser.readValueAs(short.class);
 assertEquals(number, deserializedNumber);
 }

 @Test
 public void testShortSerializerWithMaxValue() throws Exception {
 Short number = Short.MAX_VALUE;
 String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(number);
 JsonParser parser = mapper.getFactory().createParser(json);
 short deserializedNumber = parser.readValueAs(short.class);
 assertEquals(number, deserializedNumber);
 }

 @Test(expected = IOException.class)
 public void testShortSerializerWithString() throws Exception {
 String json = "\"abc\"";
 JsonParser parser = mapper.getFactory().createParser(json);
 parser.readValueAs(short.class);
 }

 @Test
 public void testShortSerializerWithNaN() throws Exception {
 double nan = Double.NaN;
 String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(nan);
 JsonParser parser = mapper.getFactory().createParser(json);
 assertFalse(parser.readValueAs(short.class) instanceof Number);
 }

 @Test
 public void testFloatSerializer() throws Exception {
 Float number = 123.45f;
 String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(number);
 JsonParser parser = mapper.getFactory().createParser(json);
 float deserializedNumber = parser.readValueAs(float.class);
 assertEquals(number, deserializedNumber, 0.001f);
 }

 @Test
 public void testFloatSerializerWithNaN() throws Exception {
 Float number = Float.NaN;
 String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(number);
 JsonParser parser = mapper.getFactory().createParser(json);
 assertEquals(Float.floatToIntBits(number.floatValue()), parser.readValueAsInt());
 }

 @Test(expected = IOException.class)
 public void testFloatSerializerWithString() throws Exception {
 String json = "\"abc\"";
 JsonParser parser = mapper.getFactory().createParser(json);
 parser.readValueAs(float.class);
 }

 @Test
 public void testFloatSerializerWithMaxValue() throws Exception {
 Float number = Float.MAX_VALUE;
 String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(number);
 JsonParser parser = mapper.getFactory().createParser(json);
 float deserializedNumber = parser.readValueAs(float.class);
 assertEquals(number, deserializedNumber, 0.001f);
 }

 @Test
 public void testDoubleSerializer() throws Exception {
 Double number = 123.45;
 String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(number);
 JsonParser parser = mapper.getFactory().createParser(json);
 double deserializedNumber = parser.readValueAs(double.class);
 assertEquals(number, deserializedNumber, 0.001);
 }

 @Test
 public void testDoubleSerializerWithNaN() throws Exception {
 Double number = Double.NaN;
 String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(number);
 JsonParser parser = mapper.getFactory().createParser(json);
 assertEquals(Double.doubleToLongBits(number.doubleValue()), parser.readValueAsLong());
 }

 @Test(expected = IOException.class)
 public void testDoubleSerializerWithString() throws Exception {
 String json = "\"abc\"";
 JsonParser parser = mapper.getFactory().createParser(json);
 parser.readValueAs(double.class);
 }

 @Test
 public void testDoubleSerializerWithMaxValue() throws Exception {
 Double number = Double.MAX_VALUE;
 String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(number);
 JsonParser parser = mapper.getFactory().createParser(json);
 double deserializedNumber = parser.readValueAs(double.class);
 assertEquals(number, deserializedNumber, 0.001);
 }
}