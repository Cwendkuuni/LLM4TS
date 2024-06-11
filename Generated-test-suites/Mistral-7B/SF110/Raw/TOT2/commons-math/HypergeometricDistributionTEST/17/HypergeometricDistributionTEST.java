package org.junit.jupiter.api;
import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.random.MersenneTwister;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HypergeometricDistributionTest {

@Test
public void testConstruction() {
HypergeometricDistribution dist = new HypergeometricDistribution(5, 3, 4);
assertEquals(5, dist.getPopulationSize());
assertEquals(3, dist.getNumberOfSuccesses());
assertEquals(4, dist.getSampleSize());
}

@Test
public void testConstructionWithRandomGenerator() {
int populationSize = 5;
int numberOfSuccesses = 3;
int sampleSize = 4;

MersenneTwister rng = new MersenneTwister();
HypergeometricDistribution dist = new HypergeometricDistribution(rng, populationSize, numberOfSuccesses, sampleSize);
assertEquals(populationSize, dist.getPopulationSize());
assertEquals(numberOfSuccesses, dist.getNumberOfSuccesses());
assertEquals(sampleSize, dist.getSampleSize());
}

@Test
public void testCumulativeProbability() {
HypergeometricDistribution dist = new HypergeometricDistribution(5, 3, 4);
double expectedCumulativeProbability = 0.6; // for example, x = 3
double actualCumulativeProbability = dist.cumulativeProbability(3);
assertEquals(expectedCumulativeProbability, actualCumulativeProbability, 0.0001);
}

@Test
public void testProbability() {
HypergeometricDistribution dist = new HypergeometricDistribution(5, 3, 4);
double expectedProbability = 0.3; // for example, x = 2
double actualProbability = dist.probability(2);
assertEquals(expectedProbability, actualProbability, 0.0001);
}

@Test
public void testGetNumberOfSuccesses() {
HypergeometricDistribution dist = new HypergeometricDistribution(5, 3, 4);
int expectedNumberOfSuccesses = 3;
int actualNumberOfSuccesses = dist.getNumberOfSuccesses();
assertEquals(expectedNumberOfSuccesses, actualNumberOfSuccesses);
}

@Test
public void testGetSampleSize() {
HypergeometricDistribution dist = new HypergeometricDistribution(5, 3, 4);
int expectedSampleSize = 4;
int actualSampleSize = dist.getSampleSize();
assertEquals(expectedSampleSize, actualSampleSize);
}

@Test(expected = NotStrictlyPositiveException.class)
public void testNegativeNumberOfSuccesses() {
HypergeometricDistribution dist = new HypergeometricDistribution(5, -3, 4);
}

@Test(expected = NotPositiveException.class)
public void testNegativePopulationSize() {
HypergeometricDistribution dist = new HypergeometricDistribution(-5, 3, 4);
}

@Test(expected = NotPositiveException.class)
public void testNegativeSampleSize() {
HypergeometricDistribution dist = new HypergeometricDistribution(5, 3, -4);
}

@Test(expected = NumberIsTooLargeException.class)
public void testNumberOfSuccessesLargerThanPopulationSize() {
HypergeometricDistribution dist = new HypergeometricDistribution(5, 6, 4);
}

@Test(expected = NumberIsTooLargeException.class)
public void testSampleSizeLargerThanPopulationSize() {
HypergeometricDistribution dist = new HypergeometricDistribution(5, 3, 6);
}

}