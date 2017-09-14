package apping.trashsoundboard;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class XMLParser {

    ArrayList<String> nome = new ArrayList<String>(50);
    ArrayList<String> prefnome = new ArrayList<String>(50);
    ArrayList<String> prefautore = new ArrayList<String>(50);
    List<String> tuttinomi = new ArrayList<String>(50);
    List<String> nomiselezionati = new ArrayList<String>(50);
    ArrayList<String> autore = new ArrayList<String>(50);
    ArrayList<String> prefsuono = new ArrayList<String>(50);
    ArrayList<String> fileimg = new ArrayList<String>(50);
    ArrayList<String> filesuono = new ArrayList<String>(50);
    ArrayList<String> sfondi = new ArrayList<String>(50);
    int sfondo;

    CreateXML createXML;

    Context context;
    String categ;
    Document doc;
    DocumentBuilder manipoloDati;
    DocumentBuilderFactory strumentiDati;

    // Costruttore
    public XMLParser(Context context){
        try {
            this.context = context;

            strumentiDati = DocumentBuilderFactory.newInstance();
            manipoloDati = strumentiDati.newDocumentBuilder();
            File root = new File(Environment.getExternalStorageDirectory(), "TrashSoundBoard/tb.xml");
            doc = manipoloDati.parse(root);

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public ArrayList<String> selectAllAutore() {
        try {
            doc.getDocumentElement().normalize();
            NodeList nodi = doc.getElementsByTagName("item");

            for (int i = 0; i < nodi.getLength(); i++) {
                Node nodoItem = nodi.item(i);
                if (nodoItem.getNodeType() == Node.ELEMENT_NODE) {
                    Element elemento = (Element) nodoItem;
                    if (elemento.getElementsByTagName("tipo").item(0).getTextContent().equals("autore")){
                        autore.add(elemento.getElementsByTagName("autore").item(0).getTextContent());
                    }

                }
            }
        }
        catch(Exception e){
            System.err.println(e.toString());
            e.printStackTrace();
        }

        Log.d("Element autore tot: ", String.valueOf(autore));
        return autore;
    }



    public ArrayList<String> selectAllNome(String autore) {
        try {
            doc.getDocumentElement().normalize();
            NodeList nodi = doc.getElementsByTagName("item");

            for (int i = 0; i < nodi.getLength(); i++) {
                Node nodoItem = nodi.item(i);
                if (nodoItem.getNodeType() == Node.ELEMENT_NODE) {
                    Element elemento = (Element) nodoItem;
                    if (elemento.getElementsByTagName("autore").item(0).getTextContent().equals(autore) && elemento.getElementsByTagName("tipo").item(0).getTextContent().equals("suono")) {
                        nome.add(elemento.getElementsByTagName("nome").item(0).getTextContent());

                    }


                }
            }
        }
        catch(Exception e){
            System.err.println(e.toString());
            e.printStackTrace();
        }

        Log.d("Element nome tot: ", String.valueOf(nome));
        return nome;
    }

    public ArrayList<String> selectAllPrefAutori() {
        try {
            doc.getDocumentElement().normalize();
            NodeList nodi = doc.getElementsByTagName("item");
            ManageFavorite manageFavorite = new ManageFavorite(context);
            for (int i = 0; i < nodi.getLength(); i++) {
                Node nodoItem = nodi.item(i);
                if (nodoItem.getNodeType() == Node.ELEMENT_NODE) {
                    Element elemento = (Element) nodoItem;
                    if (elemento.getElementsByTagName("tipo").item(0).getTextContent().equals("suono") && manageFavorite.isFavorite(elemento.getElementsByTagName("filesuono").item(0).getTextContent())) {
                        Log.d("XMLPref element nome", elemento.getElementsByTagName("nome").item(0).getTextContent());

                        prefautore.add(elemento.getElementsByTagName("autore").item(0).getTextContent());

                    }


                }
            }
        }
        catch(Exception e){
            System.err.println(e.toString());
            e.printStackTrace();
        }

        prefautore = new ArrayList<String>(new LinkedHashSet<String>(prefautore));


        Log.d("XMLPref", String.valueOf(prefautore));
        return prefautore;
    }


    public ArrayList<String> selectAllPrefNome(String autore) {
        try {
            doc.getDocumentElement().normalize();
            NodeList nodi = doc.getElementsByTagName("item");
            ManageFavorite manageFavorite = new ManageFavorite(context);
            for (int i = 0; i < nodi.getLength(); i++) {
                Node nodoItem = nodi.item(i);
                if (nodoItem.getNodeType() == Node.ELEMENT_NODE) {
                    Element elemento = (Element) nodoItem;
                    if (elemento.getElementsByTagName("autore").item(0).getTextContent().equals(autore) && elemento.getElementsByTagName("tipo").item(0).getTextContent().equals("suono") && manageFavorite.isFavorite(elemento.getElementsByTagName("filesuono").item(0).getTextContent())) {
                        Log.d("XMLPref element nome", elemento.getElementsByTagName("nome").item(0).getTextContent());

                        prefnome.add(elemento.getElementsByTagName("nome").item(0).getTextContent());

                    }


                }
            }
        }
        catch(Exception e){
            System.err.println(e.toString());
            e.printStackTrace();
        }

        Log.d("XMLPref", String.valueOf(prefnome));
        return prefnome;
    }

    public ArrayList<String> selectAllPrefSuoniFrom(String autore) {
        try {
            doc.getDocumentElement().normalize();
            NodeList nodi = doc.getElementsByTagName("item");
            ManageFavorite manageFavorite = new ManageFavorite(context);
            for (int i = 0; i < nodi.getLength(); i++) {
                Node nodoItem = nodi.item(i);
                if (nodoItem.getNodeType() == Node.ELEMENT_NODE) {
                    Element elemento = (Element) nodoItem;
                    if (elemento.getElementsByTagName("autore").item(0).getTextContent().contains(autore) && manageFavorite.isFavorite(elemento.getElementsByTagName("filesuono").item(0).getTextContent())){
                        Log.d("XMLPref element suono", elemento.getElementsByTagName("nome").item(0).getTextContent());

                        prefsuono.add(elemento.getElementsByTagName("filesuono").item(0).getTextContent());
                    }

                }
            }
        }
        catch(Exception e){
            System.err.println(e.toString());
            e.printStackTrace();
        }

        Log.d("XMLPref", String.valueOf(prefsuono));
        return prefsuono;
    }

    public ArrayList<String> selectAllFilesuono(String autore) {
        try {
            doc.getDocumentElement().normalize();
            NodeList nodi = doc.getElementsByTagName("item");

            for (int i = 0; i < nodi.getLength(); i++) {
                Node nodoItem = nodi.item(i);
                if (nodoItem.getNodeType() == Node.ELEMENT_NODE) {
                    Element elemento = (Element) nodoItem;
                    if (elemento.getElementsByTagName("autore").item(0).getTextContent().equals(autore)  && elemento.getElementsByTagName("tipo").item(0).getTextContent().equals("suono")) {
                        filesuono.add(elemento.getElementsByTagName("filesuono").item(0).getTextContent());

                    }


                }
            }
        }
        catch(Exception e){
            System.err.println(e.toString());
            e.printStackTrace();
        }

        Log.d("Element filesuono tot: ", String.valueOf(filesuono));
        return filesuono;
    }

    public ArrayList<String> selectAllSfondi() {
        try {
            doc.getDocumentElement().normalize();
            NodeList nodi = doc.getElementsByTagName("item");

            for (int i = 0; i < nodi.getLength(); i++) {
                Node nodoItem = nodi.item(i);
                if (nodoItem.getNodeType() == Node.ELEMENT_NODE) {
                    Element elemento = (Element) nodoItem;
                    if (elemento.getElementsByTagName("tipo").item(0).getTextContent().equals("autore")){
                        sfondi.add(elemento.getElementsByTagName("sfondo").item(0).getTextContent());
                    }

                }
            }
        }
        catch(Exception e){
            System.err.println(e.toString());
            e.printStackTrace();
        }

        Log.d("Element sfondi tot: ", String.valueOf(autore));
        return sfondi;
    }

    public int selectSfondo(String autore) {
        try {
            doc.getDocumentElement().normalize();
            NodeList nodi = doc.getElementsByTagName("item");

            for (int i = 0; i < nodi.getLength(); i++) {
                Node nodoItem = nodi.item(i);
                if (nodoItem.getNodeType() == Node.ELEMENT_NODE) {
                    Element elemento = (Element) nodoItem;
                    if (elemento.getElementsByTagName("autore").item(0).getTextContent().equals(autore)  && elemento.getElementsByTagName("tipo").item(0).getTextContent().equals("autore")
                            ) {
                        sfondo = context.getResources().getIdentifier(elemento.getElementsByTagName("sfondo").item(0).getTextContent(), "drawable", context.getPackageName());

                    }

                }
            }
        }
        catch(Exception e){
            System.err.println(e.toString());
            e.printStackTrace();
        }

        Log.d("Element sfondo: ", String.valueOf(sfondo));
        return sfondo;
    }

    public void reset(){
        prefnome = new ArrayList<String>(50);
        prefsuono = new ArrayList<String>(50);
    }

}
