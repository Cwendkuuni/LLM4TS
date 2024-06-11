package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FormElementTest {

 private static final String BASE_URI = "http://example.com";
 private static final String FORM_ACTION = "http://example.com/submit";
 private static final String FORM_ELEMENT_TAG = "form";
 private static final String FORM_CONTROL_INPUT_TAG = "input";
 private static final String FORM_CONTROL_SELECT_TAG = "select";
 private static final String FORM_CONTROL_CHECKBOX_TAG = "input";
 private static final String FORM_CONTROL_CHECKBOX_TYPE = "checkbox";
 private static final String FORM_CONTROL_RADIO_TAG = "input";
 private static final String FORM_CONTROL_RADIO_TYPE = "radio";
 private static final String FORM_CONTROL_SELECT_OPTION_TAG = "option";
 private static final String FORM_CONTROL_SELECT_NAME = "exampleSelect";
 private static final String FORM_CONTROL_SELECT_VALUE_1 = "option1";
 private static final String FORM_CONTROL_SELECT_VALUE_2 = "option2";

 private FormElement formElement;
 private Element formControlInput;
 private Element formControlSelect;
 private Element formControlCheckbox;
 private Element formControlRadio;
 private Connection connection;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);

 formElement = mock(FormElement.class);
 when(formElement.tagName()).thenReturn(FORM_ELEMENT_TAG);
 when(formElement.baseUri()).thenReturn(BASE_URI);
 when(formElement.attributes()).thenReturn(new Attributes());

 formControlInput = mock(Element.class);
 when(formControlInput.tagName()).thenReturn(FORM_CONTROL_INPUT_TAG);
 when(formControlInput.val()).thenReturn("exampleInputValue");
 when(formControlInput.attr("name")).thenReturn("exampleInputName");
 when(formControlInput.attr("type")).thenReturn("text");
 when(formElement.elements()).thenReturn(new Elements(formControlInput));

 formControlSelect = mock(Element.class);
 when(formControlSelect.tagName()).thenReturn(FORM_CONTROL_SELECT_TAG);
 when(formControlSelect.val()).thenReturn("exampleSelectValue");
 when(formControlSelect.attr("name")).thenReturn(FORM_CONTROL_SELECT_NAME);
 Elements formControlSelectOptions = new Elements();
 Element formControlSelectOption1 = mock(Element.class);
 when(formControlSelectOption1.tagName()).thenReturn(FORM_CONTROL_SELECT_OPTION_TAG);
 when(formControlSelectOption1.val()).thenReturn(FORM_CONTROL_SELECT_VALUE_1);
 Element formControlSelectOption2 = mock(Element.class);
 when(formControlSelectOption2.tagName()).thenReturn(FORM_CONTROL_SELECT_OPTION_TAG);
 when(formControlSelectOption2.val()).thenReturn(FORM_CONTROL_SELECT_VALUE_2);
 formControlSelectOptions.add(formControlSelectOption1);
 formControlSelectOptions.add(formControlSelectOption2);
 when(formControlSelect.select("option[selected]")).thenReturn(formControlSelectOptions);
 when(formControlSelect.select("option")).thenReturn(formControlSelectOptions);
 when(formControlSelect.attr("multiple")).thenReturn("multiple");
 when(formElement.elements()).thenReturn(new Elements(formControlSelect));

 formControlCheckbox = mock(Element.class);
 when(formControlCheckbox.tagName()).thenReturn(FORM_CONTROL_CHECKBOX_TAG);
 when(formControlCheckbox.attr("name")).thenReturn("exampleCheckboxName");
 when(formControlCheckbox.attr("type")).thenReturn(FORM_CONTROL_CHECKBOX_TYPE);
 when(formControlCheckbox.hasAttr("checked")).thenReturn(true);
 when(formElement.elements()).thenReturn(new Elements(formControlCheckbox));

 formControlRadio = mock(Element.class);
 when(formControlRadio.tagName()).thenReturn(FORM_CONTROL_RADIO_TAG);
 when(formControlRadio.attr("name")).thenReturn("exampleRadioName");
 when(formControlRadio.attr("type")).thenReturn(FORM_CONTROL_RADIO_TYPE);
 when(formControlRadio.hasAttr("checked")).thenReturn(true);
 when(formElement.elements()).thenReturn(new Elements(formControlRadio));

 connection = mock(Connection.class);
 }

 @Test
 public void testGetElements() {
 Elements expectedElements = new Elements(formControlInput, formControlSelect, formControlCheckbox, formControlRadio);
 Elements actualElements = formElement.elements();
 assertEquals(expectedElements.size(), actualElements.size());
 for (Element expectedElement : expectedElements) {
 assertTrue(actualElements.contains(expectedElement));
 }
 }

 @Test
 public void testAddElement() {
 Element addedElement = mock(Element.class);
 when(addedElement.tagName()).thenReturn(FORM_CONTROL_INPUT_TAG);
 formElement.addElement(addedElement);
 assertEquals(5, formElement.elements().size());
 }

 @Test
 public void testSubmitWithTextInput() throws Exception {
 List<Connection.KeyVal> expectedFormData = Arrays.asList(
 new Connection.KeyVal("exampleInputName", "exampleInputValue"));
 List<Connection.KeyVal> actualFormData = formElement.submit().data();
 assertEquals(expectedFormData, actualFormData);
 }

 @Test
 public void testSubmitWithSelectInput() throws Exception {
 List<Connection.KeyVal> expectedFormData = Arrays.asList(
 new Connection.KeyVal(FORM_CONTROL_SELECT_NAME, FORM_CONTROL_SELECT_VALUE_1));
 List<Connection.KeyVal> actualFormData = formElement.submit().data();
 assertEquals(expectedFormData, actualFormData);
 }

 @Test
 public void testSubmitWithCheckboxInput() throws Exception {
 List<Connection.KeyVal> expectedFormData = Arrays.asList(
 new Connection.KeyVal("exampleCheckboxName", "on"));
 List<Connection.KeyVal> actualFormData = formElement.submit().data();
 assertEquals(expectedFormData, actualFormData);
 }

 @Test
 public void testSubmitWithRadioInput() throws Exception {
 List<Connection.KeyVal> expectedFormData = Arrays.asList(
 new Connection.KeyVal("exampleRadioName", "exampleRadioValue"));
 when(formControlRadio.val()).thenReturn("exampleRadioValue");
 List<Connection.KeyVal> actualFormData = formElement.submit().data();
 assertEquals(expectedFormData, actualFormData);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitWithNoActionUrl() {
 try {
 formElement.submit();
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }
}