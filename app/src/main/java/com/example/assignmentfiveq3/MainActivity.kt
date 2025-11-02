package com.example.assignmentfiveq3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.assignmentfiveq3.ui.theme.AssignmentFiveQ3Theme
import androidx.compose.material3.ButtonDefaults

val AndroidGreen = Color(0xFF3DDC84)

// location class
data class Location(val id: Int, val name: String, val description: String)

// data
val bostonData = mapOf(
    "Landmarks" to listOf(
        Location(1, "Faneuil Hall", "Historic marketplace and meeting hall since 1742."),
        Location(2, "Bunker Hill Monument", "Obelisk commemorating the Battle of Bunker Hill."),
        Location(3, "Old North Church", "Famous church for 'One if by land, two if by sea.'")
    ),
    "Cafes" to listOf(
        Location(4, "Tatte Bakery & Cafe", "Mediterranean inspired pastries and great coffee."),
        Location(5, "Thinking Cup", "Cozy cafe near Boston Common."),
        Location(6, "Flour Bakery", "Local favorite for pastries and sandwiches.")
    ),
    "Neighborhoods" to listOf(
        Location(7, "Back Bay", "Victorian brownstones, Newbury Street, and Copley Square."),
        Location(8, "North End", "Boston’s Little Italy, has great italin restaurants."),
        Location(9, "Seaport District", "Waterfront area with modern dining and harbor views.")
    ),
    "Universities" to listOf(
        Location(10, "Harvard University", "Historic Ivy League campus in nearby Cambridge."),
        Location(11, "Boston University", "Large private research university along the Charles."),
        Location(12, "Northeastern University", "Known for its urban campus, has a co-op program.")
    )
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AssignmentFiveQ3Theme {
                BostonTourApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BostonTourApp() {
    // 1. Create the NavController
    val navController = rememberNavController()

    // 2. Get the current back stack entry to determine the route
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    Scaffold(
        topBar = {
            // 3. Reusable TopAppBar that shows/hides the back button
            AppTopBar(
                currentRoute = currentRoute,
                onBackClicked = { navController.navigateUp() }
            )
        }
    ) { innerPadding ->
        // 4. NavHost contains all the navigation logic
        AppNavHost(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(currentRoute: String?, onBackClicked: () -> Unit) {
    // Logic to determine the title and if the back button should be shown
    val (title, canNavigateBack) = when {
        currentRoute?.startsWith("category") == true -> "Categories" to true
        currentRoute?.startsWith("list") == true -> {
            val category = currentRoute.substringAfter('/').substringBefore('}')
            category to true
        }
        currentRoute?.startsWith("detail") == true -> "Details" to true
        else -> "Explore Boston" to false
    }

    TopAppBar(
        title = { Text(title) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        navigationIcon = {
            // Show back button only if canNavigateBack is true
            if (canNavigateBack) {
                IconButton(onClick = onBackClicked) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        }
    )
}

@Composable
fun AppNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = modifier
    ) {
        // --- 1. Home Screen ---
        composable("home") {
            HomeScreen(onNavigateToCategories = {
                navController.navigate("category")
            })
        }

        // --- 2. Categories Screen ---
        composable("category") {
            CategoriesScreen(
                categories = bostonData.keys.toList(),
                onNavigateToList = { category ->
                    // Navigate with a String argument
                    navController.navigate("list/$category")
                }
            )
        }

        // --- 3. List Screen ---
        composable(
            route = "list/{category}",
            arguments = listOf(navArgument("category") { type = NavType.StringType })
        ) { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category") ?: ""
            val locations = bostonData[category] ?: emptyList()
            ListScreen(
                category = category,
                locations = locations,
                onNavigateToDetail = { locationId ->
                    // Navigate with both String and Int arguments
                    navController.navigate("detail/$category/$locationId")
                }
            )
        }

        // --- 4. Detail Screen ---
        composable(
            route = "detail/{category}/{locationId}",
            arguments = listOf(
                navArgument("category") { type = NavType.StringType },
                navArgument("locationId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category") ?: ""
            val locationId = backStackEntry.arguments?.getInt("locationId") ?: 0
            val location = bostonData[category]?.find { it.id == locationId }

            DetailScreen(
                location = location,
                onNavigateHome = {
                    navController.navigate("home") {
                        // Clear the entire back stack up to 'home'
                        popUpTo("home") {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun ScreenContent(title: String, content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(text = title, style = MaterialTheme.typography.headlineLarge, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(24.dp))
        content()
    }
}

@Composable
fun HomeScreen(onNavigateToCategories: () -> Unit) {
    ScreenContent(title = "Welcome to Boston!") {
        Text("Explore the best places the city has to offer.", textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onNavigateToCategories,
            colors = ButtonDefaults.buttonColors(
                containerColor = AndroidGreen,
                contentColor = Color.Black
            )) {
            Text("Start Exploring")
        }
    }
}

@Composable
fun CategoriesScreen(categories: List<String>, onNavigateToList: (String) -> Unit) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(categories) { category ->
            Card(
                modifier = Modifier.clickable { onNavigateToList(category) },
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Text(
                    text = category,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

@Composable
fun ListScreen(category: String, locations: List<Location>, onNavigateToDetail: (Int) -> Unit) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text(
                text = "Showing all: $category",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
        items(locations) { location ->
            Card(
                modifier = Modifier.clickable { onNavigateToDetail(location.id) },
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Text(
                    text = location.name,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

@Composable
fun DetailScreen(location: Location?, onNavigateHome: () -> Unit) {
    ScreenContent(title = location?.name ?: "Unknown Location") {
        Text(location?.description ?: "No details available.", textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onNavigateHome,
            colors = ButtonDefaults.buttonColors(
                containerColor = AndroidGreen,
                contentColor = Color.Black
            )) {
            Text("Go Back to Home")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    AssignmentFiveQ3Theme {
        HomeScreen(onNavigateToCategories = {})
    }
}

@Preview(showBackground = true)
@Composable
fun CategoriesScreenPreview() {
    AssignmentFiveQ3Theme {
        CategoriesScreen(categories = listOf("Museums", "Parks"), onNavigateToList = {})
    }
}
