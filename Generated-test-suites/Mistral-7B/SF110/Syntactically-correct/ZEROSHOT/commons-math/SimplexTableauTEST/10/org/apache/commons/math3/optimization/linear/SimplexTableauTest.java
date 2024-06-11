import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.linear.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.linear.*;
import java.util.*;
import junit.framework.*;

public class SimplexTableauTest {

 private final SimplexTableau simplexTableau;

 public SimplexTableauTest() {
 LinearObjectiveFunction function = new LinearObjectiveFunction(new RealVector(new double[]{-3.0, 2.0}));
 LinearConstraint constraint1 = new LinearConstraint(new RealVector(new double[]{1.0, 1.0}), Relationship.LEQ, 1.0);
 LinearConstraint constraint2 = new LinearConstraint(new RealVector(new double[]{1.0, -1.0}), Relationship.LEQ, 2.0);
 GoalType goalType = GoalType.MAXIMIZE;
 boolean restrictToNonNegative = false;
 double epsilon = 1e-6;
 int maxUlps = 10;

 List<LinearConstraint> constraints = new ArrayList<>();
 constraints.add(constraint1);
 constraints.add(constraint2);

 simplexTableau = new SimplexTableau(function, constraints, goalType, restrictToNonNegative, epsilon, maxUlps);
 }

 @Test
 public void testCreateTableau() {
 // Check the dimensions of the tableau
 Assert.assertEquals(simplexTableau.getHeight(), 3);
 Assert.assertEquals(simplexTableau.getWidth(), 6);
 }

 @Test
 public void testInitializeColumnLabels() {
 List<String> expectedColumnLabels = Arrays.asList(
 "W", "Z", "x0", "x1", "x-", "s0", "s1", "a0", "a1", "RHS"
 );
 List<String> actualColumnLabels = new ArrayList<>(simplexTableau.columnLabels);
 Collections.sort(expectedColumnLabels);
 Collections.sort(actualColumnLabels);
 Assert.assertEquals(expectedColumnLabels, actualColumnLabels);
 }

 @Test
 public void testGetNumObjectiveFunctions() {
 Assert.assertEquals(simplexTableau.getNumObjectiveFunctions(), 1);
 }

 @Test
 public void testGetConstraintTypeCounts() {
 Assert.assertEquals(simplexTableau.getConstraintTypeCounts(Relationship.LEQ), 2);
 Assert.assertEquals(simplexTableau.getConstraintTypeCounts(Relationship.GEQ), 0);
 Assert.assertEquals(simplexTableau.getConstraintTypeCounts(Relationship.EQ), 0);
 }

 @Test
 public void testGetInvertedCoefficientSum() {
 RealVector objectiveCoefficients = simplexTableau.f.getCoefficients();
 double expectedSum = -(objectiveCoefficients.getEntry(0) + objectiveCoefficients.getEntry(1));
 double actualSum = SimplexTableau.getInvertedCoefficientSum(objectiveCoefficients.toArray());
 Assert.assertEquals(expectedSum, actualSum, 0.0);
 }

 @Test
 public void testGetBasicRow() {
 int col = 1;
 Integer expectedRow = 1;
 Integer actualRow = simplexTableau.getBasicRow(col);
 Assert.assertEquals(expectedRow, actualRow);
 }

 @Test
 public void testDropPhase1Objective() {
 simplexTableau.dropPhase1Objective();

 // Check the dimensions of the new tableau
 Assert.assertEquals(simplexTableau.getHeight(), 2);
 Assert.assertEquals(simplexTableau.getWidth(), 5);
 }

 @Test
 public void testIsOptimal() {
 Assert.assertFalse(simplexTableau.isOptimal());
 }

 @Test
 public void testGetSolution() {
 PointValuePair actualSolution = simplexTableau.getSolution();
 double[] coefficients = actualSolution.getPoint();
 double expectedValue = 3.0;
 Assert.assertEquals(expectedValue, actualSolution.getValue(), 0.0);
 Assert.assertArrayEquals(new double[]{Double.NEGATIVE_INFINITY, 2.0}, coefficients, 0.0);
 }

 @Test
 public void testDivideRow() {
 int dividendRow = 1;
 double divisor = 2.0;
 simplexTableau.divideRow(dividendRow, divisor);

 double[] expectedData = {
 {-1.5, 1.0, 0.75, 1.0, 0.0, 0.0, 0.0, Double.NaN, Double.NaN, 2.5},
 {0.0, 0.0, 0.5, -0.5, 0.0, 0.0, 0.0, 1.0, 1.0, 1.0},
 {-3.0, 2.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}
 };
 double[][] actualData = simplexTableau.getData();
 Arrays.sort(expectedData);
 Arrays.sort(actualData[0]);
 for (int i = 0; i < actualData.length; i++) {
 Arrays.sort(actualData[i]);
 }
 Assert.assertArrayEquals(expectedData, actualData, 0.0);
 }

 @Test
 public void testSubtractRow() {
 int minuendRow = 1;
 int subtrahendRow = 2;
 double multiple = 1.0;
 simplexTableau.subtractRow(minuendRow, subtrahendRow, multiple);

 double[] expectedData = {
 {-1.5, 1.0, 0.0, 1.0, 0.0, 0.0, 0.0, Double.NaN, Double.NaN, 2.5},
 {0.0, 0.0, 0.5, -0.5, 0.0, 0.0, 0.0, 1.0, 1.0, 1.0},
 {-3.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}
 };
 double[][] actualData = simplexTableau.getData();
 Arrays.sort(expectedData);
 Arrays.sort(actualData[0]);
 for (int i = 0; i < actualData.length; i++) {
 Arrays.sort(actualData[i]);
 }
 Assert.assertArrayEquals(expectedData, actualData, 0.0);
 }
}