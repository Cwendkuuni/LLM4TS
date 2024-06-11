import org.apache.commons.lang.text.ExtendedMessageFormat;
import org.apache.commons.lang.text.FormatFactory;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExtendedMessageFormatTest {

    @Test
    public void testExtendedMessageFormatConstruction() {
        String pattern = "Hello {0, choice, 0#Foo, 1#Bar}{1, date, " +
                "short, 'EEEE dd MMMM yyyy'}";
        ExtendedMessageFormat fmt = new ExtendedMessageFormat(pattern);
        assertEquals("", fmt.toPattern());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExtendedMessageFormatConstructionWithInvalidPattern() {
        String pattern = "Hello {0, choice, 0#Foo, 1#Bar}";
        new ExtendedMessageFormat(pattern);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExtendedMessageFormatConstructionWithNullPattern() {
        new ExtendedMessageFormat(null);
    }

    @Test
    public void testExtendedMessageFormatSetLocale() {
        String pattern = "{0, choice, 0#Foo, 1#Bar}";
        ExtendedMessageFormat fmt = new ExtendedMessageFormat(pattern);
        Locale locale = Locale.FRENCH;
        fmt.setLocale(locale);
        assertEquals(locale, fmt.getLocale());
    }

    @Test
    public void testExtendedMessageFormatApplyPattern() {
        String pattern = "{0, choice, 0#Foo, 1#Bar}";
        ExtendedMessageFormat fmt = new ExtendedMessageFormat(pattern);
        Object[] args = new Object[]{"Foo", "Bar"};
        String result = fmt.format(args);
        assertEquals("Foo", result);
        result = fmt.applyPattern("{0, choice, 0#Bar, 1#Foo}");
        fmt.applyPattern("{0, choice, 0#Bar, 1#Foo}");
        assertEquals("Bar", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExtendedMessageFormatApplyPatternWithInvalidPattern() {
        String pattern = "{0, choice, 0#Foo, 1#Bar}";
        ExtendedMessageFormat fmt = new ExtendedMessageFormat(pattern);
        fmt.applyPattern("{0, choice, 0#Foo, 1#Bar}");
        fmt.applyPattern("{0, choice, 0#Bar, 1#Foo}");
        fmt.applyPattern("{1, date, short, 'EEEE dd MMMM yyyy'}");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testExtendedMessageFormatSetFormatByArgumentIndex() {
        String pattern = "{0, choice, 0#Foo, 1#Bar}";
        ExtendedMessageFormat fmt = new ExtendedMessageFormat(pattern);
        fmt.setFormatByArgumentIndex(0, new Format() {});
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testExtendedMessageFormatSetFormat() {
        String pattern = "{0, choice, 0#Foo, 1#Bar}";
        ExtendedMessageFormat fmt = new ExtendedMessageFormat(pattern);
        fmt.setFormat(0, new Format() {});
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testExtendedMessageFormatSetFormats() {
        String pattern = "{0, choice, 0#Foo, 1#Bar}";
        ExtendedMessageFormat fmt = new ExtendedMessageFormat(pattern);
        Format[] formats = new Format[1];
        fmt.setFormats(formats);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testExtendedMessageFormatSetFormatsByArgumentIndex() {
        String pattern = "{0, choice, 0#Foo, 1#Bar}";
        ExtendedMessageFormat fmt = new ExtendedMessageFormat(pattern);
        Format[] formats = new Format[1];
        fmt.setFormatsByArgumentIndex(formats);
    }

    @Test
    public void testExtendedMessageFormatFormatWithCustomFormat() {
        String pattern = "{0, choice, 0#{1, number, #, 0#CustomFormat}, 1#Bar}";
        ExtendedMessageFormat fmt = new ExtendedMessageFormat(pattern,
                new CustomFormatRegistry());
        Object[] args = new Object[]{123, "Bar"};
        String result = fmt.format(args);
        assertEquals("123", result);
    }

    @Test
    public void testExtendedMessageFormatFormatWithCustomFormatStyle() {
        String pattern = "{0, choice, 0#{1, number, #, 0#CustomFormat}, 1#Bar}";
        ExtendedMessageFormat fmt = new ExtendedMessageFormat(pattern,
                new CustomFormatRegistry("CustomFormat", "CustomStyle"));
        Object[] args = new Object[]{123.5, "Bar"};
        String result = fmt.format(args);
        assertEquals("123.5 (CustomStyle)", result);
    }

    @Test
    public void testExtendedMessageFormatFormatWithCustomFormatAndStyle() {
        String pattern = "{0, choice, 0#{1, number, #, 0#CustomFormat}, 1#Bar}";
        ExtendedMessageFormat fmt = new ExtendedMessageFormat(pattern,
                new CustomFormatRegistry("CustomFormat", "CustomStyle1") {
                    @Override
                    public Format getFormat(String name, String style, Locale locale) {
                        return new Format() {
                            @Override
                            public String format(Object obj, Locale locale,
                                                 String format, ParsePosition pos) {
                                return String.format("%s (%s)", obj, style);
                            }
                        };
                    }
                });
        Object[] args = new Object[]{123.5, "Bar"};
        String result = fmt.format(args);
        assertEquals("123.5 (CustomStyle1)", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExtendedMessageFormatFormatWithUnregisteredFormat() {
        String pattern = "{0, choice, 0#{1, number, #, 0#CustomFormat}, 1#Bar}";
        ExtendedMessageFormat fmt = new ExtendedMessageFormat(pattern,
                new CustomFormatRegistry());
        Object[] args = new Object[]{123.5, "Bar"};
        fmt.applyPattern("{0, choice, 0#{1, number, #, 0#UnknownFormat}, 1#Bar}");
    }

    @Test
    public void testExtendedMessageFormatFormatWithRegisteredFormat() {
        String pattern = "{0, choice, 0#{1, number, #, 0#CustomFormat}, 1#Bar}";
        ExtendedMessageFormat fmt = new ExtendedMessageFormat(pattern,
                new CustomFormatRegistry("CustomFormat", new CustomNumberFormat()));
        Object[] args = new Object[]{123.5, "Bar"};
        String result = fmt.format(args);
        assertEquals("123.5 (CustomNumberFormat)", result);
    }

    private static class CustomFormatRegistry implements Map<String, FormatFactory> {
        private Map<String, FormatFactory> delegatedMap = new HashMap<>();

        public CustomFormatRegistry() {
            delegatedMap.put("CustomFormat", new CustomFormatFactory());
        }

        public CustomFormatRegistry(String formatName, FormatFactory formatFactory) {
            delegatedMap.put(formatName, formatFactory);
        }

        @Override
        public int size() {
            return delegatedMap.size();
        }

        @Override
        public boolean isEmpty() {
            return delegatedMap.isEmpty();
        }

        @Override
        public boolean containsKey(Object key) {
            return delegatedMap.containsKey(key);
        }

        @Override
        public FormatFactory get(Object key) {
            return (FormatFactory) delegatedMap.get(key);
        }

        @Override
        public FormatFactory put(String key, FormatFactory value) {
            return (FormatFactory) delegatedMap.put(key, value);
        }

        @Override
        public FormatFactory remove(Object key) {
            return (FormatFactory) delegatedMap.remove(key);
        }

        @Override
        public void putAll(Map<? extends String, ? extends FormatFactory> m) {
            delegatedMap.putAll(m);
        }

        @Override
        public void clear() {
            delegatedMap.clear();
        }

        @Override
        public Collection<String> keySet() {
            return delegatedMap.keySet();
        }

        @Override
        public Collection<FormatFactory> values() {
            return delegatedMap.values();
        }

        @Override
        public Collection<Entry<String, FormatFactory>> entrySet() {
            return delegatedMap.entrySet();
        }
    }

    private static class CustomFormatFactory implements FormatFactory {
        @Override
        public Format getFormat(String name, String style, Locale locale) {
            if ("CustomFormat".equals(name)) {
                return new CustomFormat();
            }
            throw new UnsupportedOperationException();
        }
    }

    private static class CustomFormat implements Format {
        @Override
        public String format(Object obj, Locale locale, String format, ParsePosition pos) {
            return String.format("%s (%s)", obj, "CustomFormat");
        }
    }

    private static class CustomNumberFormat implements Format {
        @Override
        public String format(Object obj, Locale locale, String format, ParsePosition pos) {
            Number number = (Number) obj;
            return String.format("%.2f", number);
        }
    }
}