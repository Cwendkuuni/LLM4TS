import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper.OPTION;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

public class NumericEntityUnescaperTest {

    @Test
    public void testNumericEntityUnescaperConstructor() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Assert.assertFalse(unescaper.isSet(OPTION.semiColonRequired));
        Assert.assertTrue(unescaper.isSet(OPTION.semiColonRequired.getName().charAt(0) == 's' ? OPTION.semiColonOptional : OPTION.semiColonRequired));

        NumericEntityUnescaper unescaperWithOptions = new NumericEntityUnescaper(OPTION.semiColonRequired, OPTION.errorIfNoSemiColon);
        Assert.assertTrue(unescaperWithOptions.isSet(OPTION.semiColonRequired));
        Assert.assertTrue(unescaperWithOptions.isSet(OPTION.errorIfNoSemiColon));
    }

    @Test
    public void testIsSet() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Assert.assertFalse(unescaper.isSet(OPTION.semiColonRequired));
        NumericEntityUnescaper unescaperWithOptions = new NumericEntityUnescaper(OPTION.semiColonRequired);
        Assert.assertTrue(unescaperWithOptions.isSet(OPTION.semiColonRequired));
    }

    @Test
    public void testTranslate_ValidNumericEntity_SingleCharacter() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter outWriter = new StringWriter();
        char[] expectedOutput = {'&', '#', '1', ';'};
        CharSequence input = "&#1;";
        int length = unescaper.translate(input, 0, outWriter);
        Assert.assertEquals(expectedOutput.length, length);
        Assert.assertEquals(new String(expectedOutput), outWriter.toString());
    }

    @Test
    public void testTranslate_ValidNumericEntity_MultipleCharacters() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter outWriter = new StringWriter();
        char[] expectedOutput = {'&', '#', 'x', '3', 'a', ';'};
        CharSequence input = "&#x3a;";
        int length = unescaper.translate(input, 0, outWriter);
        Assert.assertEquals(expectedOutput.length, length);
        Assert.assertEquals(new String(expectedOutput), outWriter.toString());
    }

    @Test
    public void testTranslate_ValidNumericEntity_HexValueBiggerThan16Bits() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter outWriter = new StringWriter();
        char[] expectedOutput = {'&', '#', 'x', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', ';'};
        CharSequence input = "&#xFFFFF;";
        int length = unescaper.translate(input, 0, outWriter);
        Assert.assertEquals(expectedOutput.length, length);
        Assert.assertEquals(new String(expectedOutput), outWriter.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslate_MissingSemiColon() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter outWriter = new StringWriter();
        CharSequence input = "&#1";
        unescaper.translate(input, 0, outWriter);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslate_InvalidNumericEntity() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter outWriter = new StringWriter();
        CharSequence input = "&#abc;";
        unescaper.translate(input, 0, outWriter);
    }

    @Test
    public void testTranslate_InvalidNumericEntity_EmptyInput() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter outWriter = new StringWriter();
        CharSequence input = "";
        int length = unescaper.translate(input, 0, outWriter);
        Assert.assertEquals(0, length);
        Assert.assertEquals("", outWriter.toString());
    }

    @Test
    public void testTranslate_NumericEntityWithOptionalSemiColon() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional);
        StringWriter outWriter = new StringWriter();
        char[] expectedOutput = {'&', '#', '1'};
        CharSequence input = "&#1";
        int length = unescaper.translate(input, 0, outWriter);
        Assert.assertEquals(expectedOutput.length, length);
        Assert.assertEquals(new String(expectedOutput), outWriter.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslate_NumericEntityWithErrorIfNoSemiColon() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.errorIfNoSemiColon);
        StringWriter outWriter = new StringWriter();
        CharSequence input = "&#1";
        unescaper.translate(input, 0, outWriter);
    }
}