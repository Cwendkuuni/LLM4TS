package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.junit.Assert;

import java.io.StringWriter;
import java.io.Writer;

/**
 * Class NumericEntityUnescaperTest is a JUnit 4 test class for the NumericEntityUnescaper class.
 */
public class NumericEntityUnescaperTest {

    private Writer out;
    private StringWriter stringWriter;

    @Before
    public void setUp() {
        out = new StringWriter();
        stringWriter = new StringWriter();
        this.stringWriter = stringWriter;
    }

    @After
    public void tearDown() {
        out = null;
        stringWriter = null;
    }

    /**
     * Test method for {@link NumericEntityUnescaper#translate(CharSequence, int, Writer)}.
     * Testing with valid numeric entities with semi-colon.
     */
    @Test
    public void testTranslateWithValidNumericEntitiesAndSemicolon() {

        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String testString = "&amp;#160;&#128;&#x2122;&#12345; &#xFF01;&#xFFFC;";

        int lengthBefore = this.stringWriter.getBuffer().length();
        unescaper.translate(StringUtils.subString(testString, 0, testString.length()), 0, this.out);
        String result = this.out.toString();
        int lengthAfter = this.stringWriter.getBuffer().length();

        char[] expectedChars = ArrayUtils.addAll(Character.toChars(0x00A0), Character.toChars(0x0080),
                Character.toChars(0x201C), Character.toChars(0x2302), Character.toChars(0x2321),
                Character.toChars(0xFF01), Character.toChars(0xFFFC));

        Assert.assertArrayEquals(expectedChars, StringUtils.toCharArray(this.stringWriter.toString().substring(lengthBefore, lengthAfter + result.length())));
    }

    /**
     * Test method for {@link NumericEntityUnescaper#translate(CharSequence, int, Writer)}.
     * Testing with valid numeric entities without semicolon.
     */
    @Test
    public void testTranslateWithValidNumericEntitiesWithoutSemicolon() {

        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String testString = "&amp;#160;&#128;&#x2122;&#12345; &#xFF01,&#xFFFC;";

        int lengthBefore = this.stringWriter.getBuffer().length();
        unescaper.translate(StringUtils.subString(testString, 0, testString.length()), 0, this.out);
        String result = this.out.toString();
        int lengthAfter = this.stringWriter.getBuffer().length();

        char[] expectedChars = ArrayUtils.addAll(Character.toChars(0x00A0), Character.toChars(0x0080),
                Character.toChars(0x201C), Character.toChars(0x2302), Character.toChars(0x2321),
                Character.toChars(0xFF01), Character.toChars(0xFFFC));

        Assert.assertArrayEquals(expectedChars, StringUtils.toCharArray(this.stringWriter.toString().substring(lengthBefore, lengthAfter + result.length())));
    }

    /**
     * Test method for {@link NumericEntityUnescaper#translate(CharSequence, int, Writer)}.
     * Testing invalid numeric entity.
     */
    @Test
    public void testTranslateWithInvalidNumericEntity() {

        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String testString = "&amp;#InvalidEntity;";

        int lengthBefore = this.stringWriter.getBuffer().length();
        unescaper.translate(StringUtils.subString(testString, 0, testString.length()), 0, this.out);
        String result = this.out.toString();
        int lengthAfter = this.stringWriter.getBuffer().length();

        Assert.assertEquals("", result);
    }

    /**
     * Test method for {@link NumericEntityUnescaper#translate(CharSequence, int, Writer)}.
     * Testing empty input.
     */
    @Test
    public void testTranslateWithEmptyInput() {

        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        int lengthBefore = this.stringWriter.getBuffer().length();
        unescaper.translate(StringUtils.EMPTY, 0, this.out);
        String result = this.out.toString();
        int lengthAfter = this.stringWriter.getBuffer().length();

        Assert.assertEquals("", result);
    }
}