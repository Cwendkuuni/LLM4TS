import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Well19937c;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.Pair;
import org.junit.Test;
import static org.junit.Assert.*;

public class DiscreteDistributionTest {

 private static final T NULL_VALUE = null;
 private static final T VALUE_ONE = new Object() {};
 private static final T VALUE_TWO = new Object() {};
 private static final Double PROBABILITY_ONE = 0.5;
 private static final Double PROBABILITY_TWO = 0.3;
 private static final Double TOTAL_PROBABILITY = PROBABILITY_ONE + PROBABILITY_TWO;

 @Test
 public void testDiscreteDistributionConstructionWithValidArguments() throws Exception {
 List<Pair<T, Double>> samples = new ArrayList<>();
 samples.add(new Pair<>(VALUE_ONE, PROBABILITY_ONE));
 samples.add(new Pair<>(VALUE_TWO, PROBABILITY_TWO));
 DiscreteDistribution<T> discreteDistribution = new DiscreteDistribution<>(new Well19937c(), samples);
 assertEquals(TOTAL_PROBABILITY, discreteDistribution.probability(VALUE_ONE) + discreteDistribution.probability(VALUE_TWO), 0.001);
 }

 @Test(expected = NotPositiveException.class)
 public void testDiscreteDistributionConstructionWithNegativeProbability() {
 List<Pair<T, Double>> samples = new ArrayList<>();
 samples.add(new Pair<>(VALUE_ONE, -1.0));
 samples.add(new Pair<>(VALUE_TWO, PROBABILITY_TWO));
 try {
 new DiscreteDistribution<>(new Well19937c(), samples);
 fail("Expected NotPositiveException");
 } catch (NotPositiveException e) {
 // Expected
 }
 }

 @Test(expected = MathArithmeticException.class)
 public void testDiscreteDistributionConstructionWithZeroProbabilities() throws Exception {
 List<Pair<T, Double>> samples = new ArrayList<>();
 samples.add(new Pair<>(VALUE_ONE, 0.0));
 samples.add(new Pair<>(VALUE_TWO, 0.0));
 try {
 new DiscreteDistribution<>(new Well19937c(), samples);
 fail("Expected MathArithmeticException");
 } catch (MathArithmeticException e) {
 // Expected
 }
 }

 @Test(expected = MathIllegalArgumentException.class)
 public void testDiscreteDistributionConstructionWithInfiniteProbability() throws Exception {
 List<Pair<T, Double>> samples = new ArrayList<>();
 samples.add(new Pair<>(VALUE_ONE, Double.POSITIVE_INFINITY));
 samples.add(new Pair<>(VALUE_TWO, PROBABILITY_TWO));
 try {
 new DiscreteDistribution<>(new Well19937c(), samples);
 fail("Expected MathIllegalArgumentException");
 } catch (MathIllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testProbabilityMethodWithValidArguments() throws Exception {
 DiscreteDistribution<T> discreteDistribution = new DiscreteDistribution<>(new Well19937c(), samples);
 assertEquals(PROBABILITY_ONE, discreteDistribution.probability(VALUE_ONE), 0.001);
 assertEquals(PROBABILITY_TWO, discreteDistribution.probability(VALUE_TWO), 0.001);
 assertEquals(0.0, discreteDistribution.probability(NULL_VALUE), 0.001);
 }

 @Test
 public void testSampleMethodWithValidArguments() throws Exception {
 DiscreteDistribution<T> discreteDistribution = new DiscreteDistribution<>(new Well19937c(), samples);
 T sample = discreteDistribution.sample();
 assertEquals(VALUE_ONE, sample);
 }

 @Test
 public void testSampleMethodWithMultipleSamples() throws Exception {
 DiscreteDistribution<T> discreteDistribution = new DiscreteDistribution<>(new Well19937c(), samples);
 T[] samples = discreteDistribution.sample(5);
 int countOne = 0;
 int countTwo = 0;for (T value : samples) {
 if (value.equals(VALUE_ONE)) {
 countOne++;
 } else if (value.equals(VALUE_TWO)) {
 countTwo++;
 }
 }
 assertEquals(2, countOne);
 assertEquals(3, countTwo);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testSampleMethodWithNegativeSampleSize() throws Exception {
 DiscreteDistribution<T> discreteDistribution = new DiscreteDistribution<>(new Well19937c(), samples);
 try {
 discreteDistribution.sample(-1);
 fail("Expected NotStrictlyPositiveException");
 } catch (NotStrictlyPositiveException e) {
 // Expected
 }
 }

}