package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;

import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.FractionFormat;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.exception.MathParseException;
import org.apache.commons.math3.exception.NumberFormatException;
import org.apache.commons.math3.exception.NullArgumentException;

public class ProperFractionFormatTest {

    @Test
    public void testProperFractionFormatDefaultConstructor() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        FractionFormat defaultFormat = FractionFormat.getDefaultFormat();
        assertEquals(defaultFormat, properFractionFormat.getNumeratorFormat());
        assertEquals(defaultFormat, properFractionFormat.getDenominatorFormat());
        assertNull(properFractionFormat.getWholeFormat());
    }

    @Test
    public void testProperFractionFormatCustomConstructor() {
        NumberFormat wholeFormat = NumberFormat.getNumberInstance();
        ProperFractionFormat properFractionFormat = new ProperFractionFormat(wholeFormat);
        assertEquals(wholeFormat, properFractionFormat.getWholeFormat());
        FractionFormat numeratorFormat = FractionFormat.getDefaultFormat();
        assertEquals(numeratorFormat, properFractionFormat.getNumeratorFormat());
        assertEquals(numeratorFormat, properFractionFormat.getDenominatorFormat());
    }

    @Test
    public void testProperFractionFormatCustomConstructors() {
        NumberFormat wholeFormat = NumberFormat.getNumberInstance();
        NumberFormat numeratorFormat = NumberFormat.getNumberInstance();
        NumberFormat denominatorFormat = NumberFormat.getNumberInstance();
        ProperFractionFormat properFractionFormat = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
        assertEquals(wholeFormat, properFractionFormat.getWholeFormat());
        assertEquals(numeratorFormat, properFractionFormat.getNumeratorFormat());
        assertEquals(denominatorFormat, properFractionFormat.getDenominatorFormat());
    }

    @Test
    public void testFormatMethod() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        Fraction fraction = new Fraction(2, 3);
        StringBuffer toAppendTo = new StringBuffer();
        FieldPosition pos = new FieldPosition(0, 0);
        
        properFractionFormat.format(fraction, toAppendTo, pos);
        
        assertEquals("2/3", toAppendTo.toString());
    }

    @Test
    public void testFormatWithWholePart() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        Fraction fraction = new Fraction(5, 3);
        StringBuffer toAppendTo = new StringBuffer();
        FieldPosition pos = new FieldPosition(0, 0);
        
        properFractionFormat.format(fraction, toAppendTo, pos);
        
        assertEquals("5 2/3", toAppendTo.toString());
    }

    @Test
    public void testParseMethod() throws MathParseException, NumberFormatException, NullArgumentException {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        ProperFractionFormat customFormat = new ProperFractionFormat();
        customFormat.setWholeFormat(NumberFormat.getNumberInstance());
        
        Fraction fraction = properFractionFormat.parse("2/3", new ParsePosition(0));
        assertEquals("2/3", properFractionFormat.format(fraction, new StringBuffer(), null));
        
        fraction = customFormat.parse("5 2/3", new ParsePosition(0));
        assertEquals("5 2/3", customFormat.format(fraction, new StringBuffer(), null));
    }

    @Test
    public void testParseWithWholePart() throws MathParseException, NumberFormatException, NullArgumentException {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        ProperFractionFormat customFormat = new ProperFractionFormat();
        customFormat.setWholeFormat(NumberFormat.getNumberInstance());
        
        Fraction fraction = properFractionFormat.parse("5 2/3", new ParsePosition(0));
        assertEquals("5 2/3", properFractionFormat.format(fraction, new StringBuffer(), null));
        
        fraction = customFormat.parse("5 2/3", new ParsePosition(0));
        assertEquals("5 2/3", customFormat.format(fraction, new StringBuffer(), null));
    }

    @Test
    public void testParseWithInvalidInput() throws MathParseException, NumberFormatException, NullArgumentException {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        Fraction fraction = null;
        
        try {
            fraction = properFractionFormat.parse("InvalidInput", new ParsePosition(0));
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
        
        try {
            fraction = properFractionFormat.parse("2/0", new ParsePosition(0));
            fail("Expected MathParseException");
        } catch (MathParseException e) {
            // Expected
        }
        
        try {
            fraction = properFractionFormat.parse("-2/3", new ParsePosition(0));
            fail("Expected MathParseException");
        } catch (MathParseException e) {
            // Expected
        }
    }

    @Test
    public void testParseWithInvalidInputWithWholePart() throws MathParseException, NumberFormatException, NullArgumentException {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        ProperFractionFormat customFormat = new ProperFractionFormat();
        customFormat.setWholeFormat(NumberFormat.getNumberInstance());
        
        Fraction fraction = null;
        
        try {
            fraction = properFractionFormat.parse("InvalidInput", new ParsePosition(0));
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
        
        try {
            fraction = properFractionFormat.parse("5 -2/3", new ParsePosition(0));
            fail("Expected MathParseException");
        } catch (MathParseException e) {
            // Expected
        }
        
        try {
            fraction = properFractionFormat.parse("5 2/0", new ParsePosition(0));
            fail("Expected MathParseException");
        } catch (MathParseException e) {
            // Expected
        }

        try {
            fraction = customFormat.parse("5 -2/3", new ParsePosition(0));
            fail("Expected MathParseException");
        } catch (MathParseException e) {
            // Expected
        }

        try {
            fraction = customFormat.parse("5 2/0", new ParsePosition(0));
            fail("Expected MathParseException");
        } catch (MathParseException e) {
            // Expected
        }
    }

    @Test
    public void testSetWholeFormat() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        NumberFormat wholeFormat = NumberFormat.getNumberInstance();
        
        properFractionFormat.setWholeFormat(wholeFormat);
        assertEquals(wholeFormat, properFractionFormat.getWholeFormat());
    }

    @Test(expected = NullArgumentException.class)
    public void testSetWholeFormatWithNullInput() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        
        properFractionFormat.setWholeFormat(null);
        fail("Expected NullArgumentException");
    }
}