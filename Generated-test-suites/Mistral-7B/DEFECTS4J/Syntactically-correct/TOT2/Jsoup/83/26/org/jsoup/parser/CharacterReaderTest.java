package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstructorWithReader() {
        Reader reader = new StringReader("Hello World!");
        CharacterReader characterReader = new CharacterReader(reader);
        Assert.assertEquals("Hello World!", characterReader.toString());
    }

    @Test
    public void testCharacterReaderConstructorWithString() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        Assert.assertEquals("Hello World!", characterReader.toString());
    }

    @Test
    public void testEmptyCharacterReader() {
        CharacterReader characterReader = new CharacterReader(new StringReader(""));
        Assert.assertTrue(characterReader.isEmpty());
        Assert.assertEquals("", characterReader.toString());
    }

    @Test(expected = UncheckedIOException.class)
    public void testIOExceptionDuringBufferUp() throws IOException {
        Reader reader = new StringReader("Hello World!");
        CharacterReader characterReader = new CharacterReader(reader);
        characterReader.bufferUp();
        reader.close(); // simulate IOException during bufferUp
        characterReader.consume();
    }

    @Test
    public void testConsumeSingleCharacter() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        Assert.assertEquals('H', characterReader.consume());
    }

    @Test
    public void testConsumeAllCharacters() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        StringBuilder stringBuilder = new StringBuilder();
        while (!characterReader.isEmpty()) {
            stringBuilder.append(characterReader.consume());
        }
        Assert.assertEquals("Hello World!", stringBuilder.toString());
    }

    @Test
    public void testAdvance() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        characterReader.advance();
        Assert.assertEquals('H', characterReader.current());
    }

    @Test
    public void testMarkAndRewind() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        characterReader.mark();
        characterReader.consume();
        characterReader.rewindToMark();
        Assert.assertEquals('H', characterReader.current());
    }

    @Test
    public void testNextIndexOfSingleCharacter() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        Assert.assertEquals(0, characterReader.nextIndexOf('H'));
    }

    @Test
    public void testNextIndexOfMultiCharacterSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        Assert.assertEquals(4, characterReader.nextIndexOf("ll"));
    }

    @Test
    public void testNextIndexOfEmptySequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        Assert.assertEquals(-1, characterReader.nextIndexOf(""));
    }

    @Test
    public void testConsumeTo() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        Assert.assertEquals("Hello", characterReader.consumeTo(' '));
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        Assert.assertEquals("Hello World!", characterReader.consumeToEnd());
    }

    @Test
    public void testConsumeToAnySingleCharacter() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        Assert.assertEquals("World", characterReader.consumeToAny(new char[]{'W'}));
    }

    @Test
    public void testConsumeToAnyMultiCharacter() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        Assert.assertEquals("World", characterReader.consumeToAny(new char[]{'o', 'l'}));
    }

    @Test
    public void testConsumeData() {
        CharacterReader characterReader = new CharacterReader(new StringReader("&lt;script src=\"script.js\"&gt;"));
        Assert.assertEquals("&lt;script src=\"script.js\"", characterReader.consumeData());
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader characterReader = new CharacterReader(new StringReader("<html>\n<head>\n<title>Title</title>\n</head>\n<body>\n</body>\n</html>"));
        Assert.assertEquals("<html>", characterReader.consumeTagName());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("HelloWorld"));
        Assert.assertEquals("HelloWorld", characterReader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello12345World"));
        Assert.assertEquals("Hello12345World", characterReader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("&#x1234;"));
        Assert.assertEquals("&#x1234;", characterReader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("12345"));
        Assert.assertEquals("12345", characterReader.consumeDigitSequence());
    }

    @Test
    public void testMatchesSingleCharacter() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        Assert.assertTrue(characterReader.matches('H'));
    }

    @Test
    public void testMatchesMultiCharacterSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        Assert.assertTrue(characterReader.matches("ll"));
    }

    @Test
    public void testMatchesSingleCharacterIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        Assert.assertTrue(characterReader.matchesIgnoreCase("h"));
    }

    @Test
    public void testMatchesMultiCharacterSequenceIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        Assert.assertTrue(characterReader.matchesIgnoreCase("lL"));
    }

    @Test
    public void testMatchesAnySingleCharacter() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        Assert.assertTrue(characterReader.matchesAny('l'));
    }

    @Test
    public void testMatchesAnyMultiCharacters() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        Assert.assertTrue(characterReader.matchesAny(new char[]{'l', 'o'}));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        Assert.assertTrue(characterReader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        Assert.assertTrue(characterReader.matchesDigit());
    }

    @Test
    public void testMatchConsumeSingleCharacter() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        Assert.assertEquals("World", characterReader.matchConsume("H"));
    }

    @Test
    public void testMatchConsumeMultiCharacterSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        Assert.assertEquals("World", characterReader.matchConsume("ll"));
    }

    @Test
    public void testMatchConsumeMultiCharacterSequenceIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        Assert.assertEquals("World", characterReader.matchConsumeIgnoreCase("lL"));
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("<html>\n<head>\n<title>Title</title>\n</head>\n<body>\n</body>\n</html>"));
        Assert.assertTrue(characterReader.containsIgnoreCase("/title"));
    }
}