import org.junit.Test;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import static org.junit.Assert.assertEquals;

public class FormElementTest {

    @Test
    public void testElements() {
        Tag tag = new Tag("form");
        FormElement formElement = new FormElement(tag, "baseUri", new Attributes());
        Element element = new Element("input");
        formElement.addElement(element);

        Elements expectedElements = new Elements();
        expectedElements.add(element);

        Elements actualElements = formElement.elements();

        assertEquals(expectedElements, actualElements);
    }
}