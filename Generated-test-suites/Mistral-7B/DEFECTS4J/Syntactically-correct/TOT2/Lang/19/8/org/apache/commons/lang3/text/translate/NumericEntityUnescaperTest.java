package org.example.junit4;

import org.apache.commons.lang3.text.translate.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

    @Test
    public void testDecimalEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "&amp;#123;";
        String expectedOutput = "{\"\\u007B\", \"\\u0031\", \"\\u0032\", \"\\u0033\"}";
        String output = new String(new char[unescaper.translate(new StringSource(input), 0, new StringWriter()).getBuffer().toString().toCharArray()]);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testHexadecimalEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "&amp;#x1F0D;";
        String expectedOutput = "\"⏏\"";
        String output = new String(new char[unescaper.translate(new StringSource(input), 0, new StringWriter()).getBuffer().toString().toCharArray()]);
        assertEquals(expectedOutput, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInput() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = null;
        unescaper.translate(new StringSource(input), 0, new StringWriter());
    }

    @Test
    public void testEntityAtBeginning() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "&amp;#123;abcd";
        String expectedOutput = "{\"\\u007B\", \"\\u0031\", \"\\u0032\", \"\\u0033\"}abcd";
        String output = new String(new char[unescaper.translate(new StringSource(input), 0, new StringWriter()).getBuffer().toString().toCharArray()]);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testEntityInMiddle() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "abcd&amp;#123;efgh";
        String expectedOutput = "abcd{\"\\u007B\", \"\\u0031\", \"\\u0032\", \"\\u0033\"}efgh";
        String output = new String(new char[unescaper.translate(new StringSource(input), 0, new StringWriter()).getBuffer().toString().toCharArray()]);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testEntityAtEnd() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "abcd&amp;#123;";
        String expectedOutput = "abcd{\"\\u007B\", \"\\u0031\", \"\\u0032\", \"\\u0033\"}";
        String output = new String(new char[unescaper.translate(new StringSource(input), 0, new StringWriter()).getBuffer().toString().toCharArray()]);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testEntityWithSemicolon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "&amp;#123;";
        String expectedOutput = "{\"\\u007B\", \"\\u0031\", \"\\u0032\", \"\\u0033\"}";
        String output = new String(new char[unescaper.translate(new StringSource(input), 0, new StringWriter()).getBuffer().toString().toCharArray()]);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testEntityWithoutSemicolon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "&amp;#123";
        String expectedOutput = "{\"\\u007B\", \"\\u0031\", \"\\u0032\", \"\\u0033\"}";
        String output = new String(new char[unescaper.translate(new StringSource(input), 0, new StringWriter()).getBuffer().toString().toCharArray()]);
        assertEquals(expectedOutput, output);
    }

    @Test(expected = NumberFormatException.class)
    public void testInvalidEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "&amp;#abc;";
        unescaper.translate(new StringSource(input), 0, new StringWriter());
    }

    @Test(expected = IOException.class)
    public void testIOError() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer writer = new StringWriter();
        unescaper.translate(new StringSource(""), 0, writer);
    }

    @Test
    public void testEntityWithMoreThanFourDigits() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "&amp;#123456;";
        String expectedOutput = "{\"\\u007B\", \"\\u0031\", \"\\u0032\", \"\\u0033\", \"\\u0034\", \"\\u0035\", \"\\u0036\"}";
        String output = new String(new char[unescaper.translate(new StringSource(input), 0, new StringWriter()).getBuffer().toString().toCharArray()]);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testEntityWithLeadingWhiteSpace() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = " &amp;#123;";
        String expectedOutput = "{\"\\u0020\", " + "\"\\u007B\", \"\\u0031\", \"\\u0032\", \"\\u0033\"}";
        String output = new String(new char[unescaper.translate(new StringSource(input), 0, new StringWriter()).getBuffer().toString().toCharArray()]);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testEntityWithTrailingWhiteSpace() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "abcd&amp;#123; ";
        String expectedOutput = "abcd{\"\\u007B\", \"\\u0031\", \"\\u0032\", \"\\u0033\"}";
        String output = new String(new char[unescaper.translate(new StringSource(input), 0, new StringWriter()).getBuffer().toString().toCharArray()]);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testEntityWithMoreThanOneCharacter() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "&amp;#x1F000;&#x1F600;";
        String expectedOutput = "\"🅅\"\"🅣\"";
        String output = new String(new char[unescaper.translate(new StringSource(input), 0, new StringWriter()).getBuffer().toString().toCharArray()]);
        assertEquals(expectedOutput, output);
    }
}