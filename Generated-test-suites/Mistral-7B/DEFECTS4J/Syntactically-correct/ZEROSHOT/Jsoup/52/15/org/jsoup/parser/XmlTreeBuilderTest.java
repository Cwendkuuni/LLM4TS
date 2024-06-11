package org.jsoup.parser;

import org.jsoup.HelperConverters;
import org.jsoup.nodes.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(Parameterized.class)
public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private List<Node> result;

 @Parameters
 public static List<Object[]> data() {
 return HelperConverters.asList(new Object[][]{
 // test cases here, you can add more as needed
 {"<root><element attr='value'/><element/></root>", "<root><element attr=\"value\"/><element/></root>", "<root><element attr='value'/><element/></root>"},
 {"<root><element attr='value'/><element attr='value'/></root>", "<root><element attr=\"value\" /><element attr=\"value\" /></root>", "<root><element attr='value'/><element attr='value'/></root>"},
 {"<root><element attr='value'/><comment>This is a comment</comment></root>", "<root><element attr=\"value\" /><comment>This is a comment</comment></root>", "<root><element attr='value'/><comment>This is a comment</comment></root>"},
 {"<root><!DOCTYPE root SYSTEM \"http://example.com\"[ <!ATTLIST element attr CDATA #REQUIRED > ]>", "<root><!DOCTYPE root SYSTEM \"http://example.com\"[ <!ATTLIST element attr CDATA #REQUIRED > ]>", "<root><!DOCTYPE root SYSTEM \"http://example.com\"[ <!ATTLIST element attr CDATA #REQUIRED > ]>"},
 {"<root><element><![CDATA[This is CDATA]]></element></root>", "<root><element><![CDATA[This is CDATA]]></element></root>", "<root><element><![CDATA[This is CDATA]]></element></root>"},
 {"<root><element></element></root>", "<root><element /></root>", "<root><element></element></root>"},
 {"<root><element /><element /></root>", "<root><element /><element /></root>", "<root><element /><element /></root>"}
 });
 }

 @Before
 public void setup() {
 xmlTreeBuilder = new XmlTreeBuilder();
 }

 @Test
 public void testParseFragment() {
 String inputFragment = (String) this.getData()[0];
 String expectedOutput = (String) this.getData()[1];
 result = xmlTreeBuilder.parseFragment(inputFragment, null, null);
 compareNodes(expectedOutput, result);
 }

 private void compareNodes(String expectedOutput, List<Node> result) {
 String actualOutput = new XmlTreeBuilder().outputText(result.get(0));
 assertEquals(expectedOutput, actualOutput);
 }

}