package org.jsoup.select;

import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Element;
import org.jsoup.select.CombiningEvaluator;
import org.jsoup.select.Elements;
import org.junit.Test;
import static org.junit.Assert.*;

public class CombiningEvaluatorTest {
    // Previous tests

    @Test
    public void testToString() {
        CombiningEvaluator and = new CombiningEvaluator.And(
                new Evaluator() {
                    @Override
                    public boolean matches(Element root, Element node) {
                        return "class1".equals(node.tagName());
                    }
                },
                new Evaluator() {
                    @Override
                    public boolean matches(Element root, Element node) {
                        return "class2".equals(node.tagName());
                    }
                }
        );

        String expected = "class1 class2";
        assertEquals(expected, and.toString());

        CombiningEvaluator or = new CombiningEvaluator.Or();
        or.add(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return "class1".equals(node.tagName());
            }
        });
        or.add(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return "class2".equals(node.tagName());
            }
        });

        expected = ":or class1 class2";
        assertEquals(expected, or.toString());
    }

    @Test
    public void testCombiningEvaluatorWithEmptySelectors() {
        Element root = Element.parse("<html><body></body></html>");

        // Test And
        CombiningEvaluator and = new CombiningEvaluator.And();
        assertFalse(and.matches(root, null));

        // Test Or
        CombiningEvaluator or = new CombiningEvaluator.Or();
        assertFalse(or.matches(root, null));
    }

    @Test
    public void testCombiningEvaluatorWithSingleEvaluator() {
        Element root = Element.parse("<html><body><div class='test'></div></body></html>");

        // Test And
        CombiningEvaluator and = new CombiningEvaluator.And(
                new Evaluator() {
                    @Override
                    public boolean matches(Element root, Element node) {
                        return "div".equals(node.tagName());
                    }
                }
        );
        assertTrue(and.matches(root, root.select("div").first()));

        // Test Or
        CombiningEvaluator or = new CombiningEvaluator.Or(
                new Evaluator() {
                    @Override
                    public boolean matches(Element root, Element node) {
                        return "div".equals(node.tagName());
                    }
                }
        );
        assertTrue(or.matches(root, root.select("div").first()));
    }

    @Test
    public void testCombiningEvaluatorWithMultipleEvaluators() {
        Element root = Element.parse("<html><body><div class='class1'></div><div class='class2'></div></body></html>");

        // Test And
        CombiningEvaluator and = new CombiningEvaluator.And(
                new Evaluator() {
                    @Override
                    public boolean matches(Element root, Element node) {
                        return "div".equals(node.tagName());
                    }
                },
                new Evaluator() {
                    @Override
                    public boolean matches(Element root, Element node) {
                        return "class1".equals(node.className());
                    }
                }
        );
        assertFalse(and.matches(root, root.select("div.class2").first()));
        assertTrue(and.matches(root, root.select("div.class1").first()));

        // Test Or
        CombiningEvaluator or = new CombiningEvaluator.Or(
                new Evaluator() {
                    @Override
                    public boolean matches(Element root, Element node) {
                        return "div".equals(node.tagName());
                    }
                },
                new Evaluator() {
                    @Override
                    public boolean matches(Element root, Element node) {
                        return "class1".equals(node.className());
                    }
                }
        );
        assertTrue(or.matches(root, root.select("div.class1").first()));
        assertTrue(or.matches(root, root.select("div").first()));
    }

    @Test
    public void testCombiningEvaluatorWithEmptyCollection() {
        Element root = Element.parse("<html><body></body></html>");

        // Test And
        CombiningEvaluator and = new CombiningEvaluator.And();
        assertFalse(and.matches(root, root.select("div").empty()));

        // Test Or
        CombiningEvaluator or = new CombiningEvaluator.Or();
        assertFalse(or.matches(root, root.select("div").empty()));
    }

    @Test
    public void testCombiningEvaluatorWithNull() {
        // Test And
        CombiningEvaluator and = new CombiningEvaluator.And();
        assertFalse(and.matches(null, null));

        // Test Or
        CombiningEvaluator or = new CombiningEvaluator.Or();
        assertFalse(or.matches(null, null));
    }
}