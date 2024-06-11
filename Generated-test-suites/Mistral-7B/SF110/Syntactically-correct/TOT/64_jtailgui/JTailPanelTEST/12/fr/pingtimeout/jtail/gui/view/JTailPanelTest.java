package fr.pingtimeout.jtail.gui.view.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import fr.pingtimeout.jtail.gui.controller.*;
import fr.pingtimeout.jtail.gui.model.*;
import fr.pingtimeout.jtail.gui.view.*;
import fr.pingtimeout.jtail.util.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;
import org.junit.*;
import org.mockito.MockitoAnnotations;

public class JTailPanelTest {

 private JTailModel model;
 private JTailPanel panel;

 @Before
 public void setup() {
 JTailLogger.debug("Setting up test environment");
 model = mock(JTailModel.class);
 panel = new JTailPanel(model);
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testConstructor() {
 JTailLogger.debug("Testing constructor");
 assertNotNull(panel.getVerticalScrollBar());
 assertNotNull(panel.getDataTable());
 assertNotNull(panel.getScrollPane());
 }

 @Test
 public void testUpdateMethodWithResizedEvent() {
 JTailLogger.debug("Testing update method with resized event");
 // Arrange
 final int firstLine = 1;
 final int nbDisplayedLines = 10;
 final int maxLine = 20;
 final int minLine = 5;
 when(model.getMinLine()).thenReturn(minLine);
 when(model.getFirstLine()).thenReturn(firstLine);
 when(model.getNbDisplayedLines()).thenReturn(nbDisplayedLines);
 when(model.getMaxLine()).thenReturn(maxLine);

 // Act
 panel.update(model, new JTailModelEvent(JTailModelEvent.Type.RESIZED));

 // Assert
 final JScrollBar verticalScrollBar = panel.getVerticalScrollBar();
 assertEquals(minLine, verticalScrollBar.getValue());
 assertEquals(firstLine, verticalScrollBar.getValuePosition());
 assertEquals(nbDisplayedLines, verticalScrollBar.getExtent());
 assertEquals(maxLine, verticalScrollBar.getMaximum());
 }

 @Test
 public void testUpdateMethodWithScrolledEvent() {
 JTailLogger.debug("Testing update method with scrolled event");
 // Arrange
 final String content = "Line 1\nLine 2\nLine 3\nLine 4";
 when(model.getContent()).thenReturn(content);

 // Act
 panel.update(model, new JTailModelEvent(JTailModelEvent.Type.SCROLLED));

 // Assert
 final JTable dataTable = panel.getDataTable();
 assertEquals("Line 1", dataTable.getValueAt(0, 1).toString());
 assertEquals("Line 2", dataTable.getValueAt(1, 1).toString());
 }

 @Test
 public void testUpdateDataTableMethod() {
 JTailLogger.debug("Testing updateDataTable method");
 // Arrange
 final String lineNumbers = "1\n2\n3\n4";
 final String lines = "Line 1\nLine 2\nLine 3\nLine 4";

 // Act
 panel.updateDataTable(lineNumbers, lines);

 // Assert
 final JTable dataTable = panel.getDataTable();
 assertEquals(4, dataTable.getRowCount());
 assertEquals("1", dataTable.getValueAt(0, 0).toString());
 assertEquals("Line 1", dataTable.getValueAt(0, 1).toString());
 assertEquals("2", dataTable.getValueAt(1, 0).toString());
 assertEquals("Line 2", dataTable.getValueAt(1, 1).toString());
 }

}