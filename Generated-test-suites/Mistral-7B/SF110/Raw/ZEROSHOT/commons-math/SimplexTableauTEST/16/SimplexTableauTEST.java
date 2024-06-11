import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.optim.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexTableauTest {

 private SimplexTableau createSimplexTableau() {
 LinearObjectiveFunction f = new QuadraticObjectiveFunction(new double[][]{{-1.0, 2.0}, {3.0, 1.0}}, 2.0);
 LinearConstraint constraint1 = new LinearConstraint(new ArrayRealVector(new double[]{-2.0, 1.0}), Relationship.LEQ, -1.0);
 LinearConstraint constraint2 = new LinearConstraint(new ArrayRealVector(new double[]{-3.0, 1.0}), Relationship.GEQ, 1.0);
 LinearConstraint constraints[] = {constraint1, constraint2};
 return new SimplexTableau(f, Arrays.asList(constraints), GoalType.MAXIMIZE, false, 1e-12);
 }

 @Test
 public void testInitializeColumnLabels() {
 SimplexTableau tableau = createSimplexTableau();
 List<String> expectedColumnLabels = Arrays.asList("W", "Z", "x0", "x1", "x-", "s0", "s1", "a0", "a1", "RHS");
 assertEquals(expectedColumnLabels, tableau.columnLabels);
 }

 @Test
 public void testCreateTableau() {
 SimplexTableau tableau = createSimplexTableau();
 RealMatrix expectedTableau = new Array2DRowRealMatrix(new double[][]{
 // W, Z, x0, x1, x-, s0, s1, a0, a1, RHS
 {1.0, 0.0, -1.0, 2.0, -1.0, 1.0, 1.0, 1.0, 1.0, -2.0},
 {0.0, 1.0, 3.0, 1.0, 0.0, 1.0, 0.0, 0.0, 0.0, 1.0},
 {-1.0, -1.0, -1.0, 2.0, 1.0, 0.0, 0.0, -1.0, -1.0, 0.0}
 });
 assertEquals(expectedTableau, tableau.tableau);
 }

 @Test
 public void testNormalizeConstraints() {
 SimplexTableau tableau = createSimplexTableau();
 LinearConstraint[] originalConstraints = tableau.constraints.toArray(new LinearConstraint[0]);
 LinearConstraint[] expectedNormalizedConstraints = new LinearConstraint[]{
 new LinearConstraint(new ArrayRealVector(new double[]{-1.0, 1.0}), Relationship.LEQ, 1.0),
 new LinearConstraint(new ArrayRealVector(new double[]{-3.0, 1.0}), Relationship.GEQ, -1.0)
 };
 assertArrayEquals(expectedNormalizedConstraints, tableau.normalizeConstraints(Arrays.asList(originalConstraints)).toArray(new LinearConstraint[0]));
 }

 @Test
 public void testGetNumObjectiveFunctions() {
 SimplexTableau tableau = createSimplexTableau();
 assertEquals(tableau.getNumObjectiveFunctions(), 2);
 }

 @Test
 public void testGetConstraintTypeCounts() {
 SimplexTableau tableau = createSimplexTableau();
 assertEquals(tableau.getConstraintTypeCounts(Relationship.LEQ), 1);
 assertEquals(tableau.getConstraintTypeCounts(Relationship.GEQ), 1);
 assertEquals(tableau.getConstraintTypeCounts(Relationship.EQ), 0);
 }

 @Test
 public void testGetInvertedCoefficientSum() {
 RealVector coefficients = new ArrayRealVector(new double[]{-1.0, 2.0});
 double expected = -(coefficients.getDimension() + 1);
 assertEquals(expected, SimplexTableau.getInvertedCoefficientSum(coefficients));
 }

 @Test
 public void testGetBasicRow() {
 SimplexTableau tableau = createSimplexTableau();
 int col = 0;
 Integer expectedRow = 2;
 Integer actualRow = tableau.getBasicRow(col);
 assertEquals(expectedRow, actualRow);
 }

 @Test
 public void testIsOptimal() {
 SimplexTableau tableau = createSimplexTableau();
 PointValuePair solution = tableau.getSolution();
 tableau.setEntry(1, 0, solution.getPoint()[0]); // Set the value of x0 to the optimal solution value
 assertTrue(tableau.isOptimal());
 }

 @Test
 public void testGetSolution() {
 SimplexTableau tableau = createSimplexTableau();
 PointValuePair solution = tableau.getSolution();
 double[] expectedSolutionPoint = new double[]{0.0, 3.0};
 double expectedSolutionValue = 3.0;
 assertArrayEquals(expectedSolutionPoint, solution.getPoint(), 1e-12);
 assertEquals(expectedSolutionValue, solution.getValue(), 1e-12);
 }
}