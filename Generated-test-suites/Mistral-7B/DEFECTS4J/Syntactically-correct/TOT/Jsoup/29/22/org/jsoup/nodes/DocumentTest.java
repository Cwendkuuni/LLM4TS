package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.nio.charset.Charset;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import org.junit.Test;

public class DocumentTest {

	@Test
	public void testDocumentCreation() {
		Document doc = new Document("http://example.com");
		assertNotNull(doc);
		assertEquals("#document", doc.nodeName());
	}

	@Test
	public void testCreateShell() {
		Document doc = Document.createShell("http://example.com");
		assertNotNull(doc);
		assertNotNull(doc.head());
		assertNotNull(doc.body());
	}

	@Test
	public void testEmptyTitle() {
		Document doc = new Document("http://example.com");
		assertNull(doc.title());
	}

	@Test
	public void testSetTitle() {
		Document doc = new Document("http://example.com");
		doc.title("Test Title");
		assertEquals("Test Title", doc.title());
	}

	@Test
	public void testCreateElement() {
		Document doc = new Document("http://example.com");
		Element element = doc.createElement("p");
		assertEquals("p", element.nodeName());
		assertEquals(doc.baseUri(), element.baseUri());
	}

	@Test
	public void testNormalise() {
		Document doc = Jsoup.parse("<html><head><title>Test</title></head><body><h1>Test</h1></body></html>");
		doc.normalise();
		assertSame(doc.head(), doc.getElementsByTag("head").first());
		assertSame(doc.body(), doc.getElementsByTag("body").first());
		assertNull(doc.getElementsByTag("html").first());
		assertNull(doc.getElementsByTag("title").last());
	}

	@Test
	public void testNormaliseTextNodes() {
		Document doc = Jsoup.parse("<html><head><title>Test</title></html><body>Test <h1>Test</h1></body>");
		doc.normalise();
		Elements textNodes = doc.body().getElementsByTag("text");
		assertEquals(2, textNodes.size());
		String text = textNodes.get(0).text();
		assertEquals(" Test ", text);
		text = textNodes.get(1).text();
		assertEquals("Test\n", text);
	}

	@Test
	public void testOutputSettings() {
		Document doc = Jsoup.parse("<html><head><title>Test</title></head><body><h1>Test</h1></body></html>");
		OutputSettings outputSettings = doc.outputSettings();
		assertNotNull(outputSettings);
		assertEquals(Charset.forName("UTF-8"), outputSettings.charset());
		assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
		assertFalse(outputSettings.prettyPrint());
	}

	@Test
	public void testClone() {
		Document doc = Jsoup.parse("<html><head><title>Test</title></head><body><h1>Test</h1></body></html>");
		Document clone = doc.clone();
		assertEquals(doc.text(), clone.text());
		assertNotSame(doc, clone);
	}

	@Test(expected = CloneNotSupportedException.class)
	public void testCloneThrowsException() {
		Document doc = Jsoup.parse("<html><head><title>Test</title></head><body><h1>Test</h1></body></html>");
		doc.clone(); // no need to assert anything here
	}
}