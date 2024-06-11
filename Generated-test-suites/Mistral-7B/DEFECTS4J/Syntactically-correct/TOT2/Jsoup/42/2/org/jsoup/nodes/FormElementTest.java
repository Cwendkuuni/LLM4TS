import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import static org.junit.Assert.assertEquals;

public class FormElementTest {
    @Test
    public void testFormElementConstruction() {
        Tag tag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "form1");

        FormElement formElement = new FormElement(tag, baseUri, attributes);

        assertEquals("form", formElement.tagName());
        assertEquals("form1", formElement.attr("id"));
        assertEquals("http://example.com", formElement.baseUri());
    }
}