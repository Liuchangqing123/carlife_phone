package com.didi365.carlife.video;

import android.util.Log;

import java.io.Serializable;

public class MediaModel implements Serializable {

    private String uri = "";
    private String title = "";
    private String artist = "";
    private String album = "";
    private String albumiconuri = "";
    private String objectclass = "";

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = (title != null ? title : "");
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = (artist != null ? artist : "");
    }

    public void setAlbum(String album) {
        this.album = (album != null ? album : "");
    }

    public String getAlbum() {
        return album;
    }

    public void setObjectClass(String objectClass) {
        this.objectclass = (objectClass != null ? objectClass : "");
    }

    public String getObjectClass() {
        return objectclass;
    }

    public void setUrl(String uri) {
        this.uri = (uri != null ? uri : "");
    }

    public String getUrl() {
        return uri;
    }

    public String getAlbumUri() {
        return albumiconuri;
    }

    public void setAlbumUri(String albumiconuri) {
        this.albumiconuri = (albumiconuri != null ? albumiconuri : "");
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"uri\":");
        sb.append("\""+uri + "\"");
        sb.append(",");
        sb.append("\"title\":");
        sb.append("\"" + title + "\"");
        sb.append(",");
        sb.append("\"album\":");
        sb.append("\"" + album + "\"");
        sb.append(",");
        sb.append("\"albumiconuri\":");
        sb.append("\"" + albumiconuri + "\"");
        sb.append(",");
        sb.append("\"objectclass\":");
        sb.append("\"" + objectclass + "\"");
        sb.append("}");
        Log.d("RenderApplication", "toString " + sb.toString());
        return sb.toString();
    }
}
