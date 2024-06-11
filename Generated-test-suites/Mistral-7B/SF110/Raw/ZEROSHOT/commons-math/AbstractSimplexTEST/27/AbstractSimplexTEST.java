package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.MultivariateFunction;
import org.apache.commons.math3.optim.initializer.AveragedStartingPointGenerator;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinearConstraint;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.AbstractSimplex;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.AbstractSimplex.PointValuePair;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.SimplexMethod;

public class AbstractSimplexTest {

    private AbstractSimplex simplex;
    private MultivariateFunction objectiveFunction;
    private LinOptimizer optimizer;

    @Before
    public void setUp() {
        this.objectiveFunction = new ObjectiveFunction() {
            @Override
            public double value(double[] point) {
                return FunctionUtils.sum(point) * FunctionUtils.sqrt(FunctionUtils.sum(PointValuePair.pointMultiply(point, point)));
            }

            @Override
            public int getNumberOfVariables() {
                return 3;
            }
        };

        this.simplex = new SimplexMethod(3);
        this.optimizer = new LinOptimizer(GoalType.MINIMIZE, new AveragedStartingPointGenerator(new double[][]{{1.0, 1.0, 1.0}}), this.objectiveFunction);
        this.simplex.build(new double[]{1.0, 1.0, 1.0});
        this.simplex.iterate(this.objectiveFunction, null);
        this.simplex.evaluate(this.objectiveFunction, null);
    }

    @Test
    public void testAbstractSimplexConstructor() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        assertEquals("Dimension should be 3", 3, simplex.getDimension());
        simplex = new AbstractSimplex(3, 1.0);
        assertEquals("Dimension should be 3", 3, simplex.getDimension());
        simplex = new AbstractSimplex(new double[]{0.1, 0.2, 0.3});
        assertEquals("Dimension should be 3", 3, simplex.getDimension());
        simplex = new AbstractSimplex(new double[][]{{1.0, 1.0, 1.0}, {2.0, 2.0, 2.0}, {3.0, 3.0, 3.0}});
        assertEquals("Dimension should be 3", 3, simplex.getDimension());
    }

    @Test(expected = NullArgumentException.class)
    public void testAbstractSimplexConstructorNullSteps() {
        new AbstractSimplex(null);
    }

    @Test(expected = ZeroException.class)
    public void testAbstractSimplexConstructorZeroSteps() {
        new AbstractSimplex(new double[0]);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testAbstractSimplexConstructorInvalidReferenceSimplex() {
        new AbstractSimplex(new double[][]{{1.0, 1.0}, {1.0, 1.0}});
    }

    @Test(expected = EqualVerticesInSimplexException.class)
    public void testAbstractSimplexConstructorEqualVertices() {
        new AbstractSimplex(new double[]{{1.0, 1.0}, {1.0, 1.0}});
    }

    @Test
    public void testGetDimension() {
        assertEquals("Dimension should be 3", 3, simplex.getDimension());
    }

    @Test
    public void testGetSize() {
        assertEquals("Size should be 4", 4, simplex.getSize());
    }

    @Test
    public void testIterate() {
        double[] bestPoint = this.simplex.getPoint(3).getPointRef();
        double bestValue = this.simplex.getPoint(3).getValue();
        PointValuePair worstPoint = this.simplex.getPoint(0);

        this.simplex.iterate(this.objectiveFunction, null);

        PointValuePair bestPointNew = this.simplex.getPoint(3);
        assertNotEquals("Best point should have changed", bestPoint, bestPointNew.getPointRef());
        assertLessThan("New best value should be less than old best value", bestValue, bestPointNew.getValue());

        PointValuePair worstPointNew = this.simplex.getPoint(0);
        assertNotEquals("Worst point should have changed", worstPoint, worstPointNew);
    }

    @Test
    public void testBuild() {
        PointValuePair[] points = new PointValuePair[4];
        simplex.build(new double[]{1.0, 1.0, 1.0});
        points[0] = simplex.getPoint(0);
        points[1] = simplex.getPoint(1);
        points[2] = simplex.getPoint(2);
        points[3] = simplex.getPoint(3);

        for (int i = 0; i < 4; ++i) {
            double[] point = points[i].getPointRef();
            double step = 0.0;
            for (int j = 0; j < 3; ++j) {
                assertEquals("Vertex " + i + " should have the correct coordinate " + j, (i + 1) * 1.0 / (double) (simplex.getDimension() + 1) + step, point[j]);
                step += 1.0 / (double) (simplex.getDimension() + 1);
            }
        }
    }

    @Test
    public void testEvaluate() {
        double[] bestPoint = this.simplex.getPoint(3).getPointRef();
        double bestValue = this.simplex.getPoint(3).getValue();
        PointValuePair worstPoint = this.simplex.getPoint(0);

        this.simplex.evaluate(this.objectiveFunction, null);

        PointValuePair bestPointNew = this.simplex.getPoint(3);
        assertNotEquals("Best point should have changed", bestPoint, bestPointNew.getPointRef());
        assertLessThan("New best value should be less than old best value", bestValue, bestPointNew.getValue());

        PointValuePair worstPointNew = this.simplex.getPoint(0);
        assertNotEquals("Worst point should have changed", worstPoint, worstPointNew);
    }

    @Test
    public void testReplaceWorstPoint() {
        PointValuePair worstPoint = this.simplex.getPoint(0);
        PointValuePair newPoint = new PointValuePair(new double[]{1.1, 1.1, 1.1}, 100.0);
        this.simplex.replaceWorstPoint(newPoint, null);
        PointValuePair newWorstPoint = this.simplex.getPoint(0);
        assertNotEquals("Worst point should have been replaced", worstPoint, newWorstPoint);
    }

    @Test
    public void testGetPoints() {
        PointValuePair[] points = this.simplex.getPoints();
        assertEquals("Size of points array should be 4", 4, points.length);
        for (PointValuePair point : points) {
            double[] pointRef = point.getPointRef();
            for (int i = 0; i < 3; ++i) {
                assertTrue("Point " + point + " should be within the simplex bounds", this.simplex.getStartConfiguration()[i][i] <= pointRef[i] && pointRef[i] <= this.simplex.getStartConfiguration()[0][i]);
            }
        }
    }

    @Test
    public void testGetPoint() {
        PointValuePair point = this.simplex.getPoint(3);
        double[] pointRef = point.getPointRef();
        double value = point.getValue();
        for (int i = 0; i < 3; ++i) {
            assertTrue("Point " + point + " should be within the simplex bounds", this.simplex.getStartConfiguration()[i][i] <= pointRef[i] && pointRef[i] <= this.simplex.getStartConfiguration()[0][i]);
        }
        assertLessThan("Value of best point should be less than other points", value, this.simplex.getPoint(0).getValue());
    }

    @Test
    public void testSetPoint() {
        PointValuePair point = this.simplex.getPoint(3);
        double[] newPoint = new double[]{2.0, 2.0, 2.0};
        this.simplex.setPoint(3, new PointValuePair(newPoint, Double.NaN));
        PointValuePair newPointCheck = this.simplex.getPoint(3);
        assertNotEquals("Point 3 should have been changed", point, newPointCheck);
        assertArrayEquals("New point should be the one set", newPoint, newPointCheck.getPointRef());
    }

    @Test
    public void testSetPoints() {
        PointValuePair[] newPoints = new PointValuePair[4];
        for (int i = 0; i < 4; ++i) {
            newPoints[i] = new PointValuePair(new double[]{i + 1.0, i + 1.0, i + 1.0}, Double.NaN);
        }
        this.simplex.setPoints(newPoints);
        PointValuePair[] points = this.simplex.getPoints();
        for (int i = 0; i < 4; ++i) {
            assertNotEquals("Point " + i + " should have been changed", this.simplex.getPoint(i), points[i]);
            assertArrayEquals("New point should be the one set", newPoints[i].getPointRef(), points[i].getPointRef());
        }
    }

    @Test
    public void testCreateHypercubeSteps() {
        double[] steps = AbstractSimplex.createHypercubeSteps(3, 1.0);
        assertEquals("Size of steps array should be 3", 3, steps.length);
        for (int i = 0; i < 3; ++i) {
            assertEquals("Step " + i + " should be the same for all dimensions", steps[i], steps[0]);
        }
    }
}