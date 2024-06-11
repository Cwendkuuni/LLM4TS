package JSci.maths.symbolic;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class SimpleCharStreamTest {

    private SimpleCharStream charStream;

    @Before
    public void setUp() {
        String input = "Hello\nWorld\nThis is a test.\n";
        charStream = new SimpleCharStream(new StringReader(input));
        charStream.backup(0);
        charStream.FillBuff();
    }

    @After
    public void tearDown() {
        charStream.Done();
    }

    @Test
    public void testBeginToken() throws IOException {
        char c = charStream.BeginToken();
        assertEquals('\n', c);
        assertEquals(-1, charStream.tokenBegin);

        c = charStream.BeginToken();
        assertEquals('H', c);
        assertEquals(0, charStream.tokenBegin);

        charStream.backup(1);
        c = charStream.BeginToken();
        assertEquals('e', c);
        assertEquals(0, charStream.tokenBegin);
    }

    @Test
    public void testUpdateLineColumn() throws IOException {
        char c = charStream.BeginToken();
        charStream.UpdateLineColumn(c);
        assertEquals(1, charStream.line);
        assertEquals(1, charStream.column);

        c = charStream.readChar();
        charStream.UpdateLineColumn(c);
        assertEquals(1, charStream.line);
        assertEquals(2, charStream.column);

        c = '\n';
        charStream.UpdateLineColumn(c);
        assertEquals(2, charStream.line);
        assertEquals(1, charStream.column);
    }

    @Test
    public void testReadChar() throws IOException {
        char c = charStream.readChar();
        assertEquals('\n', c);
        assertEquals(0, charStream.bufpos);

        c = charStream.readChar();
        assertEquals('H', c);
        assertEquals(1, charStream.bufpos);

        c = charStream.readChar();
        assertEquals('e', c);
        assertEquals(2, charStream.bufpos);

        c = '\r';
        charStream.UpdateLineColumn(c);
        charStream.backup(1);
        c = charStream.readChar();
        assertEquals('\n', c);
        assertEquals(2, charStream.bufpos);

        charStream.backup(1);
        c = charStream.readChar();
        assertEquals('l', c);
        assertEquals(1, charStream.bufpos);

        charStream.backup(1);
        c = charStream.readChar();
        assertEquals('o', c);
        assertEquals(0, charStream.bufpos);

        charStream.backup(1);
        c = charStream.readChar();
        assertNull(c);
        assertEquals(-1, charStream.bufpos);
    }

    @Test
    public void testGetLineColumn() throws IOException {
        char c = charStream.BeginToken();
        charStream.UpdateLineColumn(c);

        assertEquals(1, charStream.getLine());
        assertEquals(1, charStream.getColumn());

        c = charStream.readChar();
        charStream.UpdateLineColumn(c);

        assertEquals(1, charStream.getLine());
        assertEquals(2, charStream.getColumn());

        c = '\n';
        charStream.UpdateLineColumn(c);

        assertEquals(2, charStream.getLine());
        assertEquals(1, charStream.getColumn());
    }

    @Test
    public void testBackup() throws IOException {
        char c = charStream.BeginToken();
        charStream.UpdateLineColumn(c);

        assertEquals(1, charStream.getLine());
        assertEquals(1, charStream.getColumn());

        charStream.backup(1);

        assertEquals(1, charStream.getLine());
        assertEquals(0, charStream.getColumn());

        charStream.backup(1);

        assertEquals(1, charStream.getLine());
        assertEquals(-1, charStream.tokenBegin);
    }

    @Test
    public void testExpandBuff() throws IOException {
        char[] buffer = new char[10];
        SimpleCharStream charStream = new SimpleCharStream(new StringReader(""), 1, 1, buffer.length);

        char c = charStream.BeginToken();
        assertEquals('\n', c);
        assertEquals(-1, charStream.tokenBegin);

        for (int i = 0; i < buffer.length; i++) {
            c = charStream.BeginToken();
            charStream.UpdateLineColumn(c);
            assertEquals(1, charStream.line);
            assertEquals(i + 1, charStream.column);
            assertEquals(i, charStream.bufpos);
            charStream.Backup(1);
            assertEquals(i, charStream.bufpos);
        }

        charStream.backup(buffer.length);
        assertEquals(-1, charStream.tokenBegin);
        assertEquals(-1, charStream.bufpos);
    }

    @Test
    public void testFillBuff() throws IOException {
        char[] buffer = new char[10];
        SimpleCharStream charStream = new SimpleCharStream(new StringReader(""), 1, 1, buffer.length);

        char c = charStream.BeginToken();
        assertEquals('\n', c);
        assertEquals(-1, charStream.tokenBegin);

        charStream.FillBuff();
        for (int i = 0; i < buffer.length; i++) {
            c = charStream.readChar();
            assertEquals('', c);
            assertEquals(i, charStream.bufpos);
        }

        charStream.backup(buffer.length);
        assertEquals(-1, charStream.tokenBegin);
        assertEquals(-1, charStream.bufpos);
    }

    @Test
    public void testGetImage() {
        String input = "Hello\nWorld\nThis is a test.\n";
        charStream = new SimpleCharStream(new StringReader(input));
        charStream.backup(0);
        charStream.FillBuff();

        String expected = "Hello\nWorld\nThis is a test.\n";
        String result = charStream.GetImage();
        assertEquals(expected, result);
    }

    @Test
    public void testGetSuffix() {
        String input = "Hello\nWorld\nThis is a test.\n";
        charStream = new SimpleCharStream(new StringReader(input));
        charStream.backup(0);
        charStream.FillBuff();

        String expected = "test.\n";
        String result = charStream.GetSuffix(expected.length());
        assertEquals(expected, result);
    }

    @Test
    public void testDone() throws IOException {
        char c = charStream.BeginToken();
        charStream.UpdateLineColumn(c);

        charStream.Done();

        assertEquals(null, charStream.buffer);
        assertEquals(null, charStream.bufline);
        assertEquals(null, charStream.bufcolumn);
    }

    @Test
    public void testAdjustBeginLineColumn() {
        String input = "1\n2\n3\n4\n5\n";
        charStream = new SimpleCharStream(new StringReader(input));
        charStream.backup(0);
        charStream.FillBuff();

        charStream.adjustBeginLineColumn(3, 1);

        assertEquals(3, charStream.line);
        assertEquals(1, charStream.column);
        assertEquals(4, charStream.bufline[0]);
        assertEquals(4, charStream.bufcolumn[0]);
        assertEquals(3, charStream.bufline[1]);
        assertEquals(1, charStream.bufcolumn[1]);
        assertEquals(3, charStream.bufline[2]);
        assertEquals(2, charStream.bufcolumn[2]);
        assertEquals(3, charStream.bufline[3]);
        assertEquals(5, charStream.bufline[4]);
        assertEquals(5, charStream.bufcolumn[4]);
        assertEquals(3, charStream.bufline[5]);
        assertEquals(3, charStream.bufcolumn[5]);
    }

    @Test
    public void testNullInput() {
        charStream = new SimpleCharStream(null, 1, 1, 4096);

        assertFalse(charStream.inputStream instanceof StringReader);
        assertNull(charStream.buffer);
        assertNull(charStream.bufline);
        assertNull(charStream.bufcolumn);
    }

    @Test
    public void testInvalidInput() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Hello\nWorld\nThis is a test.\n".getBytes());
        charStream = new SimpleCharStream(inputStream, 1, 1, 4096);

        char c = charStream.BeginToken();
        charStream.UpdateLineColumn(c);

        assertEquals(1, charStream.line);
        assertEquals(1, charStream.column);

        charStream. backup(1);
        assertEquals(1, charStream.line);
        assertEquals(0, charStream.column);

        charStream. backup(1);
        assertEquals(1, charStream.line);
        assertEquals(-1, charStream.tokenBegin);

        charStream.FillBuff();

        char[] buffer = new char[10];
        System.arraycopy(charStream.buffer, 0, buffer, 0, 10);

        String expected = "Hello\nWorld\nThis is a test.\n";
        String result = new String(buffer);
        assertEquals(expected, result);
    }
}