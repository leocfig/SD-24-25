# TupleSpaces

Distributed Systems Project 2025
 
**Group A02**

**Difficulty level: I am Death incarnate!**


### Code Identification

In all source files (namely in the *groupId*s of the POMs), replace __GXX__ with your group identifier. The group
identifier consists of either A or T followed by the group number - always two digits. This change is important for 
code dependency management, to ensure your code runs using the correct components and not someone else's.

### Team Members

| Number  | Name              | User                                     | Email                                      |
|---------|-------------------|------------------------------------------|--------------------------------------------|
| 106157  | Leonor Figueira   | <https://github.com/leocfig>             | <leonor.figueira@tecnico.ulisboa.pt>       |
| 106198  | Sofia Lopes       | <https://github.com/sofiarodrigueslopes> | <sofia.rodrigues.lopes@tecnico.ulisboa.pt> |
| 106322  | Raquel Rodrigues  | <https://github.com/RaquelASRodrigues>   | <raquel.c.rodrigues@tecnico.ulisboa.pt>    |

## Getting Started

The overall system is made up of several modules.
The definition of messages and services is in _Contract_.

See the [Project Statement](https://github.com/tecnico-distsys/Tuplespaces-2025) for a complete domain and system description.

### Prerequisites

The Project is configured with Java 17 (which is only compatible with Maven >= 3.8), but if you want to use Java 11 you
can too -- just downgrade the version in the POMs.

To confirm that you have them installed and which versions they are, run in the terminal:

```s
javac -version
mvn -version
```

### Setting up the virtual environment

To ensure dependencies are managed correctly, it's recommended to use a Python virtual environment.

To create and activate the virtual environment, run in the project's root directory:

```s
python3 -m venv .venv
source .venv/bin/activate
```

Once the virtual environment is active, install the project dependencies:

```s
pip install -r requirements.txt
```

### Installation

To compile and install all modules:

```s
mvn clean install
```

### Tests

Since the TupleSpaces uses a Hashmap to store the tuples, the getTupleSpacesState command
will not print the tuples in insertion order. For this reason it might not match the 
expected output for some tests. 

## Built With

* [Maven](https://maven.apache.org/) - Build and dependency management tool;
* [gRPC](https://grpc.io/) - RPC framework.
