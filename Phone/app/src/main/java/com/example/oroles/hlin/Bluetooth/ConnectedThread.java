package com.example.oroles.hlin.Bluetooth;

import android.bluetooth.BluetoothSocket;

import com.example.oroles.hlin.Controllers.BluetoothConnectionController;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ConnectedThread extends Thread {
    private boolean stop;
    private final InputStream mmInStream;
    private final OutputStream mmOutStream;
    private BluetoothConnectionController mBluetoothConnectionController;

    public ConnectedThread(BluetoothConnectionController bluetoothConnectionController,
                           BluetoothSocket socket) {
        InputStream tmpIn = null;
        OutputStream tmpOut = null;
        mBluetoothConnectionController = bluetoothConnectionController;

        // Get the input and output streams, using temp objects because
        // member streams are final
        try {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
            }

        mmInStream = tmpIn;
        mmOutStream = tmpOut;
        stop = false;
    }

    public void run() {
        byte[] buffer = new byte[1024];  // buffer where reads from the stream
        int bytes; // bytes returned from read()
        String command = "";

        // Keep listening to the InputStream until an exception occurs
        while (!stop) {
            try {
                // Read from the InputStream
                bytes = mmInStream.read(buffer);

                // create aux variable, for easy processing
                String aux =  new String(buffer, 0, bytes);
                command = command.concat(aux);
                if ( command.contains("\n")) {
                    // found the end of the command

                    // send the message
                    //mHandler.obtainMessage(Utils.MESSAGE_READ, command).sendToTarget();
                    mBluetoothConnectionController.notifyReceiveMessageListeners(command);

                    // reset the message
                    command = "";
                }
                // reset the command if it is to long
                if (command.length() > 1024 ) {
                    command = "";
                }

            } catch (IOException e) {
                break;
            }
        }
    }

    /* Writes bytes on the stream */
    public void write(byte[] bytes) {
        try {
            if (mmOutStream != null)
                mmOutStream.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* Close the connection */
    public void close() {
        try {
            stop = true;

            if (mmOutStream != null) {
                mmOutStream.close();
            }
            if (mmInStream != null) {
                mmInStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
