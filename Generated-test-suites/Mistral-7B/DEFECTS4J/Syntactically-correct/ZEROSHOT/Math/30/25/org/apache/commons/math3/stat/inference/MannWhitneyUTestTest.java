import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.stat.ranking.NaturalRanking;
import org.apache.commons.math3.stat.ranking.TiesStrategy;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class MannWhitneyUTestTest {

 private MannWhitneyUTest mannWhitneyUTest;

 @Test(expected = NullArgumentException.class)
 public void testNullArgumentException() {
 mannWhitneyUTest = new MannWhitneyUTest();
 mannWhitneyUTest.mannWhitneyUTest(null, new double[0]);
 }

 @Test(expected = NoDataException.class)
 public void testNoDataException() {
 mannWhitneyUTest = new MannWhitneyUTest();
 mannWhitneyUTest.mannWhitneyUTest(new double[0], new double[0]);
 }

 @Test
 public void testMannWhitneyU() {
 mannWhitneyUTest = new MannWhitneyUTest();
 double[] x = {1, 2, 3};
 double[] y = {4, 5, 6};
 double mannWhitneyU = mannWhitneyUTest.mannWhitneyU(x, y);
 assertEquals(7.5, mannWhitneyU, 0.001);
 }

 @Test
 public void testMannWhitneyUTest() {
 mannWhitneyUTest = new MannWhitneyUTest();
 double[] x = {1, 1, 2, 2, 3};
 double[] y = {1, 1, 3, 4, 5};
 try {
 double pValue = mannWhitneyUTest.mannWhitneyUTest(x, y);
 assertEquals(0.333, pValue, 0.001);
 } catch (NoDataException | ConvergenceException | MaxCountExceededException e) {
 assertThrows(NoDataException.class, () -> mannWhitneyUTest.mannWhitneyUTest(x, y));
 }
 }

 @Test
 public void testMannWhitneyUTestWithCustomStrategies() {
 mannWhitneyUTest = new MannWhitneyUTest(NaNStrategy.REMOVE, TiesStrategy.MIN);
 double[] x = {1, 1, 2, 2, 3, Double.NaN};
 double[] y = {1, 1, 3, 3, 4, 5};
 try {
 double pValue = mannWhitneyUTest.mannWhitneyUTest(x, y);
 assertEquals(0.4, pValue, 0.001);
 } catch (NoDataException | ConvergenceException | MaxCountExceededException e) {
 assertThrows(NoDataException.class, () -> mannWhitneyUTest.mannWhitneyUTest(x, y));
 }
 }

}