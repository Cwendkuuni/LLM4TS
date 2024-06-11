import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.TokenQueue;
import org.jsoup.helper.StringUtil;
import org.jsoup.helper.Validate;
import org.jsoup.internal.Normalizer;
import org.jsoup.select.QueryParser;
import org.jsoup.select.Evaluator;

public class QueryParserTest {

    @Test
    public void testParseEmptyQuery() {
        QueryParser parser = new QueryParser("");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.AllElements);
    }

    @Test
    public void testParseBasicSelectors() {
        String[] queries = new String[]{"#id", ".class", "tag"};
        for (String query : queries) {
            QueryParser parser = new QueryParser(query);
            Evaluator evaluator = parser.parse();
            if (query.startsWith("#")) {
                assertTrue(evaluator instanceof Evaluator.Id);
                assertEquals(query.substring(1), evaluator.getId());
            } else if (query.startsWith(".")) {
                assertTrue(evaluator instanceof Evaluator.Class);
                assertEquals(query.substring(1), evaluator.getClass().trim());
            } else {
                assertTrue(evaluator instanceof Evaluator.Tag);
                assertEquals(query.trim(), evaluator.getTagName());
            }
        }
    }

    @Test
    public void testParseAttributeSelectors() {
        String[] queries = new String[]{"[data-test]", "[class='test']", "[href='/']", "[title='Test']", "[title~='Test']", "[title^='Test']", "[title$='Test']"};
        for (String query : queries) {
            QueryParser parser = new QueryParser(query);
            Evaluator evaluator = parser.parse();
            assertTrue(evaluator instanceof Evaluator.Attribute);
            String attrName = evaluator.getAttributeName();
            String attrValue = evaluator.getAttributeValue();
            String expected = query.substring(1, query.length() - 1);
            if (expected.startsWith("[") && expected.endsWith("]")) {
                expected = expected.substring(1, expected.length() - 1);
                String[] parts = expected.split("=");
                assertEquals(parts[0], attrName);
                if (parts.length > 1) {
                    attrValue = parts[1].substring(1, parts[1].length() - 1);
                }
            }
            if (attrValue != null) {
                assertEquals(attrValue, evaluator.getAttributeValue());
            }
            if (attrName.equals("data-test")) {
                assertTrue(evaluator instanceof Evaluator.AttributePrefix);
            } else {
                assertTrue(evaluator instanceof Evaluator.AttributeWithValue);
                if (attrValue == null) {
                    assertTrue(evaluator.matches(new Evaluator.ElementMatcher() {
                        @Override
                        public boolean matches(Evaluator evaluator) {
                            return evaluator instanceof Evaluator.Tag;
                        }
                    }));
                } else {
                    assertTrue(evaluator.matches(new Evaluator.ElementMatcher() {
                        @Override
                        public boolean matches(Evaluator evaluator) {
                            if (evaluator instanceof Evaluator.Text) {
                                return evaluator.getText().equals(attrValue);
                            }
                            return evaluator instanceof Evaluator.Attribute && evaluator.getAttributeName().equals(attrName) && evaluator.getAttributeValue().equals(attrValue);
                        }
                    }));
                }
            }
        }
    }

    @Test
    public void testParseCombinedSelectors() {
        String[] queries = new String[]{"#id .class", "#id > tag", "#id ~ tag", "#id + tag", "#id, #id2"};
        for (String query : queries) {
            QueryParser parser = new QueryParser(query);
            Evaluator evaluator = parser.parse();
            List<Evaluator> evals = new ArrayList<>();
            while (evaluator instanceof CombiningEvaluator) {
                if (evaluator instanceof CombiningEvaluator.And) {
                    CombiningEvaluator.And and = (CombiningEvaluator.And) evaluator;
                    for (Evaluator eval : and.getChildren()) {
                        evals.add(eval);
                    }
                } else if (evaluator instanceof CombiningEvaluator.Or) {
                    CombiningEvaluator.Or or = (CombiningEvaluator.Or) evaluator;
                    for (Evaluator eval : or.getChildren()) {
                        evals.add(eval);
                    }
                }
                evaluator = and.getLeftMostEvaluator();
            }
            Evaluator expected = null;
            if (query.startsWith("#")) {
                expected = new Evaluator.Id(query.substring(1));
            } else {
                String[] parts = query.split(" ");
                if (parts.length > 1) {
                    String combinator = parts[0];
                    String selector = parts[1];
                    if (combinator.equals(">")) {
                        expected = new StructuralEvaluator.ImmediateParent(new Evaluator.Id(selector.substring(1)));
                    } else if (combinator.equals("+")) {
                        expected = new StructuralEvaluator.ImmediatePreviousSibling(new Evaluator.Id(selector.substring(1)));
                    } else if (combinator.equals("~")) {
                        expected = new StructuralEvaluator.PreviousSibling(new Evaluator.Id(selector.substring(1)));
                    } else if (combinator.equals(",")) {
                        expected = new CombiningEvaluator.Or(new Evaluator.Id(selector.substring(1)), new Evaluator.Id(queries[queries.length - 1].substring(1)));
                    }
                }
            }
            assertEquals(evals.size(), 1);
            assertEquals(evals.get(0), expected);
        }
    }

    @Test
    public void testParsePseudoSelectors() {
        String[] queries = new String[]{"div:first-child", "div:last-child", "div:first-of-type", "div:last-of-type", "div:only-child", "div:only-of-type", "div:empty", "div:root"};
        for (String query : queries) {
            QueryParser parser = new QueryParser(query);
            Evaluator evaluator = parser.parse();
            String tagName = evaluator.getTagName();
            if (tagName == null) {
                assertTrue(evaluator instanceof Evaluator.PseudoClass);
                switch (query) {
                    case "div:first-child":
                        assertTrue(evaluator instanceof Evaluator.IsFirstChild);
                        break;
                    case "div:last-child":
                        assertTrue(evaluator instanceof Evaluator.IsLastChild);
                        break;
                    case "div:first-of-type":
                        assertTrue(evaluator instanceof Evaluator.IsFirstOfType);
                        break;
                    case "div:last-of-type":
                        assertTrue(evaluator instanceof Evaluator.IsLastOfType);
                        break;
                    case "div:only-child":
                        assertTrue(evaluator instanceof Evaluator.IsOnlyChild);
                        break;
                    case "div:only-of-type":
                        assertTrue(evaluator instanceof Evaluator.IsOnlyOfType);
                        break;
                    case "div:empty":
                        assertTrue(evaluator instanceof Evaluator.IsEmpty);
                        break;
                    case "div:root":
                        assertTrue(evaluator instanceof Evaluator.IsRoot);
                        break;
                    default:
                        fail("Unexpected pseudo selector: " + query);
                }
            } else {
                assertEquals(tagName, "div");
                if (query.contains(":nth")) {
                    int a = 0, b = 0;
                    Pattern p = Pattern.compile("(([+-])?(\\d+)?)n(\\s*([+-])?\\s*\\d+)?");
                    Matcher m = p.matcher(query);
                    if (m.find()) {
                        a = Integer.parseInt(m.group(2).replaceFirst("^\\+", ""));
                        b = Integer.parseInt(m.group(5).replaceFirst("^\\+", ""));
                    }
                    assertEquals(evaluator, new Evaluator.IsNthChild(a, b));
                } else {
                    fail("Unexpected selector: " + query);
                }
            }
        }
    }

    @Test
    public void testParseInvalidQuery() {
        String[] queries = new String[]{"", "div", "div#", "div.class#id", "div#id.class", "div#id.class[attr=value]", "div#id[attr=value] .class", "div#id[attr=value] + tag", "div#id[attr=value] ~ tag", "div#id[attr=value] > tag", "div[attr=value] > #id", "div[attr=value] ~ #id", "div[attr=value] > #id ~ tag", "div[attr=value] ~ #id > tag"};
        for (String query : queries) {
            try {
                QueryParser parser = new QueryParser(query);
                parser.parse();
                fail("Expected IllegalArgumentException for query: " + query);
            } catch (IllegalArgumentException e) {
                assertEquals(e.getMessage(), "Failed to parse CSS query: " + query + ' ' + e.getCause().getMessage());
            }
        }
    }
}