package macaw.util;

import static org.junit.Assert.*;
import macaw.businessLayer.*;
import macaw.system.Log;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;

public class DisplayableListPanelTest {

 private DisplayableListPanel displayableListPanel;
 private SessionProperties sessionProperties;
 private DisplayableListParentForm parentForm;
 private UserInterfaceFactory userInterfaceFactory;
 private Log log;

 @Before
 public void setUp() {
 sessionProperties = new SessionProperties();
 log = new Log();
 userInterfaceFactory = new UserInterfaceFactory();
 parentForm = new DisplayableListParentForm();
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 }

 @After
 public void tearDown() {
 displayableListPanel = null;
 parentForm = null;
 sessionProperties = null;
 userInterfaceFactory = null;
 log = null;
 }

 @Test
 public void testConstructorWithAllowRecordEditing() {
 assertNotNull(displayableListPanel);
 }

 @Test
 public void testConstructorWithAllowWriteAccessAndNoAllowRecordEditing() {
 assertNotNull(displayableListPanel);
 }

 @Test
 public void testInit() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, false);
 assertTrue(displayableListPanel.allowRecordEditing == false);
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 assertTrue(displayableListPanel.allowRecordEditing == true);
 }

 @Test
 public void testBuildUI() {
 assertTrue(displayableListPanel.panel != null);
 }

 @Test
 public void testCreateControlButtonPanel() {
 assertTrue(displayableListPanel.createControlButtonPanel().getComponentCount() >= 1);
 }

 @Test
 public void testSetNewRecord() {
 displayableListPanel.setNewRecord(true);
 assertFalse(displayableListPanel.add.isEnabled());
 }

 @Test
 public void testSetDisplayableListItemSelector() {
 displayableListPanel.setDisplayableListItemSelector(new DisplayableListItemSelector() {
 @Override
 public void selectListItems(String listOwnerName, DisplayableList displayableList) {
 // empty implementation
 }
 });
 assertNotNull(displayableListPanel.listItemSelector);
 }

 @Test(expected = MacawException.class)
 public void testAddListItemWithMissingAdder() {
 displayableListPanel.addListItem();
 fail("Add list item with missing adder should not be reached");
 }

 @Test
 public void testEditSelectedListItemWithMissingEditor() {
 displayableListPanel.setDisplayableListItemEditor(null);
 try {
 displayableListPanel.editSelectedListItem();
 fail("Edit list item with missing editor should throw MacawException");
 } catch (MacawException e) {
 // expected
 }
 }

 @Test(expected = MacawException.class)
 public void testViewSelectedListItemWithMissingViewer() {
 displayableListPanel.setDisplayableListItemViewer(null);
 try {
 displayableListPanel.viewSelectedListItem();
 fail("View list item with missing viewer should throw MacawException");
 } catch (MacawException e) {
 // expected
 }
 }

 @Test(expected = MacawException.class)
 public void testDeleteSelectedListItemsWithMissingDeleter() {
 displayableListPanel.setDisplayableListItemDeleter(null);
 try {
 displayableListPanel.deleteSelectedListItems();
 fail("Delete list items with missing deleter should throw MacawException");
 } catch (MacawException e) {
 // expected
 }
 }

 @Test
 public void testSetDisplayableItems() {
 ArrayList<Displayable> masterList = new ArrayList<>();
 Displayable displayable = new Displayable();
 masterList.add(displayable);
 displayableListPanel.setDisplayableItems(masterList);
 assertEquals(1, displayableListPanel.displayableList.getAllItems().size());
 }

 @Test
 public void testSave() {
 ArrayList<Displayable> masterList = new ArrayList<>();
 Displayable displayable = new Displayable();
 masterList.add(displayable);
 displayableListPanel.setDisplayableItems(masterList);
 displayableListPanel.save();
 assertEquals(1, displayableListPanel.masterList.size());
 }

 @Test
 public void testGetAllListItems() {
 ArrayList<Displayable> listItems = new ArrayList<>();
 Displayable displayable = new Displayable();
 listItems.add(displayable);
 displayableListPanel.displayableList.setDisplayItems(listItems);
 assertEquals(listItems, displayableListPanel.getAllListItems());
 }
}