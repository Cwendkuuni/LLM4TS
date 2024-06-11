package macaw.util;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import macaw.businessLayer.*;
import java.awt.*;
import macaw.system.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.*;

public class DisplayableListPanelTest {

 private DisplayableListPanel displayableListPanel;
 private SessionProperties sessionProperties;
 private DisplayableListParentForm parentForm;
 private UserInterfaceFactory userInterfaceFactory;
 private DisplayableList displayableList;
 private JButton selectButton;
 private JButton addButton;
 private JButton editButton;
 private JButton viewButton;
 private JButton deleteButton;

 @Before
 public void setUp() {
 sessionProperties = new SessionProperties();
 parentForm = new DisplayableListParentForm();
 userInterfaceFactory = new UserInterfaceFactory();
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true);
 displayableList = displayableListPanel.displayableList;
 selectButton = displayableListPanel.select;
 addButton = displayableListPanel.add;
 editButton = displayableListPanel.edit;
 viewButton = displayableListPanel.view;
 deleteButton = displayableListPanel.delete;
 }

 @Test
 public void testInit() {
 assertEquals("Test Panel", displayableListPanel.panelTitleText);
 assertNotNull(displayableListPanel.sessionProperties);
 assertNotNull(displayableListPanel.log);
 assertNotNull(displayableListPanel.parentForm);
 assertNotNull(displayableListPanel.panel);
 assertNotNull(displayableListPanel.displayableList);
 assertNotNull(selectButton);
 assertNotNull(addButton);
 assertNotNull(editButton);
 assertNotNull(viewButton);
 assertNotNull(deleteButton);
 }

 @Test
 public void testSetDisplayableItems() {
 ArrayList<Displayable> masterList = new ArrayList<>();
 masterList.add(new Displayable("Item 1"));
 masterList.add(new Displayable("Item 2"));
 displayableListPanel.setDisplayableItems(masterList);
 assertEquals(2, displayableList.getModel().getSize());
 }

 @Test
 public void testSave() {
 displayableList.add(new Displayable("Test Item"));
 displayableListPanel.save();
 assertEquals(1, masterList.size());
 assertEquals("Test Item", masterList.get(0).getDisplayName());
 }

 @Test
 public void testGetAllListItems() {
 assertEquals(0, displayableList.getAllItems().size());
 displayableList.add(new Displayable("Test Item"));
 assertEquals(1, displayableList.getAllItems().size());
 }

 @Test
 public void testSetListOwnerName() {
 displayableListPanel.setListOwnerName("Test Owner");
 assertEquals("Test Owner", displayableListPanel.listOwnerName);
 }

 @Test
 public void testActionPerformed_Select() {
 displayableList.setSelectedIndex(0);
 selectButton.doClick();
 assertTrue(displayableList.itemsAreSelected());
 }

 @Test
 public void testActionPerformed_Add() {
 addButton.doClick();
 // Add method should be implemented in subclass
 fail("Add method not implemented");
 }

 @Test
 public void testActionPerformed_Edit() {
 Displayable displayable = new Displayable("Test Item");
 displayableList.add(displayable);
 displayableList.setSelectedIndex(0);
 editButton.doClick();
 // Edit method should be implemented in subclass
 fail("Edit method not implemented");
 }

 @Test
 public void testActionPerformed_View() {
 Displayable displayable = new Displayable("Test Item");
 displayableList.add(displayable);
 displayableList.setSelectedIndex(0);
 viewButton.doClick();
 // View method should be implemented in subclass
 fail("View method not implemented");
 }

 @Test
 public void testActionPerformed_Delete() {
 Displayable displayable = new Displayable("Test Item");
 displayableList.add(displayable);
 displayableList.setSelectedIndex(0);
 deleteButton.doClick();
 // Delete method should be implemented in subclass
 fail("Delete method not implemented");
 }
}