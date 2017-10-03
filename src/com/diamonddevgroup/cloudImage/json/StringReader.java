/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diamonddevgroup.cloudImage.json;

import java.io.IOException;
import java.io.Reader;

/**
 *
 * @author Diamond
 */
class StringReader extends Reader {

    final String s;
    final int len;
    int pos=0;
    public StringReader(String s) {
        this.s = s;
        this.len = s.length();
    }

    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
        len = Math.min(s.length()-pos, len);
        if (len <= 0) {
            return -1;
        }
        System.arraycopy(s.toCharArray(), pos, cbuf, off, len);
        pos+=len;
        return len;
    }

    @Override
    public void close() throws IOException {
        
    }

    
    
    
}
