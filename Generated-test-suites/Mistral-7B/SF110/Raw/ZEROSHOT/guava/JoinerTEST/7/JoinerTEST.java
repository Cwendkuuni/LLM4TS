import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Arrays;
import com.google.common.collect.Iterables;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class JoinerTest {

 @Test
 public void testOn() {
 Joiner joiner = Joiner.on(",");
 assertSame(joiner, joiner.on(","));
 }

 @Test
 public void testOnChar() {
 Joiner joiner = Joiner.on('-');
 assertSame(joiner, joiner.on('-'));
 }

 @Test
 public void testAppendToIterable() throws IOException {
 StringWriter stringWriter = new StringWriter();
 Joiner joiner = Joiner.on(",");
 joiner.appendTo(stringWriter, Arrays.asList("apple", "banana", "cherry"));
 assertEquals("apple,banana,cherry", stringWriter.toString());
 }

 @Test
 public void testAppendToObjectArray() throws IOException {
 StringWriter stringWriter = new StringWriter();
 Joiner joiner = Joiner.on(",");
 joiner.appendTo(stringWriter, new Object[]{"apple", "banana", "cherry"});
 assertEquals("apple,banana,cherry", stringWriter.toString());
 }

 @Test
 public void testAppendToNull() throws IOException {
 StringWriter stringWriter = new StringWriter();
 Joiner joiner = Joiner.on(",");
 joiner.appendTo(stringWriter, (Iterable<?>)Collections.singletonList(null));
 assertEquals("", stringWriter.toString());
 }

 @Test
 public void testAppendToNullWithUseForNull() throws IOException {
 StringWriter stringWriter = new StringWriter();
 Joiner joiner = Joiner.on(",").useForNull("N/A");
 joiner.appendTo(stringWriter, Arrays.asList(null, "apple", null));
 assertEquals("N/A,apple,N/A", stringWriter.toString());
 }

 @Test
 public void testAppendToNullWithSkipNulls() throws IOException {
 StringWriter stringWriter = new StringWriter();
 Joiner joiner = Joiner.on(",").skipNulls();
 joiner.appendTo(stringWriter, Arrays.asList(null, "apple", null, "banana"));
 assertEquals("apple,banana", stringWriter.toString());
 }

 @Test
 public void testAppendToMap() throws IOException {
 StringWriter stringWriter = new StringWriter();
 Joiner joiner = Joiner.on(",");
 Map<String, String> map = new HashMap<>();
 map.put("key1", "value1");
 map.put("key2", "value2");
 joiner.appendTo(stringWriter, map.entrySet());
 assertEquals("key1=value1,key2=value2", stringWriter.toString());
 }

 @Test
 public void testJoinIterable() {
 String join = Joiner.on(",").join(Arrays.asList("apple", "banana", "cherry"));
 assertEquals("apple,banana,cherry", join);
 }

 @Test
 public void testJoinObjectArray() {
 String join = Joiner.on(",").join(new Object[]{"apple", "banana", "cherry"});
 assertEquals("apple,banana,cherry", join);
 }

 @Test
 public void testJoinNull() {
 String join = Joiner.on(",").join((Iterable<?>)Collections.singletonList(null));
 assertEquals("", join);
 }

 @Test
 public void testJoinNullWithUseForNull() {
 String join = Joiner.on(",").useForNull("N/A").join(Arrays.asList(null, "apple", null));
 assertEquals("N/A,apple,N/A", join);
 }

 @Test
 public void testJoinNullWithSkipNulls() {
 String join = Joiner.on(",").skipNulls().join(Arrays.asList(null, "apple", null, "banana"));
 assertEquals("apple,banana", join);
 }

 @Test
 public void testJoinMap() {
 String join = Joiner.on(",").join(new HashMap<String, String>() {{
 put("key1", "value1");
 put("key2", "value2");
 }});
 assertEquals("key1=value1,key2=value2", join);
 }
}