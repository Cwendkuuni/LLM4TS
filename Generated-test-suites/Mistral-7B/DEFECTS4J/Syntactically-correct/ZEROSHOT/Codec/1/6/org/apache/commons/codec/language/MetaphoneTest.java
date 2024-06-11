package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.language.Metaphone;

import java.util.Locale;

public class MetaphoneTest {

	@org.junit.Before
	public void setUp() {
		// Setup
	}

	@org.junit.Test
	public void testEmptyString() throws EncoderException {
		// Test empty string
		Metaphone metaphone = new Metaphone();
		String emptyString = "";
		assertEquals("", metaphone.metaphone(emptyString));
	}

	@org.junit.Test
	public void testSingleCharacter() throws EncoderException {
		// Test single character
		Metaphone metaphone = new Metaphone();
		String singleCharacter = "A";
		assertEquals("A", metaphone.metaphone(singleCharacter));
	}

	@org.junit.Test
	public void testNullInput() throws EncoderException {
		// Test null input
		Metaphone metaphone = new Metaphone();
		String input = null;
		try {
			metaphone.metaphone(input);
			assertFalse(true);
		} catch (NullPointerException e) {
			// Expected
		}
	}

	@org.junit.Test
	public void testCases() throws EncoderException {
		// Test various cases
		Metaphone metaphone = new Metaphone();
		String apple = "Apple";
		String appel = "Appel";
		String apricot = "Apricot";
		String banana = "Banana";
		String cat = "Cat";
		String dog = "Dog";
		String egg = "Egg";
		String fish = "Fish";
		String goat = "Goat";
		String hamster = "Hamster";
		String horse = "Horse";
		String igloo = "Igloo";
		String kangaroo = "Kangaroo";
		String lemon = "Lemon";
		String monkey = "Monkey";
		String octopus = "Octopus";
		String potato = "Potato";
		String queen = "Queen";
		String rabbit = "Rabbit";
		String strawberry = "Strawberry";
		String tiger = "Tiger";
		String umbrella = "Umbrella";
		String unity = "Unity";
		String violet = "Violet";
		String watermelon = "Watermelon";
		String xylophone = "Xylophone";
		String yellow = "Yellow";
		String zebra = "Zebra";

		assertEquals("A000", metaphone.metaphone(apple));
		assertEquals("A000", metaphone.metaphone(appel));
		assertEquals("A130", metaphone.metaphone(apricot));
		assertEquals("A122", metaphone.metaphone(banana));
		assertEquals("K121", metaphone.metaphone(cat));
		assertEquals("K121", metaphone.metaphone(cat));
		assertEquals("E220", metaphone.metaphone(egg));
		assertEquals("A000", metaphone.metaphone(fish));
		assertEquals("G000", metaphone.metaphone(goat));
		assertEquals("H112", metaphone.metaphone(hamster));
		assertEquals("H222", metaphone.metaphone(horse));
		assertEquals("I100", metaphone.metaphone(igloo));
		assertEquals("K321", metaphone.metaphone(kangaroo));
		assertEquals("A122", metaphone.metaphone(lemon));
		assertEquals("M111", metaphone.metaphone(monkey));
		assertEquals("K321", metaphone.metaphone(octopus));
		assertEquals("P220", metaphone.metaphone(potato));
		assertEquals("Q100", metaphone.metaphone(queen));
		assertEquals("R122", metaphone.metaphone(rabbit));
		assertEquals("S211", metaphone.metaphone(strawberry));
		assertEquals("T222", metaphone.metaphone(tiger));
		assertEquals("U200", metaphone.metaphone(umbrella));
		assertEquals("U200", metaphone.metaphone(unity));
		assertEquals("V110", metaphone.metaphone(violet));
		assertEquals("W111", metaphone.metaphone(watermelon));
		assertEquals("X422", metaphone.metaphone(xylophone));
		assertEquals("Y011", metaphone.metaphone(yellow));
		assertEquals("S521", metaphone.metaphone(zebra));
	}

	@org.junit.Test
	public void testInvalidInput() throws EncoderException {
		// Test invalid input
		Metaphone metaphone = new Metaphone();
		String input = "!@#$%^&*()_+-=[]{}|;:,.<>/?`~";
		try {
			metaphone.metaphone(input);
			assertFalse(true);
		} catch (DecoderException e) {
			// Expected
		}
	}

	@org.junit.Test
	public void testMaxLength() throws EncoderException {
		// Test max length
		Metaphone metaphone = new Metaphone();
		String longString = "LongStringLongerThanMaxLength";
		assertEquals(metaphone.getMaxCodeLen(), metaphone.metaphone(longString).length());
	}

	@org.junit.Test
	public void testChangeMaxLength() throws EncoderException {
		// Test change max length
		Metaphone metaphone = new Metaphone();
		int maxCodeLen = 5;
		metaphone.setMaxCodeLen(maxCodeLen);
		String longString = "LongStringLongerThanMaxLength";
		assertEquals(maxCodeLen, metaphone.metaphone(longString).length());
	}

	@org.junit.Test
	public void testEncode() throws EncoderException {
		// Test encode method
		Metaphone metaphone = new Metaphone();
		String apple = "Apple";
		assertEquals("A000", metaphone.encode(apple));
	}

	@org.junit.Test
	public void testEncodeNullInput() throws EncoderException {
		// Test encode with null input
		Metaphone metaphone = new Metaphone();
		String input = null;
		try {
			metaphone.encode(input);
			assertFalse(true);
		} catch (NullPointerException e) {
			// Expected
		}
	}

	@org.junit.Test
	public void testIsMetaphoneEqual() throws EncoderException {
		// Test isMetaphoneEqual method
		Metaphone metaphone = new Metaphone();
		String apple = "Apple";
		String appel = "Appel";
		String apricot = "Apricot";
		assertTrue(metaphone.isMetaphoneEqual(apple, apple));
		assertTrue(metaphone.isMetaphoneEqual(apple, appel));
		assertFalse(metaphone.isMetaphoneEqual(apple, apricot));
	}

	@org.junit.Test
	public void testIsMetaphoneEqualNullInput() {
		// Test isMetaphoneEqual with null input
		Metaphone metaphone = new Metaphone();
		String apple = "Apple";
		String input1 = null;
		String input2 = null;
		assertFalse(metaphone.isMetaphoneEqual(apple, input1));
		assertFalse(metaphone.isMetaphoneEqual(apple, input2));
	}

	@org.junit.After
	public void tearDown() {
		// Teardown
	}

}