import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionDeserializerTest {

 private final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testDeserializeEmptyArray() throws IOException {
 Object result = mapper.readValue(new JsonFactory(), CollectionDeserializer.class, new TypeReference<Collection<Object>>(){});
 assertNotNull(result);
 assertTrue(result instanceof Collection);
 assertTrue(result.isEmpty());
 }

 @Test
 public void testDeserializeSingleValue() throws IOException {
 Object result = mapper.readValue(new JsonFactory(){{ setFeature(JsonParser.Feature.ALLOW_SINGLE_VALUE_ARRAYS, true); }},
 CollectionDeserializer.class, new Object());
 assertNotNull(result);
 assertTrue(result instanceof Collection);
 assertEquals(1, result.size());
 assertEquals(result.iterator().next(), new Object());
 }

 @Test
 public void testDeserializeArrayWithNullValue() throws IOException {
 Object result = mapper.readValue(new JsonFactory(), CollectionDeserializer.class, "[null]");
 assertNotNull(result);
 assertTrue(result instanceof Collection);
 assertEquals(1, result.size());
 assertNull(((Collection) result).iterator().next());
 }

 @Test
 public void testDeserializeArrayWithObjects() throws IOException {
 Object result = mapper.readValue(new JsonFactory(), CollectionDeserializer.class, "[1, 2, 3]");
 assertNotNull(result);
 assertTrue(result instanceof Collection);
 assertEquals(3, result.size());
 List list = (List) result;
 assertEquals(list.get(0), Integer.valueOf(1));
 assertEquals(list.get(1), Integer.valueOf(2));
 assertEquals(list.get(2), Integer.valueOf(3));
 }

 @Test(expected = IOException.class)
 public void testDeserializeInvalidJson() throws IOException {
 mapper.readValue(new JsonFactory(), CollectionDeserializer.class, "[abcd]");
 fail("Expected IOException");
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeMalformedJsonObject() throws IOException {
 mapper.readValue(new JsonFactory(), CollectionDeserializer.class, "{invalidJson: [1, 2, 3]}");
 fail("Expected JsonMappingException");
 }
}