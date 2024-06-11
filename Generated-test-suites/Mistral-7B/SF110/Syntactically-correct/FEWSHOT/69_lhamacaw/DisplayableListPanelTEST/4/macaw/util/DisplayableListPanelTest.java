package macaw.util;

import static org.junit.Assert.*;
import org.junit.Test;
import macaw.businessLayer.*;
import macaw.system.*;
import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

public class DisplayableListPanelTest {

 private DisplayableListPanel panel;
 private DisplayableListParentForm parentForm;
 private SessionProperties sessionProperties;
 private Log log;

 @Before
 public void setUp() {
 sessionProperties = new SessionProperties();
 log = new Log();
 parentForm = mock(DisplayableListParentForm.class);
 panel = new DisplayableListPanel(sessionProperties, parentForm, "Panel Title", true);
 }

 @Test
 public void testDisplayableListPanelCreation() {
 assertEquals("Panel Title", panel.getPanelTitleText());
 assertNotNull(panel.getPanel());
 }

 @Test
 public void testAddListItem() {
 panel.setDisplayableListItemAdder(mock(DisplayableListItemAdder.class));
 panel.addListItem();
 assertFalse(panel.isNewRecord);
 }

 @Test(expected = MacawException.class)
 public void testAddListItemMissingAdder() {
 panel.addListItem();
 fail("An MacawException should be thrown when no adder is provided.");
 }

 @Test
 public void testEditSelectedListItem() {
 panel.setDisplayableListItemEditor(mock(DisplayableListItemEditor.class));
 panel.displayableList.setSelectedIndex(0);
 panel.editSelectedListItem();
 assertFalse(panel.isNewRecord);
 }

 @Test(expected = MacawException.class)
 public void testEditSelectedListItemMissingEditor() {
 panel.editSelectedListItem();
 fail("An MacawException should be thrown when no editor is provided.");
 }

 @Test
 public void testViewSelectedListItem() {
 panel.setDisplayableListItemViewer(mock(DisplayableListItemViewer.class));
 panel.displayableList.setSelectedIndex(0);
 panel.viewSelectedListItem();
 assertTrue(panel.isNewRecord);
 }

 @Test(expected = MacawException.class)
 public void testViewSelectedListItemMissingViewer() {
 panel.viewSelectedListItem();
 fail("An MacawException should be thrown when no viewer is provided.");
 }

 @Test
 public void testDeleteSelectedListItems() {
 panel.setDisplayableListItemDeleter(mock(DisplayableListItemDeleter.class));
 panel.displayableList.setSelectedIndices(new int[] { 0, 1 });
 panel.deleteSelectedListItems();
 assertFalse(panel.isNewRecord);
 }

 @Test(expected = MacawException.class)
 public void testDeleteSelectedListItemsMissingDeleter() {
 panel.deleteSelectedListItems();
 fail("An MacawException should be thrown when no deletion is provided.");
 }

 @Test
 public void testSelectListItems() {
 panel.setDisplayableListItemSelector(mock(DisplayableListItemSelector.class));
 panel.selectListItems();
 assertTrue(panel.displayableList.isSelected(0));
 }

 @Test(expected = MacawException.class)
 public void testSelectListItemsMissingSelector() {
 panel.selectListItems();
 fail("An MacawException should be thrown when no selector is provided.");
 }

 @Test
 public void testUpdateButtonStates() {
 panel.isNewRecord = true;
 panel.updateButtonStates();
 assertFalse(panel.add.isEnabled());
 assertFalse(panel.delete.isEnabled());
 assertFalse(panel.edit.isEnabled());
 assertFalse(panel.view.isEnabled());
 assertFalse(panel.select.isEnabled());

 panel.isNewRecord = false;
 panel.updateButtonStates();
 assertTrue(panel.add.isEnabled());
 assertTrue(panel.select.isEnabled());
 assertFalse(panel.delete.isEnabled());
 assertFalse(panel.edit.isEnabled());
 assertFalse(panel.view.isEnabled());

 panel.allowWriteAccess = false;
 panel.updateButtonStates();
 assertFalse(panel.add.isEnabled());
 assertFalse(panel.delete.isEnabled());
 assertFalse(panel.edit.isEnabled());
 assertFalse(panel.view.isEnabled());
 assertTrue(panel.select.isEnabled());
 }

 @Test
 public void testSetDisplayableItems() {
 ArrayList<Displayable> masterList = new ArrayList<>();
 masterList.add(new Displayable());
 DisplayableList displayableList = new DisplayableList();
 displayableList.setDisplayItems(masterList);
 panel.setDisplayableItems(masterList);
 assertEquals(masterList, panel.displayableList.getAllItems());
 }

 @Test
 public void testSave() {
 ArrayList<Displayable> masterList = new ArrayList<>();
 masterList.add(new Displayable());
 DisplayableList displayableList = new DisplayableList();
 displayableList.setDisplayItems(masterList);
 panel.displayableList = displayableList;
 panel.save();
 assertEquals(masterList, panel.masterList);
 }

 @Test
 public void testGetAllListItems() {
 ArrayList<Displayable> allListItems = panel.getAllListItems();
 assertNotNull(allListItems);
 assertEquals(0, allListItems.size());
 }

}