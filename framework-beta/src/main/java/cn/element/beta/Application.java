package cn.element.beta;

import cn.element.beta.server.ApplicationServer;
import org.apache.catalina.LifecycleException;

public class Application {

    public static void main(String[] args) throws LifecycleException {
        ApplicationServer.run();
    }
}
