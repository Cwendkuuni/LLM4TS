import org.junit.Test;
import org.jsoup.parser.QueryParser;
import static org.junit.Assert.*;

public class QueryParserTest {

	@Test
	public void testParseSimpleSelector() {
		String query = "div.class1";
		Evaluator evaluator = QueryParser.parse(query);
		assertTrue(evaluator instanceof CombiningEvaluator.And);
		assertEquals(2, ((CombiningEvaluator.And) evaluator).size());
		assertEquals(Tag.DIV, ((Evaluator.Tag) ((CombiningEvaluator.And) evaluator).get(0)).getTagName());
		assertEquals("class1", ((Evaluator.Class) ((CombiningEvaluator.And) evaluator).get(1)).getClassValue());
	}

	@Test
	public void testParseComplexSelector() {
		String query = "div#id1 > ul > li:nth-child(2) > a[href^='http']";
		Evaluator evaluator = QueryParser.parse(query);
		assertTrue(evaluator instanceof CombiningEvaluator.And);
		assertEquals(7, ((CombiningEvaluator.And) evaluator).size());
		// ... check each evaluator in the tree
	}

	@Test(expected = Selector.SelectorParseException.class)
	public void testParseInvalidQuery() {
		String query = "div#id1 > ul > li:nth-child(2) > a[href^='http' bar]";
		QueryParser.parse(query);
	}

	@Test
	public void testParseAttributeSelector() {
		String query = "input[type='text'][name='username']";
		Evaluator evaluator = QueryParser.parse(query);
		assertTrue(evaluator instanceof CombiningEvaluator.And);
		assertEquals(3, ((CombiningEvaluator.And) evaluator).size());
		assertEquals(Tag.INPUT, ((Evaluator.Tag) ((CombiningEvaluator.And) evaluator).get(0)).getTagName());
		assertEquals("text", ((Evaluator.Attribute) ((CombiningEvaluator.And) evaluator).get(1)).getAttributeName());
		assertEquals("username", ((Evaluator.AttributeWithValue) ((CombiningEvaluator.And) evaluator).get(2)).getValue());
	}

	@Test
	public void testParseIdSelector() {
		String query = "#id1";
		Evaluator evaluator = QueryParser.parse(query);
		assertEquals(IdEvaluator.class, evaluator.getClass());
		assertEquals("id1", evaluator.getTextValue());
	}

	@Test
	public void testParseClassSelector() {
		String query = ".class1";
		Evaluator evaluator = QueryParser.parse(query);
		assertEquals(ClassEvaluator.class, evaluator.getClass());
		assertEquals("class1", evaluator.getTextValue());
	}

	@Test
	public void testParseTagSelector() {
		String query = "p";
		Evaluator evaluator = QueryParser.parse(query);
		assertEquals(TagEvaluator.class, evaluator.getClass());
		assertEquals(Tag.P, evaluator.getTagName());
	}

	@Test
	public void testParseCombinatorSelector() {
		String query = "div > p";
		Evaluator evaluator = QueryParser.parse(query);
		assertTrue(evaluator instanceof CombiningEvaluator.And);
		assertEquals(2, ((CombiningEvaluator.And) evaluator).size());
		assertEquals(Tag.DIV, ((Evaluator.Tag) ((CombiningEvaluator.And) evaluator).get(0)).getTagName());
		assertTrue(((CombiningEvaluator.And) evaluator).get(1) instanceof StructuralEvaluator.Parent);
	}

	@Test
	public void testParseDescendantSelector() {
		String query = "div p";
		Evaluator evaluator = QueryParser.parse(query);
		assertTrue(evaluator instanceof CombiningEvaluator.And);
		assertEquals(2, ((CombiningEvaluator.And) evaluator).size());
		assertEquals(Tag.DIV, ((Evaluator.Tag) ((CombiningEvaluator.And) evaluator).get(0)).getTagName());
		assertTrue(((CombiningEvaluator.And) evaluator).get(1) instanceof StructuralEvaluator.Descendant);
	}

	@Test
	public void testParseAttributeWithValueSelector() {
		String query = "input[type='text'][name='username']";
		Evaluator evaluator = QueryParser.parse(query);
		assertTrue(evaluator instanceof CombiningEvaluator.And);
		assertEquals(3, ((CombiningEvaluator.And) evaluator).size());
		assertEquals(Tag.INPUT, ((Evaluator.Tag) ((CombiningEvaluator.And) evaluator).get(0)).getTagName());
		assertEquals("text", ((Evaluator.Attribute) ((CombiningEvaluator.And) evaluator).get(1)).getAttributeName());
		assertEquals("username", ((Evaluator.AttributeWithValue) ((CombiningEvaluator.And) evaluator).get(2)).getValue());
	}

	@Test
	public void testParseAttributeStartingSelector() {
		String query = "input[name^='user']";
		Evaluator evaluator = QueryParser.parse(query);
		assertTrue(evaluator instanceof CombiningEvaluator.And);
		assertEquals(2, ((CombiningEvaluator.And) evaluator).size());
		assertEquals(Tag.INPUT, ((Evaluator.Tag) ((CombiningEvaluator.And) evaluator).get(0)).getTagName());
		assertEquals("user", ((Evaluator.AttributeStarting) ((CombiningEvaluator.And) evaluator).get(1)).getTextValue());
	}

	@Test
	public void testParseAttributeEndingSelector() {
		String query = "input[name$='text']";
		Evaluator evaluator = QueryParser.parse(query);
		assertTrue(evaluator instanceof CombiningEvaluator.And);
		assertEquals(2, ((CombiningEvaluator.And) evaluator).size());
		assertEquals(Tag.INPUT, ((Evaluator.Tag) ((CombiningEvaluator.And) evaluator).get(0)).getTagName());
		assertEquals("text", ((Evaluator.AttributeEnding) ((CombiningEvaluator.And) evaluator).get(1)).getTextValue());
	}

	@Test
	public void testParseAttributeContainsSelector() {
		String query = "input[name*='user']";
		Evaluator evaluator = QueryParser.parse(query);
		assertTrue(evaluator instanceof CombiningEvaluator.And);
		assertEquals(2, ((CombiningEvaluator.And) evaluator).size());
		assertEquals(Tag.INPUT, ((Evaluator.Tag) ((CombiningEvaluator.And) evaluator).get(0)).getTagName());
		assertEquals("user", ((Evaluator.AttributeContaining) ((CombiningEvaluator.And) evaluator).get(1)).getTextValue());
	}

	@Test
	public void testParseAttributeContainsMatchingSelector() {
		String query = "input[name~='user']";
		Evaluator evaluator = QueryParser.parse(query);
		assertTrue(evaluator instanceof CombiningEvaluator.And);
		assertEquals(2, ((CombiningEvaluator.And) evaluator).size());
		assertEquals(Tag.INPUT, ((Evaluator.Tag) ((CombiningEvaluator.And) evaluator).get(0)).getTagName());
		assertTrue(((Evaluator.AttributeWithValueMatching) ((CombiningEvaluator.And) evaluator).get(1)).isMatching());
		assertTrue(((Evaluator.AttributeWithValueMatching) ((CombiningEvaluator.And) evaluator).get(1)).getPattern().matcher("user123").matches());
		assertTrue(((Evaluator.AttributeWithValueMatching) ((CombiningEvaluator.And) evaluator).get(1)).getPattern().matcher("user_input").matches());
		assertFalse(((Evaluator.AttributeWithValueMatching) ((CombiningEvaluator.And) evaluator).get(1)).getPattern().matcher("password").matches());
	}

	@Test
	public void testParseAttributeNotSelector() {
		String query = "input:not([type='text'])";
		Evaluator evaluator = QueryParser.parse(query);
		assertTrue(evaluator instanceof CombiningEvaluator.Not);
		assertTrue(evaluator instanceof StructuralEvaluator.Tag);
		assertTrue(((Evaluator.Tag) evaluator).getTagName() == Tag.INPUT);
		assertFalse(((StructuralEvaluator.Not) evaluator).getChild().isMatchingTag(Tag.INPUT, null, false));
	}

	@Test
	public void testParseNthChildSelector() {
		String query = "li:nth-child(3)";
		Evaluator evaluator = QueryParser.parse(query);
		assertTrue(evaluator instanceof IsNthChild);
		assertEquals(3, ((IsNthChild) evaluator).getIndex());
		assertEquals(Tag.LI, ((IsNthChild) evaluator).getParentTag());
	}

	@Test
	public void testParseNthOfTypeSelector() {
		String query = "li:nth-of-type(3)";
		Evaluator evaluator = QueryParser.parse(query);
		assertTrue(evaluator instanceof IsNthOfType);
		assertEquals(3, ((IsNthOfType) evaluator).getIndex());
		assertEquals(Tag.LI, ((IsNthOfType) evaluator).getTagName());
	}

	@Test
	public void testParseNthLastChildSelector() {
		String query = "li:nth-last-child(3)";
		Evaluator evaluator = QueryParser.parse(query);
		assertTrue(evaluator instanceof IsNthLastChild);
		assertEquals(3, ((IsNthLastChild) evaluator).getIndex());
		assertEquals(Tag.LI, ((IsNthLastChild) evaluator).getTagName());
	}

	@Test
	public void testParseNthLastOfTypeSelector() {
		String query = "li:nth-last-of-type(3)";
		Evaluator evaluator = QueryParser.parse(query);
		assertTrue(evaluator instanceof IsNthLastOfType);
		assertEquals(3, ((IsNthLastOfType) evaluator).getIndex());
		assertEquals(Tag.LI, ((IsNthLastOfType) evaluator).getTagName());
	}

	@Test
	public void testParseFirstChildSelector() {
		String query = "li:first-child";
		Evaluator evaluator = QueryParser.parse(query);
		assertTrue(evaluator instanceof IsFirstChild);
		assertEquals(Tag.LI, ((IsFirstChild) evaluator).getParentTag());
	}

	@Test
	public void testParseLastChildSelector() {
		String query = "li:last-child";
		Evaluator evaluator = QueryParser.parse(query);
		assertTrue(evaluator instanceof IsLastChild);
		assertEquals(Tag.LI, ((IsLastChild) evaluator).getParentTag());
	}

	@Test
	public void testParseFirstOfTypeSelector() {
		String query = "li:first-of-type";
		Evaluator evaluator = QueryParser.parse(query);
		assertTrue(evaluator instanceof IsFirstOfType);
		assertEquals(Tag.LI, ((IsFirstOfType) evaluator).getTagName());
	}

	@Test
	public void testParseLastOfTypeSelector() {
		String query = "li:last-of-type";
		Evaluator evaluator = QueryParser.parse(query);
		assertTrue(evaluator instanceof IsLastOfType);
		assertEquals(Tag.LI, ((IsLastOfType) evaluator).getTagName());
	}

	@Test
	public void testParseOnlyChildSelector() {
		String query = "li:only-child";
		Evaluator evaluator = QueryParser.parse(query);
		assertTrue(evaluator instanceof IsOnlyChild);
		assertEquals(Tag.LI, ((IsOnlyChild) evaluator).getParentTag());
	}

	@Test
	public void testParseOnlyOfTypeSelector() {
		String query = "li:only-of-type";
		Evaluator evaluator = QueryParser.parse(query);
		assertTrue(evaluator instanceof IsOnlyOfType);
		assertEquals(Tag.LI, ((IsOnlyOfType) evaluator).getTagName());
	}

	@Test
	public void testParseEmptySelector() {
		String query = ":empty";
		Evaluator evaluator = QueryParser.parse(query);
		assertTrue(evaluator instanceof IsEmpty);
	}

	@Test
	public void testParseRootSelector() {
		String query = ":root";
		Evaluator evaluator = QueryParser.parse(query);
		assertTrue(evaluator instanceof IsRoot);
	}
}