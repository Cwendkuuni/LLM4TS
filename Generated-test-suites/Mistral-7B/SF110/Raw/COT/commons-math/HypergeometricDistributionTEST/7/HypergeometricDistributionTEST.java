import org.apache.commons.math3.*;
import org.apache.commons.math3.distribution.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.random.*;
import org.apache.commons.math3.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

 private HypergeometricDistribution hd;

 @Test
 public void testHypergeometricDistributionConstructor() {
 try {
 hd = new HypergeometricDistribution(1, 1, 1);
 } catch (Exception e) {
 fail("Should not throw any exception");
 }
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testHypergeometricDistributionConstructorWithNegativePopulationSize() {
 try {
 hd = new HypergeometricDistribution(-1, 1, 1);
 } catch (NotStrictlyPositiveException e) {
 // Expected exception
 }
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testHypergeometricDistributionConstructorWithNegativeNumberOfSuccesses() {
 try {
 hd = new HypergeometricDistribution(1, -1, 1);
 } catch (NotStrictlyPositiveException e) {
 // Expected exception
 }
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testHypergeometricDistributionConstructorWithNegativeSampleSize() {
 try {
 hd = new HypergeometricDistribution(1, 1, -1);
 } catch (NotStrictlyPositiveException e) {
 // Expected exception
 }
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testHypergeometricDistributionConstructorWithNumberOfSuccessesLargerThanPopulationSize() {
 try {
 hd = new HypergeometricDistribution(1, 2, 1);
 } catch (NumberIsTooLargeException e) {
 // Expected exception
 }
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testHypergeometricDistributionConstructorWithSampleSizeLargerThanPopulationSize() {
 try {
 hd = new HypergeometricDistribution(1, 1, 2);
 } catch (NumberIsTooLargeException e) {
 // Expected exception
 }
 }

 @Test
 public void testCumulativeProbability() {
 hd = new HypergeometricDistribution(10, 5, 5);
 int x = 2;
 double expected = 0.1389;
 double actual = hd.cumulativeProbability(x);
 assertEquals(expected, actual, 0.001);
 }

 @Test
 public void testCumulativeProbabilityEdgeCaseMin() {
 hd = new HypergeometricDistribution(10, 5, 5);
 int x = 0;
 double expected = 0.0;
 double actual = hd.cumulativeProbability(x);
 assertEquals(expected, actual, 0.001);
 }

 @Test
 public void testCumulativeProbabilityEdgeCaseMax() {
 hd = new HypergeometricDistribution(10, 5, 5);
 int x = 10;
 double expected = 0.9977;
 double actual = hd.cumulativeProbability(x);
 assertEquals(expected, actual, 0.001);
 }

 @Test
 public void testProbability() {
 hd = new HypergeometricDistribution(10, 5, 5);
 int x = 2;
 double expected = 0.1389;
 double actual = hd.probability(x);
 assertEquals(expected, actual, 0.001);
 }

 @Test
 public void testProbabilityEdgeCaseMin() {
 hd = new HypergeometricDistribution(10, 5, 5);
 int x = 0;
 double expected = 0.0;
 double actual = hd.probability(x);
 assertEquals(expected, actual, 0.001);
 }

 @Test
 public void testProbabilityEdgeCaseMax() {
 hd = new HypergeometricDistribution(10, 5, 5);
 int x = 10;
 double expected = 0.0;
 double actual = hd.probability(x);
 assertEquals(expected, actual, 0.001);
 }

 @Test
 public void testUpperCumulativeProbability() {
 hd = new HypergeometricDistribution(10, 5, 5);
 int x = 2;
 double expected = 0.8611;
 double actual = hd.upperCumulativeProbability(x);
 assertEquals(expected, actual, 0.001);
 }

 @Test
 public void testUpperCumulativeProbabilityEdgeCaseMin() {
 hd = new HypergeometricDistribution(10, 5, 5);
 int x = 0;
 double expected = 1.0;
 double actual = hd.upperCumulativeProbability(x);
 assertEquals(expected, actual, 0.001);
 }

 @Test
 public void testUpperCumulativeProbabilityEdgeCaseMax() {
 hd = new HypergeometricDistribution(10, 5, 5);
 int x = 10;
 double expected = 0.0;
 double actual = hd.upperCumulativeProbability(x);
 assertEquals(expected, actual, 0.001);
 }

 @Test
 public void testNumericalMean() {
 hd = new HypergeometricDistribution(10, 5, 5);
 double expected = 5.0;
 double actual = hd.getNumericalMean();
 assertEquals(expected, actual, 0.001);
 }

 @Test
 public void testNumericalVariance() {
 hd = new HypergeometricDistribution(10, 5, 5);
 double expected = 1.9615;
 double actual = hd.getNumericalVariance();
 assertEquals(expected, actual, 0.001);
 }

 @Test
 public void testGetSupportLowerBound() {
 hd = new HypergeometricDistribution(10, 5, 5);
 int expected = 2;
 int actual = hd.getSupportLowerBound();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetSupportUpperBound() {
 hd = new HypergeometricDistribution(10, 5, 5);
 int expected = 5;
 int actual = hd.getSupportUpperBound();
 assertEquals(expected, actual);
 }

 @Test
 public void testIsSupportConnected() {
 hd = new HypergeometricDistribution(10, 5, 5);
 boolean expected = true;
 boolean actual = hd.isSupportConnected();
 assertEquals(expected, actual);
 }

}