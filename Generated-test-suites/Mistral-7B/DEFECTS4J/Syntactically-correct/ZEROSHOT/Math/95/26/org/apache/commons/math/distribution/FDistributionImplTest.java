import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.FDistributionImpl;
import org.apache.commons.math3.special.Beta;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class FDistributionImplTest {

 private FDistribution dist;

 @Test
 public void testFDistributionImpl() {
 dist = new FDistributionImpl(3.0, 5.0);
 }

 @Test
 public void testCumulativeProbability() throws MathException {
 double p = 0.5;
 double expected = 0.341940280467563;
 double result = dist.cumulativeProbability(dist.inverseCumulativeProbability(p));
 assertEquals(expected, result, 1e-8);
 }

 @Test
 public void testInverseCumulativeProbability() throws MathException {
 double p = 0.5;
 double expected = 1.5542056214494;
 double result = dist.inverseCumulativeProbability(p);
 assertEquals(expected, result, 1e-8);
 }

 @Test
 public void testSetNumeratorDegreesOfFreedom() {
 dist.setNumeratorDegreesOfFreedom(6.0);
 assertEquals(6.0, dist.getNumeratorDegreesOfFreedom(), 1e-12);
 }

 @Test
 public void testSetDenominatorDegreesOfFreedom() {
 dist.setDenominatorDegreesOfFreedom(8.0);
 assertEquals(8.0, dist.getDenominatorDegreesOfFreedom(), 1e-12);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeNumeratorDegreesOfFreedom() {
 dist.setNumeratorDegreesOfFreedom(-1.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeDenominatorDegreesOfFreedom() {
 dist.setDenominatorDegreesOfFreedom(-1.0);
 }

 @Test
 public void testSetBothDegreesOfFreedom() {
 dist.setNumeratorDegreesOfFreedom(2.0);
 dist.setDenominatorDegreesOfFreedom(4.0);
 assertEquals(2.0, dist.getNumeratorDegreesOfFreedom(), 1e-12);
 assertEquals(4.0, dist.getDenominatorDegreesOfFreedom(), 1e-12);
 }
}