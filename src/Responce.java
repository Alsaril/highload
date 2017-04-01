import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

public class Responce {

    public final String protocol;
    public final String code;
    public final HashMap<String, String> headers = new HashMap<>();
    private FileContent content = null;

    public Responce(String protocol, String code) {
        this.protocol = protocol;
        this.code = code;
    }

    public void setContent(FileContent content, boolean head) {
        if (!head) {
            this.content = content;
        }
        headers.put("Content-Length", Long.toString(content.length));
        if (content.type.contentType != null) {
            headers.put("Content-Type", content.type.contentType);
        }
    }

    public void write(PrintStream ps) throws IOException {
        ps.print(protocol);
        ps.print(" ");
        ps.print(code);
        ps.print("\r\n");
        for (Map.Entry<String, String> e : headers.entrySet()) {
            ps.print(e.getKey());
            ps.print(": ");
            ps.print(e.getValue());
            ps.print("\r\n");
        }
        ps.print("\r\n");
        if (content != null) {
            content.write(ps);
        }
        ps.flush();
        ps.close();
    }
}
