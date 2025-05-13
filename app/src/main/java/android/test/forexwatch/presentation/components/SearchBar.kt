package android.test.forexwatch.presentation.components

import android.test.forexwatch.presentation.theme.White
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SearchBar(
    searchQuery: String,
    onSearch: (String) -> Unit,
    onClear: () -> Unit,
) {
    val focusRequester = remember { FocusRequester() }

    Row(
        verticalAlignment = Alignment.CenterVertically

    ) {


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp)
                .background(White),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val focusManager = LocalFocusManager.current

            TextField(
                value = searchQuery,
                onValueChange = { onSearch(it) },
                placeholder = {
                    Text(
                        text = "Search by Code",
                        color = Color.Gray
                    )
                },
                singleLine = true,
                modifier = Modifier
                    .weight(1f)
                    .background(Color.Transparent)
                    .focusRequester(focusRequester),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        onSearch(searchQuery)
                    }
                ),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    cursorColor = Color.Gray,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Gray,
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

                        focusRequester.requestFocus()
                    }
                }
            ) {
                Icon(
                    imageVector = if (searchQuery.isNotEmpty()) Icons.Default.Close else Icons.Default.Search,
                    contentDescription = "Search/Clear",
                    tint = Color.Black
                )
            }
        }
    }


}

@Preview
@Composable
fun SearchBarPreview() {
    Box(
        modifier = Modifier
            .padding(16.dp)
            .background(Color.Black)
    ) {
        SearchBar(searchQuery = "", onSearch = {}, onClear = {})
    }

}
