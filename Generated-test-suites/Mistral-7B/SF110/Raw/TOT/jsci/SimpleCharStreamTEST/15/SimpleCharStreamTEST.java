package JSci.maths.symbolic.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.Assert.*;

public class SimpleCharStreamTest {

    private static final String TEST_FILE = "TestData.txt";
    private SimpleCharStream scs;
    private ByteArrayOutputStream outputStream;
    private ByteArrayInputStream inputStream;

    @Before
    public void setUp() throws IOException {
        outputStream = new ByteArrayOutputStream();
        System.setIn(inputStream = new ByteArrayInputStream(TEST_FILE.getBytes()));
        scs = new SimpleCharStream(inputStream, 1, 1);
    }

    @After
    public void tearDown() throws Exception {
        scs.Done();
        System.setIn(System.in);
    }

    @Test
    public void testBeginToken() throws IOException {
        char c = scs.BeginToken();
        assertEquals('\u0000', c);
        c = scs.readChar();
        assertEquals(c, scs.BeginToken());
        assertEquals(c, scs.readChar());

        int line = scs.getLine();
        int column = scs.getColumn();
        assertEquals(1, line);
        assertEquals(1, column);
    }

    @Test
    public void testFillBuff() throws IOException {
        scs.FillBuff();
        char c = scs.readChar();
        UpdateLineColumn(c);
        assertEquals(c, scs.readChar());

        line = scs.getLine();
        column = scs.getColumn();
        assertEquals(1, line);
        assertEquals(2, column);

        scs.FillBuff();
        c = scs.readChar();
        UpdateLineColumn(c);
        assertEquals(c, scs.readChar());

        line = scs.getLine();
        column = scs.getColumn();
        assertEquals(1, line);
        assertEquals(3, column);
    }

    @Test(expected = IOException.class)
    public void testFillBuffException() throws IOException {
        scs.FillBuff();
        scs.inputStream.close();
        scs.FillBuff();
    }

    @Test
    public void testUpdateLineColumn() throws IOException {
        char c = scs.BeginToken();
        UpdateLineColumn(c);
        assertEquals(1, scs.getLine());
        assertEquals(1, scs.getColumn());

        c = scs.readChar();
        UpdateLineColumn(c);
        assertEquals(1, scs.getLine());
        assertEquals(2, scs.getColumn());

        c = '\r';
        UpdateLineColumn(c);
        assertEquals(1, scs.getLine());
        assertEquals(1, scs.getColumn());

        c = '\n';
        UpdateLineColumn(c);
        assertEquals(2, scs.getLine());
        assertEquals(1, scs.getColumn());

        c = '\t';
        UpdateLineColumn(c);
        assertEquals(2, scs.getLine());
        assertEquals(9, scs.getColumn());
    }

    @Test
    public void testExpandBuff() throws IOException {
        int size = 1024;
        SimpleCharStream scs1 = new SimpleCharStream(inputStream, 1, 1, size);
        scs1.FillBuff();
        scs1.FillBuff();
        SimpleCharStream scs2 = new SimpleCharStream(inputStream, 1, 1, size * 2);
        scs2.FillBuff();
        scs2.FillBuff();

        int i = 0;
        char c1, c2;
        while ((c1 = scs1.readChar()) != '\u0000' && (c2 = scs2.readChar()) != '\u0000') {
            assertEquals(c1, c2);
            ++i;
        }
        assertEquals(i, size);
    }

    @Test
    public void testBackup() throws IOException {
        char[] suffix = SimpleCharStream.GetSuffix(5);
        scs.backup(3);
        for (int i = 0; i < 3; ++i) {
            assertEquals(suffix[i], scs.readChar());
        }

        scs.backup(5);
        for (int i = 0; i < 5; ++i) {
            assertEquals(suffix[i], scs.readChar());
        }
    }

    @Test
    public void testReInit() throws IOException {
        scs.backup(10);
        int line = scs.getLine();
        int column = scs.getColumn();
        int endLine = scs.getEndLine();
        int endColumn = scs.getEndColumn();
        int beginLine = scs.getBeginLine();
        int beginColumn = scs.getBeginColumn();

        scs.ReInit(inputStream, 1, 1);
        line = scs.getLine();
        column = scs.getColumn();
        endLine = scs.getEndLine();
        endColumn = scs.getEndColumn();
        beginLine = scs.getBeginLine();
        beginColumn = scs.getBeginColumn();

        assertEquals(1, line);
        assertEquals(1, column);
        assertEquals(beginLine, endLine);
        assertEquals(beginColumn, endColumn);
    }

    @Test
    public void testReadChar() throws IOException {
        char c;
        for (int i = 0; i < TEST_FILE.length(); ++i) {
            c = scs.readChar();
            UpdateLineColumn(c);
            assertEquals(TEST_FILE.charAt(i), c);
        }
    }

    @Test
    public void testGetImage() throws IOException {
        String image = SimpleCharStream.GetImage();
        assertEquals(TEST_FILE, image);
    }

    private void UpdateLineColumn(char c) {
        scs.UpdateLineColumn(c);
    }
}