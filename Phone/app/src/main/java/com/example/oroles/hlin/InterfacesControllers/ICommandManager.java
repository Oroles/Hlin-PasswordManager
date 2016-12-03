package com.example.oroles.hlin.InterfacesControllers;

public interface ICommandManager {

    void addCommand(int commandId);
    boolean existCommand(int commandId);
    void removeAll(int commandId);
    void removeAll();
}
