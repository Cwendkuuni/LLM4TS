import org.apache.commons.math3.special.Gamma;
import org.junit.Test;
import static org.junit.Assert.*;

public class GammaTest {

 @Test
 public void testLogGamma() {
 double result;

 // Test valid input
 result = Gamma.logGamma(1.0);
 assertEquals(0.0, result, 1e-11);
 result = Gamma.logGamma(2.0);
 assertEquals(Math.log(1.0 / 2.0) + Math.log(Math.PI) + 0.5 * Math.log(2.0), result, 1e-11);
 result = Gamma.logGamma(3.0);
 assertEquals(Math.log(SquareRoot2M_1_6) + Math.log(SquareRoot3M_1_6) + 0.5 * Math.log(3.0), result, 1e-11);
 result = Gamma.logGamma(0.5);
 assertEquals(Math.log(SquareRootPi) + Math.log(2.0) - 0.5 * Math.log(2.0), result, 1e-11);
 result = Gamma.logGamma(1.5);
 assertEquals(Math.log(SquareRoot2) + Math.log(SquareRoot3) + 0.5 * Math.log(1.5), result, 1e-11);
 result = Gamma.logGamma(2.5);
 assertEquals(Math.log(SquareRoot2M_1_6) * 2 + 0.5 * Math.log(2.5), result, 1e-11);

 // Test invalid input
 result = Gamma.logGamma(-0.5);
 assertNotSame(Double.NaN, result);
 result = Gamma.logGamma(0.0);
 assertNotSame(Double.NaN, result);
 result = Gamma.logGamma(1.0 / 0.0);
 assertNotSame(Double.NaN, result);
 }

 @Test
 public void testRegularizedGammaP() {
 double result;

 // Test valid input
 result = Gamma.regularizedGammaP(1.0, 1.0);
 assertEquals(1.0, result, 1e-11);
 result = Gamma.regularizedGammaP(0.5, 1.0);
 assertEquals(Math.sqrt(Math.PI) / 2, result, 1e-11);
 result = Gamma.regularizedGammaP(1.5, 2.0);
 assertEquals(Math.sqrt(2.0) / 2, result, 1e-11);
 result = Gamma.regularizedGammaP(2.5, 3.0);
 assertEquals(Math.sqrt(3.0) / 2, result, 1e-11);
 result = Gamma.regularizedGammaP(2.0, 1.0);
 assertEquals(Math.E / 2, result, 1e-11);
 result = Gamma.regularizedGammaP(3.0, 2.0);
 assertEquals(1 - Math.sqrt(2.0) / 2, result, 1e-11);
 result = Gamma.regularizedGammaP(4.0, 3.0);
 assertEquals(1 - Math.sqrt(3.0) / 2, result, 1e-11);
 result = Gamma.regularizedGammaP(2.0, 4.0);
 assertEquals(1 - Math.sqrt(2.0) / 2, result, 1e-11);
 result = Gamma.regularizedGammaP(3.0, 5.0);
 assertEquals(1 - Math.sqrt(5.0) / 2, result, 1e-11);
 result = Gamma.regularizedGammaP(5.0, 3.0);
 assertEquals(1 - Math.sqrt(3.0) / 2, result, 1e-11);
 result = Gamma.regularizedGammaP(5.0, 10.0);
 assertEquals(1 - Math.sqrt(10.0) / 2, result, 1e-11);

 // Test invalid input
 result = Gamma.regularizedGammaP(-1.0, 1.0);
 assertNotSame(Double.NaN, result);
 result = Gamma.regularizedGammaP(1.0, -1.0);
 assertNotSame(Double.NaN, result);
 result = Gamma.regularizedGammaP(1.0, 0.0);
 assertNotSame(Double.NaN, result);
 result = Gamma.regularizedGammaP(2.0, 0.0);
 assertNotSame(Double.NaN, result);
 result = Gamma.regularizedGammaP(1.0, 2.0);
 assertEquals(1.0, result, 1e-11);
 result = Gamma.regularizedGammaP(1.0, 3.0);
 assertEquals(1.0, result, 1e-11);
 result = Gamma.regularizedGammaP(1.0, 4.0);
 assertEquals(1.0, result, 1e-11);
 result = Gamma.regularizedGammaP(1.0, 5.0);
 assertEquals(1.0, result, 1e-11);
 result = Gamma.regularizedGammaP(1.0, 10.0);
 assertEquals(1.0, result, 1e-11);
 }

 @Test(expected = MathException.class)
 public void testRegularizedGammaPException() {
 Gamma.regularizedGammaP(-1.0, 1.0);
 }

 // Test regularizedGammaQ
 @Test
 public void testRegularizedGammaQ() {
 double result;

 // Test valid input
 result = Gamma.regularizedGammaQ(1.0, 1.0);
 assertEquals(1.0 - Math.sqrt(Math.PI) / 2, result, 1e-11);
 result = Gamma.regularizedGammaQ(0.5, 1.0);
 assertEquals(1.0 - Math.sqrt(Math.PI) / 4, result, 1e-11);
 result = Gamma.regularizedGammaQ(1.5, 2.0);
 assertEquals(1.0 - Math.sqrt(2.0) / 4, result, 1e-11);
 result = Gamma.regularizedGammaQ(2.5, 3.0);
 assertEquals(1.0 - Math.sqrt(3.0) / 4, result, 1e-11);
 result = Gamma.regularizedGammaQ(2.0, 1.0);
 assertEquals(1.0 - Math.E / 4, result, 1e-11);
 result = Gamma.regularizedGammaQ(3.0, 2.0);
 assertEquals(1.0 - (1.0 - Math.sqrt(2.0) / 2), result, 1e-11);
 result = Gamma.regularizedGammaQ(4.0, 3.0);
 assertEquals(1.0 - (1.0 - Math.sqrt(3.0) / 2), result, 1e-11);
 result = Gamma.regularizedGammaQ(2.0, 4.0);
 assertEquals(1.0 - (1.0 - Math.sqrt(2.0) / 2), result, 1e-11);
 result = Gamma.regularizedGammaQ(3.0, 5.0);
 assertEquals(1.0 - (1.0 - Math.sqrt(5.0) / 2), result, 1e-11);
 result = Gamma.regularizedGammaQ(5.0, 3.0);
 assertEquals(1.0 - (1.0 - Math.sqrt(3.0) / 2), result, 1e-11);
 result = Gamma.regularizedGammaQ(5.0, 10.0);
 assertEquals(1.0 - (1.0 - Math.sqrt(10.0) / 2), result, 1e-11);

 // Test invalid input
 result = Gamma.regularizedGammaQ(-1.0, 1.0);
 assertNotSame(Double.NaN, result);
 result = Gamma.regularizedGammaQ(1.0, -1.0);
 assertNotSame(Double.NaN, result);
 result = Gamma.regularizedGammaQ(1.0, 0.0);
 assertEquals(1.0, result, 1e-11);
 result = Gamma.regularizedGammaQ(2.0, 0.0);
 assertEquals(1.0, result, 1e-11);
 result = Gamma.regularizedGammaQ(1.0, 2.0);
 assertEquals(1.0, result, 1e-11);
 result = Gamma.regularizedGammaQ(1.0, 3.0);
 assertEquals(1.0, result, 1e-11);
 result = Gamma.regularizedGammaQ(1.0, 4.0);
 assertEquals(1.0, result, 1e-11);
 result = Gamma.regularizedGammaQ(1.0, 5.0);
 assertEquals(1.0, result, 1e-11);
 result = Gamma.regularizedGammaQ(1.0, 10.0);
 assertEquals(1.0, result, 1e-11);
 }

 @Test(expected = MathException.class)
 public void testRegularizedGammaQException() {
 Gamma.regularizedGammaQ(-1.0, 1.0);
 }

 @Test
 public void testRegularizedGammaPAndQ() {
 double pResult, qResult;

 // Test valid input
 pResult = Gamma.regularizedGammaP(1.5, 2.0);
 qResult = Gamma.regularizedGammaQ(1.5, 2.0);
 assertEquals(pResult, 1.0 - qResult, 1e-11);
 pResult = Gamma.regularizedGammaP(2.5, 3.0);
 qResult = Gamma.regularizedGammaQ(2.5, 3.0);
 assertEquals(pResult, 1.0 - qResult, 1e-11);
 pResult = Gamma.regularizedGammaP(3.5, 4.0);
 qResult = Gamma.regularizedGammaQ(3.5, 4.0);
 assertEquals(pResult, 1.0 - qResult, 1e-11);
 pResult = Gamma.regularizedGammaP(4.5, 5.0);
 qResult = Gamma.regularizedGammaQ(4.5, 5.0);
 assertEquals(pResult, 1.0 - qResult, 1e-11);
 pResult = Gamma.regularizedGammaP(5.5, 6.0);
 qResult = Gamma.regularizedGammaQ(5.5, 6.0);
 assertEquals(pResult, 1.0 - qResult, 1e-11);

 // Test invalid input
 pResult = Gamma.regularizedGammaP(-1.0, 1.0);
 assertNotSame(Double.NaN, pResult);
 qResult = Gamma.regularizedGammaQ(-1.0, 1.0);
 assertNotSame(Double.NaN, qResult);
 pResult = Gamma.regularizedGammaP(1.0, -1.0);
 assertNotSame(Double.NaN, pResult);
 qResult = Gamma.regularizedGammaQ(1.0, -1.0);
 assertNotSame(Double.NaN, qResult);
 pResult = Gamma.regularizedGammaP(1.0, 0.0);
 assertEquals(1.0, pResult, 1e-11);
 qResult = Gamma.regularizedGammaQ(1.0, 0.0);
 assertEquals(1.0, qResult, 1e-11);
 pResult = Gamma.regularizedGammaP(2.0, 0.0);
 assertEquals(1.0, pResult, 1e-11);
 qResult = Gamma.regularizedGammaQ(2.0, 0.0);
 assertEquals(1.0, qResult, 1e-11);
 pResult = Gamma.regularizedGammaP(1.0, 2.0);
 assertEquals(1.0, pResult, 1e-11);
 qResult = Gamma.regularizedGammaQ(1.0, 2.0);
 assertEquals(1.0, qResult, 1e-11);
 pResult = Gamma.regularizedGammaP(1.0, 3.0);
 assertEquals(1.0, pResult, 1e-11);
 qResult = Gamma.regularizedGammaQ(1.0, 3.0);
 assertEquals(1.0, qResult, 1e-11);
 pResult = Gamma.regularizedGammaP(1.0, 4.0);
 assertEquals(1.0, pResult, 1e-11);
 qResult = Gamma.regularizedGammaQ(1.0, 4.0);
 assertEquals(1.0, qResult, 1e-11);
 pResult = Gamma.regularizedGammaP(1.0, 5.0);
 assertEquals(1.0, pResult, 1e-11);
 qResult = Gamma.regularizedGammaQ(1.0, 5.0);
 assertEquals(1.0, qResult, 1e-11);
 pResult = Gamma.regularizedGammaP(1.0, 10.0);
 assertEquals(1.0, pResult, 1e-11);
 qResult = Gamma.regularizedGammaQ(1.0, 10.0);
 assertEquals(1.0, qResult, 1e-11);
 }
}