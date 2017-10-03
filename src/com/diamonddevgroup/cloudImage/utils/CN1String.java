/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diamonddevgroup.cloudImage.utils;

import com.codename1.util.StringUtil;
import com.codename1.util.regex.RE;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Diamond
 */
public class CN1String implements CharSequence {

    private final String s;

    public CN1String(String str) {
        this.s = str;
    }

    public boolean isEmpty() {
        return s.length() == 0;
    }

    @Override
    public int length() {
        return s.length();
    }

    @Override
    public char charAt(int index) {
        return s.charAt(index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return new CN1String(s.substring(start, end));
    }

    @Override
    public String toString() {
        return s;
    }

    public String[] split(String regex) {
        RE r = new RE(regex);
        return r.split(s);
    }

    public String[] split(String regex, int limit) {
        // Create new vector
        RE r = new RE(regex);
        ArrayList v = new ArrayList();

        // Start at position 0 and search the whole string
        int pos = 0;
        int len = s.length();

        // Try a match at each position
        while (pos < len && v.size() < limit && r.match(s, pos)) {
            // Get start of match
            int start = r.getParenStart(0);

            // Get end of match
            int newpos = r.getParenEnd(0);

            // Check if no progress was made
            if (newpos == pos) {
                v.add(s.substring(pos, start + 1));
                newpos++;
            } else {
                v.add(s.substring(pos, start));
            }

            // Move to new position
            pos = newpos;
        }

        // Push remainder if it's not empty
        String remainder = s.substring(pos);
        if (remainder.length() != 0) {
            v.add(remainder);
        }

        // Return vector as an array of strings
        String[] ret = new String[v.size()];
        v.toArray(ret);
        return ret;
    }

    public String[] split(char sep) {
        List<String> l = StringUtil.tokenize(s, sep);
        return l.toArray(new String[l.size()]);
    }

    public String replaceFirst(String regex, String replacement) {
        RE r = new RE(regex);
        return r.subst(s, replacement, RE.REPLACE_FIRSTONLY);
    }

    public String replace(String needle, String replacement) {
        return StringUtil.replaceAll(s, needle, replacement);
    }

    public String replaceAll(String regex, String replacement) {
        RE r = new RE(regex);
        return r.subst(s, replacement, RE.REPLACE_ALL | RE.REPLACE_BACKREFERENCES);
    }

    public boolean matches(String regex) {
        RE r = new RE(regex);
        return r.match(s);
    }

    public boolean contains(String substr) {
        return s.indexOf(substr) != -1;
    }

    public static class Builder implements CharSequence {

        private String s;

        public Builder(String str) {
            this.s = str;
        }

        public Builder replace(String needle, String replacement) {
            return new Builder(new CN1String(s).replace(needle, replacement));
        }

        public Builder replaceAll(String regex, String replacement) {
            return new Builder(new CN1String(s).replaceAll(regex, replacement));
        }

        @Override
        public int length() {
            return s.length();
        }

        @Override
        public char charAt(int index) {
            return s.charAt(index);
        }

        @Override
        public CharSequence subSequence(int start, int end) {
            return new Builder(s.substring(start, end));
        }

        @Override
        public String toString() {
            return s;
        }

    }

    public static boolean isSpace(char ch) {
        // Performing the subtraction up front alleviates need to compare longs.
        return ch-- <= ' ' && ((1 << ch)
                & ((1 << (' ' - 1))
                | (1 << ('\t' - 1))
                | (1 << ('\n' - 1))
                | (1 << ('\r' - 1))
                | (1 << ('\f' - 1)))) != 0;
    }

}
