import org.apache.commons.lang3.text.ExtendedMessageFormat;
import org.apache.commons.lang3.text.MessageFormat;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ExtendedMessageFormatTest {

    @Test
    public void testConstructors() {
        // Test default constructor
        ExtendedMessageFormat defaultFormat = new ExtendedMessageFormat("");
        
        // Test constructor with pattern and default locale
        ExtendedMessageFormat patternFormat = new ExtendedMessageFormat("{0}, {1}");
        
        // Test constructor with pattern, locale, and empty registry
        ExtendedMessageFormat patternLocaleFormat = new ExtendedMessageFormat("{0}, {1}", Locale.US);
        
        // Test constructor with pattern, locale, and non-empty registry
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customFormat", new MyFormatFactory());
        ExtendedMessageFormat patternRegistryFormat = new ExtendedMessageFormat("{0}, {1}", Locale.US, registry);

        // Test toPattern method
        assertEquals("", defaultFormat.toPattern());
        assertEquals("{0}, {1}", patternFormat.toPattern());
        assertEquals("{0}, {1}", patternLocaleFormat.toPattern());
        assertEquals("{0}, {1}", patternRegistryFormat.toPattern());
    }

    @Test
    public void testApplyPattern() {
        // Test simple case
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}, {1}");
        Object[] args = {"Hello", "World"};
        String result = format.format(args);
        assertEquals("Hello, World", result);

        // Test with custom format
        format = new ExtendedMessageFormat("{0}, {1}", Locale.US, new HashMap<>()) {
            {
                putFormat("customDate", new CustomDateFormat());
            }
        };
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        args = new Object[] {dateFormat.format(new Date()), "World"};
        result = format.format(args);
        assertEquals("dd/MM/yyyy, World", result);

        // Test with error cases
        try {
            format = new ExtendedMessageFormat("{0}, {1}{2}");
            args = new Object[] {"Hello", "World"};
            format.format(args);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            format = new ExtendedMessageFormat("{0}, {1}");
            format.applyPattern("");
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    // Add edge cases and other test methods here
}

class MyFormatFactory implements FormatFactory {
    @Override
    public Format getFormat(String name, String argument, Locale locale) {
        return new SimpleDateFormat("yyyy-MM-dd");
    }
}

class CustomDateFormat extends SimpleDateFormat {
    public CustomDateFormat() {
        super("dd/MM/yyyy");
    }
}