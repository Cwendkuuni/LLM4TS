import org.junit.Test;
import org.apache.commons.math3.special.Gamma;
import static org.junit.Assert.*;

public class GammaTest {

 // Test for logGamma method
 @Test
 public void testLogGamma() {
 double result = Gamma.logGamma(1.5);
 double expected = Math.log(Math.PI) + 0.5 * Math.log(1.5) - 0.066863489664;
 assertEquals("Incorrect log gamma value for test case 1", expected, result, 1e-5);
 }

 // Test for logGamma method with negative argument
 @Test(expected = MathException.class)
 public void testLogGammaNegativeArgument() {
 Gamma.logGamma(-1.0);
 }

 // Test for logGamma method with zero argument
 @Test(expected = MathException.class)
 public void testLogGammaZeroArgument() {
 Gamma.logGamma(0.0);
 }

 // Test for logGamma method with non-numeric argument
 @Test(expected = MathException.class)
 public void testLogGammaNonNumericArgument() {
 Gamma.logGamma('a');
 }

 // Test for regularizedGammaP method with valid arguments
 @Test
 public void testRegularizedGammaP() {
 double result = Gamma.regularizedGammaP(1.5, 2.5);
 double expected = 0.611811295294;
 assertEquals("Incorrect regularized gamma P value for test case 1", expected, result, 1e-5);
 }

 // Test for regularizedGammaP method with invalid arguments
 @Test(expected = MathException.class)
 public void testRegularizedGammaPInvalidArguments() {
 Gamma.regularizedGammaP(-1.0, 2.5);
 }

 // Test for regularizedGammaP method with x = a
 @Test
 public void testRegularizedGammaPEqualToA() {
 double result = Gamma.regularizedGammaP(1.5, 1.5);
 double expected = 0.741182352984;
 assertEquals("Incorrect regularized gamma P value for test case 2", expected, result, 1e-5);
 }

 // Test for regularizedGammaP method with x = a - 1
 @Test
 public void testRegularizedGammaPXEqualsAMinusOne() {
 double result = Gamma.regularizedGammaP(1.5, 0.5);
 double expected = 0.71198190451;
 assertEquals("Incorrect regularized gamma P value for test case 3", expected, result, 1e-5);
 }

 // Test for regularizedGammaQ method with valid arguments
 @Test
 public void testRegularizedGammaQ() {
 double result = Gamma.regularizedGammaQ(1.5, 2.5);
 double expected = 1.0 - 0.611811295294;
 assertEquals("Incorrect regularized gamma Q value for test case 1", expected, result, 1e-5);
 }

 // Test for regularizedGammaQ method with invalid arguments
 @Test(expected = MathException.class)
 public void testRegularizedGammaQInvalidArguments() {
 Gamma.regularizedGammaQ(-1.0, 2.5);
 }

 // Test for regularizedGammaQ method with x = a
 @Test
 public void testRegularizedGammaQEqualToA() {
 double result = Gamma.regularizedGammaQ(1.5, 1.5);
 double expected = 1.0 - 0.741182352984;
 assertEquals("Incorrect regularized gamma Q value for test case 2", expected, result, 1e-5);
 }

 // Test for regularizedGammaQ method with x = a - 1
 @Test
 public void testRegularizedGammaQXEqualsAMinusOne() {
 double result = Gamma.regularizedGammaQ(1.5, 0.5);
 double expected = 1.0 - 0.71198190451;
 assertEquals("Incorrect regularized gamma Q value for test case 3", expected, result, 1e-5);
 }
}