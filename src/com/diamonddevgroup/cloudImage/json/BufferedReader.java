/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diamonddevgroup.cloudImage.json;

import java.io.IOException;
import java.io.Reader;

/*
 * Copyright (c) 2012, Eric Coolman, 1815750 Ontario Inc. and/or its 
 * affiliates. All rights reserved.
 * 
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  1815750 Ontario Inc designates 
 * this  * particular file as subject to the "Classpath" exception as provided
 * in the LICENSE file that accompanied this code.
 *  
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 * 
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 * 
 * Please contact 1815750 Ontario Inc. through http://www.coolman.ca/ if you 
 * need additional information or have any questions.
 */
/**
 * An implementation of a BufferedReader.
 *
 * @author Eric Coolman
 *
 */
public class BufferedReader extends Reader {

    public static final int EOF = -1;
    private static final int DEFAULT_SIZE = 8192;

    private char buffer[];
    private Reader parent;
    int buflen;
    int pointer;

    /**
     * Construct a Buffered reader with a given reader instance, using a default
     * buffer size.
     *
     * @param reader
     */
    public BufferedReader(Reader reader) {
        this(reader, DEFAULT_SIZE);
    }

    /**
     * Construct a Buffered reader with a given reader instance, and specific
     * size for buffer.
     *
     * @param reader
     * @param sz
     */
    public BufferedReader(Reader reader, int sz) {
        this.buffer = new char[sz];
        this.parent = reader;
        pointer = 0;
        buflen = 0;
    }

    /**
     * Close the stream.
     *
     * @throws IOException on error closing the stream.
     */
    public void close() throws IOException {
        parent.close();
    }

    /**
     * Mark the present position in the stream, this implementation currently
     * does not support marking.
     *
     * @param readAheadLimit
     */
    public void mark(int readAheadLimit) {

    }

    /**
     * Tell whether this stream supports the mark() operation, which this
     * implementation currently does not.
     *
     */
    public boolean markSupported() {
        return false;
    }

    /**
     * Peek at next single character without advancing the stream (ie. next
     * read() operation should return this same character).
     *
     * @return a single character from the stream, or EOF if end of file has
     * been reached.
     * @throws IOException on error reading from the stream.
     */
    public int peek() throws IOException {
        if (buflen == EOF) {
            return EOF;
        }
        synchronized (lock) {
            if (pointer > (buflen - 1)) {
                buflen = parent.read(buffer);
                pointer = 0;
            }
            if ((buflen == EOF) || (pointer > (buflen - 1))) {
                return EOF;
            }
            return buffer[pointer];
        }
    }

    /**
     * Read a single character.
     *
     * @return a single character from the stream, or EOF if end of file has
     * been reached.
     * @throws IOException on error reading from the stream.
     */
    public int read() throws IOException {
        int ch = peek();
        pointer++;
        return ch;
    }

    /**
     * Read characters into a portion of an array.
     *
     * @param cbuf the destination buffer to read characters into.
     * @param off the offset index of cbuf to start reading characters to.
     * @param len the number of characters to read into the buffer.
     * @return the number of characters actually read, or EOF if end of file was
     * previously reached.
     * @throws IOException on error reading from the stream.
     */
    public int read(char[] cbuf, int off, int len) throws IOException {
        if (buflen == EOF) {
            return EOF;
        }
        for (int i = 0; i < len; i++) {
            int ch = read();
            if (ch == EOF) {
                return i;
            }
            cbuf[off + i] = (char) ch;
        }
        return len;
    }

    /**
     * Read a line of text.
     *
     * @return a line of text, or null if the end of file was previously
     * reached.
     * @throws IOException on error reading from the stream.
     */
    public String readLine() throws IOException {
        if (buflen == EOF) {
            return null;
        }
        StringBuffer line = new StringBuffer();
        int ch;
        boolean eol = false;
        while ((ch = read()) != EOF) {
            switch (ch) {
                case '\n':
                    eol = true;
                    break;
                case '\r':
                    eol = true;
                    if (peek() == '\n') {
                        read();
                    }
                    break;
            }
            if (eol) {
                break;
            }
            line.append((char) ch);
        }
        if ((ch == EOF) && (line.length() == 0)) {
            return null;
        }
        return line.toString();
    }

    /**
     * Tell whether this stream is ready to be read.
     *
     * @return true if characters can be read from the stream without blocking.
     * @throws IOException on error reading from the stream.
     */
    public boolean ready() throws IOException {
        return parent.ready();
    }

    /**
     * Reset the stream to the most recent mark. This implementation currently
     * does not support marking, so this method does nothing.
     */
    public void reset() {
    }

    /**
     * Skip characters.
     *
     * @return the number of characters actually skipped.
     * @throws IOException on error reading from the stream.
     */
    public long skip(long n) throws IOException {
        for (long i = 0L; i < n; i++) {
            if (read() == EOF) {
                return i;
            }
        }
        return n;
    }

}
