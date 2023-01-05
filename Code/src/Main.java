public class Main {
    public static void main(String[] args) {
        Server server = new Server();

        Client client1 = new Client(1, 0);
        Client client2 = new Client(2, 1);
        Client client3 = new Client(3, 0);
        Client client4 = new Client(4, 0);
        Client client5 = new Client(5, 1);

        server.start();

        client1.start();
        client2.start();
        client3.start();
        client4.start();
        client5.start();
    }
}
