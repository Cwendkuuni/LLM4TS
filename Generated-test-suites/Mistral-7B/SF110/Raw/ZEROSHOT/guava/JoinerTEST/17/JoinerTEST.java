import com.google.common.base.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class JoinerTest {

 @Test
 public void testJoinerOnString() {
 Joiner joiner = Joiner.on(",");
 StringBuilder expected = new StringBuilder();
 expected.append("apple");
 expected.append(joiner.separator);
 expected.append("banana");
 expected.append(joiner.separator);
 expected.append("orange");

 StringBuilder result = new StringBuilder();
 try {
 joiner.appendTo(result, Arrays.asList("apple", "banana", "orange"));
 } catch (IOException e) {
 throw new RuntimeException(e);
 }
 assertEquals(expected.toString(), result.toString());
 }

 @Test
 public void testJoinerOnChar() {
 Joiner joiner = Joiner.on('/');
 StringBuilder expected = new StringBuilder();
 expected.append("apple");
 expected.append(joiner.separator);
 expected.append("banana");
 expected.append(joiner.separator);
 expected.append("orange");

 StringBuilder result = new StringBuilder();
 try {
 joiner.appendTo(result, Arrays.asList("apple", "banana", "orange"));
 } catch (IOException e) {
 throw new RuntimeException(e);
 }
 assertEquals(expected.toString(), result.toString());
 }

 @Test
 public void testAppendToObjectArray() {
 Joiner joiner = Joiner.on(",");
 StringBuilder expected = new StringBuilder();
 expected.append("apple");
 expected.append(joiner.separator);
 expected.append("banana");
 expected.append(joiner.separator);
 expected.append("orange");

 StringBuilder result = new StringBuilder();
 try {
 joiner.appendTo(result, new Object[]{"apple", "banana", "orange"});
 } catch (IOException e) {
 throw new RuntimeException(e);
 }
 assertEquals(expected.toString(), result.toString());
 }

 @Test
 public void testAppendToObjectArrayWithNullFirst() {
 Joiner joiner = Joiner.on(",");
 StringBuilder expected = new StringBuilder();
 expected.append("apple");
 expected.append(joiner.separator);
 expected.append("banana");
 expected.append(joiner.separator);
 expected.append("orange");

 StringBuilder result = new StringBuilder();
 Joiner joinerWithNull = joiner.useForNull("NULL");
 try {
 joinerWithNull.appendTo(result, new Object[]{"null", "apple", "banana", "orange"});
 } catch (IOException e) {
 throw new RuntimeException(e);
 }
 assertEquals(expected.toString(), result.toString());
 }

 @Test
 public void testAppendToIterable() {
 Joiner joiner = Joiner.on(",");
 StringBuilder expected = new StringBuilder();
 expected.append("apple");
 expected.append(joiner.separator);
 expected.append("banana");
 expected.append(joiner.separator);
 expected.append("orange");

 StringBuilder result = new StringBuilder();
 try {
 joiner.appendTo(result, Arrays.asList("apple", "banana", "orange").iterator());
 } catch (IOException e) {
 throw new RuntimeException(e);
 }
 assertEquals(expected.toString(), result.toString());
 }

 @Test
 public void testJoin() {
 Joiner joiner = Joiner.on(",");
 String expected = "apple,banana,orange";
 String result = joiner.join(Arrays.asList("apple", "banana", "orange"));
 assertEquals(expected, result);
 }

 @Test
 public void testJoinIterable() {
 Joiner joiner = Joiner.on(",");
 Iterator<Object> iterator = Arrays.asList("apple", "banana", "orange").iterator();
 StringBuilder expected = new StringBuilder();
 expected.append("apple");
 expected.append(joiner.separator);
 expected.append("banana");
 expected.append(joiner.separator);
 expected.append("orange");

 StringBuilder result = new StringBuilder();
 try {
 joiner.appendTo(result, iterator);
 } catch (IOException e) {
 throw new RuntimeException(e);
 }
 assertEquals(expected.toString(), result.toString());
 String joined = joiner.join(iterator);
 assertEquals(expected.toString(), joined);
 }

 @Test
 public void testJoinObjectArray() {
 Joiner joiner = Joiner.on(",");
 String expected = "apple,banana,orange";
 String result = joiner.join(new Object[]{"apple", "banana", "orange"});
 assertEquals(expected, result);
 }

 @Test
 public void testJoinObjectArrayWithNullFirst() {
 Joiner joiner = Joiner.on(",");
 String expected = "apple,banana,orange";
 String result = joiner.useForNull("NULL").join(new Object[]{"null", "apple", "banana", "orange"});
 assertEquals(expected, result);
 }

 @Test
 public void testJoinMap() {
 Joiner joiner = Joiner.on(" => ");
 Map<String, String> map = new HashMap<>();
 map.put("key1", "value1");
 map.put("key2", "value2");
 map.put("key3", "value3");
 String expected = "key1 => value1 " + joiner.separator + "key2 => value2 " + joiner.separator + "key3 => value3";
 String result = joiner.join(map.entrySet());
 assertEquals(expected, result);
 }

 @Test
 public void testJoinMapWithUseForNull() {
 Joiner joiner = Joiner.on(" => ");
 Map<String, String> map = new HashMap<>();
 map.put("key1", "value1");
 map.put(null, "valueNull");
 map.put("key3", "value3");
 String expected = "key1 => value1 " + joiner.separator + "null => valueNull " + joiner.separator + "key3 => value3";
 String result = joiner.useForNull("NULL").join(map.entrySet());
 assertEquals(expected, result);
 }
}