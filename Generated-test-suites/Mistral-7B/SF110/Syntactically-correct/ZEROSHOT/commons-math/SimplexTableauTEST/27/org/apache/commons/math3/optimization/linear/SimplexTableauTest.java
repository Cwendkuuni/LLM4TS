import org.apache.commons.math3.optim.LinearConstraint;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.goaltypes.GoalType;
import org.apache.commons.math3.optim.linear.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.linear.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class SimplexTableauTest {

 @Test
 public void testSimplexTableauConstruction() {
 // Test case for SimplexTableau constructor with one objective function and no constraints
 LinearObjectiveFunction f = new LinearObjectiveFunction(new RealVector(new double[]{1.0, 2.0, 3.0}), 5.0);
 List<LinearConstraint> constraints = new ArrayList<>();
 SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 0.001);

 // Test the size of the tableau
 Assert.assertEquals(3 + 1 + 1 + 1, tableau.getHeight());
 Assert.assertEquals(5, tableau.getWidth());

 // Test the objective function and constraints
 LinearObjectiveFunction expectedF = new LinearObjectiveFunction(new RealVector(new double[]{-1.0, -1.0, -1.0}), -5.0);
 List<LinearConstraint> expectedConstraints = Arrays.asList(
 new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, -1.0}}), Relationship.LEQ, 0.0),
 new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}}), Relationship.LEQ, 0.0),
 new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, -1.0}}), Relationship.LEQ, 0.0)
 );

 Assert.assertEquals(expectedF, tableau.getF());
 Assert.assertEquals(expectedConstraints, tableau.getConstraints());

 // Test the column labels
 List<String> expectedColumnLabels = Arrays.asList("x0", "x1", "x2", "x-", "s0", "s1", "s2", "RHS");
 Assert.assertEquals(expectedColumnLabels, tableau.getColumnLabels());
 }

 @Test
 public void testSimplexTableauConstructionWithConstraints() {
 // Test case for SimplexTableau constructor with one objective function and constraints
 LinearObjectiveFunction f = new LinearObjectiveFunction(new RealVector(new double[]{1.0, 2.0, 3.0}), 5.0);
 List<LinearConstraint> constraints = Arrays.asList(
 new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, -1.0}}), Relationship.LEQ, 0.0),
 new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}}), Relationship.LEQ, 0.0),
 new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, -1.0}}), Relationship.LEQ, 0.0),
 new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{0.0, 1.0}}), Relationship.GEQ, 1.0)
 );
 SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 0.001);

 // Test the size of the tableau
 Assert.assertEquals(3 + 1 + 1 + 1, tableau.getHeight());
 Assert.assertEquals(6, tableau.getWidth());

 // Test the objective function and constraints
 LinearObjectiveFunction expectedF = new LinearObjectiveFunction(new RealVector(new double[]{-1.0, -1.0, -1.0}), -5.0);
 List<LinearConstraint> expectedConstraints = Arrays.asList(
 new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, -1.0}}), Relationship.LEQ, 0.0),
 new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}}), Relationship.LEQ, 0.0),
 new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, -1.0}}), Relationship.LEQ, 0.0),
 new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{0.0, 1.0}}), Relationship.GEQ, 1.0),
 new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{0.0, -1.0}}), Relationship.GEQ, 0.0),
 new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{0.0, 0.0}}), Relationship.EQ, 0.0)
 );

 Assert.assertEquals(expectedF, tableau.getF());
 Assert.assertEquals(expectedConstraints, tableau.getConstraints());

 // Test the column labels
 List<String> expectedColumnLabels = Arrays.asList("x0", "x1", "x2", "x-", "s0", "s1", "s2", "RHS");
 Assert.assertEquals(expectedColumnLabels, tableau.getColumnLabels());
 }

 @Test
 public void testSimplexTableauNormalizeConstraints() {
 // Test case for normalizeConstraints method
 LinearObjectiveFunction f = new LinearObjectiveFunction(new RealVector(new double[]{1.0, 2.0, 3.0}), 5.0);
 List<LinearConstraint> constraints = Arrays.asList(
 new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, -1.0}}), Relationship.LEQ, 0.0),
 new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}}), Relationship.GEQ, 0.0),
 new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, -1.0}}), Relationship.LEQ, 0.0)
 );
 SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 0.001);
 List<LinearConstraint> expectedNormalizedConstraints = Arrays.asList(
 new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, -1.0}}), Relationship.GEQ, 0.0),
 new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}}), Relationship.LEQ, 0.0),
 new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, -1.0}}), Relationship.LEQ, 0.0)
 );
 List<LinearConstraint> normalizedConstraints = tableau.normalizeConstraints(constraints);
 Assert.assertEquals(expectedNormalizedConstraints, normalizedConstraints);
 }

 @Test
 public void testSimplexTableauCreateTableau() {
 // Test case for createTableau method
 LinearObjectiveFunction f = new LinearObjectiveFunction(new RealVector(new double[]{1.0, 2.0, 3.0}), 5.0);
 List<LinearConstraint> constraints = Arrays.asList(
 new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, -1.0}}), Relationship.LEQ, 0.0),
 new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}}), Relationship.GEQ, 0.0),
 new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, -1.0}}), Relationship.LEQ, 0.0)
 );
 SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 0.001);
 RealMatrix expectedTableau = new Array2DRowRealMatrix(new double[][]{{-1.0, -1.0, -1.0, 1.0, -1.0, 1.0, 1.0, 1.0},
 {-5.0, -3.0, -2.0, 1.0, 0.0, 0.0, 0.0, 0.0},
 {0.0, 0.0, 0.0, 0.0, 0.0, 1.0, -1.0, 0.0},
 {1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
 {0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0},
 {0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0},
 {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0},
 {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0}
 });
 RealMatrix tableauMatrix = tableau.getTableau();
 Assert.assertEquals(expectedTableau, tableauMatrix);
 }

 @Test
 public void testSimplexTableauIsOptimal() {
 // Test case for isOptimal method
 LinearObjectiveFunction f = new LinearObjectiveFunction(new RealVector(new double[]{1.0, 2.0, 3.0}), 5.0);
 List<LinearConstraint> constraints = Arrays.asList(
 new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, -1.0}}), Relationship.LEQ, 0.0),
 new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}}), Relationship.GEQ, 0.0),
 new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, -1.0}}), Relationship.LEQ, 0.0),
 new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{0.0, 1.0}}), Relationship.GEQ, 1.0)
 );
 SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 0.001);
 Assert.assertFalse(tableau.isOptimal());

 LinearObjectiveFunction f2 = new LinearObjectiveFunction(new RealVector(new double[]{1.0, 2.0, 3.0}), 5.0);
 List<LinearConstraint> constraints2 = Arrays.asList(
 new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, -1.0}}), Relationship.LEQ, 0.0),
 new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}}), Relationship.LEQ, 0.0),
 new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, -1.0}}), Relationship.LEQ, 0.0),
 new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{0.0, 1.0}}), Relationship.GEQ, 1.0),
 new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{0.0, 0.0}}), Relationship.EQ, 0.0),
 new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{0.0, -1.0}}), Relationship.LEQ, 0.0)
 );
 SimplexTableau optimalTableau = new SimplexTableau(f2, constraints2, GoalType.MAXIMIZE, false, 0.001);
 Assert.assertTrue(optimalTableau.isOptimal());
 }

 @Test
 public void testSimplexTableauGetSolution() {
 // Test case for getSolution method
 LinearObjectiveFunction f = new LinearObjectiveFunction(new RealVector(new double[]{1.0, 2.0, 3.0}), 5.0);
 List<LinearConstraint> constraints = Arrays.asList(
 new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, -1.0}}), Relationship.LEQ, 0.0),
 new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}}), Relationship.GEQ, 0.0),
 new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, -1.0}}), Relationship.LEQ, 0.0),
 new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{0.0, 1.0}}), Relationship.GEQ, 1.0)
 );
 SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 0.001);
 PointValuePair solution = tableau.getSolution();
 double[] expectedSolution = new double[]{Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, 0.0, 0.0, 1.0, 1.0, 1.0};
 Arrays.sort(solution.getPoint());
 Assert.assertArrayEquals(expectedSolution, solution.getPoint(), 0.0);
 Assert.assertEquals(5.0, solution.getValue(), 0.0);
 }
}