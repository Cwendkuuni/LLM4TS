import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.junit.Assert;
import org.junit.Test;

public class FormElementTest {
    @Test
    public void testElements() {
        Tag tag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        FormElement formElement = new FormElement(tag, baseUri, attributes);
        Element element = new Element("input");

        formElement.addElement(element);

        List<Element> expectedElements = new ArrayList<>();
        expectedElements.add(element);

        List<Element> actualElements = formElement.elements();

        Assert.assertEquals(expectedElements, actualElements);
    }
}