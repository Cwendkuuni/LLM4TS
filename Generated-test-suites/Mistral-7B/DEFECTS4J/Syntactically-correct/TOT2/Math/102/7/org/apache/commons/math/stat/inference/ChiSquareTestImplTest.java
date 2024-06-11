package org.apache.commons.math.stat.inference;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistributionImpl;
import org.apache.commons.math3.exception.MathException;
import org.junit.Test;
import static org.junit.Assert.*;

public class ChiSquareTestImplTest {

 private ChiSquareTestImpl chiSquareTest;
 private ChiSquaredDistribution dist;

 @Before
 public void setUp() throws Exception {
 chiSquareTest = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(1.0));
 dist = new ChiSquaredDistributionImpl(1.0);
 }

 @Test
 public void testChiSquareWithValidInputs() {
 long[] observed = {1, 2, 3, 4, 5, 5, 6, 7, 8, 8};
 long[] expected = {2, 3, 3, 2, 2, 2, 1, 1, 1, 1};
 double chiSquareStat = chiSquareTest.chiSquare(expected, observed);
 double pValue = chiSquareTest.chiSquareTest(expected, observed);
 assertTrue(chiSquareStat > 0);
 assertTrue(pValue > 0);
 assertTrue(pValue < 1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testChiSquareWithInvalidLengthInputs() {
 long[] observed = {1, 2};
 long[] expected = {2, 3, 3};
 chiSquareTest.chiSquare(expected, observed);
 fail("IllegalArgumentException expected but not thrown");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testChiSquareWithInvalidNonPositiveExpectedInputs() {
 long[] observed = {1, 2, 3, 4, 5, 5, 6, 7, 8, 8};
 long[] expected = {-1, 2, 3, 2, 2, 2, 1, 1, 1, 1};
 chiSquareTest.chiSquare(expected, observed);
 fail("IllegalArgumentException expected but not thrown");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testChiSquareWithInvalidNonNonNegativeObservedInputs() {
 long[] observed = {-1, 2, 3, 4, 5, 5, 6, 7, 8, 8};
 long[] expected = {2, 3, 3, 2, 2, 2, 1, 1, 1, 1};
 chiSquareTest.chiSquare(expected, observed);
 fail("IllegalArgumentException expected but not thrown");
 }

 @Test
 public void testChiSquareWithZeroObservedAndExpectedValues() {
 long[] observed = {0, 0};
 long[] expected = {0, 0};
 double chiSquareStat = chiSquareTest.chiSquare(expected, observed);
 double pValue = chiSquareTest.chiSquareTest(expected, observed);
 assertTrue(chiSquareStat == 0);
 assertTrue(pValue == 1.0);
 }

 @Test
 public void testChiSquareWithLargeInputs() {
 long[] observed = new long[1000];
 long[] expected = new long[1000];
 for (int i = 0; i < observed.length; i++) {
 observed[i] = (long) (Math.random() * 100);
 expected[i] = (long) (Math.random() * 100) + 5;
 }
 double chiSquareStat = chiSquareTest.chiSquare(expected, observed);
 double pValue = chiSquareTest.chiSquareTest(expected, observed);
 assertTrue(chiSquareStat > 0);
 assertTrue(pValue > 0);
 assertTrue(pValue < 1);
 }

 @Test
 public void testChiSquareWithLargeInputsAndSmallDegreesOfFreedom() {
 long[] observed = new long[1000];
 long[] expected = new long[1000];
 for (int i = 0; i < observed.length; i++) {
 observed[i] = (long) (Math.random() * 100);
 expected[i] = (long) (Math.random() * 100) + 5;
 }
 ChiSquareTestImpl chiSquareTestWithSmallDegreesOfFreedom = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(1000.0 - 1.0));
 double chiSquareStat = chiSquareTestWithSmallDegreesOfFreedom.chiSquare(expected, observed);
 double pValue = chiSquareTestWithSmallDegreesOfFreedom.chiSquareTest(expected, observed);
 assertTrue(chiSquareStat > 0);
 assertTrue(pValue > 0);
 assertTrue(pValue < 1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testChiSquareWithInvalidDegreesOfFreedom() {
 ChiSquareTestImpl chiSquareTestWithNegativeDegreesOfFreedom = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(-1.0));
 chiSquareTestWithNegativeDegreesOfFreedom.chiSquareTest(new long[10][10], new long[10][10]);
 fail("IllegalArgumentException expected but not thrown");
 }

 @Test
 public void testChiSquareTestWithValidInputs() {
 long[] observed = {1, 2, 3, 4, 5, 5, 6, 7, 8, 8};
 long[] expected = {2, 3, 3, 2, 2, 2, 1, 1, 1, 1};
 double alpha = 0.01;
 boolean result = chiSquareTest(expected, observed, alpha);
 assertTrue(result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testChiSquareTestWithInvalidAlpha() {
 long[] observed = {1, 2, 3, 4, 5, 5, 6, 7, 8, 8};
 long[] expected = {2, 3, 3, 2, 2, 2, 1, 1, 1, 1};
 boolean result = chiSquareTest(expected, observed, 1.1);
 fail("IllegalArgumentException expected but not thrown");
 }

 @Test
 public void testChiSquareTestWithValidInputsAndSmallAlpha() {
 long[] observed = {1, 2, 3, 4, 5, 5, 6, 7, 8, 8};
 long[] expected = {2, 3, 3, 2, 2, 2, 1, 1, 1, 1};
 double alpha = 0.0001;
 boolean result = chiSquareTest(expected, observed, alpha);
 assertFalse(result);
 }

 @Test
 public void testChiSquareTestWithValidInputsAndAlphaGreaterThanOne() {
 long[] observed = {1, 2, 3, 4, 5, 5, 6, 7, 8, 8};
 long[] expected = {2, 3, 3, 2, 2, 2, 1, 1, 1, 1};
 double alpha = 2.0;
 boolean result = chiSquareTest(expected, observed, alpha);
 assertFalse(result);
 }

 @Test
 public void testChiSquareTestWithLargeInputs() {
 long[] observed = new long[1000];
 long[] expected = new long[1000];
 for (int i = 0; i < observed.length; i++) {
 observed[i] = (long) (Math.random() * 100);
 expected[i] = (long) (Math.random() * 100) + 5;
 }
 boolean result = chiSquareTest(expected, observed, 0.05);
 assertTrue(result);
 }

 @Test
 public void testChiSquareTestWithLargeInputsAndSmallAlpha() {
 long[] observed = new long[1000];
 long[] expected = new long[1000];
 for (int i = 0; i < observed.length; i++) {
 observed[i] = (long) (Math.random() * 100);
 expected[i] = (long) (Math.random() * 100) + 5;
 }
 boolean result = chiSquareTest(expected, observed, 0.0001);
 assertFalse(result);
 }

 @Test
 public void testChiSquareTestWithLargeInputsAndAlphaGreaterThanOne() {
 long[] observed = new long[1000];
 long[] expected = new long[1000];
 for (int i = 0; i < observed.length; i++) {
 observed[i] = (long) (Math.random() * 100);
 expected[i] = (long) (Math.random() * 100) + 5;
 }
 boolean result = chiSquareTest(expected, observed, 2.0);
 assertFalse(result);
 }

 @Test
 public void testChiSquareTestDataSetsComparisonWithValidInputs() {
 long[] observed1 = {1, 2, 3, 4, 5, 5, 6, 7, 8, 8};
 long[] observed2 = {2, 3, 3, 2, 2, 2, 1, 1, 1, 1};
 boolean result = chiSquareTestDataSetsComparison(observed1, observed2, 0.01);
 assertTrue(result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testChiSquareTestDataSetsComparisonWithInvalidAlpha() {
 long[] observed1 = {1, 2, 3, 4, 5, 5, 6, 7, 8, 8};
 long[] observed2 = {2, 3, 3, 2, 2, 2, 1, 1, 1, 1};
 boolean result = chiSquareTestDataSetsComparison(observed1, observed2, 1.1);
 fail("IllegalArgumentException expected but not thrown");
 }

 @Test
 public void testChiSquareTestDataSetsComparisonWithValidInputsAndSmallAlpha() {
 long[] observed1 = {1, 2, 3, 4, 5, 5, 6, 7, 8, 8};
 long[] observed2 = {2, 3, 3, 2, 2, 2, 1, 1, 1, 1};
 boolean result = chiSquareTestDataSetsComparison(observed1, observed2, 0.0001);
 assertFalse(result);
 }

 @Test
 public void testChiSquareTestDataSetsComparisonWithLargeInputs() {
 long[] observed1 = new long[1000];
 long[] observed2 = new long[1000];
 for (int i = 0; i < observed1.length; i++) {
 observed1[i] = (long) (Math.random() * 100);
 observed2[i] = (long) (Math.random() * 100);
 }
 boolean result = chiSquareTestDataSetsComparison(observed1, observed2, 0.05);
 assertTrue(result);
 }

 @Test
 public void testChiSquareTestDataSetsComparisonWithLargeInputsAndSmallAlpha() {
 long[] observed1 = new long[1000];
 long[] observed2 = new long[1000];
 for (int i = 0; i < observed1.length; i++) {
 observed1[i] = (long) (Math.random() * 100);
 observed2[i] = (long) (Math.random() * 100);
 }
 boolean result = chiSquareTestDataSetsComparison(observed1, observed2, 0.0001);
 assertFalse(result);
 }

 @Test
 public void testChiSquareTestDataSetsComparisonWithLargeInputsAndAlphaGreaterThanOne() {
 long[] observed1 = new long[1000];
 long[] observed2 = new long[1000];
 for (int i = 0; i < observed1.length; i++) {
 observed1[i] = (long) (Math.random() * 100);
 observed2[i] = (long) (Math.random() * 100);
 }
 boolean result = chiSquareTestDataSetsComparison(observed1, observed2, 2.0);
 assertFalse(result);
 }

 //---------------------  Private helper methods  ----------------------------------------
 private void chiSquareTest(long[][] counts, double alpha) throws IllegalArgumentException, MathException {
 chiSquareTest = new ChiSquareTestImpl(dist);
 boolean result = chiSquareTest.chiSquareTest(counts, alpha);
 assertEquals(result, chiSquareTest.chiSquareTestDataSetsComparison(counts, alpha));
 }
}