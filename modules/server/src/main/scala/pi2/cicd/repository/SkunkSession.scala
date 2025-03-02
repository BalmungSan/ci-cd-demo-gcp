package co.edu.eafit.dis.pi2.cicd
package repository

import config.DBConfig

import cats.effect.{IO, Resource}
import skunk.Session
import org.typelevel.otel4s.trace.Tracer

object SkunkSession:
  def make(
      config: DBConfig
  ): Resource[IO, Session[IO]] =
    given Tracer[IO] = Tracer.noop
    for
      dbHost <- config.host.resolve[IO].toResource
      session <- Session.single[IO](
        host = dbHost.toUriString,
        port = config.port.value,
        database = config.database,
        user = config.user,
        password = Some(config.password)
      )
    yield session
  end make
end SkunkSession
