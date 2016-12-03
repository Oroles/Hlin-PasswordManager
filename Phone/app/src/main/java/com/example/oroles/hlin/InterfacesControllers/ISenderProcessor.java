package com.example.oroles.hlin.InterfacesControllers;


public interface ISenderProcessor {
    String createGetPasswordRequest(String password);
    String createGetNoteRequest(String text);

    String createGetHashRequest();
    String createCloseRequest(String hash);
    String createDecryptKey(String salt);
    String createIsAliveRequest();
    String createLastTimeUsedRequest();
    String createSetLastTimeUsedRequest(long time);

    String createAddEntryRequest(String website, String username, String password);
    String createAddAndGenerateEntryRequest(String website, String username, String allowTypes, int length);

    String createAddNoteRequest(String title, String text);
}
