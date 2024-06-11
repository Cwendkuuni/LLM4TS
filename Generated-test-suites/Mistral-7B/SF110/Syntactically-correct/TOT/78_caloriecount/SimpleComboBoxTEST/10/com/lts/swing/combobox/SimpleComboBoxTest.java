import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.lts.swing.combobox.SimpleComboBox;
import com.lts.swing.combobox.SimpleComboBoxModel;
import java.util.Arrays;

public class SimpleComboBoxTest {

 private SimpleComboBox<Integer> comboBox;
 private SimpleComboBoxModel<Integer> model;

 @Before
 public void setUp() {
 model = new SimpleComboBoxModel<>();
 model.addElement(1);
 model.addElement(2);
 model.addElement(3);
 comboBox = new SimpleComboBox<>(model);
 }

 @Test
 public void testGetSelectedValue() {
 assertEquals(1, comboBox.getSelectedValue());
 comboBox.setSelectedIndex(1);
 assertEquals(2, comboBox.getSelectedValue());
 }

 @Test
 public void testSetSelectedValue() {
 comboBox.setSelectedValue(2);
 assertEquals(2, comboBox.getSelectedValue());
 }

 @Test
 public void testGetSelectedInt() {
 assertEquals(1, comboBox.getSelectedInt());
 comboBox.setSelectedIndex(1);
 assertEquals(2, comboBox.getSelectedInt());
 }

 @Test
 public void testGetSelectedLong() {
 assertEquals(1, comboBox.getSelectedLong());
 comboBox.setSelectedIndex(1);
 assertEquals(2, comboBox.getSelectedLong());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSelectedValueOrThrowIllegalArgumentException() {
 model.addElement("not a number");
 comboBox.setModel(model);
 assertNull(comboBox.getSelectedValue());
 comboBox.setSelectedValue("not a number");
 }

 @Test
 public void testGetSelectedIntOrThrowIllegalArgumentException() {
 model.addElement("not a number");
 comboBox.setModel(model);
 assertEquals(-1, comboBox.getSelectedInt());
 comboBox.setSelectedValue("not a number");
 }

 @Test
 public void testGetSelectedLongOrThrowIllegalArgumentException() {
 model.addElement("not a number");
 comboBox.setModel(model);
 assertEquals(-1L, comboBox.getSelectedLong());
 comboBox.setSelectedValue("not a number");
 }

 @Test
 public void testSetSelectedValueNull() {
 model.addElement(null);
 comboBox.setModel(model);
 assertNull(comboBox.getSelectedValue());
 comboBox.setSelectedValue(null);
 }

 @Test
 public void testSetSelectedValueNullAndGetSelectedValue() {
 model.addElement(null);
 comboBox.setModel(model);
 assertNull(comboBox.getSelectedValue());
 comboBox.setSelectedValue(2);
 assertEquals(2, comboBox.getSelectedValue());
 }

 @Test
 public void testSetSelectedValueNonInteger() {
 model.addElement("not an integer");
 comboBox.setModel(model);
 assertNull(comboBox.getSelectedValue());
 comboBox.setSelectedValue("not an integer");
 }

 @Test
 public void testSetSelectedValueNonIntegerAndGetSelectedValue() {
 model.addElement("not an integer");
 comboBox.setModel(model);
 assertNull(comboBox.getSelectedValue());
 comboBox.setSelectedValue(2);
 assertEquals(2, comboBox.getSelectedValue());
 }

 @Test
 public void testSetSelectedIntOutOfBounds() {
 comboBox.setModel(model);
 try {
 comboBox.setSelectedInt(-1);
 fail();
 } catch (ArrayIndexOutOfBoundsException e) {
 // expected
 }
 }

 @Test
 public void testSetSelectedLongOutOfBounds() {
 comboBox.setModel(model);
 try {
 comboBox.setSelectedLong(-1);
 fail();
 } catch (ArrayIndexOutOfBoundsException e) {
 // expected
 }
 }

 @Test
 public void testSetSelectedIntLargerThanSize() {
 comboBox.setModel(model);
 try {
 comboBox.setSelectedInt(Arrays.asList(model.getElements()).size());
 fail();
 } catch (ArrayIndexOutOfBoundsException e) {
 // expected
 }
 }

 @Test
 public void testSetSelectedLongLargerThanSize() {
 comboBox.setModel(model);
 try {
 comboBox.setSelectedLong(Arrays.asList(model.getElements()).size() + 1);
 fail();
 } catch (ArrayIndexOutOfBoundsException e) {
 // expected
 }
 }
}