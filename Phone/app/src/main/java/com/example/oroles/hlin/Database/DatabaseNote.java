package com.example.oroles.hlin.Database;

import java.io.Serializable;

public class DatabaseNote implements Serializable{

    private int mId;
    private String mTitle;
    private String mText;
    private long mCreateTime;

    public DatabaseNote() {

    }

    public DatabaseNote(String title, String text) {
        mTitle = title;
        mText = text;
        mCreateTime = System.currentTimeMillis();
    }

    public String getTitle() {
        return mTitle;
    }

    public String getText() {
        return mText;
    }

    public int getId() {
        return mId;
    }

    public long getCreateTime() {
        return mCreateTime;
    }

    public void setText(String text) {
        mText = text;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setId(int id) {
        mId = id;
    }

    public void setCreateTime(long createTime) {
        mCreateTime = createTime;
    }



    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if ((DatabaseNote)obj == null) {
            return false;
        }
        return mTitle.equals(((DatabaseNote) obj).getTitle());
    }
}
