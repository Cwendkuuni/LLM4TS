import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FormElementTest {
    @Test
    public void testFormElementConstructor() {
        Tag tag = new Tag("form");
        Attributes attributes = new Attributes();
        attributes.put("action", "http://example.com/form");

        FormElement formElement = new FormElement(tag, "http://example.com", attributes);

        assertEquals("form", formElement.tagName());
        assertEquals("http://example.com/form", formElement.attributes().get("action"));
    }
}