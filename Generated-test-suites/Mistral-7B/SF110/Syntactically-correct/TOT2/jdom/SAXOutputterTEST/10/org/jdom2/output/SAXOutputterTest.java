import org.jdom2.output.SAXOutputter;
import org.junit.Assert;
import org.junit.Test;

public class SAXOutputterTest {
    @Test
    public void testDefaultSAXOutputterConstruction() {
        SAXOutputter saxOutputter = new SAXOutputter();

        Assert.assertFalse(saxOutputter.isDeclareNamespaces());
        Assert.assertTrue(saxOutputter.isReportDtdEvents());
        Assert.assertEquals(SAXOutputter.DEFAULT_PROCESSOR, saxOutputter.getSAXOutputProcessor());
        Assert.assertEquals(Format.getRawFormat(), saxOutputter.getFormat());
    }
}