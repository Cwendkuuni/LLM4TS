package fr.pingtimeout.jtail.gui.view.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.pingtimeout.jtail.gui.controller.ScrollBarAdjustmentController;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.gui.model.JTailModelEvent;
import fr.pingtimeout.jtail.gui.view.JTailPanel;
import fr.pingtimeout.jtail.util.JTailLogger;
import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;

class JTailPanelTest {

 private JTailModel mockModel;
 private JTailPanel jTailPanel;

 @BeforeEach
 public void setUp() {
 mockModel = mock(JTailModel.class);
 jTailPanel = new JTailPanel(mockModel);
 }

 @Test
 void testCharacterHeight() {
 assertEquals(13, jTailPanel.getCharacterHeight());
 }

 @Test
 void testUpdateWhenModelIsResized() {
 // Given
 final int maxLine = 100;
 final int nbDisplayedLines = 20;
 final int firstLine = 30;
 final int minLine = 15;
 final String lineNumbers = "30\n40\n50\n...\n" + maxLine;
 final String lines = "Line1\nLine2\nLine3\n..." + Arrays.repeat("Line" + maxLine, maxLine - 29);
 final JTailModelEvent event = new JTailModelEvent(mockModel, JTailModelEvent.Type.RESIZED, lineNumbers, lines);
 doNothing().when(mockModel).addObserver(any());

 // When
 jTailPanel.update(mockModel, event);

 // Then
 verify(mockModel, times(1)).addObserver(jTailPanel);
 TableModelEvent tableModelEvent = argThat(e -> e.getSource() == jTailPanel.tableModel);
 verify(jTailPanel.dataTable).setModel(argThat(m -> m == jTailPanel.tableModel));
 TableModelEvent tableModelEvent1 = argThat(e -> e.getFirstRow() == 0 && e.getLastRow() == maxLine);
 verify(jTailPanel.tableModel).fireTableDataChanged(TableModelEvent.ALL_COLUMNS, tableModelEvent1.getFirstRow(), tableModelEvent1.getLastRow());
 TableModelEvent tableModelEvent2 = argThat(e -> e.getColumnCount() > 0);
 verify(jTailPanel.tableModel).fireTableStructureChanged(TableModelEvent.HEADER_ROW, 0, 0);
 verify(mockModel, times(1)).removeObserver(jTailPanel);
 }

 @Test
 void testUpdateWhenModelIsScrolled() {
 // Given
 final int maxLine = 100;
 final int nbDisplayedLines = 20;
 final int firstLine = 30;
 final int minLine = 15;
 final String lineNumbers = "30\n40\n50\n...\n" + maxLine;
 final String lines = "Line1\nLine2\nLine3\n..." + Arrays.repeat("Line" + maxLine, maxLine - 29);
 final JTailModelEvent event = new JTailModelEvent(mockModel, JTailModelEvent.Type.SCROLLED, null, null);
 doNothing().when(mockModel).addObserver(any());

 // When
 jTailPanel.update(mockModel, event);

 // Then
 verify(mockModel, times(1)).addObserver(jTailPanel);
 TableModelEvent tableModelEvent = argThat(e -> e.getSource() == jTailPanel.tableModel);
 verify(jTailPanel.dataTable).setModel(argThat(m -> m == jTailPanel.tableModel));
 }
}