package fr.pingtimeout.jtail.gui.view.test;

import static org.junit.Assert.*;
import fr.pingtimeout.jtail.gui.controller.*;
import fr.pingtimeout.jtail.gui.model.*;
import fr.pingtimeout.jtail.util.*;
import org.junit.*;
import org.junit.runner.*;
import javax.swing.*;
import javax.swing.table.*;

@RunWith(JUnit4.class)
public class JTailPanelTest {

 private JTailModel model;
 private JTailPanel jTailPanel;

 @Before
 public void setUp() {
 JTailLogger.setLevel(Level.FINEST);
 model = new JTailModel();
 jTailPanel = new JTailPanel(model);
 }

 @Test
 public void testConstructor() {
 assertEquals("Building a new JTailPanel", JTailLogger.getLastLogMessage());
 assertNotNull(jTailPanel.getVerticalScrollBar());
 assertNotNull(jTailPanel.getDataTable());
 }

 @Test
 public void testGetCharacterHeight() {
 int characterHeight = jTailPanel.getCharacterHeight();
 assertEquals("Character height should be defined", characterHeight, jTailPanel.characterHeight);
 }

 @Test
 public void testGetVerticalScrollBar() {
 JScrollBar verticalScrollBar = jTailPanel.getVerticalScrollBar();
 assertNotNull(verticalScrollBar);
 }

 @Test
 public void testUpdate() {
 model.firePropertyChange(JTailModel.PROPERTY_RESIZED, null, null);
 assertEquals("The window has beed resized", JTailLogger.getLastLogMessage());
 JScrollBar verticalScrollBar = jTailPanel.getVerticalScrollBar();
 assertEquals("The vertical scrollbar should have been updated", 0, verticalScrollBar.getValue());
 assertEquals("The vertical scrollbar should have been updated", model.getMinLine(), verticalScrollBar.getMinimum());
 assertEquals("The vertical scrollbar should have been updated", model.getFirstLine(), verticalScrollBar.getValueIsAdjusting());
 verticalScrollBar.setValue(model.getFirstLine() + 1);
 model.firePropertyChange(JTailModel.PROPERTY_SCROLLED, null, null);
 assertEquals("The user scrolled the file", JTailLogger.getLastLogMessage());
 assertEquals("The data table should have been updated", model.getContent(), jTailPanel.getDataTable().getValueAt(0, 1).toString());
 }

 @Test
 public void testUpdateDataTable() {
 String lineNumbers = "1\n2\n3\n4\n5";
 String lines = "one\ntwo\nthree\ntwo\nfour\nfive";
 model.setLineNumbers(lineNumbers);
 model.setContent(lines);
 jTailPanel.update(model, new JTailModelEvent(model, JTailModelEvent.Type.RESIZED, null));
 assertEquals("The data table should have the good number of rows", 5, jTailPanel.getDataTable().getRowCount());
 assertEquals("The data table should have the good content", lines, jTailPanel.getDataTable().getValueAt(0, 1).toString());
 assertEquals("The first column should have the good width", lineNumbers.split("\n")[lineNumbers.split("\n").length - 1].length() * jTailPanel.characterHeight * 10, jTailPanel.getDataTable().getColumnModel().getColumn(0).getPreferredWidth());
 assertEquals("The second column should have the good width", jTailPanel.getDataTable().getWidth() - jTailPanel.getDataTable().getColumnModel().getColumn(0).getPreferredWidth(), jTailPanel.getDataTable().getColumnModel().getColumn(1).getPreferredWidth());
 }

 @After
 public void tearDown() {
 model = null;
 jTailPanel = null;
 }
}