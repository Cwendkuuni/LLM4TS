import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() throws IOException {
        CharSequence input = "&amp; #123; &#x7f;";
        Writer out = mock(Writer.class);
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        int result = unescaper.translate(input, 0, out);
        String expectedOutput = "\u0001\u007b\u007f";
        String actualOutput = "";
        try (Writer writer = new StringWriter()) {
            unescaper.translate(input, 0, writer);
            actualOutput = writer.toString();
        }

        assertThat(result, is(expectedOutput.length()));
        assertThat(actualOutput, is(expectedOutput));
    }

    // Add edge cases and exception handling scenarios in the next steps
}