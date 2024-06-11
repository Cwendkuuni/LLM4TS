import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.jsoup.nodes.Element;

@RunWith(MockitoJUnitRunner.class)
public class CombiningEvaluatorTest {

    @Test
    public void testAndMethod() {
        Evaluator evaluator1 = Mockito.mock(Evaluator.class);
        Evaluator evaluator2 = Mockito.mock(Evaluator.class);

        And and = new And(Arrays.asList(evaluator1, evaluator2));

        Element root = Mockito.mock(Element.class);
        Element node = Mockito.mock(Element.class);

        Mockito.when(evaluator1.matches(root, node)).thenReturn(true);
        Mockito.when(evaluator2.matches(root, node)).thenReturn(true);

        assertTrue(and.matches(root, node));
    }

    @Test
    public void testOrMethod() {
        Evaluator evaluator1 = Mockito.mock(Evaluator.class);
        Evaluator evaluator2 = Mockito.mock(Evaluator.class);

        Or or = new Or(Arrays.asList(evaluator1, evaluator2));

        Element root = Mockito.mock(Element.class);
        Element node = Mockito.mock(Element.class);

        Mockito.when(evaluator1.matches(root, node)).thenReturn(false);
        Mockito.when(evaluator2.matches(root, node)).thenReturn(true);

        assertTrue(or.matches(root, node));
    }

    @Test
    public void testEmptyEvaluatorsList() {
        And and = new And(new ArrayList<>());

        assertFalse(and.matches(Mockito.mock(Element.class), Mockito.mock(Element.class)));
    }

    @Test
    public void testNullRoot() {
        Evaluator evaluator = Mockito.mock(Evaluator.class);
        And and = new And(Arrays.asList(evaluator));

        assertFalse(and.matches(null, Mockito.mock(Element.class)));
    }

    @Test
    public void testNullEvaluator() {
        Evaluator evaluator1 = Mockito.mock(Evaluator.class);
        Evaluator evaluator2 = null;

        And and = new And(Arrays.asList(evaluator1, evaluator2));

        Element root = Mockito.mock(Element.class);
        Element node = Mockito.mock(Element.class);

        assertFalse(and.matches(root, node));
    }

    @Test
    public void testUnmatchedEvaluator() {
        Evaluator evaluator1 = Mockito.mock(Evaluator.class);
        Evaluator evaluator2 = Mockito.mock(Evaluator.class);
        Evaluator unmatchedEvaluator = Mockito.mock(Evaluator.class);

        And and = new And(Arrays.asList(evaluator1, evaluator2, unmatchedEvaluator));

        Element root = Mockito.mock(Element.class);
        Element node = Mockito.mock(Element.class);

        Mockito.when(evaluator1.matches(root, node)).thenReturn(true);
        Mockito.when(evaluator2.matches(root, node)).thenReturn(true);
        Mockito.when(unmatchedEvaluator.matches(root, node)).thenReturn(false);

        assertFalse(and.matches(root, node));
    }

    @Test
    public void testNullRootAndNode() {
        And and = new And(Arrays.asList(Mockito.mock(Evaluator.class), Mockito.mock(Evaluator.class)));

        assertFalse(and.matches(null, null));
    }
}