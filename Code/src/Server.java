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

    public Server() {
        try {
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

                        serverEngine(data, client);
                    }
                    iterator.remove();
                }

            } catch (Exception e) {
                System.out.println("Exception run: " + e.getMessage());
                break;
            }
        }
    }

    private void serverEngine(String data, SocketChannel client) throws IOException {
        String[] trama = data.split("-");

        if (trama[0].equals("U")) {
            //UPDATE
            System.out.println("Receive updated ...");
            value = Integer.parseInt(trama[1]);
        } else if (trama[0].equals("R")) {
            //READ
            System.out.println("Receive read ...");
            String message = Integer.toString(value);

            ByteBuffer buffer = ByteBuffer.allocate(1024);
            buffer.put(message.getBytes());
            buffer.flip();
            client.write(buffer);
        }
    }
}
