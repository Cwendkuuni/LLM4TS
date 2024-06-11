package macaw.util;

import static org.junit.Assert.*;

import macaw.businessLayer.*;
import macaw.system.Log;
import org.junit.Before;
import org.junit.Test;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class DisplayableListPanelTest {

 private DisplayableListPanel displayableListPanel;
 private SessionProperties sessionProperties;
 private DisplayableListParentForm parentForm;
 private Log log;

 @Before
 public void setUp() {
 sessionProperties = new SessionProperties();
 parentForm = new DisplayableListParentForm();
 log = new Log();
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true);
 }

 @Test
 public void testConstructor() {
 assertNotNull(displayableListPanel);
 }

 @Test
 public void testSetNewRecord() {
 displayableListPanel.setNewRecord(true);
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
 }

 @Test
 public void testSetDisplayableListItemSelector() {
 DisplayableListItemSelector mockListItemSelector = new DisplayableListItemSelector() {};
 displayableListPanel.setDisplayableListItemSelector(mockListItemSelector);
 assertTrue(displayableListPanel.select.isEnabled());
 }

 @Test(expected = MacawException.class)
 public void testAddListItemMissingAdder() {
 displayableListPanel.addListItem();
 fail("Expected MacawException to be thrown when addListItem is called without an addListItemAdder");
 }

 @Test
 public void testEditSelectedListItemMissingEditor() {
 try {
 displayableListPanel.editSelectedListItem();
 fail("Expected MacawException to be thrown when editSelectedListItem is called without an editListItemEditor");
 }
 catch (MacawException e) {
 // Expected
 }
 }

 @Test(expected = MacawException.class)
 public void testViewSelectedListItemMissingViewer() {
 try {
 displayableListPanel.viewSelectedListItem();
 fail("Expected MacawException to be thrown when viewSelectedListItem is called without an viewListItemViewer");
 }
 catch (MacawException e) {
 // Expected
 }
 }

 @Test(expected = MacawException.class)
 public void testDeleteSelectedListItemsMissingDeleter() {
 try {
 displayableListPanel.deleteSelectedListItems();
 fail("Expected MacawException to be thrown when deleteSelectedListItems is called without an deleteListItemDeleter");
 }
 catch (MacawException e) {
 // Expected
 }
 }

 @Test
 public void testDeleteSelectedListItemsConfirmationDialog() {
 displayableListPanel.displayableList = new DisplayableList(new UserInterfaceFactory());
 final int itemsCount = 3;
 for (int i = 0; i < itemsCount; ++i) {
 displayableListPanel.displayableList.add(new Displayable());
 }
 displayableListPanel.displayableList.setSelectedIndex(0);
 displayableListPanel.deleteSelectedListItems();
 assertEquals(itemsCount - 1, displayableListPanel.displayableList.size());
}

 // Add more test cases for other methods as needed

}