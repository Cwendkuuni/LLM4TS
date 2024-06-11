import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class FormElementTest {
    @Test
    public void testElements() {
        Tag tag = new Tag("form");
        String baseUri = "example.com";
        Attributes attributes = new Attributes();

        FormElement formElement = new FormElement(tag, baseUri, attributes);
        Elements expectedElements = new Elements();
        Elements actualElements = formElement.elements();

        expectedElements.add(new Element("input", new Attributes()));
        expectedElements.add(new Element("textarea", new Attributes()));
        expectedElements.add(new Element("select", new Attributes()));

        assertEquals(expectedElements, actualElements);
    }
}