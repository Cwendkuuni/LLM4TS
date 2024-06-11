import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.awt.EventQueue;
import java.awt.event.InvocationEvent;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.lts.swing.combobox.SimpleComboBox;
import com.lts.swing.combobox.SimpleComboBoxModel;

public class SimpleComboBoxTest {

 private JFrame frame;
 private SimpleComboBox<Integer> comboBox;

 @Before
 public void setUp() {
 frame = new JFrame();
 frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

 comboBox = new SimpleComboBox<>(new SimpleComboBoxModel<>(Arrays.asList(1, 2, 3, 4, 5)));
 frame.add(comboBox);
 frame.setSize(300, 200);
 frame.setVisible(true);
 }

 @After
 public void tearDown() {
 SwingUtilities.invokeLater(new Runnable() {
 @Override
 public void run() {
 frame.dispose();
 }
 });
 }

 @Test
 public void testGetSelectedValue_whenComboBoxHasValue_thenReturnSelectedValue() {
 final int expectedValue = 3;
 SwingUtilities.invokeLater(new Runnable() {
 @Override
 public void run() {
 comboBox.setSelectedIndex(3);
 }
 });

 EventQueue.invokeLater(new Runnable() {
 @Override
 public void run() {
 final Object actualValue = comboBox.getSelectedValue();
 assertEquals(expectedValue, actualValue);
 }
 });
 }

 @Test
 public void testGetSelectedValue_whenComboBoxHasNoValue_thenReturnNull() {
 SwingUtilities.invokeLater(new Runnable() {
 @Override
 public void run() {
 comboBox.setModel(new SimpleComboBoxModel<>());
 }
 });

 EventQueue.invokeLater(new Runnable() {
 @Override
 public void run() {
 final Object actualValue = comboBox.getSelectedValue();
 assertNull(actualValue);
 }
 });
 }

 @Test
 public void testSetSelectedValue_whenValueIsPresentInComboBox_thenSetSelectedValue() {
 final int valueToSet = 2;
 SwingUtilities.invokeLater(new Runnable() {
 @Override
 public void run() {
 comboBox.setSelectedValue(valueToSet);
 }
 });

 EventQueue.invokeLater(new Runnable() {
 @Override
 public void run() {
 final Object actualValue = comboBox.getSelectedValue();
 assertEquals(valueToSet, actualValue);
 }
 });
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetSelectedValue_whenValueIsNotInstanceOfIntegerOrLongOrShortOrByte_thenThrowIllegalArgumentException() {
 SwingUtilities.invokeLater(new Runnable() {
 @Override
 public void run() {
 comboBox.setSelectedValue("not a number");
 }
 });

 EventQueue.invokeLater(new Runnable() {
 @Override
 public void run() {
 assertThrows(IllegalArgumentException.class, new Runnable() {
 @Override
 public void run() {
 comboBox.getSelectedValue();
 }
 });
 }
 });
 }

 @Test
 public void testGetSelectedInt_whenComboBoxHasIntegerValue_thenReturnIntValue() {
 final int expectedValue = 3;
 SwingUtilities.invokeLater(new Runnable() {
 @Override
 public void run() {
 comboBox.setSelectedIndex(3);
 }
 });

 EventQueue.invokeLater(new Runnable() {
 @Override
 public void run() {
 final int actualValue = comboBox.getSelectedInt();
 assertEquals(expectedValue, actualValue);
 }
 });
 }

 @Test
 public void testGetSelectedInt_whenComboBoxHasLongValue_thenReturnCastedIntValue() {
 final long expectedValue = 10L;
 SwingUtilities.invokeLater(new Runnable() {
 @Override
 public void run() {
 comboBox.setSelectedValue(expectedValue);
 }
 });

 EventQueue.invokeLater(new Runnable() {
 @Override
 public void run() {
 final int actualValue = comboBox.getSelectedInt();
 assertEquals(2, actualValue); // assuming long value gets casted to int with a loss of precision
 }
 });
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSelectedInt_whenComboBoxHasValueWhichCannotBeCastToInt_thenThrowIllegalArgumentException() {
 SwingUtilities.invokeLater(new Runnable() {
 @Override
 public void run() {
 comboBox.setSelectedValue(new Byte((byte)5));
 }
 });

 EventQueue.invokeLater(new Runnable() {
 @Override
 public void run() {
 assertThrows(IllegalArgumentException.class, new Runnable() {
 @Override
 public void run() {
 comboBox.getSelectedInt();
 }
 });
 }
 });
 }

 @Test
 public void testGetSelectedLong_whenComboBoxHasLongValue_thenReturnLongValue() {
 final long expectedValue = 10L;
 SwingUtilities.invokeLater(new Runnable() {
 @Override
 public void run() {
 comboBox.setSelectedValue(expectedValue);
 }
 });

 EventQueue.invokeLater(new Runnable() {
 @Override
 public void run() {
 final long actualValue = comboBox.getSelectedLong();
 assertEquals(expectedValue, actualValue);
 }
 });
 }

 @Test
 public void testGetSelectedLong_whenComboBoxHasValueWhichCannotBeCastToLong_thenReturnCastedLongValue() {
 final int expectedValue = 10;
 SwingUtilities.invokeLater(new Runnable() {
 @Override
 public void run() {
 comboBox.setSelectedValue(expectedValue);
 }
 });

 EventQueue.invokeLater(new Runnable() {
 @Override
 public void run() {
 final long actualValue = comboBox.getSelectedLong();
 assertEquals(expectedValue, actualValue);
 }
 });
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSelectedLong_whenComboBoxHasValueWhichCannotBeCastToLong_thenThrowIllegalArgumentException() {
 SwingUtilities.invokeLater(new Runnable() {
 @Override
 public void run() {
 comboBox.setSelectedValue('a');
 }
 });

 EventQueue.invokeLater(new Runnable() {
 @Override
 public void run() {
 assertThrows(IllegalArgumentException.class, new Runnable() {
 @Override
 public void run() {
 comboBox.getSelectedLong();
 }
 });
 }
 });
 }
}