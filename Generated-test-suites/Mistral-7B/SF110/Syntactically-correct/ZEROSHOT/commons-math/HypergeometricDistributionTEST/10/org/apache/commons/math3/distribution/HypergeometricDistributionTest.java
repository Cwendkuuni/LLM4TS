package org.apache.commons.math3.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.RandomUtils;
import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.FastMath;

public class HypergeometricDistributionTest {

 private HypergeometricDistribution distribution;

 @Before
 public void setUp() {
 try {
 distribution = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 } catch (NotPositiveException | NotStrictlyPositiveException | NumberIsTooLargeException e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testConstructionWithValidParameters() {
 assertEquals(10, distribution.getPopulationSize());
 assertEquals(3, distribution.getNumberOfSuccesses());
 assertEquals(5, distribution.getSampleSize());
 }

 @Test(expected = NotPositiveException.class)
 public void testConstructionWithInvalidPopulationSize() {
 new HypergeometricDistribution(new Well19937c(), -1, 3, 5);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testConstructionWithInvalidNumberOfSuccesses() {
 new HypergeometricDistribution(new Well19937c(), 10, -1, 5);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testConstructionWithInvalidSampleSize() {
 new HypergeometricDistribution(new Well19937c(), 10, 3, -1);
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testConstructionWithNumberOfSuccessesLargerThanPopulationSize() {
 new HypergeometricDistribution(new Well19937c(), 10, 11, 5);
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testConstructionWithSampleSizeLargerThanPopulationSize() {
 new HypergeometricDistribution(new Well19937c(), 10, 3, 11);
 }

 @Test
 public void testCumulativeProbability() {
 for (int i = 0; i <= 7; i++) {
 double expected = FastMath.comb(5, i) / FastMath.pow(10, 5.0) * FastMath.pow(9, i) / FastMath.pow(13, 5.0);
 double actual = distribution.cumulativeProbability(i);
 assertEquals(expected, actual, 1e-10);
 }
 }

 @Test
 public void testProbability() {
 for (int i = 0; i <= 5; i++) {
 double expected = FastMath.comb(5, i) / FastMath.pow(13, 5.0);
 double actual = distribution.probability(i);
 assertEquals(expected, actual, 1e-10);
 }
 }

 @Test
 public void testUpperCumulativeProbability() {
 for (int i = 1; i <= 5; i++) {
 double expected = 1 - FastMath.comb(5, i - 1) / FastMath.pow(9, i) / FastMath.pow(13, 5.0);
 double actual = distribution.upperCumulativeProbability(i);
 assertEquals(expected, actual, 1e-10);
 }
 }

 @Test
 public void testGetNumberOfSuccesses() {
 assertEquals(3, distribution.getNumberOfSuccesses());
 }

 @Test
 public void testGetPopulationSize() {
 assertEquals(10, distribution.getPopulationSize());
 }

 @Test
 public void testGetSampleSize() {
 assertEquals(5, distribution.getSampleSize());
 }

 @Test
 public void testGetNumericalMean() {
 double expected = 3.0 * 5.0 / 10.0;
 double actual = distribution.getNumericalMean();
 assertEquals(expected, actual, 1e-10);
 }

 @Test
 public void testGetNumericalVariance() {
 double expected = 5.0 * 3.0 * (10.0 - 5.0) * (10.0 - 3.0) / (10.0 * 10.0 * (10.0 - 1.0));
 double actual = distribution.getNumericalVariance();
 assertEquals(expected, actual, 1e-10);
 }

 @Test
 public void testGetSupportLowerBound() {
 int expected = FastMath.max(0, 5 + 3 - 10);
 int actual = distribution.getSupportLowerBound();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetSupportUpperBound() {
 int expected = FastMath.min(3, 5);
 int actual = distribution.getSupportUpperBound();
 assertEquals(expected, actual);
 }

 @Test
 public void testIsSupportConnected() {
 assertTrue(distribution.isSupportConnected());
 }

 @Test
 public void testNextInt() {
 for (int i = 0; i < 1000; i++) {
 int actual = distribution.nextInt();
 int expectedDomain[] = distribution.getDomain();
 int domainLowerBound = expectedDomain[0];
 int domainUpperBound = expectedDomain[1];
 assertTrue(actual >= domainLowerBound && actual <= domainUpperBound);
 }
 }

}