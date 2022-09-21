public class RunServer {
    public static void main(String[] args) {
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        Servidores server = new Servidores();
        System.out.println("Iniciando servidor\n");
        server.startServer();
    }
}
