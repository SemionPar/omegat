/**************************************************************************
 OmegaT - Computer Assisted Translation (CAT) tool 
          with fuzzy matching, translation memory, keyword search, 
          glossaries, and translation leveraging into updated projects.

 Copyright (C) 2014 Alex Buloichik
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

package org.omegat.filters2.text;

import java.io.IOException;
import java.io.Writer;

import org.omegat.tokenizer.ITokenizer;
import org.omegat.util.Token;

/**
 * Output filter for limit line length.
 * 
 * @author Alex Buloichik (alex73mail@gmail.com)
 */
public class LineLengthLimitWriter extends Writer {
    final Writer out;
    final int lineLength;
    final int maxLineLength;
    final ITokenizer tokenizer;
    final StringBuilder str = new StringBuilder();

    public LineLengthLimitWriter(Writer out, int lineLength, int maxLineLength, ITokenizer tokenizer) {
        this.out = out;
        this.lineLength = lineLength;
        this.maxLineLength = maxLineLength;
        this.tokenizer = tokenizer;
    }

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        for (int i = 0; i < len; i++) {
            char ch = cbuf[off + i];
            if (ch == '\n' || ch == '\r') {
                out();
                out.write(ch);
            } else {
                str.append(ch);
            }
        }
    }

    void out() throws IOException {
        if (str.length() == 0) {
            return;
        }
        Token[] tokens = tokenizer.tokenizeAllExactly(str.toString());
        again: while (str.length() > 0) {
            for (int i = 0; i < tokens.length; i++) {
                Token t = tokens[i];
                if (t == null) {
                    // less than begin
                    continue;
                }
                if (t.getOffset() >= lineLength) {
                    if (t.getOffset() < maxLineLength) {
                        if (t.getLength() == 1 && Character.isWhitespace(str.charAt(t.getOffset()))) {
                            // space - use next token
                            continue;
                        }
                        // ok, just break
                        breakAt(t.getOffset(), tokens);
                        continue again;
                    } else {
                        // max exceed - use previously
                        if (i > 0 && tokens[i - 1] != null) {
                            breakAt(tokens[i - 1].getOffset(), tokens);
                            continue again;
                        } else {
                            // previous not exist
                            breakAt(t.getOffset(), tokens);
                            continue again;
                        }
                    }
                }
            }
            // length not exceed limit, but need to write tail
            out.write(str.toString());
            str.setLength(0);
        }
    }

    /**
     * Write part of line to specified position, and change token offsets.
     */
    void breakAt(int pos, Token[] tokens) throws IOException {
        out.write(str.toString(), 0, pos);
        out.write("\n");// TODO
        str.delete(0, pos);
        for (int i = 0; i < tokens.length; i++) {
            Token t = tokens[i];
            if (t == null || t.getOffset() < pos) {
                tokens[i] = null;
            } else {
                tokens[i] = new Token(null, t.getOffset() - pos, t.getLength());
            }
        }
    }

    @Override
    public void flush() throws IOException {
        out();
        out.flush();
    }

    @Override
    public void close() throws IOException {
        out();
        out.close();
    }
}