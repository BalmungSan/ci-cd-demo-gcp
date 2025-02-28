package co.edu.eafit.dis.pi2.cicd

import cats.effect.{IO, IOApp, Resource}

object Main extends IOApp.Simple:
  private val resources: Resource[IO, Unit] =
    for
      config <- config.load.toResource
      skunkSession <- repository.SkunkSession.make(config = config.db)
      todoRepository <- repository.TodoRepository.make(session = skunkSession)
      todoService = service.TodoService.make(repository = todoRepository)
      server <- server.make(
        config = config.server,
        service = todoService
      )
      _ <- IO.println(s"Server started in address: ${server}").toResource
    yield ()

  override final val run: IO[Unit] =
    resources.useForever
end Main
