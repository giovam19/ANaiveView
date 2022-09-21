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
import java.util.Iterator;
import java.util.Set;

public class ServidorCentral {
    private final int PUERTO = 1234;
    private ServerSocket server;
    private Socket socket;
    private DataInputStream input;
    private DataOutputStream out;
    private String message;

    private Selector selector;
    private SelectionKey selectionKey;
    private ServerSocketChannel socketChannel;
    private InetSocketAddress socketAddr;

    public ServidorCentral() {
        try {
            /*server = new ServerSocket(PUERTO);
            socket = new Socket();*/
            message = "null";

            selector = Selector.open();
            socketChannel = ServerSocketChannel.open();
            socketAddr = new InetSocketAddress("localhost", PUERTO);
            socketChannel.bind(socketAddr);
            socketChannel.configureBlocking(false);
            int ops = socketChannel.validOps();
            selectionKey = socketChannel.register(selector, ops, null);

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void startCentralServerNIO() {
        while (true) {
            System.out.println("Waiting for connections ...");

            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = keys.iterator();

            while (iterator.hasNext()) {
                SelectionKey myKey = iterator.next();

                if (myKey.isAcceptable()) {
                    try {
                        SocketChannel client = socketChannel.accept();
                        client.configureBlocking(false);
                        client.register(selector, SelectionKey.OP_READ);
                        System.out.println("Connection accepted: " + client.getLocalAddress());
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                } else if (myKey.isReadable()) {
                    SocketChannel client = (SocketChannel) myKey.channel();
                    ByteBuffer
                    client.read();
                    String result = new String()
                }
            }
        }
    }

    public void startCentralServer() {
        try {
            //TODO: buscar multi-cliente con libreria NIO.
            System.out.println("Esperando...");
            socket = server.accept(); //Accept comienza el socket y espera una conexión desde un cliente
            System.out.println("Cliente en línea");

            input = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            out = new DataOutputStream(socket.getOutputStream());

            while (!message.equals("fin")) {
                message = input.readUTF();
                System.out.println("Mensaje: " + message);
            }

            System.out.println("Fin de la conexión");
            server.close();//Se finaliza la conexión con el cliente
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
