import org.apache.commons.csv.ExtendedBufferedReader;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;

public class ExtendedBufferedReaderTest {

    @Test
    public void testDefaultConstructor() {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(""));
        Assert.assertNull(reader.readLine());
        Assert.assertEquals(-1, reader.getLineNumber());
    }

    @Test
    public void testReadCharacter() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("hello world\n"));

        Assert.assertEquals('h', reader.read());
        Assert.assertEquals('e', reader.read());
        Assert.assertEquals('l', reader.read());
        Assert.assertEquals('l', reader.read());
        Assert.assertEquals('o', reader.read());
        Assert.assertEquals('\n', reader.read());
        Assert.assertEquals('w', reader.read());
        Assert.assertEquals(' ', reader.read());
        Assert.assertEquals('r', reader.read());
        Assert.assertEquals('d', reader.read());
        Assert.assertEquals('\n', reader.read());
        Assert.assertEquals(-1, reader.read());
        Assert.assertEquals(-1, reader.readAgain());
        Assert.assertEquals(-1, reader.getLineNumber());
    }

    @Test
    public void testReadLine() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("hello world\n"));

        String line = reader.readLine();
        Assert.assertEquals("hello", line);
        Assert.assertEquals('w', reader.read());
        Assert.assertEquals('r', reader.read());
        Assert.assertEquals('d', reader.read());
        Assert.assertEquals('\n', reader.read());
        Assert.assertEquals(-1, reader.read());
        Assert.assertEquals("1", reader.readLine());
        Assert.assertEquals(-1, reader.readLine());
        Assert.assertEquals(-1, reader.readLine());
        Assert.assertEquals(-1, reader.readLine());
        Assert.assertEquals(-1, reader.readLine());
        Assert.assertEquals(-1, reader.readLine());
        Assert.assertEquals(-1, reader.readLine());
        Assert.assertEquals(-1, reader.readLine());
        Assert.assertEquals(-1, reader.readLine());
        Assert.assertEquals(-1, reader.readLine());
        Assert.assertEquals(-1, reader.readLine());
        Assert.assertEquals(-1, reader.readLine());
        Assert.assertEquals(-1, reader.readLine());
        Assert.assertEquals(-1, reader.readLine());
        Assert.assertEquals(-1, reader.readLine());
        Assert.assertEquals(-1, reader.readLine());
        Assert.assertEquals(-1, reader.readLine());

        Assert.assertEquals(12, reader.getLineNumber());
        Assert.assertEquals(-1, reader.readLine());
        Assert.assertEquals(-1, reader.readLine());
        Assert.assertEquals(-1, reader.readLine());
        Assert.assertEquals(-1, reader.readLine());
        Assert.assertEquals(-1, reader.readLine());
        Assert.assertEquals(-1, reader.readLine());
        Assert.assertEquals(-1, reader.readLine());
        Assert.assertEquals(-1, reader.readLine());
        Assert.assertEquals(-1, reader.readLine());
        Assert.assertEquals(-1, reader.readLine());
        Assert.assertEquals(-1, reader.readLine());
        Assert.assertEquals(-1, reader.readLine());
        Assert.assertEquals(-1, reader.readLine());
        Assert.assertEquals(-1, reader.readLine());
        Assert.assertEquals(-1, reader.readLine());
    }

    @Test
    public void testLookAhead() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("hello world\n"));

        Assert.assertEquals('h', reader.lookAhead());
        Assert.assertEquals('e', reader.lookAhead());
        Assert.assertEquals('l', reader.lookAhead());
        Assert.assertEquals('l', reader.lookAhead());
        Assert.assertEquals('o', reader.lookAhead());
        Assert.assertEquals('\n', reader.lookAhead());
        Assert.assertEquals('w', reader.lookAhead());
        Assert.assertEquals('r', reader.lookAhead());
        Assert.assertEquals('d', reader.lookAhead());
        Assert.assertEquals('\n', reader.lookAhead());
        Assert.assertEquals(-1, reader.lookAhead());
        Assert.assertEquals(-1, reader.lookAhead());
        Assert.assertEquals(-1, reader.lookAhead());
        Assert.assertEquals(-1, reader.lookAhead());
        Assert.assertEquals(-1, reader.lookAhead());
        Assert.assertEquals(-1, reader.lookAhead());
        Assert.assertEquals(-1, reader.lookAhead());

        reader.readLine();

        Assert.assertEquals('w', reader.lookAhead());
        Assert.assertEquals('r', reader.lookAhead());
        Assert.assertEquals('d', reader.lookAhead());
        Assert.assertEquals('\n', reader.lookAhead());
        Assert.assertEquals(-1, reader.lookAhead());
        Assert.assertEquals(-1, reader.lookAhead());
    }

}