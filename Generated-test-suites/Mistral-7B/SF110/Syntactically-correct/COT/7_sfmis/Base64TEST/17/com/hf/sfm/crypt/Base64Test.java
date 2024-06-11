package com.hf.sfm.crypt.test;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import com.hf.sfm.crypt.Base64;

public class Base64Test {

	@Test
	public void testAltBase64ToByteArray() {
		String input = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
		byte[] expectedOutput = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57 };
		byte[] output = Base64.altBase64ToByteArray(input);
		assertArrayEquals(expectedOutput, output);
	}

	@Test
	public void testAltBase64ToByteArrayWithInvalidLength() {
		String input = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/AB";
		try {
			Base64.altBase64ToByteArray(input);
			fail("Expected IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			assertEquals("String length must be a multiple of four.", e.getMessage());
		}
	}

	@Test
	public void testAltBase64ToByteArrayWithInvalidCharacter() {
		String input = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/Z";
		try {
			Base64.altBase64ToByteArray(input);
			fail("Expected IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			assertEquals("Illegal character Z", e.getMessage());
		}
	}

	@Test
	public void testBase64ToByteArray() {
		String input = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
		byte[] expectedOutput = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57 };
		byte[] output = Base64.base64ToByteArray(input);
		assertArrayEquals(expectedOutput, output);
	}

	@Test
	public void testBase64ToByteArrayWithInvalidLength() {
		String input = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/AB";
		try {
			Base64.base64ToByteArray(input);
			fail("Expected IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			assertEquals("String length must be a multiple of four.", e.getMessage());
		}
	}

	@Test
	public void testByteArrayToAltBase64() {
		byte[] input = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57 };
		String expectedOutput = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
		String output = Base64.byteArrayToAltBase64(input);
		assertEquals(expectedOutput, output);
	}

	@Test
	public void testByteArrayToBase64() {
		byte[] input = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57 };
		String expectedOutput = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
		String output = Base64.byteArrayToBase64(input);
		assertEquals(expectedOutput, output);
	}

	@Test
	public void testByteArrayToBase64WithPadding() {
		byte[] input = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		String expectedOutput = "MTIzNDU2Nzg5QGdtYWlsLmNvbS5hbWU9InRyYXN5IHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIu";
		String output = Base64.byteArrayToBase64(input);
		assertEquals(expectedOutput, output);
	}

	@Test
	public void testByteArrayToBase64WithNoPadding() {
		byte[] input = { 0, 1, 2, 3, 4, 5, 6, 7 };
		String expectedOutput = "MTIzNDU2Nzgx";
		String output = Base64.byteArrayToBase64(input);
		assertEquals(expectedOutput, output);
	}
}