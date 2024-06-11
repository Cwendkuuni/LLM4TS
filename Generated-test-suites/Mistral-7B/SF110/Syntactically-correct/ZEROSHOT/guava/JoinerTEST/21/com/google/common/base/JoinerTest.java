import com.google.common.base.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class JoinerTest {

 @Test
 public void testJoinerOnString() {
 Joiner joiner = Joiner.on(",");
 String result = joiner.join(Arrays.asList("apple", "banana", "cherry"));
 assertEquals("apple,banana,cherry", result);
 }

 @Test
 public void testJoinerOnChar() {
 Joiner joiner = Joiner.on('/');
 String result = joiner.join(Arrays.asList("apple", "banana", "cherry"));
 assertEquals("apple/banana/cherry", result);
 }

 @Test
 public void testAppendToIterable() throws IOException {
 StringBuilder sb = new StringBuilder();
 Joiner joiner = Joiner.on(",");
 joiner.appendTo(sb, Arrays.asList("apple", "banana", "cherry"));
 assertEquals("apple,banana,cherry", sb.toString());
 }

 @Test
 public void testAppendToObject() throws IOException {
 StringBuilder sb = new StringBuilder();
 Joiner joiner = Joiner.on(",");
 joiner.appendTo(sb, new Object[]{"apple", "banana", "cherry"});
 assertEquals("apple,banana,cherry", sb.toString());
 }

 @Test
 public void testAppendToIterableWithNull() throws IOException {
 StringBuilder sb = new StringBuilder();
 Joiner joiner = Joiner.on(",");
 joiner.appendTo(sb, Arrays.asList("apple", null, "cherry"));
 assertEquals("apple,null,cherry", sb.toString());
 }

 @Test
 public void testAppendToObjectWithNull() throws IOException {
 StringBuilder sb = new StringBuilder();
 Joiner joiner = Joiner.on(",");
 joiner.appendTo(sb, new Object[]{"apple", null, "cherry"});
 assertEquals("apple,null,cherry", sb.toString());
 }

 @Test
 public void testAppendToIterableWithUseForNull() throws IOException {
 StringBuilder sb = new StringBuilder();
 String nullText = "N/A";
 Joiner joiner = Joiner.on(",").useForNull(nullText);
 joiner.appendTo(sb, Arrays.asList("apple", null, "cherry"));
 assertEquals("apple," + nullText + ",cherry", sb.toString());
 }

 @Test
 public void testAppendToObjectWithUseForNull() throws IOException {
 StringBuilder sb = new StringBuilder();
 String nullText = "N/A";
 Joiner joiner = Joiner.on(",").useForNull(nullText);
 joiner.appendTo(sb, new Object[]{"apple", null, "cherry"});
 assertEquals("apple," + nullText + ",cherry", sb.toString());
 }

 @Test
 public void testJoinIterable() {
 String result = Joiner.on(",").join(Arrays.asList("apple", "banana", "cherry"));
 assertEquals("apple,banana,cherry", result);
 }

 @Test
 public void testJoinObject() {
 String result = Joiner.on(",").join(new Object[]{"apple", "banana", "cherry"});
 assertEquals("apple,banana,cherry", result);
 }

 @Test
 public void testJoinIterableWithNull() {
 String result = Joiner.on(",").join(Arrays.asList("apple", null, "cherry"));
 assertEquals("apple,null,cherry", result);
 }

 @Test
 public void testJoinObjectWithNull() {
 String result = Joiner.on(",").join(new Object[]{"apple", null, "cherry"});
 assertEquals("apple,null,cherry", result);
 }

 @Test
 public void testJoinIterableWithUseForNull() {
 String nullText = "N/A";
 String result = Joiner.on(",").useForNull(nullText).join(Arrays.asList("apple", null, "cherry"));
 assertEquals("apple," + nullText + ",cherry", result);
 }

 @Test
 public void testJoinObjectWithUseForNull() {
 String nullText = "N/A";
 String result = Joiner.on(",").useForNull(nullText).join(new Object[]{"apple", null, "cherry"});
 assertEquals("apple," + nullText + ",cherry", result);
 }

 @Test
 public void testSkipNullsIterable() {
 String result = Joiner.on(",").skipNulls().join(Arrays.asList("apple", null, "cherry"));
 assertEquals("apple,cherry", result);
 }

 @Test
 public void testSkipNullsObject() {
 String result = Joiner.on(",").skipNulls().join(new Object[]{"apple", null, "cherry"});
 assertEquals("apple,cherry", result);
 }

 @Test
 public void testMapJoiner() {
 Map<String, String> map = new HashMap<>();
 map.put("apple", "fruit");
 map.put("banana", "fruit");
 map.put("cherry", null);

 MapJoiner mapJoiner = Joiner.on(":").withKeyValueSeparator("=");
 String result = mapJoiner.join(map);
 assertEquals("apple=fruit:banana=fruit", result);
 }

}