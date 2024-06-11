import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DistanceMeasure;
import net.sf.javaml.core.Fold;
import net.sf.javaml.core.Instance;
import net.sf.javaml.distance.CosineDistance;
import org.junit.Test;
import static org.junit.Assert.*;

public class FoldTest {

 @Test
 public void testFoldInstanceAccess() {
 Fold fold = new Fold(new DefaultDataset(), new int[]{0, 1, 2, 3});
 Instance inst1 = fold.instance(0);
 Instance inst2 = fold.instance(1);
 DistanceMeasure dm = new CosineDistance();
 assertEquals(inst1.classValue(), inst2.classValue());
 assertNotSame(inst1, inst2);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testFoldAddInstance() {
 Fold fold = new Fold(new DefaultDataset(), new int[]{0, 1, 2, 3});
 Instance inst = new DefaultInstance(10.0, new double[]{1.0, 2.0});
 fold.add(inst);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testFoldFolds() {
 Fold fold = new Fold(new DefaultDataset(), new int[]{0, 1, 2, 3});
 fold.folds(5, new Random());
 }

 @Test
 public void testFoldSize() {
 Fold fold = new Fold(new DefaultDataset(), new int[]{0, 1, 2, 3});
 assertEquals(fold.size(), 4);
 }

 @Test
 public void testFoldIterator() {
 Fold fold = new Fold(new DefaultDataset(), new int[]{0, 1, 2, 3});
 for (Instance inst : fold) {
 Instance inst2 = fold.instance(fold.indexOf(inst));
 assertEquals(inst, inst2);
 }
 }
}