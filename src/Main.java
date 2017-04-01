import org.apache.commons.cli.*;

public class Main {
    public static void main(String[] args) {
        final Options options = new Options();
        options.addOption(
                Option.builder("c")
                        .hasArg()
                        .required()
                        .desc("number of cpu")
                        .type(Number.class)
                        .build());
        options.addOption(
                Option.builder("r")
                        .hasArg()
                        .required()
                        .desc("root directory")
                        .build());
        final int n;
        final String rootPath;
        try {
            final CommandLine cmd = new DefaultParser().parse(options, args);
            n = ((Number) cmd.getParsedOptionValue("c")).intValue();
            rootPath = cmd.getOptionValue("r");
        } catch (ParseException | NumberFormatException e) {
            System.out.println(e.getMessage());
            new HelpFormatter().printHelp("httpd", options);
            System.exit(1);
            return;
        }
        System.out.println("Starting server at port 80");
        System.out.print("root path = '");
        System.out.print(rootPath);
        System.out.println("'");
        System.out.print("Threads = ");
        System.out.println(n);
        FileContent.setRootPath(rootPath);
        new Server(n).start();
    }
}
