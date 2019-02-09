package de.mrfrey.photos.store.web;

import de.mrfrey.photos.store.photo.ImageCommand;
import de.mrfrey.photos.store.photo.Photo;

public class ReprocessRequest {
    private ImageCommand command;
    private Photo.Size size;

    public ImageCommand getCommand() {
        return command;
    }

    public void setCommand( ImageCommand command ) {
        this.command = command;
    }

    public Photo.Size getSize() {
        return size;
    }

    public void setSize( Photo.Size size ) {
        this.size = size;
    }
}
