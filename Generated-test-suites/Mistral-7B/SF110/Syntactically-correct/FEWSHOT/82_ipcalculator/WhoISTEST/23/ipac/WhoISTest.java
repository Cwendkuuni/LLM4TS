package ipac.test;

import static org.junit.Assert.*;
import ipac.WhoIS;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import org.junit.Before;
import org.junit.Test;

public class WhoISTest {

 private WhoIS whoIS;

 @Before
 public void setup() {
 whoIS = new WhoIS();
 }

 @Test
 public void testQueryPanel() {
 JPanel queryPanel = whoIS.queryPanel();
 assertNotNull(queryPanel);
 assertEquals(3, queryPanel.getComponentCount());
 JButton queryButton = (JButton)queryPanel.getComponent(2);
 assertNotNull(queryButton);
 assertEquals("Query", queryButton.getText());
 }

 @Test
 public void testAdditionalQueriesPanel() {
 JPanel additionalQueriesPanel = whoIS.additionalQueriesPanel();
 assertNotNull(additionalQueriesPanel);
 assertEquals(2, additionalQueriesPanel.getComponentCount());
 JPanel lookupsPanel = (JPanel)additionalQueriesPanel.getComponent(0);
 assertNotNull(lookupsPanel);
 assertEquals(7, lookupsPanel.getComponentCount());
 JRadioButton lowercaseL = (JRadioButton)lookupsPanel.getComponent(0);
 assertNotNull(lowercaseL);
 assertEquals("1st level less specific", lowercaseL.getText());
 }

 @Test
 public void testMiscellaneousQueries() {
 JPanel miscPanel = whoIS.MiscellaneousQueries();
 assertNotNull(miscPanel);
 assertEquals(2, miscPanel.getComponentCount());
 JLabel miscLabel = (JLabel)miscPanel.getComponent(0);
 assertNotNull(miscLabel);
 assertEquals("Object Types", miscLabel.getText());
 JList miscList = (JList)miscPanel.getComponent(1);
 assertNotNull(miscList);
 assertEquals(16, miscList.getItemCount());
 }

 @Test
 public void testCreatePopupMenu() {
 JPopupMenu popupMenu = whoIS.popupMenu;
 assertNotNull(popupMenu);
 assertEquals(4, popupMenu.getComponentCount());
 JMenuItem copy = (JMenuItem)popupMenu.getComponent(1);
 assertNotNull(copy);
 assertEquals("Copy", copy.getText());
 }

 @Test
 public void testActionPerformed() {
 whoIS.actionPerformed(new ActionEvent(new Object(), 0, ""));
 assertFalse(whoIS.booleanD);
 }

 @Test
 public void testStartQuery() {
 String query = "example.com";
 whoIS.queryTextField.setText(query);
 whoIS.startQuery();
 // add assertions for checking the query is sent to the Whois server with correct options
 }
}