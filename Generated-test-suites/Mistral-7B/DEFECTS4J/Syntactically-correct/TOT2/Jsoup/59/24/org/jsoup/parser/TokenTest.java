package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;

public class TokenTest {
  @Test
  public void testResetDoctype() {
    Token doctype = new Token.Doctype();
    doctype.reset();
    assertEquals(TokenType.Doctype, doctype.tokenType());
    assertNull(doctype.getName());
    assertNull(doctype.getPubSysKey());
    assertNull(doctype.getPublicIdentifier());
    assertNull(doctype.getSystemIdentifier());
    assertFalse(doctype.isForceQuirks());
  }

  @Test
  public void testGetNameDoctype() {
    Token doctype = new Token.Doctype();
    doctype.getName("html");
    assertEquals("html", doctype.getName());
    assertEquals("html", doctype.normalName());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEmptyDoctypeName() {
    Token doctype = new Token.Doctype();
    doctype.name("");
  }

  @Test
  public void testEmptyTagAttributes() {
    Token tag = new Token.StartTag();
    tag.newAttribute();
    assertNull(tag.getAttributes());
  }

  @Test(expected = NullPointerException.class)
  public void testNullTagAttributes() {
    Token tag = new Token.StartTag();
    tag.newAttribute(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEmptyCharacterdata() {
    Token character = new Token.Character();
    character.data("");
  }

  // ... Continue with other tests for each method and edge cases
}