package macaw.util;

import macaw.businessLayer.*;
import java.awt.*;
import macaw.system.*;
import javax.swing.*;
import java.util.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

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
 userInterfaceFactory = new MockUserInterfaceFactory();
 log = new MockLog();
 sessionProperties.setLog(log);
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test DisplayableListPanel", true);
 }

 @After
 public void tearDown() {
 displayableListPanel = null;
 sessionProperties = null;
 parentForm = null;
 userInterfaceFactory = null;
 log = null;
 }

 @Test
 public void testDisplayableListPanelConstructionWithAllowWriteAccess() {
 assertEquals("Test DisplayableListPanel", displayableListPanel.getPanelTitleText());
 assertTrue(displayableListPanel.isAllowWriteAccess());
 }

 @Test
 public void testDisplayableListPanelConstructionWithAllowWriteAccessAndAllowRecordEditing() {
 assertEquals("Test DisplayableListPanel", displayableListPanel.getPanelTitleText());
 assertTrue(displayableListPanel.isAllowWriteAccess());
 assertTrue(displayableListPanel.isAllowRecordEditing());
 }

 @Test
 public void testInit() {
 assertNotNull(displayableListPanel.getPanel());
 assertNotNull(displayableListPanel.select);
 assertNotNull(displayableListPanel.add);
 assertNotNull(displayableListPanel.edit);
 assertNotNull(displayableListPanel.view);
 assertNotNull(displayableListPanel.delete);
 }

 @Test
 public void testBuildUI() {
 assertNotNull(displayableListPanel.getPanel());
 assertNotNull(displayableListPanel.displayableList);
 }

 @Test
 public void testSetNewRecord() {
 displayableListPanel.setNewRecord(true);
 assertFalse(displayableListPanel.add.isEnabled());
 assertFalse(displayableListPanel.delete.isEnabled());
 assertFalse(displayableListPanel.edit.isEnabled());
 assertFalse(displayableListPanel.view.isEnabled());
 assertFalse(displayableListPanel.select.isEnabled());
 }

 @Test
 public void testSetNewRecordFalse() {
 displayableListPanel.setNewRecord(false);
 assertTrue(displayableListPanel.add.isEnabled());
 assertTrue(displayableListPanel.select.isEnabled());
 assertFalse(displayableListPanel.delete.isEnabled());
 assertFalse(displayableListPanel.edit.isEnabled());
 assertFalse(displayableListPanel.view.isEnabled());
 }

 @Test
 public void testSetDisplayableListItemSelector() {
 DisplayableListItemSelector mockListItemSelector = new MockDisplayableListItemSelector();
 displayableListPanel.setDisplayableListItemSelector(mockListItemSelector);
 assertSame(mockListItemSelector, displayableListPanel.listItemSelector);
 }

 @Test
 public void testSetDisplayableListItemAdder() {
 DisplayableListItemAdder mockListItemAdder = new MockDisplayableListItemAdder();
 displayableListPanel.setDisplayableListItemAdder(mockListItemAdder);
 assertSame(mockListItemAdder, displayableListPanel.listItemAdder);
 }

 @Test
 public void testSetDisplayableListItemEditor() {
 DisplayableListItemEditor mockListItemEditor = new MockDisplayableListItemEditor();
 displayableListPanel.setDisplayableListItemEditor(mockListItemEditor);
 assertSame(mockListItemEditor, displayableListPanel.listItemEditor);
 }

 @Test
 public void testSetDisplayableListItemViewer() {
 DisplayableListItemViewer mockListItemViewer = new MockDisplayableListItemViewer();
 displayableListPanel.setDisplayableListItemViewer(mockListItemViewer);
 assertSame(mockListItemViewer, displayableListPanel.listItemViewer);
 }

 @Test
 public void testSetDisplayableListItemDeleter() {
 DisplayableListItemDeleter mockListItemDeleter = new MockDisplayableListItemDeleter();
 displayableListPanel.setDisplayableListItemDeleter(mockListItemDeleter);
 assertSame(mockListItemDeleter, displayableListPanel.listItemDeleter);
 }

 @Test
 public void testAddListItem() {
 //TODO: test addListItem method
 }

 @Test
 public void testEditSelectedListItem() {
 //TODO: test editSelectedListItem method
 }

 @Test
 public void testViewSelectedListItem() {
 //TODO: test viewSelectedListItem method
 }

 @Test
 public void testDeleteSelectedListItems() {
 //TODO: test deleteSelectedListItems method
 }

 @Test
 public void testSelectListItems() {
 //TODO: test selectListItems method
 }

 @Test
 public void testUpdateButtonStates() {
 displayableListPanel.setNewRecord(false);
 displayableListPanel.addListItem();
 displayableListPanel.setDisplayableListItemSelector(new MockDisplayableListItemSelector());
 displayableListPanel.updateButtonStates();
 assertTrue(displayableListPanel.add.isEnabled());
 assertTrue(displayableListPanel.select.isEnabled());
 assertTrue(displayableListPanel.delete.isEnabled());
 assertTrue(displayableListPanel.edit.isEnabled());
 assertTrue(displayableListPanel.view.isEnabled());
 }

 @Test
 public void testSetDisplayableItems() {
 ArrayList<Displayable> masterList = new ArrayList<>();
 Displayable displayable = new Displayable();
 masterList.add(displayable);
 displayableListPanel.setDisplayableItems(masterList);
 assertEquals(masterList, displayableListPanel.displayableList.getAllItems());
 }

 @Test
 public void testSave() {
 ArrayList<Displayable> masterList = new ArrayList<>();
 Displayable displayable = new Displayable();
 masterList.add(displayable);
 displayableListPanel.setDisplayableItems(masterList);
 displayableListPanel.save();
 assertEquals(masterList, displayableListPanel.masterList);
 }

 @Test
 public void testGetAllListItems() {
 ArrayList<Displayable> allListItems = new ArrayList<>();
 Displayable displayable = new Displayable();
 allListItems.add(displayable);
 displayableListPanel.displayableList.setDisplayItems(allListItems);
 assertEquals(allListItems, displayableListPanel.getAllListItems());
 }

 @Test
 public void testSetListOwnerName() {
 String listOwnerName = "TestListOwnerName";
 displayableListPanel.setListOwnerName(listOwnerName);
 assertEquals(listOwnerName, displayableListPanel.listOwnerName);
 }

}