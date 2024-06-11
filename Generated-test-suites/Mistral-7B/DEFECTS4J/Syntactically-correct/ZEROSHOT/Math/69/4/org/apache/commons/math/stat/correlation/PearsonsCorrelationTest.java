package org.apache.commons.math.stat.correlation; // package import

import static org.junit.Assert.assertEquals; // for assertions
import static org.junit.Assert.assertNotNull; // for assertions

import org.apache.commons.math3.linear.BlockRealMatrix; // for test data
import org.apache.commons.math3.linear.RealMatrix; // for test data and matrix creation
import org.apache.commons.math3.stat.regression.SimpleRegression; // for correlation computation
import org.junit.Test; // for Test annotation

public class PearsonsCorrelationTest {

 @Test
 public void testEmptyConstructor() {
 PearsonsCorrelation pcc = new PearsonsCorrelation();
 assertNotNull(pcc);
 assertEquals(0, pcc.getCorrelationMatrix().getRowDimension());
 }

 @Test
 public void testDataConstructor() {
 double[][] testData = new double[][]{{1.0, 2.0, 3.0}, {2.1, 3.1, 4.1}, {3.2, 4.2, 5.2}};
 PearsonsCorrelation pcc = new PearsonsCorrelation(testData);
 assertNotNull(pcc);
 assertEquals(3, pcc.getCorrelationMatrix().getRowDimension());
 }

 @Test
 public void testMatrixConstructor() {
 RealMatrix testMatrix = new BlockRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.1, 3.1, 4.1}, {3.2, 4.2, 5.2}});
 PearsonsCorrelation pcc = new PearsonsCorrelation(testMatrix);
 assertNotNull(pcc);
 assertEquals(3, pcc.getCorrelationMatrix().getRowDimension());
 }

 @Test
 public void testCovarianceConstructor() {
 Covariance covariance = new Covariance(testData); // testData is defined as above
 PearsonsCorrelation pcc = new PearsonsCorrelation(covariance);
 assertNotNull(pcc);
 assertEquals(3, pcc.getCorrelationMatrix().getRowDimension());
 }

 @Test
 public void testCorrelationStandardErrors() throws Exception {
 RealMatrix correlationMatrix = new BlockRealMatrix(new double[][]{{1.0, 0.9}, {0.9, 1.0}});
 PearsonsCorrelation pcc = new PearsonsCorrelation(correlationMatrix);
 RealMatrix stdErrors = pcc.getCorrelationStandardErrors();
 assertEquals(2, stdErrors.getRowDimension());
 assertEquals(2, stdErrors.getColumnDimension());
 assertEquals(0.3, stdErrors.getEntry(0, 0), 0.01); // rounding error
 assertEquals(0.3, stdErrors.getEntry(1, 1), 0.01);
 }

 @Test
 public void testCorrelationPValues() throws Exception {
 RealMatrix correlationMatrix = new BlockRealMatrix(new double[][]{{1.0, 0.9}, {0.9, 1.0}});
 PearsonsCorrelation pcc = new PearsonsCorrelation(correlationMatrix);
 RealMatrix pValues = pcc.getCorrelationPValues();
 assertEquals(2, pValues.getRowDimension());
 assertEquals(2, pValues.getColumnDimension());
 assertEquals(1.0 / 3.0, pValues.getEntry(0, 0), 0.01); // rounding error
 assertEquals(1.0 / 3.0, pValues.getEntry(1, 1), 0.01);
 }

 @Test
 public void testComputeCorrelationMatrix() {
 double[][] testData = new double[][]{{1.0, 2.0, 3.0}, {2.1, 3.1, 4.1}, {3.2, 4.2, 5.2}};
 RealMatrix correlationMatrix = new BlockRealMatrix(new double[][]{{1.0, 0.941, 0.963}, {0.941, 1.0, 0.963}, {0.963, 0.963, 1.0}});
 PearsonsCorrelation pcc = new PearsonsCorrelation();
 RealMatrix result = pcc.computeCorrelationMatrix(testData);
 assertEquals(3, result.getRowDimension());
 assertEquals(3, result.getColumnDimension());
 assertEquals(correlationMatrix, result);
 }

 @Test
 public void testComputeCorrelationMatrixWithMatrix() {
 RealMatrix testMatrix = new BlockRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.1, 3.1, 4.1}, {3.2, 4.2, 5.2}});
 RealMatrix correlationMatrix = new BlockRealMatrix(new double[][]{{1.0, 0.941, 0.963}, {0.941, 1.0, 0.963}, {0.963, 0.963, 1.0}});
 PearsonsCorrelation pcc = new PearsonsCorrelation();
 RealMatrix result = pcc.computeCorrelationMatrix(testMatrix);
 assertEquals(3, result.getRowDimension());
 assertEquals(3, result.getColumnDimension());
 assertEquals(correlationMatrix, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidData() {
 double[][] testData = new double[][]{{1.0}, {2.0}}; // invalid data
 PearsonsCorrelation pcc = new PearsonsCorrelation(testData);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMatrixWithInvalidData() {
 RealMatrix testMatrix = new BlockRealMatrix(new double[][]{{1.0, 2.0}, {2.1, 3.0}, {3.2, 5.0}}); // invalid data
 PearsonsCorrelation pcc = new PearsonsCorrelation(testMatrix);
 }
}