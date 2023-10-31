package com.example.bnibagus

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Size
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.camera.core.CameraSelector
import androidx.camera.core.CameraSelector.LENS_FACING_BACK
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageAnalysis.STRATEGY_BLOCK_PRODUCER
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.bnibagus.model.TransaksiQrDB
import com.example.bnibagus.ui.theme.BNIBAGUSTheme
import com.example.bnibagus.viewmodel.DataTransaksiViewModel
import java.util.jar.Manifest

class MainActivity : ComponentActivity() {

    private val db by lazy{
        Room.databaseBuilder(
            applicationContext,
            TransaksiQrDB::class.java,
            "datatransaksiqr.db"
        ).allowMainThreadQueries().build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            BNIBAGUSTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,


                    ) {

//                    val jadicode by remember {
//                        mutableStateOf("")
//                    }

//                    val state by viewModel.state.collectAsState()
//                    TransaksiScreen(state = state, onEvent = viewModel::onEvent )
                    Column(verticalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth(),horizontalAlignment=Alignment.CenterHorizontally ) {
                        DisplayNav()
                    }
                }
            }
        }
    }


    private val viewModel by viewModels<DataTransaksiViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory{
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return DataTransaksiViewModel(db.dao) as T
                }
            }
        }
    )

    @Composable
    fun DisplayNav()
    {
        //nav conttroller
        val navController = rememberNavController()
        NavHost(navController = navController , startDestination = "First Screen"){

            composable(route = "First Screen"){
                FirstScreen(navController)
            }
            composable(route = "Scan QR"){
                ScanQR(navController)
            }
            composable(route = "Second Screen"){
                SecondScreen(navController)
            }
//            composable(route = "Second Screen" + "{code}"){
//                val usercode = it.arguments?.get("code")
//                SecondScreen(navController,usercode.toString())
//            }
            composable(route = "Third Screen"){
                ThirdScreen(navController)
            }
            composable(route = "Transaksi Screen"){
                val state by viewModel.state.collectAsState()
                TransaksiScreen(navController,state = state, onEvent = viewModel::onEvent )
            }

        }
    }
    @Composable
    fun FirstScreen(navController: NavController)
    {
        Column(
            verticalArrangement = Arrangement.Center
        ) {
            Button(onClick = { navController.navigate("Scan QR")}, modifier = Modifier.size(width = 300.dp,height =100.dp )) {
                Text(text = "Scan QR")
            }
        }


    }
    @Composable
    fun ScanQR(navController: NavController)
    {
        var code by remember {
            mutableStateOf("")
        }
        val context = LocalContext.current
        val lifecycleOwner = LocalLifecycleOwner.current
        val cameraProvider = remember {
            ProcessCameraProvider.getInstance(context)
        }

        var hasCampermision by remember {
            mutableStateOf( ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.CAMERA
            )== PackageManager.PERMISSION_GRANTED
            )
        }

        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = {granted->
                hasCampermision = granted

            }
        )
        LaunchedEffect(key1 = true )
        {
            launcher.launch(android.Manifest.permission.CAMERA)
        }






        if(hasCampermision)
        {
            AndroidView(factory = {context ->
                val previewView = PreviewView(context)

                val preview =androidx.camera.core.Preview.Builder().build()
                val selector = CameraSelector.Builder()
                    .requireLensFacing(CameraSelector.LENS_FACING_BACK).build()

                preview.setSurfaceProvider(previewView.surfaceProvider)
                val imageAnalisys = ImageAnalysis.Builder()
                    .setTargetResolution(Size(previewView.width,previewView.height))
                    .setBackpressureStrategy(STRATEGY_BLOCK_PRODUCER)
                    .build()
                imageAnalisys.setAnalyzer(
                    ContextCompat.getMainExecutor(context),
                    QrCodeAnalyzer{ result ->
                        code = result
                        if(code!="")
                        {
                            navController.navigate("Second Screen/"+code.toString())
                        }
                    }
                )
                try {
                    cameraProvider.get().bindToLifecycle(
                        lifecycleOwner,
                        selector,
                        preview,
                        imageAnalisys
                    )
                }catch (e : Exception)
                {
                    e.printStackTrace()
                }
                previewView
            },
//                        modifier = Modifier.weight(1f)
            )
            Text(
                text = code,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp)

            )

        }
        if(code.toString()!="")
        {
        }
    }


    @Composable
    fun SecondScreen(navController: NavController)
    {
        Button(onClick = { navController.navigate("First Screen") }) {
            Text(text = "usercode")
        }

    }

    @Composable
    fun ThirdScreen(navController: NavController)
    {
        Button(onClick = { navController.navigate("First Screen") }) {
            Text(text = "welcome to Second Screen")
        }

    }

    @Composable
    fun Greeting(name: String, modifier: Modifier = Modifier) {
        Text(
            text = "Hello $name!",
            modifier = modifier
        )
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        BNIBAGUSTheme {
            Greeting("Android")
        }
    }
}

