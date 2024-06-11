import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

import org.junit.Test;
import static org.junit.Assert.*;

public class ValueInstantiatorTest {

 private ObjectMapper mapper;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 mapper = new ObjectMapper();
 }

 @Test
 public void testValueInstantiator_getDefaultCreator() throws Exception {
 ValueInstantiator vi = new ValueInstantiator.Base(String.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 Object result = vi.createUsingDefault(ctxt);
 assertNotNull(result);
 assertTrue(result instanceof String);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testValueInstantiator_createUsingDefault_MissingDefaultCreator() throws Exception {
 ValueInstantiator vi = new ValueInstantiator.Base(Integer.class) {
 @Override
 public AnnotatedWithParams getDefaultCreator() {
 return null;
 }
 };
 DeserializationContext ctxt = mapper.getDeserializationContext();
 vi.createUsingDefault(ctxt);
 }

 @Test
 public void testValueInstantiator_createFromString() throws Exception {
 ValueInstantiator vi = new ValueInstantiator.Base(String.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 String value = "hello";
 Object result = vi.createFromString(ctxt, value);
 assertNotNull(result);
 assertEquals(value, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testValueInstantiator_createFromString_MissingStringCreator() throws Exception {
 ValueInstantiator vi = new ValueInstantiator.Base(Integer.class) {
 @Override
 public boolean canCreateFromString() {
 return false;
 }
 };
 DeserializationContext ctxt = mapper.getDeserializationContext();
 String value = "hello";
 vi.createFromString(ctxt, value);
 }

 @Test
 public void testValueInstantiator_createFromInt() throws Exception {
 ValueInstantiator vi = new ValueInstantiator.Base(Integer.class) {
 @Override
 public boolean canCreateFromInt() {
 return true;
 }
 };
 DeserializationContext ctxt = mapper.getDeserializationContext();
 Object result = vi.createFromInt(ctxt, 10);
 assertNotNull(result);
 assertEquals(10, result.hashCode());
 assertEquals(Integer.class, result.getClass());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testValueInstantiator_createFromInt_MissingIntCreator() throws Exception {
 ValueInstantiator vi = new ValueInstantiator.Base(String.class) {
 @Override
 public boolean canCreateFromInt() {
 return false;
 }
 };
 DeserializationContext ctxt = mapper.getDeserializationContext();
 Object result = vi.createFromInt(ctxt, 10);
 }

 @Test
 public void testValueInstantiator_createFromObjectWith() throws Exception {
 TypeDeserializer td = mapper.getTypeDeserializerForProperty("name", String.class);
 ValueInstantiator vi = new ValueInstantiator.Base(Person.class) {
 @SuppressWarnings("unchecked")
 @Override
 public SettableBeanProperty[] getFromObjectArguments(DeserializationConfig config) {
 return new SettableBeanProperty[] { config.findBeanProperty("name", String.class) };
 }
 };
 DeserializationContext ctxt = mapper.getDeserializationContext();
 Object input = mapper.readValue("[{\"name\":\"John\"}]", Person[].class);
 Person result = (Person) vi.createFromObjectWith(ctxt, input);
 assertNotNull(result);
 assertEquals("John", result.getName());
 }

 @Test
 public void testValueInstantiator_createFromObjectWith_MissingPropertyCreator() throws Exception {
 ValueInstantiator vi = new ValueInstantiator.Base(Person.class) {
 @Override
 public SettableBeanProperty[] getFromObjectArguments(DeserializationConfig config) {
 return new SettableBeanProperty[0];
 }
 };
 DeserializationContext ctxt = mapper.getDeserializationContext();
 Object input = mapper.readValue("[{\"name\":\"John\"}]", Person[].class);
 Person result = (Person) vi.createFromObjectWith(ctxt, input);
 assertNull(result);
 }

 @Test
 public void testValueInstantiator_canInstantiate() throws Exception {
 ValueInstantiator vi = new ValueInstantiator.Base(String.class);
 assertTrue(vi.canInstantiate());
 }

 @Test
 public void testValueInstantiator_canInstantiate_NoCreators() throws Exception {
 ValueInstantiator vi = new ValueInstantiator.Base(Person.class) {
 @Override
 public boolean canCreateUsingDefault() {
 return false;
 }
 @Override
 public boolean canCreateUsingDelegate() {
 return false;
 }
 @Override
 public boolean canCreateFromObjectWith() {
 return false;
 }
 @Override
 public boolean canCreateFromString() {
 return false;
 }
 @Override
 public boolean canCreateFromInt() {
 return false;
 }
 @Override
 public boolean canCreateFromLong() {
 return false;
 }
 @Override
 public boolean canCreateFromDouble() {
 return false;
 }
 @Override
 public boolean canCreateFromBoolean() {
 return false;
 }
 };
 assertFalse(vi.canInstantiate());
 }

 @Test(expected = JsonProcessingException.class)
 public void testValueInstantiator_createUsingDelegate() throws Exception {
 ValueInstantiator vi = new ValueInstantiator.Base(Person.class) {
 @Override
 public AnnotatedWithParams getDelegateCreator() {
 return mapper.getDeserializationContext().findValueDeserializer(Person.class);
 }
 };
 DeserializationContext ctxt = mapper.getDeserializationContext();
 vi.createUsingDelegate(ctxt, null);
 }

 @Test
 public void testValueInstantiator_createUsingDelegate_MissingDelegateCreator() throws Exception {
 ValueInstantiator vi = new ValueInstantiator.Base(Person.class) {
 @Override
 public boolean canCreateUsingDelegate() {
 return true;
 }
 @Override
 public AnnotatedWithParams getDelegateCreator() {
 return null;
 }
 };
 DeserializationContext ctxt = mapper.getDeserializationContext();
 vi.createUsingDelegate(ctxt, null);
 }

 @Test
 public void testValueInstantiator_createFromObjectWith_ArrayDelegate() throws Exception {
 ValueInstantiator arrayDelegate = new ValueInstantiator.Base(Person[].class) {
 @Override
 public boolean canCreateUsingArrayDelegate() {
 return true;
 }
 @Override
 public JavaType getArrayDelegateType(DeserializationConfig config) {
 return mapper.getTypeFactory().constructArrayType(Person.class);
 }
 };
 ValueInstantiator vi = new ValueInstantiator.Base(Person.class) {
 @Override
 public AnnotatedWithParams getDelegateCreator() {
 return arrayDelegate;
 }
 };
 DeserializationContext ctxt = mapper.getDeserializationContext();
 Object input = mapper.readValue("[{\"name\":\"John\"}]", Person[].class);
 Person result = (Person) vi.createUsingDelegate(ctxt, input);
 assertNotNull(result);
 assertEquals("John", result.getName());
 }

 @Test
 public void testValueInstantiator_createUsingDelegate_MissingArrayDelegate() throws Exception {
 ValueInstantiator vi = new ValueInstantiator.Base(Person.class) {
 @Override
 public boolean canCreateUsingDelegate() {
 return true;
 }
 @Override
 public AnnotatedWithParams getDelegateCreator() {
 return null;
 }
 };
 DeserializationContext ctxt = mapper.getDeserializationContext();
 Object input = mapper.readValue("[{\"name\":\"John\"}]", Person[].class);
 vi.createUsingDelegate(ctxt, input);
 }

 @Test
 public void testValueInstantiator_createUsingDelegate_NullDelegate() throws Exception {
 ValueInstantiator vi = new ValueInstantiator.Base(Person.class) {
 @Override
 public boolean canCreateUsingDelegate() {
 return true;
 }
 @Override
 public AnnotatedWithParams getDelegateCreator() {
 return null;
 }
 };
 DeserializationContext ctxt = mapper.getDeserializationContext();
 vi.createUsingDelegate(ctxt, null);
 }

}

class Person {
 private String name;

 public String getName() {
 return name;
 }

 public void setName(String name) {
 this.name = name;
 }
}