package ru.petrelevich.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import ru.petrelevich.domain.Message;
import ru.petrelevich.domain.MessageDto;
import ru.petrelevich.service.DataStore;

@RestController
public class DataController {
    private static final Logger log = LoggerFactory.getLogger(DataController.class);
    private final DataStore dataStore;
    private final Scheduler workerPool;

    public DataController(DataStore dataStore, Scheduler workerPool) {
        this.dataStore = dataStore;
        this.workerPool = workerPool;
    }

    @PostMapping(value = "/msg/{roomId}")
    public Mono<Long> messageFromChat(@PathVariable("roomId") String roomId, @RequestBody MessageDto messageDto) {
        var messageStr = messageDto.messageStr();

        var msgId = Mono.just(new Message(null, roomId, messageStr))
                .doOnNext(msg -> log.info("messageFromChat:{}", msg))
                .flatMap(dataStore::saveMessage)
                .publishOn(workerPool)
                .doOnNext(msgSaved -> log.info("msgSaved id:{}", msgSaved.id()))
                .map(Message::id)
                .subscribeOn(workerPool);

        log.info("messageFromChat, roomId:{}, msg:{} done", roomId, messageStr);
        return msgId;
    }

    @GetMapping(value = "/msg", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<MessageDto> getMessages(@RequestParam(value = "roomId", required = false) String roomId) {
        Flux<Message> messagesFlux = (roomId != null)
                ? dataStore.loadMessages(roomId).doOnSubscribe(sub -> log.info("getMessagesByRoomId, room: {}", roomId))
                : dataStore.loadAllMessages().doOnSubscribe(sub -> log.info("getAllMessages"));
        return messagesFlux.map(message -> new MessageDto(message.msgText()))
                .doOnNext(msgDto -> log.info("msgDto:{}", msgDto))
                .subscribeOn(workerPool);
    }

}
