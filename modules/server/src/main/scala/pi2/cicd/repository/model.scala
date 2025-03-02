package co.edu.eafit.dis.pi2.cicd
package repository
package model

import java.util.UUID
import java.time.Instant

final case class TodoData(
    todoId: UUID,
    reminder: String,
    dueTime: Instant,
    completionTime: Option[Instant]
)
