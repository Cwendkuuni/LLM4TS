package com.allenstudio.ir.ui;

import static org.junit.Assert.*;

import org.junit.Test;

import com.allenstudio.ir.core.InspirentoMediator;
import com.allenstudio.ir.ui.MainMenu;

public class MainMenuTest {
    

    @Test
    public void testMainMenuCreation() {
        String menuString = "File*Open>File*Save>File*Save As>File*Export>Help*About";
        MainMenu menu = new MainMenu(new InspirentoMediator());

        // Test menu creation with valid labels and mnemonics
        String[] menuLabels = InspirentoUtilities.tokenize(menuString);
        JMenu[] menus = new JMenu[menuLabels.length];
        for (int i = 0; i < menuLabels.length; i++) {
            String label = menuLabels[i];
            menus[i] = menu.createMenu(label);
            if (menus[i] != null) {
                assertEquals(label.substring(0, label.length() - 2), menus[i].getText());
                assertEquals(label.charAt(label.length() - 1), menus[i].getMnemonic());
            }
        }

        // Test creation of checkbox menu items
        String checkboxLabel = "Show Gridlines>Show Gridlines";
        JCheckBoxMenuItem checkboxMenuItem = (JCheckBoxMenuItem) menus[Arrays.asList(menuLabels).indexOf(checkboxLabel.substring(0, checkboxLabel.length() - 2))].getComponent(1);
        assertTrue(checkboxMenuItem instanceof JCheckBoxMenuItem);
        assertEquals(checkboxLabel.substring(0, checkboxLabel.length() - 2), checkboxMenuItem.getText());
        assertFalse(checkboxMenuItem.isSelected());

        // Test creation of regular menu items
        String regularLabel = "New Document";
        JMenuItem regularMenuItem = (JMenuItem) menus[Arrays.asList(menuLabels).indexOf(regularLabel)];
        assertTrue(regularMenuItem instanceof JMenuItem);
        assertEquals(regularLabel, regularMenuItem.getText());
        assertNull(regularMenuItem.getIcon());
        assertNull(regularMenuItem.getAccelerator());
    }

    
}