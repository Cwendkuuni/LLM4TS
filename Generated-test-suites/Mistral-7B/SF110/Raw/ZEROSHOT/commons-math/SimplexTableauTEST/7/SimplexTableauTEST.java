import org.apache.commons.math3.optim.linear.*;
import org.apache.commons.math3.optim.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.linear.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexTableauTest {

 @Test
 public void testSimplexTableauConstruction() {
 SimplexTableau st = new SimplexTableau(new LinearObjectiveFunction() {
 @Override
 public double value(RealVector x) {
 return x.dotProduct(new ArrayRealVector(new double[] { 1.0, 2.0, 3.0 }));
 }

 @Override
 public RealVector gradient(RealVector x) {
 return new ArrayRealVector(new double[] { 1.0, 2.0, 3.0 });
 }
 }, Arrays.asList(
 new LinearConstraint(new Array2DRowRealMatrix(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } }), Relationship.LEQ, 5.0),
 new LinearConstraint(new Array2DRowRealMatrix(new double[][] { { 1.0, -1.0 }, { -1.0, 2.0 } }), Relationship.GEQ, 1.0)
 ), GoalType.MINIMIZE, false, 1e-5);

 assertEquals(3, st.getNumDecisionVariables());
 assertEquals(2, st.getNumSlackVariables());
 assertEquals(1, st.getNumArtificialVariables());
 assertEquals(11, st.getWidth());
 assertEquals(3, st.getHeight());
 }

 @Test
 public void testInitializeColumnLabels() {
 SimplexTableau st = new SimplexTableau(new LinearObjectiveFunction() {
 @Override
 public double value(RealVector x) {
 return x.dotProduct(new ArrayRealVector(new double[] { 1.0, 2.0, 3.0 }));
 }

 @Override
 public RealVector gradient(RealVector x) {
 return new ArrayRealVector(new double[] { 1.0, 2.0, 3.0 });
 }
 }, Arrays.asList(
 new LinearConstraint(new Array2DRowRealMatrix(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } }), Relationship.LEQ, 5.0),
 new LinearConstraint(new Array2DRowRealMatrix(new double[][] { { 1.0, -1.0 }, { -1.0, 2.0 } }), Relationship.GEQ, 1.0)
 ), GoalType.MINIMIZE, false, 1e-5);

 List<String> columnLabels = st.columnLabels;
 assertEquals(3, columnLabels.size());
 assertEquals("x0", columnLabels.get(0));
 assertEquals("x1", columnLabels.get(1));
 assertEquals("x2", columnLabels.get(2));
 if (!st.restrictToNonNegative) {
 assertEquals("x-", columnLabels.get(3));
 }
 assertEquals("s0", columnLabels.get(4));
 assertEquals("s1", columnLabels.get(5));
 assertEquals("a0", columnLabels.get(6));
 assertEquals("a1", columnLabels.get(7));
 assertEquals("RHS", columnLabels.get(10));
 }

 @Test
 public void testCreateTableau() {
 SimplexTableau st = new SimplexTableau(new LinearObjectiveFunction() {
 @Override
 public double value(RealVector x) {
 return x.dotProduct(new ArrayRealVector(new double[] { 1.0, 2.0, 3.0 }));
 }

 @Override
 public RealVector gradient(RealVector x) {
 return new ArrayRealVector(new double[] { 1.0, 2.0, 3.0 });
 }
 }, Arrays.asList(
 new LinearConstraint(new Array2DRowRealMatrix(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } }), Relationship.LEQ, 5.0),
 new LinearConstraint(new Array2DRowRealMatrix(new double[][] { { 1.0, -1.0 }, { -1.0, 2.0 } }), Relationship.GEQ, 1.0)
 ), GoalType.MINIMIZE, false, 1e-5);

 RealMatrix tableau = st.tableau;
 assertEquals(11, tableau.getColumnDimension());
 assertEquals(11, tableau.getRowDimension());
 double[][] data = tableau.getData();
 // First row (objective function)
 assertArrayEquals(new double[] { -1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 1.0, 1.0, 0.0, 0.0, 5.0 }, data[0], 0.001);
 // Second row (constraint 1)
 assertArrayEquals(new double[] { 1.0, 2.0, -1.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 5.0 }, data[1], 0.001);
 // Third row (constraint 2)
 assertArrayEquals(new double[] { 1.0, -1.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0 }, data[2], 0.001);
 }

 @Test
 public void testIsOptimal() {
 SimplexTableau st = new SimplexTableau(new LinearObjectiveFunction() {
 @Override
 public double value(RealVector x) {
 return x.dotProduct(new ArrayRealVector(new double[] { 1.0, 2.0, 3.0 }));
 }

 @Override
 public RealVector gradient(RealVector x) {
 return new ArrayRealVector(new double[] { 1.0, 2.0, 3.0 });
 }
 }, Arrays.asList(
 new LinearConstraint(new Array2DRowRealMatrix(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } }), Relationship.LEQ, 5.0),
 new LinearConstraint(new Array2DRowRealMatrix(new double[][] { { 1.0, -1.0 }, { -1.0, 2.0 } }), Relationship.GEQ, 1.0)
 ), GoalType.MINIMIZE, false, 1e-5);

 assertTrue(st.isOptimal());

 st.setEntry(1, 1, 10.0); // modify tableau
 assertFalse(st.isOptimal());
 }

 @Test
 public void testGetSolution() {
 SimplexTableau st = new SimplexTableau(new LinearObjectiveFunction() {
 @Override
 public double value(RealVector x) {
 return x.dotProduct(new ArrayRealVector(new double[] { 1.0, 2.0, 3.0 }));
 }

 @Override
 public RealVector gradient(RealVector x) {
 return new ArrayRealVector(new double[] { 1.0, 2.0, 3.0 });
 }
 }, Arrays.asList(
 new LinearConstraint(new Array2DRowRealMatrix(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } }), Relationship.LEQ, 5.0),
 new LinearConstraint(new Array2DRowRealMatrix(new double[][] { { 1.0, -1.0 }, { -1.0, 2.0 } }), Relationship.GEQ, 1.0)
 ), GoalType.MINIMIZE, false, 1e-5);

 PointValuePair solution = st.getSolution();
 RealVector coefficients = solution.getPoint();
 assertEquals(3, coefficients.getDimension());
 assertEquals(0.4166666872599347, coefficients.getEntry(0), 0.001);
 assertEquals(1.333333312740955, coefficients.getEntry(1), 0.001);
 assertEquals(1.6666666222065393, coefficients.getEntry(2), 0.001);
 double optimalValue = solution.getValue();
 assertEquals(-6.999999999999998, optimalValue, 0.001);
 }
}