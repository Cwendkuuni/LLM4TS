import org.apache.commons.math3.special.Gamma;
import org.junit.Test;
import static org.junit.Assert.*;

public class GammaTest {

 @Test
 public void testLogGamma() {
 // Test valid inputs
 double x = 1.5;
 double expected = 0.405828131945;
 assertEquals(expected, Gamma.logGamma(x), 0.0001);

 x = 0.5;
 expected = -0.577215664902;
 assertEquals(expected, Gamma.logGamma(x), 0.0001);

 x = 2.0;
 expected = 1.17809551051;
 assertEquals(expected, Gamma.logGamma(x), 0.0001);

 // Test invalid inputs
 x = -1.0;
 assertNaN(Gamma.logGamma(x));

 x = 0.0;
 assertNaN(Gamma.logGamma(x));
 }

 @Test
 public void testRegularizedGammaP() {
 // Test valid inputs
 double a = 1.5;
 double x = 1.0;
 double expected = 0.93115266557;
 assertEquals(expected, Gamma.regularizedGammaP(a, x), 0.0001);

 a = 0.5;
 x = 1.0;
 expected = 0.78803632259;
 assertEquals(expected, Gamma.regularizedGammaP(a, x), 0.0001);

 a = 2.0;
 x = 2.0;
 expected = 1.0;
 assertEquals(expected, Gamma.regularizedGammaP(a, x), 0.0001);

 // Test invalid inputs
 a = -1.0;
 x = 1.0;
 assertNaN(Gamma.regularizedGammaP(a, x));

 a = 1.0;
 x = -1.0;
 assertNaN(Gamma.regularizedGammaP(a, x));

 a = 0.0;
 x = 1.0;
 assertNaN(Gamma.regularizedGammaP(a, x));
 }

 @Test
 public void testRegularizedGammaQ() {
 // Test valid inputs
 double a = 1.5;
 double x = 1.0;
 double expected = 0.068847334381;
 assertEquals(expected, 1.0 - Gamma.regularizedGammaP(a, x), 0.0001);
 assertEquals(expected, Gamma.regularizedGammaQ(a, x), 0.0001);

 a = 0.5;
 x = 1.0;
 expected = 0.21196367741;
 assertEquals(expected, 1.0 - Gamma.regularizedGammaP(a, x), 0.0001);
 assertEquals(expected, Gamma.regularizedGammaQ(a, x), 0.0001);

 a = 2.0;
 x = 2.0;
 expected = 0.0;
 assertEquals(expected, Gamma.regularizedGammaQ(a, x), 0.0001);

 // Test invalid inputs
 a = -1.0;
 x = 1.0;
 assertNaN(Gamma.regularizedGammaQ(a, x));

 a = 1.0;
 x = -1.0;
 assertNaN(Gamma.regularizedGammaQ(a, x));

 a = 0.0;
 x = 1.0;
 assertNaN(Gamma.regularizedGammaQ(a, x));
 }
}