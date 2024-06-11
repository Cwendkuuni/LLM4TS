import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MappingIteratorTest {

 private MappingIterator<Integer> iterator;
 private ObjectMapper mapper;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 try {
 iterator = mapper.readerForListOf(Integer.class).readValue(new ByteArrayInputStream(
 new byte[] { JsonPointer.START_ARRAY.asInt(), JsonToken.VALUE_NUMBER_INT.asInt(1), JsonToken.VALUE_NUMBER_INT.asInt(2), JsonToken.END_ARRAY.asInt() }));
 } catch (IOException e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testHasNext() {
 assertTrue(iterator.hasNext());
 }

 @Test
 public void testHasNext_NoMoreValues() {
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testNext() {
 assertEquals(1, iterator.next().intValue());
 assertEquals(2, iterator.next().intValue());
 assertFalse(iterator.hasNext());
 }

 @Test(expected = NoSuchElementException.class)
 public void testNext_NoMoreValuesException() {
 iterator.next();
 iterator.next();
 iterator.next();
 assertFalse(iterator.hasNext());
 iterator.next();
 }

 @Test(expected = RuntimeJsonMappingException.class)
 public void testNext_JsonMappingException() {
 JsonParser parser = mapper.getFactory().createParser("{\"name\":\"John\", \"age\":\"30\"}");
 MappingIterator<Person> iterator = mapper.readerFor(Person.class).readValues(parser);
 assertFalse(iterator.hasNext());
 Person person = iterator.next();
 fail("Expected JsonMappingException");
 }

 @Test(expected = RuntimeException.class)
 public void testNext_IOException() {
 JsonParser parser = new JsonParser(new ByteArrayInputStream(new byte[] { JsonToken.START_ARRAY.asInt() }));
 MappingIterator<Integer> iterator = mapper.readerForListOf(Integer.class).readValues(parser);
 assertFalse(iterator.hasNext());
 iterator.next();
 try {
 iterator.next();
 } catch (IOException e) {
 fail("Expected IOException");
 }
 }

 @Test
 public void testReadAll() {
 List<Integer> resultList = new ArrayList<>();
 iterator.readAll(resultList);
 assertEquals(2, resultList.size());
 assertEquals(1, resultList.get(0).intValue());
 assertEquals(2, resultList.get(1).intValue());
 }

 @Test
 public void testReadAll_EmptyIterator() {
 List<Integer> resultList = new ArrayList<>();
 iterator = MappingIterator.emptyIterator();
 iterator.readAll(resultList);
 assertTrue(resultList.isEmpty());
 }

 @Test
 public void testReadAll_NullResultList() {
 iterator.readAll((Collection<? super Integer>) null);
 }

 @Test
 public void testReadAll_CustomCollection() {
 List<Integer> resultList = new ArrayList<>();
 MappingIterator<Integer> iterator = mapper.readerForListOf(Integer.class).readValues(new ByteArrayInputStream(
 new byte[] { JsonPointer.START_ARRAY.asInt(), JsonToken.VALUE_NUMBER_INT.asInt(1), JsonToken.VALUE_NUMBER_INT.asInt(2), JsonToken.END_ARRAY.asInt() }));
 iterator.readAll(resultList);
 assertEquals(2, resultList.size());
 assertEquals(1, resultList.get(0).intValue());
 assertEquals(2, resultList.get(1).intValue());
 }

 @Test
 public void testGetParser() {
 JsonParser parser = iterator.getParser();
 assertNotNull(parser);
 }

 @Test
 public void testGetParserSchema() {
 FormatSchema schema = iterator.getParserSchema();
 assertNull(schema);
 }

 @Test
 public void testGetCurrentLocation() {
 JsonLocation location = iterator.getCurrentLocation();
 assertNotNull(location);
 }

}