import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Server {
    private Executor ex;

    public Server(int threads) {
        ex = Executors.newFixedThreadPool(threads);
    }

    public void start() {
        try {
            final ServerSocket ss = new ServerSocket(80);
            while (true) {
                ex.execute(new Query(ss.accept()));
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
