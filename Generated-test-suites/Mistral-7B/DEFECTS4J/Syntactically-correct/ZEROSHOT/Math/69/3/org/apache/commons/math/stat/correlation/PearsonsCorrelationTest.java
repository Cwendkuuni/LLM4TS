import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.junit.Test;
import static org.junit.Assert.*;

public class PearsonsCorrelationTest {

 @Test
 public void testEmptyConstructor() {
 PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation();
 assertNull(pearsonCorrelation.getCorrelationMatrix());
 assertEquals(0, pearsonCorrelation.nObs);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullDataConstructor() {
 new PearsonsCorrelation(null);
 }

 @Test
 public void testTwoByTwoDataConstructor() {
 double[][] data = {{1.0, 2.0}, {2.0, 3.0}};
 PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(data);
 RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();
 assertNotNull(correlationMatrix);
 assertEquals(2, correlationMatrix.getRowDimension());
 assertEquals(2, correlationMatrix.getColumnDimension());
 assertEquals(1.0, correlationMatrix.getEntry(0, 0), 0.001);
 assertEquals(0.8, correlationMatrix.getEntry(0, 1), 0.001);
 assertEquals(0.8, correlationMatrix.getEntry(1, 0), 0.001);
 assertEquals(0.64, correlationMatrix.getEntry(1, 1), 0.001);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRectangularDataConstructor() {
 double[][] data = {{1.0, 2.0}, {2.0}};
 new PearsonsCorrelation(data);
 }

 @Test
 public void testCovarianceConstructor() {
 RealMatrix covarianceMatrix = new BlockRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
 PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(covarianceMatrix, 4);
 RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();
 assertNotNull(correlationMatrix);
 assertEquals(2, correlationMatrix.getRowDimension());
 assertEquals(2, correlationMatrix.getColumnDimension());
 assertEquals(1.0, correlationMatrix.getEntry(0, 0), 0.001);
 assertEquals(0.8, correlationMatrix.getEntry(0, 1), 0.001);
 assertEquals(0.8, correlationMatrix.getEntry(1, 0), 0.001);
 assertEquals(0.64, correlationMatrix.getEntry(1, 1), 0.001);
 }

 @Test
 public void testCorrelationMatrixGetters() {
 PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(new BlockRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}}));
 RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();
 assertNotNull(correlationMatrix);
 assertEquals(2, correlationMatrix.getRowDimension());
 assertEquals(2, correlationMatrix.getColumnDimension());
 }

 @Test
 public void testCorrelationStandardErrorsGetter() {
 PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(new BlockRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}}));
 RealMatrix correlationStandardErrors = pearsonCorrelation.getCorrelationStandardErrors();
 assertNotNull(correlationStandardErrors);
 assertEquals(2, correlationStandardErrors.getRowDimension());
 assertEquals(2, correlationStandardErrors.getColumnDimension());
 assertEquals(0.3873, correlationStandardErrors.getEntry(0, 0), 0.001);
 assertEquals(0.4495, correlationStandardErrors.getEntry(0, 1), 0.001);
 assertEquals(0.3873, correlationStandardErrors.getEntry(1, 0), 0.001);
 assertEquals(0.4495, correlationStandardErrors.getEntry(1, 1), 0.001);
 }

 @Test
 public void testCorrelationPValuesGetter() throws Exception {
 PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(new BlockRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}}));
 RealMatrix correlationPValues = pearsonCorrelation.getCorrelationPValues();
 assertNotNull(correlationPValues);
 assertEquals(2, correlationPValues.getRowDimension());
 assertEquals(2, correlationPValues.getColumnDimension());
 assertEquals(0.1227, correlationPValues.getEntry(0, 0), 0.001);
 assertEquals(0.1227, correlationPValues.getEntry(1, 0), 0.001);
 assertEquals(0.4571, correlationPValues.getEntry(0, 1), 0.001);
 assertEquals(0.4571, correlationPValues.getEntry(1, 1), 0.001);
 }

 @Test
 public void testCorrelationMethod() {
 double[] xArray = {1.0, 2.0, 3.0};
 double[] yArray = {2.0, 3.0, 4.0};
 double correlation = new PearsonsCorrelation().correlation(xArray, yArray);
 assertEquals(0.8, correlation, 0.001);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidArrayLengthsForCorrelationMethod() {
 double[] xArray = {1.0};
 double[] yArray = {2.0, 3.0};
 new PearsonsCorrelation().correlation(xArray, yArray);
 }
}