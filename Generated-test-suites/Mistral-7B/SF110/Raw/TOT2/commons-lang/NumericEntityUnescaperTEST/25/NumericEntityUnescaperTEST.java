package org.apache.commons.lang3.text.translate;

import static org.apache.commons.lang3.ArrayUtils.toObject;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.stream.Collectors;

import org.junit.jupiter.api.*;

public class NumericEntityUnescaperTest {
    

    @Test
    public void testNumericEntityUnescaperConstructor() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        EnumSet<NumericEntityUnescaper.OPTION> expectedOptions = EnumSet.copyOf(NumericEntityUnescaper.OPTION.values());
        assertEquals(expectedOptions, unescaper.options);

        NumericEntityUnescaper unescaperWithOptions = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        EnumSet<NumericEntityUnescaper.OPTION> expectedOptionsWithSemicolon = EnumSet.copyOf(NumericEntityUnescaper.OPTION.values());
        expectedOptionsWithSemicolon.add(NumericEntityUnescaper.OPTION.semiColonRequired);
        assertEquals(expectedOptionsWithSemicolon, unescaperWithOptions.options);
    }

    // Test remaining methods and edge cases here

    @Test
    public void testTranslateMethodHappyPath() throws IOException {
        String input = "This is a &amp;#160;test&amp;#160;.";
        StringWriter outWriter = new StringWriter();
        CharSequence charSequence = new StringCharSequence(input);

        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        int length = unescaper.translate(charSequence, 0, outWriter);

        assertEquals("This is a " + (char) 160 + "test" + (char) 160 + ".", outWriter.toString());
        assertEquals(input.length() + 2, length);
    }

    // Test remaining methods and edge cases here

    
}