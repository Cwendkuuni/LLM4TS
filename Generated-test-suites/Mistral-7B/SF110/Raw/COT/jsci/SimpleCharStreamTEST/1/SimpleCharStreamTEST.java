package JSci.maths.symbolic;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;

public class SimpleCharStreamTest {

    private static final int BUFFER_SIZE = 10;
    private SimpleCharStream simpleCharStream;
    private ByteArrayInputStream inputStream;

    @Before
    public void setUp() {
        simpleCharStream = new SimpleCharStream(new ByteArrayInputStream("Test string".getBytes()), 1, 1, BUFFER_SIZE);
        inputStream = new ByteArrayInputStream("Test string".getBytes());
    }

    @Test
    public void testExpandBuff() throws IOException {
        simpleCharStream.ExpandBuff(false);
        Assert.assertThat(simpleCharStream.available, is(BUFFER_SIZE * 2));
        Assert.assertThat(simpleCharStream.buffer.length, is(BUFFER_SIZE * 2));

        simpleCharStream.ExpandBuff(true);
        Assert.assertThat(simpleCharStream.available, is(BUFFER_SIZE * 3));
        Assert.assertThat(simpleCharStream.buffer.length, is(BUFFER_SIZE * 3));
    }

    @Test
    public void testFillBuff() throws IOException {
        simpleCharStream.FillBuff();
        Assert.assertThat(simpleCharStream.maxNextCharInd, is(BUFFER_SIZE));
        Assert.assertThat(simpleCharStream.available, is(BUFFER_SIZE));

        simpleCharStream.FillBuff();
        Assert.assertThat(simpleCharStream.maxNextCharInd, is(2 * BUFFER_SIZE));
        Assert.assertThat(simpleCharStream.available, is(2 * BUFFER_SIZE));
    }

    @Test
    public void testBeginToken() throws IOException {
        Assert.assertThat(simpleCharStream.BeginToken(), is('T'));
        Assert.assertThat(simpleCharStream.getLine(), is(1));
        Assert.assertThat(simpleCharStream.getColumn(), is(1));

        simpleCharStream.UpdateLineColumn(' ');
        Assert.assertThat(simpleCharStream.getLine(), is(1));
        Assert.assertThat(simpleCharStream.getColumn(), is(2));

        simpleCharStream.UpdateLineColumn('\n');
        Assert.assertThat(simpleCharStream.getLine(), is(2));
        Assert.assertThat(simpleCharStream.getColumn(), is(1));

        simpleCharStream.UpdateLineColumn('\r');
        simpleCharStream.UpdateLineColumn('T');
        Assert.assertThat(simpleCharStream.getLine(), is(3));
        Assert.assertThat(simpleCharStream.getColumn(), is(1));
    }

    @Test
    public void testReadChar() throws IOException {
        Assert.assertThat(simpleCharStream.readChar(), is('T'));
        Assert.assertThat(simpleCharStream.getLine(), is(1));
        Assert.assertThat(simpleCharStream.getColumn(), is(2));

        simpleCharStream.backup(1);
        Assert.assertThat(simpleCharStream.readChar(), is('T'));

        simpleCharStream.FillBuff();
        simpleCharStream.backup(2);
        Assert.assertThat(simpleCharStream.readChar(), is('s'));

        simpleCharStream.backup(10);
        Assert.assertThat(simpleCharStream.readChar(), isNullValue());
    }

    @Test
    public void testGetColumn() {
        Assert.assertThat(simpleCharStream.getColumn(), is(1));

        simpleCharStream.UpdateLineColumn(' ');
        simpleCharStream.UpdateLineColumn('\t');
        Assert.assertThat(simpleCharStream.getColumn(), is(3));

        simpleCharStream.UpdateLineColumn('\n');
        Assert.assertThat(simpleCharStream.getColumn(), is(1));
    }

    @Test
    public void testGetLine() {
        Assert.assertThat(simpleCharStream.getLine(), is(1));

        simpleCharStream.UpdateLineColumn('\n');
        Assert.assertThat(simpleCharStream.getLine(), is(2));

        simpleCharStream.UpdateLineColumn('\r');
        simpleCharStream.UpdateLineColumn('T');
        Assert.assertThat(simpleCharStream.getLine(), is(3));
    }

    @Test(expected = IOException.class)
    public void testIOException() throws IOException {
        simpleCharStream.FillBuff();
        simpleCharStream.inputStream.close();
        simpleCharStream.BeginToken();
    }
}