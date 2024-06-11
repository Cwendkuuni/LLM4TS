import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Well19937c;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.util.Pair;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DiscreteDistributionTest {

 private List<Pair<Integer, Double>> intList = new ArrayList<>();
 private List<Pair<Double, Double>> doubleList = new ArrayList<>();

 @Test
 public void testDiscreteDistributionInt() {
 RandomGenerator rng = new Well19937c();
 List<Pair<Integer, Double>> samples = new ArrayList<>();
 samples.add(new Pair<>(1, 0.2));
 samples.add(new Pair<>(2, 0.3));
 samples.add(new Pair<>(3, 0.5));

 try {
 DiscreteDistribution distribution = new DiscreteDistribution<>(rng, samples);
 DiscreteDistribution testedDistribution = new DiscreteDistribution<>(rng, samples);

 assertEquals("test probabilities", 0.2, distribution.probability(1), 0.001);
 assertEquals("test probabilities", 0.3, distribution.probability(2), 0.001);
 assertEquals("test probabilities", 0.5, distribution.probability(3), 0.001);

 assertEquals("test getSamples", samples, distribution.getSamples());

 Integer sample = distribution.sample();
 assertEquals("test sample", 1, sample.intValue()); // 20% chance of this value

 int[] samplesArray = distribution.sample(5);
 for (int sampleValue : samplesArray) {
 assertNotNull("test samplesArray", sampleValue);
 }
 } catch (Exception e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testDiscreteDistributionDouble() {
 RandomGenerator rng = new Well19937c();
 List<Pair<Double, Double>> samples = new ArrayList<>();
 samples.add(new Pair<>(2.5, 0.2));
 samples.add(new Pair<>(3.5, 0.3));
 samples.add(new Pair<>(4.5, 0.5));

 try {
 DiscreteDistribution distribution = new DiscreteDistribution<>(rng, samples);
 DiscreteDistribution testedDistribution = new DiscreteDistribution<>(rng, samples);

 assertEquals("test probabilities", 0.2, distribution.probability(2.5), 0.001);
 assertEquals("test probabilities", 0.3, distribution.probability(3.5), 0.001);
 assertEquals("test probabilities", 0.5, distribution.probability(4.5), 0.001);

 assertEquals("test getSamples", samples, distribution.getSamples());

 Double sample = distribution.sample();
 assertEquals("test sample", 2.5, sample.doubleValue(), 0.001); // 20% chance of this value

 double[] samplesArray = new double[5];
 for (int i = 0; i < samplesArray.length; i++) {
 samplesArray[i] = distribution.sample();
 }
 for (double sampleValue : samplesArray) {
 assertNotNull("test samplesArray", sampleValue);
 }
 } catch (Exception e) {
 e.printStackTrace();
 }
 }

 @Test(expected = NotPositiveException.class)
 public void testNegativeProbability() {
 RandomGenerator rng = new Well19937c();
 List<Pair<Integer, Double>> samples = new ArrayList<>();
 samples.add(new Pair<>(1, -0.1));
 try {
 new DiscreteDistribution<>(rng, samples);
 } catch (Exception e) {
 assertNotNull("test exception", e);
 }
 }

 @Test(expected = MathArithmeticException.class)
 public void testSumProbabilitiesZero() {
 RandomGenerator rng = new Well19937c();
 List<Pair<Integer, Double>> samples = new ArrayList<>();
 samples.add(new Pair<>(1, 0.1));
 samples.add(new Pair<>(2, 0.1));
 samples.add(new Pair<>(3, -0.1));
 try {
 new DiscreteDistribution<>(rng, samples);
 } catch (Exception e) {
 assertNotNull("test exception", e);
 }
 }

 @Test(expected = MathIllegalArgumentException.class)
 public void testInfiniteProbability() {
 RandomGenerator rng = new Well19937c();
 List<Pair<Integer, Double>> samples = new ArrayList<>();
 samples.add(new Pair<>(1, Double.POSITIVE_INFINITY));
 try {
 new DiscreteDistribution<>(rng, samples);
 } catch (Exception e) {
 assertNotNull("test exception", e);
 }
 }
}