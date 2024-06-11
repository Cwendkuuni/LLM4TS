import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper.OPTION;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.io.*;
import java.util.EnumSet;
import java.util.EnumSet;
import java.util.Properties;
import java.util.StringJoiner;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_DefaultOptions() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder output = new StringBuilder();
        String input = "Hello &amp;#160; World";
        int index = 0;
        unescaper.translate(new StringReader(input), index, new StringWriter(output));
        assertEquals("Hello &   World", input.substring(0, index) + output.toString());
    }

    @Test
    public void testTranslate_WithSemiColonRequiredOption() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
        StringBuilder output = new StringBuilder();
        String input = "Hello &amp;#160; World";
        int index = 0;
        unescaper.translate(new StringReader(input), index, new StringWriter(output));
        assertEquals("Hello &   World;", input.substring(0, index) + output.toString());
    }

    @Test
    public void testTranslate_WithSemiColonOptionalOption() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional);
        StringBuilder output = new StringBuilder();
        String input = "Hello &amp;#160; World";
        int index = 0;
        unescaper.translate(new StringReader(input), index, new StringWriter(output));
        assertEquals("Hello &   World", input.substring(0, index) + output.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslate_WithErrorIfNoSemiColonOption() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.errorIfNoSemiColon);
        StringBuilder output = new StringBuilder();
        String input = "Hello &amp;#160; World";
        int index = 0;
        unescaper.translate(new StringReader(input), index, new StringWriter(output));
    }

    @Test
    public void testTranslate_InvalidNumericEntity() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder output = new StringBuilder();
        String input = "Hello &amp;#xxx World";
        int index = 0;
        unescaper.translate(new StringReader(input), index, new StringWriter(output));
        assertEquals("Hello &amp;#xxx World", input.substring(0, index) + output.toString());
    }

    @Test
    public void testTranslate_EmptyInput() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder output = new StringBuilder();
        String input = "";
        int index = 0;
        unescaper.translate(new StringReader(input), index, new StringWriter(output));
        assertEquals("", input.substring(0, index) + output.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslate_NullInput() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder output = new StringBuilder();
        String input = null;
        int index = 0;
        unescaper.translate(new StringReader(input), index, new StringWriter(output));
    }

    @Test(expected = java.text.ParseException.class)
    public void testTranslate_InvalidCharSequence() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder output = new StringBuilder();
        String input = "Hello &amp;#";
        int index = 0;
        unescaper.translate(new StringReader(input), index, new StringWriter(output));
    }

    @Test
    public void testTranslate_DifferentCharactersBeforeAndAfterNumericEntity() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder output = new StringBuilder();
        String input = "Hello &amp;#160! World";
        int index = 0;
        unescaper.translate(new StringReader(input), index, new StringWriter(output));
        assertEquals("Hello &  ! World", input.substring(0, index) + output.toString());
    }

    @Test
    public void testTranslate_NumericEntityWithSemiColon() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder output = new StringBuilder();
        String input = "Hello &amp;#160; World";
        int index = 0;
        unescaper.translate(new StringReader(input), index, new StringWriter(output));
        assertEquals("Hello &   World;", input.substring(0, index) + output.toString());
    }

    @Test
    public void testTranslate_NumericEntityWithoutSemiColon() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder output = new StringBuilder();
        String input = "Hello &amp;#160 World";
        int index = 0;
        unescaper.translate(new StringReader(input), index, new StringWriter(output));
        assertEquals("Hello &   World", input.substring(0, index) + output.toString());
    }
}