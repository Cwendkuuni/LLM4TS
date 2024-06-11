package org.apache.commons.lang3;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertIterableEquals;
import static org.junit.Assert.assertMapEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertThrowsExactly;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.junit.Test;

public class ValidateTest {

 @Test
 public void testIsTrue() {
 Validate.isTrue(true);
 Validate.isTrue(true, "Expected true but was false");
 Validate.isTrue(false, "Expected false but was true", 1L);
 Validate.isTrue(false, "Expected false but was true", 1.0);
 Validate.isTrue(false, "Expected false but was true", new Object[] { 1, 2, 3 });
 Validate.isTrue(false, "Expected false but was true");
 }

 @Test
 public void testNotNull() {
 Validate.notNull(new Object());
 Validate.notNull(new byte[0]);
 Validate.notNull(new short[0]);
 Validate.notNull(new int[0]);
 Validate.notNull(new long[0]);
 Validate.notNull(new float[0]);
 Validate.notNull(new double[0]);
 Validate.notNull(new char[0]);
 Validate.notNull(new boolean[0]);
 Validate.notNull(new String());
 Validate.notNull(new StringBuilder());
 Validate.notNull(new HashSet<>());
 Validate.notNull(new ArrayList<>());
 Validate.notNull(new LinkedList<>());
 Validate.notNull(new HashMap<>());
 Validate.notNull(new LinkedHashMap<>());
 Validate.notNull(new TreeSet<>());
 Validate.notNull(new TreeMap<>());
 Validate.notNull(new EnumSet<>(Enum.class));
 Validate.notNull(new EnumMap<>(Enum.class));
 Validate.notNull(null);
 Validate.notNull(null, "Expected non-null but was null", new Object[0]);
 }

 @Test
 public void testArrayNotEmpty() {
 byte[] bytes = new byte[0];
 short[] shorts = new short[0];
 int[] ints = new int[0];
 long[] longs = new long[0];
 float[] floats = new float[0];
 double[] doubles = new double[0];
 char[] chars = new char[0];
 boolean[] booleans = new boolean[0];
 String[] strings = new String[0];
 Validate.notEmpty(bytes, "Expected non-empty array but was empty", new Object[0]);
 Validate.notEmpty(shorts, "Expected non-empty array but was empty", new Object[0]);
 Validate.notEmpty(ints, "Expected non-empty array but was empty", new Object[0]);
 Validate.notEmpty(longs, "Expected non-empty array but was empty", new Object[0]);
 Validate.notEmpty(floats, "Expected non-empty array but was empty", new Object[0]);
 Validate.notEmpty(doubles, "Expected non-empty array but was empty", new Object[0]);
 Validate.notEmpty(chars, "Expected non-empty array but was empty", new Object[0]);
 Validate.notEmpty(booleans, "Expected non-empty array but was empty", new Object[0]);
 Validate.notEmpty(strings, "Expected non-empty array but was empty", new Object[0]);
 Validate.notEmpty(new byte[] { 1 }, "Expected non-empty array but was empty", new Object[0]);
 Validate.notEmpty(new short[] { 1 }, "Expected non-empty array but was empty", new Object[0]);
 Validate.notEmpty(new int[] { 1 }, "Expected non-empty array but was empty", new Object[0]);
 Validate.notEmpty(new long[] { 1L }, "Expected non-empty array but was empty", new Object[0]);
 Validate.notEmpty(new float[] { 1f }, "Expected non-empty array but was empty", new Object[0]);
 Validate.notEmpty(new double[] { 1d }, "Expected non-empty array but was empty", new Object[0]);
 Validate.notEmpty(new char[] { 'a' }, "Expected non-empty array but was empty", new Object[0]);
 Validate.notEmpty(new boolean[] { true }, "Expected non-empty array but was empty", new Object[0]);
 Validate.notEmpty(new String[] { "" }, "Expected non-empty array but was empty", new Object[0]);
 }

 @Test
 public void testCollectionNotEmpty() {
 List<Integer> list = Collections.emptyList();
 Set<Integer> set = Collections.emptySet();
 Validate.notEmpty(list, "Expected non-empty collection but was empty", new Object[0]);
 Validate.notEmpty(set, "Expected non-empty collection but was empty", new Object[0]);
 List<Integer> list2 = new ArrayList<>();
 Set<Integer> set2 = new HashSet<>();
 Validate.notEmpty(list2, "Expected non-empty collection but was empty", new Object[0]);
 Validate.notEmpty(set2, "Expected non-empty collection but was empty", new Object[0]);
 list2.add(1);
 set2.add(1);
 Validate.notEmpty(list2, "Expected non-empty collection but was empty", new Object[0]);
 Validate.notEmpty(set2, "Expected non-empty collection but was empty", new Object[0]);
 }

 @Test
 public void testMapNotEmpty() {
 Map<String, Integer> map = Collections.emptyMap();
 Validate.notEmpty(map, "Expected non-empty map but was empty", new Object[0]);
 Map<String, Integer> map2 = new HashMap<>();
 map2.put("key", 1);
 Validate.notEmpty(map2, "Expected non-empty map but was empty", new Object[0]);
 }

 @Test
 public void testCharSequenceNotEmpty() {
 String str = "";
 StringBuilder sb = new StringBuilder();
 Validate.notEmpty(str, "Expected non-empty character sequence but was empty", new Object[0]);
 Validate.notEmpty(sb, "Expected non-empty character sequence but was empty", new Object[0]);
 }

 @Test
 public void testCharSequenceNotBlank() {
 String str = "";
 StringBuilder sb = new StringBuilder();
 sb.append("a");
 Validate.notBlank(str, "Expected non-blank character sequence but was blank", new Object[0]);
 Validate.notBlank(sb, "Expected non-blank character sequence but was blank", new Object[0]);
 }

 @Test
 public void testArrayNoNullElements() {
 byte[] bytes = new byte[] { 1, null };
 short[] shorts = new short[] { 1, 2, null };
 int[] ints = new int[] { 1, 2, 3, null };
 long[] longs = new long[] { 1L, 2L, 3L, null };
 float[] floats = new float[] { 1f, 2f, 3f, null };
 double[] doubles = new double[] { 1d, 2d, 3d, null };
 char[] chars = new char[] { 'a', 'b', '\u0000' };
 boolean[] booleans = new boolean[] { true, false, null };
 String[] strings = new String[] { "", null };
 Validate.noNullElements(bytes, "Expected array without null elements but contained null", new Object[0]);
 Validate.noNullElements(shorts, "Expected array without null elements but contained null", new Object[0]);
 Validate.noNullElements(ints, "Expected array without null elements but contained null", new Object[0]);
 Validate.noNullElements(longs, "Expected array without null elements but contained null", new Object[0]);
 Validate.noNullElements(floats, "Expected array without null elements but contained null", new Object[0]);
 Validate.noNullElements(doubles, "Expected array without null elements but contained null", new Object[0]);
 Validate.noNullElements(chars, "Expected array without null elements but contained null", new Object[0]);
 Validate.noNullElements(booleans, "Expected array without null elements but contained null", new Object[0]);
 Validate.noNullElements(strings, "Expected array without null elements but contained null", new Object[0]);
 }

 @Test
 public void testIterableNoNullElements() {
 List<Integer> list = new ArrayList<>();
 list.add(1);
 list.add(null);
 Validate.noNullElements(list, "Expected collection without null elements but contained null", new Object[0]);
 }

 @Test
 public void testValidIndexArray() {
 byte[] bytes = new byte[10];
 Validate.validIndex(bytes, 0, "Expected valid array index but was invalid: %d", new Object[] { -1 });
 Validate.validIndex(bytes, 9, "Expected valid array index but was invalid: %d", new Object[] { 10 });
 Validate.validIndex(bytes, 9, "Expected valid array index but was invalid", new Object[0]);
 }

 @Test
 public void testValidIndexCharSequence() {
 StringBuilder sb = new StringBuilder("hello");
 Validate.validIndex(sb, 0, "Expected valid character sequence index but was invalid: %d", new Object[] { 6 });
 Validate.validIndex(sb, 5, "Expected valid character sequence index but was invalid: %d", new Object[] { 11 });
 Validate.validIndex(sb, 5, "Expected valid character sequence index but was invalid", new Object[0]);
 }

 @Test
 public void testValidIndexCollection() {
 List<Integer> list = new ArrayList<>();
 list.add(1);
 list.add(2);
 Validate.validIndex(list, 0, "Expected valid collection index but was invalid: %d", new Object[] { 2 });
 Validate.validIndex(list, 1, "Expected valid collection index but was invalid: %d", new Object[] { -1 });
 Validate.validIndex(list, 1, "Expected valid collection index but was invalid", new Object[0]);
 }

 @Test
 public void testValidState() {
 boolean test = false;
 Validate.validState(test);
 Validate.validState(test, "Expected valid state but was invalid");
 }

 @Test
 public void testMatchesPattern() {
 String input = "foo";
 String pattern = "foo";
 Validate.matchesPattern(input, pattern);
 Validate.matchesPattern(input, pattern, "Expected input to match pattern but did not", new Object[0]);
 String input2 = "foo bar";
 String pattern2 = "foo";
 Validate.assertThrows(IllegalArgumentException.class, () -> Validate.matchesPattern(input2, pattern2));
 }

 @Test
 public void testInclusiveBetween() {
 int i = 5;
 Validate.inclusiveBetween(2, 5, i);
 Validate.inclusiveBetween(2, 5, i, "Expected value to be inclusive between %s and %s but was %s", new Object[] { 2, 5, i });
 int j = 10;
 Validate.inclusiveBetween(-1, 10, j);
 Validate.inclusiveBetween(-1, 10, j, "Expected value to be inclusive between %s and %s but was %s", new Object[] { -1, 10, j });
 int k = 5;
 Validate.inclusiveBetween(5, 5, k);
 Validate.inclusiveBetween(5, 5, k, "Expected value to be inclusive between %s and %s but was %s", new Object[] { 5, 5, k });
 }

 @Test
 public void testExclusiveBetween() {
 int i = 5;
 Validate.exclusiveBetween(4, 6, i);
 Validate.exclusiveBetween(4, 6, i, "Expected value to be exclusive between %s and %s but was %s", new Object[] { 4, 6, i });
 int j = 10;
 Validate.exclusiveBetween(9, 11, j);
 Validate.exclusiveBetween(9, 11, j, "Expected value to be exclusive between %s and %s but was %s", new Object[] { 9, 11, j });
 int k = 5;
 Validate.exclusiveBetween(5, 5, k);
 Validate.exclusiveBetween(5, 5, k, "Expected value to be exclusive between %s and %s but was %s", new Object[] { 5, 5, k });
 }

 @Test
 public void testIsInstanceOf() {
 String str = "test";
 Validate.isInstanceOf(String.class, str);
 Validate.isInstanceOf(String.class, str, "Expected %s but was %s", new Object[] { "String", "Object" });
 Object obj = new Object();
 Validate.isInstanceOf(String.class, obj);
 Validate.assertThrowsExactly(IllegalArgumentException.class, () -> Validate.isInstanceOf(String.class, obj));
 }

 @Test
 public void testIsAssignableFrom() {
 String str = "test";
 Validate.isAssignableFrom(String.class, String.class);
 Validate.isAssignableFrom(String.class, Object.class);
 Validate.isAssignableFrom(String.class, Integer.class);
 Validate.assertThrowsExactly(IllegalArgumentException.class, () -> Validate.isAssignableFrom(Integer.class, String.class));
 }
}