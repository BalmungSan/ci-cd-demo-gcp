package co.edu.eafit.dis.pi2.cicd
package resources

import config.{TodoAppConfig, ServerConfig}

import cats.effect.{IO, Resource}
import org.http4s.server.Server
import com.comcast.ip4s.{Host, Port}

object TodoAppResource:
  val make: Resource[IO, Server] =
    PostgreSQLResource.make.flatMap { dBConfig =>
      TodoApp.make(
        config = TodoAppConfig(
          db = dBConfig,
          server = ServerConfig(
            host = Host.fromString("localhost").get,
            port = Port.fromInt(8181).get
          )
        )
      )
    }
end TodoAppResource
