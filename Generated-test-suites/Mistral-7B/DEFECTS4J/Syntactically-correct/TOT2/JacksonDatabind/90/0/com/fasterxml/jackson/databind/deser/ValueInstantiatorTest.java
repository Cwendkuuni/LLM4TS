import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import org.junit.Test;
import static org.junit.Assert.*;

public class ValueInstantiatorTest {

 private static class TestValueInstantiator extends ValueInstantiator {
 @Override
 public boolean canCreateFromString() {
 return true;
 }

 @Override
 public Object createFromString(DeserializationContext ctxt, String value) throws IOException {
 return value;
 }
 }

 @Test
 public void testCanInstantiate() {
 ValueInstantiator valueInst = new TestValueInstantiator();
 assertTrue(valueInst.canInstantiate());
 }

 @Test(expected = AssertionError.class)
 public void testCreateFromNull() throws IOException {
 ValueInstantiator valueInst = new TestValueInstantiator();
 JsonParser parser = new JsonParser();
 Object result = valueInst.createUsingDefault(parser);
 assertNotNull(result);
 try {
 Object nullValue = null;
 result = valueInst.createFromObjectWith(parser, new Object[] { nullValue });
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test(expected = JsonMappingException.class)
 public void testCreateFromEmptyString() throws IOException {
 ValueInstantiator valueInst = new TestValueInstantiator();
 JsonParser parser = new JsonParser();
 String emptyString = "";
 try {
 Object result = valueInst.createFromObjectWith(parser, new Object[] { emptyString });
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 }
 }

 @Test
 public void testCreateFromString() throws IOException {
 ValueInstantiator valueInst = new TestValueInstantiator();
 JsonParser parser = new JsonParser();
 String value = "test";
 Object result = valueInst.createFromObjectWith(parser, new Object[] { value });
 assertEquals(value, result);
 }

 @Test
 public void testCreateFromInt() throws IOException {
 ValueInstantiator valueInst = new TestValueInstantiator();
 JsonParser parser = new JsonParser();
 int value = 123;
 Object result = valueInst.createFromInt(parser, value);
 assertEquals(Integer.valueOf(value), result);
 }

 @Test(expected = JsonMappingException.class)
 public void testCreateFromNegativeInt() throws IOException {
 ValueInstantiator valueInst = new TestValueInstantiator();
 JsonParser parser = new JsonParser();
 int value = -123;
 Object result = valueInst.createFromInt(parser, value);
 fail("Expected JsonMappingException");
 }

 @Test
 public void testCreateFromLong() throws IOException {
 ValueInstantiator valueInst = new TestValueInstantiator();
 JsonParser parser = new JsonParser();
 long value = 123L;
 Object result = valueInst.createFromLong(parser, value);
 assertEquals(Long.valueOf(value), result);
 }

 @Test(expected = JsonMappingException.class)
 public void testCreateFromNegativeLong() throws IOException {
 ValueInstantiator valueInst = new TestValueInstantiator();
 JsonParser parser = new JsonParser();
 long value = -123L;
 Object result = valueInst.createFromLong(parser, value);
 fail("Expected JsonMappingException");
 }

 @Test
 public void testCreateFromDouble() throws IOException {
 ValueInstantiator valueInst = new TestValueInstantiator();
 JsonParser parser = new JsonParser();
 double value = 123.45;
 Object result = valueInst.createFromDouble(parser, value);
 assertEquals(Double.valueOf(value), result);
 }

 @Test(expected = JsonMappingException.class)
 public void testCreateFromNegativeDouble() throws IOException {
 ValueInstantiator valueInst = new TestValueInstantiator();
 JsonParser parser = new JsonParser();
 double value = -123.45;
 Object result = valueInst.createFromDouble(parser, value);
 fail("Expected JsonMappingException");
 }

 @Test
 public void testCreateFromBoolean() throws IOException {
 ValueInstantiator valueInst = new TestValueInstantiator();
 JsonParser parser = new JsonParser();
 boolean value = true;
 Object result = valueInst.createFromBoolean(parser, value);
 assertEquals(Boolean.valueOf(value), result);
 }

 @Test
 public void testCreateFromFalseBoolean() throws IOException {
 ValueInstantiator valueInst = new TestValueInstantiator();
 JsonParser parser = new JsonParser();
 boolean value = false;
 Object result = valueInst.createFromBoolean(parser, value);
 assertEquals(Boolean.FALSE, result);
 }

 @Test(expected = JsonMappingException.class)
 public void testCreateFromInvalidBoolean() throws IOException {
 ValueInstantiator valueInst = new TestValueInstantiator();
 JsonParser parser = new JsonParser();
 String value = "invalid";
 Object result = valueInst.createFromString(parser, value);
 fail("Expected JsonMappingException");
 }

 @Test
 public void testCreateFromObjectWith() throws IOException {
 ValueInstantiator valueInst = new TestValueInstantiator();
 JsonParser parser = new JsonParser();
 String value = "test";
 Object[] args = new Object[] { value };
 Object result = valueInst.createFromObjectWith(parser, args);
 assertEquals(value, result);
 }

 @Test(expected = JsonMappingException.class)
 public void testCreateFromObjectWithNull() throws IOException {
 ValueInstantiator valueInst = new TestValueInstantiator();
 JsonParser parser = new JsonParser();
 Object[] args = new Object[] { null };
 Object result = valueInst.createFromObjectWith(parser, args);
 fail("Expected JsonMappingException");
 }

 @Test(expected = JsonMappingException.class)
 public void testCreateFromObjectWithInvalidType() throws IOException {
 ValueInstantiator valueInst = new TestValueInstantiator();
 JsonParser parser = new JsonParser();
 Object[] args = new Object[] { 123 };
 try {
 Object result = valueInst.createFromObjectWith(parser, args);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 }
 }

 @Test
 public void testCreateUsingDefault() throws IOException {
 ValueInstantiator valueInst = new TestValueInstantiator();
 JsonParser parser = new JsonParser();
 Object result = valueInst.createUsingDefault(parser);
 assertNotNull(result);
 }

 @Test(expected = JsonMappingException.class)
 public void testCreateUsingDefaultWithInvalidType() throws IOException {
 ValueInstantiator valueInst = new TestValueInstantiator();
 JsonParser parser = new JsonParser();
 Object result = valueInst.createUsingDefault(parser);
 fail("Expected JsonMappingException");
 }

}