import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

public class ServidorCentral extends Thread{
    private final int PUERTO = 1234;
    private ServerSocket server;
    private ArrayList<Socket> sockets;
    private Socket aux;
    private DataInputStream input;
    private DataOutputStream out;
    private BufferedReader br;
    private String message;
    private int value;

    public ServidorCentral() {
        try {
            server = new ServerSocket(PUERTO);
            sockets = new ArrayList<>();
            aux = new Socket();
            message = "null";
            value = 1;

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void startServer() {
        while (true) {
            try {
                System.out.println("Esperando...");
                aux = server.accept();
                sockets.add(aux); //Accept comienza el socket y espera una conexión desde un cliente
                System.out.println("Cliente en línea\n");
            } catch (IOException e) {
                System.out.println(e.getMessage());
                break;
            }
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                for (Socket s : sockets) {
                    input = new DataInputStream(s.getInputStream());
                    out = new DataOutputStream(s.getOutputStream());

                    if (input.available() > 0) {
                        System.out.println("Socket receiving message ...");
                        message = input.readUTF();//U-n ; R-
                        String[] data = message.split("-");
                        System.out.println("Message received ...");

                        if (data[0].equals("U")) {
                            //UPDATE
                            System.out.println("Receive updated ...");
                            value = Integer.parseInt(data[1]);
                            //out.writeUTF(Integer.toString(value));
                        } else if (data[0].equals("R")) {
                            //READ
                            System.out.println("Receive read ...");
                            message = Integer.toString(value);
                            out.writeUTF(message);
                            out.flush();
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}
