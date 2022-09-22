public class RunCentral {
    public static void main(String[] args) {
        System.out.println("Iniciando central\n");
        Server server = new Server();
        server.startServer();
        /*ServidorCentral central = new ServidorCentral();
        central.start();
        central.startServer();*/
    }
}
