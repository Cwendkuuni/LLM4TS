package org.apache.commons.math3.dfp;

import static org.junit.Assert.*;

import org.apache.commons.math3.dfp.Dfp;
import org.apache.commons.math3.dfp.DfpDec;
import org.apache.commons.math3.dfp.DfpField;
import org.apache.commons.math3.dfp.DfpFieldConfigurable;
import org.apache.commons.math3.dfp.DfpRoundingMode;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class DfpDecTest {

 private DfpFieldConfigurable field = new Dfp128Field();

 @org.junit.Before
 public void setUp() {
 DfpDec.setDefaultRoundingMode(DfpRoundingMode.HALF_UP);
 }

 @org.junit.Test
 public void testNewInstance() {
 DfpDec d1 = new DfpDec(field);
 DfpDec d2 = d1.newInstance();
 assertEquals(d1, d2);
 }

 @org.junit.Test
 public void testNewInstanceInt() {
 DfpDec d1 = new DfpDec(field, 42);
 DfpDec d2 = d1.newInstance(42);
 assertEquals(d1, d2);
 }

 @org.junit.Test
 public void testNewInstanceLong() {
 DfpDec d1 = new DfpDec(field, 123456789L);
 DfpDec d2 = d1.newInstance(123456789L);
 assertEquals(d1, d2);
 }

 @org.junit.Test
 public void testNewInstanceDouble() {
 DfpDec d1 = new DfpDec(field, 3.14159265);
 DfpDec d2 = d1.newInstance(3.14159265);
 assertEquals(d1, d2, 0.00000001);
 }

 @org.junit.Test
 public void testNewInstanceDfp() {
 DfpDec d1 = new DfpDec(field, new DfpDec(field, 1.5));
 DfpDec d2 = d1.newInstance(d1);
 assertEquals(d1, d2);
 }

 @org.junit.Test
 public void testRound() {
 DfpDec d1 = new DfpDec(field, 1.2345);
 int roundingMode = DfpRoundingMode.DOWN.getValue();
 int result = d1.round(roundingMode);
 assertEquals(0, result);
 DfpDec d2 = new DfpDec(field, 1.5);
 result = d2.round(roundingMode);
 assertEquals(1, result);
 }

 @org.junit.Test
 public void testRoundWithCustomRoundingMode() {
 DfpDec d1 = new DfpDec(field, 1.5);
 int customRoundingMode = RoundingMode.UP.getValue();
 int result = d1.round(customRoundingMode);
 assertEquals(1, result);
 }

 @org.junit.Test
 public void testRoundWithBigDecimal() {
 DfpDec d1 = new DfpDec(field, new BigDecimal("1.2345"));
 int roundingMode = RoundingMode.HALF_UP.getValue();
 int result = d1.round(roundingMode);
 assertEquals(0, result);
 DfpDec d2 = new DfpDec(field, new BigDecimal("1.5"));
 result = d2.round(roundingMode);
 assertEquals(1, result);
 }

 @org.junit.Test
 public void testNextAfter() {
 DfpDec d1 = new DfpDec(field, 1.2345);
 DfpDec d2 = d1.nextAfter(new DfpDec(field, 1.235));
 assertEquals(d2, new DfpDec(field, 1.23500001), 0.00000001);
 DfpDec d3 = new DfpDec(field, 1.2345);
 DfpDec d4 = d3.nextAfter(new DfpDec(field, 1.2));
 assertEquals(d4, new DfpDec(field, 1.2), 0.00000001);
 }

 @org.junit.Test
 public void testNextAfterWithZero() {
 DfpDec d1 = new DfpDec(field, 0.0);
 DfpDec d2 = d1.nextAfter(new DfpDec(field, 1.0));
 assertEquals(d2, new DfpDec(field, 1.0));
 }

 @org.junit.Test
 public void testNextAfterWithNegativeNumber() {
 DfpDec d1 = new DfpDec(field, -1.2345);
 DfpDec d2 = d1.nextAfter(new DfpDec(field, -1.235));
 assertEquals(d2, new DfpDec(field, -1.23500001), 0.00000001);
 }

 @org.junit.Test
 public void testNextAfterWithBigDecimalNegativeNumber() {
 DfpDec d1 = new DfpDec(field, new BigDecimal("-1.2345"));
 DfpDec d2 = d1.nextAfter(new DfpDec(field, new BigDecimal("-1.235")));
 assertEquals(d2, new DfpDec(field, new BigDecimal("-1.23500001")), 0.00000001);
 }
}