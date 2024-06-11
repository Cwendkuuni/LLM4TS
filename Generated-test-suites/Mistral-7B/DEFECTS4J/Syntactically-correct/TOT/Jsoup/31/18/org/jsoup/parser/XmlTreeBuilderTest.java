import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.*;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokenizer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Tokenizer tokenizer;

 @Before
 public void setup() {
 xmlTreeBuilder = new XmlTreeBuilder();
 tokenizer = new Tokenizer();
 }

 @Test
 public void testStartTag() {
 String xml = "<root><child tag=\"test\">test</child></root>";
 Token[] tokens = tokenizer.tokenize(xml);
 xmlTreeBuilder.initialiseParse(xml, null, null);
 List<Token> input = new ArrayList<>();
 for (Token token : tokens) {
 input.add(token);
 xmlTreeBuilder.process(token);
 }
 Element root = (Element) xmlTreeBuilder.stack.peek();
 Element child = root.child(0);
 assertEquals("root", root.tag().name());
 assertEquals("child", child.tag().name());
 assertEquals("test", child.tag().attributes().get("tag"));
 }

 @Test
 public void testEndTag() {
 String xml = "<root><child/></root>";
 Token[] tokens = tokenizer.tokenize(xml);
 xmlTreeBuilder.initialiseParse(xml, null, null);
 List<Token> input = new ArrayList<>();
 for (Token token : tokens) {
 input.add(token);
 xmlTreeBuilder.process(token);
 }
 Element root = (Element) xmlTreeBuilder.stack.peek();
 assertEquals(1, root.childSize());
 xmlTreeBuilder.popStackToClose(tokenizer.nextToken().asEndTag());
 assertEquals(0, root.childSize());
 }

 @Test
 public void testComment() {
 String xml = "<!-- comment -->";
 Token[] tokens = tokenizer.tokenize(xml);
 xmlTreeBuilder.initialiseParse(xml, null, null);
 List<Token> input = new ArrayList<>();
 for (Token token : tokens) {
 input.add(token);
 xmlTreeBuilder.process(token);
 }
 assertEquals(1, xmlTreeBuilder.stack.size());
 Element comment = (Comment) xmlTreeBuilder.stack.peek();
 assertEquals("comment", comment.data());
 }

 @Test
 public void testCharacter() {
 String xml = "<root>test</root>";
 Token[] tokens = tokenizer.tokenize(xml);
 xmlTreeBuilder.initialiseParse(xml, null, null);
 List<Token> input = new ArrayList<>();
 for (Token token : tokens) {
 input.add(token);
 xmlTreeBuilder.process(token);
 }
 Element root = (Element) xmlTreeBuilder.stack.peek();
 Text text = (Text) root.child(0);
 assertEquals("test", text.data());
 }

 @Test
 public void testDoctype() {
 String xml = "<!DOCTYPE html SYSTEM \"about:legacy-compat\">";
 Token[] tokens = tokenizer.tokenize(xml);
 xmlTreeBuilder.initialiseParse(xml, null, null);
 List<Token> input = new ArrayList<>();
 for (Token token : tokens) {
 input.add(token);
 xmlTreeBuilder.process(token);
 }
 DocumentType doctype = (DocumentType) xmlTreeBuilder.stack.peek();
 assertEquals("html", doctype.name());
 assertEquals("about:legacy-compat", doctype.publicIdentifier());
 }

 @Test(expected = AssertionError.class)
 public void testUnexpectedTokenType() {
 xmlTreeBuilder.initialiseParse("", null, null);
 Token token = tokenizer.nextToken();
 xmlTreeBuilder.process(token); // this should throw an AssertionError
 }
}