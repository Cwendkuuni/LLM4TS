import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;

public class JoinerTest {

 private Joiner joiner;
 private ByteArrayOutputStream outputStream;

 @Before
 public void setUp() {
 joiner = Joiner.on("/");
 outputStream = new ByteArrayOutputStream();
 }

 @Test
 public void testAppendToIterable() throws IOException {
 Iterable<String> parts = Arrays.asList("test1", "test2", "test3");
 joiner.appendTo(outputStream, parts);
 assertEquals("/test1/test2/test3", outputStream.toString());
 }

 @Test
 public void testAppendToObject() throws IOException {
 Object[] parts = new Object[]{"test1", "test2", "test3"};
 joiner.appendTo(outputStream, parts);
 assertEquals("/test1/test2/test3", outputStream.toString());
 }

 @Test
 public void testAppendToIterator() throws IOException {
 Iterator<String> iterator = Arrays.asList("test1", "test2", "test3").iterator();
 joiner.appendTo(outputStream, iterator);
 assertEquals("/test1/test2/test3", outputStream.toString());
 }

 @Test
 public void testAppendToStringBuilder() {
 StringBuilder builder = new StringBuilder();
 joiner.appendTo(builder, Arrays.asList("test1", "test2", "test3"));
 assertEquals("/test1/test2/test3", builder.toString());
 }

 @Test
 public void testAppendToStringBuilderWithEmptyList() {
 StringBuilder builder = new StringBuilder();
 joiner.appendTo(builder, Collections.emptyList());
 assertEquals("", builder.toString());
 }

 @Test
 public void testJoinIterable() {
 Iterable<String> parts = Arrays.asList("test1", "test2", "test3");
 assertEquals("/test1/test2/test3", joiner.join(parts));
 }

 @Test
 public void testJoinObject() {
 Object[] parts = new Object[]{"test1", "test2", "test3"};
 assertEquals("/test1/test2/test3", joiner.join(parts));
 }

 @Test
 public void testJoinIterator() {
 Iterator<String> iterator = Arrays.asList("test1", "test2", "test3").iterator();
 assertEquals("/test1/test2/test3", joiner.join(iterator));
 }

 @Test
 public void testJoinStringBuilder() {
 StringBuilder builder = new StringBuilder();
 joiner.appendTo(builder, Arrays.asList("test1", "test2", "test3"));
 assertEquals("/test1/test2/test3", joiner.join(builder));
 }

 @Test
 public void testJoinEmptyStringBuilder() {
 StringBuilder builder = new StringBuilder();
 assertEquals("", joiner.join(builder));
 }

 @Test
 public void testJoinNull() {
 Object[] parts = new Object[]{null, "test2", "test3"};
 Joiner joinerNull = joiner.useForNull("null");
 assertEquals("null/test2/test3", joinerNull.join(parts));
 }

 @Test
 public void testJoinMap() {
 Map<String, String> map = new HashMap<>();
 map.put("key1", "value1");
 map.put("key2", "value2");
 Joiner mapJoiner = joiner.withKeyValueSeparator(":");
 assertEquals("key1:value1:key2:value2", mapJoiner.join(map));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSkipNullsAndUseForNull() {
 Object[] parts = new Object[]{null, "test2", "test3"};
 Joiner joinerUseForNull = joiner.useForNull("null");
 Joiner joinerSkipNulls = joinerUseForNull.skipNulls();
 joinerSkipNulls.appendTo(outputStream, parts);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testMapJoinerWithUseForNull() {
 Map<String, String> map = new HashMap<>();
 map.put("key1", "value1");
 map.put("key2", "value2");
 Joiner mapJoinerUseForNull = joiner.useForNull("null");
 assertEquals("null:value1:null:value2", mapJoinerUseForNull.withKeyValueSeparator(":").join(map));
 }

 @Test
 public void testJoinMapIterable() throws IOException {
 Map<String, String> map = new HashMap<>();
 map.put("key1", "value1");
 map.put("key2", "value2");
 Joiner mapJoiner = joiner.withKeyValueSeparator(":");
 ByteArrayOutputStream outputStreamMap = new ByteArrayOutputStream();
 mapJoiner.appendTo(outputStreamMap, map.entrySet());
 assertEquals("key1:value1:key2:value2", outputStreamMap.toString());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testUseForNullWithSkipNulls() {
 Object[] parts = new Object[]{null, "test2", "test3"};
 Joiner joinerUseForNull = joiner.useForNull("null");
 Joiner joinerSkipNulls = joinerUseForNull.skipNulls();
 assertThrows(UnsupportedOperationException.class, () -> joinerSkipNulls.appendTo(outputStream, parts));
 }
}