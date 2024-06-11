import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class FormElementTest {

    private Element form;
    private Element input;
    private Element select;
    private Element checkbox;

    @Before
    public void setUp() {
        String html = "<html lang=\"en\"><head></head><body>" +
                "<form action=\"http://example.com\" method=\"post\">" +
                "<input type=\"text\" name=\"input\" value=\"inputValue\">" +
                "<select name=\"select\">" +
                "<option value=\"option1\" selected>Option 1</option>" +
                "<option value=\"option2\">Option 2</option>" +
                "</select>" +
                "<input type=\"checkbox\" name=\"checkbox\" checked>" +
                "</form>" +
                "</body></html>";

        Document doc = Jsoup.parse(html);
        form = doc.select("form").first();
        input = form.select("input[type=\"text\"]").first();
        select = form.select("select").first();
        checkbox = form.select("input[type=\"checkbox\"]").first();
    }

    @Test
    public void testFormElementCreation() {
        assertNotNull(form);
    }

    @Test
    public void testElements() {
        Elements elements = form.elements();
        List<Element> expectedElements = Arrays.asList(input, select, checkbox);

        assertEquals(expectedElements.size(), elements.size());
        for (Element element : expectedElements) {
            assertEquals(element, elements.get(elements.indexOf(element)));
        }
    }

    @Test
    public void testAddElement() {
        FormElement formElement = new FormElement(Tag.FORM, "", new Attributes());
        Element elementToAdd = new Element("input", new Attributes().put("type", "text"));

        formElement.addElement(elementToAdd);
        Elements elements = formElement.elements();

        assertEquals(1, elements.size());
        assertEquals(elementToAdd, elements.get(0));
    }

    @Test
    public void testSubmit() {
        Connection connection = form.submit();
        String action = form.attr("action");
        assertEquals(action, connection.url());
    }

    @Test
    public void testFormDataInput() {
        List<Connection.KeyVal> formData = form.formData();
        List<Connection.KeyVal> expectedFormData = Arrays.asList(
                new Connection.KeyVal("input", "inputValue"),
                new Connection.KeyVal("select", "option1")
        );

        assertEquals(expectedFormData.size(), formData.size());
        for (int i = 0; i < formData.size(); i++) {
            assertEquals(expectedFormData.get(i), formData.get(i));
        }
    }

    @Test
    public void testFormDataSelect() {
        List<Connection.KeyVal> formData = form.formData();
        List<Connection.KeyVal> expectedFormData = Arrays.asList(
                new Connection.KeyVal("select", "option1")
        );

        select.select("option[selected]").remove();
        select.select("option:first").attr("selected", "selected");

        formData = form.formData();

        assertEquals(expectedFormData.size(), formData.size());
        for (int i = 0; i < formData.size(); i++) {
            assertEquals(expectedFormData.get(i), formData.get(i));
        }
    }

    @Test
    public void testFormDataCheckbox() {
        List<Connection.KeyVal> formData = form.formData();
        List<Connection.KeyVal> expectedFormData = Arrays.asList(
                new Connection.KeyVal("checkbox", "on")
        );

        checkbox.attr("checked", "false");

        formData = form.formData();

        assertEquals(expectedFormData.size(), formData.size());
        for (int i = 0; i < formData.size(); i++) {
            assertEquals(expectedFormData.get(i), formData.get(i));
        }

        checkbox.attr("checked", "true");

        formData = form.formData();

        assertEquals(expectedFormData.size(), formData.size());
        for (int i = 0; i < formData.size(); i++) {
            assertEquals(expectedFormData.get(i), formData.get(i));
        }
    }
}