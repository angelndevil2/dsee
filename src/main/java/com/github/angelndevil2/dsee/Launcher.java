package com.github.angelndevil2.dsee;

import com.github.angelndevil2.dsee.util.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;

/**
 * @author k, Created on 16. 2. 21.
 */
@Slf4j
public class Launcher {

    private static String vmArgs;

    /**
     * for command line to attach this agent.
     *
     * @param args command line arguments
     */
    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws IOException, ParseException {

        CmdOptions options = new CmdOptions();

        options.setArgs(args);

        CommandLine cmd = options.getCmd();
        if (cmd.hasOption('h')) {
            options.printUsage();
            return;
        }

        if (cmd.hasOption("d")) {
            try {

                PropertiesUtil.setDirs(cmd.getOptionValue("d").trim());

            } catch (IOException e) {

                System.err.println(PropertiesUtil.getConfDir() + File.separator + PropertiesUtil.AppProperties + " not found. may use -d option" + e);
                return;
            }
        }

        if (cmd.hasOption('p')) {

            String pid = cmd.getOptionValue('p');
            if (pid == null) throw new NullPointerException("pid is null");

                try {
                    Class vmClass = Class.forName("com.sun.tools.attach.VirtualMachine");
                    Object virtualMachine = vmClass.getMethod("attach", String.class).invoke(null, pid);
                    String jarName = Bootstrap.findPathJar(null);
                    virtualMachine.getClass().getMethod("loadAgent", String.class, String.class).invoke(virtualMachine, jarName, vmArgs);
                    log.debug(jarName + " registered.");
                    virtualMachine.getClass().getMethod("detach").invoke(virtualMachine);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.exit(0);
                }

        } else {
            System.out.println("d or p options is required");
            options.printUsage();
        }
    }
}
