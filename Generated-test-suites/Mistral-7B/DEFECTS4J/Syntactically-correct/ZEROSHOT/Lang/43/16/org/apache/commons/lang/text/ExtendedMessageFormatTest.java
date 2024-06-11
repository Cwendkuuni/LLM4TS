import org.apache.commons.lang.text.ExtendedMessageFormat;
import org.apache.commons.lang.text.FormatFactory;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExtendedMessageFormatTest {

    @Test
    public void testExtendedMessageFormat() {
        // Test empty constructor
        ExtendedMessageFormat emptyMessageFormat = new ExtendedMessageFormat("");
        assertEquals("", emptyMessageFormat.toPattern());

        // Test constructor with pattern and default locale
        ExtendedMessageFormat patternMessageFormat = new ExtendedMessageFormat("{0} {1}", Locale.getDefault());
        assertEquals("{0} {1}", patternMessageFormat.toPattern());

        // Test constructor with pattern, locale, and registry
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customFormat", new FormatFactory() {
            @Override
            public Format getFormat(String name, String format, Locale locale) {
                return new MyFormat();
            }
        });
        ExtendedMessageFormat registryMessageFormat = new ExtendedMessageFormat("{0, format(\"customFormat\")}", Locale.getDefault(), registry);
        assertEquals("{0, format(\"customFormat\")}", registryMessageFormat.toPattern());

        // Test getFormats() method throws UnsupportedOperationException
        try {
            registryMessageFormat.getFormats();
            fail("getFormats() should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }

        // Test setFormats() method throws UnsupportedOperationException
        try {
            registryMessageFormat.setFormats(new Format[0]);
            fail("setFormats() should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }

        // Test setFormat() method throws UnsupportedOperationException
        try {
            registryMessageFormat.setFormat(0, new MyFormat());
            fail("setFormat() should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }

        // Test setFormatsByArgumentIndex() method throws UnsupportedOperationException
        try {
            registryMessageFormat.setFormatsByArgumentIndex(new Format[0]);
            fail("setFormatsByArgumentIndex() should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    private static class MyFormat implements Format {
        @Override
        public Object parseObject(String source, ParsePosition pos) {
            return null;
        }

        @Override
        public String format(Object obj, Locale locale) {
            return "Custom Format";
        }

        @Override
        public StringBuffer format(Object obj, StringBuffer toAppendTo, Format parseObject) {
            toAppendTo.append("Custom Format");
            return toAppendTo;
        }

        @Override
        public StringBuffer format(Object obj, Locale locale, StringBuffer toAppendTo, Format parseObject) {
            toAppendTo.append("Custom Format");
            return toAppendTo;
        }
    }
}