import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class HypergeometricDistributionTest {

private int populationSize;
private int numberOfSuccesses;
private int sampleSize;

public HypergeometricDistributionTest(int populationSize, int numberOfSuccesses, int sampleSize) {
this.populationSize = populationSize;
this.numberOfSuccesses = numberOfSuccesses;
this.sampleSize = sampleSize;
}

@Parameterized.Parameters(name = "populationSize={0}, numberOfSuccesses={1}, sampleSize={2}")
public static Iterable<Object[]> data() {
return new Object[][] {
{10, 2, 3},
{50, 30, 5},
{100, 50, 10},
{Integer.MAX_VALUE, 1, 1},
{Integer.MAX_VALUE, Integer.MAX_VALUE, 1},
{Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE}
};
}

@Test(expected = NotPositiveException.class)
public void testInvalidPopulationSize() {
try {
new HypergeometricDistribution(new Well19937c(), -1, 2, 3);
fail();
} catch (NotPositiveException e) {
// expected
}
}

@Test(expected = NotStrictlyPositiveException.class)
public void testInvalidNumberOfSuccesses() {
try {
new HypergeometricDistribution(new Well19937c(), 1, -1, 3);
fail();
} catch (NotStrictlyPositiveException e) {
// expected
}

try {
new HypergeometricDistribution(new Well19937c(), 1, 0, 3);
fail();
} catch (NotPositiveException e) {
// expected
}
}

@Test(expected = NotStrictlyPositiveException.class)
public void testInvalidSampleSize() {
try {
new HypergeometricDistribution(new Well19937c(), 10, 2, -1);
fail();
} catch (NotStrictlyPositiveException e) {
// expected
}
}

@Test(expected = NumberIsTooLargeException.class)
public void testNumberOfSuccessesTooLargeThanPopulationSize() {
try {
new HypergeometricDistribution(new Well19937c(), 5, 6, 5);
fail();
} catch (NumberIsTooLargeException e) {
// expected
}
}

@Test(expected = NumberIsTooLargeException.class)
public void testSampleSizeTooLargeThanPopulationSize() {
try {
new HypergeometricDistribution(new Well19937c(), 10, 2, 11);
fail();
} catch (NumberIsTooLargeException e) {
// expected
}
}

@Test
public void testProbability() {
HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), populationSize, numberOfSuccesses, sampleSize);
double expected = SaddlePointExpansion.logBinomialProbability(sampleSize, numberOfSuccesses, sampleSize / (double) populationSize, (populationSize - sampleSize) / (double) populationSize);
double actual = dist.probability(sampleSize - numberOfSuccesses);
assertEquals("Probability calculation is incorrect.", expected, actual, 0.001);
}

@Test
public void testCumulativeProbability() {
HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), populationSize, numberOfSuccesses, sampleSize);
double expected = 0;
for (int i = 0; i <= sampleSize - numberOfSuccesses; i++) {
expected += dist.probability(i);
}
double actual = dist.cumulativeProbability(sampleSize - numberOfSuccesses);
assertEquals("Cumulative probability calculation is incorrect.", expected, actual, 0.001);
}

@Test
public void testNumericalVariance() {
HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), populationSize, numberOfSuccesses, sampleSize);
double expected = dist.calculateNumericalVariance();
double actual = dist.getNumericalVariance();
assertEquals("Numerical variance calculation is incorrect.", expected, actual, 0.001);
}

@Test
public void testSupportBounds() {
HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), populationSize, numberOfSuccesses, sampleSize);
int lowerBound = dist.getSupportLowerBound();
int upperBound = dist.getSupportUpperBound();
assertTrue("Support bounds are incorrect.", lowerBound <= upperBound);
}

@Test
public void testIsSupportConnected() {
HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), populationSize, numberOfSuccesses, sampleSize);
assertTrue("Support is not connected.", dist.isSupportConnected());
}
}