import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MappingIteratorTest {

 private MappingIterator<String> mappingIterator;
 private ObjectMapper objectMapper;

 @Before
 public void setUp() throws IOException {
 objectMapper = new ObjectMapper();
 String json = "[\"apple\",\"banana\",\"orange\"]";
 mappingIterator = objectMapper.readValue(new StringReader(json),
 MappingIterator.class);
 }

 @Test
 public void testHasNext() {
 assertTrue(mappingIterator.hasNext());
 assertFalse(mappingIterator.hasNext());
 }

 @Test
 public void testNext() {
 assertEquals("apple", mappingIterator.next());
 assertEquals("banana", mappingIterator.next());
 assertEquals("orange", mappingIterator.next());
 assertFalse(mappingIterator.hasNext());
 }

 @Test(expected = NoSuchElementException.class)
 public void testNextWithNoMoreElements() {
 mappingIterator.next();
 mappingIterator.next();
 mappingIterator.next();
 mappingIterator.next();
 }

 @Test
 public void testClose() throws IOException {
 mappingIterator.close();
 assertFalse(mappingIterator.hasNext());
 }

 @Test
 public void testReadAll() throws IOException {
 List<String> list = mappingIterator.readAll();
 assertEquals(3, list.size());
 assertEquals("apple", list.get(0));
 assertEquals("banana", list.get(1));
 assertEquals("orange", list.get(2));
 }

 @Test
 public void testReadAllWithArrayList() throws IOException {
 ArrayList<String> list = new ArrayList<>();
 mappingIterator.readAll(list);
 assertEquals(3, list.size());
 assertEquals("apple", list.get(0));
 assertEquals("banana", list.get(1));
 assertEquals("orange", list.get(2));
 }

 @Test
 public void testReadAllWithLinkedList() throws IOException {
 LinkedList<String> list = new LinkedList<>();
 mappingIterator.readAll(list);
 assertEquals(3, list.size());
 assertEquals("apple", list.getFirst());
 assertEquals("banana", list.get(1));
 assertEquals("orange", list.getLast());
 }
}