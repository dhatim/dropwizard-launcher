package org.dhatim.mojo;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Mojo(name = "launch-dropwizard")
public class DropwizardLauncherMojo extends AbstractMojo {
    @Parameter(required = true)
    private String jarFile;

    @Parameter(required = true)
    private String confFile;

    @Parameter
    private List jvmarg = new ArrayList<>();

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        // create command to be launched
        List<String> command = new ArrayList<>();
        command.add("java");
        Optional.ofNullable(jvmarg).ifPresent(l -> command.addAll(l));
        command.add("-DSTOP.PORT=44156");
        command.add("-DSTOP.KEY=stop_key");
        command.add("-jar");
        command.add(jarFile);
        command.add("server");
        command.add(confFile);

        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.inheritIO();
        Process process;
        try {
            process = processBuilder.start();
            // add shutdown hook to end the process
            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    if (process.isAlive()) {
                        process.destroy();
                    }
                }
            });
        } catch (IOException ex) {
            throw new MojoFailureException("Problem at dropwizard process start", ex);
        }
        URL url;
        try {
            // get port number from configuration file
            YamlReader reader = new YamlReader(new FileReader(confFile));
            Map config = (Map) reader.read();
            String port = Optional.ofNullable((Map) config.get("server"))
                                    .filter(p -> Optional.ofNullable(p.get("adminConnectors")).isPresent())
                                    .filter(p -> ((List) (p.get("adminConnectors"))).size() > 0)
                                    .filter(p -> Optional.ofNullable(((Map)(((List) (p.get("adminConnectors"))).get(0))).get("port")).isPresent())
                                    .map(m -> ((Map)(((List) (m.get("adminConnectors"))).get(0))).get("port").toString())
                                    .orElse("8081");
            url = new URL("http://localhost:" + port + "/ping");
        } catch (MalformedURLException ex) {
            throw new MojoFailureException("url malformed", ex);
        } catch (FileNotFoundException ex) {
            throw new MojoFailureException("configuration file not found", ex);
        } catch (YamlException ex) {
            throw new MojoFailureException("yaml parsing error", ex);
        }
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                throw new MojoFailureException("Interrupted while waiting for server startup", ex);
            }
            if (process.isAlive()) {
                try {
                    url.openStream().close();
                    break;
                } catch (IOException ex) {
                    // waiting for connection
                }
            } else {
                throw new MojoFailureException("Process is not running");
            }
        }
    }
}
