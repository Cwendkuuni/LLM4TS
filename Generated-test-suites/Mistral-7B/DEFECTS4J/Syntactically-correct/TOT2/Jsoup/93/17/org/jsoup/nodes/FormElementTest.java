import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.junit.Test;
import static org.junit.Assert.*;

public class FormElementTest {
    @Test
    public void testFormElementConstructor() {
        Tag tag = new Tag("form");
        String baseUri = "http://www.example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        assertEquals("form", formElement.tagName());
    }
}