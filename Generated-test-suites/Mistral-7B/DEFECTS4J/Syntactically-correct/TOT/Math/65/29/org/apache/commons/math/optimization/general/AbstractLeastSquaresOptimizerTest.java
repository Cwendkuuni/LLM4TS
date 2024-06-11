package org.apache.commons.math.optimization.general;

import junit.framework.TestCase;
import org.apache.commons.math3.analysis.Polynomials;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.NonlinearOptimizer;
import org.apache.commons.math3.optim.nonlinear.gradient.LevenbergMarquardtOptimizer;
import org.apache.commons.math3.optim.nonlinear.leastsquares.AbstractLinearRegression;
import org.apache.commons.math3.optim.nonlinear.leastsquares.Line;
import org.apache.commons.math3.optim.nonlinear.leastsquares.LinearRegression;
import org.apache.commons.math3.optim.nonlinear.leastsquares.Residual;
import org.apache.commons.math3.optim.nonlinear.linear.LinearConstraint;
import org.apache.commons.math3.optim.nonlinear.linear.LinearEqualityConstraint;
import org.apache.commons.math3.optim.nonlinear.linear.LinearInequalityConstraint;
import org.apache.commons.math3.optim.nonlinear.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.linear.LinearSystem;
import org.apache.commons.math3.optim.nonlinear.linear.Regression;
import org.apache.commons.math3.optim.nonlinear.linear.WeightedLinearRegression;
import org.apache.commons.math3.optim.point.PointValuePair;

public class AbstractLeastSquaresOptimizerTest extends TestCase {

    /**
     * Test optimization for a simple linear regression problem (without any
     * constraint).
     */
    public void testSimpleLinearRegression() {
        double[] x = {1.0, 2.0, 3.0, 4.0};
        double[] y = {2.0, 4.0, 6.0, 8.0};
        LinearObjectiveFunction f = new LinearObjectiveFunction(new Line(0.5, 1.0));
        PointValuePair result = new AbstractLeastSquaresOptimizer().optimize(f, y, null, x);
        assertEquals(new PointValuePair(3.5, 0.5), result);
    }

    /**
     * Test optimization for a linear regression problem with a lower bound
     * constraint.
     */
    public void testLinearRegressionWithLowerBound() {
        double[] x = {1.0, 2.0, 3.0, 4.0};
        double[] y = {2.0, 4.0, 6.0, 8.0};
        LinearObjectiveFunction f = new LinearObjectiveFunction(new Line(0.5, 1.0));
        LinearConstraint lc = new LinearInequalityConstraint(new LinearSystem(1, 1, new double[]{-1.0}));
        PointValuePair result = new AbstractLeastSquaresOptimizer().optimize(f, y, lc, x);
        assertEquals(new PointValuePair(3.5, 0.5), result);
    }

    /**
     * Test optimization for a linear regression problem with equality constraints.
     */
    public void testLinearRegressionWithEqualityConstraints() {
        double[] x = {1.0, 2.0, 3.0, 4.0};
        double[] y = {2.0, 4.0, 6.0, 8.0};
        LinearObjectiveFunction f = new LinearObjectiveFunction(new Line(0.5, 1.0));
        LinearEqualityConstraint ec1 = new LinearEqualityConstraint(new LinearSystem(1, 1, new double[]{1.0, -1.0}));
        LinearEqualityConstraint ec2 = new LinearEqualityConstraint(new LinearSystem(1, 1, new double[]{1.0, 1.0}));
        PointValuePair result = new AbstractLeastSquaresOptimizer().optimize(f, y, new LinearConstraint[]{ec1, ec2}, x);
        assertEquals(new PointValuePair(3.5, 0.5), result);
    }

    /**
     * Test optimization for a weighted linear regression problem.
     */
    public void testWeightedLinearRegression() {
        double[] x = {1.0, 2.0, 3.0, 4.0};
        double[] y = {2.0, 4.0, 6.0, 8.0};
        double[] w = {1.0, 1.0, 1.0, 1.0};
        LinearObjectiveFunction f =
            new WeightedLinearRegression(new Line(0.5, 1.0), new SimpleBounds(0.0, 10.0), w);
        PointValuePair result = new AbstractLeastSquaresOptimizer().optimize(f, y, null, x);
        assertEquals(new PointValuePair(3.5, 0.5), result);
    }

    /**
     * Test optimization for a linear regression problem with non-zero initial
     * guess.
     */
    public void testLinearRegressionWithInitialGuess() {
        double[] x = {1.0, 2.0, 3.0, 4.0};
        double[] y = {2.0, 4.0, 6.0, 8.0};
        LinearObjectiveFunction f = new LinearObjectiveFunction(new Line(0.5, 1.0));
        PointValuePair initialGuess = new PointValuePair(new double[]{2.0, 0.5});;
        PointValuePair result = new AbstractLeastSquaresOptimizer().optimize(f, y, null, x, initialGuess);
        assertEquals(new PointValuePair(3.5, 0.5), result);
    }

    /**
     * Test optimization for a simple polynomial regression problem.
     */
    public void testSimplePolynomialRegression() {
        double[] x = {1.0, 2.0, 3.0, 4.0};
        double[] y = {1.0, 4.0, 9.0, 16.0};
        RealVector coefficients = new Array2DRowRealMatrix(1, 3).transpose().toVector();
        Polynomials p = new Polynomials(coefficients);
        Regression r = new LinearRegression(p, new InitialGuess(x, new double[x.length]), new SimpleBounds(0.0, 10.0));
        PointValuePair result = new AbstractLeastSquaresOptimizer().optimize(r, y, null, x);
        assertEquals(new PointValuePair(new double[]{1.0, 2.0, 1.0}, 0.0), result);
    }

    /**
     * Test optimization for a polynomial regression problem with lower bound
     * constraint.
     */
    public void testPolynomialRegressionWithLowerBound() {
        double[] x = {1.0, 2.0, 3.0, 4.0};
        double[] y = {1.0, 4.0, 9.0, 16.0};
        RealVector coefficients = new Array2DRowRealMatrix(1, 3).transpose().toVector();
        Polynomials p = new Polynomials(coefficients);
        Regression r = new LinearRegression(p, new InitialGuess(x, new double[x.length]), new SimpleBounds(0.0, 10.0));
        LinearConstraint lc = new LinearInequalityConstraint(new LinearSystem(1, 1, new double[]{-1.0}));
        PointValuePair result = new AbstractLeastSquaresOptimizer().optimize(r, y, lc, x);
        assertEquals(new PointValuePair(new double[]{1.0, 2.0, 1.0}, 0.0), result);
    }

    /**
     * Test optimization for a polynomial regression problem with equality constraints.
     */
    public void testPolynomialRegressionWithEqualityConstraints() {
        double[] x = {1.0, 2.0, 3.0, 4.0};
        double[] y = {1.0, 4.0, 9.0, 16.0};
        RealVector coefficients = new Array2DRowRealMatrix(1, 3).transpose().toVector();
        Polynomials p = new Polynomials(coefficients);
        Regression r = new LinearRegression(p, new InitialGuess(x, new double[x.length]), new SimpleBounds(0.0, 10.0));
        LinearEqualityConstraint ec1 = new LinearEqualityConstraint(new LinearSystem(1, 1, new double[]{1.0, -1.0}));
        LinearEqualityConstraint ec2 = new LinearEqualityConstraint(new LinearSystem(1, 1, new double[]{1.0, 1.0}));
        PointValuePair result = new AbstractLeastSquaresOptimizer().optimize(r, y, new LinearConstraint[]{ec1, ec2}, x);
        assertEquals(new PointValuePair(new double[]{1.0, 2.0, 1.0}, 0.0), result);
    }

    /**
     * Test optimization for an ill-posed polynomial regression problem.
     */
    public void testPolynomialRegressionWithSingularMatrix() {
        double[] x = {1.0, 2.0, 3.0, 4.0};
        double[] y = {1.0, 4.0, 9.0, 16.0};
        RealVector coefficients = new Array2DRowRealMatrix(1, 3).transpose().toVector();
        Polynomials p = new Polynomials(coefficients);
        Regression r = new LinearRegression(p, new InitialGuess(x, new double[x.length]), new SimpleBounds(0.0, 10.0));
        LinearInequalityConstraint ic = new LinearInequalityConstraint(new LinearSystem(1, 1, new double[]{1.0, 0.0}));
        try {
            new AbstractLeastSquaresOptimizer().optimize(r, y, ic, x);
            fail("Should have thrown an exception");
        } catch (OptimizationException e) {
            // Expected
        }
    }

    /**
     * Test optimization for a polynomial regression problem with a nonlinear
     * objective function.
     */
    public void testPolynomialRegressionWithNonlinearObjectiveFunction() {
        double[] x = {1.0, 2.0, 3.0, 4.0};
        double[] y = {Math.exp(1.0), Math.exp(2.0), Math.exp(3.0), Math.exp(4.0)};
        RealVector coefficients = new Array2DRowRealMatrix(1, 3).transpose().toVector();
        Polynomials p = new Polynomials(coefficients);
        Regression r = new LinearRegression(p, new InitialGuess(x, new double[x.length]), new SimpleBounds(0.0, 10.0));
        DifferentiableMultivariateVectorialFunction f = new Residual(new LinearObjectiveFunction(p), y);
        try {
            new AbstractLeastSquaresOptimizer().optimize(f, y, null, x);
            fail("Should have thrown an exception");
        } catch (OptimizationException e) {
            // Expected
        }
    }

    /**
     * Test optimization for a polynomial regression problem with a nonlinear objective function
     * and a lower bound constraint.
     */
    public void testPolynomialRegressionWithNonlinearObjectiveFunctionAndLowerBound() {
        double[] x = {1.0, 2.0, 3.0, 4.0};
        double[] y = {Math.exp(1.0), Math.exp(2.0), Math.exp(3.0), Math.exp(4.0)};
        RealVector coefficients = new Array2DRowRealMatrix(1, 3).transpose().toVector();
        Polynomials p = new Polynomials(coefficients);
        Regression r = new LinearRegression(p, new InitialGuess(x, new double[x.length]), new SimpleBounds(0.0, 10.0));
        DifferentiableMultivariateVectorialFunction f = new Residual(new LinearObjectiveFunction(p), y);
        LinearConstraint lc = new LinearInequalityConstraint(new LinearSystem(1, 1, new double[]{-1.0}));
        try {
            new AbstractLeastSquaresOptimizer().optimize(f, y, lc, x);
            fail("Should have thrown an exception");
        } catch (OptimizationException e) {
            // Expected
        }
    }

    /**
     * Test optimization for a polynomial regression problem with a nonlinear objective function
     * and equality constraints.
     */
    public void testPolynomialRegressionWithNonlinearObjectiveFunctionAndEqualityConstraints() {
        double[] x = {1.0, 2.0, 3.0, 4.0};
        double[] y = {Math.exp(1.0), Math.exp(2.0), Math.exp(3.0), Math.exp(4.0)};
        RealVector coefficients = new Array2DRowRealMatrix(1, 3).transpose().toVector();
        Polynomials p = new Polynomials(coefficients);
        Regression r = new LinearRegression(p, new InitialGuess(x, new double[x.length]), new SimpleBounds(0.0, 10.0));
        DifferentiableMultivariateVectorialFunction f = new Residual(new LinearObjectiveFunction(p), y);
        LinearEqualityConstraint ec1 = new LinearEqualityConstraint(new LinearSystem(1, 1, new double[]{1.0, -1.0}));
        LinearEqualityConstraint ec2 = new LinearEqualityConstraint(new LinearSystem(1, 1, new double[]{1.0, 1.0}));
        try {
            new AbstractLeastSquaresOptimizer().optimize(f, y, new LinearConstraint[]{ec1, ec2}, x);
            fail("Should have thrown an exception");
        } catch (OptimizationException e) {
            // Expected
        }
    }
}