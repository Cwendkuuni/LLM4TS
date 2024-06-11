package org.apache.commons.math3.optimization.linear;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.linear.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.linear.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexTableauTest {

 @Test
 public void testSimplexTableauConstruction() {
 LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new RealVector(new double[] {-2.0, 3.0}));
 List<LinearConstraint> constraints = new ArrayList<>();
 constraints.add(new LinearConstraint(new RealVector(new double[] {1.0, 1.0}), Relationship.LEQ, 2.0));
 constraints.add(new LinearConstraint(new RealVector(new double[] {2.0, -1.0}), Relationship.GEQ, 3.0));
 SimplexTableau simplexTableau = new SimplexTableau(objectiveFunction, constraints, GoalType.MINIMIZE, false, 1e-6, 10);
 assertEquals(2, simplexTableau.getNumObjectiveFunctions());
 assertEquals(4, simplexTableau.getNumDecisionVariables());
 assertEquals(2, simplexTableau.getNumSlackVariables());
 assertEquals(1, simplexTableau.getNumArtificialVariables());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSimplexTableauConstructionInvalidConstraints() {
 LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new RealVector(new double[] {-2.0, 3.0}));
 List<LinearConstraint> constraints = new ArrayList<>();
 constraints.add(new LinearConstraint(new RealVector(new double[] {1.0, 1.0}), Relationship.LEQ, 3.0));
 SimplexTableau simplexTableau = null;
 try {
 simplexTableau = new SimplexTableau(objectiveFunction, constraints, GoalType.MINIMIZE, false, 1e-6, 10);
 } catch (IllegalArgumentException e) {
 simplexTableau = e.getCause();
 }
 assertNotNull(simplexTableau);
 }

 @Test
 public void testSimplexTableauEquals() {
 LinearObjectiveFunction objectiveFunction1 = new LinearObjectiveFunction(new RealVector(new double[] {-2.0, 3.0}));
 List<LinearConstraint> constraints1 = new ArrayList<>();
 constraints1.add(new LinearConstraint(new RealVector(new double[] {1.0, 1.0}), Relationship.LEQ, 2.0));
 constraints1.add(new LinearConstraint(new RealVector(new double[] {2.0, -1.0}), Relationship.GEQ, 3.0));
 SimplexTableau simplexTableau1 = new SimplexTableau(objectiveFunction1, constraints1, GoalType.MINIMIZE, false, 1e-6, 10);
 LinearObjectiveFunction objectiveFunction2 = new LinearObjectiveFunction(new RealVector(new double[] {-2.0, 3.0}));
 List<LinearConstraint> constraints2 = new ArrayList<>();
 constraints2.add(new LinearConstraint(new RealVector(new double[] {1.0, 1.0}), Relationship.LEQ, 2.0));
 constraints2.add(new LinearConstraint(new RealVector(new double[] {2.0, -1.0}), Relationship.GEQ, 3.0));
 SimplexTableau simplexTableau2 = new SimplexTableau(objectiveFunction2, constraints2, GoalType.MINIMIZE, false, 1e-6, 10);
 assertTrue(simplexTableau1.equals(simplexTableau2));
 }

 @Test
 public void testSimplexTableauNotEquals() {
 LinearObjectiveFunction objectiveFunction1 = new LinearObjectiveFunction(new RealVector(new double[] {-2.0, 3.0}));
 List<LinearConstraint> constraints1 = new ArrayList<>();
 constraints1.add(new LinearConstraint(new RealVector(new double[] {1.0, 1.0}), Relationship.LEQ, 2.0));
 constraints1.add(new LinearConstraint(new RealVector(new double[] {2.0, -1.0}), Relationship.GEQ, 3.0));
 SimplexTableau simplexTableau1 = new SimplexTableau(objectiveFunction1, constraints1, GoalType.MINIMIZE, false, 1e-6, 10);
 LinearObjectiveFunction objectiveFunction2 = new LinearObjectiveFunction(new RealVector(new double[] {-2.0, 3.0, 1.0}));
 List<LinearConstraint> constraints2 = new ArrayList<>();
 constraints2.add(new LinearConstraint(new RealVector(new double[] {1.0, 1.0}), Relationship.LEQ, 2.0));
 constraints2.add(new LinearConstraint(new RealVector(new double[] {2.0, -1.0}), Relationship.GEQ, 3.0));
 SimplexTableau simplexTableau2 = new SimplexTableau(objectiveFunction2, constraints2, GoalType.MINIMIZE, false, 1e-6, 10);
 assertFalse(simplexTableau1.equals(simplexTableau2));
 }

 @Test
 public void testSimplexTableauIsOptimal() {
 LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new RealVector(new double[] {-2.0, 3.0}));
 List<LinearConstraint> constraints = new ArrayList<>();
 constraints.add(new LinearConstraint(new RealVector(new double[] {1.0, 1.0}), Relationship.LEQ, 2.0));
 constraints.add(new LinearConstraint(new RealVector(new double[] {2.0, -1.0}), Relationship.GEQ, 3.0));
 SimplexTableau simplexTableau = new SimplexTableau(objectiveFunction, constraints, GoalType.MINIMIZE, false, 1e-6, 10);
 assertTrue(simplexTableau.isOptimal());
 }

 @Test
 public void testSimplexTableauNotOptimal() {
 LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new RealVector(new double[] {-2.0, 3.0}));
 List<LinearConstraint> constraints = new ArrayList<>();
 constraints.add(new LinearConstraint(new RealVector(new double[] {1.0, 1.0}), Relationship.LEQ, 1.0));
 constraints.add(new LinearConstraint(new RealVector(new double[] {2.0, -1.0}), Relationship.GEQ, 3.0));
 SimplexTableau simplexTableau = new SimplexTableau(objectiveFunction, constraints, GoalType.MINIMIZE, false, 1e-6, 10);
 assertFalse(simplexTableau.isOptimal());
 }

 @Test
 public void testSimplexTableauGetSolution() {
 LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new RealVector(new double[] {-2.0, 3.0}));
 List<LinearConstraint> constraints = new ArrayList<>();
 constraints.add(new LinearConstraint(new RealVector(new double[] {1.0, 1.0}), Relationship.LEQ, 2.0));
 constraints.add(new LinearConstraint(new RealVector(new double[] {2.0, -1.0}), Relationship.GEQ, 3.0));
 SimplexTableau simplexTableau = new SimplexTableau(objectiveFunction, constraints, GoalType.MINIMIZE, false, 1e-6, 10);
 PointValuePair solution = simplexTableau.getSolution();
 assertArrayEquals(new double[] {1.0 / 3.0, 2.0 / 3.0}, solution.getPoint(), 1e-6);
 assertEquals(-5.0 / 3.0, solution.getValue(), 1e-6);
 }

 @Test
 public void testSimplexTableauTableauData() {
 LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new RealVector(new double[] {-2.0, 3.0}));
 List<LinearConstraint> constraints = new ArrayList<>();
 constraints.add(new LinearConstraint(new RealVector(new double[] {1.0, 1.0}), Relationship.LEQ, 2.0));
 constraints.add(new LinearConstraint(new RealVector(new double[] {2.0, -1.0}), Relationship.GEQ, 3.0));
 SimplexTableau simplexTableau = new SimplexTableau(objectiveFunction, constraints, GoalType.MINIMIZE, false, 1e-6, 10);
 double[][] expectedData = new double[][] {{1.0 / 3.0, 2.0 / 3.0, -1.0 / 3.0, -1.0 / 3.0, 1.0, 1.0, 1.0},
 {-1.0 / 3.0, 1.0 / 3.0, 0.0, -1.0, 1.0, 1.0, 1.0},
 {-1.0 / 3.0, 1.0 / 3.0, 0.0, -1.0, 0.0, 1.0, 1.0},
 {-2.0 / 3.0, -1.0 / 3.0, 1.0, 0.0, 0.0, 0.0, 1.0},
 {-5.0 / 3.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}};
 assertArrayEquals(expectedData, simplexTableau.getData(), 1e-6);
 }

 @Test
 public void testSimplexTableauDropPhase1Objective() {
 LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new RealVector(new double[] {-2.0, 3.0, 1.0}));
 List<LinearConstraint> constraints = new ArrayList<>();
 constraints.add(new LinearConstraint(new RealVector(new double[] {1.0, 1.0}), Relationship.LEQ, 2.0));
 constraints.add(new LinearConstraint(new RealVector(new double[] {2.0, -1.0}), Relationship.GEQ, 3.0));
 SimplexTableau simplexTableau = new SimplexTableau(objectiveFunction, constraints, GoalType.MINIMIZE, false, 1e-6, 10);
 simplexTableau.dropPhase1Objective();
 double[][] expectedData = new double[][] {{1.0 / 3.0, 2.0 / 3.0, -1.0 / 3.0, 1.0, 1.0, 1.0, 1.0},
 {-1.0 / 3.0, 1.0 / 3.0, 0.0, 1.0, 1.0, 1.0, 1.0},
 {-1.0 / 3.0, 1.0 / 3.0, 0.0, 1.0, 0.0, 1.0, 1.0},
 {-2.0 / 3.0, -1.0 / 3.0, 1.0, 0.0, 0.0, 0.0, 1.0}};
 assertArrayEquals(expectedData, simplexTableau.getData(), 1e-6);
 assertEquals(1, simplexTableau.getNumObjectiveFunctions());
 }
}