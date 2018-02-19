/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import datos.Libro;
import modelo.NegLibros;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author jjrodmar
 */

//Clase controladora de la carga del  JTable partiendo de la clase abstracta AbstractTableModel
//https://docs.oracle.com/javase/7/docs/api/javax/swing/table/AbstractTableModel.html
public class CtrlTablaLibro extends AbstractTableModel{
  
    private final NegLibros _neglibros;
    private int _ultimaFila=-1;
    private Libro libro;
    private static final String _columnas[] = {
        "Codigo",
        "Titulo",
        "Autor",
        "Editorial",
        "Asignatura",
        "Estado"
    };
    
    
    public CtrlTablaLibro(NegLibros neglibros) {
        _neglibros = neglibros;
    }

    // Indica el número de filas que tendrá la tabla
    @Override
    public int getRowCount() {
        try {
            return _neglibros.NumeroRegistros();
        } catch (Exception e) {
        }
        return 0;
    }

    //Indica el número de campos a mostrar en la tabla
    
    @Override
    public int getColumnCount() {
        return _columnas.length;
    }

    //Carga el valor en la fila y columna correspondiente
    //atención es +1 pq los metadatos trabajan en arry base 1 y yo espero en base 0
    //voy a comentar el que puse inicialmente y voy a poner un array para cargar los datos
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex >= 0) {
            try {
                
                if(rowIndex != _ultimaFila){
                    libro = _neglibros.getLibro(rowIndex + 1);
                    _ultimaFila=rowIndex;
                    
                }
                               
                switch (columnIndex) {

                    case 0:
                        return libro.getCodigo();
                    case 1:
                        return libro.getTitulo();
                    case 2:
                        return libro.getAutor();
                    case 3:
                        return libro.getEditorial();
                    case 4:
                        return libro.getAsignatura();
                    case 5:
                        return libro.getEstado();
                }
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
        return "";
    }

    //Indica el nombre de la columna según array _columnas
    @Override
    public String getColumnName(int columnIndex) {
        return _columnas[ columnIndex];
    }

    //Indica si las celldas del Jtable son editable o no. En este caso ningun campo es editable
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

}