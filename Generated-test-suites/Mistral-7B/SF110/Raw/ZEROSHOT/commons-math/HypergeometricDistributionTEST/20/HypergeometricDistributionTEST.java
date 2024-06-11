import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

 @Test
 public void testHypergeometricDistribution_ValidConstruction() {
 try {
 HypergeometricDistribution hd = new HypergeometricDistribution(new Well19937c(), 10, 5, 50);
 assertEquals(5, hd.getNumberOfSuccesses());
 assertEquals(10, hd.getPopulationSize());
 assertEquals(50, hd.getSampleSize());
 } catch (NotPositiveException | NotStrictlyPositiveException | NumberIsTooLargeException e) {
 fail("Construction should not throw exceptions with valid arguments.");
 }
 }

 @Test(expected = NotPositiveException.class)
 public void testHypergeometricDistribution_PopulationSizeNegative() {
 try {
 new HypergeometricDistribution(new Well19937c(), -1, 5, 50);
 fail("Should throw exception with population size less than zero.");
 } catch (NotPositiveException e) {
 // Expected exception
 }
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testHypergeometricDistribution_NumberOfSuccessesNegative() {
 try {
 new HypergeometricDistribution(new Well19937c(), 10, -1, 50);
 fail("Should throw exception with number of successes less than zero.");
 } catch (NotStrictlyPositiveException e) {
 // Expected exception
 }
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testHypergeometricDistribution_SampleSizeNegative() {
 try {
 new HypergeometricDistribution(new Well19937c(), 10, 5, -1);
 fail("Should throw exception with sample size less than zero.");
 } catch (NotStrictlyPositiveException e) {
 // Expected exception
 }
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testHypergeometricDistribution_NumberOfSuccessesLargerThanPopulationSize() {
 try {
 new HypergeometricDistribution(new Well19937c(), 10, 11, 50);
 fail("Should throw exception with number of successes larger than population size.");
 } catch (NumberIsTooLargeException e) {
 // Expected exception
 }
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testHypergeometricDistribution_SampleSizeLargerThanPopulationSize() {
 try {
 new HypergeometricDistribution(new Well19937c(), 10, 5, 11);
 fail("Should throw exception with sample size larger than population size.");
 } catch (NumberIsTooLargeException e) {
 // Expected exception
 }
 }

 @Test
 public void testHypergeometricDistribution_CumulativeProbability() {
 HypergeometricDistribution hd = new HypergeometricDistribution(new Well19937c(), 10, 5, 50);
 int[] domain = hd.getDomain();
 for (int x = domain[0]; x <= domain[1]; x++) {
 double expected = hd.cumulativeProbability(x);
 double actual = hd.sample();
 assertEquals(expected, actual, 1e-9);
 }
 }

 @Test
 public void testHypergeometricDistribution_Probability() {
 HypergeometricDistribution hd = new HypergeometricDistribution(new Well19937c(), 10, 5, 50);
 int[] domain = hd.getDomain();
 for (int x = domain[0]; x <= domain[1]; x++) {
 double expected = hd.probability(x);
 double actual = hd.probability(x);
 assertEquals(expected, actual, 1e-9);
 }
 }

 @Test
 public void testHypergeometricDistribution_UpperCumulativeProbability() {
 HypergeometricDistribution hd = new HypergeometricDistribution(new Well19937c(), 10, 5, 50);
 int[] domain = hd.getDomain();
 for (int x = domain[0]; x <= domain[1]; x++) {
 double expected = hd.upperCumulativeProbability(x);
 double actual = hd.cumulativeProbability(x + 1);
 assertEquals(expected, actual, 1e-9);
 }
 }

 @Test
 public void testHypergeometricDistribution_Mean() {
 HypergeometricDistribution hd = new HypergeometricDistribution(new Well19937c(), 10, 5, 50);
 double expected = hd.getSampleSize() * hd.getNumberOfSuccesses() / hd.getPopulationSize();
 double actual = hd.getNumericalMean();
 assertEquals(expected, actual, 1e-9);
 }

 @Test
 public void testHypergeometricDistribution_Variance() {
 HypergeometricDistribution hd = new HypergeometricDistribution(new Well19937c(), 10, 5, 50);
 double expected = hd.calculateNumericalVariance();
 double actual = hd.getNumericalVariance();
 assertEquals(expected, actual, 1e-9);
 }
}