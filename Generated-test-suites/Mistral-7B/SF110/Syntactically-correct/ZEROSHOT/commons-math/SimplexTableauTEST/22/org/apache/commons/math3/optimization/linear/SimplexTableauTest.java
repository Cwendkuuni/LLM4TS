import org.apache.commons.math3.optim.LinearConstraint;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.GoalType;
import org.apache.commons.math3.optim.linear.Relationship;
import org.apache.commons.math3.linear.*;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SimplexTableauTest {

 @Test
 public void testSimplexTableauConstruction() {
 SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(Arrays.asList(1.0, 2.0), 5.0), Arrays.asList(
 new LinearConstraint(Arrays.asList(1.0, 3.0), Relationship.LEQ, 2.0),
 new LinearConstraint(Arrays.asList(-2.0, -1.0), Relationship.GEQ, 1.0)
 ), GoalType.MAXIMIZE, false, 0.001);

 assertEquals(2, tableau.getNumObjectiveFunctions());
 assertEquals(3, tableau.getOriginalNumDecisionVariables());
 assertEquals(2, tableau.getNumSlackVariables());
 assertEquals(1, tableau.getNumArtificialVariables());
 assertEquals(0.001, tableau.getEpsilon(), 0.0);
 assertEquals(10, tableau.getMaxUlps());

 RealMatrix expectedTableau = new Array2DRowRealMatrix(7, 7)
 .setSubMatrix(0, 0, new Array2DRowRealMatrix(2, 3)
 .setEntry(0, 0, -1.0)
 .setEntry(0, 1, 1.0)
 .setEntry(0, 2, 3.0)
 .setEntry(1, 0, 5.0)
 .setEntry(1, 1, -1.0)
 .setEntry(1, 2, -2.0)
 )
 .setSubMatrix(1, 0, new Array2DRowRealMatrix(1, 3)
 .setEntry(1, 0, 1.0)
 .setEntry(1, 1, -1.0)
 .setEntry(1, 2, 2.0)
 )
 .setSubMatrix(2, 0, new Array2DRowRealMatrix(1, 3)
 .setEntry(2, 0, 1.0)
 .setEntry(2, 1, -1.0)
 .setEntry(2, 2, -1.0)
 )
 .setEntry(2, 6, 2.0)
 .setEntry(3, 0, -1.0)
 .setEntry(3, 1, 1.0)
 .setEntry(3, 2, 3.0)
 .setEntry(3, 6, 1.0)
 .setEntry(4, 0, -1.0)
 .setEntry(4, 1, -1.0)
 .setEntry(4, 2, -2.0)
 .setEntry(4, 6, 1.0)
 .setEntry(5, 0, -1.0)
 .setEntry(5, 1, -1.0)
 .setEntry(5, 2, -1.0)
 .setEntry(5, 6, -1.0)
 .setEntry(6, 0, 1.0)
 .setEntry(6, 1, -1.0)
 .setEntry(6, 2, -3.0)
 .setEntry(6, 6, -2.0);

 RealMatrix actualTableau = tableau.getData();
 assertTrue(actualTableau.equals(expectedTableau));
 }

 @Test
 public void testSimplexTableauNormalizeConstraints() {
 SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(Arrays.asList(-1.0, 2.0), 5.0), Arrays.asList(
 new LinearConstraint(Arrays.asList(1.0, 3.0), Relationship.LEQ, 2.0),
 new LinearConstraint(Arrays.asList(-2.0, -1.0), Relationship.GEQ, 1.0)
 ), GoalType.MAXIMIZE, false, 0.001);

 List<LinearConstraint> normalizedConstraints = tableau.normalizeConstraints(Arrays.asList(
 new LinearConstraint(Arrays.asList(1.0, 3.0), Relationship.LEQ, 2.0),
 new LinearConstraint(Arrays.asList(-2.0, -1.0), Relationship.GEQ, 1.0)
 ));

 List<LinearConstraint> expectedNormalizedConstraints = Arrays.asList(
 new LinearConstraint(Arrays.asList(1.0, 3.0), Relationship.GREATER_EQUAL, -2.0),
 new LinearConstraint(Arrays.asList(-2.0, -1.0), Relationship.LESS_EQUAL, -1.0)
 );

 assertTrue(normalizedConstraints.equals(expectedNormalizedConstraints));
 }

 @Test
 public void testSimplexTableauGetNumObjectiveFunctions() {
 SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(Arrays.asList(1.0, 2.0), 5.0), Arrays.asList(
 new LinearConstraint(Arrays.asList(1.0, 3.0), Relationship.LEQ, 2.0),
 new LinearConstraint(Arrays.asList(-2.0, -1.0), Relationship.GEQ, 1.0)
 ), GoalType.MAXIMIZE, false, 0.001);

 assertEquals(2, tableau.getNumObjectiveFunctions());
 }

 @Test
 public void testSimplexTableauGetOriginalNumDecisionVariables() {
 SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(Arrays.asList(1.0, 2.0), 5.0), Arrays.asList(
 new LinearConstraint(Arrays.asList(1.0, 3.0), Relationship.LEQ, 2.0),
 new LinearConstraint(Arrays.asList(-2.0, -1.0), Relationship.GEQ, 1.0)
 ), GoalType.MAXIMIZE, false, 0.001);

 assertEquals(2, tableau.getOriginalNumDecisionVariables());
 }

 @Test
 public void testSimplexTableauGetNumSlackVariables() {
 SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(Arrays.asList(1.0, 2.0), 5.0), Arrays.asList(
 new LinearConstraint(Arrays.asList(1.0, 3.0), Relationship.LEQ, 2.0),
 new LinearConstraint(Arrays.asList(-2.0, -1.0), Relationship.GEQ, 1.0)
 ), GoalType.MAXIMIZE, false, 0.001);

 assertEquals(2, tableau.getNumSlackVariables());
 }

 @Test
 public void testSimplexTableauGetNumArtificialVariables() {
 SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(Arrays.asList(1.0, 2.0), 5.0), Arrays.asList(
 new LinearConstraint(Arrays.asList(1.0, 3.0), Relationship.LEQ, 2.0),
 new LinearConstraint(Arrays.asList(-2.0, -1.0), Relationship.GEQ, 1.0)
 ), GoalType.MAXIMIZE, false, 0.001);

 assertEquals(1, tableau.getNumArtificialVariables());
 }

 @Test
 public void testSimplexTableauGetEpsilon() {
 SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(Arrays.asList(1.0, 2.0), 5.0), Arrays.asList(
 new LinearConstraint(Arrays.asList(1.0, 3.0), Relationship.LEQ, 2.0),
 new LinearConstraint(Arrays.asList(-2.0, -1.0), Relationship.GEQ, 1.0)
 ), GoalType.MAXIMIZE, false, 0.001);

 assertEquals(0.001, tableau.getEpsilon(), 0.0);
 }

 @Test
 public void testSimplexTableauGetMaxUlps() {
 SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(Arrays.asList(1.0, 2.0), 5.0), Arrays.asList(
 new LinearConstraint(Arrays.asList(1.0, 3.0), Relationship.LEQ, 2.0),
 new LinearConstraint(Arrays.asList(-2.0, -1.0), Relationship.GEQ, 1.0)
 ), GoalType.MAXIMIZE, false, 0.001);

 assertEquals(10, tableau.getMaxUlps());
 }

 @Test
 public void testSimplexTableauIsOptimal() {
 SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(Arrays.asList(1.0, 2.0), 5.0), Arrays.asList(
 new LinearConstraint(Arrays.asList(1.0, 3.0), Relationship.LEQ, 2.0),
 new LinearConstraint(Arrays.asList(-2.0, -1.0), Relationship.GEQ, 1.0)
 ), GoalType.MAXIMIZE, false, 0.001);

 tableau.setEntry(0, 0, 1.001);
 tableau.setEntry(1, 1, 1.001);
 tableau.setEntry(1, 2, 3.001);
 tableau.setEntry(3, 1, 1.001);
 tableau.setEntry(3, 2, 2.999);
 tableau.setEntry(3, 6, 1.001);

 assertFalse(tableau.isOptimal());

 tableau.setEntry(3, 2, 3.0);
 assertTrue(tableau.isOptimal());
 }

 @Test
 public void testSimplexTableauGetSolution() {
 SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(Arrays.asList(1.0, 2.0), 5.0), Arrays.asList(
 new LinearConstraint(Arrays.asList(1.0, 3.0), Relationship.LEQ, 2.0),
 new LinearConstraint(Arrays.asList(-2.0, -1.0), Relationship.GEQ, 1.0)
 ), GoalType.MAXIMIZE, false, 0.001);

 PointValuePair solution = tableau.getSolution();

 double[] coefficients = solution.getPoint();
 double optimalValue = solution.getValue();

 assertEquals(2, coefficients.length);
 assertEquals(1.3333333333333333, coefficients[0], 0.001);
 assertEquals(4.333333333333333, coefficients[1], 0.001);
 assertEquals(5.0, optimalValue, 0.001);
 }

 @Test
 public void testSimplexTableauDropPhase1Objective() {
 SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(Arrays.asList(1.0, 2.0, 3.0), 5.0), Arrays.asList(
 new LinearConstraint(Arrays.asList(1.0, 3.0, 2.0), Relationship.LEQ, 2.0),
 new LinearConstraint(Arrays.asList(-2.0, -1.0, -1.0), Relationship.GEQ, 1.0),
 new LinearConstraint(Arrays.asList(0.0, 2.0, 2.0), Relationship.EQ, 0.0)
 ), GoalType.MAXIMIZE, false, 0.001);

 tableau.dropPhase1Objective();

 RealMatrix expectedTableau = new Array2DRowRealMatrix(6, 6)
 .setSubMatrix(0, 0, new Array2DRowRealMatrix(2, 4)
 .setEntry(0, 0, -1.0)
 .setEntry(0, 1, 1.0)
 .setEntry(0, 2, 2.0)
 .setEntry(0, 3, 3.0)
 )
 .setSubMatrix(1, 0, new Array2DRowRealMatrix(1, 4)
 .setEntry(1, 0, 1.0)
 .setEntry(1, 1, -1.0)
 .setEntry(1, 2, 1.0)
 )
 .setSubMatrix(2, 0, new Array2DRowRealMatrix(1, 4)
 .setEntry(2, 0, 1.0)
 .setEntry(2, 1, -1.0)
 .setEntry(2, 2, 1.0)
 )
 .setSubMatrix(3, 0, new Array2DRowRealMatrix(1, 4)
 .setEntry(3, 0, 1.0)
 .setEntry(3, 1, -1.0)
 .setEntry(3, 2, 1.0)
 )
 .setSubMatrix(4, 0, new Array2DRowRealMatrix(1, 4)
 .setEntry(4, 0, 1.0)
 .setEntry(4, 1, -1.0)
 .setEntry(4, 2, 1.0)
 )
 .setSubMatrix(5, 0, new Array2DRowRealMatrix(1, 4)
 .setEntry(5, 0, 1.0)
 .setEntry(5, 1, -1.0)
 .setEntry(5, 2, 1.0)
 )
 .setEntry(6, 0, 1.0)
 .setEntry(6, 1, -1.0)
 .setEntry(6, 2, -5.0)
 .setEntry(6, 3, 0.0)
 .setEntry(6, 4, 0.0)
 .setEntry(6, 5, 0.0);

 RealMatrix actualTableau = tableau.getData();
 assertTrue(actualTableau.equals(expectedTableau));
 }
}