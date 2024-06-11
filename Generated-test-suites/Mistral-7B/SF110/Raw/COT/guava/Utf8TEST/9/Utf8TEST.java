package com.google.common.base;

import static org.junit.Assert.*;

import org.junit.Test;

public class Utf8Test {

	@Test
	public void testEncodedLength() {
		CharSequence sequence = "UTF-8 test";
		int length = Utf8.encodedLength(sequence);
		assertEquals(11, length);

		sequence = "\u00e9UTF-8 test";
		length = Utf8.encodedLength(sequence);
		assertEquals(12, length);
	}

	@Test
	public void testEncodedLengthGeneral() {
		CharSequence sequence = "UTF-8 test";
		int start = 1;
		int length = Utf8.encodedLengthGeneral(sequence, start);
		assertEquals(8, length);

		sequence = "\u00e9UTF-8 test";
		start = 1;
		length = Utf8.encodedLengthGeneral(sequence, start);
		assertEquals(10, length);
	}

	@Test
	public void testIsWellFormed() {
		byte[] bytes = {0x48, 0x65, 0x6C, 0x6C, 0x6F};
		assertTrue(Utf8.isWellFormed(bytes));

		bytes = new byte[] {0xEF, 0xBB, 0xBF, 0x48, 0x65, 0x6C, 0x6C, 0x6F};
		assertTrue(Utf8.isWellFormed(bytes));

		bytes = new byte[] {(byte) 0xEF, (byte) 0xBF, 0xBF, 0x48, 0x65, 0x6C, 0x6C, 0x6F};
		assertFalse(Utf8.isWellFormed(bytes));

		bytes = new byte[] {0xFF, 0xFF, 0xFF, 0xFF};
		assertFalse(Utf8.isWellFormed(bytes));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEncodedLengthIllegalArgumentException() {
		CharSequence sequence = "UTF-8 test";
		Utf8.encodedLength(sequence); // Method does not throw an exception, so test it with an assertion
		assertEquals(11, Utf8.encodedLength(sequence));

		sequence = new String(new byte[] {0xEF, 0xBF, 0xBF, (byte) 0x80}, "UTF-8");
		assertFalse(Utf8.isWellFormed(sequence.getBytes()));
		Utf8.encodedLength(sequence); // Method should throw an IllegalArgumentException
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEncodedLengthGeneralIllegalArgumentException() {
		CharSequence sequence = "UTF-8 test";
		Utf8.encodedLengthGeneral(sequence, 1); // Method does not throw an exception, so test it with an assertion
		assertEquals(8, Utf8.encodedLengthGeneral(sequence, 1));

		sequence = "\u00e9UTF-8 test";
		Utf8.encodedLengthGeneral(sequence, 1); // Method does not throw an exception, so test it with an assertion
		assertEquals(10, Utf8.encodedLengthGeneral(sequence, 1));

		sequence = new StringBuilder().append("\uD800").append("\uDC00").toString();
		Utf8.encodedLengthGeneral(sequence, 0); // Method should throw an IllegalArgumentException
	}
}