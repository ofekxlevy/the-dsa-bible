# Data Structures & Algorithms — The DSA Bible

A comprehensive educational implementation of the data structures and algorithms covered in the **Data Structures** course.

The project is written in **Java** and implements the core data structures and algorithms **from scratch**, with an emphasis on understanding how they work internally rather than relying on Java's built-in data structure implementations.

Each implementation includes documentation explaining the underlying ideas, important design decisions, and time complexity where relevant.

---

## 🎯 Project Goals

The main goals of this project are:

- Implement the fundamental data structures from scratch.
- Implement the main algorithms studied in the Data Structures course.
- Understand the internal mechanics behind commonly used data structures.
- Practice generic and object-oriented programming in Java.
- Analyze the time complexity of operations and algorithms.
- Create a reusable reference for reviewing Data Structures & Algorithms.

---

## 📚 Data Structures

### Lists

Implementations of fundamental sequential data structures:

- Dynamic Array
- Singly Linked List
- Doubly Linked List
- Custom List interface
- Custom Iterators

---

### Stacks & Queues

Implementations of:

- Stack
- Queue
- Stack implemented using a Queue

The implementations reuse data structures implemented elsewhere in the project rather than Java's built-in collections.

---

### Trees

Implementations of several tree-based data structures:

- Binary Tree
- Binary Search Tree (BST)
- AVL Tree
- Order Statistic Tree (OST)

The AVL Tree maintains logarithmic height using rotations.

The Order Statistic Tree extends the idea of a balanced search tree by maintaining subtree sizes, allowing efficient operations such as:

- `select(index)`
- `rank(value)`

---

### Heap

Implementation of:

- Binary Heap

The heap supports the fundamental heap operations as well as operations required by other algorithms in the project, such as `decreaseKey`.

---

### Skip List

Implementation of a randomized **Skip List**, providing expected logarithmic-time search, insertion, and deletion.

---

### Hash Tables

Implementations of hash tables using several collision-resolution strategies:

- Separate Chaining
- Linear Probing with Tombstones
- Linear Probing with Reinsertion

These implementations demonstrate the differences between chaining and open addressing, including the complications introduced by deletion in linear probing.

---

### Disjoint Sets

Implementation of the **Disjoint Set Forest / Union-Find** data structure.

Optimizations include:

- Union by Rank
- Path Compression

These optimizations provide near-constant amortized running time and are used by Kruskal's Minimum Spanning Tree algorithm.

---

## 🔗 Graphs

The project contains graph representations for vertices:

`1, 2, ..., n`

where `n` is defined when the graph is created.

### Basic Graphs

Graph representations and fundamental traversal algorithms:

- Edge
- Graph
- Adjacency List Graph
- Breadth-First Search (BFS)
- Depth-First Search (DFS)

### Weighted Graphs

Support for weighted graphs:

- Weighted Edge
- Weighted Graph
- Weighted Adjacency List Graph

### Minimum Spanning Trees

Implementations of:

- Prim's Algorithm
- Kruskal's Algorithm

Prim's algorithm uses the project's Binary Heap implementation, while Kruskal's algorithm uses the project's Disjoint Set Forest implementation.

---

## 🔀 Sorting Algorithms

Implementations of several fundamental sorting algorithms:

- Merge Sort
- Quick Sort
- Heap Sort
- Deterministic Quick Sort using Median of Medians

Where possible, the algorithms reuse data structures implemented in this project.

For example, Heap Sort uses the project's own Binary Heap implementation.

---

## 🎯 Selection Algorithms

The project includes deterministic selection using:

- Median of Medians
- DSelect

These algorithms demonstrate how an element with a given order statistic can be found in linear worst-case time.

---

## 🗜️ Compression Algorithms

Implementations of lossless compression algorithms studied in the course:

- Huffman Coding
- Lempel–Ziv

### Huffman Coding

Huffman Coding constructs a prefix-code tree according to character frequencies.

More frequent symbols receive shorter binary representations, while less frequent symbols receive longer representations.

### Lempel–Ziv

Lempel–Ziv compression builds a dictionary based on repeated patterns in the input, allowing repeated sequences to be represented efficiently.

---

## ⏱️ Complexity Analysis

A major focus of the project is not only implementing the algorithms, but understanding their performance.

Implementations contain documentation describing the relevant time complexities.

Some examples:

| Data Structure / Algorithm | Operation | Complexity |
|---|---|---:|
| Dynamic Array | Access | O(1) |
| Linked List | Access | O(n) |
| Stack | Push / Pop | O(1) |
| Queue | Enqueue / Dequeue | O(1) |
| BST | Search | O(h) |
| AVL Tree | Search / Insert / Delete | O(log n) |
| Binary Heap | Insert / Extract | O(log n) |
| Hash Table | Search / Insert | O(1) expected |
| Skip List | Search / Insert / Delete | O(log n) expected |
| BFS | Traversal | O(V + E) |
| DFS | Traversal | O(V + E) |
| Prim | MST | Depends on graph and heap representation |
| Kruskal | MST | O(E log E) |
| Merge Sort | Sorting | O(n log n) |
| Heap Sort | Sorting | O(n log n) |
| Quick Sort | Sorting | O(n log n) expected |
| DSelect | Selection | O(n) worst case |

---

## 🧠 Implementation Philosophy

This project intentionally avoids using Java's built-in data structures when the goal is to understand or implement that data structure directly.

For example, instead of treating `ArrayList`, `LinkedList`, `Stack`, `Queue`, or similar classes as black boxes, the relevant structures are implemented manually.

Higher-level algorithms then reuse these implementations.

This creates a hierarchy similar to:

```text
Primitive Structures
        ↓
Lists / Trees / Hash Tables / Heaps
        ↓
Stacks / Queues / Disjoint Sets / Graphs
        ↓
Graph & Sorting Algorithms
        ↓
Compression and Advanced Algorithms
```

This approach makes the project more than a collection of isolated implementations: data structures developed earlier are used as building blocks for more advanced algorithms.

---

## 🛠️ Technologies

- Java
- IntelliJ IDEA
- Git
- GitHub

No external data-structure libraries are required.

---

## 🎓 Educational Purpose

This repository was created as a personal implementation of the material covered in a university-level **Data Structures** course.

The purpose is educational: to understand not only **how to use** common data structures and algorithms, but also **how they are implemented**, **why they work**, and **what their computational costs are**.

The project can therefore serve as:

- A study resource
- A reference before exams
- Practice for technical interviews
- A foundation for further algorithmic study
- A demonstration of fundamental Data Structures & Algorithms knowledge

---

## 🚧 Status

The project is continuously expanded as additional data structures and algorithms are implemented or improved.

The goal is to maintain a clean, well-documented collection of the fundamental algorithms and data structures studied throughout the course.

---

## 📄 License

This project is intended primarily for educational purposes.