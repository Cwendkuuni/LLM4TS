import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MappingIteratorTest {

 private ObjectMapper objectMapper;
 private JsonParser jsonParser;
 private MappingIterator<String> mappingIterator;

 @Before
 public void setup() throws IOException {
 objectMapper = new ObjectMapper();
 jsonParser = objectMapper.getFactory().createParser("[\"apple\",\"banana\",\"orange\"]");
 mappingIterator = new MappingIterator<String>(objectMapper.getTypeFactory().constructType(List.class), jsonParser, null, null, true, null);
 }

 @Test
 public void testHasNextAndNextWhenIteratingOverValidSequence() {
 assertTrue(mappingIterator.hasNext());
 assertEquals("apple", mappingIterator.next());
 assertTrue(mappingIterator.hasNext());
 assertEquals("banana", mappingIterator.next());
 assertTrue(mappingIterator.hasNext());
 assertEquals("orange", mappingIterator.next());
 assertFalse(mappingIterator.hasNext());
 }

 @Test
 public void testCloseParserWhenManagedParserIsPassed() throws IOException {
 mappingIterator = new MappingIterator<String>(objectMapper.getTypeFactory().constructType(List.class), jsonParser, null, null, true, null);
 mappingIterator.next();
 mappingIterator.next();
 mappingIterator.close();
 assertFalse(jsonParser.hasNextToken());
 }

 @Test(expected = NoSuchElementException.class)
 public void testNoSuchElementExceptionWhenIteratingOverEmptySequence() {
 mappingIterator = MappingIterator.emptyIterator();
 mappingIterator.next();
 }

 @Test(expected = IOException.class)
 public void testIOExceptionWhenInputStreamIsClosed() throws IOException {
 mappingIterator = new MappingIterator<String>(objectMapper.getTypeFactory().constructType(List.class), jsonParser, null, null, true, null);
 mappingIterator.next();
 mappingIterator.next();
 jsonParser.close();
 mappingIterator.next();
 }

 @Test(expected = JsonMappingException.class)
 public void testJsonMappingExceptionWhenDeserializationFails() throws IOException {
 jsonParser = objectMapper.getFactory().createParser("{\"invalid\":[1,2,3]}");
 mappingIterator = new MappingIterator<String>(objectMapper.getTypeFactory().constructType(List.class), jsonParser, null, null, true, null);
 mappingIterator.next();
 mappingIterator.next();
 mappingIterator.next();
 mappingIterator.next();
 JsonDeserializer<String> deserializer = new JsonDeserializer<String>() {
 @Override
 public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 throw new JsonMappingException("Deserialization failed");
 }
 };
 mappingIterator.setDeserializer(deserializer);
 mappingIterator.next();
 }

 @Test(expected = JsonMappingException.class)
 public void testJsonMappingExceptionWhenDeserializationFailsWithNullValue() throws IOException {
 jsonParser = objectMapper.getFactory().createParser("null");
 mappingIterator = new MappingIterator<String>(objectMapper.getTypeFactory().constructType(List.class), jsonParser, null, null, true, null);
 mappingIterator.next();
 mappingIterator.next();
 mappingIterator.next();
 mappingIterator.next();
 mappingIterator.next();
 JsonDeserializer<String> deserializer = new JsonDeserializer<String>() {
 @Override
 public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 throw new JsonMappingException("Deserialization failed");
 }
 };
 mappingIterator.setDeserializer(deserializer);
 mappingIterator.next();
 }

 @Test(expected = JsonMappingException.class)
 public void testJsonMappingExceptionWhenDeserializationFailsWithNullDeserializer() {
 mappingIterator = new MappingIterator<String>(objectMapper.getTypeFactory().constructType(List.class), jsonParser, null, null, true, null);
 mappingIterator.setDeserializer(null);
 mappingIterator.next();
 mappingIterator.next();
 mappingIterator.next();
 mappingIterator.next();
 mappingIterator.next();
 }

 @Test(expected = JsonMappingException.class)
 public void testJsonMappingExceptionWhenDeserializationFailsWithNullContext() {
 mappingIterator = new MappingIterator<String>(objectMapper.getTypeFactory().constructType(List.class), jsonParser, null, null, true, null);MappingIterator<String> mappingIterator = new MappingIterator<String>(objectMapper.getTypeFactory().constructType(List.class), jsonParser, null, null, true, null);
 mappingIterator.setDeserializationContext(null);
 mappingIterator.next();
 mappingIterator.next();
 mappingIterator.next();
 mappingIterator.next();
 mappingIterator.next();
 }

 @Test(expected = IllegalStateException.class)
 public void testIllegalStateExceptionWhenCallingHasNextOrNextAfterClosingParser() throws IOException {
 mappingIterator = new MappingIterator<String>(objectMapper.getTypeFactory().constructType(List.class), jsonParser, null, null, true, null);
 mappingIterator.next();
 mappingIterator.next();
 mappingIterator.close();
 assertFalse(mappingIterator.hasNext());
 try {
 mappingIterator.next();
 } catch (IllegalStateException e) {
 assertEquals("Iterator closed", e.getMessage());
 }
 }

}