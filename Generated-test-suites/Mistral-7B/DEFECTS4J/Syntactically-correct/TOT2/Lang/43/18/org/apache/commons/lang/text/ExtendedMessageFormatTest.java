import org.apache.commons.lang.text.ExtendedMessageFormat;
import org.apache.commons.lang.text.FormatFactory;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExtendedMessageFormatTest {

 // Test case for constructor with empty pattern
 @Test
 public void testEmptyPattern() {
 ExtendedMessageFormat forma = new ExtendedMessageFormat("");
 assertEquals("", forma.toString());
 }

 // Test case for constructor with pattern and default locale
 @Test
 public void testPatternAndDefaultLocale() {
 ExtendedMessageFormat forma = new ExtendedMessageFormat("{0} {1}", Locale.getDefault());
 assertEquals("{0} {1}", forma.toPattern());
 }

 // Test case for constructor with pattern, locale, and registry
 @Test
 public void testPatternLocaleAndRegistry() {
 FormatFactory registry = new FormatFactory() {
 @Override
 public Format getFormat(String formatName, String formatStyle, Locale locale) {
 return new TestFormat(formatName, formatStyle, locale);
 }
 };
 ExtendedMessageFormat forma = new ExtendedMessageFormat("{0} {1}", Locale.getDefault(), registry);
 assertEquals("{0} {1}", forma.toPattern());
 }

 // Test case for applyPattern method with empty pattern
 @Test(expected = IllegalArgumentException.class)
 public void testApplyPatternEmptyPattern() {
 ExtendedMessageFormat forma = new ExtendedMessageFormat("");
 forma.applyPattern("");
 }

 // Test case for applyPattern method with pattern containing quote
 @Test
 public void testApplyPatternWithQuote() {
 ExtendedMessageFormat forma = new ExtendedMessageFormat("{0}'{1}'");
 forma.applyPattern("{0}'{1}'");
 assertEquals("{0}'{1}'", forma.toPattern());
 }

 // Test case for applyPattern method with pattern containing format element
 @Test
 public void testApplyPatternWithFormatElement() {
 ExtendedMessageFormat forma = new ExtendedMessageFormat("{0} {1}");
 forma.applyPattern("{0} {1}");
 assertEquals("{0} {1}", forma.toPattern());
 }

 // Test case for applyPattern method with pattern containing format element and format name
 @Test
 public void testApplyPatternWithFormatElementAndFormatName() {
 FormatFactory registry = new FormatFactory() {
 @Override
 public Format getFormat(String formatName, String formatStyle, Locale locale) {
 return new TestFormat(formatName, formatStyle, locale);
 }
 };
 ExtendedMessageFormat forma = new ExtendedMessageFormat("{0} {1}", null, registry);
 forma.applyPattern("{0,date,MM/dd/yyyy} {1,choice,0#Hello|1#Goodbye}");
 assertEquals("{0,date,MM/dd/yyyy} {1,choice,0#Hello|1#Goodbye}", forma.toPattern());
 }

 // Test case for applyPattern method with pattern containing format element and format name and format style
 @Test
 public void testApplyPatternWithFormatElementAndFormatNameAndFormatStyle() {
 FormatFactory registry = new FormatFactory() {
 @Override
 public Format getFormat(String formatName, String formatStyle, Locale locale) {
 return new TestFormat(formatName, formatStyle, locale);
 }
 };
 ExtendedMessageFormat forma = new ExtendedMessageFormat("{0} {1}", null, registry);
 forma.applyPattern("{0,date,full,MM/dd/yyyy} {1,choice,0#Hello|1#Goodbye}");
 assertEquals("{0,date,full,MM/dd/yyyy} {1,choice,0#Hello|1#Goodbye}", forma.toPattern());
 }

 // Test case for setFormat method
 @Test(expected = UnsupportedOperationException.class)
 public void testSetFormat() {
 ExtendedMessageFormat forma = new ExtendedMessageFormat("{0} {1}");
 forma.setFormat(0, new TestFormat("date", "full", Locale.getDefault()));
 }

 // Test case for setFormatByArgumentIndex method
 @Test(expected = UnsupportedOperationException.class)
 public void testSetFormatByArgumentIndex() {
 ExtendedMessageFormat forma = new ExtendedMessageFormat("{0} {1}");
 forma.setFormatByArgumentIndex(1, new TestFormat("choice", null, Locale.getDefault()));
 }

 // Test case for setFormats method
 @Test(expected = UnsupportedOperationException.class)
 public void testSetFormats() {
 ExtendedMessageFormat forma = new ExtendedMessageFormat("{0} {1}");
 Format[] formats = {new TestFormat("date", "full", Locale.getDefault()), new TestFormat("choice", null, Locale.getDefault())};
 forma.setFormats(formats);
 }

 // Test case for setFormatsByArgumentIndex method
 @Test(expected = UnsupportedOperationException.class)
 public void testSetFormatsByArgumentIndex() {
 ExtendedMessageFormat forma = new ExtendedMessageFormat("{0} {1}");
 Format[] formats = {new TestFormat("date", "full", Locale.getDefault()), new TestFormat("choice", null, Locale.getDefault())};
 forma.setFormatsByArgumentIndex(new Format[] {new TestFormat("date", "full", Locale.getDefault()), new TestFormat("choice", null, Locale.getDefault())});
 }

}

class TestFormat implements Format {
 private String formatName;
 private String formatStyle;
 private Locale locale;

 TestFormat(String formatName, String formatStyle, Locale locale) {
 this.formatName = formatName;
 this.formatStyle = formatStyle;
 this.locale = locale;
 }

 @Override
 public String format(Object obj, Locale locale, String format, ParsePosition parsePosition) {
 throw new UnsupportedOperationException();
 }

 @Override
 public Object parseObject(String source, ParsePosition parsePosition) {
 throw new UnsupportedOperationException();
 }

 @Override
 public String toString() {
 return formatName + (formatStyle == null ? "" : "_" + formatStyle);
 }
}