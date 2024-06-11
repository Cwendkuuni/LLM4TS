package org.apache.commons.math.distribution;

 import static org.junit.Assert.assertEquals;
 import static org.junit.Assert.assertTrue;

 import org.apache.commons.math3.special.Erf;
 import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
 import org.apache.commons.math3.stat.descriptive.moment.Mean;
 import org.junit.Test;
 import org.junit.runner.RunWith;
 import org.junit.runners.Parameterized;
 import org.junit.runners.Parameterized.Parameters;

 import java.util.Arrays;
 import java.util.Collection;

 @RunWith(Parameterized.class)
 public class NormalDistributionImplTest {

 private NormalDistributionImpl distribution;
 private double mean;
 private double stdDev;

 public NormalDistributionImplTest(double mean, double stdDev) {
 this.mean = mean;
 this.stdDev = stdDev;
 distribution = new NormalDistributionImpl(mean, stdDev);
 }

 @Parameters(name = "{index}: Mean={0}, StandardDeviation={1}")
 public static Collection<Object[]> data() {
 return Arrays.asList(new Object[][] { {0.0, 1.0}, {1.0, 0.5}, {2.0, 2.0} });
 }

 @Test
 public void testMean() {
 assertEquals(mean, distribution.getMean(), 1e-10);
 }

 @Test
 public void testStandardDeviation() {
 assertEquals(stdDev, distribution.getStandardDeviation(), 1e-10);
 }

 @Test
 public void testCumulativeProbability() {
 for (double x = -10; x <= 10; x += 1) {
 double prob = distribution.cumulativeProbability(x);
 double expected = 0.5 + 0.5 * Erf.erf((x - mean) / (stdDev * Math.sqrt(2)));
 assertEquals(expected, prob, 1e-5);
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetNegativeStandardDeviation() {
 distribution.setStandardDeviation(-1);
 }

 @Test
 public void testInverseCumulativeProbability() throws MathException {
 for (double p = 0; p <= 1; p += 0.01) {
 double x = distribution.inverseCumulativeProbability(p);
 double meanX = mean + stdDev * Erf.erfinv(2 * p - 1);
 assertEquals(meanX, x, 1e-5);
 }
 }

 @Test(expected = MaxIterationsExceededException.class)
 public void testInverseCumulativeProbabilityWithLargeAbsMeanAndLargeAbsStandardDeviation() throws MathException {
 Mean mean = new Mean();
 StandardDeviation stdDev = new StandardDeviation();
 distribution = new NormalDistributionImpl(1000000.0, 1000000.0);
 double x = distribution.inverseCumulativeProbability(0.5);
 mean.evaluate(distribution.sample(10000));
 stdDev.evaluate(distribution.sample(10000));
 assertTrue(Math.abs(mean.getResult() - 1000000.0) < 1e-5);
 assertTrue(Math.abs(stdDev.getResult() - 1000000.0) < 1e-5);
 assertEquals(x, meanX = 1000000.0 + 1000000.0 * Erf.erfinv(0.5), 1e-5);
 }
 }