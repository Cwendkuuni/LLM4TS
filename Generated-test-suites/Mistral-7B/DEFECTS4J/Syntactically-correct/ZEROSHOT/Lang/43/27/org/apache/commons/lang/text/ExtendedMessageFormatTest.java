import org.apache.commons.lang.text.ExtendedMessageFormat;
import org.apache.commons.lang.text.FormatFactory;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ExtendedMessageFormatTest {

    private ExtendedMessageFormat messageFormat;
    private FormatFactory formatFactory;
    private Map<String, FormatFactory> formatFactoryRegistry;

    @Before
    public void setUp() {
        formatFactory = new FormatFactory() {
            @Override
            public Format getFormat(String pattern, String arguments, Locale locale) {
                if ("dateFormat".equals(pattern)) {
                    return new SimpleDateFormat(pattern);
                }
                return null;
            }
        };
        formatFactoryRegistry = new HashMap<>();
        formatFactoryRegistry.put("dateFormat", formatFactory);

        String pattern = "Hello {0,choice(0|1)({2,dateFormat(dd-MM-yyyy)},{1,dateFormat(dd-MM-yyyy)})}";
        messageFormat = new ExtendedMessageFormat(pattern, formatFactoryRegistry);
    }

    @After
    public void tearDown() {
        messageFormat = null;
    }

    @Test
    public void testToPattern() {
        assertEquals("Hello {0,choice(0|1)({2,dd-MM-yyyy},{1,dd-MM-yyyy})} ",
                messageFormat.toPattern());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        messageFormat.setFormat(0, new SimpleDateFormat("dd-MM-yyyy"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        messageFormat.setFormatByArgumentIndex(0, new SimpleDateFormat("dd-MM-yyyy"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        SimpleDateFormat[] formats = new SimpleDateFormat[1];
        formats[0] = new SimpleDateFormat("dd-MM-yyyy");

        messageFormat.setFormats(formats);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        SimpleDateFormat[] formats = new SimpleDateFormat[1];
        formats[0] = new SimpleDateFormat("dd-MM-yyyy");

        messageFormat.setFormatsByArgumentIndex(formats);
    }

    @Test
    public void testFormat() {
        Object[] args = new Object[]{1, 2, "31-12-2019", "31-12-2020"};
        String expected = "Hello 1, 31-12-2019 (choice: 0 - 1, 31-12-2019, 31-12-2020)";
        String result = messageFormat.format(args);
        assertEquals(expected, result);
    }

    @Test
    public void testFormat_NullLocale() {
        Object[] args = new Object[]{1, 2, "31-12-2019", "31-12-2020"};
        String expected = "Hello 1, {2,dateFormat(dd-MM-yyyy)}, {1,dateFormat(dd-MM-yyyy)}";
        String result = messageFormat.format(args, null);
        assertEquals(expected, result);
    }

    @Test
    public void testFormat_EmptyArgs() {
        Object[] args = new Object[0];
        String expected = "Hello {}";
        String result = messageFormat.format(args);
        assertEquals(expected, result);
    }

    @Test
    public void testFormat_InvalidFormatArgumentIndex() {
        Object[] args = new Object[]{1, 2, "31-12-2019", "31-12-2020"};
        String pattern = "Hello {0,choice(0|1)({2,dateFormat(dd-MM-yyyy)},{1,dateFormat(dd-MM-yyyy)})} {3}";
        ExtendedMessageFormat newMessageFormat = new ExtendedMessageFormat(pattern,
                formatFactoryRegistry);
        try {
            newMessageFormat.format(args);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testFormat_MissingFormatFactory() {
        formatFactoryRegistry.remove("dateFormat");

        String pattern = "Hello {0,choice(0|1)({2,dateFormat(dd-MM-yyyy)},{1,dateFormat(dd-MM-yyyy)})}";
        ExtendedMessageFormat newMessageFormat = new ExtendedMessageFormat(pattern,
                formatFactoryRegistry);

        Object[] args = new Object[]{1, 2, "31-12-2019", "31-12-2020"};
        try {
            newMessageFormat.format(args);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParse() {
        String input = "Hello 1, 31-12-2019, 31-12-2020";
        Object[] expectedArgs = new Object[]{1, "31-12-2019", "31-12-2020"};
        ParsePosition pos = new ParsePosition(0);

        Object[] args = messageFormat.parse(input, pos);
        assertEquals(expectedArgs.length, args.length);
        for (int i = 0; i < expectedArgs.length; i++) {
            assertEquals(expectedArgs[i], args[i]);
        }
        assertEquals(input.length(), pos.getIndex());
    }

    @Test
    public void testParse_NullInput() {
        String input = null;
        ParsePosition pos = new ParsePosition(0);

        Object[] args = messageFormat.parse(input, pos);
        assertNull(args);
        assertEquals(0, pos.getIndex());
    }

    @Test
    public void testParse_EmptyInput() {
        String input = StringUtils.EMPTY;
        ParsePosition pos = new ParsePosition(0);

        Object[] args = messageFormat.parse(input, pos);
        assertEquals(0, args.length);
        assertEquals(input.length(), pos.getIndex());
    }

    @Test
    public void testParse_InvalidFormatArgumentIndex() {
        String input = "Hello 1, 31-12-2019, 31-12-2020, 4";
        ParsePosition pos = new ParsePosition(0);

        Object[] args = messageFormat.parse(input, pos);
        assertNull(args);
        assertEquals(input.length(), pos.getIndex());
    }

    @Test
    public void testParse_MissingFormatFactory() {
        formatFactoryRegistry.remove("dateFormat");
        String input = "Hello 1, {2,dateFormat(dd-MM-yyyy)}, 31-12-2020";
        ParsePosition pos = new ParsePosition(0);

        Object[] args = messageFormat.parse(input, pos);
        assertNull(args);
        assertEquals(input.length(), pos.getIndex());
    }

    @Test
    public void testApplyPattern() {
        String pattern = "Hello {0,choice(0|1)({2,dateFormat(dd-MM-yyyy)},{1,dateFormat(dd-MM-yyyy)})}";
        ExtendedMessageFormat newMessageFormat = new ExtendedMessageFormat(pattern,
                formatFactoryRegistry);

        String input = "Hello 1, 31-12-2019, 31-12-2020";
        Object[] args = newMessageFormat.parse(input, new ParsePosition(0));

        newMessageFormat.applyPattern(pattern);
        String newPattern = newMessageFormat.toPattern();
        String expectedPattern = "Hello {0,choice(0|1)({2,dd-MM-yyyy},{1,dd-MM-yyyy})}";
        assertEquals(expectedPattern, newPattern);
    }
}

public class FormatFactory {
    public interface Format {
    }

    public Format getFormat(String pattern, String arguments, Locale locale) {
        return null;
    }
}