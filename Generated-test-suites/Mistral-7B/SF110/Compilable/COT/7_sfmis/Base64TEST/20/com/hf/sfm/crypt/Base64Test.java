package com.hf.sfm.crypt.test;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import com.hf.sfm.crypt.Base64;

public class Base64Test {

	@Test
	public void testAltBase64ToByteArray_validInput() {
		String input = "QWNjZXNzIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdGggdG8gYW5kIHlvdSBuZWVk";
		byte[] expected = { 71, 73, 73, 73, 68, 83, 67, 82, 80, 73, 68, 81, 69, 72, 83, 82, 79, 70, 75, 72, 69, 82, 84, 64, 81, 72, 81, 63, 83, 74, 83, 83, 81, 83, 72, 76, 82, 83, 64, 83, 65, 83, 69, 84, 66, 85, 66, 83, 82, 79, 65, 75, 81, 75, 74, 82, 84, 79, 71, 75, 80, 71, 84, 83, 83, 74, 75, 72 };
		byte[] result = Base64.altBase64ToByteArray(input);
		assertArrayEquals(expected, result);
	}

	@Test
	public void testAltBase64ToByteArray_invalidLength() {
		String input = "QWNjZXNzIGlzIGh1bWFuLCBidXQgdG8gYW5kIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdGggdG8gYW5kIHlvdSBuZWVkIHNwZWNpZXM=";
		try {
			Base64.altBase64ToByteArray(input);
			fail("IllegalArgumentException expected");
		} catch (IllegalArgumentException e) {
			// Expected
		}
	}

	@Test
	public void testAltBase64ToByteArray_invalidCharacter() {
		String input = "QWNjZXNzIGlzIGh1bWFuLCBidXQgdG8gYW5kIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdGggdG8gYW5kIHlvdSBuZWVkIHNwZWNpZXM%";
		try {
			Base64.altBase64ToByteArray(input);
			fail("IllegalArgumentException expected");
		} catch (IllegalArgumentException e) {
			// Expected
		}
	}

	@Test
	public void testBase64ToByteArray_validInput() {
		String input = "SGVsbG8sIFdvcmxkIQ==";
		byte[] expected = { 72, 73, 76, 65, 72, 73, 76, 65, 72, 73, 76, 65, 72, 73, 76, 65, 72, 73, 76, 65, 72, 73, 76 };
		byte[] result = Base64.base64ToByteArray(input);
		assertArrayEquals(expected, result);
	}

	@Test
	public void testBase64ToByteArray_invalidLength() {
		String input = "SGVsbG8sIFdvcmxkIQ===";
		try {
			Base64.base64ToByteArray(input);
			fail("IllegalArgumentException expected");
		} catch (IllegalArgumentException e) {
			// Expected
		}
	}

	@Test
	public void testBase64ToByteArray_invalidCharacter() {
		String input = "SGVsbG8sIFdvcmxkIQ==%";
		try {
			Base64.base64ToByteArray(input);
			fail("IllegalArgumentException expected");
		} catch (IllegalArgumentException e) {
			// Expected
		}
	}

	@Test
	public void testByteArrayToAltBase64_validInput() {
		byte[] input = { 71, 73, 73, 73, 68, 83, 67, 82, 80, 73, 68, 81, 69, 72, 83, 82, 79, 70, 75, 72, 69, 82, 84, 64, 81, 72, 81, 63, 83, 74, 83, 83, 81, 83, 72, 76, 82, 83, 64, 83, 65, 83, 69, 84, 66, 85, 66, 83, 82, 79, 65, 75, 81, 75, 74, 82, 84, 79, 71, 75, 80, 71, 84, 83, 83, 74, 75, 72 };
		String expected = "QWNjZXNzIGlzIGh1bWFuLCBidXQgdG8gYW5kIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdGggdG8gYW5kIHlvdSBuZWVk";
		String result = Base64.byteArrayToAltBase64(input);
		assertEquals(expected, result);
	}

	@Test
	public void testByteArrayToBase64_validInput() {
		byte[] input = { 72, 73, 76, 65, 72, 73, 76, 65, 72, 73, 76, 65, 72, 73, 76, 65, 72, 73, 76, 65, 72, 73, 76 };
		String expected = "SGVsbG8sIFdvcmxkIQ==";
		String result = Base64.byteArrayToBase64(input);
		assertEquals(expected, result);
	}

	@Test
	public void testByteArrayToBase64_invalidLength() {
		byte[] input = { 72, 73, 76, 65, 72, 73, 76, 65, 72, 73, 76, 65, 72, 73, 76, 65, 72, 73, 76 };
		try {
			Base64.byteArrayToBase64(Arrays.copyOfRange(input, 0, input.length - 1));
			fail("ArrayIndexOutOfBoundsException expected");
		} catch (ArrayIndexOutOfBoundsException e) {
			// Expected
		}
	}
}