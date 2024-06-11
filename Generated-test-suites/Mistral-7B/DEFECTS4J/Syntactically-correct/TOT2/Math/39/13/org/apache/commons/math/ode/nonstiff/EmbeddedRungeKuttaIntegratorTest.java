import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.special.BesselFunctions;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {

    private static final double ACCURACY = 1e-10;
    private static final double RELATIVE_Tolerance = 1e-6;
    private static final double ABSOLUTE_Tolerance = 1e-6;
    private static final double INITIAL_TIME = 0.0;
    private static final double FINAL_TIME = 2 * Math.PI;

    private ExpandableStatefulODE equations;
    private EmbeddedRungeKuttaIntegrator integrator;
    private RungeKuttaStepInterpolator interpolator;

    @Before
    public void setup() {
        equations = new ODEFunction("BesselODETest", 1, 1, this::besselFunction);
        integrator = new RungeKuttaFehlberg78Method(1.0, RELATIVE_Tolerance, ABSOLUTE_Tolerance, equations);
        interpolator = new RungeKuttaStepInterpolator();
    }

    @Test
    public void testIntegration() throws MathIllegalArgumentException {
        integrator.integrate(equations, INITIAL_TIME, FINAL_TIME);
        double[] y = equations.getCompleteState();
        assertEquals(BesselFunctions.ivesR0(1.0, FINAL_TIME), y[0], ACCURACY);
    }

    // Basic test case for the constructor
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullName() {
        new EmbeddedRungeKuttaIntegrator(null, true, null, null, null, null, 1.0, 1.0, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorEmptyTimeSteps() {
        double[] c = new double[0];
        new EmbeddedRungeKuttaIntegrator("TestMethod", true, c, null, null, null, 1.0, 1.0, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorEmptyWeights() {
        double[][] a = new double[0][];
        new EmbeddedRungeKuttaIntegrator("TestMethod", true, null, a, null, null, 1.0, 1.0, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullPrototype() {
        RungeKuttaStepInterpolator prototype = null;
        new EmbeddedRungeKuttaIntegrator("TestMethod", true, null, null, null, prototype, 1.0, 1.0, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntegrationWithIllegalTime() {
        integrator.integrate(equations, -1.0, FINAL_TIME);
    }

    @Test(expected = IllegalArgumentArgumentException.class)
    public void testIntegrationWithIllegalInitialState() {
        double[] initialState = new double[1];
        integrator.integrate(equations, INITIAL_TIME, INITIAL_TIME, initialState);
    }

    // Edge case: Testing the integrator with a time step that is slightly larger than the maximum allowed
    @Test
    public void testIntegrationWithMaxStepSize() throws MathIllegalArgumentException {
        double maxStep = Double.MAX_VALUE / 10.0;
        integrator = new RungeKuttaFehlberg78Method("TestMethod", true, null, null, null, null, 1.0, maxStep, null, null);
        integrator.integrate(equations, INITIAL_TIME, FINAL_TIME);
        double[] y = equations.getCompleteState();
        assertEquals(BesselFunctions.ivesR0(1.0, FINAL_TIME), y[0], ACCURACY);
    }

    // Edge case: Testing the integrator with a time step that is slightly smaller than the minimum allowed
    @Test
    public void testIntegrationWithMinStepSize() throws MathIllegalArgumentException {
        double minStep = 1.0e-15;
        integrator = new RungeKuttaFehlberg78Method("TestMethod", true, null, null, null, null, 1.0, minStep, null, null);
        integrator.integrate(equations, INITIAL_TIME, FINAL_TIME);
        double[] y = equations.getCompleteState();
        assertEquals(BesselFunctions.ivesR0(1.0, FINAL_TIME), y[0], ACCURACY);
    }

    private double[] besselFunction(double t, double[] y) {
        return new double[]{BesselFunctions.ivesR0(1.0, t)};
    }

}