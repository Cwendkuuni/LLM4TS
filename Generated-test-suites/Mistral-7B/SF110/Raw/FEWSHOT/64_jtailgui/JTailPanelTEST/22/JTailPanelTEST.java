package fr.pingtimeout.jtail.gui.view;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import fr.pingtimeout.jtail.gui.controller.*;
import fr.pingtimeout.jtail.gui.model.*;
import fr.pingtimeout.jtail.util.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class JTailPanelTest {

 private JTailModel mockModel;
 private JTailPanel tailPanel;
 private ScrollBarAdjustmentController mockScrollBarController;
 private JTailSizeController mockSizeController;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 mockModel = mock(JTailModel.class);
 mockScrollBarController = mock(ScrollBarAdjustmentController.class);
 mockSizeController = mock(JTailSizeController.class);
 tailPanel = new JTailPanel(mockModel);
 }

 @Test
 public void shouldInitializeJTailPanel() {
 assertEquals("Building a new JTailPanel", JTailLogger.getDebugLog().get(0));
 assertNotNull(tailPanel.model);
 assertNotNull(tailPanel.scrollPane);
 assertNotNull(tailPanel.verticalScrollBar);
 assertNotNull(tailPanel.dataTable);
 assertNotNull(tailPanel.tableModel);
 assertNotNull(tailPanel.characterHeight);
 assertNull(tailPanel.maxCharacterWidth);
 assertSame(BorderLayout.CENTER, tailPanel.getLayout());
 }

 @Test
 public void shouldReturnCharacterHeight() {
 assertEquals(this.characterHeight, tailPanel.getCharacterHeight());
 }

 @Test
 public void shouldReturnVerticalScrollBar() {
 assertEquals(tailPanel.verticalScrollBar, tailPanel.getVerticalScrollBar());
 }

 @Test
 public void shouldUpdateTableModelWhenModelNotifiesObserver() {
 when(mockModel.getLineNumbers()).thenReturn("1\n2\n3");
 when(mockModel.getContent()).thenReturn("line1\nline2\nline3");
 tailPanel.update(mockModel, new JTailModelEvent(JTailModelEvent.Type.RESIZED, mockModel));
 verify(mockModel, times(1)).getLineNumbers();
 verify(mockModel, times(1)).getContent();
 assertEquals(3, tailPanel.tableModel.getRowCount());
 }

 @Test
 public void shouldSetEnabledScrollBarWhenFirstLineIsFirstDisplayedLine() {
 when(mockModel.getLineNumbers()).thenReturn("1\n1");
 when(mockModel.getContent()).thenReturn("line1\nline2");
 tailPanel.update(mockModel, new JTailModelEvent(JTailModelEvent.Type.RESIZED, mockModel));
 verify(mockScrollBarController).setEnabled(false);
 }

 @Test
 public void shouldSetEnabledScrollBarWhenFirstLineIsNotFirstDisplayedLine() {
 when(mockModel.getLineNumbers()).thenReturn("1\n2");
 when(mockModel.getContent()).thenReturn("line1\nline2");
 tailPanel.update(mockModel, new JTailModelEvent(JTailModelEvent.Type.RESIZED, mockModel));
 verify(mockScrollBarController).setEnabled(true);
 }

 @Test
 public void shouldUpdateTableColumnsWidths() {
 tailPanel.updateDataTable("1\n2\n3\n4", "line1\nline2\nline3\nline4");
 TableColumn numbersColumn = tailPanel.dataTable.getColumnModel().getColumn(0);
 TableColumn linesColumn = tailPanel.dataTable.getColumnModel().getColumn(1);
 assertEquals(20, numbersColumn.getWidth());
 assertEquals(tailPanel.dataTable.getWidth() - 20, linesColumn.getWidth());
 }
}