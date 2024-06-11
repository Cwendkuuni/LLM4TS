import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.ParseSetting;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.jsoup.Jsoup.connect;
import static org.jsoup.Jsoup.parse;

public class FormElementTest {

 private static final String HTML_FORM_WITH_ELEMENTS = "" +
         "<html lang=\"en\">" +
         "<head>" +
         "<meta charset=\"UTF-8\">" +
         "</head>" +
         "<body>" +
         "<form action=\"/submit\">" +
         "<input name=\"input1\" type=\"text\" value=\"value1\">" +
         "<select name=\"select1\">" +
         "<option value=\"option1\">Option 1</option>" +
         "<option value=\"option2\" selected>Option 2</option>" +
         "</select>" +
         "<input name=\"input2\" type=\"hidden\" value=\"value2\">" +
         "<input name=\"input3\" type=\"checkbox\" value=\"value3\">" +
         "<input name=\"input4\" type=\"radio\" value=\"value4\" checked>" +
         "</form>" +
         "</body>" +
         "</html>";

 private Element formElement;

 @Before
 public void setUp() {
 ParseSetting parseSetting = new ParseSetting.Builder().build();
 formElement = parse(HTML_FORM_WITH_ELEMENTS, parseSetting).body().selectFirst("form");
 }

 @Test
 public void testElementsReturnFormControls() {
 Elements formControls = formElement.elements();
 List<String> formControlNames = new ArrayList<>();
 for (Element control : formControls) {
 formControlNames.add(control.attr("name"));
 }
 List<String> expectedFormControlNames = List.of("input1", "select1", "input2", "input3", "input4");
 assertThat(formControlNames, is(expectedFormControlNames));
 }

 @Test
 public void testSubmitReturnsConnectionToFormActionUrl() {
 Connection connection = formElement.submit();
 String formActionUrl = connection.url();
 String expectedFormActionUrl = "/submit";
 assertThat(formActionUrl, is(expectedFormActionUrl));
 }

 @Test
 public void testFormDataContainsExpectedValues() {
 List<Connection.KeyVal> formData = formElement.formData();
 List<Connection.KeyVal> expectedFormData = List.of(
         new Connection.KeyVal("input1", "value1"),
         new Connection.KeyVal("select1", "option2"),
         new Connection.KeyVal("input2", "value2"),
         new Connection.KeyVal("input3", "value3"),
         new Connection.KeyVal("input4", "value4")
 );
 assertThat(formData, is(expectedFormData));
 }
}