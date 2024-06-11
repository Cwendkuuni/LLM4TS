import org.apache.commons.math3.SpecialFunctions;
import org.junit.Test;
import static org.junit.Assert.*;

public class GammaTest {

 @Test
 public void testLogGamma() {
 double result;

 // Test valid inputs
 result = SpecialFunctions.logGamma(1.0);
 assertEquals("logGamma(1.0) should be approximately 0.4342944819032518", 0.4342944819032518, result, 1e-9);
 result = SpecialFunctions.logGamma(2.0);
 assertEquals("logGamma(2.0) should be approximately 0.9182968654635243", 0.9182968654635243, result, 1e-9);

 // Test invalid inputs
 result = SpecialFunctions.logGamma(0.0);
 assertEquals("logGamma(0.0) should be NaN", Double.NaN, result);
 result = SpecialFunctions.logGamma(-1.0);
 assertEquals("logGamma(-1.0) should be NaN", Double.NaN, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLogGammaInvalidInput() {
 SpecialFunctions.logGamma(-2.0);
 }

 @Test
 public void testRegularizedGammaP() {
 double result;

 // Test valid inputs
 result = SpecialFunctions.regularizedGammaP(1.0, 1.0);
 assertEquals("regularizedGammaP(1.0, 1.0) should be approximately 0.5", 0.5, result, 1e-9);
 result = SpecialFunctions.regularizedGammaP(2.0, 1.0);
 assertEquals("regularizedGammaP(2.0, 1.0) should be approximately 0.6389056", 0.6389056, result, 1e-9);
 result = SpecialFunctions.regularizedGammaP(3.0, 1.0);
 assertEquals("regularizedGammaP(3.0, 1.0) should be approximately 0.7113258", 0.7113258, result, 1e-9);
 result = SpecialFunctions.regularizedGammaP(4.0, 1.0);
 assertEquals("regularizedGammaP(4.0, 1.0) should be approximately 0.7659116", 0.7659116, result, 1e-9);

 // Test invalid inputs
 result = SpecialFunctions.regularizedGammaP(0.0, 1.0);
 assertEquals("regularizedGammaP(0.0, 1.0) should be NaN", Double.NaN, result);
 result = SpecialFunctions.regularizedGammaP(-1.0, 1.0);
 assertEquals("regularizedGammaP(-1.0, 1.0) should be NaN", Double.NaN, result);
 result = SpecialFunctions.regularizedGammaP(1.0, 0.0);
 assertEquals("regularizedGammaP(1.0, 0.0) should be NaN", Double.NaN, result);
 result = SpecialFunctions.regularizedGammaP(1.0, -1.0);
 assertEquals("regularizedGammaP(1.0, -1.0) should be NaN", Double.NaN, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRegularizedGammaPInvalidInput() {
 SpecialFunctions.regularizedGammaP(-2.0, 1.0);
 SpecialFunctions.regularizedGammaP(1.0, 0.0);
 SpecialFunctions.regularizedGammaP(1.0, -1.0);
 }

 @Test
 public void testRegularizedGammaQ() {
 double result;

 // Test valid inputs
 result = SpecialFunctions.regularizedGammaQ(1.0, 1.0);
 assertEquals("regularizedGammaQ(1.0, 1.0) should be approximately 0.5", 0.5, result, 1e-9);
 result = SpecialFunctions.regularizedGammaQ(2.0, 1.0);
 assertEquals("regularizedGammaQ(2.0, 1.0) should be approximately 0.4231103", 0.4231103, result, 1e-9);
 result = SpecialFunctions.regularizedGammaQ(3.0, 1.0);
 assertEquals("regularizedGammaQ(3.0, 1.0) should be approximately 0.3192534", 0.3192534, result, 1e-9);
 result = SpecialFunctions.regularizedGammaQ(4.0, 1.0);
 assertEquals("regularizedGammaQ(4.0, 1.0) should be approximately 0.2389539", 0.2389539, result, 1e-9);

 // Test invalid inputs
 result = SpecialFunctions.regularizedGammaQ(0.0, 1.0);
 assertEquals("regularizedGammaQ(0.0, 1.0) should be NaN", Double.NaN, result);
 result = SpecialFunctions.regularizedGammaQ(-1.0, 1.0);
 assertEquals("regularizedGammaQ(-1.0, 1.0) should be NaN", Double.NaN, result);
 result = SpecialFunctions.regularizedGammaQ(1.0, 0.0);
 assertEquals("regularizedGammaQ(1.0, 0.0) should be NaN", Double.NaN, result);
 result = SpecialFunctions.regularizedGammaQ(1.0, -1.0);
 assertEquals("regularizedGammaQ(1.0, -1.0) should be NaN", Double.NaN, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRegularizedGammaQInvalidInput() {
 SpecialFunctions.regularizedGammaQ(-2.0, 1.0);
 SpecialFunctions.regularizedGammaQ(1.0, 0.0);
 SpecialFunctions.regularizedGammaQ(1.0, -1.0);
 }
}