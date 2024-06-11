import org.jsoup.parser.CharacterReader;
import org.jsoup.helper.Validate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class CharacterReaderTest {

    private CharacterReader reader;

    @Before
    public void setUp() {
        String text = "This is a test for CharacterReader class.";
        reader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream(text.getBytes())));
    }

    @After
    public void tearDown() {
        reader.close();
    }

    @Test
    public void testNewInstance() {
        CharacterReader reader1 = new CharacterReader(new InputStreamReader(System.in));
        Validate.notNull(reader1);
        reader1.close();

        CharacterReader reader2 = new CharacterReader(new StringReader(""));
        Validate.notNull(reader2);
        reader2.close();

        CharacterReader reader3 = new CharacterReader(new StringReader("test"));
        Validate.notNull(reader3);
        reader3.close();
    }

    @Test
    public void testPos() {
        int pos = reader.pos();
        Assert.assertEquals(0, pos);

        reader.consume();
        pos = reader.pos();
        Assert.assertEquals(1, pos);

        reader.consume(5);
        pos = reader.pos();
        Assert.assertEquals(6, pos);

        reader.rewindToMark();
        pos = reader.pos();
        Assert.assertEquals(1, pos);
    }

    @Test
    public void testIsEmpty() {
        Assert.assertFalse(reader.isEmpty());

        reader.consume(10);
        Assert.assertTrue(reader.isEmpty());

        reader.bufferUp();
        Assert.assertFalse(reader.isEmpty());
    }

    @Test(expected = UncheckedIOException.class)
    public void testIOException() throws IOException {
        CharacterReader reader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream(new byte[0])));
        reader.consume();
    }

    @Test
    public void testCurrent() {
        char current = reader.current();
        Assert.assertEquals('T', current);

        reader.consume();
        current = reader.current();
        Assert.assertEquals('h', current);

        reader.consumeTo('s');
        current = reader.current();
        Assert.assertEquals('s', current);

        reader.rewindToMark();
        current = reader.current();
        Assert.assertEquals('h', current);
    }

    @Test
    public void testConsume() {
        char c = reader.consume();
        Assert.assertEquals('T', c);

        c = reader.consume();
        Assert.assertEquals('h', c);

        c = reader.consume();
        Assert.assertEquals('i', c);

        c = reader.consume();
        Assert.assertEquals('s', c);

        c = reader.consume();
        Assert.assertEquals(' ', c);

        c = reader.consume();
        Assert.assertEquals('a', c);

        c = reader.consume();
        Assert.assertEquals(' ', c);

        c = reader.consume();
        Assert.assertEquals('t', c);

        c = reader.consume();
        Assert.assertEquals('e', c);

        c = reader.consume();
        Assert.assertEquals('s', c);

        c = reader.consume();
        Assert.assertEquals('t', c);

        c = reader.consume();
        Assert.assertEquals(' ', c);

        c = reader.consume();
        Assert.assertEquals('f', c);

        c = reader.consume();
        Assert.assertEquals('o', c);

        c = reader.consume();
        Assert.assertEquals('r', c);

        c = reader.consume();
        Assert.assertEquals(' ', c);

        c = reader.consume();
        Assert.assertEquals('c', c);

        c = reader.consume();
        Assert.assertEquals('h', c);

        c = reader.consume();
        Assert.assertEquals('a', c);

        c = reader.consume();
        Assert.assertEquals('r', c);

        c = reader.consume();
        Assert.assertEquals('a', c);

        c = reader.consume();
        Assert.assertEquals('c', c);

        c = reader.consume();
        Assert.assertEquals(Character.MIN_VALUE, c); // EOF

        reader.rewindToMark();
        c = reader.consume();
        Assert.assertEquals('h', c);
    }

    @Test
    public void testUnconsume() {
        reader.consume();
        reader.unconsume();
        char current = reader.current();
        Assert.assertEquals('T', current);
    }

    @Test
    public void testAdvance() {
        reader.advance();
        char current = reader.current();
        Assert.assertEquals('h', current);
    }

    @Test
    public void testMark() {
        reader.mark();
        char current = reader.current();
        Assert.assertEquals('h', current);

        reader.consume();
        reader.rewindToMark();
        current = reader.current();
        Assert.assertEquals('h', current);
    }

    @Test
    public void testNextIndexOf() {
        Assert.assertEquals(11, reader.nextIndexOf('s'));
        Assert.assertEquals(14, reader.nextIndexOf('f'));
        Assert.assertEquals(-1, reader.nextIndexOf('x'));

        reader.consume(11);
        Assert.assertEquals(Figure.SPACE, reader.nextIndexOf(' '));
    }

    @Test
    public void testNextIndexOfSequence() {
        Assert.assertEquals(11, reader.nextIndexOf("for"));
        Assert.assertEquals(-1, reader.nextIndexOf("not"));
        Assert.assertEquals(11, reader.nextIndexOf("for", 12));
    }

    @Test
    public void testConsumeTo() {
        String string = reader.consumeTo('s');
        Assert.assertEquals("This is a test for ", string);
        Assert.assertEquals(11, reader.pos());

        string = reader.consumeTo("for");
        Assert.assertEquals("This is a test for CharacterReader class.", string);
        Assert.assertEquals(25, reader.pos());

        string = reader.consumeToAny('x', 'y');
        Assert.assertEquals("This is a test for CharacterReader class.", string);
        Assert.assertEquals(25, reader.pos());

        string = reader.consumeToAnySorted(new char[]{'x', 'z'});
        Assert.assertEquals("This is a test for CharacterReader class.", string);
        Assert.assertEquals(25, reader.pos());

        string = reader.consumeData();
        Assert.assertEquals("This is a test for CharacterReader class.", string);
        Assert.assertEquals(25, reader.pos());

        string = reader.consumeTagName();
        Assert.assertEquals("for", string);
        Assert.assertEquals(25, reader.pos());

        string = reader.consumeToEnd();
        Assert.assertEquals("This is a test for CharacterReader class.", string);
        Assert.assertEquals(36, reader.pos());

        string = reader.consumeLetterSequence();
        Assert.assertEquals("CharacterReader", string);
        Assert.assertEquals(31, reader.pos());

        string = reader.consumeLetterThenDigitSequence();
        Assert.assertEquals("CharacterReaderclass", string);
        Assert.assertEquals(36, reader.pos());
    }

    @Test
    public void testMatches() {
        Assert.assertTrue(reader.matches('h'));
        Assert.assertTrue(reader.matches("ha"));
        Assert.assertTrue(reader.matches("ha"));
        Assert.assertTrue(reader.matchesIgnoreCase("Ha"));
        Assert.assertTrue(reader.matchesAny('h', 'a'));
        Assert.assertTrue(reader.matchesAnySorted(new char[]{'h', 'a'}));
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesDigit());

        reader.consume(11);
        Assert.assertFalse(reader.matches('s'));
        Assert.assertFalse(reader.matches("as"));
        Assert.assertFalse(reader.matches("AS"));
        Assert.assertFalse(reader.matchesAny('x', 'y'));
        Assert.assertFalse(reader.matchesAnySorted(new char[]{'x', 'z'}));
        Assert.assertFalse(reader.matchesLetter());
        Assert.assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        String string = reader.matchConsume("for");
        Assert.assertEquals("This is a test for CharacterReader class.", string);
        Assert.assertEquals(25, reader.pos());

        string = reader.matchConsumeIgnoreCase("FOR");
        Assert.assertEquals("This is a test for CharacterReader class.", string);
        Assert.assertEquals(25, reader.pos());
    }

    @Test
    public void testContainsIgnoreCase() {
        Assert.assertTrue(reader.containsIgnoreCase("for"));
        Assert.assertTrue(reader.containsIgnoreCase("FOR"));
        Assert.assertFalse(reader.containsIgnoreCase("not"));
    }

    @Test
    public void testToString() {
        Assert.assertEquals("This is a test for CharacterReader class.", reader.toString());
    }
}