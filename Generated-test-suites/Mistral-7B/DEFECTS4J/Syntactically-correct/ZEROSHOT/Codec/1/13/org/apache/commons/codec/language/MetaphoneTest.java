package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * MetaphoneTest class to test Metaphone implementation
 */
public class MetaphoneTest {

	/**
	 * Test method for {@link Metaphone#metaphone(String)}
	 *
	 * @throws EncoderException
	 */
	@Test
	public void testMetaphone() throws EncoderException {
		Metaphone metaphone = new Metaphone();

		// Test empty string
		assertEquals("", metaphone.metaphone(""));

		// Test single character string
		assertEquals("B", metaphone.metaphone("A"));
		assertEquals("B", metaphone.metaphone("B"));
		assertEquals("B", metaphone.metaphone("C"));

		// Test basic words
		assertEquals("B332", metaphone.metaphone("Baker"));
		assertEquals("B261", metaphone.metaphone("Bakeries"));
		assertEquals("B261", metaphone.metaphone("Bakery"));
		assertEquals("B332", metaphone.metaphone("Bakers"));
		assertEquals("B332", metaphone.metaphone("Baker's"));
		assertEquals("B332", metaphone.metaphone("Baker's Dozen"));

		assertEquals("C321", metaphone.metaphone("Cat"));
		assertEquals("C321", metaphone.metaphone("Cats"));
		assertEquals("C321", metaphone.metaphone("Catherine"));
		assertEquals("C321", metaphone.metaphone("Cathy"));

		assertEquals("D221", metaphone.metaphone("Dog"));
		assertEquals("D221", metaphone.metaphone("Dogs"));
		assertEquals("D221", metaphone.metaphone("Dog's"));
		assertEquals("J221", metaphone.metaphone("Dogs'"));

		assertEquals("F112", metaphone.metaphone("Five"));
		assertEquals("F112", metaphone.metaphone("Fives"));
		assertEquals("F112", metaphone.metaphone("Five's"));

		assertEquals("G311", metaphone.metaphone("Grape"));
		assertEquals("G311", metaphone.metaphone("Grapes"));
		assertEquals("G311", metaphone.metaphone("Grapes'"));
		assertEquals("K311", metaphone.metaphone("Grapes'")); // Silent G

		// Test special cases
		assertEquals("A123", metaphone.metaphone("Aardvark")); // Silent A
		assertEquals("A213", metaphone.metaphone("Abraham")); // Silent H
		assertEquals("A213", metaphone.metaphone("Abrahams")); // Silent H
		assertEquals("S020", metaphone.metaphone("Thought")); // Silent TH

		// Test complex words
		assertEquals("C321X", metaphone.metaphone("Cacophony"));
		assertEquals("C321S", metaphone.metaphone("Cacophonies"));
		assertEquals("C321S", metaphone.metaphone("Cacophony's"));
		assertEquals("C321X", metaphone.metaphone("Cacophonist"));
		assertEquals("C321X", metaphone.metaphone("Cacophonous"));
		assertEquals("C321S", metaphone.metaphone("Cacophonists"));
		assertEquals("C321S", metaphone.metaphone("Cacophonous'"));
	}

	/**
	 * Test method for {@link Metaphone#isMetaphoneEqual(String, String)}
	 */
	@Test
	public void testIsMetaphoneEqual() {
		Metaphone metaphone = new Metaphone();

		// Test empty strings
		assertTrue(metaphone.isMetaphoneEqual("", ""));

		// Test single character strings
		assertTrue(metaphone.isMetaphoneEqual("A", "A"));

		// Test basic words
		assertTrue(metaphone.isMetaphoneEqual("Baker", "Baker"));
		assertTrue(metaphone.isMetaphoneEqual("Bakeries", "Bakeries"));
		assertTrue(metaphone.isMetaphoneEqual("Bakery", "Bakery"));
		assertTrue(metaphone.isMetaphoneEqual("Bakers", "Bakers"));
		assertTrue(metaphone.isMetaphoneEqual("Baker's", "Baker's"));
		assertTrue(metaphone.isMetaphoneEqual("Baker's Dozen", "Baker's Dozen"));

		assertTrue(metaphone.isMetaphoneEqual("Cat", "Cat"));
		assertTrue(metaphone.isMetaphoneEqual("Cats", "Cats"));
		assertTrue(metaphone.isMetaphoneEqual("Catherine", "Catherine"));
		assertTrue(metaphone.isMetaphoneEqual("Cathy", "Cathy"));

		assertTrue(metaphone.isMetaphoneEqual("Dog", "Dog"));
		assertTrue(metaphone.isMetaphoneEqual("Dogs", "Dogs"));
		assertTrue(metaphone.isMetaphoneEqual("Dog's", "Dog's"));
		assertTrue(metaphone.isMetaphoneEqual("Dogs'", "Dogs'"));

		assertTrue(metaphone.isMetaphoneEqual("Five", "Five"));
		assertTrue(metaphone.isMetaphoneEqual("Fives", "Fives"));
		assertTrue(metaphone.isMetaphoneEqual("Five's", "Five's"));

		assertTrue(metaphone.isMetaphoneEqual("Grape", "Grape"));
		assertTrue(metaphone.isMetaphoneEqual("Grapes", "Grapes"));
		assertTrue(metaphone.isMetaphoneEqual("Grapes'", "Grapes'"));
		assertTrue(metaphone.isMetaphoneEqual("Grapes'", "Grapes'")); // Silent G

		// Test special cases
		assertTrue(metaphone.isMetaphoneEqual("Aardvark", "Aardvark")); // Silent A
		assertTrue(metaphone.isMetaphoneEqual("Abraham", "Abraham")); // Silent H
		assertTrue(metaphone.isMetaphoneEqual("Abrahams", "Abrahams")); // Silent H
		assertTrue(metaphone.isMetaphoneEqual("Thought", "Thought")); // Silent TH

		// Test complex words
		assertTrue(metaphone.isMetaphoneEqual("Cacophony", "Cacophony"));
		assertTrue(metaphone.isMetaphoneEqual("Cacophonies", "Cacophonies"));
		assertTrue(metaphone.isMetaphoneEqual("Cacophony's", "Cacophony's"));
		assertTrue(metaphone.isMetaphoneEqual("Cacophonist", "Cacophonist"));
		assertTrue(metaphone.isMetaphoneEqual("Cacophonous", "Cacophonous"));
		assertTrue(metaphone.isMetaphoneEqual("Cacophonists", "Cacophonists"));
		assertTrue(metaphone.isMetaphoneEqual("Cacophonous'", "Cacophonous'"));

		// Test non-matching words
		assertFalse(metaphone.isMetaphoneEqual("Baker", "Cats"));
	}

	/**
	 * Test method for {@link Metaphone#isMetaphoneEqual(Object, Object)}
	 */
	@Test(expected = EncoderException.class)
	public void testIsMetaphoneEqualObject() {
		Metaphone metaphone = new Metaphone();

		metaphone.isMetaphoneEqual(1, "Baker");
	}

	/**
	 * Test method for {@link Metaphone#encode(Object)}
	 */
	@Test(expected = EncoderException.class)
	public void testEncodeObject() {
		Metaphone metaphone = new Metaphone();

		metaphone.encode(1);
	}

	/**
	 * Test method for {@link Metaphone#encode(String)}
	 */
	@Test
	public void testEncode() {
		Metaphone metaphone = new Metaphone();

		assertEquals("B332", metaphone.encode("Baker"));
		assertEquals("B261", metaphone.encode("Bakeries"));
		assertEquals("B261", metaphone.encode("Bakery"));
		assertEquals("B332", metaphone.encode("Bakers"));
		assertEquals("B332", metaphone.encode("Baker's"));
		assertEquals("B332", metaphone.encode("Baker's Dozen"));

		assertEquals("C321", metaphone.encode("Cat"));
		assertEquals("C321", metaphone.encode("Cats"));
		assertEquals("C321", metaphone.encode("Catherine"));
		assertEquals("C321", metaphone.encode("Cathy"));

		assertEquals("D221", metaphone.encode("Dog"));
		assertEquals("D221", metaphone.encode("Dogs"));
		assertEquals("J221", metaphone.encode("Dogs'"));

		assertEquals("F112", metaphone.encode("Five"));
		assertEquals("F112", metaphone.encode("Fives"));
		assertEquals("F112", metaphone.encode("Five's"));

		assertEquals("G311", metaphone.encode("Grape"));
		assertEquals("G311", metaphone.encode("Grapes"));
		assertEquals("G311", metaphone.encode("Grapes'"));
		assertEquals("K311", metaphone.encode("Grapes'")); // Silent G

		assertEquals("A123", metaphone.encode("Aardvark")); // Silent A
		assertEquals("A213", metaphone.encode("Abraham")); // Silent H
		assertEquals("A213", metaphone.encode("Abrahams")); // Silent H
		assertEquals("S020", metaphone.encode("Thought")); // Silent TH

		assertEquals("C321X", metaphone.encode("Cacophony"));
		assertEquals("C321S", metaphone.encode("Cacophonies"));
		assertEquals("C321S", metaphone.encode("Cacophony's"));
		assertEquals("C321X", metaphone.encode("Cacophonist"));
		assertEquals("C321X", metaphone.encode("Cacophonous"));
		assertEquals("C321S", metaphone.encode("Cacophonists"));
		assertEquals("C321S", metaphone.encode("Cacophonous'"));
	}

	/**
	 * Test method for {@link Metaphone#getMaxCodeLen()}
	 */
	@Test
	public void testGetMaxCodeLen() {
		Metaphone metaphone = new Metaphone();

		assertEquals(4, metaphone.getMaxCodeLen());
	}

	/**
	 * Test method for {@link Metaphone#setMaxCodeLen(int)}
	 */
	@Test
	public void testSetMaxCodeLen() {
		Metaphone metaphone = new Metaphone();

		metaphone.setMaxCodeLen(5);
		assertEquals(5, metaphone.getMaxCodeLen());
	}

	private void assertTrue(boolean condition) {
		if (!condition) {
			fail();
		}
	}
}