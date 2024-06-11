import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NumericEntityUnescaperTest {

 @Test
 public void testTranslateNumericEntityWithDecimalFormat() throws Exception {
 CharSequence input = "&amp;#10; &#160; &#32; &#128; &#183; &#8211; &#8221; &#8230; &#9605; &#9679; &#9658; &#8482;";
 Writer outputWriter = new Writer() {
 @Override
 public void write(int c) throws IOException {
 char ch = (char) c;
 System.out.print(ch);
 }

 @Override
 public void flush() throws IOException {
 // empty
 }

 @Override
 public void close() throws IOException {
 // empty
 }
 };

 NumericEntityUnescaper translator = new NumericEntityUnescaper();
 int index = 0;
 int length = input.length();

 while (index < length) {
 int charsTranslated = translator.translate(input, index, outputWriter);
 index += charsTranslated;
 }

 assertEquals("€−“ ∫±−⋅∞", outputWriter.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTranslateNonNumericEntity() throws Exception {
 CharSequence input = "&amp;abcd;";
 Writer outputWriter = new Writer() {
 // empty
 };

 NumericEntityUnescaper translator = new NumericEntityUnescaper();
 int index = 0;
 int length = input.length();

 while (index < length) {
 translator.translate(input, index, outputWriter);
 index++;
 }
 }

 @Test
 public void testTranslateNumericEntityWithHexFormat() throws Exception {
 CharSequence input = "&amp;#x40; &#x201c; &#x201d; &#x2212; &#x221E; &#x2325; &#x2326; &#x2327; &#x2605; &#x260E; &#x2618; &#x266F; &#x2718;";
 Writer outputWriter = new Writer() {
 // empty
 };

 NumericEntityUnescaper translator = new NumericEntityUnescaper();
 int index = 0;
 int length = input.length();

 while (index < length) {
 int charsTranslated = translator.translate(input, index, outputWriter);
 index += charsTranslated;
 }

 assertEquals("€““ ∫±−⋅∞", outputWriter.toString());
 }
}