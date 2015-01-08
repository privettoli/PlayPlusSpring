package org.spend.devblog.config;

import play.libs.Akka;
import scala.concurrent.ExecutionContext;

public interface ExecutionContexts {
    ExecutionContext databaseExecutionContext = Akka.system().dispatchers().lookup("akka.db-dispatcher");
}
