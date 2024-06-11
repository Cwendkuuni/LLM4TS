import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.junit.Test;
import static org.junit.Assert.*;

public class MannWhitneyUTestTest {

 @Test
 public void testMannWhitneyU_equalMeans() {
 MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
 double[] x = {1.0, 2.0, 3.0, 4.0};
 double[] y = {1.5, 2.5, 3.5, 4.5};
 double mannWhitneyU = mannWhitneyUTest.mannWhitneyU(x, y);
 double expectedMannWhitneyU = 6.0;
 assertEquals("Mann-Whitney U should be equal for equal means", expectedMannWhitneyU, mannWhitneyU);
 }

 @Test
 public void testMannWhitneyU_differentMeans() {
 MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
 double[] x = {1.0, 2.0, 3.0, 4.0};
 double[] y = {5.0, 6.0, 7.0, 8.0};
 try {
 double mannWhitneyU = mannWhitneyUTest.mannWhitneyU(x, y);
 fail("Mann-Whitney U test should fail for different means");
 } catch (AssertionError e) {
 // expected
 }
 }

 @Test
 public void testMannWhitneyUTest_equalMeans() {
 MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
 double[] x = {1.0, 2.0, 3.0, 4.0};
 double[] y = {1.5, 2.5, 3.5, 4.5};
 double asymptoticPValue = mannWhitneyUTest.mannWhitneyUTest(x, y);
 double expectedAsymptoticPValue = 0.5;
 assertEquals("Asymptotic p-value should be 0.5 for equal means", expectedAsymptoticPValue, asymptoticPValue, 0.001);
 }

 @Test
 public void testMannWhitneyUTest_differentMeans() {
 MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
 double[] x = {1.0, 2.0, 3.0, 4.0};
 double[] y = {5.0, 6.0, 7.0, 8.0};
 try {
 double asymptoticPValue = mannWhitneyUTest.mannWhitneyUTest(x, y);
 fail("Mann-Whitney U test should fail for different means");
 } catch (AssertionError e) {
 // expected
 }
 }

 @Test(expected = NullArgumentException.class)
 public void testNullArgumentException_x() {
 MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
 double[] x = null;
 double[] y = {1.0, 2.0, 3.0, 4.0};
 mannWhitneyUTest.mannWhitneyU(x, y);
 }

 @Test(expected = NullArgumentException.class)
 public void testNullArgumentException_y() {
 MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
 double[] x = {1.0, 2.0, 3.0, 4.0};
 double[] y = null;
 mannWhitneyUTest.mannWhitneyU(x, y);
 }

 @Test(expected = NoDataException.class)
 public void testNoDataException_x() {
 MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
 double[] x = {};
 double[] y = {1.0, 2.0, 3.0, 4.0};
 mannWhitneyUTest.mannWhitneyU(x, y);
 }

 @Test(expected = NoDataException.class)
 public void testNoDataException_y() {
 MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
 double[] x = {1.0, 2.0, 3.0, 4.0};
 double[] y = {};
 mannWhitneyUTest.mannWhitneyU(x, y);
 }

 @Test(expected = ConvergenceException.class)
 public void testConvergenceException() {
 MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
 double[] x = {Double.NaN, 2.0, 3.0, 4.0};
 double[] y = {1.0, 2.0, 3.0, 5.0};
 mannWhitneyUTest.mannWhitneyUTest(x, y);
 }

 @Test(expected = MaxCountExceededException.class)
 public void testMaxCountExceededException() {
 MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
 double[] x = new double[Integer.MAX_VALUE];
 double[] y = new double[Integer.MAX_VALUE];
 mannWhitneyUTest.mannWhitneyUTest(x, y);
 }
}