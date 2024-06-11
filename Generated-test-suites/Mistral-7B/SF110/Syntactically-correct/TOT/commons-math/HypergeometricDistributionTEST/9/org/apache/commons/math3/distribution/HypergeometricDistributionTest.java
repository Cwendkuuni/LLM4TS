import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

 @Test
 public void testHypergeometricDistributionConstruction() {
 HypergeometricDistribution dist;
 // Positive construction
 dist = new HypergeometricDistribution(10, 3, 5);
 // Negative construction exception
 try {
 dist = new HypergeometricDistribution(-10, 3, 5);
 fail("Exception expected: NotStrictlyPositiveException");
 } catch (NotStrictlyPositiveException e) {
 // Expected exception
 }
 // Population size larger than sample size
 try {
 dist = new HypergeometricDistribution(10, 3, 11);
 fail("Exception expected: NumberIsTooLargeException");
 } catch (NumberIsTooLargeException e) {
 // Expected exception
 }
 // Number of successes larger than population size
 try {
 dist = new HypergeometricDistribution(10, 11, 5);
 fail("Exception expected: NumberIsTooLargeException");
 } catch (NumberIsTooLargeException e) {
 // Expected exception
 }
 // Negative sample size
 try {
 dist = new HypergeometricDistribution(10, 3, -5);
 fail("Exception expected: NotPositiveException");
 } catch (NotPositiveException e) {
 // Expected exception
 }
 // Successes larger than population size
 try {
 dist = new HypergeometricDistribution(10, 11, 5);
 fail("Exception expected: NotPositiveException");
 } catch (NotPositiveException e) {
 // Expected exception
 }
 }

 @Test
 public void testGetters() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 assertEquals(10, dist.getPopulationSize());
 assertEquals(3, dist.getNumberOfSuccesses());
 assertEquals(5, dist.getSampleSize());
 }

 @Test
 public void testCumulativeProbability() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 double[] expected = {0.0, 0.00392857142857, 0.02380952380952, 0.04520985505617, 0.06661267605634, 0.08801571428571, 0.10941874999995, 0.13082173525623, 0.15222471428571, 0.17362770112455, 0.19503068837204, 0.21643367548743, 0.23783666259281, 0.25923964970719, 0.28064263682157, 0.30204562403595, 0.32344861124933, 0.34485159846271, 0.366254585675, 0.38765757288737, 0.40906056010044, 0.43046354731351, 0.45186653452658, 0.47326952174065, 0.49467250895572, 0.51597549516079, 0.53727848136686, 0.55858146757393, 0.58018445388201, 0.60178743918909, 0.62339042449616, 0.64509340980324, 0.66679639411032, 0.6884993784174, 0.71020236272448, 0.73190534693156, 0.75360833113864, 0.77531131534572, 0.7970142995528, 0.81871728375988, 0.83941926796796, 0.86012125217604, 0.88082323638412, 0.90152422058219, 0.92222520478026, 0.94292618897833, 0.9636271731764, 0.98432815737447, 1.0};
 for (int i = 0; i < expected.length; i++) {
 double actual = dist.cumulativeProbability(i);
 assertEquals(expected[i], actual, 0.0001);
 }
 }

 @Test
 public void testProbability() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 double[] expected = {0.0, 0.004761904814814, 0.01389563403293, 0.02511289050133, 0.03843470126582, 0.05411287711861, 0.07227725288061, 0.09234301863861, 0.11472147441661, 0.13910653019461, 0.16579828597261, 0.19538217174961, 0.22785373752761, 0.2636112933056, 0.3030359690836, 0.3463634348616, 0.3937456406396, 0.4455616464176, 0.5019976922056, 0.5634963079936, 0.6303472337816, 0.7028311995706, 0.7821760543596, 0.8695124891486, 0.9657750839376, 1.0};
 for (int i = 0; i < expected.length; i++) {
 double actual = dist.probability(i);
 assertEquals(expected[i], actual, 0.0001);
 }
 }

 @Test
 public void testGetNumericalMean() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 double expected = 3.0;
 double actual = dist.getNumericalMean();
 assertEquals(expected, actual, 0.01);
 }

 @Test
 public void testGetNumericalVariance() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 double expected = 0.00148056689254;
 double actual = dist.getNumericalVariance();
 assertEquals(expected, actual, 0.00001);
 }

 @Test
 public void testSupportBounds() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 int expectedLowerBound = 2;
 int actualLowerBound = dist.getSupportLowerBound();
 assertEquals(expectedLowerBound, actualLowerBound);
 int expectedUpperBound = 6;
 int actualUpperBound = dist.getSupportUpperBound();
 assertEquals(expectedUpperBound, actualUpperBound);
 }

 @Test
 public void testIsSupportConnected() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 boolean expected = true;
 boolean actual = dist.isSupportConnected();
 assertEquals(expected, actual);
 }

 @Test
 public void testRandomSample() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 int sampleSize = 10000;
 int[] actual = new int[sampleSize];
 for (int i = 0; i < sampleSize; i++) {
 actual[i] = dist.nextInt();
 }
 int[] expected = new int[sampleSize];
 for (int i = 0; i < sampleSize; i++) {
 expected[i] = i % (populationSize + 1);
 }
 Arrays.sort(expected);
 Arrays.sort(actual);
 for (int i = 0; i < sampleSize; i++) {
 assertEquals(expected[i], actual[i]);
 }
 }
}