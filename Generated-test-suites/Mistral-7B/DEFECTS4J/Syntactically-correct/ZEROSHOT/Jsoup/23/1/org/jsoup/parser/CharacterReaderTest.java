package org.jsoup.parser.test;

import org.junit.Test;
import org.jsoup.parser.CharacterReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CharacterReaderTest {

private CharacterReader characterReader;

@Before
public void setUp() {
characterReader = new CharacterReader("Hello World\n1234abcdEF56\n");
}

@Test
public void testNewInstance() {
Validate.notNull(characterReader);
}

@Test(expected = NullPointerException.class)
public void testNewInstanceNullInput() {
new CharacterReader(null);
}

@Test
public void testIsEmpty() {
assertTrue(characterReader.isEmpty());
}

@Test
public void testIsEmptyAfterConsumption() {
characterReader.consume();
assertTrue(characterReader.isEmpty());
}

@Test
public void testCurrentChar() {
assertEquals('H', characterReader.current());
characterReader.consume();
assertEquals('H', characterReader.current());
}

@Test
public void testConsumeSingleChar() {
characterReader.consume();
assertEquals('H', characterReader.current());
assertEquals('o', characterReader.current());
}

@Test
public void testConsumeMultipleChars() {
characterReader.consume(2);
assertEquals('o', characterReader.current());
assertEquals('W', characterReader.current());
}

@Test
public void testConsumeToEnd() {
assertEquals("World\n1234abcdEF56\n", characterReader.consumeToEnd());
assertTrue(characterReader.isEmpty());
}

@Test
public void testConsumeToSingleChar() {
assertEquals("World\n1234", characterReader.consumeTo('4'));
assertEquals('4', characterReader.current());
}

@Test
public void testConsumeToMultiChar() {
assertEquals("ef", characterReader.consumeTo("EF"));
assertEquals('5', characterReader.current());
}

@Test
public void testConsumeToSequence() {
assertEquals("abcd", characterReader.consumeTo("ef"));
assertEquals('E', characterReader.current());
}

@Test
public void testConsumeToAnySingleChar() {
assertEquals("1", characterReader.consumeToAny('4', '5'));
assertEquals('1', characterReader.current());
}

@Test
public void testConsumeToAnyMultiChars() {
assertEquals("123", characterReader.consumeToAny('3', '5'));
assertEquals('4', characterReader.current());
}

@Test
public void testConsumeLetterSequence() {
assertEquals("World", characterReader.consumeLetterSequence());
assertEquals('5', characterReader.current());
}

@Test
public void testConsumeHexSequence() {
assertEquals("1234", characterReader.consumeHexSequence());
assertEquals('a', characterReader.current());
}

@Test
public void testConsumeDigitSequence() {
assertEquals("1234", characterReader.consumeDigitSequence());
assertEquals('a', characterReader.current());
}

@Test
public void testMatchesChar() {
assertTrue(characterReader.matches('H'));
assertFalse(characterReader.matches('!'));
}

@Test
public void testMatchesString() {
assertTrue(characterReader.matches("Hello"));
assertFalse(characterReader.matches("World"));
}

@Test
public void testMatchesIgnoreCaseString() {
assertTrue(characterReader.matchesIgnoreCase("hello"));
assertTrue(characterReader.matchesIgnoreCase("Hello"));
assertFalse(characterReader.matchesIgnoreCase("World"));
}

@Test
public void testMatchesAnySingleChar() {
assertTrue(characterReader.matchesAny('o'));
assertFalse(characterReader.matchesAny('!'));
}

@Test
public void testMatchesLetter() {
assertTrue(characterReader.matchesLetter());
assertFalse(characterReader.matchesDigit());
}

@Test
public void testMatchesDigit() {
assertFalse(characterReader.matchesLetter());
assertTrue(characterReader.matchesDigit());
}

@Test
public void testMatchConsumeString() {
assertEquals("World", characterReader.matchConsume("World"));
assertEquals("1234", characterReader.matchConsume("1234"));
assertEquals("EF", characterReader.matchConsume("EF"));
assertEquals("", characterReader.matchConsume("abcd"));
}

@Test
public void testMatchConsumeIgnoreCaseString() {
assertEquals("World", characterReader.matchConsumeIgnoreCase("World"));
assertEquals("1234", characterReader.matchConsumeIgnoreCase("1234"));
assertEquals("EF", characterReader.matchConsumeIgnoreCase("ef"));
assertEquals("", characterReader.matchConsumeIgnoreCase("abcd"));
}

@Test
public void testContainsIgnoreCaseString() {
assertTrue(characterReader.containsIgnoreCase("world"));
assertTrue(characterReader.containsIgnoreCase("World"));
assertFalse(characterReader.containsIgnoreCase("Zxcv"));
}

@Test
public void testToString() {
assertEquals("1234abcdEF", characterReader.toString());
}

@Test
public void testMarkAndRewind() {
characterReader.mark();
characterReader.consume();
characterReader.rewindToMark();
assertEquals('H', characterReader.current());
}
}