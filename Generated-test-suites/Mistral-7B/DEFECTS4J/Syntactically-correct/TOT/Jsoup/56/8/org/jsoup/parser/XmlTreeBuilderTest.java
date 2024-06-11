import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokenizer;

import java.util.List;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Document document;
 private Tokenizer tokenizer;

 @Before
 public void setup() {
 xmlTreeBuilder = new XmlTreeBuilder();
 document = Jsoup.parse("<root><tag attr=\"value\" /><anotherTag /></root>", "", ParseSettings.preserveCase());
 tokenizer = new Tokenizer(document.outerHtml());
 }

 @Test
 public void testParseStartTag() {
 Token startTag = tokenizer.nextToken();
 Element element = xmlTreeBuilder.insert(startTag.asStartTag());
 assertEquals("tag", element.tagName());
 assertEquals("value", element.attributes().get("attr"));
 }

 @Test
 public void testParseEndTag() {
 Token endTag = tokenizer.nextToken();
 xmlTreeBuilder.popStackToClose(endTag.asEndTag());
 assertTrue(xmlTreeBuilder.stack.isEmpty());
 }

 @Test
 public void testParseComment() {
 Token commentToken = tokenizer.nextToken();
 xmlTreeBuilder.insert(commentToken.asComment());
 assertEquals("", document.body().text());
 }

 @Test
 public void testParseCharacter() {
 Token characterToken = tokenizer.nextToken();
 xmlTreeBuilder.insert(characterToken.asCharacter());
 assertEquals("abc", document.body().text());
 }

 @Test
 public void testParseDoctype() {
 Token doctype = tokenizer.nextToken();
 xmlTreeBuilder.insert(doctype.asDoctype());
 assertEquals("root", document.documentElement().nodeName());
 }

 @Test
 public void testParseUnknownSelfClosingTag() {
 Token startTag = tokenizer.nextToken();
 Tag tag = Tag.valueOf(startTag.name(), xmlTreeBuilder.settings);
 Element el = xmlTreeBuilder.insert(startTag.asStartTag());
 assertEquals(tag, el.tag());
 assertFalse(el.hasChildren());
 }

 @Test
 public void testParseUnknownNonSelfClosingTag() {
 Token startTag = tokenizer.nextToken();
 Tag tag = Tag.valueOf(startTag.name(), xmlTreeBuilder.settings);
 Element el = xmlTreeBuilder.insert(startTag.asStartTag());
 assertEquals(tag, el.tag());
 assertTrue(el.hasChildren());
 }

 @Test
 public void testParseEmptyInput() {
 Document doc = xmlTreeBuilder.parse("", "");
 assertEquals(0, doc.childNodes().size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseNullInput() {
 xmlTreeBuilder.parse(null, "");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseNullBaseUri() {
 xmlTreeBuilder.parse("", null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidSettings() {
 ParseSettings invalidSettings = new ParseSettings();
 invalidSettings.setPreserveCase(null);
 xmlTreeBuilder.parse("", "", invalidSettings);
 }
}