package org.apache.commons.lang3.text;

import static org.junit.Assert.*;

import java.text.*;
import java.util.*;
import org.apache.commons.lang3.*;
import org.apache.commons.cli.ParsePosition;

public class ExtendedMessageFormatTest {

    @Test
    public void testExtendedMessageFormat() {
        String pattern = "Hello {0}! Your age is {1}, and you have {2} apples.";
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(pattern);
        assertEquals(pattern, messageFormat.toPattern());

        Object[] objects = {"John", 25, new Integer(5)};
        String formattedMessage = messageFormat.format(objects);
        assertEquals("Hello John! Your age is 25, and you have 5 apples.", formattedMessage);

        pattern = "{1}, {0}!";
        messageFormat = new ExtendedMessageFormat(pattern);
        formattedMessage = messageFormat.format(objects);
        assertEquals("25, John!", formattedMessage);

        Map<String, FormatFactory> registry = new HashMap<>();
        FormatFactory formatFactory = new SimpleFormatFactory("MyFormatter");
        registry.put("MyFormatter", formatFactory);

        pattern = "{MyFormatter,arg1,arg2}";
        messageFormat = new ExtendedMessageFormat(pattern, null, registry);
        formattedMessage = messageFormat.format(new Object[]{new Object[]{"arg1", "arg2"}});
        assertEquals(formatFactory.format(new Object[]{"arg1", "arg2"}, Locale.getDefault()), formattedMessage);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedMethods() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("{0}");
        messageFormat.setFormat(0, new SimpleDateFormat("dd/MM/yyyy"));
    }

    private static class SimpleFormatFactory implements FormatFactory {
        private final String description;

        public SimpleFormatFactory(String description) {
            this.description = description;
        }

        @Override
        public Format getFormat(String description, Locale locale, Object[] arguments) {
            return new SimpleDateFormat(this.description);
        }
    }

    @Test
    public void testApplyPatternWithQuotes() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("{0}") {
            @Override
            void appendQuotedString(String pattern, ParsePosition pos, StringBuilder appendTo, boolean escapingOn) {
                appendTo.append("{quoted}");
            }
        };

        String pattern = "Hello '{0}'!";
        messageFormat.applyPattern(pattern);
        assertEquals("Hello {quoted}", messageFormat.toPattern());
    }

    @Test
    public void testApplyPatternWithFormats() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("{0}") {
            @Override
            Format getFormat(String description) {
                if (description.equals("MyFormatter")) {
                    return new SimpleDateFormat("dd/MM/yyyy");
                }
                return super.getFormat(description);
            }
        };

        String pattern = "{MyFormatter} {0} {MyFormatter}";
        messageFormat.applyPattern(pattern);
        assertEquals("{dd/MM/yyyy} {0} {dd/MM/yyyy}", messageFormat.toPattern());
    }

    @Test
    public void testParsePosition() {
        String pattern = "Hello {0}! Your age is {1}, and you have {2} apples.";
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(pattern);

        ParsePosition pos = new ParsePosition(0);
        messageFormat.parse(pattern.toCharArray(), pos, Locale.getDefault());
        assertEquals(0, pos.getIndex());

        messageFormat.parse(pattern.toCharArray(), pos, Locale.getDefault(), new Object[]{new Object[]{"John", 25, new Integer(5)}});
        assertEquals(pattern.length(), pos.getIndex());

        int index = messageFormat.readArgumentIndex(pattern, pos);
        assertEquals(0, index);

        String description = messageFormat.parseFormatDescription(pattern, pos);
        assertEquals("0", description);
    }
}