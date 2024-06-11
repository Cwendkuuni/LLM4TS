package org.jsoup.nodes.test;

import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.DocumentType;
import static org.junit.Assert.*;

public class DocumentTypeTest {
  @Test
  public void testDocumentTypeCreation() {
    DocumentType docType = new DocumentType("html", "publicID", "systemID", "baseUri");
    assertEquals("#doctype", docType.nodeName());
    assertEquals("html", docType.attr("name"));
    assertEquals("publicID", docType.attr("publicId"));
    assertEquals("systemID", docType.attr("systemId"));
    assertEquals("baseUri", docType.baseUri().toString());
  }

  @Test
  public void testDocumentTypeOuterHtmlHead() {
    DocumentType docType = new DocumentType("html", "publicID", "systemID", "baseUri");
    StringBuilder accum = new StringBuilder();
    docType.outerHtmlHead(accum, 0, null);

    String expected = "<!DOCTYPE html PUBLIC \"publicID\" systemID>\n";
    assertEquals(expected, accum.toString());
  }

  @Test
  public void testDocumentTypeNullPublicId() {
    assertThrows(NullPointerException.class, () -> new DocumentType("html", null, "systemID", "baseUri"));
  }

  @Test
  public void testDocumentTypeNullSystemId() {
    assertThrows(NullPointerException.class, () -> new DocumentType("html", "publicID", null, "baseUri"));
  }

  @Test
  public void testDocumentTypeEmptyStringPublicId() {
    DocumentType docType = new DocumentType("html", "", "systemID", "baseUri");
    StringBuilder accum = new StringBuilder();
    docType.outerHtmlHead(accum, 0, null);

    String expected = "<!DOCTYPE html>\n";
    assertEquals(expected, accum.toString());
  }

  @Test
  public void testDocumentTypeEmptyStringSystemId() {
    DocumentType docType = new DocumentType("html", "publicID", "", "baseUri");
    StringBuilder accum = new StringBuilder();
    docType.outerHtmlHead(accum, 0, null);

    String expected = "<!DOCTYPE html PUBLIC \"publicID\" >\n";
    assertEquals(expected, accum.toString());
  }
}