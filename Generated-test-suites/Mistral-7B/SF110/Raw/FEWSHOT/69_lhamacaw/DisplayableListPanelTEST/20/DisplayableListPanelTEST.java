package macaw.util;

import static org.junit.Assert.*;
import org.junit.Test;
import static macaw.util.MacawMessages.getMessage;

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
 private Log log;

 @Before
 public void setup() {
 sessionProperties = new SessionProperties();
 parentForm = new DisplayableListParentForm();
 userInterfaceFactory = new UserInterfaceFactory();
 log = new Log();
 sessionProperties.setLog(log);
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true);
 }

 @Test
 public void testDisplayableListPanelConstructionWithAllowWriteAccess() {
 assertEquals("Test Panel", displayableListPanel.getPanelTitleText());
 assertTrue(displayableListPanel.isNewRecord());
 assertNull(displayableListPanel.getListItemSelector());
 assertNull(displayableListPanel.getListItemAdder());
 assertNull(displayableListPanel.getListItemEditor());
 assertNull(displayableListPanel.getListItemViewer());
 assertNull(displayableListPanel.getListItemDeleter());
 assertNull(displayableListPanel.getDisplayableList());
 assertNull(displayableListPanel.getSelect());
 assertNull(displayableListPanel.getAdd());
 assertNull(displayableListPanel.getEdit());
 assertNull(displayableListPanel.getView());
 assertNull(displayableListPanel.getDelete());
 assertNull(displayableListPanel.getPanel());
 }

 @Test
 public void testDisplayableListPanelConstructionWithAllowWriteAccessAndAllowRecordEditing() {
 assertEquals("Test Panel", displayableListPanel.getPanelTitleText());
 assertTrue(displayableListPanel.isNewRecord());
 assertNull(displayableListPanel.getListItemSelector());
 assertNull(displayableListPanel.getListItemAdder());
 assertNull(displayableListPanel.getListItemEditor());
 assertNull(displayableListPanel.getListItemViewer());
 assertNull(displayableListPanel.getListItemDeleter());
 assertNull(displayableListPanel.getDisplayableList());
 assertNull(displayableListPanel.getSelect());
 assertNull(displayableListPanel.getAdd());
 assertNull(displayableListPanel.getEdit());
 assertNull(displayableListPanel.getView());
 assertNull(displayableListPanel.getDelete());
 assertNull(displayableListPanel.getPanel());
 }

 @Test
 public void testDisplayableListPanelInitialization() {
 DisplayableList displayableList = new DisplayableList(userInterfaceFactory);
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 assertEquals("Test Panel", displayableListPanel.getPanelTitleText());
 assertFalse(displayableListPanel.isNewRecord());
 assertNull(displayableListPanel.getListItemSelector());
 assertNull(displayableListPanel.getListItemAdder());
 assertNull(displayableListPanel.getListItemEditor());
 assertNull(displayableListPanel.getListItemViewer());
 assertNull(displayableListPanel.getListItemDeleter());
 assertSame(displayableList, displayableListPanel.getDisplayableList());
 assertNotNull(displayableListPanel.getSelect());
 assertNotNull(displayableListPanel.getAdd());
 assertNotNull(displayableListPanel.getEdit());
 assertNotNull(displayableListPanel.getView());
 assertNotNull(displayableListPanel.getDelete());
 assertNotNull(displayableListPanel.getPanel());
 }

 @Test
 public void testBuildUI() {
 JPanel panel = displayableListPanel.getPanel();
 assertEquals("Test Panel", panel.getName());
 assertTrue(panel.getComponentCount() > 0);
 }

 @Test
 public void testCreateControlButtonPanel() {
 JPanel controlButtonPanel = displayableListPanel.createControlButtonPanel();
 assertNotNull(controlButtonPanel);
 assertEquals(0, controlButtonPanel.getComponentCount());
 }

 @Test
 public void testSetNewRecord() {
 displayableListPanel.setNewRecord(false);
 assertFalse(displayableListPanel.isNewRecord());
 }

 @Test
 public void testGetPanel() {
 JPanel panel = displayableListPanel.getPanel();
 assertNotNull(panel);
 }

 @Test
 public void testSetDisplayableListItemSelector() {
 DisplayableListItemSelector listItemSelector = new DisplayableListItemSelector();
 displayableListPanel.setDisplayableListItemSelector(listItemSelector);
 assertEquals(listItemSelector, displayableListPanel.getListItemSelector());
 }

 @Test
 public void testSetDisplayableListItemAdder() {
 DisplayableListItemAdder listItemAdder = new DisplayableListItemAdder();
 displayableListPanel.setDisplayableListItemAdder(listItemAdder);
 assertEquals(listItemAdder, displayableListPanel.getListItemAdder());
 }

 @Test
 public void testSetDisplayableListItemEditor() {
 DisplayableListItemEditor listItemEditor = new DisplayableListItemEditor();
 displayableListPanel.setDisplayableListItemEditor(listItemEditor);
 assertEquals(listItemEditor, displayableListPanel.getListItemEditor());
 }

 @Test
 public void testSetDisplayableListItemViewer() {
 DisplayableListItemViewer listItemViewer = new DisplayableListItemViewer();
 displayableListPanel.setDisplayableListItemViewer(listItemViewer);
 assertEquals(listItemViewer, displayableListPanel.getListItemViewer());
 }

 @Test
 public void testSetDisplayableListItemDeleter() {
 DisplayableListItemDeleter listItemDeleter = new DisplayableListItemDeleter();
 displayableListPanel.setDisplayableListItemDeleter(listItemDeleter);
 assertEquals(listItemDeleter, displayableListPanel.getListItemDeleter());
 }

 @Test
 public void testAddListItem() {
 try {
 displayableListPanel.addListItem();
 fail("Expected MacawException to be thrown");
 } catch (MacawException e) {
 assertEquals(MacawErrorType.MISSING_DISPLAY_LIST_ADDER, e.getErrorType());
 }
 }

 @Test
 public void testEditSelectedListItem() {
 Displayable selectedListItem = new Displayable();
 displayableListPanel.setDisplayableListItemEditor(new DisplayableListItemEditor() {
 @Override
 public void editListItem(String listOwnerName, Displayable listItem, DisplayableList displayableList) {
 // Do nothing
 }
 });
 displayableListPanel.getDisplayableList().add(selectedListItem);
 displayableListPanel.editSelectedListItem();
 verify(displayableListPanel.getListItemEditor(), never()).editListItem(eq("Test Owner"), eq(selectedListItem), eq(displayableListPanel.getDisplayableList()));
 }

 @Test
 public void testViewSelectedListItem() {
 Displayable selectedListItem = new Displayable();
 displayableListPanel.getDisplayableList().add(selectedListItem);
 displayableListPanel.viewSelectedListItem();
 verify(displayableListPanel.getListItemViewer(), never()).viewListItem(selectedListItem);
 }

 @Test
 public void testDeleteSelectedListItems() {
 displayableListPanel.getDisplayableList().add(new Displayable());
 displayableListPanel.getDisplayableList().add(new Displayable());
 displayableListPanel.deleteSelectedListItems();
 verify(displayableListPanel.getListItemDeleter(), never()).deleteListItems(eq("Test Owner"), eq(displayableListPanel.getDisplayableList()));
 }

 @Test
 public void testSelectListItems() {
 displayableListPanel.getDisplayableList().add(new Displayable());
 displayableListPanel.getDisplayableList().add(new Displayable());
 displayableListPanel.selectListItems();
 verify(displayableListPanel.getListItemSelector(), never()).selectListItems(eq("Test Owner"), eq(displayableListPanel.getDisplayableList()));
 }

 @Test
 public void testUpdateButtonStates() {
 displayableListPanel.updateButtonStates();
 assertTrue(displayableListPanel.getSelect().isEnabled());
 assertTrue(displayableListPanel.getAdd().isEnabled());
 assertFalse(displayableListPanel.getEdit().isEnabled());
 assertFalse(displayableListPanel.getView().isEnabled());
 assertFalse(displayableListPanel.getDelete().isEnabled());
 }

 @Test
 public void testSetDisplayableItems() {
 ArrayList<Displayable> masterList = new ArrayList<>();
 masterList.add(new Displayable());
 displayableListPanel.setDisplayableItems(masterList);
 assertEquals(masterList, displayableListPanel.getDisplayableList().getAllItems());
 }

 @Test
 public void testSave() {
 ArrayList<Displayable> masterList = new ArrayList<>();
 masterList.add(new Displayable());
 displayableListPanel.setDisplayableItems(masterList);
 displayableListPanel.save();
 assertEquals(masterList, displayableListPanel.getDisplayableList().getAllItems());
 }

 @Test
 public void testGetAllListItems() {
 ArrayList<Displayable> allListItems = displayableListPanel.getAllListItems();
 assertNotNull(allListItems);
 assertTrue(allListItems.isEmpty());
 }

 @Test
 public void testSetListOwnerName() {
 displayableListPanel.setListOwnerName("Test Owner");
 assertEquals("Test Owner", displayableListPanel.getListOwnerName());
 }

 @Test
 public void testActionPerformed() {
 ActionEvent event = new ActionEvent(new Object(), 0, "test");
 displayableListPanel.actionPerformed(event);
 verify(displayableListPanel, never()).selectListItems();
 verify(displayableListPanel, never()).addListItem();
 verify(displayableListPanel, never()).editSelectedListItem();
 verify(displayableListPanel, never()).viewSelectedListItem();
 verify(displayableListPanel, never()).deleteSelectedListItems();
 }

 @Test
 public void testActionPerformedSelect() {
 ActionEvent event = new ActionEvent(displayableListPanel.getSelect(), 0, "test");
 displayableListPanel.actionPerformed(event);
 verify(displayableListPanel, times(1)).selectListItems();
 }

 @Test
 public void testActionPerformedAdd() {
 ActionEvent event = new ActionEvent(displayableListPanel.getAdd(), 0, "test");
 displayableListPanel.actionPerformed(event);
 try {
 displayableListPanel.addListItem();
 fail("Expected MacawException to be thrown");
 } catch (MacawException e) {
 assertEquals(MacawErrorType.MISSING_DISPLAY_LIST_ADDER, e.getErrorType());
 }
 }

 @Test
 public void testActionPerformedEdit() {
 Displayable selectedListItem = new Displayable();
 displayableListPanel.getDisplayableList().add(selectedListItem);
 ActionEvent event = new ActionEvent(displayableListPanel.getEdit(), 0, "test");
 displayableListPanel.actionPerformed(event);
 verify(displayableListPanel.getListItemEditor(), never()).editListItem(eq("Test Owner"), eq(selectedListItem), eq(displayableListPanel.getDisplayableList()));
 }

 @Test
 public void testActionPerformedView() {
 Displayable selectedListItem = new Displayable();
 displayableListPanel.getDisplayableList().add(selectedListItem);
 ActionEvent event = new ActionEvent(displayableListPanel.getView(), 0, "test");
 displayableListPanel.actionPerformed(event);
 verify(displayableListPanel.getListItemViewer(), never()).viewListItem(selectedListItem);
 }

 @Test
 public void testActionPerformedDelete() {
 Displayable selectedListItem = new Displayable();
 displayableListPanel.getDisplayableList().add(selectedListItem);
 ActionEvent event = new ActionEvent(displayableListPanel.getDelete(), 0, "test");
 displayableListPanel.actionPerformed(event);
 verify(displayableListPanel.getListItemDeleter(), never()).deleteListItems(eq("Test Owner"), eq(displayableListPanel.getDisplayableList()));
 }
}