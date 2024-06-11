import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.CharacterReader;
import java.io.StringReader;

public class CharacterReaderTest {

    @Test
    public void testCharacterReader() {
        // Test empty reader
        CharacterReader readerEmpty = new CharacterReader(new StringReader(""));
        Assert.assertEquals(-1, readerEmpty.pos());
        Assert.assertTrue(readerEmpty.isEmpty());

        // Test reader with content
        CharacterReader readerNormal = new CharacterReader(new StringReader("Hello, World!"));
        Assert.assertEquals(0, readerNormal.pos());
        Assert.assertFalse(readerNormal.isEmpty());
        Assert.assertEquals('H', readerNormal.current());
        Assert.assertEquals('H', readerNormal.consume());
        Assert.assertEquals('e', readerNormal.current());
        readerNormal.rewindToMark();
        Assert.assertEquals('H', readerNormal.current());

        // Test reader with custom buffer size
        CharacterReader readerCustom = new CharacterReader(new StringReader("Test"), 5);
        Assert.assertEquals(0, readerCustom.pos());
        Assert.assertFalse(readerCustom.isEmpty());
        Assert.assertEquals('T', readerCustom.current());
        readerCustom.advance();
        Assert.assertEquals('e', readerCustom.current());
        readerCustom.mark();
        readerCustom.advance();
        readerCustom.rewindToMark();
        Assert.assertEquals('T', readerCustom.current());

        // Test reading different characters and sequences
        CharacterReader readerChars = new CharacterReader(new StringReader("Hello, &<World>!"));
        Assert.assertEquals("H", readerChars.consumeTo(' '));
        Assert.assertEquals("ello, &<World>", readerChars.consumeToAny(new char[]{' ', '&', '<'}));
        Assert.assertEquals("World>", readerChars.consumeToAnySorted(new char[]{'>', '&'}));
        Assert.assertEquals("&<World>", readerChars.consumeToAny(new char[]{'&', '<'}));
        Assert.assertEquals("World>", readerChars.consumeToEnd());

        // Test reading different characters and sequences with specific delimiters
        CharacterReader readerTags = new CharacterReader(new StringReader("<html><head></head><body><title>Test</title></body></html>"));
        Assert.assertEquals("<html>", readerTags.consumeTo("<head"));
        Assert.assertEquals("<head></head><body>", readerTags.consumeTo("<body"));
        Assert.assertEquals("<title>Test</title></body>", readerTags.consumeTo("<title>"));
        Assert.assertEquals("Test", readerTags.consumeLetterSequence());
        Assert.assertEquals("Test<title>", readerTags.consumeLetterThenDigitSequence());
        Assert.assertEquals("</html>", readerTags.consumeEnd());

        // Test reading hex sequences
        CharacterReader readerHex = new CharacterReader(new StringReader("&#[x3F]"));
        Assert.assertEquals("\u003F", readerHex.consumeData());

        // Test error handling
        CharacterReader readerError = new CharacterReader(new StringReader(""));
        try {
            readerError.consume();
        } catch (IllegalStateException e) {
            Assert.assertEquals("No character to read.", e.getMessage());
        }
    }
}