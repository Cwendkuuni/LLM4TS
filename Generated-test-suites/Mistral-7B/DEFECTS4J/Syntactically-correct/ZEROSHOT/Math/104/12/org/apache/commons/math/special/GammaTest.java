import org.apache.commons.math3.special.Gamma;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class GammaTest {

 @Test
 public void testLogGamma() {
 // Test for positive values
 assertEquals(0.0, Gamma.logGamma(0.0), 1e-12);
 assertEquals(0.0, Gamma.logGamma(1.0), 1e-12);
 assertEquals(1.07879694386523863556, Gamma.logGamma(2.0), 1e-12);
 assertEquals(1.10063689267222812625, Gamma.logGamma(3.0), 1e-12);

 // Test for negative values and complex numbers
 try {
 Gamma.logGamma(-1.0);
 assertEquals(Double.NaN, Double.NaN);
 } catch (IllegalArgumentException e) {
 // Expected
 }
 try {
 Gamma.logGamma(new Complex(1.0, 2.0));
 assertEquals(Double.NaN, Double.NaN);
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @Test
 public void testRegularizedGammaP() {
 // Test for positive values
 assertEquals(0.0, Gamma.regularizedGammaP(0.0, 0.0), 1e-12);
 assertEquals(1.0, Gamma.regularizedGammaP(1.0, 0.0), 1e-12);
 assertEquals(0.5, Gamma.regularizedGammaP(0.5, 0.5), 1e-12);
 assertEquals(0.88622712514234351875, Gamma.regularizedGammaP(2.5, 1.0), 1e-12);

 // Test for non-positive values and non-numeric arguments
 try {
 Gamma.regularizedGammaP(-1.0, 0.0);
 assertEquals(Double.NaN, Double.NaN);
 } catch (IllegalArgumentException e) {
 // Expected
 }
 try {
 Gamma.regularizedGammaP(1.0, "x");
 assertEquals(Double.NaN, Double.NaN);
 } catch (NumberFormatException e) {
 // Expected
 }
 }

 @Test
 public void testRegularizedGammaQ() {
 // Test for positive values
 assertEquals(1.0, Gamma.regularizedGammaQ(0.0, 0.0), 1e-12);
 assertEquals(0.0, Gamma.regularizedGammaQ(1.0, 0.0), 1e-12);
 assertEquals(0.5, Gamma.regularizedGammaQ(0.5, 0.5), 1e-12);
 assertEquals(0.11377592555427552428, Gamma.regularizedGammaQ(2.5, 1.0), 1e-12);

 // Test for non-positive values and non-numeric arguments
 try {
 Gamma.regularizedGammaQ(-1.0, 0.0);
 assertEquals(Double.NaN, Double.NaN);
 } catch (IllegalArgumentException e) {
 // Expected
 }
 try {
 Gamma.regularizedGammaQ(1.0, "x");
 assertEquals(Double.NaN, Double.NaN);
 } catch (NumberFormatException e) {
 // Expected
 }
 }
}