import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.NormalDistributionImpl;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {

 @Test
 public void testNormalDistributionImplConstruction() {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
 NormalDistributionImpl normalDistributionWithMeanAndStddev = new NormalDistributionImpl(1.0, 2.0);

 assertEquals(0.0, normalDistribution.getMean(), 0.01);
 assertEquals(1.0, normalDistribution.getStandardDeviation(), 0.01);
 assertEquals(1.0, normalDistributionWithMeanAndStddev.getMean(), 0.01);
 assertEquals(2.0, normalDistributionWithMeanAndStddev.getStandardDeviation(), 0.01);
 }

 @Test
 public void testNormalDistributionImplMeanSetter() {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
 normalDistribution.setMean(3.0);
 assertEquals(3.0, normalDistribution.getMean(), 0.01);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNormalDistributionImplMeanNegativeSetter() {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
 normalDistribution.setMean(-1.0);
 }

 @Test
 public void testNormalDistributionImplStandardDeviationSetter() {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
 normalDistribution.setStandardDeviation(4.0);
 assertEquals(4.0, normalDistribution.getStandardDeviation(), 0.01);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNormalDistributionImplStandardDeviationZeroSetter() {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
 normalDistribution.setStandardDeviation(0.0);
 }

 @Test
 public void testNormalDistributionImplCumulativeProbability() throws MathException {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl();

 double x = 0.0;
 double p = normalDistribution.cumulativeProbability(x);
 assertEquals(0.5, p, 0.01);

 x = 1.0;
 p = normalDistribution.cumulativeProbability(x);
 assertEquals(0.841345, p, 0.01);

 x = 2.0;
 p = normalDistribution.cumulativeProbability(x);
 assertEquals(0.977241, p, 0.01);

 x = 3.0;
 p = normalDistribution.cumulativeProbability(x);
 assertEquals(0.997343, p, 0.01);
 }

 @Test(expected = MathException.class)
 public void testNormalDistributionImplCumulativeProbabilityOutOfRange() throws MathException {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
 normalDistribution.cumulativeProbability(Double.NEGATIVE_INFINITY);
 }

 @Test
 public void testNormalDistributionImplInverseCumulativeProbability() throws MathException {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl();

 double p = 0.1;
 double x = normalDistribution.inverseCumulativeProbability(p);
 assertEquals(-1.281552, x, 0.01);

 p = 0.5;
 x = normalDistribution.inverseCumulativeProbability(p);
 assertEquals(0.0, x, 0.01);

 p = 0.9;
 x = normalDistribution.inverseCumulativeProbability(p);
 assertEquals(1.281552, x, 0.01);
 }

 @Test(expected = MathException.class)
 public void testNormalDistributionImplInverseCumulativeProbabilityOutOfRange() throws MathException {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
 normalDistribution.inverseCumulativeProbability(Double.NEGATIVE_INFINITY);
 normalDistribution.inverseCumulativeProbability(Double.POSITIVE_INFINITY);
 }

 @Test
 public void testNormalDistributionImplInverseCumulativeProbabilityConvergence() throws MathException {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl();

 double p = 0.999999999;
 double x = normalDistribution.inverseCumulativeProbability(p);
 assertEquals(6.344938, x, 0.01);
 }
}