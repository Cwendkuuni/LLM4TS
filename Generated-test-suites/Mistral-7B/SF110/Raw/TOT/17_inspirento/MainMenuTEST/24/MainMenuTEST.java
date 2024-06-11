package com.allenstudio.ir.ui;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.allenstudio.ir.core.*;
import com.allenstudio.ir.util.*;
import org.junit.*;
import org.junit.runner.*;
import java.util.*;
import javax.swing.*;
import java.net.*;

@RunWith(MockitoJUnitRunner.class)
public class MainMenuTest {
    

    @Test
    public void testMainMenuConstruction() {
        String[] menus = InspirentoUtilities.tokenize(UIResources.getString("menu"));
        InspirentoMediator mediator = mock(InspirentoMediator.class);
        MainMenu mainMenu = new MainMenu(mediator);

        for (String menu : menus) {
            JMenu jMenu = mainMenu.createMenu(menu);
            assertNotNull(jMenu);
        }

        verify(mediator).setMenuBar(mainMenu);
    }

    @Test
    public void testCreateMenuWithLabel() {
        String menuLabel = "File";
        JMenu jMenu = new MainMenu("").createMenu(menuLabel);
        assertEquals(menuLabel, jMenu.getText());
    }

    @Test
    public void testCreateMenuWithLabelAndSubmenu() {
        String menuLabel = "Edit";
        String submenuLabel = "Undo";
        JMenu jMenu = new MainMenu("").createMenu(menuLabel);
        JMenu submenu = jMenu.add(new JMenu(submenuLabel));
        assertEquals(menuLabel, jMenu.getText());
        assertEquals(submenuLabel, submenu.getText());
    }

    @Test
    public void testCreateCheckboxMenuItem() {
        String itemKey = "Preferences";
        JCheckBoxMenuItem jMenuItem = new MainMenu("").createMenu("").addCheckBoxMenuItem(itemKey);
        assertEquals(UIResources.getString(itemKey + "Label"), jMenuItem.getText());
    }

    @Test
    public void testCreateMenuItem() {
        String itemKey = "Help";
        JMenuItem jMenuItem = new MainMenu("").createMenu("").addMenuItem(itemKey);
        assertEquals(UIResources.getString(itemKey + "Label"), jMenuItem.getText());
    }

    @Test
    public void testAddShortcutAndIcon() {
        String itemKey = "Preferences";
        String iconPath = "icon.png";
        String shortcut = "Ctrl+P";

        JMenuItem jMenuItem = new MainMenu("").createMenu("").addMenuItem(itemKey);
        new MainMenu("").addShortcutAndIcon(jMenuItem, itemKey);

        assertEquals(Short.parseShort(shortcut.charAt(0) + ""), jMenuItem.getAccelerator().getValue()[0]);
        assertEquals(Short.parseShort(shortcut.charAt(2) + ""), jMenuItem.getAccelerator().getValue()[1]);
        assertNotNull(jMenuItem.getIcon());

        try {
            URL url = new MainMenu("").getClass().getResource(iconPath);
            assertEquals(url.getFile(), jMenuItem.getIcon().getImage().toString());
        } catch (NullPointerException e) {
            fail("Icon file wasn't found.");
        }
    }

    
}