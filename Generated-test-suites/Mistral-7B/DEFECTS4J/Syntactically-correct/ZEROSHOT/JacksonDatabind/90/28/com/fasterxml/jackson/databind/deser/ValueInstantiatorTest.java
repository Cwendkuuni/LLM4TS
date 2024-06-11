import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class ValueInstantiatorTest {

 private ValueInstantiator valueInstantiator = new ValueInstantiator.Base(String.class);

 @Test
 public void testValueTypeDesc() {
 String expectedValueTypeDesc = "java.lang.String";
 String actualValueTypeDesc = valueInstantiator.getValueTypeDesc();
 assertEquals(expectedValueTypeDesc, actualValueTypeDesc);
 }

 @Test(expected = JsonProcessingException.class)
 public void testCannotCreateUsingDefault() throws Exception {
 Object result = valueInstantiator.createUsingDefault(mockDeserializationContext());
 fail("createUsingDefault should have thrown JsonProcessingException");
 }

 @Test(expected = JsonProcessingException.class)
 public void testCannotCreateFromObjectWithNullArgs() throws Exception {
 Object[] nullArgs = null;
 Object result = valueInstantiator.createFromObjectWith(mockDeserializationContext(), nullArgs);
 fail("createFromObjectWith with null args should have thrown JsonProcessingException");
 }

 @Test(expected = JsonProcessingException.class)
 public void testCannotCreateFromObjectWithEmptyArgs() throws Exception {
 Object[] emptyArgs = new Object[0];
 Object result = valueInstantiator.createFromObjectWith(mockDeserializationContext(), emptyArgs);
 fail("createFromObjectWith with empty args should have thrown JsonProcessingException");
 }

 @Test(expected = JsonProcessingException.class)
 public void testCannotCreateFromObjectWithInvalidArgs() throws Exception {
 SettableBeanProperty[] args = new SettableBeanProperty[] {mockProperty(), mockProperty()};
 Object[] invalidArgs = new Object[] {new Object(), "invalidArg"};
 Object result = valueInstantiator.createFromObjectWith(mockDeserializationContext(), args, invalidArgs);
 fail("createFromObjectWith with invalid args should have thrown JsonProcessingException");
 }

 @Test
 public void testCanCreateFromObjectWithValidArgs() throws Exception {
 String arg1 = "arg1";
 String arg2 = "arg2";
 Object[] validArgs = new Object[] {arg1, arg2};
 SettableBeanProperty[] props = new SettableBeanProperty[] {mockProperty("prop1"), mockProperty("prop2")};
 Object result = valueInstantiator.createFromObjectWith(mockDeserializationContext(), props, validArgs);
 assertNotNull(result);
 assertEquals(arg1, result.getClass().getField("prop1").get(result));
 assertEquals(arg2, result.getClass().getField("prop2").get(result));
 }

 @Test(expected = JsonProcessingException.class)
 public void testCannotCreateUsingDelegate() throws Exception {
 Object result = valueInstantiator.createUsingDelegate(mockDeserializationContext(), mockObject());
 fail("createUsingDelegate should have thrown JsonProcessingException");
 }

 @Test(expected = JsonProcessingException.class)
 public void testCannotCreateUsingString() throws Exception {
 String input = "";
 Object result = valueInstantiator.createFromString(mockDeserializationContext(), input);
 fail("createFromString with empty string should have thrown JsonProcessingException");
 }

 @Test
 public void testCreateFromStringWithValidInput() throws Exception {
 String input = "test";
 Object result = valueInstantiator.createFromString(mockDeserializationContext(), input);
 assertNotNull(result);
 assertEquals(input, result.toString());
 }

 @Test(expected = JsonProcessingException.class)
 public void testCannotCreateUsingInt() throws Exception {
 int input = 42;
 Object result = valueInstantiator.createFromInt(mockDeserializationContext(), input);
 fail("createFromInt should have thrown JsonProcessingException");
 }

 @Test
 public void testCreateFromIntWithValidInput() throws Exception {
 int input = 42;
 Object result = valueInstantiator = new ValueInstantiator.Base(Integer.class).createFromInt(mockDeserializationContext(), input);
 assertEquals(input, result.intValue());
 }

 @Test(expected = JsonProcessingException.class)
 public void testCannotCreateUsingLong() throws Exception {
 long input = 42L;
 Object result = valueInstantiator.createFromLong(mockDeserializationContext(), input);
 fail("createFromLong should have thrown JsonProcessingException");
 }

 @Test
 public void testCreateFromLongWithValidInput() throws Exception {
 long input = 42L;
 Object result = valueInstantiator = new ValueInstantiator.Base(Long.class).createFromLong(mockDeserializationContext(), input);
 assertEquals(input, result.longValue());
 }

 @Test(expected = JsonProcessingException.class)
 public void testCannotCreateUsingDouble() throws Exception {
 double input = 42.0;
 Object result = valueInstantiator.createFromDouble(mockDeserializationContext(), input);
 fail("createFromDouble should have thrown JsonProcessingException");
 }

 @Test
 public void testCreateFromDoubleWithValidInput() throws Exception {
 double input = 42.0;
 Object result = valueInstantiator = new ValueInstantiator.Base(Double.class).createFromDouble(mockDeserializationContext(), input);
 assertEquals(input, result.doubleValue());
 }

 @Test(expected = JsonProcessingException.class)
 public void testCannotCreateUsingBoolean() throws Exception {
 boolean input = true;
 Object result = valueInstantiator.createFromBoolean(mockDeserializationContext(), input);
 fail("createFromBoolean should have thrown JsonProcessingException");
 }

 @Test
 public void testCreateFromBooleanWithValidInput() throws Exception {
 boolean input = true;
 Object result = valueInstantiator = new ValueInstantiator.Base(Boolean.class).createFromBoolean(mockDeserializationContext(), input);
 assertEquals(input, result.booleanValue());
 }

 /**
 * Helper method to create a mock {@link DeserializationContext} object.
 */
 private DeserializationContext mockDeserializationContext() {
 ObjectMapper mapper = new ObjectMapper();
 DeserializationConfig config = mapper.getDeserializationConfig();
 return mapper.getDeserializationContext();
 }

 /**
 * Helper method to create a mock {@link PropertyName} object.
 */
 private PropertyName mockPropertyName() {
 return new PropertyName("propName");
 }

 /**
 * Helper method to create a mock {@link SettableBeanProperty} object.
 */
 private SettableBeanProperty mockProperty() {
 return new AnnotatedStandardGetterProperty(AnnotatedClass.construct(String.class, null), mockPropertyName());
 }

 /**
 * Helper method to create a mock {@link Object} object.
 */
 private Object mockObject() {
 return new Object();
 }
}