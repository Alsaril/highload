import java.io.*;
import java.util.HashMap;

public final class FileContent {

    private static String rootPath = null;

    public final FileType type;
    public final long length;
    private InputStream is;

    private FileContent(FileType type) {
        this(type, 0, null);
    }

    private FileContent(FileType type, long length, InputStream is) {
        this.type = type;
        this.length = length;
        this.is = is;
    }

    private static FileContent notFound() {
        return new FileContent(FileType.NOT_FOUND);
    }

    private static FileContent forbidden() {
        return new FileContent(FileType.FORBIDDEN);
    }

    public static FileContent load(String url) {
        if (url.contains("../")) return forbidden();
        if (url.endsWith("/")) {
            url = url + "index.html";
            if (!new File(rootPath + url).exists()) {
                return forbidden();
            }
        }
        final File file = new File(rootPath + url);
        if (file.exists()) {
            try {
                return addExtension(file, new BufferedInputStream(new FileInputStream(rootPath + url)));
            } catch (FileNotFoundException e) {
                return notFound();
            }
        } else {
            return notFound();
        }
    }

    private static FileContent addExtension(File file, InputStream is) {
        String extension = "";

        final int i = file.getName().lastIndexOf('.');
        if (i > 0) {
            extension = file.getName().substring(i + 1);
        }
        final FileType type = FileType.types.getOrDefault(extension, FileType.BYTE);

        return new FileContent(type, file.length(), is);
    }

    public static void setRootPath(String rootPath) {
        FileContent.rootPath = rootPath;
    }

    public void write(PrintStream ps) throws IOException {
        if (is == null) return;
        final byte[] buffer = new byte[512 * 1024];
        while (true) {
            final int read = is.read(buffer);
            if (read == -1) break;
            ps.write(buffer, 0, read);
        }
    }

    public enum FileType {
        HTML("html", "text/html"),
        CSS("css", "text/css"),
        JS("js", "text/javascript"),
        JPG("jpg", "image/jpeg"),
        PNG("png", "image/png"),
        GIF("gif", "image/gif"),
        SWF("swf", "application/x-shockwave-flash"),
        BYTE(null, null),
        FORBIDDEN(null, null),
        NOT_FOUND(null, null);

        public static final HashMap<String, FileType> types = new HashMap<>();

        static {
            for (FileType ft : FileType.values()) {
                types.put(ft.extension, ft);
            }
            types.put("jpeg", JPG);
        }

        public final String contentType;
        private final String extension;

        FileType(String extension, String contentType) {
            this.extension = extension;
            this.contentType = contentType;
        }
    }
}
