package org.apache.commons.math3.dfp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.dfp.Dfp;
import org.apache.commons.math3.dfp.DfpDec;
import org.apache.commons.math3.dfp.DfpField;
import org.apache.commons.math3.dfp.DfpFactory2;
import org.apache.commons.math3.dfp.DfpRoundingMode;
import org.junit.Before;
import org.junit.Test;

public class DfpDecTest {

 private DfpFactory2 dfpFactory;
 private DfpDec dfpDec;

 @Before
 public void setUp() {
 dfpFactory = new Dfp128Field();
 dfpDec = new DfpDec(dfpFactory);
 }

 @Test
 public void testNewInstance() {
 DfpDec dfpDec1 = dfpDec.newInstance();
 assertEquals(dfpDec, dfpDec1);
 DfpDec dfpDec2 = dfpDec.newInstance(1);
 assertEquals(1, dfpDec2.intValueExact());
 DfpDec dfpDec3 = dfpDec.newInstance(0.5);
 assertEquals(0.5, dfpDec3.doubleValue(), 0.0001);
 }

 @Test
 public void testNewInstanceWithDfp() {
 DfpDec dfpDec1 = new DfpDec(dfpFactory, new DfpDec(dfpFactory, 1));
 DfpDec dfpDec2 = dfpDec.newInstance(dfpDec1);
 assertEquals(dfpDec1, dfpDec2);
 }

 @Test(expected = IllegalStateException.class)
 public void testInvalidNewInstance() {
 try {
 DfpDec dfpDec1 = new DfpDec(dfpFactory, new Dfp(dfpFactory, 3.14));
 } catch (IllegalStateException e) {
 assertEquals("Incompatible fields: 10 vs 128", e.getMessage());
 }
 }

 @Test
 public void testGetDecimalDigits() {
 assertEquals(483, dfpDec.getDecimalDigits());
 }

 @Test
 public void testRounding() {
 DfpDec dfpDec1 = new DfpDec(dfpFactory, 1.2345);
 DfpDec dfpDec2 = new DfpDec(dfpFactory, 1.2);
 DfpDec roundHalfDown = dfpDec1.round(DfpRoundingMode.HALF_DOWN);
 assertTrue(roundHalfDown.compareTo(new DfpDec(dfpFactory, 1.234)) <= 0);
 DfpDec roundHalfUp = dfpDec1.round(DfpRoundingMode.HALF_UP);
 assertFalse(roundHalfDown.equals(roundHalfUp));
 assertTrue(roundHalfUp.compareTo(new DfpDec(dfpFactory, 1.235)) > 0);
 DfpDec roundDown = dfpDec2.round(DfpRoundingMode.DOWN);
 assertEquals(new DfpDec(dfpFactory, 1.2), roundDown);
 DfpDec roundUp = dfpDec2.round(DfpRoundingMode.UP);
 assertEquals(new DfpDec(dfpFactory, 1.3), roundUp);
 DfpDec roundHalfEven = dfpDec2.round(DfpRoundingMode.HALF_EVEN);
 assertEquals(new DfpDec(dfpFactory, 1.2), roundHalfEven);
 }

 @Test
 public void testNextAfter() {
 DfpDec dfpDec1 = new DfpDec(dfpFactory, 1.2345);
 DfpDec dfpDec2 = dfpDec1.nextAfter(new DfpDec(dfpFactory, 1.235));
 assertEquals(dfpDec2, new DfpDec(dfpFactory, 1.235));
 DfpDec dfpDec3 = dfpDec1.nextAfter(new DfpDec(dfpFactory, 1.234));
 assertEquals(dfpDec3, new DfpDec(dfpFactory, 1.234));
 DfpDec dfpDec4 = dfpDec1.nextAfter(new DfpDec(dfpFactory, 1.233));
 assertEquals(dfpDec4, new DfpDec(dfpFactory, 1.234));
 DfpDec dfpDec5 = dfpDec1.nextAfter(new DfpDec(dfpFactory, 1.232));
 assertEquals(dfpDec5, new DfpDec(dfpFactory, 1.233));
 }

 @Test
 public void testNegativeNextAfter() {
 DfpDec dfpDec1 = new DfpDec(dfpFactory, -1.2345);
 DfpDec dfpDec2 = dfpDec1.nextAfter(new DfpDec(dfpFactory, -1.235));
 assertEquals(dfpDec2, new DfpDec(dfpFactory, -1.235));
 DfpDec dfpDec3 = dfpDec1.nextAfter(new DfpDec(dfpFactory, -1.234));
 assertEquals(dfpDec3, new DfpDec(dfpFactory, -1.234));
 DfpDec dfpDec4 = dfpDec1.nextAfter(new DfpDec(dfpFactory, -1.233));
 assertEquals(dfpDec4, new DfpDec(dfpFactory, -1.234));
 DfpDec dfpDec5 = dfpDec1.nextAfter(new DfpDec(dfpFactory, -1.232));
 assertEquals(dfpDec5, new DfpDec(dfpFactory, -1.233));
 }

 @Test
 public void testZeroNextAfter() {
 DfpDec dfpDec = new DfpDec(dfpFactory, 0);
 DfpDec nextAfterZero = dfpDec.nextAfter(new DfpDec(dfpFactory, 1E-308));
 assertNull(nextAfterZero);
 }

 @Test
 public void testNaNNextAfter() {
 DfpDec dfpDec = new DfpDec(dfpFactory, Double.NaN);
 DfpDec nextAfterNaN = dfpDec.nextAfter(new DfpDec(dfpFactory, Double.POSITIVE_INFINITY));
 assertEquals(dfpDec, nextAfterNaN);
 }
}