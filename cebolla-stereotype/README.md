# Cebolla Stereotype

The stereotypes used in ddd-cebolla are derived from the internal building blocks from Domain-Driven Design and are
specifically defined for their use within an Onion Architecture. Stereotypes can be used for various valuable purposes 
within the context of a Domain-Driven Design-based architecture and implementation:

* definition of the set of commonly understood architectural elements (building blocks) with defined names and semantics
* common language for design discussions and documentation
* stringent mapping of modelling concepts from Domain-Driven Design down to the code base
* definition of allowed dependencies between various architectural elements
* automated architecture governance (implementation of architectural rules)
* generation of source or byte code
* configuration of technical framework aspects without domain model pollution

Currently, the following stereotypes have proven to be useful when using an Onion Architecture:


## Bounded Context

_Internal Building Block: Bounded Context_

- represents a bounded context
- is usually represented as a module or package


## Value Object

_Internal Building Block: Value Object_

- represents a domain concept
- is named according to the ubiquitous language of the bounded context
- has no identity, only immutable state 
- may contain domain logic


## Entity

_Internal Building Block: Entity_

- represents a domain concept
- is named according to the ubiquitous language of the bounded context
- has a single identifying domain-related property 
- may contain mutable state / have a lifecycle
- may contain domain logic


## Aggregate

_Internal Building Block: Aggregate_

- represents a domain concept
- is named according to the ubiquitous language of the bounded context
- encapsulates some coherent domain logic and its related state
- represents the smallest unit of consistency
- represents a domain model sub-graph consisting of value objects and entities
- has a single identifying property (aggregate id)
- may contain mutable state / have a lifecycle


## Aggregate Id

_Internal Building Block: n/a_

- highlights the identifying property of an aggregate (or its accessor)
- is typically of a value object type (or basic type)


## Aggregate Factory

_Internal Building Block: Factory_

- encapsulates domain logic required to be known for constructing an instance of a specific aggregate
- may be implemented as a class or a static factory method 


## Domain Service

_Internal Building Block: Service_

- encapsulates domain logic acting across several aggregates / not naturally assignable to a specific aggregate


## Application Service

_Internal Building Block: n/a_

- implements a use case on top of domain logic
- provides access to domain logic from "outside"


## Repository

_Internal Building Block: Repository_

- keeps and provides access to existing aggregate instances
- interface is part of application core, implementation is part of infrastructure 


## Infrastructure Service

_Internal Building Block: n/a_

- represents a service expected by the application core to be provided by any type of infrastructure (database, 
  message broker, external systems, ...)
- interface is part of application core, implementation is part of infrastructure 


## Domain Event

_Internal Building Block: Domain Event_

- represents some business-relevant event that has happened
- carries no intention and no expectation on the component(s) handling the domain event
- is published either by aggregates or domain services


## Domain Event Publisher

_Internal Building Block: n/a_

- provides support for publishing domain events from either aggregates or domain services
- interface is part of application core, implementation is part of infrastructure 

 
## Domain Event Handler

_Internal Building Block: n/a_

- handles published domain events (either from the same or potentially from remote bounded contexts - if the domain 
  event from a remote bounded context is also part of the local domain model)


## Read Model

_Internal Building Block: n/a_

- represents a specific view on domain state built based on potentially multiple aggregates


## Command (Domain Task)

_Internal Building Block: n/a_

- represents an intention to be fulfilled by a specific component (aggregate, domain service or external bounded 
  context)

## Command Handler (Domain Task Handler)

_Internal Building Block: n/a_

- handles commands triggered by other components

## Query

_Internal Building Block: n/a_

- represents a query for specific state 


## Web Service

_Internal Building Block: n/a_

- provides access to specific domain logic to remote clients or user interfaces


## Message Handler

_Internal Building Block: n/a_

- handles messages retrieved from an external messaging system (e.g. messages resulting from domain events of remote 
  bounded contexts)
