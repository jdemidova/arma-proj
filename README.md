# 📈 Statistical Analysis and Modeling of Autoregressive and Moving Average (ARMA) Processes 

---

## 🎯 Idea and Objective
The project focuses on analyzing a **stationary ergodic random process** represented as a discrete **time series** of 10,000 samples.  
The goal is to estimate statistical properties of this process, construct mathematical models that describe it — **AR (autoregressive)**, **MA (moving average)**, and **ARMA (combined)** — and verify which model reproduces the process most accurately.

Two software environments are used:
- **Java** — for calculating statistical parameters, correlation functions, and simulation of new sequences.  
- **Octave** — for solving model equations (Yule–Walker and nonlinear systems) using numerical methods.

---

## ⚙️ Workflow

### 1️⃣ Data and Initial Analysis
Input: a sample of 10,000 consecutive observations of a stationary time series.  
The code computes:
- sample mean and variance,  
- standard deviation (SD),  
- correlation and normalized correlation functions (NCF),  
- correlation interval (where NCF drops below e⁻¹).

These calculations are performed by the Java class `Calculations` (methods `average`, `dispersion`, `CorrelationFunction`, `NCF`).

---

### 2️⃣ Autoregressive Model Construction (AR)
For AR models of orders M = 0–3:
1. The **Yule–Walker equations** are solved in **Octave** using `fsolve` (see `AR3.m` and `AR3_solve.m`).
2. Model parameters are computed.
3. Theoretical NCFs are obtained using recursive relation.
   <img width="443" height="53" alt="image" src="https://github.com/user-attachments/assets/1610113c-9c08-4d1a-8caa-be641dace340" />

4. Each model’s accuracy is evaluated via mean-square error between theoretical and empirical NCFs.

All calculations and comparisons are automated in Java (`AR` method).  
Results show the **AR(3)** model provides the best fit among AR types.

---

### 3️⃣ Moving Average Model Construction (MA)
For MA models of orders N = 0–3:
1. Systems of nonlinear equations are solved in Octave using `fsolve`.
2. Parameters are substituted into the NCF recurrence and compared to empirical data.  
3. Non-existent models correspond to unsolved or non-real systems.

These calculations are managed in Java (`MA` method).  
Only **MA(1)** was valid and stable, though less accurate than AR(3).

---

### 4️⃣ Mixed ARMA Model Construction
Combined **ARMA(M, N)** models (up to order 3) were explored.  
Systems of equations connecting parameters with correlation functions were formed and solved via Octave (`ARMA21.m`, `ARMA21_solve.m`).

For each model:
- parameters were estimated,
- stability was checked (all roots inside unit circle),
- theoretical NCFs were calculated and compared to experimental data.

Java’s `ARMA` method runs this comparison automatically.  
Among all tested models, **ARMA(2,3)** achieved the smallest modeling error and was found stable.

---

### 5️⃣ Simulation of New Random Processes
Using parameters of the three best models — **AR(3)**, **MA(1)**, and **ARMA(2,3)** — new time series of 10,000 values were generated in Java:

- `AR3_modelling()` — generates AR(3) sequence.  
- `MA1_modelling()` — generates MA(1) sequence.  
- `ARMA23_modelling()` — generates ARMA(2,3) sequence.  

Each function:
1. Synthesizes noise with Gaussian distribution.  
2. Produces the modeled process.  
3. Writes results and statistics (mean, variance, NCF, model error) into output files.

---

## 🧩 Summary
1. Load real process data (`process.txt`) for analysis.  
2. Compute sample statistics and correlation metrics.  
3. Estimate parameters of AR, MA, and ARMA models via Octave (`fsolve`).  
4. Evaluate each model’s accuracy in Java using empirical vs theoretical NCFs.  
5. Generate simulated sequences for top models.  
6. Compare modeled and original data visually and numerically.

---

## 📊 Results
- The **Java** program successfully computes key statistical measures, correlation structure, and simulates random processes.  
- The **Octave** scripts automatically solve model systems and export coefficients.  
- Comparison of theoretical and experimental NCFs shows:

  | Model | Mean Error (ε²) | Accuracy Rank |
  |--------|-----------------|----------------|
  | AR(3)  | 0.0024          | 2nd            |
  | MA(1)  | 0.0373          | 3rd            |
  | ARMA(2,3) | **0.00013**  | **Best**       |

- The generated process based on **ARMA(2,3)** preserves the same mean, variance, and correlation pattern as the original data.

---

## 🧠 Conclusion
The developed system performs full-cycle **statistical analysis, parameter estimation, and stochastic process modeling**.  
The combined use of **Octave for parameter solving** and **Java for computation and simulation** enables efficient reproduction and verification of complex autoregressive and moving-average behaviors.  
Among tested configurations, the **ARMA(2,3)** model most accurately represents the dynamics of the studied process.
