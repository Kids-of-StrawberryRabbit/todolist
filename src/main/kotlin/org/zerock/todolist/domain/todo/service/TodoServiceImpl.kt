package org.zerock.todolist.domain.todo.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.zerock.todolist.domain.exception.ModelNotFoundException
import org.zerock.todolist.domain.todo.dto.CreateTodoRequest
import org.zerock.todolist.domain.todo.dto.TodoResponse
import org.zerock.todolist.domain.todo.dto.UpdateTodoRequest
import org.zerock.todolist.domain.todo.model.Todo
import org.zerock.todolist.domain.todo.model.toResponse
import org.zerock.todolist.domain.todo.repository.TodoRepository

@Service
class TodoServiceImpl(
    private val todoRepository: TodoRepository
) : TodoService {

    override fun getAllTodoList(pageable: Pageable, writer: String?): Page<TodoResponse> {
        if (writer != null) {
            return todoRepository.findByWriter(writer, pageable).map { it.toResponse() }
        } else {
            return todoRepository.findAll(pageable).map { it.toResponse() }
        }
    }

    override fun getTodoById(todoId: Long): TodoResponse {
        val todo = todoRepository.findByIdOrNull(todoId) ?: throw ModelNotFoundException("Todo", todoId)
        return todo.toResponse()
    }

    @Transactional
    override fun createTodo(request: CreateTodoRequest): TodoResponse {
        return todoRepository.save(
            Todo(
                title = request.title,
                content = request.content,
                writer = request.writer
            )
        ).toResponse()
    }

    @Transactional
    override fun updateTodo(todoId: Long, request: UpdateTodoRequest): TodoResponse {
        val todo = todoRepository.findByIdOrNull(todoId) ?: throw ModelNotFoundException("Todo", todoId)
        val (title, content, writer) = request

        todo.title = title
        todo.content = content
        todo.writer = writer

        return todoRepository.save(todo).toResponse()
    }

    @Transactional
    override fun deleteTodo(todoId: Long) {
        val todo = todoRepository.findByIdOrNull(todoId) ?: throw ModelNotFoundException("Todo", todoId)
        todoRepository.delete(todo)
    }

//    override fun paging(pageable: Pageable): Page<Todo> {
//        val todo = QTodo.todo
//
//        val query = from(todo)
//
//        query.where(todo.id.gt(pageable.offset))
////        query.orderBy(pageable.) // TODO: 정렬 기준을 받아 오름차순, 내림차순 정렬
//        query.limit(pageable.pageSize.toLong())
//
//        this.querydsl?.applyPagination(pageable, query)
//
//        val content = query.fetch()
//
//        val totalCount = query.fetchCount()
//
//        return PageImpl(content, pageable, totalCount)
//    }
}