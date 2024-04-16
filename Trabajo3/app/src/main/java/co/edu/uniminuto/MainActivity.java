package co.edu.uniminuto;

//las dependencias para reutiliar codigo en la app
import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.os.Environment;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    //1. Declaracion de los pobjetos de la interface que se usaran en la puerta
    public static final int REQUEST_CODE = 21;
    public static final int DEFAULT_VALUE = 0;
    public static final int REQUEST_CODE1 = 26;
    public static final int REQUEST_CODE2 = 27;
    public static final int REQUEST_CODE3 = 28;
    public static final int REQUEST_CODE4 = 29;
    public static final int REQUEST_CODE5 = 30;
    private Button btnCheckPermissions;
    private Button btnRequestPermissions;
    private TextView tvCamera;
    private TextView tvBiometric;
    private TextView tvExternalWS;
    private TextView tvReadExternalS;
    private TextView tvInternet;
    private TextView tvResponse;
    //1.1 Objetos para recursos
    private TextView versipmAndroid;
    private int versionSDK;
    private ProgressBar pbLevelBatt;
    private TextView tvLevelBatt;

    private TextView tvLevelBaterry;

    IntentFilter batFilter;
    CameraManager cameraManager;
    String cameraId;
    private Button btnOn;
    private Button btnOff;
    ConnectivityManager conexion;

    private TextView tvSms;
    private TextView tvReadContacts;
    private TextView btnResquestPermissionDactilar;
    private TextView btnResquestPermissionES;
    private TextView btnResquestPermissionRS;
    private TextView btnResquestPermissionSMS;

    private EditText etDoc;
    private Button btnSaveFileTxt;
    private TextView tvConexionInternet;
    private  Button btnResquestPermissionRC;

    ///2. Enlace de objetos
    private void initObject(){
    //Validación de los permisos del celular
        btnCheckPermissions = findViewById(R.id.btnCheckPermission);
    // Sesion de la version android del celular
        versipmAndroid = findViewById(R.id.tvVersionAndroid);
    //Botones de encendido y apagado de la literna
        btnOn = findViewById(R.id.btnOn);
        btnOff = findViewById(R.id.btnOff);

    // Sesion del estado de la bateria
        pbLevelBatt = findViewById(R.id.pbLevelBatery);
        tvLevelBatt = findViewById(R.id.tvLevelBaterry);
    //Sesion del estado del internet
        tvConexionInternet = findViewById(R.id.tvConexionInternet);
    //Text View donde se visualizará los permisos del celular
        tvBiometric = findViewById(R.id.tvDactilar);
        tvCamera = findViewById(R.id.tvCamera);
        tvExternalWS = findViewById(R.id.tvEws);
        tvReadExternalS = findViewById(R.id.tvRs);
        tvInternet = findViewById(R.id.tvInternet);
        tvSms = findViewById(R.id.tvSms);
        tvReadContacts = findViewById(R.id.tvReadContacts);
        tvResponse = findViewById(R.id.tvResponse);
        etDoc =findViewById(R.id.etDoc);
    // Botones para solicitar los permisos del celular
        btnRequestPermissions = findViewById(R.id.btnRequestPermission);
        btnRequestPermissions.setEnabled(false);
        btnResquestPermissionDactilar = findViewById(R.id.btnResquestPermissionDactilar);
        btnResquestPermissionDactilar.setEnabled(false);
        btnResquestPermissionES = findViewById(R.id.btnResquestPermissionES);
        btnResquestPermissionES.setEnabled(false);
        btnResquestPermissionRS = findViewById(R.id.btnResquestPermissionRS);
        btnResquestPermissionRS.setEnabled(false);
        btnResquestPermissionSMS = findViewById(R.id.btnResquestPermissionSMS);
        btnResquestPermissionSMS.setEnabled(false);
        btnResquestPermissionRC = findViewById(R.id.btnResquestPermissionRC);
        btnResquestPermissionRC.setEnabled(false);
        btnSaveFileTxt = findViewById(R.id.btnSaveFileTxt);

    }

    // Clase principal de la app
    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    //3. llamado del metodo de enlace de objetos
        initObject();
    //4. Enlace de botones a los metodos
        btnCheckPermissions.setOnClickListener(this::voidCheckPermissions);
        btnRequestPermissions.setOnClickListener(this::voidRequestPermissions);
    // Botones para la linterna
        btnOn.setOnClickListener(this::onLigth);
        btnOff.setOnClickListener(this::offLigth);
    //bateria
        batFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(receiver,batFilter);
    //dactilar
        btnResquestPermissionDactilar.setOnClickListener(this::voidResquestPermissionDactilar);
    //Write y Read de External Storage
        btnResquestPermissionES.setOnClickListener(this::voidResquestPermissionES);
        btnResquestPermissionRS.setOnClickListener(this::voidResquestPermissionRS);
    //Mensaje de texto
        btnResquestPermissionSMS.setOnClickListener(this::voidResquestPermissionSMS);
    //Leer contactos
        btnResquestPermissionRC.setOnClickListener(this::voidResquestPermissionRC);
    //12. llamado del metodo del internet
        checkInternetConnection();
    //14. escuchar el boton de guardar info en el txt
        btnSaveFileTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDataToFile();
            }
        });
    }

    //5. Verificacion De Permisos
    private void voidCheckPermissions(View view) {
        //Si hay permiso ---> 0 ---> si no --> 1
        int statusCamera = ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.CAMERA);
        int statusWES = ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int statusRES = ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE);
        int statusInternet = ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.INTERNET);
        int statusBiometric =ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.USE_BIOMETRIC);
        int statusSms =ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.SEND_SMS);
        int statusReadContacts =ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_CONTACTS);

        //Mostrar el Estado de los Permisos del celular
        tvCamera.setText("Status Camara:"+statusCamera);
        tvExternalWS.setText("Status WES:"+statusWES);
        tvReadExternalS.setText("Status RES:"+statusRES);
        tvInternet.setText("Status Internet:"+statusInternet);
        tvBiometric.setText("Status Biometric:"+statusBiometric);
        tvSms.setText("Status Send SMS:"+statusSms);
        tvReadContacts.setText("Status Read Contacts:"+statusReadContacts);
        btnRequestPermissions.setEnabled(true);
        btnResquestPermissionDactilar.setEnabled(true);
        btnResquestPermissionES.setEnabled(true);
        btnResquestPermissionRS.setEnabled(true);
        btnResquestPermissionSMS.setEnabled(true);
        btnResquestPermissionRC.setEnabled(true);
    }

    //6. Solicitud de permisos
    private void voidRequestPermissions(View view) {
        if(ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.CAMERA},REQUEST_CODE);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.P)
    private void voidResquestPermissionDactilar(View view) {
        if(ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.USE_BIOMETRIC) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.USE_BIOMETRIC}, REQUEST_CODE1);
        }
    }
    private void voidResquestPermissionES(View view) {
        if(ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE2);
        }
    }
    private void voidResquestPermissionRS(View view) {
        if(ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE3);
        }
    }
    private void voidResquestPermissionSMS(View view) {
        if(ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.SEND_SMS}, REQUEST_CODE4);
        }
    }

    private void voidResquestPermissionRC(View view) {
        if(ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CODE5);
        }
    }

    //7. Gestion de respuesa del usuario respecto a la solicitud del permiso

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        tvResponse.setText(""+grantResults[0]);
        if (requestCode == REQUEST_CODE){
            if(grantResults[0] != PackageManager.PERMISSION_GRANTED){
                new AlertDialog.Builder(this)
                        .setTitle("Box Permissions")
                        .setMessage("You denied the permissions Camera")
                        .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                        Uri.fromParts("package", getPackageName(), null));
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                        }).setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                finish();
                            }
                        }).create().show();

            }else{
                Toast.makeText(this, "Usted no aprovo el permiso", Toast.LENGTH_LONG);
            }
        }else{
            Toast.makeText(this, "Usted aprovo el permiso", Toast.LENGTH_SHORT);
        }
    }

    //8. Implementacion del OnResume para la version de androis

    @Override
    protected void onResume() {
        super.onResume();
        String versionSO = Build.VERSION.RELEASE;
        versionSDK = Build.VERSION.SDK_INT;
        versipmAndroid.setText("Version SO:"+versionSO+" / SDK:"+versionSDK);
    }
    //9. Metodo de Encendido y apagado de la linterna
    private void onLigth(View view) {
        try {
            cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
            cameraId = cameraManager.getCameraIdList()[0];
            cameraManager.setTorchMode(cameraId,true);

        }catch (Exception e){
            Toast.makeText(this, "No se puede encender la linterna", Toast.LENGTH_SHORT).show();
            Log.i("Flash",e.getMessage());
        }
    }
    private void offLigth(View view) {
        try {
            cameraManager.setTorchMode(cameraId,false);
        }catch (Exception e){
            Toast.makeText(this, "No se puede encender la linterna", Toast.LENGTH_SHORT).show();
            Log.i("Flash",e.getMessage());
        }
    }

    //10. Metodo del nivel de la Bateria
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int levelbaterry = intent.getIntExtra(BatteryManager.EXTRA_LEVEL,DEFAULT_VALUE);
            pbLevelBatt.setProgress(levelbaterry);
            tvLevelBatt.setText("Level Battery: "+levelbaterry+" %");
        }
    };

    //11. Metodo de crear y guardar informacion en la carpeta documentos
    private void saveDataToFile() {
        String fileName = etDoc.getText().toString();
        if (!fileName.isEmpty()) {
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName + ".txt");
            try {
                file.createNewFile();
                FileOutputStream fos = new FileOutputStream(file, true);
                String data = "Nivel de batería: " + tvLevelBatt.getText().toString() + "\n" +
                        "Versión del sistema operativo: " + versipmAndroid.getText().toString() + "\n";
                fos.write(data.getBytes());
                fos.close();
                Toast.makeText(this, "Datos guardados en " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                Toast.makeText(this, "Error al guardar los datos: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Por favor, ingrese un nombre de archivo", Toast.LENGTH_SHORT).show();
        }
    }

    //13.Método para verificar la conexión a Internet y mostrar el estado
    private void checkInternetConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                // Hay conexión a Internet
                tvConexionInternet.setText("Conectado a Internet");
            } else {
                // No hay conexión a Internet
                tvConexionInternet.setText("Sin conexión a Internet");
            }
        } else {
            // No se puede obtener el servicio de ConnectivityManager
            tvConexionInternet.setText("No se puede verificar la conexión a Internet");
        }
    }


}