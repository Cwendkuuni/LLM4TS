package org.jsoup.safety;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.junit.Test;
import static org.junit.Assert.*;

public class CleanerTest {

@Test
public void testCleanValidDocument() {
Whitelist whitelist = Whitelist.simpleText();
Cleaner cleaner = new Cleaner(whitelist);
Document dirtyDocument = Jsoup.parse("<html><body><p>This is a valid document</p></body></html>");
Document cleanDocument = cleaner.clean(dirtyDocument);
assertEquals("<html><body><p>This is a valid document</p></body></html>", cleanDocument.body().html());
}

@Test
public void testCleanInvalidDocument() {
Whitelist whitelist = Whitelist.simpleText();
Cleaner cleaner = new Cleaner(whitelist);
Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS');</script><p>This is an invalid document</p></body></html>");
Document cleanDocument = cleaner.clean(dirtyDocument);
assertEquals("<html><body><p>This is an invalid document</p></body></html>", cleanDocument.body().html());
}

@Test
public void testCleanMixedDocument() {
Whitelist whitelist = Whitelist.simpleText();
whitelist.addTags("p", "div");
Cleaner cleaner = new Cleaner(whitelist);
Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS');</script><p>This is a valid p tag</p><div>This is a valid div tag</div></body></html>");
Document cleanDocument = cleaner.clean(dirtyDocument);
assertEquals("<html><body><p>This is a valid p tag</p><div>This is a valid div tag</div></body></html>", cleanDocument.body().html());
}

@Test
public void testIsValidValidDocument() {
Whitelist whitelist = Whitelist.simpleText();
Cleaner cleaner = new Cleaner(whitelist);
Document dirtyDocument = Jsoup.parse("<html><body><p>This is a valid document</p></body></html>");
assertTrue(cleaner.isValid(dirtyDocument));
}

@Test
public void testIsInvalidValidDocument() {
Whitelist whitelist = Whitelist.simpleText();
Cleaner cleaner = new Cleaner(whitelist);
Document dirtyDocument = Jsoup.parse("<html><body><p>This is a valid document</p></body></html>");
assertFalse(cleaner.isValid(dirtyDocument));
}

@Test
public void testIsInvalidInvalidDocument() {
Whitelist whitelist = Whitelist.simpleText();
Cleaner cleaner = new Cleaner(whitelist);
Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS');</script><p>This is an invalid document</p></body></html>");
assertFalse(cleaner.isValid(dirtyDocument));
}

@Test
public void testIsInvalidMixedDocument() {
Whitelist whitelist = Whitelist.simpleText();
Cleaner cleaner = new Cleaner(whitelist);
Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS');</script><p>This is a valid p tag</p><div>This is a valid div tag</div></body></html>");
assertFalse(cleaner.isValid(dirtyDocument));
}
}