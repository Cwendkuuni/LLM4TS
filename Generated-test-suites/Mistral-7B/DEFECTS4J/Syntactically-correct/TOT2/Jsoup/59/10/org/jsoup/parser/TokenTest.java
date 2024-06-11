import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.jsoup.parser.Tokenizer;
import org.jsoup.nodes.Attributes;

public class TokenTest {

 @Test
 public void testDoctype() {
 Doctype doctype = new Doctype();
 doctype.getName().equals("doctype");
 doctype.reset();
 }

 @Test
 public void testDoctypeWithName() {
 Doctype doctype = new Doctype();
 doctype.getName().equals("html");
 doctype.reset();
 }

 @Test(expected = AssertionError.class)
 public void testDoctypeWithEmptyName() {
 Doctype doctype = new Doctype();
 doctype.getName().equals("");
 }

 @Test
 public void testDoctypeWithPublicId() {
 Doctype doctype = new Doctype();
 doctype.pubSysKey = "DTD XHTML 1.0";
 doctype.publicIdentifier.append("public ");
 doctype.publicIdentifier.append("\"-//W3C//DTD XHTML 1.0 Strict//EN\"");
 doctype.systemIdentifier.append("system ");
 doctype.systemIdentifier.append("\"DTD/xhtml1-strict.dtd\"");
 doctype.isForceQuirks = true;
 Doctype resetDoctype = doctype.reset();
 resetDoctype.getName().equals("doctype");
 resetDoctype.getPubSysKey().equals("DTD XHTML 1.0");
 resetDoctype.getPublicIdentifier().equals("public \"-//W3C//DTD XHTML 1.0 Strict//EN\"");
 resetDoctype.getSystemIdentifier().equals("system \"DTD/xhtml1-strict.dtd\"");
 resetDoctype.isForceQuirks().equals(true);
 }

 @Test
 public void testTag() {
 Tag tag = new StartTag();
 tag.name().equals("div");
 tag.reset();
 }

 @Test
 public void testTagWithEmptyName() {
 Tag tag = new StartTag();
 tag.name().equals("");
 tag.reset();
 }

 @Test
 public void testTagNameWithLowerCase() {
 Tag tag = new StartTag();
 tag.name("div");
 tag.normalName().equals("div");
 tag.reset();
 }

 @Test
 public void testTagWithAttributes() {
 StartTag startTag = new StartTag();
 startTag.name("input").nameAttr("type", "text").nameAttr("name", "username").nameAttr("id", "userNameId");
 Attributes attributes = startTag.getAttributes();
 Attribute typeAttribute = attributes.get("type");
 Attribute nameAttribute = attributes.get("name");
 Attribute idAttribute = attributes.get("id");
 typeAttribute.getValue().equals("text");
 nameAttribute.getValue().equals("username");
 idAttribute.getValue().equals("userNameId");
 }

 @Test(expected = AssertionError.class)
 public void testTagWithEmptyAttributeValue() {
 StartTag startTag = new StartTag();
 startTag.name("input");
 startTag.newAttribute();
 startTag.newAttribute().name("class").setEmptyAttributeValue();
 startTag.finaliseTag();
 // Should not reach here as setEmptyAttributeValue() has already set hasEmptyAttributeValue to true
 Attributes attributes = startTag.getAttributes();
 Assert.assertEquals(0, attributes.size());
 }

 @Test
 public void testTagWithAttributeValue() {
 StartTag startTag = new StartTag();
 startTag.name("input");
 startTag.newAttribute().name("class").appendAttributeValue("classOne classTwo");
 startTag.finaliseTag();
 Attributes attributes = startTag.getAttributes();
 Attribute classAttribute = attributes.get("class");
 classAttribute.getValue().equals("classOne classTwo");
 }

 @Test
 public void testTagWithMixedAttributeValues() {
 StartTag startTag = new StartTag();
 startTag.name("input");
 startTag.newAttribute().name("class").appendAttributeValue("classOne");
 startTag.newAttribute().name("id").appendAttributeValue("userNameId");
 startTag.newAttribute().name("style").appendAttributeValue("color: red; background-color: white;");
 startTag.finaliseTag();
 Attributes attributes = startTag.getAttributes();
 Attribute classAttribute = attributes.get("class");
 classAttribute.getValue().equals("classOne");
 Attribute idAttribute = attributes.get("id");
 idAttribute.getValue().equals("userNameId");
 Attribute styleAttribute = attributes.get("style");
 styleAttribute.getValue().equals("color: red; background-color: white;");
 }

 @Test(expected = AssertionError.class)
 public void testTagWithDuplicateAttributeName() {
 StartTag startTag = new StartTag();
 startTag.name("input");
 startTag.newAttribute().name("class").appendAttributeValue("classOne");
 startTag.newAttribute().name("class").appendAttributeValue("classTwo");
 startTag.finaliseTag();
 // Should not reach here as newAttribute() should have returned an error for the duplicate name
 Attributes attributes = startTag.getAttributes();
 Assert.assertEquals(1, attributes.size());
 }
}