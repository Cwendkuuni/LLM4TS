import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.NormalDistributionImpl;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class NormalDistributionImplTest {

 /**
 * Test constructor with mean and standard deviation.
 */
 @Test
 public void testNormalDistributionImplWithMeanAndStdDeviation() {
 NormalDistributionImpl nd = new NormalDistributionImpl(1.2, 3.4);
 assertEquals(1.2, nd.getMean(), 0.001);
 assertEquals(3.4, nd.getStandardDeviation(), 0.001);
 }

 /**
 * Test constructor with mean equal to zero and standard deviation equal to one.
 */
 @Test
 public void testNormalDistributionImplDefaultConstructor() {
 NormalDistributionImpl nd = new NormalDistributionImpl();
 assertEquals(0.0, nd.getMean(), 0.001);
 assertEquals(1.0, nd.getStandardDeviation(), 0.001);
 }

 /**
 * Test getMean method.
 */
 @Test
 public void testGetMeanMethod() {
 NormalDistributionImpl nd = new NormalDistributionImpl();
 nd.setMean(3.14);
 assertEquals(3.14, nd.getMean(), 0.001);
 }

 /**
 * Test setMean method with negative value.
 */
 @Test(expected = IllegalArgumentException.class)
 public void testSetMeanMethodWithNegativeValue() {
 NormalDistributionImpl nd = new NormalDistributionImpl();
 nd.setMean(-1.2);
 }

 /**
 * Test setMean method with zero value.
 */
 @Test(expected = IllegalArgumentException.class)
 public void testSetMeanMethodWithZeroValue() {
 NormalDistributionImpl nd = new NormalDistributionImpl();
 nd.setMean(0.0);
 }

 /**
 * Test getStandardDeviation method.
 */
 @Test
 public void testGetStandardDeviationMethod() {
 NormalDistributionImpl nd = new NormalDistributionImpl();
 nd.setStandardDeviation(2.718);
 assertEquals(2.718, nd.getStandardDeviation(), 0.001);
 }

 /**
 * Test setStandardDeviation method with negative value.
 */
 @Test(expected = IllegalArgumentException.class)
 public void testSetStandardDeviationMethodWithNegativeValue() {
 NormalDistributionImpl nd = new NormalDistributionImpl();
 nd.setStandardDeviation(-1.2);
 }

 /**
 * Test setStandardDeviation method with zero value.
 */
 @Test(expected = IllegalArgumentException.class)
 public void testSetStandardDeviationMethodWithZeroValue() {
 NormalDistributionImpl nd = new NormalDistributionImpl();
 nd.setStandardDeviation(0.0);
 }

 /**
 * Test cumulativeProbability method with value less than mean.
 */
 @Test
 public void testCumulativeProbabilityMethodLessThanMean() {
 NormalDistributionImpl nd = new NormalDistributionImpl();
 double p = nd.cumulativeProbability(-3.0);
 assertTrue(p < 0.5);
 }

 /**
 * Test cumulativeProbability method with value equal to mean.
 */
 @Test
 public void testCumulativeProbabilityMethodEqualToMean() {
 NormalDistributionImpl nd = new NormalDistributionImpl();
 double p = nd.cumulativeProbability(0.0);
 assertEquals(0.5, p, 0.001);
 }

 /**
 * Test cumulativeProbability method with value greater than mean.
 */
 @Test
 public void testCumulativeProbabilityMethodGreaterThanMean() {
 NormalDistributionImpl nd = new NormalDistributionImpl();
 double p = nd.cumulativeProbability(2.0);
 assertTrue(p > 0.5);
 }

 /**
 * Test inverseCumulativeProbability method with probability less than zero.
 */
 @Test(expected = IllegalArgumentException.class)
 public void testInverseCumulativeProbabilityMethodLessThanZero() {
 NormalDistributionImpl nd = new NormalDistributionImpl();
 nd.inverseCumulativeProbability(-0.5);
 }

 /**
 * Test inverseCumulativeProbability method with probability greater than one.
 */
 @Test(expected = IllegalArgumentException.class)
 public void testInverseCumulativeProbabilityMethodGreaterThanOne() {
 NormalDistributionImpl nd = new NormalDistributionImpl();
 nd.inverseCumulativeProbability(1.5);
 }

 /**
 * Test inverseCumulativeProbability method with probability equal to zero.
 */
 @Test(expected = MathException.class)
 public void testInverseCumulativeProbabilityMethodZero() {
 NormalDistributionImpl nd = new NormalDistributionImpl();
 nd.inverseCumulativeProbability(0.0);
 }

 /**
 * Test inverseCumulativeProbability method with probability close to zero.
 */
 @Test
 public void testInverseCumulativeProbabilityMethodCloseToZero() {
 NormalDistributionImpl nd = new NormalDistributionImpl();
 double p = nd.inverseCumulativeProbability(0.01);
 assertEquals(-2.533568644757132, p, 0.001);
 }

 /**
 * Test inverseCumulativeProbability method with probability close to one.
 */
 @Test
 public void testInverseCumulativeProbabilityMethodCloseToOne() {
 NormalDistributionImpl nd = new NormalDistributionImpl();
 double p = nd.inverseCumulativeProbability(0.99);
 assertEquals(2.332254069515169, p, 0.001);
 }
}