package co.edu.actividad4.model;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import co.edu.actividad4.entity.User;

//1. Esta clase se encarga de interactuar con la base de datos para realizar operaciones CRUD en la tabla de usuarios.
public class UserDao {

    private  ManagerDataBase managerDataBase; //2. Instancia de la clase ManagerDataBase para acceder a la base de datos.
    Context context;
    View view;
    private User user; // Instancia de la clase User para manejar los datos del usuario.

    // 3. Constructor de la clase UserDao.
    public UserDao(Context context, View view) {
        this.context = context;
        this.view = view;
        managerDataBase= new ManagerDataBase(this.context);

    }

    //4. Método para insertar un nuevo usuario en la base de datos.
    public void insertUser(User user){
        try{//para crear excepciones
            SQLiteDatabase db = managerDataBase.getWritableDatabase();
            if (db !=null ){
                ContentValues values = new ContentValues();//instanciamos el objeto
                values.put("use_document", user.getDocument());
                values.put("use_user",user.getUser());
                values.put("use_names",user.getNames());
                values.put("use_lastNames",user.getLastNames());
                values.put("use_password",user.getPassword());
                values.put("use_status","1");
                //para  ejecutar
                long cod = db.insert("users", null, values);// Inserta el usuario en la tabla.
                Snackbar.make(this.view, "Se ha Registrado el Usurio"+ cod,Snackbar.LENGTH_LONG).show();

            }else {

                Snackbar.make(this.view, "NO Se ha Registrado el Usurio", Snackbar.LENGTH_LONG).show();// retiro el metodo COD

            }
        }catch(SQLException e){
            Log.i("DB",""+e);
        }
    }

    //5. Método para obtener una lista de todos los usuarios activos.
    public ArrayList<User> getUserList(){

        ArrayList<User> listUsers = new ArrayList<>();

        try {
            SQLiteDatabase db =managerDataBase.getReadableDatabase();
            String query ="select *  from users where use_status = 1;";
            //ejecutamos el query a un cursor, para recorrer todos los tados
            Cursor cursor  = db.rawQuery(query, null);

            if(cursor.moveToFirst()){
                do{
                    User user1= new User();
                    user1.setDocument(cursor.getInt(0));
                    user1.setUser(cursor.getString(1));
                    user1.setNames(cursor.getString(2));
                    user1.setLastNames(cursor.getString(3));
                    user1.setPassword(cursor.getString(4));
                    listUsers.add(user1);
                }while (cursor.moveToNext());
            }
            cursor.close();
            db.close();

        }catch (SQLException e) {
            Log.i("DB", "" +e);

        }
        return listUsers;
    }
    //6. Método para marcar un usuario como eliminado cambiando su estado en la base de datos.
    public void deleteUser(int document) {
        SQLiteDatabase db = managerDataBase.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put("use_status", "0"); // Cambia el valor del campo use_status a 0 en lugar de eliminar el usuario

            // Actualiza el usuario donde el documento coincide.
            String selection = "use_document = ?";
            String[] selectionArgs = {String.valueOf(document)};
            int count = db.update("users", values, selection, selectionArgs);

            if (count > 0) {
                Snackbar.make(view, "Usuario marcado como eliminado", Snackbar.LENGTH_LONG).show();
            } else {
                Snackbar.make(view, "No se encontró ningún usuario con ese documento", Snackbar.LENGTH_LONG).show();
            }
        } catch (SQLException e) {
            Log.e("DB", "Error al marcar el usuario como eliminado", e);
            Snackbar.make(view, "Error al marcar el usuario como eliminado", Snackbar.LENGTH_LONG).show();
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }



    //7. Método para actualizar los datos de un usuario en la base de datos.
    public void updateUser(User user) {
        SQLiteDatabase db = managerDataBase.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("use_user", user.getUser());
        values.put("use_names", user.getNames());
        values.put("use_lastNames", user.getLastNames());
        values.put("use_password", user.getPassword());
        // Se asume que el estado no cambia o no necesita ser actualizado.
        // Actualiza el usuario donde el documento coincide.
        String selection = "use_document = ?";
        String[] selectionArgs = { String.valueOf(user.getDocument()) };

        try {
            int count = db.update("users", values, selection, selectionArgs);
            Snackbar.make(view, "Usuario actualizado con éxito, número de filas afectadas: " + count, Snackbar.LENGTH_LONG).show();
        } catch (SQLException e) {
            Log.i("DB", "Error al actualizar usuario: " + e);
            Snackbar.make(view, "Error al actualizar usuario", Snackbar.LENGTH_LONG).show();
        } finally {
            db.close();
        }
    }

    //8. Método para buscar un usuario por su número de documento.
    public User searchUser(int document) {
        SQLiteDatabase db = managerDataBase.getReadableDatabase();
        User user = null;

        try {
            String query = "SELECT * FROM users WHERE use_status=1 AND use_document = ?";
            Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(document)});

            if (cursor.moveToFirst()) {
                user = new User();
                user.setDocument(cursor.getInt(0));
                user.setUser(cursor.getString(1));
                user.setNames(cursor.getString(2));
                user.setLastNames(cursor.getString(3));
                user.setPassword(cursor.getString(4));
            }

            cursor.close();
        } catch (SQLException e) {
            Log.e("DB", "Error al buscar usuario: " + e);
        } finally {
            if (db != null) {
                db.close();
            }
        }

        return user;
    }

}
