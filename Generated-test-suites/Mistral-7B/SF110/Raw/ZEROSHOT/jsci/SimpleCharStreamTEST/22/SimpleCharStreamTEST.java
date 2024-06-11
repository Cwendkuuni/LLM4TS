package jsci.maths.symbolic.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SimpleCharStreamTest {

    private SimpleCharStream sut;

    @Before
    public void setUp() {
        String testString = "line1\ncolumn1 tab\r\nline2\ncolumn2";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testString.getBytes());
        sut = new SimpleCharStream(new InputStreamReader(inputStream), 1, 1);
    }

    @After
    public void tearDown() {
        SimpleCharStream.Done();
    }

    @Test
    public void testBeginToken() throws IOException {
        char expected = 'l';
        char actual = sut.BeginToken();
        assertEquals(expected, actual);
        assertEquals(1, sut.getLine());
        assertEquals(1, sut.getColumn());
    }

    @Test
    public void testUpdateLineColumn() throws IOException {
        for (char character : "line1\ncolumn1 tab\r\nline2\ncolumn2".toCharArray()) {
            sut.readChar();
            sut.UpdateLineColumn(character);
        }

        int expectedLine = 2;
        int expectedColumn = 3;
        int actualLine = sut.getLine();
        int actualColumn = sut.getColumn();
        assertEquals(expectedLine, actualLine);
        assertEquals(expectedColumn, actualColumn);
    }

    @Test
    public void testReadChar() throws IOException {
        char expected = 'l';
        char actual = sut.readChar();
        assertEquals(expected, actual);
        assertEquals(1, sut.getLine());
        assertEquals(1, sut.getColumn());
    }

    @Test
    public void testGetColumn() {
        assertEquals(1, sut.getColumn());
    }

    @Test
    public void testGetLine() {
        assertEquals(1, sut.getLine());
    }

    @Test
    public void testGetEndColumn() {
        assertEquals(1, sut.getEndColumn());
    }

    @Test
    public void testGetEndLine() {
        assertEquals(1, sut.getEndLine());
    }

    @Test
    public void testGetBeginColumn() {
        assertEquals(1, sut.getBeginColumn());
    }

    @Test
    public void testGetBeginLine() {
        assertEquals(1, sut.getBeginLine());
    }

    @Test(expected = IOException.class)
    public void testReadCharWhenEndOfStreamReached() throws IOException {
        sut.readChar();
        sut.readChar();
        sut.readChar();
        sut.readChar();
        sut.readChar();
        sut.readChar();
        sut.readChar();
        sut.readChar();

        sut.readChar();
    }

    @Test
    public void testBackup() throws IOException {
        sut.readChar();
        sut.readChar();
        sut.backup(1);

        char expected = 'l';
        char actual = sut.readChar();
        assertEquals(expected, actual);
        assertEquals(1, sut.getLine());
        assertEquals(1, sut.getColumn());
    }

    @Test
    public void testReInit() throws IOException {
        sut.readChar();
        sut.readChar();

        byte[] testString2 = "line3\ncolumn3 tab\r\nline4\ncolumn4".getBytes();
        ByteArrayInputStream inputStream2 = new ByteArrayInputStream(testString2);
        sut.ReInit(new InputStreamReader(inputStream2), 3, 3);

        char expected = 'l';
        char actual = sut.readChar();
        assertEquals(3, sut.getLine());
        assertEquals(3, sut.getColumn());
    }

    @Test
    public void testGetImage() throws IOException {
        StringBuilder expected = new StringBuilder();
        char character;
        while ((character = sut.readChar()) != -1) {
            expected.append(character);
        }

        String actual = SimpleCharStream.GetImage();
        assertEquals(expected.toString(), actual);
    }

    @Test
    public void testGetSuffix() throws IOException {
        int suffixLength = 3;
        char[] expected = new char[suffixLength];

        for (int i = 0; i < suffixLength; i++) {
            expected[i] = sut.readChar();
        }

        char[] actual = SimpleCharStream.GetSuffix(suffixLength);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testDone() throws IOException {
        sut.readChar();
        sut.readChar();

        SimpleCharStream.Done();
        assertNull(sut.getBeginLine());
        assertNull(sut.getBeginColumn());
        assertNull(sut.getEndLine());
        assertNull(sut.getEndColumn());
    }

    @Test
    public void testAdjustBeginLineColumn() throws IOException {
        int newLine = 5;
        int newColumn = 5;

        sut.readChar();
        sut.readChar();
        sut.readChar();
        sut.readChar();
        sut.readChar();
        sut.readChar();
        sut.readChar();
        sut.readChar();
        sut.readChar();
        sut.readChar();
        sut.readChar();
        sut.readChar();
        sut.readChar();
        sut.readChar();
        sut.readChar();
        sut.readChar();
        sut.readChar();
        sut.readChar();
        sut.readChar();
        sut.readChar();

        SimpleCharStream.adjustBeginLineColumn(newLine, newColumn);

        int expectedLine = 6;
        int expectedColumn = 6;
        int actualLine = sut.getLine();
        int actualColumn = sut.getColumn();
        assertEquals(expectedLine, actualLine);
        assertEquals(expectedColumn, actualColumn);
    }
}