import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionDeserializerTest {

 private ObjectMapper mapper;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 }

 @Test
 public void testEmptyArray() throws IOException {
 JsonParser p = mapper.getFactory().createParser("[]");
 CollectionDeserializer deserializer = mapper.findValueDeserializer(Collection.class);
 Collection<Object> result = deserializer.deserialize(p, mapper.getDeserializationContext());
 assertTrue(result.isEmpty());
 }

 @Test
 public void testSingleElementArray() throws IOException {
 JsonParser p = mapper.getFactory().createParser("[\n" +
 "  {\"name\": \"John\", \"age\": 30}\n" +
 "]");
 CollectionDeserializer deserializer = mapper.findValueDeserializer(Collection.class);
 Collection<Object> result = deserializer.deserialize(p, mapper.getDeserializationContext());
 assertEquals(1, result.size());
 Object element = result.iterator().next();
 Map<String, Object> expectedElement = new HashMap<>();
 expectedElement.put("name", "John");
 expectedElement.put("age", 30);
 assertEquals(expectedElement, element);
 }

 @Test
 public void testNullArray() throws IOException {
 JsonParser p = mapper.getFactory().createParser("[]");
 CollectionDeserializer deserializer = mapper.findValueDeserializer(Collection.class);
 Collection<Object> result = deserializer.deserialize(p, mapper.getDeserializationContext());
 assertNull(result);
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidJson() throws IOException {
 JsonParser p = mapper.getFactory().createParser("{\"name\": \"John\"}");
 CollectionDeserializer deserializer = mapper.findValueDeserializer(Collection.class);
 deserializer.deserialize(p, mapper.getDeserializationContext());
 fail("Expected JsonMappingException");
 }

 @Test(expected = JsonMappingException.class)
 public void testUnexpectedToken() throws IOException {
 JsonParser p = mapper.getFactory().createParser("[\n" +
 "  {\"name\": \"John\", \"age\": 30,\"city\": \"New York\"}\n" +
 ",\n" +
 "  \"Hello World\"\n" +
 "]");
 CollectionDeserializer deserializer = mapper.findValueDeserializer(Collection.class);
 deserializer.deserialize(p, mapper.getDeserializationContext());
 fail("Expected JsonMappingException");
 }

 @Test
 public void testArrayOfNonObjectTypes() throws IOException {
 JsonParser p = mapper.getFactory().createParser("[\n" +
 "  1,\n" +
 "  3.14,\n" +
 "  true,\n" +
 "  \"Hello World\"\n" +
 "]");
 CollectionDeserializer deserializer = mapper.findValueDeserializer(Collection.class);
 Collection<Object> result = deserializer.deserialize(p, mapper.getDeserializationContext());
 assertEquals(4, result.size());
 assertTrue(result.contains(1));
 assertTrue(result.contains(3.14));
 assertTrue(result.contains(true));
 assertTrue(result.contains("Hello World"));
 }

 @Test
 public void testArrayOfCustomClass() throws IOException {
 class CustomClass {
 String name;
 int age;

 public CustomClass() {}
 }

 JsonParser p = mapper.getFactory().createParser("[\n" +
 "  {\"name\": \"John\", \"age\": 30},\n" +
 "  {\"name\": \"Mary\", \"age\": 25},\n" +
 "  {\"name\": \"Bob\", \"age\": 42}\n" +
 "]");
 CollectionDeserializer deserializer = mapper.findValueDeserializer(Collection.class);
 Collection<Object> result = deserializer.deserialize(p, mapper.getDeserializationContext());
 assertEquals(3, result.size());
 for (Object obj : result) {
 CustomClass element = (CustomClass) obj;
 assertEquals("name", "John", element.name);
 assertEquals("age", 30, element.age);
 // or any other suitable assertions for the "Mary" and "Bob" elements
 }
 }
}