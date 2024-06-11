import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

 @Test(expected = NotPositiveException.class)
 public void testInvalidPopulationSize() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), -1, 10, 5);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testInvalidNumberOfSuccesses() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 0, 5);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testInvalidSampleSize() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 10, -5);
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testNumberOfSuccessesLargerThanPopulationSize() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 11, 5);
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testSampleSizeLargerThanPopulationSize() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 10, 11);
 }

 @Test
 public void testCumulativeProbabilityZero() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 2, 5);
 double actual = dist.cumulativeProbability(0);
 double expected = 0.0;
 assertEquals(expected, actual, 1e-10);
 }

 @Test
 public void testCumulativeProbabilityEdgeCase() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 2, 5);
 double actual = dist.cumulativeProbability(5);
 double expected = 0.5;
 assertEquals(expected, actual, 1e-10);
 }

 @Test
 public void testCumulativeProbabilityMax() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 2, 5);
 double actual = dist.cumulativeProbability(10);
 double expected = 1.0;
 assertEquals(expected, actual, 1e-10);
 }

 @Test
 public void testProbabilityZero() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 2, 5);
 double actual = dist.probability(0);
 double expected = 0.0;
 assertEquals(expected, actual, 1e-10);
 }

 @Test
 public void testProbabilityEdgeCase() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 2, 5);
 int x = dist.getPopulationSize() - dist.getNumberOfSuccesses();
 double actual = dist.probability(x);
 double expected = dist.getPopulationSize() / (dist.getPopulationSize() + dist.getSampleSize());
 assertEquals(expected, actual, 1e-10);
 }

 @Test
 public void testProbabilityMax() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 2, 5);
 int x = dist.getSampleSize() - dist.getNumberOfSuccesses();
 double actual = dist.probability(x);
 double expected = dist.getNumberOfSuccesses() / (dist.getPopulationSize() + dist.getSampleSize());
 assertEquals(expected, actual, 1e-10);
 }

 @Test
 public void testUpperCumulativeProbabilityZero() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 2, 5);
 double actual = dist.upperCumulativeProbability(0);
 double expected = 1.0;
 assertEquals(expected, actual, 1e-10);
 }

 @Test
 public void testUpperCumulativeProbabilityEdgeCase() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 2, 5);
 int x = dist.getLowerDomain(dist.getPopulationSize(), dist.getNumberOfSuccesses(), dist.getSampleSize());
 double actual = dist.upperCumulativeProbability(x);
 double expected = dist.cumulativeProbability(x);
 assertEquals(expected, actual, 1e-10);
 }

 @Test
 public void testUpperCumulativeProbabilityMax() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 2, 5);
 int x = dist.getUpperDomain(dist.getPopulationSize(), dist.getSampleSize());
 double actual = dist.upperCumulativeProbability(x);
 double expected = 0.0;
 assertEquals(expected, actual, 1e-10);
 }

 @Test
 public void testNumericalMean() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 2, 5);
 double actual = dist.getNumericalMean();
 double expected = 2.0 * 5.0 / 10.0;
 assertEquals(expected, actual, 1e-10);
 }

 @Test
 public void testNumericalVariance() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 2, 5);
 double actual = dist.getNumericalVariance();
 double expected = 2.0 * 5.0 * (10.0 - 5.0) * (10.0 - 2.0) / (10.0 * 10.0 * 9.0);
 assertEquals(expected, actual, 1e-10);
 }

 @Test
 public void testGetSupportLowerBound() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 2, 5);
 int actual = dist.getSupportLowerBound();
 int expected = 5;
 assertEquals(expected, actual);
 }

 @Test
 public void testGetSupportUpperBound() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 2, 5);
 int actual = dist.getSupportUpperBound();
 int expected = 5;
 assertEquals(expected, actual);
 }

 @Test
 public void testIsSupportConnected() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 2, 5);
 boolean actual = dist.isSupportConnected();
 boolean expected = true;
 assertTrue(expected);
 }
}