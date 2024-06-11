import org.apache.commons.lang.text.ExtendedMessageFormat;
import org.apache.commons.lang.text.FormatFactory;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExtendedMessageFormatTest {

 private static final String DUMMY_FORMAT_NAME = "DummyFormat";
 private static final String DUMMY_FORMAT_STYLE = "style1";
 private static final String DUMMY_PATTERN = "{0,formatName:DUMMY_FORMAT_NAME,formatStyle:DUMMY_FORMAT_STYLE}";

 @Test
 public void testValidConstruction() {
 ExtendedMessageFormat format = new ExtendedMessageFormat(DUMMY_PATTERN);
 assertEquals(DUMMY_PATTERN, format.toPattern());
 }

 @Test
 public void testRegistryNull() {
 ExtendedMessageFormat format = new ExtendedMessageFormat(DUMMY_PATTERN, null);
 assertEquals(DUMMY_PATTERN, format.toPattern());
 }

 @Test
 public void testRegistryEmpty() {
 Map<String, FormatFactory> registry = new HashMap<>();
 ExtendedMessageFormat format = new ExtendedMessageFormat(DUMMY_PATTERN, registry);
 assertEquals(DUMMY_PATTERN, format.toPattern());
 }

 @Test
 public void testRegistryValid() {
 Map<String, FormatFactory> registry = new HashMap<>();
 registry.put(DUMMY_FORMAT_NAME, new MyFormatFactory());
 ExtendedMessageFormat format = new ExtendedMessageFormat(DUMMY_PATTERN, registry);
 assertEquals(DUMMY_PATTERN, format.toPattern());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidPattern() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("{0,formatName:DUMMY_FORMAT_NAME,formatStyle:DUMMY_FORMAT_STYLE}abc");
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testGetFormat() {
 ExtendedMessageFormat format = new ExtendedMessageFormat(DUMMY_PATTERN, registry);
 Format result = format.getFormat(DUMMY_FORMAT_NAME, DUMMY_FORMAT_STYLE);
 assertNotNull(result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetFormatNullFormatFactory() {
 Map<String, FormatFactory> registry = new HashMap<>();
 registry.put(DUMMY_FORMAT_NAME, null);
 ExtendedMessageFormat format = new ExtendedMessageFormat(DUMMY_PATTERN, registry);
 format.getFormat(DUMMY_FORMAT_NAME, DUMMY_FORMAT_STYLE);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testGetFormatEmptyRegistry() {
 ExtendedMessageFormat format = new ExtendedMessageFormat(DUMMY_PATTERN, new HashMap<>());
 Format result = format.getFormat(DUMMY_FORMAT_NAME, DUMMY_FORMAT_STYLE);
 assertNull(result);
 }

 @Test
 public void testGetFormatNoMatch() {
 ExtendedMessageFormat format = new ExtendedMessageFormat(DUMMY_PATTERN, registry);
 Format result = format.getFormat("NO_MATCH", DUMMY_FORMAT_STYLE);
 assertNull(result);
 }

 @Test
 public void testToPattern() {
 ExtendedMessageFormat format = new ExtendedMessageFormat(DUMMY_PATTERN, registry);
 String pattern = format.toPattern();
 assertEquals(DUMMY_PATTERN, pattern);
 }

 @Test
 public void testToPatternEmpty() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("");
 String pattern = format.toPattern();
 assertEquals("", pattern);
 }

 @Test
 public void testToPatternWithFormats() {
 ExtendedMessageFormat format = new ExtendedMessageFormat(DUMMY_PATTERN, registry);
 format.applyPattern(DUMMY_PATTERN);
 String pattern = format.toPattern();
 assertEquals(DUMMY_PATTERN, pattern);
 }

 @Test
 public void testFormat() {
 ExtendedMessageFormat format = new ExtendedMessageFormat(DUMMY_PATTERN, registry);
 format.applyPattern(DUMMY_PATTERN);
 Object[] args = new Object[] { "test" };
 String result = format.format(args);
 assertEquals("test", result);
 }

 @Test
 public void testFormatWithFormat() {
 ExtendedMessageFormat format = new ExtendedMessageFormat(DUMMY_PATTERN, registry);
 format.applyPattern(DUMMY_PATTERN);
 Object[] args = new Object[] { new MyCustomFormat(), "test" };
 String result = format.format(args);
 assertEquals("test", result);
 }

 @Test
 public void testFormatWithCustomPattern() {
 ExtendedMessageFormat format = new ExtendedMessageFormat(DUMMY_PATTERN, registry);
 Format customFormat = new MyCustomFormat();
 format.setFormat(0, customFormat);
 Object[] args = new Object[] { "test" };
 String result = format.format(args);
 assertEquals("test", result);
 }

 @Test
 public void testFormatWithCustomPatternStyle() {
 ExtendedMessageFormat format = new ExtendedMessageFormat(DUMMY_PATTERN, registry);
 Format customFormat = new MyCustomFormat();
 format.setFormatByArgumentIndex(0, customFormat, "style2");
 Object[] args = new Object[] { "test" };
 String result = format.format(args);
 assertEquals("test", result);
 }

 @Test
 public void testFormatWithCustomPatterns() {
 ExtendedMessageFormat format = new ExtendedMessageFormat(DUMMY_PATTERN, registry);
 format.applyPattern(DUMMY_PATTERN);
 Format[] customFormats = new Format[] { new MyCustomFormat() };
 format.setFormats(customFormats);
 Object[] args = new Object[] { "test" };
 String result = format.format(args);
 assertEquals("test", result);
 }

 @Test
 public void testFormatWithCustomPatternsByArgumentIndex() {
 ExtendedMessageFormat format = new ExtendedMessageFormat(DUMMY_PATTERN, registry);
 format.applyPattern(DUMMY_PATTERN);
 Format[] customFormats = new Format[] { new MyCustomFormat() };
 format.setFormatsByArgumentIndex(new Format[0], customFormats);
 Object[] args = new Object[] { "test" };
 String result = format.format(args);
 assertEquals("test", result);
 }

 @Test
 public void testFormatWithInvalidIndex() {
 ExtendedMessageFormat format = new ExtendedMessageFormat(DUMMY_PATTERN, registry);
 format.applyPattern(DUMMY_PATTERN);
 throw new UnsupportedOperationException();
 }
}

class MyFormatFactory implements FormatFactory {
 @Override
 public Format getFormat(String formatName, String formatStyle, Locale locale) {
 return new MyCustomFormat();
 }
}

class MyCustomFormat extends Format {
 @Override
 public String format(Object obj, String format, Locale locale, ParsePosition parsePosition) {
 return StringUtils.defaultString((String) obj);
 }
}