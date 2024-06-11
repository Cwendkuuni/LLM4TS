import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.ConvergenceChecker;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseOptimizerTest<PAIR extends PointValuePair<Point, Number>> {

 private BaseOptimizer<PAIR> optimizer;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 optimizer = new BaseOptimizer<PAIR>() {
 @Override
 public int getMaxEvaluations() {
 return MaxEval.UNLIMITED;
 }

 @Override
 public int getEvaluations() {
 return 0;
 }

 @Override
 public ConvergenceChecker<PAIR> getConvergenceChecker() {
 return new ConvergenceChecker<PAIR>() {
 @Override
 public boolean checkConvergence(PAIR currentBest, PAIR currentPoint) {
 return false;
 }
 };
 }
 };
 }

 @Test
 public void testGetMaxEvaluations() {
 assertEquals(MaxEval.UNLIMITED, optimizer.getMaxEvaluations());
 }

 @Test
 public void testGetEvaluations() {
 assertEquals(0, optimizer.getEvaluations());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetConvergenceCheckerWithNoSetter() {
 optimizer.getConvergenceChecker(); // no getter error
 }

 @Test
 public void testSetGetConvergenceChecker() {
 ConvergenceChecker<PAIR> convergenceChecker = new ConvergenceChecker<PAIR>() {
 @Override
 public boolean checkConvergence(PAIR currentBest, PAIR currentPoint) {
 return true;
 }
 };
 optimizer.setConvergenceChecker(convergenceChecker);
 ConvergenceChecker<PAIR> retrievedChecker = optimizer.getConvergenceChecker();
 assertEquals(convergenceChecker, retrievedChecker);
}
}