import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NumericEntityUnescaperTest {

 @Test
 public void testTranslateBasic() throws IOException {
 String input = "&#[x1F600];";
 CharSequence charSequenceInput = new StringCharSequence(input);
 Writer writer = new StringWriter();
 int actual = new NumericEntityUnescaper().translate(charSequenceInput, 0, writer);
 String expected = "\uD83C\uDFF00";
 assertEquals(expected, writer.toString());
 assertEquals(actual, 4);
 }

 @Test(expected = IOException.class)
 public void testTranslateInvalidEntityStart() throws IOException {
 String input = "&%#";
 CharSequence charSequenceInput = new StringCharSequence(input);
 Writer writer = new StringWriter();
 new NumericEntityUnescaper().translate(charSequenceInput, 0, writer);
 }

 @Test(expected = IOException.class)
 public void testTranslateInvalidEntityEnd() throws IOException {
 String input = "&%#x";
 CharSequence charSequenceInput = new StringCharSequence(input);
 Writer writer = new StringWriter();
 new NumericEntityUnescaper().translate(charSequenceInput, 0, writer);
 }

 @Test(expected = IOException.class)
 public void testTranslateEntityWithinEntity() throws IOException {
 String input = "&%#xAB&%#xCD;";
 CharSequence charSequenceInput = new StringCharSequence(input);
 Writer writer = new StringWriter();
 new NumericEntityUnescaper().translate(charSequenceInput, 0, writer);
 }

 @Test(expected = IOException.class)
 public void testTranslateNonNumericEntity() throws IOException {
 String input = "&%#xABcd;";
 CharSequence charSequenceInput = new StringCharSequence(input);
 Writer writer = new StringWriter();
 new NumericEntityUnescaper().translate(charSequenceInput, 0, writer);
 }

 @Test
 public void testTranslateEmptyInput() throws IOException {
 String input = "";
 CharSequence charSequenceInput = new StringCharSequence(input);
 Writer writer = new StringWriter();
 new NumericEntityUnescaper().translate(charSequenceInput, 0, writer);
 assertEquals("", writer.toString());
 assertEquals(0, writer.getBuffer().length());
 }

 @Test
 public void testTranslateNullInput() throws IOException {
 CharSequence charSequenceInput = null;
 Writer writer = new StringWriter();
 try {
 new NumericEntityUnescaper().translate(charSequenceInput, 0, writer);
 fail("Expected IOException");
 } catch (IOException e) {
 // expected
 }
 }

}