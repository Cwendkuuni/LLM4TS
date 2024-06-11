package org.jsoup.select.test;

import org.jsoup.helper.ElementUtils;
import org.jsoup.nodes.Element;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(Parameterized.class)
public class CombiningEvaluatorTest {

    private Evaluator evaluator;
    private Element element;
    private Element target;

    public CombiningEvaluatorTest(Evaluator evaluator, String html, String selector, boolean expectedResult) {
        this.evaluator = evaluator;
        this.element = ElementUtils.parseHtml(html);
        this.target = ElementUtils.selectFirst(element, selector);
        this.expectedResult = expectedResult;
    }

    @Parameterized.Parameters(name = "{index}: {0}({1}) should {2} match {3}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {new And(Arrays.asList(new ElementName("p"), new ElementClass("foo"))),
                        "<html><body><p class=\"foo\"></p></body></html>", "p.foo", true},
                {new And(Arrays.asList(new ElementName("p"), new ElementClass("foo"))),
                        "<html><body><p class=\"bar\"></p></body></html>", "p.foo", false},
                {new And(Arrays.asList(new ElementName("p"), new ElementClass("foo"))),
                        "<html><body><p></p><p class=\"foo\"></p></body></html>", "p.foo", true},

                {new Or(Arrays.asList(new ElementName("p"), new ElementName("div"))),
                        "<html><body><p class=\"foo\"></p><div class=\"foo\"></div></body></html>", "p, div.foo", true},
                {new Or(Arrays.asList(new ElementName("p"), new ElementName("div"))),
                        "<html><body><p class=\"foo\"></p><div class=\"bar\"></div></body></html>", "p, div.foo", false},
                {new Or(Arrays.asList(new ElementName("p"), new ElementName("div"))),
                        "<html><body><p class=\"foo\"></p><div></div></body></html>", "p, div.foo", false},

                {new Or(Arrays.asList(new ElementName("p"), new ElementName("div"))).add(new ElementClass("foo")),
                        "<html><body><p class=\"foo\"></p><div class=\"foo\"></div></body></html>", "p, div.foo, p.foo, div.foo", true},
                {new Or(Arrays.asList(new ElementName("p"), new ElementName("div"))).add(new ElementClass("foo")),
                        "<html><body><p class=\"bar\"></p><div class=\"foo\"></div></body></html>", "p, div.foo, p.foo, div.foo", false},
                {new Or(Arrays.asList(new ElementName("p"), new ElementName("div"))).add(new ElementClass("foo")),
                        "<html><body><p class=\"foo\"></p><div></div></body></html>", "p, div.foo, p.foo, div.foo", false}
        });
    }

    private boolean expectedResult;

    @Test
    public void testMatches() {
        Evaluator evaluator = this.evaluator;
        Element target = this.target;
        boolean actualResult = evaluator.matches(ElementUtils.parseHtml("<html><body>" + ElementUtils.html(element) + "</body></html>"), target);
        assertThat(actualResult, is(expectedResult));
    }
}

class ElementName extends TagNameEvaluator {
    ElementName(String tagName) {
        super(tagName);
    }
}

class ElementClass extends AttributeContainsEvaluator {
    ElementClass(String attributeName) {
        super(attributeName, "");
    }
}

abstract class Evaluator {
    abstract boolean matches(Element root, Element node);
}

class TagNameEvaluator extends Evaluator {
    private String tagName;

    TagNameEvaluator(String tagName) {
        this.tagName = tagName;
    }

    @Override
    public boolean matches(Element root, Element node) {
        return node.tagName().equals(tagName);
    }
}

class AttributeContainsEvaluator extends Evaluator {
    private String attributeName;
    private String attributeValue;

    AttributeContainsEvaluator(String attributeName, String attributeValue) {
        this.attributeName = attributeName;
        this.attributeValue = attributeValue;
    }

    @Override
    public boolean matches(Element root, Element node) {
        return node.attr(attributeName).contains(attributeValue);
    }
}