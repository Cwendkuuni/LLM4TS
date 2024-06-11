import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.linear.*;
import org.apache.commons.math3.linear.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexTableauTest {

 private SimplexTableau tableau;

 @Test
 public void testConstructorWithObjectiveFunctionAndConstraints() {
 LinearObjectiveFunction objectiveFunction = new QuadraticObjectiveFunction(new QuadraticForm(new double[][]{{1.0, -1.0}, {-1.0, 1.0}}, new double[]{0.0, 0.0}));
 List<LinearConstraint> constraints = new ArrayList<>();
 LinearConstraint constraint1 = new LinearConstraint(new ArrayRealVector(new double[]{1.0, -1.0}), Relationship.LEQ, 0.0);
 LinearConstraint constraint2 = new LinearConstraint(new ArrayRealVector(new double[]{-1.0, 1.0}), Relationship.GEQ, 0.0);
 constraints.add(constraint1);
 constraints.add(constraint2);
 tableau = new SimplexTableau(objectiveFunction, constraints, GoalType.MINIMIZE, false, 1e-6);
 }

 @Test
 public void testConstructorWithObjectiveFunctionAndEmptyConstraints() {
 LinearObjectiveFunction objectiveFunction = new QuadraticObjectiveFunction(new QuadraticForm(new double[][]{{1.0, -1.0}, {-1.0, 1.0}}, new double[]{0.0, 0.0}));
 tableau = new SimplexTableau(objectiveFunction, Collections.emptyList(), GoalType.MINIMIZE, false, 1e-6);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNullObjectiveFunction() {
 LinearObjectiveFunction objectiveFunction = null;
 List<LinearConstraint> constraints = new ArrayList<>();
 tableau = new SimplexTableau(objectiveFunction, constraints, GoalType.MINIMIZE, false, 1e-6);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNullConstraints() {
 LinearObjectiveFunction objectiveFunction = new QuadraticObjectiveFunction(new QuadraticForm(new double[][]{{1.0, -1.0}, {-1.0, 1.0}}, new double[]{0.0, 0.0}));
 List<LinearConstraint> constraints = null;
 tableau = new SimplexTableau(objectiveFunction, constraints, GoalType.MINIMIZE, false, 1e-6);
 }

 @Test
 public void testInitializeColumnLabels() {
 LinearObjectiveFunction objectiveFunction = new QuadraticObjectiveFunction(new QuadraticForm(new double[][]{{1.0, -1.0}, {-1.0, 1.0}}, new double[]{0.0, 0.0}));
 List<LinearConstraint> constraints = new ArrayList<>();
 LinearConstraint constraint1 = new LinearConstraint(new ArrayRealVector(new double[]{1.0, -1.0}), Relationship.LEQ, 0.0);
 LinearConstraint constraint2 = new LinearConstraint(new ArrayRealVector(new double[]{-1.0, 1.0}), Relationship.GEQ, 0.0);
 constraints.add(constraint1);
 constraints.add(constraint2);
 tableau = new SimplexTableau(objectiveFunction, constraints, GoalType.MINIMIZE, false, 1e-6);
 String[] expectedColumnLabels = {"W", "Z", "x0", "x1", "x-"};
 assertArrayEquals(expectedColumnLabels, tableau.columnLabels.toArray(expectedColumnLabels));
 }

 @Test
 public void testCreateTableau() {
 LinearObjectiveFunction objectiveFunction = new QuadraticObjectiveFunction(new QuadraticForm(new double[][]{{1.0, -1.0}, {-1.0, 1.0}}, new double[]{0.0, 0.0}));
 List<LinearConstraint> constraints = new ArrayList<>();
 LinearConstraint constraint1 = new LinearConstraint(new ArrayRealVector(new double[]{1.0, -1.0}), Relationship.LEQ, 0.0);
 LinearConstraint constraint2 = new LinearConstraint(new ArrayRealVector(new double[]{-1.0, 1.0}), Relationship.GEQ, 0.0);
 constraints.add(constraint1);
 constraints.add(constraint2);
 tableau = new SimplexTableau(objectiveFunction, constraints, GoalType.MINIMIZE, false, 1e-6);
 RealMatrix expectedTableau = new Array2DRowRealMatrix(new double[][]{
            {-1.0, 1.0, 1.0, -1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
            {0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, -1.0, -1.0, -1.0, 1.0},
            {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, 1.0, 0.0},
            {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
            {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0}
 });
 RealMatrix tableauMatrix = tableau.getData();
 assertArrayEquals(expectedTableau.getData(), tableauMatrix.getData(), 1e-6);
 }

 @Test
 public void testNormalizeConstraints() {
 LinearObjectiveFunction objectiveFunction = new QuadraticObjectiveFunction(new QuadraticForm(new double[][]{{1.0, -1.0}, {-1.0, 1.0}}, new double[]{0.0, 0.0}));
 List<LinearConstraint> originalConstraints = new ArrayList<>();
 LinearConstraint constraint1 = new LinearConstraint(new ArrayRealVector(new double[]{1.0, -1.0}), Relationship.LEQ, 0.0);
 LinearConstraint constraint2 = new LinearConstraint(new ArrayRealVector(new double[]{-1.0, 1.0}), Relationship.GEQ, 0.0);
 originalConstraints.add(constraint1);
 originalConstraints.add(constraint2);
 tableau = new SimplexTableau(objectiveFunction, originalConstraints, GoalType.MINIMIZE, false, 1e-6);
 List<LinearConstraint> normalizedConstraints = tableau.normalizeConstraints(originalConstraints);
 LinearConstraint expectedConstraint1 = new LinearConstraint(new ArrayRealVector(new double[]{1.0, 1.0}), Relationship.GEQ, 0.0);
 LinearConstraint expectedConstraint2 = new LinearConstraint(new ArrayRealVector(new double[]{-1.0, -1.0}), Relationship.LEQ, 0.0);
 assertTrue(normalizedConstraints.get(0).equals(expectedConstraint1));
 assertTrue(normalizedConstraints.get(1).equals(expectedConstraint2));
 }

 @Test
 public void testGetNumObjectiveFunctions() {
 LinearObjectiveFunction objectiveFunction = new QuadraticObjectiveFunction(new QuadraticForm(new double[][]{{1.0, -1.0}, {-1.0, 1.0}}, new double[]{0.0, 0.0}));
 List<LinearConstraint> constraints = new ArrayList<>();
 tableau = new SimplexTableau(objectiveFunction, constraints, GoalType.MINIMIZE, false, 1e-6);
 assertEquals(1, tableau.getNumObjectiveFunctions());
 LinearObjectiveFunction objectiveFunction2 = new QuadraticObjectiveFunction(new QuadraticForm(new double[][]{{1.0, -1.0}, {-1.0, 1.0}, {1.0, 1.0}}, new double[]{0.0, 0.0, 0.0}));
 constraints.clear();
 tableau = new SimplexTableau(objectiveFunction2, constraints, GoalType.MINIMIZE, false, 1e-6);
 assertEquals(2, tableau.getNumObjectiveFunctions());
 }

 @Test
 public void testGetConstraintTypeCounts() {
 LinearObjectiveFunction objectiveFunction = new QuadraticObjectiveFunction(new QuadraticForm(new double[][]{{1.0, -1.0}, {-1.0, 1.0}}, new double[]{0.0, 0.0}));
 List<LinearConstraint> constraints = new ArrayList<>();
 LinearConstraint constraint1 = new LinearConstraint(new ArrayRealVector(new double[]{1.0, -1.0}), Relationship.LEQ, 0.0);
 LinearConstraint constraint2 = new LinearConstraint(new ArrayRealVector(new double[]{-1.0, 1.0}), Relationship.GEQ, 0.0);
 LinearConstraint constraint3 = new LinearConstraint(new ArrayRealVector(new double[]{1.0, 1.0}), Relationship.EQ, 0.0);
 constraints.add(constraint1);
 constraints.add(constraint2);
 constraints.add(constraint3);
 tableau = new SimplexTableau(objectiveFunction, constraints, GoalType.MINIMIZE, false, 1e-6);
 assertEquals(2, tableau.getConstraintTypeCounts(Relationship.LEQ));
 assertEquals(1, tableau.getConstraintTypeCounts(Relationship.GEQ));
 assertEquals(1, tableau.getConstraintTypeCounts(Relationship.EQ));
 }

 @Test
 public void testGetInvertedCoefficientSum() {
 RealVector objectiveCoefficients = new ArrayRealVector(new double[]{1.0, -1.0});
 double[] coefficients = objectiveCoefficients.toArray();
 double expected = -(coefficients[0] + coefficients[1]);
 double result = SimplexTableau.getInvertedCoefficientSum(objectiveCoefficients);
 assertEquals(expected, result, 1e-6);
 }

 @Test
 public void testGetBasicRow() {
 LinearObjectiveFunction objectiveFunction = new QuadraticObjectiveFunction(new QuadraticForm(new double[][]{{1.0, -1.0}, {-1.0, 1.0}}, new double[]{0.0, 0.0}));
 List<LinearConstraint> constraints = new ArrayList<>();
 LinearConstraint constraint1 = new LinearConstraint(new ArrayRealVector(new double[]{1.0, -1.0}), Relationship.LEQ, 0.0);
 LinearConstraint constraint2 = new LinearConstraint(new ArrayRealVector(new double[]{-1.0, 1.0}), Relationship.GEQ, 0.0);
 constraints.add(constraint1);
 constraints.add(constraint2);
 tableau = new SimplexTableau(objectiveFunction, constraints, GoalType.MINIMIZE, false, 1e-6);
 int numColumns = tableau.getWidth();
 int numRows = tableau.getHeight();
 for (int i = 0; i < numRows; ++i) {
 int col = 0;
 Integer basicRow = tableau.getBasicRow(col);
 if (basicRow != null) {
 assertEquals(i, basicRow);
 }
 col++;
 }
 }

 @Test
 public void testIsOptimal() {
 LinearObjectiveFunction objectiveFunction = new QuadraticObjectiveFunction(new QuadraticForm(new double[][]{{1.0, -1.0}, {-1.0, 1.0}}, new double[]{0.0, 0.0}));
 List<LinearConstraint> constraints = new ArrayList<>();
 LinearConstraint constraint1 = new LinearConstraint(new ArrayRealVector(new double[]{1.0, -1.0}), Relationship.LEQ, 0.0);
 LinearConstraint constraint2 = new LinearConstraint(new ArrayRealVector(new double[]{-1.0, 1.0}), Relationship.GEQ, 0.0);
 constraints.add(constraint1);
 constraints.add(constraint2);
 tableau = new SimplexTableau(objectiveFunction, constraints, GoalType.MINIMIZE, false, 1e-6);
 double[][] data = tableau.getData().getData();
 double optimalValue = objectiveFunction.value(new ArrayRealVector(new double[]{data[0][numDecisionVariables], data[0][numDecisionVariables + numSlackVariables]}));
 tableau.isOptimal();
 assertEquals(optimalValue, tableau.getSolution().getValue(), 1e-6);
 }

 @Test
 public void testGetSolution() {
 LinearObjectiveFunction objectiveFunction = new QuadraticObjectiveFunction(new QuadraticForm(new double[][]{{1.0, -1.0}, {-1.0, 1.0}}, new double[]{0.0, 0.0}));
 List<LinearConstraint> constraints = new ArrayList<>();
 LinearConstraint constraint1 = new LinearConstraint(new ArrayRealVector(new double[]{1.0, -1.0}), Relationship.LEQ, 0.0);
 LinearConstraint constraint2 = new LinearConstraint(new ArrayRealVector(new double[]{-1.0, 1.0}), Relationship.GEQ, 0.0);
 constraints.add(constraint1);
 constraints.add(constraint2);
 tableau = new SimplexTableau(objectiveFunction, constraints, GoalType.MINIMIZE, false, 1e-6);
 PointValuePair solution = tableau.getSolution();
 double[] coefficients = solution.getPoint().toArray();
 double optimalValue = objectiveFunction.value(solution.getPoint());
 assertEquals(coefficients.length, tableau.getOriginalNumDecisionVariables());
 assertArrayEquals(new double[]{0.0, 0.0}, coefficients, 1e-6);
 assertEquals(optimalValue, solution.getValue(), 1e-6);
 }

 @Test
 public void testDropPhase1Objective() {
 LinearObjectiveFunction objectiveFunction = new QuadraticObjectiveFunction(new QuadraticForm(new double[][]{{1.0, -1.0}, {-1.0, 1.0}}, new double[]{0.0, 0.0}));
 List<LinearConstraint> constraints = new ArrayList<>();
 LinearConstraint constraint1 = new LinearConstraint(new ArrayRealVector(new double[]{1.0, -1.0}), Relationship.LEQ, 0.0);
 LinearConstraint constraint2 = new LinearConstraint(new ArrayRealVector(new double[]{-1.0, 1.0}), Relationship.GEQ, 0.0);
 constraints.add(constraint1);
 constraints.add(constraint2);
 tableau = new SimplexTableau(objectiveFunction, constraints, GoalType.MINIMIZE, false, 1e-6);
 tableau.dropPhase1Objective();
 int numRows = tableau.getHeight();
 int numColumns = tableau.getWidth();
 assertFalse(tableau.isOptimal());
 assertEquals(numRows - 1, tableau.getHeight());
 assertEquals(numColumns - 1, tableau.getWidth());
 }
}