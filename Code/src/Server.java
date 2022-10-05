import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class Server {
    private Selector selector;
    private ServerSocketChannel socketChannel;
    private ServerSocket serverSocket;
    private int value;
    private int idPriority;

    public Server() {
        try {
            idPriority = -1;
            value = 0;
            selector = Selector.open();
            socketChannel = ServerSocketChannel.open();
            serverSocket = socketChannel.socket();
            serverSocket.bind(new InetSocketAddress("localhost", 8089));
            socketChannel.configureBlocking(false);
            int ops = socketChannel.validOps();
            socketChannel.register(selector, ops, null);
        } catch (Exception e) {
            System.out.println("Exception const: " + e.getMessage());
        }
    }

    public void startServer() {
        while (true) {
            try {
                selector.select();
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectedKeys.iterator();

                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();

                    if (key.isAcceptable()) {
                        //new client
                        SocketChannel client = socketChannel.accept();
                        client.configureBlocking(false);
                        client.register(selector, SelectionKey.OP_READ);
                    } else if (key.isReadable()) {
                        //read on client
                        SocketChannel client = (SocketChannel) key.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        client.read(buffer);

                        //parse from buffer to string
                        String data = new String(buffer.array()).trim();

                        if (!data.isEmpty()) {
                            System.out.println("data: " + data);
                            serverEngine(data, client);
                        }
                    }
                    iterator.remove();
                }

            } catch (Exception e) {
                System.out.println("Exception server: " + e.getMessage());
                break;
            }
        }
    }

    private void serverEngine(String data, SocketChannel client) throws Exception {
        String[] trama = data.split("-");

        switch (trama[0]) {
            case "P":
                if (idPriority == -1) {
                    idPriority = Integer.parseInt(trama[1]);
                    writeToClient("OK", client);
                } else {
                    writeToClient("KO", client);
                }
                break;
            case "F":
                int id = Integer.parseInt(trama[1]);
                if (idPriority == id) {
                    idPriority = -1;
                }
                break;
            case "U":
                //UPDATE
                System.out.println("Receive updated ...");
                value = Integer.parseInt(trama[1]);
                break;
            case "R":
                //READ
                System.out.println("Receive read ...");
                String message = Integer.toString(value);
                writeToClient(message, client);
                break;
        }
    }

    private void writeToClient(String message, SocketChannel client) throws Exception {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.put(message.getBytes());
        buffer.flip();
        client.write(buffer);
    }
}
