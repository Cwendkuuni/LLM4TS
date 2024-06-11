package org.apache.commons.math3.fraction.test;

import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.NumberFormatUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

 private ProperFractionFormat properFractionFormat;

 @Test
 public void testFormatProperFraction() {
 Fraction fraction = new Fraction(2, 3);
 String expectedOutput = "1 2/3";
 StringBuffer sb = new StringBuffer();
 properFractionFormat = new ProperFractionFormat(NumberFormatUtils.DEFAULT);
 properFractionFormat.format(fraction, sb, null);
 assertEquals(expectedOutput, sb.toString());
 }

 @Test
 public void testFormatMixedNumber() {
 Fraction fraction = new Fraction(4, 3);
 String expectedOutput = "1 1 1/3";
 StringBuffer sb = new StringBuffer();
 ProperFractionFormat properFractionFormat = new ProperFractionFormat(NumberFormatUtils.DEFAULT, NumberFormatUtils.DEFAULT, NumberFormatUtils.DEFAULT);
 properFractionFormat.format(fraction, sb, null);
 assertEquals(expectedOutput, sb.toString());
 }

 @Test
 public void testFormatZeroFraction() {
 Fraction fraction = new Fraction(0, 1);
 String expectedOutput = "0";
 StringBuffer sb = new StringBuffer();
 ProperFractionFormat properFractionFormat = new ProperFractionFormat(NumberFormatUtils.DEFAULT);
 properFractionFormat.format(fraction, sb, null);
 assertEquals(expectedOutput, sb.toString());
 }

 @Test
 public void testFormatNegativeFraction() {
 Fraction fraction = new Fraction(-2, 3);
 String expectedOutput = "-1 2/3";
 StringBuffer sb = new StringBuffer();
 ProperFractionFormat properFractionFormat = new ProperFractionFormat(NumberFormatUtils.DEFAULT);
 properFractionFormat.format(fraction, sb, null);
 assertEquals(expectedOutput, sb.toString());
 }

 @Test
 public void testParseProperFraction() {
 String input = "1 2/3";
 Fraction fraction = ProperFractionFormat.parse(input);
 assertEquals(new Fraction(1, 2), fraction);
 }

 @Test
 public void testParseMixedNumber() {
 String input = "1 1 1/3";
 Fraction fraction = ProperFractionFormat.parse(input);
 assertEquals(new Fraction(4, 3), fraction);
 }

 @Test
 public void testParseZeroFraction() {
 String input = "0";
 Fraction fraction = ProperFractionFormat.parse(input);
 assertEquals(new Fraction(0, 1), fraction);
 }

 @Test
 public void testParseNegativeFraction() {
 String input = "-1 2/3";
 Fraction fraction = ProperFractionFormat.parse(input);
 assertEquals(new Fraction(-1, 2), fraction);
 }

 @Test
 public void testParseInvalidInput() {
 String input = "1/0";
 Fraction fraction = ProperFractionFormat.parse(input);
 assertNull(fraction);
 }

 @Test
 public void testSetWholeFormat() {
 ProperFractionFormat properFractionFormat = new ProperFractionFormat(NumberFormatUtils.DEFAULT);
 NumberFormat wholeFormat = NumberFormatUtils.getNumberInstance();
 properFractionFormat.setWholeFormat(wholeFormat);
 assertEquals(wholeFormat, properFractionFormat.getWholeFormat());
 }
}