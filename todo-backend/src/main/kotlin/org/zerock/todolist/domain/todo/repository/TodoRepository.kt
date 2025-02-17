package org.zerock.todolist.domain.todo.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.zerock.todolist.domain.todo.model.Todo

interface TodoRepository : JpaRepository<Todo, Long> {

    fun findByWriter(writer: String, pageable: Pageable) : Page<Todo>
}