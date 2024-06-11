import org.junit.Test;
import static org.junit.Assert.*;

import com.lts.swing.combobox.SimpleComboBox;
import com.lts.swing.combobox.SimpleComboBoxModel;

public class SimpleComboBoxTest {

 private SimpleComboBox<Integer> combobox;
 private SimpleComboBoxModel<Integer> model;

 @Test
 public void testConstructorWithModel() {
 combobox = new SimpleComboBox<>(model = new SimpleComboBoxModel<>());
 assertTrue(combobox instanceof SimpleComboBox);
 assertEquals(model, combobox.getModel());
 }

 @Test
 public void testSetModel() {
 SimpleComboBox<String> oldCombobox = new SimpleComboBox<>(model = new SimpleComboBoxModel<>());
 SimpleComboBox<String> newCombobox = new SimpleComboBox<>(new SimpleComboBoxModel<>());
 oldCombobox.setModel(newCombobox.getModel());
 assertEquals(newCombobox.getModel(), oldCombobox.getModel());
 }

 @Test
 public void testGetSelectedValueWithNullModel() {
 combobox = new SimpleComboBox<>(null);
 assertNull(combobox.getSelectedValue());
 }

 @Test
 public void testGetSelectedValueWithIntegerModelAndSelectedValue() {
 model = new SimpleComboBoxModel<>(new Integer[10]);
 combobox = new SimpleComboBox<>(model);
 combobox.setSelectedIndex(5);
 assertEquals(new Integer(5), combobox.getSelectedValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSelectedValueWithInvalidType() {
 model = new SimpleComboBoxModel<>("Test");
 combobox = new SimpleComboBox<>(model);
 combobox.setSelectedValue("Test");
 }

 @Test
 public void testGetSelectedIntWithNullValue() {
 combobox = new SimpleComboBox<>(model = new SimpleComboBoxModel<Integer>(new Integer[10]));
 combobox.setSelectedValue(null);
 assertEquals(-1, combobox.getSelectedInt());
 }

 @Test
 public void testGetSelectedIntWithIntegerValue() {
 combobox = new SimpleComboBox<>(model = new SimpleComboBoxModel<Integer>(new Integer[10]));
 combobox.setSelectedValue(new Integer(5));
 assertEquals(5, combobox.getSelectedInt());
 }

 @Test
 public void testGetSelectedIntWithLongValue() {
 combobox = new SimpleComboBox<>(model = new SimpleComboBoxModel<Integer>(new Integer[10]));
 combobox.setSelectedValue(new Long(5));
 assertEquals(5, combobox.getSelectedInt());
 }

 @Test
 public void testGetSelectedIntWithShortValue() {
 combobox = new SimpleComboBox<>(model = new SimpleComboBoxModel<Integer>(new Integer[10]));
 combobox.setSelectedValue(new Short("5"));
 assertEquals(5, combobox.getSelectedInt());
 }

 @Test
 public void testGetSelectedIntWithByteValue() {
 combobox = new SimpleComboBox<>(model = new SimpleComboBoxModel<Integer>(new Integer[10]));
 combobox.setSelectedValue(new Byte("5"));
 assertEquals(5, combobox.getSelectedInt());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSelectedIntWithInvalidTypeValue() {
 combobox = new SimpleComboBox<>(model = new SimpleComboBoxModel<Integer>(new Integer[10]));
 combobox.setSelectedValue(new String("Test"));
 }

 @Test
 public void testSetSelectedValueWithNullValue() {
 combobox = new SimpleComboBox<>(model = new SimpleComboBoxModel<Integer>(new Integer[10]));
 combobox.setSelectedValue(null);
 assertNull(model.getSelectedValue());
 }

 @Test
 public void testSetSelectedValueWithIntegerValue() {
 model.setSelectedValue(5);
 combobox.setSelectedValue(new Integer(5));
 assertEquals(5, model.getSelectedValue());
 }

 @Test
 public void testSetSelectedValueWithLongValue() {
 model.setSelectedValue(5L);
 combobox.setSelectedValue(new Long(5));
 assertEquals(5, model.getSelectedValue());
 }

 @Test
 public void testSetSelectedValueWithShortValue() {
 model.setSelectedValue((short)5);
 combobox.setSelectedValue(new Short("5"));
 assertEquals(5, model.getSelectedValue());
 }

 @Test
 public void testSetSelectedValueWithByteValue() {
 model.setSelectedValue((byte)5);
 combobox.setSelectedValue(new Byte("5"));
 assertEquals(5, model.getSelectedValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetSelectedValueWithInvalidTypeValue() {
 model.setSelectedValue("Test");
 combobox.setSelectedValue("Test");
 }
}