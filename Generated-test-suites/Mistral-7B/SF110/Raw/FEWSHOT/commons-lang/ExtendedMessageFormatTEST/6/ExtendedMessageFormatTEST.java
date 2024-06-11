package org.apache.commons.lang3.text;

import static org.junit.Assert.*;
import org.apache.commons.lang3.text.ExtendedMessageFormat;
import org.apache.commons.lang3.text.MessageFormat;
import org.apache.commons.lang3.text.ParsePosition;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.text.*;
import java.util.*;
import org.apache.commons.lang3.*;
import org.junit.Test;

public class ExtendedMessageFormatTest {

    @Test
    public void testNewInstance() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("Hello {0}, your age is {1}.");
        assertEquals("Hello {0}, your age is {1}", messageFormat.toPattern());
        assertNull(messageFormat.registry);

        messageFormat = new ExtendedMessageFormat("Hello {0}, your age is {1}", Locale.US);
        assertEquals("Hello {0}, your age is {1}", messageFormat.toPattern());
        assertNull(messageFormat.registry);

        messageFormat = new ExtendedMessageFormat("Hello {0}, your age is {1}", new HashMap<>());
        assertEquals("Hello {0}, your age is {1}", messageFormat.toPattern());
        assertNotNull(messageFormat.registry);

        messageFormat = new ExtendedMessageFormat("Hello {0}, your age is {1}", new HashMap<>(), Locale.US);
        assertEquals("Hello {0}, your age is {1}", messageFormat.toPattern());
        assertNotNull(messageFormat.registry);

        messageFormat = new ExtendedMessageFormat("Hello {0}, your age is {1}", new HashMap<>(), Locale.US, new HashMap<>());
        assertEquals("Hello {0}, your age is {1}", messageFormat.toPattern());
        assertNotNull(messageFormat.registry);
    }

    @Test
    public void testToPattern() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("Hello {0}, your age is {1}.");
        assertEquals("Hello {0}, your age is {1}", messageFormat.toPattern());

        messageFormat.applyPattern("Hello {2}, your name is {0} and your age is {1}.");
        assertEquals("Hello {2}, your name is {0} and your age is {1}", messageFormat.toPattern());

        messageFormat = new ExtendedMessageFormat("Hello {0}, your age is {1}", new HashMap<>());
        messageFormat.applyPattern("Hello {2}, your name is {0} and your age is {1}.");
        assertEquals("Hello {2}, your name is {0} and your age is {1}", messageFormat.toPattern());
    }

    @Test
    public void testApplyPattern() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("Hello {0}, your age is {1}.");

        messageFormat.applyPattern("Hello {2}, your name is {0} and your age is {1}.");

        Object[] arguments = {"John Doe", 30};
        String formattedMessage = messageFormat.format(arguments);
        assertEquals("Hello John Doe, your name is John Doe and your age is 30", formattedMessage);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatUnsupported() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("Hello {0}, your age is {1}.");
        messageFormat.setFormat(0, new MessageFormat("{0}"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndexUnsupported() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("Hello {0}, your age is {1}.");
        messageFormat.setFormatByArgumentIndex(0, new MessageFormat("{0}"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsUnsupported() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("Hello {0}, your age is {1}.");
        messageFormat.setFormats(new MessageFormat[] { new MessageFormat("{0}") });
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndexUnsupported() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("Hello {0}, your age is {1}.");
        messageFormat.setFormatsByArgumentIndex(new MessageFormat[] { new MessageFormat("{0}") });
    }

    @Test
    public void testEquals() {
        ExtendedMessageFormat messageFormat1 = new ExtendedMessageFormat("Hello {0}, your age is {1}.");
        ExtendedMessageFormat messageFormat2 = new ExtendedMessageFormat("Hello {0}, your age is {1}.");
        ExtendedMessageFormat messageFormat3 = new ExtendedMessageFormat("Hello {0}, your name is {1}.");

        assertTrue(messageFormat1.equals(messageFormat1));
        assertTrue(messageFormat1.equals(messageFormat2));
        assertFalse(messageFormat1.equals(messageFormat3));
        assertFalse(messageFormat1.equals(null));
        assertFalse(messageFormat1.equals(""));
    }

    @Test
    public void testHashCode() {
        ExtendedMessageFormat messageFormat1 = new ExtendedMessageFormat("Hello {0}, your age is {1}.");
        ExtendedMessageFormat messageFormat2 = new ExtendedMessageFormat("Hello {0}, your age is {1}.");
        ExtendedMessageFormat messageFormat3 = new ExtendedMessageFormat("Hello {0}, your name is {1}.");

        int hashCode1 = messageFormat1.hashCode();
        int hashCode2 = messageFormat1.hashCode();
        int hashCode3 = messageFormat3.hashCode();

        assertEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
    }

    @Test
    public void testGetFormat() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("Hello {0}, your age is {1}.");

        messageFormat.applyPattern("Hello {2}, your name is {0} and your age is {1}.");

        Format format = messageFormat.getFormat("0");
        assertEquals(MessageFormat.class, format.getClass());
    }

    @Test
    public void testReadArgumentIndex() {
        String pattern = "Hello {0}, your age is {1}, and your favorite color is {2}.";
        ParsePosition pos = new ParsePosition(0);

        int index0 = messageFormat.readArgumentIndex(pattern, pos);
        assertEquals(0, index0);

        messageFormat.next(pos);

        int index1 = messageFormat.readArgumentIndex(pattern, pos);
        assertEquals(1, index1);

        messageFormat.next(pos);

        int index2 = messageFormat.readArgumentIndex(pattern, pos);
        assertEquals(2, index2);

        messageFormat.next(pos);

        int index3 = messageFormat.readArgumentIndex(pattern, pos);
        assertEquals(-1, index3);
    }

    @Test
    public void testParseFormatDescription() {
        String pattern = "Hello {0}, your age is {1}, and your favorite color is {2}.";
        ParsePosition pos = new ParsePosition(0);

        String description0 = messageFormat.parseFormatDescription(pattern, pos);
        assertEquals("0", description0);

        messageFormat.next(pos);

        String description1 = messageFormat.parseFormatDescription(pattern, pos);
        assertEquals("1", description1);

        messageFormat.next(pos);

        String description2 = messageFormat.parseFormatDescription(pattern, pos);
        assertEquals("2", description2);

        messageFormat.next(pos);

        String description3 = messageFormat.parseFormatDescription(pattern, pos);
        assertEquals(null, description3);
    }

    @Test
    public void testInsertFormats() {
        String pattern = "Hello {0}, your age is {1}, and your favorite color is {2}.";

        ArrayList<String> customPatterns = new ArrayList<>();
        customPatterns.add("{color}");

        String formattedPattern = messageFormat.insertFormats(pattern, customPatterns);
        assertEquals("Hello {0}, your age is {1}, and your favorite color is {color}", formattedPattern);

        customPatterns.add(null);

        formattedPattern = messageFormat.insertFormats(pattern, customPatterns);
        assertEquals("Hello {0}, your age is {1}, and your favorite color is null", formattedPattern);

        customPatterns.clear();

        formattedPattern = messageFormat.insertFormats(pattern, customPatterns);
        assertEquals("Hello {0}, your age is {1}, and your favorite color is {}", formattedPattern);
    }

    @Test
    public void testSeekNonWs() {
        String pattern = "Hello {0}, your age is {1}, and your favorite color is {2}.";
        ParsePosition pos = new ParsePosition(0);

        int len = messageFormat.seekNonWs(pattern, pos);
        assertEquals(0, len);

        messageFormat.next(pos);

        len = messageFormat.seekNonWs(pattern, pos);
        assertEquals(1, len);

        messageFormat.next(pos);

        len = messageFormat.seekNonWs(pattern, pos);
        assertEquals(5, len);

        messageFormat.next(pos);

        len = messageFormat.seekNonWs(pattern, pos);
        assertEquals(-1, len);
    }

    @Test
    public void testNext() {
        String pattern = "Hello {0}, your age is {1}, and your favorite color is {2}.";
        ParsePosition pos = new ParsePosition(0);

        messageFormat.next(pos);
        assertEquals(1, pos.getIndex());

        messageFormat.next(pos);
        assertEquals(6, pos.getIndex());

        messageFormat.next(pos);
        assertEquals(11, pos.getIndex());

        messageFormat.next(pos);
        assertEquals(12, pos.getIndex());

        messageFormat.next(pos);
        assertEquals(13, pos.getIndex());

        messageFormat.next(pos);
        assertEquals(14, pos.getIndex());

        messageFormat.next(pos);
        assertEquals(-1, pos.getIndex());
    }

    @Test
    public void testAppendQuotedString() {
        String pattern = "Hello {0}, your favorite \"color\" is {1}.";
        ParsePosition pos = new ParsePosition(0);

        StringBuilder sb = messageFormat.appendQuotedString(pattern, pos, null, true);
        assertEquals(7, pos.getIndex());
        assertEquals("color", sb.toString());

        sb = messageFormat.appendQuotedString(pattern, pos, null, false);
        assertEquals(11, pos.getIndex());
        assertNull(sb);

        sb = messageFormat.appendQuotedString("Hello {0}, your favorite \"color\" is {1}. {\"Hello\"}", pos, null, true);
        assertEquals(24, pos.getIndex());
        assertEquals("Hello", sb.toString());

        sb = messageFormat.appendQuotedString("Hello {0}, your favorite \"color\" is {1}. {\"Hello\"}", pos, null, false);
        assertEquals(-1, pos.getIndex());
        assertNull(sb);
    }

    @Test
    public void testGetQuotedString() {
        String pattern = "Hello {0}, your favorite \"color\" is {1}.";
        ParsePosition pos = new ParsePosition(0);

        messageFormat.getQuotedString(pattern, pos, true);
        assertEquals(7, pos.getIndex());
        assertEquals("color", pos.getInput().substring(0, pos.getIndex()));

        messageFormat.getQuotedString(pattern, pos, false);
        assertEquals(11, pos.getIndex());
        assertEquals("", pos.getInput().substring(0, pos.getIndex()));

        messageFormat.getQuotedString("Hello {0}, your favorite \"color\" is {1}. {\"Hello\"}", pos, true);
        assertEquals(24, pos.getIndex());
        assertEquals("Hello", pos.getInput().substring(0, pos.getIndex()));

        messageFormat.getQuotedString("Hello {0}, your favorite \"color\" is {1}. {\"Hello\"}", pos, false);
        assertEquals(-1, pos.getIndex());
        assertEquals("", pos.getInput().substring(0, pos.getIndex()));
    }

    @Test
    public void testContainsElements() {
        Collection<String> collection = new ArrayList<>();
        collection.add("color");
        collection.add(null);
        collection.add("");

        assertTrue(messageFormat.containsElements(collection));

        collection.clear();
        collection.add("color");

        assertFalse(messageFormat.containsElements(collection));
    }
}