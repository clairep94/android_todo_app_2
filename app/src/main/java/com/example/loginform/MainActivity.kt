package com.example.loginform

import android.os.Bundle
import android.widget.Space

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.loginform.ui.theme.LoginFormTheme
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LoginFormTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
//                    App()
                    SwipeTestScreen()
                }
            }
        }
    }
}

@Composable

fun App(){

    val navController = rememberNavController()
    val loginScreenName = "LOGIN"
    val swipeTestScreenName = "SWIPETEST"

    NavHost(navController = navController, startDestination = loginScreenName, builder = {
        composable(loginScreenName) {
            LoginScreen(onCredentialsValidated = {
                navController.navigate(swipeTestScreenName)
            })
        }
        composable(swipeTestScreenName) {
            SwipeTestScreen()
        }

    })

}

@Composable
@Preview(showBackground = true)
fun SwipeTestScreen(){
    val screenSizeDp = LocalConfiguration.current.screenWidthDp.dp
    val modifier = Modifier
        .fillMaxSize()
        .padding(20.dp)
        .offset(0.dp, (-40).dp)

    Column(modifier = modifier, verticalArrangement = Arrangement.Center) {
        Text(
            text = "Testing Swipeable Cards",
            fontSize = 32.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth() // needs this to make text align center work.
        )
        Spacer(modifier = Modifier.height(30.dp))
        SingleCard(screenSizeDp)

    }
}


@Composable
fun SingleCard(screenSizeDp: Dp){
    var offsetX by remember { mutableStateOf(0f) }

    val cardModifier = Modifier
        .fillMaxWidth()
        .height(50.dp)
        .offset { IntOffset(offsetX.roundToInt(), 0) }
        .draggable(
            orientation = Orientation.Horizontal,
            state = rememberDraggableState { delta ->
                offsetX += delta
            },
            onDragStopped = {
                println("stopped drag ${offsetX.dp}")
                println("Screensize = $screenSizeDp")
                println("Screensize/2 = ${screenSizeDp/2}")
                println("Neg Screensize/2 = ${screenSizeDp/-2}")
//                if (offsetX.dp >= (screenSizeDp/2)) {
//                    println("Swipe Right")
//                } else if (offsetX.dp <= (screenSizeDp/-2)) {
//                    println("Swipe Left")
//                } else {
//                    println("Return to center")
//                }
                // Above is not finding the right threshold?? Threshold should be like +-500dp minus padding+extra -> 420
                if (offsetX.dp >= 420.dp) {
                    println("Swipe Right")
                    offsetX = 1000.0F // This is a bit fast & should be 1100+ to leave screen
                } else if (offsetX.dp <= 420.dp*-1) {
                    println("Swipe Left")
                    offsetX = -1000.0F // This is a bit fast & should be 1100+ to leave screen
                } else {
                    println("Return to center")
                    offsetX = 0.0F
                }
            }
        )


    ElevatedCard(modifier = cardModifier,
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)) {
        Text (
            text = "My First Task",
            modifier = Modifier.padding(16.dp),
            textAlign = TextAlign.Center
        )
}}


@Composable
fun LoginScreen(onCredentialsValidated: () -> Unit) {

    val correctPassword = "password" // hardcode for now -- should be API call

    // LIFTED FROM BODY: useState -> re-renders entirety of body when usernameValue is changed
    val (usernameValue, setUsernameValue) = remember {mutableStateOf("")}
    val (passwordValue, setPasswordValue) = remember { mutableStateOf("") }

    // LIFTED FROM HEADER: title -- I did this for a simple app that just personalises the
    // page
    val (title, setTitle) = remember { mutableStateOf("Welcome to my awesome app") }

    // Create a new 'modifier' object by calling Modifier.fillMaxSize()
    val modifier = Modifier
        .fillMaxSize()
        .padding(20.dp)
        .offset(0.dp, (-40).dp)

    Column(modifier = modifier, verticalArrangement = Arrangement.Center) {
//        Header(usernameValue=usernameValue, buttonClicked=buttonClicked)
        Header(title=title)
        Spacer(modifier = Modifier.height(30.dp))
        Body(usernameValue = usernameValue,
            onUsernameValueChange = {
                setUsernameValue(it)
            },
            passwordValue = passwordValue,
            onPasswordValueChange = {
                setPasswordValue(it)
            },
            onButtonClick = {
//                buttonClicked=true
//                setButtonClicked(true)
                if (passwordValue == correctPassword) {
//                    setTitle("Welcome $usernameValue")
                    onCredentialsValidated()
                } else {
                    setTitle("Wrong password")
                }
            })
    }
}

@Composable
// @Preview(showBackground = true)
//fun Header(usernameValue: String, buttonClicked: Boolean) {
fun Header(title: String) {
    Column {

        val imageModifier = Modifier.fillMaxWidth()
//        val title = if (buttonClicked) "Welcome $usernameValue" else "Welcome to my awesome app"

        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo of the page",
            // alignment = Alignment.Center,
            modifier = imageModifier
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = title,
            fontSize = 32.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth() // needs this to make text align center work.
        )
    }
}

@Composable
fun Body(usernameValue: String,
         onUsernameValueChange: (String) -> Unit,
         passwordValue: String,
         onPasswordValueChange: (String) -> Unit,
         onButtonClick: () -> Unit
         ) {

    val buttonEnabled = usernameValue.length >= 2

    TextField(
        value = usernameValue,
        onValueChange=onUsernameValueChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = {
            Text(text = "Enter your username")
        }
    )

    TextField(
        value = passwordValue,
        onValueChange=onPasswordValueChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = {
            Text(text = "Enter your password")
        },
        visualTransformation = PasswordVisualTransformation()
//        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
    )

    Spacer(modifier = Modifier.height(20.dp))

    Button(
        onClick = { onButtonClick()
                  },
        enabled = buttonEnabled,
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
    ) {
        Text(text = "Click me!")
    }
}