import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {

 private NormalDistribution distribution;

 @Test
 public void testNormalDistributionCreation() {
 NormaldistributionImpl normalDistribution = new NormalDistributionImpl();
 NormalDistribution expectedNormalDistribution = new NormalDistribution();
 assertEquals(expectedNormalDistribution.getClass(), normalDistribution.getClass());
 distribution = normalDistribution;
 }

 @Test
 public void testNormalDistributionCreationWithMeanAndStandardDeviation() {
 double mean = 5.0;
 double standardDeviation = 3.0;
 NormaldistributionImpl normalDistribution = new NormalDistributionImpl(mean, standardDeviation);
 NormalDistribution expectedNormalDistribution = new NormalDistribution(mean, standardDeviation);
 assertEquals(expectedNormalDistribution.getClass(), normalDistribution.getClass());
 assertEquals(mean, normalDistribution.getMean(), 0.0);
 assertEquals(standardDeviation, normalDistribution.getStandardDeviation(), 0.0);
 distribution = normalDistribution;
 }

 @Test
 public void testGetMean() {
 double mean = 4.5;
 distribution.setMean(mean);
 assertEquals(mean, distribution.getMean(), 0.0);
 }

 @Test
 public void testSetMeanWithNegativeValue() {
 try {
 distribution.setMean(-2.5);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testSetMeanWithZeroValue() {
 try {
 distribution.setMean(0.0);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testSetStandardDeviation() {
 double standardDeviation = 2.5;
 distribution.setStandardDeviation(standardDeviation);
 assertEquals(standardDeviation, distribution.getStandardDeviation(), 0.0);
 }

 @Test
 public void testSetStandardDeviationWithZeroOrNegativeValue() {
 try {
 distribution.setStandardDeviation(0.0);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }

 try {
 distribution.setStandardDeviation(-2.5);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testCumulativeProbabilityWithValidX() throws MathException {
 double x = 1.5;
 double expectedCumulativeProbability = 0.433192757958652;
 double actualCumulativeProbability = distribution.cumulativeProbability(x);
 assertEquals(expectedCumulativeProbability, actualCumulativeProbability, 0.001);
 }

 @Test
 public void testCumulativeProbabilityWithXMoreThan20StandardDeviationsFromMean() throws MathException {
 double x = 50 * distribution.getStandardDeviation();
 double expectedCumulativeProbability = 0.0;
 double actualCumulativeProbability = distribution.cumulativeProbability(x);
 assertEquals(expectedCumulativeProbability, actualCumulativeProbability, 0.0);
 }

 @Test(expected = MathException.class)
 public void testCumulativeProbabilityWithXLessThanMeanMinus20StandardDeviations() throws MathException {
 double x = -50 * distribution.getStandardDeviation();
 distribution.cumulativeProbability(x);
 fail("Expected MathException");
 }

 @Test
 public void testInverseCumulativeProbabilityWithZero() throws MathException {
 double p = 0.0;
 double expectedX = Double.NEGATIVE_INFINITY;
 double actualX = distribution.inverseCumulativeProbability(p);
 assertEquals(expectedX, actualX, 0.0);
 }

 @Test
 public void testInverseCumulativeProbabilityWithOne() throws MathException {
 double p = 1.0;
 double expectedX = Double.POSITIVE_INFINITY;
 double actualX = distribution.inverseCumulativeProbability(p);
 assertEquals(expectedX, actualX, 0.0);
 }

 @Test
 public void testInverseCumulativeProbabilityWithProbabilityBetweenZeroAndOne() throws MathException {
 double p = 0.5;
 double expectedX = 0.0;
 double actualX = distribution.inverseCumulativeProbability(p);
 assertEquals(expectedX, actualX, 0.0);
 }

 @Test
 public void testInverseCumulativeProbabilityWithProbabilityMoreThanOne() throws MathException {
 double p = 1.5;
 double expectedX = Double.POSITIVE_INFINITY;
 double actualX = distribution.inverseCumulativeProbability(p);
 assertEquals(expectedX, actualX, 0.0);
 }

 @Test(expected = MathException.class)
 public void testInverseCumulativeProbabilityWithProbabilityLessThanZero() throws MathException {
 double p = -0.5;
 distribution.inverseCumulativeProbability(p);
 fail("Expected MathException");
 }

 @Test
 public void testGetDomainLowerBound() {
 double p = 0.2;
 double expectedDomainLowerBound = -Double.MAX_VALUE;
 double actualDomainLowerBound = distribution.getDomainLowerBound(p);
 assertEquals(expectedDomainLowerBound, actualDomainLowerBound, 0.0);
 }

 @Test
 public void testGetDomainUpperBound() {
 double p = 0.8;
 double expectedDomainUpperBound = Double.MAX_VALUE;
 double actualDomainUpperBound = distribution.getDomainUpperBound(p);
 assertEquals(expectedDomainUpperBound, actualDomainUpperBound, 0.0);
 }

 @Test
 public void testGetInitialDomain() {
 double p = 0.6;
 double expectedInitialDomain = 2.564946526868136;
 double actualInitialDomain = distribution.getInitialDomain(p);
 assertEquals(expectedInitialDomain, actualInitialDomain, 0.0);
 }

}