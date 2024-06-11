import static org.junit.Assert.*;
import com.google.common.base.Joiner;
import com.google.common.base.MapJoiner;
import java.util.*;

public class JoinerTest {

 @SuppressWarnings("unchecked")
 @org.junit.BeforeClass
 public static void setup() {
 Joiner commaJoiner = Joiner.on(",");
 Joiner dotJoiner = Joiner.on(".");
 }

 @org.junit.Test
 public void testAppendToIterable() throws IOException {
 Iterable<String> iterable = Arrays.asList("apple", "banana", "cherry");
 StringBuilder expected = new StringBuilder();
 expected.append("apple");
 expected.append(Joiner.on(",").separator);
 expected.append("banana");
 expected.append(Joiner.on(",").separator);
 expected.append("cherry");

 StringBuilder result = new StringBuilder();
 Joiner joiner = Joiner.on(",");
 joiner.appendTo(result, iterable);
 assertEquals(expected.toString(), result.toString());
 }

 @org.junit.Test
 public void testAppendToObject() throws IOException {
 Object[] array = new Object[]{1, 2, 3};
 StringBuilder expected = new StringBuilder();
 expected.append("1");
 expected.append(Joiner.on(",").separator);
 expected.append("2");
 expected.append(Joiner.on(",").separator);
 expected.append("3");

 StringBuilder result = new StringBuilder();
 Joiner joiner = Joiner.on(",");
 joiner.appendTo(result, array);
 assertEquals(expected.toString(), result.toString());
 }

 @org.junit.Test
 public void testAppendToStringBuilder() {
 StringBuilder expected = new StringBuilder();
 expected.append("apple");
 expected.append(Joiner.on(",").separator);
 expected.append("banana");
 expected.append(Joiner.on(",").separator);
 expected.append("cherry");

 StringBuilder result = new StringBuilder();
 Joiner joiner = Joiner.on(",");
 joiner.appendTo(result, Arrays.asList("apple", "banana", "cherry"));
 assertEquals(expected.toString(), result.toString());
 }

 @org.junit.Test
 public void testJoinIterable() {
 Iterable<String> iterable = Arrays.asList("apple", "banana", "cherry");
 String expected = "apple,banana,cherry";
 String result = Joiner.on(",").join(iterable);
 assertEquals(expected, result);
 }

 @org.junit.Test
 public void testJoinObject() {
 Object[] array = new Object[]{1, 2, 3};
 String expected = "1,2,3";
 String result = Joiner.on(",").join(array);
 assertEquals(expected, result);
 }

 @org.junit.Test
 public void testJoinStringBuilder() {
 StringBuilder stringBuilder = new StringBuilder();
 stringBuilder.append("apple");
 stringBuilder.append(Joiner.on(",").separator);
 stringBuilder.append("banana");
 stringBuilder.append(Joiner.on(",").separator);
 stringBuilder.append("cherry");
 String expected = "apple,banana,cherry";
 String result = Joiner.on(",").join(stringBuilder.toString().split(Joiner.on(",").separator()));
 assertEquals(expected, result);
 }

 @org.junit.Test
 public void testAppendToMap() throws IOException {
 Map<String, String> map = new HashMap<>();
 map.put("name", "John");
 map.put("age", "25");
 map.put("city", "New York");

 StringBuilder expected = new StringBuilder();
 expected.append("name=");
 expected.append(Joiner.on("=").separator);
 expected.append("John");
 expected.append(Joiner.on("=").separator);
 expected.append("age=");
 expected.append(Joiner.on("=").separator);
 expected.append("25");
 expected.append(Joiner.on("=").separator);
 expected.append("city=");
 expected.append(Joiner.on("=").separator);
 expected.append("New York");

 StringBuilder result = new StringBuilder();
 MapJoiner mapJoiner = Joiner.on("=").withKeyValueSeparator("=");
 mapJoiner.appendTo(result, map);
 assertEquals(expected.toString(), result.toString());
 }

 @org.junit.Test
 public void testJoinMap() {
 Map<String, String> map = new HashMap<>();
 map.put("name", "John");
 map.put("age", "25");
 map.put("city", "New York");
 String expected = "name=John,age=25,city=New York";
 String result = MapJoiner.on("=").withKeyValueSeparator("=").join(map);
 assertEquals(expected, result);
 }
}