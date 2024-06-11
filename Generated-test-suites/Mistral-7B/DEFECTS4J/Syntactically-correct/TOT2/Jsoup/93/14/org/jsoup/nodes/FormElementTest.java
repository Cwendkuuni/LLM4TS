import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;

public class FormElementTest {
    @Test
    public void testElements() {
        String html = "<html><body><form><input type='text' name='input1'></form></body></html>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = (FormElement) doc.select("form").first();
        Elements expectedElements = doc.select("input[name='input1']");

        Assert.assertEquals(expectedElements.size(), formElement.elements().size());
        for (Element element : formElement.elements()) {
            Assert.assertTrue(expectedElements.contains(element));
        }
    }
}