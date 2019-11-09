# ddd-cebolla
Playground for exploring lightweight utilities and samples for building applications following the ideas of 
Domain-Driven Design and Onion Architecture.



## Goal
The project aims to provide lightweight, yet powerful utilities for building applications based on the concepts of 
Domain-Driven Design and following the Onion Architecture approach. The utilities should stay as non-invasive and 
technology-independent as possible, imposing as little code dependency to ddd-cebolla as possible on the project 
application core code.

## Requirements

This project requires Java 11 or higher.


## Utilities

Currently, ddd-cebolla contains the following utilities (mostly still in prototype mode):


### cebolla-stereotype

Provides stereotypes used to identify and qualify corresponding elements in the code base. 

Currently, the following stereotypes are available:

- `@ValueObject`: represents a value object
- `@Aggregate`: represents an aggregate
- `@AggregateId`: represents the property (method) identifying an aggregate

More stereotypes will be added as required / supported.

In the future, the usage of these stereotypes should be optional for all other features provided by ddd-cebolla, instead
custom stereotype annotations will be configurable in order to keep the application code base (or at least the 
application core) free of any direct dependency to ddd-cebolla.


### cebolla-compiler-plugin

Provides a plugin for the Javac compiler for generating code for various stereotypes.

Currently, the following features are supported:

#### Code Generation
- generate `equals()` and `hashCode()` for value objects based on its entire state
- generate `equals()` and `hashCode()` for aggregates based on its aggregate id
- generate default constructor for value objects and aggregates

#### Compile Errors
- compile error if an aggregate has no aggregate id
- compile error if a class has multiple stereotypes

More aspects will be supported soon :-)


### cebolla-sample

Provides a sample application built using Onion Architecture and ddd-cebolla.

Currently only contains an initial description of some use cases to be implemented. Please see 
https://github.com/cstettler/ddd-to-the-code-workshop-sample for a more complete sample (not built on ddd-cebolla, but 
using similar mechanisms).



## Future Ideas

The following ideas exist in the context of ddd-cebolla:

- architecture governance: plugin with concepts, constraints and reports based on stereotypes for jQAssistant
- domain event support: infrastructure for publishing and receiving domain events (within the same bounded context and 
between different bounded contexts)
- ...