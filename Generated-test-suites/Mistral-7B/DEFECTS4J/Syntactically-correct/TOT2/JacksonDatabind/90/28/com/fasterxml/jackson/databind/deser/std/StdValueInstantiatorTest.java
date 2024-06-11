import com.fasterxml.jackson.core.base.JsonMappingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class StdValueInstantiatorTest {

 private ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testCreateUsingDefault() throws IOException {
 StdValueInstantiator instantiator = mapper.findValueInstantiator(String.class);
 String result = instantiator.createUsingDefault(mapper.getDeserializationContext());
 assertNotNull(result);
 assertEquals("", result);
 }

 @Test
 public void testCreateFromObjectWith() throws IOException {
 Object[] args = {new Object(), new Object()};
 StdValueInstantiator instantiator = mapper.findValueInstantiator(Object[].class);
 instantiator.configureFromObjectSettings(null, null, null, null, null, args);
 Object result = instantiator.createFromObjectWith(mapper.getDeserializationContext(), args);
 assertNotNull(result);
 assertEquals(2, result.length);
 }

 @Test
 public void testCreateUsingDelegate() throws IOException {
 StdValueInstantiator instantiator = mapper.findValueInstantiator(String.class);
 instantiator.configureDelegate("com.fasterxml.jackson.databind.deser.std.TestDelegate");
 Object result = instantiator.createUsingDelegate(mapper.getDeserializationContext(), new TestDelegate());
 assertNotNull(result);
 assertEquals("Delegate result", result.toString());
 }

 @Test(expected = JsonMappingException.class)
 public void testCreateUsingDelegateException() throws IOException {
 StdValueInstantiator instantiator = mapper.findValueInstantiator(String.class);
 instantiator.configureDelegate("com.fasterxml.jackson.databind.deser.std.TestDelegateWithException");
 instantiator.createUsingDelegate(mapper.getDeserializationContext(), new TestDelegateWithException());
 }

 @Test
 public void testCreateFromString() throws IOException {
 StdValueInstantiator instantiator = mapper.findValueInstantiator(String.class);
 Object result = instantiator.createFromString(mapper.getDeserializationContext(), "");
 assertNotNull(result);
 assertEquals("", result.toString());
 }

 @Test(expected = JsonMappingException.class)
 public void testCreateFromStringEmpty() throws IOException {
 StdValueInstantiator instantiator = mapper.findValueInstantiator(String.class);
 Object result = instantiator.createFromString(mapper.getDeserializationContext(), null);
 }

 @Test
 public void testCreateFromInt() throws IOException {
 StdValueInstantiator instantiator = mapper.findValueInstantiator(Integer.class);
 int value = 42;
 Object result = instantiator.createFromInt(mapper.getDeserializationContext(), value);
 assertNotNull(result);
 assertEquals(value, result.intValue());
 }

 @Test
 public void testCreateFromLong() throws IOException {
 StdValueInstantiator instantiator = mapper.findValueInstantiator(Long.class);
 long value = 42L;
 Object result = instantiator.createFromLong(mapper.getDeserializationContext(), value);
 assertNotNull(result);
 assertEquals(value, result.longValue());
 }

 @Test
 public void testCreateFromDouble() throws IOException {
 StdValueInstantiator instantiator = mapper.findValueInstantiator(Double.class);
 double value = 42.3;
 Object result = instantiator.createFromDouble(mapper.getDeserializationContext(), value);
 assertNotNull(result);
 assertEquals(value, result.doubleValue(), 0.0);
 }

 @Test
 public void testCreateFromBoolean() throws IOException {
 StdValueInstantiator instantiator = mapper.findValueInstantiator(Boolean.class);
 boolean value = true;
 Object result = instantiator.createFromBoolean(mapper.getDeserializationContext(), value);
 assertNotNull(result);
 assertEquals(value, result.booleanValue());
 }

 @Test
 public void testCreateUsingArrayDelegate() throws IOException {
 StdValueInstantiator instantiator = mapper.findValueInstantiator(int[].class);
 instantiator.configureFromArraySettings(null, null, null);
 Object[] result = instantiator.createUsingArrayDelegate(mapper.getDeserializationContext(), new int[0]);
 assertNotNull(result);
 assertEquals(0, result.length);
 }

 @Test
 public void testCreateFromStringFallback() throws IOException {
 StdValueInstantiator instantiator = mapper.findValueInstantiator(String.class);
 instantiator.configureFromStringCreator(null);
 Object result = instantiator.createFromString(mapper.getDeserializationContext(), "test");
 assertNotNull(result);
 assertEquals("test", result.toString());
 }
}