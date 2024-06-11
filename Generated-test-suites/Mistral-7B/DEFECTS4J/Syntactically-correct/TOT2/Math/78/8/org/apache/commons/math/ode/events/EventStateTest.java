import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.events.EventHandler;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class EventStateTest {

 private EventState eventState;
 private EventHandler mockEventHandler;

 @Before
 public void setUp() {
 mockEventHandler = new MockEventHandler();
 eventState = new EventState(mockEventHandler, 0.01, 0.001, 100);
 }

 @Test
 public void testReinitializeBegin() throws EventException {
 double tStart = 0.0;
 double[] yStart = new double[]{1.0, 2.0};
 eventState.reinitializeBegin(tStart, yStart);
 assertEquals(mockEventHandler, eventState.getEventHandler());
 assertEquals(0.01, eventState.getMaxCheckInterval(), 0.0);
 assertEquals(0.001, eventState.getConvergence(), 0.0);
 assertEquals(100, eventState.getMaxIterationCount());
 assertEquals(Double.NaN, eventState.getT0());
 assertEquals(Double.NaN, eventState.getG0());
 assertFalse(eventState.isG0Positive());
 assertFalse(eventState.isPendingEvent());
 assertNaN(eventState.getPendingEventTime());
 assertNaN(eventState.getPreviousEventTime());
 assertTrue(eventState.isForward());
 assertFalse(eventState.isIncreasing());
 assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
 }

 @Test
 public void testEvaluateStepNoEvent() throws DerivativeException, EventException, ConvergenceException {
 StepInterpolator interpolator = new StepInterpolator() {
 @Override
 public boolean isForward() {
 return true;
 }

 @Override
 public double getCurrentTime() {
 return 0.1;
 }

 @Override
 public void setInterpolatedTime(double time) {
 // empty
 }

 @Override
 public double[] getInterpolatedState() {
 return new double[]{1.0, 2.0};
 }
 };
 assertFalse(eventState.evaluateStep(interpolator));
 assertEquals(Double.NaN, eventState.getPendingEventTime());
 assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
 }

 @Test
 public void testEvaluateStepEventOccurs() throws DerivativeException, EventException, ConvergenceException {
 StepInterpolator interpolator = new StepInterpolator() {
 @Override
 public boolean isForward() {
 return true;
 }

 @Override
 public double getCurrentTime() {
 return 0.2;
 }

 @Override
 public void setInterpolatedTime(double time) {
 interpolator.setCurrentTime(time);
 }

 @Override
 public double[] getInterpolatedState() {
 return new double[]{1.0, 2.0};
 }
 };
 interpolator.setCurrentTime(0.2);
 mockEventHandler.setExpectedEventTime(0.21);
 mockEventHandler.setExpectedEventValue(0.0);
 mockEventHandler.setExpectedEventDirection(true);
 eventState = new EventState(mockEventHandler, 0.01, 0.001, 100);
 eventState.reinitializeBegin(0.2, new double[]{1.0, 2.0});
 assertTrue(eventState.evaluateStep(interpolator));
 assertEquals(0.21, eventState.getPendingEventTime(), 0.0);
 assertEquals(EventHandler.RESET_STATE, eventState.getNextAction());
 }

 @Test(expected = DerivativeException.class)
 public void testEvaluateStepDerivativeException() throws DerivativeException, EventException, ConvergenceException {
 StepInterpolator interpolator = new StepInterpolator() {
 @Override
 public boolean isForward() {
 return true;
 }

 @Override
 public double getCurrentTime() {
 return 0.2;
 }

 @Override
 public void setInterpolatedTime(double time) {
 // empty
 }

 @Override
 public double[] getInterpolatedState() {
 return new double[]{1.0, 2.0};
 }
 };
 mockEventHandler.setExpectedEventTime(0.21);
 mockEventHandler.setExpectedEventValue(0.0);
 mockEventHandler.setExpectedEventDirection(true);
 eventState = new EventState(mockEventHandler, 0.01, 0.001, 100);
 eventState.reinitializeBegin(0.2, new double[]{1.0, 2.0});
 interpolator.setCurrentTime(0.2);
 interpolator.setInterpolatedTime(0.2);
 eventState.evaluateStep(interpolator);
 fail("Expected a DerivativeException");
 }

 @Test(expected = EventException.class)
 public void testEvaluateStepEventException() throws DerivativeException, EventException, ConvergenceException {
 StepInterpolator interpolator = new StepInterpolator() {
 @Override
 public boolean isForward() {
 return true;
 }

 @Override
 public double getCurrentTime() {
 return 0.2;
 }

 @Override
 public void setInterpolatedTime(double time) {
 interpolator.setCurrentTime(time);
 }

 @Override
 public double[] getInterpolatedState() {
 return new double[]{1.0, 2.0};
 }
 };
 mockEventHandler.setExpectedEventTime(0.21);
 mockEventHandler.setExpectedEventValue(0.0);
 mockEventHandler.setExpectedEventDirection(true);
 eventState = new EventState(mockEventHandler, 0.01, 0.001, 100);
 eventState.reinitializeBegin(0.2, new double[]{1.0, 2.0});
 interpolator.setCurrentTime(0.2);
 interpolator.setInterpolatedTime(0.2);
 mockEventHandler.throwEventException();
 eventState.evaluateStep(interpolator);
 fail("Expected an EventException");
 }

 @Test
 public void testEvaluateStepNoEventAfterStepAccepted() throws DerivativeException, EventException, ConvergenceException {
 StepInterpolator interpolator = new StepInterpolator() {
 @Override
 public boolean isForward() {
 return true;
 }

 @Override
 public double getCurrentTime() {
 return 0.3;
 }

 @Override
 public void setInterpolatedTime(double time) {
 interpolator.setCurrentTime(time);
 }

 @Override
 public double[] getInterpolatedState() {
 return new double[]{1.0, 2.0};
 }
 };
 eventState.stepAccepted(0.3, new double[]{1.0, 2.0});
 assertFalse(eventState.evaluateStep(interpolator));
 assertEquals(Double.NaN, eventState.getPendingEventTime());
 assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
 }

 @Test
 public void testStepAccepted() throws EventException {
 double t = 0.3;
 double[] y = new double[]{1.0, 2.0};
 eventState.stepAccepted(t, y);
 assertEquals(t, eventState.getT0());
 assertEquals(eventState.getEventHandler().g(t, y), eventState.getG0());
 assertEquals(eventState.isG0Positive(), eventState.isG0Positive());
 assertFalse(eventState.isPendingEvent());
 assertNaN(eventState.getPendingEventTime());
 assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
 }

 @Test
 public void testStop() {
 eventState.setNextAction(EventHandler.STOP);
 assertTrue(eventState.stop());
 }

 @Test
 public void testReset() throws EventException {
 double t = 0.4;
 double[] y = new double[]{1.0, 2.0};
 eventState.stepAccepted(t, y);
 eventState.setNextAction(EventHandler.RESET_STATE);
 assertTrue(eventState.reset(t, y));
 assertEquals(Double.NaN, eventState.getPendingEventTime());
 assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
 }

 @Test
 public void testResetDerivatives() throws EventException {
 double t = 0.4;
 double[] y = new double[]{1.0, 2.0};
 eventState.stepAccepted(t, y);
 eventState.setNextAction(EventHandler.RESET_DERIVATIVES);
 assertTrue(eventState.reset(t, y));
 assertFalse(eventState.isPendingEvent());
 assertNaN(eventState.getPendingEventTime());
 assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
 }

}

class MockEventHandler implements EventHandler {
 private double expectedEventTime;
 private double expectedEventValue;
 private boolean expectedEventDirection;

 @Override
 public double g(double t, double[] y) throws EventException {
 if (expectedEventTime != Double.NaN) {
 if (Math.abs(t - expectedEventTime) <= 0.001) {
 assertEquals(expectedEventValue, g, 0.0);
 assertEquals(expectedEventDirection, isIncreasing());
 expectedEventTime = Double.NaN;
 expectedEventValue = Double.NaN;
 expectedEventDirection = false;
 return nextActionValue();
 }
 }
 throw new EventException("Event handler exception");
 }

 @Override
 public int nextAction() {
 return EventHandler.CONTINUE;
 }

 @Override
 public int nextActionValue() {
 return 0;
 }

 @Override
 public void eventOccurred(double t, double[] y, boolean trigger) throws EventException {
 if (expectedEventTime == Double.NaN) {
 expectedEventTime = t;
 expectedEventValue = g(t, y);
 expectedEventDirection = trigger;
 } else if (Math.abs(t - expectedEventTime) <= 0.001) {
 assertEquals(expectedEventValue, g(t, y), 0.0);
 assertEquals(expectedEventDirection, isIncreasing());
 expectedEventTime = Double.NaN;
 expectedEventValue = Double.NaN;
 expectedEventDirection = false;
 } else {
 throw new EventException("Unexpected event occurrence");
 }
 }

 @Override
 public void resetState(double t, double[] y) throws EventException {
 // empty
 }

 @Override
 public void resetDerivatives() {
 // empty
 }

 public void setExpectedEventTime(double eventTime) {
 this.expectedEventTime = eventTime;
 }

 public void setExpectedEventValue(double eventValue) {
 this.expectedEventValue = eventValue;
 }

 public void setExpectedEventDirection(boolean eventDirection) {
 this.expectedEventDirection = eventDirection;
 }

 public void throwEventException() {
 expectedEventTime = Double.NaN;
 throw new EventException("Event handler exception");
 }

 private boolean isIncreasing() {
 return expectedEventDirection;
 }

}