import org.apache.commons.lang.text.ExtendedMessageFormat;
import org.apache.commons.lang.text.FormatFactory;
import org.junit.Assert;
import org.junit.Test;

import java.text.MessageFormat;
import java.text.ParsePosition;
import java.util.HashMap;
import java.util.Locale;

public class ExtendedMessageFormatTest {

    @Test
    public void testBasicUsage() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customFormat", new CustomFormatFactory());

        String pattern = "{0,choice,0#Hello|1#Goodbye}{1,number,integer}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        Object[] args = new Object[]{"Hello", 2};
        String result = format.format(args);
        Assert.assertEquals("Hello2", result);
    }

    @Test
    public void testMissingFormatFactory() {
        Map<String, FormatFactory> registry = new HashMap<>();
        String pattern = "{0,choice,0#Hello|1#Goodbye}{1,format,missingFormat}";

        try {
            ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testNullFormatFactory() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("nullFormatFactory", null);
        String pattern = "{0,format,nullFormatFactory}";

        try {
            ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
            Assert.fail();
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testFormatFactoryThrowsException() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customFormat", new CustomFormatFactoryThrowingException());
        String pattern = "{0,format,customFormat}";

        try {
            ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
            Object[] args = new Object[]{"Test"};
            format.format(args);
            Assert.fail();
        } catch (Exception e) {
            // expected
        }
    }

    @Test
    public void testCustomFormatWithArgs() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customFormat", new CustomFormatFactoryWithArgs());
        String pattern = "{0,format,customFormat} {1}";

        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        Object[] args = new Object[]{"Hello", "World"};
        String result = format.format(args);
        Assert.assertEquals("Hello World", result);
    }

    @Test
    public void testCustomFormatWithLocale() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customFormat", new CustomFormatFactoryWithLocale());
        String pattern = "{0,format,customFormat} {1}";

        ExtendedMessageFormat formatFR = new ExtendedMessageFormat(pattern, registry, Locale.FRENCH);
        Object[] args = new Object[]{"Hello", "World"};
        String result = formatFR.format(args);
        Assert.assertEquals("Hello Monde", result);

        ExtendedMessageFormat formatEN = new ExtendedMessageFormat(pattern, registry, Locale.ENGLISH);
        Object[] argsEN = new Object[]{"Hello", "World"};
        String resultEN = formatEN.format(argsEN);
        Assert.assertEquals("Hello World", resultEN);
    }

    @Test
    public void testInvalidFormatDescription() {
        Map<String, FormatFactory> registry = new HashMap<>();
        String pattern = "{0,format,invalidFormat}";

        try {
            ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testInvalidArgumentIndex() {
        Map<String, FormatFactory> registry = new HashMap<>();
        String pattern = "{}";

        try {
            ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
            Object[] args = new Object[]{"Test"};
            format.format(args);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testInvalidFormatElement() {
        Map<String, FormatFactory> registry = new HashMap<>();
        String pattern = "{{0}}";

        try {
            ExtendedMessageFormat format = new ExtendedMessageFormat(pattern,
                    registry);
            Object[] args = new Object[]{"Test"};
            format.format(args);
            Assert.fail();
        } catch (MessageFormatException e) {
            // expected
        }
    }

    @Test
    public void testInvalidFormatWithFormat() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customFormat", new CustomFormatWithFormat());
        String pattern = "{0,format,customFormat}{1}";

        try {
            ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
            Object[] args = new Object[]{"Test", null};
            format.format(args);
            Assert.fail();
        } catch (NullPointerException e) {
            // expected
        }
    }

    private static class CustomFormatFactory implements FormatFactory {
        @Override
        public Format getFormat(String name, String format, Locale locale) {
            if (name.equals("customFormat")) {
                return new CustomFormat();
            }
            throw new IllegalArgumentException("Unknown format: " + name);
        }
    }

    private static class CustomFormatFactoryWithArgs implements FormatFactory {
        @Override
        public Format getFormat(String name, String format, Locale locale) {
            if (name.equals("customFormat")) {
                return new CustomFormatWithArgs();
            }
            throw new IllegalArgumentException("Unknown format: " + name);
        }
    }

    private static class CustomFormatFactoryWithLocale implements FormatFactory {
        @Override
        public Format getFormat(String name, String format, Locale locale) {
            if (name.equals("customFormat")) {
                return new CustomFormatWithLocale();
            }
            throw new IllegalArgumentException("Unknown format: " + name);
        }
    }

    private static class CustomFormatFactoryThrowingException implements FormatFactory {
        @Override
        public Format getFormat(String name, String format, Locale locale) {
            throw new RuntimeException("Custom format factory exception");
        }
    }

    private static class CustomFormat implements Format {
        @Override
        public Object parseObject(String source, ParsePosition pos) {
            return "CustomFormat";
        }

        @Override
        public String format(Object object, Locale locale) {
            return "CustomFormat";
        }
    }

    private static class CustomFormatWithArgs implements Format {
        @Override
        public Object parseObject(String source, ParsePosition pos) {
            Object arg = parseObject(source, pos);
            int index = Integer.parseInt(readArgumentIndex(source, pos));
            return arg + "";
        }

        @Override
        public String format(Object object, Locale locale) {
            return "FormatArg{" + object + "}";
        }

        private Object parseObject(String source, ParsePosition pos) {
            int start = pos.getIndex();
            pos.nextToken();
            int index = readArgumentIndex(source, pos);
            throw new UnsupportedOperationException("parseObject not supported");
        }

        private int readArgumentIndex(String source, ParsePosition pos) {
            int start = pos.getIndex();
            pos.nextToken();
            int len = 0;
            while (Character.isDigit(source.charAt(pos.getIndex()))) {
                len *= 10;
                len += Character.isDigit(source.charAt(pos.getIndex()))
                        ? Character.digit(source.charAt(pos.getIndex())) : 0;
                next(pos);
            }
            return len;
        }

        private void next(ParsePosition pos) {
            pos.setIndex(pos.getIndex() + 1);
        }
    }

    private static class CustomFormatWithLocale implements Format {
        @Override
        public Object parseObject(String source, ParsePosition pos) {
            Locale locale = parseLocale(source, pos);
            int index = readArgumentIndex(source, pos);
            return new LocaleFormat(locale, index);
        }

        @Override
        public String format(Object object, Locale locale) {
            return "LocaleFormat{" + locale + "}";
        }

        private Locale parseLocale(String source, ParsePosition pos) {
            int start = pos.getIndex();
            pos.nextToken();
            int len = 0;
            while (Character.isLetterOrDigit(source.charAt(pos.getIndex()))) {
                len *= 26;
                len += Character.isUpperCase(source.charAt(pos.getIndex()))
                        ? Character.toUpperCase(source.charAt(pos.getIndex()))
                        : Character.toLowerCase(source.charAt(pos.getIndex())) - 'a';
                next(pos);
            }
            if (pos.getIndex() < source.length()) {
                char sep = source.charAt(pos.getIndex());
                if (sep == '_') {
                    next(pos);
                    String country = "";
                    while (Character.isLetterOrDigit(source.charAt(pos.getIndex()))) {
                        country += source.charAt(pos.getIndex());
                        next(pos);
                    }
                    return new Locale(source.substring(start, pos.getIndex()), country);
                }
            }
            return new Locale(source.substring(start, pos.getIndex()));
        }
    }

    private static class CustomFormatWithFormat implements Format {
        @Override
        public Object parseObject(String source, ParsePosition pos) {
            String formatName = parseFormatName(source, pos);
            return new CustomFormatWithFormat(formatName);
        }

        @Override
        public String format(Object object, Locale locale) {
            throw new UnsupportedOperationException();
        }

        private String parseFormatName(String source, ParsePosition pos) {
            int start = pos.getIndex();
            pos.nextToken();
            String formatName = "";
            while (Character.isLetterOrDigit(source.charAt(pos.getIndex()))) {
                formatName += source.charAt(pos.getIndex());
                next(pos);
            }
            return formatName;
        }

        private CustomFormatWithFormat(String formatName) {
            this.formatName = formatName;
        }

        private final String formatName;
    }
}