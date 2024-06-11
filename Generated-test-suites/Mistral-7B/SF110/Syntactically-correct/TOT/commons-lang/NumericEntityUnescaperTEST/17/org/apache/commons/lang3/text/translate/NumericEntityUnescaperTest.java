import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper.OPTION;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.EnumSet;

public class NumericEntityUnescaperTest {

 @Test
 public void testTranslate_SimpleNumericEntity() throws Exception {
 String input = "&amp;#123;";
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 StringWriter out = new StringWriter();
 int length = unescaper.translate(new StringReader(input), 0, out);
 assertEquals("abc", out.toString().substring(0, length - 1));
 }

 @Test
 public void testTranslate_HexNumericEntity() throws Exception {
 String input = "&amp;#x4141;";
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 StringWriter out = new StringWriter();
 int length = unescaper.translate(new StringReader(input), 0, out);
 assertEquals("AA", out.toString().substring(0, length - 1));
 }

 @Test
 public void testTranslate_LongNumericEntity() throws Exception {
 String input = "&amp;#65534;";
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 StringWriter out = new StringWriter();
 int length = unescaper.translate(new StringReader(input), 0, out);
 assertEquals("\uD800", out.toString().substring(0, length - 1));
 }

 @Test
 public void testTranslate_InvalidNumericEntity() throws Exception {
 String input = "&amp;#invalid;";
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 StringWriter out = new StringWriter();
 int length = unescaper.translate(new StringReader(input), 0, out);
 assertEquals("", out.toString());
 }

 @Test
 public void testTranslate_MissingSemiColon() throws Exception {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
 String input = "&amp;#123;";
 StringWriter out = new StringWriter();
 int length = unescaper.translate(new StringReader(input), 0, out);
 assertThrows(IllegalArgumentException.class, () -> unescaper.translate(new StringReader(input), 0, out));
 }

 @Test
 public void testTranslate_ExtraSemiColon() throws Exception {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional);
 String input = "&amp;#123;";
 StringWriter out = new StringWriter();
 int length = unescaper.translate(new StringReader(input), 0, out);
 assertEquals("abc", out.toString().substring(0, length));

 String inputWithSemiColon = "&amp;#123;";
 StringWriter outWithSemiColon = new StringWriter();
 int lengthWithSemiColon = unescaper.translate(new StringReader(inputWithSemiColon), 0, outWithSemiColon);
 assertEquals("abc;", outWithSemiColon.toString().substring(0, lengthWithSemiColon));
 }
}