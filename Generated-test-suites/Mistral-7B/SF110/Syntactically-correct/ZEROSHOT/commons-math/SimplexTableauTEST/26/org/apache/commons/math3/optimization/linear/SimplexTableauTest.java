import org.apache.commons.math3.optim.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.*;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Assert.*;

public class SimplexTableauTest {

 @Test
 public void testSimplexTableauConstruction() {
 LinearObjectiveFunction f = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}), new ArrayRealVector(new double[]{5.0, 6.0}));
 List<LinearConstraint> constraints = new ArrayList<>();
 constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, -1.0}, {1.0, -2.0}}), Relationship.LEQ, 0.0));
 constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}, {-1.0, 2.0}}), Relationship.GEQ, 0.0));
 SimplexTableau st = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1.0E-6);

 assertEquals(11, st.getWidth());
 assertEquals(3, st.getHeight());
 assertEquals(3, st.getNumDecisionVariables());
 assertEquals(2, st.getNumSlackVariables());
 assertEquals(1, st.getNumArtificialVariables());
 assertNull(st.tableau);
 }

 @Test
 public void testInitializeColumnLabels() {
 LinearObjectiveFunction f = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}), new ArrayRealVector(new double[]{5.0, 6.0}));
 List<LinearConstraint> constraints = new ArrayList<>();
 constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, -1.0}, {1.0, -2.0}}), Relationship.LEQ, 0.0));
 constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}, {-1.0, 2.0}}), Relationship.GEQ, 0.0));
 SimplexTableau st = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1.0E-6);

 assertEquals(13, st.columnLabels.size());
 assertEquals("W", st.columnLabels.get(0));
 assertEquals("Z", st.columnLabels.get(1));
 for (int i = 0; i < 3; i++) {
 assertEquals("x" + i, st.columnLabels.get(i + 2));
 }
 assertEquals("x-", st.columnLabels.get(6));
 for (int i = 0; i < 2; i++) {
 assertEquals("s" + i, st.columnLabels.get(i + 7));
 }
 for (int i = 0; i < 1; i++) {
 assertEquals("a" + i, st.columnLabels.get(i + 9));
 }
 assertEquals("RHS", st.columnLabels.get(12));
 }

 @Test
 public void testCreateTableau() {
 LinearObjectiveFunction f = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}), new ArrayRealVector(new double[]{5.0, 6.0}));
 List<LinearConstraint> constraints = new ArrayList<>();
 constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, -1.0}, {1.0, -2.0}}), Relationship.LEQ, 0.0));
 constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}, {-1.0, 2.0}}), Relationship.GEQ, 0.0));
 SimplexTableau st = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1.0E-6);

 double[][] expected = {
 {-1.0, 2.0, 1.0, -1.0, 1.0, -2.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0},
 {1.0, -5.0, -3.0, -1.0, 0.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 5.0},
 {3.0, -2.0, -1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, 1.0, 1.0, 1.0, 2.0},
 {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, 1.0, 1.0, 0.0}
 };

 RealMatrix actual = st.tableau;
 assertArrayEquals(expected[0], actual.getDataRef()[0]);
 assertArrayEquals(expected[1], actual.getDataRef()[1]);
 assertArrayEquals(expected[2], actual.getDataRef()[2]);
 assertArrayEquals(expected[3], actual.getDataRef()[3]);
 }

 @Test
 public void testDropPhase1Objective() {
 LinearObjectiveFunction f = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}), new ArrayRealVector(new double[]{5.0, 6.0}));
 List<LinearConstraint> constraints = new ArrayList<>();
 constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, -1.0}, {1.0, -2.0}}), Relationship.LEQ, 0.0));
 constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}, {-1.0, 2.0}}), Relationship.GEQ, 0.0));
 SimplexTableau st = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1.0E-6);
 st.dropPhase1Objective();

 int expectedWidth = 10;
 int expectedHeight = 3;
 double[][] expected = {
 {-1.0, 2.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0},
 {1.0, -5.0, -3.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
 {3.0, -2.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, 1.0},
 {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.1, 1.2}
 };

 RealMatrix actual = st.tableau;
 assertArrayEquals(expected[0], actual.getDataRef()[0]);
 assertArrayEquals(expected[1], actual.getDataRef()[1]);
 assertArrayEquals(expected[2], actual.getDataRef()[2]);
 assertArrayEquals(expected[3], actual.getDataRef()[3]);
 assertEquals(expectedWidth, actual.getColumnDimension());
 assertEquals(expectedHeight, actual.getRowDimension());
 }
}