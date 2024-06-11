package umd.cs.shop;
import org.junit.Assert;
import org.junit.Test;
import umd.cs.shop.JSJshop;

public class JSJshopTest {

    @Test
    public void testJSJshopConstructorWithValidArguments() {
        String domainFileName = "domainFile.shp";
        String problemFileName = "problemFile.shp";
        JSJshop jsJshop = new JSJshop(domainFileName, problemFileName);
        Assert.assertNotNull(jsJshop.getTree());
        Assert.assertNotNull(jsJshop.getSolution());
    }
}