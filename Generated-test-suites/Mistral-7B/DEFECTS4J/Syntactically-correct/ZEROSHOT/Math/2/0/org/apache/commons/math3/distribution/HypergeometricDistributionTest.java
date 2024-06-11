import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class HypergeometricDistributionTest {

 @Test(expected = NotPositiveException.class)
 public void testConstructorWithNegativeNumberOfSuccesses() {
 try {
 new HypergeometricDistribution(10, -1, 5);
 } catch (NotPositiveException e) {
 // expected exception
 }
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testConstructorWithZeroPopulationSize() {
 try {
 new HypergeometricDistribution(10, 5, 5);
 } catch (NotStrictlyPositiveException e) {
 // expected exception
 }
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testConstructorWithNumberOfSuccessesGreaterThanPopulationSize() {
 try {
 new HypergeometricDistribution(10, 11, 5);
 } catch (NumberIsTooLargeException e) {
 // expected exception
 }
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testConstructorWithSampleSizeGreaterThanPopulationSize() {
 try {
 new HypergeometricDistribution(10, 5, 11);
 } catch (NumberIsTooLargeException e) {
 // expected exception
 }
 }

 @Test
 public void testGetNumberOfSuccesses() {
 HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
 assertEquals(5, dist.getNumberOfSuccesses());
 }

 @Test
 public void testGetPopulationSize() {
 HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
 assertEquals(10, dist.getPopulationSize());
 }

 @Test
 public void testGetSampleSize() {
 HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
 assertEquals(5, dist.getSampleSize());
 }

 @Test
 public void testCumulativeProbability() {
 HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
 // test lower bound
 assertEquals(0.0, dist.cumulativeProbability(0), 1e-10);
 // test success case
 assertEquals(0.10954568379144524, dist.cumulativeProbability(2), 1e-10);
 // test upper bound
 assertEquals(1.0, dist.cumulativeProbability(5), 1e-10);
 }

 @Test
 public void testProbability() {
 HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
 // test lower bound
 assertEquals(0.0, dist.probability(0), 1e-10);
 // test success case
 assertEquals(0.07878787878787879, dist.probability(2), 1e-10);
 // test upper bound
 assertEquals(0.10954568379144524, dist.probability(2), 1e-10);
 }

 @Test
 public void testUpperCumulativeProbability() {
 HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
 // test lower bound
 assertEquals(1.0, dist.upperCumulativeProbability(0), 1e-10);
 // test success case
 assertEquals(0.8904543162121217, dist.upperCumulativeProbability(2), 1e-10);
 // test upper bound
 assertEquals(0.0, dist.upperCumulativeProbability(5), 1e-10);
 }

 @Test
 public void testNumericalMean() {
 HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
 assertEquals(3.5, dist.getNumericalMean(), 1e-10);
 }

 @Test
 public void testNumericalVariance() {
 HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
 assertEquals(1.6713223223223223, dist.getNumericalVariance(), 1e-10);
 }

 @Test
 public void testSupportLowerBound() {
 HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
 assertEquals(5, dist.getSupportLowerBound());
 }

 @Test
 public void testSupportUpperBound() {
 HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
 assertEquals(5, dist.getSupportUpperBound());
 }

 @Test
 public void testIsSupportConnected() {
 HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
 assertEquals(true, dist.isSupportConnected());
 }
}