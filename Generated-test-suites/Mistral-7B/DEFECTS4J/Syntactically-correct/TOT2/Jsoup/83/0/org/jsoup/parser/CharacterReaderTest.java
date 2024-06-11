import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.UncheckedIOException;
import org.junit.Assert;
import org.junit.Test;
import java.io.StringReader;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstructorWithReader() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals("Hello World", reader.toString());
        Assert.assertEquals(11, reader.pos());
        Assert.assertFalse(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConstructorWithString() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        reader.consumeTo('W');
        Assert.assertEquals("Hello", reader.toString());
        Assert.assertEquals(6, reader.pos());
        Assert.assertFalse(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumer() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        char c;
        for (int i = 0; i < 11; i++) {
            c = reader.consume();
            Assert.assertEquals((char) ('H' + i), c);
        }
        Assert.assertEquals(CharacterReader.EOF, reader.consume());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderAdvanceAndRewind() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        reader.advance();
        reader.rewindToMark();
        Assert.assertEquals('H', reader.current());
        reader.mark();
        reader.advance();
        reader.advance();
        Assert.assertEquals('L', reader.current());
        reader.rewindToMark();
        Assert.assertEquals('H', reader.current());
    }

    @Test
    public void testCharacterReaderNextIndexOfChar() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals(5, reader.nextIndexOf('O'));
        reader.consumeTo('O');
        Assert.assertEquals(0, reader.nextIndexOf('O'));
    }

    @Test
    public void testCharacterReaderNextIndexOfString() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals(6, reader.nextIndexOf("ll"));
        Assert.assertEquals(-1, reader.nextIndexOf("XX"));
    }

    @Test(expected = UncheckedIOException.class)
    public void testCharacterReaderWithIOException() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello") {
            @Override
            public int read(char[] cbuf, int off, int len) throws IOException {
                throw new IOException("Test IOException");
            }
        });
        reader.consume();
    }
}