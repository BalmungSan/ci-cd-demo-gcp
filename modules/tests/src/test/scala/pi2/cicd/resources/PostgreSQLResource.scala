package co.edu.eafit.dis.pi2.cicd
package resources

import config.DBConfig

import cats.effect.{IO, Resource}
import com.dimafeng.testcontainers.PostgreSQLContainer
import com.comcast.ip4s.{Host, Port}

object PostgreSQLResource:
  private val database = "test_db"
  private val user = "test_user"
  private val password = "1234"

  val make: Resource[IO, DBConfig] =
    ContainerResource
      .make(
        container = PostgreSQLContainer(
          databaseName = database,
          username = user,
          password = password
        )
      )
      .map { postgreSQLContainer =>
        DBConfig(
          host = Host.fromString(postgreSQLContainer.host).get,
          port = Port.fromInt(postgreSQLContainer.firstMappedPort).get,
          database = database,
          user = user,
          password = password
        )
      }
end PostgreSQLResource
