package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParsePosition;
import org.apache.commons.commons.math3.fraction.Fraction;
import org.apache.commons.commons.math3.util.MathUtils;
import org.apache.commons.commons.math3.exception.ParseException;
import org.apache.commons.commons.math3.exception.NullArgumentException;

public class ProperFractionFormatTest {

    @Test
    public void testProperFractionFormatEmpty() {
        ProperFractionFormat format = new ProperFractionFormat();
        assertNull(format.format(new Fraction(1, 1), new StringBuffer(), new FieldPosition(0, 0)));
        assertNull(format.parse("", new ParsePosition(0)));
    }

    @Test
    public void testProperFractionFormatDefault() {
        ProperFractionFormat format = new ProperFractionFormat();
        NumberFormat wholeFormat = new DecimalFormat("0");
        ProperFractionFormat newFormat = new ProperFractionFormat(wholeFormat);
        assertEquals("1", newFormat.format(new Fraction(1, 1), new StringBuffer(), new FieldPosition(0, 0)).toString());
        assertEquals(new Fraction(1, 1), newFormat.parse("1", new ParsePosition(0)));
        assertEquals("1", newFormat.format(new Fraction(1, 2), new StringBuffer(), new FieldPosition(0, 0)).toString());
        assertEquals(new Fraction(1, 2), newFormat.parse("1 2/1", new ParsePosition(0)));
        assertEquals("1 2/1", newFormat.format(new Fraction(3, 2), new StringBuffer(), new FieldPosition(0, 0)).toString());
        assertEquals(new Fraction(3, 2), newFormat.parse("1 2/1", new ParsePosition(0)));
    }

    @Test
    public void testProperFractionFormatCustom() {
        NumberFormat wholeFormat = new DecimalFormat("#,##0");
        NumberFormat numeratorFormat = new DecimalFormat("0");
        NumberFormat denominatorFormat = new DecimalFormat("#");
        ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
        assertEquals("1", format.format(new Fraction(1, 1), new StringBuffer(), new FieldPosition(0, 0)).toString());
        assertEquals(new Fraction(1, 1), format.parse("1", new ParsePosition(0)));
        assertEquals("1", format.format(new Fraction(1, 2), new StringBuffer(), new FieldPosition(0, 0)).toString());
        assertEquals(new Fraction(1, 2), format.parse("1 2/1", new ParsePosition(0)));
        assertEquals("1 2/1", format.format(new Fraction(3, 2), new StringBuffer(), new FieldPosition(0, 0)).toString());
        assertEquals(new Fraction(3, 2), format.parse("1 2/1", new ParsePosition(0)));
        assertEquals("2,001", format.format(new Fraction(2001, 1), new StringBuffer(), new FieldPosition(0, 0)).toString());
        assertEquals(new Fraction(2001, 1), format.parse("2,001", new ParsePosition(0)));
        assertEquals("3 6/7", format.format(new Fraction(21, 7), new StringBuffer(), new FieldPosition(0, 0)).toString());
        assertEquals(new Fraction(21, 7), format.parse("3 6/7", new ParsePosition(0)));
        assertEquals("1 0/3", format.format(new Fraction(0, 3), new StringBuffer(), new FieldPosition(0, 0)).toString());
        assertNull(format.parse("1 0/3", new ParsePosition(0)));
    }

    @Test
    public void testProperFractionFormatParseException() {
        NumberFormat wholeFormat = new DecimalFormat("#,##0.##");
        NumberFormat numeratorFormat = new DecimalFormat("#,##0.##");
        NumberFormat denominatorFormat = new DecimalFormat("#,##0.##");
        ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
        try {
            format.parse("1,0/0", new ParsePosition(0));
            fail();
        } catch (ParseException e) {
            // Expected
        }
        try {
            format.parse("1,0/-1", new ParsePosition(0));
            fail();
        } catch (ParseException e) {
            // Expected
        }
        try {
            format.parse("1,1/0", new ParsePosition(0));
            fail();
        } catch (ParseException e) {
            // Expected
        }
        try {
            format.parse("-1,1/1", new ParsePosition(0));
            fail();
        } catch (ParseException e) {
            // Expected
        }
        try {
            format.parse("1,-1/1", new ParsePosition(0));
            fail();
        } catch (ParseException e) {
            // Expected
        }
        try {
            format.parse("1,1/-1", new ParsePosition(0));
            fail();
        } catch (ParseException e) {
            // Expected
        }
    }

    @Test
    public void testProperFractionFormatNullArgumentException() {
        try {
            new ProperFractionFormat(null);
            fail();
        } catch (NullArgumentException e) {
            // Expected
        }
        try {
            new ProperFractionFormat(null, null, null);
            fail();
        } catch (NullArgumentException e) {
            // Expected
        }
    }
}