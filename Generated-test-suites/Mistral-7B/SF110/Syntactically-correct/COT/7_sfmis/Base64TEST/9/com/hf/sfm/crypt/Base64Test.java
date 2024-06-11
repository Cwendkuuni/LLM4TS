package com.hf.sfm.crypt.test;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import com.hf.sfm.crypt.Base64;

public class Base64Test {

	@Test
	public void testAltBase64ToByteArray_validInput() {
		String input = "aGVsbG8gdG8gdGhpbXBsZS4=";
		byte[] expectedOutput = { 72, 101, 118, 101, 114, 103, 114, 117, 99, 114, 105, 112, 116, 108, 101, 115, 47, 103, 111, 111, 118, 101, 114, 103, 114, 101, 116, 117, 108, 107, 99, 114, 105, 112, 116, 108, 101, 115 };
		byte[] output = Base64.altBase64ToByteArray(input);
		assertArrayEquals(expectedOutput, output);
	}

	@Test
	public void testAltBase64ToByteArray_invalidInputLengthMultipleOfFour() {
		String input = "aGVsbG8gdG8gdGhpbXBsZS4=";
		byte[] output = Base64.altBase64ToByteArray(input);
		assertEquals(output.length, 24);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAltBase64ToByteArray_invalidInputLengthNotMultipleOfFour() {
		String input = "aGVsbG8gdG8gdGhpbXBsZS4=";
		String invalidInput = input.substring(0, input.length() - 1);
		Base64.altBase64ToByteArray(invalidInput);
	}

	@Test
	public void testAltBase64ToByteArray_invalidCharacter() {
		String input = "aGVsbG8gdG8gdGhpbXBsZS%";
		try {
			Base64.altBase64ToByteArray(input);
			fail("Expected IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			// Expected
		}
	}

	@Test
	public void testAltBase64ToByteArray_emptyInput() {
		String input = "";
		byte[] output = Base64.altBase64ToByteArray(input);
		assertArrayEquals(new byte[0], output);
	}

	@Test
	public void testBase64ToByteArray_validInput() {
		String input = "SGVsbG8sIFdvcmxkIQ==";
		byte[] expectedOutput = { 83, 101, 80, 115, 115, 116, 105, 110, 103, 32, 73, 110, 118, 101, 32, 68, 101, 114, 105, 112, 116, 115, 32, 110, 111, 116, 108, 101, 115, 116 };
		byte[] output = Base64.base64ToByteArray(input);
		assertArrayEquals(expectedOutput, output);
	}

	@Test
	public void testBase64ToByteArray_invalidCharacter() {
		String input = "SGVsbG8sIFdvcmxkIQ%";
		try {
			Base64.base64ToByteArray(input);
			fail("Expected IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			// Expected
		}
	}

	@Test
	public void testBase64ToByteArray_emptyInput() {
		String input = "";
		byte[] output = Base64.base64ToByteArray(input);
		assertArrayEquals(new byte[0], output);
	}

	@Test
	public void testByteArrayToAltBase64_validInput() {
		byte[] input = { 72, 101, 118, 101, 114, 103, 114, 117, 99, 114, 105, 112, 116, 108, 101, 115, 47, 103, 111, 111, 118, 101, 114, 103, 114, 101, 116, 117, 108, 107, 99, 114, 105, 112, 116, 108, 101, 115 };
		String expectedOutput = "aGVsbG8gdG8gdGhpbXBsZS4=";
		String output = Base64.byteArrayToAltBase64(input);
		assertEquals(expectedOutput, output);
	}

	@Test
	public void testByteArrayToBase64_validInput() {
		byte[] input = { 83, 101, 80, 115, 115, 116, 105, 110, 103, 32, 73, 110, 118, 101, 32, 68, 101, 114, 105, 112, 116, 115, 32, 110, 111, 116, 108, 101, 115, 116 };
		String expectedOutput = "SGVsbG8sIFdvcmxkIQ==";
		String output = Base64.byteArrayToBase64(input);
		assertEquals(expectedOutput, output);
	}

	@Test
	public void testByteArrayToBase64_emptyInput() {
		byte[] input = new byte[0];
		String output = Base64.byteArrayToBase64(input);
		assertEquals("", output);
	}

}