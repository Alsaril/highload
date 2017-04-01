import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.NoSuchElementException;

public class Query implements Runnable {

    private Socket socket;
    private InputStream is;
    private PrintStream ps;

    public Query(Socket socket) {
        this.socket = socket;
        try {
            is = socket.getInputStream();
            ps = new PrintStream(socket.getOutputStream());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void run() {
        final Request request;
        try {
            request = new Request(is);
        } catch (NoSuchElementException e) {
            return;
        }
        final Responce responce;
        if (!request.isMethodAllowed()) {
            responce = new Responce(request.protocol, "405 Method Not Allowed");
            responce.headers.put("Allow", "GET, HEAD");
        } else {
            final FileContent file = FileContent.load(request.url);
            if (file.type == FileContent.FileType.NOT_FOUND) {
                responce = new Responce(request.protocol, "404 Not Found");
            } else if (file.type == FileContent.FileType.FORBIDDEN) {
                responce = new Responce(request.protocol, "403 Forbidden");
            } else {
                responce = new Responce(request.protocol, "200 OK");
                responce.setContent(file, request.isHead());
            }
        }
        responce.headers.put("Date", new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US).format(new Date(System.currentTimeMillis())));
        responce.headers.put("Server", "Java Thread Pool Server");
        responce.headers.put("Connection", "Close");
        try {
            responce.write(ps);
            request.close();
            socket.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
