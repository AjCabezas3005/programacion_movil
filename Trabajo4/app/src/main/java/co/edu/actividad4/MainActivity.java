package co.edu.actividad4;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

import co.edu.actividad4.entity.User;
import co.edu.actividad4.model.UserDao;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    // 1. Declaración de variables
    public  static final  String TAG = "VALIDATE ";
    private EditText etDocumento;
    private EditText etUsuario;
    private EditText etNombres;
    private EditText etApellidos;
    private EditText etContra;
    private ListView listUsers;

    private int documento;

    private Context context;
    private Button btnSave;
    private Button  btnListUsers;
    private String usuario;
    private String nombres;

    private String apellidos;
    private String contra;
    SQLiteDatabase sqLiteDatabase;
    private Button btnEliminar;
    private Button btnUpdate;
    private Button btnLimpiar;
    private EditText etSearch;



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
        //3.  Inicialización de variables y configuración de listeners
        context=this;
        initObject();
        btnSave.setOnClickListener(this::createUser);
        btnListUsers.setOnClickListener(this::listUserShow);
        btnEliminar.setOnClickListener(this::deleteUser);
        btnEliminar.setEnabled(false);// Botón eliminar inicialmente deshabilitado
        btnUpdate.setOnClickListener(this::updateUser);
        btnUpdate.setEnabled(false); // Botón actualizar inicialmente deshabilitado
        btnLimpiar.setOnClickListener(v -> clearEditText());// Limpiar campos de texto al presionar el botón Limpiar
    }

    //2. Metodo para inicializar los objetos
    private void  initObject(){
        btnSave=findViewById(R.id.btnRegistar);
        btnListUsers=findViewById(R.id.btnListar);
        etNombres= findViewById(R.id.etNombres);
        etApellidos= findViewById(R.id.etApellidos);
        etDocumento= findViewById(R.id.etDocumento);
        etUsuario= findViewById(R.id.etUsuario);
        etContra= findViewById(R.id.etContra);
        /*listUsers= findViewById(R.id.lvLista);*/
        btnEliminar = findViewById(R.id.btnEliminar);
        btnUpdate = findViewById(R.id.btnActualizar);
        btnLimpiar = findViewById(R.id.btnLimpiar);
        etSearch = findViewById(R.id.etSearch);
    }

    //4. Método para obtener los datos ingresados por el usuario
    private void getData(){
        documento= Integer.parseInt(etDocumento.getText().toString()); // casteo
        usuario=  etUsuario.getText().toString();
        nombres=  etNombres.getText().toString();
        apellidos=  etApellidos.getText().toString();
        contra=  etContra.getText().toString();
    }

    // 5. Método para crear un nuevo usuario
    private void createUser(View view) {
        getData();
        User user = new User(documento, nombres, apellidos,usuario,contra);//instanciamos un objeto de tipo user
        UserDao dao = new UserDao(context, view);
        dao.insertUser(user);
        //invoco el metodo de listrar nuevamente
        listUserShow(view); // Actualizar la lista de usuarios después de insertar uno nuevo

    }

    // 6. Método para mostrar la lista de usuarios en una tabla
    private void listUserShow(View view) {
        UserDao dao = new UserDao(context, view);
        ArrayList<User> users = dao.getUserList();

        TableLayout tableLayout = findViewById(R.id.tableLayout);

        // Eliminar todas las filas de datos
        tableLayout.removeViews(1, tableLayout.getChildCount() - 1);// Limpiar filas de la tabla

        if (users.isEmpty()) {
            // No se encontraron usuarios, mostrar un mensaje en lugar de eliminar todas las filas de datos
            TableRow messageRow = new TableRow(context);
            TextView messageText = new TextView(context);
            messageText.setText("Usuario no encontrado");
            messageRow.addView(messageText);
            tableLayout.addView(messageRow);
        } else {
            // Se encontraron usuarios, agregar filas de datos a la tabla
            for (User user : users) {
                TableRow row = new TableRow(context);

                // Agregar información del usuario a las celdas de la fila
                TextView tvDocument = new TextView(context);
                tvDocument.setText(String.valueOf(user.getDocument()));
                row.addView(tvDocument);

                TextView tvUser = new TextView(context);
                tvUser.setText(user.getUser());
                row.addView(tvUser);

                TextView tvNames = new TextView(context);
                tvNames.setText(user.getNames());
                row.addView(tvNames);

                TextView tvLastNames = new TextView(context);
                tvLastNames.setText(user.getLastNames());
                row.addView(tvLastNames);

                TextView tvPassword = new TextView(context);
                tvPassword.setText(user.getPassword());
                row.addView(tvPassword);

                // Agregar la fila a la tabla
                tableLayout.addView(row);
            }
        }
    }

    //7. Método para eliminar un usuario
    private void deleteUser(View view) {
        EditText etDocumento = findViewById(R.id.etDocumento);
        if (etDocumento.getText().toString().isEmpty()) {
            Snackbar.make(view, "Ingrese el documento del usuario a eliminar", Snackbar.LENGTH_LONG).show();
            return;
        }
        int documento = Integer.parseInt(etDocumento.getText().toString());
        UserDao dao = new UserDao(context, view);
        dao.deleteUser(documento);
        // Actualizar la lista después de eliminar un usuario
        listUserShow(view);
    }

    //8. Método para actualizar un usuario
    private void updateUser(View view) {
        getData();
        User user = new User(documento, nombres, apellidos, usuario, contra);
        UserDao dao = new UserDao(context, view);
        dao.updateUser(user);
        listUserShow(view);  // Actualizar la lista después de actualizar un usuario
    }

    //9. Método para buscar un usuario por número de documento
    public void searchUser(View view) {
        EditText etSearch = findViewById(R.id.etSearch);
        String documentStr = etSearch.getText().toString();


        if (documentStr.isEmpty()) {
            Snackbar.make(view, "Ingrese un número de documento", Snackbar.LENGTH_LONG).show();
            return;
        }

        int document = Integer.parseInt(documentStr);
        UserDao dao = new UserDao(context, view);
        User user = dao.searchUser(document);

        TableLayout tableLayout = findViewById(R.id.tableLayout);
        tableLayout.removeViews(1, tableLayout.getChildCount() - 1);

        if (user != null) {
            // Mostrar información del usuario si se encuentra
            TableRow row = new TableRow(context);

            TextView tvDocument = new TextView(context);
            tvDocument.setText(String.valueOf(user.getDocument()));
            row.addView(tvDocument);

            TextView tvUser = new TextView(context);
            tvUser.setText(user.getUser());
            row.addView(tvUser);

            TextView tvNames = new TextView(context);
            tvNames.setText(user.getNames());
            row.addView(tvNames);

            TextView tvLastNames = new TextView(context);
            tvLastNames.setText(user.getLastNames());
            row.addView(tvLastNames);

            TextView tvPassword = new TextView(context);
            tvPassword.setText(user.getPassword());
            row.addView(tvPassword);
            // Agregar información del usuario a la tabla
            tableLayout.addView(row);
            loadUserData(user); // Cargar los datos del usuario encontrado en los campos de texto
            btnUpdate.setEnabled(true); // Habilitar el botón de actualizar
            btnEliminar.setEnabled(true);// Habilitar el botón de eliminar
        } else {
            Snackbar.make(view, "Usuario no encontrado", Snackbar.LENGTH_LONG).show();
        }
    }

    //10. Método para limpiar los campos de texto
    private void clearEditText() {
        try {
            etNombres.setText("");
            etApellidos.setText("");
            etDocumento.setText("");
            etUsuario.setText("");
            etContra.setText("");
            etSearch.setText("");// Limpiar también el campo de búsqueda
        } catch (Exception e) {
            Log.e("ClearEditText", "Error al limpiar campos de texto", e);
        }
    }
    //11. Método para cargar los datos del usuario encontrado en los campos de texto
    private void loadUserData(User user) {
        etDocumento.setText(String.valueOf(user.getDocument()));
        etUsuario.setText(user.getUser());
        etNombres.setText(user.getNames());
        etApellidos.setText(user.getLastNames());
        etContra.setText(user.getPassword());
    }

}