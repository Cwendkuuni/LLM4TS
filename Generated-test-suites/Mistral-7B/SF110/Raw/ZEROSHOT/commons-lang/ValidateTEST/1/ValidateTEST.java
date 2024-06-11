package org.apache.commons.lang3;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.junit.Test;

public class ValidateTest {

	@Test
	public void testIsTrueWithMessageAndLongArgument() {
		Validate.isTrue(true, "The value {0} is not true", 42);
		try {
			Validate.isTrue(false, "The value {0} is not true", 42);
			fail();
		} catch (IllegalArgumentException e) {
			assertTrue(e.getMessage().contains("The value 42 is not true"));
		}
	}

	@Test
	public void testIsTrueWithMessageAndDoubleArgument() {
		Validate.isTrue(true, "The value {0} is not true", 3.14);
		try {
			Validate.isTrue(false, "The value {0} is not true", 3.14);
			fail();
		} catch (IllegalArgumentException e) {
			assertTrue(e.getMessage().contains("The value 3.14 is not true"));
		}
	}

	@Test
	public void testIsTrueWithMessageAndObjectsArrayArgument() {
		Validate.isTrue(true, "The value {0} is not true", "foo", "bar");
		try {
			Validate.isTrue(false, "The value {0} is not true", "foo", "bar");
			fail();
		} catch (IllegalArgumentException e) {
			assertTrue(e.getMessage().contains("The value bar is not true"));
		}
	}

	@Test
	public void testIsTrueWithoutMessage() {
		Validate.isTrue(true);
		try {
			Validate.isTrue(false);
			fail();
		} catch (IllegalArgumentException e) {
			// expected
		}
	}

	@Test
	public void testNotNull() {
		String str = new String("foo");
		Validate.notNull(str);

		try {
			Validate.notNull(null);
			fail();
		} catch (NullPointerException e) {
			// expected
		}
	}

	@Test
	public void testNotEmptyArray() {
		int[] arr = new int[] { 1, 2, 3 };
		Validate.notEmpty(arr);

		try {
			Validate.notEmpty(new int[0]);
			fail();
		} catch (IllegalArgumentException e) {
			// expected
		}
	}

	@Test
	public void testNotEmptyCollection() {
		List<String> list = new ArrayList<>();
		list.add("foo");
		list.add("bar");
		Validate.notEmpty(list);

		try {
			Validate.notEmpty(Collections.emptyList());
			fail();
		} catch (IllegalArgumentException e) {
			// expected
		}
	}

	@Test
	public void testNotEmptyMap() {
		Map<String, Integer> map = new HashMap<>();
		map.put("foo", 1);
		map.put("bar", 2);
		Validate.notEmpty(map);

		try {
			Validate.notEmpty(Collections.emptyMap());
			fail();
		} catch (IllegalArgumentException e) {
			// expected
		}
	}

	@Test
	public void testNotBlankString() {
		String str = "foo";
		Validate.notBlank(str);

		try {
			Validate.notBlank(null);
			fail();
		} catch (NullPointerException e) {
			// expected
		}

		try {
			Validate.notBlank(new String(""));
			fail();
		} catch (IllegalArgumentException e) {
			// expected
		}
	}

	@Test
	public void testNoNullElementsArray() {
		String[] arr = new String[] { "foo", "bar" };
		Validate.noNullElements(arr);

		try {
			Validate.noNullElements(new String[0]);
			fail();
		} catch (IllegalArgumentException e) {
			// expected
		}

		try {
			Validate.noNullElements(new String[] { null });
			fail();
		} catch (IllegalArgumentException e) {
			// expected
		}
	}

	@Test
	public void testNoNullElementsCollection() {
		List<String> list = new ArrayList<>();
		list.add("foo");
		list.add("bar");
		Validate.noNullElements(list);

		try {
			Validate.noNullElements(Collections.emptyList());
			fail();
		} catch (IllegalArgumentException e) {
			// expected
		}

		try {
			Validate.noNullElements(Collections.singletonList(null));
			fail();
		} catch (IllegalArgumentException e) {
			// expected
		}
	}

	@Test
	public void testValidIndexArray() {
		int[] arr = new int[] { 1, 2, 3 };
		Validate.validIndex(arr, 1);

		try {
			Validate.validIndex(arr, -1);
			fail();
		} catch (IndexOutOfBoundsException e) {
			// expected
		}

		try {
			Validate.validIndex(arr, 3);
			fail();
		} catch (IndexOutOfBoundsException e) {
			// expected
		}
	}

	@Test
	public void testValidIndexCollection() {
		List<Integer> list = new ArrayList<>();
		list.add(1);
		list.add(2);
		list.add(3);
		Validate.validIndex(list, 1);

		try {
			Validate.validIndex(list, -1);
			fail();
		} catch (IndexOutOfBoundsException e) {
			// expected
		}

		try {
			Validate.validIndex(list, 3);
			fail();
		} catch (IndexOutOfBoundsException e) {
			// expected
		}
	}

	@Test
	public void testValidIndexString() {
		String str = "foo";
		Validate.validIndex(str, 2);

		try {
			Validate.validIndex(str, -1);
			fail();
		} catch (IndexOutOfBoundsException e) {
			// expected
		}

		try {
			Validate.validIndex(str, 3);
			fail();
		} catch (IndexOutOfBoundsException e) {
			// expected
		}
	}

	@Test
	public void testValidState() {
		boolean state = true;
		Validate.validState(state);

		try {
			Validate.validState(false);
			fail();
		} catch (IllegalStateException e) {
			// expected
		}
	}

	@Test
	public void testMatchesPattern() {
		String input = "foo[bar]";
		String pattern = "[fo]o[ba]r";
		Validate.matchesPattern(input, pattern);

		try {
			String input2 = null;
			String pattern2 = "[fo]o[ba]r";
			Validate.matchesPattern(input2, pattern2);
			fail();
		} catch (IllegalArgumentException e) {
			// expected
		}

		try {
			String input3 = "foo[bar]";
			String pattern3 = "[ba]r[fo]o";
			Validate.matchesPattern(input3, pattern3);
			fail();
		} catch (IllegalArgumentException e) {
			// expected
		}
	}

	@Test
	public void testInclusiveBetweenCharSequence() {
		String start = "foo";
		String end = "bar";
		String value = "foo";
		Validate.inclusiveBetween(start, end, value);

		try {
			String start2 = "foo";
			String end2 = "bar";
			String value2 = "baz";
			Validate.inclusiveBetween(start2, end2, value2);
			fail();
		} catch (IllegalArgumentException e) {
			// expected
		}

		try {
			String start3 = "foo";
			String end3 = "bar";
			String value3 = "foo";
			Validate.inclusiveBetween(start3, end3, null);
			fail();
		} catch (NullPointerException e) {
			// expected
		}
	}

	@Test
	public void testInclusiveBetweenLong() {
		long start = 1;
		long end = 3;
		long value = 2;
		Validate.inclusiveBetween(start, end, value);

		try {
			long start2 = 1;
			long end2 = 3;
			long value2 = 0;
			Validate.inclusiveBetween(start2, end2, value2);
			fail();
		} catch (IllegalArgumentException e) {
			// expected
		}

		try {
			long start3 = 1;
			long end3 = 3;
			long value3 = 4;
			Validate.inclusiveBetween(start3, end3, value3);
			fail();
		} catch (IllegalArgumentException e) {
			// expected
		}
	}

	@Test
	public void testInclusiveBetweenDouble() {
		double start = 1.0;
		double end = 3.0;
		double value = 2.0;
		Validate.inclusiveBetween(start, end, value);

		try {
			double start2 = 1.0;
			double end2 = 3.0;
			double value2 = 0.5;
			Validate.inclusiveBetween(start2, end2, value2);
			fail();
		} catch (IllegalArgumentException e) {
			// expected
		}

		try {
			double start3 = 1.0;
			double end3 = 3.0;
			double value3 = 4.5;
			Validate.inclusiveBetween(start3, end3, value3);
			fail();
		} catch (IllegalArgumentException e) {
			// expected
		}
	}

	@Test
	public void testExclusiveBetweenCharSequence() {
		String start = "foo";
		String end = "bar";
		String value = "foo";
		Validate.exclusiveBetween(start, end, value);

		try {
			String start2 = "foo";
			String end2 = "bar";
			String value2 = "baz";
			Validate.exclusiveBetween(start2, end2, value2);
			fail();
		} catch (IllegalArgumentException e) {
			// expected
		}

		try {
			String start3 = "foo";
			String end3 = "bar";
			String value3 = "foo";
			Validate.exclusiveBetween(start3, end3, null);
			fail();
		} catch (NullPointerException e) {
			// expected
		}
	}

	@Test
	public void testExclusiveBetweenLong() {
		long start = 1;
		long end = 3;
		long value = 2;
		Validate.exclusiveBetween(start, end, value);

		try {
			long start2 = 1;
			long end2 = 3;
			long value2 = 0;
			Validate.exclusiveBetween(start2, end2, value2);
			fail();
		} catch (IllegalArgumentException e) {
			// expected
		}

		try {
			long start3 = 1;
			long end3 = 3;
			long value3 = 4;
			Validate.exclusiveBetween(start3, end3, value3);
			fail();
		} catch (IllegalArgumentException e) {
			// expected
		}
	}

	@Test
	public void testExclusiveBetweenDouble() {
		double start = 1.0;
		double end = 3.0;
		double value = 2.0;
		Validate.exclusiveBetween(start, end, value);

		try {
			double start2 = 1.0;
			double end2 = 3.0;
			double value2 = 0.5;
			Validate.exclusiveBetween(start2, end2, value2);
			fail();
		} catch (IllegalArgumentException e) {
			// expected
		}

		try {
			double start3 = 1.0;
			double end3 = 3.0;
			double value3 = 4.5;
			Validate.exclusiveBetween(start3, end3, value3);
			fail();
		} catch (IllegalArgumentException e) {
			// expected
		}
	}

	@Test
	public void testIsInstanceOf() {
		String str = "foo";
		Validate.isInstanceOf(String.class, str);

		try {
			Validate.isInstanceOf(String.class, null);
			fail();
		} catch (IllegalArgumentException e) {
			// expected
		}

		try {
			Validate.isInstanceOf(Integer.class, str);
			fail();
		} catch (IllegalArgumentException e) {
			// expected
		}
	}

	@Test
	public void testIsAssignableFrom() {
		String str = "foo";
		Validate.isAssignableFrom(String.class, String.class, str);

		try {
			Validate.isAssignableFrom(String.class, Integer.class, str);
			fail();
		} catch (IllegalArgumentException e) {
			// expected
		}
	}
}