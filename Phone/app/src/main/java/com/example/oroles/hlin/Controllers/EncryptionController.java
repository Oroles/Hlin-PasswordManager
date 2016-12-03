package com.example.oroles.hlin.Controllers;

import com.example.oroles.hlin.InterfacesControllers.IEncryption;
import com.example.oroles.hlin.InterfacesControllers.IStore;
import com.example.oroles.hlin.Utils.SharedPreferencesHelper;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptionController implements IEncryption
{
    //private static final String HASH_FILENAME = "Hash";

    private IStore mStore;
    private String mHashValue;


    public EncryptionController(IStore store) {
        mStore = store;
        //readHash();
    }

    @Override
    public void readHash(String mac) {
        mHashValue = SharedPreferencesHelper.getBluetoothHash(mStore.getContext(), mac);

        /*try {
            FileInputStream file = mStore.getContext().openFileInput(HASH_FILENAME);
            mHashValue = "";
            int c;
            while( (c = file.read()) != -1){
                mHashValue = mHashValue + Character.toString((char)c);
            }
            file.close();
        } catch (IOException e) {
            mHashValue = "00000000000000000000000000000000";
        }*/
    }

    @Override
    public void storeHash(String mac) {
        SharedPreferencesHelper.setBluetoothHash(mStore.getContext(), mac, mHashValue);
        /*try {
            FileOutputStream file = mStore.getContext().openFileOutput(HASH_FILENAME, Context.MODE_PRIVATE);
            file.write(mHashValue.getBytes());
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public boolean compareHash(String value) {
        return mHashValue.equals(value);
    }

    @Override
    public String getHash() {
        return mHashValue;
    }

    @Override
    public void appendHash(String value) {
        mHashValue = mHashValue.concat(value);
        mHashValue = md5(mHashValue);
    }

    private String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
