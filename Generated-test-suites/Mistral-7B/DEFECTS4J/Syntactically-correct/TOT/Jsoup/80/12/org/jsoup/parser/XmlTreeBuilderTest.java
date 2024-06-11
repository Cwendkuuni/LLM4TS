import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 }

 @Test
 public void testParse() {
 String xmlInput = "<root><element attr=\"value\">Content</element></root>";
 Document xmlDoc = xmlTreeBuilder.parse(new StringReader(xmlInput), "");
 Element root = xmlDoc.rootElement();
 assertEquals("root", root.tagName());
 Element element = root.child(0);
 assertEquals("element", element.tagName());
 assertEquals("value", element.attr("attr"));
 assertEquals("Content", element.text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithNullInput() {
 xmlTreeBuilder = new XmlTreeBuilder();
 try {
 xmlTreeBuilder.parse(null, "");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithEmptyInput() {
 xmlTreeBuilder = new XmlTreeBuilder();
 String emptyInput = "";
 try {
 xmlTreeBuilder.parse(new StringReader(emptyInput), "");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithInvalidInput() {
 xmlTreeBuilder = new XmlTreeBuilder();
 String invalidInput = "<root><element attr=\"value>Content</element>";
 try {
 xmlTreeBuilder.parse(new StringReader(invalidInput), "");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testParseFragment() {
 String xmlFragment = "<element attr=\"value\">Content</element>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, "", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 Element element = (Element) nodes.get(0);
 assertEquals("element", element.tagName());
 assertEquals("value", element.attr("attr"));
 assertEquals("Content", element.text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseFragmentWithNullInput() {
 xmlTreeBuilder = new XmlTreeBuilder();
 String emptyInput = null;
 try {
 xmlTreeBuilder.parseFragment(emptyInput, "", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseFragmentWithEmptyInput() {
 xmlTreeBuilder = new XmlTreeBuilder();
 String emptyInput = "";
 try {
 xmlTreeBuilder.parseFragment(emptyInput, "", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseFragmentWithInvalidInput() {
 xmlTreeBuilder = new XmlTreeBuilder();
 String invalidInput = "<root><element attr=\"value>Content</element>";
 try {
 xmlTreeBuilder.parseFragment(new StringReader(invalidInput), "", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }
}