package org.jsoup.select.test;

import org.jsoup.helper.ElementFinder;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Element;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class CombiningEvaluatorTest {

    private static final ElementFinder finder = new ElementFinder();

    private CombiningEvaluator evaluator;
    private List<Evaluator> evaluators;
    private Element root;
    private String selector;
    private Element expectedResult;

    public CombiningEvaluatorTest(String selector, Element root, Evaluator... evaluators) {
        this(selector, root, Arrays.asList(evaluators));
    }

    public CombiningEvaluatorTest(String selector, Element root, Collection<Evaluator> evaluators) {
        this.selector = selector;
        this.root = root;
        this.evaluators = evaluators;
        this.evaluator = createCombiningEvaluator(evaluators);
    }

    @Parameterized.Parameters(name = "{index}: {0} = {1}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"a > b > c", ElementFinder.parse("html<body><a id='a'><b id='b'><c id='c'></c></b></a></body>"), Arrays.asList(new ElementSelector("a"), new ElementSelector("b"))},
                {"a > b > c, b > d > e", ElementFinder.parse("html<body><a id='a'><b id='b'><c id='c'></c><d id='d'><e id='e'></e></d></b></a></body>"), Arrays.asList(new ElementSelector("a"), new ElementSelector("b"), new ElementSelector("d"))},
                {"a:only-child > b:only-child > c:only-child", ElementFinder.parse("html<body><a><b><c></c></b></a></body>"), Arrays.asList(new ElementSelector("a:only-child"), new ElementSelector("b:only-child"), new ElementSelector("c:only-child"))},
                {"a:only-child > b > c", ElementFinder.parse("html<body><a><b id='b'><c id='c'></c></b></a></body>"), Arrays.asList(new ElementSelector("a:only-child"), new ElementSelector("b"))},
                {"a:only-child > b, a:only-child > c", ElementFinder.parse("html<body><a><b id='b'></b><c id='c'></c></a></body>"), Arrays.asList(new ElementSelector("a:only-child"), new ElementSelector("b"), new ElementSelector("c"))},
                {"a:only-child > b:not(.foo), a:only-child > c:not(.foo)", ElementFinder.parse("html<body><a><b id='b' class='bar'>Not foo</b><c id='c' class='bar'>Not foo</c></a></body>"), Arrays.asList(new ElementSelector("a:only-child"), new NotSelector(".foo"), new ElementSelector("b"))},
                {"a:not(.foo) > b:not(.foo) > c:not(.foo)", ElementFinder.parse("html<body><a class='bar'>Not foo</a><b class='bar'>Not foo</b><c class='bar'>Not foo</c></body>"), Arrays.asList(new NotSelector(".foo"), new ElementSelector("a"), new NotSelector(".foo"), new ElementSelector("b"), new NotSelector(".foo"), new ElementSelector("c"))},
                {"a > b > c:empty", ElementFinder.parse("html<body><a id='a'><b id='b'></b><c id='c'></c></a></body>"), Arrays.asList(new ElementSelector("a"), new ElementSelector("b"), new EmptySelector())},
                {"a:not(.foo) > b > c", ElementFinder.parse("html<body><a class='bar'>Not foo</a><b id='b'><c id='c'></c></b></a></body>"), Arrays.asList(new NotSelector(".foo"), new ElementSelector("a"), new ElementSelector("b"))},
                {"a > b:empty > c", ElementFinder.parse("html<body><a id='a'><b></b><c id='c'></c></a></body>"), Arrays.asList(new ElementSelector("a"), new EmptySelector(), new ElementSelector("b"))},
                {"a:not(.foo) > b > c:not(.foo)", ElementFinder.parse("html<body><a class='bar'>Not foo</a><b class='foo'>Foo</b><c class='bar'>Not foo</c></a></body>"), Arrays.asList(new NotSelector(".foo"), new ElementSelector("a"), new NotSelector(".foo"), new ElementSelector("c"))},
                {"a:not(.foo) > b > c:not(.foo)", ElementFinder.parse("html<body><a class='bar'>Not foo</a><b class='foo'>Foo</b><c class='bar'>Foo</c></a></body>"), Arrays.asList(new NotSelector(".foo"), new ElementSelector("a"), new ElementSelector("c"))},
                {"a > b:not(.foo) > c > d:not(.foo)", ElementFinder.parse("html<body><a id='a'><b class='foo'>Foo</b><c id='c' class='bar'>Not foo</c><d class='foo'>Foo</d></a></body>"), Arrays.asList(new ElementSelector("a"), new NotSelector(".foo"), new ElementSelector("b"), new NotSelector(".foo"), new ElementSelector("c"), new ElementSelector("d"))}
        });
    }

    @Test
    public void testMatches() {
        Element result = finder.findDeep(root, selector, 0);
        assertTrue("Expected selector to match: " + StringUtil.join(evaluators, " > ") + ", but didn't", evaluator.matches(root, result));
        expectedResult = result;
    }

    @Test
    public void testToString() {
        String expected = StringUtil.join(evaluators, " > ");
        String actual = evaluator.toString();
        assertTrue("Expected: " + expected + ", but got: " + actual, actual.equals(expected));
    }

    @Test
    public void testMismatches() {
        Element result = finder.findDeep(root, "xpath-mismatch", 0);
        assertFalse("Expected selector to not match: " + StringUtil.join(evaluators, " > ") + ", but did", evaluator.matches(root, result));
        if (evaluator instanceof And) {
            for (Evaluator evaluator1 : ((And) evaluator).evaluators) {
                if (evaluator1 instanceof ElementSelector) {
                    Element selectorElement = finder.findDeep(root, ((ElementSelector) evaluator1).selector, 0);
                    assertFalse("Expected selector to not match: " + StringUtil.join(evaluators, " > ") + ", but did (mismatch with " + ((ElementSelector) evaluator1).selector + ")", evaluator.matches(root, selectorElement));
                }
            }
        } else if (evaluator instanceof Or) {
            for (Evaluator evaluator1 : ((Or) evaluator).evaluators) {
                if (evaluator1 instanceof ElementSelector) {
                    Element selectorElement = finder.findDeep(root, ((ElementSelector) evaluator1).selector, 0);
                    assertFalse("Expected selector to not match: " + StringUtil.join(evaluators, " > ") + ", but did (mismatch with " + ((ElementSelector) evaluator1).selector + ")", evaluator.matches(root, selectorElement));
                }
                else if (evaluator1 instanceof CombiningEvaluator) {
                    CombiningEvaluator subEvaluator = (CombiningEvaluator) evaluator1;
                    testMismatches(subEvaluator, root, selector);
                }
            }
        }
    }

    private void testMismatches(CombiningEvaluator subEvaluator, Element root, String selector) {
        Element result = finder.findDeep(root, selector, 0);
        assertFalse("Expected selector to not match: " + subEvaluator.toString() + ", but did", subEvaluator.matches(root, result));
    }

    private CombiningEvaluator createCombiningEvaluator(List<Evaluator> evaluators) {
        try {
            return evaluators.get(0) instanceof And ? new And(evaluators) : new Or(evaluators);
        } catch (ClassCastException e) {
            throw new RuntimeException("Expected the first evaluator to be And or Or, but got " + evaluators.get(0).getClass().getSimpleName(), e);
        }
    }
}