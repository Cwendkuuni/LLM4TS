import org.apache.commons.lang3.text.ExtendedMessageFormat;
import org.apache.commons.lang3.text.FormatFactory;
import org.apache.commons.lang3.text.SimpleFormatFactory;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExtendedMessageFormatTest {

    @Test
    public void testExtendedMessageFormat() {
        // Test default constructor
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}, your age is {1}.");
        assertEquals("Hello {0}, your age is {1}", format.toPattern());
        Object[] args = new Object[]{"John", 25};
        String result = format.format(args);
        assertEquals("Hello John, your age is 25", result);

        // Test constructor with registry
        Map<String, FormatFactory> registry = new java.util.HashMap<>();
        FormatFactory factory = new SimpleFormatFactory();
        registry.put("customDateFormat", factory);

        ExtendedMessageFormat formatWithRegistry = new ExtendedMessageFormat("Hello {0}, today is {1, customDateFormat, date, long}", registry);
        assertEquals("Hello {0}, today is {1, customDateFormat, date, long}", formatWithRegistry.toPattern());
        Object[] argsWithRegistry = new Object[]{"John", new java.util.Date()};
        String resultWithRegistry = formatWithRegistry.format(argsWithRegistry);
        String expectedWithRegistry = String.format("Hello John, today is %s", factory.getFormat("customDateFormat", null, Locale.getDefault()).format(new java.text.DateFormatSymbols(Locale.getDefault()), new java.util.Date()));
        assertEquals(expectedWithRegistry, resultWithRegistry);

        // Test toPattern()
        ExtendedMessageFormat formatWithCustomPattern = new ExtendedMessageFormat("Hello {0}, today is {1, customDateFormat, date, long}", registry);
        formatWithCustomPattern.applyPattern("Hello {0}, today is {1, customFormat, date, long}");
        assertEquals("Hello {0}, today is {1, customFormat, date, long}", formatWithCustomPattern.toPattern());

        // Test setFormats() throws UnsupportedOperationException
        try {
            formatWithCustomPattern.setFormats(new Format[0]);
            fail();
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        // Test getFormat() with null format name
        try {
            formatWithCustomPattern.getFormat(null, null);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test getFormat() with non-existent format name
        FormatFactory nonExistentFactory = new SimpleFormatFactory();
        registry.put("newFormatName", nonExistentFactory);
        try {
            formatWithCustomPattern.getFormat("newFormatName", null);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test getFormat() with existing format name
        Format existingFormat = factory.getFormat("customDateFormat", null, Locale.getDefault());
        assertEquals("customDateFormat", formatWithCustomPattern.getFormat("customDateFormat", null).getClass().getName());
        assertEquals(existingFormat, formatWithCustomPattern.getFormat("customDateFormat", null));

        // Test format() with null arguments
        String resultNullArgs = formatWithCustomPattern.format(null);
        assertNull(resultNullArgs);

        // Test format() with wrong number of arguments
        Object[] wrongNumberArgs = new Object[]{new Object[]{"John"}, new Object[]{"John", 25, "30"}};
        String resultWrongNumberArgs = formatWithCustomPattern.format(wrongNumberArgs[0]);
        assertNull(resultWrongNumberArgs);
        try {
            resultWrongNumberArgs = formatWithCustomPattern.format(wrongNumberArgs[1]);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}