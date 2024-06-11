import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import org.junit.Test;
import static org.junit.Assert.*;

public class ValueInstantiatorTest {

 private ValueInstantiator valueInstantiator;

 @Test
 public void testDefaultInstantiator() throws IOException {
 ValueInstantiator instantiator = new StdValueInstantiator();
 assertNotNull(instantiator.createUsingDefault(mockDeserializationContext()));
 }

 @Test(expected = IOException.class)
 public void testMissingInstantiator() throws IOException {
 valueInstantiator = new ValueInstantiator() {
 @Override
 public Object createUsingDefault(DeserializationContext ctxt) throws IOException {
 throw new IOException("Mock error message");
 }
 };
 String jsonString = "{\"name\": \"John Doe\"}";
 ObjectMapper mapper = new ObjectMapper();
 mapper.readValue(jsonString, valueInstantiator.getValueType());
 }

 @Test
 public void testCreateFromString() throws IOException {
 valueInstantiator = new ValueInstantiator() {
 @Override
 public Object createFromString(DeserializationContext ctxt, String value) throws IOException {
 return value.toUpperCase();
 }
 };
 String jsonString = "{\"name\": \"john doe\"}";
 ObjectMapper mapper = new ObjectMapper();
 assertEquals("JOHN DOE", mapper.readValue(jsonString, valueInstantiator.getValueType()));
 }

 @Test
 public void testCreateFromInt() throws IOException {
 valueInstantiator = new ValueInstantiator() {
 @Override
 public Object createFromInt(DeserializationContext ctxt, int value) throws IOException {
 return value * 2;
 }
 };
 String jsonNumberString = "42";
 ObjectMapper mapper = new ObjectMapper();
 assertEquals(84, mapper.readValue(jsonNumberString, valueInstantiator.getValueType()));
 }

 @Test
 public void testCreateFromLong() throws IOException {
 valueInstantiator = new ValueInstantiator() {
 @Override
 public Object createFromLong(DeserializationContext ctxt, long value) throws IOException {
 return value * 2;
 }
 };
 String jsonLongNumberString = "42";
 ObjectMapper mapper = new ObjectMapper();
 assertEquals(84, mapper.readValue(jsonLongNumberString, valueInstantiator.getValueType()));
 }

 @Test
 public void testCreateFromDouble() throws IOException {
 valueInstantiator = new ValueInstantiator() {
 @Override
 public Object createFromDouble(DeserializationContext ctxt, double value) throws IOException {
 return value * 2;
 }
 };
 String jsonDoubleNumberString = "42.5";
 ObjectMapper mapper = new ObjectMapper();
 assertEquals(85.0, mapper.readValue(jsonDoubleNumberString, valueInstantiator.getValueType()));
 }

 @Test
 public void testCreateFromBoolean() throws IOException {
 valueInstantiator = new ValueInstantiator() {
 @Override
 public Object createFromBoolean(DeserializationContext ctxt, boolean value) throws IOException {
 return !value;
 }
 };
 String jsonBooleanString = "true";
 ObjectMapper mapper = new ObjectMapper();
 assertFalse(mapper.readValue(jsonBooleanString, valueInstantiator.getValueType()));
 }

 @Test
 public void testCreateFromObjectWith() throws IOException {
 valueInstantiator = new ValueInstantiator() {
 @Override
 public Object createFromObjectWith(DeserializationContext ctxt, SettableBeanProperty[] args) throws IOException {
 return new MyClass(args[0].getValue(ctxt.getCurrentValueContext()), args[1].getValue(ctxt.getCurrentValueContext()));
 }
 };
 String jsonObjectString = "{\"name\": \"John Doe\", \"age\": 30}";
 ObjectMapper mapper = new ObjectMapper();
 MyClass myClass = mapper.readValue(jsonObjectString, valueInstantiator.getValueType());
 assertEquals("John Doe", myClass.getName());
 assertEquals(30, myClass.getAge());
 }

 @Test(expected = IOException.class)
 public void testMissingPropertyWhenCreatingFromObjectWith() throws IOException {
 valueInstantiator = new ValueInstantiator() {
 @Override
 public Object createFromObjectWith(DeserializationContext ctxt, SettableBeanProperty[] args) throws IOException {
 return new MyClass(args[0].getValue(ctxt.getCurrentValueContext()), args[1].getValue(ctxt.getCurrentValueContext()));
 }
 };
 String jsonObjectString = "{\"name\": \"John Doe\"}";
 ObjectMapper mapper = new ObjectMapper();
 mapper.readValue(jsonObjectString, valueInstantiator.getValueType());
 }

 @Test
 public void testCanCreateFromString() {
 assertTrue(valueInstantiator.canCreateFromString());
 }

 @Test
 public void testCannotCreateFromString() {
 valueInstantiator = new ValueInstantiator() {
 @Override
 public boolean canCreateFromString() {
 return false;
 }
 };
 assertFalse(valueInstantiator.canCreateFromString());
 }

 @Test
 public void testCanCreateFromInt() {
 assertTrue(valueInstantiator.canCreateFromInt());
 }

 @Test
 public void testCannotCreateFromInt() {
 valueInstantiator = new ValueInstantiator() {
 @Override
 public boolean canCreateFromInt() {
 return false;
 }
 };
 assertFalse(valueInstantiator.canCreateFromInt());
 }

 @Test
 public void testCanCreateFromLong() {
 assertTrue(valueInstantiator.canCreateFromLong());
 }

 @Test
 public void testCannotCreateFromLong() {
 valueInstantiator = new ValueInstantiator() {
 @Override
 public boolean canCreateFromLong() {
 return false;
 }
 };
 assertFalse(valueInstantiator.canCreateFromLong());
 }

 @Test
 public void testCanCreateFromDouble() {
 assertTrue(valueInstantiator.canCreateFromDouble());
 }

 @Test
 public void testCannotCreateFromDouble() {
 valueInstantiator = new ValueInstantiator() {
 @Override
 public boolean canCreateFromDouble() {
 return false;
 }
 };
 assertFalse(valueInstantiator.canCreateFromDouble());
 }

 @Test
 public void testCanCreateFromBoolean() {
 assertTrue(valueInstantiator.canCreateFromBoolean());
 }

 @Test
 public void testCannotCreateFromBoolean() {
 valueInstantiator = new ValueInstantiator() {
 @Override
 public boolean canCreateFromBoolean() {
 return false;
 }
 };
 assertFalse(valueInstantiator.canCreateFromBoolean());
 }

 @Test
 public void testCanCreateUsingDefault() {
 assertTrue(valueInstantiator.canInstantiate());
 }

 @Test
 public void testCannotCreateUsingDefault() {
 valueInstantiator = new ValueInstantiator() {
 @Override
 public boolean canCreateUsingDefault() {
 return false;
 }
 };
 assertFalse(valueInstantiator.canInstantiate());
 }

 @Test
 public void testCanCreateUsingDelegate() {
 valueInstantiator = new ValueInstantiator() {
 @Override
 public boolean canCreateUsingDelegate() {
 return true;
 }
 };
 assertTrue(valueInstantiator.canCreateUsingDelegate());
 }

 @Test
 public void testCannotCreateUsingDelegate() {
 valueInstantiator = new ValueInstantiator() {
 @Override
 public boolean canCreateUsingDelegate() {
 return false;
 }
 };
 assertFalse(valueInstantiator.canCreateUsingDelegate());
 }

 @Test
 public void testCanCreateFromObjectWith() {
 valueInstantiator = new ValueInstantiator() {
 @Override
 public boolean canCreateFromObjectWith() {
 return true;
 }
 };
 assertTrue(valueInstantiator.canCreateFromObjectWith());
 }

 @Test
 public void testCannotCreateFromObjectWith() {
 valueInstantiator = new ValueInstantiator() {
 @Override
 public boolean canCreateFromObjectWith() {
 return false;
 }
 };
 assertFalse(valueInstantiator.canCreateFromObjectWith());
 }

 @Test
 public void testGetValueClass() {
 assertEquals(Object.class, valueInstantiator.getValueClass());
 }

 @Test
 public void testGetValueTypeDesc() {
 valueInstantiator = new ValueInstantiator() {
 @Override
 public String getValueTypeDesc() {
 return "MyCustomClass";
 }
 };
 assertEquals("MyCustomClass", valueInstantiator.getValueTypeDesc());
 }
}