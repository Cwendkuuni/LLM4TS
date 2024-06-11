import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.TokenQueue;
import org.jsoup.helper.StringUtil;
import org.jsoup.helper.Validate;
import org.jsoup.select.QueryParser;
import org.jsoup.select.Evaluator;

public class QueryParserTest {

    @Test
    public void testParseSimpleSelector() {
        String query = "div.class1";
        Evaluator evaluator = QueryParser.parse(query);
        StructuralEvaluator.Tag tagEvaluator = (StructuralEvaluator.Tag) evaluator;
        assertEquals("div", tagEvaluator.getTagName());
        assertEquals("class1", tagEvaluator.getClassNames().get(0));
    }

    @Test
    public void testParseTagSelector() {
        String query = "ul";
        Evaluator evaluator = QueryParser.parse(query);
        StructuralEvaluator.Tag tagEvaluator = (StructuralEvaluator.Tag) evaluator;
        assertEquals("ul", tagEvaluator.getTagName());
    }

    @Test
    public void testParseIdSelector() {
        String query = "#elementId";
        Evaluator evaluator = QueryParser.parse(query);
        Evaluator.Id idEvaluator = (Evaluator.Id) evaluator;
        assertEquals("elementId", idEvaluator.getId());
    }

    @Test
    public void testParseClassSelector() {
        String query = ".class1.class2";
        Evaluator evaluator = QueryParser.parse(query);
        CombiningEvaluator andEvaluator = (CombiningEvaluator.And) evaluator;
        Evaluator.Class class1Evaluator = (Evaluator.Class) andEvaluator.getEvaluators().get(0);
        assertEquals("class1", class1Evaluator.getClassNames().get(0));
        Evaluator.Class class2Evaluator = (Evaluator.Class) andEvaluator.getEvaluators().get(1);
        assertEquals("class2", class2Evaluator.getClassNames().get(0));
    }

    @Test
    public void testParseCombinatorSelector() {
        String query = "div > p";
        Evaluator evaluator = QueryParser.parse(query);
        CombiningEvaluator.And andEvaluator = (CombiningEvaluator.And) evaluator;
        Evaluator immediateParentEvaluator = andEvaluator.getEvaluators().get(0);
        StructuralEvaluator.ImmediateParent parentEvaluator = (StructuralEvaluator.ImmediateParent) immediateParentEvaluator;
        assertEquals("div", parentEvaluator.getElementName());
        Evaluator childEvaluator = andEvaluator.getEvaluators().get(1);
        StructuralEvaluator.Tag tagEvaluator = (StructuralEvaluator.Tag) childEvaluator;
        assertEquals("p", tagEvaluator.getTagName());
    }

    @Test
    public void testParseAttributeSelector() {
        String query = "input[type='text']";
        Evaluator evaluator = QueryParser.parse(query);
        CombiningEvaluator.And andEvaluator = (CombiningEvaluator.And) evaluator;
        Evaluator attributeEvaluator = andEvaluator.getEvaluators().get(0);
        Evaluator.Attribute attributeEvaluator1 = (Evaluator.Attribute) attributeEvaluator;
        assertEquals("type", attributeEvaluator1.getAttributeName());
        assertEquals("text", attributeEvaluator1.getAttributeValue());
    }

    @Test
    public void testParsePseudoClassSelector() {
        String query = "a:link";
        Evaluator evaluator = QueryParser.parse(query);
        StructuralEvaluator.PseudoClass pseudoClassEvaluator = (StructuralEvaluator.PseudoClass) evaluator;
        assertEquals(":link", pseudoClassEvaluator.getPseudoClass());
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseInvalidSelector() {
        String query = "div#class1.class2 > ul#class3";
        QueryParser.parse(query);
    }
}