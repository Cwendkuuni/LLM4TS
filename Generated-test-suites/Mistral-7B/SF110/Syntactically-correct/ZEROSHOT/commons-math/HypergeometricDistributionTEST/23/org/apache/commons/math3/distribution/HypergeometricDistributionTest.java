import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Assert;
import org.junit.Test;

public class HypergeometricDistributionTest {

 @Test(expected = NotPositiveException.class)
 public void testHypergeometricDistributionWithNegativePopulationSize() {
 try {
 new HypergeometricDistribution(new Well19937c(), -5, 3, 5);
 } catch (NotStrictlyPositiveException e) {
 // expected exception for population size
 }

 try {
 new HypergeometricDistribution(new Well19937c(), 5, -3, 5);
 } catch (NotPositiveException e) {
 // expected exception for number of successes
 }

 try {
 new HypergeometricDistribution(new Well19937c(), 5, 3, -5);
 } catch (NumberIsTooLargeException e) {
 // expected exception for sample size
 }

 try {
 new HypergeometricDistribution(new Well19937c(), 5, 3, 5, 5);
 } catch (NotPositiveException e) {
 // expected exception for population size
 Assert.fail("Unexpected exception: " + e.getMessage());
 }
 }

 @Test
 public void testHypergeometricDistributionWithValidParameters() {
 HypergeometricDistribution hd = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);

 Assert.assertEquals(5, hd.getNumberOfSuccesses());
 Assert.assertEquals(10, hd.getPopulationSize());
 Assert.assertEquals(5, hd.getSampleSize());

 Assert.assertEquals(0.0, hd.cumulativeProbability(0), 0.001);
 Assert.assertEquals(1.0, hd.cumulativeProbability(hd.getSupportUpperBound()), 0.001);
 Assert.assertEquals(0.0, hd.probability(hd.getSupportLowerBound()), 0.001);
 Assert.assertEquals(1.0, hd.probability(hd.getSupportUpperBound()), 0.001);

 Assert.assertEquals(hd.getNumericalMean(), hd.getSampleSize() * hd.getNumberOfSuccesses() / hd.getPopulationSize(), 0.01);
 Assert.assertEquals(hd.getNumericalVariance(), hd.calculateNumericalVariance(), 0.01);
 }

 @Test
 public void testHypergeometricDistributionProbability() {
 HypergeometricDistribution hd = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);

 int[] expectedProbabilities = { 0.004711, 0.013757, 0.036532, 0.066488, 0.117647, 0.168067, 0.201369, 0.183138, 0.101365, 0.037575, 0.009775, 0.001949, 0.000296 };

 for (int i = 0; i <= hd.getSupportUpperBound(); i++) {
 double actualProbability = hd.probability(i);
 Assert.assertEquals(expectedProbabilities[i], actualProbability, 0.001);
 }
 }

 @Test
 public void testHypergeometricDistributionCumulativeProbability() {
 HypergeometricDistribution hd = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);

 double[] expectedCumulativeProbabilities = { 0.0, 0.004711, 0.004752, 0.008463, 0.013757, 0.01812, 0.019786, 0.023521, 0.025137, 0.027354, 0.028473, 0.028871, 0.028871 };

 for (int i = 0; i <= hd.getSupportUpperBound(); i++) {
 double actualCumulativeProbability = hd.cumulativeProbability(i);
 Assert.assertEquals(expectedCumulativeProbabilities[i], actualCumulativeProbability, 0.001);
 }
 }

 @Test
 public void testHypergeometricDistributionUpperCumulativeProbability() {
 HypergeometricDistribution hd = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);

 double[] expectedUpperCumulativeProbabilities = { 1.0, 0.995289, 0.990527, 0.985765, 0.978572, 0.969605, 0.958914, 0.945698, 0.928935, 0.906943, 0.87887, 0.841771, 0.796201 };

 for (int i = 0; i <= hd.getSupportUpperBound(); i++) {
 double actualUpperCumulativeProbability = hd.upperCumulativeProbability(i);
 Assert.assertEquals(expectedUpperCumulativeProbabilities[i], actualUpperCumulativeProbability, 0.001);
 }
 }
}