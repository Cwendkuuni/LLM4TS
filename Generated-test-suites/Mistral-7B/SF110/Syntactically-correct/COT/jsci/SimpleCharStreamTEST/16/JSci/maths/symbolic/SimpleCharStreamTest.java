package JSci.maths.symbolic.test;

import org.junit.Assert;
import org.junit.Test;

import JSci.maths.symbolic.SimpleCharStream;

public class SimpleCharStreamTest {

    @Test
    public void testExpandBuff() throws Exception {
        SimpleCharStream stream = new SimpleCharStream(new StringReader("test"), 1, 1, 1024);
        for (int i = 0; i < 1024; i++) {
            stream.readChar();
        }
        stream.ExpandBuff(false);
        int initialSize = SimpleCharStream.bufsize;
        stream.ExpandBuff(false);
        int newSize = SimpleCharStream.bufsize;
        Assert.assertTrue("Buffer size should have been increased", newSize > initialSize);
        Assert.assertEquals("Expected available to be 0 after expanding buffer", 0, SimpleCharStream.available);
        Assert.assertEquals("Expected tokenBegin to be 0 after expanding buffer", 0, SimpleCharStream.tokenBegin);
    }

    @Test
    public void testFillBuff() throws Exception {
        SimpleCharStream stream = new SimpleCharStream(new StringReader("test"), 1, 1, 1024);
        for (int i = 0; i < 1024; i++) {
            stream.readChar();
        }
        stream.FillBuff();
        char c = stream.readChar();
        Assert.assertNotNull("Expected to read a character", c);
    }

    @Test(expected = IOException.class)
    public void testFillBuffWithIOException() throws Exception {
        SimpleCharStream stream = new SimpleCharStream(new StringReader("test"), 1, 1, 1024);
        for (int i = 0; i < 1024; i++) {
            stream.readChar();
        }
        SimpleCharStream.inputStream.close();
        stream.FillBuff();
    }

    @Test
    public void testBeginToken() throws Exception {
        SimpleCharStream stream = new SimpleCharStream(new StringReader("test"), 1, 1, 1024);
        char c = stream.BeginToken();
        Assert.assertNotNull("Expected to read a character", c);
    }

    @Test
    public void testUpdateLineColumn() throws Exception {
        SimpleCharStream stream = new SimpleCharStream(new StringReader("test\r\nline1\tcolumn1\r\nline2\tcolumn2\r\nline3\tcolumn3"), 1, 1, 1024);
        for (int i = 0; i < 18; i++) {
            stream.readChar();
        }
        char c = stream.readChar();
        stream.UpdateLineColumn(c);
        int line = SimpleCharStream.bufline[SimpleCharStream.bufpos];
        int column = SimpleCharStream.bufcolumn[SimpleCharStream.bufpos];
        Assert.assertEquals("Expected line to be 2", 2, line);
        Assert.assertEquals("Expected column to be 1 or 2 depending on the character read", 1, column);
    }

    @Test
    public void testGetLineAndColumn() throws Exception {
        SimpleCharStream stream = new SimpleCharStream(new StringReader("test\r\nline1\tcolumn1\r\nline2\tcolumn2\r\nline3\tcolumn3"), 1, 1, 1024);
        for (int i = 0; i < 18; i++) {
            stream.readChar();
        }
        char c = stream.readChar();
        int line = SimpleCharStream.getLine();
        int column = SimpleCharStream.getColumn();
        Assert.assertEquals("Expected line to be 2", 2, line);
        Assert.assertEquals("Expected column to be 1 or 2 depending on the character read", 1, column);
    }

    @Test
    public void testBackup() throws Exception {
        SimpleCharStream stream = new SimpleCharStream(new StringReader("test\r\nline1\tcolumn1\r\nline2\tcolumn2\r\nline3\tcolumn3"), 1, 1, 1024);
        for (int i = 0; i < 18; i++) {
            stream.readChar();
        }
        char c = stream.readChar();
        int backupAmount = 1;
        stream.backup(backupAmount);
        int line = SimpleCharStream.getLine();
        int column = SimpleCharStream.getColumn();
        Assert.assertEquals("Expected line to be 2", 2, line);
        Assert.assertEquals("Expected column to be 1 or 2 depending on the character read", 1, column);
        Assert.assertEquals("Expected char at previous position to be the same as the one read before", c, SimpleCharStream.buffer[SimpleCharStream.bufpos]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBackupWithNegativeAmount() throws Exception {
        SimpleCharStream stream = new SimpleCharStream(new StringReader("test"), 1, 1, 1024);
        for (int i = 0; i < 1024; i++) {
            stream.readChar();
        }
        stream.backup(-1);
    }

    @Test
    public void testSimpleCharStreamConstructor() {
        SimpleCharStream stream = new SimpleCharStream(new StringReader("test"), 1, 1, 1024);
        Assert.assertNotNull(SimpleCharStream.inputStream);
        Assert.assertEquals(1, SimpleCharStream.line);
        Assert.assertEquals(1, SimpleCharStream.column);
        Assert.assertEquals(1024, SimpleCharStream.bufsize);
        Assert.assertEquals(1024, SimpleCharStream.available);
    }

    @Test
    public void testSimpleCharStreamConstructorWithStartlineAndStartcolumn() {
        SimpleCharStream stream = new SimpleCharStream(new StringReader("test"), 2, 2, 1024);
        Assert.assertNotNull(SimpleCharStream.inputStream);
        Assert.assertEquals(2, SimpleCharStream.line);
        Assert.assertEquals(2, SimpleCharStream.column);
        Assert.assertEquals(1024, SimpleCharStream.bufsize);
        Assert.assertEquals(1024, SimpleCharStream.available);
    }

    @Test
    public void testSimpleCharStreamConstructorWithReaderOnly() {
        SimpleCharStream stream = new SimpleCharStream(new StringReader("test"));
        Assert.assertNotNull(SimpleCharStream.inputStream);
        Assert.assertEquals(1, SimpleCharStream.line);
        Assert.assertEquals(1, SimpleCharStream.column);
        Assert.assertEquals(4096, SimpleCharStream.bufsize);
        Assert.assertEquals(4096, SimpleCharStream.available);
    }

    @Test
    public void testGetImage() throws Exception {
        SimpleCharStream stream = new SimpleCharStream(new StringReader("test"), 1, 1, 1024);
        for (int i = 0; i < 1024; i++) {
            stream.readChar();
        }
        String image = SimpleCharStream.GetImage();
        Assert.assertEquals("Expected the image to be the same as the input string", "testtesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttest", image);
    }

    @Test
    public void testGetSuffix() throws Exception {
        SimpleCharStream stream = new SimpleCharStream(new StringReader("testtesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttest"), 1, 1, 1024);
        for (int i = 0; i < 1023; i++) {
            stream.readChar();
        }
        char[] suffix = SimpleCharStream.GetSuffix(5);
        String expectedSuffix = "test";
        Assert.assertEquals("Expected suffix to be 'testtesttesttesttesttest'", expectedSuffix, new String(suffix));
    }

    @Test
    public void testDone() throws Exception {
        SimpleCharStream stream = new SimpleCharStream(new StringReader("test"), 1, 1, 1024);
        for (int i = 0; i < 1024; i++) {
            stream.readChar();
        }
        SimpleCharStream.Done();
        Assert.assertNull(SimpleCharStream.buffer);
        Assert.assertNull(SimpleCharStream.bufline);
        Assert.assertNull(SimpleCharStream.bufcolumn);
    }

    @Test
    public void testAdjustBeginLineColumn() throws Exception {
        SimpleCharStream stream = new SimpleCharStream(new StringReader("test\r\nline1\tcolumn1\r\nline2\tcolumn2\r\nline3\tcolumn3"), 1, 1, 1024);
        for (int i = 0; i < 18; i++) {
            stream.readChar();
        }
        int newLine = 5;
        int newColumn = 10;
        SimpleCharStream.adjustBeginLineColumn(newLine, newColumn);
        int line = SimpleCharStream.getLine();
        int column = SimpleCharStream.getColumn();
        Assert.assertEquals("Expected line to be 5", 5, line);
        Assert.assertEquals("Expected column to be 10", 10, column);
    }
}