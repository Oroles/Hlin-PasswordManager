package com.example.oroles.hlin.InterfacesControllers;


public interface IEncryption {

    void readHash(String mac);
    void storeHash(String mac);
    boolean compareHash(String value);
    String getHash();
    void appendHash(String value);
}
