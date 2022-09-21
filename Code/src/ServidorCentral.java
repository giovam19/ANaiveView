import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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
    private DataInputStream input;
    private DataOutputStream out;
    private String message;
    private int value;

    public ServidorCentral() {
        try {
            server = new ServerSocket(PUERTO);
            sockets = new ArrayList<>();
            message = "null";
            value = 1;

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void startCentralServer() {
        try {
            System.out.println("Esperando...");
            sockets.add(server.accept()); //Accept comienza el socket y espera una conexión desde un cliente
            System.out.println("Cliente en línea");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void run() {
        try {
            for (Socket s : sockets) {
                input = new DataInputStream(new BufferedInputStream(s.getInputStream()));
                out = new DataOutputStream(s.getOutputStream());

                if (input.available() > 0) {
                    message = input.readUTF();//U-n ; R-
                    String[] data = message.split("-");

                    if (data[0].equals("U")){
                        //UPDATE
                        value = Integer.parseInt(data[1]);
                        out.writeUTF(Integer.toString(value));
                    } else if (data[0].equals("R")){
                        //READ
                        out.writeUTF(Integer.toString(value));
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
