package fr.pingtimeout.jtail.gui.view.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import fr.pingtimeout.jtail.gui.controller.*;
import fr.pingtimeout.jtail.gui.model.*;
import fr.pingtimeout.jtail.util.*;

import javax.swing.*;
import javax.swing.table.*;
import java.util.*;

import org.junit.*;
import org.mockito.MockitoAnnotations;

public class JTailPanelTest {

 private JTailModel model;
 private JTailPanel panel;
 private DefaultTableModel tableModelSpy;

 @Before
 public void setUp() {
 JTailLogger.init(); // Initialize logger for testing
 model = mock(JTailModel.class);
 tableModelSpy = mock(DefaultTableModel.class);
 panel = new JTailPanel(model);
 panel.tableModel = tableModelSpy;
 }

 @Test
 public void testJTailPanelConstructor() {
 // Test that new instance of JTailPanel is created correctly
 assertNotNull(panel);
 // Test that model is added as observer
 verify(model).addObserver(panel);
 }

 @Test
 public void testUpdateWithEmptyModel() {
 // Given empty model
 model = new JTailModel();
 panel = new JTailPanel(model);
 // When model is updated
 model.fireNotification(new JTailModelEvent(JTailModelEvent.Type.RESIZED, 0, 0, 0, 0, new String[0], new String[0]));
 // Then data table is empty
 assertEquals(0, panel.dataTable.getRowCount());
 }

 @Test
 public void testUpdateWithValidModel() {
 // Given model with data
 model = new JTailModel("Test data\nLine 1\nLine 2\nLine 3", 3);
 panel = new JTailPanel(model);
 // When model is updated
 model.fireNotification(new JTailModelEvent(JTailModelEvent.Type.RESIZED, 0, 0, 3, 3, new String[]{"1", "2", "3"}, new String[]{"Test data\nLine 1", "Line 2", "Line 3"}));
 // Then data table contains the correct data
 assertEquals(3, panel.dataTable.getRowCount());
 assertEquals("1", panel.dataTable.getValueAt(0, 0).toString());
 assertEquals("Test data\nLine 1", panel.dataTable.getValueAt(0, 1).toString());
 assertEquals("2", panel.dataTable.getValueAt(1, 0).toString());
 assertEquals("Line 2", panel.dataTable.getValueAt(1, 1).toString());
 assertEquals("3", panel.dataTable.getValueAt(2, 0).toString());
 assertEquals("Line 3", panel.dataTable.getValueAt(2, 1).toString());
 }

 @Test
 public void testUpdateWithScrollBar() {
 // Given model with data and initial scroll position
 model = new JTailModel("Test data\nLine 1\nLine 2\nLine 3", 3);
 panel = new JTailPanel(model);
 panel.verticalScrollBar.setValue(1);
 // When model is updated
 model.fireNotification(new JTailModelEvent(JTailModelEvent.Type.RESIZED, 0, 0, 3, 3, new String[]{"1", "2", "3"}, new String[]{"Test data\nLine 1", "Line 2", "Line 3"}));
 // Then scroll bar value is updated accordingly
 assertEquals(1, panel.verticalScrollBar.getValue());
 }

 @Test
 public void testDisableScrollBarWhenAllLinesFit() {
 // Given model with data and all lines fitting in the display
 model = new JTailModel("Test data\nLine 1\nLine 2\nLine 3", 3);
 panel = new JTailPanel(model);
 // When model is updated
 model.fireNotification(new JTailModelEvent(JTailModelEvent.Type.RESIZED, 0, 0, 3, 3, new String[]{"1", "2", "3"}, new String[]{"Test data\nLine 1", "Line 2", "Line 3"}));
 // Then scroll bar is disabled
 assertFalse(panel.verticalScrollBar.isEnabled());
 }

 @After
 public void tearDown() {
 MockitoAnnotations.initMocks(this);
}

}