package co.edu.eafit.dis.pi2.cicd

import config.TodoAppConfig

import cats.effect.{IO, Resource}
import org.http4s.server.Server

object TodoApp:
  def make(
      config: TodoAppConfig
  ): Resource[IO, Server] =
    for
      skunkSession <- repository.SkunkSession.make(config = config.db)
      _ <- repository.migrations.run(session = skunkSession).toResource
      todoRepository = repository.TodoRepository.make(session = skunkSession)
      todoService = service.todo.make(repository = todoRepository)
      server <- server.make(
        config = config.server,
        service = todoService
      )
    yield server
  end make
end TodoApp
