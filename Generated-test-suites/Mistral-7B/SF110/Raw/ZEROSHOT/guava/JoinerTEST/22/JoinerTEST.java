import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import static com.google.common.base.Joiner.on;

public class JoinerTest {

 @Test
 public void testAppendToIterable() throws IOException {
 String separator = ",";
 Joiner joiner = on(separator);
 List<String> list = Arrays.asList("apple", "banana", "cherry");
 StringBuilder expected = new StringBuilder();
 expected.append("apple,");
 expected.append("banana,");
 expected.append("cherry");
 StringBuilder actual = new StringBuilder();
 joiner.appendTo(actual, list.iterator());
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testAppendToArray() throws IOException {
 String separator = "--";
 Joiner joiner = on(separator);
 String[] array = {"apple", "banana", "cherry"};
 StringBuilder expected = new StringBuilder();
 expected.append("apple--");
 expected.append("banana--");
 expected.append("cherry");
 StringBuilder actual = new StringBuilder();
 joiner.appendTo(actual, array);
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testAppendToObject() throws IOException {
 String separator = " | ";
 Joiner joiner = on(separator);
 Object[] objects = {"apple", null, "cherry"};
 StringBuilder expected = new StringBuilder();
 expected.append("apple | ");
 expected.append(joiner.useForNull("NULL")).append(" | ");
 expected.append("cherry");
 StringBuilder actual = new StringBuilder();
 joiner.appendTo(actual, objects);
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testAppendToMapEntrySet() throws IOException {
 String separator = ": ";
 Joiner joiner = on(separator);
 Map<String, String> map = new HashMap<>();
 map.put("key1", "value1");
 map.put("key2", "value2");
 MapJoiner mapJoiner = joiner.withKeyValueSeparator(" = ");
 StringBuilder expected = new StringBuilder();
 expected.append("key1 = value1: ");
 expected.append("key2 = value2");
 StringBuilder actual = new StringBuilder();
 mapJoiner.appendTo(actual, map.entrySet());
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testJoinIterable() {
 String separator = "_";
 Joiner joiner = on(separator);
 List<String> list = Arrays.asList("apple", "banana", "cherry");
 String expected = "apple_banana_cherry";
 String actual = joiner.join(list);
 assertEquals(expected, actual);
 }

 @Test
 public void testJoinArray() {
 String separator = "-";
 Joiner joiner = on(separator);
 String[] array = {"apple", "banana", "cherry"};
 String expected = "apple-banana-cherry";
 String actual = joiner.join(array);
 assertEquals(expected, actual);
 }

 @Test
 public void testJoinObjectArray() {
 String separator = " | ";
 Joiner joiner = on(separator);
 Object[] objects = {"apple", null, "cherry"};
 String expected = "apple | NULL | cherry";
 String actual = joiner.join(objects);
 assertEquals(expected, actual);
 }

 @Test
 public void testJoinMapEntrySet() {
 String separator = ": ";
 Joiner joiner = on(separator);
 Map<String, String> map = new HashMap<>();
 map.put("key1", "value1");
 map.put("key2", "value2");
 MapJoiner mapJoiner = joiner.withKeyValueSeparator(" = ");
 String expected = "key1 = value1: key2 = value2";
 String actual = joiner.join(map.entrySet());
 assertEquals(expected, actual);
 }

 @Test
 public void testJoinEmptyIterable() {
 String separator = "|";
 Joiner joiner = on(separator);
 List<String> list = Collections.emptyList();
 String expected = "";
 String actual = joiner.join(list);
 assertEquals(expected, actual);
 }

 @Test (expected = IllegalArgumentException.class)
 public void testJoinNullIterable() {
 String separator = "|";
 Joiner joiner = on(separator);List<String> list = null;
 String actual = joiner.join(list);
 }

 @Test (expected = IllegalArgumentException.class)
 public void testJoinNullObjectArray() {
 String separator = "|";
 Joiner joiner = on(separator);
 Object[] objects = null;
 String actual = joiner.join(objects);
 }

 @Test (expected = IllegalArgumentException.class)
 public void testJoinNullMap() {
 String separator = "|";
 Joiner joiner = on(separator);
 Map<String, String> map = null;
 String actual = joiner.join(map.entrySet());
 }
}