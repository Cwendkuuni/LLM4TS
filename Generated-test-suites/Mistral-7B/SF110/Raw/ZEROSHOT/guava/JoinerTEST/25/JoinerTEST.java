import org.junit.Assert;
import org.junit.Test;
import com.google.common.base.Joiner;
import com.google.common.base.MapJoiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class JoinerTest {

 @Test
 public void testJoinerOnString() {
 Joiner joiner = Joiner.on(",");
 String result = joiner.join(Arrays.asList("apple", "banana", "cherry"));
 Assert.assertEquals("apple,banana,cherry", result);
 }

 @Test
 public void testJoinerOnChar() {
 Joiner joiner = Joiner.on('/');
 String result = joiner.join(Arrays.asList("apple", "banana", "cherry"));
 Assert.assertEquals("apple/banana/cherry", result);
 }

 @Test
 public void testAppendTo() throws IOException {
 StringBuilder sb = new StringBuilder();
 Joiner joiner = Joiner.on(",");
 joiner.appendTo(sb, Arrays.asList("apple", "banana", "cherry"));
 Assert.assertEquals("apple,banana,cherry", sb.toString());
 }

 @Test
 public void testAppendToArray() throws IOException {
 StringBuilder sb = new StringBuilder();
 Joiner joiner = Joiner.on(",");
 joiner.appendTo(sb, new Object[]{"apple", "banana", "cherry"});
 Assert.assertEquals("apple,banana,cherry", sb.toString());
 }

 @Test
 public void testAppendToIterable() throws IOException {
 StringBuilder sb = new StringBuilder();
 Joiner joiner = Joiner.on(",");
 joiner.appendTo(sb, Lists.newArrayList("apple", "banana", "cherry"));
 Assert.assertEquals("apple,banana,cherry", sb.toString());
 }

 @Test
 public void testJoin() {
 Joiner joiner = Joiner.on(",");
 String result = joiner.join(Arrays.asList("apple", "banana", "cherry"));
 Assert.assertEquals("apple,banana,cherry", result);
 }

 @Test
 public void testJoinArray() {
 Joiner joiner = Joiner.on(",");
 String result = joiner.join(new Object[]{"apple", "banana", "cherry"});
 Assert.assertEquals("apple,banana,cherry", result);
 }

 @Test
 public void testJoinIterable() {
 Joiner joiner = Joiner.on(",");
 String result = joiner.join(Arrays.asList(new Object[]{"apple", "banana", "cherry"}));
 Assert.assertEquals("apple,banana,cherry", result);
 }

 @Test
 public void testUseForNull() {
 Joiner joiner = Joiner.on(",").useForNull("N/A");
 List<String> list = Arrays.asList(null, "apple", null, "banana", null);
 String result = joiner.join(list);
 Assert.assertEquals("N/A,apple,N/A,banana,N/A", result);
 }

 @Test
 public void testSkipNulls() {
 Joiner joiner = Joiner.on(",").skipNulls();
 List<String> list = Arrays.asList(null, "apple", null, "banana", null);
 String result = joiner.join(list);
 Assert.assertEquals("apple,banana", result);
 }

 @Test
 public void testMapJoiner() {
 Joiner joiner = Joiner.on(",");
 Map<String, String> map = Maps.newHashMap();
 map.put("apple", "fruit");
 map.put("banana", "yellow");
 map.put("cherry", "tree");
 MapJoiner mapJoiner = joiner.withKeyValueSeparator(":");
 String result = mapJoiner.join(map.entrySet());
 Assert.assertEquals("apple:fruit,banana:yellow,cherry:tree", result);
 }

 @Test
 public void testMapJoinerUseForNull() {
 Joiner joiner = Joiner.on(",").useForNull("N/A");
 Map<String, String> map = Maps.newHashMap();
 map.put(null, "value");
 MapJoiner mapJoiner = joiner.withKeyValueSeparator(":");
 String result = mapJoiner.join(map.entrySet());
 Assert.assertEquals("N/A:value", result);
 }
}