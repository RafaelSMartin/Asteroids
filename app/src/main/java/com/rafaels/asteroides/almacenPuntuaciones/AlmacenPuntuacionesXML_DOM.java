package com.rafaels.asteroides.almacenPuntuaciones;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Rafael S Martin on 15/12/2017.
 */

@TargetApi(Build.VERSION_CODES.FROYO)
public class AlmacenPuntuacionesXML_DOM implements AlmacenPuntuaciones {

    private static String FICHERO = Environment.getExternalStorageDirectory()
            + "/puntuaciones_dom.xml";

    private Context contexto;
    private Document documento;
    private boolean cargadoDocumento;

    public AlmacenPuntuacionesXML_DOM(Context contexto) {
        this.contexto = contexto;
        cargadoDocumento = false;

        if (ExisteDirectorioAsteroides()) {
            Log.e("Asteroides", "EXISTEDIRECTORIO Existe carpeta Asteroides");
            Log.e("Asteroides", "EXISTEDIRECTORIO " + FICHERO);
            Log.e("Asteroides",
                    "EXISTEDIRECTORIO" + "IndexOf "
                            + FICHERO.indexOf("/Asteroides/"));
            if (FICHERO.indexOf("/Asteroides/") == -1) {
                FICHERO = Environment.getExternalStorageDirectory()
                        + "/Asteroides/puntuaciones_dom.xml";
            }
        } else {
            Log.e("EXISTEDIRECTORIO", "No Existe carpeta Asteroides");
        }
    }

    @Override
    public void guardarPuntuacion(int puntos, String nombre, long fecha) {
        try {
            if (!cargadoDocumento) {

                // leerXML(contexto.openFileInput(FICHERO));
                leerXML(new FileInputStream(FICHERO));
            }
        } catch (FileNotFoundException e) {
            crearXML();
        } catch (Exception e) {
            Log.e("Asteroides", e.getMessage(), e);
        }
        nuevo(puntos, nombre, fecha);
        try {
            // escribirXML(contexto.openFileOutput(FICHERO,
            // Context.MODE_PRIVATE));
            escribirXML(new FileOutputStream(FICHERO, true));
        } catch (Exception e) {
            Log.e("Asteroides", e.getMessage(), e);
        }
    }

    // Nuevo elemento de puntuacion
    public void nuevo(int puntos, String nombre, long fecha) {
        if (cargadoDocumento) {
            Element puntuacion = documento.createElement("puntuacion");
            puntuacion.setAttribute("fecha", String.valueOf(fecha));
            Element e_nombre = documento.createElement("nombre");
            Text texto = documento.createTextNode(nombre);
            e_nombre.appendChild(texto);
            puntuacion.appendChild(e_nombre);
            Element e_puntos = documento.createElement("puntos");
            texto = documento.createTextNode(String.valueOf(puntos));
            e_puntos.appendChild(texto);
            puntuacion.appendChild(e_puntos);
            Element raiz = documento.getDocumentElement();
            raiz.appendChild(puntuacion);
        }
    }

    public Vector<String> aVectorString() {
        Vector<String> result = new Vector<String>();
        String nombre = "", puntos = "";

        if (cargadoDocumento) {
            Element raiz = documento.getDocumentElement();
            NodeList puntuaciones = raiz.getElementsByTagName("puntuacion");
            for (int i = 0; i < puntuaciones.getLength(); i++) {
                Node puntuacion = puntuaciones.item(i);
                NodeList propiedades = puntuacion.getChildNodes();
                for (int j = 0; j < propiedades.getLength(); j++) {
                    Node propiedad = propiedades.item(j);
                    String etiqueta = propiedad.getNodeName();
                    if (etiqueta != null) {
                        if (etiqueta.equals("nombre")) {
                            nombre = propiedad.getFirstChild().getNodeValue();
                        } else if (etiqueta.equals("puntos")) {
                            puntos = propiedad.getFirstChild().getNodeValue();
                        }
                    }
                }
                result.add(nombre + " " + puntos);
            }
        }

        return result;
    }

    // Compatible solo a partir API 8 Android
    public void escribirXML(OutputStream salida) throws Exception {
        if (cargadoDocumento) {
            // volcamos el XML al fichero
            TransformerFactory fabrica = TransformerFactory.newInstance();

            // añadimos sangrado y la cabecera de XML
            Transformer transformador = fabrica.newTransformer();
            fabrica.setAttribute("indent-number", new Integer(3));
            transformador.setOutputProperty(OutputKeys.INDENT, "yes");
            transformador.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");

            // hacemos la transformacion
            StreamResult resultado = new StreamResult(salida);
            DOMSource fuente = new DOMSource(documento);
            transformador.transform(fuente, resultado);

        }
    }

    @Override
    public Vector<String> listaPuntuaciones(int cantidad) {
        try {
            if (!cargadoDocumento) {
                // leerXML(contexto.openFileInput(FICHERO));
                leerXML(new FileInputStream(FICHERO));
            }
        } catch (FileNotFoundException e) {
            crearXML();
        } catch (Exception e) {
            Log.e("Asteroides", e.getMessage(), e);
        }

        return aVectorString();
    }

    public void crearXML() {
        try {
            DocumentBuilderFactory fabrica = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder constructor = fabrica.newDocumentBuilder();
            documento = constructor.newDocument();
            Element raiz = documento.createElement("lista_puntuaciones");
            documento.appendChild(raiz);
            cargadoDocumento = true;
        } catch (Exception e) {
            Log.e("Asteroides", e.getMessage(), e);
        }
    }

    public void leerXML(InputStream entrada) throws Exception {
        DocumentBuilderFactory fabrica = DocumentBuilderFactory.newInstance();
        DocumentBuilder constructor = fabrica.newDocumentBuilder();
        documento = constructor.parse(entrada);
        cargadoDocumento = true;
    }

    private boolean ExisteDirectorioAsteroides() {
        String directorioSD = Environment.getExternalStorageDirectory()
                .toString();

        String stadoSD = Environment.getExternalStorageState();
        if (!stadoSD.equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(contexto, "No puedo leer en la memoria externa",
                    Toast.LENGTH_LONG).show();
            return false;
        }

        File f = new File(directorioSD, "Asteroides");

        if (f == null) { // No ha creado el objeto correctamente
            return false;
        }

        if (!f.exists()) { // Existe el directorio Asteroides?
            if (!f.mkdir()) { // No existe lo creamos.
                return false;
            } else {
                return true;
            }
        }

        return true;

    }

}
