import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper.OPTION;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.EnumSet;

public class NumericEntityUnescaperTest {

    @Test
    public void testNumericEntityUnescaperConstructorWithOptions() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired, OPTION.errorIfNoSemiColon);
        Assert.assertTrue(unescaper.isSet(OPTION.semiColonRequired));
        Assert.assertTrue(unescaper.isSet(OPTION.errorIfNoSemiColon));
    }

    @Test
    public void testNumericEntityUnescaperConstructorWithoutOptions() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Assert.assertFalse(unescaper.isSet(OPTION.semiColonRequired));
        Assert.assertFalse(unescaper.isSet(OPTION.errorIfNoSemiColon));
    }

    @Test
    public void testIsSetMethod() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired, OPTION.errorIfNoSemiColon);
        Assert.assertTrue(unescaper.isSet(OPTION.semiColonRequired));
        Assert.assertTrue(unescaper.isSet(OPTION.errorIfNoSemiColon));
        Assert.assertFalse(unescaper.isSet(OPTION.semiColonOptional));
    }

    @Test
    public void testTranslateMethod_SingleNumericEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "&amp;#160;";
        StringWriter out = new StringWriter();
        int length = unescaper.translate(new StringReader(input), 0, out);
        Assert.assertEquals(" ", out.toString());
        Assert.assertEquals(1, length);
    }

    @Test
    public void testTranslateMethod_MultipleNumericEntities() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "Hello &amp;#160;World &amp;#161; &amp;#x41;Hello";
        StringWriter out = new StringWriter();
        int length = unescaper.translate(new StringReader(input), 0, out);
        Assert.assertEquals("Hello   World  A        Hello", out.toString());
        Assert.assertEquals(13, length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateMethod_MissingSemiColon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.errorIfNoSemiColon);
        String input = "Hello &amp;#160;World &amp;#161;";
        StringWriter out = new StringWriter();
        int length = unescaper.translate(new StringReader(input), 0, out);
        Assert.assertEquals("Hello   World  A", out.toString());
        Assert.assertEquals(10, length);
    }

    @Test
    public void testTranslateMethod_InvalidHexEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "Hello &amp;#xx;World &amp;#161;";
        StringWriter out = new StringWriter();
        int length = unescaper.translate(new StringReader(input), 0, out);
        Assert.assertEquals("Hello &amp;#xx;World A", out.toString());
        Assert.assertEquals(11, length);
    }

    @Test
    public void testTranslateMethod_ValidHexEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "Hello &amp;#xFFFF;World &amp;#161;";
        StringWriter out = new StringWriter();
        int length = unescaper.translate(new StringReader(input), 0, out);
        Assert.assertEquals("Hello World A", out.toString());
        Assert.assertEquals(13, length);
    }

    @Test
    public void testTranslateMethod_EmptyInput() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();
        int length = unescaper.translate(new StringReader(""), 0, out);
        Assert.assertEquals("", out.toString());
        Assert.assertEquals(0, length);
    }

    @Test
    public void testTranslateMethod_LargeNumericEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "Hello &amp;#1111111111;World &amp;#161;";
        StringWriter out = new StringWriter();
        int length = unescaper.translate(new StringReader(input), 0, out);
        Assert.assertEquals("Hello １１１１１１１１１World A", out.toString());
        Assert.assertEquals(21, length);
    }

    @Test
    public void testTranslateMethod_EmptyEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "Hello &amp;##;World &amp;#161;";
        StringWriter out = new StringWriter();
        int length = unescaper.translate(new StringReader(input), 0, out);
        Assert.assertEquals("Hello &amp;##;World A", out.toString());
        Assert.assertEquals(12, length);
    }
}