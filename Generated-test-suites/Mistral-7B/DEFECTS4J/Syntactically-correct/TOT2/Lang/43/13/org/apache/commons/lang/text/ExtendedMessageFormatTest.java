import org.apache.commons.lang.text.ExtendedMessageFormat;
import org.apache.commons.lang.text.FormatFactory;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ExtendedMessageFormatTest {

    @Test
    public void testDefaultConstructor() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("Hello {0}!");
        String formattedMessage = messageFormat.format(new Object[]{"World"});
        Assert.assertEquals("Hello World!", formattedMessage);
    }

    @Test
    public void testConstructorWithLocale() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("Hello {0}!", Locale.US);
        String formattedMessage = messageFormat.format(new Object[]{"World"});
        Assert.assertEquals("Hello World!", formattedMessage);
    }

    @Test
    public void testConstructorWithRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("currency", new CurrencyFormatFactory());
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("The balance is {0, currency}", registry);
        String formattedMessage = messageFormat.format(new Object[]{1234.56});
        Assert.assertEquals("The balance is $1,234.56", formattedMessage);
    }

    @Test
    public void testApplyPattern() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("Hello {0}! {1, choice, 0#Invalid|1#Valid}");
        String formattedMessage = messageFormat.format(new Object[]{"John", "Valid"});
        Assert.assertEquals("Hello John! Valid", formattedMessage);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidArgumentIndex() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("Hello {-1}!");
        messageFormat.applyPattern("Hello {0}!");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFormatDescription() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("Hello {0}! {1, format, invalid}");
        messageFormat.applyPattern("Hello {0}! {1, choice, 0#Invalid|1#Valid}");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("Hello {0}!");
        messageFormat.setFormats(new Format[0]);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("Hello {0}!");
        messageFormat.setFormatByArgumentIndex(0, new Format() {});
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("Hello {0}!");
        messageFormat.setFormat(0, new Format() {});
    }

    private static class CurrencyFormatFactory implements FormatFactory {
        @Override
        public Format getFormat(String format, String formatStyle, Locale locale) {
            return new SimpleDateFormat("$#,##0.##");
        }
    }
}