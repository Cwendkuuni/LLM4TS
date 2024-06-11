import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class HypergeometricDistributionTest {

 private HypergeometricDistribution dist;

 @Test
 public void testHypergeometricDistribution_ValidConstruction() {
 try {
 dist = new HypergeometricDistribution(10, 5, 5);
 } catch (Exception e) {
 e.printStackTrace();
 fail("Exception during instantiation");
 }
 Assert.assertEquals(5, dist.getNumberOfSuccesses());
 Assert.assertEquals(10, dist.getPopulationSize());
 Assert.assertEquals(5, dist.getSampleSize());
 }

 @Test(expected = NotPositiveException.class)
 public void testHypergeometricDistribution_NegativePopulationSize() {
 try {
 dist = new HypergeometricDistribution(-5, 5, 5);
 } catch (Exception e) {
 Assert.assertSame(e.getCause(), new NotPositiveException(e.getMessage()));
 }
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testHypergeometricDistribution_ZeroNumberOfSuccesses() {
 try {
 dist = new HypergeometricDistribution(5, 0, 5);
 } catch (Exception e) {
 Assert.assertSame(e.getCause(), new NotStrictlyPositiveException(e.getMessage()));
 }
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testHypergeometricDistribution_ZeroNumberOfSuccessesAndPopulationSize() {
 try {
 dist = new HypergeometricDistribution(0, 5, 5);
 } catch (Exception e) {
 Assert.assertSame(e.getCause(), new NotStrictlyPositiveException(e.getMessage()));
 }
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testHypergeometricDistribution_ZeroSampleSize() {
 try {
 dist = new HypergeometricDistribution(5, 5, 0);
 } catch (Exception e) {
 Assert.assertSame(e.getCause(), new NotStrictlyPositiveException(e.getMessage()));
 }
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testHypergeometricDistribution_NumberOfSuccessesLargerThanPopulationSize() {
 try {
 dist = new HypergeometricDistribution(5, 6, 5);
 } catch (Exception e) {
 Assert.assertSame(e.getCause(), new NumberIsTooLargeException(e.getMessage(), 6, 5, true));
 }
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testHypergeometricDistribution_SampleSizeLargerThanPopulationSize() {
 try {
 dist = new HypergeometricDistribution(5, 5, 6);
 } catch (Exception e) {
 Assert.assertSame(e.getCause(), new NumberIsTooLargeException(e.getMessage(), 6, 5, true));
 }
 }

 @Test
 public void testCumulativeProbability_ValidInput() {
 try {
 int expected = 0.5;
 int x = (dist.getUpperDomain(dist.getPopulationSize(), dist.getNumberOfSuccesses()) + dist.getNumberOfSuccesses()) / 2;
 double actual = dist.cumulativeProbability(x);
 Assert.assertEquals(expected, actual, 0.0001);
 } catch (Exception e) {
 e.printStackTrace();
 fail("Exception during calculation of cumulative probability");
 }
 }

 @Test
 public void testCumulativeProbability_OutOfRangeInput() {
 try {
 int x = dist.getLowerDomain(dist.getPopulationSize(), dist.getNumberOfSuccesses(), dist.getSampleSize()) - 1;
 double actual = dist.cumulativeProbability(x);
 double expected = 0.0;
 Assert.assertEquals(expected, actual, 0.0001);
 } catch (Exception e) {
 e.printStackTrace();
 fail("Exception during calculation of cumulative probability with out-of-range input");
 }
}

 @Test
 public void testProbability_ValidInput() {
 try {
 int x = (dist.getUpperDomain(dist.getPopulationSize(), dist.getNumberOfSuccesses()) + dist.getNumberOfSuccesses()) / 2;
 double actual = dist.probability(x);
 double expected = dist.cumulativeProbability(x + 1) - dist.cumulativeProbability(x);
 Assert.assertEquals(expected, actual, 0.0001);
 } catch (Exception e) {
 e.printStackTrace();
 fail("Exception during calculation of probability");
 }
 }

 @Test
 public void testUpperCumulativeProbability_ValidInput() {
 try {
 int x = (dist.getUpperDomain(dist.getPopulationSize(), dist.getNumberOfSuccesses()) + dist.getNumberOfSuccesses()) / 2;
 double actual = dist.upperCumulativeProbability(x);
 double expected = 1.0 - dist.cumulativeProbability(x);
 Assert.assertEquals(expected, actual, 0.0001);
 } catch (Exception e) {
 e.printStackTrace();
 fail("Exception during calculation of upper cumulative probability");
 }
 }

 @Test
 public void testUpperCumulativeProbability_OutOfRangeInput() {
 try {
 int x = dist.getLowerDomain(dist.getPopulationSize(), dist.getNumberOfSuccesses(), dist.getSampleSize()) + 1;
 double actual = dist.upperCumulativeProbability(x);
 double expected = 0.0;
 Assert.assertEquals(expected, actual, 0.0001);
 } catch (Exception e) {
 e.printStackTrace();
 fail("Exception during calculation of upper cumulative probability with out-of-range input");
 }
 }
}