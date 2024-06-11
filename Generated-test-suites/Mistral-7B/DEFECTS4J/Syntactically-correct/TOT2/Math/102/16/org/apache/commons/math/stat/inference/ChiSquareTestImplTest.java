import static org.junit.Assert.*;
import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistributionImpl;
import org.apache.commons.math3.stat.inference.ChiSquareTestImpl;
import org.junit.Test;

public class ChiSquareTestImplTest {

 private ChiSquareTestImpl test;
 private long[][] testData;
 private long[] observed1, observed2;
 private long[] expected1, expected2;

 @Before
 public void setUp() {
 test = new ChiSquareTestImpl();
 testData = new long[][]{{1, 2, 3, 4}, {2, 3, 4, 5}, {3, 4, 5, 6}, {4, 5, 6, 7}};
 observed1 = new long[]{2, 2, 3, 3};
 expected1 = new long[]{3, 1, 2, 2};
 observed2 = new long[]{2, 3, 2, 3};
 expected2 = new long[]{3, 1, 2, 2};
 }

 @Test
 public void testChiSquareWithSingleArray() throws Exception {
 long[] input = {1, 2, 3, 4};
 double result = test.chiSquare(input);
 assertEquals(6.25, result, 0.01);
 }

 @Test
 public void testChiSquareWithTwoArrays() throws Exception {
 double result = test.chiSquareDataSetsComparison(observed1, observed2);
 double expected = 4.0;
 assertEquals(expected, result, 0.01);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testChiSquareWithInvalidTwoArrays() throws Exception {
 long[] invalidObs1 = {1, 2};
 long[] invalidObs2 = new long[0];
 test.chiSquareDataSetsComparison(invalidObs1, invalidObs2);
 }

 @Test
 public void testChiSquareWithLargeTwoArrays() throws Exception {
 long[][] inputArrays = new long[200][200];
 for (int i = 0; i < inputArrays[0].length; i++) {
 inputArrays[0][i] = (long) Math.pow(2, i);
 inputArrays[1][i] = (long) Math.pow(3, i);
 }
 double result = test.chiSquareDataSetsComparison(inputArrays[0], inputArrays[1]);
 }

 @Test
 public void testChiSquareTestWithSingleArray() throws Exception {
 double[] input = {1.2, 1.3, 1.4, 1.5};
 double expected = 3.841554;
 double result = test.chiSquareTest(expected, input);
 assertEquals(expected, result, 0.001);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testChiSquareTestWithInvalidInput() throws Exception {
 double[] invalidInput = new double[]{1.2, 1.3};
 test.chiSquareTest(expected1, invalidInput);
 }

 @Test
 public void testChiSquareTestWithLargeInput() throws Exception {
 double[] input = new double[1000];
 for (int i = 0; i < input.length; i++) {
 input[i] = Math.random();
 }
 double expected = 1.0;
 double result = test.chiSquareTest(expected, input);
 }

 @Test(expected = MathException.class)
 public void testChiSquareTestWithMathException() throws Exception {
 double[] input = new double[]{1.0, -1.0};
 test.chiSquareTest(expected1, input);
 }

 @Test
 public void testChiSquareTestWithSmallAlpha() throws Exception {
 double[] input = {1.0, 1.0, 1.0, 1.0};
 double alpha = 0.01;
 boolean reject = test.chiSquareTest(input, 1.0, alpha);
 assertFalse(reject);
 }

 @Test
 public void testChiSquareTestWithLargeAlpha() throws Exception {
 double[] input = {1.0, 1.0, 1.0, 1.0};
 double alpha = 0.51;
 boolean reject = test.chiSquareTest(input, 1.0, alpha);
 assertTrue(reject);
 }

 @Test
 public void testChiSquareWithInvalidInputCounts() throws Exception {
 long[][] input = new long[1][1];
 try {
 test.chiSquare(input);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testChiSquareWithZeroCounts() throws Exception {
 long[] input = {0, 0, 0, 0};
 try {
 test.chiSquareTest(input, 1.0);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testChiSquareWithInvalidInputDistribution() throws Exception {
 ChiSquaredDistributionImpl dist = new ChiSquaredDistributionImpl(0.0);
 try {
 test.chiSquareTest(expected1, observed1, 0.0);
 fail("Expected MathException");
 } catch (MathException e) {
 // expected
 }
 }
}