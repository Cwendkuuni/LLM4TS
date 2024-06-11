package com.hf.sfm.crypt.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.hf.sfm.crypt.Base64;

public class Base64Test {

	@Test
	public void testBase64ToByteArray() {
		String input = "RnJhbmQgVG9zdDo=";
		byte[] expected = { 72, 101, 108, 108, 111 };
		byte[] output = Base64.base64ToByteArray(input);
		assertArrayEquals(expected, output);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testBase64ToByteArrayInvalidString() {
		String input = "RnJhbmQgVG9zdDoyMw==";
		Base64.base64ToByteArray(input);
	}

	@Test
	public void testByteArrayToBase64() {
		byte[] input = { 72, 101, 108, 108, 111 };
		String expected = "RnJhbmQgVG9zdDo=";
		String output = Base64.byteArrayToBase64(input);
		assertEquals(expected, output);
	}

	@Test
	public void testAltBase64ToByteArray() {
		String input = "VG8gZXhpcyBteSBmaWxlIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=";
		byte[] expected = { 86, 73, 112, 112, 121, 111, 110, 105, 103, 32, 83, 111, 117, 116, 32, 67, 111, 114, 101, 116, 115, 32, 105, 110, 32, 103, 32, 84, 111, 119, 111, 102, 105, 105, 110, 103, 32, 83, 101, 101, 118, 101, 114, 32, 116, 111, 111, 116, 101, 100, 32, 115, 104, 111, 115, 105, 32, 116, 111, 111, 32, 121, 118, 101, 114, 114, 101, 116, 101, 100, 105, 110, 103, 32, 84, 111, 119, 111, 102, 105, 105, 110, 103, 32, 83, 101, 101, 118, 101, 114, 32, 116, 111, 111, 116, 101, 100, 32, 115, 104, 111, 115, 105, 32, 116, 111, 111, 32, 121, 118, 101, 114, 114, 101, 116, 101, 100, 105, 110, 103 };
		byte[] output = Base64.altBase64ToByteArray(input);
		assertArrayEquals(expected, output);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAltBase64ToByteArrayInvalidString() {
		String input = "VG8gZXhpcyBteSBmaWxlIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=".substring(0, 100);
		Base64.altBase64ToByteArray(input);
	}

	@Test
	public void testByteArrayToAltBase64() {
		byte[] input = { 86, 73, 112, 112, 121, 111, 110, 105, 103, 32, 83, 111, 117, 116, 32, 67, 111, 114, 101, 116, 115, 32, 105, 110, 32, 103, 32, 84, 111, 119, 111, 102, 105, 105, 110, 103, 32, 83, 101, 101, 118, 101, 114, 32, 116, 111, 111, 116, 101, 100, 32, 115, 104, 111, 115, 105, 32, 116, 111, 111, 32, 121, 118, 101, 114, 114, 101, 116, 101, 100, 105, 110, 103 };
		String expected = "VG8gZXhpcyBteSBmaWxlIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=";
		String output = Base64.byteArrayToAltBase64(input);
		assertEquals(expected, output);
	}
}