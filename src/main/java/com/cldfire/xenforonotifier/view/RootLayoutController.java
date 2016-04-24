package com.cldfire.xenforonotifier.view;

import com.cldfire.xenforonotifier.XenForoNotifier;
import com.cldfire.xenforonotifier.util.LangUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ToggleGroup;

public class RootLayoutController {

    private XenForoNotifier xenForoNotifier;

    @FXML
    public Menu language;

    public void initialize() {
        ToggleGroup languageGroup = new ToggleGroup();
        for (LangUtils.Locale locale : LangUtils.Locale.values()) {
            RadioMenuItem radioMenuItem = new RadioMenuItem(locale.getName());
            radioMenuItem.setId(locale.name());
            radioMenuItem.setToggleGroup(languageGroup);
            language.getItems().add(radioMenuItem);
        }

        ((RadioMenuItem) language.getItems().get(0)).setSelected(true); // Hardcoded I know but so is the Enum.

        language.setOnAction(event -> {
            for (MenuItem menuItem : language.getItems()) {
                RadioMenuItem radioMenuItem = ((RadioMenuItem) menuItem);
                if (radioMenuItem.isSelected()) {
                    LangUtils.loadLocale(LangUtils.Locale.valueOf(radioMenuItem.getId())); // TODO: Reload all UI text, messages, etc. when language is changed
                }
            }
        });
    }

    public void setXenForoNotifier(XenForoNotifier xenForoNotifier) {
        this.xenForoNotifier = xenForoNotifier;
    }
}
