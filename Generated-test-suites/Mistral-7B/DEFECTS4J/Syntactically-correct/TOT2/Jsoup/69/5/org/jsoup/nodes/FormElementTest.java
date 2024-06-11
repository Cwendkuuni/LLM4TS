import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.*;

public class FormElementTest {
    @Test
    public void testElements() {
        Tag tag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Element formElement = new FormElement(tag, baseUri, attributes);
        Elements expectedElements = new Elements();
        Element inputElement = new Element("input", new Attributes());
        expectedElements.add(inputElement);
        formElement.addElement(inputElement);
        Elements actualElements = formElement.elements();
        Assert.assertEquals(expectedElements, actualElements);
    }
}