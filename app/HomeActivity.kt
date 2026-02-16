@Composable
fun HomeScreen(modifier: Modifier = Modifier) {

    var query by remember { mutableStateOf("") }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("Android News", fontSize = 40.sp)

        Spacer(modifier = Modifier.height(40.dp))

        Row {

            Text("Term Search:")

            TextField(
                value = query,
                onValueChange = { newValue ->
                    query = newValue
                },
                label = { Text("Search") },
                modifier = Modifier.padding(8.dp)
            )

            Text(
                text = "Search",
                modifier = Modifier.clickable {
                    // navigate to results
                }
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        Row {
            Text("View by Location ")

            Text(
                text = "VIEW MAP",
                modifier = Modifier.clickable { }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row {
            Text("Top Headlines ")

            Text(
                text = "VIEW TOP HEADLINES",
                modifier = Modifier.clickable { }
            )
        }
    }
}
@Preview(showBackground = true)
@Composable
fun HomePreview() {
    HomeScreen()
}
