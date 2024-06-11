import org.junit.Test;
import org.jsoup.parser.TokenQueue;
import org.jsoup.parser.QueryParser;
import static org.junit.Assert.*;

public class QueryParserTest {

    @Test
    public void testParseSimpleSelector() {
        String query = "div.class1";
        Evaluator evaluator = QueryParser.parse(query);
        StructuralEvaluator.Tag tagEvaluator = (StructuralEvaluator.Tag) evaluator;
        assertEquals("div", tagEvaluator.tagName());
        assertEquals("class1", tagEvaluator.className());
    }

    @Test
    public void testParseComplexSelector() {
        String query = "div#id1 > ul > li:nth-child(2)";
        Evaluator evaluator = QueryParser.parse(query);
        CombiningEvaluator andEvaluator = (CombiningEvaluator.And) evaluator;
        StructuralEvaluator.Tag tagEvaluator = (StructuralEvaluator.Tag) andEvaluator.getEvaluator(0);
        assertEquals("div", tagEvaluator.tagName());
        assertEquals("id1", tagEvaluator.getAttributeValue("id"));

        StructuralEvaluator.ImmediateChild immediateChildEvaluator = (StructuralEvaluator.ImmediateChild) andEvaluator.getEvaluator(1);
        StructuralEvaluator.Tag ulTagEvaluator = (StructuralEvaluator.Tag) immediateChildEvaluator.getEvaluator(0);
        assertEquals("ul", ulTagEvaluator.tagName());

        StructuralEvaluator.NthChild nthChildEvaluator = (StructuralEvaluator.NthChild) andEvaluator.getEvaluator(2);
        assertEquals(2, nthChildEvaluator.index());
        assertEquals("li", nthChildEvaluator.tagName());
    }

    @Test
    public void testParseInvalidSelector() {
        String query = "div#id1 > ul > li:nth-child(2)#invalid";
        try {
            QueryParser.parse(query);
            fail("Expected Selector.SelectorParseException");
        } catch (Selector.SelectorParseException e) {
            assertEquals("Unexpected identifier: invalid", e.getMessage());
        }
    }

    @Test
    public void testParseAttributeSelector() {
        String query = "div[class='class1']";
        Evaluator evaluator = QueryParser.parse(query);
        Evaluator attributeEvaluator = evaluator;
        StructuralEvaluator.Tag tagEvaluator = null;

        while (attributeEvaluator instanceof Evaluator.Attribute) {
            attributeEvaluator = attributeEvaluator.getNext();
            Evaluator.Attribute attributeEval = (Evaluator.Attribute) attributeEvaluator;
            assertEquals("class", attributeEval.attributeName());
            assertEquals("class1", attributeEval.getAttributeValue("class"));
            tagEvaluator = (StructuralEvaluator.Tag) attributeEvaluator.getPrevious();
        }

        assertEquals("div", tagEvaluator.tagName());
    }

    @Test
    public void testParseAttributeSelectorWithValue() {
        String query = "div[class='class1' and id='id1']";
        Evaluator evaluator = QueryParser.parse(query);
        CombiningEvaluator andEvaluator = (CombiningEvaluator.And) evaluator;
        Evaluator tagEvaluator = andEvaluator.getEvaluator(0);
        StructuralEvaluator.Tag tag = (StructuralEvaluator.Tag) tagEvaluator;
        assertEquals("div", tag.tagName());

        Evaluator attributeEvaluator1 = andEvaluator.getEvaluator(1);
        Evaluator.Attribute attributeEval1 = (Evaluator.Attribute) attributeEvaluator1;
        assertEquals("class", attributeEval1.attributeName());
        assertEquals("class1", attributeEval1.getAttributeValue("class"));

        Evaluator attributeEvaluator2 = andEvaluator.getEvaluator(2);
        Evaluator.Attribute attributeEval2 = (Evaluator.Attribute) attributeEvaluator2;
        assertEquals("id", attributeEval2.attributeName());
        assertEquals("id1", attributeEval2.getAttributeValue("id"));
    }

    @Test
    public void testParsePseudoClassSelector() {
        String query = "div:empty";
        Evaluator evaluator = QueryParser.parse(query);
        Evaluator isEmptyEvaluator = (Evaluator.IsEmpty) evaluator;
        assertEquals("div", isEmptyEvaluator.tagName());
    }

    @Test
    public void testParsePseudoClassSelectorWithClass() {
        String query = "div:empty.class1";
        Evaluator evaluator = QueryParser.parse(query);
        StructuralEvaluator.Tag tagEvaluator = (StructuralEvaluator.Tag) evaluator;
        assertEquals("div", tagEvaluator.tagName());
        assertEquals("class1", tagEvaluator.className());

        Evaluator isEmptyEvaluator = (Evaluator.IsEmpty) tagEvaluator.getPrevious();
        assertEquals("div", isEmptyEvaluator.tagName());
    }

    @Test
    public void testParseDescendantSelector() { {
        String query = "div > ul > li";
        Evaluator evaluator = QueryParser.parse(query);
        CombiningEvaluator andEvaluator = (CombiningEvaluator.And) evaluator;
        StructuralEvaluator.Tag tagEvaluator = (StructuralEvaluator.Tag) andEvaluator.getEvaluator(0);
        assertEquals("div", tagEvaluator.tagName());

        StructuralEvaluator.ImmediateChild immediateChildEvaluator = (StructuralEvaluator.ImmediateChild) andEvaluator.getEvaluator(1);
        StructuralEvaluator.Tag ulTagEvaluator = (StructuralEvaluator.Tag) immediateChildEvaluator.getEvaluator(0);
        assertEquals("ul", ulTagEvaluator.tagName());

        StructuralEvaluator.Tag liTagEvaluator = (StructuralEvaluator.Tag) andEvaluator.getEvaluator(2);
        assertEquals("li", liTagEvaluator.tagName());
    }
    }

    @Test
    public void testParseComplexDescendantSelector() {
        String query = "div > ul > li:nth-child(2) > a:contains('link')";
        Evaluator evaluator = QueryParser.parse(query);
        CombiningEvaluator andEvaluator = (CombiningEvaluator.And) evaluator;
        StructuralEvaluator.Tag tagEvaluator = andEvaluator.getEvaluator(0);
        assertEquals("div", tagEvaluator.tagName());

        StructuralEvaluator.ImmediateChild immediateChildEvaluator = (StructuralEvaluator.ImmediateChild) andEvaluator.getEvaluator(1);
        StructuralEvaluator.Tag ulTagEvaluator = (StructuralEvaluator.Tag) immediateChildEvaluator.getEvaluator(0);
        assertEquals("ul", ulTagEvaluator.tagName());

        StructuralEvaluator.NthChild nthChildEvaluator = (StructuralEvaluator.NthChild) andEvaluator.getEvaluator(2);
        assertEquals(2, nthChildEvaluator.index());
        assertEquals("li", nthChildEvaluator.tagName());

        StructuralEvaluator.Tag liTagEvaluator = (StructuralEvaluator.Tag) andEvaluator.getEvaluator(3);
        StructuralEvaluator.ImmediateChild linkChildEvaluator = (StructuralEvaluator.ImmediateChild) liTagEvaluator.getEvaluator(0);
        StructuralEvaluator.Tag aTagEvaluator = (StructuralEvaluator.Tag) linkChildEvaluator.getEvaluator(0);
        assertEquals("a", aTagEvaluator.tagName());

        Evaluator.ContainsText containsTextEvaluator = (Evaluator.ContainsText) aTagEvaluator.getNext();
        assertEquals("link", containsTextEvaluator.text());
    }

    @Test
    public void testParseAttributeWithValueContainingSelector() {
        String query = "div[class*='class']";
        Evaluator evaluator = QueryParser.parse(query);
        Evaluator attributeEvaluator = evaluator;

        while (attributeEvaluator instanceof Evaluator.Attribute && !attributeEvaluator.getAttributeName().equals("class")) {
            attributeEvaluator = attributeEvaluator.getNext();
        }

        Evaluator.Attribute attribute = (Evaluator.Attribute) attributeEvaluator;
        assertEquals("class", attribute.attributeName());
        assertTrue(attribute.getAttributeValue("class").matches(".*class.*"));
    }

    @Test
    public void testParseAttributeWithValueStartingSelector() {
        String query = "div[class^='class']";
        Evaluator evaluator = QueryParser.parse(query);
        Evaluator attributeEvaluator = evaluator;

        while (attributeEvaluator instanceof Evaluator.Attribute && !attributeEvaluator.getAttributeName().equals("class")) {
            attributeEvaluator = attributeEvaluator.getNext();
        }

        Evaluator.Attribute attribute = (Evaluator.Attribute) attributeEvaluator;
        assertEquals("class", attribute.attributeName());
        assertTrue(attribute.getAttributeValue("class").startsWith("class"));
    }

    @Test
    public void testParseAttributeWithValueEndingSelector() {
        String query = "div[class$='class']";
        Evaluator evaluator = QueryParser.parse(query);
        Evaluator attributeEvaluator = evaluator;

        while (attributeEvaluator instanceof Evaluator.Attribute && !attributeEvaluator.getAttributeName().equals("class")) {
            attributeEvaluator = attributeEvaluator.getNext();
        }

        Evaluator.Attribute attribute = (Evaluator.Attribute) attributeEvaluator;
        assertEquals("class", attribute.attributeName());
        assertTrue(attribute.getAttributeValue("class").endsWith("class"));
    }

    @Test
    public void testParseAttributeWithValueMatchingSelector() {
        String query = "div[class~='class']";
        Evaluator evaluator = QueryParser.parse(query);
        Evaluator attributeEvaluator = evaluator;

        while (attributeEvaluator instanceof Evaluator.Attribute && !attributeEvaluator.getAttributeName().equals("class")) {
            attributeEvaluator = attributeEvaluator.getNext();
        }

        Evaluator.Attribute attribute = (Evaluator.Attribute) attributeEvaluator;
        assertEquals("class", attribute.attributeName());
        Pattern pattern = attribute.getPattern();
        assertTrue(pattern.matcher("class1 class class2 class class").find());
    }

    @Test
    public void testParseAttributeWithValueNotSelector() {
        String query = "div:not([class='class'])";
        Evaluator evaluator = QueryParser.parse(query);
        StructuralEvaluator.Not notEvaluator = (StructuralEvaluator.Not) evaluator;
        Evaluator attributeEvaluator = notEvaluator.getEvaluator(0);

        while (attributeEvaluator instanceof Evaluator.Attribute && !(attributeEvaluator.getAttributeName().equals("class") && attributeEvaluator.getAttributeValue("class").equals("class"))) {
            attributeEvaluator = attributeEvaluator.getNext();
        }

        assertNull(attributeEvaluator);
    }

    @Test
    public void testParsePseudoElementSelector() {
        String query = "div:first-child";
        Evaluator evaluator = QueryParser.parse(query);
        Evaluator isFirstChildEvaluator = (Evaluator.IsFirstChild) evaluator;
        assertEquals("div", isFirstChildEvaluator.tagName());
    }

    @Test
    public void testParsePseudoElementSelectorWithTag() {
        String query = "ul:last-of-type > li:first-child";
        Evaluator evaluator = QueryParser.parse(query);
        CombiningEvaluator andEvaluator = (CombiningEvaluator.And) evaluator;
        StructuralEvaluator.Tag tagEvaluator = andEvaluator.getEvaluator(0);
        assertEquals("ul", tagEvaluator.tagName());
        assertTrue(tagEvaluator.isLastOfType());

        StructuralEvaluator.Tag lastChildTagEvaluator = andEvaluator.getEvaluator(1);
        assertEquals("li", lastChildTagEvaluator.tagName());
        Evaluator isFirstChildEvaluator = lastChildTagEvaluator.getPrevious();
        assertEquals("li", isFirstChildEvaluator.tagName());
        assertTrue(isFirstChildEvaluator.isFirstChild());
    }
}