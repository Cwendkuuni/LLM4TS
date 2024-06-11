import org.apache.commons.lang.text.ExtendedMessageFormat;
import org.apache.commons.lang.text.FormatFactory;
import org.apache.commons.lang.text.SimpleFormatFactory;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExtendedMessageFormatTest {

 private static final String DEFAULT_PATTERN = "{0,date,dd.MM.yyyy} {1} {2,number,number}";
 private static final String LOCALE_PATTERN = "{0,date,dd.MM.yyyy} {1,locale} {2,number,number}";
 private static final String REGISTRY_PATTERN = "{0,date,dd.MM.yyyy} {1,format:customDate} {2,number,number}";

 @Test
 public void testDefaultPattern() {
 ExtendedMessageFormat fmt = new ExtendedMessageFormat(DEFAULT_PATTERN);
 assertNotNull(fmt);
 Object[] args = new Object[]{"25", "April", 2023};
 String result = fmt.format(args);
 assertEquals("25 April 2023 1", result);
 }

 @Test
 public void testLocalePattern() {
 ExtendedMessageFormat fmt = new ExtendedMessageFormat(LOCALE_PATTERN, Locale.FRENCH);
 assertNotNull(fmt);
 Object[] args = new Object[]{"25", Locale.FRENCH, 2023};
 String result = fmt.format(args);
 assertEquals("25 avril 2023 1", result);
 }

 @Test
 public void testRegistryPattern() {
 FormatFactory customFactory = new SimpleFormatFactory();
 ExtendedMessageFormat fmt = new ExtendedMessageFormat(REGISTRY_PATTERN, null, Map.of("customDate", customFactory));
 assertNotNull(fmt);
 Object[] args = new Object[]{"25", "{0,date,dd.MM.yyyy}", 2023};
 String result = fmt.format(args);
 assertEquals("25 25.05.2023 1", result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidPattern() {
 ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0,date,dd.MM.yyyy}{1");
 assertNull(fmt);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidFormat() {
 ExtendedMessageFormat fmt = new ExtendedMessageFormat(DEFAULT_PATTERN);
 fmt.applyPattern("{0,date,dd.MM.yyyy} {1,format:invalid} {2,number,number}");
 assertNull(fmt.toPattern());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMissingFormatDescription() {
 ExtendedMessageFormat fmt = new ExtendedMessageFormat(DEFAULT_PATTERN, null, null);
 fmt.applyPattern("{0,date,dd.MM.yyyy} {1} {2,number,missing}");
 assertNull(fmt.toPattern());
 }

 @Test
 public void testFormatFactory() {
 FormatFactory customFormatFactory = new SimpleFormatFactory() {
 @Override
 public Format getFormat(String pattern, String style, Locale locale) {
 return new CustomFormat(pattern, style, locale);
 }
 };
 ExtendedMessageFormat fmt = new ExtendedMessageFormat(REGISTRY_PATTERN, null, Map.of("customDate", customFormatFactory));
 fmt.applyPattern("{0,date,dd.MM.yyyy} {1,format:customDate} {2,number,number}");
 assertEquals("25 25.05.2023 1", fmt.format(new Object[]{"25", "{0,date,dd.MM.yyyy}", 2023}));
 }

 @Test
 public void testFormatFactoryWithNullRegistry() {
 ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0,date,dd.MM.yyyy} {1,format:customDate} {2,number,number}", null);
 fmt.applyPattern("{0,date,dd.MM.yyyy} {1,format:customDate} {2,number,number}");
 assertNull(fmt.toPattern());
 }

 @Test
 public void testFormatFactoryWithEmptyRegistry() {
 ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0,date,dd.MM.yyyy} {1,format:customDate} {2,number,number}", Map.of());
 fmt.applyPattern("{0,date,dd.MM.yyyy} {1,format:customDate} {2,number,number}");
 assertNull(fmt.toPattern());
 }

 @Test
 public void testFormatFactoryWithInvalidFormatName() {
 ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0,date,dd.MM.yyyy} {1,format:invalidFormat} {2,number,number}", Map.of("invalidFormat", new SimpleFormatFactory()));
 fmt.applyPattern("{0,date,dd.MM.yyyy} {1,format:invalidFormat} {2,number,number}");
 assertNull(fmt.toPattern());
 }
}