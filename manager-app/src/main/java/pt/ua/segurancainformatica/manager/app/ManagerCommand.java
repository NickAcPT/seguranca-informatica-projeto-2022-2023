package pt.ua.segurancainformatica.manager.app;

import picocli.CommandLine;

@CommandLine.Command(name = "manager", description = "Tool for managing licenses",
        mixinStandardHelpOptions = true, subcommands = {
        KeyCommands.class,
        ReleaseCommands.class,
        LicenseCommands.class
})
public class ManagerCommand {

    public static void main(String[] args) {
        int exitCode = new CommandLine(new ManagerCommand()).execute(args);
        System.exit(exitCode);
    }

}
