import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.distribution.HypergeometricDistributionTest;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Assert;

public class HypergeometricDistributionTest {

 private static final int POPULATION_SIZE = 10;
 private static final int NUMBER_OF_SUCCESS = 5;
 private static final int SAMPLE_SIZE = 3;

 @org.junit.Test
 public void testHypergeometricDistribution_ValidConstruction() {
 try {
 HypergeometricDistribution hd = new HypergeometricDistribution(POPULATION_SIZE, NUMBER_OF_SUCCESS, SAMPLE_SIZE);
 } catch (NotPositiveException e) {
 Assert.fail("NotPositiveException should not be thrown during valid construction.");
 } catch (NotStrictlyPositiveException e) {
 Assert.fail("NotStrictlyPositiveException should not be thrown during valid construction.");
 } catch (NumberIsTooLargeException e) {
 Assert.fail("NumberIsTooLargeException should not be thrown during valid construction.");
 }
 }

 @org.junit.Test(expected = NotPositiveException.class)
 public void testHypergeometricDistribution_InvalidPopulationSize() {
 try {
 new HypergeometricDistribution(POPULATION_SIZE, NUMBER_OF_SUCCESS, SAMPLE_SIZE)
 .cumulativeProbability(-1);
 } catch (NotPositiveException e) {
 // expected
 }
 }

 @org.junit.Test(expected = NotStrictlyPositiveException.class)
 public void testHypergeometricDistribution_InvalidPopulationSize_Negative() {
 try {
 new HypergeometricDistribution(-POPULATION_SIZE, NUMBER_OF_SUCCESS, SAMPLE_SIZE);
 } catch (NotStrictlyPositiveException e) {
 // expected
 }
 }

 @org.junit.Test(expected = NotPositiveException.class)
 public void testHypergeometricDistribution_InvalidNumberOfSuccesses() {
 try {
 new HypergeometricDistribution(POPULATION_SIZE, NUMBER_OF_SUCCESS, SAMPLE_SIZE)
 .cumulativeProbability(-1);
 } catch (NotPositiveException e) {
 // expected
 }
 }

 @org.junit.Test(expected = NotStrictlyPositiveException.class)
 public void testHypergeometricDistribution_InvalidNumberOfSuccesses_Zero() {
 try {
 new HypergeometricDistribution(POPULATION_SIZE, 0, SAMPLE_SIZE);
 } catch (NotStrictlyPositiveException e) {
 // expected
 }
 }

 @org.junit.Test(expected = NotPositiveException.class)
 public void testHypergeometricDistribution_InvalidSampleSize() {
 try {
 new HypergeometricDistribution(POPULATION_SIZE, NUMBER_OF_SUCCESS, SAMPLE_SIZE)
 .cumulativeProbability(-1);
 } catch (NotPositiveException e) {
 // expected
 }
 }

 @org.junit.Test(expected = NumberIsTooLargeException.class)
 public void testHypergeometricDistribution_InvalidPopulationSize_NumberOfSuccessesGreaterThanPopulationSize() {
 try {
 new HypergeometricDistribution(POPULATION_SIZE, POPULATION_SIZE + 1, SAMPLE_SIZE);
 } catch (NumberIsTooLargeException e) {
 // expected
 }
 }

 @org.junit.Test(expected = NumberIsTooLargeException.class)
 public void testHypergeometricDistribution_InvalidSampleSize_GreaterThanPopulationSize() {
 try {
 new HypergeometricDistribution(POPULATION_SIZE, NUMBER_OF_SUCCESS, POPULATION_SIZE + 1);
 } catch (NumberIsTooLargeException e) {
 // expected
 }
 }

 @org.junit.Test
 public void testHypergeometricDistribution_CumulativeProbability() {
 HypergeometricDistribution hd = new HypergeometricDistribution(POPULATION_SIZE, NUMBER_OF_SUCCESS, SAMPLE_SIZE);
 int[] testCases = {0,NUMBER_OF_SUCCESS, POPULATION_SIZE};
 for (int i : testCases) {
 double expected = 0;
 if (i >= 0 && i <= NUMBER_OF_SUCCESS) {
 expected = FastMath.comb(NUMBER_OF_SUCCESS, i) * FastMath.pow(FastMath.doubleDivide(SAMPLE_SIZE, POPULATION_SIZE), i) * FastMath.pow(FastMath.subtract(1, FastMath.doubleDivide(SAMPLE_SIZE, POPULATION_SIZE)), POPULATION_SIZE - i);
 }
 double actual = hd.cumulativeProbability(i);
 Assert.assertEquals("Unexpected cumulative probability value for test case: " + i, expected, actual, 1e-13);
 }
 }

 @org.junit.Test
 public void testHypergeometricDistribution_Probability() {
 HypergeometricDistribution hd = new HypergeometricDistribution(POPULATION_SIZE, NUMBER_OF_SUCCESS, SAMPLE_SIZE);
 int[] testCases = {0,1,2,NUMBER_OF_SUCCESS, POPULATION_SIZE};
 for (int i : testCases) {
 double expected = 0;
 if (i >= 0 && i <= NUMBER_OF_SUCCESS) {
 expected = FastMath.comb(NUMBER_OF_SUCCESS, i) * FastMath.pow(FastMath.doubleDivide(SAMPLE_SIZE, POPULATION_SIZE), i) * FastMath.pow(FastMath.subtract(1, FastMath.doubleDivide(SAMPLE_SIZE, POPULATION_SIZE)), POPULATION_SIZE - i);
 } else if (i >= NUMBER_OF_SUCCESS && i <= POPULATION_SIZE) {
 expected = FastMath.comb(POPULATION_SIZE - NUMBER_OF_SUCCESS, POPULATION_SIZE - i) * FastMath.pow(FastMath.subtract(1, FastMath.doubleDivide(NUMBER_OF_SUCCESS, POPULATION_SIZE)), NUMBER_OF_SUCCESS - i) * FastMath.pow(FastMath.doubleDivide(SAMPLE_SIZE, POPULATION_SIZE), i);
 }
 double actual = hd.probability(i);
 Assert.assertEquals("Unexpected probability value for test case: " + i, expected, actual, 1e-13);
 }
 }

 @org.junit.Test
 public void testHypergeometricDistribution_UpperCumulativeProbability() {
 HypergeometricDistribution hd = new HypergeometricDistribution(POPULATION_SIZE, NUMBER_OF_SUCCESS, SAMPLE_SIZE);
 int[] testCases = {NUMBER_OF_SUCCESS + 1, POPULATION_SIZE + 1};
 for (int i : testCases) {
 double expected = 1;
 double actual = hd.upperCumulativeProbability(i);
 Assert.assertEquals("Unexpected upper cumulative probability value for test case: " + i, expected, actual, 1e-13);
 }
 }

 @org.junit.Test
 public void testHypergeometricDistribution_NumericalMean() {
 HypergeometricDistribution hd = new HypergeometricDistribution(POPULATION_SIZE, NUMBER_OF_SUCCESS, SAMPLE_SIZE);
 double expected = FastMath.doubleDivide(SAMPLE_SIZE, POPULATION_SIZE) * NUMBER_OF_SUCCESS;
 double actual = hd.getNumericalMean();
 Assert.assertEquals("Unexpected numerical mean value.", expected, actual, 1e-13);
 }

 @org.junit.Test
 public void testHypergeometricDistribution_NumericalVariance() {
 HypergeometricDistribution hd = new HypergeometricDistribution(POPULATION_SIZE, NUMBER_OF_SUCCESS, SAMPLE_SIZE);
 double expected = FastMath.multiply(SAMPLE_SIZE, NUMBER_OF_SUCCESS) * FastMath.multiply(POPULATION_SIZE - SAMPLE_SIZE, POPULATION_SIZE - NUMBER_OF_SUCCESS) / FastMath.multiply(FastMath.pow(POPULATION_SIZE, 2), FastMath.subtract(1, 1));
 double actual = hd.getNumericalVariance();
 Assert.assertEquals("Unexpected numerical variance value.", expected, actual, 1e-13);
 }

 @org.junit.Test
 public void testHypergeometricDistribution_RandomGeneration() {
 HypergeometricDistribution hd = new HypergeometricDistribution(POPULATION_SIZE, NUMBER_OF_SUCCESS, SAMPLE_SIZE);
 RandomGenerator rg = new Well19937c();
 int samplesCount = 10000;
 int[] testCases = new int[samplesCount];
 for (int i = 0; i < samplesCount; i++) {
 testCases[i] = hd.sample(rg);
 }
 int numberOfSuccessesInSamples = 0;
 for (int i : testCases) {
 numberOfSuccessesInSamples += i;
 }
 double expected = FastMath.doubleDivide(samplesCount, POPULATION_SIZE) * NUMBER_OF_SUCCESS;
 double actual = (double) numberOfSuccessesInSamples / samplesCount;
 Assert.assertEquals("Unexpected number of successes in samples.", expected, actual, 1e-13);
 }
}