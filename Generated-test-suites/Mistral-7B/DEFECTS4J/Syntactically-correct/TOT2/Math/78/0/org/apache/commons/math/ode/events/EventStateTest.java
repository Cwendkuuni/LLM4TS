import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.events.MockEventHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.ode.space.VectorValue;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EventStateTest {

    private EventState eventState;
    private MockEventHandler mockEventHandler;
    private StepInterpolator stepInterpolator;
    private VectorValue initialState;

    @BeforeAll
    public void setup() {
        mockEventHandler = new MockEventHandler();
        stepInterpolator = new StepInterpolator();
        initialState = new VectorValue(new double[]{0.0, 1.0});
        eventState = new EventState(mockEventHandler, 0.1, 0.001, 10);
    }

    @Test
    public void testConstructor() {
        assertNotNull(eventState);
        assertEquals(mockEventHandler, eventState.getEventHandler());
        assertEquals(0.1, eventState.getMaxCheckInterval(), 0.0);
        assertEquals(0.001, eventState.getConvergence(), 0.0);
        assertEquals(10, eventState.getMaxIterationCount());
        assertTrue(Double.isNaN(eventState.getT0()));
        assertTrue(Double.isNaN(eventState.getG0()));
        assertFalse(eventState.isPendingEvent());
        assertEquals(Double.NaN, eventState.getPendingEventTime());
        assertEquals(Double.NaN, eventState.getPreviousEventTime());
        assertTrue(eventState.isForward());
        assertTrue(eventState.isIncreasing());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
    }

    @Test
    public void testReinitializeBegin() throws EventException {
        double tStart = 0.0;
        double[] yStart = new double[]{0.1, 0.2};
        eventState.reinitializeBegin(tStart, yStart);
        assertEquals(tStart, eventState.getT0(), 0.0);
        assertEquals(mockEventHandler.g(tStart, yStart), eventState.getG0(), 0.0);
    }

    @Test
    public void testEvaluateStep() throws DerivativeException, EventException, ConvergenceException {
        double t1 = 1.0;
        stepInterpolator.setInterpolatedState(initialState);
        stepInterpolator.setForward(true);
        stepInterpolator.setInterpolatedTime(t1);
        double gb = mockEventHandler.g(t1, initialState.toArray());
        eventState.evaluateStep(stepInterpolator);
        assertTrue(eventState.isPendingEvent());
        assertEquals(t1, eventState.getPendingEventTime(), 0.0);
        assertEquals(gb, eventState.getG0(), 0.0);
        assertEquals(EventHandler.EVENT_TRIGGERED, eventState.getNextAction());
    }

    @Test
    public void testStepAccepted() throws EventException {
        double t = 1.5;
        double[] y = new double[]{2.0, 3.0};
        eventState.stepAccepted(t, y);
        assertEquals(t, eventState.getT0(), 0.0);
        assertEquals(mockEventHandler.g(t, y), eventState.getG0(), 0.0);
        assertFalse(eventState.isPendingEvent());
        assertFalse(eventState.isResetting());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
    }

    @Test
    public void testReset() throws EventException {
        double t = 1.5;
        double[] y = new double[]{2.0, 3.0};
        eventState.reset(t, y);
        assertTrue(eventState.isResetting());
        assertFalse(eventState.isPendingEvent());
        assertFalse(eventState.isStopped());
    }

    @Test
    public void testStop() {
        eventState.setNextAction(EventHandler.STOP);
        assertTrue(eventState.stop());
    }

    @Test
    public void testGetEventTime() {
        double t1 = 1.0;
        eventState.setPendingEventTime(t1);
        assertEquals(t1, eventState.getEventTime(), 0.0);
    }

    @Test
    public void testGetEventHandler() {
        assertEquals(mockEventHandler, eventState.getEventHandler());
    }

    @Test
    public void testGetMaxCheckInterval() {
        assertEquals(0.1, eventState.getMaxCheckInterval(), 0.0);
    }

    @Test
    public void testGetConvergence() {
        assertEquals(0.001, eventState.getConvergence(), 0.0);
    }

    @Test
    public void testGetMaxIterationCount() {
        assertEquals(10, eventState.getMaxIterationCount());
    }

    @Test
    public void testGetT0() {
        double t0 = 0.0;
        eventState.setT0(t0);
        assertEquals(t0, eventState.getT0(), 0.0);
    }

    @Test
    public void testGetG0() {
        double g0 = 0.1;
        eventState.setG0(g0);
        assertEquals(g0, eventState.getG0(), 0.0);
    }

    @Test
    public void testIsPendingEvent() {
        assertTrue(eventState.isPendingEvent());
        eventState.setPendingEvent(false);
        assertFalse(eventState.isPendingEvent());
    }

    @Test
    public void testIsResetting() {
        assertFalse(eventState.isResetting());
        eventState.setResetting(true);
        assertTrue(eventState.isResetting());
    }

    @Test
    public void testIsStopped() {
        assertFalse(eventState.isStopped());
        eventState.setStopped(true);
        assertTrue(eventState.isStopped());
    }

    @Test
    public void testIsForward() {
        assertTrue(eventState.isForward());
        eventState.setForward(false);
        assertFalse(eventState.isForward());
    }

    @Test
    public void testIsIncreasing() {
        assertTrue(eventState.isIncreasing());
        eventState.setIncreasing(false);
        assertFalse(eventState.isIncreasing());
    }

    @Test
    public void testGetNextAction() {
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
        eventState.setNextAction(EventHandler.EVENT_TRIGGERED);
        assertEquals(EventHandler.EVENT_TRIGGERED, eventState.getNextAction());
    }

    @Test
    public void testEventException() {
        mockEventHandler = new MockEventHandler() {
            @Override
            public double g(double time, double[] state) throws EventException {
                throw new EventException("Test exception");
            }
        };
        eventState = new EventState(mockEventHandler, 0.1, 0.001, 10);
        assertThrows(EventException.class, () -> eventState.evaluateStep(stepInterpolator));
    }

    @Test
    public void testDerivativeException() {
        mockEventHandler = new MockEventHandler() {
            @Override
            public double g(double time, double[] state) throws EventException {
                throw new DerivativeException("Test derivative exception");
            }
        };
        eventState = new EventState(mockEventHandler, 0.1, 0.001, 10);
        assertThrows(DerivativeException.class, () -> eventState.evaluateStep(stepInterpolator));
    }

    @Test
    public void testFunctionEvaluationException() {
        mockEventHandler = new MockEventHandler() {
            @Override
            public double g(double time, double[] state) throws EventException {
                throw new FunctionEvaluationException("Test function evaluation exception");
            }
        };
        eventState = new EventState(mockEventHandler, 0.1, 0.001, 10);
        assertThrows(FunctionEvaluationException.class, () -> eventState.evaluateStep(stepInterpolator));
    }

    @Test
    public void testConvergenceException() {
        mockEventHandler = new MockEventHandler() {
            @Override
            public double g(double time, double[] state) throws EventException {
                return 0.0;
            }
        };
        eventState = new EventState(mockEventHandler, 0.1, 0.001, 10);
        assertThrows(ConvergenceException.class, () -> eventState.evaluateStep(stepInterpolator));
    }

    @Test
    public void testMaxEvalException() {
        mockEventHandler = new MockEventHandler() {
            @Override
            public double g(double time, double[] state) throws EventException {
                int count = 0;
                while (++count < 1000) {
                    if (time > 100.0) {
                        throw new MaxEvalException("Test max evaluation exception");
                    }
                }
                return 0.0;
            }
        };
        eventState = new EventState(mockEventHandler, 0.1, 0.001, 10);
        assertThrows(MaxEvalException.class, () -> eventState.evaluateStep(stepInterpolator));
    }

    @Test
    public void testNoBracketingException() {
        mockEventHandler = new MockEventHandler() {
            @Override
            public double g(double time, double[] state) throws EventException {
                throw new NoBracketingException("Test no bracketing exception");
            }
        };
        eventState = new EventState(mockEventHandler, 0.1, 0.001, 10);
        assertThrows(NoBracketingException.class, () -> eventState.evaluateStep(stepInterpolator));
    }

}