import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.FDistributionImpl;
import org.apache.commons.math3.special.Beta;
import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionImplTest {

 private FDistribution dist;

 @Test
 public void testFDistributionImplConstruction() {
  dist = new FDistributionImpl(1.0, 2.0);
  assertEquals(1.0, dist.getNumeratorDegreesOfFreedom(), 0.0);
  assertEquals(2.0, dist.getDenominatorDegreesOfFreedom(), 0.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFDistributionImplNegativeDegreesOfFreedom() {
  dist = new FDistributionImpl(-1.0, 2.0);
  fail("An IllegalArgumentException should have been thrown");
 }

 @Test
 public void testFDistributionImplCumulativeProbability() throws MathException {
  dist = new FDistributionImpl(1.0, 2.0);
  double p = 0.5;
  double expected = 0.3989323935; // value from MathWorld
  double result = dist.cumulativeProbability(p);
  assertEquals(expected, result, 1e-6);

  p = 0.95;
  expected = 0.9967765756;
  result = dist.cumulativeProbability(p);
  assertEquals(expected, result, 1e-6);
 }

 @Test(expected = MathException.class)
 public void testFDistributionImplInvalidCumulativeProbability() throws MathException {
  dist = new FDistributionImpl(1.0, 2.0);
  double p = -0.5;
  dist.cumulativeProbability(p);
  fail("A MathException should have been thrown");
 }

 @Test
 public void testFDistributionImplInverseCumulativeProbability() throws MathException {
  dist = new FDistributionImpl(1.0, 2.0);
  double p = 0.3;
  double expected = 1.255673738; // value from MathWorld
  double result = dist.inverseCumulativeProbability(p);
  assertEquals(expected, result, 1e-6);

  p = 0.9;
  expected = 1.845889416;
  result = dist.inverseCumulativeProbability(p);
  assertEquals(expected, result, 1e-6);
 }

 @Test(expected = MathException.class)
 public void testFDistributionImplInvalidInverseCumulativeProbability() throws MathException {
  dist = new FDistributionImpl(1.0, 2.0);
  double p = 1.1;
  dist.inverseCumulativeProbability(p);
  fail("A MathException should have been thrown");
 }

 @Test
 public void testFDistributionImplGetters() {
  dist = new FDistributionImpl(1.0, 2.0);
  assertEquals(1.0, dist.getNumeratorDegreesOfFreedom(), 0.0);
  assertEquals(2.0, dist.getDenominatorDegreesOfFreedom(), 0.0);
 }

 @Test
 public void testFDistributionImplSetters() {
  dist = new FDistributionImpl(1.0, 2.0);
  dist.setNumeratorDegreesOfFreedom(3.0);
  assertEquals(3.0, dist.getNumeratorDegreesOfFreedom(), 0.0);
  dist.setDenominatorDegreesOfFreedom(4.0);
  assertEquals(4.0, dist.getDenominatorDegreesOfFreedom(), 0.0);
 }
}