import org.apache.commons.lang3.text.ExtendedMessageFormat;
import org.apache.commons.lang3.text.MessageFormat;
import org.apache.commons.lang3.text.ParsePosition;
import org.apache.commons.lang3.Validate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.text.Format;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ExtendedMessageFormatTest {

    private static final String DUMMY_PATTERN = "";

    private ExtendedMessageFormat extendedMessageFormat;
    private MessageFormat messageFormat;
    private ParsePosition parsePosition;

    @Before
    public void setUp() {
        parsePosition = new ParsePosition(0);
        messageFormat = new MessageFormat(DUMMY_PATTERN);
        extendedMessageFormat = new ExtendedMessageFormat(DUMMY_PATTERN);
    }

    @Test
    public void testDefaultConstructor() {
        Assert.assertNotNull(extendedMessageFormat);
    }

    @Test
    public void testConstructorWithPattern() {
        Assert.assertNotNull(extendedMessageFormat = new ExtendedMessageFormat("{0}"));
    }

    @Test
    public void testConstructorWithPatternAndLocale() {
        Assert.assertNotNull(extendedMessageFormat = new ExtendedMessageFormat("{0}", Locale.US));
    }

    @Test
    public void testConstructorWithPatternAndRegistry() {
        final Map<String, FormatFactory> registry = new HashMap<>();
        Assert.assertNotNull(extendedMessageFormat = new ExtendedMessageFormat("{0}", Locale.US, registry));
    }

    @Test
    public void testConstructorWithPatternAndLocaleAndRegistry() {
        final Map<String, FormatFactory> registry = new HashMap<>();
        Assert.assertNotNull(extendedMessageFormat = new ExtendedMessageFormat("{0}", Locale.US, registry));
    }

    @Test
    public void testToPatternMethod() {
        extendedMessageFormat = new ExtendedMessageFormat("{0} {1}");
        Assert.assertEquals("{0} {1}", extendedMessageFormat.toPattern());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatMethod() {
        extendedMessageFormat = new ExtendedMessageFormat("{0}");
        extendedMessageFormat.setFormat(0, messageFormat);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndexMethod() {
        extendedMessageFormat = new ExtendedMessageFormat("{0}");
        extendedMessageFormat.setFormatByArgumentIndex(0, messageFormat);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsMethod() {
        extendedMessageFormat = new ExtendedMessageFormat("{0}");
        extendedMessageFormat.setFormats(new Format[0]);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndexMethod() {
        extendedMessageFormat = new ExtendedMessageFormat("{0}");
        extendedMessageFormat.setFormatsByArgumentIndex(new Format[0]);
    }

    @Test
    public void testEqualsMethod() {
        final ExtendedMessageFormat extendedMessageFormat1 = new ExtendedMessageFormat("{0}");
        final ExtendedMessageFormat extendedMessageFormat2 = new ExtendedMessageFormat("{0}");
        final ExtendedMessageFormat extendedMessageFormat3 = new ExtendedMessageFormat("{1}");

        Assert.assertTrue(extendedMessageFormat1.equals(extendedMessageFormat1));
        Assert.assertTrue(extendedMessageFormat1.equals(extendedMessageFormat2));
        Assert.assertFalse(extendedMessageFormat1.equals(extendedMessageFormat3));
        Assert.assertFalse(extendedMessageFormat1.equals(null));
        Assert.assertFalse(extendedMessageFormat1.equals(new Object()));
    }

    @Test
    public void testHashCodeMethod() {
        final ExtendedMessageFormat extendedMessageFormat1 = new ExtendedMessageFormat("{0}");
        final ExtendedMessageFormat extendedMessageFormat2 = new ExtendedMessageFormat("{0}");
        final ExtendedMessageFormat extendedMessageFormat3 = new ExtendedMessageFormat("{1}");

        Assert.assertEquals(extendedMessageFormat1.hashCode(), extendedMessageFormat1.hashCode());
        Assert.assertEquals(extendedMessageFormat1.hashCode(), extendedMessageFormat2.hashCode());
        Assert.assertNotEquals(extendedMessageFormat1.hashCode(), extendedMessageFormat3.hashCode());
        Assert.assertNotEquals(extendedMessageFormat1.hashCode(), null.hashCode());
        Assert.assertNotEquals(extendedMessageFormat1.hashCode(), new Object().hashCode());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetFormatMethod() {
        extendedMessageFormat = new ExtendedMessageFormat("{0}");
        extendedMessageFormat.getFormat(0);
    }

    @Test
    public void testApplyPatternMethodWithEmptyRegistry() {
        extendedMessageFormat = new ExtendedMessageFormat("{0}{1}") {
            @Override
            protected Format getFormat(String desc) {
                return messageFormat.getFormat(desc);
            }
        };

        extendedMessageFormat.applyPattern("{0}{1}");
        Assert.assertEquals("{0}{1}", extendedMessageFormat.toPattern());
    }

    @Test
    public void testApplyPatternMethodWithRegistry() {
        final FormatFactory formatFactory = new FormatFactory() {
            @Override
            public Format getFormat(String name, String arg, Locale locale) {
                return new SimpleDateFormat(name, locale);
            }
        };

        final Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", formatFactory);

        extendedMessageFormat = new ExtendedMessageFormat("{date}") {
            @Override
            protected FormatFactory getFormat(String desc) {
                return registry.get(desc);
            }
        };

        extendedMessageFormat.applyPattern("{date}");
        Assert.assertEquals("{date}", extendedMessageFormat.toPattern());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testApplyPatternMethodWithInvalidFormatDescription() {
        extendedMessageFormat = new ExtendedMessageFormat("{0}{1}") {
            @Override
            protected FormatFactory getFormat(String desc) {
                return null;
            }
        };

        extendedMessageFormat.applyPattern("{0}{1}");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testApplyPatternMethodWithUnmatchedFormatElement() {
        extendedMessageFormat = new ExtendedMessageFormat("{0}{1}") {
            @Override
            protected FormatFactory getFormat(String desc) {
                if ("date".equals(desc)) {
                    return new FormatFactory() {
                        @Override
                        public Format getFormat(String name, String arg, Locale locale) {
                            return new SimpleDateFormat(name, locale);
                        }
                    };
                }
                return null;
            }
        };

        extendedMessageFormat.applyPattern("{0}{2}");
    }

    @Test
    public void testGetFormatsMethod() {
        final FormatFactory formatFactory = new FormatFactory() {
            @Override
            public Format getFormat(String name, String arg, Locale locale) {
                return new SimpleDateFormat(name, locale);
            }
        };

        final Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", formatFactory);

        extendedMessageFormat = new ExtendedMessageFormat("{date}") {
            @Override
            protected FormatFactory getFormat(String desc) {
                return registry.get(desc);
            }
        };

        extendedMessageFormat.applyPattern("{date}");

        Assert.assertNotNull(extendedMessageFormat.getFormats());
        Assert.assertEquals(1, extendedMessageFormat.getFormats().length);
        Assert.assertNotNull(extendedMessageFormat.getFormats()[0]);
    }
}

class FormatFactory {
    public abstract Format getFormat(String name, String arg, Locale locale);
}