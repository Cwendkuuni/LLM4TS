import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.simplifier.MessageFormatParser;
import org.apache.commons.text.simplifier.extended.ExtendedFormatFactory;
import org.junit.Assert;
import org.junit.Test;

import java.util.Locale;
import java.util.Map;

public class ExtendedMessageFormatTest {

    @Test
    public void testDefaultConstructor() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}, your age is {1}.");
        Assert.assertNotNull(format);
        String result = format.format(new Object[]{"John", 25});
        Assert.assertEquals("Hello John, your age is 25.", result);
    }

    @Test
    public void testConstructorWithLocale() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}, your age is {1}.", Locale.ENGLISH);
        Assert.assertNotNull(format);
        String result = format.format(new Object[]{"John", 25});
        Assert.assertEquals("Hello John, your age is 25.", result);
    }

    @Test
    public void testConstructorWithRegistry() {
        Map<String, ExtendedFormatFactory> registry = new java.util.HashMap<>();
        ExtendedFormatFactory factory = new ExtendedFormatFactory();
        registry.put("upper", factory);

        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}, your {1} is {2}.", registry);
        Assert.assertNotNull(format);
        String result = format.format(new Object[]{"John", "age", 25});
        Assert.assertEquals("Hello John, your age is UPPER.", result);
    }

    @Test
    public void testToPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}, your {1} is {2}.", Locale.ENGLISH, Map.of("upper", new ExtendedFormatFactory()));
        String originalPattern = format.toPattern();

        format.applyPattern("Hello {0}, your {1:upper} is {2}.");
        String newPattern = format.toPattern();

        Assert.assertNotEquals(originalPattern, newPattern);
        Assert.assertEquals("Hello {0}, your {1} is {2}.", originalPattern);
        Assert.assertEquals("Hello {0}, your {1:} is {2}.", newPattern);
    }

    @Test
    public void testCustomFormat() {
        Map<String, ExtendedFormatFactory> registry = new java.util.HashMap<>();
        ExtendedFormatFactory factory = new ExtendedFormatFactory();

        registry.put("currency", factory);

        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}, you have {1:currency} in your account.", Locale.ENGLISH, registry);
        Assert.assertNotNull(format);

        format.applyPattern("Hello {0}, you have {1:currency(USD)} in your account.");

        Assert.assertEquals(1, format.getFormats().length);
        Assert.assertEquals(factory, format.getFormats()[0]);

        String result = format.format(new Object[]{"John", 12345.67});
        Assert.assertEquals("Hello John, you have $12,345.67 in your account.", result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}, your {1} is {2}.", Locale.ENGLISH);
        format.setFormatByArgumentIndex(1, new MyFormat());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}, your {1} is {2}.", Locale.ENGLISH);
        format.setFormatsByArgumentIndex(new Format[]{new MyFormat(), new MyFormat()});
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}, your {1} is {2}.", Locale.ENGLISH);
        format.setFormat(0, new MyFormat());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}, your {1} is {2}.", Locale.ENGLISH);
        format.setFormats(new Format[]{new MyFormat(), new MyFormat()});
    }

    private class MyFormat extends Format {
        @Override
        public String format(Object obj, String pattern, Locale locale) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Object parseObject(String source, ParsePosition pos) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String format(Object obj, Locale locale) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ParsePosition parse(String source, ParsePosition pos, Object param) {
            throw new UnsupportedOperationException();
        }
    }
}