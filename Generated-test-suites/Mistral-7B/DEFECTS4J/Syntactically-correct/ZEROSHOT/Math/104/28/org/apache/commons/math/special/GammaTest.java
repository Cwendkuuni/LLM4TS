import org.apache.commons.math3.special.Gamma;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GammaTest {

 @Test
 public void testLogGamma() {
 // Test valid inputs
 double result = Gamma.logGamma(1.0);
 assertEquals(0.0, result, 1e-10);

 result = Gamma.logGamma(2.5);
 assertEquals(Math.log(Math.PI) + Math.log(2.5) - 0.5 * Math.log(2.0), result, 1e-10);

 result = Gamma.logGamma(0.5);
 assertEquals(-Math.log(2.0) + 0.5 * Math.log(Math.PI), result, 1e-10);

 // Test invalid inputs
 result = Gamma.logGamma(-1.0);
 assertFalse("logGamma should return NaN for negative arguments", Double.isNaN(result));

 result = Gamma.logGamma(0.0);
 assertFalse("logGamma should return NaN for zero arguments", Double.isNaN(result));
 }

 @Test
 public void testRegularizedGammaP() {
 // Test valid inputs
 double result = Gamma.regularizedGammaP(1.0, 1.0);
 assertEquals(1.0, result, 1e-10);

 result = Gamma.regularizedGammaP(2.5, 1.5);
 assertEquals(1.1274209693584436033, result, 1e-10);

 result = Gamma.regularizedGammaP(0.5, 1.0);
 assertEquals(0.5, result, 1e-10);

 // Test invalid inputs
 result = Gamma.regularizedGammaP(-1.0, 1.0);
 assertFalse("regularizedGammaP should return NaN for negative arguments", Double.isNaN(result));

 result = Gamma.regularizedGammaP(1.0, 0.0);
 assertFalse("regularizedGammaP should return NaN for x < 0", Double.isNaN(result));
 }

 @Test(expected = MathException.class)
 public void testRegularizedGammaPException() {
 try {
 Gamma.regularizedGammaP(1.0, -1.0);
 } catch (MathException e) {
 // Test exception for x < 0
 return;
 }
 fail("regularizedGammaP should throw MathException for x < 0");
 }

 @Test
 public void testRegularizedGammaQ() {
 // Test valid inputs
 double result = Gamma.regularizedGammaQ(1.0, 1.0);
 assertEquals(0.0, result, 1e-10);

 result = Gamma.regularizedGammaQ(2.5, 1.5);
 assertEquals(0.8725806446254622967, result, 1e-10);

 result = Gamma.regularizedGammaQ(0.5, 1.0);
 assertEquals(0.5, result, 1e-10);

 // Test invalid inputs
 result = Gamma.regularizedGammaQ(-1.0, 1.0);
 assertFalse("regularizedGammaQ should return NaN for negative arguments", Double.isNaN(result));

 result = Gamma.regularizedGammaQ(1.0, 0.0);
 assertFalse("regularizedGammaQ should return NaN for x < 0", Double.isNaN(result));
 }

 @Test(expected = MathException.class)
 public void testRegularizedGammaQException() {
 try {
 Gamma.regularizedGammaQ(1.0, -1.0);
 } catch (MathException e) {
 // Test exception for x < 0
 return;
 }
 fail("regularizedGammaQ should throw MathException for x < 0");
 }

 @Test
 public void testRegularizedGammaQReciprocal() {
 // Test valid inputs
 double result = Gamma.regularizedGammaP(1.0, 1.0);
 assertEquals(1.0, result, 1e-10);

 result = Gamma.regularizedGammaQ(1.0, 1.0);
 assertEquals(0.0, result, 1e-10);

 assertEquals(1.0 / result, Gamma.regularizedGammaP(1.0, 1.0), 1e-10);

 // Test invalid inputs
 result = Gamma.regularizedGammaP(-1.0, 1.0);
 assertFalse("regularizedGammaP should return NaN for negative arguments", Double.isNaN(result));

 result = Gamma.regularizedGammaQ(-1.0, 1.0);
 assertFalse("regularizedGammaQ should return NaN for negative arguments", Double.isNaN(result));
 }
}