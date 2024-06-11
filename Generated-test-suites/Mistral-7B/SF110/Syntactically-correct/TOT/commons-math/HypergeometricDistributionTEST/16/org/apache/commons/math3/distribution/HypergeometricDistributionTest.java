import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import org.junit.Assert.*;

public class HypergeometricDistributionTest {

 @Test(expected = NotPositiveException.class)
 public void testConstructorWithNegativePopulationSize() {
 HypergeometricDistribution dist = null;
 try {
 dist = new HypergeometricDistribution(new Well19937c(), -5, 3, 5);
 } catch (NotStrictlyPositiveException | NumberIsTooLargeException e) {
 // Expected NegativePopulationException
 }
 Assert.assertNull(dist);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testConstructorWithNonPositiveNumberOfSuccesses() {
 HypergeometricDistribution dist = null;
 try {
 dist = new HypergeometricDistribution(new Well19937c(), 5, -2, 5);
 } catch (NotPositiveException | NumberIsTooLargeException e) {
 // Expected NotPositiveNumberOfSuccessesException
 }
 Assert.assertNull(dist);
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testConstructorWithNumberOfSuccessesGreaterThanPopulationSize() {
 HypergeometricDistribution dist = null;
 try {
 dist = new HypergeometricDistribution(new Well19937c(), 10, 12, 5);
 } catch (NotPositiveException | NotStrictlyPositiveException e) {
 // Expected NumberIsTooLargeException
 }
 Assert.assertNull(dist);
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testConstructorWithSampleSizeGreaterThanPopulationSize() {
 HypergeometricDistribution dist = null;
 try {
 dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 12);
 } catch (NotPositiveException | NotStrictlyPositiveException e) {
 // Expected NumberIsTooLargeException
 }
 Assert.assertNull(dist);
 }

 @Test
 public void testCumulativeProbability() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 int[] domain = dist.getDomain();
 for (int i = domain[0]; i <= domain[1]; i++) {
 double p = dist.cumulativeProbability(i);
 System.out.printf("cumulativeProbability(%d) = %f\n", i, p);
 Assert.assertTrue(p >= 0 && p <= 1);
 }
 }

 @Test
 public void testProbability() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 for (int i = 0; i <= 10; i++) {
 double p = dist.probability(i);
 System.out.printf("probability(%d) = %f\n", i, p);
 Assert.assertTrue(p >= 0);
 }
 }

 @Test
 public void testGetDomain() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 int[] domain = dist.getDomain();
 System.out.printf("getDomain(): lower bound = %d, upper bound = %d\n", domain[0], domain[1]);
 assertTrue(domain[0] >= 0 && domain[1] <= 10);
 }

 @Test
 public void testGetNumberOfSuccesses() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 int numberOfSuccesses = dist.getNumberOfSuccesses();
 System.out.printf("getNumberOfSuccesses(): %d\n", numberOfSuccesses);
 assertEquals(3, numberOfSuccesses);
 }

 @Test
 public void testGetPopulationSize() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 int populationSize = dist.getPopulationSize();
 System.out.printf("getPopulationSize(): %d\n", populationSize);
 assertEquals(10, populationSize);
 }

 @Test
 public void testGetSampleSize() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 int sampleSize = dist.getSampleSize();
 System.out.printf("getSampleSize(): %d\n", sampleSize);
 assertEquals(5, sampleSize);
 }

 @Test
 public void testGetNumericalMean() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 double numericalMean = dist.getNumericalMean();
 System.out.printf("getNumericalMean(): %f\n", numericalMean);
 assertEquals(3.0, numericalMean, 0.001);
 }

 @Test
 public void testGetNumericalVariance() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 double numericalVariance = dist.getNumericalVariance();
 System.out.printf("getNumericalVariance(): %f\n", numericalVariance);
 assertEquals(2.976131523738387E-4, numericalVariance, 1e-9);
 }

 @Test
 public void testGetSupportLowerBound() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 int supportLowerBound = dist.getSupportLowerBound();
 System.out.printf("getSupportLowerBound(): %d\n", supportLowerBound);
 assertEquals(2, supportLowerBound);
 }

 @Test
 public void testGetSupportUpperBound() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 int supportUpperBound = dist.getSupportUpperBound();
 System.out.printf("getSupportUpperBound(): %d\n", supportUpperBound);
 assertEquals(3, supportUpperBound);
 }

 @Test
 public void testIsSupportConnected() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 boolean isSupportConnected = dist.isSupportConnected();
 System.out.printf("isSupportConnected(): %b\n", isSupportConnected);
 assertTrue(isSupportConnected);
 }
}