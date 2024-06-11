import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FDistributionTest {

 private FDistribution fdistribution;
 private double[] testValues;

 @Before
 public void setUp() throws NotStrictlyPositiveException {
 fdistribution = new FDistribution(3.0, 5.0);
 testValues = new double[] { 0.0, 0.1, 1.0, 2.0, 3.0, 5.0, 10.0 };
 }

 @After
 public void tearDown() {
 fdistribution = null;
 }

 @Test
 public void testGetNumeratorDegreesOfFreedom() {
 assertEquals(3.0, fdistribution.getNumeratorDegreesOfFreedom(), 0.0);
 }

 @Test
 public void testGetDenominatorDegreesOfFreedom() {
 assertEquals(5.0, fdistribution.getDenominatorDegreesOfFreedom(), 0.0);
 }

 @Test
 public void testGetSolverAbsoluteAccuracy() {
 assertEquals(FDistribution.DEFAULT_INVERSE_ABSOLUTE_ACCURACY,
 fdistribution.getSolverAbsoluteAccuracy(), 0.0);
 }

 @Test
 public void testDensity() {
 for (double x : testValues) {
 double density = fdistribution.density(x);
 System.out.println("Density for x = " + x + " is " + density);
 assertEquals(Math.isFinite(density), true, 0.0);
 }
 }

 @Test
 public void testCumulativeProbability() {
 for (double x : testValues) {
 double cumProb = fdistribution.cumulativeProbability(x);
 System.out.println("Cumulative probability for x = " + x + " is " + cumProb);
 assertEquals(Math.isFinite(cumProb), true, 0.0);
 }
 }

 @Test
 public void testGetNumericalMean() {
 double numericalMean = fdistribution.getNumericalMean();
 System.out.println("Numerical mean is " + numericalMean);
 assertTrue(numericalMean > 0.0);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetNumericalVarianceWithInvalidDF() {
 FDistribution fdistributionInvalid = new FDistribution(1.0, 1.0);
 fdistributionInvalid.getNumericalVariance();
 }

 @Test
 public void testGetNumericalVariance() {
 double numericalVariance = fdistribution.getNumericalVariance();
 System.out.println("Numerical variance is " + numericalVariance);
 assertTrue(numericalVariance > 0.0);
 }

 @Test
 public void testGetSupportLowerBound() {
 assertEquals(0.0, fdistribution.getSupportLowerBound(), 0.0);
 }

 @Test
 public void testGetSupportUpperBound() {
 assertEquals(Double.POSITIVE_INFINITY, fdistribution.getSupportUpperBound(), 0.0);
 }

 @Test
 public void testIsSupportLowerBoundInclusive() {
 assertTrue(fdistribution.isSupportLowerBoundInclusive());
 }

 @Test
 public void testIsSupportUpperBoundInclusive() {
 assertTrue(fdistribution.isSupportUpperBoundInclusive() == false);
 }

 @Test
 public void testIsSupportConnected() {
 assertTrue(fdistribution.isSupportConnected());
 }
}