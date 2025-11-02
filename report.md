# Assignment 4 – Smart City / Smart Campus Scheduling

### 🎯 Goal
The goal of this project is to combine two main topics from the DAA course:
**Strongly Connected Components (SCC)** and **Shortest / Longest Paths in DAGs**.  
The task simulates Smart City scheduling where some tasks depend on each other.  
We need to find cyclic dependencies, compress them, and build an optimal order of execution.

---

### ⚙️ Implementation
The project is written in Java using Maven.  
It includes several main parts:

- **Kosaraju SCC** – finds all strongly connected components
- **Kahn Topological Sort** – builds the correct task order (DAG)
- **Shortest and Longest Path algorithms** – find optimal and critical routes
- **Metrics** – count operations and execution time
- **JUnit Tests** – check correctness of algorithms

Input data is stored in `data/tasks.json` (directed graph with weights).

---

### 📊 Dataset
| Parameter | Value |
|------------|--------|
| Vertices | 8 |
| Edges | 7 |
| Directed | true |
| Source | 4 |
| Weight model | edge weights |
| Cycle | yes (1 → 2 → 3 → 1) |

---

### 🧮 Results
SCC count = 6
Topo order (components): [0, 4, 1, 5, 2, 3]
Shortest path: 0 → 1 → 2 → 3 (length = 8)
Critical path: 0 → 1 → 2 → 3 (length = 8)


| Algorithm | Operations | Time (ms) |
|------------|-------------|-----------|
| SCC (Kosaraju) | dfsVisits=16, dfsEdges=14 | 0.069 |
| Topological Sort (Kahn) | queuePush=6, queuePop=6 | 0.010 |
| Shortest Path | relaxations=3 | 0.027 |
| Longest Path | relaxations=3 | 0.024 |

---

### 🔍 Analysis
The SCC algorithm correctly found all cycles and grouped the graph into 6 components.  
After condensation, the DAG was sorted topologically using Kahn’s method.  
Shortest and longest paths were then calculated to show task dependencies and the critical chain.  
All algorithms worked very fast – under 1 ms.

---

### ✅ Conclusion
- SCC helps to detect cyclic dependencies between city tasks.
- Topological order defines the correct and safe task sequence.
- Shortest and Longest paths show optimal and critical plans.
- The implementation is efficient (O(V+E)) and suitable for Smart City scheduling problems.

---

**Repository:** [nargizamm001/assig4DAA](https://github.com/nargizamm001/assig4DAA)  
**Build command:** `mvn clean test compile exec:java -Dexec.mainClass=Main`
