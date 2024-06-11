import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.util.ODEEventRecognizer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EmbeddedRungeKuttaIntegratorTest {

    private static final double ACCURACY = 1e-12;
    private EmbeddedRungeKuttaIntegrator integrator;
    private ExpandableStatefulODE equations;
    private StepHandler stepHandler;
    private ODEEventRecognizer eventRecognizer;

    @Before
    public void setUp() {
        integrator = new RungeKuttaFehlberg78Method(RungeKuttaStepInterpolator.RKF78_FSAL,
                0.0, 1.0, 1.0e-6, 1.0e-6, null, null);
        equations = new MockODEEquations();
        stepHandler = new MockStepHandler();
        eventRecognizer = new MockEventRecognizer();
    }

    @Test
    public void testBasicFunctionality() {
        double initialTime = 0.0;
        double finalTime = 2.0;
        integrator.integrate(equations, initialTime, finalTime);
        assertEquals(finalTime, equations.getTime(), ACCURACY);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testInvalidInitialTime() {
        double initialTime = Double.NEGATIVE_INFINITY;
        integrator.integrate(equations, initialTime, 1.0);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testInvalidFinalTime() {
        double finalTime = Double.POSITIVE_INFINITY;
        integrator.integrate(equations, 0.0, finalTime);
    }

    @Test
    public void testEvents() {
        double initialTime = 0.0;
        double finalTime = 1.0;
        equations.addEvent(1.0, new MockEvent());
        integrator.integrate(equations, initialTime, finalTime, stepHandler, eventRecognizer);
        assertEquals(1, stepHandler.getCallCount());
        assertEquals(1, eventRecognizer.getEventCount());
    }

    @Test
    public void testStepSizeControl() {
        double initialTime = 0.0;
        double finalTime = 1.0;
        double minStep = 0.01;
        double maxStep = 0.1;
        integrator = new RungeKuttaFehlberg78Method(RungeKuttaStepInterpolator.RKF78_FSAL,
                initialTime, finalTime, minStep, maxStep, null, null);
        integrator.integrate(equations, initialTime, finalTime);
        double stepSize = integrator.getStepsize();
        assertEquals(maxStep, stepSize, ACCURACY);

        initialTime = 0.0;
        finalTime = 2.0;
        integrator = new RungeKuttaFehlberg78Method(RungeKuttaStepInterpolator.RKF78_FSAL,
                initialTime, finalTime, minStep, maxStep, null, null);
        integrator.integrate(equations, initialTime, finalTime);
        stepSize = integrator.getStepsize();
        assertEquals(minStep, stepSize, ACCURACY);
    }

}

class MockODEEquations implements ExpandableStatefulODE {
    private double[] state;

    public MockODEEquations() {
        state = new double[1];
    }

    @Override
    public int getStateDimension() {
        return state.length;
    }

    @Override
    public int getOrder() {
        return 1;
    }

    @Override
    public double getTime() {
        return state[0];
    }

    @Override
    public void setTime(double time) {
        state[0] = time;
    }

    @Override
    public double[] getCompleteState() {
        return state;
    }

    @Override
    public void setCompleteState(double[] state) {
        this.state = state;
    }

    @Override
    public void setState(int index, double value) {
        this.state[index] = value;
    }

    @Override
    public double getState(int index) {
        return state[index];
    }

    @Override
    public void addEvent(double time, Object event) {

    }

    @Override
    public void addEvent(double time, Object event, Object userObject) {

    }

    @Override
    public void removeEvent(double time) {

    }

    @Override
    public void removeEvent(double time, Object event) {

    }

    @Override
    public void removeAllEvents() {

    }

    @Override
    public void setEventRecognizer(ODEEventRecognizer recognizer) {

    }

    @Override
    public boolean hasEvents() {
        return false;
    }

    @Override
    public void computeDerivatives(double time, double[] y, double[] yDot) {
        yDot[0] = 1.0;
    }

    @Override
    public void setEquations(ExpandableStatefulODE equations) {

    }
}

class MockEvent {

}

class MockStepHandler implements StepHandler {
    private int callCount = 0;

    @Override
    public void handleStep(ExpandableStatefulODE ode, double time, double[] y, double[] yDot, float flag)
            throws IllegalArgumentException {
        callCount++;
    }

    public int getCallCount() {
        return callCount;
    }

}

class MockEventRecognizer implements ODEEventRecognizer {
    private int eventCount = 0;

    @Override
    public void addEvent(double time, int eventType, Object flag) {

    }

    @Override
    public void removeEvent(double time, int eventType, Object flag) {

    }

    @Override
    public void reset() {

    }

    @Override
    public boolean recognize(ExpandableStatefulODE ode, double time, double[] state, double[] derivatives) {
        eventCount++;
        return false;
    }

    public int getEventCount() {
        return eventCount;
    }

}