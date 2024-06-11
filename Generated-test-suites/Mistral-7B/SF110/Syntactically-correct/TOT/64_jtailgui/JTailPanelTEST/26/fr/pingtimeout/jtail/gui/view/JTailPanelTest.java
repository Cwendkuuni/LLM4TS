package fr.pingtimeout.jtail.gui.view.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import fr.pingtimeout.jtail.gui.controller.ScrollBarAdjustmentController;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.gui.model.JTailModelEvent;
import fr.pingtimeout.jtail.gui.view.JTailPanel;
import fr.pingtimeout.jtail.util.JTailLogger;
import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockitoAnnotations;

public class JTailPanelTest {

 private JTailModel model;
 private JTailPanel panel;
 private ScrollBarAdjustmentController scrollController;
 private JScrollBar scrollBar;

 @Before
 public void setUp() {
 JTailLogger.init();
 this.model = mock(JTailModel.class);
 this.scrollController = mock(ScrollBarAdjustmentController.class);
 this.scrollBar = mock(JScrollBar.class);

 this.panel = new JTailPanel(this.model);
 this.panel.verticalScrollBar = this.scrollBar;
 this.panel.addObserver(this.model);
 this.panel.verticalScrollBar.addAdjustmentListener(this.scrollController);
 }

 @Test
 public void testInitialization() {
 assertEquals("Building a new JTailPanel", JTailLogger.getLastMessage());
 assertNotNull(this.panel.getVerticalScrollBar());
 assertNotNull(this.panel.getDataTable());
 }

 @Test
 public void testUpdateWithResizedEvent() {
 // Given
 final int firstLine = 42;
 final int minLine = 0;
 final int maxLine = 10;
 final int nbDisplayedLines = 5;
 final JTailModelEvent event = new JTailModelEvent(this.model, JTailModelEvent.Type.RESIZED, firstLine, minLine, maxLine);

 when(this.model.getLineNumbers()).thenReturn("1\n2\n3\n4\n5\n6\n7\n8\n9\n10");
 when(this.model.getContent()).thenReturn("line1\nline2\nline3\nline4\nline5\nline6\nline7\nline8\nline9\nline10");

 // When
 this.panel.update(this.model, event);

 ArgumentCaptor<TableModelEvent> tableModelEventCaptor = ArgumentCaptor.forClass(TableModelEvent.class);
 verify(this.panel.getDataTable().getModel()).fireTableDataChanged(tableModelEventCaptor.capture());

 assertEquals(event.getFirstLine(), tableModelEventCaptor.getValue().getFirstIndex());
 assertEquals(event.getNbDisplayedLines(), tableModelEventCaptor.getValue().getLastIndex() - tableModelEventCaptor.getValue().getFirstIndex() + 1);
 }

 @Test
 public void testUpdateWithScrolledEvent() {
 // Given
 when(this.model.getLineNumbers()).thenReturn("1\n2\n3\n4\n5\n6\n7\n8\n9\n10");
 when(this.model.getContent()).thenReturn("line1\nline2\nline3\nline4\nline5\nline6\nline7\nline8\nline9\nline10");

 // When
 this.panel.update(this.model, new JTailModelEvent(this.model, JTailModelEvent.Type.SCROLLED, 2));

 // Then
 assertEquals("The user scrolled the file", JTailLogger.getLastMessage());
 }
}