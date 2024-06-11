import org.apache.commons.math3.optim.LinearConstraint;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.goaltypes.GoalType;
import org.apache.commons.math3.optim.linear.*;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.linear.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SimplexTableauTest {

 private static final double EPSILON = 1.0E-9;

 @Test
 public void testSimplexTableauConstruction() {
 SimplexTableau tableau = new SimplexTableau(
 new LinearObjectiveFunction(Arrays.asList(1.0, 2.0), 3.0),
 Arrays.asList(
 new LinearConstraint(Arrays.asList(1.0, 1.0), Relationship.LEQ, 2.0),
 new LinearConstraint(Arrays.asList(2.0, 1.0), Relationship.GEQ, 1.0)
 ), GoalType.MAXIMIZE, false, EPSILON);

 assertEquals(3, tableau.getNumDecisionVariables());
 assertEquals(1, tableau.getNumSlackVariables());
 assertEquals(0, tableau.getNumArtificialVariables());
 assertEquals(2, tableau.getNumObjectiveFunctions());

 RealMatrix expectedTableau = new Array2DRowRealMatrix(
 new double[][]{
 // Objective Function 1
 {-1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 1.0, 0.0},
 // Objective Function 2
 {0.0, -1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0},
 // Constraint 1
 {1.0, 1.0, 1.0, 0.0, 1.0, 0.0, 0.0, -1.0, 2.0},
 // Constraint 2
 {2.0, 1.0, 0.0, 0.0, 0.0, 1.0, 0.0, -1.0, 1.0}
 }
 );

 RealMatrix actualTableau = tableau.getData();
 assertEquals(expectedTableau, actualTableau);
 }

 @Test
 public void testSimplexTableauNormalizeConstraints() {
 SimplexTableau tableau = new SimplexTableau(
 new LinearObjectiveFunction(Arrays.asList(1.0, 2.0), 3.0),
 Arrays.asList(
 new LinearConstraint(Arrays.asList(1.0, 1.0), Relationship.LEQ, 2.0),
 new LinearConstraint(Arrays.asList(-1.0, 1.0), Relationship.GEQ, -2.0)
 ), GoalType.MAXIMIZE, false, EPSILON);

 List<LinearConstraint> constraints = tableau.getConstraints();
 assertEquals(2, constraints.size());

 LinearConstraint constraint1 = constraints.get(0);
 LinearConstraint constraint2 = constraints.get(1);

 assertEquals(Relationship.LEQ, constraint1.getRelationship());
 assertEquals(2.0, constraint1.getValue(), EPSILON);
 assertEquals(Arrays.asList(1.0, 1.0), constraint1.getCoefficients());

 assertEquals(Relationship.GEQ, constraint2.getRelationship());
 assertEquals(-2.0, constraint2.getValue(), EPSILON);
 assertEquals(Arrays.asList(-1.0, 1.0), constraint2.getCoefficients());
 }

 @Test
 public void testIsOptimal() {
 SimplexTableau tableau = new SimplexTableau(
 new LinearObjectiveFunction(Arrays.asList(1.0, 2.0), 3.0),
 Arrays.asList(
 new LinearConstraint(Arrays.asList(1.0, 1.0), Relationship.LEQ, 2.0),
 new LinearConstraint(Arrays.asList(2.0, 1.0), Relationship.GEQ, 1.0)
 ), GoalType.MAXIMIZE, false, EPSILON);

 assertTrue(tableau.isOptimal());

 tableau.setEntry(1, 1, -0.1);
 assertFalse(tableau.isOptimal());
 }

 @Test
 public void testGetSolution() {
 SimplexTableau tableau = new SimplexTableau(
 new LinearObjectiveFunction(Arrays.asList(1.0, 2.0), 3.0),
 Arrays.asList(
 new LinearConstraint(Arrays.asList(1.0, 1.0), Relationship.LEQ, 2.0),
 new LinearConstraint(Arrays.asList(2.0, 1.0), Relationship.GEQ, 1.0)
 ), GoalType.MAXIMIZE, false, EPSILON);

 PointValuePair solution = tableau.getSolution();

 assertEquals(new double[]{0.0, 2.0}, solution.getPoint(), EPSILON);
 assertEquals(3.0, solution.getValue(), EPSILON);
 }

}