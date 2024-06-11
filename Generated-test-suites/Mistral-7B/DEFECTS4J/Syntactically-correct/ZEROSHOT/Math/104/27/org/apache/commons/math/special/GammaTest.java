import org.apache.commons.math3.SpecialFunctions;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class GammaTest {

 @Test
 public void testLogGamma() {
 double[] testCases = {-1.0, 0.0, 0.5, 1.0, 2.0, 3.0, 4.0, 5.0};
 for (double x : testCases) {
 try {
 double result = SpecialFunctions.logGamma(x);
 System.out.println("logGamma(" + x + ") = " + result);
 assertEquals(expectedValue(x), result, 1e-9);
 } catch (MathException e) {
 System.out.println("Expected MathException for x = " + x);
 }
 }
 }

 @Test(expected = MathException.class)
 public void testLogGammaNegative() {
 double x = -1.0;
 double result = SpecialFunctions.logGamma(x);
 }

 @Test
 public void testRegularizedGammaP() {
 double[][] testCases = {
 {1.0, 0.5, 0.886226925452758},
 {2.0, 0.5, 0.918283064559855},
 {3.0, 0.5, 0.943471270255644},
 {4.0, 0.5, 0.962225690355968},
 {5.0, 0.5, 0.974998209431253},
 {2.0, 1.0, 0.865065538422024},
 {3.0, 1.5, 0.938983641228787},
 {4.0, 2.0, 0.972753232025154},
 {5.0, 2.5, 0.988840423368026},
 {6.0, 3.0, 0.995218856379468},
 {7.0, 3.5, 0.998054379306853},
 {8.0, 4.0, 0.998876127048761},
 {-1.0, 1.0, Double.NaN},
 {1.0, -1.0, Double.NaN},
 {0.5, 0.5, Double.NaN},
 {1.0, 0.0, 1.0},
 {1.0, 1.0, 1.0},
 {2.0, 2.0, 1.0},
 {2.0, 3.0, 0.886226925452758},
 {3.0, 3.0, 0.842701147955713},
 {4.0, 4.0, 0.807353986351808},
 {5.0, 5.0, 0.781751863111772},
 {6.0, 6.0, 0.759438360882127},
 {7.0, 7.0, 0.741370218743788},
 {8.0, 8.0, 0.726693317243929},
 {9.0, 9.0, 0.712994116133823},
 {10.0, 10.0, 0.700912378856911},
 {11.0, 11.0, 0.689800737885535},
 {12.0, 12.0, 0.679159878999678},
 {13.0, 13.0, 0.66906579677081},
 {14.0, 14.0, 0.660319472486306},
 {15.0, 15.0, 0.652592809325431}
 };
 for (double[] testCase : testCases) {
 double a = testCase[0];
 double x = testCase[1];
 double result = SpecialFunctions.regularizedGammaP(a, x);
 System.out.println("P( " + a + ", " + x + ") = " + result);
 assertEquals(expectedValue(a, x), result, 1e-9);
 }
 }

 @Test
 public void testRegularizedGammaQ() {
 double[][] testCases = {
 {1.0, 0.5, 0.115740772463521},
 {2.0, 0.5, 0.083918730717925},
 {3.0, 0.5, 0.064717127112567},
 {4.0, 0.5, 0.053081301310509},
 {5.0, 0.5, 0.046394964204471},
 {2.0, 1.0, 0.135123932053245},
 {3.0, 1.5, 0.166636413462243},
 {4.0, 2.0, 0.193121343356305},
 {5.0, 2.5, 0.214962381327395},
 {6.0, 3.0, 0.22931517270713},
 {7.0, 3.5, 0.239472550563237},
 {8.0, 4.0, 0.24518977964368},
 {-1.0, 1.0, Double.NaN},
 {1.0, -1.0, Double.NaN},
 {0.5, 0.5, Double.NaN},
 {1.0, 0.0, 1.0},
 {1.0, 1.0, 0.0},
 {2.0, 2.0, 0.0},
 {2.0, 3.0, 0.115740772463521},
 {3.0, 3.0, 0.121570501252363},
 {4.0, 4.0, 0.123527212175452},
 {5.0, 5.0, 0.124673052956358},
 {6.0, 6.0, 0.12546666235483},
 {7.0, 7.0, 0.125942348284248},
 {8.0, 8.0, 0.126191979176045},
 {9.0, 9.0, 0.126346279067913},
 {10.0, 10.0, 0.126417732118228},
 {11.0, 11.0, 0.126454659387524},
 {12.0, 12.0, 0.126480113079273},
 {13.0, 13.0, 0.126494103233044},
 {14.0, 14.0, 0.126498824897472},
 {15.0, 15.0, 0.126499945356957}
 };
 for (double[] testCase : testCases) {
 double a = testCase[0];
 double x = testCase[1];
 double result = SpecialFunctions.regularizedGammaQ(a, x);
 System.out.println("Q( " + a + ", " + x + ") = " + result);
 assertEquals(expectedValue(a, x), 1.0 - result, 1e-9);
 }
 }

 private double expectedValue(double a, double x) {
 if (x == 0.0) {
 return (a > 1.0) ? 1.0 : 0.0;
 }
 return Math.exp(-x + (a * Math.log(x)) - SpecialFunctions.logGamma(a)) / SpecialFunctions.gamma(a + 1.0);
 }

 @Test(expected = MaxIterationsExceededException.class)
 public void testRegularizedGammaPMaxIterations() {
 double a = 1.0;
 double x = 1.0;
 SpecialFunctions.regularizedGammaP(a, x, 1e-16, 10);
 }

 @Test(expected = MaxIterationsExceededException.class)
 public void testRegularizedGammaQMaxIterations() {
 double a = 1.0;
 double x = 1.0;
 SpecialFunctions.regularizedGammaQ(a, x, 1e-16, 10);
 }
}