package com.example.oroles.hlin.Utils;

public class Utils {

    public static final char SPLITTER = '\r';
    public static final char END_COMMAND = '\n';
    public static final char PADDING = '\t';

    //change these in ascii code from 1 -> 1 + '0', etc.
    public static final int ADD_ENTRY_TYPE = 1 + '0';
    public static final int REQUEST_PASSWORD_MESSAGE_TYPE = 2 + '0';
    public static final int GET_STORED_IN_BUFFER = 3 + '0';
    public static final int LAST_TIME_USED = 4 + '0';
    public static final int CLOSE_MESSAGE_TYPE = 5 + '0';
    public static final int RETRIEVE_HASH_MESSAGE_TYPE = 6 + '0';
    public static final int ADD_GENERATE_ENTRY_TYPE = 7 + '0';
    public static final int DECRYPT_KEY = 8 + '0';
    public static final int IS_ALIVE_MESSAGE_TYPE = 9 + '0';
    public static final int ERROR_TYPE = 11 + '0';
    public static final int SET_LAST_TIME_USED = 10 + '0';
    public static final int ADD_NOTE_TYPE = 12 + '0';
    public static final int REQUEST_NOTE_PASSWORD_TYPE = 13 + '0';

    public enum BLUETOOTH_FOUND_OPERATION {
        CONNECT,
        ADD_TO_LIST
    }


    static public int getMessageType(String message) {
        try {
            //return Integer.parseInt(message.substring(0,1));
            return message.substring(0,1).charAt(0);
        } catch(Exception e) {
            return 0;
        }
    }

    static public String getWebsite(String message) {
        try {
            return message.substring(2, message.indexOf(SPLITTER, 2));
        } catch(Exception e) {
            return "";
        }
    }

    static public String getTitle(String message) {
        try {
            return message.substring(2, message.indexOf(SPLITTER, 2));
        } catch (Exception e) {
            return "";
        }
    }

    static public String getNote(String message) {
        try {
            return message.substring(message.lastIndexOf(SPLITTER) + 1, message.length() - 1);
        } catch (Exception e) {
            return "";
        }
    }

    static public String getHash(String message) {
        try {
            return message.substring(message.lastIndexOf(SPLITTER) + 1, message.length() - 1);
        } catch (Exception e) {
            return "";
        }
    }

    static public String getUsername(String message, char usernameSplitter) {
        try {
            return message.substring(message.indexOf(SPLITTER, message.indexOf(SPLITTER) + 1) + 1, message.lastIndexOf(usernameSplitter));
        } catch(Exception e) {
            return "";
        }
    }

    static public String getPassword(String message) {
        try {
            return message.substring(message.lastIndexOf(SPLITTER) + 1, message.length() - 1);
        } catch (Exception e) {
            return "";
        }
    }

    static public String getFirstMessage(String message) {
        try {
            return message.substring(2, message.length());
        } catch(Exception e) {
            return "";
        }
    }

    static public String generateAllowTypes(boolean allowSymbols, boolean allowNumbers, boolean allowLetters) {
        if ((!allowSymbols) && (!allowNumbers) && (allowLetters)) return "1";
        if ((!allowSymbols) && (allowNumbers) && (!allowLetters)) return "2";
        if ((!allowSymbols) && (allowNumbers) && allowLetters) return "3";
        if ((allowSymbols) && (!allowNumbers) && (!allowLetters)) return "4";
        if ((allowSymbols) && (!allowNumbers) && (allowLetters)) return "5";
        if ((allowSymbols) && (allowNumbers) && (!allowLetters)) return "6";
        if ((allowSymbols) && (allowNumbers) && (allowLetters)) return "7";
        return "0";
    }
}
