import org.jsoup.nodes.Element;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class CombiningEvaluatorTest {

 private CombiningEvaluator evaluator;
 private List<Evaluator> evaluators;

 @Parameterized.Parameters(name = "{0} test")
 public static List<Object[]> data() {
 return Arrays.asList(new Object[][]{
 new Object[]{new And(Arrays.asList(Mockito.mock(Evaluator.class), Mockito.mock(Evaluator.class))), Arrays.asList(Element.class, Element.class)},
 new Object[]{new Or(Arrays.asList(Mockito.mock(Evaluator.class), Mockito.mock(Evaluator.class))), Arrays.asList(Element.class, Element.class)},
 new Object[]{new And(Arrays.asList(Mockito.mock(Evaluator.class))), Arrays.asList(Element.class)}
 });
 }

 @Parameterized.Constructor
 public CombiningEvaluatorTest(CombiningEvaluator evaluator, List<Element> nodes) {
 this.evaluator = evaluator;
 this.evaluators = evaluator.getEvaluators();
 }

 @Test
 public void testMatches() {
 Element root = Mockito.mock(Element.class);
 Element node = Mockito.mock(Element.class);

 for (Evaluator evaluator : evaluators) {
 Mockito.when(evaluator.matches(root, node)).thenReturn(true);
 }

 assertTrue(evaluator.matches(root, node));
 }

 @Test
 public void testMatches_whenOneEvaluatorFails_returnsFalse() {
 Element root = Mockito.mock(Element.class);
 Element node = Mockito.mock(Element.class);

 for (int i = 0; i < evaluators.size(); i++) {
 if (i == 1) {
 Mockito.when(evaluators.get(i).matches(root, node)).thenReturn(false);
 } else {
 Mockito.when(evaluators.get(i).matches(root, node)).thenReturn(true);
 }
 }

 assertFalse(evaluator.matches(root, node));
 }

 @Test
 public void testToString() {
 String expected = "";
 if (evaluators.size() > 1) {
 expected += "(";
 for (int i = 0; i < evaluators.size() - 1; i++) {
 expected += evaluators.get(i).toString() + " ";
 }
 expected += evaluators.get(evaluators.size() - 1).toString() + ")";
 } else {
 expected = evaluators.get(0).toString();
 }

 assertEquals(expected, evaluator.toString());
 }
}