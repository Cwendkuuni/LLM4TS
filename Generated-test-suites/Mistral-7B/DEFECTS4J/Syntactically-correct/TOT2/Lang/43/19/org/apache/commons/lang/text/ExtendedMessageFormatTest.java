import org.apache.commons.lang.text.ExtendedMessageFormat;
import org.apache.commons.lang.text.FormatFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.text.Format;
import java.text.MessageFormat;
import java.text.ParsePosition;
import java.util.HashMap;
import java.util.Locale;

/**
 * This class contains JUnit test cases for the {@link ExtendedMessageFormat} class.
 */
public class ExtendedMessageFormatTest {

    private ExtendedMessageFormat messageFormat;

    /**
     * Initialize a test instance with a simple pattern.
     */
    @Before
    public void setUp() {
        String pattern = "Hello {0}, your age is {1}.";
        messageFormat = new ExtendedMessageFormat(pattern, new HashMap<>());
    }

    /**
     * Test a simple message instantiation.
     */
    @Test
    public void testSimpleMessageFormat() {
        String message = messageFormat.format(new Object[]{"John", 30});
        Assert.assertEquals("Hello John, your age is 30.", message);
    }

    /**
     * Test a message instantiation with custom format elements.
     */
    @Test
    public void testCustomFormatElements() {
        // Add a custom format element for a format factory that converts strings to uppercase.
        messageFormat = new ExtendedMessageFormat(messageFormat.toPattern(), new HashMap<>());
        messageFormat.addFormatFactory(new FormatFactory() {
            @Override
            public Format getFormat(String name, String format, Locale locale) {
                return new UpperCaseFormat();
            }
        });

        String message = messageFormat.format(new Object[]{"John", 30});
        Assert.assertEquals("Hello John, your age is 30.", message);
        Assert.assertEquals("Hello JOHN, your age is 30.", messageFormat.format(new Object[]{"John", 30}, new Locale("en", "US")));
    }

    /**
     * Test parsing an invalid pattern with a missing format element.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPatternMissingFormatElement() {
        String pattern = "Hello {0}, your age is {1}.";
        new ExtendedMessageFormat(pattern, new HashMap<>());
        // The following line should throw an IllegalArgumentException.
        messageFormat.format(new Object[]{"John", 30});
    }

    /**
     * Test parsing an invalid pattern with an missing or invalid format name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPatternMissingOrInvalidFormatName() {
        messageFormat.toPattern(); // Update the pattern with a custom format element.
        messageFormat.applyPattern("Hello {0,INVALID_FORMAT_NAME}, your age is {1}.");
        // The following line should throw an IllegalArgumentException.
        messageFormat.format(new Object[]{"John", 30});
    }

    /**
     * Test parsing an invalid pattern with an unsupported format style.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPatternUnsupportedFormatStyle() {
        messageFormat.toPattern(); // Update the pattern with a custom format element.
        messageFormat.applyPattern("Hello {0,INVALID_FORMAT_STYLE}, your age is {1}.");
        // The following line should throw an IllegalArgumentException.
        messageFormat.format(new Object[]{"John", 30});
    }

    /**
     * Custom format factory for converting strings to upper case.
     */
    private static class UpperCaseFormat implements Format {
        @Override
        public String format(Object obj, Locale locale, String format, ParsePosition parsePosition) {
            if (obj == null) {
                return "";
            }
            return obj.toString().toUpperCase();
        }

        @Override
        public Object parseObject(String source, ParsePosition parsePosition) {
            return source;
        }
    }
}