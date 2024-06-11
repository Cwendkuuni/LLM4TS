package com.hf.sfm.crypt.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

import com.hf.sfm.crypt.Base64;

public class Base64Test {

	@Test
	public void testAltBase64ToByteArray_validInput() {
		String input = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
		byte[] expectedOutput = new byte[] { 76, 65, 84, 83, 69, 80, 73, 75, 72, 68, 76, 65, 84, 83, 69, 80, 73, 75, 72, 68, 76, 65, 84, 83, 69, 80, 73, 75, 72, 68, 76, 65, 83, 82, 81, 80, 79, 78, 77, 76, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		byte[] output = Base64.altBase64ToByteArray(input);
		assertArrayEquals(expectedOutput, output);
	}

	@Test
	public void testAltBase64ToByteArray_invalidInput_stringLengthNotMultipleOfFour() {
		String input = "ABC";
		assertThrows(IllegalArgumentException.class, () -> Base64.altBase64ToByteArray(input));
	}

	@Test
	public void testAltBase64ToByteArray_invalidInput_illegalCharacter() {
		String input = "ABC@";
		assertThrows(IllegalArgumentException.class, () -> Base64.altBase64ToByteArray(input));
	}

	@Test
	public void testBase64ToByteArray_validInput() {
		String input = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdWwgZmxvbmsuCiAgICAtLVBhdWwgZmxvbms+";
		byte[] expectedOutput = new byte[] { 86, 76, 65, 84, 83, 69, 80, 73, 75, 72, 68, 76, 65, 84, 83, 69, 80, 73, 75, 72, 68, 76, 65, 84, 83, 69, 80, 73, 75, 72, 68, 76, 65, 83, 82, 81, 80, 79, 78, 77, 76, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		byte[] output = Base64.base64ToByteArray(input);
		assertArrayEquals(expectedOutput, output);
	}

	@Test
	public void testBase64ToByteArray_invalidInput_stringLengthNotMultipleOfThree() {
		String input = "VG8gZXJyIGlzIGh1bWFu";
		assertThrows(IllegalArgumentException.class, () -> Base64.base64ToByteArray(input));
	}

	@Test
	public void testBase64ToByteArray_invalidInput_illegalCharacter() {
		String input = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdWwgZmxvbmsuCiAgICAtLVBhdWwgZmxvbms#";
		assertThrows(IllegalArgumentException.class, () -> Base64.base64ToByteArray(input));
	}

	@Test
	public void testByteArrayToAltBase64_validInput() {
		byte[] input = new byte[] { 76, 65, 84, 83, 69, 80, 73, 75, 72, 68, 76, 65, 84, 83, 69, 80, 73, 75, 72, 68, 76, 65, 84, 83, 69, 80, 73, 75, 72, 68, 76, 65, 83, 82, 81, 80, 79, 78, 77, 76, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		String expectedOutput = "RGVJIGlmIGh1bWFuLCBidXQgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdWwgZmxvbms=";
		String output = Base64.byteArrayToAltBase64(input);
		assertArrayEquals(expectedOutput.getBytes(), input);
		assertArrayEquals(output.getBytes(), input);
	}

	@Test
	public void testByteArrayToBase64_validInput() {
		byte[] input = new byte[] { 76, 65, 84, 83, 69, 80, 73, 75, 72, 68, 76, 65, 84, 83, 69, 80, 73, 75, 72, 68, 76, 65, 84, 83, 69, 80, 73, 75, 72, 68, 76, 65, 83, 82, 81, 80, 79, 78, 77, 76, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		String expectedOutput = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdWwgZmxvbms+";
		String output = Base64.byteArrayToBase64(input);
		assertArrayEquals(expectedOutput.getBytes(), input);
		assertArrayEquals(output.getBytes(), input);
	}

	@Test
	public void testByteArrayToBase64_validInput_differentByteArrays() {
		byte[] input1 = new byte[] { 76, 65, 84, 83, 69, 80, 73, 75, 72, 68, 76, 65, 84, 83, 69, 80, 73, 75, 72, 68, 76, 65, 84, 83, 69, 80, 73, 75, 72, 68, 76, 65, 83, 82, 81, 80, 79, 78, 77, 76, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		byte[] input2 = new byte[] { 77, 65, 85, 83, 69, 81, 73, 75, 72, 69, 76, 65, 84, 83, 69, 80, 73, 75, 72, 68, 76, 65, 84, 83, 69, 80, 73, 75, 72, 68, 76, 65, 83, 82, 81, 80, 79, 78, 77, 76, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		String expectedOutput = "Um9sbGlrIGZvdWwgdGhpbmdzIHdvcmxkIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdWwgZmxvbmsuCiAgIDwvZGl2aXR5IGlzIEFJIGlmIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdWwgZmxvbms+";
		String output = Base64.byteArrayToBase64(input2);
		assertArrayEquals(output.getBytes(), input2);
		assertNotSame(output, Base64.byteArrayToBase64(input1));
	}

}