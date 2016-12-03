package com.example.oroles.hlin.Controllers;

import com.example.oroles.hlin.InterfacesControllers.ICommandManager;
import com.example.oroles.hlin.InterfacesControllers.IStore;

import java.util.ArrayList;

public class CommandManagerController implements ICommandManager {

    private IStore mStore;
    private ArrayList<Integer> mCommands;

    public CommandManagerController(IStore store) {
        mStore = store;
        mCommands = new ArrayList<>();
    }

    @Override
    public void addCommand(int commandId) {
        mCommands.add(0, (int) commandId);
    }

    @Override
    public boolean existCommand(int commandId) {
        return -1 != mCommands.indexOf(Integer.valueOf(commandId));
    }

    @Override
    public void removeAll(int commandId) {
        int index = 0;
        while ((index = mCommands.indexOf(Integer.valueOf(commandId))) != -1) {
            mCommands.remove(index);
        }
    }

    @Override
    public void removeAll() {
        mCommands.clear();
    }


}
