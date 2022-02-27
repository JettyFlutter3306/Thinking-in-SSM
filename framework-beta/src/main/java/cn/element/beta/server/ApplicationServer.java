package cn.element.beta.server;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

import java.io.File;

public class ApplicationServer {

    private static final int port = 8080;
    private static final String path = "framework-beta/src/main/webapp";
    private static final Tomcat tomcat;

    static {
        tomcat = new Tomcat();
    }

    public static void run() throws LifecycleException {
        tomcat.setPort(port);
        tomcat.getConnector();

        Context ctx = tomcat.addWebapp("", new File(path).getAbsolutePath());
        WebResourceRoot resources = new StandardRoot(ctx);
        resources.addPreResources(new DirResourceSet(
                resources,
                "/WEB-INF/classes",
                new File(path).getAbsolutePath(),
                "/"
            )
        );
        ctx.setResources(resources);

        tomcat.start();
        tomcat.getServer().await();
    }

}
