package JSci.maths.symbolic;

import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleCharStreamTest {

    @Test
    public void testExpandBuff() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new StringReader("test"), 1, 1, 1024);
        for (int i = 0; i < 1030; i++) {
            scs.readChar();
        }
        scs.ExpandBuff(false);
        assertEquals(2048, scs.bufsize);
        assertEquals(2048, scs.available);
        assertEquals(0, scs.tokenBegin);
    }

    @Test
    public void testExpandBuffWrapAround() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new StringReader("test"), 1, 1, 1024);
        for (int i = 0; i < 1030; i++) {
            scs.readChar();
        }
        scs.ExpandBuff(true);
        assertEquals(4096, scs.bufsize);
        assertEquals(4096, scs.available);
        assertEquals(-1024, scs.tokenBegin);
    }

    @Test(expected = IOException.class)
    public void testFillBuffException() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new StringReader("test"), 1, 1, 1024);
        for (int i = 0; i < 1030; i++) {
            scs.readChar();
        }
        SimpleCharStream.inputStream.close();
        scs.FillBuff();
    }

    @Test
    public void testFillBuff() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new StringReader("test"), 1, 1, 1024);
        for (int i = 0; i < 1030; i++) {
            scs.readChar();
        }
        scs.FillBuff();
        assertEquals(2048, scs.available);
    }

    @Test
    public void testBeginToken() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new StringReader("test"), 1, 1, 1024);
        assertEquals('t', scs.BeginToken());
        assertEquals(0, scs.tokenBegin);
    }

    @Test
    public void testUpdateLineColumn() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new StringReader("test\r\nfoo\r\nbar"), 1, 1, 1024);
        scs.UpdateLineColumn('\r');
        assertEquals(1, scs.line);
        assertEquals(1, scs.column);
        scs.UpdateLineColumn('\n');
        assertEquals(2, scs.line);
        assertEquals(1, scs.column);
        scs.UpdateLineColumn('f');
        assertEquals(2, scs.line);
        assertEquals(2, scs.column);
        scs.UpdateLineColumn('\t');
        assertEquals(2, scs.line);
        assertEquals(11, scs.column);
        scs.UpdateLineColumn('\r');
        assertEquals(3, scs.line);
        assertEquals(1, scs.column);
        scs.UpdateLineColumn('\n');
        assertEquals(4, scs.line);
        assertEquals(1, scs.column);
        scs.UpdateLineColumn('b');
        assertEquals(4, scs.line);
        assertEquals(2, scs.column);
    }

    @Test
    public void testReadChar() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new StringReader("test"), 1, 1, 1024);
        assertEquals('t', scs.readChar());
        assertEquals(1, scs.line);
        assertEquals(1, scs.column);
        assertEquals('e', scs.readChar());
        assertEquals(1, scs.line);
        assertEquals(2, scs.column);
        assertEquals('s', scs.readChar());
        assertEquals(1, scs.line);
        assertEquals(3, scs.column);
        assertEquals('\r', scs.readChar());
        assertEquals(1, scs.line);
        assertEquals(3, scs.column);
        assertEquals('t', scs.readChar());
        assertEquals(1, scs.line);
        assertEquals(4, scs.column);
        assertEquals('\n', scs.readChar());
        assertEquals(2, scs.line);
        assertEquals(1, scs.column);
        assertEquals('f', scs.readChar());
        assertEquals(2, scs.line);
        assertEquals(2, scs.column);
        assertEquals('\r', scs.readChar());
        assertEquals(3, scs.line);
        assertEquals(1, scs.column);
        assertEquals('\n', scs.readChar());
        assertEquals(4, scs.line);
        assertEquals(1, scs.column);
        assertEquals('b', scs.readChar());
        assertEquals(4, scs.line);
        assertEquals(2, scs.column);
        assertEquals('\0', scs.readChar());
        assertEquals(Error.class, scs.readCharException());
    }

    @Test
    public void testGetColumn() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new StringReader("test"), 1, 1, 1024);
        assertEquals(1, scs.getColumn());
        scs.UpdateLineColumn('t');
        assertEquals(1, scs.line);
        assertEquals(2, scs.column);
        scs.UpdateLineColumn('\r');
        assertEquals(1, scs.line);
        assertEquals(3, scs.column);
        scs.UpdateLineColumn('\n');
        assertEquals(2, scs.line);
        assertEquals(1, scs.column);
        scs.UpdateLineColumn('f');
        assertEquals(2, scs.line);
        assertEquals(2, scs.column);
        scs.UpdateLineColumn('\t');
        assertEquals(2, scs.line);
        assertEquals(11, scs.column);
        scs.UpdateLineColumn('\r');
        assertEquals(3, scs.line);
        assertEquals(1, scs.column);
        scs.UpdateLineColumn('\n');
        assertEquals(4, scs.line);
        assertEquals(1, scs.column);
        scs.UpdateLineColumn('b');
        assertEquals(4, scs.line);
        assertEquals(2, scs.column);
        scs.UpdateLineColumn('\0');
        assertEquals(4, scs.line);
        assertEquals(3, scs.column);
    }

    @Test
    public void testGetLine() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new StringReader("test\r\nfoo\r\nbar"), 1, 1, 1024);
        assertEquals(1, scs.getLine());
        scs.UpdateLineColumn('\r');
        assertEquals(1, scs.line);
        scs.UpdateLineColumn('\n');
        assertEquals(2, scs.line);
        scs.UpdateLineColumn('f');
        assertEquals(2, scs.line);
        scs.UpdateLineColumn('\r');
        assertEquals(3, scs.line);
        scs.UpdateLineColumn('\n');
        assertEquals(4, scs.line);
        scs.UpdateLineColumn('b');
        assertEquals(4, scs.line);
    }

    @Test
    public void testGetEndLine() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new StringReader("test\r\nfoo\r\nbar"), 1, 1, 1024);
        assertEquals(4, scs.getEndLine());
    }

    @Test
    public void testGetBeginLine() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new StringReader("test\r\nfoo\r\nbar"), 1, 1, 1024);
        assertEquals(1, scs.getBeginLine());
    }

    @Test
    public void testGetBeginColumn() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new StringReader("test\r\nfoo\r\nbar"), 1, 1, 1024);
        assertEquals(1, scs.getBeginColumn());
    }

    @Test
    public void testGetEndColumn() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new StringReader("test\r\nfoo\r\nbar"), 1, 1, 1024);
        assertEquals(13, scs.getEndColumn());
    }

    @Test
    public void testBackup() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new StringReader("test"), 1, 1, 1024);
        assertEquals('t', scs.readChar());
        scs.backup(1);
        assertEquals('t', scs.readChar());
    }

    @Test
    public void testBackupNegative() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new StringReader("test"), 1, 1, 1024);
        assertEquals('t', scs.readChar());
        scs.backup(-1);
        assertEquals('t', scs.readChar());
    }

    @Test
    public void testBackupEndOfStream() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new StringReader("test"), 1, 1, 1024);
        for (int i = 0; i < 1025; i++) {
            scs.readChar();
        }
        scs.backup(1025);
        assertEquals('\0', scs.readChar());
    }

    @Test
    public void testReInit() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new StringReader("test"), 1, 1, 1024);
        for (int i = 0; i < 1030; i++) {
            scs.readChar();
        }
        SimpleCharStream.inputStream.close();
        scs.ReInit(new StringReader("testReInit"), 1, 1, 1024);
        assertEquals('t', scs.readChar());
        assertEquals(1, scs.line);
        assertEquals(1, scs.column);
    }

    @Test
    public void testReInitSameFile() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new StringReader("test"), 1, 1, 1024);
        for (int i = 0; i < 1030; i++) {
            scs.readChar();
        }
        scs.ReInit(new StringReader("test"), 1, 1, 1024);
        assertEquals('t', scs.readChar());
        assertEquals(1, scs.line);
        assertEquals(1, scs.column);
    }

    @Test
    public void testReInitNewFile() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new StringReader("test"), 1, 1, 1024);
        for (int i = 0; i < 1030; i++) {
            scs.readChar();
        }
        SimpleCharStream scs2 = new SimpleCharStream(new StringReader("test2"), 1, 1, 1024);
        scs.ReInit(scs2.inputStream, 1, 1, 1024);
        assertEquals('t', scs.readChar());
        assertEquals(1, scs.line);
        assertEquals(1, scs.column);
    }

    @Test
    public void testGetImage() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new StringReader("test"), 1, 1, 1024);
        assertEquals("test", new String(SimpleCharStream.GetImage()));
    }

    @Test
    public void testGetSuffix() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new StringReader("testfoo"), 1, 1, 1024);
        assertEquals("foo", new String(SimpleCharStream.GetSuffix(3)));
    }

    @Test
    public void testDone() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new StringReader("test"), 1, 1, 1024);
        SimpleCharStream.Done();
    }
}