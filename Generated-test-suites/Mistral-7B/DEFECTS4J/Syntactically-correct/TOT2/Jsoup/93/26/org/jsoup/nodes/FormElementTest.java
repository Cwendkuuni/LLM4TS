import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.junit.Test;
import static org.junit.Assert.*;

public class FormElementTest {

    @Test
    public void testFormElementConstructor() {
        // Given a valid tag, baseUri, and attributes
        Tag tag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "form1");

        // When creating a new FormElement instance
        FormElement formElement = new FormElement(tag, baseUri, attributes);

        // Then the FormElement instance should be created successfully
        assertNotNull(formElement);
        assertEquals(tag.tagName(), formElement.tagName());
        assertEquals(baseUri, formElement.baseUri());
        assertEquals(attributes, formElement.attributes());
    }
}