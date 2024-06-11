import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.nodes.Element;
import org.jsoup.helper.ElementFinder;

public class CombiningEvaluatorTest {

    @Test
    public void testCombiningEvaluator() {
        CombiningEvaluator ce = new CombiningEvaluator();
        assertNull(ce.matches(null, null));
        Element root = new ElementFinder("html").htmlParse("<html><body></body></html>");
        assertFalse(ce.matches(root, null));
        // Test null evaluators
        CombiningEvaluator ceNullEvaluators = new CombiningEvaluator(null);
        assertNull(ceNullEvaluators.matches(root, null));
        // Test empty evaluator list
        CombiningEvaluator ceEmptyEvaluators = new CombiningEvaluator();
        assertFalse(ceEmptyEvaluators.matches(root, node));
    }

    @Test
    public void testCombiningEvaluatorWithEvaluators() {
        Evaluator e1 = new ElementFinder("p");
        Evaluator e2 = new ElementFinder("div");
        CombiningEvaluator ce = new CombiningEvaluator(Arrays.asList(e1, e2));
        Element root = new ElementFinder("html").htmlParse("<html><body><p></p><div></div></body></html>");
        assertFalse(ce.matches(root, null));
        // Test matching with no nodes
        assertFalse(ce.matches(root, null));
    }

    @Test
    public void testAndEvaluator() {
        Evaluator e1 = new ElementFinder("p");
        Evaluator e2 = new ElementFinder("div");
        And and = new And(Arrays.asList(e1, e2));
        Element root = new ElementFinder("html").htmlParse("<html><body><p></p><div></div></body></html>");
        assertFalse(and.matches(root, null));
        // Test null evaluators
        And andNullEvaluators = new And(null);
        assertFalse(andNullEvaluators.matches(root, null));
        // Test empty evaluator list
        And andEmptyEvaluators = new And();
        assertFalse(andEmptyEvaluators.matches(root, node));
    }

    @Test
    public void testOrEvaluator() {
        Evaluator e1 = new ElementFinder("p");
        Evaluator e2 = new ElementFinder("div");
        Or or = new Or(Arrays.asList(e1, e2));
        Element root = new ElementFinder("html").htmlParse("<html><body><p></p></body></html>");
        assertFalse(or.matches(root, null));
        // Test single evaluator
        Or orSingleEvaluator = new Or(e1);
        root = new ElementFinder("html").htmlParse("<html><body><p></p></body></html>");
        assertFalse(orSingleEvaluator.matches(root, null));
        // Test null evaluator
        Or orNullEvaluator = new Or(null);
        assertFalse(orNullEvaluator.matches(root, null));
        // Test empty evaluator list
        Or orEmptyEvaluators = new Or();
        assertFalse(orEmptyEvaluators.matches(root, node));
    }

    @Test
    public void testNestedOrAndEvaluators() {
        Evaluator e1 = new ElementFinder("p");
        Evaluator e2 = new ElementFinder("div");
        Evaluator e3 = new ElementFinder("span");
        Or or = new Or(Arrays.asList(new And(Arrays.asList(e1, e2)), e3));
        Element root = new ElementFinder("html").htmlParse("<html><body><p></p><div></div><span></span></body></html>");
        assertFalse(or.matches(root, null));
        // Test null evaluators
        Or orNullEvaluators = new Or(Arrays.asList(new And(null), e3));
        assertFalse(orNullEvaluators.matches(root, null));
        // Test empty evaluator list
        Or orEmptyEvaluators = new Or();
        assertFalse(orEmptyEvaluators.matches(root, node));
    }
}