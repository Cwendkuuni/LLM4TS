package JSci.maths.symbolic; // replace with the actual package name if different

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
    // Add instance variables for test objects here if needed

    private SimpleCharStream charStream;

    @Before
    public void setUp() {
        charStream = new SimpleCharStream(new ByteArrayInputStream("Test string".getBytes()), 1, 1, 4096);
    }

    @Test
    public void testExpandBuff() {
        charStream.ExpandBuff(false);
        assertThat(charStream.bufsize, is(4096));
        charStream.ExpandBuff(true);
        assertThat(charStream.bufsize, is(6144));
    }

    @Test
    public void testFillBuff() throws IOException {
        charStream.FillBuff();
        char c = charStream.readChar();
        assertThat(c, is('T'));
        charStream.FillBuff();
        char nextChar = charStream.readChar();
        assertThat(nextChar, is('e'));
        charStream.FillBuff();
        char thirdChar = charStream.readChar();
        assertThat(thirdChar, is('s'));
    }

    @Test(expected = IOException.class)
    public void testFillBuffWithIOException() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        charStream = new SimpleCharStream(inputStream, 1, 1, 4096);
        charStream.FillBuff();
        charStream.readChar();
    }

    @Test
    public void testBeginToken() throws IOException {
        char c = charStream.BeginToken();
        assertThat(c, is('T'));
    }

    @Test
    public void testUpdateLineColumn() throws IOException {
        char nextChar = charStream.readChar();
        char streamChar = charStream.readChar();
        char endChar = charStream.readChar();

        charStream.UpdateLineColumn('\r');
        assertThat(charStream.line, is(1));
        assertThat(charStream.column, is(0));

        charStream.UpdateLineColumn('\n');
        assertThat(charStream.line, is(2));
        assertThat(charStream.column, is(0));

        charStream.UpdateLineColumn('\t');
        assertThat(charStream.column, is(9));

        charStream.UpdateLineColumn(' ');
        assertThat(charStream.column, is(10));

        charStream.UpdateLineColumn('a');
        assertThat(charStream.column, is(12));

        charStream.UpdateLineColumn('z');
        assertThat(charStream.column, is(13));

        charStream.UpdateLineColumn('\r');
        charStream.UpdateLineColumn('\n');
        charStream.UpdateLineColumn('b');
        charStream.UpdateLineColumn('z');
        assertThat(charStream.line, is(3));
        assertThat(charStream.column, is(15));

        charStream.UpdateLineColumn('\r');
        charStream.UpdateLineColumn('\n');
        assertThat(charStream.line, is(4));
        assertThat(charStream.column, is(0));

        charStream.UpdateLineColumn('a');
        char nextLineChar = charStream.readChar();
        char nextNextChar = charStream.readChar();
        char endLineChar = charStream.readChar();

        assertThat(nextLineChar, is('b'));
        assertThat(nextNextChar, is('z'));
        assertThat(endLineChar, is('\r'));
    }

    @Test
    public void testGetLineColumn() {
        int line = charStream.getLine();
        int column = charStream.getColumn();
        int endLine = charStream.getEndLine();
        int endColumn = charStream.getEndColumn();
        int beginLine = charStream.getBeginLine();
        int beginColumn = charStream.getBeginColumn();

        charStream.UpdateLineColumn('\r');
        charStream.UpdateLineColumn('\n');
        charStream.UpdateLineColumn('a');
        charStream.UpdateLineColumn('b');
        charStream.UpdateLineColumn('c');
        charStream.UpdateLineColumn('\r');
        charStream.UpdateLineColumn('\n');
        charStream.UpdateLineColumn('d');
        charStream.UpdateLineColumn('e');
        charStream.UpdateLineColumn('f');
        charStream.UpdateLineColumn('\r');
        charStream.UpdateLineColumn('\n');
        charStream.UpdateLineColumn('g');
        charStream.UpdateLineColumn('h');

        assertThat(line, is(1));
        assertThat(column, is(3));
        assertThat(endLine, is(4));
        assertThat(endColumn, is(6));
        assertThat(beginLine, is(3));
        assertThat(beginColumn, is(1));
    }

    @Test
    public void testBackup() {
        char c1 = charStream.readChar();
        char c2 = charStream.readChar();
        char c3 = charStream.readChar();
        char c4 = charStream.readChar();
        char c5 = charStream.readChar();

        charStream.backup(2);

        char prevChar1 = charStream.readChar();
        char prevChar2 = charStream.readChar();

        assertThat(prevChar1, is(c3));
        assertThat(prevChar2, is(c2));

        charStream.backup(3);
        char prevChar3 = charStream.readChar();

        assertThat(prevChar3, is(c2));

        charStream.backup(2);
        char prevChar4 = charStream.readChar();

        assertThat(prevChar4, is(c1));

        charStream.backup(5);

        assertThat(charStream.bufpos, is(-3));
        char prevChar5 = charStream.readChar();
        char prevChar6 = charStream.readChar();

        assertThat(prevChar5, is(c4));
        assertThat(prevChar6, is(c3));

        charStream.backup(3);
        char prevChar7 = charStream.readChar();

        assertThat(prevChar7, is(c3));
    }

    @Test
    public void testGetImage() {
        char[] image = SimpleCharStream.GetImage();
        char expected = 'T';
        for (int i = 0; i < image.length; i++) {
            if (i >= charStream.tokenBegin && i < charStream.bufpos) {
                assertThat(image[i], is(expected));
                expected = (char) (expected + 1);
            }
            else {
                assertThat(image[i], is(nullValue()));
            }
        }
    }

    @Test
    public void testGetSuffix() {
        char[] suffix = SimpleCharStream.GetSuffix(3);
        char expected = 's';
        for (int i = 0; i < suffix.length; i++) {
            assertThat(suffix[i], is(expected));
            expected = (char) (expected + 1);
        }
    }

    @Test
    public void testDone() {
        SimpleCharStream.Done();
        assertThat(SimpleCharStream.buffer, nullValue());
        assertThat(SimpleCharStream.bufline, nullValue());
        assertThat(SimpleCharStream.bufcolumn, nullValue());
    }

    @Test
    public void testGetImageWithEmptyBuffer() {
        SimpleCharStream emptyCharStream = new SimpleCharStream(new ByteArrayInputStream(new byte[0]), 1, 1, 4096);
        char[] image = SimpleCharStream.GetImage();
        assertThat(image, nullValue());
    }

    @Test
    public void testGetSuffixWithEmptyBuffer() {
        SimpleCharStream emptyCharStream = new SimpleCharStream(new ByteArrayInputStream(new byte[0]), 1, 1, 4096);
        char[] suffix = SimpleCharStream.GetSuffix(3);
        assertThat(suffix, nullValue());
    }
}