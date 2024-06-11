package org.junit.jupiter.api;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.jsoup.select.QueryParser;
import org.jsoup.parser.TokenQueue;

import java.util.ArrayList;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class QueryParserTest {

 private QueryParser queryParser;
 private TokenQueue tokenQueue;

 @BeforeAll
 public void setup() {
 queryParser = new QueryParser("");
 tokenQueue = new TokenQueue("");
 }

 @Test
 public void testParseEmptyQuery() {
 Evaluator result = queryParser.parse();
 assertEquals(new CombiningEvaluator.And(new Evaluator.AllElements()), result);
 }

 @Test
 public void testParseSimpleTagSelector() {
 tokenQueue.append("div");
 queryParser = new QueryParser(tokenQueue.toString());
 Evaluator result = queryParser.parse();
 assertEquals(new Evaluator.Tag("div"), result);
 }

 @Test
 public void testParseClassSelector() {
 tokenQueue.append("class:test");
 queryParser = new QueryParser(tokenQueue.toString());
 Evaluator result = queryParser.parse();
 assertEquals(new Evaluator.Class("test"), result);
 }

 @Test
 public void testParseIdSelector() {
 tokenQueue.append("#myId");
 queryParser = new QueryParser(tokenQueue.toString());
 Evaluator result = queryParser.parse();
 assertEquals(new Evaluator.Id("myId"), result);
 }

 @Test
 public void testParseCombinedSelectors() {
 tokenQueue.append("div.red.class");
 queryParser = new QueryParser(tokenQueue.toString());
 Evaluator result = queryParser.parse();
 StructuralEvaluator and = new StructuralEvaluator.And();
 and.add(new Evaluator.Tag("div"));
 and.add(new Evaluator.Class("red"));
 and.add(new Evaluator.Class("class"));
 assertEquals(and, result);
 }

 @Test
 public void testParseDescendantSelector() {
 tokenQueue.append("div > p");
 queryParser = new QueryParser(tokenQueue.toString());
 Evaluator result = queryParser.parse();
 StructuralEvaluator and = new StructuralEvaluator.And();
 and.add(new Evaluator.Tag("div"));
 and.add(new StructuralEvaluator.ImmediateChild(new Evaluator.Tag("p")));
 assertEquals(and, result);
 }

 @Test
 public void testParseAttributeSelector() {
 tokenQueue.append("[href]");
 queryParser = new QueryParser(tokenQueue.toString());
 Evaluator result = queryParser.parse();
 assertEquals(new Evaluator.Attribute("href"), result);
 }

 @Test
 public void testParseAttributeEqualsSelector() {
 tokenQueue.append("[href='test']");
 queryParser = new QueryParser(tokenQueue.toString());
 Evaluator result = queryParser.parse();
 assertEquals(new Evaluator.AttributeWithValue("href", "test"), result);
 }

 @Test
 public void testParseAttributeNotEqualsSelector() {
 tokenQueue.append("[href!='test']");
 queryParser = new QueryParser(tokenQueue.toString());
 Evaluator result = queryParser.parse();
 assertEquals(new Evaluator.AttributeWithValueNot("href", "test"), result);
 }

 @Test
 public void testParseAttributeStartsWithSelector() {
 tokenQueue.append("[class^='prefix']");
 queryParser = new QueryParser(tokenQueue.toString());
 Evaluator result = queryParser.parse();
 assertEquals(new Evaluator.AttributeWithValueStarting("class", "prefix"), result);
 }

 @Test
 public void testParseAttributeEndsWithSelector() {
 tokenQueue.append("[class$='suffix']");
 queryParser = new QueryParser(tokenQueue.toString());
 Evaluator result = queryParser.parse();
 assertEquals(new Evaluator.AttributeWithValueEnding("class", "suffix"), result);
 }

 @Test
 public void testParseAttributeContainsSelector() {
 tokenQueue.append("[class*='substring']");
 queryParser = new QueryParser(tokenQueue.toString());
 Evaluator result = queryParser.parse();
 assertEquals(new Evaluator.AttributeWithValueContaining("class", "substring"), result);
 }

 @Test
 public void testParseAttributeRegexSelector() {
 tokenQueue.append("[class~='regex']");
 queryParser = new QueryParser(tokenQueue.toString());
 assertThrows(IllegalArgumentException.class, () -> queryParser.parse());
 }

 @Test
 public void testParsePseudoClasses() { //:first-child
 tokenQueue.append(":first-child");
 queryParser = new QueryParser(tokenQueue.toString());
 Evaluator result = queryParser.parse();
 assertEquals(new Evaluator.IsFirstChild(), result);
 }

 @Test
 public void testParsePseudoClassesLastChild() { //:last-child
 tokenQueue.append(":last-child");
 queryParser = new QueryParser(tokenQueue.toString());
 Evaluator result = queryParser.parse();
 assertEquals(new Evaluator.IsLastChild(), result);
 }

 @Test
 public void testParsePseudoClassesFirstOfType() { //:first-of-type
 tokenQueue.append(":first-of-type");
 queryParser = new QueryParser(tokenQueue.toString());
 Evaluator result = queryParser.parse();
 assertEquals(new Evaluator.IsFirstOfType(), result);
 }

 @Test
 public void testParsePseudoClassesLastOfType() { //:last-of-type
 tokenQueue.append(":last-of-type");
 queryParser = new QueryParser(tokenQueue.toString());
 Evaluator result = queryParser.parse();
 assertEquals(new Evaluator.IsLastOfType(), result);
 }

 @Test
 public void testParsePseudoClassesOnlyChild() { //:only-child
 tokenQueue.append(":only-child");
 queryParser = new QueryParser(tokenQueue.toString());
 Evaluator result = queryParser.parse();
 assertEquals(new Evaluator.IsOnlyChild(), result);
 }

 @Test
 public void testParsePseudoClassesOnlyOfType() { //:only-of-type
 tokenQueue.append(":only-of-type");
 queryParser = new QueryParser(tokenQueue.toString());
 Evaluator result = queryParser.parse();
 assertEquals(new Evaluator.IsOnlyOfType(), result);
 }

 @Test
 public void testParsePseudoClassesEmpty() { //:empty
 tokenQueue.append(":empty");
 queryParser = new QueryParser(tokenQueue.toString());
 Evaluator result = queryParser.parse();
 assertEquals(new Evaluator.IsEmpty(), result);
 }

 @Test
 public void testParsePseudoClassesRoot() { //:root
 tokenQueue.append(":root");
 queryParser = new QueryParser(tokenQueue.toString());
 Evaluator result = queryParser.parse();
 assertEquals(new Evaluator.IsRoot(), result);
 }

 @Test
 public void testParsePseudoClassesNthChild() { //:nth-child
 tokenQueue.append(":nth-child(1)");
 queryParser = new QueryParser(tokenQueue.toString());
 Evaluator result = queryParser.parse();
 assertEquals(new Evaluator.IsNthChild(1, 0), result);
 }

 @Test
 public void testParsePseudoClassesNthLastChild() { //:nth-last-child
 tokenQueue.append(":nth-last-child(1)");
 queryParser = new QueryParser(tokenQueue.toString());
 Evaluator result = queryParser.parse();
 assertEquals(new Evaluator.IsNthLastChild(1, 0), result);
 }

 @Test
 public void testParsePseudoClassesNthOfType() { //:nth-of-type
 tokenQueue.append(":nth-of-type(1)");
 queryParser = new QueryParser(tokenQueue.toString());
 Evaluator result = queryParser.parse();
 assertEquals(new Evaluator.IsNthOfType(1, 0), result);
 }

 @Test
 public void testParsePseudoClassesNthLastOfType() { //:nth-last-of-type
 tokenQueue.append(":nth-last-of-type(1)");
 queryParser = new QueryParser(tokenQueue.toString());
 Evaluator result = queryParser.parse();
 assertEquals(new Evaluator.IsNthLastOfType(1, 0), result);
 }

 @Test
 public void testParsePseudoClassesContains() { //:contains
 tokenQueue.append(":contains(text)");
 queryParser = new QueryParser(tokenQueue.toString());
 Evaluator result = queryParser.parse();
 assertEquals(new Evaluator.ContainsText("text"), result);
 }

 @Test
 public void testParsePseudoClassesContainsOwn() { //:contains-own
 tokenQueue.append(":containsOwn(text)");
 queryParser = new QueryParser(tokenQueue.toString());
 Evaluator result = queryParser.parse();
 assertEquals(new Evaluator.ContainsOwnText("text"), result);
 }

 @Test
 public void testParsePseudoClassesHas() { //:has
 tokenQueue.append(":has(el)");
 queryParser = new QueryParser(tokenQueue.toString());
 Evaluator result = queryParser.parse();
 assertEquals(new StructuralEvaluator.Has(new Evaluator.Tag("el")), result);
 }

 @Test
 public void testParsePseudoClassesMatches() { //:matches
 tokenQueue.append(":matches(regex)");
 queryParser = new QueryParser(tokenQueue.toString());
 assertThrows(IllegalArgumentException.class, () -> queryParser.parse());
 }

 @Test
 public void testParsePseudoClassesMatchesOwn() { //:matches-own
 tokenQueue.append(":matchesOwn(regex)");
 queryParser = new QueryParser(tokenQueue.toString());
 Evaluator result = queryParser.parse();
 assertEquals(new Evaluator.MatchesOwn(Pattern.compile((String) null)), result);
 }

 @Test
 public void testParsePseudoClassesNot() { //:not
 tokenQueue.append(":not(el)");
 queryParser = new QueryParser(tokenQueue.toString());
 Evaluator result = queryParser.parse();
 assertEquals(new StructuralEvaluator.Not(new Evaluator.Tag("el")), result);
 }

 @Test
 public void testParseMultipleSelectors() {
 tokenQueue.append("div.red.class p#id");
 queryParser = new QueryParser(tokenQueue.toString());
 StructuralEvaluator and = new StructuralEvaluator.And();
 and.add(new Evaluator.Tag("div"));
 and.add(new Evaluator.Class("red"));
 and.add(new Evaluator.Class("class"));
 and.add(new Evaluator.Tag("p"));
 and.add(new Evaluator.Id("id"));
 Evaluator result = queryParser.parse();
 assertEquals(and, result);
 }
}