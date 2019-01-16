package org.dhatim.mojo;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Mojo(name = "stop-dropwizard")
public class DropwizardStopperMojo extends AbstractMojo {

    @Parameter
    private int stopPort = 44156;

    @Parameter
    private boolean skip;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (skip) {
            return;
        }

        Socket socket;
        try {
            socket = new Socket("localhost", stopPort);
        } catch (IOException ex) {
            throw new MojoFailureException("socket connection error", ex);
        }
        PrintWriter out;
        try {
            out = new PrintWriter(socket.getOutputStream());
        } catch (IOException ex) {
            throw new MojoFailureException("socket output error", ex);
        }
        out.print("stop_key\r\nstop\r\n");
        out.flush();
        try {
            socket.close();
        } catch (IOException ex) {
            throw new MojoFailureException("socket closure error", ex);
        }
    }
}
