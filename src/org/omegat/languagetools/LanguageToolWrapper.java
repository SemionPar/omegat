/**************************************************************************
 OmegaT - Computer Assisted Translation (CAT) tool 
          with fuzzy matching, translation memory, keyword search, 
          glossaries, and translation leveraging into updated projects.

 Copyright (C) 2010-2013 Alex Buloichik
               2015 Aaron Madlon-Kay
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

package org.omegat.languagetools;

import java.util.ArrayList;
import java.util.List;

import javax.swing.text.Highlighter.HighlightPainter;

import org.languagetool.JLanguageTool;
import org.languagetool.Language;
import org.languagetool.Languages;
import org.languagetool.rules.Rule;
import org.languagetool.rules.RuleMatch;
import org.languagetool.rules.bitext.BitextRule;
import org.languagetool.rules.bitext.DifferentLengthRule;
import org.languagetool.rules.bitext.DifferentPunctuationRule;
import org.languagetool.rules.bitext.SameTranslationRule;
import org.languagetool.rules.spelling.SpellingCheckRule;
import org.languagetool.tools.Tools;
import org.omegat.core.Core;
import org.omegat.core.CoreEvents;
import org.omegat.core.data.SourceTextEntry;
import org.omegat.core.events.IProjectEventListener;
import org.omegat.gui.editor.UnderlineFactory;
import org.omegat.gui.editor.mark.IMarker;
import org.omegat.gui.editor.mark.Mark;
import org.omegat.util.Log;
import org.omegat.util.StringUtil;
import org.omegat.util.gui.Styles;

/**
 * Marker implementation for LanguageTool support.
 * 
 * Bilingual check described <a href=
 * "http://languagetool.wikidot.com/checking-translations-bilingual-texts">here
 * </a>
 * 
 * @author Alex Buloichik (alex73mail@gmail.com)
 * @author Aaron Madlon-Kay
 */
public class LanguageToolWrapper implements IMarker, IProjectEventListener {
    protected static final HighlightPainter PAINTER = new UnderlineFactory.WaveUnderline(Styles.EditorColor.COLOR_LANGUAGE_TOOLS.getColor());

    private JLanguageTool sourceLt, targetLt;
    private List<BitextRule> bRules;

    public LanguageToolWrapper() throws Exception {
        CoreEvents.registerProjectChangeListener(this);
    }

    public boolean isEnabled() {
        return Core.getEditor().getSettings().isMarkLanguageChecker();
    }

    public synchronized void onProjectChanged(PROJECT_CHANGE_TYPE eventType) {
        switch (eventType) {
        case CREATE:
        case LOAD:
            Language sourceLang = getLTLanguage(Core.getProject().getProjectProperties().getSourceLanguage());
            Language targetLang = getLTLanguage(Core.getProject().getProjectProperties().getTargetLanguage());
            sourceLt = getLanguageToolInstance(sourceLang);
            targetLt = getLanguageToolInstance(targetLang);
            if (sourceLt != null && targetLt != null) {
                bRules = getBiTextRules(sourceLang, targetLang);
            }
            break;
        case CLOSE:
            sourceLt = null;
            targetLt = null;
            break;
        default:
            // Nothing
        }
    }

    protected JLanguageTool getLanguageToolInstance(Language ltLang) {
        if (ltLang == null) {
            return null;
        }
        JLanguageTool result = null;

        try {
            result = new JLanguageTool(ltLang);
            result.getAllRules().stream().filter(rule -> rule instanceof SpellingCheckRule).map(Rule::getId)
                    .forEach(result::disableRule);
        } catch (Exception ex) {
            Log.log(ex);
        }

        return result;
    }

    @Override
    public synchronized List<Mark> getMarksForEntry(SourceTextEntry ste, String sourceText, String translationText,
            boolean isActive) throws Exception {
        if (translationText == null || !isEnabled()) {
            return null;
        }

        JLanguageTool ltSource = sourceLt;
        JLanguageTool ltTarget = targetLt;
        if (ltTarget == null) {
            // LT doesn't know anything about target language
            return null;
        }

        // LanguageTool claims to expect text in NFKC, but that actually causes problems for some rules:
        // https://github.com/languagetool-org/languagetool/issues/379
        // From the discussion it seems the intent behind NFKC was to break up multi-letter codepoints such as
        // U+FB00 LATIN SMALL LIGATURE FF. These are unlikely to be found in user input in our case, so
        // instead we will use NFC. We already normalize our source to NFC when loading, so we only need to
        // handle the translation here:
        if (translationText != null) {
            translationText = StringUtil.normalizeUnicode(translationText);
        }

        List<Mark> r = new ArrayList<Mark>();
        List<RuleMatch> matches;
        if (ltSource != null && bRules != null) {
            // LT knows about source and target languages both and has bitext rules.

            // sourceText represents the displayed source text: it may be null (not displayed) or have extra
            // bidi characters for display. Since we need it for linguistic comparison here, if it's null then
            // we pull from the SourceTextEntry, which is guaranteed not to be null.
            matches = Tools.checkBitext(sourceText == null ? ste.getSrcText() : sourceText, translationText,
                    ltSource, ltTarget, bRules);
        } else {
            // LT knows about target language only
            matches = ltTarget.check(translationText);
        }

        for (RuleMatch match : matches) {
            Mark m = new Mark(Mark.ENTRY_PART.TRANSLATION, match.getFromPos(), match.getToPos());
            m.toolTipText = match.getMessage();
            m.painter = PAINTER;
            r.add(m);
        }

        return r;
    }

    private Language getLTLanguage(org.omegat.util.Language lang) {
        String omLang = lang.getLanguageCode();
        for (Language ltLang : Languages.get()) {
            if (omLang.equalsIgnoreCase(ltLang.getShortName())) {
                return ltLang;
            }
        }
        return null;
    }

    /**
     * Retrieve bitext rules for specified languages, but remove some rules, which not required in OmegaT
     */
    private List<BitextRule> getBiTextRules(Language sourceLang, Language targetLang) {
        List<BitextRule> result;
        try {
            result = Tools.getBitextRules(sourceLang, targetLang);
        } catch (Exception ex) {
            // bitext rules can be not defined
            return null;
        }
        for (int i = 0; i < result.size(); i++) {
            if (result.get(i) instanceof DifferentLengthRule) {
                result.remove(i--);
                continue;
            }
            if (result.get(i) instanceof SameTranslationRule) {
                result.remove(i--);
                continue;
            }
            if (result.get(i) instanceof DifferentPunctuationRule) {
                result.remove(i--);
                continue;
            }
        }
        return result;
    }
}
