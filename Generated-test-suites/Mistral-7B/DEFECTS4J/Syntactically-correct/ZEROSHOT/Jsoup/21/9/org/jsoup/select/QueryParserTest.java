import org.jsoup.helper.StringUtil;
import org.jsoup.parser.TokenQueue;
import org.jsoup.select.QueryParser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class QueryParserTest {

    private QueryParser queryParser;

    @Before
    public void setUp() {
        queryParser = new QueryParser("#test");
    }

    @After
    public void tearDown() {
        queryParser = null;
    }

    @Test
    public void testParseEmptyQuery() {
        String query = "";
        Evaluator evaluator = QueryParser.parse(query);
        assertNull(evaluator);
    }

    @Test
    public void testParseSingleSelector() {
        String query = "#test";
        Evaluator evaluator = queryParser.parse();
        assertNotNull(evaluator);
        assertSame(Evaluator.Id.class, evaluator.getClass());
        assertEquals("#test", evaluator.toString());
    }

    @Test
    public void testParseMultipleSelectorsWithCombinator() {
        String query = "#header > .nav > a";
        Evaluator evaluator = queryParser.parse();
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof CombiningEvaluator.And);
        CombiningEvaluator.And andEvaluator = (CombiningEvaluator.And) evaluator;
        assertEquals(3, andEvaluator.getEvaluators().size());
        Evaluator idEvaluator = andEvaluator.getEvaluators().get(0);
        assertEquals(Evaluator.Id.class, idEvaluator.getClass());
        assertEquals("#header", idEvaluator.toString());
        Evaluator tagNameEvaluator = andEvaluator.getEvaluators().get(1);
        assertEquals(Evaluator.Class.class, tagNameEvaluator.getClass());
        assertEquals(".nav", tagNameEvaluator.toString());
        Evaluator tagNameEvaluator2 = andEvaluator.getEvaluators().get(2);
        assertEquals(Evaluator.Tag.class, tagNameEvaluator2.getClass());
        assertEquals("a", tagNameEvaluator2.toString());
    }

    @Test
    public void testParseAttributeSelector() {
        String query = "[title='Test']";
        Evaluator evaluator = queryParser.parse();
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.Attribute);
        Evaluator.Attribute attributeEvaluator = (Evaluator.Attribute) evaluator;
        assertEquals("title", attributeEvaluator.getAttributeName());
        assertEquals("Test", attributeEvaluator.getAttributeValue());
    }

    @Test
    public void testParseClassSelector() {
        String query = ".class";
        Evaluator evaluator = queryParser.parse();
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.Class);
        Evaluator.Class classEvaluator = (Evaluator.Class) evaluator;
        assertEquals(".class", classEvaluator.toString());
    }

    @Test
    public void testParseIdSelector() {
        String query = "#id";
        Evaluator evaluator = queryParser.parse();
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.Id);
        Evaluator.Id idEvaluator = (Evaluator.Id) evaluator;
        assertEquals("#id", idEvaluator.toString());
    }

    @Test
    public void testParseTagSelector() {
        String query = "tag";
        Evaluator evaluator = queryParser.parse();
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.Tag);
        Evaluator.Tag tagEvaluator = (Evaluator.Tag) evaluator;
        assertEquals("tag", tagEvaluator.toString());
    }

    @Test
    public void testParsePseudoClassSelector() {
        String query = ":empty";
        Evaluator evaluator = queryParser.parse();
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.PseudoClass);
        Evaluator.PseudoClass pseudoClassEvaluator = (Evaluator.PseudoClass) evaluator;
        assertEquals(":empty", pseudoClassEvaluator.toString());
    }

    @Test
    public void testParsePseudoElementSelector() {
        String query = ":first-child";
        Evaluator evaluator = queryParser.parse();
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.PseudoElement);
        Evaluator.PseudoElement pseudoElementEvaluator = (Evaluator.PseudoElement) evaluator;
        assertEquals(":first-child", pseudoElementEvaluator.toString());
    }

    @Test
    public void testParseDescendantSelector() {
        String query = "div p";
        Evaluator evaluator = queryParser.parse();
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof CombiningEvaluator.Descendant);
        CombiningEvaluator.Descendant descendantEvaluator = (CombiningEvaluator.Descendant) evaluator;
        assertEquals(2, descendantEvaluator.getSpecificity());
        Evaluator parentEvaluator = descendantEvaluator.getLeft();
        assertEquals(Evaluator.Tag.class, parentEvaluator.getClass());
        assertEquals("div", parentEvaluator.toString());
        Evaluator childEvaluator = descendantEvaluator.getRight();
        assertEquals(Evaluator.Tag.class, childEvaluator.getClass());
        assertEquals("p", childEvaluator.toString());
    }

    @Test
    public void testParseChildSelector() {
        String query = "div > p";
        Evaluator evaluator = queryParser.parse();
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof CombiningEvaluator.ImmediateChild);
        CombiningEvaluator.ImmediateChild immediateChildEvaluator = (CombiningEvaluator.ImmediateChild) evaluator;
        assertEquals(2, immediateChildEvaluator.getSpecificity());
        Evaluator parentEvaluator = immediateChildEvaluator.getLeft();
        assertEquals(Evaluator.Tag.class, parentEvaluator.getClass());
        assertEquals("div", parentEvaluator.toString());
        Evaluator childEvaluator = immediateChildEvaluator.getRight();
        assertEquals(Evaluator.Tag.class, childEvaluator.getClass());
        assertEquals("p", childEvaluator.toString());
    }

    @Test
    public void testParseAttributeWithValueSelector() {
        String query = "[title='Test']";
        Evaluator evaluator = queryParser.parse();
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.AttributeWithValue);
        Evaluator.AttributeWithValue attributeWithValueEvaluator = (Evaluator.AttributeWithValue) evaluator;
        assertEquals("title", attributeWithValueEvaluator.getAttributeName());
        assertEquals("Test", attributeWithValueEvaluator.getAttributeValue());
    }

    @Test
    public void testParseAttributeWithValueStartingSelector() {
        String query = "[title^='Test']";
        Evaluator evaluator = queryParser.parse();
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.AttributeWithValueStarting);
        Evaluator.AttributeWithValueStarting attributeWithValueStartingEvaluator = (Evaluator.AttributeWithValueStarting) evaluator;
        assertEquals("title", attributeWithValueStartingEvaluator.getAttributeName());
        assertEquals("Test", attributeWithValueStartingEvaluator.getAttributeValueStarting());
    }

    @Test
    public void testParseAttributeWithValueEndingSelector() {
        String query = "[title$='Test']";
        Evaluator evaluator = queryParser.parse();
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.AttributeWithValueEnding);
        Evaluator.AttributeWithValueEnding attributeWithValueEndingEvaluator = (Evaluator.AttributeWithValueEnding) evaluator;
        assertEquals("title", attributeWithValueEndingEvaluator.getAttributeName());
        assertEquals("Test", attributeWithValueEndingEvaluator.getAttributeValueEnding());
    }

    @Test
    public void testParseAttributeWithValueContainingSelector() {
        String query = "[title*='Test']";
        Evaluator evaluator = queryParser.parse();
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.AttributeWithValueContaining);
        Evaluator.AttributeWithValueContaining attributeWithValueContainingEvaluator = (Evaluator.AttributeWithValueContaining) evaluator;
        assertEquals("title", attributeWithValueContainingEvaluator.getAttributeName());
        assertEquals("Test", attributeWithValueContainingEvaluator.getAttributeValueContaining());
    }

    @Test
    public void testParseAttributeWithValueMatchingSelector() {
        String query = "[title~='Test']";
        Evaluator evaluator = queryParser.parse();
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.AttributeWithValueMatching);
        Evaluator.AttributeWithValueMatching attributeWithValueMatchingEvaluator = (Evaluator.AttributeWithValueMatching) evaluator;
        assertEquals("title", attributeWithValueMatchingEvaluator.getAttributeName());
        assertTrue(Pattern.compile(attributeWithValueMatchingEvaluator.getAttributeValue()).matcher("Test Title Test").find());
    }

    @Test
    public void testParseAttributeWithValueNotSelector() {
        String query = "[title!='Test']";
        Evaluator evaluator = queryParser.parse();
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.AttributeWithValueNot);
        Evaluator.AttributeWithValueNot attributeWithValueNotEvaluator = (Evaluator.AttributeWithValueNot) evaluator;
        assertEquals("title", attributeWithValueNotEvaluator.getAttributeName());
        assertEquals("Test", attributeWithValueNotEvaluator.getAttributeValue());
    }

    @Test
    public void testParsePrefixSelector() {
        String query = "> .prefix";
        Evaluator evaluator = queryParser.parse();
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.Prefix);
        Evaluator prefixEvaluator = (Evaluator.Prefix) evaluator;
        assertEquals("> .prefix", prefixEvaluator.toString());
    }

    @Test
    public void testParseSuffixSelector() {
        String query = "p ~ .suffix";
        Evaluator evaluator = queryParser.parse();
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.Suffix);
        Evaluator suffixEvaluator = (Evaluator.Suffix) evaluator;
        assertEquals("p ~ .suffix", suffixEvaluator.toString());
    }

    @Test
    public void testParseNegatedSelector() {
        String query = ":not(#test)";
        Evaluator evaluator = queryParser.parse();
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.Negated);
        Evaluator negatedEvaluator = (Evaluator.Negated) evaluator;
        assertEquals("#test", negatedEvaluator.getSelector().toString());
    }

    @Test
    public void testParseGroupSelector() {
        String query = "#header > .nav > ul > li > a:not([href^=" + "'/admin'" + "]):not([href^=" + "'/login'" + "])";
        Evaluator evaluator = queryParser.parse();
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof CombiningEvaluator.Group);
        CombiningEvaluator.Group groupEvaluator = (CombiningEvaluator.Group) evaluator;
        assertEquals(6, groupEvaluator.getEvaluators().size());
        Evaluator parentEvaluator = groupEvaluator.getEvaluators().get(0);
        assertEquals(Evaluator.Tag.class, parentEvaluator.getClass());
        assertEquals("ul", parentEvaluator.toString());
        Evaluator descendantEvaluator1 = groupEvaluator.getEvaluators().get(1);
        assertEquals(Evaluator.Tag.class, descendantEvaluator1.getClass());
        assertEquals("li", descendantEvaluator1.toString());
        Evaluator descendantEvaluator2 = groupEvaluator.getEvaluators().get(2);
        assertEquals(Evaluator.Tag.class, descendantEvaluator2.getClass());
        assertEquals("a", descendantEvaluator2.toString());
        Evaluator negatedEvaluator1 = (Evaluator.Negated) groupEvaluator.getEvaluators().get(3);
        assertEquals("#test", negatedEvaluator1.getSelector().toString());
        Evaluator attributeEvaluator1 = (Evaluator.AttributeWithValue) groupEvaluator.getEvaluators().get(4);
        assertEquals("href", attributeEvaluator1.getAttributeName());
        assertEquals("/admin", attributeEvaluator1.getAttributeValue());
        Evaluator attributeEvaluator2 = (Evaluator.AttributeWithValue) groupEvaluator.getEvaluators().get(5);
        assertEquals("href", attributeEvaluator2.getAttributeName());
        assertEquals("/login", attributeEvaluator2.getAttributeValue());
    }
}