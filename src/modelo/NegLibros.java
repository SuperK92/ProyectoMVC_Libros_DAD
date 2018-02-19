/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import conexion.ClaseDatos;
import datos.Libro;
import java.sql.ResultSet;

/**
 *
 * @author jj
 */
public class NegLibros {

    private final ClaseDatos _cldatos;
    private ResultSet _rs;
    private String _buscar;
    public static final int COLUMN_LIBRO_CODIGO = 0;
    public static final int COLUMN_LIBRO_TITULO = 1;
    public static final int COLUMN_LIBRO_AUTOR = 2;
    public static final int COLUMN_LIBRO_EDITORIAL = 3;
    public static final int COLUMN_LIBRO_ASIGNATURA = 4;
    public static final int COLUMN_LIBRO_ESTADO = 5;

   
    public NegLibros(ClaseDatos cldatos) {
        _cldatos = cldatos;
        _rs = null;
        _buscar = "";
    }

    public int NumeroRegistros() throws Exception {
        int fila = -1;
        if (_rs.last()) {
            fila = _rs.getRow();
        }
        return fila;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////
    public void actualizar() throws Exception {
       String sql; 
        if (_buscar.isEmpty()) {

            sql = "SELECT * FROM libros";

        } else {
            ////////////////////////////////////////////////////////
            //sql = "SELECT * FROM libros"; //Consulta a modificar
            //Modifica el query para realizar búsquedas de libros
            ///////////////////////////////////////////////////////
            
            sql = "select * from libros where "
                    + "Titulo like '%" + _buscar + "%'"
                    + "or Autor like '%" + _buscar + "%'"
                    + "or Editorial like '%" + _buscar + "%'"
                    + "or Asignatura like '%" + _buscar + "%'"
                    + "or estado like '%" + _buscar + "%'";
        }
        _cldatos.Ejecutar_Consulta(sql);
        _rs = _cldatos.getRs();

    }
    
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    public int Modificar(Libro libro) throws Exception {
        
      int id = _cldatos.Ejecutar_Actualizacion("UPDATE libros SET "
                + "Titulo = '" + libro.getTitulo()+ "', "
                + "Autor = '" + libro.getAutor()+ "', " 
                + "Editorial = '" + libro.getEditorial()+ "', " 
                + "Asignatura = '" + libro.getAsignatura()+ "', "
                + "estado = '" + libro.getEstado()+ "' "
                + "WHERE codigo = " + libro.getCodigo()+ ";"
        );
      
        
        //alumno.setRegistro(id);
        //if (id > 0) {
            actualizar();
        
        //}
        return id;
    }
//////////////////////////////////////////////////////////////////////////////////////////////////
    public int altas(Libro libro) throws Exception {
    int id = _cldatos.Ejecutar_Insercion("INSERT INTO libros VALUES( "
                + "null, "
                + "'" + libro.getTitulo() + "', "
                + "'" + libro.getAutor() + "', "
                + "'" + libro.getEditorial() + "', "
                + "'" + libro.getAsignatura() + "', "
                + "'" + libro.getEstado() + "' )");
        libro.setCodigo(id);
        if (id > 0) {
            actualizar();
        }
        return id;

    }
///////////////////////////////////////////////////////////////////////////////////////////////////////////
    public int borrar(Libro libro) throws Exception {
       //Añadir el código necesario para poder eliminar un registro
       int fila = _cldatos.Ejecutar_Actualizacion("DELETE FROM libros "
                + "WHERE codigo = " + libro.getCodigo());
        if (fila > 0) {
            actualizar();
        }
        return fila;
     
    } 
    
 //////////////////////////////////////////////////////////////////////////////////////////////////   

 //los metadatos empiezan en base 1 por eso +1
    public Libro getLibro(int fila) throws Exception {

        Libro libro = null;
        if (_rs.absolute(fila)) {

            libro = new Libro();
// +1 a cada columna
            int codigo = _rs.getInt(COLUMN_LIBRO_CODIGO + 1);
            String titulo = _rs.getString(COLUMN_LIBRO_TITULO + 1);
            String autor = _rs.getString(COLUMN_LIBRO_AUTOR + 1);
            String editorial = _rs.getString(COLUMN_LIBRO_EDITORIAL + 1);
            String asignatura = _rs.getString(COLUMN_LIBRO_ASIGNATURA + 1);
            String estado = _rs.getString(COLUMN_LIBRO_ESTADO + 1);

            libro.setCodigo(codigo);
            libro.setTitulo(titulo);
            libro.setAutor(autor);
            libro.setEditorial(editorial);
            libro.setAsignatura(asignatura);
            libro.setEstado(estado);

        }
        return libro;
    }
    
    public String getBuscar() {
        return _buscar;
    }

    public void setBuscar(String buscar) throws Exception {
        if (!_buscar.equals(buscar)) {
            _buscar = buscar;
            actualizar();
        }
    }

  
}
