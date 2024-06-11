package fr.pingtimeout.jtail.gui.view;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import fr.pingtimeout.jtail.gui.controller.*;
import fr.pingtimeout.jtail.gui.model.*;
import fr.pingtimeout.jtail.util.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;

public class JTailPanelTest {

 private JTailPanel panel;
 private JTailModel model;
 private Observer observer;
 private JScrollPane scrollPane;
 private JScrollBar verticalScrollBar;
 private JTable dataTable;
 private DefaultTableModel tableModel;
 private int characterHeight;
 private int maxCharacterWidth;

 @Before
 public void setUp() {
 model = mock(JTailModel.class);
 observer = mock(Observer.class);
 scrollPane = mock(JScrollPane.class);
 verticalScrollBar = mock(JScrollBar.class);
 dataTable = mock(JTable.class);
 tableModel = mock(DefaultTableModel.class);
 when(model.getLineNumbers()).thenReturn("1\n2\n3\n4");
 when(model.getContent()).thenReturn("line1\nline2\nline3\nline4");
 panel = new JTailPanel(model);
 panel.addObserver(observer);
 panel.verticalScrollBar = verticalScrollBar;
 panel.tableModel = tableModel;
 panel.dataTable = dataTable;
 panel.characterHeight = characterHeight;
 panel.maxCharacterWidth = maxCharacterWidth;
 }

 @Test
 public void testJTailPanelConstruction() {
 assertEquals("Building a new JTailPanel", JTailLogger.getDebugLog().get(0));
 assertNotNull(panel.model);
 assertNotNull(panel.scrollPane);
 assertNotNull(panel.verticalScrollBar);
 assertNotNull(panel.dataTable);
 assertNotNull(panel.tableModel);
 }

 @Test
 public void testUpdate() {
 JTailModelEvent event = new JTailModelEvent(model, JTailModelEvent.Type.RESIZED, null);
 panel.update(model, event);
 assertEquals("JTailPanel just received a notification : Resized", JTailLogger.getDebugLog().get(0));
 assertEquals(1, panel.verticalScrollBar.getModel().getMinimum());
 assertEquals(1, panel.verticalScrollBar.getModel().getValue());
 assertEquals(1, panel.verticalScrollBar.getModel().getExtent());
 assertEquals(4, panel.verticalScrollBar.getModel().getMaximum());
 assertFalse(panel.verticalScrollBar.isEnabled());
 }

 @Test
 public void testUpdate2() {
 JTailModelEvent event = new JTailModelEvent(model, JTailModelEvent.Type.SCROLLED, null);
 panel.update(model, event);
 assertEquals("The user scrolled the file", JTailLogger.getDebugLog().get(0));
 }

 @Test
 public void testUpdateDataTable() {
 panel.updateDataTable(model.getLineNumbers(), model.getContent());
 assertEquals(4, panel.tableModel.getRowCount());
 }

 @Test
 public void testGetCharacterHeight() {
 assertEquals(this.characterHeight, panel.getCharacterHeight());
 }

 @Test
 public void testGetVerticalScrollBar() {
 assertEquals(this.verticalScrollBar, panel.getVerticalScrollBar());
 }
}