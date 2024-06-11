package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.EnumSet;

import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper.OPTION;

import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslateWithDefaultOptions() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "The text &num;123&num; with &#x1F602; &#x1F603; entities.";
        StringWriter output = new StringWriter();
        int index = 0;

        while (index < input.length()) {
            int charsTranslated = unescaper.translate(new CharSequence() {
                @Override
                public char charAt(int index) {
                    return input.charAt(index);
                }

                @Override
                public int length() {
                    return input.length();
                }
            }, index, output);
            index += charsTranslated;
        }

        String expectedOutput = "The text 123 with ✈️ ✉️ entities.";
        assertEquals(expectedOutput, output.toString());
    }

    @Test
    public void testTranslateWithSemiColonRequiredOption() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
        String input = "The text &num;123";
        StringWriter output = new StringWriter();
        int index = 0;

        while (index < input.length()) {
            int charsTranslated = unescaper.translate(new CharSequence() {
                @Override
                public char charAt(int index) {
                    return input.charAt(index);
                }

                @Override
                public int length() {
                    return input.length();
                }
            }, index, output);
            index += charsTranslated;
        }

        String expectedOutput = "The text 123";
        assertEquals(expectedOutput, output.toString());

        input = "The text &num;123&";
        index = 0;

        while (index < input.length()) {
            int charsTranslated = unescaper.translate(new CharSequence() {
                @Override
                public char charAt(int index) {
                    return input.charAt(index);
                }

                @Override
                public int length() {
                    return input.length();
                }
            }, index, output);
            index += charsTranslated;
        }

        expectedOutput = "The text 123";
        assertEquals(expectedOutput, output.toString());

        input = "The text &num;123";
        index = 0;

        try {
            unescaper.translate(new CharSequence() {
                @Override
                public char charAt(int index) {
                    return input.charAt(index);
                }

                @Override
                public int length() {
                    return input.length();
                }
            }, index, output);

            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }

        expectedOutput = "The text 123";
        assertEquals(expectedOutput, output.toString());
    }

    @Test
    public void testTranslateWithSemiColonOptionalOption() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional);
        String input = "The text &num;123";
        StringWriter output = new StringWriter();
        int index = 0;

        while (index < input.length()) {
            int charsTranslated = unescaper.translate(new CharSequence() {
                @Override
                public char charAt(int index) {
                    return input.charAt(index);
                }

                @Override
                public int length() {
                    return input.length();
                }
            }, index, output);
            index += charsTranslated;
        }

        String expectedOutput = "The text 123";
        assertEquals(expectedOutput, output.toString());

        input = "The text &num;123&";
        index = 0;

        while (index < input.length()) {
            int charsTranslated = unescaper.translate(new CharSequence() {
                @Override
                public char charAt(int index) {
                    return input.charAt(index);
                }

                @Override
                public int length() {
                    return input.length();
                }
            }, index, output);
            index += charsTranslated;
        }

        expectedOutput = "The text 123";
        assertEquals(expectedOutput, output.toString());
    }

    @Test
    public void testTranslateWithErrorIfNoSemiColonOption() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.errorIfNoSemiColon);
        String input = "The text &num;123";
        StringWriter output = new StringWriter();
        int index = 0;

        while (index < input.length()) {
            try {
                unescaper.translate(new CharSequence() {
                    @Override
                    public char charAt(int index) {
                        return input.charAt(index);
                    }

                    @Override
                    public int length() {
                        return input.length();
                    }
                }, index, output);
                fail("Expected IllegalArgumentException");
            } catch (IllegalArgumentException e) {
                // pass
            }
        }

        String expectedErrorMessage = "Semi-colon required at end of numeric entity";
        assertTrue(expectedErrorMessage.equals(unescaper.getMessage()));
    }

    @Test
    public void testTranslateWithHexOption() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "The text &#xA9;";
        StringWriter output = new StringWriter();
        int index = 0;

        while (index < input.length()) {
            int charsTranslated = unescaper.translate(new CharSequence() {
                @Override
                public char charAt(int index) {
                    return input.charAt(index);
                }

                @Override
                public int length() {
                    return input.length();
                }
            }, index, output);
            index += charsTranslated;
        }

        String expectedOutput = "The text √";
        assertEquals(expectedOutput, output.toString());
    }

    @Test
    public void testTranslateMultipleEntitiesWithDefaultOptions() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "The text &num;123&num;456&num;789";
        StringWriter output = new StringWriter();
        int index = 0;

        while (index < input.length()) {
            int charsTranslated = unescaper.translate(new CharSequence() {
                @Override
                public char charAt(int index) {
                    return input.charAt(index);
                }

                @Override
                public int length() {
                    return input.length();
                }
            }, index, output);
            index += charsTranslated;
        }

        String expectedOutput = "The text 123456789";
        assertEquals(expectedOutput, output.toString());
    }

    @Test
    public void testTranslateMultipleEntitiesWithSemiColonRequiredOption() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
        String input = "The text &num;123&num;456&num;789";
        StringWriter output = new StringWriter();
        int index = 0;

        while (index < input.length()) {
            int charsTranslated = unescaper.translate(new CharSequence() {
                @Override
                public char charAt(int index) {
                    return input.charAt(index);
                }

                @Override
                public int length() {
                    return input.length();
                }
            }, index, output);
            index += charsTranslated;
        }

        String expectedOutput = "The text 123\n456\n789";
        assertEquals(expectedOutput, output.toString());
    }

    @Test
    public void testTranslateMultipleEntitiesWithSemiColonOptionalOption() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional);
        String input = "The text &num;123&num;456&num;789";
        StringWriter output = new StringWriter();
        int index = 0;

        while (index < input.length()) {
            int charsTranslated = unescaper.translate(new CharSequence() {
                @Override
                public char charAt(int index) {
                    return input.charAt(index);
                }

                @Override
                public int length() {
                    return input.length();
                }
            }, index, output);
            index += charsTranslated;
        }

        String expectedOutput = "The text 123456789";
        assertEquals(expectedOutput, output.toString());
    }

    @Test
    public void testTranslateMultipleEntitiesWithErrorIfNoSemiColonOption() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.errorIfNoSemiColon);
        String input = "The text &num;123&num;456&num;789";
        StringWriter output = new StringWriter();
        int index = 0;

        while (index < input.length()) {
            try {
                unescaper.translate(new CharSequence() {
                    @Override
                    public char charAt(int index) {
                        return input.charAt(index);
                    }

                    @Override
                    public int length() {
                        return input.length();
                    }
                }, index, output);
                fail("Expected IllegalArgumentException");
            } catch (IllegalArgumentException e) {
                // pass
            }
        }

        String expectedErrorMessage = "Semi-colon required at end of numeric entity";
        assertTrue(expectedErrorMessage.equals(unescaper.getMessage()));
    }
}