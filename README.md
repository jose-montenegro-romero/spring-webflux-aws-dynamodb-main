## spring-webflux-aws-dynamodb
Reactive REST API with AWS DynamoDB and Spring WebFlux

## Description
Reactive REST API using Spring WebFlux and AWS DynamoDB.

## LocalStack Commands

### Estado de localstack:

`http://localhost:4566/_localstack/health`

### Listar tablas de Dynamodb:

`aws dynamodb list-tables --endpoint-url http://localhost:4566`

Puedes hacer consultas usando scan para obtener todos los elementos:

`
aws dynamodb scan \
--endpoint-url http://localhost:4566 \
--table-name author
`

O puedes realizar una consulta query para obtener un elemento específico según la clave primaria:

`
aws dynamodb query \
--endpoint-url http://localhost:4566 \
--table-name author \
--key-condition-expression "id = :id" \
--expression-attribute-values '{":id":{"S":"1"}}'
`

### Crear tabla de dynamodb para Author

`
aws dynamodb create-table \
--endpoint-url http://localhost:4566 \
--table-name author \
--attribute-definitions AttributeName=id,AttributeType=S \
--key-schema AttributeName=id,KeyType=HASH \
--provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5
`
### Agregar datos a la tabla Author
`
aws dynamodb put-item \
--endpoint-url http://localhost:4566 \
--table-name author \
--item '{
"id": {"S": "1"},
"lastname": {"S": "Smith"},
"middleName": {"S": "J"},
"firstname": {"S": "John"}
}'
`

`
aws dynamodb put-item \
--endpoint-url http://localhost:4566 \
--table-name author \
--item '{
"id": {"S": "2"},
"lastname": {"S": "Doe"},
"middleName": {"S": "A"},
"firstname": {"S": "Alice"}
}'
`