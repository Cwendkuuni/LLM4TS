import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class HypergeometricDistributionTest {

 @Test
 public void testConstructorWithValidParameters() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
 assertEquals(10, distribution.getPopulationSize());
 assertEquals(5, distribution.getNumberOfSuccesses());
 assertEquals(3, distribution.getSampleSize());
 }

 @Test
 public void testConstructorWithInvalidPopulationSize() {
 assertThrows(NotStrictlyPositiveException.class, () -> new HypergeometricDistribution(-1, 5, 3));
 }

 @Test
 public void testConstructorWithInvalidNumberOfSuccesses() {
 assertThrows(NotPositiveException.class, () -> new HypergeometricDistribution(10, -1, 3));
 }

 @Test
 public void testConstructorWithInvalidSampleSize() {
 assertThrows(NotPositiveException.class, () -> new HypergeometricDistribution(10, 5, -1));
 }

 @Test
 public void testConstructorWithNumberOfSuccessesGreaterThanPopulationSize() {
 assertThrows(NumberIsTooLargeException.class, () -> new HypergeometricDistribution(10, 11, 3));
 }

 @Test
 public void testConstructorWithSampleSizeGreaterThanPopulationSize() {
 assertThrows(NumberIsTooLargeException.class, () -> new HypergeometricDistribution(10, 5, 11));
 }

 @Test
 public void testCumulativeProbability() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);

 int[] domain = distribution.getDomain();

 // Test for lower bound
 assertEquals(0.0, distribution.cumulativeProbability(domain[0] - 1), 0.001);

 // Test for boundary values
 assertEquals(0.5, distribution.cumulativeProbability(domain[0]), 0.001);
 assertEquals(0.5, distribution.cumulativeProbability(domain[1] - 1), 0.001);

 // Test for upper bound
 assertEquals(1.0, distribution.cumulativeProbability(domain[1]), 0.001);
 }

 @Test
 public void testProbability() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);

 int[] domain = distribution.getDomain();

 // Test for lower bound
 assertEquals(0.0, distribution.probability(domain[0] - 1), 0.001);

 // Test for boundary values
 assertEquals(0.111111111, distribution.probability(domain[0]), 0.0001);
 assertEquals(0.1818181818, distribution.probability(domain[1] - 1), 0.0001);

 // Test for upper bound
 assertEquals(0.3636363636, distribution.probability(domain[1]), 0.0001);
 }

 @Test
 public void testUpperCumulativeProbability() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);

 int[] domain = distribution.getDomain();

 // Test for lower bound
 assertEquals(1.0, distribution.upperCumulativeProbability(domain[0] - 1), 0.001);

 // Test for boundary values
 assertEquals(0.4928571429, distribution.upperCumulativeProbability(domain[0]), 0.0001);
 assertEquals(0.4928571429, distribution.upperCumulativeProbability(domain[1] - 1), 0.0001);

 // Test for upper bound
 assertEquals(0.0, distribution.upperCumulativeProbability(domain[1]), 0.001);
 }

 @Test
 public void testGetNumericalMean() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
 assertEquals(3.0, distribution.getNumericalMean(), 0.001);
 }

 @Test
 public void testGetNumericalVariance() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
 assertEquals(1.723701921, distribution.getNumericalVariance(), 0.001);
 }

 @Test
 public void testGetSupportLowerBound() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
 assertEquals(2, distribution.getSupportLowerBound());
 }

 @Test
 public void testGetSupportUpperBound() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
 assertEquals(3, distribution.getSupportUpperBound());
 }

 @Test
 public void testIsSupportConnected() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
 assertTrue(distribution.isSupportConnected());
 }
}