import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.linear.*;
import org.apache.commons.math3.linear.*;

public class SimplexTableauTest {

    @Test
    public void testInitialization() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new RealVector(new double[]{1.0, 2.0}));
        Collection<LinearConstraint> constraints = new ArrayList<>();
        LinearConstraint constraint1 = new LinearConstraint(new RealVector(new double[]{1.0, -1.0}), Relationship.LEQ, 0.0);
        LinearConstraint constraint2 = new LinearConstraint(new RealVector(new double[]{0.0, 1.0}), Relationship.GEQ, 0.0);
        constraints.add(constraint1);
        constraints.add(constraint2);

        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 0.001, 10);

        // Check the number of decision variables, slack variables, and artificial variables
        int numDecisionVariables = tableau.getNumDecisionVariables();
        int numSlackVariables = tableau.getNumSlackVariables();
        int numArtificialVariables = tableau.getNumArtificialVariables();

        assertEquals(4, numDecisionVariables); // 2 original variables + 1 extra for non-negativity + 1 objective function
        assertEquals(2, numSlackVariables); // 2 constraints
        assertEquals(1, numArtificialVariables); // 1 artificial variable for each constraint

        // Check the tableau dimensions and initial values
        int tableauWidth = tableau.getWidth();
        int tableauHeight = tableau.getHeight();

        assertEquals(13, tableauWidth); // Decision variables + Slack variables + Artificial variables + Objective functions + RHS + Constant term
        assertEquals(12, tableauHeight); // Constraints + Objective functions

        double[][] tableauData = tableau.getData();
        for (int i = 0; i < tableauHeight; i++) {
            for (int j = 0; j < tableauWidth; j++) {
                if (i == 0 && j == 0) {
                    assertEquals(-1.0, tableauData[i][j], 1E-9); // Corresponds to the objective function coefficients
                } else if (i < 12) {
                    if (i < 2) {
                        if (j == 0) {
                            assertEquals(-1.0 * f.getCoefficients().getEntry(j), tableauData[i][j], 1E-9);
                        } else {
                            assertEquals(0.0, tableauData[i][j], 1E-9);
                        }
                    } else {
                        assertEquals(0.0, tableauData[i][j], 1E-9);
                    }
                    if (j == tableauWidth - 1) {
                        if (i < 2) {
                            if (i == 0) {
                                assertEquals(-f.getConstantTerm(), tableauData[i][j], 1E-9);
                            } else {
                                assertEquals(0.0, tableauData[i][j], 1E-9);
                            }
                        } else {
                            assertEquals(0.0, tableauData[i][j], 1E-9);
                        }
                    }
                } else if (j == tableauWidth - 1) {
                    if (i == tableauHeight - 1) {
                        assertEquals(constraint2.getValue(), tableauData[i][j], 1E-9);
                    } else {
                        assertEquals(constraint1.getValue(), tableauData[i][j], 1E-9);
                    }
                } else {
                    if (i < 2) {
                        if (j < 2) {
                            assertEquals(constraint1.getCoefficients().getEntry(j), tableauData[i][j], 1E-9);
                        } else {
                            assertEquals(0.0, tableauData[i][j], 1E-9);
                        }
                    } else if (i >= tableauHeight - 1 && j < 2) {
                        assertEquals(0.0, tableauData[i][j], 1E-9);
                    } else {
                        assertEquals(0.0, tableauData[i][j], 1E-9);
                    }
                }
            }
        }
    }
}