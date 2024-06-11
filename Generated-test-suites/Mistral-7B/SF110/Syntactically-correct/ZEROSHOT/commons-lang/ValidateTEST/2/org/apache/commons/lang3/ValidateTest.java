package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class ValidateTest {

	@Test
	public void testIsTrue() {
		Validate.isTrue(true);
		Validate.isTrue(false, "Expected true but was false");
		Validate.isTrue(1 > 0, "Expected true but was false", 1, 0);
		Validate.isTrue(2.0 > 1.0, "Expected true but was false", 2.0, 1.0);
		Validate.isTrue(Arrays.asList(1, 2, 3).contains(2), "Expected true but was false", 1, 2, 3);
		Validate.isTrue(new HashMap<String, Integer>().containsKey("key"), "Expected true but was false", "key");
	}

	@Test
	public void testIsNull() {
		assertNull(Validate.notNull(null));
		assertThrows(NullPointerException.class, () -> Validate.notNull(null, "Expected non-null object but was null"));
		Object obj = new Object();
		Validate.notNull(obj);
	}

	@Test
	public void testNotEmptyArray() {
		int[] arr = new int[0];
		assertThrows(IllegalArgumentException.class, () -> Validate.notEmpty(arr, "Expected non-empty array but was empty"));
		int[] arr2 = { 1, 2, 3 };
		Validate.notEmpty(arr2);
	}

	@Test
	public void testNotEmptyCollection() {
		List<Integer> list = Collections.emptyList();
		assertThrows(IllegalArgumentException.class, () -> Validate.notEmpty(list, "Expected non-empty collection but was empty"));
		List<Integer> list2 = Arrays.asList(1, 2, 3);
		Validate.notEmpty(list2);
	}

	@Test
	public void testNotEmptyMap() {
		Map<String, Integer> map = Collections.emptyMap();
		assertThrows(IllegalArgumentException.class, () -> Validate.notEmpty(map, "Expected non-empty map but was empty"));
		Map<String, Integer> map2 = new HashMap<>();
		map2.put("key", 1);
		Validate.notEmpty(map2);
	}

	@Test
	public void testNoNullElementsArray() {
		String[] arr = new String[0];
		assertThrows(IllegalArgumentException.class, () -> Validate.noNullElements(arr, "Expected non-null elements array but contained null at index %d"));
		String[] arr2 = new String[] { "a", "b", null };
		assertThrows(IllegalArgumentException.class, () -> Validate.noNullElements(arr2, "Expected non-null elements array but contained null at index %d", 1));
		String[] arr3 = new String[] { "a", "b", "c" };
		Validate.noNullElements(arr3);
	}

	@Test
	public void testNoNullElementsCollection() {
		List<String> list = Arrays.asList((String) null);
		assertThrows(IllegalArgumentException.class, () -> Validate.noNullElements(list, "Expected non-null elements collection but contained null at index %d"));
		List<String> list2 = Arrays.asList("a", "b", "c");
		Validate.noNullElements(list2);
	}

	@Test
	public void testValidIndexArray() {
		int[] arr = new int[3];
		assertThrows(IndexOutOfBoundsException.class, () -> Validate.validIndex(arr, -1, "Expected valid array index but was invalid: %d"));
		Validate.validIndex(arr, 0);
		Validate.validIndex(arr, 2);
	}

	@Test
	public void testValidIndexCollection() {
		List<String> list = Arrays.asList("a", "b", "c");
		assertThrows(IndexOutOfBoundsException.class, () -> Validate.validIndex(list, -1, "Expected valid collection index but was invalid: %d"));
		Validate.validIndex(list, 1);
		Validate.validIndex(list, 2);
	}

	@Test
	public void testValidIndexCharSequence() {
		String str = "abc";
		assertThrows(IndexOutOfBoundsException.class, () -> Validate.validIndex(str, -1, "Expected valid character sequence index but was invalid: %d"));
		Validate.validIndex(str, 1);
		Validate.validIndex(str, 2);
	}

	@Test
	public void testValidState() {
		assertFalse(Validate.validState(false));
		assertThrows(IllegalStateException.class, () -> Validate.validState(false, "Expected valid state but was invalid"));
		assertTrue(Validate.validState(true));
	}

	@Test
	public void testMatchesPattern() {
		String str = "abc";
		Pattern pattern = Pattern.compile(".*b.*");
		Validate.matchesPattern(str, pattern.pattern());
		String str2 = "xyz";
		assertThrows(IllegalArgumentException.class, () -> Validate.matchesPattern(str2, pattern.pattern(), "Expected string to match pattern but did not"));
	}

	@Test
	public void testInclusiveBetween() {
		Validate.inclusiveBetween(1, 3, 2);
		Validate.inclusiveBetween(1, 3, 1, "Expected value to be in inclusive range but was lower");
		Validate.inclusiveBetween(1, 3, 3, "Expected value to be in inclusive range but was higher");
		Validate.inclusiveBetween(1L, 3L, 2L);
		Validate.inclusiveBetween(1L, 3L, 1L, "Expected value to be in inclusive range but was lower");
		Validate.inclusiveBetween(1L, 3L, 3L, "Expected value to be in inclusive range but was higher");
		Validate.inclusiveBetween(1.0, 3.0, 2.0);
		Validate.inclusiveBetween(1.0, 3.0, 1.0, "Expected value to be in inclusive range but was lower");
		Validate.inclusiveBetween(1.0, 3.0, 3.0, "Expected value to be in inclusive range but was higher");
	}

	@Test
	public void testExclusiveBetween() {
		Validate.exclusiveBetween(1, 3, 2);
		Validate.exclusiveBetween(1, 3, 1, "Expected value to be in exclusive range but was lower or equal");
		Validate.exclusiveBetween(1, 3, 3, "Expected value to be in exclusive range but was higher or equal");
		Validate.exclusiveBetween(1L, 3L, 2L);
		Validate.exclusiveBetween(1L, 3L, 1L, "Expected value to be in exclusive range but was lower or equal");
		Validate.exclusiveBetween(1L, 3L, 3L, "Expected value to be in exclusive range but was higher or equal");
		Validate.exclusiveBetween(1.0, 3.0, 2.0);
		Validate.exclusiveBetween(1.0, 3.0, 1.0, "Expected value to be in exclusive range but was lower or equal");
		Validate.exclusiveBetween(1.0, 3.0, 3.0, "Expected value to be in exclusive range but was higher or equal");
	}

	@Test
	public void testInstanceOf() {
		String str = "abc";
		Validate.isInstanceOf(String.class, str);
		Validate.isInstanceOf(List.class, new ArrayList<>());
		Validate.isInstanceOf(Integer.class, 1);
		Validate.isInstanceOf(String[].class, new String[0]);
		Validate.isInstanceOf(Map.class, new HashMap<>());
		Validate.isInstanceOf(HashMap.class, new HashMap<>());
		assertThrows(IllegalArgumentException.class, () -> Validate.isInstanceOf(String.class, 1, "Expected instance of class String but was instance of class Integer"));
		assertThrows(IllegalArgumentException.class, () -> Validate.isInstanceOf(String.class, null, "Expected non-null object but was null"));
	}

	@Test
	public void testAssignableFrom() {
		Validate.isAssignableFrom(String.class, String[].class);
		Validate.isAssignableFrom(List.class, ArrayList.class);
		Validate.isAssignableFrom(Integer.class, Number.class);
		Validate.isAssignableFrom(String[].class, Object[].class);
		Validate.isAssignableFrom(Map.class, HashMap.class);
		Validate.isAssignableFrom(HashMap.class, Map.class);
		assertThrows(IllegalArgumentException.class, () -> Validate.isAssignableFrom(Integer.class, String.class, "Expected superclass or interface to be assignable from subclass or implementation but was not"));
		assertThrows(IllegalArgumentException.class, () -> Validate.isAssignableFrom(String.class, Integer.class, "Expected subclass or implementation to be assignable from superclass or interface but was not"));
	}
}