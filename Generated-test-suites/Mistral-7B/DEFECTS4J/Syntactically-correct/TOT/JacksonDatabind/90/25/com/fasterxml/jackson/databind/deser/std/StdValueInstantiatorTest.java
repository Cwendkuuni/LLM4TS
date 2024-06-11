import com.fasterxml.jackson.core.base.JsonMappingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdValueInstantiatorTest {

 private StdValueInstantiator valueInstantiator;
 private ObjectMapper objectMapper;

 @Before
 public void setup() throws Exception {
 objectMapper = new ObjectMapper();
 valueInstantiator = objectMapper.findValueInstantiator(Object.class);
 }

 @Test
 public void testCreateUsingDefault() throws Exception {
 Object result = valueInstantiator.createUsingDefault(objectMapper.getDeserializationContext());
 assertNotNull(result);
 assertEquals("default value", result.toString());
 }

 @Test
 public void testCreateFromObjectWith() throws Exception {
 ObjectNode node = objectMapper.readValue("{\"name\":\"John\", \"age\":30}", ObjectNode.class);
 Object result = valueInstantiator.createFromObjectWith(objectMapper.getDeserializationContext(), node.toStdValueArray());
 assertTrue(result instanceof Person);
 assertEquals("John", ((Person) result).getName());
 assertEquals(30, ((Person) result).getAge());
 }

 @Test
 public void testCreateUsingDelegate() throws Exception {
 JavaType delegateType = objectMapper.getTypeFactory().constructType(Person.class);
 AnnotatedWithParams delegateCreator = valueInstantiator.getDefaultCreator();
 SettableBeanProperty[] delegateArguments = new SettableBeanProperty[]{new BeanProperty.Std("name"), new BeanProperty.Std("age")};
 valueInstantiator.configureFromDelegateSettings(delegateCreator, delegateArguments);
 Object result = valueInstantiator.createUsingDelegate(objectMapper.getDeserializationContext(), new Person());
 assertTrue(result instanceof Person);
 assertEquals("default name", result.getClass().getField("name").get(result));
 assertEquals(0, result.getClass().getField("age").get(result));
 }

 @Test(expected = JsonMappingException.class)
 public void testCreateUsingInvalidDelegate() throws Exception {
 JavaType delegateType = objectMapper.getTypeFactory().constructType(Person.class);
 AnnotatedWithParams delegateCreator = valueInstantiator.getDefaultCreator();
 SettableBeanProperty[] delegateArguments = new SettableBeanProperty[]{new BeanProperty.Std("name"), new BeanProperty.Std("age")};
 valueInstantiator.configureFromDelegateSettings(delegateCreator, delegateArguments);
 valueInstantiator.createUsingDelegate(objectMapper.getDeserializationContext(), new Object());
 }

 @Test
 public void testCreateFromString() throws Exception {
 Object result = valueInstantiator.createFromString(objectMapper.getDeserializationContext(), "42");
 assertEquals(42, result.intValue());
 }

 @Test
 public void testCreateFromInt() throws Exception {
 Object result = valueInstantiator.createFromInt(objectMapper.getDeserializationContext(), 42);
 assertEquals(42, result.intValue());
 }

 @Test
 public void testCreateFromLong() throws Exception {
 Object result = valueInstantiator.createFromLong(objectMapper.getDeserializationContext(), 42L);
 assertEquals(42, result.longValue());
 }

 @Test
 public void testCreateFromDouble() throws Exception {
 Object result = valueInstantiator.createFromDouble(objectMapper.getDeserializationContext(), 42.0d);
 assertEquals(42.0, result.doubleValue(), 0.0);
 }

 @Test
 public void testCreateFromBoolean() throws Exception {
 Object result = valueInstantiator.createFromBoolean(objectMapper.getDeserializationContext(), true);
 assertEquals(true, result.booleanValue());
 }

 @Test
 public void testCreateFromIncompleteParameter() throws Exception {
 AnnotatedParameter incompleteParameter = valueInstantiator.getDefaultCreator().getParameter(0);
 valueInstantiator.configureIncompleteParameter(incompleteParameter);
 Object result = valueInstantiator.createUsingDefault(objectMapper.getDeserializationContext());
 assertNotNull(result);
 assertEquals("default value", result.toString());
 }
}

class Person {
 private String name;
 private int age;

 public Person() {
 }

 public Person(String name, int age) {
 this.name = name;
 this.age = age;
 }

 public String getName() {
 return name;
 }

 public int getAge() {
 return age;
 }
}