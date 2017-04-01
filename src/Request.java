import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Request {

    public final String protocol;
    public final String method;
    public final String url;

    private final Scanner sc;

    public Request(InputStream is) {
        sc = new Scanner(is);
        final StringTokenizer st = new StringTokenizer(sc.nextLine(), " ");
        method = st.nextToken();
        String encURL = st.nextToken();
        protocol = st.nextToken();
        try {
            encURL = URLDecoder.decode(encURL, "utf-8");
        } catch (UnsupportedEncodingException e) {
        }
        int end = encURL.indexOf('#');
        if (end != -1) encURL = encURL.substring(0, end);
        end = encURL.indexOf('?');
        if (end != -1) encURL = encURL.substring(0, end);
        url = encURL;
    }

    public boolean isMethodAllowed() {
        return method.equals("GET") || method.equals("HEAD");
    }

    public boolean isHead() {
        return method.equals("HEAD");
    }

    public void close() {
        sc.close();
    }
}
