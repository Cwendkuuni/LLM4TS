package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;

/**
 * Test class for Metaphone.java
 */
public class MetaphoneTest {

	/**
	 * Test initialization of new Metaphone instance
	 */
	@Test
	public void testMetaphoneInitialization() {
		Metaphone metaphone = new Metaphone();
		assertNotNull(metaphone);
	}

	/**
	 * Test empty String encoding
	 */
	@Test
	public void testMetaphoneEmptyString() {
		Metaphone metaphone = new Metaphone();
		String encodedString = metaphone.metaphone("");
		assertNull(encodedString);
	}

	/**
	 * Test encoding of single character string
	 */
	@Test
	public void testMetaphoneSingleCharacterString() {
		Metaphone metaphone = new Metaphone();
		String encodedString = metaphone.metaphone("A");
		assertEquals("A", encodedString);
	}

	/**
	 * Test encoding of special cases
	 */
	@Test
	public void testMetaphoneSpecialCases() {
		Metaphone metaphone = new Metaphone();
		String encodedString1 = metaphone.metaphone("KNIGHT");
		assertEquals("N2T", encodedString1);

		String encodedString2 = metaphone.metaphone("APPLE");
		assertEquals("P1L", encodedString2);

		String encodedString3 = metaphone.metaphone("BANana");
		assertEquals("M6", encodedString3);

		String encodedString4 = metaphone.metaphone("QUICK");
		assertEquals("K3", encodedString4);

		String encodedString5 = metaphone.metaphone("BUSINESS");
		assertEquals("S5N", encodedString5);
	}

	/**
	 * Test encoding of multiple words separated by spaces
	 */
	@Test
	public void testMetaphoneMultipleWords() {
		Metaphone metaphone = new Metaphone();
		String input = "TO BE OR NOT TO BE OR WANT TO BE OR NOT WANT TO BE";
		String encodedString = metaphone.metaphone(input);
		assertEquals("T2B0 N2T N1W2T N1W2T B3", encodedString);
	}

	/**
	 * Test encoding of String with numbers
	 */
	@Test
	public void testMetaphoneWithNumbers() {
		Metaphone metaphone = new Metaphone();
		String encodedString = metaphone.metaphone("apple123banana");
		assertEquals("P1L", encodedString.substring(0, 3));
		assertEquals("M6", encodedString.substring(4, 7));
	}

	/**
	 * Test encoding of String with punctuation marks
	 */
	@Test
	public void testMetaphoneWithPunctuationMarks() {
		Metaphone metaphone = new Metaphone();
		String encodedString = metaphone.metaphone("apple. Banana!");
		assertEquals("P1L", encodedString.substring(0, 3));
		assertEquals("M6", encodedString.substring(4, 7));
	}

	/**
	 * Test encoding with maximum code length
	 */
	@Test
	public void testMetaphoneMaxCodeLength() {
		Metaphone metaphone = new Metaphone();
		String encodedString1 = metaphone.metaphone("TO BE OR NOT TO BE OR WANT TO BE OR NOT WANT TO BE");
		assertEquals("T2B0 N2T N1W2T N1W2T B3", encodedString1);

		Metaphone metaphone2 = new Metaphone();
		metaphone2.setMaxCodeLen(5);
		String encodedString2 = metaphone2.metaphone("TO BE OR NOT TO BE OR WANT TO BE OR NOT WANT TO BE");
		assertEquals("T2B0 N2T N1W2T N1W2T B3T", encodedString2);
	}

	/**
	 * Test encoding with null String
	 */
	@Test(expected = NullPointerException.class)
	public void testMetaphoneNullString() {
		Metaphone metaphone = new Metaphone();
		metaphone.metaphone(null);
	}

	/**
	 * Test encoding with non-String object
	 */
	@Test(expected = EncoderException.class)
	public void testMetaphoneNonStringObject() {
		Metaphone metaphone = new Metaphone();
		metaphone.encode(new Object());
	}
}