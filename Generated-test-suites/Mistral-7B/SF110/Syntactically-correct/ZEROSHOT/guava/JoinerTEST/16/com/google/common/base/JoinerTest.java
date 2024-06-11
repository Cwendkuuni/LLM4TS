import org.junit.Test;
import static org.junit.Assert.*;
import com.google.common.base.Joiner;
import java.util.*;

public class JoinerTest {

 @Test
 public void testJoinerWithStringSeparator() {
 Joiner separatorJoiner = Joiner.on(",");
 String joined = separatorJoiner.join(Arrays.asList("apple", "banana", "cherry"));
 assertEquals("apple,banana,cherry", joined);
 }

 @Test
 public void testJoinerWithCharSeparator() {
 Joiner separatorJoiner = Joiner.on('-');
 String joined = separatorJoiner.join(Arrays.asList("apple", "banana", "cherry"));
 assertEquals("apple-banana-cherry", joined);
 }

 @Test
 public void testAppendToIterable() throws IOException {
 StringBuilder stringBuilder = new StringBuilder();
 Joiner joiner = Joiner.on(",");
 joiner.appendTo(stringBuilder, Arrays.asList("apple", "banana", "cherry"));
 assertEquals("apple,banana,cherry", stringBuilder.toString());
 }

 @Test
 public void testAppendToArray() throws IOException {
 StringBuilder stringBuilder = new StringBuilder();
 Joiner joiner = Joiner.on(",");
 joiner.appendTo(stringBuilder, new Object[]{"apple", "banana", "cherry"});
 assertEquals("apple,banana,cherry", stringBuilder.toString());
 }

 @Test
 public void testAppendToObjectNullAndString() throws IOException {
 StringBuilder stringBuilder = new StringBuilder();
 Joiner joiner = Joiner.on(",");
 joiner.appendTo(stringBuilder, null, "null", "apple", "banana", "cherry");
 assertEquals("null,apple,banana,cherry", stringBuilder.toString());
 }

 @Test
 public void testAppendToObjectArrayWithNull() throws IOException {
 StringBuilder stringBuilder = new StringBuilder();
 Joiner joiner = Joiner.on(",");
 joiner.appendTo(stringBuilder, new Object[]{null, "apple", "banana", "cherry"});
 assertEquals("null,apple,banana,cherry", stringBuilder.toString());
 }

 @Test
 public void testUseForNull() {
 Joiner nullJoiner = Joiner.on(",").useForNull("N/A");
 String joined = nullJoiner.join(Arrays.asList(null, "apple", "banana", "cherry"));
 assertEquals("N/A,apple,banana,cherry", joined);
 }

 @Test
 public void testSkipNulls() {
 Joiner nullJoiner = Joiner.on(",").skipNulls();
 String joined = nullJoiner.join(Arrays.asList(null, "apple", null, "banana", null, "cherry"));
 assertEquals("apple,banana", joined);
 }

 @Test
 public void testMapJoiner() {
 Map<String, String> map = new HashMap<>();
 map.put("name", "John");
 map.put("age", "25");
 map.put("city", "New York");

 Joiner separatorJoiner = Joiner.on(":");
 MapJoiner mapJoiner = separatorJoiner.withKeyValueSeparator(":");

 StringBuilder stringBuilder = new StringBuilder();
 mapJoiner.appendTo(stringBuilder, map);
 assertEquals("name:John:age:25:city:New York", stringBuilder.toString());
 }
}