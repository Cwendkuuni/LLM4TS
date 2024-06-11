package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;

/**
 * Test class for Metaphone.java
 */
public class MetaphoneTest {

	private Metaphone metaphone = new Metaphone();

	/**
	 * Test method for {@link Metaphone#metaphone(String)}
	 */
	@Test
	public void testMetaphone() {
		String input = "apple";
		String expected = "A0P0L0E0";
		String result = metaphone.metaphone(input);
		assertEquals(expected, result);

		input = "appel";
		expected = "A0P0L0E1";
		result = metaphone.metaphone(input);
		assertEquals(expected, result);

		input = "apfel";
		expected = "A0P0L0E2";
		result = metaphone.metaphone(input);
		assertEquals(expected, result);

		input = "banana";
		expected = "B2N1A0N1A0";
		result = metaphone.metaphone(input);
		assertEquals(expected, result);

		input = "bananen";
		expected = "B2N1A0N1E0";
		result = metaphone.metaphone(input);
		assertEquals(expected, result);

		input = "banane";
		expected = "B2N1A0N1E1";
		result = metaphone.metaphone(input);
		assertEquals(expected, result);

		input = "katze";
		expected = "K1T2Z1E0";
		result = metaphone.metaphone(input);
		assertEquals(expected, result);

		input = "Katze";
		expected = "K1T2Z1E1";
		result = metaphone.metaphone(input);
		assertEquals(expected, result);

		input = "katz";
		expected = "K1T2Z1E0";
		result = metaphone.metaphone(input);
		assertEquals(expected, result);

		input = "Katz";
		expected = "K1T2Z1E1";
		result = metaphone.metaphone(input);
		assertEquals(expected, result);

		input = "Banana";
		expected = "B2N1A0N1A0";
		result = metaphone.metaphone(input);
		assertEquals(expected, result);

		input = "Apple";
		expected = "A0P0L0E0";
		result = metaphone.metaphone(input);
		assertEquals(expected, result);

		input = "Appels";
		expected = "A0P0L0E1S";
		result = metaphone.metaphone(input);
		assertEquals(expected, result);

		input = "Apfel";
		expected = "A0P0L0E2";
		result = metaphone.metaphone(input);
		assertEquals(expected, result);

		input = "Apfels";
		expected = "A0P0L0E2S";
		result = metaphone.metaphone(input);
		assertEquals(expected, result);

		input = "Appelsin";
		expected = "A0P0L0E1S1N";
		result = metaphone.metaphone(input);
		assertEquals(expected, result);

		input = "Apfelstrudel";
		expected = "A0P0L0E2S1T1R1U1L1D1";
		result = metaphone.metaphone(input);
		assertEquals(expected, result);

		input = "Apfelstrudeln";
		expected = "A0P0L0E2S1T1R1U1L1D1N";
		result = metaphone.metaphone(input);
		assertEquals(expected, result);

		input = "Appelflaus";
		expected = "A0P0L0E0F1L1A1U1S";
		result = metaphone.metaphone(input);
		assertEquals(expected, result);

		input = "Appelflause";
		expected = "A0P0L0E0F1L1A1U1S1E";
		result = metaphone.metaphone(input);
		assertEquals(expected, result);

		input = "Appelfläschchen";
		expected = "A0P0L0E0F1L1A1U1S1C1H1E1N";
		result = metaphone.metaphone(input);
		assertEquals(expected, result);
	}

	/**
	 * Test method for {@link Metaphone#isMetaphoneEqual(String, String)}
	 */
	@Test
	public void testIsMetaphoneEqual() {
		String input1 = "apple";
		String input2 = "apple";
		assertTrue(metaphone.isMetaphoneEqual(input1, input2));

		input1 = "apple";
		input2 = "apfel";
		assertFalse(metaphone.isMetaphoneEqual(input1, input2));

		input1 = "appel";
		input2 = "apfel";
		assertTrue(metaphone.isMetaphoneEqual(input1, input2));

		input1 = "banana";
		input2 = "banana";
		assertTrue(metaphone.isMetaphoneEqual(input1, input2));

		input1 = "banana";
		input2 = "bananen";
		assertTrue(metaphone.isMetaphoneEqual(input1, input2));

		input1 = "banane";
		input2 = "banane";
		assertTrue(metaphone.isMetaphoneEqual(input1, input2));

		input1 = "banane";
		input2 = "bananen";
		assertTrue(metaphone.isMetaphoneEqual(input1, input2));

		input1 = "katze";
		input2 = "katze";
		assertTrue(metaphone.isMetaphoneEqual(input1, input2));

		input1 = "Katze";
		input2 = "Katze";
		assertTrue(metaphone.isMetaphoneEqual(input1, input2));

		input1 = "katz";
		input2 = "katz";
		assertTrue(metaphone.isMetaphoneEqual(input1, input2));

		input1 = "Katz";
		input2 = "Katz";
		assertTrue(metaphone.isMetaphoneEqual(input1, input2));

		input1 = "Banana";
		input2 = "Banana";
		assertTrue(metaphone.isMetaphoneEqual(input1, input2));

		input1 = "Apple";
		input2 = "Apple";
		assertTrue(metaphone.isMetaphoneEqual(input1, input2));

		input1 = "Appels";
		input2 = "Appels";
		assertTrue(metaphone.isMetaphoneEqual(input1, input2));

		input1 = "Apfel";
		input2 = "Apfel";
		assertTrue(metaphone.isMetaphoneEqual(input1, input2));

		input1 = "Apfels";
		input2 = "Apfels";
		assertTrue(metaphone.isMetaphoneEqual(input1, input2));

		input1 = "Appelsin";
		input2 = "Appelsin";
		assertTrue(metaphone.isMetaphoneEqual(input1, input2));

		input1 = "Apfelstrudel";
		input2 = "Apfelstrudel";
		assertTrue(metaphone.isMetaphoneEqual(input1, input2));

		input1 = "Apfelstrudeln";
		input2 = "Apfelstrudeln";
		assertTrue(metaphone.isMetaphoneEqual(input1, input2));

		input1 = "Appelflaus";
		input2 = "Appelflaus";
		assertTrue(metaphone.isMetaphoneEqual(input1, input2));

		input1 = "Appelflause";
		input2 = "Appelflause";
		assertTrue(metaphone.isMetaphoneEqual(input1, input2));

		input1 = "Appelfläschchen";
		input2 = "Appelfläschchen";
		assertTrue(metaphone.isMetaphoneEqual(input1, input2));
	}

	/**
	 * Test method for {@link Metaphone#encode(Object)}
	 */
	@Test(expected = EncoderException.class)
	public void testEncodeObject() {
		Metaphone metaphone = new Metaphone();
		Object input = 123;
		metaphone.encode(input);
	}

	/**
	 * Test method for {@link Metaphone#encode(String)}
	 */
	@Test
	public void testEncodeString() {
		Metaphone metaphone = new Metaphone();
		String input = "apple";
		String expected = "A0P0L0E0";
		String result = metaphone.encode(input);
		assertEquals(expected, result);
	}

	/**
	 * Test method for {@link Metaphone#getMaxCodeLen()}
	 */
	@Test
	public void testGetMaxCodeLen() {
		Metaphone metaphone = new Metaphone();
		int expected = 4;
		int result = metaphone.getMaxCodeLen();
		assertEquals(expected, result);
	}

	/**
	 * Test method for {@link Metaphone#setMaxCodeLen(int)}
	 */
	@Test
	public void testSetMaxCodeLen() {
		Metaphone metaphone = new Metaphone();
		int maxCodeLen = 5;
		metaphone.setMaxCodeLen(maxCodeLen);
		assertEquals(maxCodeLen, metaphone.getMaxCodeLen());
	}
}