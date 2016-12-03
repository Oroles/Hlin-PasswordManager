package com.example.oroles.hlin.Database;

import java.io.Serializable;

public class DatabaseEntry implements Serializable {

    private int id;
    private String website;
    private String username;
    private String password;
    private long time;
    private boolean expired;
    private long lastUsedTime;
    private long usedTimes;
    private boolean duplicated;

    public DatabaseEntry() {}

    public DatabaseEntry(String website, String username, String password) {
        this.website = website;
        this.username = username;
        this.password = password;
        this.expired =  false;
        this.usedTimes = 0;

        long currentTime = System.currentTimeMillis();
        this.lastUsedTime = currentTime;
        this.time = currentTime;
        this.duplicated = false;
    }

    public DatabaseEntry(String website, String username, String password, boolean expired) {
        this.website = website;
        this.username = username;
        this.password = password;
        this.expired =  expired;
        this.usedTimes = 0;

        long currentTime = System.currentTimeMillis();
        this.lastUsedTime = currentTime;
        this.time = currentTime;
        this.duplicated = false;
    }

    // Getters & setters

    public String getWebsite() {
        return this.website;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public long getTime(){
        return this.time;
    }

    public boolean getExpired() {return this.expired; }

    public long getLastUsedTime() {
        return this.lastUsedTime;
    }

    public long getUsedTimes() {
        return this.usedTimes;
    }

    public boolean getDuplicated() {return  this.duplicated;}

    public void setId(int id) {
        this.id = id;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setExpired(boolean expired) { this.expired = expired; }

    public void setLastUsedTime(long time) {
        this.lastUsedTime = time;
    }

    public void setUsedTimes(long times) {
        this.usedTimes = times;
    }

    public void setDuplicated(boolean duplicated) { this.duplicated = duplicated; }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if ((DatabaseEntry)obj == null) return false;
        DatabaseEntry tmp = (DatabaseEntry)obj;
        return tmp.getUsername().equals(this.username) &&
                tmp.getWebsite().equals(this.website);
    }

    @Override
    public String toString() {
        return "Entry [id=" + id + ", website=" + website + ", username=" + username +
                ", password=" + password + ", time=" + time +
                ", lastTimeUsed=" + lastUsedTime + "]";
    }
}
