package ru.mygames.vk.samples

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SideEffectTest(number: Number) {
    Column {
        LazyColumn {
            repeat(5) {
                item {
                    Text("number: ${number.a}")
                }
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        number.a = 5
        LazyColumn {
            repeat(5) {
                item {
                    Text("number: ${number.a}")
                }
            }
        }
    }
}
data class Number(
    var a: Int
)