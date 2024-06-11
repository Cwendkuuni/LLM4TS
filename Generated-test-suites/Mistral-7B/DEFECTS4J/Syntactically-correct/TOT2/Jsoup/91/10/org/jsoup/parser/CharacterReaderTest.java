import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.CharacterReader;
import java.io.StringReader;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstructorWithReader() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals("Hello World", characterReader.toString());
    }

    @Test
    public void testCharacterReaderConstructorWithString() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        Assert.assertEquals("Hello World", characterReader.toString());
    }

    @Test
    public void testBufferUp() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.bufferUp();
        Assert.assertEquals(5, characterReader.bufLength);
        Assert.assertEquals(0, characterReader.bufPos);
        Assert.assertEquals(0, characterReader.bufMark);
        Assert.assertEquals(5, characterReader.bufSplitPoint);
    }

    @Test
    public void testPos() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.advance();
        characterReader.advance();
        Assert.assertEquals(2, characterReader.pos());
    }

    @Test(expected = UncheckedIOException.class)
    public void testIOException() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.bufferUp();
        characterReader.reader.close();
        characterReader.consume();
    }

    @Test
    public void testIsEmpty() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertFalse(characterReader.isEmpty());
        characterReader.consumeToEnd();
        Assert.assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals('H', characterReader.current());
        characterReader.advance();
        Assert.assertEquals('e', characterReader.current());
        characterReader.consume();
        Assert.assertEquals('l', characterReader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        String consumed = characterReader.consumeTo('l');
        Assert.assertEquals("He", consumed);
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        String consumed = characterReader.consumeToEnd();
        Assert.assertEquals("Hello World", consumed);
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World <html>"));
        String consumed = characterReader.consumeToAny('h', 'l');
        Assert.assertEquals("Hello Wo", consumed);
    }

    @Test
    public void testConsumeToAnySorted() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World <html>"));
        String consumed = characterReader.consumeToAnySorted('l', 'h');
        Assert.assertEquals("World <html>", consumed);
    }

    @Test
    public void testConsumeData() {
        CharacterReader characterReader = new CharacterReader(new StringReader("&amp;<html>"));
        String consumed = characterReader.consumeData();
        Assert.assertEquals("&amp;<", consumed);
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader characterReader = new CharacterReader(new StringReader("<html>"));
        String consumed = characterReader.consumeTagName();
        Assert.assertEquals("html", consumed);
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World <html>"));
        Assert.assertEquals(13, characterReader.nextIndexOf('<'));
        Assert.assertEquals(6, characterReader.nextIndexOf('W'));
        Assert.assertEquals(-1, characterReader.nextIndexOf('X'));
    }

    @Test
    public void testNextIndexOfWithCharSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World <html>"));
        Assert.assertEquals(12, characterReader.nextIndexOf("html"));
        Assert.assertEquals(0, characterReader.nextIndexOf("Hello"));
        Assert.assertEquals(-1, characterReader.nextIndexOf("XXX"));
    }

    @Test
    public void testMatches() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertTrue(characterReader.matches('l'));
        Assert.assertTrue(characterReader.matches("ll"));
        Assert.assertFalse(characterReader.matches('x'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertTrue(characterReader.matchesIgnoreCase("l"));
        Assert.assertTrue(characterReader.matchesIgnoreCase("ll"));
        Assert.assertFalse(characterReader.matchesIgnoreCase("x"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertTrue(characterReader.matchesAny('l', 'e'));
        Assert.assertTrue(characterReader.matchesAny('l', 'L'));
        Assert.assertFalse(characterReader.matchesAny('x'));
    }

    @Test
    public void testMatchesAnySorted() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertTrue(characterReader.matchesAnySorted(new char[]{'l', 'e'}));
        Assert.assertTrue(characterReader.matchesAnySorted(new char[]{'L', 'l'}));
        Assert.assertFalse(characterReader.matchesAnySorted(new char[]{'x'}));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertTrue(characterReader.matchesLetter());
        characterReader.advance();
        Assert.assertTrue(characterReader.matchesLetter());
        characterReader.consume();
        Assert.assertTrue(characterReader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertFalse(characterReader.matchesDigit());
        characterReader.advance();
        Assert.assertFalse(characterReader.matchesDigit());
        characterReader.consume();
        Assert.assertFalse(characterReader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World <html>"));
        Assert.assertFalse(characterReader.matchConsume("html"));
        characterReader.consumeTo("html");
        Assert.assertTrue(characterReader.matchConsume("html"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World <html>"));
        Assert.assertFalse(characterReader.matchConsumeIgnoreCase("HTML"));
        characterReader.consumeTo("html");
        Assert.assertTrue(characterReader.matchConsumeIgnoreCase("html"));
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World <html>"));
        Assert.assertTrue(characterReader.containsIgnoreCase("html"));
        Assert.assertTrue(characterReader.containsIgnoreCase("HTML"));
        Assert.assertFalse(characterReader.containsIgnoreCase("XXX"));
    }
}