/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import conexion.ClaseDatos;
import datos.Libro;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import modelo.NegLibros;
import vista.DlgLibros;


/**
 *
 * @author jjrodmar
 */

public class CtrlLibro implements ActionListener {

   private static final int TIEMPOBUSCAR = 300;
    private Timer timerbuscar; //Se establece un tiempo para realizar buscar datos en la tabla libros
    private CtrlTablaLibro rsmodel;
    private ClaseDatos _cldatos = null; //Clase de conexión a la base de datos
    private NegLibros neglibros; //La clase del Modelo
    private Libro libro; //Clase define la estructura de los campos que necesitamos del libro
    private Libro currentLibro; //Define al libro actual;
    private boolean _activoSeleccionar;
    //private boolean _hayseleccion;
    private DlgLibros dlglibros = null; //Indica una variable de la vista del formulario de libros

   
    
    //Constructor de la clase
   public CtrlLibro(java.awt.Frame parent, ClaseDatos cldatos, boolean activoSeleccionar) {

        IniciarFormulario(parent);
        IniciarEventos();
        InicializarFormulario(cldatos, activoSeleccionar);
        IncializarTabla();
        
        this.dlglibros.setVisible(true);

    }
    

   
    private void IniciarFormulario(java.awt.Frame parent) {
      this.dlglibros = new DlgLibros(parent);
        this.dlglibros.setLocationRelativeTo(null);
        this.dlglibros.setResizable(false);
        this.dlglibros.setTitle("Libros");

    }

   
    private void InicializarFormulario(ClaseDatos cldatos, boolean activoSeleccionar) {
        this._cldatos = cldatos;
        this._activoSeleccionar = activoSeleccionar;
        this.dlglibros.getBtnSeleccionar().setEnabled(_activoSeleccionar);
        this.currentLibro = null;

    }

   
    private void IniciarEventos() {
       // Los eventos de documento ocurren cuando el contenido de un documento cambia de alguna manera
        // en este caso  para habilitar los campos o no
         //Implemento el listener de comuent para TxtCodigo, TxtTitulo, TxtAutor, TxtAsignatura,TxtEditorial,TxtAutor 
       // this.dlglibros.getTxtCodigo().getDocument().addDocumentListener(HabilitarBotonAltas());
        this.dlglibros.getTxtTitulo().getDocument().addDocumentListener(HabilitarBotonAltas());
        this.dlglibros.getTxtAutor().getDocument().addDocumentListener(HabilitarBotonAltas());
        this.dlglibros.getTxtAsignatura().getDocument().addDocumentListener(HabilitarBotonAltas());
        this.dlglibros.getTxtEditorial().getDocument().addDocumentListener(HabilitarBotonAltas());
        this.dlglibros.getTxtAutor().getDocument().addDocumentListener(HabilitarBotonAltas());

        
        //Implemento el listener de document para txtbuscar. Activa la busqueda de datos
        this.dlglibros.getTxtBuscar().getDocument().addDocumentListener(ActivarBusqueda()); 
        
        //Indico la ActionListener que deseo eschucar los eventos de los diferentes botones
        this.dlglibros.getBtnSalir().addActionListener(this);
        this.dlglibros.getBtnAltas().addActionListener(this);
        this.dlglibros.getBtnBajas().addActionListener(this);
        this.dlglibros.getBtnModificaciones().addActionListener(this);  
        this.dlglibros.getBtnSeleccionar().addActionListener(this);

        //Agrega un oyente a la lista que se notifica cada vez que se produce un cambio en el modelo de datos.
        this.dlglibros.getTablaLibros().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) { //Cambios que se produce en el Jtable
                selectionChanged();
            }
        });

    }

      
    private void IncializarTabla() {
     try {
            libro = new Libro();
            neglibros = new NegLibros(_cldatos); //Llamo al constructor de la clase neglibros que contiene el modelo de datos de libro
            neglibros.actualizar();//hace la consulta y vuelca resulset a negocio de todo el contenido de la tabla libros

            rsmodel = new CtrlTablaLibro(neglibros); //Asigna la clase CtrlTablaLibro a la variable rsmodel del mismo tipo
            this.dlglibros.getTablaLibros().setModel(rsmodel); //Cargo en el Jtable TablaLibros el contenido de select realizado en neglibros.actualizar

        } catch (Exception ex) {
            VentanaMensajeError(ex.toString());
        }

    }

   
    //El procedimiento ocurre cuando selecciono un registro del Jtable
    private void selectionChanged() { 

        int fila = this.dlglibros.getTablaLibros().getSelectedRow();
        //System.out.println( fila );
        try {
            currentLibro = neglibros.getLibro(fila + 1);  //Es en base 1
        } catch (Exception e) {
            currentLibro = null;
        }
        MostrarDatos();

        this.dlglibros.getBtnBajas().setEnabled(fila != -1);
        this.dlglibros.getBtnModificaciones().setEnabled(fila != -1); 

    }

  
    //Muestra en los JtextField los datos que hemos seleccionado en el Jtable.
    private void MostrarDatos() {
      
        this.dlglibros.getTxtCodigo().setText(currentLibro != null ? Integer.toString(currentLibro.getCodigo()) : "");
        this.dlglibros.getTxtTitulo().setText(currentLibro != null ? currentLibro.getTitulo() : "");
        this.dlglibros.getTxtAutor().setText(currentLibro != null ? currentLibro.getAutor() : "");
        this.dlglibros.getTxtAsignatura().setText(currentLibro != null ? currentLibro.getAsignatura() : "");
        this.dlglibros.getTxtEditorial().setText(currentLibro != null ? currentLibro.getEditorial() : "");
        this.dlglibros.getTxtEstado().setText(currentLibro != null ? currentLibro.getEstado() : "");

    }

  
    //Es función controla si el botón btnAltas esta activo o no  si los jtextfiel estan vacios o no.
//https://docs.oracle.com/javase/tutorial/uiswing/events/documentlistener.html
    private DocumentListener HabilitarBotonAltas() { 

        DocumentListener documento;

        documento = new DocumentListener() {
            public void HayCambio() {
                
             dlglibros.getBtnAltas().setEnabled(
                        !dlglibros.getTxtTitulo().getText().isEmpty() 
                        && !dlglibros.getTxtAutor().getText().isEmpty()
                        && !dlglibros.getTxtEditorial().getText().isEmpty()
                        && !dlglibros.getTxtAsignatura().getText().isEmpty());
            }

            @Override
            public void insertUpdate(DocumentEvent e) { //Se invoca cuando  se inserta en el documento escuchado
                HayCambio();
            }

            @Override
            public void removeUpdate(DocumentEvent e) { //Se invoca cuando se elimina texto del documento escuchado
                HayCambio();
            }

            @Override
            public void changedUpdate(DocumentEvent e) { //Se invoca cuando cambia el documento que escucha
                HayCambio();
            }
        };
        return documento;


    }

//Cuando existe un cambio txt_buscar realiza la busqueda en la tabla libros
//https://docs.oracle.com/javase/tutorial/uiswing/events/documentlistener.html
    private DocumentListener ActivarBusqueda() { 
       DocumentListener documento;

        documento = new DocumentListener() {

            private void ActivoTimer() {

                if ((timerbuscar != null) && timerbuscar.isRunning()) {
                    timerbuscar.restart();
                } else {
                    timerbuscar = new Timer(TIEMPOBUSCAR, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent evt) {
                            timerbuscar = null;
                            BuscarAhora();
                        }
                    });
                    timerbuscar.setRepeats(false); //no queremos que sea repetitivo
                    timerbuscar.start();// el timer comienza a contar
                }
            }

            @Override
            public void insertUpdate(DocumentEvent e) {//Se invoca cuando  se inserta en el documento escuchado
                ActivoTimer();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {//Se invoca cuando se elimina texto del documento escuchado
                ActivoTimer();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {//Se invoca cuando cambia el documento que escucha
                ActivoTimer();
            }
        };

        return documento;

  
    }

    //Procedimiento que busca en la tabla algun valor introducido en el jtexfield txt_buscar
    private void BuscarAhora() {
      try {
            neglibros.setBuscar(dlglibros.getTxtBuscar().getText());
            //Notifica a todos los oyentes que todos los valores de celda en las filas de la tabla pueden haber cambiado.
            rsmodel.fireTableDataChanged();  

        } catch (Exception e) {
            VentanaMensajeError(e.toString());
        }

    }

  
    public Libro getLibro() {
        return currentLibro;
 
    }

    private void Cerrar() {
      dlglibros.setVisible(false);
        
    }



    private void VentanaMensajeError(String ex) {
        JOptionPane.showMessageDialog(null, ex, "Excepción", JOptionPane.ERROR_MESSAGE);

    }
 
    //Control de los ActionListener que estamos controlando (btnAltas, btnSalir, btnBajas, btnModificaciones)

    @Override
    public void actionPerformed(ActionEvent e) {

       if (e.getSource() == dlglibros.getBtnAltas()) {
            try {
                                
                libro.setTitulo(dlglibros.getTxtTitulo().getText());
                libro.setAutor(dlglibros.getTxtAutor().getText());
                libro.setAsignatura(dlglibros.getTxtAsignatura().getText());
                libro.setEditorial(dlglibros.getTxtEditorial().getText());
                libro.setEstado(dlglibros.getTxtEstado().getText());
 

                int id = neglibros.altas(libro);
                dlglibros.getTxtCodigo().setText(String.valueOf(id));
                rsmodel.fireTableDataChanged();

            } catch (Exception ex) {
                VentanaMensajeError(ex.toString());
            }

        } else if (e.getSource() == dlglibros.getBtnSalir()) {
            Cerrar();
        } else if (e.getSource() == dlglibros.getBtnBajas()) {
            try {
                if (JOptionPane.showConfirmDialog(dlglibros,
                 "¿Está seguro que desea borrar el registro seleccionado ?",
                 "Atención",
                 JOptionPane.YES_NO_CANCEL_OPTION,
                 JOptionPane.QUESTION_MESSAGE) == JOptionPane.OK_OPTION) {
                
                 neglibros.borrar(currentLibro);
                rsmodel.fireTableDataChanged(); //Notifica a todos los oyentes que todos los valores de celda en las filas de la tabla pueden haber cambiado.
                
                } else{
                    JOptionPane.showMessageDialog(dlglibros, "La baja no se ha realizado"); 
                }

            } catch (Exception ex) {
                VentanaMensajeError(ex.toString());
            }
        } else if (e.getSource() == dlglibros.getBtnModificaciones()) { 
            try {

                currentLibro.setTitulo(dlglibros.getTxtTitulo().getText());
                currentLibro.setAutor(dlglibros.getTxtAutor().getText());
                currentLibro.setAsignatura(dlglibros.getTxtAsignatura().getText());
                currentLibro.setEditorial(dlglibros.getTxtEditorial().getText());
                currentLibro.setEstado(dlglibros.getTxtEstado().getText());
                 neglibros.Modificar(currentLibro);
                 rsmodel.fireTableDataChanged(); //Notifica a todos los oyentes que todos los valores de celda en las filas de la tabla pueden haber cambiado.
                //también puedo solo actualizar la fila en concreto
               

            } catch (Exception ex) {
                VentanaMensajeError(ex.toString());
            }
        } else if (e.getSource() == dlglibros.getBtnSalir()) {
            Cerrar();

        } else if (e.getSource() == dlglibros.getBtnSeleccionar()) {

            Cerrar();
        }

    }

}
