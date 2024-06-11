import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.lts.swing.combobox.SimpleComboBox;
import com.lts.swing.combobox.SimpleComboBoxModel;

import org.junit.Test;

public class SimpleComboBoxTest {

 private SimpleComboBoxModel mockModel;
 private SimpleComboBox simpleComboBox;

 @Test
 public void testConstructorWithValidModel() {
  mockModel = mock(SimpleComboBoxModel.class);
  simpleComboBox = new SimpleComboBox(mockModel);
  assertEquals(mockModel, simpleComboBox.getModel());
 }

 @Test
 public void testSetModelWithValidModel() {
  mockModel = mock(SimpleComboBoxModel.class);
  simpleComboBox = new SimpleComboBox(null);
  simpleComboBox.setModel(mockModel);
  assertEquals(mockModel, simpleComboBox.getModel());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetModelWithInvalidModel() {
  simpleComboBox = new SimpleComboBox(null);
  simpleComboBox.setModel(new Object());
 }

 @Test
 public void testGetSelectedValueWhenNullModel() {
  simpleComboBox = new SimpleComboBox(null);
  assertNull(simpleComboBox.getSelectedValue());
 }

 @Test
 public void testGetSelectedValueWithValidValue() {
  mockModel = spy(new SimpleComboBoxModel() {{
   setSelectedValue(new Integer(1));
  }});
  simpleComboBox = new SimpleComboBox(mockModel);
  assertEquals(1, simpleComboBox.getSelectedValue());
 }

 @Test
 public void testGetSelectedValueWithInvalidValue() {
  mockModel = spy(new SimpleComboBoxModel() {{
   setSelectedValue(new String("test"));
  }});
  simpleComboBox = new SimpleComboBox(mockModel);
  assertNull(simpleComboBox.getSelectedValue());
 }

 @Test
 public void testSetSelectedValueWithValidValue() {
  mockModel = mock(SimpleComboBoxModel.class);
  simpleComboBox = new SimpleComboBox(mockModel);
  simpleComboBox.setSelectedValue(new Integer(1));
  verify(mockModel).setSelectedValue(1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetSelectedValueWithInvalidValue() {
  simpleComboBox = new SimpleComboBox(null);
  simpleComboBox.setSelectedValue(new Object());
 }

 @Test
 public void testGetSelectedIntWhenNullModel() {
  simpleComboBox = new SimpleComboBox(null);
  assertEquals(-1, simpleComboBox.getSelectedInt());
 }

 @Test
 public void testGetSelectedIntWithValidIntValue() {
  simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
  simpleComboBox.setSelectedValue(new Integer(1));
  assertEquals(1, simpleComboBox.getSelectedInt());
 }

 @Test
 public void testGetSelectedIntWithLongValue() {
  simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
  simpleComboBox.setSelectedValue(new Long(1));
  assertEquals(1, simpleComboBox.getSelectedInt());
 }

 @Test
 public void testGetSelectedIntWithShortValue() {
  simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
  simpleComboBox.setSelectedValue(new Short("1"));
  assertEquals(1, simpleComboBox.getSelectedInt());
 }

 @Test
 public void testGetSelectedIntWithByteValue() {
  simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
  simpleComboBox.setSelectedValue(new Byte("1"));
  assertEquals(1, simpleComboBox.getSelectedInt());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSelectedIntWithInvalidValue() {
  simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
  simpleComboBox.setSelectedValue(new Object());
 }

 @Test
 public void testGetSelectedLongWhenNullModel() {
  simpleComboBox = new SimpleComboBox(null);
  assertEquals(-1L, simpleComboBox.getSelectedLong());
 }

 @Test
 public void testGetSelectedLongWithValidIntValue() {
  simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
  simpleComboBox.setSelectedValue(new Integer(1));
  assertEquals(1L, simpleComboBox.getSelectedLong());
 }

 @Test
 public void testGetSelectedLongWithValidLongValue() {
  simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
  simpleComboBox.setSelectedValue(new Long(1));
  assertEquals(1L, simpleComboBox.getSelectedLong());
 }

 @Test
 public void testGetSelectedLongWithShortValue() {
  simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
  simpleComboBox.setSelectedValue(new Short("1"));
  assertEquals(1L, simpleComboBox.getSelectedLong());
 }

 @Test
 public void testGetSelectedLongWithByteValue() {
  simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
  simpleComboBox.setSelectedValue(new Byte("1"));
  assertEquals(1L, simpleComboBox.getSelectedLong());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSelectedLongWithInvalidValue() {
  simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
  simpleComboBox.setSelectedValue(new Object());
 }
}