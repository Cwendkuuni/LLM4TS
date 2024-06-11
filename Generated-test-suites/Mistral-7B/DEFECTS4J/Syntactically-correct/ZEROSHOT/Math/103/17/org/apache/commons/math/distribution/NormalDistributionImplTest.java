import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.NormalDistributionImpl;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {

 @Test
 public void testNormalDistributionImplConstruction() {
 NormalDistributionImpl nd = new NormalDistributionImpl();
 assertEquals(0.0, nd.getMean(), 0.0);
 assertEquals(1.0, nd.getStandardDeviation(), 0.0);

 NormalDistributionImpl nd2 = new NormalDistributionImpl(2.5, 2.0);
 assertEquals(2.5, nd2.getMean(), 0.0);
 assertEquals(2.0, nd2.getStandardDeviation(), 0.0);
 }

 @Test
 public void testNormalDistributionImplMeanSetter() {
 NormalDistributionImpl nd = new NormalDistributionImpl();
 nd.setMean(3.5);
 assertEquals(3.5, nd.getMean(), 0.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNormalDistributionImplMeanNegative() {
 NormalDistributionImpl nd = new NormalDistributionImpl();
 nd.setMean(-3.5);
 }

 @Test
 public void testNormalDistributionImplStandardDeviationSetter() {
 NormalDistributionImpl nd = new NormalDistributionImpl();
 nd.setStandardDeviation(3.5);
 assertEquals(3.5, nd.getStandardDeviation(), 0.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNormalDistributionImplStandardDeviationZero() {
 NormalDistributionImpl nd = new NormalDistributionImpl();
 nd.setStandardDeviation(0.0);
 }

 @Test
 public void testNormalDistributionImplCumulativeProbability() throws MathException {
 NormalDistributionImpl nd = new NormalDistributionImpl();

 double p = 0.1;
 double result = nd.cumulativeProbability(0.5);
 assertEquals(p, result, 0.01);

 p = 0.9999;
 result = nd.cumulativeProbability(3.0);
 assertEquals(p, result, 0.01);

 p = 0.5;
 result = nd.cumulativeProbability(1.5);
 assertEquals(p, result, 0.01);
 }

 @Test(expected = MathException.class)
 public void testNormalDistributionImplCumulativeProbabilityTooFarFromMean() throws MathException {
 NormalDistributionImpl nd = new NormalDistributionImpl();
 nd.cumulativeProbability(100.0);
 }

 @Test
 public void testNormalDistributionImplInverseCumulativeProbability() throws MathException {
 NormalDistributionImpl nd = new NormalDistributionImpl();

 double p = 0.1;
 double result = nd.inverseCumulativeProbability(nd.cumulativeProbability(0.5));
 assertEquals(-0.5, result, 0.01);

 p = 0.9999;
 result = nd.inverseCumulativeProbability(nd.cumulativeProbability(3.0));
 assertEquals(3.0, result, 0.01);

 p = 0.5;
 result = nd.inverseCumulativeProbability(nd.cumulativeProbability(1.5));
 assertEquals(1.5, result, 0.01);
 }

 @Test(expected = MathException.class)
 public void testNormalDistributionImplInverseCumulativeProbabilityNotAProbability() throws MathException {
 NormalDistributionImpl nd = new NormalDistributionImpl();
 nd.inverseCumulativeProbability(1.51);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNormalDistributionImplInverseCumulativeProbabilityInvalidProbability() throws MathException {
 NormalDistributionImpl nd = new NormalDistributionImpl();
 nd.inverseCumulativeProbability(-0.1);
 }

}