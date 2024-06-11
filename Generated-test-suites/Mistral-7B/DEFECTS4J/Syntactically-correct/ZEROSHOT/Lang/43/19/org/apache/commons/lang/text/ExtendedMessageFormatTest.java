import org.apache.commons.lang.text.ExtendedMessageFormat;
import org.apache.commons.lang.text.FormatFactory;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ExtendedMessageFormatTest {

    @Test
    public void testExtendedMessageFormat() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customFormat", new CustomFormatFactory());

        String pattern = "{0,format} {1,customFormat} {2}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        Object[] args = new Object[]{"%d %05d", "12345", "abc"};

        String formatted = format.format(args);
        Assert.assertEquals("12345  12345  abc", formatted);

        try {
            ParsePosition pos = new ParsePosition(0);
            MessageFormat.parse(formatted, pos, Locale.getDefault());
            Assert.fail();
        } catch (ParseException e) {
            // expected
        }

        String customPattern = "{3,format} {4,customFormat}";
        format.applyPattern(customPattern);
        formatted = format.format(args);
        Assert.assertEquals("12345  12345  abc", formatted);

        try {
            ParsePosition pos = new ParsePosition(0);
            MessageFormat.parse(formatted, pos, Locale.getDefault());
            Assert.fail();
        } catch (ParseException e) {
            // expected
        }
    }

    @Test
    public void testCustomFormat() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customFormat", new CustomFormatFactory());

        String pattern = "{0,format} {1,customFormat} {2}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        Object[] args = new Object[]{"%d %05d", "12345", "abc"};

        String formatted = format.format(args);
        Assert.assertEquals("12345  12345  abc", formatted);

        String customPattern = "{3,format} {4,customFormat}";
        format.applyPattern(customPattern);
        formatted = format.format(args);
        Assert.assertEquals("12345  12345  abc", formatted);

        String customFormatPattern = "CustomFormat[argIndex={0}]";
        format.applyPattern(customFormatPattern);
        formatted = format.format(args);
        Assert.assertEquals("12345 CustomFormat[argIndex=0] 12345  abc", formatted);

        try {
            ParsePosition pos = new ParsePosition(0);
            MessageFormat.parse(formatted, pos, Locale.getDefault());
            Assert.fail();
        } catch (ParseException e) {
            // expected
        }
    }

    private static class CustomFormatFactory implements FormatFactory {
        @Override
        public Format getFormat(String name, String arguments, Locale locale) {
            if (StringUtils.equals("customFormat", name)) {
                return new CustomFormat();
            }
            return null;
        }
    }

    private static class CustomFormat implements Format {
        @Override
        public String format(Object obj) {
            return "CustomFormat[" + obj + "]";
        }

        @Override
        public Object parseObject(String source, ParsePosition pos) throws ParseException {
            throw new UnsupportedOperationException();
        }

        @Override
        public StringBuffer format(StringBuffer sb, Object obj, Locale locale) {
            sb.append("CustomFormat[").append(obj).append(']');
            return sb;
        }

        @Override
        public ParsePosition parse(String source, ParsePosition pos, Locale locale) throws ParseException {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getPattern() {
            return null;
        }

        @Override
        public Class<?> getParameterTypes() {
            return Object[].class;
        }

        @Override
        public void setLocale(Locale locale) {
        }
    }
}