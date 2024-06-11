import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterators;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.StringWriter;
import java.util.*;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class JoinerTest {

 private Joiner stringJoiner;
 private Joiner charJoiner;
 private Joiner mapJoiner;

 @Before
 public void setup() {
 stringJoiner = Joiner.on(",");
 charJoiner = Joiner.on('-');
 mapJoiner = Joiner.on(":").useForNull("N/A");
 }

 @Test
 public void testAppendToIterable() throws IOException {
 StringWriter stringWriter = new StringWriter();
 String expected = "apple,banana,cherry";
 Joiner joiner = Joiner.on(",");
 joiner.appendTo(stringWriter, Arrays.asList("apple", "banana", "cherry"));
 assertEquals(expected, stringWriter.toString());
 }

 @Test
 public void testAppendToIterator() throws IOException {
 StringWriter stringWriter = new StringWriter();
 Iterator<String> iterator = Arrays.asList("apple", "banana", "cherry").iterator();
 Joiner joiner = Joiner.on(",");
 joiner.appendTo(stringWriter, iterator);
 assertEquals("apple,banana,cherry", stringWriter.toString());
 }

 @Test
 public void testAppendToObjectArray() throws IOException {
 StringWriter stringWriter = new StringWriter();
 Joiner joiner = Joiner.on(",");
 joiner.appendTo(stringWriter, new String[]{"apple", "banana", "cherry"});
 assertEquals("apple,banana,cherry", stringWriter.toString());
 }

 @Test
 public void testAppendToObjectArrayWithNull() throws IOException {
 StringWriter stringWriter = new StringWriter();
 Joiner joiner = Joiner.on(",");
 joiner.appendTo(stringWriter, new String[]{"apple", null, "cherry"});
 assertEquals("apple,N/A,cherry", stringWriter.toString());
 }

 @Test
 public void testAppendToMap() throws IOException {
 Map<String, String> map = new HashMap<>();
 map.put("name", "John");
 map.put("age", "25");
 MapJoiner mapJoiner = Joiner.on(":").useForNull("N/A");
 StringWriter stringWriter = new StringWriter();
 mapJoiner.appendTo(stringWriter, map);
 assertEquals("name:John:age:25", stringWriter.toString());
 }

 @Test
 public void testJoinIterable() {
 String expected = "apple,banana,cherry";
 Joiner joiner = Joiner.on(",");
 assertEquals(expected, joiner.join(Arrays.asList("apple", "banana", "cherry")));
 }

 @Test
 public void testJoinIterator() {
 Iterator<String> iterator = Arrays.asList("apple", "banana", "cherry").iterator();
 String expected = "apple,banana,cherry";
 Joiner joiner = Joiner.on(",");
 assertEquals(expected, joiner.join(iterator));
 }

 @Test
 public void testJoinObjectArray() {
 String[] array = {"apple", "banana", "cherry"};
 String expected = "apple,banana,cherry";
 Joiner joiner = Joiner.on(",");
 assertEquals(expected, joiner.join(array));
 }

 @Test
 public void testJoinObjectArrayWithNull() {
 String[] array = {"apple", null, "cherry"};
 String expected = "apple,N/A,cherry";
 Joiner joiner = Joiner.on(",");
 Joiner joinerWithNull = joiner.useForNull("N/A");
 assertEquals(expected, joinerWithNull.join(array));
 }

 @Test
 public void testUseForNull() {
 Joiner joiner = Joiner.on(",");
 Joiner joinerWithNull = joiner.useForNull("N/A");
 assertNotNull(joinerWithNull);
 assertEquals("N/A", joinerWithNull.toString(null));
 }

 @Test
 public void testSkipNulls() {
 Joiner joiner = Joiner.on(",");
 Joiner joinerSkippingNulls = joiner.skipNulls();
 assertNotNull(joinerSkippingNulls);
 String[] array = {"apple", null, "cherry"};
 assertEquals("apple,cherry", joinerSkippingNulls.join(array));
 }

 @Test
 public void testMapJoiner() {
 Map<String, String> map = new HashMap<>();
 map.put("name", "John");
 map.put("age", "25");
 MapJoiner mapJoiner = Joiner.on(":").useForNull("N/A");
 assertEquals("name:John:age:25", mapJoiner.join(map));
 }

 @Test
 public void testMapJoinerWithIterator() {
 Map<String, String> map = new HashMap<>();
 map.put("name", "John");
 map.put("age", "25");
 MapJoiner mapJoiner = Joiner.on(":").useForNull("N/A");
 Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
 assertEquals("name:John:age:25", mapJoiner.join(iterator));
 }
}