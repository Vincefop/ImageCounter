package test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;


public class ImageTesterUnPointNoir {


    public static void main(String args[]) throws Exception {
    	// Je r�cup�re le fichier
        File file = new File("C:/dev/WorkspaceFormationJava/OrganismCounter/src/test/unPointNoir.jpg");
        // Je la mets dans une stream
        ImageInputStream is = ImageIO.createImageInputStream(file);
        // Je cr�e un pointeur sur cette stream
        Iterator<?> iter = ImageIO.getImageReaders(is);

        // Si �a ne pointe pas sur un fichier je sors du programme
        if (!iter.hasNext())
        {
            System.out.println("Cannot load the specified file "+ file);
            System.exit(1);
        }
        // Je mets ce pointeur sur une imageReader
        ImageReader imageReader = (ImageReader)iter.next();
        // Je r�cup�re les Inputs de la stream
        imageReader.setInput(is);

        
        // Je cr�e mon image bufferis�e avec ce pointeur sur le d�but de l'image
        BufferedImage image = imageReader.read(0);

        // Je r�cup�re la taille de l'image
        int height = image.getHeight();
        int width = image.getWidth();

        // Je cr�e une map qui va contenir 
        // cl� = la couleur, valeur = le nombre de pixels contenant cette couleur
        Map<Integer, Integer> m = new HashMap<>();
        
        // Je parcours toute l'image bufferis�e
        // les lignes
        for(int i=0; i < width ; i++)
        {
        	// les colonnes 
            for(int j=0; j < height ; j++)
            {
            	// Je r�cup�re la couleur � la position i, j
                int rgb = image.getRGB(i, j);
                // Une table de couleurs rouge, verte, bleue
                int[] rgbArr = getRGBArr(rgb);                
                // Filter out grays...
                // Si la couleur n'est pas le gris
                if (!isGray(rgbArr)) {                
                	// On r�cup�re le compteur de pixels de la couleur rgb
                    Integer counter = (Integer) m.get(rgb);
                    //Si on rencontre la premi�re, il n'y a pas de compteur, on initialise le compteur � 0
                    if (counter == null) {
                        counter = 0;
                        }
                    //Puis on incr�mente le compteur
                    counter++;                      
                    //Puis on ajoute une cl� / valeur � la map : la cl�=la couleur rgb, la valeur=le compteur de pixel de cette couleur
                    m.put(rgb, counter);                
                }                
            }
        }   
        //On r�cup�re depuis la map une liste qu'on trie de la couleur la moins courante � la plus courante, 
        // on r�cup�re cette couleur et on l'affiche en hexad�cimal
        String colourHex = getMostCommonColour(m);
        System.out.println(colourHex);
    }


    // On r�cup�re depuis la map une liste qu'on trie de la couleur la moins courante � la plus courante, 
    // on r�cup�re cette couleur et on l'affiche en hexad�cimal
    public static String getMostCommonColour(Map<Integer, Integer> map) {
    	//
        List<?> list = new LinkedList(map.entrySet());
        Collections.sort(list, new Comparator() {
              public int compare(Object o1, Object o2) {
                return ((Comparable) ((Map.Entry) (o1)).getValue())
                  .compareTo(((Map.Entry) (o2)).getValue());
              }
        });    
        Map.Entry me = (Map.Entry )list.get(list.size()-1);
        int[] rgb= getRGBArr((Integer)me.getKey());
        System.out.println(rgb[0]+" "+rgb[1]+" "+rgb[2]);
        //Conversion en hexa decimal
        return Integer.toHexString(rgb[0])+" "+Integer.toHexString(rgb[1])+" "+Integer.toHexString(rgb[2]);        
    }    

    public static int[] getRGBArr(int pixel) {
        int alpha = (pixel >> 24) & 0xff;
        int red = (pixel >> 16) & 0xff;
        int green = (pixel >> 8) & 0xff;
        int blue = (pixel) & 0xff;
        return new int[]{red,green,blue};

  }
    public static boolean isGray(int[] rgbArr) {
        int rgDiff = rgbArr[0] - rgbArr[1];
        int rbDiff = rgbArr[0] - rgbArr[2];
        // Filter out black, white and grays...... (tolerance within 10 pixels)
        int tolerance = 10;
        if (rgDiff > tolerance || rgDiff < -tolerance) 
            if (rbDiff > tolerance || rbDiff < -tolerance) { 
                return false;
            }                 
        return true;
    }
}
