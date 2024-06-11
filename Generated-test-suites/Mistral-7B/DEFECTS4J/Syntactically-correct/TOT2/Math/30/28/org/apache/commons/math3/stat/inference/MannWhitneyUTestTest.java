import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.junit.Test;
import static org.junit.Assert.*;

public class MannWhitneyUTestTest {

 @Test
 public void testMannWhitneyUTest_SameData() throws Exception {
 MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
 double[] x = {1.0, 2.0, 3.0, 3.0, 3.0};
 double[] y = x; // same data
 double pValue = mannWhitneyUTest.mannWhitneyUTest(x, y);
 assertEquals(1.0, pValue, 0.001); // same data should have p-value close to 1
 }

 @Test
 public void testMannWhitneyUTest_DifferentData_SameMean() throws Exception {
 MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
 double[] x = {1.0, 2.0, 3.0};
 double[] y = {2.5, 3.5, 4.5};
 double pValue = mannWhitneyUTest.mannWhitneyUTest(x, y);
 assertTrue(pValue > 0.05); // different data with same mean should have p-value > 0.05
 }

 @Test
 public void testMannWhitneyUTest_DifferentData_DifferentMeans() throws Exception {
 MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
 double[] x = {1.0, 2.0, 3.0};
 double[] y = {5.0, 6.0, 7.0};
 double pValue = mannWhitneyUTest.mannWhitneyUTest(x, y);
 assertTrue(pValue < 0.05); // different data with different means should have p-value < 0.05
 }

 @Test(expected = NullArgumentException.class)
 public void testMannWhitneyUTest_NullData() throws Exception {
 MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
 double[] x = null;
 double[] y = {1.0, 2.0, 3.0};
 mannWhitneyUTest.mannWhitneyUTest(x, y);
 fail("NullArgumentException expected");
 }

 @Test(expected = NoDataException.class)
 public void testMannWhitneyUTest_EmptyData() throws Exception {
 MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
 double[] x = {};
 double[] y = {};
 mannWhitneyUTest.mannWhitneyUTest(x, y);
 fail("NoDataException expected");
 }

 @Test(expected = ConvergenceException.class)
 public void testMannWhitneyUTest_ConvergenceException() throws Exception {
 MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
 double[] x = {Double.NaN, Double.NaN, 3.0};
 double[] y = {5.0, 6.0, Double.NaN};
 mannWhitneyUTest.mannWhitneyUTest(x, y);
 fail("ConvergenceException expected");
 }

 @Test(expected = MaxCountExceededException.class)
 public void testMannWhitneyUTest_MaxCountExceededException() throws Exception {
 MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
 double[] x = new double[Integer.MAX_VALUE];
 double[] y = new double[Integer.MAX_VALUE];
 mannWhitneyUTest.mannWhitneyUTest(x, y);
 fail("MaxCountExceededException expected");
 }
}