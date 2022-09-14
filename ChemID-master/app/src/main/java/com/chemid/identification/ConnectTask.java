package com.chemid.identification;

import android.os.AsyncTask;
import android.widget.EditText;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

class ConnectTask extends AsyncTask<String, String,String> {

    DataOutputStream out;
    int sequence = 0;
    String ip = "";
    public ConnectTask(DataOutputStream o, String i) {
        super();
        out = o;
        ip = i;
    }
    protected String doInBackground(String... urls) {
        try {
            Socket client = new Socket(ip, 1735);
            OutputStream outToServer = client.getOutputStream();
            out = new DataOutputStream(outToServer);
            byte[] hello = {0x01, 0x03, 0x00, 0x00, 0x00, 0x00};
            out.write(hello);
            out.write(0x05);
            double[] val ={1.0, 2.0, 3.0};
            byte[] g = {0x11, 0x00, 0x00, 0x00, (byte) sequence, 0x11, (byte) val.length};
            byte[] f = new byte[7 + (8 * val.length)];
            for(int i = 0; i < g.length; i++) {
                f[i] = g[i];
            }
            for (int i = 0; i < val.length; i++) {
                byte[] k = ByteBuffer.allocate(8).putDouble(val[i]).array();
                for (int j = 0; j < k.length; j++) {
                    f[((i+1) * 8) + j -1] = k[j];
                }
            }
            try {
                System.out.println("written");
                out.write(f);
                for (int i = 0; i< f.length; i++) {
                    System.out.print(f[i] + ", ");
                }
                System.out.println();
                ++sequence;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "connected";
        } catch (Exception e) {
            return "failed";
        }
    };
}