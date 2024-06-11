import org.apache.commons.lang.text.ExtendedMessageFormat;
import org.apache.commons.lang.text.FormatFactory;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ExtendedMessageFormatTest {

    private static final String DUMMY_PATTERN = "";
    private static final String ESCAPED_QUOTE = "''";
    private static final char START_FMT = ',';
    private static final char END_FE = '}';
    private static final char START_FE_FORMAT = '{';
    private static final char START_FE_ARG = '{';
    private static final char QUOTE = '\'';

    @Test
    public void testDefaultConstruction() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}!");
        Assertions.assertEquals("Hello {0}!", format.toPattern());
    }

    @Test
    public void testConstructionWithLocale() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0,choice,0#Mr.|1#Mrs.|1#Ms.} {1}!", Locale.US);
        Assertions.assertEquals("Hello {0,choice,0#Mr.|1#Mrs.|1#Ms.} {1}!", format.toPattern());
    }

    @Test
    public void testConstructionWithRegistry() {
        FormatFactory formatFactory = new FormatFactory() {
            @Override
            public Format getFormat(String name, String arguments, Locale locale) {
                if (name.equals("customFormat")) {
                    return new CustomFormat();
                }
                return null;
            }
        };

        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customFormat", formatFactory);

        ExtendedMessageFormat format = new ExtendedMessageFormat("{customFormat,choice,0#Hello|1#Hi} {0}!", registry);
        Assertions.assertEquals("{customFormat,choice,0#Hello|1#Hi} {0}!", format.toPattern());
    }

    @Test
    public void testConstructionWithRegistryAndLocale() {
        FormatFactory formatFactory = new FormatFactory() {
            @Override
            public Format getFormat(String name, String arguments, Locale locale) {
                if (name.equals("customFormat")) {
                    return new CustomFormat();
                }
                return null;
            }
        };

        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customFormat", formatFactory);

        ExtendedMessageFormat format = new ExtendedMessageFormat("{customFormat,choice,0#Hello|1#Hi} {0}!", registry, Locale.US);
        Assertions.assertEquals("{customFormat,choice,0#Hello|1#Hi} {0}!", format.toPattern());
    }

    @Test
    public void testApplyPattern() {
        String pattern = "{customFormat,choice,0#Hello|1#Hi} {0}!";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        format.applyPattern(pattern);
        Assertions.assertEquals(pattern, format.toPattern());
    }

    @Test
    public void testApplyPatternWithRegistry() {
        String pattern = "{customFormat,choice,0#Hello|1#Hi} {0}!";
        FormatFactory formatFactory = new FormatFactory() {
            @Override
            public Format getFormat(String name, String arguments, Locale locale) {
                if (name.equals("customFormat")) {
                    return new CustomFormat();
                }
                return null;
            }
        };

        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customFormat", formatFactory);

        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        format.applyPattern(pattern, registry);
        Assertions.assertEquals(pattern, format.toPattern());
    }

    @Test
    public void testFormatting() throws ParseException {
        FormatFactory formatFactory = new FormatFactory() {
            @Override
            public Format getFormat(String name, String arguments, Locale locale) {
                if (name.equals("customFormat")) {
                    return new CustomFormat();
                }
                return null;
            }
        };

        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customFormat", formatFactory);

        ExtendedMessageFormat format = new ExtendedMessageFormat("{customFormat,choice,0#Hello|1#Hi} {0}!", registry);
        format.applyPattern("{customFormat,choice,0#Hello|1#Hi} {0}!");

        Object[] args = new Object[]{"John"};
        String formatted = format.format(args);
        Assertions.assertEquals("Hi John!", formatted);
    }

    @Test
    public void testUnsupportedOperations() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0} {1}");
        Assertions.assertThrows(UnsupportedOperationException.class,
                () -> format.setFormat(0, new CustomFormat()));
        Assertions.assertThrows(UnsupportedOperationException.class,
                () -> format.setFormats(new CustomFormat[]{}));
        Assertions.assertThrows(UnsupportedOperationException.class,
                () -> format.setFormatsByArgumentIndex(new CustomFormat[]{}, 0));
    }

    @Test
    public void testToPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0} {1}");
        Assertions.assertEquals("{0} {1}", format.toPattern());
    }

    private static class CustomFormat extends Format {
        @Override
        public String format(Object obj, Locale locale, String format, ParsePosition parsePosition) {
            if (obj == null) {
                return "";
            }
            return StringUtils.capitalize((String) obj);
        }

        @Override
        public Object parseObject(String source, ParsePosition parsePosition) {
            return null;
        }
    }
}