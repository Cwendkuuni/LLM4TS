package org.apache.commons.lang.test;

import org.apache.commons.lang.text.ExtendedMessageFormat;
import org.apache.commons.lang.text.FormatFactory;
import org.junit.Test;
import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Tests for {@link ExtendedMessageFormat}.
 */
public class ExtendedMessageFormatTest {

    /**
     * Test basic usage with no custom formats.
     */
    @Test
    public void testBasicUsage() {
        ExtendedMessageFormat format = new ExtendedMessageFormat(
                "Hello {0}, your age is {1}.");
        format.applyPattern(format.getPattern());
        String message = format.format(new Object[]{"John Doe", 30});
        assertEquals("Hello John Doe, your age is 30.", message);
    }

    /**
     * Test usage with custom format.
     */
    @Test
    public void testCustomFormat() {
        FormatFactory formatFactory = new FormatFactory() {
            @Override
            public Format getFormat(String name, String args, Locale locale) {
                if ("date".equals(name)) {
                    return new SimpleDateFormat("yyyy-MM-dd");
                }
                return null;
            }
        };

        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", formatFactory);

        ExtendedMessageFormat format = new ExtendedMessageFormat(
                "The event is on {0, date}.", registry);
        format.applyPattern(format.getPattern());
        String message = format.format(new Object[]{"2023-03-25"});
        assertEquals("The event is on 2023-03-25.", message);
    }

    /**
     * Test invalid format name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFormatName() {
        FormatFactory formatFactory = new FormatFactory() {
            @Override
            public Format getFormat(String name, String args, Locale locale) {
                return null;
            }
        };

        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", formatFactory);
        registry.put("invalid", formatFactory);

        ExtendedMessageFormat format = new ExtendedMessageFormat(
                "{0, invalid}", registry);
        format.applyPattern(format.getPattern());
    }

    /**
     * Test missing format name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMissingFormatName() {
        ExtendedMessageFormat format = new ExtendedMessageFormat(
                "{0, format}");
        format.applyPattern(format.getPattern());
    }

    /**
     * Test format with invalid arguments.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidArguments() {
        FormatFactory formatFactory = new FormatFactory() {
            @Override
            public Format getFormat(String name, String args, Locale locale) {
                return new SimpleDateFormat(args);
            }
        };

        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", formatFactory);

        ExtendedMessageFormat format = new ExtendedMessageFormat(
                "The event is on {0, date, invalid}.", registry);
        format.applyPattern(format.getPattern());
    }

    /**
     * Test format with missing arguments.
     */
    @Test
    public void testMissingArguments() {
        FormatFactory formatFactory = new FormatFactory() {
            @Override
            public Format getFormat(String name, String args, Locale locale) {
                if ("date".equals(name)) {
                    return new SimpleDateFormat(args);
                }
                return null;
            }
        };

        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", formatFactory);

        ExtendedMessageFormat format = new ExtendedMessageFormat(
                "The event is on {0, date}", registry);
        format.applyPattern(format.getPattern());
        try {
            format.format(null);
            fail("FormatException expected");
        } catch (IllegalArgumentException e) {
            //expected
        }
    }

    /**
     * Test format with too many arguments.
     */
    @Test
    public void testTooManyArguments() {
        FormatFactory formatFactory = new FormatFactory() {
            @Override
            public Format getFormat(String name, String args, Locale locale) {
                if ("date".equals(name)) {
                    return new SimpleDateFormat(args);
                }
                return null;
            }
        };

        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", formatFactory);

        ExtendedMessageFormat format = new ExtendedMessageFormat(
                "The event is on {0, date, long, short}", registry);
        format.applyPattern(format.getPattern());
        try {
            format.format(new Object[]{new Object[]{"2023-03-25"}, "test"});
            fail("FormatException expected");
        } catch (IllegalArgumentException e) {
            //expected
        }
    }

    /**
     * Test format with too few arguments.
     */
    @Test
    public void testTooFewArguments() {
        FormatFactory formatFactory = new FormatFactory() {
            @Override
            public Format getFormat(String name, String args, Locale locale) {
                if ("date".equals(name)) {
                    return new SimpleDateFormat(args);
                }
                return null;
            }
        };

        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", formatFactory);

        ExtendedMessageFormat format = new ExtendedMessageFormat(
                "The event is on {0, date}", registry);
        format.applyPattern(format.getPattern());
        try {
            format.format(new Object[]{});
            fail("FormatException expected");
        } catch (IllegalArgumentException e) {
            //expected
        }
    }

    /**
     * Test format with invalid format styles.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFormatStyles() {
        FormatFactory formatFactory = new FormatFactory() {
            @Override
            public Format getFormat(String name, String args, Locale locale) {
                if ("date".equals(name)) {
                    return new SimpleDateFormat(args);
                }
                return null;
            }
        };

        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", formatFactory);

        ExtendedMessageFormat format = new ExtendedMessageFormat(
                "The event is on {0, date, long, short, long}", registry);
        format.applyPattern(format.getPattern());
    }

    /**
     * Test format with empty pattern.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testEmptyPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("", null);
        try {
            format.applyPattern("");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            //expected
        }
    }

    /**
     * Test format with empty registry.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testEmptyRegistry() {
        ExtendedMessageFormat format = new ExtendedMessageFormat(
                "{0}", null);
        try {
            format.applyPattern(format.getPattern());
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            //expected
        }
    }

    /**
     * Test parsePosition out of bounds.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParsePositionOutOfBounds() {
        ExtendedMessageFormat format = new ExtendedMessageFormat(
                "The event is on {0, date}", null);
        ParsePosition pos = new ParsePosition(100);
        try {
            format.parse(new Object[]{}, pos);
            fail("IllegalArgumentException expected");
        } catch (ParseException e) {
            //expected
        }
    }

    /**
     * Test parsePosition at beginning.
     */
    @Test(expected = ParseException.class)
    public void testParsePositionBeginning() {
        ExtendedMessageFormat format = new ExtendedMessageFormat(
                "The event is on {0, date}", null);
        ParsePosition pos = new ParsePosition(0);
        format.parse(new Object[]{}, pos);
        fail("ParseException expected");
    }

    /**
     * Test parsePosition in middle.
     */
    @Test(expected = ParseException.class)
    public void testParsePositionMiddle() {
        FormatFactory formatFactory = new FormatFactory() {
            @Override
            public Format getFormat(String name, String args, Locale locale) {
                if ("date".equals(name)) {
                    return new SimpleDateFormat("yyyy-MM-dd");
                }
                return null;
            }
        };

        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", formatFactory);

        ExtendedMessageFormat format = new ExtendedMessageFormat(
                "The event is on {0, date} {1}", registry);
        format.applyPattern(format.getPattern());
        ParsePosition pos = new ParsePosition(11);
        Object[] args = {"2023-03-25", "test"};
        try {
            format.parse(args, pos);
            fail("ParseException expected");
        } catch (ParseException e) {
            //expected
        }
    }

    /**
     * Test parsePosition at end.
     */
    @Test
    public void testParsePositionEnd() {
        FormatFactory formatFactory = new FormatFactory() {
            @Override
            public Format getFormat(String name, String args, Locale locale) {
                if ("date".equals(name)) {
                    return new SimpleDateFormat("yyyy-MM-dd");
                }
                return null;
            }
        };

        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", formatFactory);

        ExtendedMessageFormat format = new ExtendedMessageFormat(
                "The event is on {0, date} {1}", registry);
        format.applyPattern(format.getPattern());
        ParsePosition pos = new ParsePosition(format.getPattern().length());
        Object[] args = {"2023-03-25", "test"};
        format.parse(args, pos);
        assertEquals(args.length, pos.getIndex());
    }
}