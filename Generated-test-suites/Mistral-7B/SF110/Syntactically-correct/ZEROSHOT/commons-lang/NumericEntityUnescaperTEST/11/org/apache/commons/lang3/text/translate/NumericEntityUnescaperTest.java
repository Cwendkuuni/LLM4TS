import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper.OPTION;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringWriter;
import java.util.Arrays;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslateWithDefaultOptions() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "&amp; &lt; &gt; &apos; &quot; &#32; &#13; &#10; &#123; &#125; &#x41; &#xFFFF; &#x1F600; &#x1F641; &#x1F642;";
        StringWriter output = new StringWriter();
        int index = 0;
        int length = input.length();

        for (; index < length; index++) {
            int translated = unescaper.translate(new CharSequence() {
                @Override
                public int length() {
                    return length;
                }

                @Override
                public char charAt(int index) {
                    return input.charAt(index);
                }
            }, index, output);

            if (translated > 0) {
                index += translated - 1;
            }
        }

        String expectedOutput = "&amp; &lt; &gt; &apos; &quot; &#x0020; &#x000D; &#x000A; &{ &#}; &p; &q; &d; &F; &π; & smile;";
        String actualOutput = output.toString();
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testTranslateWithSemiColonRequiredOption() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
        String input = "&amp; #123; &#x41;";
        StringWriter output = new StringWriter();
        int index = 0;
        int length = input.length();

        for (; index < length; index++) {
            int translated = unescaper.translate(new CharSequence() {
                @Override
                public int length() {
                    return length;
                }

                @Override
                public char charAt(int index) {
                    return input.charAt(index);
                }
            }, index, output);

            if (translated > 0) {
                index += translated - 1;
            }
        }

        String expectedOutput = "&amp; &#123; &A;";
        String actualOutput = output.toString();
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testTranslateWithSemiColonOptionalOption() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional);
        String input = "&amp; #123; &#x41";
        StringWriter output = new StringWriter();
        int index = 0;
        int length = input.length();

        for (; index < length; index++) {
            int translated = unescaper.translate(new CharSequence() {
                @Override
                public int length() {
                    return length;
                }

                @Override
                public char charAt(int index) {
                    return input.charAt(index);
                }
            }, index, output);

            if (translated > 0) {
                index += translated - 1;
            }
        }

        String expectedOutput = "&amp; &#123; A";
        String actualOutput = output.toString();
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithErrorIfNoSemiColonOption() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.errorIfNoSemiColon);
        String input = "&amp; #123";
        StringWriter output = new StringWriter();
        int index = 0;
        int length = input.length();

        for (; index < length; index++) {
            int translated = unescaper.translate(new CharSequence() {
                @Override
                public int length() {
                    return length;
                }

                @Override
                public char charAt(int index) {
                    return input.charAt(index);
                }
            }, index, output);

            if (translated > 0) {
                index += translated - 1;
            }
        }
    }

    @Test
    public void testIsSet() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired, OPTION.errorIfNoSemiColon);
        Arrays.asList(OPTION.values()).forEach(option -> {
            boolean isSet = unescaper.isSet(option);
            boolean expected = Arrays.asList(OPTION.semiColonRequired, OPTION.errorIfNoSemiColon).contains(option);
            Assert.assertEquals(expected, isSet);
        });
    }
}