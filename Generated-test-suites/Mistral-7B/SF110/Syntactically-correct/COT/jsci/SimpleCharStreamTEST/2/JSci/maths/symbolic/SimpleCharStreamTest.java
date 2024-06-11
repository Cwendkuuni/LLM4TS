package tests.JSci.maths.symbolic; // Adjust the package name according to your project structure

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class SimpleCharStreamTest {

    private static final int BUFFER_SIZE = 10;
    private static final int LINE_START = 1;
    private static final int COLUMN_START = 1;

    private SimpleCharStream charStream;
    private int currentLine;
    private int currentColumn;
    private ByteArrayInputStream inputStream;

    @Before
    public void setUp() {
        charStream = new SimpleCharStream(new ByteArrayInputStream("Test".getBytes()), LINE_START, COLUMN_START, BUFFER_SIZE);
        currentLine = charStream.getLine();
        currentColumn = charStream.getColumn();
        inputStream = (ByteArrayInputStream) charStream.inputStream;
    }

    @Test
    public void testConstructorWithReader() {
        SimpleCharStream charStream = new SimpleCharStream(new StringReader("Test"), 1, 1, 4096);
        assertThat(charStream, notNullValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullReader() {
        new SimpleCharStream(null, 1, 1, 4096);
    }

    @Test
    public void testExpandBuff() throws IOException {
        SimpleCharStream charStream = new SimpleCharStream(inputStream, 1, 1, BUFFER_SIZE);

        // Test without expanding
        char c = charStream.readChar();
        Assert.assertEquals(currentLine, 1);
        Assert.assertEquals(currentColumn, 2);

        for (int i = 0; i < 5; ++i) {
            char c = charStream.readChar();
            Assert.assertNotEquals(currentLine, 1);
            Assert.assertEquals(currentColumn, i + 3);
        }

        charStream.ExpandBuff(false);

        // Test with expanding
        char c = charStream.readChar();
        Assert.assertEquals(currentLine, 1);
        Assert.assertEquals(currentColumn, 5);

        for (int i = 0; i < 5; ++i) {
            char c = charStream.readChar();
            Assert.assertEquals(currentLine, 1);
            Assert.assertEquals(currentColumn, i + 5);
        }
    }

    @Test
    public void testFillBuff() throws IOException {
        SimpleCharStream charStream = new SimpleCharStream(inputStream, 1, 1, BUFFER_SIZE);

        for (int i = 0; i < BUFFER_SIZE - 1; ++i) {
            char c = charStream.readChar();
            Assert.assertNotEquals(currentLine, 1);
            Assert.assertEquals(currentColumn, i + 1);
        }

        charStream.FillBuff();

        char c = charStream.readChar();
        Assert.assertEquals(currentLine, 1);
        Assert.assertEquals(currentColumn, BUFFER_SIZE);

        for (int i = 0; i < BUFFER_SIZE - 1; ++i) {
            char c = charStream.readChar();
            Assert.assertEquals(currentLine, 1);
            Assert.assertEquals(currentColumn, i + BUFFER_SIZE);
        }

        charStream.ExpandBuff(true);

        char c = charStream.readChar();
        Assert.assertEquals(currentLine, 1);
        Assert.assertEquals(currentColumn, BUFFER_SIZE + 1);

        for (int i = 0; i < BUFFER_SIZE - 1; ++i) {
            char c = charStream.readChar();
            Assert.assertEquals(currentLine, 1);
            Assert.assertEquals(currentColumn, i + BUFFER_SIZE + 1);
        }
    }

    @Test
    public void testBeginToken() throws IOException {
        char expected = 'T';
        char actual;

        charStream.BeginToken();
        actual = charStream.readChar();
        Assert.assertEquals(expected, actual);
        Assert.assertEquals(1, currentLine);
        Assert.assertEquals(2, currentColumn);

        charStream.BeginToken();
        actual = charStream.readChar();
        Assert.assertEquals(expected, actual);
        Assert.assertEquals(1, currentLine);
        Assert.assertEquals(3, currentColumn);
    }

    @Test
    public void testUpdateLineColumn() throws IOException {
        // Test with '\\r' and '\\n'
        charStream.BeginToken();
        char c1 = charStream.readChar();
        Assert.assertEquals('\r', c1);
        Assert.assertEquals(1, currentLine);
        Assert.assertEquals(2, currentColumn);

        charStream.UpdateLineColumn('\n');

        char c2 = charStream.readChar();
        Assert.assertEquals('\n', c2);
        Assert.assertEquals(1, currentLine);
        Assert.assertEquals(3, currentColumn);

        charStream.UpdateLineColumn('T');
        char c3 = charStream.readChar();
        Assert.assertEquals('T', c3);
        Assert.assertEquals(1, currentLine);
        Assert.assertEquals(4, currentColumn);

        charStream.UpdateLineColumn('\t');
        char c4 = charStream.readChar();
        Assert.assertEquals('\t', c4);
        Assert.assertEquals(1, currentLine);
        Assert.assertEquals(9, currentColumn);

        charStream.UpdateLineColumn('\r');
        char c5 = charStream.readChar();
        Assert.assertEquals('\r', c5);
        Assert.assertEquals(1, currentLine);
        Assert.assertEquals(10, currentColumn);

        charStream.UpdateLineColumn('\n');
        char c6 = charStream.readChar();
        Assert.assertEquals('\n', c6);
        Assert.assertEquals(2, currentLine);
        Assert.assertEquals(1, currentColumn);

        charStream.UpdateLineColumn('T');
        char c7 = charStream.readChar();
        Assert.assertEquals('T', c7);
        Assert.assertEquals(2, currentLine);
        Assert.assertEquals(2, currentColumn);
    }

    @Test
    public void testReadChar() throws IOException {
        char expected;
        char actual;

        // Test with normal characters
        for (int i = 0; i < 127; ++i) {
            expected = (char) i;
            charStream.BeginToken();
            char c1 = charStream.readChar();
            Assert.assertEquals(expected, c1);
            Assert.assertEquals(1, currentLine);
            Assert.assertEquals(i + 2, currentColumn);
        }

        // Test with EOF
        charStream.FillBuff();
        char c1 = charStream.readChar();
        Assert.assertEquals(-1, c1);
        Assert.assertEquals(1, currentLine);
        Assert.assertEquals(128, currentColumn);

        // Test with backspaces
        charStream.backup(1);
        char c2 = charStream.readChar();
        Assert.assertEquals('T', c2);
        Assert.assertEquals(1, currentLine);
        Assert.assertEquals(127, currentColumn);

        charStream.Backup(2);
        char c3 = charStream.readChar();
        Assert.assertEquals('T', c3);
        Assert.assertEquals(1, currentLine);
        Assert.assertEquals(125, currentColumn);

        charStream.Backup(10);
        char c4 = charStream.readChar();
        Assert.assertEquals('T', c4);
        Assert.assertEquals(1, currentLine);
        Assert.assertEquals(118, currentColumn);
    }

    @Test
    public void testGetLineColumn() {
        int expectedLine;
        int expectedColumn;
        int actualLine;
        int actualColumn;

        charStream.BeginToken();
        char c1 = charStream.readChar();
        Assert.assertEquals('T', c1);
        Assert.assertEquals(1, currentLine);
        Assert.assertEquals(2, currentColumn);

        charStream.UpdateLineColumn('\n');
        char c2 = charStream.readChar();
        Assert.assertEquals('\n', c2);
        Assert.assertEquals(2, currentLine);
        Assert.assertEquals(1, currentColumn);

        charStream.UpdateLineColumn('T');
        char c3 = charStream.readChar();
        Assert.assertEquals('T', c3);
        Assert.assertEquals(2, currentLine);
        Assert.assertEquals(2, currentColumn);

        charStream.UpdateLineColumn('\r');
        char c4 = charStream.readChar();
        Assert.assertEquals('\r', c4);
        Assert.assertEquals(2, currentLine);
        Assert.assertEquals(3, currentColumn);

        charStream.UpdateLineColumn('\n');
        char c5 = charStream.readChar();
        Assert.assertEquals('\n', c5);
        Assert.assertEquals(3, currentLine);
        Assert.assertEquals(1, currentColumn);

        charStream.UpdateLineColumn('T');
        char c6 = charStream.readChar();
        Assert.assertEquals('T', c6);
        Assert.assertEquals(3, currentLine);
        Assert.assertEquals(2, currentColumn);

        charStream.UpdateLineColumn('\t');
        char c7 = charStream.readChar();
        Assert.assertEquals('\t', c7);
        Assert.assertEquals(3, currentLine);
        Assert.assertEquals(9, currentColumn);

        charStream.UpdateLineColumn('\r');
        char c8 = charStream.readChar();
        Assert.assertEquals('\r', c8);
        Assert.assertEquals(3, currentLine);
        Assert.assertEquals(10, currentColumn);

        charStream.UpdateLineColumn('\n');
        char c9 = charStream.readChar();
        Assert.assertEquals('\n', c9);
        Assert.assertEquals(4, currentLine);
        Assert.assertEquals(1, currentColumn);

        expectedLine = 3;
        expectedColumn = 11;
        actualLine = charStream.getLine();
        actualColumn = charStream.getColumn();
        Assert.assertEquals(expectedLine, actualLine);
        Assert.assertEquals(expectedColumn, actualColumn);

        charStream.UpdateLineColumn('T');
        char c10 = charStream.readChar();
        Assert.assertEquals('T', c10);
        Assert.assertEquals(4, currentLine);
        Assert.assertEquals(2, currentColumn);

        expectedLine = 4;
        expectedColumn = 3;
        actualLine = charStream.getLine();
        actualColumn = charStream.getColumn();
        Assert.assertEquals(expectedLine, actualLine);
        Assert.assertEquals(expectedColumn, actualColumn);

        charStream.UpdateLineColumn('\n');
        char c11 = charStream.readChar();
        Assert.assertEquals('\n', c11);
        Assert.assertEquals(5, currentLine);
        Assert.assertEquals(1, currentColumn);

        expectedLine = 5;
        expectedColumn = 1;
        actualLine = charStream.getLine();
        actualColumn = charStream.getColumn();
        Assert.assertEquals(expectedLine, actualLine);
        Assert.assertEquals(expectedColumn, actualColumn);
    }

    @Test(expected = NullPointerException.class)
    public void testGetImageWithNullBuffer() {
        SimpleCharStream charStream = new SimpleCharStream(null, 1, 1, 4096);
        charStream.GetImage();
    }

    @Test
    public void testGetImage() {
        String expected = "Test";
        String actual = charStream.GetImage();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetSuffix() {
        String expected = "st";
        String actual = charStream.GetSuffix(3);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDone() {
        SimpleCharStream charStream = new SimpleCharStream(inputStream, 1, 1, BUFFER_SIZE);
        charStream.Done();
        Assert.assertThat(charStream.buffer, nullValue());
        Assert.assertThat(charStream.bufline, nullValue());
        Assert.assertThat(charStream.bufcolumn, nullValue());
    }

    @Test
    public void testAdjustBeginLineColumn() {
        int newLine = 5;
        int newColumn = 10;
        SimpleCharStream charStream = new SimpleCharStream(inputStream, 1, 1, BUFFER_SIZE);

        charStream.UpdateLineColumn('\n');
        charStream.UpdateLineColumn('T');
        charStream.UpdateLineColumn('\n');
        charStream.UpdateLineColumn('T');
        charStream.UpdateLineColumn('\n');
        charStream.UpdateLineColumn('T');
        charStream.UpdateLineColumn('\n');
        charStream.UpdateLineColumn('T');
        charStream.UpdateLineColumn('\n');
        charStream.UpdateLineColumn('T');
        charStream.UpdateLineColumn('\n');
        charStream.UpdateLineColumn('T');
        charStream.UpdateLineColumn('\n');
        charStream.UpdateLineColumn('T');

        charStream.adjustBeginLineColumn(newLine, newColumn);

        int actualLine = charStream.getLine();
        int actualColumn = charStream.getColumn();
        Assert.assertEquals(newLine, actualLine);
        Assert.assertEquals(newColumn, actualColumn);
    }
}