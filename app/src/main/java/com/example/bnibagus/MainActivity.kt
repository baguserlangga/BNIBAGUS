package com.example.bnibagus

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.util.Size
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageAnalysis.STRATEGY_BLOCK_PRODUCER
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import  androidx.compose.ui.graphics.Color.*
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.White

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.Point
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.GridLines
import co.yml.charts.ui.linechart.model.IntersectionPoint
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp
import co.yml.charts.ui.linechart.model.ShadowUnderLine
import com.example.bnibagus.model.DonutChart
import com.example.bnibagus.model.LineChartModel
import com.example.bnibagus.model.TransaksiEvent
import com.example.bnibagus.model.TransaksiQrDB
import com.example.bnibagus.model.TransaksiState
import com.example.bnibagus.ui.theme.BNIBAGUSTheme
import com.example.bnibagus.viewmodel.DataTransaksiViewModel
import org.json.JSONArray
import java.io.InputStream

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

//                    myUi()
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
        val state by viewModel.state.collectAsState()
        NavHost(navController = navController , startDestination = Destinations.FirstScreen.toString()){

            composable(route = Destinations.FirstScreen.toString()){
                FirstScreen(navController, onEvent = viewModel::onEvent )
            }
            composable(route = Destinations.ScanQR.toString()){
                ScanQR(navController)
            }
            composable(route = Destinations.SecondScreen.toString()+"/{code}"){
                val code = it.arguments?.getString("code")
                SecondScreen(code,navController, state = state, onEvent = viewModel::onEvent )
            }
            composable(route = Destinations.TransaksiScreen.toString()){

                TransaksiScreen(navController,state = state, onEvent = viewModel::onEvent )
            }
            composable(route = Destinations.PortoFolio.toString()){

                readjson(navController )
            }
            composable(route = Destinations.LineChart.toString()){

                chart()
            }

        }
    }
    @Composable
    fun FirstScreen(navController: NavController,onEvent:(TransaksiEvent)->Unit)
    {
        val context = LocalContext.current
        var char by remember {
            mutableStateOf(false)
        }
        Column(
            verticalArrangement = Arrangement.Center
        ) {
            Button(onClick = { navController.navigate(Destinations.ScanQR.toString())}, modifier = Modifier.size(width = 300.dp,height =100.dp )) {
                Text(text = "Scan QR")
            }
            Button(
                onClick = { Toast.makeText(context, "On progress", Toast.LENGTH_SHORT).show() },
                modifier = Modifier
                    .size(width = 300.dp, height = 100.dp)
                    .padding(top = 15.dp),
            ) {
                Text(text = "Promo")
            }

            Button(
                onClick = { navController.navigate(Destinations.PortoFolio.toString())}
                , modifier = Modifier
                    .size(width = 300.dp, height = 100.dp)
                    .padding(top = 15.dp),
            ) {
                Text(text = "Portfolio")
//                if(char==true)
//                {
//                    SelectDialog(onEvent =onEvent )
//
//                }
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
                            navController.navigate(Destinations.SecondScreen.toString() + "/$code")
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
    }
    @Composable
    fun chart()
    {
        val listChart: List<Point> =
            listOf(
                Point(0f, 40f),
                Point(1f, 90f),
                Point(2f, 0f),
                Point(3f, 60f),
                Point(4f, 10f))
        val xAxisData = AxisData.Builder()
            .axisStepSize(100.dp)
            .backgroundColor(Blue)
            .steps(listChart.size - 1)
            .labelData { i -> i.toString() }
            .labelAndAxisLinePadding(15.dp)
            .build()
        val steps = 5

        val yAxisData = AxisData.Builder()
            .steps(steps)
            .backgroundColor(color = Red)
            .labelAndAxisLinePadding(20.dp)
            .labelData { i ->
                val yScale = 100 / steps
                (i * yScale).toString()
            }.build()

        val lineChartData = LineChartData(
            linePlotData = LinePlotData(
                lines = listOf(
                    Line(
                        dataPoints = listChart,
                        LineStyle(),
                        IntersectionPoint(),
                        SelectionHighlightPoint(),
                        ShadowUnderLine(),
                        SelectionHighlightPopUp()
                    )
                ),
            ),
            xAxisData = xAxisData,
            yAxisData = yAxisData,
            gridLines = GridLines(),
            backgroundColor = White
        )
        LineChart(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            lineChartData = lineChartData
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun SecondScreen(code:String?=null, navController: NavController,
                     state: TransaksiState,
                     onEvent:(TransaksiEvent)->Unit)
    {
        val hasilCode = code.toString().split(".")


        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            onEvent(TransaksiEvent.SetBank(hasilCode[0]))
            onEvent(TransaksiEvent.SetIdTransaksi(hasilCode[1]))
            onEvent(TransaksiEvent.SetMerchant(hasilCode[2]))

            onEvent(TransaksiEvent.SetNominal(hasilCode[3].toInt()))
            Text(text =state.bank )
            Text(text =state.merchant )
            Text(text =state.id_transaksi )
            Text(text =state.nominal.toString())


            Button(onClick = { onEvent(TransaksiEvent.saveTransaksi)
                navController.navigate(Destinations.TransaksiScreen.toString())
            }) {
                Text(text = "Save")

            }

        }

    }

    @Composable
    fun DonutChart(navController: NavController)
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

    @Composable
    fun myUi()
    {

        val bottomMenulist =prepareMenuItem()
        val context = LocalContext.current.applicationContext
        var selectedItem by remember {
            mutableStateOf("")
        }
        Column(verticalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth(),horizontalAlignment=Alignment.CenterHorizontally ) {
            DisplayNav()

        }
        Box(modifier = Modifier.fillMaxWidth()) {
            BottomNavigation(
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                bottomMenulist.forEach{
                        item->
                   BottomNavigationItem(selected = (selectedItem==item.label),
                       onClick = {
                           selectedItem=item.label
                           Toast.makeText(context, selectedItem, Toast.LENGTH_SHORT).show()},
                       icon = {
                           androidx.compose.material3.Icon(imageVector =item.icon , contentDescription =item.label ) },
                       label = { Text(text = item.label)},
                       enabled = true)
                }




            }


        }
    }

    fun prepareMenuItem():List<BottomMenuItem>{
        val bottomMenuItemList = arrayListOf<BottomMenuItem>()

        val homemenu = BottomMenuItem("Home", icon = Icons.Filled.Home)
        val scanQr = BottomMenuItem("Scan", icon = Icons.Filled.Share)

        bottomMenuItemList.add(homemenu)
        bottomMenuItemList.add(scanQr)
        return bottomMenuItemList
    }


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun readjson(
        navController: NavController,
        )
    {
        var json = ""

        val arr = ArrayList<String>()
        val arrDonutChart = ArrayList<DonutChart>()
        val arrLineChartModel = ArrayList<LineChartModel>()
        val context = LocalContext.current

        var charChoose by remember{
            mutableStateOf("")
        }

        val arrListTipe = ArrayList<String>()
        val  inputStream : InputStream = assets.open("data.json")
        json =  inputStream.bufferedReader().use{it.readText()}
        var jsonArr = JSONArray(json)
        for (i in 0..jsonArr.length()-1)
        {
            val jsonObj = jsonArr.getJSONObject(i)
            arr.add(jsonObj.getString("type"))
            if(jsonObj.getString("type")=="lineChart")
            {
                var hasil  = jsonObj.getString("data")
                    .replace("{\"month\":[","")
                    .replace("]}","")

                val hasilarray = hasil.split(",")

                Log.d("isinyaininya", "readjson:" +hasilarray)
            }
            else
            {
                var jsonHasil=""
                var hasil  = jsonObj.getString("data")
                jsonHasil =  inputStream.bufferedReader().use{hasil}
                var jsonArrs = JSONArray(jsonHasil)
                for (i in 0..jsonArrs.length()-1)
                {
                    val jsonObjs = jsonArrs.getJSONObject(i)
                    Log.d("isinyaininyass", "readjson:" +jsonObjs.getString("data"))

                    arrListTipe.add(jsonObjs.getString("label"))
                }
            }
            Column() {
                arr.forEach {
                    item->
                    Card(onClick = {charChoose=item },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .padding(top = 10.dp)
                            .align(alignment = Alignment.CenterHorizontally),

                        ) {
                        Text(text = item.toString())

                    }
                }

                if(charChoose=="donutChart")
                {
                    arrListTipe.forEach{item->
                        Card(onClick = { },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(80.dp)
                                .padding(top = 10.dp)
                                .align(alignment = Alignment.CenterHorizontally),

                            ) {
                            Text(text = item.toString())

                        }
                    }
                }
                else if(charChoose == "lineChart")
                {
                    charChoose=""
                    navController.navigate(Destinations.LineChart.toString())
                }
            }
        }
//        Log.d("isinyaini", "readjson: $arr ")



    }
}

