import org.apache.commons.lang.text.ExtendedMessageFormat;
import org.apache.commons.lang.text.FormatFactory;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ExtendedMessageFormatTest {

 private static final String DUMMY_PATTERN = "";
 private static final String ESCAPED_QUOTE = "''";
 private static final char START_FMT = ',';
 private static final char END_FE = '}';
 private static final char START_FE_VALID = '{';
 private static final char START_FE_INVALID = '}';
 private static final char QUOTE = '\'';

 @Test
 public void testConstructors() {
 ExtendedMessageFormat fmt1 = new ExtendedMessageFormat("{0}");
 ExtendedMessageFormat fmt2 = new ExtendedMessageFormat("{0}", Locale.US);
 ExtendedMessageFormat fmt3 = new ExtendedMessageFormat("{0}", null, null);
 ExtendedMessageFormat fmt4 = new ExtendedMessageFormat("{0}", Locale.US, null);
 ExtendedMessageFormat fmt5 = new ExtendedMessageFormat("{0}", null, new HashMap<>());
 ExtendedMessageFormat fmt6 = new ExtendedMessageFormat("{0}", Locale.US, new HashMap<>());

 assertEquals("", fmt1.toPattern());
 assertEquals("", fmt2.toPattern());
 assertEquals("", fmt3.toPattern());
 assertEquals("", fmt4.toPattern());
 assertEquals("", fmt5.toPattern());
 assertEquals("", fmt6.toPattern());
 }

 @Test
 public void testApplyPattern() {
 ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0} {1} {2}");
 fmt.applyPattern("{0} {1} {2}");
 assertEquals("{0} {1} {2}", fmt.toPattern());

 String pattern = "{0,number,currency} {1} {2,choice,0#No|1#Yes}";
 fmt.applyPattern(pattern);
 assertEquals(pattern, fmt.toPattern());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testApplyPattern_badPattern() {
 ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0}");
 fmt.applyPattern("{0}"); // no-op
 fmt.applyPattern("{0,invalid}");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetFormat_invalidFormatName() {
 ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0}");
 fmt.applyPattern("{0,formatName=InvalidFormat}");
 assertNull(fmt.getFormatByArgumentIndex(0));
 }

 @Test
 public void testGetFormat_validFormatName() {
 ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0} {1} {2}");
 fmt.applyPattern("{0} {1,formatName=UpperCase} {2}");
 Format format = fmt.getFormatByArgumentIndex(1);
 assertEquals("java.text.Collator", format.getClass().getName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetFormat_missingRegistry() {
 ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0}");
 fmt.applyPattern("{0,formatName=UpperCase}");
 assertNull(fmt.getFormatByArgumentIndex(0));
 }

 @Test
 public void testGetFormat_validRegistry() {
 Map<String, FormatFactory> registry = new HashMap<>();
 registry.put("UpperCase", new FormatFactory() {
 @Override
 public Format getFormat(String name, String argument, Locale locale) {
 return new CollatorRuleBasedCollator(locale);
 }
 });

 ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0} {1} {2}", null, registry);
 fmt.applyPattern("{0} {1,formatName=UpperCase} {2}");
 Format format = fmt.getFormatByArgumentIndex(1);
 assertEquals("java.text.Collator", format.getClass().getName());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetFormat() {
 ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0} {1} {2}");
 fmt.applyPattern("{0} {1} {2}");
 try {
 fmt.setFormat(0, new StringFormat());
 } catch (IllegalArgumentException e) {
 // expected
 }
 try {
 fmt.setFormatByArgumentIndex(0, new StringFormat());
 } catch (IllegalArgumentException e) {
 // expected
 }
 try {
 Format[] formats = new Format[3];
 fmt.setFormats(formats);
 } catch (UnsupportedOperationException e) {
 // expected
 }
 try {
 fmt.setFormatsByArgumentIndex(new Format[3]);
 } catch (UnsupportedOperationException e) {
 // expected
 }
 }

 @Test
 public void testInsertFormats() {
 ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0} {1} {2}");
 fmt.applyPattern("{0} {1} {2}");
 String pattern = "{0} {3,formatName=UpperCase} {2}";
 assertEquals("{0} {1} {2}", fmt.toPattern());
 fmt.applyPattern(pattern);
 assertEquals(pattern, fmt.toPattern());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReadArgumentIndex_invalidArgumentIndex() {
 ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0}");
 fmt.applyPattern("{0}");
 try {
 fmt.readArgumentIndex("{0}", new ParsePosition(0));
 } catch (ParseException e) {
 // expected
 }
 try {
 fmt.readArgumentIndex("{0}", new ParsePosition(5));
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testReadArgumentIndex_validArgumentIndex() {
 ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0} {1} {2}");
 fmt.applyPattern("{0} {1} {2}");
 ParsePosition pos = new ParsePosition(0);
 int index = fmt.readArgumentIndex("{1}", pos);
 assertEquals(1, index);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseFormatDescription_invalidFormatDescription() {
 ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0}");
 fmt.applyPattern("{0}");
 try {
 fmt.parseFormatDescription("{0}", new ParsePosition(0));
 } catch (ParseException e) {
 // expected
 }
 try {
 fmt.parseFormatDescription("{0} {1}", new ParsePosition(0));
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testParseFormatDescription_validFormatDescription() {
 ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0} {1} {2}");
 fmt.applyPattern("{0} {1} {2}");
 ParsePosition pos = new ParsePosition(0);
 String desc = fmt.parseFormatDescription("{2,formatName=UpperCase}", pos);
 assertEquals("UpperCase", desc);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetFormat_unregisteredFormatName() {
 ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0}");
 fmt.applyPattern("{0}");
 try {
 fmt.getFormat("InvalidFormat", null, Locale.getDefault());
 } catch (IllegalArgumentException e) {
 // expected
 }
 }
}