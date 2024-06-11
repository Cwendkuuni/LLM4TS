package org.jsoup.safety;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.jmock.lib.legacy.ClassImposterizer;
import org.jmock.lib.legacy.MockObject;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * Class to test the Cleaner class.
 */
public class CleanerTest {

 private Cleaner cleaner;
 private MockObject whitelistMock;

 @Rule
 public ClassImposterizer classImposterizer = new ClassImposterizer();

 @Before
 public void setUp() {
 whitelistMock = classImposterizer.mock(Whitelist.class);
 cleaner = new Cleaner(whitelistMock.proxy());
 }

 /**
 * Test constructor.
 */
 @Test
 public void testConstructor() {
 // no validation here as the constructor validation is done in the class itself
 }

 /**
 * Test clean method with valid input.
 */
 @Test
 public void testCleanValidInput() {
 Document dirtyDocument = createMockDocument("validInput.html");
 Document cleanDocument = cleaner.clean(dirtyDocument);

 // check if clean document is not null
 assertNotNull(cleanDocument);

 // check if clean document has the same baseuri as dirty document
 assertEquals(dirtyDocument.baseUri(), cleanDocument.baseUri());
 }

 /**
 * Test clean method with invalid input.
 */
 @Test(expected = IllegalArgumentException.class)
 public void testCleanInvalidInput() {
 Document dirtyDocument = null; // null input
 cleaner.clean(dirtyDocument);
 }

 /**
 * Test isValid method with valid input.
 */
 @Test
 public void testIsValidValidInput() {
 Document dirtyDocument = createMockDocument("validInput.html");
 assertTrue(cleaner.isValid(dirtyDocument));
 }

 /**
 * Test isValid method with invalid input.
 */
 @Test
 public void testIsValidInvalidInput() {
 Document dirtyDocument = createMockDocument("invalidInput.html");
 assertFalse(cleaner.isValid(dirtyDocument));
 }

 /**
 * Test copySafeNodes method with valid input.
 */
 @Test
 public void testCopySafeNodesValidInput() {
 Element sourceEl = createMockElement("validElement.html");
 Element destEl = new Element(Tag.valueOf("div"), sourceEl.baseUri());
 int numDiscarded = cleaner.copySafeNodes(sourceEl, destEl);

 // check if number of discarded elements is zero
 assertEquals(0, numDiscarded);
 }

 /**
 * Test copySafeNodes method with invalid input.
 */
 @Test
 public void testCopySafeNodesInvalidInput() {
 Element sourceEl = createMockElement("invalidElement.html");
 Element destEl = new Element(Tag.valueOf("div"), sourceEl.baseUri());
 int numDiscarded = cleaner.copySafeNodes(sourceEl, destEl);

 // check if number of discarded elements is greater than zero
 assertFalse(numDiscarded == 0);
 }

 /**
 * Helper method to create a mock Document object.
 *
 * @param fileName the name of the file containing the HTML to be used as input
 * @return a mock Document object
 */
 private Document createMockDocument(String fileName) {
 Document mockDocument = classImposterizer.mock(Document.class);
 classImposterizer.mockMethod(mockDocument, "baseUri", () -> "http://localhost:8080/test");

 Element bodyEl = new Element(Tag.valueOf("body"), null);
 mockDocument.setBody(bodyEl);

 // add some mock elements and text to the body
 Element mockElement1 = createMockElement(fileName + "_element1.html");
 Element mockElement2 = createMockElement(fileName + "_element2.html");
 TextNode mockTextNode = new TextNode("Mock text", null);

 bodyEl.appendChild(mockElement1);
 bodyEl.appendChild(mockTextNode);
 bodyEl.appendChild(mockElement2);

 return mockDocument;
 }

 /**
 * Helper method to create a mock Element object.
 *
 * @param fileName the name of the file containing the HTML to be used as input for the element
 * @return a mock Element object
 */
 private Element createMockElement(String fileName) {
 Element mockElement = classImposterizer.mock(Element.class);
 classImposterizer.mockStatic(Tag.class, "valueOf", (String tagName) -> new Tag(tagName));
 classImposterizer.mockMethod(mockElement, "tagName", () -> Tag.valueOf(fileName.substring(0, fileName.lastIndexOf("."))));

 Attributes mockAttributes = classImposterizer.mock(Attributes.class);
 mockElement.setAttributes(mockAttributes);

 return mockElement;
 }

 /**
 * Helper method to assert that an object is not null.
 *
 * @param object the object to be checked for nullity
 */
 private void assertNotNull(Object object) {
 if (object == null) {
 throw new AssertionError("Expected object to not be null");
 }
 }

 /**
 * Helper method to check if two objects are equal.
 *
 * @param expected the expected object
 * @param actual the actual object
 */
 private void assertEquals(Object expected, Object actual) {
 if (!expected.equals(actual)) {
 throw new AssertionError("Expected: <" + expected + ">. Actual: <" + actual + ">");
 }
 }
}