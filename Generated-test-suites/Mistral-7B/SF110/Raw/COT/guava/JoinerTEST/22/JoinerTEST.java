import com.google.common.base.Joiner;
import com.google.common.base.MapJoiner;
import com.google.common.collect.Iterators;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RunWith(JUnit4.class)
public class JoinerTest {

 @SuppressWarnings("unchecked")
 @Test
 public void testOn() {
 Joiner joiner1 = Joiner.on(",");
 Joiner joiner2 = Joiner.on("|");
 Assert.assertEquals(",", joiner1.separator());
 Assert.assertEquals("|", joiner2.separator());
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testAppendTo_Iterable() throws IOException {
 Joiner joiner = Joiner.on(",");
 List<Object> list = Arrays.asList("apple", "banana", "cherry");
 StringBuilder stringBuilder = new StringBuilder();
 joiner.appendTo(stringBuilder, list);
 String expected = "apple,banana,cherry";
 Assert.assertEquals(expected, stringBuilder.toString());
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testAppendTo_Iterator() throws IOException {
 Joiner joiner = Joiner.on(",");
 List<Object> list = Arrays.asList("apple", "banana", "cherry");
 Iterator<Object> iterator = list.iterator();
 StringBuilder stringBuilder = new StringBuilder();
 joiner.appendTo(stringBuilder, iterator);
 String expected = "apple,banana,cherry";
 Assert.assertEquals(expected, stringBuilder.toString());
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testAppendTo_ObjectArray() throws IOException {
 Joiner joiner = Joiner.on(",");
 Object[] array = new Object[]{"apple", "banana", "cherry"};
 StringBuilder stringBuilder = new StringBuilder();
 joiner.appendTo(stringBuilder, array);
 String expected = "apple,banana,cherry";
 Assert.assertEquals(expected, stringBuilder.toString());
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testAppendTo_ObjectArray_Nullable() throws IOException {
 Joiner joiner = Joiner.on(",");
 Object[] array = new Object[]{null, "apple", "banana", "cherry"};
 StringBuilder stringBuilder = new StringBuilder();
 joiner.appendTo(stringBuilder, array);
 String expected = "null,apple,banana,cherry";
 Assert.assertEquals(expected, stringBuilder.toString());
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testJoin_Iterable() {
 Joiner joiner = Joiner.on(",");
 List<Object> list = Arrays.asList("apple", "banana", "cherry");
 String expected = "apple,banana,cherry";
 Assert.assertEquals(expected, joiner.join(list));
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testJoin_Iterator() {
 Joiner joiner = Joiner.on(",");
 List<Object> list = Arrays.asList("apple", "banana", "cherry");
 Iterator<Object> iterator = list.iterator();
 String expected = "apple,banana,cherry";
 Assert.assertEquals(expected, joiner.join(iterator));
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testJoin_ObjectArray() {
 Joiner joiner = Joiner.on(",");
 Object[] array = new Object[]{"apple", "banana", "cherry"};
 String expected = "apple,banana,cherry";
 Assert.assertEquals(expected, joiner.join(array));
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testJoin_ObjectArray_Nullable() {
 Joiner joiner = Joiner.on(",");
 Object[] array = new Object[]{null, "apple", "banana", "cherry"};
 String expected = "null,apple,banana,cherry";
 Assert.assertEquals(expected, joiner.join(array));
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testUseForNull() {
 Joiner joiner = Joiner.on(",");
 Joiner nullJoiner = joiner.useForNull("N/A");
 Object[] array = new Object[]{null, "apple", "banana", "cherry"};
 StringBuilder stringBuilder = new StringBuilder();
 nullJoiner.appendTo(stringBuilder, array);
 String expected = "N/A,apple,banana,cherry";
 Assert.assertEquals(expected, stringBuilder.toString());
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testSkipNulls() {
 Joiner joiner = Joiner.on(",");
 Joiner skipNullsJoiner = joiner.skipNulls();
 Object[] array = new Object[]{null, "apple", null, "banana", null, "cherry"};
 StringBuilder stringBuilder = new StringBuilder();
 skipNullsJoiner.appendTo(stringBuilder, array);
 String expected = "apple,banana,cherry";
 Assert.assertEquals(expected, stringBuilder.toString());
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testMapJoiner_AppendTo_Map() throws IOException {
 Joiner joiner = Joiner.on(",");
 Map<String, String> map = new ConcurrentHashMap<>();
 map.put("key1", "value1");
 map.put("key2", "value2");
 MapJoiner mapJoiner = joiner.withKeyValueSeparator("=");
 StringBuilder stringBuilder = new StringBuilder();
 mapJoiner.appendTo(stringBuilder, map);
 String expected = "key1=value1,key2=value2";
 Assert.assertEquals(expected, stringBuilder.toString());
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testMapJoiner_Join_Map() {
 Joiner joiner = Joiner.on(",");
 Map<String, String> map = new ConcurrentHashMap<>();
 map.put("key1", "value1");
 map.put("key2", "value2");
 MapJoiner mapJoiner = joiner.withKeyValueSeparator("=");
 String expected = "key1=value1,key2=value2";
 Assert.assertEquals(expected, mapJoiner.join(map));
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testMapJoiner_UseForNull() {
 Joiner joiner = Joiner.on(",");
 Map<String, String> map = new ConcurrentHashMap<>();
 map.put("key1", "value1");
 map.put("key2", "value2");
 MapJoiner mapJoiner = joiner.withKeyValueSeparator("=").useForNull("N/A");
 map.put(null, null);
 StringBuilder stringBuilder = new StringBuilder();
 mapJoiner.appendTo(stringBuilder, map.entrySet());
 String expected = "key1=value1,key2=value2,N/A=N/A";
 Assert.assertEquals(expected, stringBuilder.toString());
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testIterableEdgeCases() {
 Joiner joiner = Joiner.on(",");
 List<Object> emptyList = Collections.emptyList();
 StringBuilder stringBuilder = new StringBuilder();
 joiner.appendTo(stringBuilder, emptyList.iterator());
 Assert.assertEquals("", stringBuilder.toString());

 Object[] nullArray = null;
 StringBuilder nullStringBuilder = new StringBuilder();
 try {
 joiner.appendTo(nullStringBuilder, nullArray);
 } catch (NullPointerException e) {
 // expected
 }

 Object[] arrayWithNull = new Object[]{null, "apple", "banana", null, "cherry"};
 StringBuilder stringBuilderWithNull = new StringBuilder();
 joiner.appendTo(stringBuilderWithNull, arrayWithNull);
 String expectedEdgeCase = "null,apple,banana,null,cherry";
 Assert.assertEquals(expectedEdgeCase, stringBuilderWithNull.toString());
 }

 @SuppressWarnings("unchecked")
 @Test(expected = IllegalArgumentException.class)
 public void testAppendTo_NullIterator() throws IOException {
 Joiner joiner = Joiner.on(",");
 Iterator<Object> nullIterator = null;
 StringBuilder stringBuilder = new StringBuilder();
 joiner.appendTo(stringBuilder, nullIterator);
 }

 @SuppressWarnings("unchecked")
 @Test(expected = NullPointerException.class)
 public void testJoin_NullIterable() {
 Joiner joiner = Joiner.on(",");
 Iterator<Object> nullIterator = null;
 String expectedException = "java.lang.NullPointerException";
 try {
 joiner.join(nullIterator);
 } catch (Exception e) {
 Assert.assertEquals(expectedException, e.getClass().getName());
 }
 }

 @SuppressWarnings("unchecked")
 @Test(expected = IllegalArgumentException.class)
 public void testJoin_NullIterator2() {
 Joiner joiner = Joiner.on(",");
 Iterator<Object> nullIterator = null;
 joiner.join(nullIterator);
 }

 @SuppressWarnings("unchecked")
 @Test(expected = NullPointerException.class)
 public void testJoin_NullIterable2() {
 Joiner joiner = Joiner.on(",");
 List<Object> nullList = null;
 String expectedException = "java.lang.NullPointerException";
 try {
 joiner.join(nullList);
 } catch (Exception e) {
 Assert.assertEquals(expectedException, e.getClass().getName());
 }
 }
}