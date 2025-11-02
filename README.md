# Assignment Five – Q3: Explore Boston

## Overview
**Explore Boston** is a multi-destination navigation app built with **Jetpack Compose Navigation** that takes users on a virtual “tour” of the city.  
It demonstrates deep navigation, argument passing, and proper backstack management across multiple screens.

---

## Features
- 🏙️ **Home Screen (Intro)** – Entry point with an overview and “Start Exploring” button  
- 🗂️ **Categories Screen** – Displays options like **Museums**, **Parks**, and **Restaurants**  
- 📋 **List Screen** – Shows a list of places within the selected category (e.g., “All Museums”)  
- 🏛️ **Detail Screen** – Displays details of a specific location (e.g., “MIT Museum”)  
- 🧭 **Navigation:**
  - Structured routes with both **String** and **Int** arguments  
  - `popUpTo(..., inclusive = true)` to clear stack when returning Home  
  - Proper use of `rememberNavController()` and a clean **NavGraph** file  
  - Correct backstack handling—back button disabled after full navigation cycle  
- 🎨 **UI Consistency:**
  - Shared **TopAppBar** and/or **BottomBar** across all destinations  
  - Maintains cohesive layout and navigation flow  

---

## Navigation Architecture
- **Routes:** Defined in a sealed class (e.g., `Routes.Home`, `Routes.Categories`, `Routes.List`, `Routes.Detail`).  
- **Arguments:** 
  - Category passed as a **String** (e.g., `"museums"`)  
  - Location ID passed as an **Int** (`NavType.IntType`)  
- **Example navigation calls:**
  ```kotlin
  navController.navigate("list/${categoryName}")
  navController.navigate("detail/${categoryName}/${locationId}")
  ```

## How to Use
```bash
git clone https://github.com/cangokmen/CS501-AssignmentFiveQ3
# Open in Android Studio and run on an emulator or device
```
- Click on categories and items to expand

## AI Documentation
Created location and bostonData manually. Collaborated on AppNavHost with Gemini. Asked Gemini to implement the rest, it had no issues. Gemini also generated the README up to this paragraph. 
