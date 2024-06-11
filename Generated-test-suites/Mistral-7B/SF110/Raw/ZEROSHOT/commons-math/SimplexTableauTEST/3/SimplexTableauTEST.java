import org.apache.commons.math3.optim.linear.*;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexTableauTest {

 private LinearObjectiveFunction mockObjectiveFunction;
 private LinearConstraint mockConstraint1;
 private LinearConstraint mockConstraint2;
 private LinearConstraint mockConstraint3;
 private List<LinearConstraint> mockConstraints;
 private SimplexTableau simplexTableau;

 @Before
 public void setUp() {
 mockObjectiveFunction = new LinearObjectiveFunction(new ArrayRealVector(new double[]{1.0, 2.0}));
 mockConstraint1 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{new double[]{1.0, -2.0}, new double[]{1.0, 1.0}, new double[]{0.0, -1.0}}), Relationship.LEQ, 1.0);
 mockConstraint2 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{new double[]{1.0, 3.0}, new double[]{1.0, 1.0}, new double[]{0.0, 2.0}}), Relationship.GEQ, 2.0);
 mockConstraint3 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{new double[]{1.0, 1.0}, new double[]{1.0, 1.0}, new double[]{0.0, -1.0}}), Relationship.EQ, 0.0);
 mockConstraints = new ArrayList<>();
 mockConstraints.add(mockConstraint1);
 mockConstraints.add(mockConstraint2);
 mockConstraints.add(mockConstraint3);
 simplexTableau = new SimplexTableau(mockObjectiveFunction, mockConstraints, GoalType.MAXIMIZE, false, 1e-6, 10);
 }

 @Test
 public void testInitializeColumnLabels() {
 int numColumns = simplexTableau.getWidth();
 assertEquals("W", simplexTableau.columnLabels.get(0));
 assertEquals("Z", simplexTableau.columnLabels.get(1));
 for (int i = 0; i < simplexTableau.getOriginalNumDecisionVariables(); ++i) {
 Equals("x" + i, simplexTableau.columnLabels.get(i + 2));
 }
 if (!simplexTableau.restrictToNonNegative) {
 assertEquals("x-", simplexTableau.columnLabels.get(simplextableau.getWidth() - 1));
 }
 for (int i = 0; i < simplexTableau.getNumSlackVariables(); ++i) {
 Equals("s" + i, simplexTableau.columnLabels.get(simplextableau.getWidth() - 2 - i));
 }
 for (int i = 0; i < simplexTableau.getNumArtificialVariables(); ++i) {
 Equals("a" + i, simplexTableau.columnLabels.get(simplextableau.getWidth() - 2 - simplexTableau.getNumSlackVariables() - i));
 }
 assertEquals("RHS", simplexTableau.columnLabels.get(simplextableau.getWidth() - 1));
 }

 @Test
 public void testCreateTableau() {
 int numRows = simplexTableau.getHeight();
 int numColumns = simplexTableau.getWidth();
 RealMatrix tableau = simplexTableau.getData();
 assertEquals(numRows, tableau.getRowDimension());
 assertEquals(numColumns, tableau.getColumnDimension());
 // Checking the first row (objective function)
 assertEquals(-1.0, tableau.getEntry(0, 0), 1e-8);
 if (simplexTableau.getNumObjectiveFunctions() == 2) {
 assertEquals(1.0, tableau.getEntry(0, 1), 1e-8);
 }
 for (int i = 0; i < simplexTableau.getOriginalNumDecisionVariables(); ++i) {
 assertEquals(-mockObjectiveFunction.getCoefficients().getEntry(i), tableau.getEntry(0, i + 1), 1e-8);
 }
 if (!simplexTableau.restrictToNonNegative) {
 assertEquals(-getInvertedCoefficientSum(mockObjectiveFunction.getCoefficients()), tableau.getEntry(0, numColumns - 1), 1e-8);
 }
 assertEquals(-mockObjectiveFunction.getConstantTerm(), tableau.getEntry(0, numColumns), 1e-8);
 // Checking the remaining rows (constraints)
 for (int i = 0; i < simplexTableau.constraints.size(); ++i) {
 LinearConstraint constraint = simplexTableau.constraints.get(i);
 int row = simplexTableau.getNumObjectiveFunctions() + i;
 // Checking constraint coefficients
 for (int j = 0; j < constraint.getCoefficients().getDimension(); ++j) {
 assertEquals(constraint.getCoefficients().getEntry(j), tableau.getEntry(row, j), 1e-8);
 }
 // Checking constraint relationships
 switch (constraint.getRelationship()) {
 case LEQ:
 assertEquals(1.0, tableau.getEntry(row, numColumns - simplexTableau.getNumSlackVariables() - i), 1e-8);
 break;
 case GEQ:
 assertEquals(-1.0, tableau.getEntry(row, numColumns - simplexTableau.getNumSlackVariables() - i), 1e-8);
 break;
 case EQ:
 assertEquals(0.0, tableau.getEntry(row, numColumns - simplexTableau.getNumSlackVariables() - i), 1e-8);
 break;
 }
 assertEquals(constraint.getValue(), tableau.getEntry(row, numColumns - 1), 1e-8);
 }
 }

 @Test
 public void testIsOptimal() {
 assertTrue(simplexTableau.isOptimal());
 }

 @Test
 public void testGetSolution() {
 PointValuePair solution = simplexTableau.getSolution();
 assertEquals(3.0, solution.getPoint()[0], 1e-6);
 assertEquals(5.0, solution.getPoint()[1], 1e-6);
 assertEquals(3.5, solution.getValue(), 1e-6);
 }

 @Test
 public void testDropPhase1Objective() {
 simplexTableau.dropPhase1Objective();
 int numColumns = simplexTableau.getWidth();
 RealMatrix tableau = simplexTableau.getData();
 assertEquals(simplexTableau.getHeight() - 1, tableau.getRowDimension());
 assertEquals(numColumns - 3, tableau.getColumnDimension());
 for (int i = 0; i < simplexTableau.getNumObjectiveFunctions() - 1; ++i) {
 for (int j = 0; j < simplexTableau.getOriginalNumDecisionVariables(); ++j) {
 assertEquals(tableau.getEntry(i, j), simplexTableau.getData().getEntry(i + 1, j), 1e-8);
 }
 }
 for (int i = 0; i < simplexTableau.getNumSlackVariables(); ++i) {
 assertEquals(tableau.getEntry(simplexTableau.getHeight() - 1, i), simplexTableau.getData().getEntry(0, simplexTableau.getArtificialVariableOffset() + i), 1e-8);
 }
 }

 private static double getInvertedCoefficientSum(RealVector coefficients) {
 double sum = 0.0;
 for (double coefficient : coefficients.toArray()) {
 sum -= coefficient;
 }
 return sum;
 }

}