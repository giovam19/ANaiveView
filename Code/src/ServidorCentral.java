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
    private BufferedReader input;
    private PrintWriter out;
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

    public void run() {
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

    public void listenRequest() {
        while (true) {
            try {
                for (Socket s : sockets) {
                    System.out.println("is in bucle");
                    input = new BufferedReader(new InputStreamReader(s.getInputStream()));
                    out = new PrintWriter(s.getOutputStream(), true);

                    if (input.ready()) {
                        System.out.println("Socket receiving message ...");
                        message = input.readLine();//U-n ; R-
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
                            out.print(message);
                        }
                    } else {
                        System.out.println("waiting message ...");
                    }
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                break;
            }
        }
    }
}
