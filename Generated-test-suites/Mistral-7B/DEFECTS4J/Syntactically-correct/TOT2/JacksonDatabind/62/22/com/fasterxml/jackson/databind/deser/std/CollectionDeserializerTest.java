import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionDeserializerTest {

 private static ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testEmptyCollectionDeserialization() throws Exception {
 ObjectReader reader = mapper.readerFor(Collection.class);
 JsonParser parser = mapper.getFactory().createParser("[]");
 Collection<Object> result = reader.readValue(parser);
 assertTrue(result.isEmpty());
 }

 @Test
 public void testSingleElementCollectionDeserialization() throws Exception {
 ObjectReader reader = mapper.readerFor(Collection.class);
 JsonParser parser = mapper.getFactory().createParser("[1]");
 Collection<Object> result = reader.readValue(parser);
 assertEquals(1, result.size());
 assertEquals(1, result.get(0));
 }

 @Test
 public void testMultipleElementsCollectionDeserialization() throws Exception {
 ObjectReader reader = mapper.readerFor(Collection.class);
 JsonParser parser = mapper.getFactory().createParser("[1, 2, 3]");
 Collection<Object> result = reader.readValue(parser);
 assertEquals(3, result.size());
 assertEquals(1, result.get(0));
 assertEquals(2, result.get(1));
 assertEquals(3, result.get(2));
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializationWithInvalidInput() throws Exception {
 ObjectReader reader = mapper.readerFor(Collection.class);
 JsonParser parser = mapper.getFactory().createParser("{ \"key\": \"value\" }");
 reader.readValue(parser);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializationWithNullValue() throws Exception {
 ObjectReader reader = mapper.readerFor(Collection.class);
 JsonParser parser = mapper.getFactory().createParser("[null]");
 reader.readValue(parser);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializationWithEmptyString() throws Exception {
 ObjectReader reader = mapper.readerFor(Collection.class);
 JsonParser parser = mapper.getFactory().createParser("[]");
 parser.nextToken(); // consume empty string
 reader.readValue(parser);
 }

 @Test
 public void testCollectionDeserializationWithWrappedSingleObject() throws Exception {
 ObjectReader reader = mapper.readerFor(Collection.class);
 JsonParser parser = mapper.getFactory().createParser("[{}]");
 Collection<Object> result = reader.readValue(parser);
 assertEquals(1, result.size());
 assertTrue(result.get(0) instanceof Map);
 }

 @Test
 public void testCollectionDeserializationWithValueDeserializer() throws Exception {
 ObjectReader reader = mapper.readerFor(Collection.class);
 JsonParser parser = mapper.getFactory().createParser("[42]");
 reader.setValueDeserializer(new SimpleIntDeserializer());
 Collection<Object> result = reader.readValue(parser);
 assertEquals(1, result.size());
 assertEquals(42, result.get(0));
 }

 @Test
 public void testCollectionDeserializationWithTypeDeserializer() throws Exception {
 ObjectReader reader = mapper.readerFor(Collection.class);
 ObjectReader nestedReader = mapper.readerFor(List.class);
 JsonParser parser = mapper.getFactory().createParser("[1, [2, 3]]");
 reader.setContentDeserializer(nestedReader);
 Collection<Object> result = reader.readValue(parser);
 assertEquals(2, result.size());
 assertTrue(result.get(0) instanceof Collection);
 assertEquals(2, ((Collection) result.get(0)).size());
 assertEquals(1, ((Collection) result.get(0)).get(0));
 assertEquals(2, ((Collection) result.get(0)).get(1));
 assertEquals(3, ((Collection) result.get(1)).get(0));
 assertEquals(3, ((Collection) result.get(1)).get(1));
 }

 @Test
 public void testCollectionDeserializationWithValueDeserializerAndTypeDeserializer() throws Exception {
 ObjectReader reader = mapper.readerFor(Collection.class);
 ObjectReader nestedReader = mapper.readerFor(List.class);
 JsonParser parser = mapper.getFactory().createParser("[1, [2, 3]]");
 reader.setValueDeserializer(new SimpleIntDeserializer());
 reader.setContentDeserializer(nestedReader);
 Collection<Object> result = reader.readValue(parser);
 assertEquals(2, result.size());
 assertTrue(result.get(0) instanceof Collection);
 assertEquals(1, ((Collection) result.get(0)).size());
 assertEquals(1, ((Collection) result.get(0)).get(0).intValue());
 assertTrue(result.get(1) instanceof Collection);
 assertEquals(2, ((Collection) result.get(1)).size());
 assertEquals(2, ((Collection) result.get(1)).get(0).intValue());
 assertEquals(3, ((Collection) result.get(1)).get(1).intValue());
 }

 @Test
 public void testCollectionDeserializationWithDelegateDeserializer() throws Exception {
 ObjectReader reader = mapper.readerFor(Collection.class);
 JsonDeserializer<List<String>> listDeserializer = new ListDeserializerWithString();
 reader.setDelegateDeserializer(listDeserializer);
 ObjectReader nestedReader = mapper.readerFor(String.class);
 JsonParser parser = mapper.getFactory().createParser("[\"one\", \"two\", \"three\"]");
 reader.setContentDeserializer(nestedReader);
 Collection<Object> result = reader.readValue(parser);
 assertEquals(3, result.size());
 assertTrue(result.get(0) instanceof Collection);
 assertEquals(3, ((Collection) result.get(0)).size());
 assertEquals("one", ((Collection) result.get(0)).get(0));
 assertEquals("two", ((Collection) result.get(0)).get(1));
 assertEquals("three", ((Collection) result.get(0)).get(2));
 }

 @Test
 public void testCollectionDeserializationWithValueDeserializerAndDelegateDeserializer() throws Exception {
 ObjectReader reader = mapper.readerFor(Collection.class);
 JsonDeserializer<List<String>> listDeserializer = new ListDeserializerWithString();
 reader.setValueDeserializer(new SimpleIntDeserializer());
 reader.setDelegateDeserializer(listDeserializer);
 ObjectReader nestedReader = mapper.readerFor(String.class);
 JsonParser parser = mapper.getFactory().createParser("[1, \"one\", 2, \"two\", 3, \"three\"]");
 reader.setContentDeserializer(nestedReader);
 Collection<Object> result = reader.readValue(parser);
 assertEquals(6, result.size());
 assertEquals(1, result.get(0).intValue());
 assertTrue(result.get(1) instanceof Collection);
 assertEquals(2, ((Collection) result.get(1)).size());
 assertEquals(1, ((Collection) result.get(1)).get(0));
 assertEquals("one", ((Collection) result.get(1)).get(1));
 assertEquals(2, result.get(2).intValue());
 assertEquals(3, result.get(3).intValue());
 assertTrue(result.get(4) instanceof Collection);
 assertEquals(2, ((Collection) result.get(4)).size());
 assertEquals("two", ((Collection) result.get(4)).get(0));
 assertEquals("three", ((Collection) result.get(4)).get(1));
 }
}