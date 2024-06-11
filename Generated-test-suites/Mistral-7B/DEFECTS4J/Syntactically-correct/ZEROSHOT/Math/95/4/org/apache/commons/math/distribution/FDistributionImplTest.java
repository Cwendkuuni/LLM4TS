import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.FDistributionImpl;
import org.apache.commons.math3.special.Beta;
import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionImplTest {

 private FDistributionImpl distribution;

 @Test
 public void testFDistributionImplConstruction() {
 FDistributionImpl fd = new FDistributionImpl(3.0, 4.0);
 assertEquals(3.0, fd.getNumeratorDegreesOfFreedom(), 0.0);
 assertEquals(4.0, fd.getDenominatorDegreesOfFreedom(), 0.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFDistributionImplNegativeDegreesOfFreedom() {
 FDistributionImpl fd = new FDistributionImpl(-3.0, 4.0);
 fail("IllegalArgumentException not thrown for negative degrees of freedom.");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFDistributionImplZeroDegreesOfFreedom() {
 FDistributionImpl fd = new FDistributionImpl(0.0, 4.0);
 fail("IllegalArgumentException not thrown for zero degrees of freedom.");
 }

 @Test
 public void testCumulativeProbability() throws MathException {
 FDistributionImpl fd = new FDistributionImpl(3.0, 4.0);
 double x = 1.5;
 double cdf = fd.cumulativeProbability(x);
 double expectedCdf = Beta.regularizedBeta((3.0 * x) / (4.0 + 3.0 * x), 1.5, 2.0);
 assertEquals(expectedCdf, cdf, 0.001);
 }

 @Test(expected = MathException.class)
 public void testCumulativeProbabilityInvalidX() throws MathException {
 FDistributionImpl fd = new FDistributionImpl(3.0, 4.0);
 double x = -1.0;
 fd.cumulativeProbability(x);
 fail("MathException not thrown for invalid x.");
 }

 @Test
 public void testInverseCumulativeProbability() throws MathException {
 FDistributionImpl fd = new FDistributionImpl(3.0, 4.0);
 double p = 0.95;
 double x = fd.inverseCumulativeProbability(p);
 double expectedX = Beta.regularizedIncompleteBetaI(0.95, 1.5, 2.0) / 3.0;
 assertEquals(expectedX, x, 0.001);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInverseCumulativeProbabilityInvalidProbability() {
 FDistributionImpl fd = new FDistributionImpl(3.0, 4.0);
 double p = 1.1;
 fd.inverseCumulativeProbability(p);
 fail("IllegalArgumentException not thrown for invalid probability.");
 }

 @Test
 public void testGetNumeratorDegreesOfFreedom() {
 FDistributionImpl fd = new FDistributionImpl(3.0, 4.0);
 assertEquals(3.0, fd.getNumeratorDegreesOfFreedom(), 0.0);
 }

 @Test
 public void testGetDenominatorDegreesOfFreedom() {
 FDistributionImpl fd = new FDistributionImpl(3.0, 4.0);
 assertEquals(4.0, fd.getDenominatorDegreesOfFreedom(), 0.0);
 }

 @Test
 public void testSetNumeratorDegreesOfFreedom() {
 FDistributionImpl fd = new FDistributionImpl(3.0, 4.0);
 fd.setNumeratorDegreesOfFreedom(5.0);
 assertEquals(5.0, fd.getNumeratorDegreesOfFreedom(), 0.0);
 }

 @Test
 public void testSetDenominatorDegreesOfFreedom() {
 FDistributionImpl fd = new FDistributionImpl(3.0, 4.0);
 fd.setDenominatorDegreesOfFreedom(6.0);
 assertEquals(6.0, fd.getDenominatorDegreesOfFreedom(), 0.0);
 }
}