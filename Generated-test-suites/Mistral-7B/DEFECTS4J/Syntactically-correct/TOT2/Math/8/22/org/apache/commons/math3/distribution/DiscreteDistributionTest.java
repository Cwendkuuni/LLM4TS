import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Well19937c;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.Pair;
import org.junit.Test;
import static org.junit.Assert.*;

public class DiscreteDistributionTest {

 private List<Pair<Object, Double>> samples = new ArrayList<>();
 private DiscreteDistribution<Object> distribution;

 @Test
 public void testConstructorWithValidSamples() throws NotPositiveException, NotStrictlyPositiveException, MathArithmeticException {
 samples.add(new Pair<>(null, 0.3));
 samples.add(new Pair<>(1L, 0.4));
 samples.add(new Pair<>(2L, 0.3));
 distribution = new DiscreteDistribution<>(new Well19937c(), samples);
 assertEquals(3, distribution.singletons.size());
 }

 @Test(expected = NotPositiveException.class)
 public void testConstructorWithNegativeProbability() {
 samples.clear();
 samples.add(new Pair<>(null, -0.3));
 samples.add(new Pair<>(1L, 0.4));
 samples.add(new Pair<>(2L, 0.3));
 try {
 distribution = new DiscreteDistribution<>(new Well19937c(), samples);
 } catch (NotPositiveException e) {
 // expected
 }
 }

 @Test(expected = MathArithmeticException.class)
 public void testConstructorWithZeroProbabilities() {
 samples.clear();
 samples.add(new Pair<>(null, 0.3));
 samples.add(new Pair<>(1L, 0.0));
 samples.add(new Pair<>(2L, 0.3));
 try {
 distribution = new DiscreteDistribution<>(new Well19937c(), samples);
 } catch (MathArithmeticException e) {
 // expected
 }
 }

 @Test
 public void testProbabilityMethod() {
 distribution = new DiscreteDistribution<>(new Well19937c(), samples);
 assertEquals(0.3, distribution.probability(null), 0.001);
 assertEquals(0.4, distribution.probability(1L), 0.001);
 assertEquals(0.3, distribution.probability(2L), 0.001);
 }

 @Test
 public void testGetSamplesMethod() {
 distribution = new DiscreteDistribution<>(new Well19937c(), samples);
 List<Pair<Object, Double>> result = distribution.getSamples();
 assertEquals(samples.size(), result.size());
 for (int i = 0; i < samples.size(); i++) {
 assertEquals(samples.get(i).getKey(), result.get(i).getKey());
 assertEquals(samples.get(i).getValue(), result.get(i).getValue(), 0.001);
 }
 }

 @Test
 public void testSampleMethod() {
 distribution = new DiscreteDistribution<>(new Well19937c(), samples);
 assertEquals(samples.get(0).getKey(), distribution.sample());
 }

 @Test
 public void testSampleMethodWithMultipleSamples() throws NotStrictlyPositiveException {
 distribution = new DiscreteDistribution<>(new Well19937c(), samples);
 Object[] result = distribution.sample(5);
 assertEquals(5, result.length);
 for (Object sample : result) {
 assertEquals(true, samples.contains(new Pair<>(sample, null)));
 }
 }

 @Test
 public void testReseedRandomGeneratorMethod() {
 distribution = new DiscreteDistribution<>(new Well19937c(), samples);
 long seed = 12345;
 distribution.reseedRandomGenerator(seed);
 // Cannot test the result directly since it depends on the random number generator seed
 }
}