import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.ParseSetup;
import org.jsoup.parser.Parser;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class FormElementTest {

    private Parser parser;
    private Element formElement;

    @Before
    public void setUp() {
        ParseSetup setup = new ParseSetup.Builder().build();
        parser = setup.parser();

        String html = "<form action=\"http://example.com\">" +
                "<input type=\"text\" name=\"username\" value=\"test\">" +
                "<select name=\"option\">" +
                "<option value=\"option1\" selected>" +
                "Option 1" +
                "</option>" +
                "<option value=\"option2\">" +
                "Option 2" +
                "</option>" +
                "</select>" +
                "<input type=\"checkbox\" name=\"checkbox\" checked>" +
                "</form>";

        formElement = parser.parse(html).selectFirst("form");
    }

    @Test
    public void testFormElementConstruction() {
        assertNotNull(formElement);
        Tag tag = formElement.tag();
        assertEquals("form", tag.name());
    }

    @Test
    public void testElements() {
        Elements elements = formElement.elements();
        assertEquals(3, elements.size());

        Element input = elements.get(0);
        assertEquals("input", input.tagName());

        Element select = elements.get(1);
        assertEquals("select", select.tagName());

        Element checkbox = elements.get(2);
        assertEquals("input", checkbox.tagName());
    }

    @Test
    public void testAddElement() {
        Element newInput = parser.parse("<input type=\"text\" name=\"newInput\" value=\"newValue\">").selectFirst("input");
        FormElement formElementAfterAdd = formElement.addElement(newInput);

        Elements newElement = formElementAfterAdd.elements();
        assertEquals(4, newElement.size());
        Element newAddedElement = newElement.get(3);
        assertEquals("input", newAddedElement.tagName());
        assertEquals("newInput", newAddedElement.attr("name"));
    }

    @Test
    public void testSubmit() {
        Connection connection = formElement.submit();
        String action = connection.url();
        assertEquals("http://example.com", action);
    }

    @Test
    public void testFormDataForTextInput() {
        List<Connection.KeyVal> formData = formElement.formData();
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("username", keyVal.key());
        assertEquals("test", keyVal.value());
    }

    @Test
    public void testFormDataForSelectInput() {
        List<Connection.KeyVal> formData = formElement.formData();
        Connection.KeyVal keyVal = formData.get(1);
        assertEquals("option", keyVal.key());
        assertEquals("option1", keyVal.value());
    }

    @Test
    public void testFormDataForCheckboxInput() {
        List<Connection.KeyVal> formData = formElement.formData();
        Connection.KeyVal keyVal = formData.get(2);
        assertEquals("checkbox", keyVal.key());
        assertTrue(keyVal.value().equals(""));
    }
}