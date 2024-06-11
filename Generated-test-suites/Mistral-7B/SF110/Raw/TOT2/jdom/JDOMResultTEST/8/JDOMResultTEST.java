import org.jdom2.JDOMException;
import org.jdom2.Content;
import org.jdom2.JDOMResult;
import org.junit.Assert;
import org.junit.Test;

public class JDOMResultTest {

    @Test
    public void testSetResult() throws JDOMException {
        JDOMResult jdomResult = new JDOMResult();
        List<Content> result = new ArrayList<>();
        result.add(new Element("testElement"));
        jdomResult.setResult(result);
        List<Content> actualResult = jdomResult.getResult();
        Assert.assertEquals(result, actualResult);
    }
}