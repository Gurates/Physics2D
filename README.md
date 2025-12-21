# 2D Physics & Liquid Simulation Engine (Open Source)

This project is a modular physics simulation environment developed using Java Swing. It provides an interactive platform to explore **Newtonian Mechanics**, **Rigid Body Collisions**, and **Smoothed Particle Hydrodynamics (SPH)** for fluid simulation.

The primary objective of this project is to demonstrate how complex physical phenomena can be modeled from scratch using pure Java, without relying on external physics libraries.

## Main Features

### 1. Rigid Body Physics
* **Collision Resolution:** Implements impulse-based collision logic with mass-weighted **position correction** to prevent particle overlapping.
* **Momentum:** Conservation of momentum for both elastic and inelastic collision scenarios.
* **Environmental Interaction:** Robust floor and wall collision detection.

### 2. Liquid Physics (SPH) & Optimization
* **Fluid Dynamics:** Real-time water simulation using the **Smoothed Particle Hydrodynamics (SPH)** algorithm.
* **Internal Forces:** Density-based **Pressure** (repulsion) and **Viscosity** (friction) calculations.
* **Spatial Hashing (Grid System):** An optimized grid-based neighbor search system that reduces complexity from $O(N^2)$ to approximately $O(N)$. This enables fluid simulation of hundreds of particles with high performance.



### 3. Interactive Interface & Controls
* **Real-time Parameters:** On-the-fly adjustment of gravity, mass, and radius values.
* **Dynamic Interaction:** Ability to grab, drag, and toss particles using the mouse, or spawn new objects with the 'S' key.
* **Modes:** Seamless switching between individual rigid bodies and large liquid volumes.

## Technical Overview

* **Integration:** Utilizes the "Semi-implicit Euler" method for stable motion calculations.
* **Vector Mathematics:** All physical calculations are processed through a custom `Vector2` library.
* **Performance:** Proximity tests for SPH calculations are optimized via a custom `HashMap`-based Grid system.

## Controls and Inputs

| Input | Action |
| :--- | :--- |
| **Mouse Drag** | Grabs and tosses particles. |
| **'S' Key** | Spawns a rigid particle at a random location. |
| **Gravity Field** | Sets the global downward force (N). |
| **Radius Field** | Determines the size of objects (px). |
| **Toggle Liquid** | Clears the scene and initializes the SPH fluid simulation. |

## Contributing

This is an **open-source educational project**, and contributions are highly encouraged. If you would like to enhance the physics engine, improve performance, or add new features, please follow the guidelines below.

### Areas for Contribution:
* **Physics Engine Improvements:** Implementation of inter-particle friction or Verlet Integration.
* **Fluid Enhancements:** Surface tension calculations or presets for different liquid types (e.g., honey, oil).
* **Visualization:** Integration with OpenGL (LWJGL) instead of Java 2D for advanced rendering.
* **Bug Fixes:** Improving edge-case handling in current collision detection algorithms.

### How to Contribute:
1.  **Fork** the repository.
2.  Create a new **branch** for your feature (`git checkout -b feature/AdvancedPhysics`).
3.  **Commit** your changes (`git commit -m 'Add: Inter-particle friction'`).
4.  **Push** to the branch (`git push origin feature/AdvancedPhysics`).
5.  Open a **Pull Request**.

## Installation and Execution

1.  **Requirement:** Ensure **JDK 8** or higher is installed on your system.
2.  **Compilation:**
    ```bash
    javac *.java
    ```
3.  **Execution:**
    ```bash
    java SimulationApp
    ```
<img width="1229" height="730" alt="Image" src="https://github.com/user-attachments/assets/635e4b57-24aa-49c3-b944-765eacea266e" />
<img width="1229" height="737" alt="Image" src="https://github.com/user-attachments/assets/af53f63f-e872-46a6-8057-be7bda5dcb06" />
