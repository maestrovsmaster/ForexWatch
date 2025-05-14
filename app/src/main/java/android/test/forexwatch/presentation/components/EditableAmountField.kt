package android.test.forexwatch.presentation.components

import android.test.forexwatch.presentation.theme.BlueDark
import android.test.forexwatch.presentation.theme.BlueLight
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun EditableAmountField(
    amount: Double,
    modifier: Modifier = Modifier,
    textColour: Color = Color.Black,
    onAmountChanged: (Double) -> Unit,
    currencyFormatter: (Double) -> String = { "%.2f".format(it) }
) {
    
    var isEditing by remember { mutableStateOf(false) }

    var input by remember {
        mutableStateOf(
            TextFieldValue(
                text = amount.toString(),
                selection = TextRange(0, amount.toString().length)
            )
        )
    }

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    Box(
        modifier = modifier
            .clickable {
                isEditing = true
                input = TextFieldValue(
                    text = amount.toString(),
                    selection = TextRange(0, amount.toString().length)
                )
            }
            .clip(RoundedCornerShape(16.dp))
            .border(
                width = 1.dp,
                color = BlueDark,
                shape = RoundedCornerShape(16.dp)
            )
            .background(BlueLight)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 4.dp)
                .fillMaxSize()
        ) {
            if (isEditing) {
                TextField(
                    value = input,
                    onValueChange = {
                        val newText = it.text
                        if (newText.matches(Regex("^\\d*\\.?\\d{0,2}\$"))) {
                            input = it
                        }
                    },
                    modifier = Modifier
                        .focusRequester(focusRequester)
                        .padding(0.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            isEditing = false
                            focusManager.clearFocus()
                            input.text.toDoubleOrNull()?.let {
                                onAmountChanged(it)
                            }
                        }
                    ),
                    textStyle = LocalTextStyle.current.copy(fontSize = 18.sp),
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = textColour,
                        backgroundColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )

                LaunchedEffect(Unit) {
                    delay(100)
                    focusRequester.requestFocus()
                }
            } else {
                Text(
                    text = currencyFormatter(amount),
                    fontSize = 18.sp,
                    modifier = Modifier.padding(end = 4.dp),
                    color = textColour
                )
            }
        }
    }
}

@Preview
@Composable
fun EditableAmountFieldPreview() {
    EditableAmountField(
        amount = 1.0,
        textColour = Color.Black,
        onAmountChanged = {},
        modifier = Modifier.padding(16.dp)
    )
}
