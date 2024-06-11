package org.jdom2.output;
import org.jdom2.*;
import org.junit.Assert;
import org.junit.Test;

public class SAXOutputterTest {
    @Test
    public void testDefaultConstructor() {
        SAXOutputter saxOutputter = new SAXOutputter();
        Assert.assertNull(saxOutputter.getContentHandler());
        Assert.assertNull(saxOutputter.getErrorHandler());
        Assert.assertNull(saxOutputter.getDTDHandler());
        Assert.assertNull(saxOutputter.getEntityResolver());
        Assert.assertNull(saxOutputter.getLexicalHandler());
        Assert.assertNull(saxOutputter.getDeclHandler());
        Assert.assertFalse(saxOutputter.getReportNamespaceDeclarations());
        Assert.assertTrue(saxOutputter.getReportDTDEvents());
        Assert.assertNull(saxOutputter.getSAXOutputProcessor());
        Assert.assertEquals(Format.getRawFormat(), saxOutputter.getFormat());
    }
}