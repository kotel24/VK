package ru.mygames.vk.ui.theme

import android.net.Uri
import android.widget.Button
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun ActivityResultTest () {
    var imageUri by remember {
        mutableStateOf(Uri.EMPTY)
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = {
            imageUri = it
        }
    )
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ){
        AsyncImage(model = imageUri, contentDescription = null, modifier = Modifier.weight(1f))
        androidx.compose.material3.Button(
            modifier = Modifier.fillMaxWidth().wrapContentHeight(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Blue, contentColor = Color.White),
            onClick = {launcher.launch("image/*")}
        ){
            Text("Get Image")
        }
    }
}