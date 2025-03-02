package co.edu.eafit.dis.pi2.cicd
package repository

import model.TodoData

import cats.effect.IO
import skunk.{Codec, Session}
import skunk.codec.all.*
import skunk.syntax.all.sql

import java.util.UUID
import java.time.{Instant, ZoneOffset}

trait TodoRepository:
  def saveTodo(todo: TodoData): IO[Unit]
  def markTodoAsCompleted(todoId: UUID, completionTime: Instant): IO[Unit]
  def listTodos: IO[List[TodoData]]

object TodoRepository:
  private val instant: Codec[Instant] =
    timestamptz.imap(_.toInstant)(_.atOffset(ZoneOffset.UTC))

  private val todoData: Codec[TodoData] =
    (uuid *: varchar *: instant *: instant.opt).to[TodoData]

  def make(
      session: Session[IO]
  ): TodoRepository =
    new TodoRepository:
      override def saveTodo(todo: TodoData): IO[Unit] =
        session
          .execute(
            command = sql"INSERT INTO todos VALUES ${todoData}".command
          )(
            args = todo
          )
          .void

      override def markTodoAsCompleted(
          todoId: UUID,
          completionTime: Instant
      ): IO[Unit] =
        session
          .execute(
            command = sql"""UPDATE todos
                      SET completionTime = ${instant}
                      WHERE todoId = ${uuid}
                   """.command
          )(
            args = (completionTime, todoId)
          )
          .void

      override def listTodos: IO[List[TodoData]] =
        session.execute(
          query = sql"SELECT * FROM todos".query(todoData)
        )
  end make
end TodoRepository
