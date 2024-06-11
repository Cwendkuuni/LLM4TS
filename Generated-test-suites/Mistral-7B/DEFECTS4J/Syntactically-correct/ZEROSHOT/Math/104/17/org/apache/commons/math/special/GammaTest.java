import org.apache.commons.math3.special.Gamma;
import org.junit.Test;
import static org.junit.Assert.*;

public class GammaTest {

 @Test
 public void testLogGamma() {
 // Test valid inputs
 double result = Gamma.logGamma(1.5);
 double expected = Math.log(Math.PI) + Math.log(1.5) - Math.log(2.0) + 0.422784335098550591;
 assertEquals("Test logGamma with valid input", expected, result, 1e-6);

 // Test invalid input: negative number
 result = Gamma.logGamma(-1.0);
 assertEquals("Test logGamma with invalid input: negative number", Double.NaN, result);

 // Test invalid input: zero or less
 result = Gamma.logGamma(0.0);
 assertEquals("Test logGamma with invalid input: zero or less", Double.NaN, result);
 }

 @Test
 public void testRegularizedGammaP() {
 // Test valid inputs: P(a, x)
 double result = Gamma.regularizedGammaP(1.5, 2.5);
 double expected = 0.712380733828435693;
 assertEquals("Test regularizedGammaP with valid input: P(a, x)", expected, result, 1e-6);

 // Test valid inputs: P(a, 0)
 result = Gamma.regularizedGammaP(1.5, 0.0);
 expected = 1.0;
 assertEquals("Test regularizedGammaP with valid input: P(a, 0)", expected, result, 1e-6);

 // Test valid inputs: P(a, x) > 1
 result = Gamma.regularizedGammaP(2.5, 2.5);
 expected = 0.921047625163228795;
 assertEquals("Test regularizedGammaP with valid input: P(a, x) > 1", expected, result, 1e-6);

 // Test valid inputs: P(a, x) < 1
 result = Gamma.regularizedGammaP(1.5, 1.5);
 expected = 0.712380733828435693;
 assertEquals("Test regularizedGammaP with valid input: P(a, x) < 1", expected, result, 1e-6);

 // Test invalid input: negative number for a
 result = Gamma.regularizedGammaP(-1.0, 2.5);
 assertEquals("Test regularizedGammaP with invalid input: negative number for a", Double.NaN, result);

 // Test invalid input: zero or less for a
 result = Gamma.regularizedGammaP(0.0, 2.5);
 assertEquals("Test regularizedGammaP with invalid input: zero or less for a", Double.NaN, result);

 // Test invalid input: negative number for x
 result = Gamma.regularizedGammaP(1.5, -1.0);
 assertEquals("Test regularizedGammaP with invalid input: negative number for x", Double.NaN, result);

 // Test invalid input: zero for x
 result = Gamma.regularizedGammaP(1.5, 0.0);
 expected = 1.0;
 assertEquals("Test regularizedGammaP with invalid input: zero for x", expected, result, 1e-6);
 }

 @Test
 public void testRegularizedGammaQ() {
 // Test valid inputs: Q(a, x) = 1 - P(a, x)
 double result = Gamma.regularizedGammaQ(1.5, 2.5);
 double expected = 1.28761926483677431;
 assertEquals("Test regularizedGammaQ with valid input: Q(a, x) = 1 - P(a, x)", expected, result, 1e-6);

 // Test valid inputs: Q(a, x) = 1 when x = a and a > 1
 result = Gamma.regularizedGammaQ(2.5, 2.5);
 expected = 1.0;
 assertEquals("Test regularizedGammaQ with valid input: Q(a, x) = 1 when x = a and a > 1", expected, result, 1e-6);

 // Test valid inputs: Q(a, x) < 1 when x < a and a > 1
 result = Gamma.regularizedGammaQ(1.5, 1.5);
 expected = 1.28761926483677431;
 assertEquals("Test regularizedGammaQ with valid input: Q(a, x) < 1 when x < a and a > 1", expected, result, 1e-6);

 // Test invalid input: negative number for a
 result = Gamma.regularizedGammaQ(-1.0, 2.5);
 assertEquals("Test regularizedGammaQ with invalid input: negative number for a", Double.NaN, result);

 // Test invalid input: zero or less for a
 result = Gamma.regularizedGammaQ(0.0, 2.5);
 assertEquals("Test regularizedGammaQ with invalid input: zero or less for a", Double.NaN, result);

 // Test invalid input: negative number for x
 result = Gamma.regularizedGammaQ(1.5, -1.0);
 assertEquals("Test regularizedGammaQ with invalid input: negative number for x", Double.NaN, result);

 // Test invalid input: zero for x
 result = Gamma.regularizedGammaQ(1.5, 0.0);
 expected = 0.0;
 assertEquals("Test regularizedGammaQ with invalid input: zero for x", expected, result, 1e-6);
 }

 // You can add more test cases here as needed

}