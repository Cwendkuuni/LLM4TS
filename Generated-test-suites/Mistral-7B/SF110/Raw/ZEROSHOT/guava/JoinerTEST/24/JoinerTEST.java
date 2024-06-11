import com.google.common.base.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class JoinerTest {

 @Test
 public void testJoinerOnString() {
 Joiner joiner = Joiner.on(",");
 StringBuilder expected = new StringBuilder();
 expected.append("apple").append(joiner.separator).append("banana").append(joiner.separator).append("orange");
 StringBuilder actual = new StringBuilder();
 joiner.appendTo(actual, Arrays.asList("apple", "banana", "orange"));
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testJoinerOnCharacter() {
 Joiner joiner = Joiner.on(';');
 StringBuilder expected = new StringBuilder();
 expected.append("apple").append(joiner.separator).append("banana").append(joiner.separator).append("orange");
 StringBuilder actual = new StringBuilder();
 joiner.appendTo(actual, Arrays.asList("apple", "banana", "orange"));
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testJoinerAppendToIterable() throws IOException {
 StringJoiner joiner = Joiner.on(",");
 StringWriter writer = new StringWriter();
 joiner.appendTo(writer, Arrays.asList("apple", "banana", "orange"));
 assertEquals("apple,banana,orange", writer.toString());
 }

 @Test
 public void testJoinerAppendToObjectArray() throws IOException {
 StringJoiner joiner = Joiner.on(",");
 StringWriter writer = new StringWriter();
 joiner.appendTo(writer, new Object[]{"apple", "banana", "orange"});
 assertEquals("apple,banana,orange", writer.toString());
 }

 @Test
 public void testJoinerAppendToNulls() throws IOException {
 StringJoiner joiner = Joiner.on(",");
 StringWriter writer = new StringWriter();
 joiner.appendTo(writer, Arrays.asList(null, "banana", null, "orange"));
 assertEquals("banana,null,orange", writer.toString());
 }

 @Test
 public void testJoinerUseForNull() {
 Joiner joiner = Joiner.on(",").useForNull("NA");
 StringBuilder expected = new StringBuilder();
 expected.append("apple").append(joiner.separator).append("NA").append(joiner.separator).append("NA").append(joiner.separator).append("orange");
 StringBuilder actual = new StringBuilder();
 joiner.appendTo(actual, Arrays.asList(new Object[]{"apple", null, null, "orange"}));
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testJoinerMapJoiner() {
 Map<String, String> map = new HashMap<>();
 map.put("apple", "fruit");
 map.put("banana", "fruit");
 map.put("orange", "fruit");

 Joiner joiner = Joiner.on(",");
 MapJoiner mapJoiner = joiner.withKeyValueSeparator(" -> ");
 StringBuilder expected = new StringBuilder();
 expected.append("apple -> fruit").append(joiner.separator).append("banana -> fruit").append(joiner.separator).append("orange -> fruit");
 StringBuilder actual = new StringBuilder();
 mapJoiner.appendTo(actual, map);
 assertEquals(expected.toString(), actual.toString());
 }
}