import org.apache.commons.lang3.text.ExtendedMessageFormat;
import org.apache.commons.lang3.text.FormatFactory;
import org.apache.commons.lang3.text.MessageFormat;
import org.apache.commons.lang3.text.SimpleDateFormat;
import org.apache.commons.lang3.Validate;
import org.junit.Assert;
import org.junit.Test;
import java.text.ParseException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ExtendedMessageFormatTest {

 private static final String PATTERN = "{0,date,dd-MM-yyyy} {1} {2,time,hh:mm a}";
 private static final Map<String, FormatFactory> REGISTRY = new HashMap<>();
 static {
 REGISTRY.put("date", (format, args, locale) -> new SimpleDateFormat(args[0], locale));
 REGISTRY.put("time", (format, args, locale) -> new SimpleDateFormat(args[0], locale));
 }

 @Test
 public void testExtendedMessageFormat() {
 ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(PATTERN, REGISTRY);
 Object[] args = new Object[]{"12-12-2012", "Hello", "12:01 PM"};
 Object result = messageFormat.format(args);
 Validate.isInstanceOf(String.class, result);
 Assert.assertEquals("12-12-2012 Hello 12:01 PM", result.toString());

 messageFormat = new ExtendedMessageFormat(PATTERN, REGISTRY);
 args = new Object[]{new Date(), "Goodbye", new Date()};
 result = messageFormat.format(args);
 Validate.isInstanceOf(String.class, result);
 Assert.assertEquals("12-12-2012 Goodbye 12:01 PM 12-12-2012", result.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidFormatPattern() {
 ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("{0} {1} {2} {3} {5}", REGISTRY);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidIndexArgument() {
 ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(PATTERN, REGISTRY);
 messageFormat.setFormat(5, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidFormatDescription() {
 ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(PATTERN, REGISTRY);
 messageFormat.applyPattern("{0} {1,date,dd-MM-yyyy} {2,time,hh:mm a}");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUnmatchedBraces() {
 ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(PATTERN, REGISTRY);
 messageFormat.applyPattern("{0} {1} {2} {3} {5} {6}");
 }

 @Test
 public void testCustomFormat() {
 ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("{0} {1,date[mm.dd.yyyy]} {2,time[HH:mm:ss z]}", REGISTRY);
 messageFormat.applyPattern(PATTERN);
 Object[] args = new Object[]{"12-12-2012", "Hello", "12:01 PM"};
 Object result = messageFormat.format(args);
 Validate.isInstanceOf(String.class, result);
 Assert.assertEquals("12-12-2012 Hello 12:01 PM", result.toString());
 }

 @Test(expected = ParseException.class)
 public void testInvalidDateFormat() {
 ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("{0,date,dd-MM-yyyy} {1} {2,time,hh:mm a}", REGISTRY);
 messageFormat.applyPattern(PATTERN);
 Object[] args = new Object[]{"12-12-2012", "Hello", "12:01 PM", "invalid date"};
 messageFormat.format(args);
 }

 @Test(expected = RuntimeException.class)
 public void testUnsupportedOperationException() {
 ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(PATTERN, REGISTRY);
 messageFormat.setFormat(0, null);
 }

 @Test(expected = RuntimeException.class)
 public void testUnsupportedOperationExceptionSetFormats() {
 ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(PATTERN, REGISTRY);
 Format[] newFormats = new Format[0];
 messageFormat.setFormats(newFormats);
 }
}