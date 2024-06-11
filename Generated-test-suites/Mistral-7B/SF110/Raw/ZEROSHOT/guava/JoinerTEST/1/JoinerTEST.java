import static org.junit.Assert.*;
import com.google.common.base.*;
import org.junit.Test;

public class JoinerTest {

 @Test
 public void testJoinerCreation() {
 Joiner joinerWithStringSeparator = Joiner.on(",");
 Joiner joinerWithCharSeparator = Joiner.on('-');
 }

 @Test
 public void testAppendToIterable() throws IOException {
 StringBuffer expected = new StringBuffer();
 expected.append("apple").append(Joiner.on(",").separator).append("banana")
 .append(Joiner.on(",").separator).append("orange");
 StringBuffer actual = new StringBuffer();
 Joiner joiner = Joiner.on(",");
 joiner.appendTo(actual, Joiner.asIterable("apple", "banana", "orange"));
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testAppendToObjectArray() throws IOException {
 StringBuffer expected = new StringBuffer();
 expected.append("apple").append(Joiner.on(",").separator).append("banana")
 .append(Joiner.on(",").separator).append("orange");
 StringBuffer actual = new StringBuffer();
 Joiner joiner = Joiner.on(",");
 joiner.appendTo(actual, "apple", "banana", "orange");
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testAppendToIterableNullFirst() throws IOException {
 StringBuffer expected = new StringBuffer();
 expected.append("");
 StringBuffer actual = new StringBuffer();
 Joiner joiner = Joiner.on(",");
 joiner.appendTo(actual, null, "banana", "orange");
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testAppendToIterableNullMiddle() throws IOException {
 StringBuffer expected = new StringBuffer();
 expected.append("banana").append(Joiner.on(",").separator).append("orange");
 StringBuffer actual = new StringBuffer();
 Joiner joiner = Joiner.on(",");
 joiner.appendTo(actual, "apple", null, "orange");
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testAppendToIterableNullLast() throws IOException {
 StringBuffer expected = new StringBuffer();
 expected.append("apple").append(Joiner.on(",").separator).append("");
 StringBuffer actual = new StringBuffer();
 Joiner joiner = Joiner.on(",");
 joiner.appendTo(actual, "apple", "banana", null);
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testAppendToAppendableNullFirst() throws IOException {
 StringBuilder expected = new StringBuilder();
 expected.append("");
 StringBuilder actual = new StringBuilder();
 Joiner joiner = Joiner.on(",");
 joiner.appendTo(actual, null, Joiner.iterable("banana", "orange"));
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testAppendToAppendableNullMiddle() throws IOException {
 StringBuilder expected = new StringBuilder();
 expected.append("banana").append(Joiner.on(",").separator);
 StringBuilder actual = new StringBuilder();
 Joiner joiner = Joiner.on(",");
 joiner.appendTo(actual, "apple", null, Joiner.iterable("orange"));
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testAppendToAppendableNullLast() throws IOException {
 StringBuilder expected = new StringBuilder();
 expected.append("apple").append(Joiner.on(",").separator);
 StringBuilder actual = new StringBuilder();
 Joiner joiner = Joiner.on(",");
 joiner.appendTo(actual, Joiner.iterable("apple", null), "orange");
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testJoinIterable() {
 String expected = "apple,banana,orange";
 String actual = Joiner.on(",").join(Joiner.asIterable("apple", "banana", "orange"));
 assertEquals(expected, actual);
 }

 @Test
 public void testJoinObjectArray() {
 String expected = "apple,banana,orange";
 String actual = Joiner.on(",").join(new String[]{"apple", "banana", "orange"});
 assertEquals(expected, actual);
 }

 @Test
 public void testJoinIterableNullFirst() {
 String expected = "";
 String actual = Joiner.on(",").join(null);
 assertEquals(expected, actual);
 }

 @Test
 public void testJoinIterableNullMiddle() {
 String expected = "banana,orange";
 String actual = Joiner.on(",").join(Joiner.iterable("apple", null, "orange"));
 assertEquals(expected, actual);
 }

 @Test
 public void testJoinIterableNullLast() {
 String expected = "apple,orange";
 String actual = Joiner.on(",").join(Joiner.iterable("apple", "banana", null));
 assertEquals(expected, actual);
 }

 @Test
 public void testJoinMap() {
 Map<String, String> map = new HashMap<>();
 map.put("key1", "value1");
 map.put("key2", "value2");
 map.put("key3", "value3");
 String expected = "key1=" + Joiner.on("=").separator + "value1," +
 Joiner.on("=").separator + "key2=" + Joiner.on("=").separator + "value2," +
 Joiner.on("=").separator + "key3=" + Joiner.on("=").separator + "value3";
 String actual = Joiner.on(",=").join(map.entrySet());
 assertEquals(expected, actual);
 }
}