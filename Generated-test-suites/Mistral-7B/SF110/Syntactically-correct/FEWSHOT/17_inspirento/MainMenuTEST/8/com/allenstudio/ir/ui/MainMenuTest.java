package com.allenstudio.ir.ui;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import com.allenstudio.ir.core.*;
import com.allenstudio.ir.util.*;
import org.junit.Test;
import java.awt.*;
import java.awt.event.*;
import java.net.*;

public class MainMenuTest {

    @Test
    public void testMainMenuConstruction() {
        InspirentoMediator mediator = new InspirentoMediator(); // Create a mock mediator
        MainMenu menu = new MainMenu(mediator);

        // Assert that the number of menus created corresponds to the number defined in the resource string
        assertEquals(11, menu.getMenuCount());
    }

    @Test
    public void testCreateMenu() {
        InspirentoMediator mediator = new InspirentoMediator();
        MainMenu menu = new MainMenu(mediator);

        String menuName = "File";
        JMenu createdMenu = menu.createMenu(menuName);

        // Assert that the created JMenu has the correct label
        assertEquals(UIResources.getString(menuName + LABEL_SUFFIX), createdMenu.getText());

        // Test the case when the menu label does not end with an asterisk
        String menuName2 = "Edit";
        JMenu createdMenu2 = menu.createMenu(menuName2);
        assertEquals(UIResources.getString(menuName2 + LABEL_SUFFIX), createdMenu2.getText());
    }

    @Test
    public void testAddShortcutAndIcon() {
        InspirentoMediator mediator = new InspirentoMediator();
        MainMenu menu = new MainMenu(mediator);

        String key = "File.Open";
        JMenuItem item = new JMenuItem("");
        menu.addShortcutAndIcon(item, key);

        // Test accelerator key
        assertEquals("Alt F", item.getAcceleratorKeyProperty());

        // Test icon
        String iconPath = "fileicon.png";
        assertEquals(UIResources.getString(key + ICON_SUFFIX), iconPath);

        URL url = menu.getClass().getResource(iconPath);
        assertNotNull(url);
    }

    @Test
    public void testCreateMenuWithCheckboxMenuItem() {
        InspirentoMediator mediator = new InspirentoMediator();
        MainMenu menu = new MainMenu(mediator);

        String key = "View.ShowGrid";
        JCheckBoxMenuItem checkboxMenuItem = new JCheckBoxMenuItem("");
        menu.addShortcutAndIcon(checkboxMenuItem, key);

        // Test accelerator key
        assertEquals("Ctrl G", checkboxMenuItem.getAcceleratorProperty().toString());

        // Test icon
        String iconPath = "gridicon.png";
        assertEquals(UIResources.getString(key + ICON_SUFFIX), iconPath);

        URL url = menu.getClass().getResource(iconPath);
        assertNotNull(url);

        // Test checkbox state
        checkboxMenuItem.setSelected(true);
        assertEquals(true, checkboxMenuItem.isSelected());
    }
}