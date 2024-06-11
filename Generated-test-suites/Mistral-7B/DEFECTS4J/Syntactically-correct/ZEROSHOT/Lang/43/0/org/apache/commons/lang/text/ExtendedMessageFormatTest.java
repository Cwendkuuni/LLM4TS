package org.apache.commons.lang.text;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.test.FormattingTestUtils;
import org.apache.commons.text.lookup.FormatFactory;
import org.apache.commons.text.lookup.FormatFactoryImpl;
import org.apache.commons.text.lookup.MessageFormatFactory;

import org.junit.Test;

/**
 * ExtendedMessageFormatTest.
 */
public class ExtendedMessageFormatTest {

    /**
     * Test method for {@link ExtendedMessageFormat#ExtendedMessageFormat(String)}
     * .
     */
    @Test
    public void testConstructor_String() {
        final String pattern = "Hello {0}, the answer is {1, date, short}.";
        final Locale locale = Locale.US;
        final Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("AnswerFormat", new FormatFactoryImpl() {

            @Override
            public Format getFormat(String name, String arguments, Locale locale) {
                return new Format() {
                    @Override
                    public String format(Object object, Locale locale,
                            String format, ParsePosition parsePosition)
                            throws ParseException {
                        return String.valueOf(FormattingTestUtils.ANSWER);
                    }
                };
            }
        });
        final ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(pattern, locale, registry);
        String formattedMessage = messageFormat.format(new Object[] { "John",
                FormattingTestUtils.ANSWER }, locale);
        assertEquals("Hello John, the answer is 42.", formattedMessage);

        try {
            final ExtendedMessageFormat messageFormat2 = new ExtendedMessageFormat(null, locale, registry);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Test method for {@link ExtendedMessageFormat#ExtendedMessageFormat(String, Locale)}
     * .
     */
    @Test
    public void testConstructor_String_Locale() {
        final String pattern = "Hello {0}, the answer is {1, date, short}.";
        final Locale locale = Locale.US;
        final Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("AnswerFormat", new FormatFactoryImpl() {

            @Override
            public Format getFormat(String name, String arguments, Locale locale) {
                return new Format() {
                    @Override
                    public String format(Object object, Locale locale,
                            String format, ParsePosition parsePosition)
                            throws ParseException {
                        return StringUtils.leftPad(String.valueOf(FormattingTestUtils.ANSWER), 2, "0");
                    }
                };
            }
        });
        final ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(pattern, locale, registry);
        String formattedMessage = messageFormat.format(new Object[] { "John",
                FormattingTestUtils.ANSWER }, locale);
        assertEquals("Hello John, the answer is 42", formattedMessage);

        try {
            final ExtendedMessageFormat messageFormat2 = new ExtendedMessageFormat(null, locale, registry);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Test method for {@link ExtendedMessageFormat#ExtendedMessageFormat(String, Locale, Map)}
     * .
     */
    @Test
    public void testConstructor_String_Locale_Map() {
        final String pattern = "Hello {0}, the answer is {1, date, short}.";
        final Locale locale = Locale.US;
        final Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("AnswerFormat", new FormatFactoryImpl() {

            @Override
            public Format getFormat(String name, String arguments, Locale locale) {
                return new Format() {
                    @Override
                    public String format(Object object, Locale locale,
                            String format, ParsePosition parsePosition)
                            throws ParseException {
                        return StringUtils.leftPad(String.valueOf(FormattingTestUtils.ANSWER), 2, "0");
                    }
                };
            }
        });
        final ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(pattern, locale, registry);
        String formattedMessage = messageFormat.format(new Object[] { "John",
                FormattingTestUtils.ANSWER }, locale);
        assertEquals("Hello John, the answer is 42", formattedMessage);

        try {
            final ExtendedMessageFormat messageFormat2 = new ExtendedMessageFormat(null, locale, registry);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Test method for {@link ExtendedMessageFormat#ExtendedMessageFormat(String, Map)}
     * .
     */
    @Test
    public void testConstructor_String_Map() {
        final String pattern = "Hello {0}, the answer is {1, date, short}.";
        final Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("AnswerFormat", new FormatFactoryImpl() {

            @Override
            public Format getFormat(String name, String arguments, Locale locale) {
                return new Format() {
                    @Override
                    public String format(Object object, Locale locale,
                            String format, ParsePosition parsePosition)
                            throws ParseException {
                        return StringUtils.leftPad(String.valueOf(FormattingTestUtils.ANSWER), 2, "0");
                    }
                };
            }
        });
        try {
            final ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(pattern, null, registry);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Test method for {@link ExtendedMessageFormat#toPattern()} .
     */
    @Test
    public void testToPattern() {
        final String pattern = "Hello {0}, the answer is {1, date, short}.";
        final Locale locale = Locale.US;
        final Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("AnswerFormat", new FormatFactoryImpl() {

            @Override
            public Format getFormat(String name, String arguments, Locale locale) {
                return new Format() {
                    @Override
                    public String format(Object object, Locale locale,
                            String format, ParsePosition parsePosition)
                            throws ParseException {
                        return StringUtils.leftPad(String.valueOf(FormattingTestUtils.ANSWER), 2, "0");
                    }
                };
            }
        });
        final ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(pattern, locale, registry);
        final String actual = messageFormat.toPattern();
        final String expected = "{0}, the answer is {1, date, short}";
        assertEquals(expected, actual);
    }

    /**
     * Test method for {@link ExtendedMessageFormat#applyPattern(String)} .
     */
    @Test
    public void testApplyPattern() {
        final String pattern = "Hello {0}, the answer is {1, date, short}.";
        final Locale locale = Locale.US;
        final Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("AnswerFormat", new FormatFactoryImpl() {

            @Override
            public Format getFormat(String name, String arguments, Locale locale) {
                return new Format() {
                    @Override
                    public String format(Object object, Locale locale,
                            String format, ParsePosition parsePosition)
                            throws ParseException {
                        return StringUtils.leftPad(String.valueOf(FormattingTestUtils.ANSWER), 2, "0");
                    }
                };
            }
        });
        final ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(pattern, locale, registry);
        final String actual = messageFormat.toPattern();
        final String expected = "{0}, the answer is {1, date, short}";
        assertEquals(expected, actual);

        final ExtendedMessageFormat messageFormat2 = new ExtendedMessageFormat(
                "{0}, the answer is {1}, {2}", locale, registry);
        final String actual2 = messageFormat2.toPattern();
        final String expected2 = "{0}, the answer is {1}, {2}";
        assertEquals(expected2, actual2);
    }

    /**
     * Test method for {@link ExtendedMessageFormat#setFormat(int, Format)} .
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        final String pattern = "Hello {0}, the answer is {1, date, short}.";
        final Locale locale = Locale.US;
        final Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("AnswerFormat", new FormatFactoryImpl() {

            @Override
            public Format getFormat(String name, String arguments, Locale locale) {
                return new Format() {
                    @Override
                    public String format(Object object, Locale locale,
                            String format, ParsePosition parsePosition)
                            throws ParseException {
                        return StringUtils.leftPad(String.valueOf(FormattingTestUtils.ANSWER), 2, "0");
                    }
                };
            }
        });
        final ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(pattern, locale, registry);
        final Format answerFormat = messageFormat.getFormats()[1];
        messageFormat.setFormat(1, new Format() {
            @Override
            public String format(Object object, Locale locale,
                    String format, ParsePosition parsePosition)
                    throws ParseException {
                return String.valueOf(FormattingTestUtils.ANSWER2);
            }
        });
    }

    /**
     * Test method for {@link ExtendedMessageFormat#setFormatByArgumentIndex(int, Format)}
     * .
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        final String pattern = "Hello {0}, the answer is {1, date, short}.";
        final Locale locale = Locale.US;
        final Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("AnswerFormat", new FormatFactoryImpl() {

            @Override
            public Format getFormat(String name, String arguments, Locale locale) {
                return new Format() {
                    @Override
                    public String format(Object object, Locale locale,
                            String format, ParsePosition parsePosition)
                            throws ParseException {
                        return StringUtils.leftPad(String.valueOf(FormattingTestUtils.ANSWER), 2, "0");
                    }
                };
            }
        });
        final ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(pattern, locale, registry);
        messageFormat.setFormatByArgumentIndex(1, new Format() {
            @Override
            public String format(Object object, Locale locale,
                    String format, ParsePosition parsePosition)
                    throws ParseException {
                return String.valueOf(FormattingTestUtils.ANSWER2);
            }
        });
    }

    /**
     * Test method for {@link ExtendedMessageFormat#setFormats(Format[])} .
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        final String pattern = "Hello {0}, the answer is {1, date, short}.";
        final Locale locale = Locale.US;
        final Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("AnswerFormat", new FormatFactoryImpl() {

            @Override
            public Format getFormat(String name, String arguments, Locale locale) {
                return new Format() {
                    @Override
                    public String format(Object object, Locale locale,
                            String format, ParsePosition parsePosition)
                            throws ParseException {
                        return StringUtils.leftPad(String.valueOf(FormattingTestUtils.ANSWER), 2, "0");
                    }
                };
            }
        });
        final ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(pattern, locale, registry);
        final Format[] formats = new Format[] { new Format() {
            @Override
            public String format(Object object, Locale locale,
                    String format, ParsePosition parsePosition)
                    throws ParseException {
                return String.valueOf(FormattingTestUtils.ANSWER2);
            }
        }, messageFormat.getFormats()[1] };
        messageFormat.setFormats(formats);
    }

    /**
     * Test method for {@link ExtendedMessageFormat#setFormatsByArgumentIndex(Format[])}
     * .
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        final String pattern = "Hello {0}, the answer is {1, date, short}.";
        final Locale locale = Locale.US;
        final Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("AnswerFormat", new FormatFactoryImpl() {

            @Override
            public Format getFormat(String name, String arguments, Locale locale) {
                return new Format() {
                    @Override
                    public String format(Object object, Locale locale,
                            String format, ParsePosition parsePosition)
                            throws ParseException {
                        return StringUtils.leftPad(String.valueOf(FormattingTestUtils.ANSWER), 2, "0");
                    }
                };
            }
        });
        final ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(pattern, locale, registry);
        final Format[] formats = new Format[] { messageFormat.getFormats()[1],
                new Format() {
                    @Override
                    public String format(Object object, Locale locale,
                            String format, ParsePosition parsePosition)
                            throws ParseException {
                        return String.valueOf(FormattingTestUtils.ANSWER2);
                    }
                } };
        messageFormat.setFormatsByArgumentIndex(new ArrayList<>(
                java.util.Arrays.asList(new Integer[] { 1 }))
                .toArray(new Format[0]));
    }
}