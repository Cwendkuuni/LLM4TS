import org.junit.Test;
import static org.junit.Assert.*;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ItemEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.EventObject;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import com.lts.swing.combobox.SimpleComboBox;
import com.lts.swing.combobox.SimpleComboBoxModel;

public class SimpleComboBoxTest {

 private SimpleComboBox<Integer> simpleComboBox;
 private SimpleComboBoxModel<Integer> model;

 @Test
 public void testSimpleComboBoxConstruction() {
 SimpleComboBox<Integer> simpleComboBox = new SimpleComboBox<>(new SimpleComboBoxModel<>());
 assertNotNull(simpleComboBox);
 }

 @Test
 public void testSetModel() {
 SimpleComboBox<Integer> simpleComboBox = new SimpleComboBox<>(new SimpleComboBoxModel<>());
 SimpleComboBoxModel<Integer> model = new SimpleComboBoxModel<>(Arrays.asList(1, 2, 3, 4, 5));
 simpleComboBox.setModel(model);
 assertSame(model, simpleComboBox.getModel());
 }

 @Test
 public void testGetSelectedValue() {
 SimpleComboBox<Integer> simpleComboBox = new SimpleComboBox<>(new SimpleComboBoxModel<>());
 simpleComboBox.setModel(new SimpleComboBoxModel<>(Arrays.asList(1, 2, 3, 4, 5)));
 simpleComboBox.setSelectedIndex(2);
 assertEquals(3, simpleComboBox.getSelectedValue());
 }

 @Test
 public void testGetSelectedValue_NullModel() {
 SimpleComboBox<Integer> simpleComboBox = new SimpleComboBox<>(new SimpleComboBoxModel<>());
 assertEquals(null, simpleComboBox.getSelectedValue());
 }

 @Test
 public void testSetSelectedValue() {
 SimpleComboBox<Integer> simpleComboBox = new SimpleComboBox<>(new SimpleComboBoxModel<>());
 SimpleComboBoxModel<Integer> model = new SimpleComboBoxModel<>(Arrays.asList(1, 2, 3, 4, 5));
 simpleComboBox.setModel(model);
 simpleComboBox.setSelectedValue(3);
 assertEquals(3, simpleComboBox.getSelectedValue());
 }

 @Test
 public void testSetSelectedValue_NullValue() {
 SimpleComboBox<Integer> simpleComboBox = new SimpleComboBox<>(new SimpleComboBoxModel<>());
 simpleComboBox.setSelectedValue(null);
 assertNull(simpleComboBox.getSelectedValue());
 }

 @Test
 public void testGetSelectedInt() {
 SimpleComboBox<Integer> simpleComboBox = new SimpleComboBox<>(new SimpleComboBoxModel<>());
 simpleComboBox.setModel(new SimpleComboBoxModel<>(Arrays.asList(1, 2, 3, 4, 5)));
 simpleComboBox.setSelectedIndex(2);
 assertEquals(3, simpleComboBox.getSelectedInt());
 }

 @Test
 public void testGetSelectedInt_NullModel() {
 SimpleComboBox<Integer> simpleComboBox = new SimpleComboBox<>(new SimpleComboBoxModel<>());
 assertEquals(-1, simpleComboBox.getSelectedInt());
 }

 @Test
 public void testGetSelectedInt_NonIntegerValue() {
 SimpleComboBox<Integer> simpleComboBox = new SimpleComboBox<>(new SimpleComboBoxModel<>());
 simpleComboBox.setModel(new SimpleComboBoxModel<>(Arrays.asList("One", "Two", "Three", "Four", "Five")));
 simpleComboBox.setSelectedIndex(2);
 assertEquals(-1, simpleComboBox.getSelectedInt());
 }

 @Test
 public void testGetSelectedLong() {
 SimpleComboBox<Long> simpleComboBox = new SimpleComboBox<>(new SimpleComboBoxModel<>());
 simpleComboBox.setModel(new SimpleComboBoxModel<>(Arrays.asList(1L, 2L, 3L, 4L, 5L)));
 simpleComboBox.setSelectedIndex(2);
 assertEquals(3L, simpleComboBox.getSelectedLong());
 }

 @Test
 public void testGetSelectedLong_NullModel() {
 SimpleComboBox<Long> simpleComboBox = new SimpleComboBox<>(new SimpleComboBoxModel<>());
 assertEquals(-1L, simpleComboBox.getSelectedLong());
 }

 @Test
 public void testGetSelectedLong_NonLongValue() {
 SimpleComboBox<Long> simpleComboBox = new SimpleComboBox<>(new SimpleComboBoxModel<>());
 simpleComboBox.setModel(new SimpleComboBoxModel<>(Arrays.asList("One", "Two", "Three", "Four", "Five")));
 simpleComboBox.setSelectedIndex(2);
 assertEquals(-1L, simpleComboBox.getSelectedLong());
 }

 @Test
 public void testOverriddenSetModel() throws IlllegalAccessException, InvocationTargetException, InstantiationException {
 SimpleComboBox<Integer> simpleComboBox = new SimpleComboBox<>(new SimpleComboBoxModel<>());
 SimpleComboBoxModel<Integer> model = new SimpleComboBoxModel<>(Arrays.asList(1, 2, 3, 4, 5));
 simpleComboBox.setModel(model);

 // call super setModel method
 Component parent = new JFrame();
 Container container = new JScrollPane();
 ((JFrame)parent).setContentPane(container);
 container.add(simpleComboBox);
 SwingUtilities.invokeAndWait(() -> simpleComboBox.setModel(new SimpleComboBoxModel<>()));

 // check if mySimpleModel remains the same
 assertEquals(model, simpleComboBox.getModel());
 }
}