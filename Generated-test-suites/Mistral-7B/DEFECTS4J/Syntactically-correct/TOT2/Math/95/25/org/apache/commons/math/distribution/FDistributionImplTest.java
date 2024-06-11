import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.FDistributionImpl;
import org.apache.commons.math3.special.Beta;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class FDistributionImplTest {

 private FDistributionImpl distribution;

 @Test
 public void testFDistributionImplConstructor() {
 distribution = new FDistributionImpl(2.0, 3.0);
 assertEquals(2.0, distribution.getNumeratorDegreesOfFreedom(), 0.0);
 assertEquals(3.0, distribution.getDenominatorDegreesOfFreedom(), 0.0);
 }

 @Test
 public void testCumulativeProbabilityZero() throws MathException {
 distribution = new FDistributionImpl(2.0, 3.0);
 assertEquals(0.0, distribution.cumulativeProbability(0.0), 0.0);
 }

 @Test
 public void testCumulativeProbabilityPositiveValue() throws MathException {
 distribution = new FDistributionImpl(2.0, 3.0);
 double x = 1.5;
 double p = distribution.cumulativeProbability(x);
 double expected = Beta.regularizedBeta((2.0 * x) / (3.0 + 2.0 * x), 1.0, 1.5);
 assertEquals(expected, p, 0.001);
 }

 @Test(expected = MathException.class)
 public void testCumulativeProbabilityNegativeValue() throws MathException {
 distribution = new FDistributionImpl(2.0, 3.0);
 double x = -1.0;
 distribution.cumulativeProbability(x);
 }

 @Test
 public void testInverseCumulativeProbabilityZero() throws MathException {
 distribution = new FDistributionImpl(2.0, 3.0);
 double p = 0.0;
 double x = distribution.inverseCumulativeProbability(p);
 assertEquals(0.0, x, 0.0);
 }

 @Test
 public void testInverseCumulativeProbabilityOne() throws MathException {
 distribution = new FDistributionImpl(2.0, 3.0);
 double p = 1.0;
 double x = distribution.inverseCumulativeProbability(p);
 assertEquals(Double.POSITIVE_INFINITY, x, Double.POSITIVE_INFINITY);
 }

 @Test
 public void testInverseCumulativeProbabilityValidProbability() throws MathException {
 distribution = new FDistributionImpl(2.0, 3.0);
 double p = 0.5;
 double x = distribution.inverseCumulativeProbability(p);
 double mean = distribution.getNumeratorDegreesOfFreedom() / (distribution.getNumeratorDegreesOfFreedom() - 2.0);
 assertEquals(mean, x, 0.001);
 }

 @Test(expected = MathException.class)
 public void testInverseCumulativeProbabilityInvalidProbability() throws MathException {
 distribution = new FDistributionImpl(2.0, 3.0);
 double p = -0.1;
 distribution.inverseCumulativeProbability(p);
 }

 @Test
 public void testSetNumeratorDegreesOfFreedom() {
 distribution = new FDistributionImpl(2.0, 3.0);
 distribution.setNumeratorDegreesOfFreedom(5.0);
 assertEquals(5.0, distribution.getNumeratorDegreesOfFreedom(), 0.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetNumeratorDegreesOfFreedomNegativeValue() {
 distribution = new FDistributionImpl(2.0, 3.0);
 distribution.setNumeratorDegreesOfFreedom(-1.0);
 }

 @Test
 public void testSetDenominatorDegreesOfFreedom() {
 distribution = new FDistributionImpl(2.0, 3.0);
 distribution.setDenominatorDegreesOfFreedom(5.0);
 assertEquals(3.0, distribution.getDenominatorDegreesOfFreedom(), 0.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetDenominatorDegreesOfFreedomNegativeValue() {
 distribution = new FDistributionImpl(2.0, 3.0);
 distribution.setDenominatorDegreesOfFreedom(-1.0);
 }
}