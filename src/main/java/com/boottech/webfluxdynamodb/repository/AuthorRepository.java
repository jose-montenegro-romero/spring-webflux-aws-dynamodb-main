package com.boottech.webfluxdynamodb.repository;

import com.boottech.webfluxdynamodb.domain.Author;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.PutItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.UpdateItemEnhancedRequest;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class AuthorRepository {
    public static final String TABLE_NAME = "author";
    private final DynamoDbAsyncTable<Author> authorTable;

    public AuthorRepository(DynamoDbEnhancedAsyncClient dynamoDbClient) {
        authorTable = dynamoDbClient
                .table(TABLE_NAME, TableSchema.fromBean(Author.class));
    }

    public Flux<Author> findAll() {
        return Flux.from(authorTable.scan().items());
    }

    public Mono<Author> findById(String id) {
        return Mono.fromFuture(authorTable.getItem(getKeyBuild(id)));
    }

    public Mono<Author> delete(String id) {
        return Mono.fromCompletionStage(authorTable.deleteItem(getKeyBuild(id)));
    }

    public Mono<Integer> count() {
        ScanEnhancedRequest scanEnhancedRequest = ScanEnhancedRequest.builder().addAttributeToProject("id").build();
        AtomicInteger counter = new AtomicInteger(0);
        return Flux.from(authorTable.scan(scanEnhancedRequest))
                .doOnNext(page -> counter.getAndAdd(page.items().size()))
                .then(Mono.defer(() -> Mono.just(counter.get())));
    }

    public Mono<Author> update(Author entity) {
        var updateRequest = UpdateItemEnhancedRequest.builder(Author.class).item(entity).build();
        return Mono.fromCompletionStage(authorTable.updateItem(updateRequest));
    }

    public Mono<Author> save(Author entity){
        entity.setId(UUID.randomUUID().toString());

        var putRequest = PutItemEnhancedRequest.builder(Author.class).item(entity).build();
        return Mono.fromCompletionStage(authorTable.putItem(putRequest).thenApply(x -> entity));
    }

    private Key getKeyBuild(String id) {
        return Key.builder().partitionValue(id).build();
    }
}
