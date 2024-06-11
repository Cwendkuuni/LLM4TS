import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.FDistributionImpl;
import org.apache.commons.math3.special.Beta;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class FDistributionImplTest {

 private FDistributionImpl distribution;

 @Test
 public void testFDistributionImplConstruction() {
 distribution = new FDistributionImpl(2.0, 3.0);
 assertEquals(2.0, distribution.getNumeratorDegreesOfFreedom(), 0.001);
 assertEquals(3.0, distribution.getDenominatorDegreesOfFreedom(), 0.001);
 }

 @Test
 public void testCumulativeProbability() throws Exception {
 distribution = new FDistributionImpl(2.0, 3.0);
 // Check for zero
 assertEquals(0.0, distribution.cumulativeProbability(0.0), 0.001);
 // Check for small values
 double x = 0.1;
 assertEquals(x / (x + 2.0), distribution.cumulativeProbability(x), 0.01);
 // Check for larger values
 x = 10.0;
 assertEquals(1.0, distribution.cumulativeProbability(x), 0.01);
 }

 @Test
 public void testInverseCumulativeProbability() throws Exception {
 distribution = new FDistributionImpl(2.0, 3.0);
 // Test for p=0
 assertEquals(0.0, distribution.inverseCumulativeProbability(0.0), 0.001);
 // Test for p=1
 assertEquals(Double.POSITIVE_INFINITY, distribution.inverseCumulativeProbability(1.0), 0.001);
 // Test for other probabilities
 double p = 0.25;
 assertEquals(1.5 * Beta.REGULARIZED_BETA.value(1.5, 2.0, 3.0),
 distribution.inverseCumulativeProbability(p), 0.01);
 }
}