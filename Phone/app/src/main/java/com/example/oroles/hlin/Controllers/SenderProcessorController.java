package com.example.oroles.hlin.Controllers;

import com.example.oroles.hlin.InterfacesControllers.ISenderProcessor;
import com.example.oroles.hlin.InterfacesControllers.IStore;
import com.example.oroles.hlin.Utils.Utils;

public class SenderProcessorController implements ISenderProcessor {

    private IStore mStore;

    public SenderProcessorController(IStore store) {
        mStore = store;
    }

    @Override
    public String createGetPasswordRequest(String password) {
        mStore.getCommandManager().addCommand(Utils.REQUEST_PASSWORD_MESSAGE_TYPE);
        return String.valueOf((char)Utils.REQUEST_PASSWORD_MESSAGE_TYPE) + Utils.SPLITTER +
                String.valueOf(password.length()) + Utils.SPLITTER + password + Utils.END_COMMAND;
    }

    @Override
    public String createGetNoteRequest(String text) {
        mStore.getCommandManager().addCommand(Utils.REQUEST_NOTE_PASSWORD_TYPE);
        return String.valueOf((char)Utils.REQUEST_NOTE_PASSWORD_TYPE) + Utils.SPLITTER +
                String.valueOf(text.length()) + Utils.SPLITTER + text + Utils.END_COMMAND;
    }

    @Override
    public String createGetHashRequest() {
        mStore.getCommandManager().addCommand(Utils.RETRIEVE_HASH_MESSAGE_TYPE);
        return String.valueOf((char)Utils.RETRIEVE_HASH_MESSAGE_TYPE) + Utils.END_COMMAND ;
    }

    @Override
    public String createCloseRequest(String hash) {
        return String.valueOf((char)Utils.CLOSE_MESSAGE_TYPE) + Utils.SPLITTER + hash + Utils.END_COMMAND;
    }

    @Override
    public String createDecryptKey(String salt) {
        return String.valueOf((char)Utils.DECRYPT_KEY) + Utils.SPLITTER + salt + Utils.END_COMMAND;
    }

    @Override
    public String createIsAliveRequest() {
        mStore.getCommandManager().addCommand(Utils.IS_ALIVE_MESSAGE_TYPE);
        return String.valueOf((char)Utils.IS_ALIVE_MESSAGE_TYPE) + Utils.END_COMMAND;
    }

    @Override
    public String createLastTimeUsedRequest() {
        mStore.getCommandManager().addCommand(Utils.LAST_TIME_USED);
        return  String.valueOf((char)Utils.LAST_TIME_USED) + Utils.END_COMMAND;
    }

    @Override
    public String createSetLastTimeUsedRequest(long time) {
        return String.valueOf((char)Utils.SET_LAST_TIME_USED) + Utils.SPLITTER + String.valueOf(time) +
                Utils.END_COMMAND;
    }

    @Override
    public String createAddEntryRequest(String website, String username, String password) {
        mStore.getCommandManager().addCommand(Utils.ADD_ENTRY_TYPE);
        String tmp = password;
        while (tmp.length() % 16 != 0) {
            tmp = tmp + Utils.PADDING;
        }
        //String tmp = String.format("%-16s", password).replace(' ', Utils.PADDING);
        return String.valueOf((char)Utils.ADD_ENTRY_TYPE) + Utils.SPLITTER + website + Utils.SPLITTER
                + username + Utils.SPLITTER + tmp + Utils.END_COMMAND;
    }

    @Override
    public String createAddAndGenerateEntryRequest(String website, String username, String allowTypes, int length) {
        mStore.getCommandManager().addCommand(Utils.ADD_GENERATE_ENTRY_TYPE);
        return String.valueOf((char)Utils.ADD_GENERATE_ENTRY_TYPE) + Utils.SPLITTER + website
                + Utils.SPLITTER + username + Utils.SPLITTER + allowTypes + Utils.SPLITTER
                + String.valueOf(length) + Utils.END_COMMAND;
    }

    @Override
    public String createAddNoteRequest(String title, String text) {
        mStore.getCommandManager().addCommand(Utils.ADD_NOTE_TYPE);
        String tmp = text;
        while (tmp.length() % 16 != 0) {
            tmp = tmp + Utils.PADDING;
        }
        return String.valueOf((char)Utils.ADD_NOTE_TYPE) + Utils.SPLITTER + title
                + Utils.SPLITTER + tmp + Utils.END_COMMAND;
    }


}
