package com.hf.sfm.crypt.test;

import static org.junit.Assert.*;

import java.nio.charset.StandardCharsets;

import org.junit.Test;

import com.hf.sfm.crypt.Base64;

public class Base64Test {

	// Test case for altBase64ToByteArray method
	@Test
	public void testAltBase64ToByteArray_ValidInput() {
		String input = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
		byte[] expectedOutput = Base64._$23169;
		byte[] output = Base64.altBase64ToByteArray(input);
		assertArrayEquals(expectedOutput, output);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAltBase64ToByteArray_InvalidInputLength() {
		String input = "AB";
		Base64.altBase64ToByteArray(input);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAltBase64ToByteArray_InvalidInput() {
		String input = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/AB";
		Base64.altBase64ToByteArray(input);
	}

	// Test case for altBase64ToByteArray method with padding
	@Test
	public void testAltBase64ToByteArray_WithPadding() {
		String input = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
		byte[] expectedOutput = Base64._$23169;
		byte[] output = Base64.altBase64ToByteArray(input);
		assertArrayEquals(expectedOutput, output);
	}

	// Test case for base64ToByteArray method
	@Test
	public void testBase64ToByteArray_ValidInput() {
		String input = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
		byte[] expectedOutput = Base64._$23169;
		byte[] output = Base64.base64ToByteArray(input);
		assertArrayEquals(expectedOutput, output);
	}

	// Test case for base64ToByteArray method with padding
	@Test
	public void testBase64ToByteArray_WithPadding() {
		String input = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
		byte[] expectedOutput = Base64._$23169;
		byte[] output = Base64.base64ToByteArray(input);
		assertArrayEquals(expectedOutput, output);
	}

	// Test case for base64ToByteArray method with invalid input
	@Test(expected = IllegalArgumentException.class)
	public void testBase64ToByteArray_InvalidInput() {
		String input = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/AB";
		Base64.base64ToByteArray(input);
	}

	// Test case for byteArrayToAltBase64 method
	@Test
	public void testByteArrayToAltBase64_ValidInput() {
		byte[] input = Base64._$23169;
		String expectedOutput = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
		String output = new String(Base64.byteArrayToAltBase64(input), StandardCharsets.UTF_8);
		assertEquals(expectedOutput, output);
	}

	// Test case for byteArrayToAltBase64 method with padding
	@Test
	public void testByteArrayToAltBase64_WithPadding() {
		byte[] input = Base64._$23169;
		byte[] padding = new byte[] { 0, 0, 0 };
		System.arraycopy(input, input.length - 3, padding, 0, 3);
		input = new byte[input.length + 3];
		System.arraycopy(input, 0, padding, 0, 3);
		System.arraycopy(input, 0, input, 3, input.length - 3);
		String expectedOutput = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
		String output = new String(Base64.byteArrayToAltBase64(input), StandardCharsets.UTF_8);
		assertEquals(expectedOutput, output);
	}

	// Test case for byteArrayToBase64 method
	@Test
	public void testByteArrayToBase64_ValidInput() {
		byte[] input = Base64._$23169;
		String expectedOutput = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
		String output = new String(Base64.byteArrayToBase64(input), StandardCharsets.UTF_8);
		assertEquals(expectedOutput, output);
	}

	// Test case for byteArrayToBase64 method with padding
	@Test
	public void testByteArrayToBase64_WithPadding() {
		byte[] input = Base64._$23169;
		byte[] padding = new byte[] { 0, 0, 0 };
		System.arraycopy(input, input.length - 3, padding, 0, 3);
		input = new byte[input.length + 3];
		System.arraycopy(input, 0, padding, 0, 3);
		System.arraycopy(input, 0, input, 3, input.length - 3);
		String expectedOutput = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
		String output = new String(Base64.byteArrayToBase64(input), StandardCharsets.UTF_8);
		assertEquals(expectedOutput, output);
	}

	// Test case for byteArrayToBase64 method with invalid input
	@Test(expected = IllegalArgumentException.class)
	public void testByteArrayToBase64_InvalidInput() {
		byte[] input = new byte[] { 0, 1, 2 };
		Base64.byteArrayToBase64(input);
	}

	// Test case for _$23183 method with valid input
	@Test
	public void test_23183_ValidInput() {
		for (char c = 0; c <= 127; c++) {
			int expectedOutput = (byte) c;
			int output = Base64._$23183(c, Base64._$23169);
			assertEquals(expectedOutput, output);
		}
	}

	// Test case for _$23183 method with invalid input
	@Test(expected = IllegalArgumentException.class)
	public void test_23183_InvalidInput() {
		char c = (char) 128;
		Base64._$23183(c, Base64._$23169);
	}
}