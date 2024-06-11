import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TokenTest {

 @Test
 public void testDoctypeToken() {
 Doctype doctype = new Doctype();
 doctype.getName().equals("doctype");
 assertEquals("", doctype.getPublicIdentifier());
 assertEquals("", doctype.getSystemIdentifier());
 assertFalse(doctype.isForceQuirks());
 }

 @Test
 public void testDoctypeTokenWithName() {
 Doctype doctype = new Doctype().name("html");
 doctype.getName().equals("doctype");
 doctype.getName().equals("html");
 assertEquals("", doctype.getPublicIdentifier());
 assertEquals("", doctype.getSystemIdentifier());
 assertFalse(doctype.isForceQuirks());
 }

 @Test
 public void testDoctypeTokenWithNameAndPublicIdentifier() {
 Doctype doctype = new Doctype().name("html").publicIdentifier("_xml");
 doctype.getName().equals("doctype");
 doctype.getName().equals("html");
 doctype.getPublicIdentifier().equals("_xml");
 assertEquals("", doctype.getSystemIdentifier());
 assertFalse(doctype.isForceQuirks());
 }

 @Test
 public void testDoctypeTokenWithNamePublicIdentifierAndSystemIdentifier() {
 Doctype doctype = new Doctype().name("html").publicIdentifier("_xml").systemIdentifier("/myapp.html");
 doctype.getName().equals("doctype");
 doctype.getName().equals("html");
 doctype.getPublicIdentifier().equals("_xml");
 doctype.getSystemIdentifier().equals("/myapp.html");
 assertFalse(doctype.isForceQuirks());
 }

 @Test
 public void testDoctypeTokenWithForceQuirks() {
 Doctype doctype = new Doctype().name("html").forceQuirks(true);
 doctype.getName().equals("doctype");
 doctype.getName().equals("html");
 assertEquals("", doctype.getPublicIdentifier());
 assertEquals("", doctype.getSystemIdentifier());
 assertTrue(doctype.isForceQuirks());
 }

 // Add test cases for the other Token subclasses here...

}