import org.apache.commons.lang.text.ExtendedMessageFormat;
import org.apache.commons.lang.text.FormatFactory;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class ExtendedMessageFormatTest {

 @Test
 public void testBasicUsage() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}, your age is {1}.", new HashMap<>());
 format.applyPattern("Hello Matt, your age is 30.");
 Object[] args = new Object[]{"Matt", 30};
 String result = format.format(args);
 assertEquals("Hello Matt, your age is 30.", result);
 }

 @Test
 public void testCustomFormat() {
 Format customFormat = new MyFormat();
 FormatFactory formatFactory = new FormatFactory() {
 @Override
 public Format getFormat(String formatName, String formatStyle, Locale locale) {
 if ("myCustomFormat".equals(formatName)) {
 return customFormat;
 }
 return null;
 }
 };
 ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {formatElement1}, your {formatElement2} is {formatElement3}.", new HashMap<>(), formatFactory);
 format.applyPattern("Hello {myCustomFormat:foo,STANDARD}, your {formatElement2} is {1}.");
 format.setFormatByArgumentIndex(0, formatFactory.getFormat("myCustomFormat", null, Locale.getDefault()));
 Object[] args = new Object[]{"Bar", "age"};
 String result = format.format(args);
 assertEquals("Hello Bar, your age is Bar.", result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidFormatArgumentIndex() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}, your age is {-1}.", new HashMap<>());
 format.applyPattern("Hello Matt, your age is 30.");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUnterminatedFormatElement() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0, your age is {1}", new HashMap<>());
 format.applyPattern("Hello Matt, your age is 30.");
 }

 @Test
 public void testEmptyStringFormat() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("", new HashMap<>());
 Object[] args = new Object[]{"Matt"};
 String result = format.format(args);
 assertEquals("", result);
 }

 @Test
 public void testNullFormatRegistry() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("", null);
 Object[] args = new Object[]{"Matt"};
 String result = format.format(args);
 assertEquals("", result);
 }
}

class MyFormat implements Format {
 @Override
 public String format(Object obj, Locale locale) {
 return obj.toString();
 }

 @Override
 public Object parseObject(String source, ParsePosition pos) {
 return null;
 }
}