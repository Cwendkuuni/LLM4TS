import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.FDistType;
import org.apache.commons.math3.special.Beta;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class FDistributionImplTest {

 @Test
 public void testFDistributionImpl() {
 FDistributionImpl distribution = new FDistributionImpl(2.0, 3.0);
 assertEquals(FDistType.F, distribution.getType());
 assertEquals(2.0, distribution.getNumeratorDegreesOfFreedom(), 0.0);
 assertEquals(3.0, distribution.getDenominatorDegreesOfFreedom(), 0.0);
 }

 @Test
 public void testCumulativeProbability() throws Exception {
 FDistributionImpl distribution = new FDistributionImpl(2.0, 3.0);
 // Test for x = 0
 assertEquals(0.0, distribution.cumulativeProbability(0.0), 0.001);
 // Test for x = mean
 double mean = 3.0 / (3.0 - 2.0);
 assertEquals(0.5, distribution.cumulativeProbability(mean), 0.001);
 // Test for x > mean
 double x = mean + 1.0;
 assertEquals(1.0, distribution.cumulativeProbability(x), 0.001);
 }

 @Test(expected = MathException.class)
 public void testCumulativeProbabilityNegativeX() throws Exception {
 FDistributionImpl distribution = new FDistributionImpl(2.0, 3.0);
 distribution.cumulativeProbability(-1.0);
 }

 @Test
 public void testInverseCumulativeProbability() throws Exception {
 FDistributionImpl distribution = new FDistributionImpl(2.0, 3.0);
 // Test for p = 0
 assertEquals(0.0, distribution.inverseCumulativeProbability(0.0), 0.001);
 // Test for p = 0.5
 double p = 0.5;
 assertEquals(3.0 / (3.0 - 2.0), distribution.inverseCumulativeProbability(p), 0.001);
 // Test for p = 1
 assertEquals(Double.POSITIVE_INFINITY, distribution.inverseCumulativeProbability(1.0), 0.0);
 }

 @Test(expected = MathException.class)
 public void testInverseCumulativeProbabilityOutOfRange() throws Exception {
 FDistributionImpl distribution = new FDistributionImpl(2.0, 3.0);
 distribution.inverseCumulativeProbability(0.51);
 }

 @Test
 public void testSetNumeratorDegreesOfFreedom() {
 FDistributionImpl distribution = new FDistributionImpl(2.0, 3.0);
 distribution.setNumeratorDegreesOfFreedom(4.0);
 assertEquals(4.0, distribution.getNumeratorDegreesOfFreedom(), 0.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetNumeratorDegreesOfFreedomNegative() {
 FDistributionImpl distribution = new FDistributionImpl(2.0, 3.0);
 distribution.setNumeratorDegreesOfFreedom(-1.0);
 }

 @Test
 public void testSetDenominatorDegreesOfFreedom() {
 FDistributionImpl distribution = new FDistributionImpl(2.0, 3.0);
 distribution.setDenominatorDegreesOfFreedom(4.0);
 assertEquals(3.0, distribution.getDenominatorDegreesOfFreedom(), 0.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetDenominatorDegreesOfFreedomNegative() {
 FDistributionImpl distribution = new FDistributionImpl(2.0, 3.0);
 distribution.setDenominatorDegreesOfFreedom(-1.0);
 }

}