import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.junit.Test;
import org.junit.Assert.*;

public class HypergeometricDistributionTest {

 private HypergeometricDistribution hd;

 @Before
 public void setUp() {
 hd = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 }

 @Test(expected = NotPositiveException.class)
 public void testConstructorWithNegativePopulationSize() {
 hd = new HypergeometricDistribution(new Well19937c(), -5, 5, 5);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testConstructorWithNonPositiveNumberOfSuccesses() {
 hd = new HypergeometricDistribution(new Well19937c(), 10, -5, 5);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testConstructorWithNonPositiveSampleSize() {
 hd = new HypergeometricDistribution(new Well19937c(), 10, 5, -5);
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testConstructorWithNumberOfSuccessesLargerThanPopulationSize() {
 hd = new HypergeometricDistribution(new Well19937c(), 10, 11, 5);
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testConstructorWithSampleSizeLargerThanPopulationSize() {
 hd = new HypergeometricDistribution(new Well19937c(), 10, 5, 11);
 }

 @Test
 public void testGetNumberOfSuccesses() {
 Assert.assertEquals(5, hd.getNumberOfSuccesses());
 }

 @Test
 public void testGetPopulationSize() {
 Assert.assertEquals(10, hd.getPopulationSize());
 }

 @Test
 public void testGetSampleSize() {
 Assert.assertEquals(5, hd.getSampleSize());
 }

 @Test
 public void testCumulativeProbabilityZero() {
 Assert.assertEquals(0.0, hd.cumulativeProbability(0), 0.001);
 }

 @Test
 public void testCumulativeProbabilitySuccess() {
 Assert.assertEquals(0.4534864137274354, hd.cumulativeProbability(3), 0.001);
 }

 @Test
 public void testCumulativeProbabilityFailure() {
 Assert.assertEquals(0.5465135862725646, hd.cumulativeProbability(5), 0.001);
 }

 @Test
 public void testProbabilityZero() {
 Assert.assertEquals(0.0, hd.probability(0), 0.001);
 }

 @Test
 public void testProbabilitySuccess() {
 Assert.assertEquals(0.2706625831262533, hd.probability(2), 0.001);
 }

 @Test
 public void testProbabilityFailure() {
 Assert.assertEquals(0.2693374168737467, hd.probability(3), 0.001);
 }

 @Test
 public void testUpperCumulativeProbabilityZero() {
 Assert.assertEquals(1.0, hd.upperCumulativeProbability(5), 0.001);
 }

 @Test
 public void testUpperCumulativeProbabilitySuccess() {
 Assert.assertEquals(0.4534864137274354, hd.upperCumulativeProbability(3), 0.001);
 }

 @Test
 public void testUpperCumulativeProbabilityFailure() {
 Assert.assertEquals(0.5465135862725646, hd.upperCumulativeProbability(6), 0.001);
 }

 @Test
 public void testGetNumericalMean() {
 Assert.assertEquals(5.0, hd.getNumericalMean(), 0.001);
 }

 @Test
 public void testGetNumericalVariance() {
 double variance = hd.getNumericalVariance();
 Assert.assertEquals(2.936228225086924, variance, 0.001);
 }

 @Test
 public void testGetSupportLowerBound() {
 Assert.assertEquals(0, hd.getSupportLowerBound());
 }

 @Test
 public void testGetSupportUpperBound() {
 Assert.assertEquals(5, hd.getSupportUpperBound());
 }

 @Test
 public void testIsSupportConnected() {
 Assert.assertTrue(hd.isSupportConnected());
 }
}