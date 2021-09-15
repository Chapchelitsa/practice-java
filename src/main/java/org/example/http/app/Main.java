package org.example.http.app;

import lombok.extern.java.Log;
import org.example.http.framework.Server;
import org.example.http.framework.resolver.argument.*;

import java.util.logging.Level;

@Log
public class Main {
  public static void main(String[] args) {
    final var server = new Server();
    server.autoRegisterHandlers("org.example.http.app");
    server.addArgumentResolver(
        new RequestHandlerMethodArgumentResolver(),
        new ResponseHandlerMethodArgumentResolver(),
        new RequestHeaderHandlerMethodArgumentResolver(),
        new RequestParseQueryHandlerMethodArgumentReslover(),
        new RequestParseBodyHandlerMethodArgumentResolver()
    );
   /*
    new Thread(() -> {
     try {
        for(int i = 1; i <= 5; i++) {
          Thread.sleep(1000);
          log.log(Level.INFO, "" + i);
        }
        server.stop();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    ).start();
    */
    server.listen(9999);
  }
}
