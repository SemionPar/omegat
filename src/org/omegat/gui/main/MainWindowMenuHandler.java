/**************************************************************************
 OmegaT - Computer Assisted Translation (CAT) tool 
          with fuzzy matching, translation memory, keyword search, 
          glossaries, and translation leveraging into updated projects.

 Copyright (C) 2000-2006 Keith Godfrey, Maxym Mykhalchuk, Henry Pijffers, 
                         Benjamin Siband, and Kim Bruning
               2007 Zoltan Bartko
               2008 Andrzej Sawula, Alex Buloichik
               2009 Didier Briel, Alex Buloichik
               2010 Wildrich Fourie, Didier Briel
               2012 Wildrich Fourie, Guido Leenders, Didier Briel
               2013 Zoltan Bartko, Didier Briel, Yu Tang
               2014 Aaron Madlon-Kay
               2015 Yu Tang, Aaron Madlon-Kay, Didier Briel
               Home page: http://www.omegat.org/
               Support center: http://groups.yahoo.com/group/OmegaT/

 This file is part of OmegaT.

 OmegaT is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 OmegaT is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 **************************************************************************/

package org.omegat.gui.main;

import java.awt.Component;
import java.awt.Desktop;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.io.File;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.text.JTextComponent;

import org.omegat.core.Core;
import org.omegat.core.CoreEvents;
import org.omegat.core.KnownException;
import org.omegat.core.data.IProject.FileInfo;
import org.omegat.core.data.SourceTextEntry;
import org.omegat.core.data.TMXEntry;
import org.omegat.core.matching.NearString;
import org.omegat.core.matching.NearString.MATCH_SOURCE;
import org.omegat.core.search.SearchMode;
import org.omegat.core.segmentation.SRX;
import org.omegat.core.segmentation.Segmenter;
import org.omegat.core.spellchecker.ISpellChecker;
import org.omegat.core.tagvalidation.ErrorReport;
import org.omegat.core.team2.gui.RepositoriesCredentialsController;
import org.omegat.filters2.master.FilterMaster;
import org.omegat.filters2.master.PluginUtils;
import org.omegat.gui.align.AlignFilePickerController;
import org.omegat.gui.dialogs.AboutDialog;
import org.omegat.gui.dialogs.AutotextAutoCompleterOptionsDialog;
import org.omegat.gui.dialogs.CharTableAutoCompleterOptionsDialog;
import org.omegat.gui.dialogs.CustomColorSelectionDialog;
import org.omegat.gui.dialogs.ExternalTMXMatchesDialog;
import org.omegat.gui.dialogs.FontSelectionDialog;
import org.omegat.gui.dialogs.GlossaryAutoCompleterOptionsDialog;
import org.omegat.gui.dialogs.GoToSegmentDialog;
import org.omegat.gui.dialogs.LastChangesDialog;
import org.omegat.gui.dialogs.LogDialog;
import org.omegat.gui.dialogs.SaveOptionsDialog;
import org.omegat.gui.dialogs.SpellcheckerConfigurationDialog;
import org.omegat.gui.dialogs.TagProcessingOptionsDialog;
import org.omegat.gui.dialogs.TeamOptionsDialog;
import org.omegat.gui.dialogs.UserPassDialog;
import org.omegat.gui.dialogs.ViewOptionsDialog;
import org.omegat.gui.dialogs.WorkflowOptionsDialog;
import org.omegat.gui.editor.EditorSettings;
import org.omegat.gui.editor.EditorUtils;
import org.omegat.gui.editor.IEditor;
import org.omegat.gui.editor.SegmentExportImport;
import org.omegat.gui.filters2.FiltersCustomizer;
import org.omegat.gui.search.SearchWindowController;
import org.omegat.gui.segmentation.SegmentationCustomizer;
import org.omegat.gui.stat.StatisticsWindow;
import org.omegat.help.Help;
import org.omegat.util.Language;
import org.omegat.util.Log;
import org.omegat.util.OStrings;
import org.omegat.util.Preferences;
import org.omegat.util.StaticUtils;
import org.omegat.util.StringUtil;
import org.omegat.util.TagUtil;
import org.omegat.util.TagUtil.Tag;

/**
 * Handler for main menu items.
 * 
 * @author Keith Godfrey
 * @author Benjamin Siband
 * @author Maxym Mykhalchuk
 * @author Kim Bruning
 * @author Henry Pijffers (henry.pijffers@saxnot.com)
 * @author Zoltan Bartko - bartkozoltan@bartkozoltan.com
 * @author Andrzej Sawula
 * @author Alex Buloichik (alex73mail@gmail.com)
 * @author Didier Briel
 * @author Wildrich Fourie
 * @author Yu Tang
 * @author Aaron Madlon-Kay
 */
public class MainWindowMenuHandler {
    private final MainWindow mainWindow;

    public MainWindowMenuHandler(final MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }

    /**
     * Create new project.
     */
    public void projectNewMenuItemActionPerformed() {
        ProjectUICommands.projectCreate();
    }

    /**
     * Create new team project.
     */
    public void projectTeamNewMenuItemActionPerformed() {
        ProjectUICommands.projectTeamCreate();
    }

    /**
     * Open project.
     */
    public void projectOpenMenuItemActionPerformed() {
        ProjectUICommands.projectOpen(null);
    }

    /**
     * Open MED project.
     */
    public void projectMedOpenMenuItemActionPerformed() {
        ProjectUICommands.projectOpenMED();
    }

    /**
     * Create MED project.
     */
    public void projectMedCreateMenuItemActionPerformed() {
        ProjectUICommands.projectCreateMED();
    }

    /**
     * Imports the file/files/folder into project's source files.
     */
    public void projectImportMenuItemActionPerformed() {
        ProjectUICommands.doPromptImportSourceFiles();
    }

    public void projectWikiImportMenuItemActionPerformed() {
        ProjectUICommands.doWikiImport();
    }

    public void projectReloadMenuItemActionPerformed() {
        ProjectUICommands.projectReload();
    }

    /**
     * Close project.
     */
    public void projectCloseMenuItemActionPerformed() {
        ProjectUICommands.projectClose();
    }

    /**
     * Save project.
     */
    public void projectSaveMenuItemActionPerformed() {
        ProjectUICommands.projectSave();
    }

    /**
     * Create translated documents.
     */
    public void projectCompileMenuItemActionPerformed() {
        if (Preferences.isPreference(Preferences.TAGS_VALID_REQUIRED)) {
            List<ErrorReport> stes = Core.getTagValidation().listInvalidTags();
            if (stes != null) {
                Core.getTagValidation().displayTagValidationErrors(stes, OStrings.getString("TF_MESSAGE_COMPILE"));
                return;
            }
        }

        ProjectUICommands.projectCompile();
    }

    /**
     * Create current translated document.
     */
    public void projectSingleCompileMenuItemActionPerformed() {
        String midName = Core.getEditor().getCurrentFile();
        if (StringUtil.isEmpty(midName)) {
            return;
        }

        String sourcePattern = StaticUtils.escapeNonRegex(midName);
        if (Preferences.isPreference(Preferences.TAGS_VALID_REQUIRED)) {
            List<ErrorReport> stes = Core.getTagValidation().listInvalidTags(sourcePattern);
            if (stes != null) {
                Core.getTagValidation().displayTagValidationErrors(stes, OStrings.getString("TF_MESSAGE_COMPILE"));
                return;
            }
        }

        ProjectUICommands.projectSingleCompile(sourcePattern);
    }

    /** Edits project's properties */
    public void projectEditMenuItemActionPerformed() {
        ProjectUICommands.projectEditProperties();
    }

    public void viewFileListMenuItemActionPerformed() {
        if (mainWindow.m_projWin == null) {
            mainWindow.menu.viewFileListMenuItem.setSelected(false);
            return;
        }

        mainWindow.m_projWin.setActive(!mainWindow.m_projWin.isActive());
    }

    public void projectAccessRootMenuItemActionPerformed() {
        if (!Core.getProject().isProjectLoaded()) {
            return;
        }
        String path = Core.getProject().getProjectProperties().getProjectRoot();
        openFile(new File(path));
    }

    public void projectAccessDictionaryMenuItemActionPerformed() {
        if (!Core.getProject().isProjectLoaded()) {
            return;
        }
        String path = Core.getProject().getProjectProperties().getDictRoot();
        openFile(new File(path));
    }

    public void projectAccessGlossaryMenuItemActionPerformed() {
        if (!Core.getProject().isProjectLoaded()) {
            return;
        }
        String path = Core.getProject().getProjectProperties().getGlossaryRoot();
        openFile(new File(path));
    }

    public void projectAccessSourceMenuItemActionPerformed() {
        if (!Core.getProject().isProjectLoaded()) {
            return;
        }
        String path = Core.getProject().getProjectProperties().getSourceRoot();
        openFile(new File(path));
    }

    public void projectAccessTargetMenuItemActionPerformed() {
        if (!Core.getProject().isProjectLoaded()) {
            return;
        }
        String path = Core.getProject().getProjectProperties().getTargetRoot();
        openFile(new File(path));
    }

    public void projectAccessTMMenuItemActionPerformed() {
        if (!Core.getProject().isProjectLoaded()) {
            return;
        }
        String path = Core.getProject().getProjectProperties().getTMRoot();
        openFile(new File(path));
    }

    public void projectAccessCurrentSourceDocumentMenuItemActionPerformed(int modifier) {
        if (!Core.getProject().isProjectLoaded()) {
            return;
        }
        String root = Core.getProject().getProjectProperties().getSourceRoot();
        String path = Core.getEditor().getCurrentFile();
        if (StringUtil.isEmpty(path)) {
            return;
        }
        File toOpen = new File(root, path);
        if ((modifier & Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()) != 0) {
            toOpen = toOpen.getParentFile();
        }
        openFile(toOpen);
    }

    public void projectAccessCurrentTargetDocumentMenuItemActionPerformed(int modifier) {
        if (!Core.getProject().isProjectLoaded()) {
            return;
        }
        String root = Core.getProject().getProjectProperties().getTargetRoot();
        String path = Core.getEditor().getCurrentTargetFile();
        if (StringUtil.isEmpty(path)) {
            return;
        }
        File toOpen = new File(root, path);
        if ((modifier & Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()) != 0) {
            toOpen = toOpen.getParentFile();
        }
        openFile(toOpen);
    }

    public void projectAccessWriteableGlossaryMenuItemActionPerformed(int modifier) {
        if (!Core.getProject().isProjectLoaded()) {
            return;
        }
        String path = Core.getProject().getProjectProperties().getWriteableGlossary();
        if (StringUtil.isEmpty(path)) {
            return;
        }
        File toOpen = new File(path);
        if ((modifier & Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()) != 0) {
            toOpen = toOpen.getParentFile();
        }
        openFile(toOpen);
    }

    private void openFile(File path) {
        try {
            path = path.getCanonicalFile(); // Normalize file name in case it is displayed
        } catch (Exception ex) {
            // Ignore
        }
        if (!path.exists()) {
            Core.getMainWindow().showStatusMessageRB("LFC_ERROR_FILE_DOESNT_EXIST", path);
            return;
        }
        try {
            Desktop.getDesktop().open(path);
        } catch (Exception ex) {
            Log.logErrorRB(ex, "RPF_ERROR");
            Core.getMainWindow().displayErrorRB(ex, "RPF_ERROR");
        }
    }

    /** Quits OmegaT */
    public void projectExitMenuItemActionPerformed() {
        boolean projectModified = false;
        if (Core.getProject().isProjectLoaded())
            projectModified = Core.getProject().isProjectModified();

        // RFE 1302358
        // Add Yes/No Warning before OmegaT quits
        if (projectModified || Preferences.isPreference(Preferences.ALWAYS_CONFIRM_QUIT)) {
            if (JOptionPane.YES_OPTION != JOptionPane.showConfirmDialog(mainWindow,
                    OStrings.getString("MW_QUIT_CONFIRM"), OStrings.getString("CONFIRM_DIALOG_TITLE"),
                    JOptionPane.YES_NO_OPTION)) {
                return;
            }
        }

        SegmentExportImport.flushExportedSegments();

        new SwingWorker<Object, Void>() {
            @Override
            protected Object doInBackground() throws Exception {
                if (Core.getProject().isProjectLoaded()) {
                    // Save the list of learned and ignore words
                    ISpellChecker sc = Core.getSpellChecker();
                    sc.saveWordLists();
                    try {
                        Core.getProject().saveProject();
                    } catch (KnownException ex) {
                        // hide exception on shutdown
                    }
                }

                CoreEvents.fireApplicationShutdown();

                PluginUtils.unloadPlugins();

                return null;
            }

            @Override
            protected void done() {
                try {
                    get();

                    MainWindowUI.saveScreenLayout(mainWindow);

                    Preferences.save();

                    System.exit(0);
                } catch (Exception ex) {
                    Log.logErrorRB(ex, "PP_ERROR_UNABLE_TO_READ_PROJECT_FILE");
                    Core.getMainWindow().displayErrorRB(ex, "PP_ERROR_UNABLE_TO_READ_PROJECT_FILE");
                }
            }
        }.execute();
    }

    public void editUndoMenuItemActionPerformed() {
        Component focused = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
        if (focused.equals(Core.getNotes())) {
            Core.getNotes().undo();
        } else {
            Core.getEditor().undo();
        }
    }

    public void editRedoMenuItemActionPerformed() {
        Component focused = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
        if (focused.equals(Core.getNotes())) {
            Core.getNotes().redo();
        } else {
            Core.getEditor().redo();
        }
    }

    public void editOverwriteTranslationMenuItemActionPerformed() {
        mainWindow.doRecycleTrans();
    }

    public void editInsertTranslationMenuItemActionPerformed() {
        mainWindow.doInsertTrans();
    }

    public void editOverwriteMachineTranslationMenuItemActionPerformed() {
        String tr = Core.getMachineTranslatePane().getDisplayedTranslation();
        if (tr == null) {
            Core.getMachineTranslatePane().forceLoad();
        } else if (!StringUtil.isEmpty(tr)) {
            Core.getEditor().replaceEditText(tr);
        }
    }

    /**
     * replaces entire edited segment text with a the source text of a segment at cursor position
     */
    public void editOverwriteSourceMenuItemActionPerformed() {
        if (!Core.getProject().isProjectLoaded())
            return;

        String toInsert = Core.getEditor().getCurrentEntry().getSrcText();
        if (Preferences.isPreference(Preferences.GLOSSARY_REPLACE_ON_INSERT)) {
            toInsert = EditorUtils.replaceGlossaryEntries(toInsert);
        }
        Core.getEditor().replaceEditText(toInsert);
    }

    /** inserts the source text of a segment at cursor position */
    public void editInsertSourceMenuItemActionPerformed() {
        if (!Core.getProject().isProjectLoaded())
            return;

        String toInsert = Core.getEditor().getCurrentEntry().getSrcText();
        if (Preferences.isPreference(Preferences.GLOSSARY_REPLACE_ON_INSERT)) {
            toInsert = EditorUtils.replaceGlossaryEntries(toInsert);
        }
        Core.getEditor().insertText(toInsert);
    }

    public void editExportSelectionMenuItemActionPerformed() {
        if (!Core.getProject().isProjectLoaded())
            return;

        String selection = Core.getEditor().getSelectedText();
        if (selection == null) {
            SourceTextEntry ste = Core.getEditor().getCurrentEntry();
            TMXEntry te = Core.getProject().getTranslationInfo(ste);
            if (te.isTranslated()) {
                selection = te.translation;
            } else {
                selection = ste.getSrcText();
            }
        }
        SegmentExportImport.exportCurrentSelection(selection);
    }

    public void editCreateGlossaryEntryMenuItemActionPerformed() {
        if (!Core.getProject().isProjectLoaded())
            return;

        Core.getGlossary().showCreateGlossaryEntryDialog(Core.getMainWindow().getApplicationFrame());
    }

    public void editFindInProjectMenuItemActionPerformed() {
        if (!Core.getProject().isProjectLoaded())
            return;

        SearchWindowController search = new SearchWindowController(SearchMode.SEARCH);
        mainWindow.addSearchWindow(search);

        search.makeVisible(getTrimmedSelectedTextInMainWindow());
    }

    void findInProjectReuseLastWindow() {
        if (!Core.getProject().isProjectLoaded()) {
            return;
        }

        List<SearchWindowController> windows = mainWindow.getSearchWindows();
        for (int i = windows.size() - 1; i >= 0; i--) {
            SearchWindowController swc = windows.get(i);
            if (swc.getMode() == SearchMode.SEARCH) {
                swc.makeVisible(getTrimmedSelectedTextInMainWindow());
                return;
            }
        }
        editFindInProjectMenuItemActionPerformed();
    }

    public void editReplaceInProjectMenuItemActionPerformed() {
        if (!Core.getProject().isProjectLoaded())
            return;

        SearchWindowController search = new SearchWindowController(SearchMode.REPLACE);
        mainWindow.addSearchWindow(search);

        search.makeVisible(getTrimmedSelectedTextInMainWindow());
    }

    private String getTrimmedSelectedTextInMainWindow() {
        String selection = null;
        Component component = mainWindow.getMostRecentFocusOwner();
        if (component instanceof JTextComponent) {
            selection = ((JTextComponent) component).getSelectedText();
            if (!StringUtil.isEmpty(selection)) {
                selection = EditorUtils.removeDirectionChars(selection);
                selection = selection.trim();
            }
        }
        return selection;
    }

    /** Set active match to #1. */
    public void editSelectFuzzy1MenuItemActionPerformed() {
        Core.getMatcher().setActiveMatch(0);
    }

    /** Set active match to #2. */
    public void editSelectFuzzy2MenuItemActionPerformed() {
        Core.getMatcher().setActiveMatch(1);
    }

    /** Set active match to #3. */
    public void editSelectFuzzy3MenuItemActionPerformed() {
        Core.getMatcher().setActiveMatch(2);
    }

    /** Set active match to #4. */
    public void editSelectFuzzy4MenuItemActionPerformed() {
        Core.getMatcher().setActiveMatch(3);
    }

    /** Set active match to #5. */
    public void editSelectFuzzy5MenuItemActionPerformed() {
        Core.getMatcher().setActiveMatch(4);
    }

    /** Set active match to the next one */
    public void editSelectFuzzyNextMenuItemActionPerformed() {
        Core.getMatcher().setNextActiveMatch();
    }
    
    /** Set active match to the previous one */
    public void editSelectFuzzyPrevMenuItemActionPerformed() {
        Core.getMatcher().setPrevActiveMatch();
    }

    public void insertCharsLRMActionPerformed() {
        Core.getEditor().insertText("\u200E");
    }

    public void insertCharsRLMActionPerformed() {
        Core.getEditor().insertText("\u200F");
    }

    public void insertCharsLREActionPerformed() {
        Core.getEditor().insertText("\u202A");
    }

    public void insertCharsRLEActionPerformed() {
        Core.getEditor().insertText("\u202B");
    }

    public void insertCharsPDFActionPerformed() {
        Core.getEditor().insertText("\u202C");
    }

    public void editMultipleDefaultActionPerformed() {
        Core.getEditor().setAlternateTranslationForCurrentEntry(false);
    }
    
    public void editMultipleAlternateActionPerformed() {
        Core.getEditor().setAlternateTranslationForCurrentEntry(true);
    }

    public void editRegisterUntranslatedMenuItemActionPerformed() {
        Core.getEditor().registerUntranslated();
    }

    public void editRegisterEmptyMenuItemActionPerformed() {
        Core.getEditor().registerEmptyTranslation();
    }

    public void editRegisterIdenticalMenuItemActionPerformed() {
        Core.getEditor().registerIdenticalTranslation();
    }

    public void cycleSwitchCaseMenuItemActionPerformed() {
        Core.getEditor().changeCase(IEditor.CHANGE_CASE_TO.CYCLE);
    }

    public void sentenceCaseMenuItemActionPerformed() {
        Core.getEditor().changeCase(IEditor.CHANGE_CASE_TO.SENTENCE);
    }

    public void titleCaseMenuItemActionPerformed() {
        Core.getEditor().changeCase(IEditor.CHANGE_CASE_TO.TITLE);
    }

    public void upperCaseMenuItemActionPerformed() {
        Core.getEditor().changeCase(IEditor.CHANGE_CASE_TO.UPPER);
    }

    public void lowerCaseMenuItemActionPerformed() {
        Core.getEditor().changeCase(IEditor.CHANGE_CASE_TO.LOWER);
    }

    public void gotoNextUntranslatedMenuItemActionPerformed() {
        Core.getEditor().nextUntranslatedEntry();
    }

    public void gotoNextUniqueMenuItemActionPerformed() {
        Core.getEditor().nextUniqueEntry();
    }
    
    public void gotoNextTranslatedMenuItemActionPerformed() {
        Core.getEditor().nextTranslatedEntry();
    }

    public void gotoNextSegmentMenuItemActionPerformed() {
        Core.getEditor().nextEntry();
    }

    public void gotoPreviousSegmentMenuItemActionPerformed() {
        Core.getEditor().prevEntry();
    }

    public void gotoNextNoteMenuItemActionPerformed() {
        Core.getEditor().nextEntryWithNote();
    }

    public void gotoPreviousNoteMenuItemActionPerformed() {
        Core.getEditor().prevEntryWithNote();
    }

    /**
     * Asks the user for a segment number and then displays the segment.
     */
    public void gotoSegmentMenuItemActionPerformed() {
        // Create a dialog for input
        GoToSegmentDialog dialog = new GoToSegmentDialog(mainWindow);
        dialog.setVisible(true);

        int jumpTo = dialog.getResult();
        
        if (jumpTo != -1) {
            Core.getEditor().gotoEntry(jumpTo);
        }
    }

    public void gotoHistoryBackMenuItemActionPerformed() {
        Core.getEditor().gotoHistoryBack();
    }

    public void gotoHistoryForwardMenuItemActionPerformed() {
        Core.getEditor().gotoHistoryForward();
    }
    
    public void gotoMatchSourceSegmentActionPerformed() {
        NearString ns = Core.getMatcher().getActiveMatch();
        if (ns != null && ns.comesFrom == MATCH_SOURCE.MEMORY) {
            Core.getEditor().gotoEntry(ns.source, ns.key);
        }
    }


    public void viewMarkTranslatedSegmentsCheckBoxMenuItemActionPerformed() {
        Core.getEditor().getSettings()
                .setMarkTranslated(mainWindow.menu.viewMarkTranslatedSegmentsCheckBoxMenuItem.isSelected());
    }

    public void viewMarkUntranslatedSegmentsCheckBoxMenuItemActionPerformed() {
        Core.getEditor()
                .getSettings()
                .setMarkUntranslated(
                        mainWindow.menu.viewMarkUntranslatedSegmentsCheckBoxMenuItem.isSelected());
    }

    public void viewDisplaySegmentSourceCheckBoxMenuItemActionPerformed() {
        Core.getEditor()
                .getSettings()
                .setDisplaySegmentSources(
                        mainWindow.menu.viewDisplaySegmentSourceCheckBoxMenuItem.isSelected());
    }

    public void viewMarkNonUniqueSegmentsCheckBoxMenuItemActionPerformed() {
        Core.getEditor()
                .getSettings()
                .setMarkNonUniqueSegments(
                        mainWindow.menu.viewMarkNonUniqueSegmentsCheckBoxMenuItem.isSelected());
    }

    public void viewMarkNotedSegmentsCheckBoxMenuItemActionPerformed(){
        Core.getEditor()
                .getSettings()
                .setMarkNotedSegments(
                        mainWindow.menu.viewMarkNotedSegmentsCheckBoxMenuItem.isSelected());
    }

    public void viewMarkNBSPCheckBoxMenuItemActionPerformed(){
        Core.getEditor()
                .getSettings()
                .setMarkNBSP(
                        mainWindow.menu.viewMarkNBSPCheckBoxMenuItem.isSelected());
    }
    public void viewMarkWhitespaceCheckBoxMenuItemActionPerformed(){
        Core.getEditor()
                .getSettings()
                .setMarkWhitespace(
                        mainWindow.menu.viewMarkWhitespaceCheckBoxMenuItem.isSelected());
    }
    public void viewMarkBidiCheckBoxMenuItemActionPerformed(){
        Core.getEditor()
                .getSettings()
                .setMarkBidi(
                        mainWindow.menu.viewMarkBidiCheckBoxMenuItem.isSelected());
    }

    public void viewMarkAutoPopulatedCheckBoxMenuItemActionPerformed() {
        Core.getEditor().getSettings()
                .setMarkAutoPopulated(mainWindow.menu.viewMarkAutoPopulatedCheckBoxMenuItem.isSelected());
    }
    
    public void viewMarkLanguageCheckerCheckBoxMenuItemActionPerformed() {
        Core.getEditor().getSettings()
                .setMarkLanguageChecker(mainWindow.menu.viewMarkLanguageCheckerCheckBoxMenuItem.isSelected());
    }

    public void viewMarkFontFallbackCheckBoxMenuItemActionPerformed() {
        Core.getEditor().getSettings()
                .setDoFontFallback(mainWindow.menu.viewMarkFontFallbackCheckBoxMenuItem.isSelected());
    }

    public void viewDisplayModificationInfoNoneRadioButtonMenuItemActionPerformed() {
        Core.getEditor().getSettings()
                .setDisplayModificationInfo(EditorSettings.DISPLAY_MODIFICATION_INFO_NONE);
    }

    public void viewDisplayModificationInfoSelectedRadioButtonMenuItemActionPerformed() {
        Core.getEditor().getSettings()
                .setDisplayModificationInfo(EditorSettings.DISPLAY_MODIFICATION_INFO_SELECTED);
    }

    public void viewDisplayModificationInfoAllRadioButtonMenuItemActionPerformed() {
        Core.getEditor().getSettings()
                .setDisplayModificationInfo(EditorSettings.DISPLAY_MODIFICATION_INFO_ALL);
    }

    public void toolsValidateTagsMenuItemActionPerformed() {
        Core.getTagValidation().displayTagValidationErrors(Core.getTagValidation().listInvalidTags(), null);
    }

    public void toolsSingleValidateTagsMenuItemActionPerformed() {
        String midName = Core.getEditor().getCurrentFile();
        List<ErrorReport> stes = null;
        if (!StringUtil.isEmpty(midName)) {
            String sourcePattern = StaticUtils.escapeNonRegex(midName);
            stes = Core.getTagValidation().listInvalidTags(sourcePattern);
        }
        Core.getTagValidation().displayTagValidationErrors(stes, null);
    }

    /**
     * Identify all the placeholders in the source text and automatically inserts them into the target text.
     */
    public void editTagPainterMenuItemActionPerformed() {
        // insert tags
        for (Tag tag : TagUtil.getAllTagsMissingFromTarget()) {
            Core.getEditor().insertTag(tag.tag);
        }
    }

    public void editTagNextMissedMenuItemActionPerformed() {
        // insert next tag
        List<Tag> tags = TagUtil.getAllTagsMissingFromTarget();
        if (tags.isEmpty()) {
            return;
        }
        Core.getEditor().insertTag(tags.get(0).tag);
    }

    public void toolsShowStatisticsStandardMenuItemActionPerformed() {
        new StatisticsWindow(Core.getMainWindow().getApplicationFrame(), StatisticsWindow.STAT_TYPE.STANDARD)
                .setVisible(true);
    }

    public void toolsShowStatisticsMatchesMenuItemActionPerformed() {
        new StatisticsWindow(Core.getMainWindow().getApplicationFrame(), StatisticsWindow.STAT_TYPE.MATCHES)
                .setVisible(true);
    }

    public void toolsShowStatisticsMatchesPerFileMenuItemActionPerformed() {
        new StatisticsWindow(Core.getMainWindow().getApplicationFrame(), StatisticsWindow.STAT_TYPE.MATCHES_PER_FILE)
                .setVisible(true);
    }

    public void toolsAlignFilesMenuItemActionPerformed() {
        AlignFilePickerController picker = new AlignFilePickerController();
        if (Core.getProject().isProjectLoaded()) {
            String srcRoot = Core.getProject().getProjectProperties().getSourceRoot();
            String curFile = Core.getEditor().getCurrentFile();
            if (curFile != null) {
                picker.setSourceFile(srcRoot + curFile);
            }
            picker.setSourceDefaultDir(srcRoot);
            picker.setDefaultSaveDir(Core.getProject().getProjectProperties().getTMRoot());
            picker.setSourceLanguage(Core.getProject().getProjectProperties().getSourceLanguage());
            picker.setTargetLanguage(Core.getProject().getProjectProperties().getTargetLanguage());
        } else {
            String srcLang = Preferences.getPreference(Preferences.SOURCE_LOCALE);
            if (!StringUtil.isEmpty(srcLang)) {
                picker.setSourceLanguage(new Language(srcLang));
            }
            String trgLang = Preferences.getPreference(Preferences.TARGET_LOCALE);
            if (!StringUtil.isEmpty(trgLang)) {
                picker.setTargetLanguage(new Language(trgLang));
            }
        }
        picker.show(mainWindow);
    }

    public void optionsTabAdvanceCheckBoxMenuItemActionPerformed() {
        Core.getEditor().getSettings()
                .setUseTabForAdvance(mainWindow.menu.optionsTabAdvanceCheckBoxMenuItem.isSelected());
    }

    public void optionsAlwaysConfirmQuitCheckBoxMenuItemActionPerformed() {
        Preferences.setPreference(Preferences.ALWAYS_CONFIRM_QUIT,
                mainWindow.menu.optionsAlwaysConfirmQuitCheckBoxMenuItem.isSelected());
    }

    public void optionsTransTipsEnableMenuItemActionPerformed() {
        Preferences.setPreference(Preferences.TRANSTIPS,
                mainWindow.menu.optionsTransTipsEnableMenuItem.isSelected());
    }

    public void optionsTransTipsExactMatchMenuItemActionPerformed() {
        Preferences.setPreference(Preferences.TRANSTIPS_EXACT_SEARCH,
                mainWindow.menu.optionsTransTipsExactMatchMenuItem.isSelected());
    }

    public void optionsAutoCompleteShowAutomaticallyItemActionPerformed() {
        Preferences.setPreference(Preferences.AC_SHOW_SUGGESTIONS_AUTOMATICALLY,
                mainWindow.menu.optionsAutoCompleteShowAutomaticallyItem.isSelected());
    }
    
    public void optionsAutoCompleteGlossaryMenuItemActionPerformed() {
        new GlossaryAutoCompleterOptionsDialog(mainWindow).setVisible(true);
    }

    public void optionsAutoCompleteAutoTextMenuItemActionPerformed() {
        new AutotextAutoCompleterOptionsDialog(mainWindow).setVisible(true);
    }

    public void optionsAutoCompleteCharTableMenuItemActionPerformed() {
        new CharTableAutoCompleterOptionsDialog(mainWindow).setVisible(true);
    }

    public void optionsAutoCompleteHistoryCompletionMenuItemActionPerformed() {
        Preferences.setPreference(Preferences.AC_HISTORY_COMPLETION_ENABLED,
                mainWindow.menu.optionsAutoCompleteHistoryCompletionMenuItem.isSelected());
    }

    public void optionsAutoCompleteHistoryPredictionMenuItemActionPerformed() {
        Preferences.setPreference(Preferences.AC_HISTORY_PREDICTION_ENABLED,
                mainWindow.menu.optionsAutoCompleteHistoryPredictionMenuItem.isSelected());
    }

    public void optionsMTAutoFetchCheckboxMenuItemActionPerformed() {
        boolean enabled = mainWindow.menu.optionsMTAutoFetchCheckboxMenuItem.isSelected();
        Preferences.setPreference(Preferences.MT_AUTO_FETCH, enabled);
        mainWindow.menu.optionsMTOnlyUntranslatedCheckboxMenuItem.setEnabled(enabled);
    }
    
    public void optionsMTOnlyUntranslatedCheckboxMenuItemActionPerformed() {
        Preferences.setPreference(Preferences.MT_ONLY_UNTRANSLATED,
                mainWindow.menu.optionsMTOnlyUntranslatedCheckboxMenuItem.isSelected());
    }
    
    public void optionsGlossaryTBXDisplayContextCheckBoxMenuItemActionPerformed() {
        Preferences.setPreference(Preferences.GLOSSARY_TBX_DISPLAY_CONTEXT,
                mainWindow.menu.optionsGlossaryTBXDisplayContextCheckBoxMenuItem.isSelected());
        Preferences.save();

        Core.getGlossaryManager().forceReloadTBX();
    }
    
    public void optionsGlossaryExactMatchCheckBoxMenuItemActionPerformed() {
        Preferences.setPreference(Preferences.GLOSSARY_NOT_EXACT_MATCH,
                mainWindow.menu.optionsGlossaryExactMatchCheckBoxMenuItem.isSelected());
        Preferences.save();
        Core.getGlossaryManager().forceUpdateGlossary();

    }
    
    public void optionsGlossaryStemmingCheckBoxMenuItemActionPerformed() {
        Preferences.setPreference(Preferences.GLOSSARY_STEMMING,
                mainWindow.menu.optionsGlossaryStemmingCheckBoxMenuItem.isSelected());
        Preferences.save();
        Core.getGlossaryManager().forceUpdateGlossary();

    }

    public void optionsGlossaryReplacementCheckBoxMenuItemActionPerformed() {
        Preferences.setPreference(Preferences.GLOSSARY_REPLACE_ON_INSERT,
                mainWindow.menu.optionsGlossaryReplacementCheckBoxMenuItem.isSelected());
    }
    
    public void optionsDictionaryFuzzyMatchingCheckBoxMenuItemActionPerformed() {
        Preferences.setPreference(Preferences.DICTIONARY_FUZZY_MATCHING,
                mainWindow.menu.optionsDictionaryFuzzyMatchingCheckBoxMenuItem.isSelected());
        Preferences.save();
        Core.getDictionaries().refresh();
    }

    /**
     * Displays the font dialog to allow selecting the font for source, target text (in main window) and for
     * match and glossary windows.
     */
    public void optionsFontSelectionMenuItemActionPerformed() {
        FontSelectionDialog dlg = new FontSelectionDialog(Core.getMainWindow().getApplicationFrame(), Core
                .getMainWindow().getApplicationFont());
        dlg.setVisible(true);
        if (dlg.getReturnStatus() == FontSelectionDialog.RET_OK_CHANGED) {
            mainWindow.setApplicationFont(dlg.getSelectedFont());
        }
    }

    /**
     * Displays the color dialog selection. It needs an application restart after color changes.
     */
    public void optionsColorsSelectionMenuItemActionPerformed() {
        CustomColorSelectionDialog dlg = new CustomColorSelectionDialog(Core.getMainWindow().getApplicationFrame());
        dlg.setVisible(true);
    }

    
    /**
     * Displays the filters setup dialog to allow customizing file filters in detail.
     */
    public void optionsSetupFileFiltersMenuItemActionPerformed() {
        FiltersCustomizer dlg = new FiltersCustomizer(mainWindow, false,
                FilterMaster.createDefaultFiltersConfig(),
                Preferences.getFilters(), null);
        if (Core.getProject().isProjectLoaded()) {
            // Don't highlight project in-use filters on global view if project
            // has project-specific filters
            if (Core.getProject().getProjectProperties().getProjectFilters() == null) {
                dlg.setInUseFilters(FileInfo.getFilterNames(Core.getProject().getProjectFiles()));
            }
        }
        dlg.setVisible(true);
        if (dlg.getReturnStatus() == FiltersCustomizer.RET_OK) {
            // saving config
            Core.setFilterMaster(new FilterMaster(dlg.result));
            Preferences.setFilters(dlg.result);
        }
    }

    /**
     * Displays the segmentation setup dialog to allow customizing the segmentation rules in detail.
     */
    public void optionsSentsegMenuItemActionPerformed() {
        SegmentationCustomizer segment_window = new SegmentationCustomizer(mainWindow, false, SRX.getDefault(),
                Preferences.getSRX(), null);
        segment_window.setVisible(true);

        if (segment_window.getReturnStatus() == SegmentationCustomizer.RET_OK) {
            Core.setSegmenter(new Segmenter(segment_window.getSRX()));
            Preferences.setSRX(segment_window.getSRX());
        }
    }

    /**
     * Opens the spell checking window
     */
    public void optionsSpellCheckMenuItemActionPerformed() {
        Language currentLanguage;
        if (Core.getProject().isProjectLoaded()) {
            currentLanguage = Core.getProject().getProjectProperties().getTargetLanguage();
        } else {
            currentLanguage = new Language(Preferences.getPreference(Preferences.TARGET_LOCALE));
        }
        SpellcheckerConfigurationDialog sd = new SpellcheckerConfigurationDialog(mainWindow, currentLanguage);

        sd.setVisible(true);

        if (sd.getReturnStatus() == SpellcheckerConfigurationDialog.RET_OK) {
            boolean isNeedToSpell = Preferences.isPreference(Preferences.ALLOW_AUTO_SPELLCHECKING);
            if (isNeedToSpell && Core.getProject().isProjectLoaded()) {
                ISpellChecker sc = Core.getSpellChecker();
                sc.destroy();
                sc.initialize();
            }
            Core.getEditor().getSettings().setAutoSpellChecking(isNeedToSpell);
        }
    }

    /**
     * Displays the workflow setup dialog to allow customizing the diverse workflow options.
     */
    public void optionsWorkflowMenuItemActionPerformed() {
        new WorkflowOptionsDialog(mainWindow).setVisible(true);
    }

    /**
     * Displays the tag validation setup dialog to allow customizing the diverse tag validation options.
     */
    public void optionsTagValidationMenuItemActionPerformed() {
        TagProcessingOptionsDialog tagProcessingOptionsDialog = new TagProcessingOptionsDialog(mainWindow);
        tagProcessingOptionsDialog.setVisible(true);
        
        if (tagProcessingOptionsDialog.getReturnStatus() == TagProcessingOptionsDialog.RET_OK
                && Core.getProject().isProjectLoaded()) {
            // Redisplay according to new view settings
            Core.getEditor().getSettings().updateTagValidationPreferences();
            ProjectUICommands.promptReload();
        }
    }

    /**
     * Displays the team options dialog to allow customizing the diverse team options.
     */
    public void optionsTeamMenuItemActionPerformed() {
        new TeamOptionsDialog(mainWindow).setVisible(true);
    }

    /**
     * Displays the external TMX dialog to allow customizing the external TMX options.
     */
    public void optionsExtTMXMenuItemActionPerformed() {

        ExternalTMXMatchesDialog externalTMXOptions = new ExternalTMXMatchesDialog(mainWindow);
        externalTMXOptions.setVisible(true);

        if (externalTMXOptions.getReturnStatus() == ExternalTMXMatchesDialog.RET_OK
                && Core.getProject().isProjectLoaded()) {
            ProjectUICommands.promptReload();
        }
    }

    /**
     * Displays the view options dialog to allow customizing the view options.
     */
    public void optionsViewOptionsMenuItemActionPerformed() {

        ViewOptionsDialog viewOptions = new ViewOptionsDialog(mainWindow);
        viewOptions.setVisible(true);

        if (viewOptions.getReturnStatus() == ViewOptionsDialog.RET_OK
                && Core.getProject().isProjectLoaded()) {
            // Redisplay according to new view settings
            Core.getEditor().getSettings().updateViewPreferences();
        }

    }

   /**
    * Display the save options dialog to allow setting the save interval
    */
    public void optionsSaveOptionsMenuItemActionPerformed() {
        SaveOptionsDialog saveOptions = new SaveOptionsDialog(mainWindow);
        saveOptions.setVisible(true);
    }

    /**
     * Restores defaults for all dockable parts. May be expanded in the future to reset the entire GUI to its
     * defaults.
     */
    public void optionsRestoreGUIMenuItemActionPerformed() {
        MainWindowUI.resetDesktopLayout(mainWindow);
    }

    public void optionsAccessConfigDirMenuItemActionPerformed() {
        openFile(new File(StaticUtils.getConfigDir()));
    }

    public void optionsRepositoriesCredentialsItemActionPerformed() {
        RepositoriesCredentialsController.show();
    }

    /**
     * Show help.
     */
    public void helpContentsMenuItemActionPerformed() {
        try {
            Help.showHelp();
        } catch (Exception ex) {
            Log.log(ex);
        }
    }

    /**
     * Shows About dialog
     */
    public void helpAboutMenuItemActionPerformed() {
        new AboutDialog(mainWindow).setVisible(true);
    }

    /**
     * Shows Last changes
     */
    public void helpLastChangesMenuItemActionPerformed() {
        new LastChangesDialog(mainWindow).setVisible(true);
    }

    /**
     * Show log
     */
    public void helpLogMenuItemActionPerformed() {
        new LogDialog(mainWindow).setVisible(true);
    }
    
    /**
     * Displays the dialog to set login and password for proxy.
     */
    public void optionsViewOptionsMenuLoginItemActionPerformed() {
        UserPassDialog proxyOptions = new UserPassDialog(mainWindow);

        String encodedUser = Preferences.getPreference(Preferences.PROXY_USER_NAME);
        String encodedPassword = Preferences.getPreference(Preferences.PROXY_PASSWORD);

        try {
            proxyOptions.userText.setText(new String(StringUtil.decodeBase64(encodedUser)));
            proxyOptions.passwordField.setText(new String(StringUtil.decodeBase64(encodedPassword)));
        } catch (IllegalArgumentException ex) {
            Log.logErrorRB("LOG_DECODING_ERROR");
            Log.log(ex);
        }

        proxyOptions.setVisible(true);

        if (proxyOptions.getReturnStatus() == UserPassDialog.RET_OK) {
            encodedUser = StringUtil.encodeBase64(proxyOptions.userText.getText().getBytes());
            encodedPassword = StringUtil.encodeBase64(new String(proxyOptions.passwordField.getPassword()).getBytes());

            Preferences.setPreference(Preferences.PROXY_USER_NAME, encodedUser);
            Preferences.setPreference(Preferences.PROXY_PASSWORD, encodedPassword);
        }
    }
}
