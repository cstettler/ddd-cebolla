# ddd-cebolla
lightweight framework for building applications following the ideas of ddd and onion architecture

_Note: the following description outlines the features, concepts and aspects to be supported by the ddd-cebolla framework in a later stage and are thus still just plain ideas on paper. Step by step, these features are going to be integrated into the ddd-cebolla framework in the form of a reusable Java code library._

## Goal
The project aims to provide a lightweight, yet powerful framework for building applications based on the concepts of domain-driven design and following the onion architecture approach. The ddd-cebolla framework aims to stay as non-invasive and technology-independent as possible, while still providing out-of-the-box support for all relevant architectural concepts of domain-driven design.

The ddd-cebolla framework does not strictly follow principles such as CQRS or EventSourcing, but applies a pragmatic approach to implementing domain-driven design that is not "radical" and thus, should also be applicable in more traditional enterprise environments.

## Supported Concepts
The ddd-cebolla framework supports the following concepts:

- Commands
- Queries
- Application Services
- Domain Services
- Domain Events (incl. Domain Event Publisher and Domain Event Handler)
- Domain Tasks (incl. Domain Task Scheduler, Domain Task Executor and Domain Task Result Consumer)
- Domain Objects (incl. Aggregates, Entities and Value Objects)
- Repositories
- Infrastructure Services

In addition to these concepts, it also provides support for the following aspects:

- transactions
- persistence
- security (authentication and authorization)

## Semantics
The concepts outlined above have the following semantics:

### Commands
- represent a single "business use case" from the perspective of the domain
- are used to trigger business logic within the domain
- contain only the data required to "configure" the command, but not the logic itself
- can optionally declare a "handle" to be returned as result of the command execution (typically an id of an aggregate or a value object, "handle" has to be provided by the handling application service))
- are handled by application services
- can (indirectly) manipulate state within the domain
- are executed within a single transaction

### Queries
- are used to retrieve data from the domain
- contain only the data required to "configure" the query, but not the query logic itself
- describe the type of the requested result (result has to be provided by the handling application service)
- are handled by application services
- cannot manipulate state within the domain
- are executed within a read-only transaction
- can return arbitrary domain objects

### Application Services
- are stateless
- handle commands and queries
- can orchestrate multiple domain services, repositories and business logic of aggregates in order to completely fulfill a business use case
- collaborate with domain services, repositories and aggregates
- can publish domain events and schedule domain tasks

### Domain Services
- are stateless
- implement business logic that needs to span multiple aggregates (typically more complex algorithms)
- can listen to domain events
- can execute domain tasks
- can handle domain task results

### Domain Events
- represent a business-relevant fact that has happened within the domain
- can be used to notify interested parties about the business-relevnt fact
- are immutable
- contain only minimal data required to further describe the business-relevant fact and to allow proper handling of the domain event by any registered domain event handler
- are published with the help of a domain event publisher and optionally handled by one or more domain event handlers (domain services)
- are "fire-and-forget" from the perspective of the publishing business logic (publishing business logic does not get any guarantee if, and when a domain events gets handled by any domain event handler, and as a consequence, must not rely on the handling of the corresponding domain event in order to succeed)
- are only published, if the transaction of the business logic publishing the domain event commits successfully
- are executed within a separate transaction, detached from the transaction that published the domain event
- are guaranteed to eventually be delivered at-least-once to all registered domain event handlers
- will be marked as handled once transaction of the target domain event handler commits successfully
- will be redelivered to the target domain event handler in case the transaction of the target domain event handler fails
- will be marked as failed after a defined amount of attempts for redelivery
- can be delivered across domain and application boundaries

### Domain Event Handler
- are stateless
- listen to specific types of domain events and handle them according to the business semantics of domain event in the receiving domain
- are invoked in a separate transaction per domain event
- can execute arbitrary business logic as a consequence of receiving a domain event

### Domain Tasks
- represent a "piece of work" to be done by a domain task executor when being instructed so by the business logic scheduling the domain task
- can be used to execute additional business logic asynchronously from the perspective of the scheduling business logic
- are immutable
- contain only minimal data required to further parametrize the domain task and to allow proper execution of the domain task by the target domain task executor
- can optionally describe a result of the domain task execution (result has to be provided by the domain task executor and is provided to domain task result consumer)
- are scheduled with the help of a domain task scheduler and executed by exactly one domain task executor
- are guaranteed to eventually at-least-once be delivered to and executed by a domain task executor
- are only scheduled for execution, if the transaction of the business logic scheduling the domain task commits successfully
- are executed within a separate transaction, detached from the transaction that scheduled the domain task
- will be marked as executed once transaction of the target domain task executor commits successfully
- will be redelivered to the target domain task executor in case the transaction of the target domain task executor fails
- will be marked as failed after a defined amount of attempts for redelivery
- can be delivered across domain- and application-boundaries

### Domain Task Executor
- are stateless
- are responsible for handling domain tasks of a specific type according to the business semantics of the domain task
- are invoked in a separate transaction per domain task
- can execute arbitrary business logic as a consequence of receiving a domain task
- will return the optionally described result of the domain task

### Domain Task Result Consumer
- are stateless
- are provided with the optionally described result of a domain task execution (provided by the corresponding domain task executor)
- are invoked in a separate transaction per domain task result
- can execute arbitrary business logic as a consequence of receiving a domain task result

### Domain Objects
- are either aggregates, entities or value objects
- contain state and (optionally) business logic
- can use domain services
- can publish domain events and schedule domain tasks

#### Aggregate
- represent a "concept" with state and business logic in the business domain
- consist of entities and/or value objects
- are managed by an aggregate root (an entity)
- ensure the business invariants of the complete aggregate including all contained entities and value objects
- have their identity defined by a specific aggregate id property (the entity id of the aggregate root entity)
- are only referred to by other aggregates via their aggregate id

#### Entities
- represent a mutable, identifiable domain object
- have their identity defined by a specific entity id property
- can only be part of an aggregate

#### Value Objects
- represent an immutable domain object
- have their identity defined by the sum of their internal state

### Repositories
- keep aggregates for later retrieval
- are used by application and domain services to store newly created aggregates
- provide access to existing aggregates (specific aggregates and collections of aggregates according to criteria)
- provide "collection semantics" (modifications to existing aggregates already known to the repository are kept without again explicitly storing the aggregate in the repository)
- can optionally persist aggregates in a database

### Infrastructure Services
- provide access to business logic and state of an external domain
- can perform (remote) invocations across domain and application boundaries
- can optionally cache data of external domains
