/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diamonddevgroup.cloudImage.utils;

import com.codename1.io.FileSystemStorage;

/**
 *
 * @author shannah
 */
public class File {

    String path;
    
    public File(String path) {
        this.path = path;
    }

    public long length() {
       return FileSystemStorage.getInstance().getLength(path);
    }

    @Override
    public String toString() {
        return path;
    }
    
    public String getPath() {
        return path;
    }
}
