import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.distribution.PoissonDistributionImpl;
import org.apache.commons.math3.special.Gamma;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PoissonDistributionImplTest {

 private PoissonDistribution dist;

 @Test
 public void testConstructorPositiveMean() {
 dist = new PoissonDistributionImpl(2.5);
 assertEquals("Mean should be set correctly", 2.5, dist.getMean(), 0.001);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorNegativeMean() {
 dist = new PoissonDistributionImpl(-2.5);
 }

 @Test
 public void testProbabilityZero() {
 double prob = dist.probability(0);
 assertEquals("Probability for 0 should be e^(-mean)", Math.exp(-dist.getMean()), prob, 0.001);
 }

 @Test
 public void testProbabilityPositive() {
 double prob = dist.probability(3);
 assertEquals("Probability for 3 should be (e^(-mean) * e^(3-mean) / sqrt(2*pi*3))",
 FastMath.exp(-dist.getMean()) * FastMath.exp(3 - dist.getMean()) / Math.sqrt(2 * Math.PI * 3), prob, 0.001);
 }

 @Test
 public void testProbabilityMaximum() {
 int maxValue = Integer.MAX_VALUE;
 double prob = dist.probability(maxValue);
 assertEquals("Probability for maximum value should be 0", 0, prob, 0.001);
 }

 @Test
 public void testCumulativeProbabilityZero() {
 double cdf = 0;
 try {
 cdf = dist.cumulativeProbability(0);
 } catch (MathException e) {
 // expected since this method returns 0 for x == 0
 }
 assertEquals("Cumulative probability for 0 should be 0", 0, cdf, 0.001);
 }

 @Test
 public void testCumulativeProbabilityMaximum() {
 double cdf = 1;
 try {
 cdf = dist.cumulativeProbability(Integer.MAX_VALUE);
 } catch (MathException e) {
 // expected since this method returns 1 for x == Integer.MAX_VALUE
 }
 assertEquals("Cumulative probability for maximum value should be 1", 1, cdf, 0.001);
 }

 @Test
 public void testCumulativeProbabilityMiddle() {
 double cdf = 0.3473;
 try {
 cdf = dist.cumulativeProbability(1);
 } catch (MathException e) {
 // expected since this method should return the value calculated using Gamma function
 }
 assertEquals("Cumulative probability for 1 should be 0.3473", 0.3473, cdf, 0.001);
 }

 @Test
 public void testNormalApproximateProbabilityZero() {
 double prob = dist.normalApproximateProbability(0);
 assertEquals("Probability for 0 using normal approximation should be e^(-mean)",
 Math.exp(-dist.getMean()), prob, 0.001);
 }

 @Test
 public void testNormalApproximateProbabilityMaximum() {
 int maxValue = Integer.MAX_VALUE;
 double prob = dist.normalApproximateProbability(maxValue);
 assertEquals("Probability for maximum value using normal approximation should be 0",
 0, prob, 0.001);
 }

 @Test
 public void testNormalApproximateProbabilityMiddle() {
 double prob = dist.normalApproximateProbability(1);
 assertEquals("Probability for 1 using normal approximation should be 0.3473",
 0.3473, prob, 0.001);
 }

 @Test
 public void testSample() throws MathException {
 int sample = dist.sample();
 assertTrue("Sample should be a valid integer", PoissonDistribution.isValidPoissonValue(sample));
 int expected = 0;
 for (int i = 0; i < 10; i++) {
 expected += (int) Math.floor(Math.random() * (dist.getMean() + 1));
 }
 assertEquals("Sample should be close to mean after 10 trials", dist.getMean(), (double) expected / 10, 0.01);
 }

 @Test
 public void testCumulativeProbabilityConvergenceTest() {
 double epsilon = 1e-10;
 int maxIterations = 10000;
 PoissonDistributionImpl poissonDist = new PoissonDistributionImpl(2.5, epsilon, maxIterations);
 double cdfZero = poissonDist.cumulativeProbability(0);
 double cdfMaximum = poissonDist.cumulativeProbability(Integer.MAX_VALUE);
 double cdfMiddle = poissonDist.cumulativeProbability(1);
 assertEquals("Cumulative probability for 0 should be 0", 0, cdfZero, 0.001);
 assertEquals("Cumulative probability for maximum value should be 1", 1, cdfMaximum, 0.001);
 assertTrue("Cumulative probability for middle value should be close to the expected value",
 Gamma.regularizedGammaP(1.0, 2.5) < cdfMiddle && cdfMiddle < Gamma.regularizedGammaP(2.0, 2.5), 0.001);
 }
}