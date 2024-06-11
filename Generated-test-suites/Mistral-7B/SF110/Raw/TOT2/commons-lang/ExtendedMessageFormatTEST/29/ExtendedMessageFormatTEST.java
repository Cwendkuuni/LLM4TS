import org.apache.commons.lang3.text.ExtendedMessageFormat;
import org.apache.commons.lang3.text.MessageFormat;
import org.junit.Assert;
import org.junit.Test;

public class ExtendedMessageFormatTest {

    @Test
    public void testExtendedMessageFormatConstructorWithPattern() {
        String pattern = "Hello, {0}! Welcome to our community.";
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(pattern);
        Assert.assertEquals("Hello, [arg0]! Welcome to our community.", messageFormat.toPattern());
    }

    @Test
    public void testExtendedMessageFormatConstructorWithPatternAndLocale() {
        String pattern = "Hello, {0}! Welcome to our community.";
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(pattern, Locale.US);
        Assert.assertEquals("Hello, [arg0]! Welcome to our community.", messageFormat.toPattern());
    }

    @Test
    public void testExtendedMessageFormatConstructorWithPatternAndRegistry() {
        String pattern = "Hello, {0}! Welcome to our {1}.";
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("currency", new CurrencyFormatFactory());
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(pattern, Locale.US, registry);
        Assert.assertEquals("Hello, [arg0]! Welcome to our {currency}", messageFormat.toPattern());
    }

    @Test
    public void testExtendedMessageFormatApplyPattern() {
        String pattern = "Hello, {0}! Welcome to our {1}.";
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(pattern, Locale.US);
        messageFormat.applyPattern("Hello, {1}! Welcome to our {0}.");
        Assert.assertEquals("Hello, [arg1]! Welcome to our [arg0]", messageFormat.toPattern());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatUnsupported() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("Hello, {0}!");
        messageFormat.setFormat(0, new SimpleDateFormat("dd/MM/yyyy"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsUnsupported() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("Hello, {0}!");
        messageFormat.setFormats(new Format[0]);
    }
}