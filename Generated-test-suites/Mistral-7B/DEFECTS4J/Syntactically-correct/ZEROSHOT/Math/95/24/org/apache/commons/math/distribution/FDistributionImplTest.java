import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.FDistributionImpl;
import org.apache.commons.math3.special.Beta;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class FDistributionImplTest {

 @Test
 public void testFDistributionImpl() {
 FDistribution fd = new FDistributionImpl(1.0, 2.0);
 assertEquals(1.0, fd.getNumeratorDegreesOfFreedom(), 0.0);
 assertEquals(2.0, fd.getDenominatorDegreesOfFreedom(), 0.0);
 }

 @Test
 public void testCumulativeProbability() throws Exception {
 FDistribution fd = new FDistributionImpl(1.0, 2.0);
 for (double x = 0.0; x <= 5.0; x += 0.1) {
 double cp = fd.cumulativeProbability(x);
 System.out.println("x=" + x + ", cp=" + cp);
 }
 }

 @Test(expected = MathException.class)
 public void testNegativeNumeratorDegreesOfFreedom() {
 FDistribution fd = new FDistributionImpl(-1.0, 2.0);
 }

 @Test(expected = MathException.class)
 public void testNegativeDenominatorDegreesOfFreedom() {
 FDistribution fd = new FDistributionImpl(1.0, -2.0);
 }

 @Test
 public void testInverseCumulativeProbability() throws Exception {
 FDistribution fd = new FDistributionImpl(1.0, 2.0);
 for (double p = 0.0; p <= 1.0; p += 0.1) {
 double x = fd.inverseCumulativeProbability(p);
 System.out.println("p=" + p + ", x=" + x);
 }
 }

 @Test(expected = MathException.class)
 public void testInverseCumulativeProbabilityOutOfBounds() throws Exception {
 FDistribution fd = new FDistributionImpl(1.0, 2.0);
 fd.inverseCumulativeProbability(1.1);
 }

 @Test
 public void testSetNumeratorDegreesOfFreedom() {
 FDistribution fd = new FDistributionImpl(1.0, 2.0);
 fd.setNumeratorDegreesOfFreedom(5.0);
 assertEquals(5.0, fd.getNumeratorDegreesOfFreedom(), 0.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetNegativeNumeratorDegreesOfFreedom() {
 FDistribution fd = new FDistributionImpl(1.0, 2.0);
 fd.setNumeratorDegreesOfFreedom(-1.0);
 }

 @Test
 public void testSetDenominatorDegreesOfFreedom() {
 FDistribution fd = new FDistributionImpl(1.0, 2.0);
 fd.setDenominatorDegreesOfFreedom(5.0);
 assertEquals(5.0, fd.getDenominatorDegreesOfFreedom(), 0.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetNegativeDenominatorDegreesOfFreedom() {
 FDistribution fd = new FDistributionImpl(1.0, 2.0);
 fd.setDenominatorDegreesOfFreedom(-1.0);
 }
}