package JSci.maths.symbolic;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class SimpleCharStreamTest {

    private static final int DEFAULT_BUFFER_SIZE = 4096;
    private SimpleCharStream scs;
    private int line = 1;
    private int column = 0;
    private Reader inputStream;

    @Before
    public void setUp() throws IOException {
        this.inputStream = new ByteArrayInputStream("Test Data".getBytes());
        this.scs = new SimpleCharStream(this.inputStream, line, column, DEFAULT_BUFFER_SIZE);
    }

    @After
    public void tearDown() {
        SimpleCharStream.Done();
    }

    @Test
    public void testBeginToken_ReturnsExpectedChar() throws IOException {
        final char expected = (char) 0x42; // 'B'
        final char actual = this.scs.BeginToken();
        assertEquals(expected, actual);
        assertEquals(column + 1, this.scs.getColumn());
        assertEquals(line, this.scs.getLine());
        assertEquals(column + 1, this.scs.getBeginColumn());
        assertEquals(line, this.scs.getBeginLine());
    }

    @Test(expected = IOException.class)
    public void testBeginToken_ThrowsIOException_WhenEndOfStreamReached() throws IOException {
        this.scs.FillBuff();
        this.scs.BeginToken();
    }

    @Test
    public void testExpandBuff_ExpandsBufferWhenRequired() {
        this.scs.ExpandBuff(false);
        assertEquals(DEFAULT_BUFFER_SIZE, SimpleCharStream.bufsize);
        this.scs.ExpandBuff(true);
        assertEquals(DEFAULT_BUFFER_SIZE * 2, SimpleCharStream.bufsize);
    }

    @Test(expected = Error.class)
    public void testExpandBuff_ThrowsError_WhenExceptionThrownDuringExpansion() throws IOException {
        this.scs.ExpandBuff(true);
        throw new RuntimeException("Test Exception");
    }

    @Test
    public void testFillBuff_FillsBufferWithDataFromInputStream() throws IOException {
        final int expected = 3; // 'T', 'e', 's'
        this.scs.FillBuff();
        assertEquals(expected, this.scs.maxNextCharInd);
        assertEquals((char) 0x54, this.scs.buffer[0]); // 'T'
        assertEquals((char) 0x65, this.scs.buffer[1]); // 'e'
        assertEquals((char) 0x73, this.scs.buffer[2]); // 's'
    }

    @Test
    public void testFillBuff_RefillsBufferWhenRequired() throws IOException {
        this.scs.FillBuff();
        this.scs.ReadAll();
        assertEquals(column + 1, this.scs.getColumn());
        assertEquals(line + 1, this.scs.getLine());
        assertTrue(this.scs.FillBuff());
        assertEquals(3, this.scs.available);
        assertEquals((char) 0x74, this.scs.readChar()); // 't'
        assertEquals((char) 0x68, this.scs.readChar()); // 'h'
        assertEquals((char) 0x65, this.scs.readChar()); // 'e'
        assertFalse(this.scs.available > 0);
    }

    @Test(expected = IOException.class)
    public void testFillBuff_ThrowsIOException_WhenExceptionThrownDuringRead() throws IOException {
        this.scs.FillBuff();
        throw new RuntimeException("Test Exception");
    }

    @Test(expected = IOException.class)
    public void testReadChar_ThrowsIOException_WhenEndOfStreamReached() throws IOException {
        this.scs.FillBuff();
        this.scs.ReadAll();
        this.scs.readChar();
        this.scs.readChar();
        this.scs.readChar();
        this.scs.readChar();
    }

    @Test
    public void testUpdateLineColumn_UpdatesLineAndColumn() throws IOException {
        this.scs.BeginToken();
        this.scs.UpdateLineColumn('\r');
        this.scs.UpdateLineColumn('\n');
        this.scs.UpdateLineColumn('T');
        this.scs.UpdateLineColumn('e');
        assertEquals(line + 1, this.scs.getLine());
        assertEquals(column + 5, this.scs.getColumn());
    }

    @Test
    public void testGetLineColumnInfo_ReturnsExpectedValuesForTokens() throws IOException {
        this.scs.BeginToken();
        this.scs.UpdateLineColumn('\r');
        this.scs.UpdateLineColumn('\n');
        this.scs.UpdateLineColumn('T');
        this.scs.UpdateLineColumn('e');
        this.scs.UpdateLineColumn(' ');
        this.scs.UpdateLineColumn('s');

        final int expectedLine = line + 1;
        final int expectedColumn = column + 6;

        assertEquals(expectedLine, this.scs.getLine());
        assertEquals(expectedColumn, this.scs.getColumn());
        assertEquals(expectedColumn, this.scs.getBeginColumn());
        assertEquals(expectedLine, this.scs.getBeginLine());
    }

    @Test(expected = IOException.class)
    public void testGetImage_ThrowsIOException_WhenEndOfStreamReached() throws IOException {
        this.scs.FillBuff();
        this.scs.ReadAll();
        this.scs.GetImage();
    }

    @Test
    public void testGetSuffix_ReturnsExpectedSubstring() throws IOException {
        final int suffixLength = 3;
        this.scs.BeginToken();
        this.scs.UpdateLineColumn('\r');
        this.scs.UpdateLineColumn('\n');
        this.scs.UpdateLineColumn('T');
        this.scs.UpdateLineColumn('e');
        this.scs.UpdateLineColumn(' ');
        this.scs.UpdateLineColumn('s');
        this.scs.UpdateLineColumn('s');
        this.scs.UpdateLineColumn('u');
        this.scs.UpdateLineColumn('f');
        this.scs.UpdateLineColumn('f');
        this.scs.UpdateLineColumn('x');

        final char[] expected = "suf".toCharArray();
        assertNotNull(this.scs.GetSuffix(suffixLength));
        assertSame(expected, this.scs.GetSuffix(suffixLength));
    }

    @Test
    public void testDone_ReleasesResources() throws IOException {
        this.scs.FillBuff();
        this.scs.ReadAll();
        SimpleCharStream.Done();
        assertNull(SimpleCharStream.buffer);
        assertNull(SimpleCharStream.bufline);
        assertNull(SimpleCharStream.bufcolumn);
    }

    @Test
    public void testAdjustBeginLineColumn_AdjustsLineAndColumn() throws IOException {
        final int newLine = 5;
        final int newColumn = 10;

        this.scs.BeginToken();
        this.scs.UpdateLineColumn('\r');
        this.scs.UpdateLineColumn('\n');
        this.scs.UpdateLineColumn('T');
        this.scs.UpdateLineColumn('e');
        this.scs.UpdateLineColumn(' ');
        this.scs.UpdateLineColumn('s');

        this.scs.adjustBeginLineColumn(newLine, newColumn);

        final int expectedNewLine = newLine;
        final int expectedNewColumn = newColumn;

        assertEquals(expectedNewLine, this.scs.getBeginLine());
        assertEquals(expectedNewColumn, this.scs.getBeginColumn());
    }
}