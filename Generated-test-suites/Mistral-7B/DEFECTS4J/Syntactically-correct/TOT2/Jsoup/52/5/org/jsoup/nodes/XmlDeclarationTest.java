
package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class XmlDeclarationTest {

    @Test
    public void testXmlDeclarationWithName() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        assertEquals("#declaration", xmlDeclaration.nodeName());
    }

    @Test
    public void testXmlDeclarationWithProcessingInstructionTrue() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, true);
        String declaration = xmlDeclaration.getWholeDeclaration();
        assertTrue(declaration.startsWith("<?xml "));
    }

    @Test
    public void testXmlDeclarationWithNameNull() {
        try {
            XmlDeclaration xmlDeclaration = new XmlDeclaration(null, null, false);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testXmlDeclarationWithVersionAttribute() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        xmlDeclaration.attributes.put("version", "1.0");
        String declaration = xmlDeclaration.getWholeDeclaration();
        assertTrue(declaration.contains(" version=\"1.0\""));
    }

    @Test
    public void testXmlDeclarationWithEncodingAttribute() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        xmlDeclaration.attributes.put("encoding", "UTF-8");
        String declaration = xmlDeclaration.getWholeDeclaration();
        assertTrue(declaration.contains(" encoding=\"UTF-8\""));
    }

    @Test
    public void testXmlDeclarationWithDuplicateAttribute() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        xmlDeclaration.attributes.put("version", "1.0");
        xmlDeclaration.attributes.put("version", "1.1");
        String declaration = xmlDeclaration.getWholeDeclaration();
        fail("Expected IllegalArgumentException");
    }

    @Test(expected = IOException.class)
    public void testIOExceptionDuringOuterHtmlHead() throws IOException {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        Appendable accum = new StringBuffer();
        xmlDeclaration.outerHtmlHead(accum, 0, null);
        xmlDeclaration.outerHtmlTail(accum, 0, null);
        fail("Expected IOException");
    }

    @Test
    public void testXmlDeclarationWithEmptyDeclaration() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("", null, false);
        String declaration = xmlDeclaration.getWholeDeclaration();
        assertTrue(declaration.isEmpty());
    }

    @Test
    public void testXmlDeclarationWithNoAttributes() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        String declaration = xmlDeclaration.getWholeDeclaration();
        assertTrue(declaration.contains("<declaration"));
        assertTrue(declaration.contains("/>"));
    }

    @Test
    public void testXmlDeclarationWithProcessingInstructionTrueAndAttributes() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, true);
        xmlDeclaration.attributes.put("version", "1.0");
        xmlDeclaration.attributes.put("encoding", "UTF-8");
        String declaration = xmlDeclaration.getWholeDeclaration();
        assertTrue(declaration.contains("<?xml version=\"1.0\" encoding=\"UTF-8\""));
    }

    @Test
    public void testXmlDeclarationWithNameEmptyAndProcessingInstructionFalse() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("", null, false);
        String declaration = xmlDeclaration.getWholeDeclaration();
        assertTrue(declaration.isEmpty());
    }

    @Test
    public void testXmlDeclarationToString() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        String xmlDeclarationString = xmlDeclaration.toString();
        assertEquals(xmlDeclaration.getWholeDeclaration(), xmlDeclarationString);
    }
}