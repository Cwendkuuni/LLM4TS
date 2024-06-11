package org.apache.commons.lang.test;

import junit.framework.TestCase;
import org.apache.commons.lang.text.ExtendedMessageFormat;
import org.apache.commons.text.simplifier.SimpleFormat;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

/**
 * Tests for {@link ExtendedMessageFormat}.
 */
public class ExtendedMessageFormatTest extends TestCase {

    private static final String DUMMY_FORMAT_NAME = "DummyFormat";

    /**
     * Test creating an ExtendedMessageFormat with a simple pattern.
     */
    public void testSimpleFormat() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put(DUMMY_FORMAT_NAME, (name, args, locale) -> new SimpleFormat());

        String pattern = "Hello {0}.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        assertEquals("Hello", format.format(new Object[]{null}));
        assertEquals("Hello John", format.format(new Object[]{"John"}));
    }

    /**
     * Test creating an ExtendedMessageFormat with a complex pattern.
     */
    public void testComplexFormat() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put(DUMMY_FORMAT_NAME, (name, args, locale) -> new SimpleFormat());

        String pattern = "Hello {0,choice,0#{Hello}{1#Good {2,number,integer} {0}!}}}.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);

        Object[][] data = {
                {null, null, null, "Hello"},
                {"John", null, null, "Hello John"},
                {"John", "choice", null, "Hello John"},
                {"John", "choice", "1", "Good John 1!"},
                {"John", "choice", "0", "Hello John"}
        };

        for (Object[] args : data) {
            assertEquals(format.format(args), format.format(new Object[]{args[0], args[1], args[2]}));
            try {
                ParsePosition pos = new ParsePosition(0);
                format.parseObject(pattern, null, pos, Locale.getDefault());
                assertEquals(args[0], pos.getIndex() == 0 ? null : pos.getIndex(), args[0]);
                assertEquals(args[1], pos.getIndex() == 1 ? null : pos.getIndex(), args[1]);
                assertEquals(args[2], pos.getIndex() == 2 ? null : pos.getIndex(), args[2]);
            } catch (ParseException e) {
                fail("Failed to parse argument positions: " + e.getMessage());
            }
        }
    }

    /**
     * Test creating an ExtendedMessageFormat with a missing format.
     */
    public void testMissingFormat() {
        Map<String, FormatFactory> registry = new HashMap<>();

        String pattern = "Hello {0,missingFormatName}.";
        try {
            new ExtendedMessageFormat(pattern, registry);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Test creating an ExtendedMessageFormat with an invalid format name.
     */
    public void testInvalidFormatName() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put(DUMMY_FORMAT_NAME, (name, args, locale) -> new SimpleFormat());

        String pattern = "Hello {0,invalidFormatName}.";
        try {
            new ExtendedMessageFormat(pattern, registry);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid format name: invalidFormatName", e.getMessage());
        }
    }

    /**
     * Test creating an ExtendedMessageFormat with an invalid format style.
     */
    public void testInvalidFormatStyle() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put(DUMMY_FORMAT_NAME, (name, args, locale) -> new SimpleFormat());

        String pattern = "Hello {0,{invalidStyle}}.";
        try {
            new ExtendedMessageFormat(pattern, registry);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid format style: invalidStyle", e.getMessage());
        }
    }

    /**
     * Test creating an ExtendedMessageFormat with an empty registry.
     */
    public void testEmptyRegistry() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}.");
        try {
            format.applyPattern("");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Test creating an ExtendedMessageFormat with a null registry.
     */
    public void testNullRegistry() {
        try {
            new ExtendedMessageFormat("Hello {0}.", null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    /**
     * Test creating an ExtendedMessageFormat with an empty pattern.
     */
    public void testEmptyPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("");
        try {
            format.applyPattern("");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Test creating an ExtendedMessageFormat with a null pattern.
     */
    public void testNullPattern() {
        try {
            new ExtendedMessageFormat(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    /**
     * Test creating an ExtendedMessageFormat with an invalid pattern.
     */
    public void testInvalidPattern() {
        Map<String, FormatFactory> registry = new HashMap<>();
        try {
            new ExtendedMessageFormat("Hello {0}", registry).applyPattern("InvalidPattern");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Test creating an ExtendedMessageFormat with a malformed pattern.
     */
    public void testMalformedPattern() {
        Map<String, FormatFactory> registry = new HashMap<>();
        try {
            new ExtendedMessageFormat("Hello {0} {1", registry).applyPattern("Hello {0} {1");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}