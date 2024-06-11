import org.apache.commons.math3.special.Gamma;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GammaTest {

 @Test
 public void testLogGamma() {
 // Test valid inputs
 double x = 2.0;
 double expected = Math.log(Gamma.EPSILON * Math.PI) + (x * Math.log(x) - x) + 0.5 * Math.log(2.0 * Math.PI) - Math.log(Gamma.GAMMA);
 double result = Gamma.logGamma(x);
 assertEquals(expected, result, 1e-9);

 // Test negative and zero input
 x = -1.0;
 expected = Double.NaN;
 result = Gamma.logGamma(x);
 assertTrue(Double.isNaN(result));

 x = 0.0;
 expected = -Infinity;
 result = Gamma.logGamma(x);
 assertEquals(expected, result, Double.POSITIVE_INFINITY);
 }

 @Test(expected = MathException.class)
 public void testLogGammaNegativeNumber() {
 Gamma.logGamma(-1.0);
 }

 @Test
 public void testRegularizedGammaP() {
 // Test valid inputs
 double a = 1.5;
 double x = 0.5;
 double expected = 0.61232229565385293244;
 double result = Gamma.regularizedGammaP(a, x);
 assertEquals(expected, result, 1e-9);

 // Test special cases
 a = 1.0;
 x = 1.0;
 expected = 0.5;
 result = Gamma.regularizedGammaP(a, x);
 assertEquals(expected, result, 1e-9);

 a = 1.0;
 x = 2.0;
 expected = 1.0;
 result = Gamma.regularizedGammaP(a, x);
 assertEquals(expected, result, 1e-9);

 a = 0.5;
 x = 1.0;
 expected = 0.78802758926862823988;
 result = Gamma.regularizedGammaP(a, x);
 assertEquals(expected, result, 1e-9);

 // Test error cases
 a = 0.0;
 x = 2.0;
 try {
 result = Gamma.regularizedGammaP(a, x);
 } catch (MathException e) {
 result = Double.NaN;
 }
 assertTrue(Double.isNaN(result));

 a = 1.5;
 x = -1.0;
 try {
 result = Gamma.regularizedGammaP(a, x);
 } catch (MathException e) {
 result = Double.NaN;
 }
 assertTrue(Double.isNaN(result));
 }

 @Test(expected = MathException.class)
 public void testRegularizedGammaPNegativeA() {
 Gamma.regularizedGammaP(-1.5, 2.0);
 }

 @Test(expected = MathException.class)
 public void testRegularizedGammaPNegativeX() {
 Gamma.regularizedGammaP(1.5, -2.0);
 }

 @Test
 public void testRegularizedGammaQ() {
 // Test valid inputs
 a = 1.5;
 x = 0.5;
 double expected = 0.38767880435576286756;
 double result = Gamma.regularizedGammaQ(a, x);
 assertEquals(expected, result, 1e-9);

 // Test special cases
 a = 1.0;
 x = 1.0;
 expected = 0.5;
 result = Gamma.regularizedGammaQ(a, x);
 assertEquals(expected, result, 1e-9);

 a = 1.0;
 x = 2.0;
 expected = 1.0;
 result = Gamma.regularizedGammaQ(a, x);
 assertEquals(expected, result, 1e-9);

 a = 0.5;
 x = 1.0;
 expected = 0.21197220270602225451;
 result = Gamma.regularizedGammaQ(a, x);
 assertEquals(expected, result, 1e-9);

 // Test error cases
 a = 0.0;
 x = 2.0;
 try {
 result = Gamma.regularizedGammaQ(a, x);
 } catch (MathException e) {
 result = Double.NaN;
 }
 assertTrue(Double.isNaN(result));

 a = 1.5;
 x = -1.0;
 try {
 result = Gamma.regularizedGammaQ(a, x);
 } catch (MathException e) {
 result = Double.NaN;
 }
 assertTrue(Double.isNaN(result));
 }

 @Test(expected = MathException.class)
 public void testRegularizedGammaQNegativeA() {
 Gamma.regularizedGammaQ(-1.5, 2.0);
 }

 @Test(expected = MathException.class)
 public void testRegularizedGammaQNegativeX() {
 Gamma.regularizedGammaQ(1.5, -2.0);
 }
}