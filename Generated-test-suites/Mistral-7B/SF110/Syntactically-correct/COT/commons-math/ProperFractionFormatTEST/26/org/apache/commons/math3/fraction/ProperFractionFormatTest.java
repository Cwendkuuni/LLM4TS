package org.apache.commons.math3.fraction;

import static org.junit.Assert.*;

import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.NumberFormatUtils;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.exception.NumberFormatException;
import org.apache.commons.math3.exception.NullArgumentException;

import java.text.NumberFormat;
import java.util.Locale;

public class ProperFractionFormatTest {

    private NumberFormat defaultNumberFormat = NumberFormatUtils.getNumberInstance();
    private NumberFormat integerFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
    private NumberFormat percentFormat = NumberFormat.getPercentInstance(Locale.ENGLISH);

    @org.junit.Before
    public void setUp() {
        ProperFractionFormat pff = new ProperFractionFormat();
        pff.setWholeFormat(integerFormat);
        pff.setNumeratorFormat(integerFormat);
        pff.setDenominatorFormat(integerFormat);
        this.properFractionFormat = pff;
    }

    // Test constructor with no arguments
    @org.junit.Test
    public void testProperFractionFormatDefaultConstructor() {
        ProperFractionFormat pff = new ProperFractionFormat();
        assertEquals("org.apache.commons.math3.fraction.ProperFractionFormat@3e826e", pff.getClass().getName());
    }

    // Test constructor with NumberFormat argument
    @org.junit.Test
    public void testProperFractionFormatWithNumberFormatConstructor() {
        ProperFractionFormat pff = new ProperFractionFormat(integerFormat);
        assertEquals("org.apache.commons.math3.fraction.ProperFractionFormat@7318436e", pff.getClass().getName());
    }

    // Test constructor with three NumberFormat arguments
    @org.junit.Test
    public void testProperFractionFormatWithThreeNumberFormatArguments() {
        NumberFormat wholeFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
        NumberFormat numeratorFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
        NumberFormat denominatorFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
        ProperFractionFormat pff = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
        assertEquals("org.apache.commons.math3.fraction.ProperFractionFormat@3e8180d4", pff.getClass().getName());
    }

    // Test format method with a simple fraction
    @org.junit.Test
    public void testFormatSimpleFraction() {
        Fraction fraction = new Fraction(1, 2);
        String formatted = this.properFractionFormat.format(fraction, new StringBuffer(), null).toString();
        assertEquals("1/2", formatted);
    }

    // Test format method with a fraction having a whole part
    @org.junit.Test
    public void testFormatFractionWithWholePart() {
        Fraction fraction = new Fraction(3, 2);
        String formatted = this.properFractionFormat.format(fraction, new StringBuffer(), null).toString();
        assertEquals("1 1/2", formatted);
    }

    // Test format method with a negative fraction
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testFormatNegativeFraction() {
        Fraction fraction = new Fraction(-1, 2);
        String formatted = this.properFractionFormat.format(fraction, new StringBuffer(), null).toString();
    }

    // Test parse method with a simple fraction
    @org.junit.Test
    public void testParseSimpleFraction() {
        Fraction fraction = this.properFractionFormat.parse("1/2", new ParsePosition(0, null));
        assertEquals(new Fraction(1, 2), fraction);
    }

    // Test parse method with a fraction having a whole part
    @org.junit.Test
    public void testParseFractionWithWholePart() {
        Fraction fraction = this.properFractionFormat.parse("1 1/2", new ParsePosition(0, null));
        assertEquals(new Fraction(1, 2), fraction.getNumerator());
        assertEquals(new Fraction(1, 2), fraction.getDenominator());
    }

    // Test parse method with a fraction having a negative sign
    @org.junit.Test
    public void testParseNegativeFraction() {
        Fraction fraction = this.properFractionFormat.parse("-1/2", new ParsePosition(0, null));
        assertEquals(new Fraction(-1, 2), fraction);
    }

    // Test parse method with an invalid input
    @org.junit.Test(expected = NumberFormatException.class)
    public void testParseInvalidInput() {
        this.properFractionFormat.parse("1 a/2", new ParsePosition(0, null));
    }

    // Test setWholeFormat method with null argument
    @org.junit.Test(expected = NullArgumentException.class)
    public void testSetWholeFormatWithNullArgument() {
        this.properFractionFormat.setWholeFormat(null);
    }

    private ProperFractionFormat properFractionFormat;
}