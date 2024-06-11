package macaw.util;

import static org.junit.Assert.*;

import macaw.businessLayer.*;
import macaw.system.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

import org.junit.Before;
import org.junit.Test;

public class DisplayableListPanelTest {

 private DisplayableListPanel displayableListPanel;
 private SessionProperties sessionProperties;
 private DisplayableListParentForm parentForm;
 private UserInterfaceFactory userInterfaceFactory;
 private Log log;

 @Before
 public void setUp() {
 sessionProperties = new SessionProperties();
 parentForm = new DisplayableListParentForm();
 userInterfaceFactory = new UserInterfaceFactory();
 log = new Log();
 sessionProperties.setLog(log);
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 }

 @Test
 public void testDisplayableListPanelConstructorWithAllowWriteAccess() {
 DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true);
 assertNotNull(displayableListPanel);
 }

 @Test
 public void testDisplayableListPanelConstructorWithAllowWriteAccessAndAllowRecordEditing() {
 DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 assertNotNull(displayableListPanel);
 }

 @Test
 public void testInitMethod() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 assertTrue(displayableListPanel.getPanel() != null);
 }

 @Test
 public void testSetDisplayableListItemSelector() {
 displayableListPanel.setDisplayableListItemSelector(new DisplayableListItemSelector() {
 @Override
 public void selectListItems(String listOwnerName, DisplayableList displayableList) {
 // implementation
 }
 });
 assertTrue(displayableListPanel.listItemSelector != null);
 }

 @Test(expected = MacawException.class)
 public void testAddListItemWithoutListItemAdder() {
 displayableListPanel.addListItem();
 fail("Add List Item should throw MacawException when listItemAdder is null");
 }

 @Test
 public void testEditSelectedListItemWithoutListItemEditor() {
 try {
 displayableListPanel.editSelectedListItem();
 fail("Edit Selected List Item should throw MacawException when listItemEditor is null");
 }
 catch (MacawException e) {
 // expected
 }
 }

 @Test(expected = MacawException.class)
 public void testViewSelectedListItemWithoutListItemViewer() {
 try {
 displayableListPanel.viewSelectedListItem();
 fail("View Selected List Item should throw MacawException when listItemViewer is null");
 }
 catch (MacawException e) {
 // expected
 }
 }

 @Test(expected = MacawException.class)
 public void testDeleteSelectedListItemsWithoutListItemDeleter() {
 try {
 displayableListPanel.deleteSelectedListItems();
 fail("Delete Selected List Items should throw MacawException when listItemDeleter is null");
 }
 catch (MacawException e) {
 // expected
 }
 }

 @Test
 public void testSetNewRecord() {
 displayableListPanel.setNewRecord(true);
 assertFalse(displayableListPanel.isNewRecord);
 displayableListPanel.setNewRecord(false);
 assertTrue(displayableListPanel.isNewRecord);
 }

 @Test(expected = MacawException.class)
 public void testSelectListItemsWithoutListItemSelector() {
 try {
 displayableListPanel.selectListItems();
 fail("Select List Items should throw MacawException when listItemSelector is null");
 }
 catch (MacawException e) {
 // expected
 }
 }

 @Test
 public void testUpdateButtonStates() {
 displayableListPanel.setDisplayableListItemSelector(new DisplayableListItemSelector() {
 @Override
 public void selectListItems(String listOwnerName, DisplayableList displayableList) {
 // implementation
 }
 });
 displayableListPanel.addListItem();
 List<Displayable> items = new ArrayList<>();
 Displayable displayable = new Displayable();
 items.add(displayable);
 displayableListPanel.setDisplayableList(new DisplayableList(userInterfaceFactory));
 displayableListPanel.setDisplayItems(items);
 displayableListPanel.updateButtonStates();
 assertFalse(displayableListPanel.add.isEnabled());
 assertFalse(displayableListPanel.delete.isEnabled());
 assertFalse(displayableListPanel.edit.isEnabled());
 assertFalse(displayableListPanel.view.isEnabled());
 assertFalse(displayableListPanel.select.isEnabled());
 displayableListPanel.setNewRecord(false);
 displayableListPanel.updateButtonStates();
 assertTrue(displayableListPanel.add.isEnabled());
 assertTrue(displayableListPanel.select.isEnabled());
 assertFalse(displayableListPanel.delete.isEnabled());
 assertFalse(displayableListPanel.edit.isEnabled());
 assertFalse(displayableListPanel.view.isEnabled());
 displayableListPanel.displayableList.addItem(displayable);
 displayableListPanel.updateButtonStates();
 assertTrue(displayableListPanel.delete.isEnabled());
 assertTrue(displayableListPanel.edit.isEnabled());
 assertTrue(displayableListPanel.view.isEnabled());
 }

 @Test
 public void testSetDisplayableItems() {
 List<Displayable> masterList = new ArrayList<>();
 Displayable displayable = new Displayable();
 masterList.add(displayable);
 displayableListPanel.setDisplayableItems(masterList);
 List<Displayable> items = displayableListPanel.displayableList.getAllItems();
 assertEquals(1, items.size());
 assertEquals(displayable, items.get(0));
 }

 @Test
 public void testSave() {
 displayableListPanel.save();
 List<Displayable> allItems = displayableListPanel.getAllListItems();
 assertFalse(displayableListPanel.masterList.isEmpty());
 assertEquals(displayableListPanel.masterList.size(), allItems.size());
 }
}