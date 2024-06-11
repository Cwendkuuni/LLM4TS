// Test file for the HypergeometricDistribution class

package org.junit.Test;

import static org.junit.Assert.*;import org.apache.commons.math3.distribution.HypergeometricDistribution;

public class HypergeometricDistributionTest {

// Test constructor with valid arguments
@Test
public void testHypergeometricDistributionConstructorValidArguments() {
int populationSize = 10, numberOfSuccesses = 3, sampleSize = 5;HypergeometricDistribution hd = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);assertEquals(populationSize, hd.getPopulationSize());assertEquals(numberOfSuccesses, hd.getNumberOfSuccesses());assertEquals(sampleSize, hd.getSampleSize());
}

// Test getters
@Test
public void testGetters() {
int populationSize = 10, numberOfSuccesses = 3, sampleSize = 5;HypergeometricDistribution hd = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);assertEquals(numberOfSuccesses, hd.getNumberOfSuccesses());assertEquals(populationSize, hd.getPopulationSize());assertEquals(sampleSize, hd.getSampleSize());
}

// Test cumulativeProbability() method with valid argument
@Test
public void testCumulativeProbability() {
int populationSize = 10, numberOfSuccesses = 3, sampleSize = 5;HypergeometricDistribution hd = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);doubletestCumulativeProbability = hd.cumulativeProbability(2);double expectedCumulativeProbability = 0.381966; // calculated using the formula for Hypergeometric DistributionassertEquals(expectedCumulativeProbability, testCumulativeProbability, 0.0001);
}

// Test probability() method with valid argument
@Test
public void testProbability() {
int populationSize = 10, numberOfSuccesses = 3, sampleSize = 5;HypergeometricDistribution hd = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);doubletestProbability = hd.probability(1);double expectedProbability = 0.195312; // calculated using the formula for Hypergeometric DistributionassertEquals(expectedProbability, testProbability, 0.0001);
}

// Test cumulativeProbability() method with edge case
@Test
public void testCumulativeProbabilityEdgeCase() {
int populationSize = 10, numberOfSuccesses = 10, sampleSize = 5;HypergeometricDistribution hd = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);doubletestCumulativeProbabilityEdgeCase = hd.cumulativeProbability(numberOfSuccesses);double expectedCumulativeProbability = 1.0;assertEquals(expectedCumulativeProbability, testCumulativeProbabilityEdgeCase, 0.0001);
}

// Test constructor with numberOfSuccesses larger than population size
@Test(expected = org.apache.commons.math3.exception.NumberIsTooLargeException.class)
public void testConstructWithNumberOfSuccessesLargerThanPopulationSize() {
int populationSize = 10, numberOfSuccesses = 11, sampleSize = 5;new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
}

// Test constructor with population size zero
@Test(expected = org.apache.commons.math3.exception.NotStrictlyPositiveException.class)
public void testConstructWithPopulationSizeZero() {
int populationSize = 0, numberOfSuccesses = 3, sampleSize = 5;try {new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);fail();} catch (NotStrictlyPositiveException e) { // expected exception
}
}
}