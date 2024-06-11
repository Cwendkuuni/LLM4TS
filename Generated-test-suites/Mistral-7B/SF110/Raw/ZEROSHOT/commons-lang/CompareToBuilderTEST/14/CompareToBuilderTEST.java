package org.apache.commons.lang3;

import org.apache.commons.lang3.builder.CompareToBuilder;
import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class CompareToBuilderTest {

	@Test
	public void testReflectionCompare() {
		Object obj1 = new Object() {
			int a = 1;
			int b = 2;
		};
		Object obj2 = new Object() {
			int a = 2;
			int b = 1;
		};
		int result = CompareToBuilder.reflectionCompare(obj1, obj2);
		assertEquals(-1, result);

		Object obj3 = new Object() {
			int a = 1;
			int b = 1;
		};
		int result2 = CompareToBuilder.reflectionCompare(obj1, obj3);
		assertEquals(0, result2);

		Object obj4 = null;
		try {
			int result3 = CompareToBuilder.reflectionCompare(obj1, obj4);
			fail("Expected NullPointerException");
		} catch (NullPointerException e) {
			// expected
		}
	}

	@Test
	public void testReflectionCompareWithExcludeFields() {
		Object obj1 = new Object() {
			int a = 1;
			int b = 2;
			String c = "test";
		};
		Object obj2 = new Object() {
			int a = 2;
			int b = 1;
			String c = "test2";
		};
		int result = CompareToBuilder.reflectionCompare(obj1, obj2, Arrays.asList("c"));
		assertEquals(0, result);
	}

	@Test
	public void testAppend() {
		CompareToBuilder builder = new CompareToBuilder();
		builder.append(1, 2);
		assertEquals(-1, builder.toComparison());

		builder = new CompareToBuilder();
		builder.append(2, 1);
		assertEquals(1, builder.toComparison());

		builder = new CompareToBuilder();
		builder.append(1.0, 1.0);
		assertEquals(0, builder.toComparison());

		builder = new CompareToBuilder();
		builder.append(true, false);
		assertEquals(-1, builder.toComparison());

		builder = new CompareToBuilder();
		int[] arr1 = { 1, 2 };
		int[] arr2 = { 2, 1 };
		builder.append(arr1, arr2);
		assertEquals(-1, builder.toComparison());
	}

	@Test
	public void testAppendSuper() {
		CompareToBuilder builder = new CompareToBuilder();
		builder.appendSuper(1);
		assertEquals(1, builder.toComparison());
	}

	@Test
	public void testAppendWithComparator() {
		CompareToBuilder builder = new CompareToBuilder();
		String str1 = "test1";
		String str2 = "test2";
		String[] arr = { str1, str2 };
		Comparator<String> comparator = new Comparator<String>() {
			@Override
			public int compare(String s1, String s2) {
				return s1.length() - s2.length();
			}
		};
		builder.append(arr, arr, comparator);
		assertEquals(-1, builder.toComparison());
	}

	@Test
	public void testAppendArrayTypes() {
		CompareToBuilder builder = new CompareToBuilder();
		long[] longArr1 = { 1, 2 };
		long[] longArr2 = { 2, 1 };
		builder.append(longArr1, longArr2);
		assertEquals(-1, builder.toComparison());

		int[] intArr1 = { 1, 2 };
		int[] intArr2 = { 2, 1 };
		builder.append(intArr1, intArr2);
		assertEquals(-1, builder.toComparison());

		short[] shortArr1 = { 1, 2 };
		short[] shortArr2 = { 2, 1 };
		builder.append(shortArr1, shortArr2);
		assertEquals(-1, builder.toComparison());

		char[] charArr1 = { 'a', 'b' };
		char[] charArr2 = { 'b', 'a' };
		builder.append(charArr1, charArr2);
		assertEquals(-1, builder.toComparison());

		byte[] byteArr1 = { 1, 2 };
		byte[] byteArr2 = { 2, 1 };
		builder.append(byteArr1, byteArr2);
		assertEquals(-1, builder.toComparison());

		double[] doubleArr1 = { 1.0, 2.0 };
		double[] doubleArr2 = { 2.0, 1.0 };
		builder.append(doubleArr1, doubleArr2);
		assertEquals(-1, builder.toComparison());

		float[] floatArr1 = { 1.0f, 2.0f };
		float[] floatArr2 = { 2.0f, 1.0f };
		builder.append(floatArr1, floatArr2);
		assertEquals(-1, builder.toComparison());

		boolean[] booleanArr1 = { true, false };
		boolean[] booleanArr2 = { false, true };
		builder.append(booleanArr1, booleanArr2);
		assertEquals(-1, builder.toComparison());
	}

	@Test
	public void testAppendArrayWithComparator() {
		CompareToBuilder builder = new CompareToBuilder();
		Integer[] intArr1 = { 1, 2 };
		Integer[] intArr2 = { 2, 1 };
		Comparator<Integer> comparator = new Comparator<Integer>() {
			@Override
			public int compare(Integer i1, Integer i2) {
				return i1.intValue() - i2.intValue();
			}
		};
		builder.append(intArr1, intArr2, comparator);
		assertEquals(-1, builder.toComparison());
	}
}