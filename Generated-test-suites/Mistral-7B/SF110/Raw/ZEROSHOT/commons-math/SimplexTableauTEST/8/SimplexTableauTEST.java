import org.apache.commons.math3.optimization.linear.*;
import org.apache.commons.math3.linear.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexTableauTest {

 @Test
 public void testSimplexTableauConstruction() {
 SimplexTableau tableau = new SimplexTableau(
 new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{-2.0}, {1.0, 3.0}, {2.0, 1.0}}), 0),
 Arrays.asList(
 new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 2.0}, {1.0, -1.0}}), Relationship.LEQ, 1.0),
 new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 3.0}, {2.0, 1.0}}), Relationship.LEQ, 1.0),
 new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-3.0, 1.0}}), Relationship.EQ, 0.0)
 ),
 GoalType.MAXIMIZE,
 false,
 1e-10);

 assertEquals(3, tableau.getNumDecisionVariables());
 assertEquals(1, tableau.getNumSlackVariables());
 assertEquals(1, tableau.getNumArtificialVariables());
 assertEquals(3, tableau.getHeight());
 assertEquals(5, tableau.getWidth());
 }

 @Test
 public void testSimplexTableauNormalizeConstraints() {
 SimplexTableau tableau = new SimplexTableau(
 new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{-2.0}, {1.0, 3.0}, {2.0, 1.0}}), 0),
 Arrays.asList(
 new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 2.0}, {1.0, -1.0}}), Relationship.LEQ, 1.0),
 new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 3.0}, {2.0, 1.0}}), Relationship.LEQ, 1.0),
 new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-3.0, 1.0}}), Relationship.EQ, 0.0)
 ),
 GoalType.MAXIMIZE,
 false,
 1e-10);

 List<LinearConstraint> normalizedConstraints = tableau.normalizeConstraints(tableau.getConstraints());
 assertEquals(3, normalizedConstraints.size());
 }

 @Test
 public void testGetEntry() {
 SimplexTableau tableau = new SimplexTableau(
 new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{-2.0}, {1.0, 3.0}, {2.0, 1.0}}), 0),
 Arrays.asList(
 new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 2.0}, {1.0, -1.0}}), Relationship.LEQ, 1.0),
 new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 3.0}, {2.0, 1.0}}), Relationship.LEQ, 1.0),
 new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-3.0, 1.0}}), Relationship.EQ, 0.0)
 ),
 GoalType.MAXIMIZE,
 false,
 1e-10);

 assertEquals(2.0, tableau.getEntry(0, 0), 1e-12);
 assertEquals(1.0, tableau.getEntry(1, 1), 1e-12);
 assertEquals(3.0, tableau.getEntry(1, 2), 1e-12);
 assertEquals(1.0, tableau.getEntry(2, 1), 1e-12);
 assertEquals(1.0, tableau.getEntry(2, 2), 1e-12);
 assertEquals(0.0, tableau.getEntry(2, 4), 1e-12);
 }

 @Test
 public void testIsOptimal() {
 SimplexTableau tableau = new SimplexTableau(
 new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{-2.0, -1.0}, {1.0, 3.0}, {2.0, 1.0}}), 0),
 Arrays.asList(
 new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 2.0}, {1.0, -1.0}}), Relationship.LEQ, 1.0),
 new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 3.0}, {2.0, 1.0}}), Relationship.LEQ, 1.0),
 new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-3.0, 1.0}}), Relationship.EQ, 0.0)
 ),
 GoalType.MAXIMIZE,
 false,
 1e-10);

 assertFalse(tableau.isOptimal());
 }

 @Test
 public void testGetSolution() {
 SimplexTableau tableau = new SimplexTableau(
 new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{-2.0, -1.0}, {1.0, 3.0}, {2.0, 1.0}}), 0),
 Arrays.asList(
 new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 2.0}, {1.0, -1.0}}), Relationship.LEQ, 1.0),
 new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 3.0}, {2.0, 1.0}}), Relationship.LEQ, 1.0),
 new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-3.0, 1.0}}), Relationship.EQ, 0.0)
 ),
 GoalType.MAXIMIZE,
 false,
 1e-10);

 PointValuePair solution = tableau.getSolution();
 assertArrayEquals(new double[]{0.0, 0.5, 1.0}, solution.getPoint(), 1e-12);
 assertEquals(3.0, solution.getValue(), 1e-12);
 }
}