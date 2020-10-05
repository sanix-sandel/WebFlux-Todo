package com.sanix.ToDoWebFlux.reactive;

import com.sanix.ToDoWebFlux.domain.ToDo;
import com.sanix.ToDoWebFlux.repository.ToDoRepository;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;

@Component
public class ToDoHandler {

    private ToDoRepository toDoRepository;

    public ToDoHandler(ToDoRepository toDoRepository) {
        this.toDoRepository = toDoRepository;
    }

    public Mono<ServerResponse> getToDo(ServerRequest request){
        String toDoId=request.pathVariable("id");
        Mono<ServerResponse> notFound=ServerResponse.notFound().build();
        Mono<ToDo> toDo=toDoRepository.findById(toDoId);
        return toDo
                .flatMap(t->
                                ServerResponse
                            .ok()
                            .contentType(APPLICATION_JSON)
                            .body(fromObject(t))
                .switchIfEmpty(notFound)

                );
    }

    public Mono<ServerResponse> getToDos(ServerRequest request){
        Flux<ToDo> toDos=toDoRepository.findAll();
        return ServerResponse
                .ok()
                .contentType(APPLICATION_JSON)
                .body(toDos, ToDo.class);
    }
}
