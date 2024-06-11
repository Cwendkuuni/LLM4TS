import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.FDistributionImpl;
import org.apache.commons.math3.special.Beta;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class FDistributionImplTest {

 @Test
 public void testFDistributionImplCreation() {
 FDistribution fd = new FDistributionImpl(2.0, 3.0);
 assertEquals(2.0, fd.getNumeratorDegreesOfFreedom(), 0.0);
 assertEquals(3.0, fd.getDenominatorDegreesOfFreedom(), 0.0);
 }

 @Test
 public void testFDistributionImplCumulativeProbability() throws Exception {
 FDistribution fd = new FDistributionImpl(2.0, 3.0);
 for (double x = 0.0; x <= 5.0; x += 0.1) {
 double cdf = fd.cumulativeProbability(x);
 System.out.printf("%.2f : %.8f%n", x, cdf);
 }
 }

 @Test
 public void testFDistributionImplInverseCumulativeProbability() throws Exception {
 FDistribution fd = new FDistributionImpl(2.0, 3.0);

 // Test impossible inputs
 assertThrows(IllegalArgumentException.class, () -> fd.inverseCumulativeProbability(-1.0));
 assertThrows(IllegalArgumentException.class, () -> fd.inverseCumulativeProbability(1.0));

 // Test valid inputs
 for (double p = 0.0; p <= 1.0; p += 0.01) {
 double x = fd.inverseCumulativeProbability(p);
 System.out.printf("%.4f : %.8f%n", p, x);
 }
 }

 @Test
 public void testFDistributionImplSetNumeratorDegreesOfFreedom() {
 FDistributionImpl fd = new FDistributionImpl(2.0, 3.0);
 fd.setNumeratorDegreesOfFreedom(5.0);
 assertEquals(5.0, fd.getNumeratorDegreesOfFreedom(), 0.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFDistributionImplInvalidSetNumeratorDegreesOfFreedom() {
 FDistributionImpl fd = new FDistributionImpl(2.0, 3.0);
 fd.setNumeratorDegreesOfFreedom(-1.0);
 }

 @Test
 public void testFDistributionImplSetDenominatorDegreesOfFreedom() {
 FDistributionImpl fd = new FDistributionImpl(2.0, 3.0);
 fd.setDenominatorDegreesOfFreedom(5.0);
 assertEquals(5.0, fd.getDenominatorDegreesOfFreedom(), 0.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFDistributionImplInvalidSetDenominatorDegreesOfFreedom() {
 FDistributionImpl fd = new FDistributionImpl(2.0, 3.0);
 fd.setDenominatorDegreesOfFreedom(-1.0);
 }
}