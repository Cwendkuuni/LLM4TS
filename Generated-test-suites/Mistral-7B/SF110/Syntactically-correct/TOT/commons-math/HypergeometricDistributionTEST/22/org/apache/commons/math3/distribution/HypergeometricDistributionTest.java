import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

 @Test
 public void testHypergeometricDistributionConstruction() {
 HypergeometricDistribution hd;

 hd = new HypergeometricDistribution(10, 5, 5); // valid parameters
 hd = new HypergeometricDistribution(new Well19937c(), 10, 5, 5); // valid parameters with seeded random

 // Invalid population size
 hd = new HypergeometricDistribution(new Well19937c(), -3, 5, 5);
 hd = new HypergeometricDistribution(10, -3, 5);
 hd = new HypergeometricDistribution(10, 5, -3);
 hd = new HypergeometricDistribution(new Well19937c(), 10, 5, -3);

 // Invalid number of successes
 hd = new HypergeometricDistribution(new Well19937c(), 10, -3, 5);
 hd = new HypergeometricDistribution(10, 5, 12);
 hd = new HypergeometricDistribution(new Well19937c(), 10, 5, 12);

 // Invalid sample size
 hd = new HypergeometricDistribution(new Well19937c(), 10, 5, -3);
 hd = new HypergeometricDistribution(10, 5, 12);
 hd = new HypergeometricDistribution(new Well19937c(), 10, 5, 12);

 // Number of successes > population size
 hd = new HypergeometricDistribution(new Well19937c(), 10, 11, 5);
 hd = new HypergeometricDistribution(10, 11, 5);
 hd = new HypergeometricDistribution(new Well19937c(), 10, 11, 5);
 }

 @Test
 public void testProbability() {
 HypergeometricDistribution hd = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 int x;

 x = 0;
 assertEquals(0.0, hd.probability(x), 0.01);

 x = 1;
 assertEquals(5.0 / 15.0, hd.probability(x), 0.01);

 x = 2;
 assertEquals(6.0 / 15.0, hd.probability(x), 0.01);

 x = 5;
 assertEquals(5.0 / 15.0, hd.probability(x), 0.01);

 x = 6;
 assertEquals(4.0 / 15.0, hd.probability(x), 0.01);
 }

 @Test
 public void testUpperCumulativeProbability() {
 HypergeometricDistribution hd = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 int x;

 x = 0;
 assertEquals(1.0, hd.upperCumulativeProbability(x), 0.01);

 x = 1;
 assertEquals(0.6667, hd.upperCumulativeProbability(x), 0.01);

 x = 2;
 assertEquals(0.8667, hd.upperCumulativeProbability(x), 0.01);

 x = 5;
 assertEquals(1.0, hd.upperCumulativeProbability(x), 0.01);

 x = 6;
 assertEquals(0.9333, hd.upperCumulativeProbability(x), 0.01);
 }

 @Test
 public void testCumulativeProbability() {
 HypergeometricDistribution hd = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 int x;

 x = 0;
 assertEquals(0.0, hd.cumulativeProbability(x), 0.01);

 x = 1;
 assertEquals(0.0667, hd.cumulativeProbability(x), 0.01);

 x = 2;
 assertEquals(0.1333, hd.cumulativeProbability(x), 0.01);

 x = 5;
 assertEquals(0.5, hd.cumulativeProbability(x), 0.01);

 x = 6;
 assertEquals(0.5333, hd.cumulativeProbability(x), 0.01);
 }
}