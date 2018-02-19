/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package datos;

/**
 *
 * @author jj
 */
public class Libro {
    private int codigo;
    private String titulo;
    private String autor;
    private String editorial;
    private String asignatura;
    private String estado;
    
    public Libro()
    {
        codigo = -1;
    }
    
    public Libro( Libro libro )
    {
        this.codigo = libro.codigo;
        this.titulo = libro.titulo;
        this.autor = libro.autor;
        this.editorial = libro.editorial;
        this.asignatura = libro.asignatura;
        this.estado = libro.estado;
    }

    public Libro( int codigo, String titulo, String autor, String editorial, String asignatura, String estado )
    {
        this.codigo = codigo;
        this.titulo = titulo;
        this.autor = autor;
        this.editorial = editorial;
        this.asignatura = asignatura;
        this.estado = estado;
    }
    

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }


    public String getTitulo() {
        return titulo;
    }


    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

 
    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

 
    public String getEditorial() {
        return editorial;
    }


    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }


    public String getAsignatura() {
        return asignatura;
    }

 
    public void setAsignatura(String asignatura) {
        this.asignatura = asignatura;
    }


    public String getEstado() {
        return estado;
    }


    public void setEstado(String estado) {
        this.estado = estado;
    }
    
}
