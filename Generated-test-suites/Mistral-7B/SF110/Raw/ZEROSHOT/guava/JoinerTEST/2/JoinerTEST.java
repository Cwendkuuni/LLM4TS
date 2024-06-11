import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;
import com.google.common.base.*;

public class JoinerTest {

 private Joiner joiner;
 private Joiner joinerWithNull;
 private Joiner joinerWithSkipNulls;
 private Joiner mapJoiner;

 @Before
 public void setup() {
 joiner = Joiner.on(",");
 joinerWithNull = joiner.useForNull("N/A");
 joinerWithSkipNulls = joiner.skipNulls();
 mapJoiner = joiner.withKeyValueSeparator(":");
 }

 @Test
 public void testJoinerOnString() throws IOException {
 String[] parts = {"apple", "banana", "cherry"};
 StringBuilder expected = new StringBuilder();
 expected.append("apple").append(",").append("banana").append(",").append("cherry");
 StringBuilder actual = new StringBuilder();
 joiner.appendTo(actual, parts);
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testJoinerOnChar() throws IOException {
 char separator = ',';
 Joiner joinerWithSeparator = Joiner.on(separator);
 String[] parts = {"apple", "banana", "cherry"};
 StringBuilder expected = new StringBuilder();
 expected.append("apple").append(separator).append("banana").append(separator).append("cherry");
 StringBuilder actual = new StringBuilder();
 joinerWithSeparator.appendTo(actual, parts);
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testJoinerAppendToIterable() throws IOException {
 Iterable<String> parts = Arrays.asList("apple", "banana", "cherry");
 StringBuilder expected = new StringBuilder();
 expected.append("apple").append(",").append("banana").append(",").append("cherry");
 StringBuilder actual = new StringBuilder();
 joiner.appendTo(actual, parts);
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testJoinerAppendToObjectArray() throws IOException {
 Object[] parts = {"apple", "banana", "cherry"};
 StringBuilder expected = new StringBuilder();
 expected.append("apple").append(",").append("banana").append(",").append("cherry");
 StringBuilder actual = new StringBuilder();
 joiner.appendTo(actual, parts);
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testJoinerUseForNull() throws IOException {
 Object[] parts = {"apple", null, "cherry"};
 StringBuilder expected = new StringBuilder();
 expected.append("apple").append(",").append("N/A").append(",").append("cherry");
 StringBuilder actual = new StringBuilder();
 joinerWithNull.appendTo(actual, parts);
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testJoinerSkipNulls() throws IOException {
 Object[] parts = {"apple", null, "cherry", "orange"};
 StringBuilder expected = new StringBuilder();
 expected.append("apple").append(",").append("cherry").append(",").append("orange");
 StringBuilder actual = new StringBuilder();
 joinerWithSkipNulls.appendTo(actual, parts);
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testJoinerMapJoiner() {
 Map<String, String> map = new HashMap<>();
 map.put("Apple", "Fruit");
 map.put("Banana", "Fruit");
 map.put("Cherry", "Fruit");
 StringBuilder expected = new StringBuilder();
 expected.append("Apple:Fruit").append(mapJoiner.separator).append("Banana:Fruit").append(mapJoiner.separator).append("Cherry:Fruit");
 StringBuilder actual = new StringBuilder();
 mapJoiner.appendTo(actual, map.entrySet());
 assertEquals(expected.toString(), actual.toString());
 }

}