package android.test.forexwatch.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SearchBar(
    searchQuery: String,
    onSearch: (String) -> Unit,
    onClear: () -> Unit,
    onBackClick: () -> Unit
) {
Row(
    verticalAlignment = Alignment.CenterVertically

){


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color.Black, shape = RoundedCornerShape(20.dp))
            .border(1.dp, Color.Gray, shape = RoundedCornerShape(20.dp)),
        verticalAlignment = Alignment.CenterVertically
    ) {




        TextField(
            value = searchQuery,
            onValueChange = { onSearch(it) },
            placeholder = {
                Text(
                    text = "Search",
                    color = Color.Gray
                )
            },
            singleLine = true,
            modifier = Modifier
                .weight(1f)
                .background(Color.Transparent),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                cursorColor = Color.White,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )


        IconButton(
            onClick = {
                if (searchQuery.isNotEmpty()) {
                    onClear()
                } else {
                    onSearch(searchQuery)
                }
            }
        ) {
            Icon(
                imageVector = if (searchQuery.isNotEmpty()) Icons.Default.Close else Icons.Default.Search,
                contentDescription = "Search/Clear",
                tint = Color.White
            )
        }
    }
}


}

@Preview
@Composable
fun SearchBarPreview() {
    Box(modifier = Modifier
        .padding(16.dp)
        .background(Color.Black)) {
        SearchBar(onBackClick = {}, searchQuery = "", onSearch = {}, onClear = {})
    }

}
